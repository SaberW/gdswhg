package com.creatoo.hn.interceptors;

import com.creatoo.hn.ext.emun.EnumStateDel;
import com.creatoo.hn.ext.emun.EnumState;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.admin.sysuser.WhgSystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理员控制台请求拦截
 */
public class AdminSessionInterceptors extends HandlerInterceptorAdapter{
	/**
	 * 管理员服务类
	 */
	@Autowired
	private WhgSystemUserService whgSystemUserService;

	/**
	 * 验证会话
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)	throws Exception {
		Object sessionObject = request.getSession().getAttribute("user");

		//管理员是否有会话
        String forwardName = "/pages/admin/login.jsp";

		if (sessionObject == null){
			request.getRequestDispatcher(forwardName).forward(request, response);
			return false;
		}

        WhgSysUser sysUser = (WhgSysUser)sessionObject;
        if(!"administrator".equals(sysUser.getAccount())){//有会话，不是超级管理员时，判断管理员是否已经停用
            sysUser = this.whgSystemUserService.t_srchOne(sysUser.getId());
            if (sysUser == null || EnumState.STATE_NO.getValue() == sysUser.getState().intValue()
                    || EnumStateDel.STATE_DEL_YES.getValue() == sysUser.getDelstate().intValue()) {
                request.getRequestDispatcher(forwardName).forward(request, response);
                return false;
            }
        }

		return true;
	}

}
