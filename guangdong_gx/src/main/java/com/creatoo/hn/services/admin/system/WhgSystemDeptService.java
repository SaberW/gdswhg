package com.creatoo.hn.services.admin.system;

import com.creatoo.hn.dao.mapper.WhgSysDeptMapper;
import com.creatoo.hn.dao.model.WhgSysDept;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */
@Service
@CacheConfig(cacheNames = "WhgSysDept", keyGenerator = "simpleKeyGenerator")
public class WhgSystemDeptService extends BaseService{
    /**
     * 系统管理员类
     */
    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 部门mapper
     */
    @Autowired
    private WhgSysDeptMapper whgSysDeptMapper;

    @Cacheable
    public List<WhgSysDept> t_srchList(WhgSysDept dept, List<String> inIds)throws Exception{
        //搜索条件
        Example example = new Example(WhgSysDept.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(dept != null && dept.getName() != null){
            c.andLike("name", "%"+dept.getName()+"%");
            dept.setName(null);
        }

        //按主键
        if(inIds != null && inIds.size() > 0){
            c.andIn("id", inIds);
            dept.setId(null);
        }

        //其它条件
        c.andEqualTo(dept);

        //排序
        example.setOrderByClause("crtdate");

        //分页查询
        return this.whgSysDeptMapper.selectByExample(example);
    }

    /**
     * 查询部门列表
     * @param dept 条件对象
     * @return 部门列表
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysDept> t_srchList(WhgSysDept dept)throws Exception{
        return t_srchList(dept, null);
    }

    /**
     * 根据PID查询下级部门
     * @param cultid
     * @param pid
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysDept> t_srchList4tree(String cultid, String pid)throws Exception{
        WhgSysDept dept = new WhgSysDept();
        dept.setState(EnumState.STATE_YES.getValue());
        dept.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        dept.setPid(pid);
        dept.setCultid(cultid);
        if(StringUtils.isEmpty(pid)){
            dept.setPid("");
        }
        return t_srchList(dept, null);
    }

    /**
     *根据部门id查其下子部门id
     * @return 部门列表
     * @throws Exception
     */
    @Cacheable
    public List<String> srchDeptStrList(String deptid){
        List<String> list=new ArrayList<String>();
        list.add(deptid);
        //搜索条件
        Example example = new Example(WhgSysDept.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("pid",deptid);
        c.andEqualTo("state",EnumState.STATE_YES.getValue());
        c.andEqualTo("delstate",EnumStateDel.STATE_DEL_NO.getValue());
        List<WhgSysDept> deptList=this.whgSysDeptMapper.selectByExample(example);
       for(WhgSysDept dept:deptList){
          list.add(dept.getId());
          list=getDeptList(dept.getId(),list);
       }
        return list;
    }

    public  List  getDeptList(String deptId,List list){
        Example example = new Example(WhgSysDept.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("pid",deptId);
        c.andEqualTo("state",EnumState.STATE_YES.getValue());
        c.andEqualTo("delstate",EnumStateDel.STATE_DEL_NO.getValue());
        List<WhgSysDept> deptList=this.whgSysDeptMapper.selectByExample(example);
        for(WhgSysDept dept:deptList){
            list.add(dept.getId());
            list=getDeptList(dept.getId(),list);
        }
        return list;
    }

    /**
     * 查询单个文化馆信息
     * @param id 文化馆主键
     * @return 文化馆对象
     * @throws Exception
     */
    @Cacheable
    public WhgSysDept t_srchOne(String id)throws Exception{
        WhgSysDept record = new WhgSysDept();
        record.setId(id);
        return this.whgSysDeptMapper.selectOne(record);
    }

    /**
     * 添加文化馆
     * @param dept
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgSysDept t_add(WhgSysDept dept, WhgSysUser user)throws Exception{
        //名称不能重复
        Example example = new Example(WhgSysDept.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", dept.getName());
        c.andEqualTo("cultid", dept.getCultid());
        int count = this.whgSysDeptMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("部门名称重复");
        }

        //设置初始值
        Date now = new Date();
        dept.setId(IDUtils.getID());
        //dept.setCultid(cult.getId());
        dept.setState(EnumState.STATE_YES.getValue());
        dept.setCrtdate(now);
        dept.setCrtuser(user.getId());
        dept.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        dept.setStatemdfdate(now);
        dept.setStatemdfuser(user.getId());

        //设置CODE
        String prefixCode = "";
        Example example2 = new Example(WhgSysDept.class);
        if(dept.getPid() != null && !dept.getPid().isEmpty()){
            WhgSysDept parent = this.whgSysDeptMapper.selectByPrimaryKey(dept.getPid());
            prefixCode = parent.getCode()+"_";
            example2.createCriteria().andEqualTo("pid", dept.getPid());
        }else{
            prefixCode = "dpt";
            example2.or().andEqualTo("pid", "");
            example2.or().andIsNull("pid");
        }
        int counts = this.whgSysDeptMapper.selectCountByExample(example2);
        dept.setCode(prefixCode+""+(counts+1));

        int rows = this.whgSysDeptMapper.insertSelective(dept);
        if(rows != 1){
            throw new Exception("添加部门资料失败");
        }

        return dept;
    }

    /**
     * 编辑文化馆
     * @param dept
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgSysDept t_edit(WhgSysDept dept, WhgSysUser user)throws Exception{
        //名称不能重复
        Example example = new Example(WhgSysDept.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", dept.getName());
        c.andEqualTo("cultid", dept.getCultid());
        c.andNotEqualTo("id", dept.getId());
        int count = this.whgSysDeptMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("部门名称重复");
        }

        //设置初始值
        Date now = new Date();
        dept.setStatemdfdate(now);
        dept.setStatemdfuser(user.getId());
        int rows = this.whgSysDeptMapper.updateByPrimaryKeySelective(dept);
        if(rows != 1){
            throw new Exception("编辑部门资料失败");
        }

        return dept;
    }

    /**
     * 删除
     * @param ids 文化馆ID
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String ids, WhgSysUser user)throws Exception{
        if(ids != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgSysDept.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            this.whgSysDeptMapper.deleteByExample(example);
            this.whgSystemUserService.t_delUserCultDept(Arrays.asList(idArr));
        }
    }

    /**
     * 更新文化馆状态
     * @param ids 文化馆ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updstate(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgSysDept.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("state", fromState);
            }
            WhgSysDept record = new WhgSysDept();
            record.setState(Integer.parseInt(toState));
            record.setStatemdfdate(new Date());
            record.setStatemdfuser(user.getId());
            this.whgSysDeptMapper.updateByExampleSelective(record, example);
        }
    }

    /**
     * 根据文化馆ID查询部门
     * @param cultid
     * @return
     */
    /*public List<WhgSysDept> t_searchByCult(String cultid){
        Example example = new Example(WhgSysDept.class);
        example.createCriteria().andEqualTo("cultid",cultid).andEqualTo("state",1);
        List<WhgSysDept> whgSysDept = this.whgSysDeptMapper.selectByExample(example);
        return whgSysDept;
    }*/

    public List<WhgSysDept> t_srchExample(Example exp) throws Exception{
        if(exp == null){
            return null;
        }
        return this.whgSysDeptMapper.selectByExample(exp);
    }

}