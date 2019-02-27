package com.creatoo.hn.controller.admin;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.login.LoginService;
import com.creatoo.hn.services.admin.login.MenusService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 站点注册
 * Created by wangxl on 2017/7/13.
 */
@RestController
@RequestMapping("/cultregist")
public class CultRegistController extends BaseController {
    /**
     * 文化馆服务类
     */
    @Autowired
    private WhgSystemCultService service;

    /**
     * 登记站点资料
     * @return 登录成功的视图
     * @throws Exception
     */
    @RequestMapping(value = "/doReqistZd", method =RequestMethod.GET)
    @WhgOPT(optType = EnumOptType.CONSOLE_LOGIN, optDesc = "登记站点资料")
    public ModelAndView doReqistZd(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)throws Exception{
        ModelAndView view = new ModelAndView();
        view.setViewName("admin/registZd");
        String basePath = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
        if (80 == httpServletRequest.getServerPort()) {
            basePath = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + httpServletRequest.getContextPath();
        }
        view.addObject("basePath",basePath);
        Object baseImgPath = httpServletRequest.getServletContext().getAttribute("baseImgPath");
        if (baseImgPath == null) {
            baseImgPath = env.getProperty("upload.local.server.addr");;
        }
        view.addObject("baseImgPath",baseImgPath);
        return view;
    }

    /**
     * 添加
     * @param request 请求对象
     * @param cult 添加的资料
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    @WhgOPT(optType = EnumOptType.CULT, optDesc = {"添加"})
    public ResponseBean add(HttpServletRequest request, WhgSysCult cult){
        ResponseBean res = new ResponseBean();
        String province=cult.getProvince()!=null&&!cult.getProvince().equals("")?cult.getProvince():null;
        String city=cult.getCity()!=null&&!cult.getCity().equals("")?cult.getCity():null;
        String area=cult.getArea()!=null&&!cult.getArea().equals("")?cult.getArea():null;
        try {
            WhgSysCult sysCult= service.findCultByCondition(province,city,area);
            if(sysCult!=null){//查找是否有同样数据
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("您所提交的文化馆已存在数据，请与管理员联系");
            }else {
                WhgSysCult sysCult2 = service.findCultByCondition(province,city, null);
                if (sysCult2 != null) {//是否存在父级 文化馆
                    cult.setPid(sysCult2.getId());//设置父站点
                    cult.setSqstate(1);//申请状态 待申请
                    cult.setState(0);//启动状态  待启动
                    service.t_add(cult, null);
                } else {
                    if(area!=null){ //没有父馆 区域有值  说明不存在父馆
                        res.setSuccess(ResponseBean.FAIL);
                        res.setErrormsg("您所提交的上级文化馆暂不存在，请与管理员联系");
                    }else{ //区域没值 说明 申请的是 市馆：只要不重复 都可以申请 父馆 默认是 广东省文化馆
                        cult.setPid("0000000000000000");//设置父站点
                        cult.setArea(null);
                        cult.setSqstate(1);//申请状态 待申请
                        cult.setState(0);//启动状态  待启动
                        service.t_add(cult, null);
                    }
                }
            }
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }
}
