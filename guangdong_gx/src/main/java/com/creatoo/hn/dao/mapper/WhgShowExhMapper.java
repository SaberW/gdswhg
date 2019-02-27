package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgShowExh;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface WhgShowExhMapper extends Mapper<WhgShowExh> {

    /**
     *查询展览类列表
     * @param cultid
     * @param type
     * @param title
     * @return
     */
    List selInsShowExhList(@Param("cultid")List cultid,@Param("deptid")List deptid, @Param("type")String type, @Param("title")String title,@Param("protype")String protype,@Param("province")String province, @Param("city")String city, @Param("area")String area, @Param("psprovince")String psprovince, @Param("pscity")String pscity);

    /**
     * 查询展览类详情
     * @param id
     * @param protype
     * @return
     */
    Object selectShowExhById(@Param("id")String id, @Param("protype")String protype);

    /**
     * 推荐展览
     * @param protype
     * @param cultid
     * @param id
     * @return
     */
    List selectExhRecommend(@Param("protype")String protype, @Param("cultid")List cultid, @Param("id")String id);


}