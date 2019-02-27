package com.creatoo.hn.controller.api.apiinside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.admin.personnel.WhgAwardsService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.api.apiinside.InsPersonnelService;
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
public class InsPersonnelController extends BaseController{

    @Autowired
    private InsPersonnelService insPersonnelService;

    @Autowired
    private WhgAwardsService whgAwardsService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    /**
     * 查询人才列表
     * @param req
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/person/list", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object personList(HttpServletRequest req,
                             @RequestParam(value = "page", defaultValue = "1", required = false)int page,
                             @RequestParam(value = "pageSize", defaultValue = "10", required = false)int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map recode = new HashMap();
            String cultid = req.getParameter("cultid");
            if (cultid != null && !cultid.isEmpty()) {
                recode.put("cultid", Arrays.asList(cultid.split("\\s*,\\s*")));
            }
            String deptid = req.getParameter("deptid");
            if (deptid != null && !deptid.isEmpty()) {
                recode.put("deptid", whgSystemDeptService.srchDeptStrList(deptid));
            }
            recode.put("type", req.getParameter("type"));
            recode.put("styletype", req.getParameter("styletype"));
            recode.put("province", req.getParameter("province"));
            recode.put("city", req.getParameter("city"));
            recode.put("area", req.getParameter("area"));
            recode.put("pscity", req.getParameter("pscity"));//配送区域
            recode.put("psprovince", req.getParameter("psprovince"));//配送区域
            recode.put("protype", EnumProject.PROJECT_NBGX.getValue());
            PageInfo pageInfo = this.insPersonnelService.selectPersonList(page, pageSize, recode);

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
     * 查询人才信息
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/person/info", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getPerson(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            Object info = this.insPersonnelService.findPerson(id, EnumProject.PROJECT_NBGX.getValue());
            arb.setData(info);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 相关人才的推荐列表
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/person/recommends", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object getRecommends(String id,
                                @RequestParam(value = "size", required = false)Integer size,
                                @RequestParam(value = "cultid", required = false)String cultid){
        ApiResultBean arb = new ApiResultBean();
        try {
            String protype = EnumProject.PROJECT_NBGX.getValue();
            List _cultid = null;
            if(cultid!=null&&!cultid.equals("")){
                _cultid = Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
            }
            List list = this.insPersonnelService.selectRecommonends(id, size, protype, _cultid);
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 查询获奖情况
     * @param entid     实体ID
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/awards",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean getAwards(@RequestParam(value = "page")int page,
                                @RequestParam(value = "pageSize")int pageSize,
                                @RequestParam(value = "entid",required = false)String entid){
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = whgAwardsService.t_getAwards(page,pageSize,entid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(102);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }
}
