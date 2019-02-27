package com.creatoo.hn.services.admin.personnel;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgPersonnelMapper;
import com.creatoo.hn.dao.mapper.WhgPersonnelMapper;
import com.creatoo.hn.dao.model.WhgPersonnel;
import com.creatoo.hn.dao.model.WhgPersonnel;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.util.ReqParamsUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 专家人才 service
 * Created by LENUVN on 2017/7/27.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgPersonnel", keyGenerator = "simpleKeyGenerator")
public class WhgPersonnelService extends BaseService {


    @Autowired
    private WhgPersonnelMapper whgPersonnelMapper;

    @Autowired
    private WhgFkProjectService whgFkProjectService;


    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @Cacheable
    public PageInfo<WhgPersonnel> t_srchList4p(HttpServletRequest request, WhgPersonnel cult) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgPersonnel.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (cult != null && cult.getName() != null) {
            c.andLike("name", "%" + cult.getName() + "%");
            cult.setName(null);
        }

        String pageType = request.getParameter("type");
        //编辑列表
        if ("edit".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(1,5));
        }
        //审核列表，查 9待审核
        if ("check".equalsIgnoreCase(pageType)){
            c.andEqualTo("state", 9);
        }
        //发布列表，查 2待发布 6已发布 4已下架
        if ("publish".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(2,6,4));
        }
        //删除列表，查已删除 否则查未删除的
        if ("recycle".equalsIgnoreCase(pageType)){
            c.andEqualTo("isdel", 1);
        }else{
            c.andEqualTo("isdel", 0);
        }
        if(request.getParameter("state") != null){
            int state = Integer.parseInt(request.getParameter("state"));
            c.andEqualTo("state", state);
        }

        example.setOrderByClause("crtdate desc");

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgPersonnel> typeList = this.whgPersonnelMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @SuppressWarnings("all")
    @Cacheable
    public PageInfo<WhgPersonnel> t_srchList4p(HttpServletRequest request, WhgPersonnel cult,List<Map> relList) throws Exception {

        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgPersonnel.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (cult != null && cult.getName() != null) {
            c.andLike("name", "%" + cult.getName() + "%");
            cult.setName(null);
        }

        String pageType = request.getParameter("type");
        //编辑列表
        if ("edit".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(1,5));
        }
        //审核列表，查 9待审核
        if ("check".equalsIgnoreCase(pageType)){
            c.andEqualTo("state", 9);
        }
        //发布列表，查 2待发布 6已发布 4已下架
        if ("publish".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(2,6,4));
        }
        //删除列表，查已删除 否则查未删除的
        if ("recycle".equalsIgnoreCase(pageType)){
            c.andEqualTo("isdel", 1);
        }else{
            c.andEqualTo("isdel", 0);
        }
        if(request.getParameter("state") != null){
            int state = Integer.parseInt(request.getParameter("state"));
            c.andEqualTo("state", state);
        }
        if(request.getParameter("cultid") != null){
            c.andEqualTo("cultid", request.getParameter("cultid"));
        }
        example.setOrderByClause("crtdate desc");

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgPersonnel> typeList = this.whgPersonnelMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 查询单条记录
     *
     * @param id id
     * @return 对象
     * @throws Exception
     */
    @Cacheable
    public WhgPersonnel t_srchOne(String id) throws Exception {
        return this.whgPersonnelMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加
     *
     * @param cultHeritage .
     */
    @CacheEvict(allEntries = true)
    public void t_add(HttpServletRequest request, WhgPersonnel cultHeritage) throws Exception {
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        //cultHeritage.setId(commService.getKey("whg_cult_heritage"));在外面赋值
        cultHeritage.setCrtdate(new Date());
        cultHeritage.setIsrecommend(0);
        cultHeritage.setIsdel(0);
        cultHeritage.setStatemdfdate(new Date());
        cultHeritage.setStatemdfuser(user.getId());
        cultHeritage.setCrtuser(user.getId());
        cultHeritage.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        int result = this.whgPersonnelMapper.insertSelective(cultHeritage);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑
     *
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgPersonnel cultHeritage) throws Exception {
        cultHeritage.setStatemdfdate(new Date());
        int result = this.whgPersonnelMapper.updateByPrimaryKeySelective(cultHeritage);
        if (result != 1) {
            throw new Exception("编辑数据失败！");
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception {
        WhgPersonnel cultHeritage = whgPersonnelMapper.selectByPrimaryKey(id);
        if (cultHeritage == null) {
            return;
        }
        if (cultHeritage.getIsdel() != null && cultHeritage.getIsdel().compareTo(1) == 0) {
            this.whgPersonnelMapper.deleteByPrimaryKey(id);
            //删除fk表中对应的数据
            this.whgFkProjectService.delByFkid(id);
        } else {
            cultHeritage = new WhgPersonnel();
            cultHeritage.setId(id);
            cultHeritage.setIsdel(1);
            this.whgPersonnelMapper.updateByPrimaryKeySelective(cultHeritage);
        }
    }

    /**
     * 还原
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    public void t_undel(String id) {
        WhgPersonnel cultHeritage = whgPersonnelMapper.selectByPrimaryKey(id);
        if (cultHeritage == null) {
            return;
        }
        cultHeritage = new WhgPersonnel();
        cultHeritage.setId(id);
        cultHeritage.setIsdel(0);
        this.whgPersonnelMapper.updateByPrimaryKeySelective(cultHeritage);
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
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser user) throws Exception {
        ResponseBean rb = new ResponseBean();
        if (ids == null) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("主键丢失");
            return rb;
        }
        Example example = new Example(WhgPersonnel.class);
        example.createCriteria()
                .andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")))
                .andIn("id", Arrays.asList(ids.split("\\s*,\\s*")));
        WhgPersonnel cultHeritage = new WhgPersonnel();
        cultHeritage.setState(tostate);
        cultHeritage.setStatemdfdate(new Date());
        cultHeritage.setStatemdfuser(user.getId());
        this.whgPersonnelMapper.updateByExampleSelective(cultHeritage, example);
        return rb;
    }

    /**
     * 更新推荐状态
     * @param ids 活动ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public int t_updCommend(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            WhgPersonnel record = whgPersonnelMapper.selectByPrimaryKey(ids);
            record.setIsrecommend(Integer.parseInt(toState));
            if(Integer.parseInt(toState) == 1){
                Date now = new Date();
                record.setStatemdfdate(now);
            }
            record.setStatemdfuser(user.getId());
            return this.whgPersonnelMapper.updateByPrimaryKey(record);
        }else {
            return 0;
        }
    }

    /**
     * 查询人才列表
     * @return
     */
    public List<WhgPersonnel> t_srchList( String cultid) {
        Example example = new Example(WhgPersonnel.class);
        Example.Criteria c = example.createCriteria();
//        if(cultids != null && cultids.size() != 0){
//            c.andIn("cultid",cultids);
//        }
        c.andEqualTo("cultid",cultid);
        c.andEqualTo("state",6);
        return this.whgPersonnelMapper.selectByExample(example);
    }
}
