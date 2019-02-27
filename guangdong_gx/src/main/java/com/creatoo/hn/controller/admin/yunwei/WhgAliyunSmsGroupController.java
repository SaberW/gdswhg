package com.creatoo.hn.controller.admin.yunwei;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgYwiAliysmsGroup;
import com.creatoo.hn.dao.model.WhgYwiAliysmsRefgc;
import com.creatoo.hn.dao.model.WhgYwiAliysmsRefusegrop;
import com.creatoo.hn.services.comm.SmsAliyunService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/yunwei/aliysmsgroup")
public class WhgAliyunSmsGroupController extends BaseController {

    @Autowired
    private SmsAliyunService smsAliyunService;

    /**
     * 跳转到短信组管理页面
     * @return
     */
    @RequestMapping("/view/list")
    public String viewlist(){
        return "/admin/yunwei/aliysmsgroup/view_list";
    }

    /**
     * 跳转到添加
     * @return
     */
    @RequestMapping("/view/add")
    public String viewadd(ModelMap mmp){
        mmp.addAttribute("pageType", "add");
        return "/admin/yunwei/aliysmsgroup/view_edit";
    }

    /**
     * 跳转到编辑
     * @return
     */
    @RequestMapping("/view/edit")
    public String viewedit(String id, ModelMap mmp){
        mmp.addAttribute("pageType", "edit");
        try {
            WhgYwiAliysmsGroup info = this.smsAliyunService.findAliySmsGroup(id);
            mmp.addAttribute("info", info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "/admin/yunwei/aliysmsgroup/view_edit";
    }

    /**
     * 跳转到查看
     * @return
     */
    @RequestMapping("/view/show")
    public String viewshow(String id, ModelMap mmp){
        mmp.addAttribute("pageType", "show");
        try {
            WhgYwiAliysmsGroup info = this.smsAliyunService.findAliySmsGroup(id);
            mmp.addAttribute("info", info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "/admin/yunwei/aliysmsgroup/view_edit";
    }


    /**
     * 获取短信组业务类型
     * @return
     */
    @RequestMapping("/getgptypes")
    @ResponseBody
    public Object getGpTypes(){
        return this.smsAliyunService.getGpTypes();
    }

    /**
     * 获取短信组切入点
     * @param gptype
     * @return
     */
    @RequestMapping("/getgptypepoints")
    @ResponseBody
    public Object getGpTypePoints(String gptype){
        return this.smsAliyunService.getGpTypePoints(gptype);
    }

    /**
     * 后台查询短信模板组列表
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WhgYwiAliysmsGroup cored){
        ResponseBean rb = new ResponseBean();
        try {
            PageInfo pageInfo = this.smsAliyunService.selectAliySmsGroups(page, rows, cored);
            rb.setRows(pageInfo.getList());
            rb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("查询数据失败");
        }
        return rb;
    }

    /**
     * 添加短信组
     * @param info
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object addGroup(WhgYwiAliysmsGroup info){
        ResponseBean rb = new ResponseBean();
        try {
            this.smsAliyunService.saveAliySmsGroup(info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("保存数据失败");
        }
        return rb;
    }

    /**
     * 保存短信组
     * @param info
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object editGroup(WhgYwiAliysmsGroup info){
        ResponseBean rb = new ResponseBean();
        try {
            this.smsAliyunService.editAliySmsGroup(info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("保存数据失败");
        }
        return rb;
    }

    /**
     * 删除短信组
     * @param id
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Object delGroup(String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.smsAliyunService.removeAliySmsGroup(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("删除数据失败");
        }
        return rb;
    }


    /**
     * 跳转设置短信组模板
     * @param id
     * @param mmp
     * @return
     */
    @RequestMapping("/smsgcref/view/list")
    public String viewSetSmsGroup(String id, ModelMap mmp){
        try {
            WhgYwiAliysmsGroup info = this.smsAliyunService.findAliySmsGroup(id);
            mmp.addAttribute("info", info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "admin/yunwei/aliysmsgroup/view_list_groupitems";
    }

    /**
     * 跳转短信组模板编辑
     * @return
     */
    @RequestMapping("/smsgcref/view/edit")
    public String viewSetSmsGcRef(String id, ModelMap mmp){
        try {
            WhgYwiAliysmsGroup info = this.smsAliyunService.findAliySmsGroup(id);
            mmp.addAttribute("info", info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "admin/yunwei/aliysmsgroup/view_edit_groupitems";
    }

    /**
     * 查询组的相关短信模板列表
     * @return
     */
    @RequestMapping("/smsgcref/srchlistgcref")
    @ResponseBody
    public Object srchList4gcref(String id){
        try {
            return this.smsAliyunService.srchAliySmsCode4Groupid(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 保存短信组与模板的关连信息
     * @param groupid
     * @param codeid
     * @param actpoint
     * @param actdesc
     * @return
     */
    @RequestMapping("/smsgcref/save")
    @ResponseBody
    public Object saveSmsGcref(WhgYwiAliysmsRefgc info){
        ResponseBean rb = new ResponseBean();
        try {
            rb = this.smsAliyunService.saveAliySmsRefgc(info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("保存数据失败");
        }
        return rb;
    }

    /**
     * 删除短信组与模板关联信息
     * @param refid
     * @return
     */
    @RequestMapping("/smsgcref/del")
    @ResponseBody
    public Object delSmsGcref(String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.smsAliyunService.removeAliySmsRefgc(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("删除数据失败");
        }
        return rb;
    }


    /**
     * 跳转短信组模板编辑
     * @return
     */
    @RequestMapping("/refuse/view/edit")
    public String viewSetSmsGcRef(ModelMap mmp, String entid, String enttype){
        mmp.addAttribute("entid", entid);
        mmp.addAttribute("enttype", enttype);
        try {
            Object info = this.smsAliyunService.findAliySmsRefuseGroup(entid, enttype);
            mmp.addAttribute("info", info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "admin/yunwei/aliysmsgroup/view_edit_refuse";
    }

    /**
     * 加载业务短信组选项列表
     * @param enttype
     * @return
     */
    @RequestMapping("/refuse/srchlistgroup")
    @ResponseBody
    public Object getRefUseGroups(String enttype){
        try {
            return this.smsAliyunService.srchAliySmsGroups4Type(enttype);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * 保存业务短信组引用
     * @param info
     * @return
     */
    @RequestMapping("/refuse/save")
    @ResponseBody
    public Object saveRefUseGroup(WhgYwiAliysmsRefusegrop info){
        ResponseBean rb = new ResponseBean();
        try {
            rb = this.smsAliyunService.saveAliySmsGroupRefuse(info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("保存数据失败");
        }
        return rb;
    }

}
