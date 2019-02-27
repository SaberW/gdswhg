package com.creatoo.hn.services.admin.feiyi.fyimanage;

import com.creatoo.hn.dao.mapper.WhgFyiExpertMapper;
import com.creatoo.hn.dao.model.WhgFyiExpert;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 非遗专家库service
 * Created by Administrator on 2017/10/31.
 */
@Service
public class WhgFyiExpertService extends BaseService{

    @Autowired
    private WhgFyiExpertMapper whgFyiExpertMapper;

    /**
     * 根据ID查询专家信息
     * @param id
     * @return
     */
    public Object t_srchOne(String id)throws Exception {
        return whgFyiExpertMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询专家库
     * @param page
     * @param rows
     * @param expert
     * @return
     */
    public PageInfo t_srchlist4p(int page, int rows, WhgFyiExpert expert) throws Exception{
        //搜索条件
        Example example = new Example(WhgFyiExpert.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(expert != null && expert.getName() != null){
            c.andLike("name", "%"+expert.getName()+"%");
            expert.setName(null);
        }
        if(expert!=null) {
            //其它条件
            c.andEqualTo(expert);
        }
        //排序
        example.setOrderByClause("statemdfdate desc");
        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgFyiExpert> list = this.whgFyiExpertMapper.selectByExample(example);
        return new PageInfo<WhgFyiExpert>(list);
    }

    /**
     * 添加专家
     * @param expert
     * @param sysUser
     */
    public void t_add(WhgFyiExpert expert, WhgSysUser sysUser) throws Exception{
        expert.setId(IDUtils.getID());
        expert.setCrtdate(new Date());
        expert.setStatemdfdate(new Date());
//        expert.setCrtuser(sysUser.getId());
        whgFyiExpertMapper.insertSelective(expert);
    }

    /**
     * 编辑专家
     * @param expert
     * @param sysUser
     */
    public void t_edit(WhgFyiExpert expert, WhgSysUser sysUser) throws Exception{
        expert.setStatemdfdate(new Date());
        expert.setStatemdfuser(sysUser.getId());
        whgFyiExpertMapper.updateByPrimaryKeySelective(expert);
    }

    /**
     * 删除专家
     * @param id
     */
    public void t_del(String id) throws Exception{
        whgFyiExpertMapper.deleteByPrimaryKey(id);
    }
}
