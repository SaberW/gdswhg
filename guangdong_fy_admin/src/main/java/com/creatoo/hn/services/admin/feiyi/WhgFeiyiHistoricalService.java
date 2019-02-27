package com.creatoo.hn.services.admin.feiyi;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgFyiHistoricalMapper;
import com.creatoo.hn.dao.model.WhgFyiHistorical;
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
 * 非遗管理 -- 非遗重点文物service
 * Created by LENUVN on 2017/7/27.
 */
@Service
public class WhgFeiyiHistoricalService extends BaseService {

    @Autowired
    private WhgFyiHistoricalMapper whgFyiHistoricalMapper;

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    public PageInfo<WhgFyiHistorical> t_srchList4p(HttpServletRequest request, WhgFyiHistorical historical) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgFyiHistorical.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (historical != null && historical.getName() != null) {
            c.andLike("name", "%" + historical.getName() + "%");
            historical.setName(null);
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
        List<WhgFyiHistorical> typeList = this.whgFyiHistoricalMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @SuppressWarnings("all")
    public PageInfo<WhgFyiHistorical> t_srchList4p(HttpServletRequest request, WhgFyiHistorical historical,List<Map> relList) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgFyiHistorical.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (historical != null && historical.getName() != null) {
            c.andLike("name", "%" + historical.getName() + "%");
            historical.setName(null);
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
        List<WhgFyiHistorical> typeList = this.whgFyiHistoricalMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 查询单条记录
     *
     * @param id id
     * @return 对象
     * @throws Exception
     */
    public WhgFyiHistorical t_srchOne(String id) throws Exception {
        return this.whgFyiHistoricalMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加
     *
     * @param historical .
     */
    public void t_add(HttpServletRequest request, WhgFyiHistorical historical) throws Exception {
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);

        //historical.setId(commService.getKey("whg_historical"));
        historical.setCrtdate(new Date());
        historical.setIsrecommend(0);
        historical.setIsdel(0);
        historical.setStatemdfdate(new Date());
        historical.setStatemdfuser(user.getId());
        historical.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        int result = this.whgFyiHistoricalMapper.insertSelective(historical);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑
     *
     * @param
     */
    public void t_edit(WhgFyiHistorical historical) throws Exception {
        historical.setStatemdfdate(new Date());
        int result = this.whgFyiHistoricalMapper.updateByPrimaryKeySelective(historical);
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
        WhgFyiHistorical historical = whgFyiHistoricalMapper.selectByPrimaryKey(id);
        if (historical == null) {
            return;
        }
        if (historical.getIsdel() != null && historical.getIsdel().compareTo(1) == 0) {
            this.whgFyiHistoricalMapper.deleteByPrimaryKey(id);
        } else {
            historical = new WhgFyiHistorical();
            historical.setId(id);
            historical.setIsdel(1);
            this.whgFyiHistoricalMapper.updateByPrimaryKeySelective(historical);
        }
    }

    /**
     * 还原
     *
     * @param id
     */
    public void t_undel(String id) {
        WhgFyiHistorical historical = whgFyiHistoricalMapper.selectByPrimaryKey(id);
        if (historical == null) {
            return;
        }
        historical = new WhgFyiHistorical();
        historical.setId(id);
        historical.setIsdel(0);
        this.whgFyiHistoricalMapper.updateByPrimaryKeySelective(historical);
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
        Example example = new Example(WhgFyiHistorical.class);
        example.createCriteria()
                .andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")))
                .andIn("id", Arrays.asList(ids.split("\\s*,\\s*")));
        WhgFyiHistorical historical = new WhgFyiHistorical();
        historical.setState(tostate);
        historical.setStatemdfdate(new Date());
        historical.setStatemdfuser(user.getId());
        this.whgFyiHistoricalMapper.updateByExampleSelective(historical, example);
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
            WhgFyiHistorical record = whgFyiHistoricalMapper.selectByPrimaryKey(ids);
            record.setIsrecommend(Integer.parseInt(toState));
            if(Integer.parseInt(toState) == 1){
                Date now = new Date();
                record.setStatemdfdate(now);
            }
            record.setStatemdfuser(user.getId());
            return this.whgFyiHistoricalMapper.updateByPrimaryKey(record);
        }else {
            return 0;
        }
    }
    
}
