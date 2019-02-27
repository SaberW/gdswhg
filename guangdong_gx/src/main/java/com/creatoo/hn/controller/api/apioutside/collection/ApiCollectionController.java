package com.creatoo.hn.controller.api.apioutside.collection;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgCollection;
import com.creatoo.hn.dao.model.WhgUser;
import com.creatoo.hn.services.api.apioutside.collection.ApiCollectionService;
import com.creatoo.hn.services.api.apioutside.user.ApiUserService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人用户中心--收藏控制类
 * 
 * @author dzl
 *
 */

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ApiCollectionController extends BaseController {

	@Autowired
	private ApiCollectionService colleService;

	@Autowired
	private ApiUserService apiUserService;



	/**
	 * 我的活动收藏查询
	 *
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/collection/myActColle", method = RequestMethod.POST)
	public Object SelectMyActColle(String userId,Integer page, Integer pageSize) {
		ApiResultBean arb = new ApiResultBean();
		Map map =new HashMap();
		try {
			map.put("list",this.colleService.SelectMyActColle(userId,page,pageSize));
			arb.setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return arb;
	}



	/**
	 * 我的活动收藏查询
	 *
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/collection/collectList", method = RequestMethod.POST)
	public Object collectList(String userId,Integer type,Integer page,Integer pageSize) {
		ApiResultBean arb = new ApiResultBean();
		Map map =new HashMap();
		PageInfo info = new PageInfo();
		try {
			if(type == Integer.parseInt(EnumTypeClazz.TYPE_ACTIVITY.getValue())){//活动
				info = this.colleService.SelectMyActColle(userId,page,pageSize);
				//map.put("list",this.colleService.SelectMyActColle(userId));
			}else if(type == Integer.parseInt(EnumTypeClazz.TYPE_TRAIN.getValue())){//培训
				info = this.colleService.SelectMyTraitmColle(userId,type,page,pageSize);
				//map.put("list",this.colleService.SelectMyTraitmColle(userId));
			}else if(type == Integer.parseInt(EnumTypeClazz.TYPE_VENUE.getValue())){//场馆
				info = this.colleService.SelectMyVenueColle(userId,page,pageSize);
				//map.put("list",this.colleService.SelectMyVenueColle(userId));
			}else if(type == Integer.parseInt(EnumTypeClazz.TYPE_ROOM.getValue())){//活动室
				info = this.colleService.SelectMyRoomColle(userId,page,pageSize);
				//map.put("list",this.colleService.SelectMyRoomColle(userId));
			}
			//map.put("list",this.colleService.getCollectionList(userId,type,page,pageSize));
			arb.setRows(info.getList());
			arb.setPageInfo(info);
		} catch (Exception e) {
			arb.setMsg("获取收藏列表有误");
			arb.setCode(1);
			log.error(e.getMessage(), e);
		}
		return arb;
	}

	/**
	 * 我的培训收藏查询
	 *
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/collection/myTraitmColle", method = RequestMethod.POST)
	public Object SelectMyTraitmColle(String userId,Integer page,Integer pageSize) {
		ApiResultBean arb = new ApiResultBean();
		Map map =new HashMap();
		Integer cmreftyp = 5;
		try {
			map.put("list",this.colleService.SelectMyTraitmColle(userId,cmreftyp,page,pageSize));
			arb.setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return arb;
	}

	/**
	 * 添加我的收藏
	 * 4 活动
	 * 5 培训
	 * 2 场馆
	 * 3 活动室
	 *
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/collection/addMyColle", method = RequestMethod.POST)
	public Object addMyColle(String cmuid,String cmrefid,String cmreftyp,String cmopttyp,String cmurl,String cmtitle,String toproject) {
		WhgCollection whcolle=new WhgCollection();
		ApiResultBean arb = new ApiResultBean();
		Map map =new HashMap();
		try {
			whcolle.setCmid(IDUtils.getID());
			whcolle.setCmdate(new Date());
			whcolle.setCmrefid(cmrefid);
			whcolle.setCmuid(cmuid);
			whcolle.setCmreftyp(cmreftyp);
			whcolle.setCmopttyp(cmopttyp);
			whcolle.setCmurl(cmurl);
			whcolle.setCmtitle(cmtitle);
			whcolle.setSystype(toproject);
			this.colleService.addMyColle(whcolle);
		} catch (Exception e) {
			e.printStackTrace();
			arb.setCode(1);
			arb.setMsg("新增收藏失败");
		}
		return arb;
	}

	/**
	 * 删除我的收藏
	 * 4 活动
	 * 5 培训
	 * 2 场馆
	 * 3 活动室
	 *
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/collection/delMyColle", method = RequestMethod.POST)
	public Object delMyColle(String userId, WebRequest request) {
		WhgCollection whcolle=new WhgCollection();
		ApiResultBean arb = new ApiResultBean();
		Map map =new HashMap();
		try {
			//取得收藏关联参数
			String cmreftyp = request.getParameter("cmreftyp"); // 收藏关联类型
			String cmrefid = request.getParameter("cmrefid"); // 收藏关联id
			String cmuid = request.getParameter("cmuid"); // 用户id
			if(userId==null){
				userId=cmuid;
			}
			this.colleService.removeCommColle(cmreftyp, cmrefid, userId);
		} catch (Exception e) {
			e.printStackTrace();
			arb.setCode(1);
			arb.setMsg("新增收藏失败");
		}
		return arb;
	}

	/**
	 * 判断用户是否点亮收藏
	 *
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/comm/isLightenColle", method = RequestMethod.POST)
	public Object isColle(String userId,String cmreftyp,String cmrefid) {
		ApiResultBean arb = new ApiResultBean();
		Map<String, Object> map = new HashMap<String, Object>();
		String success = "0";
		String errMsg = "";
		String uid = null;
		int scNum = 0;
		try {
			//取得会话
			WhgUser userSess = apiUserService.getUserDetail(userId);
			// 判断会话是否为空
			if (userSess == null) {
				success = "2";
				errMsg = "请登录后再收藏";
				arb.setMsg(errMsg);
			} else {
				// 判断用户是否已收藏
				uid = userSess.getId(); // 获得用户id
				boolean iscolle = this.colleService.isColle(uid, cmreftyp, cmrefid);
				if (iscolle) {
					success = "1"; // 已收藏
				}
			}
            //获取收藏数
            scNum = this.colleService.shouCanShu(cmreftyp, cmrefid);
		} catch (Exception e) {
			success = "3";
			errMsg = e.getMessage();
			arb.setMsg(errMsg);
		}
		map.put("scNum",scNum+"" );
		map.put("success", success);
		arb.setData(map);
		return arb;

	}
	/**
	 * 添加公共收藏
	 *
	 * @param whcolle
	 * @param request
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/comm/addColle", method = RequestMethod.POST)
	public Object addColle(String  userId, WhgCollection whcolle, WebRequest request) {
		ApiResultBean arb = new ApiResultBean();
		String success="0";
		String errMsg="";
		int scNum = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 获取存放在session的用户信息
			WhgUser userSess=apiUserService.getUserDetail(userId);
			if(userSess==null){
				userSess=apiUserService.getUserDetail(whcolle.getCmuid());
			}
			//获取收藏数
			scNum = this.colleService.shouCanShu(whcolle.getCmreftyp(), whcolle.getCmrefid());
			// 判断会话是否为空
			if (userSess == null) {
				arb.setCode(1);
				arb.setMsg( "请登录后再收藏");
				errMsg="请登录后再收藏";
			} else {
				// 添加收藏
				success="1";
				whcolle.setCmid(IDUtils.getID());
				whcolle.setCmuid(userSess.getId()); // 用户id
				whcolle.setCmdate(new Date()); // 用户收藏时间
				whcolle.setCmopttyp("0"); // 操作类型为收藏
				this.colleService.addMyColle(whcolle);
				//消息提醒 暂时屏蔽
				//this.centerAction.addNewAlert(userSess.getId(),"4");//用户中心我的收藏消息提醒
			}
		} catch (Exception e) {
			arb.setCode(1);
			arb.setMsg( e.getMessage());
			errMsg= e.getMessage();
		}
		map.put("scNum", scNum);
		map.put("success", success);
		map.put("errMsg", errMsg);
		arb.setData(map);
		return arb;
	}
	/**
	 * 删除公共收藏
	 *
	 * @param request
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/comm/removeColle", method = RequestMethod.POST)
	public Object removeColle(String userId, WhgCollection whcolle, WebRequest request) {
		ApiResultBean arb = new ApiResultBean();
		String success = "0";
		String errMsg = "";
		Map<String, Object> map = new HashMap<String, Object>();
		String uid = null; // 取得用户id
		int scNum = 0;
		try {
			//取得收藏关联参数
			String cmreftyp = request.getParameter("cmreftyp"); // 收藏关联类型
			String cmrefid = request.getParameter("cmrefid"); // 收藏关联id
			if(cmrefid!=null){
				cmrefid=whcolle.getCmrefid();
			}
			if(cmreftyp!=null){
				cmreftyp=whcolle.getCmreftyp();
			}
			WhgUser userSess=apiUserService.getUserDetail(userId);
			if(userSess==null){
				userSess=apiUserService.getUserDetail(whcolle.getCmuid());
			}
			//获取收藏数
			scNum = this.colleService.shouCanShu(cmreftyp, cmrefid);
			// 判断会话是否为空
			if (userSess == null) {
				arb.setCode(1);
				success = "2";
				errMsg = "请登录";
				arb.setMsg("请登录");
			} else {
				success = "1";
				uid = userSess.getId(); 
				// 删除用户收藏记录
				this.colleService.removeCommColle(cmreftyp, cmrefid, uid);
			}
		} catch (Exception e) {
			arb.setCode(1);
			arb.setMsg("请登录");
			success = "3";
			errMsg = e.getMessage();
		}
		map.put("scNum", scNum);
		map.put("success", success);
		map.put("errMsg", errMsg);
		arb.setData(map);
		return arb;
	}

	/**
	 * 判断用户是否点亮点赞
	 *
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/comm/isLightenGood", method = RequestMethod.POST)
	public Object IsGood(HttpServletRequest servletRequest, String userId, String cmreftyp,String cmrefid) {
		ApiResultBean arb = new ApiResultBean();
		Map<String, Object> map = new HashMap<String, Object>();
		String success = "0";
		String errMsg = "";
		String num = "0";
		
		try {
			//判断用户id是否为空
			if(userId == null){
				// 获取点赞ip地址
				ReqParamsUtil IP = new ReqParamsUtil();
				String dzIP = IP.gerClientIP(servletRequest);
				//判断是否有点赞记录
				boolean isgood = this.colleService.IsGood(dzIP, cmreftyp, cmrefid);
				if (isgood) {
					success = "1"; // 已点赞
				}
			}else{
				//用户id不为空
				boolean isgood = this.colleService.IsGood(userId, cmreftyp, cmrefid);
				if (isgood) {
					success = "1"; // 已点赞
				}
			}
			
			//设置被点赞次数
			num = this.colleService.dianZhanShu(cmreftyp, cmrefid)+"";
		} catch (Exception e) {
			success = "2";
			errMsg = e.getMessage();
			arb.setMsg(errMsg);
			arb.setCode(1);
		}
		map.put("success", success);
		map.put("num", num);
		arb.setData(map);
		return arb;
	}

	/**
	 * 添加点赞
	 *
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/comm/addGood", method = RequestMethod.POST)
	public Object addGood(String userId, HttpServletRequest servletRequest,String cmrefid,String cmreftyp,String cmurl,String cmtitle) {
		ApiResultBean arb = new ApiResultBean();
		WhgCollection whcolle=new WhgCollection();
		String success = "0";
		String errMsg = "";
		// 获取点赞ip地址
		ReqParamsUtil IP = new ReqParamsUtil();
		String dzIP = IP.gerClientIP(servletRequest);

		Map<String, Object> map = new HashMap<String, Object>();
		String num = "0";
		try {
			whcolle.setCmrefid(cmrefid);
			whcolle.setCmreftyp(cmreftyp);
			whcolle.setCmurl(cmurl);
			whcolle.setCmtitle(cmtitle);

			// 判断用户id是否为空	 null:根据ip地址添加点赞记录	 不为null:根据用户id添加点赞记录
			if (userId == null) {
				whcolle.setCmid(IDUtils.getID());
				whcolle.setCmuid(dzIP);
				whcolle.setCmdate(new Date()); // 收藏时间
				whcolle.setCmopttyp("2"); // 操作类型为点赞
				if(!colleService.IsGood(dzIP,cmreftyp,cmrefid)){
					this.colleService.addGood(whcolle);
				}
			} else {
				whcolle.setCmid(IDUtils.getID());
				whcolle.setCmuid(userId);
				whcolle.setCmdate(new Date()); // 收藏时间
				whcolle.setCmopttyp("2"); // 操作类型为点赞
				if(!colleService.IsGood(userId,cmreftyp,cmrefid)) {
					this.colleService.addGood(whcolle);
				}
			}
			num = this.colleService.dianZhanShu(whcolle.getCmreftyp(), whcolle.getCmrefid())+"";
		} catch (Exception e) {
			success = "1";
			errMsg = e.getMessage();
			arb.setMsg(errMsg);
			arb.setCode(1);
		}
		map.put("success", success);
		map.put("num", num);
		arb.setData(map);
		return arb;
	}



	/**
	 * 收藏页面加载数据
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/collection/col/loadcoll", method = RequestMethod.POST)
	public Object loadcoll(WebRequest request, String userId) {
		ApiResultBean arb = new ApiResultBean();
		try {
			return this.colleService.loadcoll(request, userId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			arb.setCode(1);
			arb.setMsg(e.getMessage());
		}
		return arb;
	}
}
