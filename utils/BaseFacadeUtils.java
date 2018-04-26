package com.jy.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zy
 * @Description: facade接口工具
 * @Version: 1.0.0
 * @Date: Created in 2018/1/4
 */
public abstract class BaseFacadeUtils {

    /**用户名*/
    protected static String USERNAME;
    /**密码*/
    protected static String PASSWORD;
    /**认证秘钥*/
    protected static String AUTHORIZATION;

    /**
     * 初始化参数
     * @return
     */
    public abstract void initParam();

    /**
     * 从缓存中获取token
     * @return
     */
    public abstract String getTokenCache();

    /**
     * 更新缓存中的token
     * @return
     */
    public abstract void updateTokenCache();

    /**
     * 通过用户名密码获取token
     * @param url
     * @return
     * @throws Exception
     */
    protected String getTokenHttp(String url) throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic " + AUTHORIZATION);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("grant_type", "password");
        querys.put("username", USERNAME);
        querys.put("password", PASSWORD);
        HttpResponse response = HttpUtils.doPost(url, "/auth-service/oauth/token", headers, querys, "");
        JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
        String accessToken = (String) jsonObject.get("access_token");
//        int expiration = (Integer) jsonObject.get("expires_in");
        return accessToken;
    }
    /**
     * @param url   url地址
     * @param path    资源路径
     * @param querys    参数
     * @return
     * @throws Exception
     */
    public JSONObject doGet(String url, String path,
                        Map<String, String> querys) throws Exception {

        String token = getTokenCache();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("access_token", token);
        HttpResponse response = HttpUtils.doGet(url, path, headers, querys);
        JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));

        if("401".equals(jsonObject.get("status"))){
            String access_token = getTokenHttp(url);
            updateTokenCache();
            headers.put("access_token", access_token);
            response = HttpUtils.doGet(url, path, headers, querys);
            jsonObject = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
        }
        return jsonObject;
    }
}
