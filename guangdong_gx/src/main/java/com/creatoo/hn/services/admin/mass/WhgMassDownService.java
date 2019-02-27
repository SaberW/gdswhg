package com.creatoo.hn.services.admin.mass;

import com.creatoo.hn.dao.mapper.WhgMassDownMapper;
import com.creatoo.hn.dao.model.WhgMassDown;
import com.creatoo.hn.dao.model.WhgMassView;
import com.creatoo.hn.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;


@SuppressWarnings("ALL")
@Service
public class WhgMassDownService extends BaseService {

    @Autowired
    private WhgMassDownMapper whgMassDownMapper;


    /**
     * 添加
     * @param down
     * @throws Exception
     * */
    public void add(WhgMassDown down)throws Exception{
        this.whgMassDownMapper.insert(down);
    }

    /**
     * 根据ID查询数量
     * @param resid
     * @throws Exception
     * */
    public int seletByresId(String resid)throws Exception{
        Example example = new Example(WhgMassDown.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("resid",resid);
        return this.whgMassDownMapper.selectCountByExample(example);
    }
}
