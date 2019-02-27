package com.creatoo.hn.services.admin.activity;

import com.creatoo.hn.dao.mapper.WhgActivityTimeMapper;
import com.creatoo.hn.dao.model.WhgActivity;
import com.creatoo.hn.dao.model.WhgActivityTime;
import com.creatoo.hn.util.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 活动场次管理 服务层
 * @author heyi
 *
 */
@Service
@CacheConfig(cacheNames = "WhgActivityTime", keyGenerator = "simpleKeyGenerator")
public class WhgActivityTimeService {
    /**
     * 活动场次DAO
     */
    @Autowired
    private WhgActivityTimeMapper whgActivityTimeMapper;
   
   /**
    * 添加场次
    * @param act
    * @return
    * @throws Exception
    */
   @CacheEvict(allEntries = true)
    public void t_add(List<Map<String, String>> timePlayList,String actId,Date time,int seats){
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	for (int i = 0; i < timePlayList.size(); i++) {
        	Map<String,String> map = timePlayList.get(i);
        	String playstrtime = String.valueOf(map.get("playstrtime"));
        	String playendtime = String.valueOf(map.get("playendtime"));
        	WhgActivityTime actTime = new WhgActivityTime();
        	try {
				Date playStartTime = sdfDateTime.parse(sdf.format(time) + " " + playstrtime);
				Date playEndTime = sdfDateTime.parse(sdf.format(time) + " " + playendtime);
        		actTime.setId(IDUtils.getID());
        		actTime.setActid(actId);
        		actTime.setPlaydate(time);
        		actTime.setPlayetime(playendtime);
        		actTime.setPlaystime(playstrtime);
        		actTime.setSeats(seats);
        		actTime.setState(1);
				actTime.setPlaystarttime(playStartTime);
				actTime.setPlayendtime(playEndTime);
	        	this.whgActivityTimeMapper.insert(actTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }

    @CacheEvict(allEntries = true)
   public void updateOne(WhgActivityTime param){
    	try{
    		whgActivityTimeMapper.updateByPrimaryKey(param);
		}catch (Exception e){
    		e.printStackTrace();
		}
	}

	@Cacheable
	public PageInfo getActivityScreenings(int page, int rows, WhgActivityTime WhgActivityTime) throws  Exception{

		Example example = new Example(WhgActivityTime.class);
		Example.Criteria c = example.createCriteria();
		c.andEqualTo(WhgActivityTime);
		example.setOrderByClause("playdate");
		PageHelper.startPage(page,rows);
		List list = whgActivityTimeMapper.selectByExample(example);
		return new PageInfo<Object>(list);
	}
    
    
    /**
     * 保存单条场次信息
     * @param WhgActivityTime
     */
    @CacheEvict(allEntries = true)
   public void addOne(WhgActivityTime WhgActivityTime){
    	whgActivityTimeMapper.insert(WhgActivityTime);
    }

    @Cacheable
    public WhgActivityTime findWhgActivityTime4Id(String id){
    	return whgActivityTimeMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据活动Id获取场次对象
     * @param actId
     * @return
     */
    @Cacheable
   public WhgActivityTime findWhgActivityTime4ActId(String actId){
    	WhgActivityTime WhgActivityTime = new WhgActivityTime();
    	WhgActivityTime.setActid(actId);
        List<WhgActivityTime> list = whgActivityTimeMapper.select(WhgActivityTime);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
