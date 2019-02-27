package com.creatoo.hn.dao.mapper.api;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/8/9.
 */
public interface ApiVenueMapper {

    /**
     * 首页查场馆列表
     * @param cultid
     * @param protype
     * @return
     */
    public List selectVenue4IndexPage(@Param("cultid") List<String> cultid,
                                      @Param("deptid") List<String> deptid,
                                      @Param("protype") String protype);

    /**
     * 查场馆列表
     * @param recode
     * @return
     */
    public List selectVenue4page(Map recode);

    /**
     * 场馆分页查询（表字段名）
     * @param recode
     * @return
     */
    public List selectVenList4page(Map recode);

    /**
     * 热门场馆分页查询（表字段名）
     * @param recode
     * @return
     */
    public List selectHotVenList4page(Map recode);


    /**
     * 查场馆
     * @param id
     * @return
     */
    public Map findVenue4id(@Param("id") String id);

    /**
     * 查场馆（表字段名）
     * @param id
     * @param protype
     * @return
     */
    public Map findVenInfo4id(@Param("id")String id, @Param("protype")String protype);

    /**
     * 查推荐场馆
     * @param cultid
     * @param protype
     * @param exVenid
     * @return
     */
    public List selectVenue4Recommend(@Param("protype") String protype,
                                      @Param("cultid")List<String> cultid,
                                      @Param("deptid")List<String> deptid,
                                      @Param("exVenid") String exVenid);

    public List selectVenue4Recommend4supply(@Param("protype") String protype,
                                      @Param("cultid")List<String> cultid,
                                      @Param("deptid")List<String> deptid,
                                      @Param("exVenid") String exVenid);

    /**
     * 查场馆相关的活动室列表
     * @param venid
     * @return
     */
    public List selectVenRoom4venid(@Param("venid") String venid,
                                    @Param("now") Date now,
                                    @Param("protype")String protype);

    /**
     * 查场馆的活动室列表(表字段名)
     * @param venid
     * @return
     */
    public List selectRooms4ven(@Param("venid") String venid,
                                @Param("protype")String protype,
                                @Param("notroomid")String notroomid);

    public List selectRooms4venSupply(@Param("venid") String venid,
                                @Param("protype")String protype,
                                @Param("notroomid")String notroomid);

    /**
     * 查指定ID的活动室
     * @param id
     * @return
     */
    public Map findVenRoom4id(@Param("id") String id);


    /**
     * 查场馆
     *
     * @param id
     * @return
     */
    public Map findVenByRoomid(@Param("roomid") String roomid);

    /**
     * 查询用户活动室订单列表
     * @param recode
     * @return
     */
    public List selectOrder4User(Map recode);


    /**
     * 记入黑名单查找活动室订单: 1.指定次没有验票 2.黑名单更改时间之后 3.当前时间之前
     * @param checkNumber 未参与的次数参考
     * @param now  当前时间
     * @return
     */
    public List selectOrder4BlackListCheck(@Param("checkNumber") int checkNumber,
                                           @Param("now")Date now);
}
