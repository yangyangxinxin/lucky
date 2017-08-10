package com.luckysweetheart;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;

/**
 * Created by yangxin on 2017/8/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LuckyWebApplication.class)
@WebAppConfiguration
public class OSSTest {

    @Value("${oss.access.key.sercet}")
    private String accessKeySecret;

    @Resource
    private OSSClient ossClient;

    @Test
    public void test1(){
        // 上传
        byte[] content = "Hello OSS".getBytes();
        PutObjectResult result = ossClient.putObject("lucky-bubu-dev", accessKeySecret, new ByteArrayInputStream(content));
        System.out.println(JSON.toJSONString(result));
    }
}
