package hn.creatoo.com.web.util;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 调用接口帮助方法
 * Created by wangxl on 2017/6/6.
 */
@Component
public class RestUtils {
    /**
     * 发送POST请求
     * @param restTemplate Rest模板对象
     * @param uri 接口相对路径
     * @param params 请求对象
     * @return 返回JSON字符串
     */
    public static String post(RestTemplate restTemplate, String uri, Map<String, String> params){
        String res = "";
        try{
            String basePath = ConfigUtils.getApiConfig().getRoot();
            String url = basePath+uri;

            String spt = "?";
            if(url.indexOf("?") > -1){
                spt = "&";
            }
            if(params != null){
                for(String key: params.keySet()){
                    String val = params.get(key);
                    url += spt + key + "={"+key+"}";
                    spt = "&";
                }
            }

            res = restTemplate.postForObject(url, null, String.class, params);
        }catch (Exception e){
            Logger.getLogger(RestUtils.class).error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 发送POST请求
     * @param restTemplate Rest模板对象
     * @param uri 接口相对路径
     * @param json 请求对象
     * @return 返回JSON字符串
     */
    public static String post(RestTemplate restTemplate, String uri, String json){
        String res = "";
        try{
            String basePath = ConfigUtils.getApiConfig().getRoot();
            String url = basePath+uri;
            res = restTemplate.postForObject(url, json, String.class);
        }catch (Exception e){
            Logger.getLogger(RestUtils.class).error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 发送Get请求
     * @param restTemplate Rest模板对象
     * @param uri 接口相对路径
     * @return 返回JSON字符串
     */
    public static String get(RestTemplate restTemplate, String uri){
        String res = "";
        try{
            String basePath = ConfigUtils.getApiConfig().getRoot();
            String url = basePath+uri;
            res = restTemplate.getForObject(url, String.class);
        }catch (Exception e){
            Logger.getLogger(RestUtils.class).error(e.getMessage(), e);
        }
        return res;
    }
}