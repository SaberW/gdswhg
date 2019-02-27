package com.creatoo.hn.services;

import com.creatoo.hn.dao.model.WhgSysDept;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 服务类的超类
 * Created by wangxl on 2017/7/13.
 */
@Transactional(rollbackFor={Exception.class, Throwable.class})
public class BaseService {
    /**
     * 日志配置
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 取配置信息, 配置文件:application[-][test|dev].properties
     * 如：env.getProperty("spring.datasource.password")
     */
    @Autowired
    public Environment env;

    /**
     * 缓存管理器
     */
    @Autowired
    protected GuavaCacheManager cacheManager;

    /**
     * 登录缓存
     */
    @Autowired
    private MemoryConstrainedCacheManager memoryConstrainedCacheManager;

    /**
     * 清除登录缓存
     */
    protected void clearLoginCache(){
        try {
            memoryConstrainedCacheManager.getCache("shiro_redis_cache").clear();
            memoryConstrainedCacheManager.getCache("com.creatoo.hn.web.config.ShiroConfigShiroRealm.authorizationCache").clear();
        }catch (Exception e){}
    }

    /**
     * 添加缓存
     * @param cacheName 缓存对象名称
     * @param key 缓存对象中的KEY
     * @param obj 缓存实际对象
     */
    public void addCache(String cacheName, String key, Object obj){
        if(this.cacheManager.getCache(cacheName) == null){
            Collection<String> cacheNames = this.cacheManager.getCacheNames();
            cacheNames.add(cacheName);
            this.cacheManager.setCacheNames(cacheNames);
        }
        Cache cache = this.cacheManager.getCache(cacheName);
        cache.put(key, obj);
    }

    /**
     * 删除缓存
     * @param cacheName 缓存对象名称
     * @param key 缓存对象中的KEY
     */
    public void delCache(String cacheName, String key){
        if(this.cacheManager.getCache(cacheName) != null){
            Cache cache = this.cacheManager.getCache(cacheName);
            if(cache != null) {
                cache.evict(key);
            }
        }
    }

    /**
     * 清除所有缓存
     * @param cacheName 缓存对象名称
     */
    public void delCache(String cacheName){
        if(this.cacheManager.getCache(cacheName) != null){
            Cache cache = this.cacheManager.getCache(cacheName);
            if(cache != null) {
                cache.clear();
            }
        }
    }

    /**
     * 删除缓存
     * @param cacheName 缓存对象名称
     * @param key 缓存对象中的KEY
     * @return 缓存实际对象
     */
    public Object getCache(String cacheName, String key){
        Object obj = null;
        if(this.cacheManager.getCache(cacheName) != null){
            Cache cache = this.cacheManager.getCache(cacheName);
            if(cache != null && cache.get(key) != null){
                obj = cache.get(key).get();
            }
        }
        return obj;
    }

    /**
     * 设置排序
     * @param example
     * @param sort
     * @param order
     * @param defaultStr
     * @throws Exception
     */
    public void setOrder(Example example, String sort, String order, String defaultStr)throws Exception{
        if(StringUtils.isNotEmpty(sort)){
            StringBuffer sb = new StringBuffer(sort);
            if(StringUtils.isNotEmpty(order) && "desc".equalsIgnoreCase(order)){
                sb.append(" ").append(order);
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause(defaultStr);
        }
    }
}
