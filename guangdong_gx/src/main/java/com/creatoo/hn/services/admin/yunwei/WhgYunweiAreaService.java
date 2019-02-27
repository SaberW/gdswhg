package com.creatoo.hn.services.admin.yunwei;

import com.creatoo.hn.dao.mapper.WhgYwiAreaMapper;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiArea;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumCultLevel;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
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
 * 省市区服务类
 * Created by wangxl on 2018/1/15.
 */
@Service
@CacheConfig(cacheNames = "WhgYunweiArea", keyGenerator = "simpleKeyGenerator")
public class WhgYunweiAreaService {
    /**
     * 文化馆服务
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 省市区Mapper
     */
    @Autowired
    private WhgYwiAreaMapper whgYwiAreaMapper;

    /**
     * 根据唯一标识查询
     * @param id
     * @return v
     * @throws Exception
     */
    @Cacheable
    public WhgYwiArea findById(String id)throws Exception{
        return this.whgYwiAreaMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据名称查询
     * @param name
     * @return
     * @throws Exception
     */
    @Cacheable
    public WhgYwiArea findByName(String name)throws Exception{
        WhgYwiArea ywiArea = new WhgYwiArea();
        ywiArea.setName(name);
        ywiArea.setState(EnumState.STATE_YES.getValue());
        return this.whgYwiAreaMapper.selectOne(ywiArea);
    }

    /**
     * 根据名称code
     * @return
     * @throws Exception
     */
    @Cacheable
    public WhgYwiArea findByCode(String code) throws Exception {
        WhgYwiArea ywiArea = new WhgYwiArea();
        ywiArea.setCode(code);
        ywiArea.setState(EnumState.STATE_YES.getValue());
        return this.whgYwiAreaMapper.selectOne(ywiArea);
    }

    /**
     * 查询省市区列表
     * @param sort 排序字段
     * @param order 排序方式
     * @param whgYwiArea 查询条件
     * @return List<WhgYwiArea>
     * @throws Exception
     */
    @Cacheable
    public List<WhgYwiArea> find(String sort, String order, WhgYwiArea whgYwiArea)throws Exception{
        Example example = new Example(WhgYwiArea.class);
        Example.Criteria c = example.createCriteria();

        if(whgYwiArea != null && StringUtils.isNotEmpty(whgYwiArea.getName())){
            c.andLike("name", "%"+whgYwiArea.getName()+"%");
            whgYwiArea.setName(null);
        }
        c.andEqualTo(whgYwiArea);

        if(StringUtils.isNotEmpty(sort)){
            String orderby = sort;
            if("asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order)){
                orderby += " "+order;
            }
            example.setOrderByClause(orderby);
        }else{
            example.setOrderByClause("idx");
        }
        return this.whgYwiAreaMapper.selectByExample(example);
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
        WhgYwiArea whgYwiArea = new WhgYwiArea();
        whgYwiArea.setPid(id);
        hasChildren = this.whgYwiAreaMapper.selectCount(whgYwiArea) > 0;
        return hasChildren;
    }

    /**
     * 判断区域节点是否有子区域或者子文化馆
     * @param id 区域标识
     * @param area 区域名称
     * @param level 区域级别
     * @return true-有子, false-没有
     * @throws Exception
     */
    public boolean hasChildrenAndCult(String id, String area, String level)throws Exception{
        boolean hasChildren = false;

        hasChildren = this.hasChildren(id);
        if(!hasChildren){
            WhgSysCult whgSysCult = new WhgSysCult();
            whgSysCult.setState(EnumBizState.STATE_PUB.getValue());
            whgSysCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            whgSysCult.setLevel(Integer.parseInt(level));
            if((EnumCultLevel.Level_Province.getValue()+"").equals(level)){
                whgSysCult.setProvince(area);
            }else if((EnumCultLevel.Level_City.getValue()+"").equals(level)){
                whgSysCult.setCity(area);
            }else if((EnumCultLevel.Level_Area.getValue()+"").equals(level)){
                whgSysCult.setArea(area);
            }
            int cnt = this.whgSystemCultService.t_count(whgSysCult);
            hasChildren = cnt > 0;
        }

        return hasChildren;
    }

    /**
     * 添加
     * @param sysUser 管理员
     * @param whgYwiArea 群文资源库信息
     * @return 群文资源库标识
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public String add(WhgSysUser sysUser, WhgYwiArea whgYwiArea)throws Exception{
        whgYwiArea.setId(IDUtils.getID32());
        whgYwiArea.setCrtuser(sysUser.getId());
        whgYwiArea.setCrtdate(new Date());
        whgYwiArea.setState(EnumState.STATE_YES.getValue());
        if(StringUtils.isEmpty(whgYwiArea.getPid())){
            whgYwiArea.setPid("ROOT");
        }

        Example example = new Example(WhgYwiArea.class);
        example.createCriteria()
                .andEqualTo("pid", whgYwiArea.getPid())
                .andEqualTo("name", whgYwiArea.getName());
        example.or(example.createCriteria()
                .andEqualTo("pid", whgYwiArea.getPid())
                .andEqualTo("code", whgYwiArea.getCode()));
        int cnt = this.whgYwiAreaMapper.selectCountByExample(example);
        if(cnt > 0){
            throw new Exception("名称或者编码重复");
        }
        int row = this.whgYwiAreaMapper.insert(whgYwiArea);
        if(row != 1){
            throw new Exception("保存失败");
        }
        return whgYwiArea.getId();
    }

    /**
     * 编辑
     * @param sysUser
     * @param whgYwiArea
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void edit(WhgSysUser sysUser, WhgYwiArea whgYwiArea)throws Exception{
        Example example = new Example(WhgYwiArea.class);
        example.createCriteria()
                .andEqualTo("pid", whgYwiArea.getPid())
                .andEqualTo("name", whgYwiArea.getName())
                .andNotEqualTo("id", whgYwiArea.getId());
        example.or(example.createCriteria()
                .andEqualTo("pid", whgYwiArea.getPid())
                .andEqualTo("code", whgYwiArea.getCode())
                .andNotEqualTo("id", whgYwiArea.getId()));
        int cnt = this.whgYwiAreaMapper.selectCountByExample(example);
        if(cnt > 0){
            throw new Exception("名称或者编码重复");
        }
        this.whgYwiAreaMapper.updateByPrimaryKeySelective(whgYwiArea);
    }

    /**
     * 删除失败
     * @param id 唯一标识
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void del(String id)throws Exception{
        Example example = new Example(WhgYwiArea.class);
        example.createCriteria().andEqualTo("pid", id);
        int cnt = this.whgYwiAreaMapper.selectCountByExample(example);
        if(cnt > 0){
            throw new Exception("存在子库不能删除");
        }
        int row = this.whgYwiAreaMapper.deleteByPrimaryKey(id);
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
            Example example = new Example(WhgYwiArea.class);
            example.createCriteria().andEqualTo("pid", id).andEqualTo("state", EnumState.STATE_YES.getValue());
            int cnt = this.whgYwiAreaMapper.selectCountByExample(example);
            if(cnt > 0){
                throw new Exception("子库未全部停用，停用失败");
            }
            example.clear();
            example.createCriteria().andEqualTo("id", id).andEqualTo("state", fromState);
            WhgYwiArea whgYwiArea = new WhgYwiArea();
            whgYwiArea.setState(EnumState.STATE_NO.getValue());
            cnt = this.whgYwiAreaMapper.updateByExampleSelective(whgYwiArea, example);
            if(cnt != 1){
                throw new Exception("停用失败");
            }
        }else if(EnumState.STATE_YES.getValue() == Integer.parseInt(toState)){//启用
            Example example = new Example(WhgYwiArea.class);
            example.createCriteria().andEqualTo("id", id).andEqualTo("state", fromState);
            WhgYwiArea whgYwiArea = new WhgYwiArea();
            whgYwiArea.setState(EnumState.STATE_YES.getValue());
            int cnt = this.whgYwiAreaMapper.updateByExampleSelective(whgYwiArea, example);
            if(cnt != 1){
                throw new Exception("启用失败");
            }
        }
    }
}
