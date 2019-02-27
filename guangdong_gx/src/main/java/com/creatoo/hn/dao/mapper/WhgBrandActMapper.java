package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgBrandAct;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WhgBrandActMapper extends Mapper<WhgBrandAct> {
    /**
     * 查找品牌活动
     * @param param
     * @return
     */
    List<Map> selBrandAct(Map<String, Object> param);
    /**
     * 查找品牌活动中所有的活动
     * @param braid
     * @return
     */
    List<Map> selBrandinfo(String braid);
    /**
     * 查找活动资讯
     * @param braid
     * @return
     */
    List<Map> selZX();

    List<Map> selectAct();

    List<Map> isPublish(String braid);
}