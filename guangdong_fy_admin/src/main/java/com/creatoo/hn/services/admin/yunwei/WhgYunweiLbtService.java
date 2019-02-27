package com.creatoo.hn.services.admin.yunwei;


import com.creatoo.hn.dao.mapper.WhgYwiLbtMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiLbt;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.enums.EnumState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统管理的轮播图配置service
 *
 * @author luzhihuai
 * @version 1-201703
 *          Created by Administrator on 2017/3/20.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgYwiLbt", keyGenerator = "simpleKeyGenerator")
public class WhgYunweiLbtService extends BaseService{
    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass().getName());


    /**
     * 文化馆DAO
     */
    @Autowired
    private WhgYwiLbtMapper whgYwiLbtMapper;

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @Cacheable
    public PageInfo<WhgYwiLbt> t_srchList4p(int page,int rows,String name,String cultid) throws Exception {

        //开始分页
        PageHelper.startPage(page, rows);
        //搜索条件
        Example example = new Example(WhgYwiLbt.class);
        Example.Criteria c = example.createCriteria();
        if (name != null && !"".equals(name)) {
            c.andLike("name", "%" + name + "%");
        }
        if (cultid != null && !"".equals(cultid)) {
            c.andEqualTo("cultid",  cultid );
        }

        c.andEqualTo("state", 1);
        c.andEqualTo("delstate", 0);
        example.setOrderByClause("crtdate");
        List<WhgYwiLbt> typeList = this.whgYwiLbtMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 列表查询
     *
     * @return
     * @throws Exception
     */
    public PageInfo<WhgYwiLbt> t_srchList(WhgYwiLbt whgYwiLbt,int page,int rows) throws Exception {
        //搜索条件
        Example example = new Example(WhgYwiLbt.class);
        Example.Criteria c = example.createCriteria();
        if(whgYwiLbt.getToproject()!=null&&!whgYwiLbt.getToproject().trim().equals("")){
            c.andLike("toproject", "%"+whgYwiLbt.getToproject().trim()+"%");
            whgYwiLbt.setToproject(null);
        }
        c.andEqualTo(whgYwiLbt);
        example.setOrderByClause("crtdate desc");
        //开始分页
        PageHelper.startPage(page, rows);
        return new PageInfo<>(this.whgYwiLbtMapper.selectByExample(example));
    }

    /**
     * 查询单条记录
     * @param id id
     * @return 对象
     * @throws Exception
     */
    @Cacheable
    public WhgYwiLbt t_srchOne(String id)throws Exception{
        if(id==null) return null;
        WhgYwiLbt record = new WhgYwiLbt();
        record.setId(id);
        return this.whgYwiLbtMapper.selectOne(record);
    }

    /**
     * 添加
     *
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgSysUser user, WhgYwiLbt wyl) throws Exception {
        WhgYwiLbt whgYwiLbt = new WhgYwiLbt();
        if (wyl.getName() != null && !"".equals(wyl.getName())) {
            whgYwiLbt.setName(wyl.getName());
        }
        if (wyl.getPicture() != null && !"".equals(wyl.getPicture())) {
            whgYwiLbt.setPicture(wyl.getPicture());
        }
        if (wyl.getUrl() != null && !"".equals(wyl.getUrl())) {
            whgYwiLbt.setUrl(wyl.getUrl());
        }
        if (wyl.getType() != null && !"".equals(wyl.getType())) {
            whgYwiLbt.setType(wyl.getType());
        }
        if (wyl.getToproject() != null && !"".equals(wyl.getToproject())) {
            whgYwiLbt.setToproject(wyl.getToproject());
        }
        if (wyl.getCultid() != null && !"".equals(wyl.getCultid())) {
            whgYwiLbt.setCultid(wyl.getCultid());
        }

        whgYwiLbt.setId(IDUtils.getID());
        whgYwiLbt.setState(EnumState.STATE_YES.getValue());
        whgYwiLbt.setCrtdate(new Date());
        whgYwiLbt.setCrtuser(user.getId());
        whgYwiLbt.setStatemdfuser(user.getId());
        int result = this.whgYwiLbtMapper.insertSelective(whgYwiLbt);
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
    public void t_edit(String id, WhgSysUser user, WhgYwiLbt wyl) throws Exception {

        WhgYwiLbt whgYwiLbt = this.whgYwiLbtMapper.selectByPrimaryKey(id);

//        Example example = new Example(WhgYwiLbt.class);
//        Example.Criteria c = example.createCriteria();
//        c.andNotEqualTo("id", wyl.getId());

//        whgYwiLbt=this.whgYwiLbtMapper.selectCountByExample(example);

        if (whgYwiLbt != null) {
            whgYwiLbt.setName(wyl.getName());
            whgYwiLbt.setPicture(wyl.getPicture());
            whgYwiLbt.setUrl(wyl.getUrl());
            whgYwiLbt.setCultid(wyl.getCultid());
            whgYwiLbt.setToproject(wyl.getToproject());
            whgYwiLbt.setState(EnumState.STATE_YES.getValue());
            whgYwiLbt.setStatemdfuser(user.getId());
        }
        int result = this.whgYwiLbtMapper.updateByPrimaryKeySelective(whgYwiLbt);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 更新状态
     *
     * @param paramMap
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updstate(String id, int state) throws Exception {
        WhgYwiLbt whgYwiLbt = this.whgYwiLbtMapper.selectByPrimaryKey(id);
        whgYwiLbt.setState(state);
        int result = this.whgYwiLbtMapper.updateByPrimaryKey(whgYwiLbt);
        if (result != 1) {
            throw new Exception("编辑数据失败！");
        }
    }

    /**
     * 删除
     *
     * @param request
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception {
        int result = this.whgYwiLbtMapper.deleteByPrimaryKey(id);
        if (result != 1) {
            throw new Exception("删除数据失败！");
        }
    }


}
