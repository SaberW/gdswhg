package com.creatoo.hn.util.annotation;

import com.creatoo.hn.util.enums.EnumOptType;

import java.lang.annotation.*;

/**
 * 系统操作注解
 * Created by wangxl on 2017/4/25.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WhgOPT {
    String[] optDesc();
    EnumOptType optType();

    String[] valid() default {"true"};
}
