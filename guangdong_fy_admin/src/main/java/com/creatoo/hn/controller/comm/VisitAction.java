package com.creatoo.hn.controller.comm;

import com.creatoo.hn.services.comm.VisitService;
import com.creatoo.hn.util.bean.ResponseBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网站流量访问统计控制器
 * Created by wangxl on 2017/6/19.
 */
@CrossOrigin
@RestController
@RequestMapping("/visit")
public class VisitAction {
    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass());

    /**
     * 保存流量访问控制服务器
     */
    @Autowired
    public VisitService visitService;

    /**
     * 保存访问记录
     * <script src="http://localhost:8080/static/admin/js/pc.js?type=1" id="whgtongji"></script>
     * @param vType 访问类型(1:PC,2:weixin,3:Android,4:IOS)
     * @param vDate 访问日期(yyyy-MM-dd)
     * @param vIp 访问IP(127.0.0.1)
     * @param visitor 访客(访客标识)
     * @param vPage 访问页面(url)
     * @param vCount 访问量
     * @return
     */
    @RequestMapping(value = "/note", method = RequestMethod.POST)
    public ResponseBean visit(String vType, String vDate, String vIp, String visitor, String vPage, String vCount){
        ResponseBean rb = new ResponseBean();

        try{
            visitService.saveVisit(vType, vDate, vIp, visitor, vPage, vCount);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg(e.getMessage());
        }
        return rb;
    }
}
