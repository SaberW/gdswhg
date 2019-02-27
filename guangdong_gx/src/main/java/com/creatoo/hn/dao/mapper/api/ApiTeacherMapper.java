package com.creatoo.hn.dao.mapper.api;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/10.
 */
public interface ApiTeacherMapper {

    /**
     * 查询培训师资列表页
     * @param recode
     * @return
     */
    List selectTeaList4page(Map recode);
}
