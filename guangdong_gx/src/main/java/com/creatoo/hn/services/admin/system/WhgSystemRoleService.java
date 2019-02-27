package com.creatoo.hn.services.admin.system;

import com.creatoo.hn.dao.mapper.WhgSysRoleMapper;
import com.creatoo.hn.dao.mapper.WhgSysRolePmsMapper;
import com.creatoo.hn.dao.model.WhgSysRole;
import com.creatoo.hn.dao.model.WhgSysRolePms;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 角色|岗位服务类
 * Created by wangxl on 2017/3/18.
 */
@Service
@CacheConfig(cacheNames = "WhgSysRole", keyGenerator = "simpleKeyGenerator")
public class WhgSystemRoleService extends BaseService {
    /**
     * 角色DAO
     */
    @Autowired
    private WhgSysRoleMapper whgSysRoleMapper;

    /**
     * 角色权限DAO
     */
    @Autowired
    private WhgSysRolePmsMapper whgSysRolePmsMapper;

    /**
     * 登录缓存
     */
    @Autowired
    private MemoryConstrainedCacheManager memoryConstrainedCacheManager;

    /**
     * 分页查询角色信息
     * @param page 第几页
     * @param rows 每页记录数
     * @param sort 排序字段
     * @param order 排序方式
     * @param role 条件信息
     * @return 分页角色信息
     * @throws Exception
     */
    @Cacheable
    public PageInfo<WhgSysRole> t_srchList4p(int page, int rows, String sort, String order, WhgSysRole role)throws Exception{
        //搜索条件
        Example example = new Example(WhgSysRole.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(role != null && StringUtils.isNotEmpty(role.getName())){
            c.andLike("name", "%"+role.getName()+"%");
            role.setName(null);
        }

        //其它条件
        c.andEqualTo(role);

        //排序
        if(StringUtils.isNotEmpty(sort)){
            StringBuffer sb = new StringBuffer(sort);
            if(StringUtils.isNotEmpty(order) && "desc".equalsIgnoreCase(order)){
                sb.append(" ").append(order);
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("crtdate desc");
        }

       /* example.or(example.createCriteria().andNotEqualTo("id", "1").andNotEqualTo("name", "1").andIn("state", Arrays.asList(new String[]{"1", "2"})));


        List<String> crList = new ArrayList();
        List<Example.Criteria> cs = example.getOredCriteria();
        for(Example.Criteria crit : cs){
            StringBuffer sb_c = new StringBuffer();
            String sp = "";
            List<Example.Criterion> criterionList = crit.getAllCriteria();
            for(Example.Criterion crions : criterionList){
                sb_c.append(sp).append(crions.getCondition()).append(" ").append(crions.getValue());
            }
            crList.add(sb_c.toString());

            crit.andNotEqualTo("id", "1");


        }*/

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgSysRole> list = this.whgSysRoleMapper.selectByExample(example);
        return new PageInfo<WhgSysRole>(list);
    }

    /**
     * 查询角色的所有权限
     * @param roleid 角色标识
     * @return List<WhgSysRolePms>
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysRolePms> t_srchRoleAllPMS(String roleid)throws Exception{
        Example pms_example = new Example(WhgSysRolePms.class);
        pms_example.createCriteria().andEqualTo("roleid", roleid)
                .andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        return this.whgSysRolePmsMapper.selectByExample(pms_example);
    }


    /**
     * 查询角色列表
     * @param sort 排序字段
     * @param order 排序列表
     * @param role 查询条件
     * @return 角色列表
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysRole> t_srchList(String sort, String order, WhgSysRole role)throws Exception{

        //搜索条件
        Example example = new Example(WhgSysRole.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(role != null && role.getName() != null){
            c.andLike("name", "%"+role.getName()+"%");
            role.setName(null);
        }

        //其它条件
        c.andEqualTo(role);

        //排序
        if(StringUtils.isNotEmpty(sort)){
            StringBuffer sb = new StringBuffer(sort);
            if(StringUtils.isNotEmpty(order) && "desc".equalsIgnoreCase(order)){
                sb.append(" ").append(order);
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("crtdate desc");
        }

        //分页查询
        return this.whgSysRoleMapper.selectByExample(example);
    }

    /**
     * 查询单个文化馆信息
     * @param id 文化馆主键
     * @return 文化馆对象
     * @throws Exception
     */
    @Cacheable
    public WhgSysRole t_srchOne(String id)throws Exception{
        WhgSysRole record = new WhgSysRole();
        record.setId(id);
        return this.whgSysRoleMapper.selectOne(record);
    }

    /**
     * 角色是否有效
     * @param roleid 角色标识
     * @return true-有效 false-角色无效
     * @throws Exception
     */
    @Cacheable
    public boolean isEnable(String roleid)throws Exception{
        Example role_example = new Example(WhgSysRole.class);
        role_example.createCriteria().andEqualTo("id", roleid).andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

        return this.whgSysRoleMapper.selectCountByExample(role_example) == 1;
    }



    /**
     * 查询角色权限
     * @param id 角色ID
     * @return 角色权限列表
     * @throws Exception
     */
    @Cacheable
    public List<String> t_srchRolePms(String id)throws Exception{
        Example example_pms = new Example(WhgSysRolePms.class);
        String str[]=id.split(",");
        example_pms.createCriteria().andIn("roleid", Arrays.asList(str));
        List<WhgSysRolePms> rpmsList = this.whgSysRolePmsMapper.selectByExample(example_pms);
        List<String> pmsList = new ArrayList<String>();
        if(rpmsList != null){
            for(WhgSysRolePms wsrp : rpmsList){
                pmsList.add(wsrp.getPmsstr());
            }
        }
        return pmsList;
    }

    /**
     * 查询文化館角色权限
     * @param cultid 文化館ID
     * @return 角色权限列表
     * @throws Exception
     */
    public List<String> t_srchCultRolePms(String cultid)throws Exception{
        Example example = new Example(WhgSysRole.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("cultid", cultid);
        List<WhgSysRole> ucrList = this.whgSysRoleMapper.selectByExample(example);
        List list=new ArrayList();
        if(ucrList != null) {
            for (WhgSysRole ucr : ucrList) {
                list.add(ucr.getId());
            }
        }
       String ids= StringUtils.join(list.toArray(), ",");
        List rpmsList= this.t_srchRolePms(ids);
        return rpmsList;
    }

    /**
     * 查询用戶角色权限
     * @param userid 用户标识
     * @return 角色权限列表
     * @throws Exception
     */
    public List<String> t_srchUserRolePms(String userid)throws Exception{
       Map map =new HashMap();
        map.put("userid", userid);
        List<Map> list= this.whgSysRolePmsMapper.srchRoleList(map);
        List rolelist=new ArrayList();
        if(list != null) {
            for (Map cmap : list) {
                rolelist.add(cmap.get("roleid"));
            }
        }
        String ids= StringUtils.join(rolelist.toArray(), ",");
        List rpmsList= this.t_srchRolePms(ids);
        return rpmsList;
    }
    /**
     * 添加文化馆
     * @param cult
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgSysRole t_add(WhgSysRole cult, String[] pms, WhgSysUser user)throws Exception{
        try {
            memoryConstrainedCacheManager.getCache("shiro_redis_cache").clear();
            memoryConstrainedCacheManager.getCache("com.creatoo.hn.web.config.ShiroConfigShiroRealm.authorizationCache").clear();
        }catch (Exception e){}

        //名称不能重复
        Example example = new Example(WhgSysRole.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", cult.getName());
        c.andEqualTo("cultid", cult.getCultid());
        int count = this.whgSysRoleMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("角色名称重复");
        }

        //设置初始值
        Date now = new Date();
        String roleId = IDUtils.getID32();//commService.getKey("whg_sys_role");
        cult.setId(roleId);
        cult.setState(EnumState.STATE_YES.getValue());
        cult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        cult.setCrtdate(now);
        cult.setCrtuser(user.getId());
        cult.setStatemdfdate(now);
        cult.setStatemdfuser(user.getId());
        int rows = this.whgSysRoleMapper.insertSelective(cult);
        if(rows != 1){
            throw new Exception("添加角色失败");
        }

        //添加角色权限
        if(pms != null){
            List<WhgSysRolePms> srpList = new ArrayList();
            for(String p : pms){
                WhgSysRolePms rpms = new WhgSysRolePms();
                rpms.setId(IDUtils.getID32());
                rpms.setState(EnumState.STATE_YES.getValue());
                rpms.setCrtdate(now);
                rpms.setCrtuser(user.getId());
                rpms.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                rpms.setStatemdfdate(now);
                rpms.setStatemdfuser(user.getId());
                rpms.setRoleid(roleId);
                rpms.setPmsstr(p);
                //this.whgSysRolePmsMapper.insert(rpms);
                srpList.add(rpms);
            }
            this.whgSysRolePmsMapper.batchInsert(srpList);
        }

        return cult;
    }

    /**
     * 编辑文化馆
     * @param cult
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgSysRole t_edit(WhgSysRole cult, String[] pms, WhgSysUser user)throws Exception{
        try {
            memoryConstrainedCacheManager.getCache("shiro_redis_cache").clear();
            memoryConstrainedCacheManager.getCache("com.creatoo.hn.web.config.ShiroConfigShiroRealm.authorizationCache").clear();
        }catch (Exception e){}

        //名称不能重复
        Example example = new Example(WhgSysRole.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", cult.getName());
        c.andEqualTo("cultid", cult.getCultid());
        c.andNotEqualTo("id", cult.getId());
        int count = this.whgSysRoleMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("角色名称重复");
        }

        //设置初始值
        Date now = new Date();
        cult.setStatemdfdate(now);
        cult.setStatemdfuser(user.getId());
        int rows = this.whgSysRoleMapper.updateByPrimaryKeySelective(cult);
        if(rows != 1){
            throw new Exception("编辑角色失败");
        }

        //添加角色权限
        Example examplex = new Example(WhgSysRolePms.class);
        Example.Criteria c2 = examplex.createCriteria();
        c2.andEqualTo("roleid", cult.getId());
        this.whgSysRolePmsMapper.deleteByExample(examplex);
        if(pms != null){
            List<WhgSysRolePms> srpList = new ArrayList();
            for(String p : pms){
                WhgSysRolePms rpms = new WhgSysRolePms();
                rpms.setId(IDUtils.getID32());
                rpms.setState(EnumState.STATE_YES.getValue());
                rpms.setCrtdate(now);
                rpms.setCrtuser(user.getId());
                rpms.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                rpms.setStatemdfdate(now);
                rpms.setStatemdfuser(user.getId());
                rpms.setRoleid(cult.getId());
                rpms.setPmsstr(p);
                //this.whgSysRolePmsMapper.insert(rpms);
                srpList.add(rpms);
            }
            this.whgSysRolePmsMapper.batchInsert(srpList);
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
        try {
            memoryConstrainedCacheManager.getCache("shiro_redis_cache").clear();
            memoryConstrainedCacheManager.getCache("com.creatoo.hn.web.config.ShiroConfigShiroRealm.authorizationCache").clear();
        }catch (Exception e){}

        if(ids != null){
            String[] idArr = ids.split(",");

            Example examplex = new Example(WhgSysRolePms.class);
            Example.Criteria c2 = examplex.createCriteria();
            c2.andIn("roleid", Arrays.asList(idArr));
            this.whgSysRolePmsMapper.deleteByExample(examplex);

            Example example = new Example(WhgSysRole.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            this.whgSysRoleMapper.deleteByExample(example);

        }
    }

    /**
     * 更新角色状态
     * @param ids 文化馆ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updstate(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        try {
            memoryConstrainedCacheManager.getCache("shiro_redis_cache").clear();
            memoryConstrainedCacheManager.getCache("com.creatoo.hn.web.config.ShiroConfigShiroRealm.authorizationCache").clear();
        }catch (Exception e){}

        if(ids != null && toState != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgSysRole.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("state", fromState);
            }
            WhgSysRole record = new WhgSysRole();
            record.setState(Integer.parseInt(toState));
            record.setStatemdfdate(new Date());
            record.setStatemdfuser(user.getId());
            this.whgSysRoleMapper.updateByExampleSelective(record, example);
        }
    }
}
