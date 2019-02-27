package com.creatoo.hn.controller.admin.yunwei;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiArea;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiAreaService;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.*;
import com.creatoo.hn.web.converter.JSONPObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市区管理维护
 * Created by wangxl on 2018/1/15.
 */
@RestController
@RequestMapping("/admin/yunwei/area")
public class WhgYunweiAreaController extends BaseController {
    /**
     * 资源库服务
     */
    @Autowired
    private WhgYunweiAreaService whgYunweiAreaService;

    /**
     * 文化馆服务
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 进入省市区管理视图
     * type: 页面类型的参数（列表、添加、编辑）
     * classify : 分类的类型（1、区域）
     * @return
     */
    @GetMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.MASS_TYPE ,optDesc = {"访问资源库页面", "访问添加资源库页面", "访问编辑资源库页面"} ,valid = {"type=list", "type=add", "type=edit"})
    public ModelAndView view(@PathVariable("type") String type, @RequestParam(value="id", required=false) String id){
        ModelAndView view = new ModelAndView("admin/yunwei/area/view_"+type);
        try{
            if("edit".equalsIgnoreCase(type)){
                WhgYwiArea whgYwiArea = this.whgYunweiAreaService.findById(id);
                view.addObject("whgYwiArea", whgYwiArea);
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
    public List<WhgYwiArea> srchList(String sort, String order, WhgYwiArea whgYwiArea)throws Exception{
        List<WhgYwiArea> cacheList = this.whgYunweiAreaService.find(sort, order, whgYwiArea);
        List<WhgYwiArea> list = new ArrayList<>();
        list.addAll(cacheList);
        if(whgYwiArea != null && StringUtils.isNotEmpty(whgYwiArea.getPid())){
            WhgYwiArea root = this.whgYunweiAreaService.findById(whgYwiArea.getPid());
            if("ROOT".equalsIgnoreCase(root.getPid())){
                list.add(0,root);
            }
        }
        return list;
    }

    /**
     * 查询一级节点根据父节点
     * @param pid 父节点
     * @return
     * @throws Exception
     */
    @PostMapping("/srchList4tree")
    public List<Map<String, String>> srchList4tree(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            @RequestParam(value="id", required=false) String pid,
            @RequestParam(value="root", required=false) String root,
            @RequestParam(value="level", required=false) String level
    )throws Exception{
        List<Map<String, String>> rtnList = new ArrayList<>();
        try {
            boolean qryDB = true;
            if(StringUtils.isEmpty(pid)){
                if("ROOT".equals(root)){//从ROOT开始查询
                    qryDB = false;
                    Map<String, String> map = new HashMap<>();
                    map.put("id", "ROOT");
                    map.put("text", "ROOT");
                    map.put("level", "0");
                    map.put("state", "closed");
                    rtnList.add(map);
                }else{//按管理员区域开始查询
                    if(EnumConsoleSystem.sysmgr.getValue().equals(sysUser.getAdmintype())){
                        String areaName = "";
                        if(EnumCultLevel.Level_Province.getValue() == sysUser.getAdminlevel()){
                            areaName = sysUser.getAdminprovince();
                        }else if(EnumCultLevel.Level_City.getValue() == sysUser.getAdminlevel()){
                            areaName = sysUser.getAdmincity();
                        }else{
                            areaName = sysUser.getAdminarea();
                        }
                        WhgYwiArea ywiArea = this.whgYunweiAreaService.findByName(areaName);
                        if(ywiArea != null){
                            qryDB = false;
                            Map<String, String> map = new HashMap<>();
                            map.put("id", ywiArea.getId());
                            map.put("text", ywiArea.getName());
                            map.put("level", sysUser.getAdminlevel()+"");
                            map.put("state", this.whgYunweiAreaService.hasChildren(ywiArea.getId()) ? "closed" : "open");
                            rtnList.add(map);
                        }
                    }
                }
            }


            String nextLevel = "1";
            if(qryDB && StringUtils.isEmpty(pid)){
                pid = "ROOT";
            }else{
                nextLevel = (Integer.parseInt(level)+1)+"";
            }

            if(qryDB) {
                WhgYwiArea whgYwiArea = new WhgYwiArea();
                whgYwiArea.setPid(pid);
                List<WhgYwiArea> list = this.whgYunweiAreaService.find(null, null, whgYwiArea);
                if (list != null) {
                    for (WhgYwiArea type : list) {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", type.getId());
                        map.put("text", type.getName());
                        map.put("level", nextLevel);
                        map.put("state", this.whgYunweiAreaService.hasChildren(type.getId()) ? "closed" : "open");
                        rtnList.add(map);
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return rtnList;
    }

    @PostMapping("/srchList4tree4cult")
    public List<Map<String, String>> srchList4tree4cult(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            @RequestParam(value="id", required=false) String pid,
            @RequestParam(value="level", required=false) String level,
            @RequestParam(value="area", required=false) String area)throws Exception{
        List<Map<String, String>> rtnList = new ArrayList<>();
        try {
                //
                boolean qryDB = true;
                if(StringUtils.isEmpty(pid)){//查询第一级
                    if(EnumConsoleSystem.sysmgr.getValue().equals(sysUser.getAdmintype())) {
                        String areaName = "";
                        if (EnumCultLevel.Level_Province.getValue().intValue() == sysUser.getAdminlevel()) {
                            areaName = sysUser.getAdminprovince();
                        } else if (EnumCultLevel.Level_City.getValue().intValue() == sysUser.getAdminlevel()) {
                            areaName = sysUser.getAdmincity();
                        } else {
                            areaName = sysUser.getAdminarea();
                        }
                        WhgYwiArea ywiArea = this.whgYunweiAreaService.findByName(areaName);
                        if(ywiArea != null){
                            qryDB = false;
                            Map<String, String> map = new HashMap<>();
                            map.put("id", ywiArea.getId());
                            map.put("text", ywiArea.getName());
                            map.put("level", sysUser.getAdminlevel()+"");
                            boolean hasChild = this.whgYunweiAreaService.hasChildrenAndCult(ywiArea.getId(), ywiArea.getName(), sysUser.getAdminlevel()+"");
                            map.put("state", hasChild ? "closed" : "open");
                            rtnList.add(map);
                        }
                    }
                }

                //按指定id查询下级节点和文化馆
                String nextLevel = "1";
                if(qryDB && StringUtils.isEmpty(pid)){
                    pid = "ROOT";
                }else if(StringUtils.isNotEmpty(level)){
                    nextLevel = (Integer.parseInt(level)+1)+"";
                }

                if(qryDB){
                    //查询上级区域下的文化馆
                    if(StringUtils.isNotEmpty(level) && StringUtils.isNotEmpty(area)){
                        //先文化馆数据
                        WhgSysCult whgSysCult = new WhgSysCult();
                        whgSysCult.setState(EnumBizState.STATE_PUB.getValue());
                        whgSysCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                        whgSysCult.setLevel(Integer.parseInt(level));
                        if((EnumCultLevel.Level_Province.getValue()+"").equals(level+"")){
                            whgSysCult.setProvince(area);
                        }else if((EnumCultLevel.Level_City.getValue()+"").equals(level+"")){
                            whgSysCult.setCity(area);
                        }else if((EnumCultLevel.Level_Area.getValue()+"").equals(level+"")){
                            whgSysCult.setArea(area);
                        }
                        List<WhgSysCult> cults = this.whgSystemCultService.t_srchList(whgSysCult);
                        if(cults != null){
                            for(WhgSysCult cult : cults){
                                Map<String, String> map = new HashMap<>();
                                map.put("id", cult.getId());
                                map.put("text", cult.getName());
                                map.put("level", nextLevel);
                                map.put("iconCls", "cultIcon");
                                map.put("iscult", "1");
                                map.put("state", "open");
                                rtnList.add(map);
                            }
                        }
                    }
                    //查询区域
                    WhgYwiArea whgYwiArea = new WhgYwiArea();
                    whgYwiArea.setPid(pid);
                    List<WhgYwiArea> list = this.whgYunweiAreaService.find(null, null, whgYwiArea);
                    if (list != null) {
                        for (WhgYwiArea type : list) {
                            Map<String, String> map = new HashMap<>();
                            map.put("id", type.getId());
                            map.put("text", type.getName());
                            map.put("level", nextLevel);
                            boolean hasChild = this.whgYunweiAreaService.hasChildrenAndCult(type.getId(), type.getName(), nextLevel);
                            map.put("state", hasChild ? "closed":"open");
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
    public ResponseBean add(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, WhgYwiArea whgYwiArea){
        ResponseBean res = new ResponseBean();
        try {
            this.whgYunweiAreaService.add(sysUser, whgYwiArea);
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
    public ResponseBean edit(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, WhgYwiArea whgYwiArea){
        ResponseBean res = new ResponseBean();
        try {
            this.whgYunweiAreaService.edit(sysUser, whgYwiArea);
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
            this.whgYunweiAreaService.del(id);
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
            this.whgYunweiAreaService.updateState(id, fromState, toState);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 根据区域值获取省市区信息
     * @param area 区域值，可以是省，市或者区域名称
     * @return
     * @throws Exception
     */
    @RequestMapping("/areaInfo")
    public Object getAreaInfo(String area)throws Exception{
        String _province = "";
        String _city = "";
        String _area = "";
        String _level = "";
        List<String> names = new ArrayList<>();
        WhgYwiArea ywiArea = this.whgYunweiAreaService.findByName(area);
        if(ywiArea != null) {
            names.add(ywiArea.getName());
            while (!"ROOT".equals(ywiArea.getPid())) {
                ywiArea = this.whgYunweiAreaService.findById(ywiArea.getPid());
                names.add(ywiArea.getName());
            }
            if (names.size() > 0) {
                _province = names.get(names.size() - 1);
                _level = "1";
                if (names.size() > 1) {
                    _city = names.get(names.size() - 2);
                    _level = "2";
                    if (names.size() > 2) {
                        _area = names.get(names.size() - 3);
                        _level = "3";
                    }
                }
            }
        }
        Map<String, String> map = new HashMap();
        map.put("province", _province);
        map.put("city", _city);
        map.put("area", _area);
        map.put("level", _level);
        return map;
    }

    /**
     * 查询省数据或者市或者区域数据
     * @param area
     * @return
     */
    @GetMapping("/findProvinceCityArea")
    public Object findProvinceCityArea(String area, String callback){
        List<WhgYwiArea> list = new ArrayList<>();
        try{
            if(StringUtils.isEmpty(area)){
                WhgYwiArea whgYwiArea = new WhgYwiArea();
                whgYwiArea.setPid("ROOT");
                whgYwiArea.setState(1);
                list = this.whgYunweiAreaService.find("idx", null, whgYwiArea);
            }else{
                WhgYwiArea whgYwiArea = this.whgYunweiAreaService.findByName(area);
                WhgYwiArea whgYwiArea2 = new WhgYwiArea();
                whgYwiArea2.setPid(whgYwiArea.getId());
                whgYwiArea2.setState(1);
                list = this.whgYunweiAreaService.find("idx", null, whgYwiArea2);
            }
        }catch(Exception e){
        }
        return new JSONPObject(callback, list);
    }

    /**
     * 获得城市代码，用于全市站域名前缀
     * @return
     */
    @GetMapping("/findCodeByName")
    public Object findCodeByName(String areaName,String callback){
        String cityCode = null;
        try{
            WhgYwiArea whgYwiArea = this.whgYunweiAreaService.findByName(areaName);
            cityCode = whgYwiArea.getCode();
            if(StringUtils.isEmpty(cityCode)){
                cityCode = "gds";
            }
        }catch(Exception e){
        }
        return new JSONPObject(callback, cityCode);
    }
}
