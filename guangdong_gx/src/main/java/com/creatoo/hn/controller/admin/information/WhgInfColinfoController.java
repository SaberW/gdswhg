package com.creatoo.hn.controller.admin.information;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgInfColinfo;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.information.WhgInfColInfoService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumOptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by LENUVN on 2017/7/25.
 */
@RequestMapping("/admin/information")
@RestController
public class WhgInfColinfoController extends BaseController {

    @Autowired
    private WhgInfColInfoService whgInfColInfoService;


    @RequestMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.NOTICE, optDesc = {"访问列表页","访问添加(编辑)页"},valid= {"type=list","type=add"})
    public Object view(@PathVariable("type")String type, WebRequest request) throws Exception{
        if ("add".equalsIgnoreCase(type)){
            ModelAndView view = new ModelAndView("admin/information/colinfo/view_add");
            String id = request.getParameter("id");
            String targetShow = request.getParameter("targetShow");
            String clnftype = request.getParameter("clnftype");
            String offH2 = request.getParameter("offH2");
            view.addObject("clnftype", clnftype);
            view.addObject("offH2", offH2);
            if (id != null){
                view.addObject("id", id);
                view.addObject("targetShow", targetShow);
                try {
                    view.addObject("info", this.whgInfColInfoService.srchOne(id));
                } catch (Exception e) {
                    log.error("加载指定ID的场馆信息失败", e);
                    throw e;
                }
            }
            return view;
        }
        ModelAndView modeview = new ModelAndView("admin/information/colinfo/view_list");
        modeview.addObject("type", type);
        return modeview;
    }

    @RequestMapping("/add")
    @WhgOPT(optType = EnumOptType.NOTICE, optDesc = {"添加"})
    public Object add(HttpSession session, WhgInfColinfo info, @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")Date clnfcrttime_str){
        ResponseBean rb = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            if (clnfcrttime_str!=null){
                info.setClnfcrttime(clnfcrttime_str);
            }
            this.whgInfColInfoService.t_add(info, sysUser);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("栏目信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/edit")
    @WhgOPT(optType = EnumOptType.NOTICE, optDesc = {"编辑"})
    public Object edit(HttpSession session, WhgInfColinfo info, @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")Date clnfcrttime_str){
        ResponseBean rb = new ResponseBean();
        if (info.getClnfid() == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("栏目主键信息丢失");
            return rb;
        }
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            if (clnfcrttime_str!=null){
                info.setClnfcrttime(clnfcrttime_str);
            }
            if (info.getClnfkey()==null) info.setClnfkey("");

            this.whgInfColInfoService.t_edit(info, sysUser);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("场馆信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    //=========================================

    @RequestMapping("/colinfo")
    public ModelAndView index() {
        return new ModelAndView("admin/information/colinfo");
    }
    /**
     * 查询
     */
    @RequestMapping("/seleinfo")
    public Object inquire(HttpServletRequest req, HttpServletResponse resp){
        //获取请求参数
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
        String type = null;
        if (paramMap.get("pageType") != null) {
            type = (String) paramMap.get("pageType");
        }
        if (paramMap.get("clnfstata") != null) {
            paramMap.put("clnfstata", Arrays.asList(paramMap.get("clnfstata").toString()));
        }
        //分页查询
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        WhgSysUser sysUser = (WhgSysUser) req.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        if (sysUser != null && type != null && type.equals("edit")) { //编辑列表
            paramMap.put("userid",sysUser.getId());
        } else if (type.equals("del")) { //回收列表
            paramMap.put("delstate", 1);
        } else {
            if (paramMap.get("clnfstata") == null || paramMap.get("clnfstata") == "") {
                paramMap.put("clnfstata", Arrays.asList(9, 2, 6, 4));
            }
        }
        try {
            rtnMap = this.whgInfColInfoService.selein(paramMap, sysUser);
        } catch (Exception e) {
            rtnMap.put("total", 0);
            rtnMap.put("rows", new ArrayList<Map<String, Object>>(0));
        }
        return rtnMap;
    }
    /**
     * 添加栏目信息
     */
    @RequestMapping("/addmusinfo")
    public Object addminfo(HttpServletRequest req, HttpServletResponse resp, WhgInfColinfo whc, @RequestParam("colinfopic_up")MultipartFile colinfopic_up, @RequestParam("colinfobigpic_up")MultipartFile colinfobigpic_up) {
        String success = "0";
        String errmasg = "";
        //图片或者文件处理
        try {
            //当前日期
            Date now = new Date();
//            //保存图片
//            String uploadPath = UploadUtil.getUploadPath(req);
//            //列表图
//            if(colinfopic_up != null && !colinfopic_up.isEmpty()){
//                String imgPath_colinfopic = UploadUtil.getUploadFilePath(colinfopic_up.getOriginalFilename(), service.getKey("art.picture"), "shop", "picture", now);
//                colinfopic_up.transferTo( UploadUtil.createUploadFile(uploadPath, imgPath_colinfopic) );
//                whc.setClnfpic(UploadUtil.getUploadFileUrl(uploadPath, imgPath_colinfopic));
//            }
//            //详情图
//            if(colinfobigpic_up != null && !colinfobigpic_up.isEmpty()){
//                String imgPath_colinfobigpic = UploadUtil.getUploadFilePath(colinfobigpic_up.getOriginalFilename(), service.getKey("art.picture"), "shop", "picture", now);
//                colinfobigpic_up.transferTo( UploadUtil.createUploadFile(uploadPath, imgPath_colinfobigpic) );
//                whc.setClnfbigpic(UploadUtil.getUploadFileUrl(uploadPath, imgPath_colinfobigpic));
//            }
            whc.setClnfid(IDUtils.getID());
            this.whgInfColInfoService.addinfo(whc);
        } catch (Exception e) {
            success = "1";
            errmasg = e.getMessage();
        }
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", success);
        res.put("msg", errmasg);
        return res;
    }
    /**
     * 编辑栏目信息
     */
    @RequestMapping("/upminfo")
    public Object upinfo(HttpServletRequest req, HttpServletResponse resp, WhgInfColinfo whc, @RequestParam("colinfopic_up")MultipartFile colinfopic_up,@RequestParam("colinfobigpic_up")MultipartFile colinfobigpic_up) {
        String success = "0";
        String errmasg = "";
        //图片或者文件处理
        try {
            //当前日期
            Date now = new Date();
            //保存图片
            /*String uploadPath = UploadUtil.getUploadPath(req);
            //列表图
            if(colinfopic_up != null && !colinfopic_up.isEmpty()){
                UploadUtil.delUploadFile(uploadPath, whc.getClnfpic());

                String imgPath_colinfopic = UploadUtil.getUploadFilePath(colinfopic_up.getOriginalFilename(), service.getKey("art.picture"), "shop", "picture", now);
                colinfopic_up.transferTo( UploadUtil.createUploadFile(uploadPath, imgPath_colinfopic) );
                whc.setClnfpic(UploadUtil.getUploadFileUrl(uploadPath, imgPath_colinfopic));
            }
            //详情图
            if(colinfobigpic_up != null && !colinfobigpic_up.isEmpty()){
                UploadUtil.delUploadFile(uploadPath, whc.getClnfbigpic());

                String imgPath_colinfobigpic = UploadUtil.getUploadFilePath(colinfobigpic_up.getOriginalFilename(), service.getKey("art.picture"), "shop", "picture", now);
                colinfobigpic_up.transferTo( UploadUtil.createUploadFile(uploadPath, imgPath_colinfobigpic) );
                whc.setClnfbigpic(UploadUtil.getUploadFileUrl(uploadPath, imgPath_colinfobigpic));
            }*/
            this.whgInfColInfoService.upmusinfo(whc);
        } catch (Exception e) {
            success = "1";
            errmasg = e.getMessage();
        }
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", success);
        res.put("msg", errmasg);
        return res;
    }

    /**
     *回收
     */
    @RequestMapping(value = "/recovery")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"回收"})
    public ResponseBean recovery(WhgInfColinfo whz, HttpServletRequest req) {
        ResponseBean res = new ResponseBean();
        try {
            whz.setDelstate(1);
            this.whgInfColInfoService.checkin(whz);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 还原
     */
    @RequestMapping(value = "/reback")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.ACT, optDesc = {"回收"})
    public ResponseBean reback(WhgInfColinfo whz, HttpServletRequest req) {
        ResponseBean res = new ResponseBean();
        try {
            whz.setDelstate(0);
            this.whgInfColInfoService.checkin(whz);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }
    /**
     * 删除deltyp
     */
    @RequestMapping("/delinfo")
    @WhgOPT(optType = EnumOptType.NOTICE, optDesc = {"删除"})
    public Object delete(String clnfid,HttpServletRequest req,String clnfpic,String clnfbigpic) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        String success = "0";
        String errmsg = "";
        //删除图片
        try {
            /*String uploadPath = UploadUtil.getUploadPath(req);
            if(clnfpic!= null && !clnfpic.isEmpty()){
                UploadUtil.delUploadFile(uploadPath, clnfpic);
            }
            if(clnfbigpic!= null && !clnfbigpic.isEmpty()){
                UploadUtil.delUploadFile(uploadPath, clnfbigpic);
            }*/
            this.whgInfColInfoService.delete(clnfid);
        } catch (Exception e) {
            errmsg = e.getMessage();
        }

        // 返回
        rtnMap.put("success", success);
        rtnMap.put("errmsg", errmsg);
        return rtnMap;
    }
    /**
     * 改变审核状态
     */
    /**
     * 审核状态
     */
    @RequestMapping("/checkinfo")
    @WhgOPT(optType = EnumOptType.NOTICE, optDesc = {"提交审核", "审核通过", "审核打回", "发布", "撤销发布"}, valid = {"clnfstata=9", "clnfstata=2", "clnfstata=1", "clnfstata=6", "clnfstata=4"})
    public Object infoCheck(WhgInfColinfo whz, HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        String success = "0";
        String errmsg = "";
        Date a = new Date();
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            whz.setClnfopttime(a);
            if (whz != null && (whz.getClnfstata().equals(EnumBizState.STATE_CAN_PUB.getValue()) || whz.getClnfstata().equals(EnumBizState.STATE_CAN_EDIT.getValue()))) {//待发布  审核者信息
                whz.setCheckor(user.getId());
                whz.setCheckdate(new Date());
            } else if (whz != null && whz.getClnfstata().equals(EnumBizState.STATE_PUB.getValue())) {//已发布记录 发布者信息
                whz.setPublisher(user.getId());
                whz.setPublishdate(new Date());
            }
            this.whgInfColInfoService.checkin(whz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            success = "1";
            errmsg = e.getMessage();
        }
        // 返回
        rtnMap.put("success", success);
        rtnMap.put("errmsg", errmsg);
        return rtnMap;
    }

    /**
     * 置顶
     * @param whz 对象
     * @return
     */
    @RequestMapping("/toTop")
    @WhgOPT(optType = EnumOptType.NOTICE, optDesc = {"置顶"})
    public String toTop(WhgInfColinfo whz){
        try {
            this.whgInfColInfoService.toTop(whz);
            return "success";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "error";
        }
    }
    /**
     * 设置上首页及排序值
     * @return
     */
    @RequestMapping("/goinfo")
    public Object goPage(WhgInfColinfo whz){
        Map<String, String> rtnMap = new HashMap<String, String>();
        String success = "0";
        String errmsg = "";
        try {
            this.whgInfColInfoService.goHomePage(whz);
        } catch (Exception e) {
            success = "1";
            errmsg = e.getMessage();
        }
        rtnMap.put("success", success);
        rtnMap.put("errmsg", errmsg);
        return rtnMap;
    }
    /**
     * 批量审核或者取消操作操作
     */
    @RequestMapping("/checkeinfos")
    public Object sendCheck(String clnfid, int fromstate, int tostate, HttpServletRequest request) {
        Map<String, Object> res = new HashMap<String, Object>();
        String success = "0";
        String errmsg = "";
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        //添加修改课时
        try {
            //修改
            this.whgInfColInfoService.checkexhi(clnfid, fromstate, tostate, user);

        } catch (Exception e) {
            success = "1";
            errmsg = e.getMessage();
        }
        res.put("success", success);
        res.put("errmsg", errmsg);
        return res;
    }

    /**
     * 是否上首页
     * @param ids
     * @param formupindex
     * @param toupindex
     * @return
     */
    @RequestMapping("/upindex")
    @WhgOPT(optType = EnumOptType.NOTICE, optDesc = {"上首页","取消上首页"}, valid = {"formupindex=1","formupindex=0"})
    public ResponseBean upindex(String ids, String formupindex, int toupindex){
        ResponseBean res = new ResponseBean();
        try {
            res = this.whgInfColInfoService.t_upindex(ids,formupindex,toupindex);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("上首页失败！");
            log.error(res.getErrormsg()+" formupindex: "+formupindex+" toupindex:"+toupindex+" ids: "+ids, e);
        }
        return res;
    }

}
