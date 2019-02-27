package com.creatoo.hn.services.admin.system;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.login.MenusService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.MD5Util;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgSysUser", keyGenerator = "simpleKeyGenerator")
public class WhgSystemUserService extends BaseService {
    /***
     * 权限组服务
     */
    @Autowired
    private WhgSystemPmsService whgSystemPmsService;

    /**
     * 菜单服务
     */
    @Autowired
    private MenusService menusService;

    /**
     * 文化馆服务类
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 岗位服务
     */
    @Autowired
    private WhgSystemJobService whgSystemJobService;

    /**
     * 文化馆部门服务类
     */
    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    @Autowired
    private WhgSysUserMapper whgSysUserMapper;

    @Autowired
    private WhgSysUserCultMapper whgSysUserCultMapper;

    @Autowired
    private WhgSysUserCultRoleMapper whgSysUserCultRoleMapper;

    @Autowired
    private WhgSysUserCultDeptMapper whgSysUserCultDeptMapper;

    @Autowired
    private WhgCodeMapper whgCodeMapper;

    /**
     * 根据主键获取用户关联的一条文化馆记录
     * @param refid 管理员关联的机构表主键
     * @return
     * @throws Exception
     */
    public WhgSysUserCult getCultIdFromUserCultRefByUserId(String userid)throws Exception{
        WhgSysUserCult wsuc = new WhgSysUserCult();
        wsuc.setUserid(userid);
        List<WhgSysUserCult> list = this.whgSysUserCultMapper.select(wsuc);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 登录逻辑
     * @param username 登录账号
     * @param password 登录密码
     * @return 1-登录成功, 2-登录账号或者密码不正确
     * @throws Exception
     */
    public WhgSysUser doLogin(String username, String password)throws Exception{
        WhgSysUser user = null;
        try{
            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            currentUser.login(token);
            user = (WhgSysUser)currentUser.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        }catch (Exception e){
            log.debug(e.getMessage(), e);
            throw e;
        }
        return user;
    }

    /**
     * 查询管理员的所有权限分馆
     * @param userid 管理员ID
     * @return List<Map<String, String>>
     * @throws Exception
     */
    public List<Map<String, String>> loadManagerPMSCult(String userid)throws Exception{
        List<Map<String, String>> cults = new ArrayList();
        if(Constant.SUPER_USER_ID.equalsIgnoreCase(userid)){//超级管理员权限分馆-所有已经发布的文化馆
            /*WhgSysCult record = new WhgSysCult();
            record.setState(EnumBizState.STATE_PUB.getValue());
            record.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            List<WhgSysCult> cultList = this.whgSystemCultService.t_srchList(record);
            if(cultList != null){
                for(WhgSysCult cult : cultList){
                    cults.add(__formatCult2Map(cult));
                }
            }*/

            List<WhgSysCult> cultids = this.whgSystemCultService.t_srchList4Publish();
            if (cultids!=null){
                for (WhgSysCult cult : cultids) {
                    cults.add(__formatCult2Map(cult));
                }
            }
        }else{//非超级管理员权限分馆
            WhgSysUser sysUser = this.t_srchOne(userid);
            if(EnumConsoleSystem.sysmgr.getValue().equals(sysUser.getAdmintype())){//区域管理员
                /*WhgSysCult whgSysCult = new WhgSysCult();
                whgSysCult.setState(EnumBizState.STATE_PUB.getValue());
                whgSysCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                String inLevel = "";
                if(EnumCultLevel.Level_Province.getValue() == sysUser.getAdminlevel().intValue()){
                    whgSysCult.setProvince(sysUser.getAdminprovince());
                    inLevel = EnumCultLevel.Level_Province.getValue()+","+EnumCultLevel.Level_City.getValue()+","+EnumCultLevel.Level_Area.getValue();
                }else if(EnumCultLevel.Level_City.getValue() == sysUser.getAdminlevel().intValue()){
                    whgSysCult.setCity(sysUser.getAdminprovince());
                    inLevel = EnumCultLevel.Level_City.getValue()+","+EnumCultLevel.Level_Area.getValue();
                }else if(EnumCultLevel.Level_Area.getValue() == sysUser.getAdminlevel().intValue()){
                    whgSysCult.setCity(sysUser.getAdminprovince());
                    inLevel = EnumCultLevel.Level_Area.getValue()+"";
                }
                List<WhgSysCult> scults = this.whgSystemCultService.t_srchList(whgSysCult, null, null, null, inLevel, null, null);
                if (scults != null) {
                    for (WhgSysCult cult : scults) {
                        cults.add(__formatCult2Map(cult));
                    }
                }*/
                String areaLevel = null;
                String areaVal = null;
                if (sysUser.getAdminprovince()!=null && !sysUser.getAdminprovince().isEmpty()){
                    areaLevel = "1";
                    areaVal = sysUser.getAdminprovince();
                }else if(sysUser.getAdmincity()!=null && !sysUser.getAdmincity().isEmpty()){
                    areaLevel = "2";
                    areaVal = sysUser.getAdmincity();
                }else if(sysUser.getAdminarea()!=null && !sysUser.getAdminarea().isEmpty()){
                    areaLevel = "3";
                    areaVal = sysUser.getAdminarea();
                }
                List<WhgSysCult> cultids = this.whgSystemCultService.t_srchByAreaCults(areaLevel, areaVal);
                if (cultids!=null){
                    for (WhgSysCult cult : cultids) {
                        cults.add(__formatCult2Map(cult));
                    }
                }
            }else{//站点管理员
                Example example = new Example(WhgSysUserCult.class);
                example.createCriteria().andEqualTo("userid", userid);
                List<WhgSysUserCult> sucList = this.whgSysUserCultMapper.selectByExample(example);
                if(sucList != null){
                    List<String> ids = new ArrayList<>();
                    for(WhgSysUserCult suc : sucList){
                        ids.add(suc.getCultid());
                    }
                    if(ids != null && ids.size() > 0) {
                        WhgSysCult _cult = new WhgSysCult();
                        _cult.setState(EnumBizState.STATE_PUB.getValue());
                        _cult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                        List<WhgSysCult> scults = this.whgSystemCultService.t_srchList(_cult, null, StringUtils.join(ids, ","), null, null, null, null);
                        if (scults != null) {
                            for (WhgSysCult cult : scults) {
                                cults.add(__formatCult2Map(cult));
                            }
                        }
                    }
                }
            }
        }
        return cults;
    }

    /**
     * 将对象转换成map
     * @param cult
     * @return
     * @throws Exception
     */
    private Map<String, String> __formatCult2Map(WhgSysCult cult)throws Exception{
        Map<String, String> map = new HashMap<>();
        map.put("id", cult.getId());
        map.put("text", cult.getName());
        map.put("province", cult.getProvince());
        map.put("city", cult.getCity());
        map.put("area", cult.getArea());
        map.put("level", cult.getLevel()+"");
        return map;
    }

    /**
     * 权限分馆下的权限部门
     * @param userid 管理员ID
     * @return Map<String, List<Map<String, String>>> 分馆下的部门
     * @throws Exception
     */
    public Map<String, List<WhgSysDept>> loadManagerPMSDept(String userid)throws Exception{
        Map<String, List<WhgSysDept>> userCultDeptMap = new HashMap<>();
        //管理员权限分馆
        if(Constant.SUPER_USER_ID.equalsIgnoreCase(userid)){//超级管理员权限分馆部门
            /*List<String> cultidList = this.getAllCultId4PMS(userid);
            if(cultidList != null){
                for(String cultid : cultidList){
                    WhgSysDept sysDept = new WhgSysDept();
                    sysDept.setCultid(cultid);
                    sysDept.setState(EnumState.STATE_YES.getValue());
                    sysDept.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                    List<WhgSysDept> deptList = this.whgSystemDeptService.t_srchList(sysDept);
                    userCultDeptMap.put(Constant.ROOT_SYS_CULT_ID, deptList);
                }
            }*/

            WhgSysDept record = new WhgSysDept();
            record.setState(EnumState.STATE_YES.getValue());
            record.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            List<WhgSysDept> depts = this.whgSystemDeptService.t_srchList(record);
            if (depts!=null){
                for(WhgSysDept dept : depts){
                    String cultid = dept.getCultid();
                    if (!userCultDeptMap.containsKey(cultid)){
                        userCultDeptMap.put(cultid, new ArrayList());
                    }
                    List ucdList = userCultDeptMap.get(cultid);
                    ucdList.add(dept);
                }
            }
        }else{//非超级管理员权限分馆
            WhgSysUser whgSysUser = this.t_srchOne(userid);
            if(EnumConsoleSystem.sysmgr.getValue().equals(whgSysUser.getAdmintype())){
                /*WhgSysCult _record = new WhgSysCult();
                _record.setState(EnumState.STATE_YES.getValue());
                _record.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                _record.setLevel(whgSysUser.getAdminlevel());
                List<WhgSysCult> cultList = this.whgSystemCultService.t_srchList(_record);
                WhgSysDept record = new WhgSysDept();
                record.setCultid(whgSysUser.getCultid());
                record.setState(EnumState.STATE_YES.getValue());
                record.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                List<WhgSysDept> deptList = this.whgSystemDeptService.t_srchList(record);
                if(deptList != null){
                    userCultDeptMap.put(whgSysUser.getCultid(), deptList);
                }*/

                String areaLevel = null;
                String areaVal = null;
                if (whgSysUser.getAdminprovince()!=null && !whgSysUser.getAdminprovince().isEmpty()){
                    areaLevel = "1";
                    areaVal = whgSysUser.getAdminprovince();
                }else if(whgSysUser.getAdmincity()!=null && !whgSysUser.getAdmincity().isEmpty()){
                    areaLevel = "2";
                    areaVal = whgSysUser.getAdmincity();
                }else if(whgSysUser.getAdminarea()!=null && !whgSysUser.getAdminarea().isEmpty()){
                    areaLevel = "3";
                    areaVal = whgSysUser.getAdminarea();
                }
                List<String> cultids = this.whgSystemCultService.t_srchByArea(areaLevel, areaVal);
                if (cultids!=null && !cultids.isEmpty()){
                    Example exp = new Example(WhgSysDept.class);
                    exp.or().andEqualTo("state", EnumState.STATE_YES.getValue())
                            .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue())
                            .andIn("cultid", cultids);

                    List<WhgSysDept> depts = this.whgSystemDeptService.t_srchExample(exp);
                    if (depts!=null){
                        for(WhgSysDept dept : depts){
                            String cultid = dept.getCultid();
                            if (!userCultDeptMap.containsKey(cultid)){
                                userCultDeptMap.put(cultid, new ArrayList());
                            }
                            List ucdList = userCultDeptMap.get(cultid);
                            ucdList.add(dept);
                        }
                    }
                }

            }else if(EnumConsoleSystem.bizmgr.getValue().equals(whgSysUser.getAdmintype()) && whgSysUser.getIsbizmgr().intValue() == 1){//超级站点管理员部门
                WhgSysDept record = new WhgSysDept();
                record.setCultid(whgSysUser.getCultid());
                record.setState(EnumState.STATE_YES.getValue());
                record.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                List<WhgSysDept> deptList = this.whgSystemDeptService.t_srchList(record);
                if(deptList != null){
                    userCultDeptMap.put(whgSysUser.getCultid(), deptList);
                }
            }else{//普通站点管理员部门
                WhgSysUserCult wsuc = new WhgSysUserCult();
                wsuc.setUserid(userid);
                List<WhgSysUserCult> wsucList = this.whgSysUserCultMapper.select(wsuc);
                if(wsucList != null){
                    for(WhgSysUserCult wsuct : wsucList){//用户分馆
                        String refid = wsuct.getId();
                        String cultid = wsuct.getCultid();
                        boolean isPublished = this.whgSystemCultService.isPublished(cultid);
                        if(isPublished){//分馆有效
                            WhgSysUserCultDept wsucd = new WhgSysUserCultDept();
                            wsucd.setRefid(refid);
                            List<WhgSysUserCultDept> wsucdList = this.whgSysUserCultDeptMapper.select(wsucd);
                            if(wsucdList != null){
                                List<String> deptids = new ArrayList<>();
                                for(WhgSysUserCultDept wsucdtt : wsucdList){//分馆下的部门
                                    deptids.add(wsucdtt.getDeptid());
                                }
                                if(deptids.size() > 0) {
                                    WhgSysDept whgSysDept = new WhgSysDept();
                                    whgSysDept.setState(EnumState.STATE_YES.getValue());
                                    whgSysDept.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                                    List<WhgSysDept> deptList = this.whgSystemDeptService.t_srchList(whgSysDept, deptids);
                                    userCultDeptMap.put(cultid, deptList);
                                }
                            }
                        }
                    }
                }
            }//END  普通站点管理员
        }
        return userCultDeptMap;
    }

    /**
     * 查询管理员的所属文化馆信息
     * @param userid 管理员标识
     * @return
     * @throws Exception
     */
    public WhgSysCult t_srchUserCult(String userid)throws Exception{
        WhgSysUser sysUser = this.t_srchOne(userid);
        if(sysUser != null && StringUtils.isNotEmpty(sysUser.getCultid())){
            return this.whgSystemCultService.t_srchOne(sysUser.getCultid());
        }
        return null;
    }

    /**
     * 构造查询模板
     * @param user
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    private Example parseExample(WhgSysUser user, String sort, String order, List<String> cultids)throws Exception{
        //搜索条件
        Example example = new Example(WhgSysUser.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(user != null && user.getAccount() != null){
            c.andLike("account", "%"+user.getAccount()+"%");
            user.setAccount(null);
        }

        //名称条件
        if(user != null && user.getContactnum() != null){
            c.andLike("contactnum", "%"+user.getContactnum()+"%");
            user.setContactnum(null);
        }

        if(cultids != null){
            c.andIn("cultid", cultids);
            user.setCultid(null);
        }

        //其它条件
        c.andEqualTo(user);

        //排序
        this.setOrder(example, sort, order, "crtdate desc");
        return example;
    }

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
    public PageInfo<WhgSysUser> t_srchList4p(int page, int rows, String sort, String order, WhgSysUser cult, WhgSysUser admin, Integer areaLevel, String areaValue)throws Exception{
        //按区域查询账号
        List<String> cultids = null;
        if(EnumCultLevel.Level_City.getValue() == areaLevel){
            cultids = new ArrayList();
            cultids.add("1");
            WhgSysCult sysCult = new WhgSysCult();
            sysCult.setState(EnumBizState.STATE_PUB.getValue());
            sysCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            sysCult.setCity(areaValue);
            List<WhgSysCult> cultList = this.whgSystemCultService.t_srchList(sysCult, null, null, null, "2,3", null, null);
            if(cultList != null){
                for(WhgSysCult whgSysCult : cultList){
                    cultids.add(whgSysCult.getId());
                }
            }
        }else if(EnumCultLevel.Level_Area.getValue() == areaLevel){
            cultids = new ArrayList();
            cultids.add("1");
            WhgSysCult sysCult = new WhgSysCult();
            sysCult.setState(EnumBizState.STATE_PUB.getValue());
            sysCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            sysCult.setArea(areaValue);
            sysCult.setLevel(EnumCultLevel.Level_Area.getValue());
            List<WhgSysCult> cultList = this.whgSystemCultService.t_srchList(sysCult);
            if(cultList != null && cultList.size() > 0){
                for(WhgSysCult whgSysCult : cultList){
                    cultids.add(whgSysCult.getId());
                }
            }
        }

        //查询条件
        Example example = this.parseExample(cult, sort, order, cultids);

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
        Example example = this.parseExample(cult, sort, order, null);
        Example.Criteria c = example.createCriteria();

        //分页查询
        return this.whgSysUserMapper.selectByExample(example);
    }

    /**
     * 查询用户列表
     */
    public List<WhgSysUser> t_srchList() throws Exception {
        //搜索条件
        Example example = new Example(WhgSysUser.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        example.setOrderByClause("crtdate desc");
        return this.whgSysUserMapper.selectByExample(example);
    }

    /**
     * 查询单个管理员信息
     * @param id 文化馆主键
     * @return 文化馆对象
     * @throws Exception
     */
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
     * 查询单个管理员信息 通过账号
     * @return 文化馆对象
     * @throws Exception
     */
    @Cacheable
    public WhgSysUser t_srchOneByAcount(String acount)throws Exception{
        //搜索条件
        Example example = new Example(WhgSysUser.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("account", acount);
        List<WhgSysUser> list =  this.whgSysUserMapper.selectByExample(example);
        WhgSysUser user=null;
        if(list.size()>0){
            user=list.get(0);
        }
        if(user != null && user.getPasswordMemo() != null) {
            user.setPasswordMemo(MD5Util.decode4Base64(user.getPasswordMemo()));
        }
        if(user==null){
            user= this.whgSysUserMapper.selectByPrimaryKey("2015121200000000");
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
        WhgSysUserCultRole record = new WhgSysUserCultRole();
        record.setRefid(userid);
        return this.whgSysUserCultRoleMapper.select(record);
    }

    /**
     * 查询管理员岗位
     * @param userid 管理员ID
     * @return 管理员岗位
     * @throws Exception
     */
    public String t_srchUserRole(String userid)throws Exception{
        String jobid = null;
        WhgSysUserCultRole cultRole = new WhgSysUserCultRole();
        cultRole.setRefid(userid);
        cultRole = this.whgSysUserCultRoleMapper.selectOne(cultRole);
        if(cultRole != null && cultRole.getRoleid() != null){
            WhgSysJob job = this.whgSystemJobService.t_srchOne(cultRole.getRoleid());
            if(job != null){
                if(job.getState() == EnumState.STATE_YES.getValue() && job.getDelstate() == EnumStateDel.STATE_DEL_NO.getValue()){
                    jobid = job.getId();
                }
            }
        }
        return jobid;
    }

    /**
     * 添加文化馆
     * @param cult
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgSysUser t_add(WhgSysUser cult, String cult_role, String[] cult_dept, String cultids, WhgSysUser user)throws Exception{
        //清除登录缓存
        this.clearLoginCache();

        //名称不能重复
        if("administrator".equals(cult.getAccount())){
            throw new Exception("此管理员账号不允许");
        }
        Example example = new Example(WhgSysUser.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("account", cult.getAccount());
        int count = this.whgSysUserMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("账号重复");
        }

        //设置初始值
        String managerId = IDUtils.getID32();
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
        //权限岗位，权限文化馆，权限部门
        saveUserCultDeptRole(cultids, cult_role, cult_dept, managerId);

        return cult;
    }

    /**
     * 保存权限岗位，权限文化馆，权限部门
     * @param cultids
     * @param cult_role
     * @param cult_dept
     * @param managerId
     * @throws Exception
     */
    private void saveUserCultDeptRole(String cultids, String cult_role, String[] cult_dept, String managerId)throws Exception{
        //角色
        if(StringUtils.isNotEmpty(cult_role)){
            String[] t_cult_roles = cult_role.split(",");
            if(t_cult_roles != null){
                for(String t_cult_role : t_cult_roles){
                    if(StringUtils.isNotEmpty(t_cult_role)) {
                        WhgSysUserCultRole cr = new WhgSysUserCultRole();
                        cr.setId(IDUtils.getID32());
                        cr.setRefid(managerId);
                        cr.setRoleid(t_cult_role);
                        this.whgSysUserCultRoleMapper.insert(cr);
                    }
                }
            }
        }

        //权限文化馆
        if(StringUtils.isNotEmpty(cultids)){
            String[] cultidArr = cultids.split(",");
            for(String _cult : cultidArr){
                if(StringUtils.isNotEmpty(_cult)) {
                    WhgSysUserCult uc = new WhgSysUserCult();
                    String user_cult_id = IDUtils.getID32();
                    uc.setId(user_cult_id);
                    uc.setUserid(managerId);
                    uc.setCultid(_cult);
                    this.whgSysUserCultMapper.insertSelective(uc);

                    //分馆部门
                    if (cult_dept != null && cult_dept.length > 0) {
                        for (String deptid : cult_dept) {
                            WhgSysUserCultDept cultDept = new WhgSysUserCultDept();
                            cultDept.setId(IDUtils.getID32());
                            cultDept.setRefid(user_cult_id);
                            cultDept.setDeptid(deptid);
                            this.whgSysUserCultDeptMapper.insert(cultDept);
                        }
                    }
                }
            }
        }
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
    public WhgSysUser t_edit(WhgSysUser cult, String cult_role, String[] cult_dept, String cultids, WhgSysUser user)throws Exception{
        //清除登录缓存
        this.clearLoginCache();

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
            throw new Exception("编辑账号失败");
        }

        //清理用户的分馆/角色/部门
        clearUserCultDeptRole(cult.getId());

        //保存权限岗位，权限文化馆，权限部门
        saveUserCultDeptRole(cultids, cult_role, cult_dept, cult.getId());

        return cult;
    }

    /**
     * 删除用户的权限文化馆，部门和角色
     * @param userid
     * @throws Exception
     */
    private void clearUserCultDeptRole(String userid)throws Exception{
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
            }
            //删除分馆
            this.whgSysUserCultMapper.deleteByExample(example_clear);
        }

        //删除角色
        WhgSysUserCultRole cultRole = new WhgSysUserCultRole();
        cultRole.setRefid(userid);
        this.whgSysUserCultRoleMapper.delete(cultRole);
    }

    /**
     * 删除
     * @param ids 文化馆ID
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String ids, WhgSysUser user)throws Exception{
        //清除登录缓存
        this.clearLoginCache();

        if(ids != null){
            String[] idArr = ids.split(",");
            if(idArr != null){
                for(String userid : idArr){
                    //清理用户的分馆/角色/部门
                    this.clearUserCultDeptRole(userid);
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
     * 删除部门和管理员文化馆之间的关系
     * @param deptids 部门ID
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_delUserCultDept(List<String> deptids)throws Exception{
        if(deptids != null && deptids.size() > 0) {
            Example example = new Example(WhgSysUserCultDept.class);
            example.createCriteria().andIn("deptid", deptids);
            this.whgSysUserCultDeptMapper.deleteByExample(example);
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
        //清除登录缓存
        this.clearLoginCache();

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

    /**
     * 获取所有权限文化馆标识
     * @param userId 管理员标识
     * @return List<String> 所有权限文化馆标识
     * @throws Exception
     */
    public List<String> getAllCultId4PMS(String userId)throws Exception{
        //文化馆
        List<String> cultids = new ArrayList<>();//权限文化馆，如果没有指定，则默认所有的权限文化馆
        List<Map<String, String>> _list = this.loadManagerPMSCult(userId);
        if(_list != null){
            for(Map<String, String> _map : _list){
                cultids.add(_map.get("id"));
            }
        }
        return cultids;
    }

    /**
     * 获取管理员所有权限馆下的所有权限部门
     * @param userId 管理员标识
     * @return List<String> 所有权限部门标识
     * @throws Exception
     */
    public List<String> getAllDeptId4PMS(String userId)throws Exception{
        //部门
        List<String> deptids = new ArrayList<>();//根据权限馆查询所有权限部门
        Map<String, List<WhgSysDept>> _map = this.loadManagerPMSDept(userId);
        if(_map != null){
            for(String cultid : _map.keySet()){
                List<WhgSysDept> deptList = _map.get(cultid);
                if(deptList != null){
                    for(WhgSysDept dept : deptList){
                        deptids.add(dept.getId());
                    }
                }
            }
        }
        return deptids;
    }


    /**
     * 修改密码
     * @param account 管理员账号
     * @param password2 修改后的密码
     * @param password4 修改后的密码明文
     * @return
     */
    public void selectmagr(String account, String password2, String password4) throws Exception {
        WhgSysUser whgSysUser = new WhgSysUser();
        whgSysUser.setPassword(password2);
        whgSysUser.setPasswordMemo(MD5Util.encode4Base64(password4));
        Example example = new Example(WhgSysUser.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("account", account);
        this.whgSysUserMapper.updateByExampleSelective(whgSysUser, example);
    }

    /**
     * 根据用户标识获到用户所有的权限字符串
     * @param userid 用户标识
     * @return 所有权限
     * @throws Exception
     */
    public List<String> getUserAllPmsStr(String userid)throws Exception{
        List<String> pmsList = new ArrayList<>();

        //超级管理员
        if(Constant.SUPER_USER_ID.equalsIgnoreCase(userid)){
            //所有权限
            List<Map> menulist = this.menusService.getMenusList4Sysflag(EnumConsoleSystem.sysmgr.getValue());//过滤了无效菜单
            if(menulist != null){
                for(Map menu : menulist){
                    if(menu.containsKey("opts") && menu.containsKey("id")){
                        String m_opts = (String)menu.get("opts");
                        String m_id = (String)menu.get("id");
                        if(m_opts != null && m_id != null && !m_opts.isEmpty() && !m_id.isEmpty()){
                            String[] _ops = m_opts.split(",");
                            for(String _op : _ops){
                                String t_op = _op.trim();
                                pmsList.add(m_id.trim()+":"+t_op);
                            }
                        }
                    }
                }
            }
        }else{//普通管理员
            // 查询用户的角色
            WhgSysUser mgr = (WhgSysUser)this.t_srchOne(userid);
            if (EnumConsoleSystem.bizmgr.getValue().equals(mgr.getAdmintype()) && mgr.getIsbizmgr().intValue() == 1) {
                //文化馆超级管理员所有站点权限组的权限
                WhgSysPms _whgSysPms = new WhgSysPms();
                List<WhgSysPms> jobPmsList = this.whgSystemPmsService.t_srchList(_whgSysPms, null, null, mgr.getCultid());
                for (WhgSysPms sysPms : jobPmsList) {
                    if (sysPms.getState().intValue() == EnumState.STATE_YES.getValue() &&
                            sysPms.getDelstate().intValue() == EnumStateDel.STATE_DEL_NO.getValue()) {
                        List<WhgSysPmsDetail> pmsDetails = this.whgSystemPmsService.t_srchPmsDetail(sysPms.getId());
                        for (WhgSysPmsDetail detail : pmsDetails) {
                            pmsList.add(detail.getPmsstr());
                        }
                    }
                }
            } else {// 普通站点管理员和区域管理员通过岗们获取菜单权限
                List<WhgSysUserCultRole> ucrs = this.t_srchUserPMSCultRoles(mgr.getId());
                if (ucrs != null && ucrs.size() > 0) {
                    for (WhgSysUserCultRole ur : ucrs) {
                        String roleid = ur.getRoleid();
                        WhgSysUserCult whgSysUserCult = this.getCultIdFromUserCultRefByUserId(mgr.getId());
                        if (roleid != null && !roleid.isEmpty()) {
                            //角色必须有效
                            boolean isEnable = this.whgSystemJobService.isEnable(roleid);
                            if (isEnable) {
                                //查询角色权限
                                List<WhgSysJobPms> jobPmsList = this.whgSystemJobService.t_srchJobPms(roleid);
                                if (jobPmsList != null) {
                                    for (WhgSysJobPms whgSysJobPms : jobPmsList) {
                                        String pmsid = whgSysJobPms.getPmsid();
                                        boolean _isEnable = this.whgSystemPmsService.isEnable(pmsid, whgSysUserCult, mgr.getAdmintype());
                                        if (_isEnable) {
                                            List<WhgSysPmsDetail> pmsDetails = this.whgSystemPmsService.t_srchPmsDetail(pmsid);
                                            for (WhgSysPmsDetail detail : pmsDetails) {
                                                pmsList.add(detail.getPmsstr());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return pmsList;
    }

    /**
     * 群文用户验证手机号是否存在
     * @param phone
     * @return
     */
    public boolean isPhone(String phone) {
        WhgSysUser user = new WhgSysUser();
        user.setContactnum(phone);
        user.setAdmintype(EnumConsoleSystem.masmgr.getValue());
        int count = this.whgSysUserMapper.selectCount(user);
        return count>0;
    }

    /**
     * 找回密码
     * @param mobile
     * @param code
     * @param password
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object setPasswd(String mobile, String code, String password) throws Exception{
        ApiResultBean arb = new ApiResultBean();

        if (mobile==null || mobile.isEmpty() || code==null || code.isEmpty()){
            arb.setCode(103);
            arb.setMsg("手机号不能为空");
            return arb;
        }

        boolean isSmsCode = this.validSmsCode(mobile, code);
        if (!isSmsCode){
            arb.setCode(102);
            arb.setMsg("验证码校验错误");
            return arb;
        }

        if (password==null || password.isEmpty()){
            arb.setCode(103);
            arb.setMsg("密码不能为空");
            return arb;
        }

        if (!mobile.matches("1[3-8]\\d{9}")){
            arb.setCode(103);
            arb.setMsg("手机格式错误");
            return arb;
        }

        WhgSysUser user = new WhgSysUser();
        user.setContactnum(mobile);
        user.setAdmintype(EnumConsoleSystem.masmgr.getValue());
        List<WhgSysUser> list = this.whgSysUserMapper.select(user);
        if (list==null || list.size()==0){
            arb.setCode(103);
            arb.setMsg("手机号不存在");
            return arb;
        }

        for(WhgSysUser ent : list){
            WhgSysUser recode = new WhgSysUser();
            recode.setId(ent.getId());
            recode.setPassword(password);
            this.whgSysUserMapper.updateByPrimaryKeySelective(recode);
        }

        return arb;
    }


    /**
     * 验证手机短信验证码
     * @param phone
     * @param code
     * @return
     * @throws Exception
     */
    public boolean validSmsCode(String phone, String code) throws Exception{
        Calendar ctime = Calendar.getInstance();
        ctime.add(Calendar.MINUTE, -5);

        Example example = new Example(WhgCode.class);
        example.createCriteria()
                .andEqualTo("msgphone", phone)
                .andEqualTo("msgcontent", code)
                .andGreaterThanOrEqualTo("msgtime", ctime.getTime());
        int usecode = this.whgCodeMapper.selectCountByExample(example);
        return usecode > 0;
    }
}
