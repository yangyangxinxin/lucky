package com.luckysweetheart.service;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.dal.dao.UserDao;
import com.luckysweetheart.dal.entity.User;
import com.luckysweetheart.dto.StoreDataDTO;
import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.store.StoreService;
import com.luckysweetheart.utils.BeanCopierUtils;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.utils.ValidateUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by yangxin on 2017/5/22.
 */
@Service
public class UserService extends ParameterizedBaseService<User,Long>  {

    private final String salt = Const.SALT;

    private final String defaultUserImg = Const.DEFAULT_USER_IMG;

    @Resource
    private UserDao userApi;

    @Resource
    private StoreService storeService;

    /**
     * 注册
     *
     * @param userDTO
     * @return
     */
    public ResultInfo<UserDTO> registerUser(UserDTO userDTO) throws BusinessException {
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
            //查询是否已经注册
            User byMoAndMobilePhone = userApi.findByMobilePhone(userDTO.getMobilePhone());
            if (byMoAndMobilePhone != null) {
                throw new BusinessException("你已注册！请登录！");
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
            User user1 = userApi.save(user);
            UserDTO dto = new UserDTO();
            BeanCopierUtils.copy(user1, dto);
            return resultInfo.success(dto);
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
                BeanCopierUtils.copy(user, userDTO);
                return resultInfo.success(userDTO);
            }
            User u = userApi.findByMobilePhone(mobilePhone);
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
            Assert.isTrue(ValidateUtil.pwdVal(password), "密码布符合规范");
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
    public UserDTO findByMobilePhone(String mobilePhone) {
        try {
            Assert.isTrue(ValidateUtil.mobileVal(mobilePhone), "手机号不符合规范");
            User user = userApi.findByMobilePhone(mobilePhone);
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
                ResultInfo<StoreDataDTO> result = storeService.uploadFile(bytes, ".png");
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
     * 修改用户信息，这种方式虽然简单方便，但是可能导致修改过程中要查询两次数据库，性能可能稍有影响。所以并不推荐。
     * 修改信息 还是推荐调用本类提供的其他方法。
     *
     * @param userDTO
     */
    public void update(UserDTO userDTO) throws BusinessException {
        try {
            User user = userApi.get(userDTO.getUserId());
            if (user != null) {
                // 对这种copy 的原理没有深究，但是这种方式类似于调用set方法，所以 调用即可update
                BeanCopierUtils.copy(userDTO, user);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException("修改用户信息出现异常");
        }
    }


}
