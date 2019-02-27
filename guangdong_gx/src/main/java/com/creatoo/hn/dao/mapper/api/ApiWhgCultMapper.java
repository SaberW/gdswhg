package com.creatoo.hn.dao.mapper.api;

import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUserCult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/3/23.
 */
@SuppressWarnings("all")
public interface ApiWhgCultMapper extends Mapper<WhgSysCult> {



    /**
     * 活跃分管
     * @return
     */
    public List<Map> getActiveFG(@Param("param") Map param, @Param("protype") String protype);

    public List<Map> getWhglm(@Param("param") Map param, @Param("protype") String protype);

    /**
     * 排行榜指数
     * @return
     */
    public List<Map> queryActPhbList(@Param("param") Map param, @Param("protype") String protype);
}
