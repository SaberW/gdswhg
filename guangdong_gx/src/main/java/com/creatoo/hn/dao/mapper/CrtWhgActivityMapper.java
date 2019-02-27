package com.creatoo.hn.dao.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
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
    public List<Map> queryActList(Map act);
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


    /**
     * 记入黑名单查找活动室订单: 1.指定次没有验票 2.黑名单更改时间之后 3.当前时间之前
     *
     * @param checkNumber 未参与的次数参考
     * @param now         当前时间
     * @return
     */
    public List selectOrder4BlackListCheck(@Param("checkNumber") int checkNumber,
                                           @Param("now") Date now, @Param("actType") String actType);

    /**
     * 记入黑名单查找取消活动超过对应次数
     *
     * @param checkNumber 未参与的次数参考
     * @param now         当前时间
     * @return
     */
    public List selectOrder5BlackListCheck(@Param("checkNumber") int checkNumber, @Param("actType") String actType);

    /**
     * 判断是否场次是否时间重合
     *
     * @return
     */
    public List<Map> checkTwoActTimes(Map acttime);

    public List<Map> getActTimes(Map acttime);
}
