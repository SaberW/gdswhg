package com.creatoo.hn.services.admin.goods;

import com.creatoo.hn.dao.mapper.WhgExhGoodsMapper;
import com.creatoo.hn.dao.model.WhgExhGoods;
import com.creatoo.hn.dao.model.WhgShowGoods;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
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
 * Created by Administrator on 2017/9/1.
 */
@SuppressWarnings("ALL")
@Service
public class WhgExhGoodsService extends BaseService{

    @Autowired
    private WhgExhGoodsMapper whgExhGoodsMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 根据id查询展演类商品信息
     * @param id
     * @return
     */
    public Object srchOne(String id) {
        return whgExhGoodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询展演类商品列表
     * @param page
     * @param rows
     * @param exhGoods
     * @param sort
     * @param order
     * @return
     */
    public PageInfo t_srchlist4p(int page, int rows, String userId,WhgExhGoods exhGoods, List states, String sort, String order) throws Exception {
        Example exp = new Example(WhgExhGoods.class);
        Example.Criteria ca = exp.createCriteria();

        if (exhGoods!=null){
            if (exhGoods.getTitle()!=null && !exhGoods.getTitle().isEmpty()){
                ca.andLike("title", "%"+exhGoods.getTitle()+"%");
                exhGoods.setTitle(null);
            }
            if (exhGoods.getCultid() == null || exhGoods.getCultid().isEmpty()) {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(userId);
                if (cultids!=null && cultids.size()>0){
                    ca.andIn("cultid",cultids);
                    exhGoods.setCultid(null);
                }
            }
            if (exhGoods.getDeptid() == null || exhGoods.getCultid().isEmpty()) {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(userId);
                if (deptids != null && deptids.size() > 0) {
                    ca.andIn("deptid",deptids);
                    exhGoods.setDeptid(null);
                }
            }
            ca.andEqualTo(exhGoods);
        }

        if (states != null && states.size() > 0) {
            ca.andIn("state", states);
        }

        if (sort!=null && !sort.isEmpty()){
            Example.OrderBy orderBy = exp.orderBy(sort);
            if (order!=null && "desc".equalsIgnoreCase(order)){
                orderBy.desc();
            }
        }else{
            exp.orderBy("crtdate").desc();
        }
        PageHelper.startPage(page, rows);
        List list = this.whgExhGoodsMapper.selectByExample(exp);
        return new PageInfo(list);
    }

    /**
     * 添加展演类商品
     * @param exhGoods
     * @param sysUser
     * @return
     */
    public void t_add(WhgExhGoods exhGoods, WhgSysUser sysUser) throws Exception{
        exhGoods.setId(IDUtils.getID());
        exhGoods.setCrtdate(new Date());
        exhGoods.setCrtuser(sysUser.getId());
        exhGoods.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        exhGoods.setStatemdfuser(sysUser.getId());
        exhGoods.setStatemdfdate(new Date());
        exhGoods.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        this.whgExhGoodsMapper.insert(exhGoods);
    }

    /**
     * 编辑商品
     * @param exhGoods
     * @return
     */
    public void t_edit(WhgExhGoods exhGoods) throws Exception{
        this.whgExhGoodsMapper.updateByPrimaryKeySelective(exhGoods);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public void t_del(String id) throws Exception{
        this.whgExhGoodsMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUser
     * @return
     */
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser sysUser,Date optTime) throws Exception{
        ResponseBean resb = new ResponseBean();
        if (ids == null){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识丢失");
            return resb;
        }

        Example example = new Example(WhgExhGoods.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );

        WhgExhGoods exhGoods = new WhgExhGoods();
        exhGoods.setState(tostate);
        if (optTime==null) {
            optTime = new Date();
        }
        exhGoods.setStatemdfdate(optTime);
        exhGoods.setStatemdfuser(sysUser.getId());

        if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()){
            exhGoods.setCheckor(sysUser.getId());
            exhGoods.setCheckdate(new Date());
        }
        if (tostate == EnumBizState.STATE_PUB.getValue()){
            exhGoods.setPublisher(sysUser.getId());
            exhGoods.setPublishdate(new Date());
        }

        this.whgExhGoodsMapper.updateByExampleSelective(exhGoods, example);
        return resb;
    }

    /**
     *查询展品列表
     * @return
     */
    public List<WhgExhGoods> t_srchList(String cultid,WhgSysUser sysUser) throws Exception{
        Example example = new Example(WhgExhGoods.class);
        Example.Criteria c = example.createCriteria();
        if (cultid== null || cultid.isEmpty()) {
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
                if (cultids!=null && cultids.size()>0){
                    c.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        c.andEqualTo("cultid",cultid);
        c.andEqualTo("state",6);
        c.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        List<WhgExhGoods> list = this.whgExhGoodsMapper.selectByExample(example);
        return list;
    }
}
