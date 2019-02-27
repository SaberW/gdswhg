package com.creatoo.hn.dao.mapper.mass;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资源库
 * Created by wangxl on 2017/11/11.
 */
public interface CrtWhgMassLibraryMapper {
    /**
     * 统计指定表中的数据
     * @param tableName 表名
     * @return
     */
    int countByTableName(@Param("tableName") String tableName);

    int countByTableNameExample(@Param("tableName") String tableName, @Param("example") Map example);

    Map countByTableNameExample1(@Param("tableName") String tableName, @Param("example") Map example);

    List<Map> countByTop(@Param("tablenames")List<String> tablenames);

    /**
     * 删除表
     * @param tableName
     */
    void dropByTableName(@Param("tableName") String tableName);

    /**
     * 创建资源库
     * @param tablename
     * @param tablecomment
     * @param fields
     */
    void createTable(@Param("tablename") String tablename,
                     @Param("tablecomment") String tablecomment,
                     @Param("fields") List<Map<String, String>> fields);

    /**
     * 添加资源库字段
     * @param tablename
     * @param fields
     */
    void alertTableAddField(@Param("tablename") String tablename,@Param("fields") List<Map<String, String>> fields);

    /**
     * 修改资源库字段
     * @param tablename
     * @param fields
     */
    void alertTableModifyField(@Param("tablename") String tablename,@Param("fields") List<Map<String, String>> fields);

    /**
     * 删除资源库字段
     * @param tablename
     * @param fieldname
     */
    void alertTableDropField(@Param("tablename") String tablename, @Param("fieldname") String fieldname);

    /**
     * 查询群文库资源
     * @param tablename 群文库资源表名
     * @param condition 查询条件
     * @param sort 排序字段
     * @param order 排序值
     * @return
     */
    List<Map> selectResource(@Param("tablename")String tablename, @Param("condition")List<Map<String, Object>> condition, @Param("sort")String sort, @Param("order")String order);

    List<Map> selectResource2(@Param("tablenames")List<String> tablenames, @Param("condition")List<Map<String, Object>> condition, @Param("sort")String sort, @Param("order")String order);


    /**
     * 添加群文库资源
     * @param tablename 群文库资源表名
     * @param fields 表字段
     * @param values 表值
     * @return
     */
    int insertResource(@Param("tablename")String tablename, @Param("fields")List<String> fields, @Param("values")List<Object> values);

    /**
     * 根据ID更新群文资源库
     * @param tablename 表名
     * @param id 唯一标识
     * @param fields 更新的字段和值
     */
    int updateResource(@Param("tablename")String tablename, @Param("id")String id, @Param("fields")List<Map<String, Object>> fields);

    /**
     * 删除资源
     * @param tablename 表名
     * @param id 唯一标识
     * @return
     */
    int deleteResource(@Param("tablename")String tablename, @Param("id")String id, @Param("refid")String refid);

    /**
     * 全局搜索资源
     * @param tablenames 资源库对应的表
     * @param srchkey 搜索关键字
     * @return
     */
    List<Map> globalFindResource(@Param("tablenames")List<String> tablenames, @Param("srchkey")String srchkey, @Param("restypes") List<String> restypes, @Param("resid") String resid,
                                 @Param("cultids") List<String> cultids,@Param("resarttype") String resarttype);

    /**
     * 全局搜索资源
     * @param tablenames 资源库对应的表
     * @param srchkey 搜索关键字
     * @return
     */
    List<Map> apiGlobalFindResource(@Param("tablenames")List<String> tablenames, @Param("srchkey")String srchkey,
                                 @Param("restypes") List<String> restypes, @Param("resid") String resid,
                                 @Param("cultids") List<String> cultids,@Param("resarttype") String resarttype,
                                 @Param("userid") String userid);
}
