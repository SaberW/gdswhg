package com.creatoo.hn.services.admin.feiyi.fyimanage;

import com.creatoo.hn.dao.mapper.WhgFyiFundMapper;
import com.creatoo.hn.dao.model.WhgFyiFund;
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
 * 专项资金service
 * Created by Administrator on 2017/11/3.
 */
@Service
public class WhgFyiFundService extends BaseService{

    @Autowired
    private WhgFyiFundMapper whgFyiFundMapper;

    /**
     * 根据id查询专项资金
     * @param id
     * @return
     */
    public Object srchOne(String id){
        return whgFyiFundMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询专项资金
     * @param page
     * @param rows
     * @param fund
     * @return
     */
    public PageInfo t_srchlist4p(int page, int rows, WhgFyiFund fund) throws Exception{
        Example example = new Example(WhgFyiFund.class);
        Example.Criteria c = example.createCriteria();
        //
        if(fund != null && fund.getFundnum() != null){
            c.andLike("fundnum", "%"+fund.getFundnum()+"%");
            fund.setFundnum(null);
        }
        if(fund != null && fund.getMatter() != null){
            c.andLike("matter", "%"+fund.getMatter()+"%");
            fund.setMatter(null);
        }
        if(fund!=null) {
            //其它条件
            c.andEqualTo(fund);
        }
        //排序
        example.setOrderByClause("statemdfdate desc");
        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgFyiFund> list = this.whgFyiFundMapper.selectByExample(example);
        return new PageInfo<WhgFyiFund>(list);
    }

    /**
     * 添加专项资金
     * @param fund
     * @param sysUser
     */
    public void t_add(WhgFyiFund fund, WhgSysUser sysUser) throws Exception{
        Date now = new Date();
        fund.setId(IDUtils.getID());
        fund.setCrtdate(now);
        fund.setStatemdfdate(now);
        whgFyiFundMapper.insertSelective(fund);
    }

    /**
     * 编辑专项资金
     * @param fund
     * @param sysUser
     */
    public void t_edit(WhgFyiFund fund, WhgSysUser sysUser) {
        fund.setStatemdfdate(new Date());
        whgFyiFundMapper.updateByPrimaryKeySelective(fund);
    }

    /**
     * 删除专项资金
     * @param id
     */
    public void t_del(String id) {
        whgFyiFundMapper.deleteByPrimaryKey(id);
    }
}
