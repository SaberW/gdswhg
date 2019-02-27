package com.creatoo.hn.services.admin.login;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgSysCultMapper;
import com.creatoo.hn.dao.mapper.WhgSysDeptMapper;
import com.creatoo.hn.dao.mapper.WhgSysUserCultDeptMapper;
import com.creatoo.hn.dao.mapper.WhgSysUserCultMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 控制台登录
 * Created by wangxl on 2017/7/13.
 */
@SuppressWarnings("ALL")
@Service
public class LoginService extends BaseService{
    @Autowired
    private WhgSysUserCultMapper whgSysUserCultMapper;

    @Autowired
    private WhgSysCultMapper whgSysCultMapper;

    @Autowired
    private WhgSysUserCultDeptMapper whgSysUserCultDeptMapper;

    @Autowired
    private WhgSysDeptMapper whgSysDeptMapper;

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
        }
        return user;
    }

    /**
     * 查询管理员的所有权限分馆及其权限分馆的子馆
     * @param id 管理员ID
     * @return 所有权限分馆
     * @throws Exception
     */
    public List<Map<String, String>> loadManagerCult(String id,String userName)throws Exception{
        List<Map<String, String>> cults = new ArrayList();
        if(userName!=null&&userName.equals("admin")){
            Example example = new Example(WhgSysCult.class);
            example.createCriteria()
                    .andEqualTo("state", EnumState.STATE_YES.getValue())
                    .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
            List<WhgSysCult> sucList = this.whgSysCultMapper.selectByExample(example);
            if(sucList != null){
                for(WhgSysCult cult : sucList){
                    Map<String, String> map_t = new HashMap<>();
                    map_t.put("id", cult.getId());
                    map_t.put("text", cult.getName());
                    map_t.put("pid", cult.getPid());
                    cults.add(map_t);
                }
            }
        }else  if(Constant.SUPER_USER_ID.equalsIgnoreCase(id)){//超级管理员权限分馆及其权限分馆的子馆
            //总馆
            WhgSysCult whgSysCult = this.whgSysCultMapper.selectByPrimaryKey(Constant.ROOT_SYS_CULT_ID);
            Map<String, String> map = new HashMap<>();
            map.put("id", Constant.ROOT_SYS_CULT_ID);
            map.put("text", whgSysCult.getName());
            map.put("pid", whgSysCult.getPid());
            cults.add(map);

            //一级子馆
            Example example = new Example(WhgSysCult.class);
            example.createCriteria()
                    .andEqualTo("pid", Constant.ROOT_SYS_CULT_ID)
                    .andEqualTo("state", EnumState.STATE_YES.getValue())
                    .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
            List<WhgSysCult> sucList = this.whgSysCultMapper.selectByExample(example);
            if(sucList != null){
                for(WhgSysCult cult : sucList){
                    Map<String, String> map_t = new HashMap<>();
                    map_t.put("id", cult.getId());
                    map_t.put("text", cult.getName());
                    map_t.put("pid", cult.getPid());
                    cults.add(map_t);
                }
            }
        }else{//非超级管理员权限分馆及其权限分馆的子馆
            Map<String, String> existCult = new HashMap<>();//去重复


            Example example = new Example(WhgSysUserCult.class);
            example.createCriteria().andEqualTo("userid", id);
            List<WhgSysUserCult> sucList = this.whgSysUserCultMapper.selectByExample(example);
            if(sucList != null){
                List<String> ids = new ArrayList<>();
                for(WhgSysUserCult suc : sucList){
                    ids.add(suc.getCultid());
                }

                //权限分馆
                Example example_scm = new Example(WhgSysCult.class);
                example_scm.createCriteria()
                        .andIn("id", ids)
                        .andEqualTo("state", EnumState.STATE_YES.getValue())
                        .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
                List<WhgSysCult> scults = this.whgSysCultMapper.selectByExample(example_scm);
                if(scults != null){
                    for(WhgSysCult cult : scults){
                        Map<String, String> map = new HashMap<>();
                        map.put("id", cult.getId());
                        map.put("text", cult.getName());
                        map.put("pid", cult.getPid());
                        cults.add(map);
                        existCult.put(cult.getId(), cult.getId());
                    }
                }

                //权限分馆的子馆
                Example example_scm2 = new Example(WhgSysCult.class);
                example_scm2.createCriteria()
                        .andIn("pid", ids)
                        .andEqualTo("state", EnumState.STATE_YES.getValue())
                        .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
                List<WhgSysCult> scults2 = this.whgSysCultMapper.selectByExample(example_scm2);
                if(scults2 != null){
                    for(WhgSysCult cult : scults2){
                        if(!existCult.containsKey(cult.getId())) {
                            Map<String, String> map = new HashMap<>();
                            map.put("id", cult.getId());
                            map.put("text", cult.getName());
                            map.put("pid", cult.getPid());
                            cults.add(map);
                            existCult.put(cult.getId(), cult.getId());
                        }
                    }
                }
            }
        }
        return cults;
    }

    /**
     * 查询管理员的所有权限分馆
     * @param id 管理员ID
     * @return List<Map<String, String>>
     * @throws Exception
     */
    public List<Map<String, String>> loadManagerPMSCult(String id,String username)throws Exception{
        List<Map<String, String>> cults = new ArrayList();
        if(username!=null&&username.equals("admin")) {
                Example example_scm = new Example(WhgSysCult.class);
                example_scm.createCriteria().andEqualTo("state", EnumState.STATE_YES.getValue())
                        .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
                List<WhgSysCult> scults = this.whgSysCultMapper.selectByExample(example_scm);
                if(scults != null){
                    for(WhgSysCult cult : scults){
                        Map<String, String> map = new HashMap<>();
                        map.put("id", cult.getId());
                        map.put("text", cult.getName());
                        cults.add(map);
                    }
                }
        }else if(Constant.SUPER_USER_ID.equalsIgnoreCase(id)){//超级管理员权限分馆
                //总馆
                WhgSysCult whgSysCult = this.whgSysCultMapper.selectByPrimaryKey(Constant.ROOT_SYS_CULT_ID);
                Map<String, String> map = new HashMap<>();
                map.put("id", Constant.ROOT_SYS_CULT_ID);
                map.put("text", whgSysCult.getName());
                cults.add(map);
            }else{//非超级管理员权限分馆
                Example example = new Example(WhgSysUserCult.class);
                example.createCriteria().andEqualTo("userid", id);
                List<WhgSysUserCult> sucList = this.whgSysUserCultMapper.selectByExample(example);
                if(sucList != null){
                    List<String> ids = new ArrayList<>();
                    for(WhgSysUserCult suc : sucList){
                        ids.add(suc.getCultid());
                    }

                    //权限分馆
                    Example example_scm = new Example(WhgSysCult.class);
                    example_scm.createCriteria().andIn("id", ids)
                            .andEqualTo("state", EnumState.STATE_YES.getValue())
                            .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
                    List<WhgSysCult> scults = this.whgSysCultMapper.selectByExample(example_scm);
                    if(scults != null){
                        for(WhgSysCult cult : scults){
                            Map<String, String> map = new HashMap<>();
                            map.put("id", cult.getId());
                            map.put("text", cult.getName());
                            cults.add(map);
                        }
                    }
                }
            }
        return cults;
    }

    /**
     * 权限分馆下的权限部门
     * @param userid 管理员ID
     * @return Map<String, List<Map<String, String>>> 分馆下的部门
     * @throws Exception
     */
    public Map<String, List<WhgSysDept>> loadManagerPMSDept(String userid,String username)throws Exception{
        Map<String, List<WhgSysDept>> userCultDeptMap = new HashMap<>();

        //管理员权限分馆
        List<String> cutlids = new ArrayList<>();
        if(username!=null&&username.equals("admin")) {
           /* Example example_srchdept = new Example(WhgSysDept.class);
            example_srchdept.createCriteria()
                    .andEqualTo("state", EnumState.STATE_YES.getValue())
                    .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
            List<WhgSysDept> deptList = this.whgSysDeptMapper.selectByExample(example_srchdept);
            userCultDeptMap.put(cultid, deptList);*/

        }else if(Constant.SUPER_USER_ID.equalsIgnoreCase(userid)){//超级管理员权限分馆部门
                WhgSysDept sysDept = new WhgSysDept();
                sysDept.setCultid(Constant.ROOT_SYS_CULT_ID);
                List<WhgSysDept> deptList = this.whgSysDeptMapper.select(sysDept);
                userCultDeptMap.put(Constant.ROOT_SYS_CULT_ID, deptList);
            }else{//非超级管理员权限分馆
                WhgSysUserCult wsuc = new WhgSysUserCult();
                wsuc.setUserid(userid);
                List<WhgSysUserCult> wsucList = this.whgSysUserCultMapper.select(wsuc);
                if(wsucList != null){
                    for(WhgSysUserCult wsuct : wsucList){//用户分馆
                        String refid = wsuct.getId();
                        String cultid = wsuct.getCultid();

                        WhgSysCult validCult = new WhgSysCult();
                        validCult.setId(cultid);
                        validCult.setState(EnumState.STATE_YES.getValue());
                        validCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
                        int count = this.whgSysCultMapper.selectCount(validCult);
                        if(count ==1){//分馆有效
                            WhgSysUserCultDept wsucd = new WhgSysUserCultDept();
                            wsucd.setRefid(refid);
                            List<WhgSysUserCultDept> wsucdList = this.whgSysUserCultDeptMapper.select(wsucd);
                            if(wsucdList != null){
                                List<String> deptids = new ArrayList<>();
                                deptids.add("0");
                                for(WhgSysUserCultDept wsucdtt : wsucdList){//分馆下的部门
                                    deptids.add(wsucdtt.getDeptid());
                                }
                                Example example_srchdept = new Example(WhgSysDept.class);
                                example_srchdept.createCriteria()
                                        .andIn("id", deptids)
                                        .andEqualTo("state", EnumState.STATE_YES.getValue())
                                        .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
                                List<WhgSysDept> deptList = this.whgSysDeptMapper.selectByExample(example_srchdept);
                                userCultDeptMap.put(cultid, deptList);
                            }
                        }
                    }
                }
            }
        return userCultDeptMap;
    }
}
