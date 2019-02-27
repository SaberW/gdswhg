package com.creatoo.hn.web.config;

import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.util.IDUtils;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存KEY默认生成规则
 * Created by wangxl on 2017/7/17.
 */
@Component("simpleKeyGenerator")
public class CacheKeyGenConfig implements KeyGenerator{
    /**
     * 日志配置
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 取配置信息, 配置文件:application[-][test|dev].properties
     * 如：env.getProperty("spring.datasource.password")
     */
    @Autowired
    protected Environment env;

    // custom cache key
    public static final int NO_PARAM_KEY = 0;
    public static final int NULL_PARAM_KEY = 53;

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(target.getClass().getSimpleName()).append(".").append(method.getName()).append(":");
        if (params.length == 0) {
            return key.append(NO_PARAM_KEY).toString();
        }
        for (Object param : params) {
            if (param == null) {
                key.append(NULL_PARAM_KEY);
            } else if (ClassUtils.isPrimitiveArray(param.getClass())) {//数组
                int length = Array.getLength(param);
                for (int i = 0; i < length; i++) {
                    key.append(Array.get(param, i));
                    key.append(',');
                }
            } else if (ClassUtils.isPrimitiveOrWrapper(param.getClass()) || param instanceof String) {//基础类或者包装器类
                key.append(param);
            } else {//对象
                //String className = ClassUtils.classNamesToString(param.getClass());
                String modelPackage = env.getProperty("mybatis.type-aliases-package");
                String paramPackage = ClassUtils.getPackageName(param.getClass());
                if(paramPackage.startsWith(modelPackage)){
                    key.append(getModelKey(param));
                }else{
                    key.append(param.hashCode());
                }
            }
            key.append('-');
        }

        String finalKey = key.toString();
        long cacheKeyHash = Hashing.murmur3_128().hashString(finalKey, Charset.defaultCharset()).asLong();
        log.debug("using cache key={} hashCode={}", finalKey, cacheKeyHash);
        return key.toString();
    }

    /**
     * BEAN根据属性生成的KEY值
     * @param model 只能是com.creatoo.hn.dao.model下面的类
     * @return BEAN根据属性生成的KEY值
     */
    public String getModelKey(Object model){
        StringBuffer sb = new StringBuffer();
        try {
            PropertyDescriptor[] props = Introspector.getBeanInfo(model.getClass(), Object.class).getPropertyDescriptors();
            if (props != null) {
                for (int i = 0; i < props.length; i++) {
                    try {
                        String aa = props[i].getName();//获取bean中的属性
                        //Object object = props[i].getPropertyType();//获取属性的类型
                        Object val = props[i].getReadMethod().invoke(model);
                        if(val != null){
                            if(sb.length() > 0){
                                sb.append(",");
                            }
                            sb.append(aa).append("=").append(val.hashCode());
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            sb = new StringBuffer(model.hashCode());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        WhgSysCult cult = new WhgSysCult();
        cult.setId(IDUtils.getID());
        cult.setName("wangxl");
        cult.setCrtdate(new Date());
        StringBuffer sb = new StringBuffer();
        PropertyDescriptor[] props = null;
        try {
            props = Introspector.getBeanInfo(cult.getClass(), Object.class).getPropertyDescriptors();
        } catch (IntrospectionException e) {
        }
        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                try {
                    String aa = props[i].getName();//获取bean中的属性
                    Object object = props[i].getPropertyType();//获取属性的类型
                    Object val = props[i].getReadMethod().invoke(cult);
                    if(val != null){
                        if(sb.length() > 0){
                            sb.append(",");
                        }
                        sb.append(aa).append("=").append(val.hashCode());
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}
