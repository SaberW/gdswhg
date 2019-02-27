package com.creatoo.hn.controller.admin.reason;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.bean.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 活动控制层
 *
 * @author heyi
 */
@Controller
@RequestMapping("/admin/reason")
public class WhgXjReasonController extends BaseController {


    @Autowired
    private WhgXjReasonService whgXjReasonService;

    /**
     * 获取下架原因
     */
    @RequestMapping(value = "/getReason")
    @ResponseBody
    public ResponseBean getReason(HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        String fkid = request.getParameter("fkid");
        try {
            WhgXjReason whgReason = new WhgXjReason();
            whgReason.setFkid(fkid);
            List<WhgXjReason> list = whgXjReasonService.t_srchList(whgReason);
            if (list.size() > 0) {
                res.setData(list.get(0));
            }
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }


}
