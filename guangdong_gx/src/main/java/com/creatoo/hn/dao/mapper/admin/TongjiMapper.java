package com.creatoo.hn.dao.mapper.admin;

import java.util.List;
import java.util.Map;

/**
 * 统计接口
 */
public interface TongjiMapper {
    /**
     * 统计培训排行
     * @param param
     * @return
     */
    List<Map> pxph(Map param);

    /**
     * 活动排行
     * @param param
     * @return
     */
    List<Map> hdph(Map param);

    /**
     * 站点活动
     * @param param
     * @return
     */
    List<Map> zdhd(Map param);

    /**
     * 站点培训
     * @param param
     * @return
     */
    List<Map> zdpx(Map param);

    /**
     * 站点场馆
     * @param param
     * @return
     */
    List<Map> zdcg(Map param);

    /**
     * 站点资讯
     * @param param
     * @return
     */
    List<Map> zdzx(Map param);

    /**
     * 注册用户，活跃用户，PV， UV
     * @return
     */
    List<Map> tjptsj();

    /**
     * 年度场馆排行
     * @return
     */
    List<Map> venActPaihang();

    /**
     * 年度场馆总活动场次
     * @return
     */
    Integer venActTotal();

    /**
     * 文化服务趋势-活动-最近六个月
     * @param param 六个月的月份： 2018-01
     * @return
     */
    List<Map> actSearch(Map param);

    /**
     * 文化服务趋势-培训-最近六个月
     * @param param
     * @return
     */
    List<Map> traSearch(Map param);

    /**
     * 年度或者月度最热活动
     * @param param
     * @return
     */
    List<Map> hotAct(Map param);
}
