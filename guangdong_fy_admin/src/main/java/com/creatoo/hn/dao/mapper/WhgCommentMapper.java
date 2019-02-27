package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgComment;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface WhgCommentMapper extends Mapper<WhgComment> {
    /**
     * 我的活动点评查询
     * @param rmuid
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<HashMap> selectMyActComm(String rmuid);

    /**
     * 我的培训点评查询
     * @param rmuid
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<HashMap> selectMyTraitmComm(String rmuid);

    /**
     * 后代查询评论
     * @param param
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<Map> selectdisList(Map<String,Object> param);


    /**
     * 查询评论
     * @param param 条件，至少需要reftyp和refid
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<HashMap> searchComment(Map<String, Object> param);

    /** 查看回复
     * @param rmids
     * @return
     */
    public List<Object> searchCommentHuifu(@Param("rmids")List<String> rmids);


    /**个人中心查询评论
     * @param rmreftyp
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<HashMap> searchMyComment(@Param("rmuid")String rmuid,@Param("rmreftyp")String rmreftyp);

    /**个人中心  查询评论回复
     * @param idArray
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<HashMap> searchMyCommentRetry(@Param("idArray")java.util.ArrayList idArray);
    /**
     * 评论后台查询评论
     * @param param
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<Map> selecthdComment(Map<String ,Object> param);
    /**
     * 评论后台查询回复 关联管理员名字
     * @param param
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<Map> selecthdCommentHF(Map<String ,Object> param);

    /**
     * 评论后台查询评论(根据模块查询)
     * @param param
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<Map> selecthdCommentByType(Map<String ,Object> param);

    /**
     * 查询评论列表
     * @param param
     * @return
     */
    public List<Map> selectCommentWithUser(Map<String,Object> param);

    public List<WhgComment> getUserComment(@Param("whgComment") WhgComment whComment);
}