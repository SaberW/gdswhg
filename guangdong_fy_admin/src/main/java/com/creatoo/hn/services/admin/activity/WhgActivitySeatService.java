package com.creatoo.hn.services.admin.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/*import com.creatoo.hn.mapper.WhgActivitySeatMapper;
import com.creatoo.hn.model.WhgActivitySeat;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.comm.CommService;*/
import com.creatoo.hn.dao.mapper.WhgActivitySeatMapper;
import com.creatoo.hn.dao.model.WhgActivitySeat;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 活动座位管理 服务层
 * @author heyi
 *
 */
@Service
@CacheConfig(cacheNames = "WhgActivitySeat", keyGenerator = "simpleKeyGenerator")
public class WhgActivitySeatService extends BaseService {

    
    /**
     * 活动DAO
     */
   @Autowired
    private WhgActivitySeatMapper WhgActivitySeatMapper;

    /**
     * 添加活动座位
     * @param actId
     * @param seatList
     * @param user
     * @return
     * @throws Exception
     */
	@CacheEvict(allEntries = true)
    public void t_add(List<Map<String, String>> seatList, WhgSysUser user, String actId)throws Exception{
        //设置初始值
        Date now = new Date();
        int num = 1;
        for (int i = 0; i < seatList.size(); i++) {
        	Map<String,String> map = seatList.get(i);
        	int seatrow = Integer.parseInt(map.get("seatrow"));
        	int seatcolumn = Integer.parseInt(map.get("seatcolumn"));
        	int seatstatus = Integer.parseInt(map.get("seatstatus"));
        	String seatcode = map.get("seatcode");
        	String numreal = map.get("numreal");
        	WhgActivitySeat actSeat = new WhgActivitySeat();
        	actSeat.setId(IDUtils.getID());
        	actSeat.setActivityid(actId);
        	actSeat.setSeatcode(seatcode);
        	actSeat.setSeatcolumn(seatcolumn);
        	actSeat.setSeatrow(seatrow);
        	actSeat.setCrtdate(now);
        	actSeat.setStatemdfdate(now);
        	actSeat.setSeatstatus(seatstatus);
        	actSeat.setCrtuser(user.getId());
        	actSeat.setStatemdfuser(user.getId());
        	actSeat.setSeatticketstatus(1);
        	actSeat.setSeatnum(numreal);
//        	actSeat.setSeatid(seatid);
        	this.WhgActivitySeatMapper.insert(actSeat);
		}
    }
    
    /**
     * 组装需要添加的活动座位信息
     */
    public List<Map<String, String>> setSeatList(String seatJson ){
    	JSONArray seatArry = JSON.parseArray(seatJson);
    	List<Map<String, String>> seatList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < seatArry.size(); i++) {
        	JSONArray jsonArray  = seatArry.getJSONArray(i);
        	for (int j = 0; j < jsonArray.size(); j++) {
        		Map<String, String> map = new HashMap<String, String>();
        		JSONObject jsonObject = jsonArray.getJSONObject(j);
        		int numrow = jsonObject.getIntValue("numrow");
        		int numcol = jsonObject.getIntValue("numcol");
        		int open = jsonObject.getIntValue("open");
        		int type = jsonObject.getIntValue("type");
        		String numreal = jsonObject.getString("numreal");
        		//type=1 && open = 1 正常座位
				//type=0 不存在的座位
				//type=1 && open = 0 不可选座位
				if(1 == type){
					if(1 == open){
						map.put("seatstatus", String.valueOf(1));
					}else {
						map.put("seatstatus", String.valueOf(2));
					}
				}else{
					map.put("seatstatus", String.valueOf(3));
				}
        		map.put("seatcode", numrow+"_"+numcol);//座位编号
        		map.put("seatrow", String.valueOf(numrow));
        		map.put("seatcolumn", String.valueOf(numcol));
        		map.put("numreal",numreal);
        		seatList.add(map);
			}
		}
        return seatList;
    }

	@CacheEvict(allEntries = true)
    public void updateActivitySeatInfo(List<Map<String, String>> seats,String activiyId,WhgSysUser whgSysUser) throws Exception{
		Date now = new Date();
    	for(Map<String, String> item : seats){
    		WhgActivitySeat WhgActivitySeat = new WhgActivitySeat();
			int seatrow = Integer.parseInt(item.get("seatrow"));
			int seatcolumn = Integer.parseInt(item.get("seatcolumn"));
			int seatstatus = Integer.parseInt(item.get("seatstatus"));
			String seatcode = item.get("seatcode");
			WhgActivitySeat actSeat = new WhgActivitySeat();
			actSeat.setActivityid(activiyId);
			actSeat.setSeatcode(seatcode);
			actSeat = WhgActivitySeatMapper.selectOne(actSeat);
			if(null == actSeat){
				continue;
			}
			actSeat.setCrtdate(now);
			actSeat.setStatemdfdate(now);
			actSeat.setSeatstatus(seatstatus);
			actSeat.setCrtuser(whgSysUser.getId());
			actSeat.setStatemdfuser(whgSysUser.getId());
			actSeat.setSeatticketstatus(1);
			WhgActivitySeatMapper.updateByPrimaryKey(actSeat);
		}
	}

    /**
	 * 获取活动座位信息
	 * added by caiyong
	 * 2017/4/6
	 * */
	@Cacheable
	public JSONObject getActivitySeatInfo(String activityId){
		JSONObject jsonObject = new JSONObject();
		WhgActivitySeat WhgActivitySeat = new WhgActivitySeat();
		WhgActivitySeat.setActivityid(activityId);
		List<WhgActivitySeat> list = WhgActivitySeatMapper.select(WhgActivitySeat);
		Map<Integer,List<WhgActivitySeat>> rowsMap = new LinkedHashMap<Integer,List<WhgActivitySeat>>();
		for(WhgActivitySeat item:list){
			if(rowsMap.containsKey(item.getSeatrow())){
				rowsMap.get(item.getSeatrow()).add(item);
			}else{
				List<WhgActivitySeat> subList = new ArrayList<WhgActivitySeat>();
				subList.add(item);
				rowsMap.put(item.getSeatrow(),subList);
			}
		}
		JSONArray jsonArray = new JSONArray();
		Iterator<Integer> iter = rowsMap.keySet().iterator();
		Integer maxCol = 0;
		while(iter.hasNext()){
			List<WhgActivitySeat> oneRow = rowsMap.get(iter.next());
			JSONArray oneRowArray = getOneRow(oneRow);
			jsonArray.add(oneRowArray);
			if(maxCol < oneRowArray.size()){
				maxCol = oneRowArray.size();
			}
		}
		jsonObject.put("rowNum",jsonArray.size());
		jsonObject.put("colNum",maxCol);
		jsonObject.put("mySeatMap",jsonArray);
		return jsonObject;
	}

	/**
	 * 组装一行座位成JSON数组
	 * added by caiyong
	 * 2017/4/6
	 * */

	private JSONArray getOneRow(List<WhgActivitySeat> oneRow){
		JSONArray jsonArray = new JSONArray();
		for(WhgActivitySeat item : oneRow){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("numrow",item.getSeatrow());
			jsonObject.put("numcol",item.getSeatcolumn());
			jsonObject.put("numreal",item.getSeatnum());
			Integer status = item.getSeatstatus();
			if(1 == status){
				jsonObject.put("type",1);
				jsonObject.put("open",1);
			}else if(2 == status){
				jsonObject.put("type",1);
				jsonObject.put("open",0);
			}else{
				jsonObject.put("type",0);
				jsonObject.put("open",0);
			}
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	/**
	 * 根据活动Id删除该活动所对应的座位信息
	 * @param actId
	 */
	@CacheEvict(allEntries = true)
	public void delActSeat4ActId(String actId){
		WhgActivitySeat WhgActivitySeat = new WhgActivitySeat();
		WhgActivitySeat.setActivityid(actId);
		WhgActivitySeatMapper.delete(WhgActivitySeat);
	}
}
