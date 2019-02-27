package com.creatoo.hn.controller.admin.gather;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgGatherOrder;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 其它众筹订单管理
 * Created by rbg on 2017/10/18.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/gather/order")
public class WhgGatherOrderController extends BaseController {

    @Autowired
    private WhgGatherService whgGatherService;

    @RequestMapping("/view/list")
    public String view(String gatherid, ModelMap mmp, WebRequest request, HttpSession session){
        mmp.addAttribute("gatherid", gatherid);
        mmp.addAttribute("title", request.getParameter("title"));
        return "admin/gather/order/view_list";
    }

    @RequestMapping("/view/show")
    public String view(String id, ModelMap mmp){
        try {
            Object info = this.whgGatherService.findGatherOrder(id);
            mmp.addAttribute("info", info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "admin/gather/order/view_edit";
    }

    @RequestMapping("/srchList")
    @ResponseBody
    public Object srchList(int page, int rows, WebRequest request, WhgGatherOrder recode){
        ResponseBean resb = new ResponseBean();
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        try {
            PageInfo pageInfo = this.whgGatherService.srchList4Order(page, rows, recode, sort, order);

            resb.setRows( (List)pageInfo.getList() );
            resb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.debug("查询数据失败", e);
            resb.setRows(new ArrayList());
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

}
