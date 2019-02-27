package com.creatoo.hn.controller.admin.train;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.model.WhgDrsc;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.train.WhgDrscService;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 用户资讯展示控制器
 * @author wangxl
 * @version 2016.11.08
 */
@RestController
@RequestMapping("/admin/drsc")
public class WhgDrscController {
	/**
	 * 日志控制器
	 */
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	public WhgDrscService service;
	@Autowired
	public Environment env;
	

	/**
	 * 数字资源管理页面
	 * @return 数字资源管理页面
	 */
	@RequestMapping("/index/{type}")
	public ModelAndView index(HttpServletRequest request, ModelMap mmp, @PathVariable("type") String type){
		ModelAndView view = new ModelAndView();
		try {
			mmp.addAttribute("type", type);
			if ("add".equalsIgnoreCase(type)){
				String id = request.getParameter("id");
				String targetShow = request.getParameter("targetShow");
				String mid = request.getParameter("mid");
				if(id != null){
					mmp.addAttribute("id", id);
					mmp.addAttribute("targetShow", targetShow);
					mmp.addAttribute("drsc",this.service.srchOne(id));
				}
				if(mid != null && !"".equals(mid)){
					mmp.addAttribute("mid",mid);
				}
				view.setViewName("admin/train/drsc/view_add");
			}else if("syspublish".equalsIgnoreCase(type)){
				view.setViewName("admin/train/drsc/sys_view_list");
			}else{
				view.setViewName("admin/train/drsc/view_list");
			}
		} catch (Exception e) {
			log.error("加载指定ID的培训师资信息失败", e);
		}
		return view;
		//return new ModelAndView( "admin/arts/drsc" );
	}
	
	/**
	 * 分页查询数字资源
	 * @param req
	 * @return
	 */
	@RequestMapping("/srchPagging")
	public Object srchPagging(int page, int rows,HttpServletRequest req, WhgDrsc drsc, HttpSession session, String defaultState){
		//获取请求参数
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		//分页查询
        try {
			Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
			WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
			String sort = req.getParameter("sort");
			String order = req.getParameter("order");
			rtnMap = this.service.srchPagging(page,rows, sort, order,drsc,user.getId(), defaultState);
		} catch (Exception e) {
	        rtnMap.put("total", 0);
	        rtnMap.put("rows", new ArrayList<Map<String, Object>>(0));
		}
		
		return rtnMap;
	}

	/**
	 *  总分馆分页加载培训列表数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/srchSysList4p")
	@ResponseBody
	public ResponseBean srchSysList4p(HttpServletRequest request, WhgDrsc drsc){
		ResponseBean resb = new ResponseBean();
		try {
			int page = Integer.parseInt((String)request.getParameter("page"));
			int rows = Integer.parseInt((String)request.getParameter("rows"));
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");

			String iscult = request.getParameter("iscult");
			String syscultid = request.getParameter("syscultid");
			String level = request.getParameter("level");
			String cultname = request.getParameter("cultname");

			Map<String, String> param = new HashMap();
			param.put("iscult", iscult);
			param.put("syscultid", syscultid);
			param.put("level", level);
			param.put("cultname", cultname);
			PageInfo pageInfo = this.service.t_srchSysList4p(page, rows, drsc, sort, order, param);
			resb.setRows( (List)pageInfo.getList() );
			resb.setTotal(pageInfo.getTotal());
		} catch (Exception e) {
			log.debug("培训资源查询失败", e);
			resb.setTotal(0);
			resb.setRows(new ArrayList());
			resb.setSuccess(ResponseBean.FAIL);
		}
		return resb;
	}
	
	/**
	 * 添加
	 * @return
	 */
	@RequestMapping("/add")
	public Object add(WhgDrsc drsc, HttpServletRequest req, HttpSession session){
		ResponseBean res = new ResponseBean();
		try {
			//当前日期
			Date now = new Date();
			//图片或者文件处理
			/*String uploadPath = UploadUtil.getUploadPath(req);
			//数字资源
			if(drscfile_up != null && !drscfile_up.isEmpty()){
				String drscfile_up_path = UploadUtil.getUploadFilePath(drscfile_up.getOriginalFilename(), commservice.getKey("drsc.video"), "drsc", "video", now);
				drscfile_up.transferTo( UploadUtil.createUploadFile(uploadPath, drscfile_up_path) );
				drsc.setDrscpath(UploadUtil.getUploadFileUrl(uploadPath, drscfile_up_path));
			}*/
			String enturl = req.getParameter("doc_enturl");
			if(enturl != null && !"".equals(enturl)){
				drsc.setEnturl(enturl);
			}
			WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
			String mid = req.getParameter("mid");
			this.service.t_add(drsc,mid,user);
		} catch (Exception e) {
			res.setSuccess(ResponseBean.FAIL);
			res.setErrormsg("保存失败");
			log.error(res.getErrormsg(), e);
		}
		return res;
	}
	
	/**
	 * 编辑
	 * @return
	 */
	@RequestMapping("/edit")
	public Object edit(WhgDrsc drsc, HttpServletRequest req){
		ResponseBean res = new ResponseBean();
		if (drsc.getDrscid() == null){
			res.setSuccess(ResponseBean.FAIL);
			res.setErrormsg("主键信息丢失");
			return res;
		}
		try {
			//当前日期
			Date now = new Date();
			//图片或者文件处理
			/*String uploadPath = UploadUtil.getUploadPath(req);
			//数字资源
			if(drscfile_up != null && !drscfile_up.isEmpty()){
				UploadUtil.delUploadFile(uploadPath, drsc.getDrscpath());//编辑修改了资源先删除之前的资源

				String drscfile_up_path = UploadUtil.getUploadFilePath(drscfile_up.getOriginalFilename(), commservice.getKey("drsc.video"), "drsc", "video", now);
				drscfile_up.transferTo( UploadUtil.createUploadFile(uploadPath, drscfile_up_path) );
				drsc.setDrscpath(UploadUtil.getUploadFileUrl(uploadPath, drscfile_up_path));
			}*/
			String enturl = req.getParameter("doc_enturl");
			if(enturl != null && !"".equals(enturl)){
				drsc.setEnturl(enturl);
			}
			this.service.t_edit(drsc);
		}catch (Exception e){
			res.setSuccess(ResponseBean.FAIL);
			res.setErrormsg("保存失败");
			log.error(res.getErrormsg(), e);
		}
		return res;
	}
	
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("/del")
	public Object del(String id, HttpServletRequest req){
		Map<String, String> rtnMap = new HashMap<String, String>();
		String success = "0";
		String errmsg = "";
		
		//添加修改课时
		try {
			//图片或者文件处理
			String uploadPath = env.getProperty("upload.local.addr");
			
			//删除
			this.service.delete(id, uploadPath);
		} catch (Exception e) {
			success = "1";
			errmsg = e.getMessage();
		}
		
		//返回
		rtnMap.put("success", success);
		rtnMap.put("errmsg", errmsg);
		return rtnMap;
	}

	/**
	 * 回收
	 * @param req
	 * @param id
	 * @return
	 */
	@RequestMapping("/recycle")
	@ResponseBody
	public Object recycle(HttpServletRequest req, String id){
		ResponseBean res = new ResponseBean();
		try {
			this.service.t_recycle(id, 1);

		} catch (Exception e) {
			res.setSuccess(ResponseBean.FAIL);
			res.setErrormsg(e.getMessage());
			log.error(res.getErrormsg(), e);
		}
		return res;
	}

	/**
	 * 撤销回收
	 * @param req
	 * @param id
	 * @return
	 */
	@RequestMapping("/unrecycle")
	@ResponseBody
	public Object unRecycle(HttpServletRequest req, String id){
		ResponseBean res = new ResponseBean();
		try {
			this.service.t_recycle(id, 0);
		} catch (Exception e) {
			res.setSuccess(ResponseBean.FAIL);
			res.setErrormsg(e.getMessage());
			log.error(res.getErrormsg(), e);
		}
		return res;
	}
	
	/**
	 * 修改资源状态
	 * @param ids 资源标识
	 * @param fromState 修改之前的状态
	 * @param toState 修改之后的状态
	 * @return
	 */
	@RequestMapping("/updState")
	public Object updState(String ids, String fromState, int toState, HttpServletRequest req){
		Map<String, String> rtnMap = new HashMap<String, String>();
		String success = "0";
		String errmsg = "";
		
		//添加修改课时
		try {
			//保存
			WhgSysUser sysUser = RequestUtils.getAdmin(req);
			this.service.updState(ids, fromState, toState, sysUser.getId());
		} catch (Exception e) {
			success = "1";
			errmsg = e.getMessage();
		}
		
		//返回
		rtnMap.put("success", success);
		rtnMap.put("errmsg", errmsg);
		return rtnMap;
	}

    @RequestMapping("/updstate")
    public ResponseBean updstate(String ids, String formstates, int tostate, HttpSession session,
                                 @RequestParam(value = "reason", required = false)String reason,
                                 @RequestParam(value = "issms", required = false, defaultValue = "0")int issms){
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser user = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
            this.service.updState(ids, formstates, tostate, user.getId(), reason, issms);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("状态更改失败");
            log.error(res.getErrormsg()+" formstate: "+formstates+" tostate:"+tostate+" ids: "+ids, e);
        }
        return res;
    }

	/**
	 *  分页查询培训资源列表数据
	 * @return
	 */
	@RequestMapping("/srchDrscList")
	@ResponseBody
	public ResponseBean srchDrscList(int page,int rows,HttpServletRequest request,HttpSession session){
		ResponseBean resb = new ResponseBean();
		try {
			String mid = request.getParameter("mid");
			WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
			String cultid = "";
			if(sysUser != null && sysUser.getId().equals("2015121200000000")){
				cultid = "0000000000000000";
			}else{
				cultid = sysUser.getCultid();
			}
			PageInfo pageInfo = this.service.t_srchDrscList(page,rows,mid,cultid);
			resb.setRows( (List)pageInfo.getList() );
			resb.setTotal(pageInfo.getTotal());
		} catch (Exception e) {
			log.debug("查询失败", e);
			resb.setTotal(0);
			resb.setRows(new ArrayList());
			resb.setSuccess(ResponseBean.FAIL);
		}
		return resb;
	}

	/**
	 * 是否推荐
	 * @param ids
	 * @param formrecoms
	 * @param torecom
	 * @return
	 */
	@RequestMapping("/updrecommend")
	@ResponseBody
	public ResponseBean updrecommend(String ids, String formrecoms, int torecom){
		ResponseBean res = new ResponseBean();
		try {
			res = this.service.t_updrecommend(ids,formrecoms,torecom);
		} catch (Exception e) {
			res.setSuccess(ResponseBean.FAIL);
			res.setErrormsg("推荐失败！");
			log.error(res.getErrormsg()+" formrecoms: "+formrecoms+" torecom:"+torecom+" ids: "+ids, e);
		}
		return res;
	}

	@RequestMapping("setToprovince")
	@ResponseBody
	public Object setToprovince(String id, int toprovince){
		ResponseBean rb = new ResponseBean();

		try {
			WhgDrsc info = new WhgDrsc();
			info.setDrscid(id);
			info.setToprovince(toprovince);

			this.service.t_edit(info);
		} catch (Exception e) {
			rb.setSuccess(ResponseBean.FAIL);
			rb.setErrormsg("设置省级推荐失败");
		}

		return rb;
	}

	@RequestMapping("setTocity")
	@ResponseBody
	public Object setTocity(String id, int tocity){
		ResponseBean rb = new ResponseBean();

		try {
			WhgDrsc info = new WhgDrsc();
			info.setDrscid(id);
			info.setTocity(tocity);

			this.service.t_edit(info);
		} catch (Exception e) {
			rb.setSuccess(ResponseBean.FAIL);
			rb.setErrormsg("设置市级推荐失败");
		}

		return rb;
	}
}
