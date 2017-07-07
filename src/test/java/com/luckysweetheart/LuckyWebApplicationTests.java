package com.luckysweetheart;

import com.alibaba.fastjson.JSON;
import com.luckysweetheart.common.Const;
import com.luckysweetheart.common.Paged;
import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.dao.UserInfoDao;
import com.luckysweetheart.dal.entity.UnLoginUrl;
import com.luckysweetheart.dal.entity.UserInfo;
import com.luckysweetheart.dal.query.PhotoQuery;
import com.luckysweetheart.dal.query.condition.ConditionParam;
import com.luckysweetheart.dal.query.field.PhotoQueryField;
import com.luckysweetheart.dal.query.order.OrderParam;
import com.luckysweetheart.dal.redis.dao.TestDao;
import com.luckysweetheart.dto.ArticleDTO;
import com.luckysweetheart.dto.PhotoDTO;
import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.service.*;
import com.luckysweetheart.store.StoreService;
import com.luckysweetheart.utils.EmailSender;
import com.luckysweetheart.utils.EmailTemplate;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.dto.StoreDataDTO;
import com.luckysweetheart.web.utils.DomainUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LuckyWebApplication.class)
@WebAppConfiguration
public class LuckyWebApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
    }

    @Resource
    private StoreService storeService;

    @Resource
    private ArticleService articleService;

    @Resource
    private EmailService emailService;

    @Test
    public void test1() {
        ResultInfo<UserDTO> resultInfo = null;
        try {
            resultInfo = userService.login("yangxin", "yangxin123");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(resultInfo);
    }

    @Test
    public void test2() throws BusinessException {
        ResultInfo<StoreDataDTO> resultInfo =
                storeService.uploadFile("F:\\code\\\\my\\\\lucky\\\\src\\\\main\\\\resources\\\\static\\\\img\\\\defaultUserImg.png", "defaultUserImg.png");

        System.out.println(resultInfo);
    }

    @Test
    public void test3() throws BusinessException, InterruptedException {
        ArticleDTO articleDTO = null;

        synchronized (this) {
            for (int i = 0; i < 100; i++) {
                articleDTO = new ArticleDTO();
                articleDTO.setDeleteStatus(0);
                if (i % 10 == 0) {
                    articleDTO.setAuthor("刘俊妤");
                } else {
                    articleDTO.setAuthor("yangxin");
                }
                articleDTO.setCommentsCount(0L);
                articleDTO.setTitle("这是第" + i + " 篇的标题");
                articleDTO.setContent("这是第" + i + "篇的内容。");
                articleDTO.setCreateTime(new Date());
                articleDTO.setUpdateTime(new Date());
                articleDTO.setOwnerUserId(1L);
                articleService.create(articleDTO);
                Thread.sleep(500L);
            }
        }

    }

    @Test
    public void test4() throws BusinessException {
        ResultInfo<Void> resultInfo = storeService.deleteFile("/1253770331/bubu/defaultUserImg.png","bubu");
        System.out.println(resultInfo);
    }

    @Test
    public void test5() throws IOException {
        File file = new File("d:/yangxin/yangxin/yangxin/test.txt");
        if(!file.exists()){
            file.createNewFile();
        }
    }

    @Test
    public void test6() {
        try {
            byte[] bytes = storeService.download("/apply_02.jpg");
            System.out.println(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test7() {
        UserDTO dto = userService.findById(1L);
        dto.setMobilePhone("18580097376");
        try {
            userService.update(dto);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

    @Resource
    private PhotoService photoService;

    @Test
    public void test8() {
        PhotoQuery photoQuery = new PhotoQuery();

        List<ConditionParam<PhotoQueryField>> conditionParams = new ArrayList<>();
        List<OrderParam<PhotoQueryField>> orderParams = new ArrayList<>();

        Paged paged = new Paged();
        paged.setPageSize(10);
        paged.setPage(1);

        // where photoId = 1
        conditionParams.add(new ConditionParam<PhotoQueryField>(PhotoQueryField.DELETE_STATUS, Const.DELETE_STATUS_NO, ConditionParam.OPERATION_EQ));
        conditionParams.add(new ConditionParam<PhotoQueryField>(PhotoQueryField.USER_ID, 2L, ConditionParam.OPERATION_EQ));

        // order by create desc
        orderParams.add(new OrderParam<PhotoQueryField>(PhotoQueryField.CREATE_TIME, OrderParam.ORDER_TYPE_DESC));

        photoQuery.setPaged(paged);
        photoQuery.setConditionParams(conditionParams);
        photoQuery.setOrderParams(orderParams);

        ResultInfo<PagedResult<PhotoDTO>> result = photoService.query(photoQuery);
        if (result.isSuccess()) {
            PagedResult<PhotoDTO> pagedResult = result.getData();
            List<PhotoDTO> photoDTOS = pagedResult.getResults();
            for (PhotoDTO photoDTO : photoDTOS) {
                System.out.println(JSON.toJSONString(photoDTO));
            }
        }
    }

    @Test
    public void testSend(){
        emailService.sendSimpleMail("848135512@qq.com","测试3","这是内容3");
    }

    @Test
    public void testSend2(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                final EmailSender emailSender = EmailSender.init().emailTemplate(EmailTemplate.REGISTER).to("848135512@qq.com").param("username","848135512").param("code","123123123123").subject("注册1");
                System.out.println("第一个线程执行");
                emailService.sendEmailTemplate(emailSender);
                System.out.println("第一个线程执行完毕");
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final EmailSender emailSender = EmailSender.init().emailTemplate(EmailTemplate.REGISTER).to("yangxin_y@qq.com").param("username","yangxin_y").param("code","123123123123").subject("注册2");
                System.out.println("第二个线程执行");
                emailService.sendEmailTemplate(emailSender);
                System.out.println("第二个线程执行完毕");
            }
        }).start();
        System.out.println("主线程执行完毕");
        try {
            Thread.sleep(10 * 1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void  test111(){
         EmailSender.init().emailTemplate(EmailTemplate.REGISTER).to("848135512@qq.com","981987024@qq.com").
                param("username","848135512").param("code","eeferfeeqq").subject("注册3").sleep(true).send();

    }

    @Resource
    private JavaMailSender sender;

    @Resource
    private TestDao testDao;

    @Test
    public void test122(){
        testDao.set("aaabbb","dwedfwfwerfr");
    }

    @Resource
    private UnLoginUrlService unLoginUrlService;

    @Test
    public void test123(){

        UnLoginUrl unLoginUrl = new UnLoginUrl();
        unLoginUrl.setCreateTime(new Date());
        unLoginUrl.setUrl("/");
        unLoginUrlService.save(unLoginUrl);

        unLoginUrl = new UnLoginUrl();
        unLoginUrl.setCreateTime(new Date());
        unLoginUrl.setUrl("/index");
        unLoginUrlService.save(unLoginUrl);

        unLoginUrl = new UnLoginUrl();
        unLoginUrl.setCreateTime(new Date());
        unLoginUrl.setUrl("/account/*");
        unLoginUrlService.save(unLoginUrl);

        unLoginUrl = new UnLoginUrl();
        unLoginUrl.setCreateTime(new Date());
        unLoginUrl.setUrl("/download");
        unLoginUrlService.save(unLoginUrl);

        unLoginUrl = new UnLoginUrl();
        unLoginUrl.setCreateTime(new Date());
        unLoginUrl.setUrl("/article/list");
        unLoginUrlService.save(unLoginUrl);

        unLoginUrl = new UnLoginUrl();
        unLoginUrl.setCreateTime(new Date());
        unLoginUrl.setUrl("/article/detail");
        unLoginUrlService.save(unLoginUrl);

        unLoginUrl = new UnLoginUrl();
        unLoginUrl.setCreateTime(new Date());
        unLoginUrl.setUrl("/photo/list");
        unLoginUrlService.save(unLoginUrl);

        unLoginUrl = new UnLoginUrl();
        unLoginUrl.setCreateTime(new Date());
        unLoginUrl.setUrl("/photo/queryPage");
        unLoginUrlService.save(unLoginUrl);

        unLoginUrl = new UnLoginUrl();
        unLoginUrl.setCreateTime(new Date());
        unLoginUrl.setUrl("/test/*");
        unLoginUrlService.save(unLoginUrl);

    }

    @Test
    public void test124(){
        List<String> list = unLoginUrlService.queryUnLoginUrl();
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserInfoDao userInfoDao;

    @Test
    public void test125(){
        long start = System.currentTimeMillis();
        UserInfo userInfo;
        for (int i = 0; i < 100000; i++) {
            userInfo = new UserInfo();
            userInfo.setName(Math.round(Math.random() * 1000) + "");
            userInfoService.save(userInfo);
        }
        long end = System.currentTimeMillis();

        //4205s
        System.out.println("共消耗时间："  + (end - start) / 1000 + " s");

    }

    @Test
    public void test126(){
        userInfoDao.updateA();
    }

    @Test
    public void test127(){
        userInfoDao.updateB();
    }

    @Test
    public void test128(){
        long start = System.currentTimeMillis();
        List<UserInfo> userInfos = userInfoDao.getAll();
        for (UserInfo userInfo : userInfos) {
            System.out.println(userInfo);
        }
        long end = System.currentTimeMillis();

        //4205s
        System.out.println("共消耗时间："  + (end - start) / 1000 + " s");
    }
}
