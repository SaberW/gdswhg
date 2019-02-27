package com.creatoo.hn.services.admin.showroom;

import com.creatoo.hn.dao.mapper.WhgFkShowroomMapper;
import com.creatoo.hn.dao.model.WhgFkShowroom;
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
 * 非遗展厅service
 *
 * @author luzhihuai
 *         Created by Administrator on 2017/10/28.
 */
@Service
@CacheConfig(cacheNames = "WhgFkShowroom", keyGenerator = "simpleKeyGenerator")
public class WhgShowRoomService extends BaseService {
    /**
     * 非遗展厅mapper
     */
    @Autowired
    private WhgFkShowroomMapper showroomMapper;

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @Cacheable
    public PageInfo<WhgFkShowroom> tSrchlist4p(HttpServletRequest request, WhgFkShowroom showroom) throws Exception {
        int page = RequestUtils.getPageParameter(request);
        int rows = RequestUtils.getRowsParameter(request);
        //搜索条件
        Example example = new Example(WhgFkShowroom.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (showroom != null && StringUtils.isNotBlank(showroom.getName())) {
            c.andLike("name", "%" + showroom.getName() + "%");
            showroom.setName(null);
        }
        //其它条件
        c.andEqualTo(showroom);
        //排序
        example.setOrderByClause("createtime desc");
        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgFkShowroom> typeList = this.showroomMapper.selectByExample(example);
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
    public WhgFkShowroom searchOne(String id) throws Exception {
        WhgFkShowroom record = new WhgFkShowroom();
        record.setId(id);
        return this.showroomMapper.selectOne(record);
    }

    /**
     * 添加
     *
     * @param showroom 非遗展馆bean
     * @param user     用户bean
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public void add(WhgFkShowroom showroom, WhgSysUser user) throws Exception {
        showroom.setId(IDUtils.getID());
        showroom.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        showroom.setIsrecommend(EnumRecommend.RECOMMEND_NO.getValue());
        showroom.setCreatetime(new Date());
        showroom.setCreateuser(user.getId());
        int result = this.showroomMapper.insertSelective(showroom);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑
     *
     * @param user     用户bean
     * @param showroom 非遗展馆bean
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public void edit(WhgSysUser user, WhgFkShowroom showroom) throws Exception {
        showroom.setStatemdfdate(new Date());
        showroom.setStatemdfuser(user.getId());
        int result = this.showroomMapper.updateByPrimaryKeySelective(showroom);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * @param id 展厅id
     * @throws Exception exception
     */
    @CacheEvict(allEntries = true)
    public void del(String id) throws Exception {
        int result = this.showroomMapper.deleteByPrimaryKey(id);
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
        Example example = new Example(WhgFkShowroom.class);
        example.createCriteria()
                .andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")))
                .andIn("id", Arrays.asList(ids.split("\\s*,\\s*")));
        WhgFkShowroom cord = new WhgFkShowroom();
        cord.setState(tostate);
        cord.setStatemdfdate(new Date());
        cord.setStatemdfuser(user.getId());
        this.showroomMapper.updateByExampleSelective(cord, example);
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
        WhgFkShowroom record = showroomMapper.selectByPrimaryKey(ids);
        record.setIsrecommend(Integer.parseInt(toState));
        if (Integer.parseInt(toState) == 1) {
            record.setStatemdfdate(new Date());
        }
        record.setStatemdfuser(user.getId());
        return this.showroomMapper.updateByPrimaryKey(record);
    }


}
