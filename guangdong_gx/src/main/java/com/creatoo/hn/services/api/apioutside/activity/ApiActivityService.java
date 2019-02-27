package com.creatoo.hn.services.api.apioutside.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.mapper.api.ApiActivityOrderMapper;
import com.creatoo.hn.dao.mapper.api.ApiActivityTicketMapper;
import com.creatoo.hn.dao.mapper.api.ApiActivityTimeMapper;
import com.creatoo.hn.dao.mapper.api.ApiWhgCultMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.user.WhgBlackListService;
import com.creatoo.hn.services.api.apiinside.InsMessageService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumOrderType;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * 文化活动
 * @author yanjianbo
 * @version 2016.11.16
 */

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgActivity", keyGenerator = "simpleKeyGenerator")
public class ApiActivityService extends BaseService {

/*
	*/
/**
	 * 短信公开服务类
	 */

	@Autowired
	private SMSService smsService;

	@Autowired
	private WhgGatherService whgGatherService;

     /**
	 * 活动
	 */

	@Autowired
	private CrtWhgActivityMapper crtActivityMapper;


	@Autowired
	private WhgActivityMapper activityMapper;
/**
	 * 活动场次
	 */
	@Autowired
	private ApiActivityTimeMapper apiActTimeMapper;

	@Autowired
	private WhgActivityTimeMapper whgActTimeMapper;

	@Autowired
	private InsMessageService insMessageService;

	@Autowired
	private WhgSystemCultService whgSystemCultService;

	@Autowired
	private WhgActivitySeatMapper whgActSeatMapper;

    @Autowired
    private WhgUserMapper whgUserMapper;

/**
	 * 座位订单mapper
	 */

	@Autowired
	private ApiActivityTicketMapper apiActTicketMapper;

	@Autowired
	private WhgActivityTicketMapper whgActTicketMapper;

	@Autowired
	private ApiActivityOrderMapper apiActOrderMapper;

	@Autowired
	private WhgActivityOrderMapper whgActOrderMapper;
	@Autowired
	private WhgBlackListService whgBlackListService;

	@Autowired
	private ApiActivityTicketMapper ticketMapper;

	@Autowired
	private WhgYwiTagMapper whgYwiTagMapper;

	@Autowired
	private WhgYwiTypeMapper whgYwiTypeMapper;

	private final  String SUCCESS="0";//成功



public List createStatusMap(List<WhgActivitySeat> whgActSeatList){
	List statusMap = new ArrayList();
	Map map = new HashMap();
	for(WhgActivitySeat whgActSeat : whgActSeatList){
		Integer row = whgActSeat.getSeatrow();
		Integer column = whgActSeat.getSeatcolumn();
		if(map.containsKey(row)){
			List list = (List) map.get(row);
			list.add(whgActSeat.getSeatstatus());
			map.put(row,list);
		}else {
			List list = new ArrayList();
			list.add(whgActSeat.getSeatstatus());
			map.put(row,list);
		}
	}
	Iterator iter = map.keySet().iterator();
	while(iter.hasNext()){
		Integer key = (Integer)iter.next();
		statusMap.add(key,map.get(key));
	}
	return statusMap;
}

	/**
	 * 查询座位信息
	 * @param actId
	 * @param seatNum
	 * @return
	 */
	public WhgActivitySeat getWhgActTicket4ActId(String actId,String seatNum){
		WhgActivitySeat whgActSeat = new WhgActivitySeat();
		whgActSeat.setActivityid(actId);
		whgActSeat.setSeatnum(seatNum);
		whgActSeat = whgActSeatMapper.selectOne(whgActSeat);
		return whgActSeat;
	}

	/**
	 * 活动总票数
	 *
	 * @param actId
	 * @param seatNum
	 * @return
	 */
	public int getActTicketList(String actId) {
		WhgActivityTime whgActTime = new WhgActivityTime();
		whgActTime.setActid(actId);
		whgActTime.setState(1);
		List<WhgActivityTime> list = whgActTimeMapper.select(whgActTime);
		int count = 0;
		for (WhgActivityTime activityTime : list) {
			count += activityTime.getSeats();
		}
		return count;
	}

	/**
	 * 查询座位信息
	 * @param actId
	 * @param seatNum
	 * @return
	 */
	public int getWhgActTicketCountsActId(String actId){
		Map<String,Object> map=apiActTicketMapper.queryTicketBookCount(actId);
	    String count=map.get("ticketCounts").toString();
		return Integer.parseInt(count);
	}




	public WhgActivityTime getActTime(String eventId){
		try {
			WhgActivityTime whgActTime = new WhgActivityTime();
			whgActTime.setId(eventId);
			whgActTime.setState(1);
			return whgActTimeMapper.selectOne(whgActTime);
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}

	// 查询 该活动 该场次下 活动票务有效数据
	public List<WhgActivityTicket> getWhgActTicket(String actId, String eventId){
		try {
			Example example1 = new Example(WhgActivityOrder.class);
			Example.Criteria criteria1 = example1.createCriteria();
			criteria1.andEqualTo("activityid",actId);
			criteria1.andEqualTo("eventid",eventId);
			criteria1.andIn("ticketstatus",Arrays.asList(1,2));
			List<WhgActivityOrder> whgActOrderList = whgActOrderMapper.selectByExample(example1);
			if(null == whgActOrderList || whgActOrderList.isEmpty()){
				return new ArrayList<WhgActivityTicket>();
			}
			List<String> orderIdList = getIdList(whgActOrderList);
			Example example2 = new Example(WhgActivityTicket.class);
			Example.Criteria criteria2 = example2.createCriteria();
			criteria2.andIn("orderid",orderIdList);
			List<WhgActivityTicket> whgActTicketList = whgActTicketMapper.selectByExample(example2);
			return whgActTicketList;
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}

	public List<WhgActivityTicket> getUserTicket(String userId,String actId,String eventId){
		try {
			Example example1 = new Example(WhgActivityOrder.class);
			Example.Criteria criteria1 = example1.createCriteria();
			criteria1.andEqualTo("activityid",actId)
					.andEqualTo("userid",userId)
					.andEqualTo("eventid",eventId).andIn("ticketstatus",Arrays.asList(1,2));
			List<WhgActivityOrder> whgActOrderList = whgActOrderMapper.selectByExample(example1);
			Example example2 = new Example(WhgActivityTicket.class);
			Example.Criteria criteria2 = example2.createCriteria();
			criteria2.andIn("orderid",getIdList(whgActOrderList));
			return whgActTicketMapper.selectByExample(example2);
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}


	private List<String> getIdList(List<WhgActivityOrder> whgActOrderList){
		List<String> stringList = new ArrayList<String>();
		for(WhgActivityOrder whgActOrder : whgActOrderList){
			stringList.add(whgActOrder.getId());
		}
		return stringList;
	}
	public int[] getSeatSize(List mapType){
		int count1 = 0;
		int count2 = 0;
		for(Object object : mapType){
			List subList = (List)object;
			for(Object item : subList){
				String str = (String)item;
				if (!str.endsWith("3") && !str.endsWith("4")) {//所有座位数 未占用+已占用
					++count2;
				}
				if(str.endsWith("2")){//已订票
					++count1;
				}
			}
		}
		return new int[]{count1,count2};
	}

	public List getMapType(List statusMap,List<WhgActivitySeat> whgActSeatList,List<WhgActivityTicket> whgActTicketList){
		List mapType = new ArrayList();
		for(int i = 0;i < statusMap.size();i++){
			List statusMapElement = (List)statusMap.get(i);
			mapType.add(creatMapTypeList(i,statusMapElement,whgActSeatList,whgActTicketList));
		}
		return mapType;
	}

	private WhgActivitySeat findSeat(WhgActivityTicket whgActTicket,List<WhgActivitySeat> whgActSeatList){
		for(WhgActivitySeat whgActSeat :whgActSeatList){
			if(whgActTicket.getSeatid().equals(whgActSeat.getId())){
				return whgActSeat;
			}
		}
		return null;
	}

	private boolean seatHasReserve(Integer row,Integer column,List<WhgActivitySeat> whgActSeatList,List<WhgActivityTicket> whgActTicketList){
		for(WhgActivityTicket whgActTicket : whgActTicketList){
			WhgActivitySeat whgActSeat = findSeat(whgActTicket, whgActSeatList);//该票信息 对应的座位信息
			if(null == whgActSeat){
				continue;
			}
			if(row == whgActSeat.getSeatrow() && column == whgActSeat.getSeatcolumn()){
				return true;
			}
		}
		return false;
	}

	private List creatMapTypeList(Integer row,List statusMapElement,List<WhgActivitySeat> whgActSeatList,List<WhgActivityTicket> whgActTicketList){
		List<String> res = new ArrayList<String>();
		for(int i = 0;i < statusMapElement.size();i++){
			Integer status = (Integer) statusMapElement.get(i);
			String item = String.valueOf(row+1) + "排" + String.valueOf(i+1) + "座-";
			if(1 == status){
				if (seatHasReserve(row, i, whgActSeatList, whgActTicketList)) {//判断座位是否被占用
					item += "2";// 已被占用
				}else {
					item += "1";// 未占用
				}
			}else if(2 == status) {
				item += "4";  // 座位 废弃
			}else {
				item += String.valueOf(status);
			}
			res.add(item);
		}
		return res;
	}


	/**
	 * 根据活动获取座位模板
	 * @param actId
	 * @return
	 */
	public List<WhgActivitySeat> getWhgActSeatListByActId(String actId){
		try {
			Example example = new Example(WhgActivitySeat.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("activityid",actId);
			example.setOrderByClause("seatrow,seatcolumn");
			return whgActSeatMapper.selectByExample(example);
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}
/**
	 * dg 活动详情推荐
	 * @param actId
	 * @return
	 * @throws Exception taizhou
	 */

	public List<WhgActivity> acttjianfortz(String actId,List cultid)throws Exception{
		WhgActivity activity = new WhgActivity();
		//activity.setIsrecommend(1);
		activity.setState(6);
		activity.setDelstate(0);
		Example example = new Example(WhgActivity.class);
		Example.Criteria c = example.createCriteria().andEqualTo(activity);

		if (cultid != null && cultid.size() > 0) {
			c.andIn("cultid", cultid);
        }
		if (actId != null) {
			c.andNotEqualTo("id", actId);
		}
		example.setOrderByClause("isrecommend desc,statemdfdate desc");
		PageHelper.startPage(1, 10);
		List<WhgActivity> list = activityMapper.selectByExample(example);
		PageInfo<WhgActivity> pageInfo = new PageInfo<WhgActivity>(list);
		return pageInfo.getList();
	}


	/**
	 * 黑名单定时调用处理--场馆活动室
	 */
	public void jobBlackCall4Act(String actType) {
		try {
			int checkNumber = 3;
			StringBuffer sb = new StringBuffer();
			StringBuffer sb_zc = new StringBuffer();
			List<Map> list = this.crtActivityMapper.selectOrder4BlackListCheck(checkNumber, new Date(), actType);
			if (list == null || list.size() == 0) {

			} else {
				for (Map ent : list) {
					try {
						String userid = (String) ent.get("userid");
						String phone = (String) ent.get("phone");
						if (actType != null && actType.equals("ZC")) {
							sb_zc.append(userid);
							sb_zc.append(",");
							this.whgBlackListService.addBlackList(userid, phone, checkNumber + "次以上没有进行众筹预订验票");
						} else {
							sb.append(userid);
							sb.append(",");
							this.whgBlackListService.addBlackList(userid, phone, checkNumber + "次以上没有进行活动预订验票");
						}
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						continue;
					}
				}
			}

			List<Map> list2 = this.crtActivityMapper.selectOrder5BlackListCheck(checkNumber, actType);
			if (list2 == null || list2.size() == 0) {
				return;
			}
			String str_sb = sb.toString();
			String str_sb_zc = sb_zc.toString();
			for (Map ent : list2) {
				try {
					String userid = (String) ent.get("userid");
					String phone = (String) ent.get("phone");
					if (actType != null && actType.equals("ZC")) {
						if (!str_sb_zc.equals("") && str_sb_zc.indexOf(userid) != -1) {// 与前面加入黑名单重复
						} else {
							this.whgBlackListService.addBlackList(userid, phone, checkNumber + "次以上取消众筹活动预订");
						}
					} else {
						if (!str_sb.equals("") && str_sb.indexOf(userid) != -1) {// 与前面加入黑名单重复
						} else {
							this.whgBlackListService.addBlackList(userid, phone, checkNumber + "次以上取消活动预订");
						}
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					continue;
				}
			}

		} catch (Exception e) {
			log.error("jobBlackCall4Ven error", e);
		}
	}


	/**
	 * dg 活动详情推荐
	 * @param actId
	 * @return
	 * @throws Exception taizhou
	 */
	public int getActTimeCount(String actId){
		WhgActivityTime activity = new WhgActivityTime();
		activity.setActid(actId);
		activity.setState(1);
		return whgActTimeMapper.select(activity).size();
	}

/**
	 * 文化活动  根据活动id获取活动详情
	 * @param actvid
	 * @return
	 */

public WhgActivity getActDetail(String actvid) {
		WhgActivity whgActivity=this.activityMapper.selectByPrimaryKey(actvid);
		if(whgActivity.getEtag()!=null){
			StringBuffer sb=new StringBuffer();
			String[] idArr=whgActivity.getEtag().split(",");
			Example example = new Example(WhgYwiTag.class);
			Example.Criteria c = example.createCriteria();
			c.andIn("id",Arrays.asList(idArr));
			List<WhgYwiTag> whgYwiTags= whgYwiTagMapper.selectByExample(example);
			for(WhgYwiTag tag:whgYwiTags){
				if(!sb.toString().isEmpty()){
					sb.append(",");
				}
				sb.append(tag.getName());
			}
			whgActivity.setEtag(sb.toString());
		}

		if(whgActivity.getEtype()!=null){
			StringBuffer sb=new StringBuffer();
			String[] idArr=whgActivity.getEtype().split(",");
			Example example = new Example(WhgYwiType.class);
			Example.Criteria c = example.createCriteria();
			c.andIn("id",Arrays.asList(idArr));
			List<WhgYwiType> whgYwiTypes= whgYwiTypeMapper.selectByExample(example);
			for(WhgYwiType type:whgYwiTypes){
				if(!sb.toString().isEmpty()){
					sb.append(",");
				}
				sb.append(type.getName());
			}
			whgActivity.setEtype(sb.toString());
		}

		 return whgActivity;
	}

/**
	 * 根据actvrefid 找 相关场次
	 * @param actvrefid
	 * @return
	 * @throws Exception
	 *//*

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
		*/
/*	List<String> liststate = new ArrayList<String>();
			liststate.add("2");
			liststate.add("3");*//*

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


	*/
/**
	 * 根据活动ID 获取该活动场次信息
	 *//*

	public List<WhgActivityTime> getActTimeList(String actId,Date playdate){
		WhgActivityTime record = new WhgActivityTime();
		record.setActid(actId);
		record.setState(1);
		if(playdate != null){
			record.setPlaydate(playdate);
		}
		List<WhgActivityTime> actTimeList = whgActTimeMapper.select(record);
		return actTimeList;
	}

	public List<WhgActivityTime> getPlayDate4ActId(String actId){
		Map<String,Object> param = new HashMap<>();
		param.put("actId", actId);
		List<WhgActivityTime> actTimeList = crtWhhdActTimeMapper.findPlayDate4actId(param);
		return actTimeList;
	}

	*/
/**
	 *获取活动日期
	 * @param actId
	 * @return
	 */
	public List<String> getActDate(String actId){
		try {
			return apiActTimeMapper.getActDate(actId);
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}
	public List<Map> getActTimes(String actId,String date){
		try {
			return apiActTimeMapper.getActTimes(actId,date);
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}

/**
	 * 获取场次信息
	 * @param eventId
	 * @return
 */
@Cacheable
	public WhgActivityTime selectOnePlay(String eventId){
		return this.whgActTimeMapper.selectByPrimaryKey(eventId);
	}

/**
	 * 根据 活动场次id 查找 余票/人数
	 * @param actvitmid
	 * @return


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
 */

/**
	 * 创建订单信息
	 * @param actId
	 * @param actOrder
	 */
@CacheEvict(allEntries = true)
	public void addActOrder(String actId,WhgActivityOrder actOrder){
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
@CacheEvict(allEntries = true)
	public void upActOrder(WhgActivityOrder actOrder){
		whgActOrderMapper.updateByPrimaryKey(actOrder);

	}


/**
	 * 根据订单ID获取订单对象
	 * @param orderId
	 * @return
	 */
@Cacheable
	public Map<String,Object> findOrderInfo4Id(String orderId){
		WhgActivityOrder whgActOrder = whgActOrderMapper.selectByPrimaryKey(orderId);
		WhgActivity whgActActivity = activityMapper.selectByPrimaryKey(whgActOrder.getActivityid());
		WhgActivityTime whgActTime = whgActTimeMapper.selectByPrimaryKey(whgActOrder.getEventid());
		Map<String,Object> param = new HashMap<>();
		param.put("orderId", whgActOrder.getId());
		param.put("address", whgActActivity.getAddress());
		param.put("imgurl", whgActActivity.getImgurl());
		param.put("id", whgActOrder.getId());
		param.put("actId", whgActActivity.getId());
		param.put("eventid", whgActOrder.getEventid());
		param.put("orderphoneno", whgActOrder.getOrderphoneno());
		param.put("name", whgActActivity.getName());
		param.put("playdate", whgActTime.getPlaydate());
		param.put("playstime", whgActTime.getPlaystime());
		return param;
	}

/**
	 * 根据Id查询订单详情
	 * @param orderId
	 * @return
	 */

	public WhgActivityOrder findOrderDetail(String orderId){
		return whgActOrderMapper.selectByPrimaryKey(orderId);
	}



	/**
	 *获取用户订单
	 * @param page
	 * @param rows
	 * @param type
	 * @param userId
	 * @return
	 */
	public PageInfo getOrderForCenter(int page,int rows,int type,String userId){
		PageHelper.startPage(page,rows);
		List<Map> list = null;
		if(1 == type){
			list = apiActOrderMapper.getUserActOrderNotTimeOut(userId);
		}else if(2 == type){
			list = apiActOrderMapper.getUserActOrderTimeOut(userId);
		}else{
			list = apiActOrderMapper.getUserActOrderAll(userId);//给个空数组
		}
		int sum = list.size();
		for(int i=0;i<sum;i++){
			Map map = list.get(i);
			WhgActivityTicket whgActTicket = new WhgActivityTicket();
			whgActTicket.setOrderid(map.get("id").toString());
			Example example = new Example(WhgActivityTicket.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("orderid",map.get("id").toString());
			example.setOrderByClause("seatcode asc");
			List<WhgActivityTicket> ticketList = whgActTicketMapper.selectByExample(example);
			String seatCode = "";
			int seatValue = 0; //验票状态
			for(int j= 0 ; j<ticketList.size();j++){
				WhgActivityTicket ticket = ticketList.get(j);
				if(ticket != null){
					/*if(1==ticketList.get(j).getTicketstatus()){
						seatCode += ticketList.get(j).getSeatcode()+"(已验票),";
					}*/
					if(0==ticketList.get(j).getTicketstatus()){
						seatCode += ticketList.get(j).getSeatcode()+"(未验票),";
					}else{
						seatCode += ticketList.get(j).getSeatcode()+"(已验票),";
						seatValue = 1;
					}

				}
			}

			if(seatCode !=""){
				seatCode = seatCode.substring(0,seatCode.length() - 1);
			}
			map.put("seatCode", seatCode);
			map.put("seatValue", seatValue);
			//验票状态
			if(ticketList.size() > 0){
				map.put("state", ticketList.get(0).getTicketstatus());
			}
			WhgActivity act = activityMapper.selectByPrimaryKey(map.get("activityid").toString());
			if(act != null){
				Date dNow = new Date();   //当前时间
				Date daFfore = new Date();
				Calendar calendar = Calendar.getInstance(); //得到日历
				calendar.setTime(dNow);//把当前时间赋给日历
				calendar.add(Calendar.DAY_OF_MONTH, +2);  //设置为后两天
				daFfore = calendar.getTime();   //得到前两天的时间
				map.put("daFfore",daFfore);
				map.put("strTime", act.getStarttime());
			}
		}
		return new PageInfo(list);
	}



	public List getTimesAct(List<String> dateList,String actId) throws ParseException {
		List myTimeList = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(null != dateList && !dateList.isEmpty()){
			for(String date : dateList){
				Map item = new HashMap();
				item.put("text",date);
				List<Map> timeList = this.getActTimes(actId,date);
				List<Map> children = new ArrayList<Map>();

				if(null != timeList && !timeList.isEmpty()){
					for(Map map : timeList){
						Date playStartTime = sdf.parse(String.valueOf(map.get("playstarttime")));
						Date nowDate = new Date();
						if(nowDate.getTime() < playStartTime.getTime()){
							String text = (String) map.get("playstime") + "-" + (String)map.get("playetime");
							String value = (String)map.get("id");
							Map child = new HashMap();
							child.put("text",text);
							child.put("value",value);
							children.add(child);
						}
					}
					item.put("children",children);
					myTimeList.add(item);
				}
			}
		}
		return  myTimeList;
	}


	/**
	 * 删除个人中心我的活动订单
	 * @param orderId
	 * @return
	 */
	public int delMyAct(String orderId){
		try {
			Example example = new Example(WhgActivityOrder.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("id",orderId);
			whgActOrderMapper.deleteByExample(example);
			return 0;
		}catch (Exception e){
			log.error(e.toString());
			return 1;
		}
	}


/**
	 * 根据活动ID获取座位信息
	 * @param actId
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public JSONObject getSeat4ActId(String actId, String eventId, String userId){
		JSONObject mapObejct = new JSONObject();
		Map<String,Object> map = new HashMap<>();
		if(eventId!=null&&!eventId.equals("")){
			map.put("eventId", eventId);
		}
		if(actId!=null&&!actId.equals("")){
			map.put("actId", actId);
		}
		WhgActivity whgActActivity = activityMapper.selectByPrimaryKey(actId);
		if(whgActActivity.getSellticket() == 3){
			map.put("sellticket", whgActActivity.getSellticket());
		}
		List<Map> orderList = apiActOrderMapper.findSeat4EventId(map);
		mapObejct.put("seatSize", orderList.size());
		//当前用户在该场次订票张数
		map.put("userId", userId);
		orderList = apiActOrderMapper.findSeat4EventId(map);
		mapObejct.put("seatSizeUser", orderList.size()); //当前用户订票数
		return mapObejct;
	}




/**
	 * 查询可预订场次座位数量
	 * @param actId
 * @return
	 */
@Cacheable
	public int getWhgActSeat4ActId(String actId){
		WhgActivitySeat whgActSeat = new WhgActivitySeat();
		whgActSeat.setActivityid(actId);
		whgActSeat.setSeatstatus(1);
		List<WhgActivitySeat> seatList = whgActSeatMapper.select(whgActSeat);
		return seatList.size();
	}


/**
	 * 保存座位订单信息
	 * @param seatId
	 * @param orderId
	 */
@CacheEvict(allEntries = true)
	public void saveSeatOrder(String seatId,String orderId,String seatCode){
		try {
			WhgActivityTicket whgActTicket = new WhgActivityTicket();
			whgActTicket.setId(IDUtils.getID());
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
@Cacheable
	public int getTicketList4OrderId(String orderId){
		WhgActivityTicket whgActTicket =new WhgActivityTicket();
		whgActTicket.setOrderid(orderId);
		List<WhgActivityTicket> ticketList = whgActTicketMapper.select(whgActTicket);
		return ticketList.size();
	}


	/**
	 * 根据活动id获取 该活动已预订的票数
	 * @param orderId
	 * @return
	 */
	//@Cacheable
	public int getTicketCountAct(String actId, String userid, String orderid){
		WhgActivityOrder actOrder = new WhgActivityOrder();
		Example example = new Example(WhgActivityOrder.class);
		Example.Criteria c = example.createCriteria();
		if(actId != null && !"".equals(actId)){
			c.andEqualTo("activityid",actId );
		}
        if (userid != null && !userid.isEmpty()) {
            c.andEqualTo("userid", userid);
        }
        if (orderid != null && !orderid.isEmpty()) {
            c.andEqualTo("id", orderid);
        }
        c.andEqualTo("orderisvalid",1);
		c.andNotEqualTo("ticketstatus",3);
		List<WhgActivityOrder> list= whgActOrderMapper.selectByExample(example);
		int count=0;
		for(WhgActivityOrder activityOrder:list){
			count+=getTicketList4OrderId(activityOrder.getId());
		}
		return count;
	}
/**
	 * 根据座位ID验证该座位是否已经预定
	 * @param eventId
	 * @return
	 */
@Cacheable
	public Map getTicketList4SeatId(String seatId,String eventId){
		Map map = ticketMapper.queryTicket4Id(seatId, eventId);

		return map;
	}

	public String checkApplyAct(String actId,String userId){
		WhgActivity actModel = activityMapper.selectByPrimaryKey(actId);

		//活动报名已结束
		if(actModel.getEndtime().before(Calendar.getInstance().getTime())){
			return "101";
		}
		//实名制验证
		if(actModel.getIsrealname() == 1){
			Map<String,Object> param = new HashMap<>();
			param.put("userId", userId);
			/*Map<String,Object> map = crtWeiXinMapper.findUserInfo4UserId(param);
			if(map.get("isrealname") == null || Integer.parseInt((String) map.get("isrealname")) != 1){
				return "104";
			}*/
		}
		return SUCCESS;
	}
	
/**
	 * 查询用户已经取消的订单
	 * @param actId
	 * @param userId
	 * @return
	 */
@Cacheable
	public List<WhgActivityOrder> findOrderList4Id(String actId,String userId){
		WhgActivityOrder actOrder = new WhgActivityOrder();
		if(actId != null){
			actOrder.setActivityid(actId);
		}
		actOrder.setUserid(userId);
		actOrder.setTicketstatus(3);
		actOrder.setOrderisvalid(2);
		return  whgActOrderMapper.select(actOrder);
	}

/**
	 * 查询活动，支持筛选
	 * @param pageNo
	 * @param pageSize
	 * @param param
	 * @return
	 */

	public PageInfo getActListForActFrontPage(String pageNo, String pageSize, Map param,String protype){
		try {
			PageHelper.startPage(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
			List<Map> list = crtActivityMapper.queryActList(param);
			if(null == list){
				return null;
			}
			return new PageInfo(list);
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}

	public PageInfo getActListPhbFrontPage(String pageNo, String pageSize, Map param,String protype){
		try {
			PageHelper.startPage(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
			List<Map> list = crtActivityMapper.queryActPhbList(param,protype);
			if(null == list){
				return null;
			}
			return new PageInfo(list);
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}


/**
	 * 判断用户是否可以报名活动
	 * @param actList
	 * @return
	 */

public List judgeCanSign2(List<WhgActivity> actList) {
		List list = new ArrayList();
		if(null == actList){
			return null;
		}
	for (WhgActivity item : actList) {
		Date enterstrtime = item.getEnterstrtime();
		Date enterendtime = item.getEnterendtime();
			int sellticket = 0;
		if (item.getSellticket() != null) {
			sellticket = item.getSellticket();
			}
		String actId = item.getId();
			Integer canSign = this.canSign(null,enterstrtime,enterendtime);
			JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(item));

			if(canSign==0){//可报名:报名时间内
				if(sellticket==1){
					jsonObject.put("liststate",3);    //直接前往：在报名时间内 又是不可预订类型 就直接前往
				} else {
					int ticketCount = actSyTicket(actId);//剩余票数
					if(ticketCount <= 0 ){
						jsonObject.put("liststate",2);//已订完
					}else{
						jsonObject.put("liststate", 1);
					}
					jsonObject.put("ticketnum",ticketCount);
				}
			}else if(canSign==104){//即将开始：还未到报名时间
				jsonObject.put("liststate",5);
			}else if(canSign==105){//可报
				jsonObject.put("liststate",4);//已结束
			}
			list.add(jsonObject);
		}
		return list;
	}

	/**
	 * 判断用户是否可以报名活动
	 *
	 * @param actList
	 * @return
	 */

	public List judgeCanSign(List<Map<String, Object>> actList) {
		List list = new ArrayList();
		if (null == actList) {
			return null;
		}
		for (Map<String, Object> item : actList) {
			Date enterstrtime = (Date) item.get("enterstrtime");
			Date enterendtime = (Date) item.get("enterendtime");
			int sellticket = 0;
			if (item.get("sellticket") != null) {
				sellticket = (int) item.get("sellticket");
			}
			String actId = (String) item.get("id");
			Integer canSign = this.canSign(null, enterstrtime, enterendtime);
			JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(item));
			int ticketCount = 0;
			if (canSign == 0) {//可报名:报名时间内
				if (sellticket == 1) {
					jsonObject.put("liststate", 3);    //直接前往：在报名时间内 又是不可预订类型 就直接前往
				} else {
					ticketCount = actSyTicket(actId);//剩余票数
					if (ticketCount <= 0) {
						jsonObject.put("liststate", 2);//已订完
					} else {
						jsonObject.put("liststate", 1);
					}

				}
			} else if (canSign == 104) {//即将开始：还未到报名时间
				jsonObject.put("liststate", 5);
			} else if (canSign == 105) {//可报
				jsonObject.put("liststate", 4);//已结束
			}
			jsonObject.put("ticketnum", actSyTicket(actId));
			jsonObject.put("ticketAllnum", this.getActTicketList(actId));
			list.add(jsonObject);
		}
		return list;
	}


	/**
	 * 活动剩余票数
	 */
	public Integer actSyTicket(String actId) {
		int ticketCounts = getWhgActTicketCountsActId(actId);//该活动 所有 已预订票数
		int actCounts = this.getActTicketList(actId);//该活动总票数
		return actCounts-ticketCounts;
	}



/**
	 * 判断用户是否能报名
	 * @param enterstrtime
	 * @return:
	 *  0:可报名
	 *  100:未登录
	 *  101:未实名认证
	 *  102:年龄段不合适
	 *  103:超过报名人数
	 *  104:还未到报名时间
	 *  105:报名时间已过
	 */

	public Integer canSign(WhgActivity whgActivity,Date enterstrtime,Date enterendtime){
		Date dd=new Date();
		if(enterstrtime!=null){
			if(enterstrtime.getTime()>dd.getTime()){
				return 104;
			}
		}
		if(enterendtime!=null){
			if(enterendtime.getTime()<dd.getTime()){
				return 105;
			}
		}
		return 0;
	}

	private Boolean isAfter(Date date1,Date date2){
		if(null == date1 || null == date2){
			return true;
		}
		LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if(localDate1.isAfter(localDate2)){
			return true;
		}
		return false;
	}

@Cacheable
	public WhgActivityTime getActTimeInfo(String eventId){
		try {
			WhgActivityTime whgActTime = new WhgActivityTime();
			whgActTime.setId(eventId);
			whgActTime = whgActTimeMapper.selectOne(whgActTime);
			return whgActTime;
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}

	public int getActTicketChecked(String orderId){
		try {
			return apiActTimeMapper.getActTicketChecked(orderId);
		}catch (Exception e){
			log.error(e.toString());
			return 0;
		}
	}

/**
 * 跳入预定之前 验证
	 * @param actId 活动Id
	 * @param eventId 场次Id
	 * @param userId 用户Id
	 * @param seatStr 在线选座座位号
	 * @param seats 自由入座票数
	 * @return map
	 */

public Map<String, Object> checkPreActBook(String actId, String userId) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("code", 0);
		map.put("msg", "");
		WhgActivity whgActActivity = this.getActDetail(actId);
	boolean flag = true;
	if (whgActActivity.getState() != 6) {
			map.put("code", 112);
			map.put("msg", "活动已下架!");
			flag = false;
			return map;
		}
		//加入黑名单
		boolean isBlack = this.whgBlackListService.isBlackListUser(userId);
		if (isBlack) {
			map.put("code", 205);
			map.put("msg", "非常抱歉！您的操作行为已被列入黑名单，如需了解详细情况，请与管理员联系！");
			flag = false;
			return map;
		}
		//实名制验证
		if(flag && whgActActivity.getIsrealname() == 1){
            WhgUser user = whgUserMapper.selectByPrimaryKey(userId);
			if(user.getIsrealname().intValue() != 1){
				map.put("code", 200);
				map.put("msg", "您还未完成实名认证，请先完成实名认证！");
				flag = false;
				return map;
			}
		}
		return map;
	}

	public Map<String, Object> checkActOrder(String actId, String eventId, String zcId, String userId, String seatStr, int seats) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("code", 0);
		map.put("msg", "");
		WhgActivity whgActActivity = this.getActDetail(actId);
		WhgActivityTime whgActTime = whgActTimeMapper.selectByPrimaryKey(eventId);
		map = checkPreActBook(actId, userId);
		if (Integer.parseInt(String.valueOf(map.get("code"))) != 0){
			return map;
		}
		//活动已结束
		if (whgActTime != null && whgActTime.getPlayendtime().before(Calendar.getInstance().getTime())) {
			map.put("code", 110);
			map.put("msg", "当前活动场次已结束!");
			return map;
		}
		//活动报名时间已结束
		if (whgActActivity != null && whgActActivity.getEnterendtime().before(new Date())) {
			map.put("code", 110);
			map.put("msg", "活动报名时间已结束!");
			return map;
		}
		//活动已开始
		if (whgActTime != null && whgActTime.getPlaystarttime().before(Calendar.getInstance().getTime())) {
			map.put("code", 110);
			map.put("msg", "当前活动场次已开始!");
			return map;
		}
		if ((actId == null || "".equals(actId))) {
			map.put("code", 101);
			map.put("msg", "活动Id不允许为空!");
			return map;
		}
		if ((eventId == null || "".equals(eventId)) ){
			map.put("code", 102);
			map.put("msg", "活动场次Id不允许为空");
			return map;
		}
		if ((userId == null || "".equals(userId))) {
			map.put("code", 103);
			map.put("msg", "用户Id不允许为空");
			return map;
		}
		if (((seatStr == null || "".equals(seatStr)) && seats < 1)) {
			map.put("code", 104);
			map.put("msg", "座位数必须大于0");
			return map;
		}
		if (whgActActivity.getState() != 6) {
			map.put("code", 105);
			map.put("msg", "该活动已下架!");
			return map;
		}
		String selectSeat[] = null;
		if (seatStr != null && !"".equals(seatStr)) {
			selectSeat = seatStr.split(",");
			for (int i = 0; i < selectSeat.length; i++) {
				WhgActivitySeat whgActSeat = this.getWhgActTicket4ActId(actId, selectSeat[i]);
				Map map_ = this.getTicketList4SeatId(whgActSeat.getId(), eventId);
				if (map_ != null) {
					map.put("code", 106);
					map.put("msg", "该座位已被预定！");
					break;
				}
			}
		}
		JSONObject seatJson = this.getSeat4ActId(actId, eventId, userId);
		int userSeatNum = seatJson.getIntValue("seatSizeUser");//当前用户订票数
		int seatSize = seatJson.getIntValue("seatSize");//当前活动已被预定票数
		int seatCount = 0;//可预订座位总数
		int seatNum = 0;
		if (whgActActivity.getSellticket() == 2) {
			seatCount = whgActTime.getSeats();//该场次种座位数
			seatNum = seats;//当前用户选择座位
		} else {
			seatCount = this.getWhgActSeat4ActId(actId);
			seatNum = selectSeat.length;
		}
		if (zcId != null) {//走众筹逻辑时 最大票数 为其众筹数
			WhgGather whgGather = whgGatherService.t_srchOneGather(zcId);
			seatCount = whgGather.getNumsum();
		}
		if (zcId != null) {
			if (seatCount - seatSize <= 0) {
				map.put("code", 107);
				map.put("msg", "该活动众筹份数已满额！");
				return map;
			}
		} else {
			if (seatCount - seatSize <= 0) {
				map.put("code", 107);
				map.put("msg", "本场活动已无可预订座位！");
				return map;
			}
		}
		int whgSeatCount = 0;
		if (whgActActivity.getSeats() != null) {
			whgSeatCount = whgActActivity.getSeats();
		}
		if (userSeatNum + seatNum > whgSeatCount) {
			map.put("code", 108);
			map.put("msg", "每位用户最多可以预定" + whgActActivity.getSeats() + "个座位！");
			return map;
		}
		if (zcId != null) {
			if (seatSize + seatNum > seatCount) {
				map.put("code", 109);
				map.put("msg", "该活动众筹份数已满额！");
				return map;
			}
		} else {
			if (seatSize + seatNum > seatCount) {
				map.put("code", 109);
				map.put("msg", "该活动已无可预订座位！");
				return map;
			}
		}
		return map;
	}

/**
	 * 保存活动信息
	 * @param actId 活动ID
	 * @param eventId 活动场次Id
	 * @param userId 用户Id
	 * @param orderPhoneNo 订单电话
	 * @param seatStr 在线选座数
	 * @param seats 自由入座座位数
	 * @param name 预定名称
	 */
@CacheEvict(allEntries = true)
	public void saveActOrder(String id,String actId,String eventId, String userId,String orderPhoneNo,String seatStr,int seats,String name){
		WhgActivityOrder actOrder = new WhgActivityOrder();
		try {
			//String id = this.commservice.getKey("WhgActivityOrder");
			actOrder.setId(id);
			actOrder.setOrderphoneno(orderPhoneNo);
			actOrder.setOrdername(name);
			actOrder.setEventid(eventId);
			actOrder.setOrdernumber(IDUtils.getOrderID(EnumOrderType.ORDER_ACT.getValue()+""));
			actOrder.setUserid(userId);
			this.addActOrder(actId, actOrder);
			WhgActivityTime actTime = this.selectOnePlay(actOrder.getEventid());
			String mySelectSeat = seatStr; //在线选座位置数
			int seatNum = seats; //自由入座位置数
			String[] selectSeat = mySelectSeat.split(",");
			int totalSeat = selectSeat.length;
			if(seatNum > 0){
				totalSeat = seatNum;
			}
			int sum_ = 1;
			for(int i=0;i<totalSeat;i++){
				if(seatNum > 0){//自由入座
					this.saveSeatOrder("P"+sum_, id,"票"+sum_);
				}else{ //在线选座
					WhgActivitySeat whgActSeat =this.getWhgActTicket4ActId(actId, selectSeat[i]);
					this.saveSeatOrder(whgActSeat.getId(), id,whgActSeat.getSeatnum());
				}
				sum_++;
			}
			//发送短信
			WhgActivity whgActActivity = this.getActDetail(actId);
			Map<String, String> smsData = new HashMap<String, String>();
			smsData.put("userName", actOrder.getOrdername());
			smsData.put("title", whgActActivity.getName());
			smsData.put("ticketCode", actOrder.getOrdernumber());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = actTime.getPlaydate();
			String dateStr = sdf.format(date);
			smsData.put("beginTime", dateStr +" "+ actTime.getPlaystime());
			int num = 0;
			if(totalSeat > 0){
				num=totalSeat;
			}else{
				num = seatNum;
			}
			smsData.put("number", String.valueOf(num));
			//smsService.t_sendSMS(actOrder.getOrderphoneno(), "ACT_DUE", smsData);
			if (whgActActivity.getNoticetype() != null && whgActActivity.getNoticetype().indexOf("ZNX") != -1) {
				if(whgActActivity.getBiz()!=null){//众筹活动
					insMessageService.t_sendZNX(actOrder.getUserid(),null, "ZC_CY_True", smsData, actOrder.getActivityid(), EnumProject.PROJECT_ZC.getValue());
				}else {
					insMessageService.t_sendZNX(actOrder.getUserid(),null, "ACT_DUE", smsData, actOrder.getActivityid(), EnumProject.PROJECT_WBGX.getValue());
				}
			}
			if (whgActActivity.getNoticetype() != null && whgActActivity.getNoticetype().indexOf("SMS") != -1) {
				if(whgActActivity.getBiz()!=null){//众筹活动
					smsService.t_sendSMS(actOrder.getOrderphoneno(), "ZC_CY_True", smsData, actId);
				}else {
                    smsService.t_sendSMS(actOrder.getOrderphoneno(), "ACT_DUE", smsData, actId);
                }
			}

			//短信发送成功后更改订单短信状态
			actOrder.setOrdersmsstate(2);
			actOrder.setOrdersmstime(new Date());
			this.upActOrder(actOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 众筹活动发送短信
	 * @param actOrder 活动订单实体
	 * @param successStr true 为成功 false 为失败
	 */
	public void sendSmsAct(WhgActivityOrder actOrder, boolean success){
		try {
            String actId=actOrder.getActivityid();
			WhgActivityTime actTime = this.selectOnePlay(actOrder.getEventid());
			Example example2 = new Example(WhgActivityTicket.class);
			Example.Criteria criteria2 = example2.createCriteria();
			criteria2.andEqualTo("orderid",actOrder.getId());
			List<WhgActivityTicket> whgActTicketList = whgActTicketMapper.selectByExample(example2);
			int num = whgActTicketList.size();
			//发送短信
			WhgActivity whgActActivity = this.getActDetail(actId);
			Map<String, String> smsData = new HashMap<String, String>();
			smsData.put("userName", actOrder.getOrdername());
			smsData.put("title", whgActActivity.getName());
			smsData.put("ticketCode", actOrder.getOrdernumber());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = actTime.getPlaydate();
			String dateStr = sdf.format(date);
			smsData.put("beginTime", dateStr +" "+ actTime.getPlaystime());
			smsData.put("number", String.valueOf(num));

			if(whgActActivity.getBiz()!=null) {//众筹活动
				if (whgActActivity.getNoticetype() != null && whgActActivity.getNoticetype().indexOf("ZNX") != -1) {//站内信
					if(success){//众筹成功
                        insMessageService.t_sendZNX(actOrder.getUserid(),null, "ZC_ACT_True", smsData, actOrder.getActivityid(), EnumProject.PROJECT_ZC.getValue());
                    }else{
                        insMessageService.t_sendZNX(actOrder.getUserid(),null, "ZC_Failure", smsData, actOrder.getActivityid(), EnumProject.PROJECT_ZC.getValue());
                    }
				}
				if (whgActActivity.getNoticetype() != null && whgActActivity.getNoticetype().indexOf("SMS") != -1) {
					if(success){//众筹成功
						smsService.t_sendSMS(actOrder.getOrderphoneno(), "ZC_ACT_True", smsData, actId);
					}else{
						smsService.t_sendSMS(actOrder.getOrderphoneno(), "ZC_Failure", smsData, actId);
					}
				}
			}else {//普通活动
				if (whgActActivity.getNoticetype() != null && whgActActivity.getNoticetype().indexOf("ZNX") != -1) {//站内信
					insMessageService.t_sendZNX(actOrder.getUserid(),null, "ACT_DUE", smsData, actOrder.getActivityid(), EnumProject.PROJECT_ZC.getValue());
				}
				if (whgActActivity.getNoticetype() != null && whgActActivity.getNoticetype().indexOf("SMS") != -1) {
					smsService.t_sendSMS(actOrder.getOrderphoneno(), "ACT_DUE", smsData, actId);
				}
			}
			//短信发送成功后更改订单短信状态
			actOrder.setOrdersmsstate(2);
			actOrder.setOrdersmstime(new Date());
			this.upActOrder(actOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
