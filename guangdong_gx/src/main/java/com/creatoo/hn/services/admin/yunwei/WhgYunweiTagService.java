package com.creatoo.hn.services.admin.yunwei;

import com.creatoo.hn.dao.mapper.WhgYwiTagMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiKey;
import com.creatoo.hn.dao.model.WhgYwiTag;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
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
 * 系统运营的标签管理action
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/3/17.
 */
@Service
@CacheConfig(cacheNames = "WhgYwiTag", keyGenerator = "simpleKeyGenerator")
public class WhgYunweiTagService extends BaseService{
    /**
     * mapper
     */
    @Autowired
    private WhgYwiTagMapper whgYwiTagMapper;

    /**
     * 管理员服务类
     */
    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 根据标签ID获取标签名称
     * @param id
     * @return
     * @throws Exception
     */
    @Cacheable
    public String findTagName(String id)throws Exception{
        WhgYwiTag tag = this.whgYwiTagMapper.selectByPrimaryKey(id);
        return tag.getName();
    }

    /**
     * 查找所有标签
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgYwiTag> findAllYwiTag(String type,String cultid)throws Exception{
        Example example = new Example(WhgYwiTag.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        if(type!=null&&!type.equals("")){
            c.andEqualTo("type", type);
        }
        if ( cultid != null && !"".equals(cultid)) {
            c.andEqualTo("cultid", cultid);
        }
        example.setOrderByClause("idx");
        return this.whgYwiTagMapper.selectByExample(example);
    }

    /**
     * 查找所有标签
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgYwiTag> findAllYwiTag(String type,String cultid,List list)throws Exception{
        Example example = new Example(WhgYwiTag.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        if(type!=null&&!type.equals("")){
            c.andEqualTo("type", type);
        }
        if ( cultid != null && !"".equals(cultid)) {
            c.andEqualTo("cultid", cultid);
        }
        c.andIn("id",list);
        example.setOrderByClause("idx");
        List<WhgYwiTag> whgYwiTags  = this.whgYwiTagMapper.selectByExample(example);
        return whgYwiTags;
    }

    /**
     * 分页查询标签列表信息
     * @param
     */
    @Cacheable
    public PageInfo<WhgYwiTag> t_srchList4p(int page, int rows, WhgYwiTag whgYwiTag, String userId, List list) throws Exception {
        //开始分页
        PageHelper.startPage(page, rows);
        Example example = new Example(WhgYwiTag.class);
        Example.Criteria c = example.createCriteria();

        if(whgYwiTag == null){
            whgYwiTag = new WhgYwiTag();
        }
        if(StringUtils.isNotEmpty(whgYwiTag.getCultid())) {
            c.andEqualTo("cultid", whgYwiTag.getCultid());
        }else if(StringUtils.isNotEmpty(userId)){
            List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(userId);
            if(cultids != null && cultids.size() > 0){
                c.andIn("cultid", cultids);
            }
        }
        if (list != null && list.size() > 0) {
            c.andIn("id", list);
        }
        if(StringUtils.isNotEmpty(whgYwiTag.getType())) {
            c.andEqualTo("type", whgYwiTag.getType());
        }
        if(StringUtils.isNotEmpty(whgYwiTag.getName())){
            c.andLike("name","%"+whgYwiTag.getName()+"%");
        }
        c.andEqualTo("state",1);
        c.andEqualTo("delstate",0);
        example.setOrderByClause("idx asc");
        List<WhgYwiTag> typeList = this.whgYwiTagMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 添加标签
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgYwiTag whgYwiTag, WhgSysUser sysUser)throws Exception {
        Example example = new Example(WhgYwiKey.class);
        example.createCriteria().andEqualTo("name", whgYwiTag.getName()).andEqualTo("type", whgYwiTag.getType())
                .andEqualTo("cultid", whgYwiTag.getCultid());
        int count = this.whgYwiTagMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称重复");
        }
        whgYwiTag.setId(IDUtils.getID());
        whgYwiTag.setState(1);
        whgYwiTag.setCrtdate(new Date());
        whgYwiTag.setCrtuser(sysUser.getId());
        whgYwiTag.setStatemdfdate(new Date());
        whgYwiTag.setStatemdfuser(sysUser.getId());
        int result = this.whgYwiTagMapper.insertSelective(whgYwiTag);
        if(result != 1){
            throw new Exception("添加数据失败！");
        }

    }

    /**
     * 编辑标签
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgYwiTag whgYwiTag,WhgSysUser sysUser)throws Exception {
        Example example = new Example(WhgYwiKey.class);
        example.createCriteria().andEqualTo("name", whgYwiTag.getName()).andEqualTo("type", whgYwiTag.getType())
                .andEqualTo("cultid", whgYwiTag.getCultid())
                .andNotEqualTo("id", whgYwiTag.getId());
        int count = this.whgYwiTagMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称重复");
        }
        WhgYwiTag _whgYwiTag = this.whgYwiTagMapper.selectByPrimaryKey(whgYwiTag.getId());
        _whgYwiTag.setName(whgYwiTag.getName());
        _whgYwiTag.setIdx(whgYwiTag.getIdx());
        _whgYwiTag.setCultid(whgYwiTag.getCultid());
        _whgYwiTag.setStatemdfdate(new Date());
        _whgYwiTag.setStatemdfuser(sysUser.getId());
        int result = this.whgYwiTagMapper.updateByPrimaryKeySelective(_whgYwiTag);
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
        int result = this.whgYwiTagMapper.deleteByPrimaryKey(id);
        if(result != 1){
            throw new Exception("删除数据失败！");
        }
    }
}
