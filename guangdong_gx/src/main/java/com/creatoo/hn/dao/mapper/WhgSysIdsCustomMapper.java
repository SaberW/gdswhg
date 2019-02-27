package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgSysIds;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 批量保存随机数的DAO
 * Created by wangxl on 2017/8/22.
 */
@Transactional
public interface WhgSysIdsCustomMapper  extends Mapper<WhgSysIds> {
    /**
     * 批量WhgSysIds表
     * @param whgSysIdsList
     */
    public void insertByBatch(List<WhgSysIds> whgSysIdsList);


}
