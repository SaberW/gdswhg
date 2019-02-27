package com.creatoo.hn.controller.api.apiinside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgCollection;
import com.creatoo.hn.dao.model.WhgUser;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.services.api.apiinside.InsCommonService;
import com.creatoo.hn.services.api.apiinside.InsUserService;
import com.creatoo.hn.services.api.apioutside.user.ApiUserService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Controller
@CrossOrigin
@RequestMapping("/api/inside")
public class InsCommonController extends BaseController{

    @Autowired
    private InsCommonService insCommonService;

    @Autowired
    private ApiUserService apiUserService;

    /**
     * 内部供需我的收藏列表
     * @param userId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/collection/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getCollections(String userId,
                                 @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                 @RequestParam(value = "pageSize", defaultValue = "1", required = false) int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo pageInfo = this.insCommonService.getCollections(page, pageSize, userId,EnumProject.PROJECT_NBGX.getValue());
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 内部供需添加收藏
     * @param cmuid  用户id
     * @param cmrefid  收藏关联类型id
     * @param cmreftyp  收藏关联类型
     * @param cmopttyp  操作类型:0收藏
     * @param cmurl  收藏对象的连接地址
     * @param cmtitle  收藏对象的标题
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/collection/addCollection", method = {RequestMethod.GET, RequestMethod.POST})
    public Object addMyColle(String cmuid,String cmrefid,String cmreftyp,String cmopttyp,String cmurl,String cmtitle) {
        WhgCollection collection = new WhgCollection();
        ApiResultBean arb = new ApiResultBean();
        Map map =new HashMap();
        try {
            collection.setCmid(IDUtils.getID());
            collection.setCmdate(new Date());
            collection.setCmrefid(cmrefid);
            collection.setCmuid(cmuid);
            collection.setCmreftyp(cmreftyp);
            collection.setSystype(EnumProject.PROJECT_NBGX.getValue());
            collection.setCmopttyp(cmopttyp);
            collection.setCmurl(cmurl);
            collection.setCmtitle(cmtitle);
            this.insCommonService.addMyColle(collection);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("新增收藏失败");
        }
        return arb;
    }

    /**
     * 判断用户是否点亮收藏
     * @param userId  用户ID
     * @param cmreftyp  收藏关联类型
     * @param cmrefid   收藏实体ID
     * @return
     */

    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/collection/isLightenColle", method = RequestMethod.POST)
    public Object isColle(String userId,String cmreftyp,String cmrefid) {
        ApiResultBean arb = new ApiResultBean();
        Map<String, Object> map = new HashMap<String, Object>();
        int scNum = 0;
        try {
            //获取收藏数
            scNum = this.insCommonService.shouCanShu(cmreftyp, cmrefid);
            arb.setData(scNum);
            boolean iscolle = this.insCommonService.isColle(userId, cmreftyp, cmrefid);
            if (iscolle) {
                arb.setCode(103);
                arb.setMsg("已收藏");
                return arb;
            }

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("查询失败");
        }

        return arb;

    }

    /**
     * 删除我的收藏
     * @param cmuid   收藏用户ID
     * @param cmrefid   收藏实体ID
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/collection/removeColle", method = RequestMethod.POST)
    public Object removeColle(String cmuid,String cmrefid) {
        ApiResultBean arb = new ApiResultBean();
        Map<String, Object> reMap = new HashMap<String, Object>();
        try {
            this.insCommonService.removeCollection(cmuid,cmrefid);
        } catch (Exception e) {
            reMap.put("success", false);
            arb.setCode(101);
            arb.setMsg("操作失败");
        }
        return arb;
    }

    /**
     * 获取资源类型的 fk 配送范围
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/comm/getCitys4resource", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getCitys4resource(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            Object info = this.insCommonService.getCitys4Resource(id);
            arb.setData(info);
        } catch (Exception e) {
            arb.setMsg("查询数据失败");
            arb.setCode(101);
            log.error(e.getMessage(),e);
        }
        return arb;
    }
}
