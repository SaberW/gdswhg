package com.creatoo.hn.services.admin.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.MD5Util;
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

import java.util.*;

/**
 * 管理员服务类
 * Created by wangxl on 2017/3/18.
 */
@Service
@CacheConfig(cacheNames = "WhgSysUser", keyGenerator = "simpleKeyGenerator")
public class WhgSystemUserService extends BaseService {

    @Autowired
    private WhgSysUserMapper whgSysUserMapper;

    @Autowired
    private WhgSysUserCultMapper whgSysUserCultMapper;

    @Autowired
    private WhgSysUserCultRoleMapper whgSysUserCultRoleMapper;

    @Autowired
    private WhgSysUserCultDeptMapper whgSysUserCultDeptMapper;


    /**
     * 分页查询管理员信息
     * @param page 第几页
     * @param rows 每页数
     * @param sort 排序字段
     * @param order 排序方式
     * @param cult 查询条件
     * @return PageInfo<WhgSysUser>
     * @throws Exception
     */
    @Cacheable
    public PageInfo<WhgSysUser> t_srchList4p(int page, int rows, String sort, String order, WhgSysUser cult)throws Exception{
        //搜索条件
        Example example = new Example(WhgSysUser.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(cult != null && cult.getAccount() != null){
            c.andLike("account", "%"+cult.getAccount()+"%");
            cult.setAccount(null);
        }

        //其它条件
        c.andEqualTo(cult);

        //排序
        if(StringUtils.isNotEmpty(sort)){
            StringBuffer sb = new StringBuffer(sort);
            if(StringUtils.isNotEmpty(order)){
                sb.append(" ").append(order);
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("crtdate desc");
        }

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgSysUser> list = this.whgSysUserMapper.selectByExample(example);
        return new PageInfo<WhgSysUser>(list);
    }

    /**
     * 查询文化馆列表
     * @param sort 排序字段
     * @param order 排序方式
     * @param cult 查询条件
     * @return List<WhgSysUser>
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysUser> t_srchList(String sort, String order, WhgSysUser cult)throws Exception{
        //搜索条件
        Example example = new Example(WhgSysUser.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(cult != null && cult.getAccount() != null){
            c.andLike("name", "%"+cult.getAccount()+"%");
            cult.setAccount(null);
        }

        //其它条件
        c.andEqualTo(cult);

        //排序
        if(StringUtils.isNotEmpty(sort)){
            StringBuffer sb = new StringBuffer(sort);
            if(StringUtils.isNotEmpty(order)){
                sb.append(" ").append(order);
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("crtdate desc");
        }

        //分页查询
        return this.whgSysUserMapper.selectByExample(example);
    }

    /**
     * 查询单个管理员信息
     * @param id 文化馆主键
     * @return 文化馆对象
     * @throws Exception
     */
    @Cacheable
    public WhgSysUser t_srchOne(String id)throws Exception{
        WhgSysUser user =  this.whgSysUserMapper.selectByPrimaryKey(id);
        if(user != null && user.getPasswordMemo() != null) {
            user.setPasswordMemo(MD5Util.decode4Base64(user.getPasswordMemo()));
        }
        if("2015121200000000".equals(id)){
            user = new WhgSysUser();
            user.setId("2015121200000000");
            user.setAccount("administrator");
        }
        return user;
    }

    /**
     * 根据账号和密码查询用户信息
     * @param userName 管理员账号
     * @param password 管理员密码
     * @return WhgSysUser
     * @throws Exception
     */
    @Cacheable
    public WhgSysUser t_srchOneByNameAndPwd(String userName, String password)throws Exception{
        WhgSysUser record = new WhgSysUser();
        record.setAccount(userName);
        record.setPassword(password);
        record.setState(EnumState.STATE_YES.getValue());
        return this.whgSysUserMapper.selectOne(record);
    }

    /**
     * 根据用户ID获取所有角色
     * @param userid 用户标识
     * @return List<WhgSysUserCultRole>
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysUserCultRole> t_srchUserPMSCultRoles(String userid)throws Exception{
        List<WhgSysUserCultRole> ucrs = new ArrayList<>();
        Example example_uc = new Example(WhgSysUserCult.class);
        example_uc.createCriteria().andEqualTo("userid", userid);
        List<WhgSysUserCult> ucList = this.whgSysUserCultMapper.selectByExample(example_uc);
        if(ucList != null) {
            List<String> ids = new ArrayList<>();
            for (WhgSysUserCult userCult : ucList) {
                ids.add(userCult.getId());
            }
            Example example_ucr = new Example(WhgSysUserCultRole.class);
            example_ucr.createCriteria().andIn("refid", ids);
            ucrs = this.whgSysUserCultRoleMapper.selectByExample(example_ucr);
        }
        return ucrs;
    }

    /**
     * 查询管理员权限分馆，角色，部门
     * @param userid 管理员ID
     * @return 管理员权限分馆，角色，部门
     * @throws Exception
     */
    @Cacheable
    public JSONObject t_srchUserCult_Role_Dept(String userid)throws Exception{
        JSONObject json = new JSONObject();
        //权限分馆
        Example example = new Example(WhgSysUserCult.class);
        example.createCriteria().andEqualTo("userid", userid);
        List<WhgSysUserCult> ucList = this.whgSysUserCultMapper.selectByExample(example);
        if(ucList != null){
            JSONArray jsonArr = new JSONArray();
            for(WhgSysUserCult uc : ucList){
                jsonArr.add(uc.getCultid());
                String refid = uc.getId();

                //分馆角色
                Example example_ucr = new Example(WhgSysUserCultRole.class);
                example_ucr.createCriteria().andEqualTo("refid", refid);
                List<WhgSysUserCultRole> ucrList = this.whgSysUserCultRoleMapper.selectByExample(example_ucr);
                if(ucrList != null){
                    JSONArray jsonArrRole = new JSONArray();
                    for(WhgSysUserCultRole ucr : ucrList){
                        jsonArrRole.add(ucr.getRoleid());
                    }
                    json.put("pms_role_"+uc.getCultid(), jsonArrRole);
                }

                //分馆部门
                Example example_ucd = new Example(WhgSysUserCultDept.class);
                example_ucd.createCriteria().andEqualTo("refid", refid);
                List<WhgSysUserCultDept> ucdList = this.whgSysUserCultDeptMapper.selectByExample(example_ucd);
                if(ucdList != null){
                    JSONArray jsonArrDept = new JSONArray();
                    for(WhgSysUserCultDept ucd : ucdList){
                        jsonArrDept.add(ucd.getDeptid());
                    }
                    json.put("pms_dept_"+uc.getCultid(), jsonArrDept);
                }
            }
            json.put("pms_cult", jsonArr);
        }
        return json;
    }

    /**
     * 添加文化馆
     * @param cult
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgSysUser t_add(WhgSysUser cult, Map<String, String[]> cult_role, Map<String, String[]> cult_dept, String[] cultids, WhgSysUser user)throws Exception{
        //名称不能重复
        if("administrator".equals(cult.getAccount())){
            throw new Exception("此管理员账号不允许");
        }
        Example example = new Example(WhgSysUser.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("account", cult.getAccount());
        int count = this.whgSysUserMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("管理员账号重复");
        }

        //设置初始值
        String managerId = IDUtils.getID();
        Date now = new Date();
        cult.setId(managerId);
        cult.setState(EnumState.STATE_YES.getValue());
        cult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        cult.setCrtdate(now);
        cult.setCrtuser(user.getId());
        cult.setStatemdfdate(now);
        cult.setStatemdfuser(user.getId());
        int rows = this.whgSysUserMapper.insertSelective(cult);
        if(rows != 1){
            throw new Exception("添加管理员账号失败");
        }

        //文化馆
        if(cultids != null){
            for(String _cult : cultids){
                WhgSysUserCult uc = new WhgSysUserCult();
                String user_cult_id = IDUtils.getID();
                uc.setId(user_cult_id);
                uc.setUserid(managerId);
                uc.setCultid(_cult);
                this.whgSysUserCultMapper.insertSelective(uc);

                //分馆角色
                if(cult_role != null && cult_role.containsKey(_cult)){
                    String[] t_cult_roles = cult_role.get(_cult);
                    if(t_cult_roles != null){
                        for(String t_cult_role : t_cult_roles){
                            if(StringUtils.isNotEmpty(t_cult_role)) {
                                WhgSysUserCultRole cr = new WhgSysUserCultRole();
                                cr.setId(IDUtils.getID());
                                cr.setRefid(user_cult_id);
                                cr.setRoleid(t_cult_role);
                                this.whgSysUserCultRoleMapper.insert(cr);
                            }
                        }
                    }
                }

                //分馆部门
                if(cult_dept != null && cult_dept.containsKey(_cult)){
                    String[] t_cult_depts = cult_dept.get(_cult);
                    if(t_cult_depts != null){
                        for(String t_cult_dept : t_cult_depts){
                            if(StringUtils.isNotEmpty(t_cult_dept)) {
                                WhgSysUserCultDept cr = new WhgSysUserCultDept();
                                cr.setId(IDUtils.getID());
                                cr.setRefid(user_cult_id);
                                cr.setDeptid(t_cult_dept);
                                this.whgSysUserCultDeptMapper.insert(cr);
                            }
                        }
                    }
                }
            }
        }

        return cult;
    }

    /**
     * 更新管理员基本信息
     * @param sysUser
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_editBaseInfo(WhgSysUser sysUser)throws Exception{
        this.whgSysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    /**
     * 编辑文化馆
     * @param cult
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgSysUser t_edit(WhgSysUser cult, Map<String, String[]> cult_role, Map<String, String[]> cult_dept, String[] cultids, WhgSysUser user)throws Exception{
        //名称不能重复
        if("administrator".equals(cult.getAccount())){
            throw new Exception("此管理员账号不允许");
        }
        Example example = new Example(WhgSysUser.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("account", cult.getAccount());
        c.andNotEqualTo("id", cult.getId());
        int count = this.whgSysUserMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("管理理员账号重复");
        }

        //设置初始值
        Date now = new Date();
        cult.setStatemdfdate(now);
        cult.setStatemdfuser(user.getId());
        int rows = this.whgSysUserMapper.updateByPrimaryKeySelective(cult);
        if(rows != 1){
            throw new Exception("编辑理员账号失败");
        }

        //清理用户的分馆/角色/部门
        Example example_clear = new Example(WhgSysUserCult.class);
        example_clear.createCriteria().andEqualTo("userid", cult.getId());
        List<WhgSysUserCult> ucList = this.whgSysUserCultMapper.selectByExample(example_clear);
        if(ucList != null){
            for(WhgSysUserCult uc : ucList){
                String ucid = uc.getId();

                //删除分馆部门
                Example example_clear_dept = new Example(WhgSysUserCultDept.class);
                example_clear_dept.createCriteria().andEqualTo("refid", ucid);
                this.whgSysUserCultDeptMapper.deleteByExample(example_clear_dept);

                //删除分馆角色
                Example example_clear_role = new Example(WhgSysUserCultRole.class);
                example_clear_role.createCriteria().andEqualTo("refid", ucid);
                this.whgSysUserCultRoleMapper.deleteByExample(example_clear_role);
            }
            //删除分馆
            this.whgSysUserCultMapper.deleteByExample(example_clear);
        }

        //文化馆
        if(cultids != null){
            for(String _cult : cultids){
                WhgSysUserCult uc = new WhgSysUserCult();
                String user_cult_id = IDUtils.getID();
                uc.setId(user_cult_id);
                uc.setUserid(cult.getId());
                uc.setCultid(_cult);
                this.whgSysUserCultMapper.insertSelective(uc);

                //分馆角色
                if(cult_role != null && cult_role.containsKey(_cult)){
                    String[] t_cult_roles = cult_role.get(_cult);
                    if(t_cult_roles != null){
                        for(String t_cult_role : t_cult_roles){
                            if(StringUtils.isNotEmpty(t_cult_role)){
                                WhgSysUserCultRole cr = new WhgSysUserCultRole();
                                cr.setId(IDUtils.getID());
                                cr.setRefid(user_cult_id);
                                cr.setRoleid(t_cult_role);
                                this.whgSysUserCultRoleMapper.insert(cr);
                            }
                        }
                    }
                }

                //分馆部门
                if(cult_dept != null && cult_dept.containsKey(_cult)){
                    String[] t_cult_depts = cult_dept.get(_cult);
                    if(t_cult_depts != null){
                        for(String t_cult_dept : t_cult_depts){
                            if(StringUtils.isNotEmpty(t_cult_dept)) {
                                WhgSysUserCultDept cr = new WhgSysUserCultDept();
                                cr.setId(IDUtils.getID());
                                cr.setRefid(user_cult_id);
                                cr.setDeptid(t_cult_dept);
                                this.whgSysUserCultDeptMapper.insert(cr);
                            }
                        }
                    }
                }
            }
        }

        return cult;
    }

    /**
     * 删除文化馆
     * @param ids 文化馆ID
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String ids, WhgSysUser user)throws Exception{
        if(ids != null){
            String[] idArr = ids.split(",");
            if(idArr != null){
                for(String userid : idArr){
                    //清理用户的分馆/角色/部门
                    Example example_clear = new Example(WhgSysUserCult.class);
                    example_clear.createCriteria().andEqualTo("userid", userid);
                    List<WhgSysUserCult> ucList = this.whgSysUserCultMapper.selectByExample(example_clear);
                    if(ucList != null){
                        for(WhgSysUserCult uc : ucList){
                            String ucid = uc.getId();

                            //删除分馆部门
                            Example example_clear_dept = new Example(WhgSysUserCultDept.class);
                            example_clear_dept.createCriteria().andEqualTo("refid", ucid);
                            this.whgSysUserCultDeptMapper.deleteByExample(example_clear_dept);

                            //删除分馆角色
                            Example example_clear_role = new Example(WhgSysUserCultRole.class);
                            example_clear_role.createCriteria().andEqualTo("refid", ucid);
                            this.whgSysUserCultRoleMapper.deleteByExample(example_clear_role);
                        }
                        //删除分馆
                        this.whgSysUserCultMapper.deleteByExample(example_clear);
                    }
                }

                //删除用户
                Example example = new Example(WhgSysUser.class);
                Example.Criteria c = example.createCriteria();
                c.andIn("id", Arrays.asList(idArr));
                this.whgSysUserMapper.deleteByExample(example);
            }
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
            Example example = new Example(WhgSysUser.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("state", fromState);
            }
            WhgSysUser record = new WhgSysUser();
            record.setState(Integer.parseInt(toState));
            record.setStatemdfdate(new Date());
            record.setStatemdfuser(user.getId());
            this.whgSysUserMapper.updateByExampleSelective(record, example);
        }
    }
}
