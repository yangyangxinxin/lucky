package com.luckysweetheart.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.dal.dao.ViolationRecordDao;
import com.luckysweetheart.dal.entity.ViolationRecord;
import com.luckysweetheart.http.request.ViolationRequest;
import com.luckysweetheart.http.response.ViolationResponse;
import com.luckysweetheart.ocr.BaiduOCRService;
import com.luckysweetheart.ocr.OCRException;
import com.luckysweetheart.ocr.VehicleLicenseInfo;
import com.luckysweetheart.service.ViolationRecordService;
import com.luckysweetheart.utils.http.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

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

    @Resource
    private ViolationRecordDao violationRecordDao;

    @Transactional(rollbackFor = Exception.class)
    public ViolationResponse getViolation(ViolationRequest request) throws IOException {

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
        response.setEngineNo(request.getEngineNo());
        response.setPlateNumber(request.getPlateNumber());
        response.setVin(request.getVin());

        ViolationRecord violationRecord = new ViolationRecord();

        violationRecord.setCreateTime(new Date());
        violationRecord.setPlateNumber(response.getPlateNumber());
        violationRecord.setVin(response.getVin());
        violationRecord.setEngineNo(response.getEngineNo());

        Boolean success = result.getBoolean("success");
        if (success) {
            violationRecord.setSuccess(ViolationRecord.SUCCESS);

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

            violationRecord.setAmount(response.getAmount());
            violationRecord.setTotalFine(response.getTotalFine());
            violationRecord.setTotalPoints(response.getTotalPoints());
            violationRecord.setUntreated(response.getUntreated());

            if(response.getViolations() != null && response.getViolations().size() > 0){
                violationRecord.setViolations(JSON.toJSONString(response.getViolations()));
            }

        }else{
            violationRecord.setSuccess(ViolationRecord.SUCCESS);
        }
        violationRecordDao.save(violationRecord);
        return response;

    }

    public ViolationResponse getViolation(byte[] bytes) throws IOException, OCRException {
        VehicleLicenseInfo vehicleLicense = baiduOCRService.getVehicleLicense(bytes, true);
        if (vehicleLicense == null) {
            throw new OCRException("识别行驶证失败，请检查照片是否是行驶证或检查清晰度是否足够");
        }
        if (StringUtils.isBlank(vehicleLicense.getPlateNumber())) {
            throw new OCRException("行驶证信息识别成功，但是车牌号未能成功识别。");
        }
        if (StringUtils.isBlank(vehicleLicense.getEngineNo())) {
            throw new OCRException("行驶证信息识别成功，但是发动机号码未能成功识别。");
        }
        if (StringUtils.isBlank(vehicleLicense.getVin())) {
            throw new OCRException("行驶证信息识别成功，但是车架号未能成功识别。");
        }
        ViolationRequest request = new ViolationRequest();
        request.setVin(vehicleLicense.getVin());
        request.setPlateNumber(vehicleLicense.getPlateNumber());
        request.setEngineNo(vehicleLicense.getEngineNo());
        return getViolation(request);
    }

}
