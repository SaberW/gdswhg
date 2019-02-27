package com.creatoo.hn.controller.admin.project;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgFkProject;
import com.creatoo.hn.dao.model.WhgPersonnel;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LENUVN on 2017/7/27.
 */
@Controller
@RequestMapping("/admin/project")
public class WhgProjectController extends BaseController {


    @Autowired
    private WhgFkProjectService whgFkProjectService;

    @Autowired
    private WhgSupplyService whgSupplyService;
    /**
     *
     *
     * @return res
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public ResponseBean srchList4p(String fkId) {
        ResponseBean res = new ResponseBean();
        Map map=new HashMap();
        StringBuffer sb=new StringBuffer();
        try {
            List<WhgFkProject> list= whgFkProjectService.srchOneByFkId(fkId);
            for(WhgFkProject fkProject:list){
                if(fkProject.getProtype().equals(EnumProject.PROJECT_NBGX.getValue())){
                    List timeList=whgSupplyService.selectTimes4Supply(fkProject.getFkid());
                    if(timeList!=null){
                        map.put("times", JSON.toJSONString(timeList));
                    }

                    map.put("pscity", fkProject.getPscity());
                    map.put("psprovince", fkProject.getPsprovince());
                }
                if(!sb.toString().equals("")){
                    sb.append(",");
                }
                sb.append(fkProject.getProtype());
            }
            map.put("proType",sb.toString());
            res.setData(map);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 添加
     *
     * @return res
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseBean add(String proType,String ids,String times, String psprovince, String pscity) {
        ResponseBean res = new ResponseBean();
        try {
            whgFkProjectService.addStr(proType,ids,times, psprovince, pscity);//加入多系统识别
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }






}
