package com.creatoo.hn.services.admin.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.dao.mapper.WhgActivityPlayMapper;
import com.creatoo.hn.dao.model.WhgActivityPlay;
import com.creatoo.hn.services.BaseService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 活动时间段管理 服务层
 * @author heyi
 *
 */


@Service
@CacheConfig(cacheNames = "WhgActivityPlay", keyGenerator = "simpleKeyGenerator")
public class WhgActivityPlayService extends BaseService {
	
    /**
     * 活动DAO
     */
   @Autowired
    private WhgActivityPlayMapper whgActPalyMapper;
    /**
     * 根据活动ID获取改活动的场次信息
     * @param actId
     * @return
     */
   public List<WhgActivityPlay> srchList4actId(String actId){
    	WhgActivityPlay record = new WhgActivityPlay();
        record.setActid(actId);
    	List<WhgActivityPlay> seatList = whgActPalyMapper.select(record);
    	return seatList;
    }
    
   /**
    * 设置活动场次
    * @param timeJson
    * @return
    */
   public List<Map<String,String>> setTimeTemp(String timeJson){
		List<Map<String, String>> timePlayList = new ArrayList<Map<String, String>>();
		JSONArray json = JSONArray.parseArray(timeJson);
		for(int i=0;i<json.size();i++){
			Map<String, String> map = new HashMap<String, String>();
		    JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
		    map.put("playstrtime", String.valueOf(job.get("playstrtime")));
		    map.put("playendtime", String.valueOf(job.get("playendtime")));
		    timePlayList.add(map);
		}
		return timePlayList;
   }

    private String getTomorrow(Date date){
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE,1);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
		date=calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}
	
}
