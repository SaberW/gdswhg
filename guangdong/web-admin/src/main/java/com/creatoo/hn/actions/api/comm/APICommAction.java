package com.creatoo.hn.actions.api.comm;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhCollection;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.home.userCenter.CollectionService;
import com.creatoo.hn.utils.ReqParamsUtil;
import com.creatoo.hn.utils.WhConstance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 公共API接口
 * Created by wangxl on 2017/4/13.
 */
@RestController
@RequestMapping("/api/com")
public class APICommAction {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private CollectionService colleService;

    @Autowired
    private CommService commService;

    /**
     *
     * 判断用户是否点亮点赞
     * param : reftyp-收藏关联类型/  refid-收藏关联id/   id-点赞的标识，userid或者一个本机的标识 (三个参数都为必输)
     * @return JSON : {
     * "success" : "1" 、"2"                       //1表示操作成功，2表示已点赞。其它为失败
     * "errormsg" :    //101-操作失败
     * "data"  :  "num"  //点赞数量
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/isLaud", method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseBean IsGood(HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        int num = 0;
        try {
            //取得收藏关联参数
            String reftyp = request.getParameter("reftyp"); // 收藏关联类型
            String refid = request.getParameter("refid"); // 收藏关联id
            String id = request.getParameter("id");  //点赞的标识，userid或者一个本机的标识
            //判断是否有点赞记录
            boolean isgood = this.colleService.IsGood(id, reftyp, refid);
            if (isgood) {
                res.setSuccess("2");// 已点赞
            }
            //设置被点赞次数
            num = this.colleService.dianZhanShu(reftyp, refid);
            res.setData(num);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("101");
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 添加点赞接口
     * param : reftyp-收藏关联类型/  refid-收藏关联id/   id-点赞的标识，userid或者一个本机的标识 (三个参数都为必输)
     *@return JSON : {
     * "success" : "1"                       //1表示操作成功
     * "errormsg" :    //101-操作失败
     * "data"  :  "num"  //点赞数量
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/addLaud", method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseBean addGood(HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        int num = 0;
        WhCollection whcolle = new WhCollection();
        try {
            //取得收藏关联参数
            String reftyp = request.getParameter("reftyp"); // 收藏关联类型
            String refid = request.getParameter("refid"); // 收藏关联id
            String id = request.getParameter("id");  //点赞的标识，userid或者一个本机的标识
            whcolle.setCmid(this.commService.getKey("whcolle"));
            whcolle.setCmreftyp(reftyp);  // 收藏关联类型
            whcolle.setCmrefid(refid);   // 收藏关联id
            whcolle.setCmuid(id);  //点赞的标识，userid或者一个本机的标识
            whcolle.setCmdate(new Date()); // 收藏时间
            whcolle.setCmopttyp("2"); // 操作类型为点赞
            this.colleService.addGood(whcolle);
            num = this.colleService.dianZhanShu(whcolle.getCmreftyp(), whcolle.getCmrefid());
            res.setData(num);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("101");
        }
        return res;
    }
}
