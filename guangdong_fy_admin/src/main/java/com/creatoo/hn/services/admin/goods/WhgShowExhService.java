package com.creatoo.hn.services.admin.goods;

import com.creatoo.hn.dao.mapper.WhgShowExhMapper;
import com.creatoo.hn.dao.model.WhgShowExh;
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
 * Created by Administrator on 2017/9/1.
 */
@Service
public class WhgShowExhService {

    @Autowired
    private WhgShowExhMapper whgShowExhMapper;

    /**
     * 根据id查询展演类商品信息
     * @param id
     * @return
     */
    public Object srchOne(String id) {
        return whgShowExhMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询展演类商品列表
     * @param page
     * @param rows
     * @param exh
     * @param sort
     * @param order
     * @return
     */
    public PageInfo t_srchlist4p(int page, int rows, WhgShowExh exh, String sort, String order) {
        Example exp = new Example(WhgShowExh.class);
        Example.Criteria ca = exp.createCriteria();

        if (exh!=null){
            if (exh.getTitle()!=null && !exh.getTitle().isEmpty()){
                ca.andLike("name", "%"+exh.getTitle()+"%");
                exh.setTitle(null);
            }
            ca.andEqualTo(exh);
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
        List list = this.whgShowExhMapper.selectByExample(exp);
        return new PageInfo(list);
    }

    /**
     * 添加展演类商品
     * @param exh
     * @param sysUser
     * @return
     */
    public ResponseBean t_add(WhgShowExh exh, WhgSysUser sysUser) {
        ResponseBean rsb = new ResponseBean();
        exh.setId(IDUtils.getID());
        exh.setCrtdate(new Date());
        exh.setCrtuser(sysUser.getId());
        exh.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        exh.setStatemdfuser(sysUser.getId());
        exh.setStatemdfdate(new Date());
        this.whgShowExhMapper.insert(exh);
        return rsb;
    }

    /**
     * 编辑商品
     * @param exh
     * @return
     */
    public ResponseBean t_edit(WhgShowExh exh) {
        ResponseBean rsb = new ResponseBean();
        this.whgShowExhMapper.updateByPrimaryKeySelective(exh);
        return rsb;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public ResponseBean t_del(String id) {
        ResponseBean resb = new ResponseBean();
        this.whgShowExhMapper.deleteByPrimaryKey(id);
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

        Example example = new Example(WhgShowExh.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );

        WhgShowExh exh = new WhgShowExh();
        exh.setState(tostate);
        if (optTime==null) {
            optTime = new Date();
        }
        exh.setStatemdfdate(optTime);
        exh.setStatemdfuser(sysUser.getId());
        this.whgShowExhMapper.updateByExampleSelective(exh, example);
        return resb;
    }

}
