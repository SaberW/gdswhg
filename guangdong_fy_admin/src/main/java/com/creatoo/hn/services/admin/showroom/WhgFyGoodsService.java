package com.creatoo.hn.services.admin.showroom;

import com.creatoo.hn.dao.mapper.WhgExhGoodsMapper;
import com.creatoo.hn.dao.model.WhgExhGoods;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 非遗展厅作品service
 *
 * @author luzhihuai
 *         Created by Administrator on 2017/10/31.
 */
@Service
@CacheConfig(cacheNames = "WhgExhGoods", keyGenerator = "simpleKeyGenerator")
public class WhgFyGoodsService extends BaseService {
    /**
     * 展厅作品mapper
     */
    @Autowired
    private WhgExhGoodsMapper goodsMapper;

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @Cacheable
    public PageInfo<WhgExhGoods> tSrchlist4p(HttpServletRequest request, WhgExhGoods goods) throws Exception {
        int page = RequestUtils.getPageParameter(request);
        int rows = RequestUtils.getRowsParameter(request);
        //搜索条件
        Example example = new Example(WhgExhGoods.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (goods != null && StringUtils.isNotBlank(goods.getTitle())) {
            c.andLike("title", "%" + goods.getTitle() + "%");
            goods.setTitle(null);
        }
        if(goods != null && StringUtils.isNotBlank(goods.getAuthor())){
            c.andLike("author", "%" + goods.getAuthor() + "%");
            goods.setAuthor(null);
        }
        //其他条件
        c.andEqualTo(goods);
        //排序
        example.setOrderByClause("crtdate desc");
        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgExhGoods> typeList = this.goodsMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 查询单条记录
     *
     * @param id id
     * @return 对象
     * @throws Exception exception
     */
    @Cacheable
    public WhgExhGoods srchOne(String id) throws Exception {
        WhgExhGoods record = new WhgExhGoods();
        record.setId(id);
        return this.goodsMapper.selectOne(record);
    }

    /**
     * 添加
     *
     * @param goods 展馆作品bean
     * @param user     用户bean
     * @throws Exception  exception
     */
    @CacheEvict(allEntries = true)
    public void add(WhgExhGoods goods, WhgSysUser user) throws Exception {
        Example example = new Example(WhgExhGoods.class);
        example.createCriteria().andIsNotNull("id").andEqualTo("showroomid", goods.getShowroomid());
        int sort = this.goodsMapper.selectCountByExample(example);
        sort++;
        goods.setId(IDUtils.getID());
        goods.setState(EnumState.STATE_NO.getValue());
        goods.setCrtdate(new Date());
        goods.setCrtuser(user.getId());
        goods.setSort(sort);
        int result = this.goodsMapper.insertSelective(goods);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑
     *
     * @param user     用户bean
     * @param goods 展馆作品bean
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public void edit(WhgSysUser user, WhgExhGoods goods) throws Exception {
        goods.setStatemdfdate(new Date());
        goods.setStatemdfuser(user.getId());
        int result = this.goodsMapper.updateByPrimaryKeySelective(goods);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * @param id 展厅作品id
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public void del(String id) throws Exception {
        int result = this.goodsMapper.deleteByPrimaryKey(id);
        if (result != 1) {
            throw new Exception("删除数据失败！");
        }
    }

    /**
     * 修改状态
     *
     * @param ids        用逗号分隔的多个ID
     * @param formstates 修改之前的状态
     * @param tostate    修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @CacheEvict(allEntries = true)
    public ResponseBean updteState(String ids, String formstates, int tostate, WhgSysUser user) throws Exception {
        ResponseBean rb = new ResponseBean();
        if (ids == null) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("主键丢失");
            return rb;
        }
        Example example = new Example(WhgExhGoods.class);
        example.createCriteria()
                .andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")))
                .andIn("id", Arrays.asList(ids.split("\\s*,\\s*")));
        WhgExhGoods cord = new WhgExhGoods();
        cord.setState(tostate);
        cord.setStatemdfdate(new Date());
        cord.setStatemdfuser(user.getId());
        this.goodsMapper.updateByExampleSelective(cord, example);
        return rb;
    }

    /**
     * 更新推荐状态
     *
     * @param ids     主键id
     * @param toState 修改后的状态
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public int updateCommend(String ids, String toState, WhgSysUser user) throws Exception {
        WhgExhGoods record = goodsMapper.selectByPrimaryKey(ids);
        record.setState(Integer.parseInt(toState));
        if (Integer.parseInt(toState) == 1) {
            record.setStatemdfdate(new Date());
        }
        record.setStatemdfuser(user.getId());
        return this.goodsMapper.updateByPrimaryKey(record);
    }


}
