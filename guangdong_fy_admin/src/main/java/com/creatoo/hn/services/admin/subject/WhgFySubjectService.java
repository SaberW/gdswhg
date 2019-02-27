package com.creatoo.hn.services.admin.subject;

import com.creatoo.hn.dao.mapper.WhgFyiSubjectMapper;
import com.creatoo.hn.dao.model.WhgFyiSubject;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
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
 * 非遗专题service
 *
 * @author luzhihuai
 *         Created by Administrator on 2017/10/31.
 */
@Service
@CacheConfig(cacheNames = "WhgFyiSubject", keyGenerator = "simpleKeyGenerator")
public class WhgFySubjectService extends BaseService {
    /**
     * 非遗专题mapper
     */
    @Autowired
    private WhgFyiSubjectMapper subjectMapper;

    /**
     *
     * @param request request对象
     * @param subject 非遗专题对象
     * @return 分页对象
     * @throws Exception exception
     */
    @Cacheable
    public PageInfo<WhgFyiSubject> tSrchlist4p(HttpServletRequest request, WhgFyiSubject subject) throws Exception {
        int page = RequestUtils.getPageParameter(request);
        int rows = RequestUtils.getRowsParameter(request);
        //搜索条件
        Example example = new Example(WhgFyiSubject.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (subject != null && StringUtils.isNotBlank(subject.getName())) {
            c.andLike("name", "%" + subject.getName() + "%");
            subject.setName(null);
        }
        //其它条件
        c.andEqualTo(subject);
        //排序
        example.setOrderByClause("createdate desc");
        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgFyiSubject> typeList = this.subjectMapper.selectByExample(example);
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
    public WhgFyiSubject searchOne(String id) throws Exception {
        WhgFyiSubject record = new WhgFyiSubject();
        record.setId(id);
        return this.subjectMapper.selectOne(record);
    }

    /**
     * 添加
     *
     * @param subject 非遗专题bean
     * @param user     用户bean
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public void add(WhgFyiSubject subject, WhgSysUser user) throws Exception {
        subject.setId(IDUtils.getID());
        subject.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        //默认是  背景图片不平铺:1-是， 0-否
        subject.setNorepeat(1);
        subject.setCreatedate(new Date());
        subject.setCreateuser(user.getId());
        int result = this.subjectMapper.insertSelective(subject);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑
     *
     * @param user     用户bean
     * @param subject 非遗专题bean
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public void edit(WhgSysUser user, WhgFyiSubject subject) throws Exception {
        subject.setStatemdfdate(new Date());
        subject.setStatemdfuser(user.getId());
        int result = this.subjectMapper.updateByPrimaryKeySelective(subject);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * @param id 专题id
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public void del(String id) throws Exception {
        int result = this.subjectMapper.deleteByPrimaryKey(id);
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
        Example example = new Example(WhgFyiSubject.class);
        example.createCriteria()
                .andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")))
                .andIn("id", Arrays.asList(ids.split("\\s*,\\s*")));
        WhgFyiSubject cord = new WhgFyiSubject();
        cord.setState(tostate);
        cord.setStatemdfdate(new Date());
        cord.setStatemdfuser(user.getId());
        this.subjectMapper.updateByExampleSelective(cord, example);
        return rb;
    }


}
