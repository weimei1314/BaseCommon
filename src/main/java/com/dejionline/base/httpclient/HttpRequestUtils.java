package com.dejionline.base.httpclient;

import com.dejionline.base.commons.constants.ResponseCodeConstants;
import com.dejionline.base.commons.utils.GsonUtils;
import com.dejionline.base.exception.ServiceException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * http访问工具
 * Created by ShengyangKong
 * on 2016/2/1.
 */
@Slf4j
public class HttpRequestUtils {

    private static final String DNS_SERVICE_URL_INDEX = "http://httpdns.djdev.com/d?dn=";

    private static final int DNS_SERVICE_TIMEOUT = 2000;

    private static final int DNS_HOST_CACHE_EXPIRY_SECONDS = 30;

    private static final int HTTP_CLIENT_CONNECTION_POOL_MAX = 1024;

    private static final int HTTP_CLIENT_CONNECTION_PER_ROUTE_MAX = 512;

    private static final int GET_CONNECT_TIME_OUT = 1000;

    private static final int CONNECT_TIME_OUT = 1000;

    private static final int DEFAULT_TIME_OUT = 3000;

    private static PoolingHttpClientConnectionManager cm;

    private static LoadingCache<String, String> hostCache = CacheBuilder.newBuilder()
            //设置写缓存后30秒钟过期
            .expireAfterWrite(DNS_HOST_CACHE_EXPIRY_SECONDS, TimeUnit.SECONDS)
            //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
            .build(
                    new CacheLoader<String, String>() {

                        @Override
                        public String load(String host) {
                            return getDnsHost(host);
                        }
                    }
            );

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
     * 基本请求
     *
     * @param request
     * @param timeout
     * @return
     * @throws ServiceException
     */
    public static String baseRequest(HttpRequestBase request, int timeout) {

        URI uri = request.getURI();

        String getHost = uri.getHost();

        try {

            getHost = hostCache.get(uri.getHost());

        } catch (ExecutionException e) {

            log.warn(e.getMessage());
        }

        if (!StringUtils.equals(getHost, uri.getHost())) {

            try {

                request.setURI(new URI(uri.getScheme(), uri.getUserInfo(), getHost
                        , uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment()));

            } catch (URISyntaxException e) {

                log.error("get dns host error", e);

                throw new ServiceException(ResponseCodeConstants.OTHER_SERVICE_ERROR);
            }

            request.addHeader("host", uri.getHost());
        }

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

    public static <T> T baseRequest(HttpRequestBase request, Type resObjType) {
        return GsonUtils.fromJson(baseRequest(request, DEFAULT_TIME_OUT), resObjType);
    }

    public static <T> T baseRequest(HttpRequestBase request, Class<T> classOfT) {
        return GsonUtils.fromJson(baseRequest(request, DEFAULT_TIME_OUT), classOfT);
    }

    /**
     * 通过dns服务获取host地址，如果获取不到采用原有地址
     *
     * @param host
     * @return
     */
    private static String getDnsHost(String host) {

        HttpGet httpGet = new HttpGet(DNS_SERVICE_URL_INDEX + host);

        httpGet.setConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(GET_CONNECT_TIME_OUT)
                .setConnectTimeout(CONNECT_TIME_OUT)
                .setSocketTimeout(DNS_SERVICE_TIMEOUT)
                .build());

        List<String> hostList;

        try {

            hostList = GsonUtils.fromJson(executeHttp(httpGet), new TypeToken<List<String>>() {
            }.getType());

        } catch (Exception e) {

            return host;
        }

        return (hostList == null || hostList.size() == 0) ? host : hostList.get(0);
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
