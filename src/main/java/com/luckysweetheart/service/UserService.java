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

/**
 * Created by yangxin on 2017/5/22.
 */
@Service
public class UserService extends BaseService {

    private final String salt = "www.luckysweetheart.com";

    @Resource
    private UserApi userApi;

    /**
     * 注册
     *
     * @param userDTO
     * @return
     */
    public ResultInfo<UserDTO> registerUser(UserDTO userDTO) throws Exception{
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
            User byMoAndMobilePhone = userApi.findByMoAndMobilePhone(userDTO.getMobilePhone());
            if (byMoAndMobilePhone != null) {
                throw new BusinessException("你已注册！请登录！");
            }
            // 加密用户密码
            String pwd = AesUtil.encrypt(salt + userDTO.getPassword());
            User user = new User();
            user.setPassword(pwd);
            user.setUsername(userDTO.getUsername());
            user.setMobilePhone(userDTO.getMobilePhone());
            User user1 = userApi.save(user);
            UserDTO dto = new UserDTO();
            BeanCopierUtils.copy(user1, dto);
            return resultInfo.success(dto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());//回滚
        }
    }

    public ResultInfo<Void> login(String mobilePhone, String password) throws Exception{
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            if (!ValidateUtil.mobileVal(mobilePhone)) {
                throw new BusinessException("用户名不符合规范");
            }
            if (!ValidateUtil.pwdVal(password)) {
                throw new BusinessException("密码不符合规范！");
            }
            password = AesUtil.encrypt(salt + password);
            User user = userApi.login(mobilePhone, password);
            if (user != null) {
                logger.info("登录成功");
                return resultInfo.success();
            }
            logger.info("登录失败");
            return resultInfo.fail("登录失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

}
