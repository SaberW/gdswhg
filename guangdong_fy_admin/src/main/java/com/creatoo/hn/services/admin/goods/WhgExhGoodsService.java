package com.creatoo.hn.services.admin.goods;

import com.creatoo.hn.dao.mapper.WhgExhGoodsMapper;
import com.creatoo.hn.dao.model.WhgExhGoods;
import com.creatoo.hn.dao.model.WhgShowGoods;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */
@Service
public class WhgExhGoodsService extends BaseService{

    @Autowired
    private WhgExhGoodsMapper whgExhGoodsMapper;

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
    public PageInfo t_srchlist4p(int page, int rows, WhgExhGoods exhGoods, String sort, String order) {
        Example exp = new Example(WhgShowGoods.class);
        Example.Criteria ca = exp.createCriteria();

        if (exhGoods!=null){
            if (exhGoods.getTitle()!=null && !exhGoods.getTitle().isEmpty()){
                ca.andLike("title", "%"+exhGoods.getTitle()+"%");
                exhGoods.setTitle(null);
            }
            ca.andEqualTo(exhGoods);
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
    public ResponseBean t_add(WhgExhGoods exhGoods, WhgSysUser sysUser) {
        ResponseBean rsb = new ResponseBean();
        exhGoods.setId(IDUtils.getID());
        exhGoods.setCrtdate(new Date());
        exhGoods.setCrtuser(sysUser.getId());
        exhGoods.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        exhGoods.setStatemdfuser(sysUser.getId());
        exhGoods.setStatemdfdate(new Date());
        this.whgExhGoodsMapper.insert(exhGoods);
        return rsb;
    }

    /**
     * 编辑商品
     * @param exhGoods
     * @return
     */
    public ResponseBean t_edit(WhgExhGoods exhGoods) {
        ResponseBean rsb = new ResponseBean();
        this.whgExhGoodsMapper.updateByPrimaryKeySelective(exhGoods);
        return rsb;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public ResponseBean t_del(String id) {
        ResponseBean resb = new ResponseBean();
        this.whgExhGoodsMapper.deleteByPrimaryKey(id);
        return resb;
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUser
     * @return
     */
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser sysUser,Date optTime) {
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
        this.whgExhGoodsMapper.updateByExampleSelective(exhGoods, example);
        return resb;
    }

    /**
     *查询展品列表
     * @return
     */
    public List<WhgExhGoods> t_srchList(String cultid) throws Exception{
        Example example = new Example(WhgExhGoods.class);
        Example.Criteria c = example.createCriteria();
//        if(cultids != null && cultids.size() != 0){
//            c.andIn("cultid",cultids);
//        }
        c.andEqualTo("cultid",cultid);
        c.andEqualTo("state",6);
        List<WhgExhGoods> list = this.whgExhGoodsMapper.selectByExample(example);
        return list;
    }
}
