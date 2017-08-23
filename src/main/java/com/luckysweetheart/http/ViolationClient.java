package com.luckysweetheart.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.http.request.ViolationRequest;
import com.luckysweetheart.http.response.ViolationResponse;
import com.luckysweetheart.utils.http.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 违章查询
 * Created by yangxin on 2017/8/23.
 */
public class ViolationClient {

    private String appcode;

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public ViolationClient(String appcode){
        this.appcode = appcode;
    }

    public ViolationResponse getViolation(ViolationRequest request) throws Exception {
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

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("plateNumber", request.getPlateNumber());
        jsonObject.put("engineNo", request.getEngineNo());
        jsonObject.put("vin", request.getVin());

        HttpResponse httpResponse = HttpUtils.doPost(host, path, method, headers, querys, jsonObject.toJSONString());

        String str = EntityUtils.toString(httpResponse.getEntity());

        JSONObject result = JSON.parseObject(str);

        ViolationResponse response = new ViolationResponse();

        Boolean success = result.getBoolean("success");
        if(success) {
            JSONObject data = result.getJSONObject("data");
            response.setAmount(data.getInteger("amount"));
            response.setToken(data.getString("token"));
            response.setTotalFine(data.getDouble("totalFine"));
            response.setTotalPoints(data.getInteger("totalPoints"));
            response.setUntreated(data.getInteger("untreated"));

            JSONArray violations = data.getJSONArray("violations");

            List<ViolationResponse.Violations> list = new ArrayList<>();

            if (violations != null) {
                for (Object violation : violations) {
                    JSONObject json = JSON.parseObject(violation.toString());
                    ViolationResponse.Violations obj = new ViolationResponse.Violations();
                    obj.setServiceFee(json.getDouble("serviceFee"));
                    obj.setProcessStatus(json.getInteger("processStatus"));
                    obj.setReason(json.getString("reason"));
                    obj.setPaymentStatus(json.getInteger("paymentStatus"));
                    obj.setCanSelect(json.getInteger("canSelect"));
                    obj.setCode(json.getString("code"));
                    obj.setViolationNum(json.getString("violationNum"));
                    obj.setViolationCity(json.getString("violationCity"));
                    obj.setCity(json.getString("city"));
                    obj.setPoint(json.getInteger("point"));
                    obj.setTime(json.getDate("time"));
                    obj.setMarkFee(json.getDouble("markFee"));
                    obj.setAddress(json.getString("address"));
                    obj.setProvince(json.getString("province"));
                    obj.setFine(json.getDouble("fine"));
                    list.add(obj);
                }
                response.setViolations(list);
            }
        }
        return response;

    }

    public ViolationResponse getViolation(byte[] bytes){
        return null;
    }

    public static void main(String[] args) {
        String str = "{" +
                "             \"success\": true," +
                "             \"data\": {" +
                "             \"token\": \"8045402120367843\", " +
                "             \"totalFine\": \"200\",                          " +
                "             \"totalPoints\": 6,                               " +
                "             \"untreated\": 3,                                 " +
                "             \"violations\": [{" +
                "             \"code\": \"1232-D1\",              " +
                "             \"time\": \"2016-06-06 12:32:38\",       " +
                "             \"fine\": \"200.00\",                    " +
                "             \"address\": \"文二西路口\",       " +
                "             \"reason\": \"您在 xx 路违反了交通规则\",      " +
                "             \"point\": 6,                              " +
                "             \"violationCity\": \"\",                  " +
                "             \"province\": \"浙江省\",              " +
                "             \"city\": \"杭州市\",                      " +
                "             \"serviceFee\": \"23.00\",          " +
                "             \"markFee\": \"0\",                 " +
                "             \"canSelect\": 1,                        " +
                "             \"processStatus\": 1,                " +
                "             \"violationNum\": \"1019\",        " +
                "             \"paymentStatus\": 1                 " +
                "             }]," +
                "             \"amount\": 3            " +
                "             }" +
                "             }";

        System.out.println(StringUtils.replace(str," ",""));
        JSONObject object = JSON.parseObject(str);
        ViolationResponse violationResponse = object.toJavaObject(ViolationResponse.class);
        System.out.println(violationResponse.toString());
        System.out.println(object.toJSONString());
    }


}
