package com.creatoo.hn.controller.api.apiinside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apiinside.InsGoodsService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Controller
@CrossOrigin
@RequestMapping("/api/inside")
public class InsGoodsController extends BaseController {

    @Autowired
    private InsGoodsService insGoodsService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    /**
     * 商品信息列表
     * @param req
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/goods/list", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object goodslist(HttpServletRequest req,
                       @RequestParam(value = "page", defaultValue = "1", required = false)int page,
                       @RequestParam(value = "pageSize", defaultValue = "10",required = false)int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = new HashMap();
            //recode.put("cultid", req.getParameter("cultid"));
            String cultid = req.getParameter("cultid");
            if (cultid != null && !cultid.isEmpty()) {
                recode.put("cultid", Arrays.asList(cultid.split("\\s*,\\s*")));
            }

            String deptid = req.getParameter("deptid");
            if (deptid!=null && !deptid.isEmpty()){
                recode.put("deptid", this.whgSystemDeptService.srchDeptStrList(deptid));
            }

            recode.put("etype", req.getParameter("etype"));
            recode.put("province", req.getParameter("province"));
            recode.put("city", req.getParameter("city"));
            recode.put("area", req.getParameter("area"));
            recode.put("area", req.getParameter("area"));
            recode.put("pscity", req.getParameter("pscity"));//配送区域
            recode.put("psprovince", req.getParameter("psprovince"));//配送区域

            recode.put("protype", EnumProject.PROJECT_NBGX.getValue());
            PageInfo pageInfo = this.insGoodsService.selectGoods(page, pageSize, recode);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arb;
    }

    /**
     * 商品详情
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/goods/info", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object goodsInfo(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            Object info = this.insGoodsService.findGoods(id, EnumProject.PROJECT_NBGX.getValue());
            arb.setData(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arb;
    }

    /**
     * 相关类型的商品
     * @param id
     * @param size
     * @param cultid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/goods/reflist", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object getGoodsReflist(String id,
                                  @RequestParam(value = "size", required = false)Integer size,
                                  @RequestParam(value = "cultid", required = false)String cultid,
                                  @RequestParam(value = "deptid", required = false)String deptid){
        ApiResultBean arb = new ApiResultBean();
        try {
            String protype = EnumProject.PROJECT_NBGX.getValue();

            List cultids = null;
            if (cultid != null && !cultid.isEmpty()) {
                cultids = Arrays.asList(cultid.split("\\s*,\\s*"));
            }

            List deptids = null;
            if (deptid!=null && !deptid.isEmpty()){
                deptids = this.whgSystemDeptService.srchDeptStrList(deptid);
            }

            List list = this.insGoodsService.selectGoodsReflist(id, size, protype, cultids, deptids);
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }
}
