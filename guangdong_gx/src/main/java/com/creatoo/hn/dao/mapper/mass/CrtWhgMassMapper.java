package com.creatoo.hn.dao.mapper.mass;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/11/4.
 */
public interface CrtWhgMassMapper {

    /**
     * 查 类型id 相关的资讯
     * @param recode (mid, metyp, sort, order)
     * @return
     */
    @SelectProvider(type = CrtWhgMassProvider.class, method = "selectRefzxList")
    List<Map> selectRefzxList(Map recode);

    /**
     * 查 群文标记相关的 资讯列表
     * @param recode
     * @return
     */
    @SelectProvider(type = CrtWhgMassProvider.class, method = "selectZxList4Mass")
    List<Map> selectZxList4Mass(Map recode);

    /**
     * 插入 关联
     * @param list
     * @return
     */
    @InsertProvider(type = CrtWhgMassProvider.class, method = "insertMassRefZxList")
    int insertMassRefZxList(@Param("list") List<Map> list);

    /**
     * 移除 关联
     * @param zxids
     * @param mid
     * @param mtype
     * @return
     */
    @DeleteProvider(type = CrtWhgMassProvider.class, method = "deleteMassRefZxList")
    int deleteMassRefZxList(@Param("zxids")List<String> zxids,
                            @Param("mid")String mid, @Param("mtype")String mtype);

    /**
     * 清理 关联
     * @param mid
     * @param mtype
     * @return
     */
    @DeleteProvider(type = CrtWhgMassProvider.class, method = "deleteMassRefZx4Mid")
    int deleteMassRefZx4Mid(@Param("mid")String mid, @Param("mtype")String mtype);


    /**
     * 查 关联的 艺术人才
     * @param recode
     * @return
     */
    @SelectProvider(type = CrtWhgMassProvider.class, method = "selectRefrcList")
    List<Map> selectRefrcList(Map recode);

    /**
     * 查可选的艺术人才
     * @param recode
     * @return
     */
    @SelectProvider(type = CrtWhgMassProvider.class, method = "selectRcList")
    List<Map> selectRcList(Map recode);


    /**
     * 插入 关联
     * @param list
     * @return
     */
    @InsertProvider(type = CrtWhgMassProvider.class, method = "insertMassRefRcList")
    int insertMassRefRcList(@Param("list") List<Map> list);

    /**
     * 移除 关联
     * @param rcids
     * @param mid
     * @param mtype
     * @return
     */
    @DeleteProvider(type = CrtWhgMassProvider.class, method = "deleteMassRefRcList")
    int deleteMassRefRcList(@Param("rcids")List<String> rcids,
                            @Param("mid")String mid, @Param("mtype")String mtype);

    /**
     * 清理 关联
     * @param mid
     * @param mtype
     * @return
     */
    @DeleteProvider(type = CrtWhgMassProvider.class, method = "deleteMassRefRc4Mid")
    int deleteMassRefRc4Mid(@Param("mid")String mid, @Param("mtype")String mtype);




    /**
     * 查 关联的 艺术团队
     * @param recode
     * @return
     */
    @SelectProvider(type = CrtWhgMassProvider.class, method = "selectReftdList")
    List<Map> selectReftdList(Map recode);

    /**
     * 查可选的艺术团队
     * @param recode
     * @return
     */
    @SelectProvider(type = CrtWhgMassProvider.class, method = "selectTdList")
    List<Map> selectTdList(Map recode);


    /**
     * 插入 关联
     * @param list
     * @return
     */
    @InsertProvider(type = CrtWhgMassProvider.class, method = "insertMassRefTdList")
    int insertMassRefTdList(@Param("list") List<Map> list);

    /**
     * 移除 关联
     * @param tdids
     * @param mid
     * @param mtype
     * @return
     */
    @DeleteProvider(type = CrtWhgMassProvider.class, method = "deleteMassRefTdList")
    int deleteMassRefTdList(@Param("tdids")List<String> tdids,
                            @Param("mid")String mid, @Param("mtype")String mtype);

    /**
     * 清理 关联
     * @param mid
     * @param mtype
     * @return
     */
    @DeleteProvider(type = CrtWhgMassProvider.class, method = "deleteMassRefTd4Mid")
    int deleteMassRefTd4Mid(@Param("mid")String mid, @Param("mtype")String mtype);



    //TODO API Methods

    /**
     * api 查询关联资讯
     * @param mid
     * @return
     */
    @SelectProvider(type = CrtWhgMassProvider.class, method = "apiSelectMassZxlist")
    List<Map> apiSelectMassZxlist(@Param("mtype")String mtype, @Param("mid")String mid, @Param("cultid")String cultid);

    /**
     * api 查询届次关联人才
     * @param batchid
     * @return
     */
    @SelectProvider(type = CrtWhgMassProvider.class, method = "apiSelectBatchRclist")
    List<Map> apiSelectBatchRclist(@Param("batchid") String batchid);

    /**
     * api 查询届次关联团队
     * @param batchid
     * @return
     */
    @SelectProvider(type = CrtWhgMassProvider.class, method = "apiSelectBatchTdlist")
    List<Map> apiSelectBatchTdlist(@Param("batchid") String batchid);


    /**
     * api 查艺术人才
     * @param params
     * @return
     */
    @SelectProvider(type = CrtWhgMassProvider.class, method = "apiSelectMassArtistList")
    List<Map> apiSelectMassArtistList(Map params);


    /**
     * api 群文收藏查询
     * @param userid
     * @return
     */
    @SelectProvider(type = CrtWhgMassProvider.class, method = "apiSelectMassCollections")
    List<Map> apiSelectMassCollections(@Param("userid") String userid,
                                       @Param("cmreftyp") String cmreftyp);
}
