package com.creatoo.hn.controller.admin.user;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgUser;
import com.creatoo.hn.dao.model.WhgUserProduct;
import com.creatoo.hn.services.admin.user.WhgUserService;
import com.creatoo.hn.services.comm.SMSService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.creatoo.hn.util.CommUtil;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.controller.BaseController;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员账号管理
 * Created by wangxl on 2017/4/8.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/admin/user/info")
public class WhgUserInfoController extends BaseController {


    /**
     * 短信服务
     */
    @Autowired
    private SMSService smsService;

    /**
     * 会员服务类
     */
    @Autowired
    private WhgUserService userService;

    /**
     * 进入type(list|add|edit|view)视图
     * @param request 请求对象
     * @param type 视图类型(list|add|edit|view)
     * @return
     */
    @RequestMapping("/view/{type}")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MEMBER, optDesc = {"访问会员列表页", "访问会员实名审核页", "访问会员编辑页"}, valid = {"type=list", "type=auth", "type=edit"})
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type")String type){
        ModelAndView view = new ModelAndView("admin/user/info/view_"+type);

        try {
            if("edit".equals(type) || "auth".equals(type)){
                String id = request.getParameter("id");
                view.addObject("whuser", this.userService.t_srchOne(id));

                List<WhgUserProduct> ups = this.userService.srchUserProduct(id);
                StringBuilder productids = new StringBuilder();
                if (ups!=null && ups.size()>0){
                    for(WhgUserProduct up : ups){
                        if (productids.length()>0){
                            productids.append(",");
                        }
                        productids.append(up.getProduct());
                    }
                }

                view.addObject("productids", productids.toString());
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        return view;
    }

    /**
     * 分页查询
     * @return
     */
    @RequestMapping(value = "/srchList4p")
    @ResponseBody
    public ResponseBean srchList4p(int page, int rows, WhgUser user, HttpServletRequest request){
        ResponseBean res = new ResponseBean();
        try {
            String sort = request.getParameter("sort");
            String order = request.getParameter("order");

            PageInfo<WhgUser> pageInfo = this.userService.t_srchList4p(page, rows, user, sort, order);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            res.setRows(new ArrayList());
            res.setTotal(0);
        }
        return res;
    }

    /**
     * 编辑
     * @param request 请求对象
     * @param user 编辑的资料
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MEMBER, optDesc = {"实名审核"})
    public ResponseBean edit(HttpServletRequest request, WhgUser user){
        ResponseBean res = new ResponseBean();
        try {
            userService.t_edit(user, (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
            try{
                WhgUser myuser = this.userService.t_srchOne(user.getId());
                if(myuser.getIsrealname() != null && myuser.getPhone() != null){
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("name",myuser.getNickname());
                    if(user.getIsrealname() == 1){
                        smsService.t_sendSMS(myuser.getPhone(), "REL_CHECK_PASS", data, user.getId());
                    }
                    if(user.getIsrealname() == 2){
                        smsService.t_sendSMS(myuser.getPhone(), "REL_CHECK_FAIL", data, user.getId());
                    }
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(),e);
        }
        return res;
    }

    /**
     * 删除
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/del")
    @ResponseBody
    @WhgOPT(optType = EnumOptType.MEMBER, optDesc = {"删除"})
    public ResponseBean del(HttpServletRequest request, String ids){
        ResponseBean res = new ResponseBean();
        try {
            userService.t_del(ids, (WhgSysUser)request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }
}
