package com.creatoo.hn.services.admin.goods;

import com.creatoo.hn.dao.mapper.WhgShowPlaybillMapper;
import com.creatoo.hn.dao.model.WhgShowPlaybill;
import com.creatoo.hn.dao.model.WhgSupplyTraperson;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiHardware;
import com.creatoo.hn.services.BaseService;
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
 * Created by Administrator on 2017/11/23.
 */
@Service
public class WhgShowJmdService extends BaseService{

    @Autowired
    private WhgShowPlaybillMapper whgShowPlaybillMapper;

    /**
     * 根据ID查询节目单
     * @param id
     * @return
     */
    public WhgShowPlaybill t_srchOne(String id) throws Exception{
        return whgShowPlaybillMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询
     * @param page
     * @param rows
     * @param play
     * @return
     */
    public PageInfo<WhgShowPlaybill> t_srchList4p(int page, int rows, WhgShowPlaybill play) throws Exception{
        //开始分页

        Example example = new Example(WhgShowPlaybill.class);
        Example.Criteria c = example.createCriteria();

        if (play.getEntid() != null && !"".equals(play.getEntid())) {
            c.andEqualTo("entid", play.getEntid());
        }
        if (play.getCultid() != null && !"".equals(play.getCultid())) {
            c.andEqualTo("cultid", play.getCultid());
        }
        if(play.getTitle() != null && !"".equals(play.getTitle())){
            c.andLike("title","%"+play.getTitle()+"%");
        }
        example.setOrderByClause("crtdate asc");
        PageHelper.startPage(page, rows);
        List<WhgShowPlaybill> typeList = this.whgShowPlaybillMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 添加节目单
     * @param play
     * @param sysUser
     */
    public void t_add(WhgShowPlaybill play, WhgSysUser sysUser) throws Exception{
        play.setId(IDUtils.getID());
        play.setCrtdate(new Date());
        play.setCrtuser(sysUser.getId());
        whgShowPlaybillMapper.insertSelective(play);
    }

    /**
     * 更新节目单信息
     * @param play
     * @param sysUser
     */
    public void t_edit(WhgShowPlaybill play, WhgSysUser sysUser) throws Exception{
        whgShowPlaybillMapper.updateByPrimaryKeySelective(play);
    }

    /**
     * 删除节目单
     * @param id
     */
    public void t_del(String id) throws Exception{
        whgShowPlaybillMapper.deleteByPrimaryKey(id);
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
        //节目单排序错误，需按创建时间进行排序
        example.setOrderByClause("crtdate asc");
        List list = whgShowPlaybillMapper.selectByExample(example);
        PageInfo info = new PageInfo(list);
        if(list != null && list.size() > 0){
            arb.setRows(info.getList());
            arb.setPageInfo(info);
        }
        return arb;
    }
}
