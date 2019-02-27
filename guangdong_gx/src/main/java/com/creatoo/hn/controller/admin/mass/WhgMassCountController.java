package com.creatoo.hn.controller.admin.mass;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.admin.mass.WhgMassCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/10/30.
 * 群文统计
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/mass/count")
public class WhgMassCountController extends BaseController {

    @Autowired
    private WhgMassCountService whgMassCountService;

    @RequestMapping("/view/list")
    public String view(ModelMap mmp, WebRequest request){
        String view = "admin/mass/count/view_list";
        mmp.addAttribute("curtDate", LocalDate.now());
        return view;
    }


    /**
     * 按分类
     * @param cultid
     * @return
     */
    @RequestMapping("/count4ResType")
    @ResponseBody
    public Object count4ResType(String cultid){
        try {
            List list = this.whgMassCountService.countData4restype(cultid);
            return list;
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    /**
     * 按年月
     * @param cultid
     * @return
     */
    @RequestMapping("/count4yearMonth")
    @ResponseBody
    public Object count4yearMonth(String cultid){
        try {
            List list = this.whgMassCountService.countData4yearMonth(cultid);
            return list;
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    /**
     * 按top10
     * @param cultid
     * @return
     */
    @RequestMapping("/count4top10")
    @ResponseBody
    public Object count4top10(String cultid){
        try {
            List list = this.whgMassCountService.countData4Top(cultid);
            return list;
        } catch (Exception e) {
            return new ArrayList();
        }
    }
}
