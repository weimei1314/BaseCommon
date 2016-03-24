package com.dejionline.base.httpclient;

import com.dejionline.base.commons.constants.HttpRequestConstants;
import com.dejionline.base.commons.enums.ResponseCodeEnums;
import com.dejionline.base.commons.utils.GsonUtils;
import com.dejionline.base.exception.ServiceException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class HttpRequestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestUtils.class);

    private static PoolingHttpClientConnectionManager cm;

    private static LoadingCache<String, String> hostCache = CacheBuilder.newBuilder()
            //设置写缓存后30秒钟过期
            .expireAfterWrite(HttpRequestConstants.DNS_HOST_CACHE_EXPIRY_SECONDS, TimeUnit.SECONDS)
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
            cm.setMaxTotal(HttpRequestConstants.HTTP_CLIENT_CONNECTION_POOL_MAX);

            //每路由最大连接数，默认值是2
            cm.setDefaultMaxPerRoute(HttpRequestConstants.HTTP_CLIENT_CONNECTION_PER_ROUTE_MAX);
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
    public static <T> T postRequest(String url, int timeout, Object paramObj, Type resObjType) throws ServiceException {

        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(GsonUtils.toJson(paramObj), ContentType.APPLICATION_JSON));

        return baseRequest(httpPost, timeout, resObjType);
    }

    /**
     * 基本post请求
     *
     * @param url
     * @param timeout
     * @param param
     * @return
     * @throws ServiceException
     */
    public static String postRequestWithoutDns(String url, int timeout, String param, ContentType contentType) throws ServiceException {

        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(param, contentType));

        httpPost.setConfig(RequestConfig.custom().setSocketTimeout(timeout).build());

        return executeHttp(httpPost);
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
    public static <T> T getRequest(String url, int timeout, Type resObjType) throws ServiceException {

        return baseRequest(new HttpGet(url), timeout, resObjType);
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
    public static <T> T putRequest(String url, int timeout, Object paramObj, Type resObjType) throws ServiceException {

        HttpPut httpPut = new HttpPut(url);

        httpPut.setEntity(new StringEntity(GsonUtils.toJson(paramObj), ContentType.APPLICATION_JSON));

        return baseRequest(httpPut, timeout, resObjType);
    }

    /**
     * delete请求
     *
     * @param url
     * @param timeout
     * @param resObjType
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T deleteRequest(String url, int timeout, Type resObjType) throws ServiceException {

        return baseRequest(new HttpDelete(url), timeout, resObjType);
    }

    /**
     * http通用请求
     *
     * @param request
     * @param timeout
     * @param resObjType
     * @param <T>
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    private static <T> T baseRequest(HttpRequestBase request, int timeout, Type resObjType) throws ServiceException {

        URI uri = request.getURI();

        String getHost = uri.getHost();

        try {

            getHost = hostCache.get(uri.getHost());

        } catch (ExecutionException e) {

            LOGGER.warn(e.getMessage());
        }

        if (!StringUtils.equals(getHost, uri.getHost())) {

            try {

                request.setURI(new URI(uri.getScheme(), uri.getUserInfo(), getHost
                        , uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment()));

            } catch (URISyntaxException e) {

                LOGGER.error("get dns host error", e);

                throw new ServiceException(ResponseCodeEnums.OTHER_SERVICE_ERROR);
            }

            request.setHeader("host", uri.getHost());
        }

        request.setConfig(RequestConfig.custom().setSocketTimeout(timeout).build());

        return GsonUtils.fromJson(executeHttp(request), resObjType);
    }

    /**
     * 通过dns服务获取host地址，如果获取不到采用原有地址
     *
     * @param host
     * @return
     */
    private static String getDnsHost(String host) {

        HttpGet httpGet = new HttpGet(HttpRequestConstants.DNS_SERVICE_URL_INDEX + host);

        httpGet.setConfig(RequestConfig.custom().setSocketTimeout(HttpRequestConstants.DNS_SERVICE_TIMEOUT).build());

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
    private static String executeHttp(HttpRequestBase request) throws ServiceException {

        CloseableHttpResponse response = null;

        try {

            response = getHttpClient().execute(request);

            if (!response.getEntity().getContentType().getValue().contains("text/html")) {

                return EntityUtils.toString(response.getEntity());
            }

            throw new ServiceException(ResponseCodeEnums.OTHER_SERVICE_ERROR.getCode(), EntityUtils.toString(response.getEntity()));

        } catch (SocketTimeoutException | ConnectTimeoutException e) {

            throw new ServiceException(ResponseCodeEnums.OTHER_SERVICE_TIMEOUT);

        } catch (IOException e) {

            throw new ServiceException(ResponseCodeEnums.OTHER_SERVICE_ERROR);

        } finally {

            if (response != null) {

                try {

                    response.close();

                } catch (IOException e) {

                    LOGGER.error("close http response error");
                }
            }
        }
    }
}
