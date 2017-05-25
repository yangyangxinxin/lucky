package com.luckysweetheart.service;

import com.luckysweetheart.dal.dao.UserApi;
import com.luckysweetheart.dal.entity.User;
import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.utils.AesUtil;
import com.luckysweetheart.utils.BeanCopierUtils;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.utils.ValidateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by yangxin on 2017/5/22.
 */
@Service
public class UserService extends BaseService {

    private final String salt = "www.luckysweetheart.com";

    private final String defaultUserImg = "/1253770331/bubu/defaultUserImg.png";

    @Resource
    private UserApi userApi;

    /**
     * 注册
     *
     * @param userDTO
     * @return
     */
    public ResultInfo<UserDTO> registerUser(UserDTO userDTO) throws Exception {
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
            String pwd = AesUtil.encrypt(salt + userDTO.getPassword());
            User user = new User();
            user.setPassword(pwd);
            user.setUsername(userDTO.getUsername());
            user.setMobilePhone(userDTO.getMobilePhone());
            user.setImgPath(defaultUserImg);
            user.setDeleteStatus(User.DELETE_STATUS_NO);
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

    public ResultInfo<UserDTO> login(String mobilePhone, String password) throws Exception {
        ResultInfo<UserDTO> resultInfo = new ResultInfo<>();
        try {
            if (!ValidateUtil.mobileVal(mobilePhone)) {
                throw new BusinessException("用户名不符合规范");
            }
            if (!ValidateUtil.pwdVal(password)) {
                throw new BusinessException("密码不符合规范！");
            }
            password = AesUtil.encrypt(salt + password);
            User user = userApi.login(mobilePhone, password);
            UserDTO userDTO = new UserDTO();
            if (user != null) {
                logger.info("登录成功");
                BeanCopierUtils.copy(user, userDTO);
                return resultInfo.success(userDTO);
            }
            logger.info("登录失败");
            return resultInfo.fail("登录失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 重置用户密码
     * @param userId
     * @param password
     * @return
     */
    public ResultInfo<UserDTO> resetPwd(Long userId, String password) {
        ResultInfo<UserDTO> resultInfo = new ResultInfo<>();
        try {
            Assert.notNull(userId, "用户id不能为空");
            Assert.isTrue(ValidateUtil.pwdVal(password), "密码布符合规范");
            User user = userApi.findOne(userId);
            Assert.notNull(user, "该用户不存在");
            password = AesUtil.encrypt(salt + password);
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

}
