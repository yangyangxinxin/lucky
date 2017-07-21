package com.luckysweetheart.service;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.common.IdWorker;
import com.luckysweetheart.dal.dao.UserDao;
import com.luckysweetheart.dal.entity.User;
import com.luckysweetheart.dto.StoreDataDTO;
import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.store.StorageGroupService;
import com.luckysweetheart.store.StoreService;
import com.luckysweetheart.utils.*;
import com.luckysweetheart.web.utils.DomainUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by yangxin on 2017/5/22.
 */
@Service
public class UserService extends ParameterizedBaseService<User, Long> {

    private final static String salt = Const.SALT;

    private final static String defaultUserImg = Const.DEFAULT_USER_IMG;

    @Resource
    private UserDao userApi;

    @Resource
    private StoreService storeService;

    @Resource
    private StorageGroupService storageGroupService;

    @Resource
    private IdWorker idWorker;

    private String getActiveCode() {
        return AesUtil.encrypt(idWorker.nextId() + "");
    }

    /**
     * 注册
     *
     * @param userDTO
     * @return
     */
    public ResultInfo<UserDTO> registerUser(final UserDTO userDTO) throws BusinessException {
        ResultInfo<UserDTO> resultInfo = new ResultInfo<>();
        try {
            if (userDTO == null) {
                throw new BusinessException("用户对象不能为空!");
            }
            if (!ValidateUtil.specialVal(userDTO.getUsername())) {
                throw new BusinessException("用户名不符合规范！");
            }
            if (!ValidateUtil.pwdVal(userDTO.getPassword())) {
                throw new BusinessException("密码不符合规范！");
            }
            if (!ValidateUtil.mobileVal(userDTO.getMobilePhone())) {
                throw new BusinessException("手机号码不符合规范！");
            }
            isTrue(ValidateUtil.email(userDTO.getEmail()), "邮箱不符合规范！");
            //查询是否已经注册
            User byMoAndMobilePhone = userApi.findByMobile(userDTO.getMobilePhone());
            if (byMoAndMobilePhone != null) {
                throw new BusinessException("该手机号已注册！请登录！");
            }

            User byEmail = userApi.findByEmail(userDTO.getEmail());

            if (byEmail != null) {
                throw new BusinessException("该邮箱已注册！请登录！");
            }

            // 加密用户密码

            String pwd = DigestUtils.sha512Hex(salt + userDTO.getPassword());

            User user = new User();
            user.setPassword(pwd);
            user.setUsername(userDTO.getUsername());
            user.setMobilePhone(userDTO.getMobilePhone());
            user.setImgPath(defaultUserImg);
            user.setDeleteStatus(Const.DELETE_STATUS_NO);
            user.setCreateTime(new Date());
            user.setEmail(userDTO.getEmail());
            user.setActiveStatus(Const.ACTIVE_STATUS_NO);
            final String activeCode = getActiveCode();
            user.setActiveCode(activeCode);

            Long userId = userApi.save(user);
            userDTO.setUserId(userId);

            String basePath = DomainUtils.getIndexUrl() + "/account/activeUser?email=" + AesUtil.encrypt(userDTO.getEmail()) + "&activeCode=" + activeCode + "&stamp=" + new Date().getTime();

            logger.info(basePath);
            EmailSender.init().to(userDTO.getEmail()).emailTemplate(EmailTemplate.REGISTER).param("email", userDTO.getEmail()).param("basePath", basePath).subject("注册_激活").send();

            return resultInfo.success(userDTO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());//回滚
        }
    }

    public ResultInfo<UserDTO> login(String mobilePhone, String password) throws BusinessException {
        ResultInfo<UserDTO> resultInfo = new ResultInfo<>();
        try {
            if (!ValidateUtil.mobileVal(mobilePhone)) {
                throw new BusinessException("用户名不符合规范");
            }
            if (!ValidateUtil.pwdVal(password)) {
                throw new BusinessException("密码不符合规范！");
            }
            password = DigestUtils.sha512Hex(salt + password);
            User user = userApi.login(mobilePhone, password);
            UserDTO userDTO = new UserDTO();
            if (user != null) {
                logger.info("登录成功");
                user.setLastLoginTime(new Date());
                update(user);
                BeanCopierUtils.copy(user, userDTO);
                return resultInfo.success(userDTO);
            }
            User u = userApi.findByMobilePhoneOrEmail(mobilePhone);
            if (u != null) {
                return resultInfo.fail("密码输入错误。");
            } else {
                return resultInfo.fail("账户不存在，请核对信息。");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 重置用户密码
     *
     * @param userId
     * @param password
     * @return
     */
    public ResultInfo<UserDTO> resetPwd(Long userId, String password) throws BusinessException {
        ResultInfo<UserDTO> resultInfo = new ResultInfo<>();
        try {
            Assert.notNull(userId, "用户id不能为空");
            Assert.isTrue(ValidateUtil.pwdVal(password), "密码不符合规范");
            User user = userApi.get(userId);
            Assert.notNull(user, "该用户不存在");
            password = DigestUtils.sha512Hex(salt + password);
            user.setUpdateTime(new Date());
            user.setPassword(password);
            // 持久态，不需update
            UserDTO dto = new UserDTO();
            BeanCopierUtils.copy(user, dto);
            return resultInfo.success(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException("重置用户密码失败。");
        }
    }

    /**
     * 根据userId查询用户信息，本来考虑加到redis中，但是网站量很少，也就没必要了。
     *
     * @param userId
     * @return
     */
    public UserDTO findById(Long userId) {
        try {
            Assert.notNull(userId, "用户id不能为空");
            User user = userApi.get(userId);
            if (user != null) {
                UserDTO userDTO = new UserDTO();
                BeanCopierUtils.copy(user, userDTO);
                return userDTO;
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 根据用户手机号 查询用户信息
     *
     * @param mobilePhone
     * @return
     */
    public UserDTO findByMobilePhoneOrEmail(String mobilePhone) {
        try {
            // Assert.isTrue(ValidateUtil.mobileVal(mobilePhone), "手机号不符合规范");
            User user = userApi.findByMobilePhoneOrEmail(mobilePhone);
            if (user != null) {
                UserDTO userDTO = new UserDTO();
                BeanCopierUtils.copy(user, userDTO);
                return userDTO;
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 修改用户名
     *
     * @param username
     * @param userId
     * @return
     * @throws BusinessException
     */
    public ResultInfo<Void> updateUserName(String username, Long userId) throws BusinessException {
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            Assert.isTrue(ValidateUtil.specialVal(username), "用户名不符合规范");
            User user = userApi.get(userId);
            if (user != null) {
                user.setUsername(username);
                return resultInfo.success();
            }
            throw new BusinessException("用户不存在");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 修改用户头像，图片处理请在前端处理。
     *
     * @param userId
     * @param bytes
     * @return
     * @throws BusinessException
     */
    public ResultInfo<Void> updateImg(Long userId, byte[] bytes) throws BusinessException {
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            Assert.notNull(userId, "用户id不能为空");
            Assert.notNull(bytes, "文件不能为空");
            User user = userApi.get(userId);
            if (user != null) {
                ResultInfo<StoreDataDTO> result = storeService.uploadFile(bytes, ".png", storageGroupService.getUserGroupName());
                if (result.isSuccess()) {
                    user.setImgPath(result.getData().getResourcePath());
                    return resultInfo.success();
                } else {
                    throw new BusinessException("上传头像出错");
                }
            }
            throw new BusinessException("该用户不存在");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }


    /**
     * 注意这里的update不含任何业务逻辑操作，请谨慎使用
     *
     * @param userDTO
     * @throws BusinessException
     */
    public void update(UserDTO userDTO) throws BusinessException {
        try {
            User user = new User();
            BeanCopierUtils.copy(userDTO, user);
            userApi.update(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException("修改用户信息出现异常");
        }
    }

    /**
     * 激活用户
     * @param email
     * @param activeCode
     * @param timestamp
     * @return
     */
    public ResultInfo<Void> activeUser(String email, String activeCode, Long timestamp) {
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            isTrue(StringUtils.isNotBlank(email), "邮箱不能为空");
            isTrue(StringUtils.isNotBlank(activeCode), "激活码不能为空");
            isTrue(timestamp != null, "连接已过期");
            Long now = new Date().getTime();

            if (now - timestamp > 30 * 60 * 1000) {
                throw new BusinessException("此连接已过期");
            }
            email = AesUtil.decrypt(email);
            User user = userApi.findByEmail(email);
            if (user != null) {
                if (user.getActiveStatus().equals(User.ACTIVE_STATUS_YES)) {
                    throw new BusinessException("此用户已经激活！");
                }
                if (!user.getActiveCode().equals(activeCode)) {
                    throw new BusinessException("激活码不正确！");
                }
                user.setActiveStatus(User.ACTIVE_STATUS_YES);
                user.setActiveDate(new Date());
                userApi.update(user);
                return resultInfo.success();
            }
            throw new BusinessException("该用户不存在！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    public ResultInfo<Void> updateMobilePhone(String mobile,Long userId){
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try{
            isTrue(ValidateUtil.mobileVal(mobile),"手机号码不符合规范");
            User findByMobile = userApi.findByMobile(mobile);
            if(findByMobile != null && !findByMobile.getUserId().equals(userId)){
                return resultInfo.fail("该手机号码已经被注册");
            }
            User user = userApi.get(userId);
            user.setMobilePhone(mobile);
            userApi.update(user);
            return resultInfo.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw e;
        }
    }

    public ResultInfo<Void> updateEmail(String email,Long userId){
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try{
            isTrue(ValidateUtil.email(email),"邮箱不符合规范");

        }catch (Exception e){

        }
        return null;
    }

}
