package com.creatoo.hn.services.admin.yunwei;

import com.creatoo.hn.dao.mapper.WhgYwiGroupMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiGroup;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.IDUtils;
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

import java.util.Date;
import java.util.List;

/**
 * 培训组
 * Created by wangxl on 2018/1/9.
 */
@Service
@CacheConfig(cacheNames = "WhgYunweiGroup", keyGenerator = "simpleKeyGenerator")
public class WhgYunweiGroupService {
    /**
     * 管理员服务类
     */
    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 培训组Mapper
     */
    @Autowired
    private WhgYwiGroupMapper whgYwiGroupMapper;

    @Cacheable
    public WhgYwiGroup findById(String id)throws Exception{
        return this.whgYwiGroupMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询培训组
     * @param whgYwiGroup
     * @param userId
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgYwiGroup> find(WhgYwiGroup whgYwiGroup, String userId)throws Exception{
        Example example = new Example(WhgYwiGroup.class);
        Example.Criteria c = example.createCriteria();

        if(StringUtils.isEmpty(whgYwiGroup.getCultid()) && StringUtils.isNotEmpty(userId) ){
            List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(userId);
            if(cultids != null && cultids.size() > 0){
                c.andIn("cultid", cultids);
            }
            whgYwiGroup.setCultid(null);
        }
        if(StringUtils.isNotEmpty(whgYwiGroup.getName())){
            c.andLike("name","%"+whgYwiGroup.getName()+"%");
            whgYwiGroup.setName(null);
        }
        if(whgYwiGroup.getState() == null){
            whgYwiGroup.setState(EnumState.STATE_YES.getValue());
        }
        c.andEqualTo(whgYwiGroup);
        example.orderBy("crtdate").desc();
        return this.whgYwiGroupMapper.selectByExample(example);
    }


    /**
     * 分页查询分类列表信息
     * @param
     */
    @Cacheable
    public PageInfo<WhgYwiGroup> t_srchList4p(int page, int rows, String sort, String order, WhgYwiGroup whgYwiGroup, String userId) throws Exception {
        Example example = new Example(WhgYwiGroup.class);
        Example.Criteria c = example.createCriteria();

        if(StringUtils.isEmpty(whgYwiGroup.getCultid()) && StringUtils.isNotEmpty(userId) ){
            List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(userId);
            if(cultids != null && cultids.size() > 0){
                c.andIn("cultid", cultids);
            }
            whgYwiGroup.setCultid(null);
        }
        if(StringUtils.isNotEmpty(whgYwiGroup.getName())){
            c.andLike("name","%"+whgYwiGroup.getName()+"%");
            whgYwiGroup.setName(null);
        }
        c.andEqualTo(whgYwiGroup);

        //排序
        if(StringUtils.isNotEmpty(sort)){
            if("desc".equalsIgnoreCase(order)){
                example.orderBy(sort).desc();
            }else{
                example.orderBy(sort).asc();
            }
        }else{
            example.orderBy("crtdate").desc();
        }

        //开始分页
        PageHelper.startPage(page, rows);
        List<WhgYwiGroup> typeList = this.whgYwiGroupMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 添加
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgYwiGroup whgYwiGroup, WhgSysUser sysUser)throws Exception {
        Example example = new Example(WhgYwiGroup.class);
        example.createCriteria().andEqualTo("name", whgYwiGroup.getName())
                .andEqualTo("cultid", whgYwiGroup.getCultid());
        int count = this.whgYwiGroupMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称不能重复");
        }
        whgYwiGroup.setId(IDUtils.getID());
        whgYwiGroup.setState(EnumState.STATE_NO.getValue());
        whgYwiGroup.setCrtdate(new Date());
        whgYwiGroup.setCrtuser(sysUser.getId());
        whgYwiGroup.setStatemdfdate(new Date());
        whgYwiGroup.setStatemdfuser(sysUser.getId());
        int result = this.whgYwiGroupMapper.insertSelective(whgYwiGroup);
        if(result != 1){
            throw new Exception("添加数据失败！");
        }

    }

    /**
     * 编辑
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgYwiGroup whgYwiGroup, WhgSysUser sysUser)throws Exception {
        Example example = new Example(WhgYwiGroup.class);
        example.createCriteria().andEqualTo("name", whgYwiGroup.getName())
                .andEqualTo("cultid", whgYwiGroup.getCultid())
                .andNotEqualTo("id", whgYwiGroup.getId());
        int count = this.whgYwiGroupMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称不能重复");
        }
        WhgYwiGroup _whgYwiKey = this.whgYwiGroupMapper.selectByPrimaryKey(whgYwiGroup.getId());
        _whgYwiKey.setName(whgYwiGroup.getName());
        _whgYwiKey.setCultid(whgYwiGroup.getCultid());
        _whgYwiKey.setStatemdfdate(new Date());
        _whgYwiKey.setStatemdfuser(sysUser.getId());
        _whgYwiKey.setMax(whgYwiGroup.getMax());
        _whgYwiKey.setState(whgYwiGroup.getState());
        int result = this.whgYwiGroupMapper.updateByPrimaryKeySelective(_whgYwiKey);
        if(result != 1){
            throw new Exception("编辑数据失败！");
        }
    }

    /**
     * 启用
     * @param id
     * @param userid
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_on(String id, String userid)throws Exception{
        WhgYwiGroup whgYwiGroup = new WhgYwiGroup();
        whgYwiGroup.setId(id);
        whgYwiGroup.setState(EnumState.STATE_YES.getValue());
        whgYwiGroup.setStatemdfdate(new Date());
        whgYwiGroup.setStatemdfuser(userid);
        this.whgYwiGroupMapper.updateByPrimaryKeySelective(whgYwiGroup);
    }

    /**
     * 停用
     * @param id
     * @param userid
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_off(String id, String userid)throws Exception{
        WhgYwiGroup whgYwiGroup = new WhgYwiGroup();
        whgYwiGroup.setId(id);
        whgYwiGroup.setState(EnumState.STATE_NO.getValue());
        whgYwiGroup.setStatemdfdate(new Date());
        whgYwiGroup.setStatemdfuser(userid);
        this.whgYwiGroupMapper.updateByPrimaryKeySelective(whgYwiGroup);
    }

    /**
     * 删除分类信息
     * @param
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception {
        int result = this.whgYwiGroupMapper.deleteByPrimaryKey(id);
        if(result != 1){
            throw new Exception("删除数据失败！");
        }
    }
}
