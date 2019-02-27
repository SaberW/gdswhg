package com.creatoo.hn.services.api.apiinside;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgSysUserMapper;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumConsoleSystem;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgSysUser", keyGenerator = "simpleKeyGenerator")
public class InsUserService extends BaseService{

    @Autowired
    private WhgSysUserMapper whgSysUserMapper;

    /**
     * 文化馆服务类
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 内部供需管理员登录信息获取
     * @param account
     * @param password
     * @return
     * @throws Exception
     */
    public Object login(String account, String password) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (account == null || account.isEmpty() || password == null || password.isEmpty()) {
            arb.setCode(102);
            arb.setMsg("帐号或密码不能为空");
            return arb;
        }
        WhgSysUser recode = new WhgSysUser();
        recode.setAccount(account);
        recode.setPassword(password);
        recode.setState(EnumState.STATE_YES.getValue());
        recode.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        Example example = new Example(WhgSysUser.class);
        example.selectProperties("id", "state", "delstate", "account", "lastdate", "email", "contact", "cultid", "admintype", "isauthinside");
        example.createCriteria().andEqualTo(recode);
        List<WhgSysUser> list = this.whgSysUserMapper.selectByExample(example);
        if (list == null || list.size() == 0) {
            arb.setCode(103);
            arb.setMsg("帐号或密码不正确");
            return arb;
        }
        WhgSysUser sysUser = list.get(0);
        if (sysUser.getAdmintype() != null && sysUser.getAdmintype().equals(EnumConsoleSystem.sysmgr.getValue())) {
            arb.setCode(104);
            arb.setMsg("区域管理员不能登录系统");
            return arb;
        }
        if (sysUser.getCultid() == null || sysUser.getCultid().isEmpty()) {
            arb.setCode(105);
            arb.setMsg("管理员信息有误");
            return arb;
        }
        if (sysUser.getIsauthinside() == null || sysUser.getIsauthinside().intValue() != 1){
            arb.setCode(106);
            arb.setMsg("帐号未开启当前系统授权");
            return arb;
        }
        WhgSysCult sysCult = this.whgSystemCultService.t_srchOne(sysUser.getCultid());
        BeanMap bm = new BeanMap();
        bm.setBean(sysUser);
        Map data = new HashMap();
        data.putAll(bm);
        data.put("theCultName", sysCult.getName());
        arb.setData(data);
        return arb;
    }


    /**
     * 查询管理员信息
     * @param id
     * @return
     * @throws Exception
     */
    public Object findWhgSysUser(String id) throws Exception{
        Example example = new Example(WhgSysUser.class);

        example.selectProperties("id", "account", "contact", "contactnum", "email", "cultid");
        example.createCriteria()
                .andEqualTo("id", id);

        List<WhgSysUser> list = this.whgSysUserMapper.selectByExample(example);
        if (list == null || list.size() == 0) {
            return null;
        }
        WhgSysUser info = list.get(0);

        WhgSysCult cult = this.whgSystemCultService.t_srchOne(info.getCultid());

        BeanMap bm = new BeanMap();
        bm.setBean(info);
        Map rest = new HashMap();
        rest.putAll(bm);
        if (cult != null) {
            rest.put("cultidName", cult.getName());
        }

        return rest;
    }

    /**
     * 处理编辑资料
     * @param id
     * @param recode
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ApiResultBean editUser(String id, WhgSysUser recode) throws Exception{
        ApiResultBean arb = new ApiResultBean();

        if (id == null || id.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("用户标识不能为空");
            return arb;
        }

        if (recode == null) {
            arb.setCode(103);
            arb.setMsg("参数不能为空");
            return arb;
        }

        WhgSysUser user = this.whgSysUserMapper.selectByPrimaryKey(id);
        if (user == null) {
            arb.setCode(103);
            arb.setMsg("用户查询失败");
            return arb;
        }

        if (recode.getContact() == null || recode.getContact().isEmpty()) {
            arb.setCode(103);
            arb.setMsg("联系人不能为空");
            return arb;
        }
        if (recode.getContactnum() == null || recode.getContactnum().isEmpty()) {
            arb.setCode(103);
            arb.setMsg("联系方式不能为空");
            return arb;
        }

        user.setContact(recode.getContact());
        user.setContactnum(recode.getContactnum());
        if (recode.getPassword() != null && !recode.getPassword().isEmpty()) {
            user.setPassword(recode.getPassword());
        }
        if (recode.getPasswordMemo() != null && !recode.getPasswordMemo().isEmpty()) {
            user.setPasswordMemo(recode.getPasswordMemo());
        }
        this.whgSysUserMapper.updateByPrimaryKeySelective(user);

        return arb;
    }
}
