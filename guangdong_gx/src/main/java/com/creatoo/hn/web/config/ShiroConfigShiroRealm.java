package com.creatoo.hn.web.config;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.login.MenusService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemJobService;
import com.creatoo.hn.services.admin.system.WhgSystemPmsService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.enums.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 身份认证和权限过滤方法
 * Created by wangxl on 2017/7/13.
 */
@SuppressWarnings("ALL")
public class ShiroConfigShiroRealm extends AuthorizingRealm {
    /**
     * 日志配置
     */
    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 系统用户服务类
     */
    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 文化馆服务类
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 角色服务
     */
    @Autowired
    private WhgSystemJobService whgSystemJobService;

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
     * Retrieves the AuthorizationInfo for the given principals from the underlying data store.  When returning
     * an instance from this method, you might want to consider using an instance of
     * {@link SimpleAuthorizationInfo SimpleAuthorizationInfo}, as it is suitable in most cases.
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     * @see SimpleAuthorizationInfo
     */
    @Override
    @Transactional
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 为当前用户设置角色和权限
        SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
        try {
            //用户身份：wh_mgr.id
            String username = (String) principals.getPrimaryPrincipal();

            //超级管理员处理
            if(Constant.SUPER_USER_NAME.equals(username)){
                List<String> rolepmsList = new ArrayList<String>();

                //所有权限
                List<Map> menulist =  menusService.getMenusList4Sysflag(EnumConsoleSystem.sysmgr.getValue());//过滤了无效菜单
                if(menulist != null){
                    for(Map menu : menulist){
                        if(menu.containsKey("opts") && menu.containsKey("id")){
                            String m_opts = (String)menu.get("opts");
                            String m_id = (String)menu.get("id");
                            if(m_opts != null && m_id != null && !m_opts.isEmpty() && !m_id.isEmpty()){
                                String[] _ops = m_opts.split(",");
                                for(String _op : _ops){
                                    String t_op = _op.trim();
                                    rolepmsList.add(m_id.trim()+":"+t_op);
                                }
                            }
                        }
                    }
                }

                //simpleAuthorInfo.addRoles(roleList);
                simpleAuthorInfo.addStringPermissions(rolepmsList);
            }else{//普通管理员
                //所有角色，所有权限
                List<String> rolepmsList = new ArrayList<String>();

                // 查询用户的角色
                Subject currentUser = SecurityUtils.getSubject();
                WhgSysUser mgr = (WhgSysUser)currentUser.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
                if(EnumConsoleSystem.bizmgr.getValue().equals(mgr.getAdmintype()) && mgr.getIsbizmgr().intValue() == 1){
                    //文化馆超级管理员所有站点权限组的权限
                    WhgSysPms _whgSysPms = new WhgSysPms();
                    List<WhgSysPms> jobPmsList = this.whgSystemPmsService.t_srchList(_whgSysPms, null, null, mgr.getCultid());
                    for(WhgSysPms sysPms : jobPmsList){
                        if(sysPms.getState().intValue() == EnumState.STATE_YES.getValue() &&
                                sysPms.getDelstate().intValue() == EnumStateDel.STATE_DEL_NO.getValue()){
                            List<WhgSysPmsDetail> pmsDetails = this.whgSystemPmsService.t_srchPmsDetail(sysPms.getId());
                            for(WhgSysPmsDetail detail : pmsDetails){
                                rolepmsList.add(detail.getPmsstr());
                            }
                        }
                    }
                }else{// 普通站点管理员和区域管理员通过岗们获取菜单权限
                    List<WhgSysUserCultRole> ucrs = this.whgSystemUserService.t_srchUserPMSCultRoles(mgr.getId());
                    if(ucrs != null && ucrs.size() > 0){
                        for(WhgSysUserCultRole ur :ucrs ){
                            String roleid = ur.getRoleid();
                            WhgSysUserCult whgSysUserCult = this.whgSystemUserService.getCultIdFromUserCultRefByUserId(mgr.getId());
                            if(roleid != null && !roleid.isEmpty()){
                                //角色必须有效
                                boolean isEnable = this.whgSystemJobService.isEnable(roleid);
                                if(isEnable){
                                    //查询角色权限
                                    List<WhgSysJobPms> jobPmsList = this.whgSystemJobService.t_srchJobPms(roleid);
                                    if(jobPmsList != null){
                                        for(WhgSysJobPms whgSysJobPms : jobPmsList){
                                            String pmsid = whgSysJobPms.getPmsid();
                                            boolean _isEnable = this.whgSystemPmsService.isEnable(pmsid, whgSysUserCult, mgr.getAdmintype());
                                            //机构是否适用了此权限组
                                            if(_isEnable){
                                                List<WhgSysPmsDetail> pmsDetails = this.whgSystemPmsService.t_srchPmsDetail(pmsid);
                                                for(WhgSysPmsDetail detail : pmsDetails){
                                                    rolepmsList.add(detail.getPmsstr());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //simpleAuthorInfo.addRoles(roleList);
                simpleAuthorInfo.addStringPermissions(rolepmsList);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return simpleAuthorInfo;
    }

    /**
     * Retrieves authentication data from an implementation-specific datasource (RDBMS, LDAP, etc) for the given
     * authentication token.
     * <p/>
     * For most datasources, this means just 'pulling' authentication data for an associated subject/user and nothing
     * more and letting Shiro do the rest.  But in some systems, this method could actually perform EIS specific
     * log-in logic in addition to just retrieving data - it is up to the Realm implementation.
     * <p/>
     * A {@code null} return value means that no account could be associated with the specified token.
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return an {@link AuthenticationInfo} object containing account data resulting from the
     * authentication ONLY if the lookup is successful (i.e. account exists and is valid, etc.)
     * @throws AuthenticationException if there is an error acquiring data or performing
     *                                 realm-specific authentication logic for the specified <tt>token</tt>
     */
    @Override
    @Transactional
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        SimpleAuthenticationInfo simpleAuthenticationInfo = null;
        try {
            //从token中 获取用户身份信息
            String username = (String) ((UsernamePasswordToken)token).getUsername();
            String password = String.valueOf(((UsernamePasswordToken)token).getPassword());

            //超级管理员
            if(Constant.SUPER_USER_NAME.equals(username)){
                if(Constant.SUPER_USER_PASSWORD.equals(password)){
                    WhgSysUser user = new WhgSysUser();
                    user.setId(Constant.SUPER_USER_ID);
                    user.setAccount(Constant.SUPER_USER_NAME);
                    user.setAdmintype(EnumConsoleSystem.sysmgr.getValue());
                    user.setAdminlevel(EnumCultLevel.Level_Province.getValue());
                    user.setAdminprovince("广东省");
                    user.setState(EnumState.STATE_YES.getValue());
                    simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, getName());
                    SecurityUtils.getSubject().getSession().setAttribute(Constant.SESSION_ADMIN_KEY, user);
                }else{
                    throw new UnknownAccountException("用户名密码不正确！");
                }
            }else{
                //拿username从数据库中查询
                WhgSysUser user = this.whgSystemUserService.t_srchOneByNameAndPwd(username, password);

                if(EnumConsoleSystem.bizmgr.getValue().equals(user.getAdmintype())){//站点管理员
                    user.getCultid();
                    WhgSysCult sysCult = this.whgSystemCultService.t_srchOne(user.getCultid());
                    if(sysCult == null || sysCult.getState() != EnumBizState.STATE_PUB.getValue() || sysCult.getDelstate() != EnumStateDel.STATE_DEL_NO.getValue()){
                        throw new UnknownAccountException("文化馆已失效！");
                    }
                }
                if(user != null && EnumState.STATE_YES.getValue() != user.getState()){
                    throw new UnknownAccountException("账号已停用！");
                }else if(user != null){
                    user.setLastdate(new Date());
                    this.whgSystemUserService.t_editBaseInfo(user);
                    simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, getName());
                    SecurityUtils.getSubject().getSession().setAttribute(Constant.SESSION_ADMIN_KEY, user);
                }else{
                    throw new UnknownAccountException("用户名密码不正确！");
                }
            }
        } catch (Exception e) {
            throw new UnknownAccountException( e.getMessage() == null ? "账号密码不正确！" : e.getMessage());
        }
        return simpleAuthenticationInfo;
    }
}
