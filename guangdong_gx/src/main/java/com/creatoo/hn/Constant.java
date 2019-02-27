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

    public static final String ROOT_SYS_CULT_ID = "0000000000000000";//总站ID
    public static final String ROOT_SYS_CULT_PID = "0";//总馆PID

    public static final String DEFAULT_USER_PASSWORD = "e10adc3949ba59abbe56e057f20f883e"; // 默认用户密码MD5

    /**
     * 后台分页控件默认第几页和每页行数
     */
    public static final int ADMIN_PAGGING_PAGE_NUMBER = 1;
    public static final int ADMIN_PAGGING_ROWS_NUMBER = 10;


    /**-------------------------邮件发送参数-------------------------------------*/
    /** SMTP服务器 */
    public static final String EMAIL_SMTPSERVER = "smtp.qq.com"; //getSysProperty("EMAIL_SMTPSERVER", "smtp.qq.com");//SMTP服务器

    /** SMTP服务器端口 */
    public static final String EMAIL_SMTPSERVER_PORT = "465";//getSysProperty("EMAIL_SMTPSERVER_PORT", "465");

    /**  邮件服务器账号 */
    public static final String EMAIL_USERNAME = "36762196@qq.com";//getSysProperty("EMAIL_USERNAME", "36762196@qq.com");

    /** 邮件服务器密码 */
    public static final String EMAIL_USERPWD = "wjehphzceqdzbjde"; //getSysProperty("EMAIL_USERPWD", "wjehphzceqdzbjde");

    /** 发送邮件的邮件地址 */
    public static final String EMAIL_SENDER = "36762196@qq.com"; //getSysProperty("EMAIL_SENDER", "36762196@qq.com");//邮箱发送人用户名

    /** 发送邮件的邮件地址的昵称 */
    public static final String EMAIL_SENDERNAME = "创图"; //getSysProperty("EMAIL_SENDERNAME", "创图");//邮箱发送人姓名

    /** 注册验证码邮件主题 */
    public static final String EMAIL_REG_SUBJECT ="湖南创图网络信息发展有限公司"; //getSysProperty("EMAIL_REG_SUBJECT", "湖南创图网络信息发展有限公司");//邮件标题

    /** 用户邮箱通知的模板 */
    public static final String EMAIL_CONTENT_TPL = "moban"; //getSysProperty("EMAIL_CONTENT_TPL", "moban");

}
