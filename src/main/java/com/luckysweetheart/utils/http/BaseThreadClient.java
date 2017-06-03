package com.luckysweetheart.utils.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luckysweetheart.utils.CommonUtil;
import com.luckysweetheart.utils.ResultInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 线程安全的服务请求类
 */
public class BaseThreadClient {

    private String appKey;
    private String appSecret;
    private String url;

    private String ts;
    private String v;

    private Map<String, Object> paramMaps;
    private Map<String, Object> extMaps;

    private boolean isSimple;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private BaseThreadClient() {
    }

    public static BaseThreadClient init(String appKey, String appSecret, String url) {
        BaseThreadClient baseThreadClient = new BaseThreadClient();
        baseThreadClient.appKey = appKey;
        baseThreadClient.appSecret = appSecret;
        baseThreadClient.url = url;
        return baseThreadClient;
    }

    public String getAppKey() {
        return appKey;
    }

    public BaseThreadClient setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public BaseThreadClient setAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public BaseThreadClient setUrl(String url) {
        this.url = url;
        return this;
    }

    public BaseThreadClient setVersion(String version) {
        this.v = version;
        return this;
    }

    public BaseThreadClient setParams(Map<String, Object> paramMaps) {
        this.paramMaps = paramMaps;
        return this;
    }

    public BaseThreadClient setExt(Map<String, Object> extMaps) {
        this.extMaps = extMaps;
        return this;
    }

    @SuppressWarnings("unchecked")
    private <T> ResultInfo<T> send(String method, Class<T> cla) {
        //生成头
        Map<String, Object> headMap = new HashMap<String, Object>();
        String v = StringUtils.isBlank(this.v) ? "v1.0.0" : this.v;
        String ts = StringUtils.isBlank(this.ts) ? new Date().getTime() + "" : this.ts;
        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(ts) || StringUtils.isBlank(v)) {
            throw new RuntimeException("请的参数头信息缺失");
        }
        headMap.put(Cons.PARAMS_HEADER_APPKEY, appKey);
        headMap.put(Cons.PARAMS_HEADER_TIMESTAMP, ts);
        headMap.put(Cons.PARAMS_HEADER_VERSION, v);
        //生成params
        //paramMaps
        //生成签名
        String headStr = getHead(headMap);
        String paramsStr = getParams(paramMaps);
        String extStr = getExt(extMaps);

        String sign = DigestUtils.sha1Hex(appSecret + paramsStr + headStr + extStr);
        headMap.put(Cons.PARAMS_HEADER_SIGNATURE, sign);
        try {
            String str = HttpClientThreadUtil.init().getPost(url, headMap, paramMaps);
            if (!isSimple) {
                ResultInfo<T> resultInfo = new ResultInfo<T>();
                resultInfo = JSON.parseObject(str, resultInfo.getClass());
                T t = CommonUtil.parValNoErr(resultInfo.getData(), cla);
                if (t == null) {
                    resultInfo.setData(JSON.parseObject(resultInfo.getData() + "", cla));
                } else {
                    if (resultInfo.getData() instanceof JSONObject) {
                        t = JSON.parseObject(((JSONObject) t).toJSONString(), cla);
                    }
                    resultInfo.setData(t);
                }
                return (ResultInfo<T>) resultInfo;
            } else {
                ResultInfo<T> resultInfo = new ResultInfo<T>();
                if (StringUtils.isBlank(str)) {
                    return resultInfo.setSuccess(false);
                } else {
                    if (cla.getCanonicalName().equals(byte[].class.getCanonicalName())) {
                        return resultInfo.setData((T) Base64.decodeBase64(str), true, "");
                    } else {
                        return resultInfo.setData((T) CommonUtil.parVal(resultInfo, cla), true, "");
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return (ResultInfo<T>) ResultInfo.createFail(e.getMessage(), e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> ResultInfo<T> send(TypeReference<ResultInfo<T>> tr) {
        //生成头
        Map<String, Object> headMap = new HashMap<String, Object>();
        String v = StringUtils.isBlank(this.v) ? "v1.0.0" : this.v;
        String ts = StringUtils.isBlank(this.ts) ? new Date().getTime() + "" : this.ts;
        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(ts) || StringUtils.isBlank(v)) {
            throw new RuntimeException("请的参数头信息缺失");
        }
        headMap.put(Cons.PARAMS_HEADER_APPKEY, appKey);
        headMap.put(Cons.PARAMS_HEADER_TIMESTAMP, ts);
        headMap.put(Cons.PARAMS_HEADER_VERSION, v);
        //生成params
        //paramMaps
        //生成签名
        String headStr = getHead(headMap);
        String paramsStr = getParams(paramMaps);
        String extStr = getExt(extMaps);

        String sign = SignUtil.sign(appSecret, paramsStr, headStr, extStr);

        headMap.put(Cons.PARAMS_HEADER_SIGNATURE, sign);
        try {
            String str = HttpClientThreadUtil.init().getPost(url, headMap, paramMaps);
            if (!isSimple) {
                ResultInfo<T> resultInfo = JSON.parseObject(str, tr);
                return (ResultInfo<T>) resultInfo;
            } else {
                ResultInfo<T> resultInfo = new ResultInfo<T>();
                if (StringUtils.isBlank(str)) {
                    return resultInfo.setSuccess(false);
                } else {
                    if (tr.getType().equals(byte[].class)) {
                        return resultInfo.setData((T) Base64.decodeBase64(str), true, "");
                    } else {
                        return resultInfo.setData((T) CommonUtil.parVal(resultInfo, ((Class) tr.getType())), true, "");
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return (ResultInfo<T>) ResultInfo.createFail(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unused")
    private ResultInfo<String> send(String method) {
        return send(method, String.class);
    }

    public ResultInfo<String> send() {
        return send("post", String.class);
    }

    public <T> ResultInfo<T> send(Class<T> cla) {
        return send("post", cla);
    }

    /**
     * 返回请求头参数
     *
     * @param
     * @return
     */
    private String getHead(Map<String, Object> maps) {
        //appKey key//ts 时间戳//v 版本号
        String appKey = maps.get(Cons.PARAMS_HEADER_APPKEY) + "";
        String ts = maps.get(Cons.PARAMS_HEADER_TIMESTAMP) + "";
        String v = maps.get(Cons.PARAMS_HEADER_VERSION) + "";
        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(ts) || StringUtils.isBlank(v)) {
            throw new RuntimeException("请的参数头信息缺失");
        }
        String resultStr = Cons.PARAMS_HEADER_APPKEY + appKey;
        resultStr += Cons.PARAMS_HEADER_TIMESTAMP + ts;
        resultStr += Cons.PARAMS_HEADER_VERSION + v;
        return resultStr;
    }

    /**
     * 返回请求参数
     *
     * @param
     * @return
     */
    private String getParams(Map<String, Object> maps) {
        String resultStr = "";
        if (maps == null) return resultStr;
        for (String set : new TreeSet<String>(maps.keySet())) {
            String val = "";
            if (maps.get(set) instanceof String) {
                val = maps.get(set) + "";
            } else if (maps.get(set) instanceof byte[]) {
                val = JSON.toJSONString(Base64.encodeBase64String((byte[]) maps.get(set)));
            } else {
                val = JSON.toJSONString(maps.get(set));
            }
            resultStr += set + val;
            maps.put(set, val);
        }
        return resultStr;
    }

    private String getExt(Map<String, Object> maps) {
        return "";
    }

    public boolean isSimple() {
        return isSimple;
    }

    public BaseThreadClient setSimple(boolean isSimple) {
        this.isSimple = isSimple;
        return this;
    }

    public static void main(String[] args) {
        /**
         Map<String,Object> paramMaps=new HashMap<String,Object>();
         paramMaps.put("preservationId", 48);
         String ke="htb.ebaoquan.org";
         String sc=SignConfig.keyMap.get(ke);
         ResultInfo<PreservationFull> resultInfo=BaseThreadClient.init(ke,sc)
         .setVersion("v1.0.0")
         .setUrl("http://localhost:8085/preservationFull/findPreservationFull")
         .setParams(paramMaps)
         .send(PreservationFull.class);
         System.out.println(JSON.toJSON(resultInfo));
         */
    }
}
