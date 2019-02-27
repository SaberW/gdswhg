package com.creatoo.hn.services.admin.supply;

import com.creatoo.hn.dao.mapper.WhgSupplyTrapersonMapper;
import com.creatoo.hn.dao.mapper.WhgYwiHardwareMapper;
import com.creatoo.hn.dao.model.WhgSupplyTraperson;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiHardware;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */
@Service
public class WhgSupplyTraPerService extends BaseService{

    /**
     * 关键字mapper
     */
    @Autowired
    private WhgSupplyTrapersonMapper whgSupplyTrapersonMapper;


    /**
     * 分页查询培训人员列表信息
     * @param
     */
    public PageInfo<WhgSupplyTraperson> t_srchList4p(int page, int rows, WhgSupplyTraperson per) throws Exception {
        //开始分页
        PageHelper.startPage(page, rows);
        Example example = new Example(WhgSupplyTraperson.class);
        Example.Criteria c = example.createCriteria();

        if (per.getEntid() != null && !"".equals(per.getEntid())) {
            c.andEqualTo("entid", per.getEntid());
        }
        if (per.getCultid() != null && !"".equals(per.getCultid())) {
            c.andEqualTo("cultid", per.getCultid());
        }
        if(per.getName() != null && !"".equals(per.getName())){
            c.andLike("name","%"+per.getName()+"%");
        }
        c.andEqualTo("state",1);
        example.setOrderByClause("statemdfdate asc");
        List<WhgSupplyTraperson> typeList = this.whgSupplyTrapersonMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 添加培训人员
     * @param
     */
    public void t_add(WhgSupplyTraperson per, WhgSysUser sysUser)throws Exception {
        per.setId(IDUtils.getID());
        per.setState(1);
        per.setStatemdfdate(new Date());
        per.setCrtdate(new Date());
        per.setCrtuser(sysUser.getId());
        per.setStatemdfuser(sysUser.getId());
        int result = this.whgSupplyTrapersonMapper.insertSelective(per);
        if(result != 1){
            throw new Exception("添加数据失败！");
        }

    }

    /**
     * 编辑培训人员
     * @param
     */
    public void t_edit(WhgSupplyTraperson per, WhgSysUser sysUser)throws Exception {
        per.setStatemdfdate(new Date());
        per.setStatemdfuser(sysUser.getId());
        int result = this.whgSupplyTrapersonMapper.updateByPrimaryKeySelective(per);
        if(result != 1){
            throw new Exception("编辑数据失败！");
        }
    }

    /**
     * 删除培训人员信息
     * @param
     * @throws Exception
     */
    public void t_del(String id) throws Exception {
        int result = this.whgSupplyTrapersonMapper.deleteByPrimaryKey(id);
        if(result != 1){
            throw new Exception("删除数据失败！");
        }
    }

    /**
     * 查询培训人员
     * @param id
     * @return
     */
    public WhgSupplyTraperson t_srchOne(String id) {
        return whgSupplyTrapersonMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询培训人员
     * @param page
     * @param pageSize
     * @param entid
     * @return
     */
    public ApiResultBean t_getPxry(int page, int pageSize, String entid) {
        ApiResultBean arb = new ApiResultBean();
        Example example = new Example(WhgSupplyTraperson.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("entid",entid);
        List list = whgSupplyTrapersonMapper.selectByExample(example);
        PageInfo info = new PageInfo(list);
        if(list != null && list.size() > 0){
            arb.setRows(info.getList());
            arb.setPageInfo(info);
        }
        return arb;
    }
}
