package com.luckysweetheart.ocr;

import com.baidu.aip.ocr.AipOcr;
import com.luckysweetheart.utils.DateUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * 百度文字识别服务，目前支持身份证识别、银行卡识别
 * Created by yangxin on 2017/8/11.
 */
@Service
public class BaiduOCRService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int MAX_LENGTH = 1024 * 150;

    @Resource
    private AipOcr aipOcr;

    @Resource
    private ThumbnailUtilService thumbnailUtilService;

    /**
     * 读取身份证信息
     *
     * @param bytes        文件
     * @param isFront      是否为身份证正面，是为true，不是为false
     * @param isCompress   是否压缩图片
     * @param isDetectRisk 是否开启风险等级检测
     * @return
     * @throws OCRException
     */
    public IdentityInfo getIdentityInfo(byte[] bytes, boolean isFront, boolean isCompress, boolean isDetectRisk) throws OCRException, IOException {
        logger.info("开始调用百度身份证识别接口... at {} ", DateUtil.formatDate(new Date()));
        long start = System.currentTimeMillis();

        if (isCompress) {
            if (bytes.length > MAX_LENGTH) {
                bytes = thumbnailUtilService.createThumbnail(bytes);
                long end = System.currentTimeMillis();
                logger.info("压缩过后的文件大小为：{} KB,cost {} ms", bytes.length / 1024, end - start);
            }
        }

        HashMap<String, String> options = new HashMap<String, String>();

        options.put("detect_direction", "true");

        if (isDetectRisk) {
            options.put("detect_risk", "true");
        }
        JSONObject idCard = aipOcr.idcard(bytes, isFront, options);
        long end = System.currentTimeMillis();
        logger.info("调用百度身份证识别接口调用结束... at {} , cost {} ms", DateUtil.formatDate(new Date()), end - start);
        return OCRUtil.getIdentityInfo(idCard);
    }

    /**
     * 读取身份证正面信息
     *
     * @param bytes 文件
     * @return
     * @throws OCRException
     */
    public IdentityInfo getIdentityInfo(byte[] bytes) throws OCRException, IOException {
        return getIdentityInfo(bytes, true, true, false);
    }

    /**
     * 读取银行卡信息，默认需要压缩图片
     *
     * @param bytes 文件
     * @return
     * @throws OCRException
     * @throws IOException
     */
    public BankCardInfo getBankCardInfo(byte[] bytes) throws OCRException, IOException {
        return this.getBankCardInfo(bytes, true);
    }

    /**
     * 读取银行卡信息
     *
     * @param bytes      文件
     * @param isCompress 是否压缩图片
     * @return
     * @throws OCRException
     */
    public BankCardInfo getBankCardInfo(byte[] bytes, boolean isCompress) throws OCRException, IOException {
        logger.info("开始调用百度银行卡识别接口... at {} ", DateUtil.formatDate(new Date()));
        long start = System.currentTimeMillis();
        if (isCompress) {
            if (bytes.length > MAX_LENGTH) {
                bytes = thumbnailUtilService.createThumbnail(bytes);
                long end = System.currentTimeMillis();
                logger.info("压缩过后的文件大小为：{} KB,cost {} ms", bytes.length / 1024, end - start);
            }
        }
        JSONObject response = aipOcr.bankcard(bytes);
        long end = System.currentTimeMillis();
        logger.info("调用百度身份证识别接口调用结束... at {} , cost {} ms", DateUtil.formatDate(new Date()), end - start);
        return OCRUtil.getBankCardInfo(response);
    }




}
