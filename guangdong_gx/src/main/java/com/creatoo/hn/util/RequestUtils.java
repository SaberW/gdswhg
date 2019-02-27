package com.creatoo.hn.util;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.model.WhgSysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 封装请求参数处理
 * Created by wangxl on 2017/7/19.
 */
public class RequestUtils {
    /**
     * 获取放在会话中管理员对象
     * @param request 请求对象
     * @return 如果会话中有admin，返回管理员对象，否则返回null
     */
    public static WhgSysUser getAdmin(HttpServletRequest request){
        if(request != null){
            return (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        }
        return null;
    }

    /**
     * 获取请求第几页参数
     * @param request 请求对象
     * @return 如果请求中没有page参数, 返回Constant.ADMIN_PAGGING_PAGE_NUMBER指定的值，否则返回请求的page值
     * @see Constant
     */
    public static Integer getPageParameter(HttpServletRequest request){
        Integer page = RequestUtils.getIntParameter(request, "page");
        if(page == null){
            return Constant.ADMIN_PAGGING_PAGE_NUMBER;
        }
        return page;
    }

    /**
     * 获取请求每页行数参数
     * @param request 请求对象
     * @return 如果请求中没有rows参数, 返回Constant.ADMIN_PAGGING_ROWS_NUMBER指定的值，否则返回请求的rows值
     * @see Constant
     */
    public static Integer getRowsParameter(HttpServletRequest request){
        Integer page = RequestUtils.getIntParameter(request, "rows");
        if(page == null){
            return Constant.ADMIN_PAGGING_ROWS_NUMBER;
        }
        return page;
    }

    /**
     * 获取请求参数的INT类型值
     * @param request 请求对象
     * @param name 请求参数
     * @return 请求参数的INT类型值
     */
    public static Integer getIntParameter(HttpServletRequest request, String name){
        Integer intVar = null;
        try {
            String str = RequestUtils.getParameter(request, name);
            if (str != null) {
                intVar = Integer.parseInt(str);
            }
        }catch (Exception e){}
        return intVar;
    }

    /**
     * 获取请求参数的值
     * @param request 请求对象
     * @param name 请求参数
     * @return 请求参数的值
     */
    public static String getParameter(HttpServletRequest request, String name){
        String str = null;
        try{
            if(request != null){
                str = request.getParameter(name);
            }
        }catch(Exception e){}
        return str;
    }
}
