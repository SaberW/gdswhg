package com.creatoo.hn.services.admin.goods;

import com.creatoo.hn.dao.mapper.WhgGoodsMapper;
import com.creatoo.hn.dao.model.WhgGoods;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by rbg on 2017/8/3.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgGoods", keyGenerator = "simpleKeyGenerator")
public class WhgGoodsService extends BaseService {

    @Autowired
    private WhgGoodsMapper whgGoodsMapper;

    @Autowired
    private WhgFkProjectService whgFkProjectService;

    /**
     * 主键查找
     * @param id
     * @return
     */
    @Cacheable
    public WhgGoods srchOne(String id){
        return this.whgGoodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询
     * @param page
     * @param rows
     * @param goods
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    @Cacheable
    public PageInfo srchlist4p(int page, int rows, WhgGoods goods, String sort, String order) throws Exception{
        Example exp = new Example(WhgGoods.class);
        Example.Criteria ca = exp.createCriteria();

        if (goods!=null){
            if (goods.getName()!=null && !goods.getName().isEmpty()){
                ca.andLike("name", "%"+goods.getName()+"%");
                goods.setName(null);
            }
            if (goods.getContacts()!=null && !goods.getContacts().isEmpty()){
                ca.andLike("contacts", "%"+goods.getContacts()+"%");
                goods.setContacts(null);
            }
            if (goods.getPhone()!=null && !goods.getPhone().isEmpty()){
                ca.andLike("phone", "%"+goods.getPhone()+"%");
                goods.setPhone(null);
            }

            ca.andEqualTo(goods);
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
        List list = this.whgGoodsMapper.selectByExample(exp);

        return new PageInfo(list);
    }

    /**
     * 添加
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_add(WhgGoods goods, WhgSysUser sysUser) throws Exception{
        ResponseBean resb = new ResponseBean();

        goods.setId(IDUtils.getID());
        goods.setCrtdate(new Date());
        goods.setCrtuser(sysUser.getId());
        goods.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        goods.setStatemdfuser(sysUser.getId());
        goods.setStatemdfdate(new Date());

        this.whgGoodsMapper.insert(goods);

        return resb;
    }

    /**
     * 编辑
     * @param goods
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_edit(WhgGoods goods) throws Exception{
        ResponseBean resb = new ResponseBean();
        this.whgGoodsMapper.updateByPrimaryKeySelective(goods);
        return resb;
    }

    /**
     * 删除
     * @param id
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_del(String id) throws Exception{
        ResponseBean resb = new ResponseBean();
        this.whgGoodsMapper.deleteByPrimaryKey(id);
        //删除fk表中对应的数据
        this.whgFkProjectService.delByFkid(id);
        return resb;
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUser
     * @param optTime
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser sysUser, Date optTime) throws Exception{
        ResponseBean resb = new ResponseBean();
        if (ids == null){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识丢失");
            return resb;
        }

        Example example = new Example(WhgGoods.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );

        WhgGoods goods = new WhgGoods();
        goods.setState(tostate);
        if (optTime==null) {
            optTime = new Date();
        }
        goods.setStatemdfdate(optTime);
        goods.setStatemdfuser(sysUser.getId());
        this.whgGoodsMapper.updateByExampleSelective(goods, example);
        return resb;
    }
}
