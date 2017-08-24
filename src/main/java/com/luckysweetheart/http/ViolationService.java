package com.luckysweetheart.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.http.request.ViolationRequest;
import com.luckysweetheart.http.response.ViolationResponse;
import com.luckysweetheart.ocr.BaiduOCRService;
import com.luckysweetheart.utils.http.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangxin on 2017/8/23.
 */
@Service
public class ViolationService {

    @Value("${ali.violation.appcode}")
    private String appCode;

    @Value("${ali.violation.host}")
    private String host;

    @Value("${ali.violation.path}")
    private String path;

    @Value("${ali.violation.method}")
    private String method;

    @Resource
    private BaiduOCRService baiduOCRService;

    public ViolationResponse getViolation(ViolationRequest request) throws Exception {

        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);
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

}
