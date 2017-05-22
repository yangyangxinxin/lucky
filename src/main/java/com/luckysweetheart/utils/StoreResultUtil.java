package com.luckysweetheart.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.vo.StoreDataDTO;

/**
 * Created by yangxin on 2017/5/22.
 */
public class StoreResultUtil {

    /**
     * {"code":0,"message":"SUCCESS","request_id":"NTkyMjk3ODFfOGViMjM1XzI2MWFfNDI2NDdj",
     * "data":{
     * "access_url":"http://bubu-1253770331.file.myqcloud.com/defaultUserImg",
     * "resource_path":"/1253770331/bubu/defaultUserImg",
     * "source_url":"http://bubu-1253770331.costj.myqcloud.com/defaultUserImg",
     * "url":"http://tj.file.myqcloud.com/files/v2/1253770331/bubu/defaultUserImg",
     * "vid":"9acab0399ccda8358b5a207bdcf99a911495439233"}}
     *
     * @param json
     * @return
     */
    public static ResultInfo<StoreDataDTO> getResult(String json) {
        ResultInfo<StoreDataDTO> resultInfo = new ResultInfo<>();
        JSONObject jsonObject = JSON.parseObject(json);
        Integer code = (Integer) jsonObject.get("code");
        resultInfo.setResultCode(code + "");
        String message = (String) jsonObject.get("message");
        resultInfo.setMsg(message);

        if (code == 0) {
            String request_id = jsonObject.getString("request_id");
            JSONObject obj = (JSONObject) jsonObject.get("data");
            String access_url = obj.getString("access_url");
            String resource_path = obj.getString("resource_path");
            String source_url = obj.getString("source_url");
            String url = obj.getString("url");
            String vid = obj.getString("vid");

            StoreDataDTO storeDataDTO = new StoreDataDTO();
            storeDataDTO.setAccessUrl(access_url);
            storeDataDTO.setResourcePath(resource_path);
            storeDataDTO.setSourceUrl(source_url);
            storeDataDTO.setUrl(url);
            storeDataDTO.setvId(vid);
            storeDataDTO.setRequestId(request_id);
            return resultInfo.success(storeDataDTO);
        }
        return resultInfo.fail();
    }

}
