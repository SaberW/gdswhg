package com.creatoo.hn.services.api.apioutside.whglm;

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
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumOrderType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * 文化管联盟
 * @author yanjianbo
 * @version 2016.11.16
 */

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgSysCult", keyGenerator = "simpleKeyGenerator")
public class ApiWhglmService extends BaseService {


	@Autowired
	private ApiWhgCultMapper apiWhgCultMapper;

	@Autowired
	private WhgYwiTagMapper whgYwiTagMapper;

	@Autowired
	private WhgYwiTypeMapper whgYwiTypeMapper;

	private final  String SUCCESS="0";//成功

	/*
	* 活跃分管
	*
	* */
	@Cacheable
	public PageInfo getActiveFG(String pageNo, String pageSize, Map param,String protype){
		try {
			PageHelper.startPage(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
			List<Map> list = apiWhgCultMapper.getActiveFG(param,protype);
			if(null == list){
				return null;
			}
			return new PageInfo(list);
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}

	/*
	* 文化馆联盟
	*
	* */
	public PageInfo getWhglm(String pageNo, String pageSize, Map param,String protype){
		try {
			PageHelper.startPage(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
			List<Map> list = apiWhgCultMapper.getWhglm(param,protype);
			if(null == list){
				return null;
			}
			return new PageInfo(list);
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}

	/*
	* 文化馆菜单
	*
	* */
	public Object getWhgMenu(String cultid){
		try {
			WhgSysCult sysCult= apiWhgCultMapper.selectByPrimaryKey(cultid);
			return sysCult;
		}catch (Exception e){
			log.error(e.toString());
			return null;
		}
	}


}
