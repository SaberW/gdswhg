package com.creatoo.hn.controller.admin.delivery;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgDelivery;
import com.creatoo.hn.dao.model.WhgDeliveryTime;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.delivery.WhgDeliveryService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.user.WhgUserService;
import com.creatoo.hn.services.api.apiinside.InsSupplyService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LENUVN on 2017/7/27.
 */
@Controller
@RequestMapping("/admin/delivery")
public class WhgDeliveryController extends BaseController {

    @Autowired
    private WhgDeliveryService whgDeliveryService;
    @Autowired
    private SMSService smsService;

    /**
     * 系统管理员服务尖
     */
    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private InsSupplyService insSupplyService;

    @Autowired
    public Environment env;
    /**
     * 进入type(list|add|edit|view)视图
     *
     * @param request 请求对象
     * @param type    视图类型(list|add|edit|view)
     * @return 视图
     */
    @RequestMapping("/view/{type}/{clazz}")
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type") String type, @PathVariable("clazz") String clazz) {
        ModelAndView view = new ModelAndView();
        try {
            view.addObject("type", type);
            view.addObject("clazz", clazz);
            String rootPath = env.getProperty("upload.local.server.addr");
            view.addObject("rootPath", rootPath);
            if ("add".equalsIgnoreCase(type)) {
                String id = request.getParameter("id");
                String targetShow = request.getParameter("targetShow");
                if (id != null) {
                    view.addObject("id", id);
                    view.addObject("targetShow", targetShow);
                    WhgDelivery whgDelivery=whgDeliveryService.t_srchOne(id);
                    if(whgDelivery.getCrtuser()!=null&&!whgDelivery.getCrtuser().equals("")){
                        WhgSysCult sysCultCrt = whgSystemUserService.t_srchUserCult(whgDelivery.getCrtuser());
                        whgDelivery.setCrtuser(sysCultCrt.getName());
                    }else {
                        whgDelivery.setCrtuser("");
                    }
                    if(whgDelivery.getTouser()!=null&&!whgDelivery.getTouser().equals("")){
                        if(whgDelivery.getTouser().equals("2015121200000000")){
                            whgDelivery.setTouser("广东省文化馆");
                        }else{
                            WhgSysCult sysCultTo = whgSystemUserService.t_srchUserCult(whgDelivery.getTouser());
                            whgDelivery.setTouser(sysCultTo.getName());
                        }
                    }else {
                        whgDelivery.setTouser("");
                    }
                    List<WhgDeliveryTime> timeList=whgDeliveryService.getPstime(whgDelivery.getId());
                    StringBuffer sb=new StringBuffer();
                    for(WhgDeliveryTime deliveryTime:timeList){
                        sb.append(deliveryTime.getDeliverytime());
                        sb.append(" ");
                    }
                    if(whgDelivery.getState()==1){
                        view.addObject("deliveryState", "待审核");
                    }else  if(whgDelivery.getState()==2){
                        view.addObject("deliveryState", "审核通过");
                    }else if(whgDelivery.getState()==3){
                        view.addObject("deliveryState", "完成配送");
                    }else if(whgDelivery.getState()==0){
                        view.addObject("deliveryState", "驳回");
                    }
                    if(whgDelivery.getAttachment()!=null&&whgDelivery.getAttachment().indexOf(",")!=-1){
                        String attchment= whgDelivery.getAttachment().substring(0,whgDelivery.getAttachment().lastIndexOf(","));
                        whgDelivery.setAttachment(rootPath+attchment);
                    }
                    view.addObject("cult", whgDelivery);
                    view.addObject("deliveryTime", sb.toString());
                    view.setViewName("admin/delivery/view_edit");
                } else {
                    view.setViewName("admin/delivery/view_add");
                }
            } else {
                view.setViewName("admin/delivery/view_list");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return res
     */
    @RequestMapping(value = "/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(HttpServletRequest request, WhgDelivery cultHeritage,HttpSession session) {
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser whgSysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            PageInfo pageInfo = whgDeliveryService.t_srchList4p(request, cultHeritage,null,whgSysUser);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
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
    public ResponseBean add(WhgDelivery whgPersonnel, HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            String newId = IDUtils.getID();
            whgPersonnel.setId(newId);
           // this.whgDeliveryService.t_add(request, whgPersonnel);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 编辑
     *
     * @param cultHeritage cultHeritage
     * @return res
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public ResponseBean edit(WhgDelivery cultHeritage,String pscity,HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        String result=request.getParameter("result");
        String pstime=request.getParameter("pstime");

       // @PathVariable("pstime")String[] pstime,@PathVariable("result")String result
        try {
            cultHeritage=whgDeliveryService.t_srchOne(cultHeritage.getId());
            cultHeritage.setRegion(pscity);
            if(result!=null&&!result.equals("")){
                cultHeritage.setState(Integer.parseInt(result));
            }
            this.whgDeliveryService.t_edit(cultHeritage,pstime);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @RequestMapping(value = "/getPstime")
    @ResponseBody
    public ResponseBean getPstime(String id) {
        ResponseBean res = new ResponseBean();
        Map map=new HashMap();
        StringBuffer sb=new StringBuffer();
        try {
            List timeList=whgDeliveryService.getPstime(id);
            map.put("times", JSON.toJSONString(timeList));
            res.setData(map);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }


    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public ResponseBean del(String id) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgDeliveryService.t_del(id);

        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("删除失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }



    /**
     * 还原删除
     *
     * @param id id
     * @return res
     */
    @RequestMapping("/firstOpenYMD")
    @ResponseBody
    public ResponseBean firstOpenYMD(String id, @RequestParam(value = "timeid", required = false) String timeid) {
        ResponseBean rb = new ResponseBean();
        try {
            Object rest = this.insSupplyService.firstOpenTime(id, timeid);
            rb.setData(rest);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("还原失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/openDays")
    @ResponseBody
    public ResponseBean getOpenDays(String id, String year, String month, String day,
                                    @RequestParam(value = "timeid", required = false) String timeid){
        ResponseBean arb = new ResponseBean();
        arb.setSuccess("true");
        try {
            Map rest = this.insSupplyService.getGxtimeDays(id, year, month, timeid);
            if(rest==null){
                arb.setSuccess("false");
                arb.setErrormsg("该月份没有设置配送时间");
                return arb;
            }
            List<Integer> dayList=(ArrayList<Integer>)rest.get("days");
            String dayslen=(String) rest.get("dayslen");
            boolean bool=false;
            //暂时屏蔽掉 配送范围的时间筛选
            for(Integer days:dayList){
                if(Integer.parseInt(day)==days){
                    bool=true;
                }
            }
            if(bool==false){
                arb.setSuccess("false");
                arb.setErrormsg("该日期不在配送"+dayslen+"范围之内");
                return arb;
            }

            List<Integer>  useDayList=(ArrayList<Integer>)rest.get("useDays");
            boolean bool2=false;
            for(Integer days:useDayList){
                if(Integer.parseInt(day)==days){
                    bool2=true;
                }
            }
            if(bool2==true){
                arb.setSuccess("false");
                arb.setErrormsg("该日期已被占用");
                return arb;
            }
        } catch (Exception e) {
            arb.setSuccess(ResponseBean.FAIL);
            arb.setErrormsg("查询数据失败");
            log.error(e.getMessage(),e);
        }

        return arb;
    }
    @RequestMapping("/checkDays")
    @ResponseBody
    public ResponseBean checkDays(String id,String fkid, String datestr){
        ResponseBean arb = new ResponseBean();
        arb.setSuccess("true");
        try {
            String supplyTimes = this.insSupplyService.getOpenDays(fkid);
            String deliveryTimes = this.insSupplyService.getUseDays(fkid);
            String[] dates=datestr.split(",");

            boolean bool=false;
            boolean bool2=false;
            StringBuffer noopenDays=new StringBuffer();
            StringBuffer useDays=new StringBuffer();
            for(String dd:dates){
                if(!dd.equals("")){
                    if(supplyTimes.indexOf(dd)==-1){
                       // bool=true; //暂时屏蔽掉 配送范围的时间筛选
                        noopenDays.append(dd);
                        noopenDays.append(" ");
                    }
                    if(deliveryTimes.indexOf(dd)!=-1){
                        bool2=true;
                        useDays.append(dd);
                        useDays.append(" ");
                    }
                }
            }
            if(bool==true){
                arb.setSuccess("false");
                arb.setErrormsg(noopenDays.toString()+"等日期不在配送范围之内");
                return arb;
            }
            if(bool2==true){
                arb.setSuccess("false");
                arb.setErrormsg(useDays.toString()+"等日期已被占用");
                return arb;
            }
            //YI@ 给申请者发送短信；
           /* WhgDelivery whgDelivery = whgDeliveryService.getWhgDelivery(id);
            String crtuser = whgDelivery.getCrtuser();
            WhgSysUser whgSysUser = whgSystemUserService.t_srchOne(crtuser);
            String telephone = whgSysUser.getContactnum();
            Map<String,String> map = new HashMap<>();
            map.put("name","乐乐了");
            smsService.t_sendSMS(telephone,"REL_CHECK_PASS",map,crtuser);*/


        } catch (Exception e) {
            arb.setSuccess(ResponseBean.FAIL);
            arb.setErrormsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 修改状态
     *
     * @param ids        id
     * @param formstates 当前状态
     * @param tostate    要改变的状态
     * @param session    session
     * @return res
     */
    @RequestMapping("/updstate")
    @ResponseBody
    public ResponseBean updstate(String ids, String formstates, int tostate, HttpSession session) {
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            res = this.whgDeliveryService.t_updstate(ids, formstates, tostate, sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("状态更改失败");
            log.error(res.getErrormsg() + " formstate: " + formstates + " tostate:" + tostate + " ids: " + ids, e);
        }
        return res;
    }



}
