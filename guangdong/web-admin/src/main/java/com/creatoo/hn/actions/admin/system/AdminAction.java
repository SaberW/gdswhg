package com.creatoo.hn.actions.admin.system;

import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.model.WhMenu;
import com.creatoo.hn.model.WhgAdminHome;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.admin.system.AdminService;
import com.creatoo.hn.utils.CacheUtil;
import com.creatoo.hn.utils.CompareTime;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author wangxl
 *
 */
@RestController
@RequestMapping("/admin")
public class AdminAction {
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private AdminService adminService;

	/**
	 * 请求映射
	 */
	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	/**
	 * 用于获取所有请求映射
	 * @return
	 */
	public List<HashMap<String, Object>> initRequestMappingMethod() {
		List<HashMap<String, Object>> urlList = new ArrayList<HashMap<String, Object>>();
		Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			RequestMappingInfo info = m.getKey();
			HandlerMethod method = m.getValue();
			PatternsRequestCondition p = info.getPatternsCondition();
			for (String url : p.getPatterns()) {
				hashMap.put("url", url);
			}
			hashMap.put("className", method.getMethod().getDeclaringClass().getName()); // 类名
			hashMap.put("class", method.getMethod()); // class
			hashMap.put("method", method.getMethod().getName()); // 方法名
			RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
			String type = methodsCondition.toString();
			if (type != null && type.startsWith("[") && type.endsWith("]")) {
				type = type.substring(1, type.length() - 1);
				hashMap.put("type", type); // 方法名
			}
			urlList.add(hashMap);
		}
		return urlList;
	}

	/**进入后台首页
	 * @return
	 */
	@RequestMapping("")
	public ModelAndView index(){
		return new ModelAndView( "admin/main" );
	}

    /**进入后台首页内容页
     * @return
     */
    @RequestMapping("/admin_home")
	public ModelAndView adminHome() {
		//ModelAndView view = new ModelAndView("admin/admin_home");
		ModelAndView view = new ModelAndView("admin/rep/view_index");
		try {
			//统计PV|UV|IP
			view.addObject("tongji_comm", adminService.tongji_PV_UV_IP());

			//统计当月新增用户数
			Date now = new Date();
			String month = new java.text.SimpleDateFormat("yyyy-MM").format(now);
			String month_title = new java.text.SimpleDateFormat("yyyy年MM月").format(now);
			String month_val = new java.text.SimpleDateFormat("yyyyMM").format(now);
			view.addObject("tongji_add_user_month", adminService.tongji_add_user_month(month));
			view.addObject("tongji_add_user_month_title", month_title);
			view.addObject("tongji_add_user_month_val", month_val);

			//统计本年新增用户数
			String year = new java.text.SimpleDateFormat("yyyy").format(now);
			view.addObject("tongji_add_user_year", adminService.tongji_add_user_year(year));
			view.addObject("tongji_add_user_year_title", year+"年");

			//统计活跃用户
			view.addObject("tongji_active_user", adminService.tongji_active_user());

			//统计年度用户粘度
			view.addObject("tongji_active_user_year", adminService.tongji_active_user_year(year));
			view.addObject("tongji_active_user_year_title", year+"年");


			//统计年度用户参加活动
			view.addObject("tongji_user_act", adminService.tongji_user_act(year));
			view.addObject("tongji_user_act_title", year+"年");

			//统计年度用户参加培训
			view.addObject("tongji_user_tra", adminService.tongji_user_tra(year));
			view.addObject("tongji_user_tra_title", year+"年");

			//view.addObject("inCount", adminService.t_srchList());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return view;
	}

	/**
	 * 根据指定month，查询上月，当月， 下月的用户新增数据
	 * @param month yyyy-MM
	 * @param type pre|next|curt
	 * @return
	 */
	@RequestMapping("/tongjiMonthAddUser")
	public Map<String, Object> tongji_add_user_month(String month, String type) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		try {
			//type: pre|next|curt
			java.text.SimpleDateFormat sdf_yyyyMM = new java.text.SimpleDateFormat("yyyyMM");
			java.text.SimpleDateFormat sdf_yyyyMM_2 = new java.text.SimpleDateFormat("yyyy年MM月");
			java.text.SimpleDateFormat sdf_yyyyMM_3 = new java.text.SimpleDateFormat("yyyy-MM");
			Date monthDate = null;
			if("curt".equals(type)){
				monthDate = new Date();
				month = sdf_yyyyMM_3.format(monthDate);
			}else if("pre".equals(type)){
				monthDate = sdf_yyyyMM.parse(month);
				java.util.Calendar c = java.util.Calendar.getInstance();
				c.setTime(monthDate);
				c.add(Calendar.MONTH, -1);
				monthDate = c.getTime();
				month = sdf_yyyyMM_3.format(monthDate);
			}else if("next".equals(type)){
				monthDate = sdf_yyyyMM.parse(month);
				java.util.Calendar c = java.util.Calendar.getInstance();
				c.setTime(monthDate);
				c.add(Calendar.MONTH, 1);
				monthDate = c.getTime();
				month = sdf_yyyyMM_3.format(monthDate);
			}

			//统计当月新增用户数
			String month_title = sdf_yyyyMM_2.format(monthDate);
			String month_val = sdf_yyyyMM.format(monthDate);
			rtnMap.put("tongji_add_user_month", adminService.tongji_add_user_month(month));
			rtnMap.put("tongji_add_user_month_title", month_title);
			rtnMap.put("tongji_add_user_month_val", month_val);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return rtnMap;
	}


	/**处理管理员登录
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/logindo")
	public ModelAndView logindo(HttpSession session, HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		String sessionId = session.getId();

		try {
			sessionId = session.getId();
			if(isLock(sessionId)){
				mav.addObject("isLock","0");
				mav.addObject("msg", "连续登录失败多次，请15分钟后再登录！");
				mav.setViewName("admin/login");
				return mav;
			}

			String name = request.getParameter("name");
			String password = request.getParameter("password");
			Object admin = this.adminService.logindo(name, password);

			if (admin != null){
				session.setAttribute("user", admin);
				mav.addObject("isLock","1");
				setLogin(sessionId,true);
				mav.setViewName("redirect:/admin");
			}else{
				if(setLogin(sessionId,false)){
					mav.addObject("isLock","0");
					mav.addObject("msg", "连续登录失败多次，请15分钟后再登录！");
				}else {
					mav.addObject("msg", "用户名或密码不正确");
				}
				mav.setViewName("admin/login");
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			//mav.addObject("msg", "用户名或密码不正确");
			//mav.setViewName("admin/login");

			if(setLogin(sessionId,false)){
				mav.addObject("isLock","0");
				mav.addObject("msg", "连续登录失败多次，请15分钟后再登录！");
			}else {
				mav.addObject("msg", "用户名或密码不正确");
			}
			mav.setViewName("admin/login");

		}

		return mav;
	}

	@SuppressWarnings("all")
	private boolean setLogin(String sessionId, boolean isSuccess){
		try {
			if(isSuccess){//登录成功，清除缓存
				CacheUtil.delCache("myClientSession",sessionId);
				return false;
			}
			Object myClientSession = CacheUtil.getCache("myClientSession",sessionId);
			if(null == myClientSession){//会话缓存数据，失败时记录
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("count",1);
				CacheUtil.putCache("myClientSession",sessionId, jsonObject,3600);
				return false;
			}
            Object cacheVal = CacheUtil.getCache("myClientSession",sessionId);
			JSONObject jsonObject = (JSONObject) cacheVal;
			int count = jsonObject.getInteger("count");//失败次数加1
			count++;
			if(count < 4){//3次以内, 记会话
				jsonObject.put("count",count);
				CacheUtil.putCache("myClientSession",sessionId,jsonObject,3600);
				return false;
			}else {//超过5次，锁住不让登录
				CacheUtil.delCache("myClientSession",sessionId);
				JSONObject lock = new JSONObject();
				lock.put("sessionId",new Date());
				CacheUtil.putCache("myClientSessionLock",sessionId,lock);
				return true;
			}
		}catch (Exception e){
			log.error(e.toString());
			return false;
		}
	}

	@SuppressWarnings("all")
	private boolean isLock(String sessionId){
		try {
			JSONObject jsonObject = (JSONObject) CacheUtil.getCache("myClientSessionLock",sessionId);
			Date date = jsonObject.getDate("sessionId");
			int saveSeconds = CompareTime.date2Seconds(date);
			saveSeconds += CompareTime.getSecondsByMin(15);//设置登录锁定时长
			int nowSeconds = CompareTime.date2Seconds(new Date());
			if(saveSeconds < nowSeconds){
				return false;
			}
			return true;
		}catch (Exception e){
			log.error(e.toString());
			return false;
		}
	}


	/**处理管理员登出
	 * @param session
	 * @return
	 */
	@RequestMapping("/loginout")
	public ModelAndView loginout(HttpSession session){
		ModelAndView mav = new ModelAndView();

		try {
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.getSession().removeAttribute("user");
			currentUser.logout();

			session.removeAttribute("user");
			mav.setViewName("admin/login");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			mav.setViewName("admin/login");
		}
		return mav;
	}

	/**进入管理菜单界面
	 * @return
	 */
	@RequestMapping("/menulist")
	public ModelAndView toMenuList(){
		return new ModelAndView("admin/system/menulist");
	}

	/**加载菜单树型数据
	 * @return
	 */
	@RequestMapping("/loadMenus")
	public Object getMenuData(String type){
		try {
			return this.adminService.getMenuList(type);
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			return null;
		}
	}


	/**获取父级菜单可选项
	 * @return
	 */
	@RequestMapping("/loadParentList")
	public Object getParentList(){
		try {
			return this.adminService.getMenuParent();
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			return null;
		}
	}

	/**添加菜单记录
	 * @param menu
	 * @return
	 */
	@RequestMapping("/addMenuItem")
	public Object addMenuItem(WhMenu menu){
		try {
			this.adminService.addMenuItem(menu);
			return "success";
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			return "error";
		}
	}

	/**编辑菜单项信息
	 * @param menu
	 * @return
	 */
	@RequestMapping("/editMenuItem")
	public Object editMenuItem(WhMenu menu){
		try {
			this.adminService.editMenuItem(menu);
			return "success";
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			return "error";
		}
	}

	/**删除菜单项信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/removeMenuItem")
	public Object removeMenuItem(String id){
		try {
			this.adminService.removeMenuItem(id);
			return "success";
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			return "error";
		}
	}
	/**
	 * 修改密码
	 */
    @RequestMapping("/modipasManage")
    public Object upmodifyPwd(HttpSession session, String password1, String password2, HttpServletRequest request) {
        String success = "0";
        String errmasg = "";
        try {
            //得到session值 向下类型转换
            WhgSysUser user = (WhgSysUser) session.getAttribute("user");
            String account = user.getAccount();
            String pwd = user.getPassword();
            //判断用户不是超级管理员
            if (account != "administrator") {
                if (pwd.equals(password1)) {
                	String password4 = request.getParameter("password4");
                    this.adminService.selectmagr(account, password2, password4);
                } else {
                    success = "1";
                    errmasg = "密码错误";
                }
            }
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
	 * 查询列表
	 *
	 * @return 。
	 */
	@RequestMapping(value = "/srchList")
	public List<WhgAdminHome> srchList() {
		List<WhgAdminHome> list = new ArrayList<>();
		try {
			 list = this.adminService.t_srchList();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * 首页统计（培训根据文化馆分类）
	 *
	 * @return 。
	 */
	@RequestMapping(value = "/traGroupByCult")
	public List<WhgAdminHome> traGroupByCult() {
		List<WhgAdminHome> list = new ArrayList<>();
		try {
			list = this.adminService.traGroupByCult();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * 首页统计（培训根据艺术类型分类）
	 *
	 * @return 。
	 */
	@RequestMapping(value = "/traGroupByArt")
	public List<WhgAdminHome> traGroupByArt() {
		List<WhgAdminHome> list = new ArrayList<>();
		try {
			list = this.adminService.traGroupByArt();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * 首页统计（培训根据艺术类型分类）
	 *
	 * @return 。
	 */
	@RequestMapping(value = "/actGroupByArt")
	public List<WhgAdminHome> actGroupByArt() {
		List<WhgAdminHome> list = new ArrayList<>();
		try {
			list = this.adminService.actGroupByArt();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}
}
