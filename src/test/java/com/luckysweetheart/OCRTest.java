package com.luckysweetheart;

import com.alibaba.fastjson.JSON;
import com.luckysweetheart.ocr.BaiduOCRService;
import com.luckysweetheart.ocr.OCRException;
import com.luckysweetheart.ocr.VehicleLicenseInfo;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * Created by yangxin on 2017/8/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LuckyWebApplication.class)
@WebAppConfiguration
public class OCRTest {

    @Resource
    private BaiduOCRService baiduOCRService;

    // 4570
    private String img = "C:\\Users\\dp\\Desktop\\证件识别\\IMG_4570.JPG";

    @Test
    public void test1() throws IOException, OCRException {
        VehicleLicenseInfo vehicleLicense = baiduOCRService.getVehicleLicense(FileUtils.readFileToByteArray(new File(img)), true);
        System.out.println(JSON.toJSONString(vehicleLicense));
    }

}
