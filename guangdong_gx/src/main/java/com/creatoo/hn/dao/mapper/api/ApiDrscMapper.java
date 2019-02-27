package com.creatoo.hn.dao.mapper.api;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/10.
 */
public interface ApiDrscMapper {

    /**
     * 查询培训资源列表
     * @param recode
     * @return
     */
    List selectDrscList4page(Map recode);
}
