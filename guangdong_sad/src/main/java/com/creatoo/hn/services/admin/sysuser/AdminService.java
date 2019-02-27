package com.creatoo.hn.services.admin.sysuser;

import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.comm.MenusService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rbg on 2017/7/7.
 */

@SuppressWarnings("ALL")
@Service
public class AdminService {

    @Autowired
    private MenusService menusService;

    /**登录验证
     * @param name
     * @param pwd
     * @return
     */
    public Object logindo(String name, String pwd)throws Exception{
        WhgSysUser mgr = null;
        try {

            Subject currentUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(name, pwd);
            currentUser.login(token);
            mgr = (WhgSysUser)currentUser.getSession().getAttribute("user");

//			WhMgr record = new WhMgr();
//			record.setName(name);
//			record.setPassword(pwd);
//			String account = mgr.getAccount();
//			WhgSysUser whgSysUser = new WhgSysUser();
//			mgr = this.whgSysUserMapper.selectOne(mgr);
//			mgr.getAccount()

        } catch (Exception e) {
            throw e;
        }
        return mgr;
    }


    /**提取全部的树型菜单内容
     * @return
     * @throws Exception
     */
    public Object getMenuList(String type) throws Exception{
		/*//List<WhMenu> list = this.menuMapper.selectAll();
		Example example = new Example(WhMenu.class);
		example.setOrderByClause("parent,idx");
		List<WhMenu> mlist = this.menuMapper.selectByExample(example);

		//权限处理
		List<WhMenu> list = new ArrayList<WhMenu>();
		if(!"off".equals(type)){
			if(mlist != null){
				Subject currentUser = SecurityUtils.getSubject();
				for(WhMenu menu : mlist){
					String permission = menu.getId()+":view";
					if(menu.getType() != 1 || currentUser.isPermitted(permission)){
						list.add(menu);
					}
				}
			}
		}else{
			list = mlist;
		}*/

		/*List<Object> res = new ArrayList<Object>();
		for (int i=0; i<list.size(); i++) {
			WhMenu whMenu = list.get(i);
			if (whMenu.getId() == null || whMenu.getId().isEmpty()){
				continue;
			}
			if (whMenu.getParent() == null || "".equals(whMenu.getParent().trim())){
				Map item = BeanUtils.describe(whMenu);
				this.compMenuTree(item, list, i);
				res.add(item);

				list.remove(i);
				--i;
			}
		}
		return res;*/

        return this.menusService.getMeunsTree4Auth(SecurityUtils.getSubject());
    }
}
