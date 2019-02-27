package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhUser_old;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.base.select.SelectOneMapper;

import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public interface WhUserMapper extends Mapper<WhUser_old>,SelectOneMapper<WhUser_old> {
	/**
	 * 个人中心消息提醒
	 * @param refuid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> msgAlert(@Param("refuid")String refuid);
	
	/**
	 * 个人中心头部消息提醒
	 * @param refuid
	 * @return
	 */
	public String msgHeader(@Param("refuid")String refuid);

	/**
	 * 获取过期的用户订单
	 * @param userid
	 * @return
	 */
	public List<Map> getUserActOrderTimeOut(@Param("userid")String userid);

	/**
	 * 获取未过期的用户订单
	 * @param userid
	 * @return
	 */
	public List<Map> getUserActOrderNotTimeOut(@Param("userid")String userid);
}