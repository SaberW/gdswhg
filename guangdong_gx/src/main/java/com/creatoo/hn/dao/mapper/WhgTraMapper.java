package com.creatoo.hn.dao.mapper;


import com.creatoo.hn.dao.model.WhgTra;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;


import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WhgTraMapper extends Mapper<WhgTra> {
    /**
     * 根据类型进行统计
     * @return
     */
    List<Map> selTraByEtype();
    /**
     * 根据区域进行统计
     * @return
     */
    List<Map> selTraByArea();

    /**
     * 培训访问量TOP10统计
     * @return
     */
    List<Map> t_searchTraTop10();

    /**
     * 查询12个月每月培训发布量
     * @return
     */
    List<Map> srchTraByMonth(@Param("startdate") Date beginYear, @Param("enddate") Date endYear);

    /**
     *查询培训数据
     * @param params
     */
    List<Map> reptra(Map<String, Object> params);


    /**
     * 查询已发布且没加入微专业的培训
     * @return
     */
    List selTraList(@Param("mid")String mid,@Param("cultid")String cultid);

    /**
     * 查询已发布的培训
     * @return
     */
    List t_srchTraList();

    /**查培训取消报名黑名单*/
    List<Map> selectPxOrder4BlackListCheck(@Param("checkNumber") int checkNumber,
                                           @Param("now") Date now, @Param("actType") String actType);

    /**查培训未签到黑名单*/
    List<Map> selectPxSign4BlackListCheck(@Param("unenrolcount")int unenrolcount, @Param("now")Date now, @Param("actType")String actType);
}