package com.creatoo.hn.controller.admin.information;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgInfUpload;
import com.creatoo.hn.services.admin.information.WhgInfUploadService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.UploadUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/infoupload")
public class WhgInfUploadController extends BaseController {


    @Autowired
    private WhgInfUploadService whgInfUploadService;

    @RequestMapping("/uploads")
    public ModelAndView index() {
        return new ModelAndView("/admin/information/upload");
    }

    /**
     * 进入type(list|add|edit|view)视图
     *
     * @param request 请求对象
     * @param type    视图类型(list|add|edit|view)
     * @return 视图
     */
    @RequestMapping("/uploads/view/{type}")
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type") String type) {
        ModelAndView view = new ModelAndView("admin/information/upload/view_" + type);
        String refid = request.getParameter("refid");
        String upid = request.getParameter("upid");

        view.addObject("refid", refid);
        view.addObject("upid", upid);
        view.addObject("upload", whgInfUploadService.select(upid));

        return view;
    }

    /**
     * 查询
     */
    @RequestMapping("/seletup")
    public Object selecuplo(HttpServletRequest req) {
        //获取请求参数
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);

        //分页查询
        Map<String, Object> rtnMap = new HashMap<>();
        try {
            rtnMap = this.whgInfUploadService.inquire(paramMap);
        } catch (Exception e) {
            rtnMap.put("total", 0);
            rtnMap.put("rows", new ArrayList<Map<String, Object>>(0));
        }
        return rtnMap;
    }

    /**
     * 添加
     */
    @RequestMapping("/adduploda")
    public Object add(WhgInfUpload whup, HttpServletRequest req) {
        String success = "0";
        String errmasg = "";
        try {
            //当前日期
            Date now = new Date();
            //保存图片
//			String uploadPath = UploadUtil.getUploadPath(req);
//			//列表图
//			if(uplink_up != null && !uplink_up.isEmpty()){
//				String imgPath_uplink = UploadUtil.getUploadFilePath(uplink_up.getOriginalFilename(), commService.getKey("art.picture"), "shop", "picture", now);
//				uplink_up.transferTo( UploadUtil.createUploadFile(uploadPath, imgPath_uplink) );
//				whup.setUplink(UploadUtil.getUploadFileUrl(uploadPath, imgPath_uplink));
//			}
            whup.setUpid(IDUtils.getID());
            whup.setUptime(now);
            this.whgInfUploadService.save(whup);
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
     * 修改
     */
    @RequestMapping("/douploda")
    public Object doloda(WhgInfUpload whup) {
        String success = "0";
        String errmasg = "";
        try {
            //当前日期
            Date now = new Date();
            //保存图片
//			String uploadPath = UploadUtil.getUploadPath(req);
//			//列表图
//			if(uplink_up != null && !uplink_up.isEmpty()){
//				UploadUtil.delUploadFile(uploadPath, whup.getUplink());
//
//				String imgPath_uplink = UploadUtil.getUploadFilePath(uplink_up.getOriginalFilename(), commService.getKey("art.picture"), "shop", "picture", now);
//				uplink_up.transferTo( UploadUtil.createUploadFile(uploadPath, imgPath_uplink) );
//				whup.setUplink(UploadUtil.getUploadFileUrl(uploadPath, imgPath_uplink));
//			}
            whup.setUptime(now);
            this.whgInfUploadService.updata(whup);
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
     * 删除
     */
    @RequestMapping("/deluploda")
    public Object delup(String upid, HttpServletRequest req, String uplink) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        String success = "0";
        String errmsg = "";
        //删除图片
        try {
            String uploadPath = UploadUtil.getUploadPath(req);
            if (uplink != null && !uplink.isEmpty()) {
                UploadUtil.delUploadFile(uploadPath, uplink);
            }
            this.whgInfUploadService.delete(upid);
        } catch (Exception e) {
            errmsg = e.getMessage();
        }
        // 返回
        rtnMap.put("success", success);
        rtnMap.put("errmsg", errmsg);
        return rtnMap;
    }

    /**
     * 改变状态
     */
    @RequestMapping("/upstate")
    public Object upCheck(WhgInfUpload whup) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        String success = "0";
        String errmsg = "";
        try {
            this.whgInfUploadService.checkup(whup);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            success = "1";
            errmsg = e.getMessage();
        }
        rtnMap.put("success", success);
        rtnMap.put("errmsg", errmsg);
        return rtnMap;
    }
}
