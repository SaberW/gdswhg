package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgCollection;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface WhgCollectionMapper extends Mapper<WhgCollection> {
    /**
     *
     * @return
     */
    public List<HashMap> selectCollection(String userid);
    /**
     *
     */
    public List<HashMap> selectTraitm(String userid);

    /**
     * 我的活动收藏--查询
     * @param cmuid
     * @return
     */
    public List<HashMap> selectMyActColle(@Param("cmuid") String cmuid);

    /**
     * 我的培训收藏--查询
     * @param cmuid
     * @return
     */
    public List<HashMap> selectMyTraitmColle(@Param("cmuid") String cmuid,@Param("cmreftyp")Integer cmreftyp);


    /**
     * 我的场馆收藏--查询
     * @param cmuid
     * @return
     */
    public List<HashMap> selectMyVenueColle(@Param("cmuid") String cmuid);

    /**
     * 我的活动室收藏--查询
     * @param cmuid
     * @return
     */
    public List<HashMap> selectMyRoomColle(@Param("cmuid") String cmuid);

    /**
     * 查询点赞列表
     * @param param
     * @return
     */
    public List<Map> selectCollectionWithUser(Map<String,Object> param);

    /**
     * 获取收藏数量
     * @param cmreftyp
     * @param cmrefid
     * @return
     */
    public Integer getCollectionCount(@Param("cmreftyp") String cmreftyp, @Param("cmrefid") String cmrefid);

    /**
     * 我的直播收藏
     * @param cmuid
     * @param cmreftyp
     * @return
     */
    List<HashMap> selectMyTraLiveColle(@Param("cmuid") String cmuid,@Param("cmreftyp")Integer cmreftyp);

    /**
     * 我的师资收藏
     * @param cmuid
     * @param cmreftyp
     * @return
     */
    List<HashMap> selectMyTraTeaColle(@Param("cmuid") String cmuid,@Param("cmreftyp")Integer cmreftyp);

    /**
     * 我的资源收藏
     * @param cmuid
     * @param cmreftyp
     * @return
     */
    List<HashMap> selectMyTraDrscColle(@Param("cmuid") String cmuid,@Param("cmreftyp")Integer cmreftyp);
}