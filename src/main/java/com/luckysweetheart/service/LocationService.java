package com.luckysweetheart.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.luckysweetheart.utils.http.HttpClientThreadUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangxin on 2017/6/3.
 */
@Service
public class LocationService extends BaseService {

    /**
     * 百度地图通过坐标值获取当前位置的API地址
     */
    @Value("${baidu.location.api.url}")
    private String locationApiUrl;

    /**
     * 百度API key
     */
    @Value("${baidu.location.api.key}")
    private String appkey;

    /**
     * 将传入的坐标值 转换为BD09,为百度坐标系，通过转换过后的坐标值更加精准，先调用这个API转换坐标值，再调用获取当前位置的API
     */
    @Value("${baidu.location.convert.url}")
    private String locationUrlConvert;

    /**
     * 电脑通过客户端IP定位当前位置
     */
    @Value("${baidu.location.ip}")
    private String ipLocationUrl;

    /**
     * 根据手机定位的坐标转换为百度的坐标系
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return {"x" : "转换过的纬度","y" : "转换过的经度"}
     */
    public Map<String, Object> converCoord(String latitude, String longitude) {
        logger.info("调用百度AP转换当前地理位置,经度:" + longitude + ",纬度:" + latitude);
        Map<String, Object> heads = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("coords", longitude + "," + latitude); // 经度,纬度
        params.put("ak", appkey);
        String result = HttpClientThreadUtil.init().getPost(locationUrlConvert, heads, params);

        JSONObject jsonObject = JSON.parseObject(result);

        String status = jsonObject.get("status") + "";

        switch (status) {
            case "0":
                JSONArray array = (JSONArray) jsonObject.get("result");
                JSONObject obj = (JSONObject) array.get(0);

                String y = obj.get("y").toString(); // 纬度
                String x = obj.get("x").toString();

                Map<String, Object> map = new HashMap<>();
                map.put("x", x);
                map.put("y", y);
                logger.info("坐标转化成功，转换后的坐标值为" + x + "," + y);
                return map;
            case "1":
                logger.info("坐标转换失败，状态码:1,内部错误,本次请求的参数为:经度:" + longitude + ",纬度:" + latitude);
                break;
            case "4":
                logger.info("坐标转换失败，状态码:4,转换失败（X→GPS时必现，根据法律规定，不支持将任何类型的坐标转换为GPS坐标）,本次请求的参数为:经度:" + longitude + ",纬度:" + latitude);
                break;
            case "21":
                logger.info("坐标转换失败，状态码:21,from非法,本次请求的参数为:经度:" + longitude + ",纬度:" + latitude);
                break;
            case "22":
                logger.info("坐标转换失败，状态码:21,to非法,本次请求的参数为:经度:" + longitude + ",纬度:" + latitude);
                break;
            case "24":
                logger.info("坐标转换失败，状态码:21,coords格式非法,本次请求的参数为:经度:" + longitude + ",纬度:" + latitude);
                break;
            case "25":
                logger.info("坐标转换失败，状态码:21,coords个数非法，超过限制,本次请求的参数为:经度:" + longitude + ",纬度:" + latitude);
                break;
            default:
                logger.info("坐标转换失败，状态码:" + status + " ,未知错误,本次请求的参数为:经度:" + longitude + ",纬度:" + latitude);
                break;
        }

        String message = "";
        if (jsonObject.containsKey("message")) {
            message = (String) jsonObject.get("message");
        }
        logger.error("坐标转换失败," + message);
        return null;
    }

    /**
     * 获取当前地址
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 当前位置
     */
    public String getLocation(String latitude, String longitude) {
        try {
            logger.info("调用百度API获取当前地理位置,经度:" + longitude + ",纬度:" + latitude);
            notNull(latitude, "纬度值不能为空");
            notNull(longitude, "经度值不能为空");

            Map<String, Object> convertLocation = converCoord(latitude, longitude);
            if (convertLocation != null && convertLocation.size() > 0 && convertLocation.containsKey("x") && convertLocation.containsKey("y")) {

                Map<String, Object> heads = new HashMap<>();
                Map<String, Object> params = new HashMap<>();

                latitude = (String) convertLocation.get("y"); // 纬度
                longitude = (String) convertLocation.get("x");//经度

                params.put("output", "json"); // 返回值为json
                params.put("ak", appkey);
                params.put("location", latitude + "," + longitude); // 纬度,经度

                String result = HttpClientThreadUtil.init().getPost(locationApiUrl, heads, params);
                JSONObject jsonObject = JSON.parseObject(result);
                String code = jsonObject.get("status") + "";
                if (StringUtils.isNotBlank(code)) {
                    switch (code) {
                        case "0":
                            JSONObject obj = (JSONObject) jsonObject.get("result");
                            String address = (String) obj.get("formatted_address");
                            logger.info("位置获取成功，当前位置:" + address);
                            return address;
                        case "1":
                            logger.info("位置信息获取失败，错误码 ：1，服务器内部错误，本次请求的经纬度为" + latitude + "," + longitude);
                            break;
                        case "2":
                            logger.info("位置信息获取失败，错误码：2，请求参数非法，本次请求的经纬度为" + latitude + "," + longitude);
                            break;
                        case "3":
                            logger.info("位置信息获取失败，错误码：3，权限校验失败，本次请求的经纬度为" + latitude + "," + longitude);
                            break;
                        case "4":
                            logger.info("位置信息获取失败，错误码：4，配额校验失败，本次请求的经纬度为" + latitude + "," + longitude);
                            break;
                        case "5":
                            logger.info("位置信息获取失败，错误码：5，ak不存在或者非法，本次请求的经纬度为" + latitude + "," + longitude);
                            break;
                        case "101":
                            logger.info("位置信息获取失败，错误码：101，服务禁用，本次请求的经纬度为" + latitude + "," + longitude);
                            break;
                        case "102":
                            logger.info("位置信息获取失败，错误码：102，不通过白名单或者安全码不对，本次请求的经纬度为" + latitude + "," + longitude);
                            break;
                        case "301":
                            logger.info("位置信息获取失败，错误码：301，永久配额超限，限制访问,本次请求的经纬度为" + latitude + "," + longitude);
                            break;
                        case "302":
                            logger.info("位置信息获取失败，错误码：302，天配额超限，限制访问，限制访问,本次请求的经纬度为" + latitude + "," + longitude);
                            break;
                    }
                    if (code.startsWith("2") && code.length() == 3) {
                        logger.info("位置信息获取失败，错误码：" + code + "，无权限，本次请求的经纬度为" + latitude + "," + longitude);
                        return null;
                    }
                }
                String message = "";
                if (jsonObject.containsKey("message")) {
                    message = (String) jsonObject.get("message");
                }
                logger.info("位置获取失败，" + message);
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据IP地址获取当前位置
     *
     * @param ip IP地址
     * @return 当前位置
     */
    public String getLocationByIp(String ip) {
        logger.info("开始调用百度IP获取地理信息的API,ip:" + ip);
        try {
            notNull(ip, "ip地址不能为空");

            Map<String, Object> heads = new HashMap<>();
            Map<String, Object> params = new HashMap<>();

            params.put("ip", ip); // 返回值为json
            params.put("ak", appkey);

            String result = HttpClientThreadUtil.init().getPost(ipLocationUrl, heads, params);

            JSONObject jsonObject = JSON.parseObject(result);

            String status = jsonObject.get("status") + "";

            if ("0".equals(status)) {
                if (jsonObject.containsKey("content")) {
                    JSONObject content = (JSONObject) jsonObject.get("content");
                    String address = (String) content.get("address");
                    logger.info("根据IP地址获取当前位置成功，当前位置：" + address);
                    return address;
                }
            }
            logger.info("通过ip获取当前位置失败，ip：" + ip + ",错误码:" + status);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }


}
