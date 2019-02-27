package com.creatoo.hn.services.admin.mass;

import com.creatoo.hn.dao.mapper.WhgMassResourceMapper;
import com.creatoo.hn.dao.model.WhgMassResource;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by rbg on 2017/11/3.
 */
@SuppressWarnings("ALL")
@Service
public class WhgMassResourceService extends BaseService {
    
    @Autowired
    private WhgMassResourceMapper whgMassResourceMapper;

    /**
     * 主键查找艺术人才
     * @param id
     * @return
     * @throws Exception
     */
    public WhgMassResource srchOne(String id) throws Exception {
        return this.whgMassResourceMapper.selectByPrimaryKey(id);
    }

    /**
     * 后台管理列表分页查询艺术人才
     * @param page
     * @param pageSize
     * @param recode
     * @param states
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public PageInfo srch4p(int page, int pageSize, WhgMassResource recode,
                           List states, String sort, String order) throws Exception {
        if (recode == null) {
            throw new Exception("param recode is null");
        }
        Example exp = new Example(recode.getClass());
        Example.Criteria c = exp.createCriteria();
        if (recode.getTitle() != null && !recode.getTitle().isEmpty()) {
            c.andLike("name", "%"+recode.getTitle()+"%");
            recode.setTitle(null);
        }

        c.andEqualTo(recode);

        if (states != null && states.size() > 0) {
            c.andIn("state", states);
        }

        if (sort!=null && !sort.isEmpty()){
            if (order != null && order.equalsIgnoreCase("desc")) {
                exp.orderBy(sort).desc();
            }else {
                exp.orderBy(sort).asc();
            }
        }else {
            exp.orderBy("crtdate").desc();
        }

        PageHelper.startPage(page, pageSize);
        List<WhgMassResource> list = this.whgMassResourceMapper.selectByExample(exp);

        return new PageInfo(list);
    }

    /**
     * 添加
     * @param info
     * @throws Exception
     */
    public void t_add(WhgMassResource info) throws Exception{
        this.whgMassResourceMapper.insert(info);
    }

    /**
     * 编辑
     * @param info
     * @throws Exception
     */
    public void t_edit(WhgMassResource info) throws Exception {
        this.whgMassResourceMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 修改state
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUserId
     * @param optTime
     * @throws Exception
     */
    public void t_updstate(String ids, String formstates, int tostate, String sysUserId, Date optTime) throws Exception{
        if (ids == null || formstates==null || formstates.isEmpty()){
            throw new Exception("params error");
        }
        Example example = new Example(WhgMassResource.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgMassResource info = new WhgMassResource();
        info.setState(tostate);
        if (optTime==null) optTime = new Date();
        info.setStatemdfdate(optTime);
        info.setStatemdfuser(sysUserId);
        this.whgMassResourceMapper.updateByExampleSelective(info, example);
    }

    /**
     * 删除
     * @param id
     * @throws Exception
     */
    public void t_del(String id) throws Exception{
        WhgMassResource info = this.whgMassResourceMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        if (info.getDelstate()!=null && info.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgMassResourceMapper.deleteByPrimaryKey(id);
        }else {
            info = new WhgMassResource();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            this.whgMassResourceMapper.updateByPrimaryKeySelective(info);
        }
    }

    /**
     * 还原
     * @param id
     * @throws Exception
     */
    public void t_undel(String id) throws Exception{
        WhgMassResource info = this.whgMassResourceMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        info = new WhgMassResource();
        info.setId(id);
        info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        this.whgMassResourceMapper.updateByPrimaryKeySelective(info);
    }
}
