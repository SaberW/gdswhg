package com.creatoo.hn.services.admin.derivative;

import com.creatoo.hn.dao.mapper.WhgFyiDerivativeMapper;
import com.creatoo.hn.dao.model.WhgFyiDerivative;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumRecommend;
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
 * 非遗衍生品service
 *
 * @author luzhihuai
 *         Created by Administrator on 2017/11/1.
 */
@Service
@CacheConfig(cacheNames = "WhgFyiDerivative", keyGenerator = "simpleKeyGenerator")
public class WhgFyDerivativeService extends BaseService {
    /**
     * 非遗衍生品mapper
     */
    @Autowired
    private WhgFyiDerivativeMapper derivativeMapper;

    /**
     * 分页查询
     * @param request 请求对象
     * @param derivative 衍生品bean
     * @return 分页对象
     * @throws Exception exception
     */
    @Cacheable
    public PageInfo<WhgFyiDerivative> tSrchlist4p(HttpServletRequest request, WhgFyiDerivative derivative) throws Exception {
        int page = RequestUtils.getPageParameter(request);
        int rows = RequestUtils.getRowsParameter(request);
        //搜索条件
        Example example = new Example(WhgFyiDerivative.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (derivative != null && StringUtils.isNotBlank(derivative.getName())) {
            c.andLike("name", "%" + derivative.getName() + "%");
            derivative.setName(null);
        }
        //其它条件
        c.andEqualTo(derivative);
        //排序
        example.setOrderByClause("createtime desc");
        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgFyiDerivative> typeList = this.derivativeMapper.selectByExample(example);
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
    public WhgFyiDerivative searchOne(String id) throws Exception {
        WhgFyiDerivative record = new WhgFyiDerivative();
        record.setId(id);
        return this.derivativeMapper.selectOne(record);
    }

    /**
     * 添加
     *
     * @param showroom 衍生品bean
     * @param user     用户bean
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public void add(WhgFyiDerivative showroom, WhgSysUser user) throws Exception {
        showroom.setId(IDUtils.getID());
        showroom.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        showroom.setIsrecommend(EnumRecommend.RECOMMEND_NO.getValue());
        showroom.setCreatetime(new Date());
        showroom.setCreateuser(user.getId());
        int result = this.derivativeMapper.insertSelective(showroom);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑
     *
     * @param user     用户bean
     * @param derivative 衍生品bean
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public void edit(WhgSysUser user, WhgFyiDerivative derivative) throws Exception {
        derivative.setStatemdfdate(new Date());
        derivative.setStatemdfuser(user.getId());
        int result = this.derivativeMapper.updateByPrimaryKeySelective(derivative);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 查询单条记录
     * @param id whg_fyi_derivative表主键id
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public void del(String id) throws Exception {
        int result = this.derivativeMapper.deleteByPrimaryKey(id);
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
    public ResponseBean updateState(String ids, String formstates, int tostate, WhgSysUser user) throws Exception {
        ResponseBean rb = new ResponseBean();
        if (ids == null) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("主键丢失");
            return rb;
        }
        Example example = new Example(WhgFyiDerivative.class);
        example.createCriteria()
                .andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")))
                .andIn("id", Arrays.asList(ids.split("\\s*,\\s*")));
        WhgFyiDerivative cord = new WhgFyiDerivative();
        cord.setState(tostate);
        cord.setStatemdfdate(new Date());
        cord.setStatemdfuser(user.getId());
        this.derivativeMapper.updateByExampleSelective(cord, example);
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
        WhgFyiDerivative record = derivativeMapper.selectByPrimaryKey(ids);
        record.setIsrecommend(Integer.parseInt(toState));
        if (Integer.parseInt(toState) == 1) {
            record.setStatemdfdate(new Date());
        }
        record.setStatemdfuser(user.getId());
        return this.derivativeMapper.updateByPrimaryKey(record);
    }


}
