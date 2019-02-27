package com.creatoo.hn.actions.admin.rep;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.services.admin.rep.RepSmsService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rbg on 2017/6/28.
 */

@Controller
@RequestMapping("/admin/rep/sms")
public class WhgRepSmsAction {
    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private RepSmsService repSmsService;

    private Date paramMonth(WebRequest request){
        String year = request.getParameter("year");
        String month = request.getParameter("month");

        try {
            if (year!=null && month!=null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                return sdf.parse(year+"-"+month);
            }
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    @RequestMapping("/smsVenCount")
    @ResponseBody
    public Object smsVenCount(WebRequest request, int page, int rows){
        ResponseBean resb = new ResponseBean();
        try{
            Date tagMonth = this.paramMonth(request);

            PageInfo pageInfo = this.repSmsService.selectRepSmsCount4Room(page, rows, tagMonth);
            resb.setRows( pageInfo.getList() );
            resb.setTotal( pageInfo.getTotal() );
        }catch (Exception e){
            log.error(e.getMessage(), e);
            resb.setRows( new ArrayList() );
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

    @RequestMapping("/smsTraCount")
    @ResponseBody
    public Object smsTraCount(WebRequest request, int page, int rows){
        ResponseBean resb = new ResponseBean();
        try{
            Date tagMonth = this.paramMonth(request);

            PageInfo pageInfo = this.repSmsService.selectRepSmsCount4Tra(page, rows, tagMonth);
            resb.setRows( pageInfo.getList() );
            resb.setTotal( pageInfo.getTotal() );
        }catch (Exception e){
            log.error(e.getMessage(), e);
            resb.setRows( new ArrayList() );
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

    @RequestMapping("/smsActCount")
    @ResponseBody
    public Object smsActCount(WebRequest request, int page, int rows){
        ResponseBean resb = new ResponseBean();
        try{
            Date tagMonth = this.paramMonth(request);

            PageInfo pageInfo = this.repSmsService.selectRepSmsCount4Act(page, rows, tagMonth);
            resb.setRows( pageInfo.getList() );
            resb.setTotal( pageInfo.getTotal() );
        }catch (Exception e){
            log.error(e.getMessage(), e);
            resb.setRows( new ArrayList() );
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }

    @RequestMapping("/zxCount")
    @ResponseBody
    public Object zxCount(WebRequest request, int page, int rows){
        ResponseBean resb = new ResponseBean();
        try{
            Date tagMonth = this.paramMonth(request);

            PageInfo pageInfo = this.repSmsService.selectRepZxCount(page, rows, tagMonth);
            resb.setRows( pageInfo.getList() );
            resb.setTotal( pageInfo.getTotal() );
        }catch (Exception e){
            log.error(e.getMessage(), e);
            resb.setRows( new ArrayList() );
            resb.setSuccess(ResponseBean.FAIL);
        }
        return resb;
    }
}
