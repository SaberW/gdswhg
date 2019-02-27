package com.creatoo.hn.controller.admin.train;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.admin.train.KaoqinTraService;
import com.creatoo.hn.util.bean.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 培训考勤
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/admin/train/kaoqin")
public class KaoqinTraController extends BaseController {

    @Autowired
    private KaoqinTraService kaoqinTraService;

    @RequestMapping("/view/list")
    public ModelAndView view(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/train/kaoqin/view_list");
        return mav;
    }

    @RequestMapping("/srchlist")
    public Object srchList(HttpServletRequest request){
        String traid = request.getParameter("traid");
        String userid = request.getParameter("userid");
        String yyyy = request.getParameter("yyyy");
        String mm = request.getParameter("mm");

        ResponseBean resb = new ResponseBean();
        resb.setData(new ArrayList());
        resb.setRows(new ArrayList());
        if (traid==null && traid.isEmpty()){
            return resb;
        }

        Map record = new HashMap();
        record.put("traid", traid);
        record.put("userid", userid);
        record.put("yyyy", yyyy);
        record.put("mm", mm);

        try {
            List list = this.kaoqinTraService.selectTraKqList(record);
            Map kqInfo = this.kaoqinTraService.getTraKqinfo(list);
            resb.setData((List) kqInfo.get("kechengs"));
            resb.setRows((List) kqInfo.get("kqusers"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return resb;
    }

    /**
     * 修改考勤的缺勤为签到
     * @return
     */
    @RequestMapping("/updateKq2qd")
    public Object updateKq2qd(String userid, String traid, String enrolid, String courseid){
        ResponseBean rb = new ResponseBean();
        try {
            rb = this.kaoqinTraService.updateKq2Qd(userid, traid, enrolid, courseid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("操作出错");
        }
        return rb;
    }
}
