package com.creatoo.hn.dao.mapper.api;

import java.util.List;
import java.util.Map;

public interface ApiInsSupplyMapper {

    /**
     * 查询供需信息列表
     * @param recode
     * @return
     */
    List selectSupplyList(Map recode);

    /**
     * 统计供需数量
     * @param recode
     * @return
     */
    int countSupplyList(Map recode);

    /**
     * 查询供给信息列表
     * @param recode
     * @return
     */
    List selectFkProjectList(Map recode);

    /**
     * 统计供给数量
     * @param recode
     * @return
     */
    int countFkProjectList(Map recode);
}
