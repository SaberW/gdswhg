package com.creatoo.hn.services.admin.project;

import com.creatoo.hn.dao.mapper.WhgFkProjectMapper;
import com.creatoo.hn.dao.mapper.WhgResourceMapper;
import com.creatoo.hn.dao.model.WhgActivity;
import com.creatoo.hn.dao.model.WhgFkProject;
import com.creatoo.hn.dao.model.WhgSupplyTime;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 关联系统管理service
 *
 */
@Service
@CacheConfig(cacheNames = "WhgFkProject", keyGenerator = "simpleKeyGenerator")
public class WhgFkProjectService extends BaseService {
    /**
     * 
     */
    @Autowired
    private WhgFkProjectMapper  whgFkProjectMapper;

    @Autowired
    private WhgSupplyService whgSupplyService;

    /**
     * 查询单条记录
     * @param id id
     * @return 对象
     * @throws Exception
     */
    @Cacheable
    public WhgFkProject t_srchOne(String id)throws Exception{
        WhgFkProject record = new WhgFkProject();
        record.setId(id);
        return this.whgFkProjectMapper.selectOne(record);
    }

    /**
     * 根据关联id查询单条记录
     * @return 对象
     * @throws Exception
     */
    public WhgFkProject srchOneByFkId(String fkId)throws Exception{
        Example example = new Example(WhgFkProject.class);
        example.createCriteria().andEqualTo("fkid", fkId);
        List<WhgFkProject> list=this.whgFkProjectMapper.selectByExample(example);
       if(list!=null&&list.size()>0){
           return list.get(0);
       }else{
           return  null;
       }
    }
    /**
     * 根据关联id查询单条记录
     * @return 对象
     * @throws Exception
     */
    @Cacheable
    public List<WhgFkProject> srchListByFkId(String fkId)throws Exception{
        Example example = new Example(WhgFkProject.class);
        example.createCriteria().andEqualTo("fkid", fkId);
        List<WhgFkProject> list=this.whgFkProjectMapper.selectByExample(example);
        return list;
    }


    /**
     * 添加 str
     *proType:系统类型标识：WGBX/NBGX
     * fkId:关联ID
     * times：时间场次（json数据）
     */
    @CacheEvict(allEntries = true)
    public void addStr(WhgFkProject whgFkProject) throws Exception {
        String strId=IDUtils.getID();
        whgFkProject.setId(strId);
        whgFkProject.setProtype(EnumProject.PROJECT_NBGX.getValue());
        t_add(whgFkProject);
    }


    /**
     * 添加
     *
     * @param wcr
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgFkProject wcr) throws Exception {

        int result = this.whgFkProjectMapper.insertSelective(wcr);
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
    public void t_edit(WhgFkProject wcr){
        int result = this.whgFkProjectMapper.updateByPrimaryKeySelective(wcr);
    }

    /**
     * 删除
     *
     * @param request
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(HttpServletRequest request) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        String entid = (String) paramMap.get("id");
        int result = this.whgFkProjectMapper.deleteByPrimaryKey(entid);
        if (result != 1) {
            throw new Exception("删除数据失败！");
        }
    }

    /**
     * 删除
     *
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void delByCon(String protype,String fkId){
        Example example = new Example(WhgFkProject.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("protype", protype);
        c.andEqualTo("fkid", fkId);
        int result=whgFkProjectMapper.deleteByExample(example);
    }

    /**
     * 指定fkid删除
     * @param fkid
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void delByFkid(String fkid){
        WhgFkProject recode = new WhgFkProject();
        recode.setFkid(fkid);
        this.whgFkProjectMapper.delete(recode);
    }
}
