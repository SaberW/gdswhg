package com.creatoo.hn.services.admin.mass;

import com.creatoo.hn.dao.mapper.WhgMassTypeMapper;
import com.creatoo.hn.dao.model.WhgMassType;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumState;
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
 * 群文资源库服务类
 * Created by wangxl on 2017/11/7.
 */
@Service
@CacheConfig(cacheNames = "WhgMassType", keyGenerator = "simpleKeyGenerator")
public class WhgMassTypeService extends BaseService {
    /**
     * 群文资源库Mapper
     */
    @Autowired
    private WhgMassTypeMapper whgMassTypeMapper;

    /**
     * 根据唯一标识查询群文资源库
     * @param id 群文资源库标识
     * @return v
     * @throws Exception
     */
    @Cacheable
    public WhgMassType findById(String id)throws Exception{
        return this.whgMassTypeMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询群文资源库列表
     * @param sort 排序字段
     * @param order 排序方式
     * @param whgMassType 查询条件
     * @return List<WhgMassType>
     * @throws Exception
     */
    @Cacheable
    public List<WhgMassType> find(String sort, String order, WhgMassType whgMassType)throws Exception{
        Example example = new Example(WhgMassType.class);
        Example.Criteria c = example.createCriteria();

        if(whgMassType != null && StringUtils.isNotEmpty(whgMassType.getName())){
            c.andLike("name", "%"+whgMassType.getName()+"%");
            whgMassType.setName(null);
        }
        c.andEqualTo(whgMassType);

        if(StringUtils.isNotEmpty(sort)){
            String orderby = sort;
            if("asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order)){
                orderby += " "+order;
            }
            example.setOrderByClause(orderby);
        }else{
            example.setOrderByClause("idx");
        }
        return this.whgMassTypeMapper.selectByExample(example);
    }

    /**
     * 资源库是否有子分类
     * @param id 资源库标识
     * @return true-有子， false-没有
     * @throws Exception
     */
    @Cacheable
    public boolean hasChildren(String id)throws Exception{
        boolean hasChildren = false;
        WhgMassType whgMassType = new WhgMassType();
        whgMassType.setPid(id);
        hasChildren = this.whgMassTypeMapper.selectCount(whgMassType) > 0;
        return hasChildren;
    }

    /**
     * 添加
     * @param sysUser 管理员
     * @param whgMassType 群文资源库信息
     * @return 群文资源库标识
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public String add(WhgSysUser sysUser, WhgMassType whgMassType)throws Exception{
        whgMassType.setId(IDUtils.getID32());
        whgMassType.setCrtuser(sysUser.getId());
        whgMassType.setCrtdate(new Date());
        whgMassType.setState(EnumState.STATE_YES.getValue());
        if(StringUtils.isEmpty(whgMassType.getPid())){
            whgMassType.setPid("ROOT");
        }

        Example example = new Example(WhgMassType.class);
        example.createCriteria()
                .andEqualTo("pid", whgMassType.getPid())
        .andEqualTo("name", whgMassType.getName());
        int cnt = this.whgMassTypeMapper.selectCountByExample(example);
        if(cnt > 0){
            throw new Exception("名称重复");
        }
        int row = this.whgMassTypeMapper.insert(whgMassType);
        if(row != 1){
            throw new Exception("保存失败");
        }
        return whgMassType.getId();
    }

    /**
     * 编辑
     * @param sysUser
     * @param whgMassType
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void edit(WhgSysUser sysUser, WhgMassType whgMassType)throws Exception{
        Example example = new Example(WhgMassType.class);
        example.createCriteria()
                .andEqualTo("pid", whgMassType.getPid())
                .andEqualTo("name", whgMassType.getName())
        .andNotEqualTo("id", whgMassType.getId());
        int cnt = this.whgMassTypeMapper.selectCountByExample(example);
        if(cnt > 0){
            throw new Exception("名称重复");
        }
        this.whgMassTypeMapper.updateByPrimaryKeySelective(whgMassType);
    }

    /**
     * 删除失败
     * @param id 唯一标识
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void del(String id)throws Exception{
        Example example = new Example(WhgMassType.class);
        example.createCriteria().andEqualTo("pid", id);
        int cnt = this.whgMassTypeMapper.selectCountByExample(example);
        if(cnt > 0){
            throw new Exception("存在子库不能删除");
        }
        int row = this.whgMassTypeMapper.deleteByPrimaryKey(id);
        if(row != 1){
            throw new Exception("删除失败");
        }
    }

    /**
     * 启用|停用
     * @param id 唯一标识
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void updateState(String id, String fromState, String toState)throws Exception{
        if(EnumState.STATE_NO.getValue() == Integer.parseInt(toState)){//停用
            Example example = new Example(WhgMassType.class);
            example.createCriteria().andEqualTo("pid", id).andEqualTo("state", EnumState.STATE_YES.getValue());
            int cnt = this.whgMassTypeMapper.selectCountByExample(example);
            if(cnt > 0){
                throw new Exception("子库未全部停用，停用失败");
            }
            example.clear();
            example.createCriteria().andEqualTo("id", id).andEqualTo("state", fromState);
            WhgMassType whgMassType = new WhgMassType();
            whgMassType.setState(EnumState.STATE_NO.getValue());
            cnt = this.whgMassTypeMapper.updateByExampleSelective(whgMassType, example);
            if(cnt != 1){
                throw new Exception("停用失败");
            }
        }else if(EnumState.STATE_YES.getValue() == Integer.parseInt(toState)){//启用
            Example example = new Example(WhgMassType.class);
            example.createCriteria().andEqualTo("id", id).andEqualTo("state", fromState);
            WhgMassType whgMassType = new WhgMassType();
            whgMassType.setState(EnumState.STATE_YES.getValue());
            int cnt = this.whgMassTypeMapper.updateByExampleSelective(whgMassType, example);
            if(cnt != 1){
                throw new Exception("启用失败");
            }
        }
    }
}
