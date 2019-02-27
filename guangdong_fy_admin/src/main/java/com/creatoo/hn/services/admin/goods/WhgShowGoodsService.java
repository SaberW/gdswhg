package com.creatoo.hn.services.admin.goods;

import com.creatoo.hn.dao.mapper.WhgShowGoodsMapper;
import com.creatoo.hn.dao.model.WhgGoods;
import com.creatoo.hn.dao.model.WhgShowGoods;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */
@Service
public class WhgShowGoodsService {

    @Autowired
    private WhgShowGoodsMapper whgShowGoodsMapper;

    /**
     * 根据id查询展演类商品信息
     * @param id
     * @return
     */
    public Object srchOne(String id) {
        return whgShowGoodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询展演类商品列表
     * @param page
     * @param rows
     * @param show
     * @param sort
     * @param order
     * @return
     */
    public PageInfo t_srchlist4p(int page, int rows, WhgShowGoods show, String sort, String order) {
        Example exp = new Example(WhgShowGoods.class);
        Example.Criteria ca = exp.createCriteria();

        if (show!=null){
            if (show.getTitle()!=null && !show.getTitle().isEmpty()){
                ca.andLike("title", "%"+show.getTitle()+"%");
                show.setTitle(null);
            }
            ca.andEqualTo(show);
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
        List list = this.whgShowGoodsMapper.selectByExample(exp);
        return new PageInfo(list);
    }

    /**
     * 添加展演类商品
     * @param show
     * @param sysUser
     * @return
     */
    public ResponseBean t_add(WhgShowGoods show, WhgSysUser sysUser) {
        ResponseBean rsb = new ResponseBean();
        show.setId(IDUtils.getID());
        show.setCrtdate(new Date());
        show.setCrtuser(sysUser.getId());
        show.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        show.setStatemdfuser(sysUser.getId());
        show.setStatemdfdate(new Date());
        this.whgShowGoodsMapper.insert(show);
        return rsb;
    }

    /**
     * 编辑商品
     * @param show
     * @return
     */
    public ResponseBean t_edit(WhgShowGoods show) {
        ResponseBean rsb = new ResponseBean();
        this.whgShowGoodsMapper.updateByPrimaryKeySelective(show);
        return rsb;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public ResponseBean t_del(String id) {
        ResponseBean resb = new ResponseBean();
        this.whgShowGoodsMapper.deleteByPrimaryKey(id);
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

        Example example = new Example(WhgShowGoods.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );

        WhgShowGoods show = new WhgShowGoods();
        show.setState(tostate);
        if (optTime==null) {
            optTime = new Date();
        }
        show.setStatemdfdate(optTime);
        show.setStatemdfuser(sysUser.getId());
        this.whgShowGoodsMapper.updateByExampleSelective(show, example);
        return resb;
    }

}
