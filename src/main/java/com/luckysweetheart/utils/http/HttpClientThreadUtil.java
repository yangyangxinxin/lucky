package com.luckysweetheart.utils.http;

import com.alibaba.fastjson.JSON;
import com.luckysweetheart.exception.ResultInfoException;
import com.luckysweetheart.utils.CommonUtil;
import com.luckysweetheart.utils.ResultInfo;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientThreadUtil {

    public Logger logger = LoggerFactory.getLogger(HttpClientThreadUtil.class);
    /**
     * 默认连接超时时间
     */
    private final static int DEFAULT_CONN_TIMEOUT = 1000 * 60; // 15s
    /**
     * 重连次数
     */
    private final static int DEFAULT_RETRY_TIMES = 3;

    private final static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36";
    private final DefaultHttpClient httpClient;

    private static HttpClientThreadUtil httpClientThreadUtil;
    private static Object objTg = new Object();

    private static final String APPLICATION_JSON = "application/json";

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    /**
     * 新建一个httpClient连接
     */
    private HttpClientThreadUtil() {
        HttpParams params = new BasicHttpParams();

        int connTimeout = DEFAULT_CONN_TIMEOUT;
        ConnManagerParams.setTimeout(params, connTimeout);
        HttpConnectionParams.setSoTimeout(params, connTimeout);
        HttpConnectionParams.setConnectionTimeout(params, connTimeout);

        HttpProtocolParams.setUserAgent(params, userAgent);

        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(10));
        ConnManagerParams.setMaxTotalConnections(params, 20);

        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSocketBufferSize(params, 1024 * 8);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLTrustAllSocketFactory.getSocketFactory(), 443));

        httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schemeRegistry), params);
        //记录重试日志。retryTask
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= DEFAULT_RETRY_TIMES) {
                    //Do not retry if over max retry count
                    return false;
                }
                if (exception instanceof InterruptedIOException) {
                    // Timeout
                    return false;
                }
                if (exception instanceof UnknownHostException) {
                    // Unknown host
                    return false;
                }
                if (exception instanceof ConnectException) {
                    // Connection refused
                    return false;
                }
                if (exception instanceof SSLException) {
                    // SSL handshake exception
                    return false;
                }
                HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    // Retry if the request is considered idempotent
                    return true;
                }
                return false;
            }

        };
        httpClient.setHttpRequestRetryHandler(myRetryHandler);
    }

    /**
     * 实例化记录
     *
     * @return
     */
    public static HttpClientThreadUtil init() {
        synchronized (objTg) {
            if (httpClientThreadUtil == null) {
                httpClientThreadUtil = new HttpClientThreadUtil();
            }
        }
        return httpClientThreadUtil;
    }

    /**
     * post请求
     *
     * @param uri
     * @param heads
     * @param params
     * @return
     */
    public String getPost(String uri, Map<String, Object> heads,
                          Map<String, Object> params) {
        String str = "";
        HttpResponse response = null;
        try {
            HttpPost httppost = new HttpPost(uri);
            httppost.addHeader("Accept", "text/html");
            //httppost.addHeader("Connection", "close");
            // 请求头
            if (heads != null && heads.size() > 0) {
                for (String key : heads.keySet()) {
                    httppost.addHeader(key, heads.get(key) + "");
                }
            }
            // 设置编码
            List<NameValuePair> pList = new ArrayList<NameValuePair>();
            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    pList.add(new BasicNameValuePair(key, params.get(key) + ""));
                }
            }
            httppost.setEntity(new UrlEncodedFormEntity(pList, HTTP.UTF_8));
            response = httpClient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得返回的字符串
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                str = result;
                // 如果是下载的文件，可以用response.getEntity().getContent返回InputStream
            } else {
                str = JSON.toJSONString(ResultInfo.createFail("请求失败:" + response.getStatusLine().getStatusCode(), null)
                        .setResultCode(response.getStatusLine().getStatusCode() + ""));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return JSON.toJSONString(ResultInfo.createFail("请求失败:" + e.getMessage(), null).setResultCode("404"));
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
            try {
                response.getEntity().getContent().close(); // !!!IMPORTANT
            } catch (Exception e) {
            }
        }
        return str;
    }


    /**
     * post请求   no Header
     *
     * @param uri
     * @param heads
     * @param params
     * @return
     */
    public String getPostNoHeader(String uri, Map<String, Object> heads,
                                  Map<String, Object> params) {
        String str = "";
        HttpResponse response = null;
        try {
            HttpPost httppost = new HttpPost(uri);
            //httppost.addHeader("Accept", "text/html");
            //httppost.addHeader("Connection", "close");
            // 请求头
            if (heads != null && heads.size() > 0) {
                for (String key : heads.keySet()) {
                    httppost.addHeader(key, heads.get(key) + "");
                }
            }
            // 设置编码
            List<NameValuePair> pList = new ArrayList<NameValuePair>();
            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    pList.add(new BasicNameValuePair(key, params.get(key) + ""));
                }
            }
            httppost.setEntity(new UrlEncodedFormEntity(pList, HTTP.UTF_8));
            response = httpClient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得返回的字符串
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                str = result;
                // 如果是下载的文件，可以用response.getEntity().getContent返回InputStream
            } else {
                str = JSON.toJSONString(ResultInfo.createFail("请求失败:" + response.getStatusLine().getStatusCode(), null)
                        .setResultCode(response.getStatusLine().getStatusCode() + ""));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return JSON.toJSONString(ResultInfo.createFail("请求失败:" + e.getMessage(), null).setResultCode("404"));
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
            try {
                response.getEntity().getContent().close(); // !!!IMPORTANT
            } catch (Exception e) {
            }
        }
        return str;
    }

    /**
     * post请求
     *
     * @param uri
     * @param heads
     * @param params
     * @return
     */
    public String getPostByThrow(String uri, Map<String, Object> heads,
                                 Map<String, Object> params) {
        String str = "";
        HttpResponse response = null;
        try {
            HttpPost httppost = new HttpPost(uri);
            httppost.addHeader("Accept", "text/html");
            //httppost.addHeader("Connection", "close");
            // 请求头
            if (heads != null && heads.size() > 0) {
                for (String key : heads.keySet()) {
                    httppost.addHeader(key, heads.get(key) + "");
                }
            }
            // 设置编码
            List<NameValuePair> pList = new ArrayList<NameValuePair>();
            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    pList.add(new BasicNameValuePair(key, params.get(key) + ""));
                }
            }
            httppost.setEntity(new UrlEncodedFormEntity(pList, HTTP.UTF_8));
            response = httpClient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得返回的字符串
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                str = result;
                // 如果是下载的文件，可以用response.getEntity().getContent返回InputStream
            } else {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                result = CommonUtil.parValNoErrDef(result, String.class, "");
                if (result.length() >= 200) {
                    result = result.substring(0, 199);
                }
                logger.error(response.getStatusLine().getStatusCode() + ".response.body:" + result);
                throw new ResultInfoException(response.getStatusLine().getStatusCode() + "", result);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ResultInfoException("404", e.getMessage());
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
            try {
                response.getEntity().getContent().close(); // !!!IMPORTANT
            } catch (Exception e) {
            }
        }
        return str;
    }

    /**
     * post请求
     *
     * @param uri
     * @param heads
     * @param params
     * @return
     */
    public String getPostFile(String uri, Map<String, Object> heads,
                              Map<String, Object> params) {
        String str = "";
        HttpResponse response = null;
        try {
            HttpPost httppost = new HttpPost(uri);
            httppost.addHeader("Accept", "text/html");
            // 请求头
            if (heads != null && heads.size() > 0) {
                for (String key : heads.keySet()) {
                    httppost.addHeader(key, heads.get(key) + "");
                }
            }
            MultipartEntityBuilder meBuiler = MultipartEntityBuilder.create();
            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    Object obj = params.get(key);
                    if (obj instanceof File) {
                        System.out.println("加入文件-" + key + "-" + ((File) obj).length());
                        FileBody fb = new FileBody((File) obj);
                        meBuiler.addPart(key, fb);
                    } else {
                        StringBody sb = new StringBody(CommonUtil.parValNoErrDef(obj, String.class, ""), ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8));
                        meBuiler.addPart(key, sb);
                    }
                }
            }
            HttpEntity httpEntity = meBuiler.build();
            httppost.setEntity(httpEntity);
            response = httpClient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得返回的字符串
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                str = result;
                // 如果是下载的文件，可以用response.getEntity().getContent返回InputStream
            } else {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                result = CommonUtil.parValNoErrDef(result, String.class, "");
                if (result.length() >= 50) {
                    result = result.substring(0, 49);
                }
                logger.error(response.getStatusLine().getStatusCode() + ".response.body:" + result);
                throw new ResultInfoException(response.getStatusLine().getStatusCode() + "", result);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ResultInfoException("404", e.getMessage());
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
            try {
                response.getEntity().getContent().close(); // !!!IMPORTANT
            } catch (Exception e) {
            }
        }
        return str;
    }

    /**
     * post请求
     *
     * @param uri
     * @param heads
     * @return
     */
    public String getStringPost(String uri, Map<String, Object> heads, String json) {
        String str = "";
        HttpResponse response = null;
        try {
            HttpPost httppost = new HttpPost(uri);
            httppost.addHeader("Accept", CONTENT_TYPE_TEXT_JSON);
            //httppost.addHeader("Connection", "close");
            // 请求头
            if (heads != null && heads.size() > 0) {
                for (String key : heads.keySet()) {
                    httppost.addHeader(key, heads.get(key) + "");
                }
            }
            // 设置编码
//			String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);
            StringEntity se = new StringEntity(json);
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            httppost.setEntity(se);

            response = httpClient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得返回的字符串
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                str = result;
                // 如果是下载的文件，可以用response.getEntity().getContent返回InputStream
            } else {
                str = JSON.toJSONString(ResultInfo.createFail("请求失败:" + response.getStatusLine().getStatusCode(), null)
                        .setResultCode(response.getStatusLine().getStatusCode() + ""));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return JSON.toJSONString(ResultInfo.createFail("请求失败:" + e.getMessage(), null).setResultCode("404"));
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
            try {
                response.getEntity().getContent().close(); // !!!IMPORTANT
            } catch (Exception e) {
            }
        }
        return str;
    }

    /**
     * get请求
     *
     * @param uri
     * @param heads
     * @param params
     * @return
     * @throws Exception
     */
    public String getGet(String uri, Map<String, Object> heads, Map<String, Object> params) {

        HttpGet httpBase = new HttpGet(uri);
        httpBase.addHeader("Accept", "text/html");
        httpBase.addHeader("Connection", "close");
        //请求头
        if (heads != null && heads.size() > 0) {
            for (String key : heads.keySet()) {
                httpBase.addHeader(key, heads.get(key) + "");
            }
        }

        HttpParams p = new BasicHttpParams();
        if (params != null && params.size() > 0) {
            //设置编码
            for (String key : params.keySet()) {
                p.setParameter(key, params.get(key));
            }
        }
        httpBase.setParams(p);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpBase);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得返回的字符串
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                return result;
                // 如果是下载的文件，可以用response.getEntity().getContent返回InputStream
            } else {
                String str = JSON.toJSONString(ResultInfo.createFail("请求失败:" + response.getStatusLine().getStatusCode(), null)
                        .setResultCode(response.getStatusLine().getStatusCode() + ""));
                return str;
            }
        } catch (IOException e) {
            return JSON.toJSONString(ResultInfo.createFail("请求失败:" + e.getMessage(), null));
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
            try {
                response.getEntity().getContent().close(); // !!!IMPORTANT
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }
    }

    /**
     * get请求
     *
     * @param uri
     * @param heads
     * @param params
     * @return
     * @throws Exception
     */
    public String getGetByThrow(String uri, Map<String, Object> heads, Map<String, Object> params) {

        HttpGet httpBase = new HttpGet(uri);
        httpBase.addHeader("Accept", "text/html");
        httpBase.addHeader("Connection", "close");
        //请求头
        if (heads != null && heads.size() > 0) {
            for (String key : heads.keySet()) {
                httpBase.addHeader(key, heads.get(key) + "");
            }
        }

        HttpParams p = new BasicHttpParams();
        if (params != null && params.size() > 0) {
            //设置编码
            for (String key : params.keySet()) {
                p.setParameter(key, params.get(key));
            }
        }
        httpBase.setParams(p);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpBase);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得返回的字符串
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                return result;
                // 如果是下载的文件，可以用response.getEntity().getContent返回InputStream
            } else {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                result = CommonUtil.parValNoErrDef(result, String.class, "");
                if (result.length() >= 50) {
                    result = result.substring(0, 49);
                }
                logger.error(response.getStatusLine().getStatusCode() + ".response.body:" + result);
                throw new ResultInfoException(response.getStatusLine().getStatusCode() + "", result);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ResultInfoException("404", e.getMessage());
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
            try {
                response.getEntity().getContent().close(); // !!!IMPORTANT
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }
    }

    /**
     * get请求
     *
     * @param uri
     * @param heads
     * @param params
     * @return
     * @throws Exception
     */
    public byte[] getGetByte(String uri, Map<String, Object> heads, Map<String, Object> params) {

        HttpGet httpBase = new HttpGet(uri);
        httpBase.addHeader("Accept", "text/html");
        httpBase.addHeader("Connection", "close");
        //请求头
        if (heads != null && heads.size() > 0) {
            for (String key : heads.keySet()) {
                httpBase.addHeader(key, heads.get(key) + "");
            }
        }

        HttpParams p = new BasicHttpParams();
        if (params != null && params.size() > 0) {
            //设置编码
            for (String key : params.keySet()) {
                p.setParameter(key, params.get(key));
            }
        }
        httpBase.setParams(p);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpBase);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得返回的字符串
                byte[] result = EntityUtils.toByteArray(response.getEntity());
                return result;
                // 如果是下载的文件，可以用response.getEntity().getContent返回InputStream
            } else {
                throw new ResultInfoException(response.getStatusLine().getStatusCode() + "", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            throw new ResultInfoException("ioError", e.getMessage());
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
            try {
                response.getEntity().getContent().close(); // !!!IMPORTANT
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }
    }
}
