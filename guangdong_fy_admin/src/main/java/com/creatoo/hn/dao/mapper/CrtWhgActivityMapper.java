package com.creatoo.hn.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/3/23.
 */
@SuppressWarnings("all")
public interface CrtWhgActivityMapper {

    /**
     * 活动 后台管理列表 查询
     * @param act
     * @return
     */
    List<Map> srchListActivity(Map act);

    /**
     * 为活动后台获取活动订单数据
     * @return
     * added by caiyong 2017/4/7
     */
    List<Map> getActOrderForBackManager(Map act);


    /**
     * 为活动前台获取活动列表数据
     * @return
     */
    public List<Map> queryActList(@Param("param") Map param,@Param("protype") String protype);
    /**
     * 活跃分管
     * @return
     */
    public List<Map> getActiveFG(@Param("param") Map param,@Param("protype") String protype);

    /**
     * 排行榜指数
     * @return
     */
    public List<Map> queryActPhbList(@Param("param") Map param,@Param("protype") String protype);
}
