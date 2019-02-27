package com.creatoo.hn.services.admin.yunwei;


import com.creatoo.hn.dao.mapper.WhgYwiKeyMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiKey;
import com.creatoo.hn.dao.model.WhgYwiTag;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/17.
 */
@Service
@CacheConfig(cacheNames = "WhgYwiKey", keyGenerator = "simpleKeyGenerator")
public class WhgYunweiKeyService extends BaseService{
    /**
     * 关键字mapper
     */
    @Autowired
    private WhgYwiKeyMapper whgYwiKeyMapper;

    /**
     * 获取关键字
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgYwiKey> findAllYwiKey(String cultid)throws Exception{
        Example example = new Example(WhgYwiKey.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        if (cultid != null && !"".equals(cultid)) {
            c.andEqualTo("cultid",cultid);
        }
        example.setOrderByClause("idx");
        return this.whgYwiKeyMapper.selectByExample(example);
    }

    @Cacheable
    public List<WhgYwiKey> findAllYwiKey(String type, String cultid)throws Exception{
        Example example = new Example(WhgYwiKey.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        if (cultid != null && !"".equals(cultid)) {
            c.andEqualTo("cultid",cultid);
        }
        if (type != null && !"".equals(type)) {
            c.andEqualTo("type",type);
        }
        example.setOrderByClause("idx");
        return this.whgYwiKeyMapper.selectByExample(example);
    }


    /**
     * 分页查询分类列表信息
     * @param
     */
    @Cacheable
    public PageInfo<WhgYwiKey> t_srchList4p(int page,int rows, WhgYwiKey whgYwiKey) throws Exception {
        //开始分页
        PageHelper.startPage(page, rows);
        Example example = new Example(WhgYwiTag.class);
        Example.Criteria c = example.createCriteria();

        if (whgYwiKey.getType() != null && !"".equals(whgYwiKey.getType())) {
            c.andEqualTo("type", whgYwiKey.getType());
        }
        if (whgYwiKey.getCultid() != null && !"".equals(whgYwiKey.getCultid())) {
            c.andEqualTo("cultid", whgYwiKey.getCultid());
        }
        if(whgYwiKey.getName() != null && !"".equals(whgYwiKey.getName())){
            c.andLike("name","%"+whgYwiKey.getName()+"%");
        }
        c.andEqualTo("state",1);
        c.andEqualTo("delstate",0);
        example.setOrderByClause("idx asc");
        List<WhgYwiKey> typeList = this.whgYwiKeyMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 添加分类
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgYwiKey whgYwiKey, WhgSysUser sysUser)throws Exception {
        Example example = new Example(WhgYwiKey.class);
        example.createCriteria().andEqualTo("name", whgYwiKey.getName()).andEqualTo("type", whgYwiKey.getType())
                .andEqualTo("cultid", whgYwiKey.getCultid());
        int count = this.whgYwiKeyMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称重复");
        }
        whgYwiKey.setId(IDUtils.getID());
        whgYwiKey.setState(1);
        whgYwiKey.setCrtdate(new Date());
        whgYwiKey.setCrtuser(sysUser.getId());
        whgYwiKey.setStatemdfdate(new Date());
        whgYwiKey.setStatemdfuser(sysUser.getId());
        int result = this.whgYwiKeyMapper.insertSelective(whgYwiKey);
        if(result != 1){
            throw new Exception("添加数据失败！");
        }

    }

    /**
     * 编辑分类
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgYwiKey whgYwiKey, WhgSysUser sysUser)throws Exception {
        Example example = new Example(WhgYwiKey.class);
        example.createCriteria().andEqualTo("name", whgYwiKey.getName()).andEqualTo("type", whgYwiKey.getType()).andEqualTo("cultid", whgYwiKey.getCultid()).andNotEqualTo("id", whgYwiKey.getId());
        int count = this.whgYwiKeyMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称重复");
        }
        WhgYwiKey _whgYwiKey = this.whgYwiKeyMapper.selectByPrimaryKey(whgYwiKey.getId());
        _whgYwiKey.setName(whgYwiKey.getName());
        _whgYwiKey.setIdx(whgYwiKey.getIdx());
        _whgYwiKey.setCultid(whgYwiKey.getCultid());
        _whgYwiKey.setStatemdfdate(new Date());
        _whgYwiKey.setStatemdfuser(sysUser.getId());
        int result = this.whgYwiKeyMapper.updateByPrimaryKeySelective(_whgYwiKey);
        if(result != 1){
            throw new Exception("编辑数据失败！");
        }
    }

    /**
     * 删除分类信息
     * @param
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception {
        int result = this.whgYwiKeyMapper.deleteByPrimaryKey(id);
        if(result != 1){
            throw new Exception("删除数据失败！");
        }
    }

    /**
     * 设置排序值
     * @param
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_sort(int idx,String id) throws Exception {
        WhgYwiKey ywKey = this.whgYwiKeyMapper.selectByPrimaryKey(id);
        if(!"".equals(idx)){
            ywKey.setIdx(idx);
        }
        int result = this.whgYwiKeyMapper.updateByPrimaryKey(ywKey);
        if(result != 1){
            throw new Exception("设置排序值失败！");
        }
    }
}
