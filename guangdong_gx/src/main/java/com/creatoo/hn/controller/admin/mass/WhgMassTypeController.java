package com.creatoo.hn.controller.admin.mass;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.mapper.WhgMassTypeMapper;
import com.creatoo.hn.dao.model.WhgMassType;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiType;
import com.creatoo.hn.services.admin.mass.WhgMassTypeService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群文资源库控制器
 * Created by wangxl on 2017/11/7.
 */
@RestController
@RequestMapping("/admin/mass/type")
public class WhgMassTypeController extends BaseController {
    /**
     * 资源库服务
     */
    @Autowired
    private WhgMassTypeService whgMassTypeService;

    /**
     * 进入分类管理视图
     * type: 页面类型的参数（列表、添加、编辑）
     * classify : 分类的类型（1、区域）
     * @return
     */
    @GetMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.MASS_TYPE ,optDesc = {"访问资源库页面", "访问添加资源库页面", "访问编辑资源库页面"} ,valid = {"type=list", "type=add", "type=edit"})
    public ModelAndView view(@PathVariable("type") String type, @RequestParam(value="id", required=false) String id){
        ModelAndView view = new ModelAndView("admin/mass/type/view_"+type);
        try{
            if("edit".equalsIgnoreCase(type)){
                WhgMassType whgMassType = this.whgMassTypeService.findById(id);
                view.addObject("whgMassType", whgMassType);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 列表查询
     * @return
     */
    @PostMapping("/srchList")
    public List<WhgMassType> srchList(String sort, String order, WhgMassType whgMassType)throws Exception{
        return this.whgMassTypeService.find(sort, order, whgMassType);
    }

    /**
     * 查询一级节点根据父节点
     * @param pid 父节点
     * @return
     * @throws Exception
     */
    @PostMapping("/srchList4tree")
    public List<Map<String, String>> srchList4tree(@RequestParam(value="id", required=false) String pid,
                                                   @RequestParam(value="pid", required=false) String root)throws Exception{
        List<Map<String, String>> rtnList = new ArrayList();
        try {
            if (StringUtils.isEmpty(pid) && StringUtils.isEmpty(root)) {
                pid = "ROOT";
                Map<String, String> map = new HashMap<>();
                map.put("id", "ROOT");
                map.put("text", "ROOT");
                map.put("state", this.whgMassTypeService.hasChildren("ROOT") ? "closed" : "open");
                rtnList.add(map);
            } else {
                WhgMassType whgMassType = new WhgMassType();
                if(StringUtils.isEmpty(pid) && StringUtils.isNotEmpty(root)){
                    whgMassType.setPid(root);
                }else{
                    whgMassType.setPid(pid);
                }
                List<WhgMassType> list = this.whgMassTypeService.find(null, null, whgMassType);
                if (list != null) {
                    for (WhgMassType type : list) {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", type.getId());
                        map.put("text", type.getName());
                        map.put("state", this.whgMassTypeService.hasChildren(type.getId()) ? "closed" : "open");
                        rtnList.add(map);
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return rtnList;
    }

    /**
     * 添加
     * @return
     */
    @PostMapping("/add")
    //@RequestMapping("/add")
    @WhgOPT(optType = EnumOptType.MASS_TYPE, optDesc = {"添加"})
    public ResponseBean add(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, WhgMassType whgMassType){
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassTypeService.add(sysUser, whgMassType);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 编辑
     * @return
     */
    //@RequestMapping("/edit")
    @PostMapping("/edit")
    @WhgOPT(optType = EnumOptType.MASS_TYPE, optDesc = {"编辑"})
    public ResponseBean edit(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, WhgMassType whgMassType){
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassTypeService.edit(sysUser, whgMassType);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/del")
    @WhgOPT(optType = EnumOptType.MASS_TYPE, optDesc = {"删除"})
    public ResponseBean del(@RequestParam String id){
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassTypeService.del(id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 修改type 开关状态
     * @return
     */
    @RequestMapping("/updateState")
    @WhgOPT(optType = EnumOptType.MASS_TYPE, optDesc = {"启用", "停用"}, valid = {"type=1", "type=0"})
    public ResponseBean updateOpt(@RequestParam String id, @RequestParam String fromState, @RequestParam String toState){
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassTypeService.updateState(id, fromState, toState);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }
}
