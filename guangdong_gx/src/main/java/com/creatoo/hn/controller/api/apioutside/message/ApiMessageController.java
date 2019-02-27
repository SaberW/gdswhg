package com.creatoo.hn.controller.api.apioutside.message;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgInsMessage;
import com.creatoo.hn.services.api.apiinside.InsMessageService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Enumeration;

/**
 * 外部供需站内信
 */
@SuppressWarnings("ALL")
@Controller
@CrossOrigin
@RequestMapping("/api/message")
public class ApiMessageController extends BaseController{

    @Autowired
    private InsMessageService insMessageService;

    /**
     * 发送内部供需消息
     * @param message
     * @return
     *
     * 参数
     * type:类型：0系统，1私人
     * message:消息信息
     * fromuserid:发送人ID
     * touserid:接收人ID
     * reftype:消息相关实体类型
     * refid:消息相关实体ID
     *
     * code
     * 101:操作失败
     * 102:消息信息为空
     * 103:参数错误
     *
     */
    @CrossOrigin
    @RequestMapping(value = "/sendMessage", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object sendMessage(WhgInsMessage message){
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = (ApiResultBean) this.insMessageService.t_add(message);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("操作失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 分页查询我的消息列表
     * @param page
     * @param pageSize
     * @param type
     * @param userid
     * @param message
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public Object list(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                       @RequestParam(value = "toproject", required = false,defaultValue="WBGX") String toproject,
                       @RequestParam(value = "type", required = true)String type,
                       @RequestParam(value = "userid", required = false) String userid) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (userid == null || userid.equals("")) {
                arb.setCode(101);
                arb.setMsg("用户id不能为空");
            } else {
                WhgInsMessage msg = new WhgInsMessage();
                msg.setTouserid(userid);
                msg.setType(type);
                msg.setToproject(toproject);
                PageInfo pageInfo = this.insMessageService.srchList4p(page, pageSize, msg, "1");
                arb.setPageInfo(pageInfo);
            }
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }


    /**
     * 删除除消息
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "remove", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object remove(String id){
        ApiResultBean arb = new ApiResultBean();
        try {
            this.insMessageService.remove(id);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("操作失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

}
