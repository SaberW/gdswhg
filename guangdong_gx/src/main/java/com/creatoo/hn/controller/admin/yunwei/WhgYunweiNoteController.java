package com.creatoo.hn.controller.admin.yunwei;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiNote;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiNoteService;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 操作日志管理
 * Created by wangxl on 2017/4/25.
 */
@RestController
@RequestMapping("/admin/yunwei/note")
public class WhgYunweiNoteController {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * 操作日志服务
     */
    @Autowired
    private WhgYunweiNoteService whgYunweiNoteService;

    /**
     * 查看操作日志分页列表视图
     * @param request 请求对象
     * @param type 视图类型
     * @return 操作日志视图
     */
    @RequestMapping("/view/{type}")
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type")String type){
        ModelAndView view = new ModelAndView("admin/yunwei/note/view_"+type);

        try {
            if("edit".equals(type) || "view".equals(type)){
                String id = request.getParameter("id");
                view.addObject("cult", whgYunweiNoteService.t_srchOne(id));
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        return view;
    }

    /**
     * 操作日志分页数据
     * @param request 请求对象
     * @param note 操作日志条件对象
     * @return 操作日志分页数据
     */
    @RequestMapping(value = "/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request, WhgYwiNote note){
        ResponseBean res = new ResponseBean();
        try {
            int page = RequestUtils.getPageParameter(request);
            int rows = RequestUtils.getRowsParameter(request);
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            String stime = RequestUtils.getParameter(request, "stime");
            String etime = RequestUtils.getParameter(request, "etime");
            WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            PageInfo<WhgYwiNote> pageInfo = whgYunweiNoteService.t_srchList4p(page, rows, sort, order, stime, etime, note);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 操作日志明细查询
     * @param request 请求对象
     * @param id 操作日志ID
     * @return 操作日志明细
     */
    @RequestMapping(value = "/srchOne")
    public ResponseBean srchOne(HttpServletRequest request, String id){
        ResponseBean res = new ResponseBean();
        try {
            WhgYwiNote note = whgYunweiNoteService.t_srchOne(id);
            res.setData(note);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 导出excel
     * @param request
     * @param note
     * @param response
     * @return
     */
    @RequestMapping("/exportExcel")
    public String exportExcel(HttpServletRequest request, WhgYwiNote note, HttpServletResponse response){
        String message = "success";
        try {
            //请求参数
            String sort = RequestUtils.getParameter(request, "sort");
            String order = RequestUtils.getParameter(request, "order");
            String stime = RequestUtils.getParameter(request, "stime");
            String etime = RequestUtils.getParameter(request, "etime");
            String cookieName = RequestUtils.getParameter(request, "ckname");

            //导出
            ServletOutputStream out = response.getOutputStream();
            String fileName = "操作日志"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xls";
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");
            this.whgYunweiNoteService.exportExcel(out, sort, order, stime, etime, note, cookieName, response);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            message = e.getMessage();
        }
        return message;
    }
}
