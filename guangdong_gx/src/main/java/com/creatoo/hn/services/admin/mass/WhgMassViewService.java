package com.creatoo.hn.services.admin.mass;


import com.creatoo.hn.dao.mapper.WhgMassViewMapper;
import com.creatoo.hn.dao.model.WhgMassView;
import com.creatoo.hn.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@SuppressWarnings("ALL")
@Service
public class WhgMassViewService extends BaseService {

    @Autowired
    private WhgMassViewMapper whgMassViewMapper;


    /**
     * 添加
     * @param view
     * @throws Exception
     */
    public void add(WhgMassView view) throws Exception{
        this.whgMassViewMapper.insert(view);
    }

    /**
     * 查询数量
     * @param resid
     * @throws Exception
     */
    public int selectCountByResId(String resid)throws Exception{
        Example example = new Example(WhgMassView.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("resid",resid);
        return this.whgMassViewMapper.selectCountByExample(example);
    }

}
