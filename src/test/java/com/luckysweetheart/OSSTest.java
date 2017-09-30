package com.luckysweetheart;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.luckysweetheart.store.StorageGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

/**
 * Created by yangxin on 2017/8/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LuckyWebApplication.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class OSSTest {

    @Value("${oss.access.key.sercet}")
    private String accessKeySecret;

    @Resource
    private OSSClient ossClient;

    @Resource
    private StorageGroupService storageGroupService;

    @Test
    public void test1(){
        // 上传
        byte[] content = "Hello OSS".getBytes();
        PutObjectResult result = ossClient.putObject("lucky-bubu-dev", accessKeySecret, new ByteArrayInputStream(content));
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void test2() {
        List<Bucket> buckets = ossClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
            System.out.println(bucket.getCreationDate());
            System.out.println(bucket.getOwner());
            System.out.println(bucket.getLocation());
        }

        System.out.println(storageGroupService.getUserGroupName());
    }

    @Test
    public void test3() {
        ObjectListing objectListing = ossClient.listObjects(storageGroupService.getDefaultGroupName());
        List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for (OSSObjectSummary objectSummary : objectSummaries) {
            System.out.println(JSON.toJSONString(objectSummary));
        }
    }

    @Test
    public void test4() throws IOException {
        OSSObject file = ossClient.getObject(storageGroupService.getDefaultGroupName(), "LxsRHeOECzxWZPc3F1wkAIGHGtMNlk");
        InputStream inputStream = file.getObjectContent();
        int n;
        OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\dp\\Desktop\\OCR\\test.txt"));
        while ((n = inputStream.read()) != -1) {
            outputStream.write(n);
        }
    }

}
