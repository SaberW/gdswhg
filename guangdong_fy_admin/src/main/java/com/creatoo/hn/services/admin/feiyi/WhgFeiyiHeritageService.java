package com.creatoo.hn.services.admin.feiyi;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgFyiHeritageMapper;
import com.creatoo.hn.dao.model.WhgFyiHeritage;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 非遗管理 -- 非遗文化遗产service
 * Created by LENUVN on 2017/7/27.
 */
@Service
public class WhgFeiyiHeritageService extends BaseService {

    @Autowired
    private WhgFyiHeritageMapper whgFyiHeritageMapper;


    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    public PageInfo<WhgFyiHeritage> t_srchList4p(HttpServletRequest request, WhgFyiHeritage cult) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgFyiHeritage.class);
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
        List<WhgFyiHeritage> typeList = this.whgFyiHeritageMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @SuppressWarnings("all")
    public PageInfo<WhgFyiHeritage> t_srchList4p(HttpServletRequest request, WhgFyiHeritage cult,List<Map> relList) throws Exception {

        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgFyiHeritage.class);
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
        List<WhgFyiHeritage> typeList = this.whgFyiHeritageMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 查询单条记录
     *
     * @param id id
     * @return 对象
     * @throws Exception
     */
    public WhgFyiHeritage t_srchOne(String id) throws Exception {
        return this.whgFyiHeritageMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加
     *
     * @param cultHeritage .
     */
    public void t_add(HttpServletRequest request, WhgFyiHeritage cultHeritage) throws Exception {
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        //cultHeritage.setId(commService.getKey("whg_cult_heritage"));在外面赋值
        cultHeritage.setCrtdate(new Date());
        cultHeritage.setIsrecommend(0);
        cultHeritage.setIsdel(0);
        cultHeritage.setStatemdfdate(new Date());
        cultHeritage.setStatemdfuser(user.getId());
        cultHeritage.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        int result = this.whgFyiHeritageMapper.insertSelective(cultHeritage);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑
     *
     * @param
     */
    public void t_edit(WhgFyiHeritage cultHeritage) throws Exception {
        cultHeritage.setStatemdfdate(new Date());
        int result = this.whgFyiHeritageMapper.updateByPrimaryKeySelective(cultHeritage);
        if (result != 1) {
            throw new Exception("编辑数据失败！");
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    public void t_del(String id) throws Exception {
        WhgFyiHeritage cultHeritage = whgFyiHeritageMapper.selectByPrimaryKey(id);
        if (cultHeritage == null) {
            return;
        }
        if (cultHeritage.getIsdel() != null && cultHeritage.getIsdel().compareTo(1) == 0) {
            this.whgFyiHeritageMapper.deleteByPrimaryKey(id);
        } else {
            cultHeritage = new WhgFyiHeritage();
            cultHeritage.setId(id);
            cultHeritage.setIsdel(1);
            this.whgFyiHeritageMapper.updateByPrimaryKeySelective(cultHeritage);
        }
    }

    /**
     * 还原
     *
     * @param id
     */
    public void t_undel(String id) {
        WhgFyiHeritage cultHeritage = whgFyiHeritageMapper.selectByPrimaryKey(id);
        if (cultHeritage == null) {
            return;
        }
        cultHeritage = new WhgFyiHeritage();
        cultHeritage.setId(id);
        cultHeritage.setIsdel(0);
        this.whgFyiHeritageMapper.updateByPrimaryKeySelective(cultHeritage);
    }

    /**
     * 修改状态
     *
     * @param ids        用逗号分隔的多个ID
     * @param formstates 修改之前的状态
     * @param tostate    修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser user) throws Exception {
        ResponseBean rb = new ResponseBean();
        if (ids == null) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("主键丢失");
            return rb;
        }
        Example example = new Example(WhgFyiHeritage.class);
        example.createCriteria()
                .andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")))
                .andIn("id", Arrays.asList(ids.split("\\s*,\\s*")));
        WhgFyiHeritage cultHeritage = new WhgFyiHeritage();
        cultHeritage.setState(tostate);
        cultHeritage.setStatemdfdate(new Date());
        cultHeritage.setStatemdfuser(user.getId());
        this.whgFyiHeritageMapper.updateByExampleSelective(cultHeritage, example);
        return rb;
    }

    /**
     * 更新推荐状态
     * @param ids 活动ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    public int t_updCommend(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            WhgFyiHeritage record = whgFyiHeritageMapper.selectByPrimaryKey(ids);
            record.setIsrecommend(Integer.parseInt(toState));
            if(Integer.parseInt(toState) == 1){
                Date now = new Date();
                record.setStatemdfdate(now);
            }
            record.setStatemdfuser(user.getId());
            return this.whgFyiHeritageMapper.updateByPrimaryKey(record);
        }else {
            return 0;
        }
    }
    
}
