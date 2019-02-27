package com.creatoo.hn.controller.api.apiinside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.admin.supply.WhgSupplyTraPerService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apiinside.InsTraService;
import com.creatoo.hn.services.api.apioutside.train.ApiTrainService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Administrator on 2017/9/5.
 */
@RequestMapping("/api/inside/tra")
@CrossOrigin
@Controller
public class InsTraController extends BaseController{

    @Autowired
    private ApiTrainService apiTrainService;

    @Autowired
    private InsTraService insTraService;

    @Autowired
    private WhgSupplyTraPerService whgSupplyTraPerService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    /**
     * 培训列表页
     * @param page 当前页
     * @param pageSize  页列表数量
     * @param cultid  文化馆id
     * @param arttype  艺术分类
     * @param type  培训类型
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
                                   @RequestParam(value = "arttype",required = false)String arttype,
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
            if(deptid != null){
                _deptid = whgSystemDeptService.srchDeptStrList(deptid);
            }
            PageInfo info = this.insTraService.getTraList(page,pageSize,_cultid,_deptid,arttype,type,title,protype,province,city,area,psprovince,pscity);
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
     * 培训详情
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
            arb.setMsg("培训ID不能为空");
            return arb;
        }
        ApiResultBean canSign = new ApiResultBean();
        String protype = EnumProject.PROJECT_NBGX.getValue();
        try {
            Object whgTra = insTraService.getOneTra(id,protype);
            try{whgTra = FilterFontUtil.clearFont4Tra((Map)whgTra);}catch (Exception e){}
            if(null == whgTra){
                arb.setCode(102);
                arb.setMsg("查询培训数据失败");
                return arb;
            }
            arb.setData(whgTra);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 查推荐培训列表，
     * @param traid   当前的培训ID
     * @param cultid  分馆id
     * @param size 条数
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/recommendTraList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getRecommendVenList(@RequestParam(required = false, value = "traid") String traid,
                                      @RequestParam(required = false, value = "cultid") String cultid,
                                      @RequestParam(required = false, value = "size") Integer size){
        ApiResultBean arb = new ApiResultBean();
        String protype = EnumProject.PROJECT_NBGX.getValue();
        List _cultid = null;
        if(cultid!=null&&!cultid.equals("")){
            _cultid = Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
        }
        try {
            List list = this.insTraService.selectRecommendTraList(traid, _cultid, size, protype);
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
     * 查询培训人员
     * @param entid     实体ID
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/pxry",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getPxry(@RequestParam(value = "page")int page,
                                 @RequestParam(value = "pageSize")int pageSize,
                                 @RequestParam(value = "entid",required = false)String entid){
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = whgSupplyTraPerService.t_getPxry(page,pageSize,entid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }
}
