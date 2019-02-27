package com.creatoo.hn.controller.api.outproject;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.information.WhgInfColInfoService;
import com.creatoo.hn.services.admin.information.WhgInfUploadService;
import com.creatoo.hn.services.admin.information.WhgInfoService;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.admin.user.WhgUserService;
import com.creatoo.hn.services.admin.yunwei.*;
import com.creatoo.hn.services.api.apioutside.collection.ApiCollectionService;
import com.creatoo.hn.services.api.apioutside.user.ApiUserService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.creatoo.hn.web.converter.JSONPObject;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 公共接口
 *
 * @author dzl
 */

@CrossOrigin
@RestController
@RequestMapping("/api/outer/comm")
public class ApiOuterCommonController extends BaseController {


    @Autowired
    private WhgYunweiTypeService whgYunweiTypeService;

    @Autowired
    private WhgYunweiKeyService whgYunweiKeyService;

    @Autowired
    private WhgYunweiTagService whgYunweiTagService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgResourceService whgResourceService;
    @Autowired
    private WhgInfColInfoService whgInfColInfoService;
    @Autowired
    private WhgInfUploadService whgInfUploadService;
    /**
     * 资源库服务
     */
    @Autowired
    private WhgYunweiAreaService whgYunweiAreaService;

    @Autowired
    private WhgInfoService whgInfoService;

    @Autowired
    private WhgUserService whgUserService;
    /**
     * 通过站点标识获取文化馆信息
     *
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/getCultBySite", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getCultBySite(@RequestParam(value = "cultsite", required = false, defaultValue = "") String cultsite) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (cultsite != "") {
                WhgSysCult cult = whgSystemCultService.t_srchOneBySite(cultsite);
                if (cult != null) {
                    arb.setData(cult);
                } else {
                    arb.setCode(2);
                    arb.setMsg("站点信息有误！");
                }
            } else {
                arb.setCode(1);
                arb.setMsg("所传参数有误");
            }
        } catch (Exception e) {
            arb.setCode(1);
            arb.setMsg("获取文化馆信息失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 获得类别
     * <p>
     * 4 活动 5 培训 2 场馆 3 活动室
     * <p>
     * 6 区域
     *
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/getTypes", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getTypes(String type, @RequestParam(value = "cultsite", required = false, defaultValue = "") String cultsite) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (cultsite != "") {
                WhgSysCult cult = whgSystemCultService.t_srchOneBySite(cultsite);
                if (cult != null) {
                    List list = whgYunweiTypeService.findAllYwiType(type, cult.getId(), true);
                    arb.setRows(list);
                } else {
                    arb.setCode(2);
                    arb.setMsg("站点信息有误！");
                }
            } else {
                arb.setCode(1);
                arb.setMsg("所传参数有误");
            }
        } catch (Exception e) {
            arb.setCode(1);
            arb.setMsg("获取区域类别失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 查询省数据或者市或者区域数据
     *
     * @param area
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/getAreas", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getAreas(String area) {
        ApiResultBean arb = new ApiResultBean();
        List<WhgYwiArea> list = new ArrayList<>();
        try {
            if (StringUtils.isEmpty(area)) {
                WhgYwiArea whgYwiArea = new WhgYwiArea();
                whgYwiArea.setPid("ROOT");
                list = this.whgYunweiAreaService.find("idx", null, whgYwiArea);
            } else {
                WhgYwiArea whgYwiArea = this.whgYunweiAreaService.findByName(area);
                WhgYwiArea whgYwiArea2 = new WhgYwiArea();
                whgYwiArea2.setPid(whgYwiArea.getId());
                list = this.whgYunweiAreaService.find("idx", null, whgYwiArea2);
            }
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(1);
            arb.setMsg("获取省市区数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 获取关键字
     * <p>
     * 4 活动 5 培训 2 场馆 3 活动室
     *
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/getKeys", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getKeys(String type, @RequestParam(value = "cultsite", required = false, defaultValue = "") String cultsite, @RequestParam(value = "page", defaultValue = "1") int page
            , @RequestParam(value = "rows", defaultValue = "10") int rows) {
        ApiResultBean arb = new ApiResultBean();
        WhgYwiKey whgYwiKey = new WhgYwiKey();
        whgYwiKey.setType(type);
        whgYwiKey.setState(1);
        whgYwiKey.setDelstate(0);

        try {
            if (cultsite != "") {
                WhgSysCult cult = whgSystemCultService.t_srchOneBySite(cultsite);
                if (cult != null) {
                    whgYwiKey.setCultid(cult.getId());
                    PageInfo<WhgYwiKey> pageInfo = this.whgYunweiKeyService.t_srchList4p(page, rows, whgYwiKey, null);
                    arb.setPageInfo(pageInfo);
                } else {
                    arb.setCode(2);
                    arb.setMsg("站点信息有误！");
                }
            } else {
                arb.setCode(1);
                arb.setMsg("所传参数有误");
            }
        } catch (Exception e) {
            arb.setCode(1);
            arb.setMsg("获取关键字失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 获得标签
     * <p>
     * 4 活动 5 培训 2 场馆 3 活动室
     *
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/getTags", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getTags(String type, @RequestParam(value = "cultsite", required = false, defaultValue = "") String cultsite, @RequestParam(value = "tagIds", required = false, defaultValue = "") String tagIds, @RequestParam(value = "page", defaultValue = "1") int page
            , @RequestParam(value = "rows", defaultValue = "10") int rows) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (cultsite != "") {
                WhgSysCult cult = whgSystemCultService.t_srchOneBySite(cultsite);
                if (cult != null) {
                    WhgYwiTag whgYwiTag = new WhgYwiTag();
                    List list = new ArrayList();
                    whgYwiTag.setType(type);
                    whgYwiTag.setState(1);
                    whgYwiTag.setDelstate(0);
                    whgYwiTag.setCultid(cult.getId());
                    if (!"".equals(tagIds)) {
                        list = Arrays.asList(tagIds.split(","));
                    }
                    PageInfo<WhgYwiTag> pageInfo = this.whgYunweiTagService.t_srchList4p(page, rows, whgYwiTag, null, list);
                    arb.setPageInfo(pageInfo);
                } else {
                    arb.setCode(2);
                    arb.setMsg("站点信息有误！");
                }
            } else {
                arb.setCode(1);
                arb.setMsg("所传参数有误");
            }
        } catch (Exception e) {
            arb.setCode(1);
            arb.setMsg("获得标签失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 查询实体关联的资讯信息
     *
     * @param entityid 实体ID
     * @param clnftype 栏目ID
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getRelationZx", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getRelationZx(String id, String type,
                                       @RequestParam(value = "page", required = false) Integer page,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            /*List<WhgInfColinfo> info = this.whgInfoService.findColinfo(id, type);
            arb.setData(info);*/

            PageInfo pageInfo = this.whgInfoService.findColinfo(page, pageSize, id, type);
            if (page!=null && pageSize != null){
                arb.setPageInfo(pageInfo);
            }else{
                arb.setData(pageInfo.getList());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 通过传来用户信息 返回 二期用户数据
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/backUser", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ApiResultBean backUser(String userphone) {
        ApiResultBean arb = new ApiResultBean();
        try {
            WhgUser info = this.whgUserService.t_srchOneByPhone(userphone);
            if (info != null) {
                arb.setData(info.getId());
            } else {
                arb.setCode(102);
                arb.setMsg("查询数据失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 获得资讯
     * <p>
     * 4 活动 5 培训 2 场馆 3 活动室
     *
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/getInfos", method = {RequestMethod.POST, RequestMethod.GET})
    public Object getInfos(String type, @RequestParam(value = "cultsite", required = false, defaultValue = "") String cultsite, @RequestParam(value = "relation", required = false, defaultValue = "") String relation, @RequestParam(value = "page", defaultValue = "1") int page
            , @RequestParam(value = "rows", defaultValue = "10") int rows) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (cultsite != "") {
                WhgSysCult cult = whgSystemCultService.t_srchOneBySite(cultsite);
                if (cult != null) {
                    Map param = new HashMap();
                    param.put("cultid", Arrays.asList(cult.getId()));
                    if (null != type) {
                        param.put("clnftype", type);
                    }
                    if ("" != relation) {
                        param.put("relation", relation);
                    }
                    param.put("toproject", "WBGX");
                    param.put("delstate", 0);
                    param.put("clnfstata", EnumBizState.STATE_PUB.getValue());
                    param.put("page", page + "");
                    param.put("rows", rows + "");
                    PageInfo pageInfo = whgInfColInfoService.selfrontlist(param);
                    if (null == pageInfo) {
                        arb.setCode(1);
                        arb.setMsg("获取资讯列表失败");
                        return arb;
                    }
                    arb.setPageInfo(pageInfo);
                    arb.setRows(pageInfo.getList());
                    return arb;
                } else {
                    arb.setCode(2);
                    arb.setMsg("站点信息有误！");
                }
            } else {
                arb.setCode(1);
                arb.setMsg("所传参数有误");
            }
        } catch (Exception e) {
            arb.setCode(1);
            arb.setMsg("获得标签失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 资讯详情
     *
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/infoDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public Object infoDetail(String type, @RequestParam(value = "cultsite", required = false, defaultValue = "") String cultsite, @RequestParam(value = "id", required = false, defaultValue = "") String id) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (cultsite != "") {
                WhgSysCult cult = whgSystemCultService.t_srchOneBySite(cultsite);
                if (cult != null) {
                    Map param = new HashMap();
                    param.put("cultid", Arrays.asList(cult.getId()));
                    if (null != type) {
                        param.put("clnftype", type);
                    }
                    WhgInfColinfo pageInfo = whgInfColInfoService.getInfo(id);
                    List<WhgInfUpload> list = whgInfUploadService.selecup(id);
                    arb.setData(pageInfo);
                    arb.setRows(list);
                    if (null == pageInfo) {
                        arb.setCode(1);
                        arb.setMsg("获取资讯详情失败");
                        return arb;
                    }
                    return arb;
                } else {
                    arb.setCode(2);
                    arb.setMsg("站点信息有误！");
                }
            } else {
                arb.setCode(1);
                arb.setMsg("所传参数有误");
            }
        } catch (Exception e) {
            arb.setCode(1);
            arb.setMsg("获得资讯失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 查询资源
     *
     * @param type 实体类型
     * @param refid   实体ID
     * @param enttype 资源类型（1图片/2视频/3音频4/文档）
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getResources", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getResource(@RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "refid", required = false) String refid,
                                     @RequestParam(value = "enttype", required = false) String enttype,
                                     @RequestParam(value = "wechat", required = false) String wechat,
                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = whgResourceService.t_getResource(type, refid, enttype, wechat, page, pageSize);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }
}
