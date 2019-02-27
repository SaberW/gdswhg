package com.creatoo.hn.services.admin.yunwei;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgYwiWhppMapper;
import com.creatoo.hn.dao.model.WhgActivity;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiWhpp;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文化品牌配置service
 *
 * @author luzhihuai
 * @version 1-201703
 *          Created by Administrator on 2017/3/22.
 */
@Service
@CacheConfig(cacheNames = "WhgYwiWhpp", keyGenerator = "simpleKeyGenerator")
public class WhgYunweiWhppService  extends BaseService {

    /**
     * 文化品牌DAO
     */
    @Autowired
    private WhgYwiWhppMapper whgYwiWhppMapper;

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    public PageInfo<WhgYwiWhpp> t_srchList4p(HttpServletRequest request, WhgYwiWhpp whgYwiWhpp) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));
        //开始分页
        PageHelper.startPage(page, rows);

        //搜索条件
        Example example = new Example(WhgYwiWhpp.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(whgYwiWhpp != null && whgYwiWhpp.getName() != null){
            c.andLike("name", "%"+whgYwiWhpp.getName()+"%");
            whgYwiWhpp.setName(null);
        }
        //其它条件
        c.andEqualTo(whgYwiWhpp);

//        c.andEqualTo("state", 1);
//        c.andEqualTo("delstate", 0);
        example.setOrderByClause("crtdate desc");

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgYwiWhpp> typeList = this.whgYwiWhppMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 查询文化品牌
     * @return
     * @throws Exception
     */
    public PageInfo<WhgYwiWhpp> findBrand(Map map)throws Exception{
        Example example = new Example(WhgYwiWhpp.class);
        Example.Criteria c = example.createCriteria();
        //分页信息
        int page = 0;
        int rows = 0;
        try {
            page = Integer.parseInt((String)map.get("page"));
            rows = Integer.parseInt((String)map.get("rows"));
        } catch (NumberFormatException e) {
            //...
        }
        c.andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

        if(map != null &&map.get("cultid") != null){
            c.andLike("cultid", map.get("cultid").toString());
        }
        example.setOrderByClause("crtdate desc");
        //分页查询
        if (page!=0 && rows !=0){
            PageHelper.startPage(page, rows);
        }
        return new PageInfo<WhgYwiWhpp>(this.whgYwiWhppMapper.selectByExample(example));
    }

    /**
     * 列表查询
     *
     * @return
     * @throws Exception
     */
    public List<WhgYwiWhpp> t_srchList(WhgYwiWhpp whgYwiWhpp) throws Exception {
        return this.whgYwiWhppMapper.select(whgYwiWhpp);
    }

    /**
     * 查询单条记录
     * @param id id
     * @return 对象
     * @throws Exception
     */
    public WhgYwiWhpp t_srchOne(String id)throws Exception{
        WhgYwiWhpp record = new WhgYwiWhpp();
        record.setId(id);
        return this.whgYwiWhppMapper.selectOne(record);
    }

    /**
     * 添加
     *
     * @param
     */
    public void t_add(HttpServletRequest request, WhgYwiWhpp wyl) throws Exception {
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        WhgYwiWhpp whgYwiWhpp = new WhgYwiWhpp();

        whgYwiWhpp.setName(wyl.getName());
        whgYwiWhpp.setShortname(wyl.getShortname());
        whgYwiWhpp.setPicture(wyl.getPicture());
        whgYwiWhpp.setPicture(wyl.getPicture());
        whgYwiWhpp.setBgpicture(wyl.getBgpicture());
        whgYwiWhpp.setBgcolour(wyl.getBgcolour());
        whgYwiWhpp.setIntroduction(wyl.getIntroduction());
        whgYwiWhpp.setNorepeat(wyl.getNorepeat());
        whgYwiWhpp.setCultid(wyl.getCultid());
        whgYwiWhpp.setId(IDUtils.getID());
        whgYwiWhpp.setState(EnumState.STATE_YES.getValue());
        whgYwiWhpp.setCrtdate(new Date());
        whgYwiWhpp.setCrtuser(user.getId());
        whgYwiWhpp.setStatemdfuser(user.getId());

        int result = this.whgYwiWhppMapper.insertSelective(whgYwiWhpp);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }

    }

    /**
     * 编辑
     *
     * @param
     */
    public void t_edit(Map<String, Object> paramMap, HttpServletRequest request, WhgYwiWhpp wyl) throws Exception {
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        String id = (String) paramMap.get("id");
        WhgYwiWhpp whgYwiWhpp = this.whgYwiWhppMapper.selectByPrimaryKey(id);

        if (whgYwiWhpp != null) {
            whgYwiWhpp.setName(wyl.getName());
            whgYwiWhpp.setShortname(wyl.getShortname());
            whgYwiWhpp.setPicture(wyl.getPicture());
            whgYwiWhpp.setPicture(wyl.getPicture());
            whgYwiWhpp.setBgpicture(wyl.getBgpicture());
            whgYwiWhpp.setBgcolour(wyl.getBgcolour());
            whgYwiWhpp.setIntroduction(wyl.getIntroduction());
            whgYwiWhpp.setState(EnumState.STATE_YES.getValue());
            whgYwiWhpp.setStatemdfuser(user.getId());
            whgYwiWhpp.setNorepeat(wyl.getNorepeat());
            whgYwiWhpp.setCultid(wyl.getCultid());
        }
        int result = this.whgYwiWhppMapper.updateByPrimaryKeySelective(whgYwiWhpp);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }


    /**
     * 更新文化品牌状态
     * @param ids 文化品牌ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    public void t_updstate(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgYwiWhpp.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("state", fromState);
            }
            WhgYwiWhpp record = new WhgYwiWhpp();
            record.setState(Integer.parseInt(toState));
            Date now = new Date();
            record.setStatemdfdate(now);
            record.setStatemdfuser(user.getId());
            this.whgYwiWhppMapper.updateByExampleSelective(record, example);
        }
    }

    /**
     * 删除
     *
     * @param request
     * @throws Exception
     */
    public void t_del(HttpServletRequest request) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        String id = (String) paramMap.get("id");
        int result = this.whgYwiWhppMapper.deleteByPrimaryKey(id);
        if (result != 1) {
            throw new Exception("删除数据失败！");
        }
    }


}
