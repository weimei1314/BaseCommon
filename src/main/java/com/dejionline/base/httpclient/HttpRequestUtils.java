package com.dejionline.base.httpclient;

import com.dejionline.base.commons.constants.ResponseCodeConstants;
import com.dejionline.base.commons.utils.GsonUtils;
import com.dejionline.base.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;

/**
 * http访问工具
 * Created by ShengyangKong
 * on 2016/2/1.
 */
@Slf4j
public class HttpRequestUtils {

    private static final int HTTP_CLIENT_CONNECTION_POOL_MAX = 1024;

    private static final int HTTP_CLIENT_CONNECTION_PER_ROUTE_MAX = 512;

    private static final int GET_CONNECT_TIME_OUT = 1000;

    private static final int CONNECT_TIME_OUT = 1000;

    private static final int DEFAULT_TIME_OUT = 3000;

    private static PoolingHttpClientConnectionManager cm;

    // 工具类不允许实例化
    private HttpRequestUtils() {
    }

    private static void init() {

        if (cm == null) {

            cm = new PoolingHttpClientConnectionManager();

            //整个连接池最大连接数
            cm.setMaxTotal(HTTP_CLIENT_CONNECTION_POOL_MAX);

            //每路由最大连接数，默认值是2
            cm.setDefaultMaxPerRoute(HTTP_CLIENT_CONNECTION_PER_ROUTE_MAX);
        }
    }

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {

        init();

        return HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * post请求，入参gson序列化
     *
     * @param url
     * @param timeout
     * @param paramObj
     * @param resObjType
     * @param <T>
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static <T> T postRequest(String url, int timeout, Object paramObj, Type resObjType) {

        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(GsonUtils.toJson(paramObj), ContentType.APPLICATION_JSON));

        return baseRequest(httpPost, timeout, resObjType);
    }

    public static <T> T postRequest(String url, int timeout, Object paramObj, Class<T> classOfT) {

        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(GsonUtils.toJson(paramObj), ContentType.APPLICATION_JSON));

        return baseRequest(httpPost, timeout, classOfT);
    }

    public static <T> T postRequest(String url, Object paramObj, Type resObjType) {
        return postRequest(url, DEFAULT_TIME_OUT, paramObj, resObjType);
    }

    public static <T> T postRequest(String url, Object paramObj, Class<T> classOfT) {
        return postRequest(url, DEFAULT_TIME_OUT, paramObj, classOfT);
    }

    /**
     * get请求
     *
     * @param url
     * @param timeout
     * @param resObjType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T getRequest(String url, int timeout, Type resObjType) {
        return baseRequest(new HttpGet(url), timeout, resObjType);
    }

    public static <T> T getRequest(String url, Type resObjType) {
        return getRequest(url, DEFAULT_TIME_OUT, resObjType);
    }

    public static <T> T getRequest(String url, int timeout, Class<T> classOfT) {
        return baseRequest(new HttpGet(url), timeout, classOfT);
    }

    public static <T> T getRequest(String url, Class<T> classOfT) {
        return getRequest(url, DEFAULT_TIME_OUT, classOfT);
    }

    /**
     * del请求
     *
     * @param url
     * @param timeout
     * @param resObjType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T deleteRequest(String url, int timeout, Type resObjType) {
        return baseRequest(new HttpDelete(url), timeout, resObjType);
    }

    public static <T> T deleteRequest(String url, Type resObjType) {
        return deleteRequest(url, DEFAULT_TIME_OUT, resObjType);
    }

    public static <T> T deleteRequest(String url, int timeout, Class<T> classOfT) {
        return baseRequest(new HttpDelete(url), timeout, classOfT);
    }

    public static <T> T deleteRequest(String url, Class<T> classOfT) {
        return deleteRequest(url, DEFAULT_TIME_OUT, classOfT);
    }

    /**
     * put请求
     *
     * @param url
     * @param timeout
     * @param resObjType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T putRequest(String url, int timeout, Type resObjType) {
        return baseRequest(new HttpPut(url), timeout, resObjType);
    }

    public static <T> T putRequest(String url, Type resObjType) {
        return putRequest(url, DEFAULT_TIME_OUT, resObjType);
    }

    public static <T> T putRequest(String url, int timeout, Class<T> classOfT) {
        return baseRequest(new HttpPut(url), timeout, classOfT);
    }

    public static <T> T putRequest(String url, Class<T> classOfT) {
        return putRequest(url, DEFAULT_TIME_OUT, classOfT);
    }


    /**
     * 基本请求
     *
     * @param request
     * @param timeout
     * @return
     * @throws ServiceException
     */
    public static String baseRequest(HttpRequestBase request, int timeout) {

        request.setConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(GET_CONNECT_TIME_OUT)
                .setConnectTimeout(CONNECT_TIME_OUT)
                .setSocketTimeout(timeout).build());

        return executeHttp(request);
    }

    public static <T> T baseRequest(HttpRequestBase request, int timeout, Type resObjType) {
        return GsonUtils.fromJson(baseRequest(request, timeout), resObjType);
    }

    public static <T> T baseRequest(HttpRequestBase request, int timeout, Class<T> classOfT) {
        return GsonUtils.fromJson(baseRequest(request, timeout), classOfT);
    }

    public static String baseRequest(HttpRequestBase request) {
        return baseRequest(request, DEFAULT_TIME_OUT);
    }

    /**
     * 执行http请求
     *
     * @param request
     * @return
     * @throws IOException
     */
    private static String executeHttp(HttpRequestBase request) {

        CloseableHttpResponse response = null;

        try {

            long start = System.currentTimeMillis();

            log.info("requestUrl:{}", request.getURI().getPath());

            response = getHttpClient().execute(request);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {

                log.error("http response status code : " + response.getStatusLine().getStatusCode());

                throw new ServiceException(ResponseCodeConstants.HTTP_RESPONSE_ERROR);
            }

            String resStr = EntityUtils.toString(response.getEntity());

            log.info("requestUrl:{}|cost:{}", request.getURI().getPath(), System.currentTimeMillis() - start);

            return resStr;

        } catch (ConnectionPoolTimeoutException e) {

            throw new ServiceException(ResponseCodeConstants.GET_HTTPCLIENT_POOL_TIMEOUT);

        } catch (SocketTimeoutException | ConnectTimeoutException e) {

            throw new ServiceException(ResponseCodeConstants.OTHER_SERVICE_TIMEOUT);

        } catch (IOException e) {

            throw new ServiceException(ResponseCodeConstants.OTHER_SERVICE_ERROR);

        } finally {

            if (response != null) {

                try {

                    response.close();

                } catch (IOException e) {

                    log.error("close http response error");
                }
            }
        }
    }
}
