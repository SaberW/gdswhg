package com.creatoo.hn.controller.admin.yunwei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.yunwei.VideoService;
import com.creatoo.hn.util.AliyunOssUtil;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.enums.EnumOptType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户资讯展示控制器
 * @author wangxl
 * @version 2016.11.08
 */
@RestController
@RequestMapping("/admin/video")
public class VideoController extends BaseController{
	/**
	 * 日志控制器
	 */
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	public VideoService service;

	@Autowired
	private WhgSystemUserService whgSystemUserService;


	/**
	 * 数字资源管理页面
	 * @return 数字资源管理页面
	 */
	@RequestMapping("/index")
	@WhgOPT(optType = EnumOptType.VIDEO, optDesc = "访问视频管理页面")
	public ModelAndView index(){
		return new ModelAndView( "admin/yunwei/video/list" );
	}

	/**
	 * 上传文件页面
	 * @return
	 */
	@RequestMapping("/upload")
	@WhgOPT(optType = EnumOptType.VIDEO, optDesc = "访问上传页面")
	public ModelAndView uploadPage(String dir){
		ModelAndView view = new ModelAndView( "admin/yunwei/video/upload" );
		view.addObject("dir", dir);
		return view;
	}

	/**
	 * 分页查询数字资源
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/srchPagging")
	public Object srchPagging(HttpSession session, HttpServletRequest req, String cultid, String dir, String keyname, String srchFile, String srchDir){
		//获取请求参数
		Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);

		//查询
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		try {
			//rtnList = (List<Map<String, Object>>)this.service.srchPagging(paramMap).get("rows");
			if ("1".equals(srchDir) && (cultid==null || cultid.isEmpty()) ){
				WhgSysUser sysUser = (WhgSysUser) session.getAttribute(Constant.SESSION_ADMIN_KEY);
				List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
				if (cultids!=null && cultids.size()>0){
					cultid = cultids.get(0);
				}
			}

			rtnList = (List<Map<String, Object>>)this.service.srchPagging(cultid, dir, keyname, srchFile, srchDir).get("rows");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return rtnList;
	}

	/**
	 * 批量删除视频
	 * @param ids
	 * @return
	 */
	@RequestMapping("/del")
	@WhgOPT(optType = EnumOptType.VIDEO, optDesc = "删除")
	public Object updState(String ids){
		Map<String, String> rtnMap = new HashMap<String, String>();
		String success = "0";
		String errmsg = "";

		//添加修改课时
		try {
			//保存
			this.service.delete(ids);
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
	 * 创建文件夹
	 * @return
	 */
	@RequestMapping("/createDir")
	@WhgOPT(optType = EnumOptType.VIDEO, optDesc = "创建目录")
	public Object createDir(String cultid, String pdir, String dir){
		Map<String, String> rtnMap = new HashMap<String, String>();
		String success = "0";
		String errmsg = "";

		//添加修改课时
		try {
			if(StringUtils.isEmpty(pdir) || "root".equals(pdir) || cultid.equals(pdir)){
				pdir = cultid;
			}else{
				pdir = cultid + "/" + pdir;
			}
			AliyunOssUtil.createDir(pdir, dir);
		} catch (Exception e) {
			success = "1";
			errmsg = e.getMessage();
		}

		//返回
		rtnMap.put("success", success);
		rtnMap.put("errmsg", errmsg);
		return rtnMap;
	}
}
