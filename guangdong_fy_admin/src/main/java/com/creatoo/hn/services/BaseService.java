package com.creatoo.hn.services;

import com.creatoo.hn.dao.model.WhgSysDept;
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
@Transactional
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

   /* //权限分馆
    private List<Map<String, String>> PMSCULT = null;
    public void setPmsCult(List<Map<String, String>> cultList){
        this.PMSCULT = cultList;
    }
    public List<Map<String, String>> getPmsCult(){
        return PMSCULT;
    }

    //权限部门
    private Map<String, List<WhgSysDept>> PMSDEPT = null;
    public void setPmsDept(Map<String, List<WhgSysDept>> deptMap){
        this.PMSDEPT = deptMap;
    }
    public Map<String, List<WhgSysDept>> getPmsDept(){
        return PMSDEPT;
    }

    *//**
     * 单表添加过滤文化馆和部门条件
     * @param example 已有条件
     * @throws Exception
     *//*
    public Example filterCultDept(Example example)throws Exception{





        List<Example.Criteria> cs = example.getOredCriteria();
        for(Example.Criteria crit : cs){
            crit.and
        }

        return null;
    }

    *//**
     * 多表查询使用SQL时过滤文化馆和部门
     * @param tableAlias 表别名
     * @return
     * @throws Exception
     *//*
    public String getFilterCultDept(String tableAlias)throws Exception{

        List<String> cultList = new ArrayList<>();
        Map<String, List<String>> cultDeptList = new HashMap<>();
        return null;
    }*/
}
