package com.creatoo.hn.controller.comm;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiKeyService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTagService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTypeService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiWhppService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.creatoo.hn.util.enums.EnumWbMenu;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类
 * Created by wangxl on 2017/3/17.
 */
@RestController
@RequestMapping("/comm")
public class CommTypeController extends BaseController {
    /**
     * 分类服务
     */
    @Autowired
    private WhgYunweiTypeService whgYunweiTypeService;

    /**
     * 标签服务
     */
    @Autowired
    private WhgYunweiTagService whgYunweiTagService;

    /**
     * 关键字服务
     */
    @Autowired
    private WhgYunweiKeyService whgYunweiKeyService;

    /**
     * 分馆服务
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;


    /**
     * 用户服务
     */
    @Autowired
    private WhgSystemUserService userService;

    /**
     * 文化品牌服务
     */
    @Autowired
    private WhgYunweiWhppService whgYunweiWhppService;

    /**
     * 分类
     * @param request
     * @return
     */
    @RequestMapping("/type/srchList")
    public ResponseBean srchListType(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            res.setData(whgYunweiTypeService.findAllYwiType(null,sysUser.getCultid()));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 获取总分类
     * @param request
     * @return
     */
    @RequestMapping("/type/srchBigTypeList")
    public ResponseBean srchBigTypeList(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        String type=request.getParameter("type");
        try {
            res.setData(whgYunweiTypeService.findAllYwiType(type,"0000000000000000"));//默认查询 全站数据
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 分类
     * @param request
     * @return
     */
    @RequestMapping("/tag/srchList")
    public ResponseBean srchListTag(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            res.setData(whgYunweiTagService.findAllYwiTag(null,sysUser.getCultid()));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 关键字
     * @param request
     * @return
     */
    @RequestMapping("/key/srchList")
    public ResponseBean srchListKey(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            res.setData(whgYunweiKeyService.findAllYwiKey(sysUser.getCultid()));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询表的公共状态信息
     * @param request
     * @return
     */
    @RequestMapping("/state/srchList/{type}")
    public ResponseBean srchListState(HttpServletRequest request, @PathVariable("type")String type){
        ResponseBean res = new ResponseBean();
        try {
            List<Map<String, String>> statelist = new ArrayList<Map<String, String>>();
            if(type != null && !type.isEmpty()){
                Class<?> class1 = Class.forName("com.creatoo.hn.util.enums."+type);
                Object[] objs = class1.getEnumConstants();
                for(Object obj : objs){
                    Method valueMethod = obj.getClass().getMethod("getValue");
                    Method nameMethod = obj.getClass().getMethod("getName");
                    String _val = valueMethod.invoke(obj).toString();
                    String _nam = nameMethod.invoke(obj).toString();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", _nam);
                    map.put("value", _val);
                    statelist.add(map);
                }
            }
            res.setData(statelist);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询文化品牌信息
     * @param request
     * @return
     */
    @RequestMapping("/brand/srchList")
    public ResponseBean srchListBrand(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            Map map =new HashMap();
            List list=new ArrayList();
            list.add(sysUser.getCultid());
            map.put("cultid",list);
            PageInfo page=whgYunweiWhppService.findBrand(map);
            res.setData(page.getList());
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询所属系统
     * @param request
     * @return
     */
    @RequestMapping("/project/srchList")
    public ResponseBean srchListProject(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            for(EnumProject enumProject:EnumProject.values()){
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", enumProject.getName());
                map.put("id", enumProject.getValue());
                list.add(map);
            }
            res.setData(list);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }
    /**
     * 查询前端菜单栏
     * @param request
     * @return
     */
    @RequestMapping("/frontmenu/srchList")
    public ResponseBean srchListMenu(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            for(EnumWbMenu enumProject: EnumWbMenu.values()){
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", enumProject.getName());
                map.put("id", enumProject.getValue());
                list.add(map);
            }
            res.setData(list);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询所属系统
     * @param request
     * @return
     */
    @RequestMapping("/fktype/srchList")
    public ResponseBean srchListFktype(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            String useStr=",2,3,5,20,25,26,23,21,";
            for(EnumTypeClazz enumProject: EnumTypeClazz.values()){
                if(useStr.indexOf(","+enumProject.getValue()+",")==-1){
                    continue;
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", enumProject.getName());
                map.put("id", enumProject.getValue());
                list.add(map);
            }
            res.setData(list);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询文化馆信息
     * @param request
     * @return
     */
    @RequestMapping("/cult/srchList")
    public ResponseBean srchListCult(HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            res.setData(whgSystemCultService.t_srchList4Publish());
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询文化馆信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/user/srchList")
    public ResponseBean srchListUser(HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            res.setData(userService.t_srchList());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }


    /**
     * 查询所属文化馆的省
     * @param session
     * @return
     */
    @RequestMapping("/cult/getProvince")
    @ResponseBody
    public ResponseBean srchProvince(HttpSession session){
        ResponseBean rsb = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            WhgSysCult cult = whgSystemCultService.t_srchOne(sysUser.getCultid());
            if(cult != null && !"".equals(cult)){
                rsb.setData(cult.getProvince());
            }
        }catch (Exception e){
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rsb;
    }

    /**
     * 查询所属文化馆的市
     * @param session
     * @return
     */
    @RequestMapping("/cult/getCity")
    @ResponseBody
    public ResponseBean srchCity(HttpSession session){
        ResponseBean rsb = new ResponseBean();
        WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            WhgSysCult cult = whgSystemCultService.t_srchOne(sysUser.getCultid());
            if(cult != null && !"".equals(cult)){
                rsb.setData(cult.getCity());
            }

        }catch (Exception e){
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return rsb;
    }
}
