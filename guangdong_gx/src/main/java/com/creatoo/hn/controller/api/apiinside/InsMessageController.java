package com.creatoo.hn.controller.api.apiinside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgInsMessage;
import com.creatoo.hn.services.api.apiinside.InsMessageService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 内部供需站内信
 */
@SuppressWarnings("ALL")
@Controller
@CrossOrigin
@RequestMapping("/api/inside/msg")
public class InsMessageController extends BaseController{

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
            message.setToproject(EnumProject.PROJECT_NBGX.getValue());
            arb = (ApiResultBean) this.insMessageService.t_add(message);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("操作失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 分页查询内部供需消息列表
     * @param page
     * @param pageSize
     * @param type
     * @param userid
     * @param message
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Object list(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                       @RequestParam(value = "type", required = true)String type,
                       @RequestParam(value = "sendType", required = false, defaultValue ="")String sendType,
                       @RequestParam(value = "toproject", required = false,defaultValue="NBGX") String toproject,
                       @RequestParam(value = "userid", required = true)String userid,
                       @RequestParam(value = "message", required = false)String message){
        ApiResultBean arb = new ApiResultBean();
        try {
            WhgInsMessage msg = new WhgInsMessage();
            if(sendType.equals("1")){//我接收的
                msg.setTouserid(userid);
            }else if(sendType.equals("0")){//我发送的
                msg.setFromuserid(userid);
            }
            msg.setType(type);
            msg.setMessage(message);
            msg.setToproject(toproject);
            PageInfo pageInfo = this.insMessageService.srchList4p(page, pageSize, msg,sendType);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(),e);
        }
        return arb;
    }

    /**
     * 查询消息详细信息
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/info", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object info(String id) {
        ApiResultBean arb = new ApiResultBean();

        try {
            Object rest = this.insMessageService.srchOne(id);
            arb.setData(rest);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
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
