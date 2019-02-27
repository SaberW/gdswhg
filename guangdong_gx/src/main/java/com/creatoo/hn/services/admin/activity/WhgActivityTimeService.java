package com.creatoo.hn.services.admin.activity;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.dao.mapper.CrtWhgActivityMapper;
import com.creatoo.hn.dao.mapper.WhgActivityTimeMapper;
import com.creatoo.hn.dao.model.WhgActivity;
import com.creatoo.hn.dao.model.WhgActivityTime;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumBizState;
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
import java.util.*;

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

	@Autowired
	private CrtWhgActivityMapper crtWhgActivityMapper;
	/**
	 * @return
	 * @throws Exception
	 */
	@CacheEvict(allEntries = true)
	public void t_add(List<Map<String, String>> timePlayList, String actId, Date time, int seats) {
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < timePlayList.size(); i++) {
			Map<String, String> map = timePlayList.get(i);
			String playstrtime = String.valueOf(map.get("playstime"));
			String playendtime = String.valueOf(map.get("playetime"));
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
	public void updateOne(WhgActivityTime param) {
		try {
			whgActivityTimeMapper.updateByPrimaryKey(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@CacheEvict(allEntries = true)
	public void updateList(String actId, WhgActivityTime param) {
		try {
			Example example = new Example(WhgActivityTime.class);
			Example.Criteria c = example.createCriteria();
			c.andEqualTo("actid", actId);
			this.whgActivityTimeMapper.updateByExampleSelective(param, example);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@CacheEvict(allEntries = true)
	public void t_del(WhgActivityTime param) {
		try {
			whgActivityTimeMapper.delete(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Cacheable
	public PageInfo getActivityScreenings(int page, int rows, WhgActivityTime WhgActivityTime) throws Exception {

		Example example = new Example(WhgActivityTime.class);
		Example.Criteria c = example.createCriteria();
		c.andEqualTo(WhgActivityTime);
		example.setOrderByClause("playdate");
		PageHelper.startPage(page, rows);
		List list = whgActivityTimeMapper.selectByExample(example);
		return new PageInfo<Object>(list);
	}

	@Cacheable
	public PageInfo getActTimes(String actId, String optstarttime, String optendtime, String playdate, String eventid) throws Exception {
		Map map = new HashMap();
		Example example = new Example(WhgActivityTime.class);
		Example.Criteria c = example.createCriteria();
		if (eventid != null) {
			map.put("eventid", eventid);
		}
		map.put("playdate", playdate);
		map.put("actid", actId);
		map.put("playstarttime", optstarttime);
		map.put("playendtime", optendtime);
		List actTimeList = crtWhgActivityMapper.checkTwoActTimes(map);
		return new PageInfo<Object>(actTimeList);
	}


	public String getTimeByAct(String actId) throws Exception {
		Map map = new HashMap();
		map.put("actid", actId);
		List actTimeList = crtWhgActivityMapper.getActTimes(map);
		if (actTimeList.size() > 0) {
			return JSON.toJSONString(actTimeList);
		} else {
			return null;
		}
	}

	/**
	 * 保存单条场次信息
	 *
	 * @param WhgActivityTime
	 */
	@CacheEvict(allEntries = true)
	public void addOne(WhgActivityTime WhgActivityTime) {
		whgActivityTimeMapper.insert(WhgActivityTime);
	}

	@Cacheable
	public WhgActivityTime findWhgActivityTime4Id(String id) {
		return whgActivityTimeMapper.selectByPrimaryKey(id);
	}

	/**
	 * 根据活动Id获取场次对象
	 *
	 * @param actId
	 * @return
	 */
	@Cacheable
	public WhgActivityTime findWhgActivityTime4ActId(String actId) {
		WhgActivityTime WhgActivityTime = new WhgActivityTime();
		WhgActivityTime.setActid(actId);
		List<WhgActivityTime> list = whgActivityTimeMapper.select(WhgActivityTime);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据活动Id获取场次对象
	 *
	 * @param actId
	 * @return
	 */
	@Cacheable
	public List<WhgActivityTime> findActTimeListActId(String actId) {
		WhgActivityTime WhgActivityTime = new WhgActivityTime();
		WhgActivityTime.setActid(actId);
		Example example = new Example(WhgActivityTime.class);
		Example.Criteria c = example.createCriteria();
		c.andEqualTo("state", 1);
		c.andEqualTo("actid", actId);
		example.setOrderByClause("playstarttime");
		List<WhgActivityTime> list = whgActivityTimeMapper.selectByExample(example);
		return list;
	}

}
