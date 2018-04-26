package com.jy.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zy
 * @Date: Created in 2018/4/26
 */
@Component
public class FacadeUtils extends BaseFacadeUtils {
    @Autowired
    private Environment env;

    @Override
    @PostConstruct
    /** 项目启动时，从配置文件、或者数据库获取 */
    public void initParam() {
        this.USERNAME = env.getProperty("username");
        this.PASSWORD = env.getProperty("password");
        this.AUTHORIZATION = env.getProperty("authorization");
    }

    @Override
    /**
     * 从缓存中获取token
     * @return
     */
    public String getTokenCache() {
        return null;
    }

    @Override
    /**
     * 更新缓存中的token
     * @return
     */
    public void updateTokenCache() {

    }

    public static void main(String args[]){
        FacadeUtils facadeUtils = new FacadeUtils();
        facadeUtils.USERNAME = "easyepc";
        facadeUtils.PASSWORD = "easyepc2018";
        facadeUtils.AUTHORIZATION = "YW5kcm9pZDphbmRyb2lk";
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("letter", "a");
        try {
            JSONObject jsonObject = facadeUtils.doGet("http://192.168.80.52:8765/","vehicle-service/brands", querys);
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
