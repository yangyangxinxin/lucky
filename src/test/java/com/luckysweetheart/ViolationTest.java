package com.luckysweetheart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.common.Paged;
import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.dao.UserDao;
import com.luckysweetheart.dal.entity.User;
import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.http.ViolationService;
import com.luckysweetheart.http.request.ViolationRequest;
import com.luckysweetheart.http.response.ViolationResponse;
import com.luckysweetheart.utils.EntityToModelUtils;
import com.luckysweetheart.utils.http.HttpClientThreadUtil;
import com.luckysweetheart.utils.http.HttpUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 违章查询测试
 * Created by yangxin on 2017/8/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LuckyWebApplication.class)
@WebAppConfiguration
@ActiveProfiles({"dev"})
public class ViolationTest {

    @Resource
    private ViolationService violationService;

    @Resource
    private UserDao userDao;

    private String img = "C:\\Users\\dp\\Desktop\\证件识别\\IMG_4570.JPG";

    @Test
    public void test1() throws Exception {
        System.out.println(violationService.getViolation(FileUtils.readFileToByteArray(new File(img))));
    }

    @Test
    public void test2() throws InstantiationException, IllegalAccessException {
        List<User> all = userDao.getAll();
        List<UserDTO> userDTOS = EntityToModelUtils.entityListToModelList(all, UserDTO.class);
        System.out.println(JSON.toJSONString(userDTOS));
    }

    @Test
    public void test3() throws InstantiationException, IllegalAccessException {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        PagedResult<User> result = userDao.findByPaged(new Paged(), detachedCriteria, User.class);
        PagedResult<UserDTO> pagedResult = EntityToModelUtils.getModelPagedResult(result, UserDTO.class);
        System.out.println(JSON.toJSONString(pagedResult));

    }



    public static void main(String[] args) {
        String host = "http://ddycapi.market.alicloudapi.com";
        String path = "/violation/query";
        String method = "POST";
        String appcode = "78fc9b12772c4213b03997cfe87720b9";


        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();

        String bodys = "{" +
                "\"plateNumber\":" +
                "\"苏A97P90\"(车牌号，必填)," +
                "\"engineNo\":\"YS669D\"(发动机号，视城市规则是否必填)," +
                "\"vin\":\"662722\"(车架号，视城市规则是否必填)," +
                "\"carType\":\"02\"(车辆类型01大车02小车,不必填,默认小车)," +
                "\"city\":\"杭州市\"(查询城市,不必填,默认查归属地)}";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("plateNumber", "新A769S8");
        jsonObject.put("engineNo", "H285674");
        jsonObject.put("vin", "502002");

        HttpResponse response = null;
        try {
            response = HttpUtils.doPost(host, path, method, headers, querys, jsonObject.toJSONString());
            System.out.println(response.toString());
            System.out.println("==============================");
            String result = EntityUtils.toString(response.getEntity());

            JSONObject result1 = JSON.parseObject(result);

            System.out.println("==============");
            System.out.println(result1.toJSONString());
            /**
             *
              {
             "success": true,
             "data": {
             "token": "8045402120367843",    //用户身份标识
             "totalFine": "200",                           //未处理违章总罚款
             "totalPoints": 6,                               //未处理违章总扣分
             "untreated": 3,                                 //未处理违章条数
             "violations": [{
             "code": "1232-D1",                  //违章编码,唯一，非违章条例码
             "time": "2016-06-06 12:32:38",         //违章时间
             "fine": "200.00",                      //罚款金额
             "address": "文二西路口",         //违章地址
             "reason": "您在 xx 路违反了交通规则",       //违章处理原因
             "point": 6,                                //违章扣分
             "violationCity": "",                   //违章发生城市，可能为空
             "province": "浙江省",               //省份
             "city": "杭州市",                       //城市
             "serviceFee": "23.00",            //服务费
             "markFee": "0",                    //代扣分费用
             "canSelect": 1,                         //能否勾选办理：0不可勾选, 1可勾选。
             "processStatus": 1,                  //违章处理状态：1：未处理，2：处理中，3：已处理，4：不支持
             "violationNum": "1019",            //违章官方编码
             "paymentStatus": 1                  //违章缴费状态 不返回表示无法获取该信息，1-未缴费 2-已缴
             }]
             "amount": 3             //违章总条数
             }
             }
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
