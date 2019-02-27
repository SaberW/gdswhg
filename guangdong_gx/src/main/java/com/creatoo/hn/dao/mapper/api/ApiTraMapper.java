package com.creatoo.hn.dao.mapper.api;

import com.creatoo.hn.dao.model.WhgTra;
import com.creatoo.hn.dao.model.WhgTraCourse;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/10.
 */
public interface ApiTraMapper {

    /**
     * 查询上课时间是否有交叉
     * @param starttime
     * @param endtime
     * @param userId
     * @return
     */
    int selCount(@Param("starttime") Date starttime, @Param("endtime") Date endtime, @Param("userid") String userId, @Param("now") Date now);

    /**
     * 查询培训详情
     * @param id
     * @return
     */
    Map selTraById(@Param("id")String id,@Param("userid")String userid);

    /**
     * 查询我的培训列表
     * @param params
     * @return
     */
    public List<Map<String,Object>> getTraEnrolListByUserId(Map<String,Object> params);

    /**
     * 分页查询培训列表
     * @param cultid
     * @param arttype
     * @param type
     * @param sort
     * @param title
     * @return
     */
    List<WhgTra> selTraList(@Param("cultid")List cultid, @Param("arttype")String arttype, @Param("type")String type, @Param("sort")String sort, @Param("title")String title,@Param("now")Date now);


    List<Map> selTraCourseByUserid(@Param("usernumber") String usernumber, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 内部供需查询培训列表
     * @param cultid
     * @param arttype
     * @param type
     * @param title
     * @return
     */
    List selInsTraList(@Param("cultid")List cultid,@Param("deptid")List deptid, @Param("arttype")String arttype, @Param("type")String type, @Param("title")String title,@Param("protype")String protype, @Param("province")String province, @Param("city")String city, @Param("area")String area, @Param("psprovince")String psprovince, @Param("pscity")String pscity);

    /**
     * 查询详情
     * @param id
     * @param protype
     * @return
     */
    Object selectOneTra(@Param("id")String id, @Param("protype")String protype);

    /**
     * 查询推荐培训
     * @param protype
     * @param cultid
     * @param traid
     * @return
     */
    List selectTrainRecommend(@Param("protype")String protype, @Param("cultid")List cultid,  @Param("traid")String traid);

    /**
     * 查询首页上的培训
     * @param cultid
     * @return
     */
    List<Map> selindexTraList(@Param("cultid")List cultid,@Param("protype")String protype);

    /**
     * 查询首页的直播培训
     * @param cultid
     * @param protype
     * @return
     */
    List<Map> selindexTraliveList(@Param("cultid")List cultid,@Param("protype")String protype);


    /**
     * 网络培训平台列表页查询
     * @param recode
     * @return
     */
    List selectTraList4page(Map recode);

    /**
     * 根据培训id查询报名人员信息
     * @param id
     * @return
     */
    List<Map> selEnrolPerson(@Param("id")String id);

    /**
     * 根据用户ID查询培训订单
     * @param userid
     * @return
     */
    List selTraEnrol4Userid(@Param("userid")String userid,@Param("type")Integer type,@Param("cultid")List cultid);

    /**
     * 查询当前课程
     * @param userid
     * @param cultid
     * @param now
     * @return
     */
    List<Map> selNowKecheng(@Param("userid")String userid, @Param("cultid")String cultid, @Param("now")Date now);

    /**
     * 查询课程表中的课程
     * @param userid
     * @param cultid
     * @return
     */
    List selAllKecheng(@Param("userid")String userid, @Param("cultid")String cultid);

    /**
     * 根据培训id查询课程
     * @param traid
     * @return
     */
    List selKecheng4Tid(@Param("traid")String traid,@Param("userid")String userid);

    /**
     * 正在
     * @param userid
     * @param cultid
     * @return
     */
    List selInClass(@Param("userid")String userid, @Param("cultid")String cultid,@Param("now")Date now);

    /**
     * 微信课程表查询
     * @param userid
     * @param cultid
     * @param now
     * @return
     */
    List t_getWxKeCheng(@Param("userid")String userid, @Param("cultid")String cultid, @Param("now")Date now);

    /**
     *  微信热门推荐
     * @param recode
     * @return
     */
    List t_hotRecommend(Map recode);
}
