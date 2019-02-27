package com.creatoo.hn.controller.api.apiinside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.admin.goods.WhgShowJmdService;
import com.creatoo.hn.services.admin.goods.WhgShowOrganService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apiinside.InsShowGoodsService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Administrator on 2017/9/5.
 * 展演类商品
 */
@Controller
@RequestMapping("/api/inside/showGoods")
@CrossOrigin
public class InsShowGoodsController extends BaseController{

    @Autowired
    private InsShowGoodsService insShowGoodsService;

    @Autowired
    private WhgShowJmdService whgShowJmdService;

    @Autowired
    private WhgShowOrganService whgShowOrganService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    /**
     * 展演类商品列表页
     * @param page 当前页
     * @param pageSize  页列表数量
     * @param cultid  文化馆id
     * @param type  商品类型
     * @param title  搜索标题
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean indexData(@RequestParam(value = "page", defaultValue = "1")int page,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                   @RequestParam(value = "cultid",required = false) String cultid,
                                   @RequestParam(value = "deptid",required = false) String deptid,
                                   @RequestParam(value = "type",required = false)String type,
                                   @RequestParam(value = "title",required = false)String title,
                                   @RequestParam(value = "province",required = false)String province,
                                   @RequestParam(value = "city",required = false)String city,
                                   @RequestParam(value = "pscity",required = false)String pscity,
                                   @RequestParam(value = "psprovince",required = false)String psprovince,
                                   @RequestParam(value = "area",required = false)String area){
        ApiResultBean rep = new ApiResultBean();
        Map data = new HashMap();
        String protype = EnumProject.PROJECT_NBGX.getValue();
        List _cultid = null;
        List _deptid = null;

        try {
            if(cultid!=null&&!cultid.equals("")){
                _cultid = Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
            }
            if(deptid != null && !"".equals(deptid)){
                _deptid = whgSystemDeptService.srchDeptStrList(deptid);
            }

            PageInfo info = this.insShowGoodsService.getShowGoodsList(page,pageSize,_cultid,_deptid,type,title,protype,province,city,area,psprovince,pscity);
            rep.setPageInfo(info);
            rep.setRows(info.getList());
        } catch (Exception e) {
            rep.setCode(1);
            rep.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return rep;
    }

    /**
     * 展演类商品详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/detail",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean traDetail(@RequestParam(value = "id",required = false)String id){
        ApiResultBean arb = new ApiResultBean();
        if(null == id){
            arb.setCode(101);
            arb.setMsg("商品ID不能为空");
            return arb;
        }
        ApiResultBean canSign = new ApiResultBean();
        String protype = EnumProject.PROJECT_NBGX.getValue();
        try {
            Map whgShowGoods = insShowGoodsService.getdetail(id,protype);
            if(null == whgShowGoods){
                arb.setCode(102);
                arb.setMsg("查询商品数据失败");
                return arb;
            }
            arb.setData(FilterFontUtil.clearFont(whgShowGoods, new String[]{"playbill", "showdesc"}));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 查相似表演列表，
     * @param id   当前的表演ID
     * @param cultid  分馆id
     * @param size 条数
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/recommendShowList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getRecommendShowList(@RequestParam(required = false, value = "id") String id,
                                      @RequestParam(required = false, value = "cultid") String cultid,
                                      @RequestParam(required = false, value = "size") Integer size){
        ApiResultBean arb = new ApiResultBean();
        String protype = EnumProject.PROJECT_NBGX.getValue();
        List _cultid = null;
        if(cultid!=null&&!cultid.equals("")){
            _cultid = Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
        }
        try {
            List list = this.insShowGoodsService.selectRecommendShowList(id, _cultid, size, protype);
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            arb.setData(new ArrayList());
            log.error(e.getMessage(), e);
        }

        return arb;
    }

    /**
     * 查询节目单
     * @param entid     实体ID
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/jmd",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getJmd(@RequestParam(value = "page")int page,
                                 @RequestParam(value = "pageSize")int pageSize,
                                 @RequestParam(value = "entid",required = false)String entid){
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = whgShowJmdService.t_getJmd(page,pageSize,entid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 查询演出机构
     * @param entid     实体ID
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/ycjg",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getYcjg(@RequestParam(value = "page")int page,
                                @RequestParam(value = "pageSize")int pageSize,
                                @RequestParam(value = "entid",required = false)String entid){
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo info = whgShowOrganService.t_getZzjg(page,pageSize,entid);
            arb.setPageInfo(info);
            arb.setRows(info.getList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

}
