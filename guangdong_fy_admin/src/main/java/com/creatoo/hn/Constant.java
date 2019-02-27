package com.creatoo.hn;

/**
 * 系统常量
 * Created by wangxl on 2017/7/13.
 */
public class Constant {
    /**
     * 超级管理员配置
     */
    public static final String SUPER_USER_ID = "2015121200000000";//超级管理员ID
    public static final String SUPER_USER_NAME = "administrator";//超级管理员NAME
    public static final String SUPER_USER_PASSWORD = "6185c28b757544b0c907ef2986285dfd";//超级管理员MD5加密后的密码

    public static final String SESSION_ADMIN_KEY = "sessionAdminUser";//管理员对象在会话中的KEY
    public static final String SESSION_ADMIN_CULT = "sessionAdminUserCults";//管理员对象的所有权限分馆在会话中的KEY
    //public static final String SESSION_ADMIN_CULT_OBJ = "sessionAdminUserCultsObj";//管理员对象的所有权限分馆在会话中的KEY
    public static final String SESSION_ADMIN_DEPT = "sessionAdminUserDepts";//管理员对象的所有权限部门在会话中的KEY
    //public static final String SESSION_ADMIN_DEPT_OBJ = "sessionAdminUserDeptsObj";//管理员对象的所有权限部门在会话中的KEY
    public static final String SESSION_ADMIN_CULT_AND_SON = "sessionAdminUserCultsAndSon";//管理员对象的所有权限分馆和下级子馆在会话中的KEY

    public static final String ROOT_SYS_CULT_ID = "0000000000000000";//总馆ID
    public static final String ROOT_SYS_CULT_PID = "0";//总馆PID

    public static final String DEFAULT_USER_PASSWORD = "e10adc3949ba59abbe56e057f20f883e"; // 默认用户密码MD5

    /**
     * 后台分页控件默认第几页和每页行数
     */
    public static final int ADMIN_PAGGING_PAGE_NUMBER = 1;
    public static final int ADMIN_PAGGING_ROWS_NUMBER = 10;
}
