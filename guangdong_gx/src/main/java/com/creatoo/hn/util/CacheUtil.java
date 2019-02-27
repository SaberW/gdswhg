package com.creatoo.hn.util;


import java.util.*;

/**
 * 模仿缓存
 */
public class CacheUtil {

    private static Map<String, Map<String, Object>> cacheManager = new HashMap<String, Map<String, Object>>();
    private static int expirySeconds = 3600;//默认过期秒数，3600秒

    /**
     * 获取缓存
     * @param cacheName 缓存名称
     * @param key 缓存KEY
     * @return Map<String, Object>
     */
    public static Object getCache(String cacheName, String key){
        Object obj = null;
        try {
            clearCache();
            Map<String, Object> cache = cacheManager.get(cacheName+key);
            if(null != cache){
                Date live = (Date) cache.get("live");
                Date now = new Date();
                if(live.before(now)){//缓存过期
                    cacheManager.remove(cacheName+key);
                }else{
                    obj = cache.get("cache");
                }
            }
        }catch (Exception e){ }
        return obj;
    }

    /**
     * 设置缓存
     * @param cacheName 缓存名称
     * @param key 缓存KEY
     * @param value 缓存值
     */
    public static void putCache(String cacheName, String key, Object value){
        try {
            clearCache();

            Map<String, Object> cache = null;
            if(cacheManager.containsKey(cacheName+key)){//有缓存，不修改过期时间
                cache = cacheManager.get(cacheName+key);
            }else{//无缓存，初始过期时间
                cache = new HashMap<String, Object>();
                int seconds = expirySeconds;
                Date now = new Date();
                now.setTime(now.getTime()+seconds*1000);
                cache.put("live", now);
            }
            cache.put("cache", value);
            cacheManager.put(cacheName+key, cache);
        }catch (Exception e){ }
    }

    /**
     * 设置缓存
     * @param cacheName 缓存名称
     * @param key 缓存KEY
     * @param value 缓存值
     * @param seconds 缓存时间（秒）
     */
    public static void putCache(String cacheName, String key, Object value, int seconds){
        try {
            clearCache();

            Map<String, Object> cache = null;
            if(cacheManager.containsKey(cacheName+key)) {//有缓存
                cache = cacheManager.get(cacheName+key);
            }else{//无缓存
                cache = new HashMap<String, Object>();
            }

            Date now = new Date();
            now.setTime(now.getTime()+seconds*1000);
            cache.put("live", now);
            cache.put("cache", value);
            cacheManager.put(cacheName+key, cache);
        }catch (Exception e){ }
    }

    /**
     * 删除缓存
     * @param cacheName
     * @param key
     */
    public static void delCache(String cacheName,String key){
        try {
            clearCache();
            if(cacheManager.containsKey(cacheName+key)) {//有缓存
                cacheManager.remove(cacheName + key);
            }
        }catch (Exception e){}
    }

    /**
     * 清除过期缓存
     */
    public static void clearCache(){
        try {
            List<String> keys = new ArrayList();
            for (String key : cacheManager.keySet()) {
                Map<String, Object> cache = cacheManager.get(key);
                Date live = (Date) cache.get("live");
                Date now = new Date();
                if (live.before(now)) {//缓存过期
                    keys.add(key);
                }
            }
            for (String key : keys) {
                cacheManager.remove(key);
            }
        }catch (Exception e){}
    }
}
