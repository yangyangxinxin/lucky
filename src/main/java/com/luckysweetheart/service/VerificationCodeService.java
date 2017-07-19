package com.luckysweetheart.service;

import com.luckysweetheart.dal.entity.VerificationCode;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by yangxin on 2017/7/19.
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class VerificationCodeService extends ParameterizedBaseService<VerificationCode,Long> {
}
