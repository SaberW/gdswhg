package com.creatoo.hn.controller.api.apitrain;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgTraEnrol;
import com.creatoo.hn.dao.model.WhgTraleave;
import com.creatoo.hn.dao.model.WhgUser;
import com.creatoo.hn.services.admin.train.KaoqinTraService;
import com.creatoo.hn.services.admin.user.WhgUserService;
import com.creatoo.hn.services.api.apioutside.collection.ApiCollectionService;
import com.creatoo.hn.services.api.apitrain.ApiMyTraService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBMState;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/10/13.
 */
@Controller
@RequestMapping("/api/px/mytra")
@CrossOrigin
public class ApiMyTraController extends BaseController{

    @Autowired
    private ApiMyTraService apiMyTraService;

    @Autowired
    private WhgUserService whgUserService;

    @Autowired
    private ApiCollectionService colleService;

    @Autowired
    private KaoqinTraService kaoqinTraService;

    /**
     * 获取用户培训订单
     * @param userid
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/list",method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResultBean getMyTraEnrol(@RequestParam(value = "page", defaultValue = "1")int page,
                                       @RequestParam(value = "pageSize", defaultValue = "10")int pageSize,
                                       @RequestParam(value = "userid",required = true)String userid,
                                       @RequestParam(value = "type",required = true)Integer type,
                                       @RequestParam(value = "cultid",required = false)String cultid){
        ApiResultBean arb = new ApiResultBean();
        Map data = new HashMap();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 2);
        data.put("twodaytime", c.getTime());
        data.put("nowTime", new Date());
        if(null == userid){
            arb.setCode(101);
            arb.setMsg("用户ID不能为空");
            return arb;
        }
        try {
            PageInfo info  = apiMyTraService.t_getUserTraEnrol(page,pageSize,userid,type,cultid);
            arb.setRows(info.getList());
            arb.setPageInfo(info);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(102);
            arb.setMsg("查询失败");
        }
        arb.setData(data);
        return arb;
    }

    /**
     * 请假
     * @param traid  培训id
     * @param userid  用户id
     * @param courseid  课程id
     * @param title  培训名称
     * @param starttime  上课开始时间
     * @param endtime  上课结束时间
     * @param name  申请人
     * @param cause  请假原因
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/leave",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ApiResultBean saveTraLeave(@RequestParam(value = "traid",required = true)String traid,
                                      @RequestParam(value = "userid",required = true)String userid,
                                      @RequestParam(value = "courseid",required = true)String courseid,
                                      @RequestParam(value = "title",required = true)String title,
                                      @RequestParam(value = "starttime",required = true)String starttime,
                                      @RequestParam(value = "endtime",required = true)String endtime,
                                      @RequestParam(value = "name",required = true)String name,
                                      @RequestParam(value = "cause",required = true)String cause){

        ApiResultBean arb = new ApiResultBean();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(starttime);
        Date date = new Date(lt);
        long _lt = new Long(endtime);
        Date _date = new Date(_lt);

        WhgTraleave leave = new WhgTraleave();
        if(cause == null || "".equals(cause.trim())){
            arb.setCode(102);
            arb.setMsg("请填写请假事由！");
            return arb;
        }

        try {
            leave.setTraid(traid);
            leave.setUserid(userid);
            leave.setCause(cause);
            leave.setCourseid(courseid);
            leave.setTratitle(title);
            leave.setStarttime(date);
            leave.setEndtime(_date);
            leave.setProposer(name);
            arb = apiMyTraService.t_saveTraLeave(leave);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(101);
            arb.setMsg("保存失败");
        }
        return arb;
    }

    /**
     *我的收藏
     * @param userid
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/myCollect",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Object collectList(@RequestParam(value = "userid",required = true)String userid,
                              @RequestParam(value = "cmreftyp",required = true)Integer cmreftyp,
                              @RequestParam(value = "page", defaultValue = "1")Integer page,
                              @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo info = this.colleService.SelectMyTraitmColle(userid,cmreftyp,page,pageSize);
            arb.setRows(info.getList());
            arb.setPageInfo(info);
            arb.setData(new Date());
        } catch (Exception e) {
            arb.setMsg("获取收藏列表有误");
            arb.setCode(1);
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 删除我的报名
     * @param
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping("/offenroll")
    public ApiResultBean delmyenroll(@RequestParam(value = "orderId",required = true)String orderId,
                                     @RequestParam(value = "userId",required = true)String userId,
                                     @RequestParam(value = "state",required = false)Integer state){
        ApiResultBean arb = new ApiResultBean();
        WhgTraEnrol enrol = new WhgTraEnrol();
        String success = "0";
        String errmsg = "";
        if(userId == null && "".equals(userId)){
            arb.setCode(103);
            arb.setMsg("用户ID不能为空");
            return arb;
        }
        if(orderId == null && "".equals(orderId)){
            arb.setCode(104);
            arb.setMsg("订单ID不能为空");
            return arb;
        }
        if(state != null && state != 2){
            enrol.setState(EnumBMState.BM_QXBM.getValue());
            enrol.setId(orderId);
            try {
                this.apiMyTraService.updateMyEnroll(enrol);
                //清除相关报名的临时上传目录
            } catch (Exception e) {
                arb.setCode(101);
                arb.setMsg("取消失败");
                log.error(e.getMessage(),e);
            }
        }else{
            try {
                this.apiMyTraService.delEnroll(orderId);
                //清除相关报名的临时上传目录
            } catch (Exception e) {
                arb.setCode(102);
                arb.setMsg("删除失败");
                log.error(e.getMessage(),e);
            }
        }
        return arb;
    }

    /**
     * 删除我的报名
     * @param
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping("/delEnrol")
    public ApiResultBean delmyenroll(@RequestParam(value = "orderId",required = true)String orderId,
                                     @RequestParam(value = "userId",required = true)String userId){
        ApiResultBean arb = new ApiResultBean();
        if(orderId == null && "".equals(orderId)){
            arb.setCode(104);
            arb.setMsg("订单ID不能为空");
            return arb;
        }
        try {
            this.apiMyTraService.delEnroll(orderId);
        } catch (Exception e) {
            arb.setCode(102);
            arb.setMsg("删除失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 课程表当前课程
     * @param userid
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/nowkecheng",method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResultBean getkecheng(@RequestParam(value = "page", defaultValue = "1")int page,
                                    @RequestParam(value = "pageSize", defaultValue = "10")int pageSize,
                                    @RequestParam(value = "userid",required = true)String userid,
                                    @RequestParam(value = "cultid",required = false)String cultid){
        ApiResultBean arb = new ApiResultBean();
        Map data = new HashMap();
        if(null == userid){
            arb.setCode(101);
            arb.setMsg("用户ID不能为空");
            return arb;
        }
        try {
            Date now = new Date();
            PageInfo info  = apiMyTraService.t_getkecheng(page,pageSize,userid,cultid,now);
            arb.setRows(info.getList());
            arb.setPageInfo(info);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(102);
            arb.setMsg("查询失败");
        }
        return arb;
    }


    /**
     * 课程表报名的所有培训
     * @param userid
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/mytraList",method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResultBean srchAllKecheng(@RequestParam(value = "userid",required = true)String userid,
                                    @RequestParam(value = "cultid",required = false)String cultid){
        ApiResultBean arb = new ApiResultBean();
        Map data = new HashMap();
        if(null == userid){
            arb.setCode(101);
            arb.setMsg("用户ID不能为空");
            return arb;
        }
        try {
            Date now = new Date();
            List info  = apiMyTraService.t_srchKecheng(userid,cultid);
            data.put("nowdate",now);
            data.put("infoList",info);
            arb.setData(data);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(102);
            arb.setMsg("查询失败");
        }
        return arb;
    }


    /**
     *根据培训id查询课程
     * @param page
     * @param pageSize
     * @param traid
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/kecheng",method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResultBean srchKecheng4Tid(@RequestParam(value = "page", defaultValue = "1")int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10")int pageSize,
                                        @RequestParam(value = "traid",required = true)String traid,
                                         @RequestParam(value = "userid",required = true)String userid){
        ApiResultBean arb = new ApiResultBean();
        Map data = new HashMap();
        try {
            Date now = new Date();
            PageInfo info  = apiMyTraService.t_srchKecheng4Tid(page,pageSize,traid,userid);
            arb.setData(now);
            arb.setRows(info.getList());
            arb.setPageInfo(info);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(102);
            arb.setMsg("查询失败");
        }
        return arb;
    }

    /**
     * 正在上课课程
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/inClass",method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResultBean srchinClass(@RequestParam(value = "userid",required = true)String userid,
                                     @RequestParam(value = "cultid",required = false)String cultid){
        ApiResultBean arb = new ApiResultBean();
        try {
            Date now = new Date();
            List info  = apiMyTraService.t_srchinClass(userid,cultid,now);
            arb.setData(info);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(102);
            arb.setMsg("查询失败");
        }
        return arb;
    }

    /**
     * weixin课程表
     * @param userid
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/wxKeCheng",method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResultBean getWxKeCheng(@RequestParam(value = "page", defaultValue = "1")int page,
                                    @RequestParam(value = "pageSize", defaultValue = "10")int pageSize,
                                    @RequestParam(value = "userid",required = true)String userid,
                                    @RequestParam(value = "cultid",required = false)String cultid){
        ApiResultBean arb = new ApiResultBean();
        Map data = new HashMap();
        if(null == userid){
            arb.setCode(101);
            arb.setMsg("用户ID不能为空");
            return arb;
        }
        try {
            Date now = new Date();
            PageInfo info  = apiMyTraService.t_getWxKeCheng(page,pageSize,userid,cultid,now);
            arb.setRows(info.getList());
            arb.setPageInfo(info);
            arb.setData(now);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            arb.setCode(102);
            arb.setMsg("查询失败");
        }
        return arb;
    }


    /**
     * 培训课程签到  POST请求
     * 访问路径 /api/px/mytra/sign
     * @param orderid 订单id
     * @param courseid 课程id
     * @param longitude 经度
     * @param latitude 纬度
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/sign",method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResultBean signUp(String orderid,String courseid,String longitude,String latitude){
        ApiResultBean arb = new ApiResultBean();
        try{
            arb = this.apiMyTraService.t_signup(orderid,courseid,longitude,latitude);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            arb.setCode(101);
            arb.setMsg("操作失败");
        }
        return arb;
    }

    /**
     * 培训课程签到  POST请求
     * 访问路径 /api/px/mytra/sign
     *
     * @param longitude 经度
     * @param latitude  纬度
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/usersign", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseBean usersign(String usernumber, String longitude, String latitude) {
        ResponseBean responseBean = new ResponseBean();
        try {
            if (longitude != null && longitude != null) {
                responseBean = this.apiMyTraService.t_usersignup(usernumber, longitude, latitude);
            } else {
                responseBean.setSuccess("100");
                responseBean.setErrormsg("参数有误");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            responseBean.setSuccess("101");
            responseBean.setErrormsg("操作失败");
        }
        return responseBean;
    }


    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/userkq4tra", method = {RequestMethod.GET, RequestMethod.POST})
    public Object userTraKq(String userid, String traid,
                                  @RequestParam(value = "page", defaultValue = "1")int page,
                                  @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
        ApiResultBean arb = new ApiResultBean();

        if (userid==null || userid.isEmpty() || traid==null || traid.isEmpty()){
            arb.setMsg("缺少用户ID或培训ID参数");
            arb.setCode(100);
            return arb;
        }

        try {
            Map record = new HashMap();
            record.put("userid", userid);
            record.put("traid", traid);

            PageInfo pageInfo = this.kaoqinTraService.getTraKqinfo4User(page, pageSize, record);
            arb.setRows(pageInfo.getList());
            arb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setMsg("查询数据失败");
            arb.setCode(500);
        }

        return arb;
    }

}
