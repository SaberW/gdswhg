package com.creatoo.hn.controller.api.apiinside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.delivery.WhgDeliveryService;
import com.creatoo.hn.services.admin.goods.WhgGoodsService;
import com.creatoo.hn.services.admin.goods.WhgShowExhService;
import com.creatoo.hn.services.admin.goods.WhgShowGoodsService;
import com.creatoo.hn.services.admin.personnel.WhgPersonnelService;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.services.admin.supply.WhgSupplyRoomService;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.services.admin.supply.WhgSupplyTraService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.api.apioutside.venue.ApiVenueService;
import com.creatoo.hn.util.UploadUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 申请配送控制器
 * Created by LENUVN on 2017/9/6.
 */
@SuppressWarnings("ALL")
@CrossOrigin
@RestController
@RequestMapping("/api/inside/delivery")
public class InsDeliveryController extends BaseController {

    @Autowired
    private WhgSupplyService whgSupplyService ;

    @Autowired
    private ApiVenueService apiVenueService;

    @Autowired
    private WhgSystemUserService whgSystemUserService ;

    @Autowired
    private WhgDeliveryService whgDeliveryService ;

    @Autowired
    private WhgShowExhService whgShowExhService;

    @Autowired
    private WhgPersonnelService whgPersonnelService;

    @Autowired
    private WhgGoodsService whgGoodsService;
    @Autowired
    private WhgShowGoodsService whgShowGoodsService;

    @Autowired
    private WhgSupplyTraService whgSupplyTraService;

    @Autowired
    private WhgSupplyRoomService whgSupplyRoomService;

    @Autowired
    private WhgFkProjectService whgFkProjectService;


    /**
     * 申请配送获取时间段以及配送范围
     * @param id 用户ID 必传
     * @param supplyId 申请配送内容ID（供需ID） 必传
     * @param request
     * @return data(供需内容配送范围) rows（配送时间段）
     */
    @RequestMapping(value = "/getDeliveryTimes",method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResultBean getDeliveryTimes(String id,String supplyId, HttpServletRequest request){
        ApiResultBean arb = new ApiResultBean() ;
        if(StringUtil.isEmpty(id) || StringUtil.isEmpty(supplyId)) {
            arb.setCode(101);
            arb.setMsg("参数为空或无效！");
            return arb ;
        }
        try {
            WhgSysUser sysUser = whgSystemUserService.t_srchOne(id) ;
            if(sysUser!=null) {
                arb.setData(whgSupplyService.srchOne(supplyId));
                arb.setRows(whgSupplyService.selectTimes4Supply(supplyId)) ;
            } else {
                arb.setCode(102);
                arb.setMsg("用户信息不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arb ;
    }

    /**
     * 检测选择时间是否可用
     * @param date 日期
     * @param supplyId 供需ID
     * @return
     */
    @RequestMapping(value = "/checkDate",method = {RequestMethod.GET,RequestMethod.POST})
    public ApiResultBean checkDate(String date,String supplyId) {
        ApiResultBean arb = new ApiResultBean() ;
        if(StringUtil.isEmpty(date)) {
            arb.setCode(101);
            arb.setMsg("参数为空或参数无效！");
            return arb ;
        }
        int result = whgDeliveryService.checkDateTime(date,supplyId) ;
        if(result > 0) {
            arb.setCode(101);
            arb.setMsg("所选时间段已被配送到其他地区");
        }
        return arb ;
    }

    /**
     * 添加配送申请
     * @param request
     * @param whgDelivery
     * @param dates
     * @param attach
     * @return
     */
    @RequestMapping(value = "/addDelivery",method = {RequestMethod.GET,RequestMethod.POST})
    public ApiResultBean saveDeliveryInfo(HttpServletRequest request, WhgDelivery whgDelivery,String dates,MultipartFile[] attach){
        ApiResultBean arb = new ApiResultBean() ;
        if(StringUtil.isEmpty(dates)) {
            arb.setCode(101);
            arb.setMsg("时间为空或无效！");
            return arb ;
        }
        String attachments = "" ;
        try {
            WhgFkProject whgFkProject = whgFkProjectService.srchOneByFkId(whgDelivery.getFkid());
            Object supplymap = whgSupplyService.srchOne(whgDelivery.getFkid());
            Map map2 = null;
            if (supplymap != null) {
                map2 = (Map) supplymap;
            }
            if (whgFkProject == null && map2 == null) {//修改状态
                arb.setCode(102);
                arb.setMsg("参数为空或参数无效！");
                return arb;
            } else {
                if (whgFkProject != null && whgFkProject.getState() != EnumBizState.STATE_PUB.getValue()) {
                    arb.setCode(103);
                    arb.setMsg("配送主体信息不是已发布状态！");
                    return arb;
                }
                Map map = apiVenueService.selectVenByRoomid(whgDelivery.getFkid());//场馆 活动室 为从属情况 需要 判断场馆是否也下架
                if (map != null) {
                    if ((map.get("state") != null && (Integer) map.get("state") != EnumBizState.STATE_PUB.getValue())) {
                        arb.setCode(103);
                        arb.setMsg("配送主体信息不是已发布状态！");
                        return arb;
                    }
                }
                if (map2 != null && map2.get("state") != null && (Integer) map2.get("state") != EnumBizState.STATE_PUB.getValue()) {
                    arb.setCode(103);
                    arb.setMsg("配送主体信息不是已发布状态！");
                    return arb;
                }
            }
            if(attach!=null) {
                for (int i = 0 ; i < attach.length ; i++) {
                    attachments += UploadUtil.upMultipartFile(attach[i]);
                    if(i != attach.length) {
                        attachments+="," ;
                    }
                }
            }
            if(whgDelivery!=null) {
                if(StringUtil.isNotEmpty(whgDelivery.getCrtuser())) {
                    WhgSysUser sysUser = whgSystemUserService.t_srchOne(whgDelivery.getCrtuser()) ;
                    WhgSysUser sysUser2 = whgSystemUserService.t_srchOne(whgDelivery.getTouser()) ;
                    String phone=null;
                    if(sysUser2!=null){
                        phone=sysUser2.getContactnum();//短信发送对象 电话号码
                    }
                    whgDelivery.setAttachment(attachments);
                    int result = whgDeliveryService.addDelivery(whgDelivery,dates,sysUser,sysUser2,phone) ;
                    if(result <= 0) {
                        arb.setCode(101);
                        arb.setMsg("申请配送失败！");
                    }
                } else {
                    arb.setCode(101);
                    arb.setMsg("参数为空！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            arb.setCode(500);
            arb.setMsg(e.getMessage().equals("Reapplication")?"已申请的不能再次申请!":"服务器异常！");
        }
        return arb;
    }

    private ApiResultBean addDeliveryInfo(WhgDelivery whgDelivery,String dates,MultipartFile[] attach){
        ApiResultBean arb = new ApiResultBean() ;

        try {
            if(StringUtil.isEmpty(dates)) {
                arb.setCode(101);
                arb.setMsg("时间为空或无效！");
                return arb ;
            }
            String attachments = "" ;

            WhgFkProject whgFkProject = whgFkProjectService.srchOneByFkId(whgDelivery.getFkid());
            Object supplymap = whgSupplyService.srchOne(whgDelivery.getFkid());
            Map map2 = null;
            if (supplymap != null) {
                map2 = (Map) supplymap;
            }
            if (whgFkProject == null && map2 == null) {//修改状态
                arb.setCode(102);
                arb.setMsg("参数为空或参数无效！");
                return arb;
            } else {
                if (whgFkProject != null && whgFkProject.getState() != EnumBizState.STATE_PUB.getValue()) {
                    arb.setCode(103);
                    arb.setMsg("配送主体信息不是已发布状态！");
                    return arb;
                }
                Map map = apiVenueService.selectVenByRoomid(whgDelivery.getFkid());//场馆 活动室 为从属情况 需要 判断场馆是否也下架
                if (map != null) {
                    if ((map.get("state") != null && (Integer) map.get("state") != EnumBizState.STATE_PUB.getValue())) {
                        arb.setCode(103);
                        arb.setMsg("配送主体信息不是已发布状态！");
                        return arb;
                    }
                }
                if (map2 != null && map2.get("state") != null && (Integer) map2.get("state") != EnumBizState.STATE_PUB.getValue()) {
                    arb.setCode(103);
                    arb.setMsg("配送主体信息不是已发布状态！");
                    return arb;
                }
            }
            if(attach!=null) {
                for (int i = 0 ; i < attach.length ; i++) {
                    attachments += UploadUtil.upMultipartFile(attach[i]);
                    if(i != attach.length) {
                        attachments+="," ;
                    }
                }
            }
            if(whgDelivery!=null) {
                if(StringUtil.isNotEmpty(whgDelivery.getCrtuser())) {
                    WhgSysUser sysUser = whgSystemUserService.t_srchOne(whgDelivery.getCrtuser()) ;
                    WhgSysUser sysUser2 = whgSystemUserService.t_srchOne(whgDelivery.getTouser()) ;
                    String phone=null;
                    if(sysUser2!=null){
                        phone=sysUser2.getContactnum();//短信发送对象 电话号码
                    }
                    whgDelivery.setAttachment(attachments);
                    int result = whgDeliveryService.addDelivery(whgDelivery,dates,sysUser,sysUser2,phone) ;
                    if(result <= 0) {
                        arb.setCode(101);
                        arb.setMsg("申请配送失败！");
                    }
                } else {
                    arb.setCode(101);
                    arb.setMsg("参数为空！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            arb.setCode(500);
            arb.setMsg("服务器异常！");
        }
        return arb;
    }

    @RequestMapping(value = "/addDelivery4callback",method = {RequestMethod.GET,RequestMethod.POST})
    public Object saveDeliveryInfo4callback(HttpServletRequest request, WhgDelivery whgDelivery, String dates, MultipartFile[] attach){
        ModelAndView mav = new ModelAndView();

        String formcallpage = request.getParameter("formcallpage");
        String callback = request.getParameter("callback");

        ApiResultBean arb = new ApiResultBean();

        arb = this.addDeliveryInfo(whgDelivery, dates, attach);
        /*arb.setCode(500);
        arb.setMsg("参数错误");*/

        try {
            if (arb.getMsg()!=null) {
                String msg = URLEncoder.encode(arb.getMsg(), "UTF-8");
                arb.setMsg(msg);
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        //String data = JSONObject.toJSONString(arb);

        formcallpage += "?callback="+callback;
        //formcallpage += "&data="+data;
        formcallpage += "&code="+arb.getCode();
        formcallpage += "&msg="+arb.getMsg();
        mav.setViewName("redirect:"+formcallpage);
        return mav;
    }


    /**
     * 获取配送指数
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/showStarts", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object showStarts(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            int count = this.whgDeliveryService.t_srchSucDelivery(id);
            arb.setData(count);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 获取说明
     * @param id
     * @param fktype
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/showMemo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object showMemo(String id,String fktype){
        ApiResultBean arb = new ApiResultBean();
        try {
            if(fktype!=null&&fktype.equals(EnumTypeClazz.TYPE_PER.getValue())){// 25 专家人才
                WhgPersonnel obj=whgPersonnelService.t_srchOne(id);
                if(obj!=null){
                    arb.setData(obj.getPerexplain());
                }
            }else if(fktype!=null&&fktype.equals(EnumTypeClazz.TYPE_GOODS.getValue())){//20 文化设施
              WhgGoods obj=  whgGoodsService.srchOne(id);
                if(obj!=null){
                    arb.setData(obj.getGoodsexplain());
                }
            }else if(fktype!=null&&fktype.equals(EnumTypeClazz.TYPE_SHOWGOODS.getValue())){//21 文艺演出
                WhgShowGoods obj=whgShowGoodsService.srchOne(id);
                if(obj!=null){
                    arb.setData(obj.getShowexplain());
                }
            }else if(fktype!=null&&fktype.equals(EnumTypeClazz.TYPE_SUP.getValue())){//26 供需类型
               WhgSupply obj= whgSupplyService.t_srch(id);
                if(obj!=null){
                    arb.setData(obj.getNotice());
                }
            }else if(fktype!=null&&fktype.equals(EnumTypeClazz.TYPE_ROOM.getValue())){//3 活动室
                WhgSupplyRoom obj=whgSupplyRoomService.t_srch(id);
                if(obj!=null){
                    arb.setData(obj.getNotice());
                }
            }else if(fktype!=null&&fktype.equals(EnumTypeClazz.TYPE_TRAIN.getValue())){//5 培训
                WhgSupplyTra obj=whgSupplyTraService.srchOne(id);
                if(obj!=null){
                    arb.setData(obj.getNotice());
                }
            }else if(fktype!=null&&fktype.equals(EnumTypeClazz.TYPE_EXH.getValue())){//23 展览展示
                WhgShowExh obj=whgShowExhService.srchOne(id);
                if(obj!=null){
                    arb.setData(obj.getExhexplain());
                }
            }
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }



    /**
     * 获取其他人成功配送信息
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/showDelivery", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object showDelivery(String id,
                               @RequestParam(value = "page", defaultValue = "1", required = false)int page,
                               @RequestParam(value = "pageSize", defaultValue = "10",required = false)int pageSize){
        ApiResultBean arb = new ApiResultBean();
        try {
            Map  paramMap = new HashMap();
            paramMap.put("delstate", 0);
            paramMap.put("fkid", id);
            paramMap.put("states", Arrays.asList(2,3));
            paramMap.put("isApiSelect", true);
            PageInfo pageInfo = this.whgDeliveryService.srch_cult_delivery(page, pageSize, paramMap);
            arb.setRows(pageInfo.getList());
            arb.setPageInfo(pageInfo);

        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }


    /**
     * 我的配送 与 我申请的配送接口
     * @param index
     * @param size
     * @param id 用户ID
     * @param type 1:我的配送 2：我申请的配送
     * @param deliveryType 1：当前配送 2：历史配送
     * @param request
     * @return
     */
    @RequestMapping(value = "/myDelivery",method = {RequestMethod.POST,RequestMethod.GET})
    public ApiResultBean myApplyDelivery(@RequestParam(name = "index",required = false,defaultValue = "1")Integer index,
                                         @RequestParam(name = "size",required = false,defaultValue = "5")Integer size,
            String id,Integer type,@RequestParam(name = "crtuser",required = false,defaultValue = "")String crtuser,@RequestParam(name = "touser",required = false,defaultValue = "")String touser,Integer deliveryType,HttpServletRequest request){
        ApiResultBean arb = new ApiResultBean() ;
        if(StringUtil.isEmpty(id) || type == null || deliveryType == null) {
            arb.setCode(101);
            arb.setMsg("参数为空！");
            return arb ;
        }
        try {
            WhgSysUser sysUser = whgSystemUserService.t_srchOne(id) ;
            if(sysUser!=null) {
                PageInfo<WhgDelivery> pageInfo = whgDeliveryService.t_ApiSrchList4p(index,size,crtuser,touser,type,deliveryType,sysUser) ;
               List<WhgDelivery> list=new ArrayList<WhgDelivery>();
               for(WhgDelivery whgDelivery:pageInfo.getList()) {
                   WhgSysCult sysCultTo = whgSystemUserService.t_srchUserCult(whgDelivery.getCrtuser());
                   if(sysCultTo!=null){
                       whgDelivery.setCrtuser(sysCultTo.getName());
                   }
                   WhgSysCult sysCultTo2 = whgSystemUserService.t_srchUserCult(whgDelivery.getTouser());
                   if(sysCultTo2!=null){
                       whgDelivery.setTouser(sysCultTo2.getName());
                   }

                   List<WhgDeliveryTime> deliveryTime=whgDeliveryService.getPstime(whgDelivery.getId());
                   StringBuffer sb=new StringBuffer();
                   for(WhgDeliveryTime delTime:deliveryTime){
                       sb.append(delTime.getDeliverytime()+" ");
                   }
                   whgDelivery.setMemo(sb.toString());
                   list.add(whgDelivery);
               }
                pageInfo.setList(list);
                arb.setPageInfo(pageInfo);
            } else {
                arb.setMsg("用户信息不存在！");
                arb.setCode(102);
            }
        } catch (Exception e) {
            e.printStackTrace();
            arb.setMsg("服务器异常！");
            arb.setCode(500);
        }
        return arb ;
    }


}
