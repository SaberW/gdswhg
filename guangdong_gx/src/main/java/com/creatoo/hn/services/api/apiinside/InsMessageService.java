package com.creatoo.hn.services.api.apiinside;

import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.creatoo.hn.util.enums.EnumState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.StringWriter;
import java.util.*;

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "insMessage", keyGenerator = "simpleKeyGenerator")
public class InsMessageService extends BaseService{

    @Autowired
    private WhgInsMessageMapper whgInsMessageMapper;

    @Autowired
    private WhgSysUserMapper whgSysUserMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;


    @Autowired
    private InsSupplyService insSupplyService;
    /**
     * 短信模板
     */
    @Autowired
    private WhgYwiSmstmpMapper whgYwiSmstmpMapper;
    /**
     * 添加内部供需消息
     * @param message
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object t_add(WhgInsMessage message) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (message == null) {
            arb.setCode(102);
            arb.setMsg("消息信息不能为空");
            return arb;
        }
        if (message.getType() == null || !Arrays.asList("0", "1").contains(message.getType())) {
            arb.setCode(103);
            arb.setMsg("消息类型为空或非法");
            return arb;
        }
        if (message.getMessage() == null) {
            arb.setCode(103);
            arb.setMsg("内容不能为空");
            return arb;
        }
        if (message.getFromuserid() == null || message.getFromuserid().isEmpty()) {
            arb.setCode(103);
            arb.setMsg("发送人不能为空");
            return arb;
        }
        /*Object user = this.whgSysUserMapper.selectByPrimaryKey(message.getFromuserid());
        if (user == null) {
            arb.setCode(103);
            arb.setMsg("发送人查找失败");
            return arb;
        }*/
        if (message.getTouserid() == null || message.getTouserid().isEmpty()) {
            arb.setCode(103);
            arb.setMsg("发送目标不能为空");
            return arb;
        }
        /*user = this.whgSysUserMapper.selectByPrimaryKey(message.getTouserid());
        if (user == null) {
            arb.setCode(103);
            arb.setMsg("发送目标查找失败");
            return arb;
        }*/

        if (message.getReftype()!=null && message.getReftype().length()>2){
            arb.setCode(103);
            arb.setMsg("实体类型非法");
            return arb;
        }
        if (message.getRefid()!=null && message.getRefid().length()>32){
            arb.setCode(103);
            arb.setMsg("实体类型ID非法");
            return arb;
        }

        message.setId(IDUtils.getID());
        message.setIsread("0");
        message.setSendtime(new Date());

        this.whgInsMessageMapper.insert(message);
        return arb;
    }


    /**
     * 站内信发送
     * @param phoneNumber 手机号
     * @param tempCode 模板号
     * @param data 模板参数
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_sendZNX(String toUserId, String fromUserId,String tempCode, Map<String, String> data, String entityId,String toproject)throws Exception{
        //手机号不能为空
        if(toUserId == null || toUserId.equals("")){
            throw new Exception("用户id不能为空");
        }

        //短信模板编码不能为空
        WhgYwiSmstmp smstmp = new WhgYwiSmstmp();
        smstmp.setCode(tempCode);
        smstmp = this.whgYwiSmstmpMapper.selectOne(smstmp);
        if(smstmp == null || smstmp.getContent() == null){
            throw new Exception("模板编号["+tempCode+"]找不到对应的短信模板内容");
        }

        //短信内容
        String smsContent = this.processSmsTemplate(smstmp.getContent(), data);


        WhgInsMessage message =new WhgInsMessage();
        message.setId(IDUtils.getID());
        message.setIsread("0");
        message.setSendtime(new Date());
        message.setMessage(smsContent);
        message.setToproject(toproject);
        message.setTouserid(toUserId);
        message.setFromuserid(fromUserId);
        message.setRefid(entityId);
        message.setType("0");//系统信息
        this.whgInsMessageMapper.insert(message);

    }



    /**
     * 解析短信模板
     * @param templateContent 模板内容
     * @param data 模板参数
     * @return 短信内容
     * @throws Exception
     */
    private String processSmsTemplate(String templateContent, Map<String, String> data)throws Exception{
        String rtnContent = templateContent;
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        String template = "smsWhgTemplate";
        stringLoader.putTemplate("smsWhgTemplate", templateContent);
        Configuration cfg =  new Configuration(Configuration.VERSION_2_3_23);
        cfg.setTemplateLoader(stringLoader);
        try {
            Template templateCon =cfg.getTemplate(template);
            StringWriter writer = new StringWriter();
            templateCon.process(data, writer);
            rtnContent = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnContent;
    }

    /**
     * 分页查询消息列表
     * @param page
     * @param pageSize
     * @param message
     * @return
     * @throws Exception
     */
    public PageInfo srchList4p(int page, int pageSize, WhgInsMessage message,String sendType) throws Exception{

        Example example = new Example(WhgInsMessage.class);
        Example.Criteria c = example.createCriteria();
        PageInfo pageInfo = null;
        if (message != null) {
            if (message.getMessage() != null && !message.getMessage().isEmpty()) {
                c.andLike("message", "%"+message.getMessage()+"%");
            }
            if (message.getType() != null && !message.getType().isEmpty()) {
                c.andEqualTo("type", message.getType());
            }
            if (message.getTouserid() != null && !message.getTouserid().isEmpty()) {
                c.andEqualTo("touserid", message.getTouserid());
            }
            if (message.getFromuserid() != null && !message.getFromuserid().isEmpty()) {
                c.andEqualTo("fromuserid", message.getFromuserid());
            }
            if (message.getToproject() != null && !message.getToproject().isEmpty()) {
                if (!message.getToproject().equals("ALL")) {//针对微信端 需要查询全部消息
                    c.andLike("toproject", "%" + message.getToproject() + "%");
                }
            }
        }
        example.orderBy("sendtime").desc();
        PageHelper.startPage(page, pageSize);
        List<WhgInsMessage> list = this.whgInsMessageMapper.selectByExample(example);
        pageInfo = new PageInfo(list);
        List<WhgInsMessage>  strList=new ArrayList<WhgInsMessage> ();
        for(WhgInsMessage insMessage:list){
            if(insMessage.getType().equals("0")){
                  insMessage.setFromuserid("系统");
            }else{
                if(insMessage.getFromuserid()!=null){
                    String findUserId="";
                    if(sendType!=null&&sendType.equals("0")){
                        findUserId=insMessage.getTouserid();
                        WhgSysCult cult=whgSystemUserService.t_srchUserCult(findUserId);
                        if(cult!=null){
                            insMessage.setTouserid(cult.getName());
                        }
                    }else{
                        findUserId=insMessage.getFromuserid();
                        WhgSysCult cult=whgSystemUserService.t_srchUserCult(findUserId);
                        if(cult!=null){
                            insMessage.setFromuserid(cult.getName());
                        }
                    }
                    if(insMessage.getToproject()!=null&&insMessage.getToproject().equals(EnumProject.PROJECT_NBGX.getValue())){
                        WhgSupply obj=insSupplyService.t_srchOne(insMessage.getRefid());
                        insMessage.setRefid(obj.getTitle());
                    }
                }
            }
            strList.add(insMessage);
        }
        pageInfo.setList(strList);
        return pageInfo;
    }

    /**
     * 主键查询消息信息
     * @param id
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object srchOne(String id) throws Exception{
        if (id == null || id.isEmpty()) {
            return null;
        }

        WhgInsMessage msg = this.whgInsMessageMapper.selectByPrimaryKey(id);
        if (msg == null){
            return null;
        }
        msg.setIsread("1");
        msg.setReadtime(new Date());
        this.whgInsMessageMapper.updateByPrimaryKeySelective(msg);

        WhgSysUser fromUser = this.whgSysUserMapper.selectByPrimaryKey(msg.getFromuserid());
        WhgSysUser toUser = this.whgSysUserMapper.selectByPrimaryKey(msg.getTouserid());

        BeanMap bm = new BeanMap();
        bm.setBean(msg);

        Map rest = new HashMap();
        rest.putAll(bm);
        rest.put("formUserAccount", fromUser.getAccount());
        rest.put("toUserAccount", toUser.getAccount());

        return rest;
    }

    /**
     * 删除消息
     * @param id
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void remove(String id) throws Exception{
        if (id == null || id.isEmpty()) {
            return;
        }
        this.whgInsMessageMapper.deleteByPrimaryKey(id);
    }
}
