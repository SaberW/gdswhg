package com.creatoo.hn.services.admin.personnel;

import com.creatoo.hn.dao.mapper.WhgPersonnelAwardsMapper;
import com.creatoo.hn.dao.mapper.WhgShowPlaybillMapper;
import com.creatoo.hn.dao.model.WhgPersonnelAwards;
import com.creatoo.hn.dao.model.WhgShowPlaybill;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */
@Service
public class WhgAwardsService {

    @Autowired
    private WhgPersonnelAwardsMapper whgPersonnelAwardsMapper;

    /**
     * 根据ID查询节目单
     * @param id
     * @return
     */
    public WhgPersonnelAwards t_srchOne(String id) throws Exception{
        return whgPersonnelAwardsMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询
     * @param page
     * @param rows
     * @param awards
     * @return
     */
    public PageInfo<WhgPersonnelAwards> t_srchList4p(int page, int rows, WhgPersonnelAwards awards) throws Exception{
        //开始分页

        Example example = new Example(WhgShowPlaybill.class);
        Example.Criteria c = example.createCriteria();

        if (awards.getEntid() != null && !"".equals(awards.getEntid())) {
            c.andEqualTo("entid", awards.getEntid());
        }
        if(awards.getTitle() != null && !"".equals(awards.getTitle())){
            c.andLike("title","%"+awards.getTitle()+"%");
        }
        example.setOrderByClause("crtdate asc");
        PageHelper.startPage(page, rows);
        List<WhgPersonnelAwards> typeList = this.whgPersonnelAwardsMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 添加节目单
     * @param awards
     * @param sysUser
     */
    public void t_add(WhgPersonnelAwards awards, WhgSysUser sysUser) throws Exception{
        awards.setId(IDUtils.getID());
        awards.setCrtdate(new Date());
        awards.setCrtuser(sysUser.getId());
        whgPersonnelAwardsMapper.insertSelective(awards);
    }

    /**
     * 更新节目单信息
     * @param awards
     * @param sysUser
     */
    public void t_edit(WhgPersonnelAwards awards, WhgSysUser sysUser) throws Exception{
        whgPersonnelAwardsMapper.updateByPrimaryKeySelective(awards);
    }

    /**
     * 删除节目单
     * @param id
     */
    public void t_del(String id) throws Exception{
        whgPersonnelAwardsMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询节目单
     * @param page
     * @param pageSize
     * @param entid
     * @return
     */
    public ApiResultBean t_getJmd(int page, int pageSize, String entid) throws Exception {
        ApiResultBean arb = new ApiResultBean();
        Example example = new Example(WhgShowPlaybill.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("entid",entid);
        List list = whgPersonnelAwardsMapper.selectByExample(example);
        PageInfo info = new PageInfo(list);
        if(list != null && list.size() > 0){
            arb.setRows(info.getList());
            arb.setPageInfo(info);
        }
        return arb;
    }

    /**
     * 根据实体ID查询获奖情况
     * @param page
     * @param pageSize
     * @param entid
     * @return
     */
    public ApiResultBean t_getAwards(int page, int pageSize, String entid) {
        ApiResultBean arb = new ApiResultBean();
        Example example = new Example(WhgPersonnelAwards.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("entid",entid);
        example.setOrderByClause("issuedate desc");
        List list = whgPersonnelAwardsMapper.selectByExample(example);
        PageInfo info = new PageInfo(list);
        if(list != null && list.size() > 0){
            arb.setRows(info.getList());
            arb.setPageInfo(info);
        }
        return arb;
    }
}
