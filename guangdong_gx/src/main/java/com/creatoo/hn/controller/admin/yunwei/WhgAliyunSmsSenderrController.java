package com.creatoo.hn.controller.admin.yunwei;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgYwiAliysmsSenderr;
import com.creatoo.hn.services.comm.SmsAliyunService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/yunwei/aliysmserr")
public class WhgAliyunSmsSenderrController extends BaseController {

    @Autowired
    private SmsAliyunService smsAliyunService;

    @RequestMapping("/view/list")
    public String viewList(){
        return "/admin/yunwei/aliysmserr/view_list";
    }

    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WhgYwiAliysmsSenderr record){
        ResponseBean rb = new ResponseBean();
        try {
            PageInfo pageInfo = this.smsAliyunService.selectAliysmsSendError4p(page, rows, record);
            rb.setRows(pageInfo.getList());
            rb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("查询数据失败");
        }
        return rb;
    }
}
