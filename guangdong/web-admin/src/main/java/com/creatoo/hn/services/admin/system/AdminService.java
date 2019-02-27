package com.creatoo.hn.services.admin.system;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.mapper.statistics.StatisticsMapper;
import com.creatoo.hn.model.WhgAdminHome;
import com.creatoo.hn.mapper.WhMenuMapper;
import com.creatoo.hn.mapper.WhMgrMapper;
import com.creatoo.hn.mapper.WhgAdminHomeMapper;
import com.creatoo.hn.mapper.WhgSysUserMapper;
import com.creatoo.hn.model.WhMenu;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.comm.MenusService;
import com.creatoo.hn.utils.MD5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.text.NumberFormat;
import java.util.*;

@Service
@SuppressWarnings("all")
public class AdminService {
	
	@Autowired
	private WhMgrMapper mgrMapper;

	@Autowired
	private WhgSysUserMapper whgSysUserMapper;
	@Autowired
	private WhMenuMapper menuMapper;
	@Autowired
	private CommService commService;

    @Autowired
    private MenusService menusService;

	@Autowired
	private WhgAdminHomeMapper whgAdminHomeMapper;

	@Autowired
	private StatisticsMapper statisticsMapper;
	
	/**登录验证
	 * @param name
	 * @param pwd
	 * @return
	 */
	public Object logindo(String name, String pwd)throws Exception{
		WhgSysUser mgr = null;
		try {
			
			Subject currentUser = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(name, pwd);
			currentUser.login(token);
			mgr = (WhgSysUser)currentUser.getSession().getAttribute("user");
			
//			WhMgr record = new WhMgr();
//			record.setName(name);
//			record.setPassword(pwd);
//			String account = mgr.getAccount();
//			WhgSysUser whgSysUser = new WhgSysUser();
//			mgr = this.whgSysUserMapper.selectOne(mgr);
//			mgr.getAccount()

		} catch (Exception e) {
			throw e;
		}
		return mgr;
	}
	
	/**提取全部的树型菜单内容
	 * @return
	 * @throws Exception
	 */
	public Object getMenuList(String type) throws Exception{
		/*//List<WhMenu> list = this.menuMapper.selectAll();
		Example example = new Example(WhMenu.class);
		example.setOrderByClause("parent,idx");
		List<WhMenu> mlist = this.menuMapper.selectByExample(example);
		
		//权限处理
		List<WhMenu> list = new ArrayList<WhMenu>();
		if(!"off".equals(type)){
			if(mlist != null){
				Subject currentUser = SecurityUtils.getSubject();
				for(WhMenu menu : mlist){
					String permission = menu.getId()+":view";
					if(menu.getType() != 1 || currentUser.isPermitted(permission)){
						list.add(menu);
					}
				}
			}
		}else{
			list = mlist;
		}*/
		
		/*List<Object> res = new ArrayList<Object>();
		for (int i=0; i<list.size(); i++) {
			WhMenu whMenu = list.get(i);
			if (whMenu.getId() == null || whMenu.getId().isEmpty()){
				continue;
			}
			if (whMenu.getParent() == null || "".equals(whMenu.getParent().trim())){
				Map item = BeanUtils.describe(whMenu);
				this.compMenuTree(item, list, i);
				res.add(item);
				
				list.remove(i);
				--i;
			}
		}
		return res;*/

        return this.menusService.getMeunsTree4Auth(SecurityUtils.getSubject());
	}
	
//	/**帮助组装树型菜单
//	 * @param item
//	 * @param list
//	 */
//	private void compMenuTree(Map<String,Object> item, List<WhMenu> list, int refidx)throws Exception{
//		if (item.get("children")==null){
//			item.put("children", new ArrayList<Object>());
//		}
//		for(int i=0; i<list.size(); i++){
//			WhMenu m = list.get(i);
//			if (m.getId()==null || m.getId().isEmpty()){
//				continue;
//			}
//			if (m.getParent()==null || m.getParent().equals(m.getId())){
//				continue;
//			}
//
//			if (item.get("id").equals(m.getParent())){
//				List children = (List) item.get("children");
//				try {
//					Map _item = BeanUtils.describe(m);
//					this.compMenuTree(_item, list, refidx);
//					children.add(_item);
//					/*list.remove(i);
//					if (refidx >= i){
//						--refidx;
//					}
//					--i;*/
//				} catch (Exception e) {
//					//...
//				}
//			}
//		}
//	}
	
	/**按Id取得菜单项
	 * @param id
	 * @return
	 */
	public Object getMenu4Id(Object id)throws Exception {
		return this.menuMapper.selectByPrimaryKey(id);
	}
	
	/**获取添加编辑菜单时供选择的上级菜单项
	 * @return
	 * @throws Exception
	 */
	public Object getMenuParent() throws Exception{
		Example example = new Example(WhMenu.class);
		example.or().andEqualTo("type", 0);
		example.setOrderByClause("parent,idx");
		return this.menuMapper.selectByExample(example);
	}
	
	/**添加菜单信息
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public Object addMenuItem(WhMenu menu) throws Exception{
		menu.setId( this.commService.getKey("WhMenu") );
		
		Example example = new Example(WhMenu.class);
		example.or().andEqualTo("parent", menu.getParent());
		int count = this.menuMapper.selectCountByExample(example);
		
		menu.setIdx(count+1);
		
		int recount = this.menuMapper.insert(menu);
		return recount;
	}
	
	/**编辑菜单信息
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public Object editMenuItem(WhMenu menu) throws Exception{
		int recount = this.menuMapper.updateByPrimaryKeySelective(menu);
		return recount;
	}
	
	/**删除菜单信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Object removeMenuItem(String id) throws Exception{
		int recount = this.menuMapper.deleteByPrimaryKey(id);
		return recount;
	}
	/**
	 * 修改密码
	 * @param name 管理员账号
	 * @param password2 修改后的密码
	 * @param password4 修改后的密码明文
	 * @return 
	 */
	public void selectmagr(String account, String password2, String password4) throws Exception {
		WhgSysUser whgSysUser = new WhgSysUser();
//		WhMgr whm = new WhMgr();
		whgSysUser.setPassword(password2);
		whgSysUser.setEpms(MD5Util.encode4Base64(password4));
		Example example = new Example(WhgSysUser.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("account", account);
		this.whgSysUserMapper.updateByExampleSelective(whgSysUser, example);
	}

	/**
	 * 首页统计数据查询
	 *
	 * @return
	 * @throws Exception
	 */
	public List<WhgAdminHome> t_srchList() throws Exception {
		return this.whgAdminHomeMapper.homeCount();
	}

	/**
	 * 首页统计（培训根据文化馆分类）
	 *
	 * @return
	 * @throws Exception
	 */
	public List<WhgAdminHome> traGroupByCult() throws Exception {
		return whgAdminHomeMapper.traGroupByCult();
	}

	/**
	 * 首页统计（培训根据艺术类型分类）
	 *
	 * @return
	 * @throws Exception
	 */
	public List<WhgAdminHome> traGroupByArt() throws Exception {
		return whgAdminHomeMapper.traGroupByArt();
	}

	/**
	 * 首页统计（活动根据艺术类型分类）
	 *
	 * @return
	 * @throws Exception
	 */
	public List<WhgAdminHome> actGroupByArt() throws Exception {
		return whgAdminHomeMapper.actGroupByArt();
	}

	public Object getIndexTongjiData()throws Exception{
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		//PV/UV/IP统计
		List<Map> pv_uv_ip = this.statisticsMapper.selectSYS_PV_UV_IP();
		if(pv_uv_ip != null){
			for(Map map : pv_uv_ip){
				String srchKey = (String) map.get("srchKey");
				Object srchVal = map.get("srchVal");
				if("PV".equals(srchKey)) rtnMap.put("PV", srchVal);
				if("UV".equals(srchKey)) rtnMap.put("UV", srchVal);
				if("IP".equals(srchKey)) rtnMap.put("IP", srchVal);
				if("USERS".equals(srchKey)) rtnMap.put("USERS", srchVal);
				if("REL_USERS".equals(srchKey)) rtnMap.put("REL_USERS", srchVal);
				if("ACTS".equals(srchKey)) rtnMap.put("ACTS", srchVal);
				if("TRAS".equals(srchKey)) rtnMap.put("TRAS", srchVal);
				if("VENS".equals(srchKey)) rtnMap.put("VENS", srchVal);
				if("PV_PC".equals(srchKey)) rtnMap.put("PV_PC", srchVal);
				if("PV_WX".equals(srchKey)) rtnMap.put("PV_WX", srchVal);
				if("USERS_WX".equals(srchKey)) rtnMap.put("USERS_WX", srchVal);
			}
		}


		//年用户新增趋势
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("year", "2017");
		List yearAddUser = this.statisticsMapper.selectYearAddUser(params);

		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("month", "2017-06");
		List monthAddUser = this.statisticsMapper.selectMonthAddUser(params2);
		System.out.println( "getIndexTongjiData================================" );

		return null;
	}

	/**
	 * 统计当日的PV|UV|IP|用户数|实名用户数|活动|培训|场馆
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tongji_PV_UV_IP()throws Exception{
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		List<Map> pv_uv_ip = this.statisticsMapper.selectSYS_PV_UV_IP();
		if(pv_uv_ip != null){
			for(Map map : pv_uv_ip){
				String srchKey = (String) map.get("srchKey");
				Object srchVal = map.get("srchVal");

				if("PV".equals(srchKey)) rtnMap.put("PV", java.text.NumberFormat.getInstance().format(srchVal));
				if("UV".equals(srchKey)) rtnMap.put("UV", java.text.NumberFormat.getInstance().format(srchVal));
				if("IP".equals(srchKey)) rtnMap.put("IP", java.text.NumberFormat.getInstance().format(srchVal));
				if("USERS".equals(srchKey)) rtnMap.put("USERS", java.text.NumberFormat.getInstance().format(srchVal));
				if("REL_USERS".equals(srchKey)) rtnMap.put("REL_USERS", java.text.NumberFormat.getInstance().format(srchVal));
				if("ACTS".equals(srchKey)) rtnMap.put("ACTS", java.text.NumberFormat.getInstance().format(srchVal));
				if("TRAS".equals(srchKey)) rtnMap.put("TRAS", java.text.NumberFormat.getInstance().format(srchVal));
				if("VENS".equals(srchKey)) rtnMap.put("VENS", java.text.NumberFormat.getInstance().format(srchVal));
				if("PV_PC".equals(srchKey)) rtnMap.put("PV_PC", java.text.NumberFormat.getInstance().format(srchVal));
				if("PV_WX".equals(srchKey)) rtnMap.put("PV_WX", java.text.NumberFormat.getInstance().format(srchVal));
				if("USERS_WX".equals(srchKey)) rtnMap.put("USERS_WX", java.text.NumberFormat.getInstance().format(srchVal));
			}
		}
		return rtnMap;
	}

	/**
	 * 统计某月用户新增数据
	 * @param month yyyy-MM格式指定的月份
	 * @return 某月用户新增数据 [['1日',3],['2日', 5],...]
	 * @throws Exception
	 */
	public JSONArray tongji_add_user_month(String month)throws Exception{
		//返回数据
		JSONArray rtnArr = new JSONArray();

		//如果参数不正确，取当月
		Date monthDate = null;
		if(month != null){
			try {
				monthDate = new java.text.SimpleDateFormat("yyyy-MM").parse(month);
			}catch(Exception e){
				monthDate = new Date();
				month = new java.text.SimpleDateFormat("yyyy-MM").format(monthDate);
			}
		}else{
			monthDate = new Date();
			month = new java.text.SimpleDateFormat("yyyy-MM").format(monthDate);
		}


		//查询数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("month", month);
		//获取月份的最大天数
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(monthDate);
		params.put("maxDays", c.getActualMaximum(Calendar.DAY_OF_MONTH)+"");
		List<Map> monthAddUser = this.statisticsMapper.selectMonthAddUser(params);
		if(monthAddUser != null){
			for(Map map : monthAddUser){
				String srchKey = (String) map.get("srchKey");
				Object srchVal = map.get("srchVal");

				JSONArray tarr = new JSONArray();
				tarr.add(srchKey);
				tarr.add(srchVal);
				rtnArr.add(tarr);
			}
		}


		return rtnArr;
	}

	/**
	 * 统计某年用户新增数据
	 * @param month yyyy-MM格式指定的月份
	 * @return 某月用户新增数据 [['1月',3],['2月', 5],...]
	 * @throws Exception
	 */
	public JSONArray tongji_add_user_year(String year)throws Exception{
		//返回数据
		JSONArray rtnArr = new JSONArray();

		//如果参数不正确，取当月
		Date yearDate = null;
		if(year != null){
			try {
				yearDate = new java.text.SimpleDateFormat("yyyy").parse(year);
			}catch(Exception e){
				yearDate = new Date();
				year = new java.text.SimpleDateFormat("yyyy").format(yearDate);
			}
		}else{
			yearDate = new Date();
			year = new java.text.SimpleDateFormat("yyyy").format(yearDate);
		}


		//查询数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("year", year);
		List<Map> yearAddUser = this.statisticsMapper.selectYearAddUser(params);
		if(yearAddUser != null){
			for(Map map : yearAddUser){
				String srchKey = (String) map.get("srchKey");
				Object srchVal = map.get("srchVal");

				JSONArray tarr = new JSONArray();
				tarr.add(srchKey);
				tarr.add(srchVal);
				rtnArr.add(tarr);
			}
		}

		return rtnArr;
	}

	/**
	 * 统计活跃用户
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tongji_active_user()throws Exception{
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		//如果参数不正确，取7天之前的日期，即一周登录过系统的为活跃用户
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.add(Calendar.DAY_OF_WEEK, -7);
		Date startDate = c.getTime();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		List<Map> activeUser = this.statisticsMapper.selectActiveUser(params);
		if(activeUser != null){
			for(Map map : activeUser){
				String srchKey = (String) map.get("srchKey");
				Object srchVal = map.get("srchVal");

				if("PC_ACTIVE_USERS".equals(srchKey)) rtnMap.put("PC_ACTIVE_USERS", java.text.NumberFormat.getInstance().format(srchVal));
				if("WX_ACTIVE_USERS".equals(srchKey)) rtnMap.put("WX_ACTIVE_USERS", java.text.NumberFormat.getInstance().format(srchVal));
				if("USERS".equals(srchKey)) rtnMap.put("USERS", java.text.NumberFormat.getInstance().format(srchVal));
				if("USERS_WX".equals(srchKey)) rtnMap.put("USERS_WX", java.text.NumberFormat.getInstance().format(srchVal));
			}
		}
		return rtnMap;
	}

	/**
	 * 查询某年的粘度用户
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public JSONArray tongji_active_user_year(String year)throws Exception{
		//返回数据
		JSONArray rtnArr = new JSONArray();

		//如果参数不正确，取当月
		Date yearDate = null;
		if(year != null){
			try {
				yearDate = new java.text.SimpleDateFormat("yyyy").parse(year);
			}catch(Exception e){
				yearDate = new Date();
				year = new java.text.SimpleDateFormat("yyyy").format(yearDate);
			}
		}else{
			yearDate = new Date();
			year = new java.text.SimpleDateFormat("yyyy").format(yearDate);
		}


		//查询数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("year", year);
		List<Map> yearAddUser = this.statisticsMapper.selectActiveUserForYear(params);
		if(yearAddUser != null){
			for(Map map : yearAddUser){
				String srchKey = (String) map.get("srchKey");
				Object srchVal = map.get("srchVal");

				JSONArray tarr = new JSONArray();
				tarr.add(srchKey);
				tarr.add(srchVal);
				rtnArr.add(tarr);
			}
		}

		return rtnArr;
	}


	/**
	 * 统计某年用户参加活动的情况
	 * @param month yyyy-MM格式指定的月份
	 * @return [['1月',3],['2月', 5],...]
	 * @throws Exception
	 */
	public JSONArray tongji_user_act(String year)throws Exception{
		//返回数据
		JSONArray rtnArr = new JSONArray();

		//如果参数不正确，取当月
		Date yearDate = null;
		if(year != null){
			try {
				yearDate = new java.text.SimpleDateFormat("yyyy").parse(year);
			}catch(Exception e){
				yearDate = new Date();
				year = new java.text.SimpleDateFormat("yyyy").format(yearDate);
			}
		}else{
			yearDate = new Date();
			year = new java.text.SimpleDateFormat("yyyy").format(yearDate);
		}


		//查询数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("year", year);
		List<Map> yearAddUser = this.statisticsMapper.selectUserJoinAct(params);
		if(yearAddUser != null){
			for(Map map : yearAddUser){
				String srchKey = (String) map.get("srchKey");
				Object srchVal = map.get("srchVal");

				JSONArray tarr = new JSONArray();
				tarr.add(srchKey);
				tarr.add(srchVal);
				rtnArr.add(tarr);
			}
		}

		return rtnArr;
	}

	/**
	 * 统计某年用户参加培训的情况
	 * @param month yyyy-MM格式指定的月份
	 * @return [['1月',3],['2月', 5],...]
	 * @throws Exception
	 */
	public JSONArray tongji_user_tra(String year)throws Exception{
		//返回数据
		JSONArray rtnArr = new JSONArray();

		//如果参数不正确，取当月
		Date yearDate = null;
		if(year != null){
			try {
				yearDate = new java.text.SimpleDateFormat("yyyy").parse(year);
			}catch(Exception e){
				yearDate = new Date();
				year = new java.text.SimpleDateFormat("yyyy").format(yearDate);
			}
		}else{
			yearDate = new Date();
			year = new java.text.SimpleDateFormat("yyyy").format(yearDate);
		}


		//查询数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("year", year);
		List<Map> yearAddUser = this.statisticsMapper.selectUserJoinTra(params);
		if(yearAddUser != null){
			for(Map map : yearAddUser){
				String srchKey = (String) map.get("srchKey");
				Object srchVal = map.get("srchVal");

				JSONArray tarr = new JSONArray();
				tarr.add(srchKey);
				tarr.add(srchVal);
				rtnArr.add(tarr);
			}
		}

		return rtnArr;
	}



	public static void main(String[] args)throws Exception {
		Date monthDate = new Date();
		monthDate = new java.text.SimpleDateFormat("yyyy-MM").parse("2017-02");

		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(monthDate);
		System.out.println(c.getActualMaximum(Calendar.DAY_OF_MONTH));

		System.out.println("-----D:"+NumberFormat.getInstance().format(132424)+"----------------");
		;
	}

}
