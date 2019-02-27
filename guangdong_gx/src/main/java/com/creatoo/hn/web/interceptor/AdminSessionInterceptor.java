package com.creatoo.hn.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgSysUserMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiNote;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiNoteService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.util.enums.EnumState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制台会话拦截
 * Created by wangxl on 2017/7/13.
 */
@SuppressWarnings("ALL")
public class AdminSessionInterceptor implements HandlerInterceptor {
    /**
     * 日志配置
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WhgSysUserMapper whgSysUserMapper;

    @Autowired
    private WhgYunweiNoteService whgYunweiNoteService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            //管理员是否有会话
            Object sessionObject = request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            if (sessionObject == null) {
                response.sendRedirect(request.getServletContext().getAttribute("basePath") + "/admin/login");
                return false;
            } else {
                WhgSysUser sysUser = (WhgSysUser) sessionObject;
                if (!"administrator".equals(sysUser.getAccount())) {//有会话，不是超级管理员时，判断管理员是否已经停用
                    sysUser = whgSysUserMapper.selectByPrimaryKey(sysUser.getId());
                    if (sysUser == null || EnumState.STATE_NO.getValue() == sysUser.getState().intValue()) {
                        response.sendRedirect(request.getServletContext().getAttribute("basePath") + "/admin/login");
                        return false;
                    }
                }
            }

            //操作日志管理
            try {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                writeOptNode(request, response, handlerMethod);
            }catch(Exception e){
                log.error(e.getMessage(), e);
            }
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
     * 记录操作日志
     * @param request 请求对象
     * @param response 响应对象
     * @param handler controller方法
     */
    private void writeOptNode(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler){
        try{
            //从会话中获取操作用户
            WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            Method method =  handler.getMethod();
            if(sysUser != null && method != null) {
                String adminId = sysUser.getId();//管理员ID
                String adminAccount = sysUser.getAccount();//管理员账号

                //操作注解
                WhgOPT whgOPT = (WhgOPT) method.getAnnotation(WhgOPT.class);
                RequestMapping requestMapping1 = (RequestMapping)method.getDeclaringClass().getAnnotation(RequestMapping.class);
                RequestMapping requestMapping2 = (RequestMapping)method.getAnnotation(RequestMapping.class);
                String curtURI = request.getRequestURI();
                parseURL(request, curtURI, requestMapping2, requestMapping1);

                if (whgOPT != null) {
                    //whgOPT注释
                    EnumOptType type = whgOPT.optType();
                    String[] opts = whgOPT.optDesc();
                    String[] valid = whgOPT.valid();

                    //获操作对象和操作说明
                    int optType = type.getValue();
                    String optDesc = "";
                    if (opts.length == 1) {
                        optDesc = opts[0];
                    } else if (opts.length > 1) {
                        for (int i = 0; i < opts.length; i++) {
                            if (valid.length >= i) {
                                optDesc = valid(valid[i], opts[i], request);
                                if (!"".equals(optDesc)) {
                                    break;
                                }
                            }
                        }
                    }

                    if (!"".equals(optDesc)) {//根据注解找到操作说明
                        //请求参数
                        Map<String, String> allProp = new HashMap<String, String>();
                        Enumeration<String> enums = request.getParameterNames();
                        int count = 2;
                        while (enums.hasMoreElements()) {
                            String pkey = enums.nextElement();
                            String pval = request.getParameter(pkey);
                            if (pval.length() > 100) {
                                pval = pval.substring(0, 100) + "...";
                            }
                            //控制总长度要小于1000
                            int curtLen = pkey.length() + pval.length() + 6;
                            if ((count + curtLen) < 1020) {
                                count = count + curtLen;
                                allProp.put(pkey, pval);
                            }
                        }
                        String optargs = JSON.toJSON(allProp).toString();

                        //System.out.println( "操作对象："+type.getName()+"    操作说明:"+optDesc +"        请求参数："+JSON.toJSON(allProp).toString());

                        //保存操作日志
                        try {
                            WhgYwiNote note = new WhgYwiNote();
                            note.setAdminid(sysUser.getId());
                            note.setAdminaccount(sysUser.getAccount());
                            note.setOpttype(optType);
                            note.setOptdesc(optDesc);
                            note.setOpttime(new Date());
                            note.setOptargs(optargs);
                            note.setCultid(sysUser.getCultid());
//                            this.videoService.saveOptNote(note);
                            this.whgYunweiNoteService.t_saveNote(note);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据表达式获取操作说明
     * @param el 表达式
     * @param optDescArg 操作说明
     * @param request 请求对象
     * @return 操作说明,如果不满足el时，返回空字符串, 否则返回optDescArg
     */
    private String valid(String el, String optDescArg, HttpServletRequest request){
        String optDesc = "";

        if(el != null && !el.isEmpty()){
            if("true".equals(el.trim())){
                optDesc = optDescArg;
            }else{
                String[] arr = el.split("&&");//key1=val1,val2,val3&&key2=val4,val5&&

                boolean is = true;
                for(String _elc : arr){
                    String[] parr = _elc.split("=");
                    String pkey = parr[0];//属性名
                    String pval = parr[1];//属性值

                    //属性值真实值
                    String propVal =  request.getParameter(pkey);
                    if(propVal == null){
                        Object object = request.getAttribute("whg_"+pkey);
                        if(object != null){
                            propVal = (String) object;
                        }
                    }

                    //是否满足条件
                    boolean isc = false;
                    String[] pvals = pval.split(",");
                    for(String pvalsc : pvals){
                        if("null".equalsIgnoreCase(pvalsc.trim()) && (propVal == null || propVal.isEmpty())){
                            isc = true;
                        }else if("notnull".equalsIgnoreCase(pvalsc.trim()) && propVal != null){
                            isc = true;
                        }else if(pvalsc != null && propVal != null && pvalsc.trim().equals(propVal.trim()) ){
                            isc = true;
                        }
                    }

                    //是否全部满足条件
                    is = is && isc;
                }

                //是否满足EL
                if(is){
                    optDesc = optDescArg;
                }
            }
        }else{
            optDesc = optDescArg;
        }

        return optDesc;
    }

    /**
     * 请求连接是否匹配
     * @param uri1 配置好的映射URI
     * @param uri2 真实的URI
     * @return true-匹配， false-不匹配
     */
    private boolean eq(String uri1, String uri2, HttpServletRequest request){
        boolean is = false;

        if(uri1 != null && uri1.equals(uri2)){
            is = true;
        }else if(uri1 != null && uri2 != null){
            String[] uri1s = uri1.split("/");
            String[] uri2s = uri2.split("/");

            if(uri1s.length == uri2s.length){
                boolean isok = true;
                for(int i=0; i<uri1s.length; i++){
                    String _u1s = uri1s[i];
                    String _u2s = uri2s[i];

                    if(!_u1s.equals(_u2s) && !_u1s.startsWith("{") ){
                        isok = false;
                        break;
                    }

                    if(_u1s.startsWith("{") && _u1s.endsWith("}")){
                        String pkey = _u1s.substring(1,_u1s.length()-1);
                        request.setAttribute("whg_"+pkey,_u2s);
                    }
                }
                is = isok;
            }
        }

        return is;
    }

    /**
     * 将请求URI( 映射路径：/abc/def/{type} -> 实际路径：/abc/def/123 )中的参数添加whg_前缀，设置到request属性中
     * 可通过request.getAttribute("whg_type")获取
     * @param request 请求对象
     * @param curtURI 当前请求URI
     * @param requestMapping_method 控制器方法的requestMapping注解
     * @param requestMapping_clazz 控制器的requestMapping注解
     */
    private void parseURL(HttpServletRequest request, String curtURI, RequestMapping requestMapping_method, RequestMapping requestMapping_clazz){
        if(requestMapping_method != null && requestMapping_method.value().length > 0){
            String uri_mapping = requestMapping_method.value()[0];
            if(requestMapping_clazz != null && requestMapping_clazz.value().length > 0){
                uri_mapping = requestMapping_clazz.value()[0]+uri_mapping;
            }

            String[] uri_crt = curtURI.split("/");
            String[] uri_map = uri_mapping.split("/");
            if(uri_crt.length == uri_map.length){
                for(int i=0; i<uri_crt.length; i++){
                    String _u1s = uri_crt[i];
                    String _u2s = uri_map[i];

                    if(_u2s.startsWith("{") && _u2s.endsWith("}")){
                        String pkey = _u2s.substring(1,_u2s.length()-1);
                        request.setAttribute("whg_"+pkey,_u1s);
                    }
                }
            }
        }
    }
}
