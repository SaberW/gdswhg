package com.creatoo.hn.controller.admin.mass;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgResource;
import com.creatoo.hn.services.admin.mass.WhgResManageService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by rbg on 2017/12/4.
 * 群文资源管理控制器
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/mass/resmanage")
public class WhgResManageController extends BaseController{

    @Autowired
    private WhgResManageService whgResManageService;

    /**
     * 界面入口
     * @param type
     * @param mmp
     * @param request reftype:实体类型枚举, refid:实体id, enttypes:资源类型限制指定"1,2,3,4"
     * @return
     */
    @RequestMapping("/view/{type}")
    public String viewList(@PathVariable("type")String type,
                           ModelMap mmp, HttpServletRequest request) {
        String view = "admin/mass/resmanage/";
        String reftype = request.getParameter("reftype");
        String refid = request.getParameter("refid");
        String enttypes = request.getParameter("enttypes");

        switch (type){
            case "show":
            case "edit":{
                String id = request.getParameter("id");
                if (id != null && !id.isEmpty()) {
                    mmp.addAttribute("id", id);
                    try {
                        mmp.addAttribute("info", this.whgResManageService.srchOne(id));
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            case "add":
                view += "view_edit";
                break;
            default:
                if (reftype.equals(EnumTypeClazz.TYPE_GAT_BRAND.getValue())
                        || reftype.equals(EnumTypeClazz.TYPE_MAJOR.getValue())
                        || reftype.equals(EnumTypeClazz.TYPE_PINPAI.getValue())){
                    //品牌类型标记操作锁
                    mmp.addAttribute("lookOpt", "lookOpt");
                }
                view += "view_list";
        }

        mmp.addAttribute("pageType", type);
        mmp.addAttribute("reftype", reftype);
        mmp.addAttribute("refid", refid);
        mmp.addAttribute("enttypes", enttypes);
        return view;
    }

    /**
     * 资源列表分页查询
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WhgResource record, HttpServletRequest request){
        ResponseBean rb = new ResponseBean();
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        try {
            PageInfo pageInfo = null;
            if (record.getReftype().equals(EnumTypeClazz.TYPE_GAT_BRAND.getValue())){
                pageInfo = this.whgResManageService.srchList4pGatherBrandRes(page, rows, record);
            }else if (record.getReftype().equals(EnumTypeClazz.TYPE_MAJOR.getValue())){
                pageInfo = this.whgResManageService.srchList4pWzyBrandRes(page, rows, record);
            }else if (record.getReftype().equals(EnumTypeClazz.TYPE_PINPAI.getValue())){
                pageInfo = this.whgResManageService.srchList4pWhppBrandRes(page, rows, record);
            }else{
                pageInfo = this.whgResManageService.srchList4p(page,rows, record, sort,order);
            }

            rb.setRows(pageInfo.getList());
            rb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setErrormsg("查询数据失败");
            rb.setSuccess(ResponseBean.FAIL);
        }

        return rb;
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object add(WhgResource info,
                      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date _crtdate){
        ResponseBean rb = new ResponseBean();
        try {
            if (_crtdate != null) {
                info.setCrtdate(_crtdate);
            }else{
                info.setCrtdate(new Date());
            }
            rb = this.whgResManageService.t_add(info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
        }
        return rb;
    }

    /**
     * 编辑
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(WhgResource info,
                       @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date _crtdate){
        ResponseBean rb = new ResponseBean();
        try {
            if (_crtdate != null) {
                info.setCrtdate(_crtdate);
            }
            this.whgResManageService.t_edit(info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
        }
        return rb;
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Object del(String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.whgResManageService.t_del(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("操作失败");
        }
        return rb;
    }


    /**
     * 设置是否获奖
     * @param id
     * @param extisaward
     * @return
     */
    @RequestMapping("/setisaward")
    @ResponseBody
    public Object setisaward(String id, int extisaward){
        ResponseBean rb = new ResponseBean();
        try {
            WhgResource info = new WhgResource();
            info.setId(id);
            info.setExtisaward(extisaward);
            this.whgResManageService.t_edit(info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("保存数据失败");
        }
        return rb;
    }

}
