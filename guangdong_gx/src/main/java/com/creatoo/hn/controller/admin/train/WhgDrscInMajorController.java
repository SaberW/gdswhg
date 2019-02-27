package com.creatoo.hn.controller.admin.train;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.admin.train.WhgDrscInMajorService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/26.
 */
@Controller
@RequestMapping("admin/major/drsc")
public class WhgDrscInMajorController extends BaseController{

    @Autowired
    private WhgDrscInMajorService whgDescInMajorService;

    /**
     * 进入微专业视图
     * @return
     */
    @RequestMapping("/view/{type}")
    @ResponseBody
    public ModelAndView view(HttpServletRequest request, @PathVariable("type") String type){
        ModelAndView view = new ModelAndView();
        String mid = request.getParameter("mid");
        view.addObject("mid",mid);
        view.setViewName("admin/train/major/drsc/view_"+type);
        return view;
    }

    /**
     * 分页查询关联课程列表
     * @param page
     * @param rows
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(int page, int rows, WebRequest request){
        ResponseBean rsb = new ResponseBean();
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String mid = request.getParameter("mid");
        try {
            PageInfo pageInfo = this.whgDescInMajorService.t_srchlist4p(page,rows,mid);
            rsb.setRows(pageInfo.getList() );
            rsb.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rsb.setRows(new ArrayList());
            rsb.setSuccess(ResponseBean.FAIL);
        }
        return rsb;
    }

    /**
     *  删除关联微专业
     * @param req
     * @param
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Object saveTraintpl(HttpServletRequest req, String id){
        ResponseBean res = new ResponseBean();
        try {
            this.whgDescInMajorService.t_del(id);

        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("关联课程删除失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 添加关联课程
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseBean add(String entid,String mid) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgDescInMajorService.t_add(entid,mid);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("关联课程保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }
}
