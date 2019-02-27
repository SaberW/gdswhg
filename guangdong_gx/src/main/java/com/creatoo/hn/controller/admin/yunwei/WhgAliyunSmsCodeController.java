package com.creatoo.hn.controller.admin.yunwei;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgYwiAliysmsCode;
import com.creatoo.hn.dao.model.WhgYwiConfig;
import com.creatoo.hn.services.comm.SmsAliyunService;
import com.creatoo.hn.services.comm.YwiConfigService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/yunwei/aliysmscode")
public class WhgAliyunSmsCodeController extends BaseController {

    @Autowired
    private SmsAliyunService smsAliyunService;

    @Autowired
    private YwiConfigService ywiConfigService;

    /**
     * 跳转到模板列表
     * @return
     */
    @RequestMapping("/view/list")
    public String viewlist(){
        return "/admin/yunwei/aliysmscode/view_list";
    }

    /**
     * 跳转到添加
     * @return
     */
    @RequestMapping("/view/add")
    public String viewadd(ModelMap mmp){
        mmp.addAttribute("pageType", "add");
        return "/admin/yunwei/aliysmscode/view_edit";
    }

    /**
     * 跳转到编辑
     * @return
     */
    @RequestMapping("/view/edit")
    public String viewedit(String id, ModelMap mmp){
        mmp.addAttribute("pageType", "edit");
        try {
            WhgYwiAliysmsCode info = this.smsAliyunService.findAliysmsCode(id);
            mmp.addAttribute("info", info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "/admin/yunwei/aliysmscode/view_edit";
    }

    /**
     * 跳转到查看
     * @return
     */
    @RequestMapping("/view/show")
    public String viewshow(String id, ModelMap mmp){
        mmp.addAttribute("pageType", "show");
        try {
            WhgYwiAliysmsCode info = this.smsAliyunService.findAliysmsCode(id);
            mmp.addAttribute("info", info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "/admin/yunwei/aliysmscode/view_edit";
    }

    @RequestMapping("/view/config")
    public String viewconfig(ModelMap mmp){
        mmp.addAttribute("cfgtype", YwiConfigService.CFGTYPE_ALIYUNSMS);
        try {
            Object cfgs = this.ywiConfigService.getConfigMap4Type(YwiConfigService.CFGTYPE_ALIYUNSMS);
            mmp.addAttribute("info", cfgs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "/admin/yunwei/aliysmscode/view_edit_config";
    }

    @RequestMapping("/saveconfig")
    @ResponseBody
    public Object saveConfig(String cfgtype, String cfgJson){
        ResponseBean rb = new ResponseBean();

        try {
            JSONArray jsonArray = JSON.parseArray(cfgJson);

            List<WhgYwiConfig> list = new ArrayList();
            for(Object item : jsonArray){
                JSONObject ent = (JSONObject) item;
                WhgYwiConfig info = new WhgYwiConfig();
                info.setCfgkey(ent.get("cfgkey").toString());
                info.setCfgtype(cfgtype);
                info.setCfgtext(ent.get("cfgtext").toString());
                info.setCfgvalue(ent.get("cfgvalue").toString());

                list.add(info);
            }
            this.ywiConfigService.saveConfigList(list);
        } catch (Exception e) {
            rb.setErrormsg("保存数据失败");
            rb.setSuccess(rb.FAIL);
            log.error(e.getMessage(), e);
        }

        return rb;
    }

    /**
     * 后台查询短信模板列表
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public Object srchList4p(int page, int rows, WhgYwiAliysmsCode record){
        ResponseBean rb = new ResponseBean();
        try {
            PageInfo pageInfo = this.smsAliyunService.selectAliysmsCodes(page, rows, record);
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
     * 添加短信模板
     * @param info
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object addGroup(WhgYwiAliysmsCode info){
        ResponseBean rb = new ResponseBean();
        try {
            this.smsAliyunService.saveAliySmsCode(info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("保存数据失败");
        }
        return rb;
    }

    /**
     * 保存短信模板
     * @param info
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object editGroup(WhgYwiAliysmsCode info){
        ResponseBean rb = new ResponseBean();
        try {
            this.smsAliyunService.editAliySmsCode(info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("保存数据失败");
        }
        return rb;
    }

    /**
     * 删除短信模板
     * @param id
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Object delGroup(String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.smsAliyunService.removeAliySmsCode(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rb.setSuccess(rb.FAIL);
            rb.setErrormsg("删除数据失败");
        }
        return rb;
    }
}
