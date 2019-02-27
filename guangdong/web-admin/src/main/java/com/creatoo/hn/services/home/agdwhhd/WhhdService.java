package com.creatoo.hn.services.home.agdwhhd;

import java.text.SimpleDateFormat;
import java.util.*;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumOptType;
import com.creatoo.hn.ext.emun.EnumOrderType;
import com.creatoo.hn.mapper.*;
import com.creatoo.hn.mapper.home.CrtTicketMapper;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.comm.SMSService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.mapper.home.CrtWeiXinMapper;
import com.creatoo.hn.mapper.home.CrtWhhdActTimeMapper;
import com.creatoo.hn.mapper.home.CrtWhhdMapper;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.utils.ReqParamsUtil;
import com.creatoo.hn.utils.WhConstance;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import sun.util.resources.th.CalendarData_th;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 文化活动
 * @author yanjianbo
 * @version 2016.11.16
 */
@SuppressWarnings("ALL")
@Service
public class WhhdService {
	/**
	 * 日志控制器
	 */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 公用服务类
	 */
	@Autowired
	public CommService commservice;

    @Autowired
    public SMSService smsService;
	
	/**
	 * 活动
	 */
	@Autowired
	private WhgActActivityMapper activityMapper;
//	private WhActivityMapper activityMapper;
	
	/**
	 * 活动场次
	 */
	@Autowired
	private WhgActTimeMapper whgActTimeMapper;
	/**
	 * 品牌活动
	 */
	@Autowired
	private WhBrandMapper brandMapper;
	/**
	 *活动 语句xml
	 */
	@Autowired
	private ActivityMapper aMapper;
	/***
	 * 页面配置元素
	 */
	@Autowired
	private WhCfgListMapper cfgListMapper;
	
	/***
	 * 栏目内容
	 */
	@Autowired
	private WhZxColinfoMapper colinfoMapper;

	/**
	 * 活动标签
	 */
	@Autowired
	private WhTagMapper whTagMapper;
	/**
	 * 活动场次
	 */
	@Autowired
	private WhActivityitmMapper activityitmMapper;
	/**
	 *报名 
	 */
	@Autowired
	private WhActivitybmMapper bmMapper;
	
	/**
	 * 活动资源
	 */
	@Autowired
	private WhgComResourceMapper whgComResourceMapper;
	/**
	 * 活动品牌关联
	 */
	@Autowired
	private WhBrandActMapper bamapper;
	
	/**
	 * 广告
	 */
	@Autowired
	private WhCfgAdvMapper cfgAdvMapper;
	
	@Autowired
	private  WhgActOrderMapper whgActOrderMapper;
	
	/**
	 * 座位mapper
	 */
	@Autowired
	private WhgActSeatMapper whgActSeatMapper;
	
	/**
	 * 座位订单mapper
	 */
	@Autowired
	private WhgActTicketMapper whgActTicketMapper;
	
	/**
	 * 多表查询订单座位信息
	 */
	@Autowired
	private CrtWhhdMapper crtWhhdMapper;
	
	/**
	 * 查询时间场次表
	 */
	@Autowired
	private CrtWhhdActTimeMapper crtWhhdActTimeMapper;
	
	/**
	 * 微信用户表
	 */
	@Autowired
	private CrtWeiXinMapper crtWeiXinMapper;
	
	/**
	 * 黑名单mapper
	 */
	@Autowired
	private WhgUsrBacklistMapper whgUsrBacklistMapper;

	@Autowired
	private WhUserMapper whUserMapper;

    @Autowired
    private CrtTicketMapper crtTicketMapper;
	
	
	private final  String SUCCESS="0";//成功
	
	/**
	 * 品牌活动
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("all")
	public Object selectBrandList(WebRequest request) throws Exception{
		Map<String,Object> params = ReqParamsUtil.parseRequest(request);
		String bratitle = (String)params.get("bratitle");
		
		String SYS_ORDER = WhConstance.getSysProperty("SYS_ORDER");
		if(SYS_ORDER != null && "1".equals(SYS_ORDER)){
			params.put("sysorder", "1");
		}
		
		if(bratitle != null){
			params.put("bratitle", "%"+bratitle+"%");
		}
		
		if (params.containsKey("datemark") && params.get("datemark") != null){
			Date nowdate = new Date(System.currentTimeMillis());
			params.put("nowdate", nowdate);
			//即将开始的限制时间
			String lastDayCfg = WhConstance.getSysProperty("START_LAST_DAY", "30");
			int lastDay = 30;
			if( lastDayCfg.matches("^\\d+$") ){
				lastDay = Integer.parseInt(lastDayCfg);
			}
			Calendar c = Calendar.getInstance();
			c.setTime(nowdate);
			c.add(Calendar.DAY_OF_YEAR, lastDay);
			Date lastdate = c.getTime();
			params.put("lastdate", lastdate);
		}
		
		int page = 1;
		int size = 9;
		if (params.containsKey("page") && params.get("page")!=null){
			page = Integer.valueOf(params.get("page").toString());
		}
		if (params.containsKey("size") && params.get("size")!=null){
			size = Integer.valueOf(params.get("size").toString());
		}
		PageHelper.startPage(page, size);
		List<Object> list = this.aMapper.selectBrandList(params);
		PageInfo pinfo = new PageInfo(list);

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("total", pinfo.getTotal());
        res.put("rows", pinfo.getList());
        res.put("page", pinfo.getPageNum());
		return res;
	}
	
	/**
	 * 活动首页 加载 活动品牌数据 取6
	 * @return
	 * @throws Exception
	 *//*
	/*public List<Map> loadPP(WebRequest request)throws Exception{
		Map<String, Object> params = ReqParamsUtil.parseRequest(request);
		List<Map> listmap = this.aMapper.loadPP(params);
		System.out.println("listmap.size()====="+listmap.size());
		return listmap;
	}
	*/
	/**
	 * 活动首页 加载 活动品牌数据
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map> ppLoad()throws Exception{
		PageHelper.startPage(1, 6);
		List<Map> list = this.aMapper.selectpptype();
		@SuppressWarnings("rawtypes")
		PageInfo pinfo = new PageInfo(list);
		return pinfo.getList();
	}
	
	/**
	 * 活动首页 加载 活动分类数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> eventLoad()throws Exception{
		PageHelper.startPage(1, 3);
		List<Map> list = this.aMapper.selectactadress();
		@SuppressWarnings("rawtypes")
		PageInfo pinfo = new PageInfo(list);
		return pinfo.getList();
	}
	
	/**
	 * 活动首页 加载资讯 公告
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<WhZxColinfo> eventZxIfno(String clnftype)throws Exception{
		Example example = new Example(WhZxColinfo.class);
		Criteria c = example.createCriteria();
		if(clnftype != null && !"".equals(clnftype)){
			c.andEqualTo("clnftype",clnftype );
		}
		c.andEqualTo("clnfstata",3);
		//按 clnfcrttime 排序 取前三
		example.setOrderByClause("clnfcrttime" + " " + "desc");
		PageHelper.startPage(1, 6);
		List<WhZxColinfo> list = this.colinfoMapper.selectByExample(example);
		@SuppressWarnings("rawtypes")
		PageInfo pinfo = new PageInfo(list);
		return pinfo.getList();
	}
	
	/**
	 * 获取资讯配置
	 * @return
	 * @throws Exception
	 */
	public List<WhZxColinfo> getzxpz()throws Exception{
		Example example = new Example(WhCfgList.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("cfgpagetype","2016102900000006");
		c.andEqualTo("cfgenttype","2016102800000001");
		c.andEqualTo("cfgentclazz","2016111900000018");
		c.andEqualTo("cfgstate",1);
		PageHelper.startPage(1, 3);
		List<WhCfgList> list = this.cfgListMapper.selectByExample(example);
		@SuppressWarnings("rawtypes")
		PageInfo pinfo = new PageInfo(list);
		return pinfo.getList();
	}
	
	/**
	 * dg 文化活动list
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object activityList(int page, int rows,WebRequest request)throws Exception{
		Map<String, Object> params = ReqParamsUtil.parseRequest(request);
		String sdate=(String) params.get("statemdfdate");
		String actvshorttitle = (String)params.get("actvshorttitle");
		String etype = (String)params.get("etype");
		String arttype = (String)params.get("arttype");
		String cultid = (String)params.get("cultid");
		String SYS_ORDER = WhConstance.getSysProperty("SYS_ORDER");
		if(SYS_ORDER != null && "1".equals(SYS_ORDER)){
			params.put("sysorder", "1");
		}
		if(actvshorttitle != null){
			params.put("name", "%"+actvshorttitle+"%");
		}
		if(etype != null){
			params.put("etype", "%"+etype+"%");
		}
		if(arttype != null){
			params.put("arttype", "%"+arttype+"%");
		}
        if (cultid!=null && !cultid.isEmpty()){
            params.put("cultid", cultid);
        }
		//时间 搜索
		if(sdate!=null && !"".equals(sdate)){
			params.put("sdate", sdate);
			Date date=null;
			Date newdate=null;
			GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
			date=cal.getTime();
			params.put("date", date);
			cal.add((GregorianCalendar.MONTH), 1);
			newdate=cal.getTime();
			params.put("newdate", newdate);
		}

        params.put("now", new Date());
		PageHelper.startPage(page, rows);
		@SuppressWarnings("rawtypes")
		List<Map> actlist = this.aMapper.selectlistAct(params);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		PageInfo pinfo = new PageInfo(actlist);
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("total", pinfo.getTotal());
		res.put("actlist", pinfo.getList());
		return res;
	}
	
	/**
	 * dg 活动详情推荐
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<WhActivity> acttjian(String actvid,WebRequest request)throws Exception{
		List list = new ArrayList();
		WhgActActivity act = activityMapper.selectByPrimaryKey(actvid);

		Map<String, Object> params = ReqParamsUtil.parseRequest(request);
		params.put("isrecommend", 1);
		if(!"".equals(act.getEkey()) && act.getEkey() != null){
			String[] a = act.getEkey().split(",");
			if(a != null && a.length >0){
				for(int i = 0; i<a.length; i++){
					list.add(a[i]);
				}
				params.put("ekey", list);
			}else{
				params.put("ekey", act.getEkey());
			}
		}
        params.put("now", new Date());
		PageHelper.startPage(1, 3);
		@SuppressWarnings("rawtypes")
		List<Map> actlist = this.aMapper.selectlistAct(params);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		PageInfo pinfo = new PageInfo(actlist);
		return pinfo.getList();
	}
	
	/**
	 * 文化活动  根据活动id获取活动详情
	 * @param actvid
	 * @return
	 */
	public WhgActActivity getActDetail(String actvid){
		return this.activityMapper.selectByPrimaryKey(actvid);
	}
	
	/**
	 * 根据主键id 查tag 标签 
	 * @param tagid
	 * @return
	 * @throws Exception
	 */
	public WhTag tagList(String tagid)throws Exception{
		return this.whTagMapper.selectByPrimaryKey(tagid);
	}
	
	/**
	 * 根据actvrefid 找 相关场次
	 * @param actvrefid
	 * @return
	 * @throws Exception
	 */
	public List<Map> findActivityitm(String actvrefid)throws Exception{
		Example example = new Example(WhActivityitm.class);
		Criteria c = example.createCriteria();
		if(actvrefid!=null && !"".equals(actvrefid)){
			c.andEqualTo("actvrefid", actvrefid);
			c.andEqualTo("actvitmstate", 1);
		}
		example.setOrderByClause("actvitmsdate" +" "+"asc" );
		List<WhActivityitm> lsititem = this.activityitmMapper.selectByExample(example);
		List<Map> lsitmap =new ArrayList<Map>(); 
 		for (int i = 0; i < lsititem.size(); i++) {
 			Map<Object, Object> map =new HashMap<Object, Object>();
			int maxpeople = lsititem.get(i).getActvitmdpcount();
			
			//已报名人数 
			Example example2 = new Example(WhActivitybm.class);
			Criteria c2 = example2.createCriteria();
		/*	List<String> liststate = new ArrayList<String>();
			liststate.add("2");
			liststate.add("3");*/
			c2.andNotEqualTo("actshstate","3");
			c2.andEqualTo("actbmstate", 1);
			c2.andEqualTo("actvitmid",lsititem.get(i).getActvitmid());
			List<WhActivitybm> listbm = this.bmMapper.selectByExample(example2);
			//已报名 总数
			int sum=0;
			for(int j = 0; j < listbm.size(); j++){
				//获取 每个 用户的报名人数
				sum+=listbm.get(j).getActbmcount();
			}
			//获取 余票数
			int leavecount=maxpeople-sum;
			map.put("object", lsititem.get(i));
			map.put("leavecount", leavecount);
			lsitmap.add(map);
		}
		return lsitmap;
	}
	
	
	/**
	 * 根据活动ID 获取该活动场次信息
	 */
	public List<WhgActTime> getActTimeList(String actId,Date playdate){
		WhgActTime record = new WhgActTime();
		record.setActid(actId);
		if(playdate != null){
			record.setPlaydate(playdate);
		}
		record.setState(1);
		List<WhgActTime> actTimeList = whgActTimeMapper.select(record);
		return actTimeList;
	}
	
	public List getPlayDate4ActId(String actId){
		Map<String,Object> param = new HashMap<>();
		param.put("actId", actId);
		List actTimeList = crtWhhdActTimeMapper.findPlayDate4actId(param);
		return actTimeList;
	}
	
	
	/**
	 * 获取场次信息
	 * @param actvitmid
	 * @return
	 */
	public WhActivityitm selectOneitm(String actvitmid){
		return this.activityitmMapper.selectByPrimaryKey(actvitmid);
	}
	
	/**
	 * 获取场次信息
	 * @return
	 */
	public WhgActTime selectOnePlay(String eventId){
		return this.whgActTimeMapper.selectByPrimaryKey(eventId);
	}
	
	/**
	 * 根据 活动场次id 查找 余票/人数
	 * @param actvitmid
	 * @return
	 */
	public int selectCount(String actvitmid){
		WhActivityitm wActivityitm = this.activityitmMapper.selectByPrimaryKey(actvitmid);
		//报名 最大人数
		int maxpeople = wActivityitm.getActvitmdpcount();
		
		//已报名人数 
		Example example = new Example(WhActivitybm.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("actshstate", "1");
		c.andEqualTo("actbmstate", 1);
		c.andEqualTo("actvitmid",actvitmid);
		List<WhActivitybm> listbm = this.bmMapper.selectByExample(example);
		//已报名 总数
		int sum=0;
		for(int i = 0; i < listbm.size(); i++){
			//获取 每个 用户的报名人数
			sum+=listbm.get(i).getActbmcount();
		}
		//获取 余票数
		int leavecount=maxpeople-sum;
		return leavecount;
	}
	
	/**
	 * 根据活动ID查询相关资源
	 * @param actvid
	 * @return  
	 */
	public List<WhgComResource> selectactSource(String actvid,String enttype,String reftype){
	Example example = new Example(WhgComResource.class);
	Criteria c = example.createCriteria();
	c.andEqualTo("enttype", enttype);
	c.andEqualTo("reftype", reftype);
	c.andEqualTo("refid",actvid);
	example.setOrderByClause("crtdate asc");
	return this.whgComResourceMapper.selectByExample(example);
}
	
	/**
	 *品牌详情页	根据品牌活动id 查详情
	 * @return
	 */
	public WhBrand selectppDetail(String braid){
		return this.brandMapper.selectByPrimaryKey(braid);
	}
	/**
	 * 品牌详情页	根据品牌活动id 查期数
	 * @param braid
	 * @return
	 */
	public List<WhBrandAct> selectppitem(String braid){
		Example example = new Example(WhBrandAct.class);
		example.or().andEqualTo("braid", braid);
		example.setOrderByClause("bracstime"+" "+"asc");
		return this.bamapper.selectByExample(example);
	}
	
	/**
	 * 品牌详情页 往期回顾
	 * @return
	 */
	public List<Map> selectppwqact(WebRequest request){
		Map<String, Object> params = ReqParamsUtil.parseRequest(request);
		params.put("nowdate",new Date() );
		PageHelper.startPage(1, 3);
		List<Map> list = this.aMapper.selectppwqact(params);
		PageInfo pinfo = new PageInfo(list);
		return pinfo.getList();
	}
	/**
	 * 品牌详情页	根据活动id 查活动详情
	 * @return
	 */
	public List<Map> selectactDetail(WebRequest request){
		Map<String, Object> params = ReqParamsUtil.parseRequest(request);
		PageHelper.startPage(1, 1);
		List<Map> lsit = this.aMapper.selectppDetail(params);
		PageInfo pinfo = new PageInfo(lsit);
		return pinfo.getList();
	}
	
	/**
	 * 活动首页大广告
	 * @return
	 * @throws Exception
	 */
	public List<WhCfgAdv> getAdv()throws Exception{
		Example example = new Example(WhCfgAdv.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("cfgadvpagetype","2016122100000006");
		c.andEqualTo("cfgadvstate",1);
		example.setOrderByClause("cfgadvidx"+" "+"desc");
		PageHelper.startPage(1, 1);
		List<WhCfgAdv> list = this.cfgAdvMapper.selectByExample(example);
		@SuppressWarnings("rawtypes")
		PageInfo pinfo = new PageInfo(list);
		return pinfo.getList();
		
	}
	
	/**
	 * 创建订单信息
	 * @param actId
	 * @param actOrder
	 */
	public void addActOrder(String actId,WhgActOrder actOrder){
		actOrder.setActivityid(actId);
		actOrder.setOrderisvalid(1);
		actOrder.setOrdercreatetime(new Date());
		actOrder.setTicketstatus(1);
		actOrder.setPrinttickettimes(0);
		whgActOrderMapper.insert(actOrder);
	}
	
	/**
	 * 更新订单状态
	 * @param actOrder
	 */
	public void upActOrder(WhgActOrder actOrder){
		actOrder.setOrdersmsstate(2);
		actOrder.setOrdersmstime(new Date());
		whgActOrderMapper.updateByPrimaryKey(actOrder);
		
	}

	
	/**
	 * 根据订单ID获取订单对象
	 * @param orderId
	 * @return
	 */
	public Map<String,Object> findOrderInfo4Id(String orderId){
		WhgActOrder whgActOrder = whgActOrderMapper.selectByPrimaryKey(orderId);
		WhgActActivity whgActActivity = activityMapper.selectByPrimaryKey(whgActOrder.getActivityid());
		WhgActTime whgActTime = whgActTimeMapper.selectByPrimaryKey(whgActOrder.getEventid());
		Map<String,Object> param = new HashMap<>();
		param.put("orderId", whgActOrder.getId());
		param.put("address", whgActActivity.getAddress());
		param.put("imgurl", whgActActivity.getImgurl());
		param.put("id", whgActOrder.getId());
		param.put("actId", whgActActivity.getId());
		param.put("telphone", whgActActivity.getTelphone());
		param.put("hasfees", whgActActivity.getHasfees());
		param.put("eventid", whgActOrder.getEventid());
		param.put("orderphoneno", whgActOrder.getOrderphoneno());
		param.put("name", whgActActivity.getName());
		param.put("playdate", whgActTime.getPlaydate());
		param.put("playstime", whgActTime.getPlaystime());
		return param;
	}
	
	/**
	 * 根据活动ID获取座位信息
	 * @param actId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getSeat4ActId(String actId,String eventId,String userId){
//		WhgActSeat whgActSeat = new WhgActSeat();
//		whgActSeat.setActivityid(actId);
//		List<WhgActSeat> seatList = whgActSeatMapper.select(whgActSeat);
        Example example = new Example(WhgActSeat.class);
        example.createCriteria().andEqualTo("activityid", actId);
        example.setOrderByClause("seatrow , seatcolumn");
        List<WhgActSeat> seatList = whgActSeatMapper.selectByExample(example);

		JSONArray mapType = new JSONArray();//座位坐标和类型
		JSONArray statusMap = new JSONArray();//座位坐标和类型
		JSONObject mapObejct = new JSONObject();
		int temp = 0;
		JSONArray mapTypeList = new JSONArray();
		JSONArray statusList = new JSONArray();
		Map<String,Object> map = new HashMap<>();
		map.put("eventId", eventId);
		WhgActActivity whgActActivity = activityMapper.selectByPrimaryKey(actId);
		if(whgActActivity.getSellticket() == 3){
			map.put("sellticket", whgActActivity.getSellticket());
		}
		List<Map> orderList = crtWhhdMapper.findSeat4EventId(map);
		for(WhgActSeat item:seatList){
			int rowNum = item.getSeatrow();
			String seatnum = item.getSeatnum();
			int status = item.getSeatstatus();
			String seatId = item.getId();//座位ID
			for(int i=0;i<orderList.size();i++){
				Map<String,Object> orderMap = orderList.get(i);
				String seatId_ = orderMap.get("seatid").toString();
				if(seatId.equals(seatId_)){
					status = 2;
				}
			}
			if(rowNum > temp  ){
				if(null != mapTypeList){
					mapType.add(mapTypeList);
				}
				if(null != statusList){
					statusMap.add(statusList);
				}
				mapTypeList = new JSONArray();
				statusList = new JSONArray();
				mapTypeList.add(seatnum+"-"+status);
				statusList.add(status);
				temp = rowNum;
			}else{
				mapTypeList.add(seatnum+"-"+status);
				statusList.add(status);
				
			}			
		}
		mapType.add(mapTypeList);
		statusMap.add(statusList);
		mapObejct.put("seatSize", orderList.size());
		//当前用户在该场次订票张数
		map.put("userId", userId);
		orderList = crtWhhdMapper.findSeat4EventId(map);
		mapObejct.put("seatSizeUser", orderList.size()); //当前用户订票数
		mapObejct.put("mapType", mapType);
		mapObejct.put("statusMap", statusMap);
		return mapObejct;
	}
	
	/**
	 * 查询座位信息
	 * @param actId
	 * @return
	 */
	public WhgActSeat getWhgActTicket4ActId(String actId,String seatNum){
		WhgActSeat whgActSeat = new WhgActSeat();
		whgActSeat.setActivityid(actId);
		whgActSeat.setSeatnum(seatNum);
		List<WhgActSeat> list = whgActSeatMapper.select(whgActSeat);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else {
			return null;
		}

	}
	
	/**
	 * 保存座位订单信息
	 * @param seatId
	 * @param orderId
	 */
	public void saveSeatOrder(String seatId,String orderId,String seatCode){
		try {
			WhgActTicket whgActTicket = new WhgActTicket();
			whgActTicket.setId(this.commservice.getKey("WhgActOrder"));
			whgActTicket.setOrderid(orderId);
			whgActTicket.setPrinttime(new Date());
			whgActTicket.setSeatid(seatId);
			whgActTicket.setTicketstatus(0);
			whgActTicket.setSeatcode(seatCode);
			whgActTicketMapper.insert(whgActTicket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据订单ID查看该订单下的票数
	 * @param orderId
	 * @return
	 */
	public int getTicketList4OrderId(String orderId){
		WhgActTicket whgActTicket =new WhgActTicket();
		whgActTicket.setOrderid(orderId);
		List<WhgActTicket> ticketList = whgActTicketMapper.select(whgActTicket);
		return ticketList.size();
	}
	
	public String checkApplyAct(String actId,String userId){
		WhgActActivity actModel = activityMapper.selectByPrimaryKey(actId);
		
		//活动报名已结束
        if(actModel.getEnterendtime().before(Calendar.getInstance().getTime())){
            return "101";
        }
        //实名制验证
        if(actModel.getIsrealname()!=null && actModel.getIsrealname().compareTo(new Integer(1))==0){
        	/*Map<String,Object> param = new HashMap<>();
        	param.put("userId", userId);
        	Map<String,Object> map = crtWeiXinMapper.findUserInfo4UserId(param);
            if(map == null || map.get("isrealname") == null || Integer.parseInt((String) map.get("isrealname")) != 1){
                return "104";
            }*/
            WhUser_old user = this.whUserMapper.selectByPrimaryKey(userId);
            if (user==null || user.getIsrealname()==null || user.getIsrealname().compareTo(new Integer(1))!=0){
                return "104";
            }
        }
		return SUCCESS;
	}
	
	/**
	 * 查询用户已经取消的订单
	 * @param actId
	 * @param userId
	 * @return
	 */
	public List<WhgActOrder> findOrderList4Id(String actId,String userId){
		WhgActOrder actOrder = new WhgActOrder();
		if(actId != null){
			actOrder.setActivityid(actId);
		}
		actOrder.setUserid(userId);
		actOrder.setTicketstatus(3);
		actOrder.setOrderisvalid(2);
		return  whgActOrderMapper.select(actOrder);
	}
	
	/**
	 * 创建黑名单
	 * @param usrBack
	 */
	public void addUserBack(WhgUsrBacklist usrBack){
		whgUsrBacklistMapper.insert(usrBack);
	}
	
	/**
	 * 查询该用户是否已加入黑名单
	 */
	public List<WhgUsrBacklist> findWhgUsrBack4UserId(WhgUsrBacklist usrBack){
		return whgUsrBacklistMapper.select(usrBack);
	}

	/**
	 * 查询活动上传
	 * @param actvid
     * @return
     */
	public List serch(String actvid)throws Exception {
		WhgActActivity ac = activityMapper.selectByPrimaryKey(actvid);
		List list = new ArrayList();
		if(!"".equals(ac) && ac != null){
			if (!"".equals(ac.getFilepath()) && ac.getFilepath() != null) {
				String[] arr = ac.getFilepath().split(",");
				list = java.util.Arrays.asList(arr);
			}
		}

		return list;
	}

	/**
	 * 查询用户是否在黑名单内
	 * @param id
	 * @return
     */
	public int selBlackCount(String id) {
		Example example = new Example(WhgUsrBacklist.class);
		example.createCriteria().andEqualTo("userid",id).andEqualTo("state",1);
		return this.whgUsrBacklistMapper.selectCountByExample(example);
	}

	/**
	 * 根据主键查询用户
	 * @param uid
	 * @return
     */
	public WhUser_old selUser(String uid) {
		return whUserMapper.selectByPrimaryKey(uid);
	}


    /**
     * 活动预订验证
     * @param actId 活动ID
     * @param userId  用户ID
     * @param actOrder 订票信息
     * @param seatStr 预订座位信息
     * @param seats
     * @return {"code": "0", "errmsg":""} 表示验证成功， 其它验证失败
     * @throws Exception
     */
    public Map<String, String> hdCheck(String actId, String userId, WhgActOrder actOrder,String seatStr,int seats)throws Exception{
        Map<String, String> rtnMap = new HashMap<String, String>();
        String code = "0";
        String errmsg = "";

        try{
            //valid falg
            boolean is = true;


            //actId不能为空
            if(actId == null || "".equals(actId)){
                code = "100";
                errmsg = "actId不能为空！";
                is = false;
            }

            //活动场次Id不允许为空
            if(is && (actOrder == null || actOrder.getEventid() == null || "".equals(actOrder.getEventid()))){
                code = "101";
                errmsg = "活动场次Id不允许为空！";
                is = false;
            }

            //用户Id不允许为空
            if(is && (userId == null || "".equals(userId))){
                code = "102";
                errmsg = "用户Id不允许为空！";
                is = false;
            }

            //座位数必须大于0
            if(is && ((seatStr == null || "".equals(seatStr)) && seats <1) ){
                code = "103";
                errmsg = "座位数必须大于0！";
                is = false;
            }

            //该活动已下架
            WhgActActivity whgAct = this.getActDetail(actId);
            if(is && (whgAct == null || whgAct.getState().intValue() != 6)){
                code = "106";
                errmsg = "该活动已下架！";
                is = false;
            }
			//活动已过时
			if(is && actOrder.getEventid() != null && !"".equals(actOrder.getEventid())){
				WhgActTime actTime = this.whgActTimeMapper.selectByPrimaryKey(actOrder.getEventid());
				Date startTime = actTime.getPlaystarttime();
				Date now = new Date();
				if(startTime.getTime() < now.getTime()){
					code = "120";
					errmsg = "该活动已开始！";
					is = false;
				}
			}



            //活动报名时间已过
            if (is){
                Date enterendtime = whgAct.getEnterendtime();
                if (enterendtime!=null){
                    Date now = new Date();
                    if (now.compareTo(enterendtime)>0){
                        code = "113";
                        errmsg = "报名时间已结束！";
                        is = false;
                    }
                }
            }

            //超过允许预订票数 验证 :  已预定数 + 当前 > 允许预订限制数
            int curtTicketNum = 0;//当前预订票数
            if(is){
                boolean validNum = true;
                Integer ticketNum = whgAct.getSeats(); //whgAct.getTicketnum();//可预定数
                int ticketNum_old = this.getTicketNums_yd(userId, actOrder.getEventid()); //已预定数

                if(ticketNum != null) {
                    if (seats > 0) {
                        curtTicketNum = seats;
                        if ((seats+ticketNum_old) > ticketNum.intValue()){
                            validNum = false;
                        }
                    } else if (seatStr != null && !"".equals(seatStr)) {
                        String[] arr = seatStr.split(",");
                        curtTicketNum = arr.length;
                        if((arr.length +ticketNum_old) > ticketNum.intValue() ){
                            validNum = false;
                        }
                    }
                }

                if(!validNum){
                    code = "109";
                    errmsg = "超过允许预订票数！";
                    is = false;
                }
            }

            //实名认证
            if(is && (whgAct.getIsrealname() != null && whgAct.getIsrealname().intValue()== 1) ){
                WhUser_old userlist = this.selUser(userId);
                if( userlist == null || userlist.getIsrealname() == null || userlist.getIsrealname().intValue() != 1  ){
                    code = "107";
                    errmsg = "该活动需实名认证后才可报名，请先进行实名认证！";
                    is = false;
                }
            }

            //验证黑名单
            if(is){
                //查询当前活动下，该用户取消次数
                List<WhgActOrder> actOrderList = this.findOrderList4Id(actId, userId);
                //查询该用户取消的所有活动次数
                List<WhgActOrder> orderList = this.findOrderList4Id(null, userId);
                //如果已经加入了黑名单则不加入
                int count = this.selBlackCount(userId);
                if(count == 0){
                    //如果用户一个活动取消两次或者一个用户统计取消活动订单超过10次则加入黑名单
                    if(actOrderList.size()>=2 || orderList.size() >= 10 ){
                        //user info
                        WhUser_old whUser = whUserMapper.selectByPrimaryKey(userId);
                        if(whUser != null && whUser.getId() != null){
                            WhgUsrBacklist userBack = new WhgUsrBacklist();
                            userBack.setUserid(userId);
                            userBack.setState(1);
                            List<WhgUsrBacklist> userBackList = this.findWhgUsrBack4UserId(userBack);
                            if(userBackList.size() < 1){
                                userBack.setId(this.commservice.getKey("WhgUsrBacklist"));
                                userBack.setState(1);
                                userBack.setJointime(new Date());
                                userBack.setType(1);
                                userBack.setUserid(userId);
                                userBack.setUserphone(whUser.getPhone());
                                this.addUserBack(userBack);
                            }
                            code = "108";
                            errmsg = "您已经被系统限制执行操作，如需了解详细情况，请联系管理员！";
                            is = false;
                        }
                    }
                }else{
					code = "108";
					errmsg = "您已经被系统限制执行操作，如需了解详细情况，请联系管理员！";
					is = false;
				}

            }

            //验证总票数
            Integer sellticket = whgAct.getSellticket();
            if(is){

                int _total = 0;//总票数
                int _ticketNum_yg = this.getTicketNums_yd(null, actOrder.getEventid());//所有用户本场次已订票数


                //自由入座-总票数
                if(sellticket != null && 2 == sellticket.intValue()){
                    _total  = whgAct.getTicketnum();
                }

                //在线选票-总票数
                if(sellticket != null && 3 == sellticket.intValue()){
                    Example example = new Example(WhgActSeat.class);
                    example.createCriteria().andEqualTo("activityid", whgAct.getId())
                    .andEqualTo("seatstatus", "1");
                    _total = this.whgActSeatMapper.selectCountByExample(example);
                }

                if(_ticketNum_yg + curtTicketNum > _total){
                    code = "110";
                    errmsg = "您预订票数已经超出允许最大预订票数！";
                    is = false;
                }
            }

            String selectSeat[] = null;
            if(is && (seatStr != null && !"".equals(seatStr))){
                selectSeat = seatStr.split(",");
                for (int i = 0; i < selectSeat.length; i++) {
                    WhgActSeat whgActSeat =this.getWhgActTicket4ActId(actId, selectSeat[i]);
                    Map map_ = this.getTicketList4SeatId(whgActSeat.getId(),actOrder.getEventid());
                    if(map_ !=null){
                        code = "111";
                        errmsg = "该座位已被预定！";
                        is = false;
                        break;
                    }
                }
            }

            if (is && selectSeat!=null){
                //在线选票-是否有选不开放座位
                if(sellticket != null && 3 == sellticket.intValue()) {
                    Example example = new Example(WhgActSeat.class);
                    example.createCriteria()
                            .andEqualTo("activityid", whgAct.getId())
                            .andEqualTo("seatstatus", "2")
                            .andIn("seatnum", Arrays.asList(selectSeat));

                    int notSeatCount = this.whgActSeatMapper.selectCountByExample(example);
                    if (notSeatCount > 0){
                        code = "112";
                        errmsg = "选中的座位中有不可被预订的座位！";
                        is = false;
                    }
                }
            }

        }catch(Exception e){
            throw e;
        }

        rtnMap.put("code", code);
        rtnMap.put("errmsg", errmsg);
        return rtnMap;
    }

    /**
     * 根据座位ID验证该座位是否已经预定
     * @param eventId
     * @return
     */
    public Map getTicketList4SeatId(String seatId,String eventId){
        Map map = crtTicketMapper.queryTicket4Id(seatId, eventId);

        return map;
    }

    /**
     * 根据用户ID，场次ID获取用户订单座位数
     * @param userId 用户ID
     * @param eventId 场次ID
     * @return
     * @throws Exception
     */
    private int getTicketNums_yd(String userId, String eventId)throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("eventId", eventId);
        if(userId != null && !userId.isEmpty()) {
            map.put("userId", userId);
        }
        List<Map> orderList = this.crtWhhdMapper.findSeat4EventId(map);
        if(orderList != null ){
            return orderList.size();
        }
        return 0;
    }

    /**
     * 预订活动
     * @param orderId 新生成的订单ID
     * @param actId 活动ID
     * @param actOrder 订单信息
     * @param userId 用户ID
     * @param seatStr 在线选座信息
     * @param seats 自由入座票数
     * @throws Exception
     */
    public void hdOrder(String orderId, String actId, WhgActOrder actOrder, String userId, String seatStr, int seats)throws Exception{
        //验证通过，报名活动
        String id = orderId;
        actOrder.setId(id);
        actOrder.setOrdernumber(this.commservice.getOrderId(EnumOrderType.ORDER_ACT.getValue()));
        actOrder.setUserid(userId);
        this.addActOrder(actId, actOrder);
        WhgActTime actTime = this.selectOnePlay(actOrder.getEventid());
        int seatNum = seats; //自由入座位置数
        int totalSeat = seatNum;//selectSeat.length;
        String[] selectSeat = null;
        if(seatNum > 0){
            totalSeat = seatNum;
        }else{
            String mySelectSeat = seatStr; //在线选座位置数
            selectSeat = mySelectSeat.split(",");
            totalSeat = selectSeat.length;
        }
        int sum_ = 1;
        for(int i=0;i<totalSeat;i++){
            if(seatNum > 0){//自由入座
                this.saveSeatOrder("P"+sum_, id,"票"+sum_);
            }else{ //在线选座
                WhgActSeat whgActSeat = this.getWhgActTicket4ActId(actId, selectSeat[i]);
                this.saveSeatOrder(whgActSeat.getId(), id,whgActSeat.getSeatnum());
            }
            sum_++;
        }

        //发送短信
        Map<String, String> smsData = new HashMap<String, String>();
        smsData.put("userName", actOrder.getOrdername());
        WhgActActivity whgActActivity = this.getActDetail(actId);
        smsData.put("activityName", whgActActivity.getName());
        smsData.put("ticketCode", actOrder.getOrdernumber());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = actTime.getPlaydate();
        String dateStr = sdf.format(date);
        smsData.put("beginTime", dateStr +" "+ actTime.getPlaystime());
        int num = 0;
        if(totalSeat > 0){
            num = totalSeat;
        }else{
            num = seatNum;
        }
        smsData.put("number", String.valueOf(num));
        smsService.t_sendSMS(actOrder.getOrderphoneno(), "ACT_DUE", smsData, actId);
        //短信发送成功后更改订单短信状态
        this.upActOrder(actOrder);
    }

    public synchronized Map<String, String> synchdOrder(String actId, String userId, WhgActOrder actOrder,String seatStr,int seats) throws Exception{
        Map<String, String> rtnMap = new HashMap<String, String>();

        rtnMap = this.hdCheck(actId, userId, actOrder, seatStr, seats);
        if(!"0".equals(rtnMap.get("code"))){
            return rtnMap;
        }

        String id = this.commservice.getKey("WhgActOrder");
        this.hdOrder(id, actId, actOrder, userId, seatStr, seats);

        rtnMap.put("id", id);
        rtnMap.put("code", "0");
        rtnMap.put("errmsg", "");
        return rtnMap;
    }
}
