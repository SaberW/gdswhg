package com.creatoo.hn.dao.mapper.aliyunsms;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public interface AliyunSmsMapper {

    /**
     * 查询指定短信组的相关短信模板列表
     * @param groupid
     * @return
     */
    @SelectProvider(type = AliyunSmsProvider.class, method="selectAliySmsRefgc4Groupid")
    List<Map> selectAliySmsRefgc4Groupid(@Param("groupid") String groupid);

    /**
     * 查询相关业务引用的短信组信息
     * @param record
     * @return
     */
    @SelectProvider(type = AliyunSmsProvider.class, method="selectAliySmsRefuseGroup")
    List<Map> selectAliySmsRefuseGroup(Map record);
}
