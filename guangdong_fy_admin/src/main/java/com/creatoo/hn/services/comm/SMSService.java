package com.creatoo.hn.services.comm;
import com.creatoo.hn.dao.mapper.WhgCodeMapper;
import com.creatoo.hn.dao.mapper.WhgYwiSmsMapper;
import com.creatoo.hn.dao.mapper.WhgYwiSmsRelMapper;
import com.creatoo.hn.dao.mapper.WhgYwiSmstmpMapper;
import com.creatoo.hn.dao.model.WhgCode;
import com.creatoo.hn.dao.model.WhgYwiSms;
import com.creatoo.hn.dao.model.WhgYwiSmsRel;
import com.creatoo.hn.dao.model.WhgYwiSmstmp;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumState;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 提供基本的短信服务
 * Created by wangxl on 2017/4/1.
 */
@Service
@CacheConfig(cacheNames = "WhgYwiSms", keyGenerator = "simpleKeyGenerator")
public class SMSService extends BaseService {

    /**
     * 短信模板
     */
    @Autowired
    private WhgYwiSmstmpMapper whgYwiSmstmpMapper;

    /**
     * 短信发送记录
     */
    @Autowired
    private WhgYwiSmsMapper whgYwiSmsMapper;

    /**
     * 短信发送记录辅助信息
     */
    @Autowired
    private WhgYwiSmsRelMapper whgYwiSmsRelMapper;


    @Autowired
    private WhgCodeMapper WhgCodeMapper;
    /**
     * 短信发送
     * @param phoneNumber 手机号
     * @param tempCode 模板号
     * @param data 模板参数
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_sendSMS(String phoneNumber, String tempCode, Map<String, String> data, String entityId)throws Exception{
        //手机号不能为空
        if(phoneNumber == null || !phoneNumber.matches("\\d{11}")){
            throw new Exception("手机号不能为空");
        }

        //短信模板编码不能为空
        WhgYwiSmstmp smstmp = new WhgYwiSmstmp();
        smstmp.setCode(tempCode);
        smstmp = this.whgYwiSmstmpMapper.selectOne(smstmp);
        if(smstmp == null || smstmp.getContent() == null){
            throw new Exception("模板编号["+tempCode+"]找不到对应的短信模板内容");
        }

        //发送短信
        String smsContent = this.processSmsTemplate(smstmp.getContent(), data);
        boolean isOK = this.gxtSendMessage(phoneNumber, smsContent);
        if(!isOK){
            return;
        }
        WhgYwiSms smsrecode = new WhgYwiSms();
        String table_key = IDUtils.getID();
        smsrecode.setId(table_key);
        smsrecode.setPhone(phoneNumber);
        smsrecode.setSenddate(new Date());
        smsrecode.setSendstate(EnumState.STATE_YES.getValue());
        smsrecode.setSmscontent(smsContent);
        this.whgYwiSmsMapper.insert(smsrecode);

        //写关联信息
        WhgYwiSmsRel smsRelRecode = new WhgYwiSmsRel();
        smsRelRecode.setSlId(table_key);
        smsRelRecode.setSlReltype(parseType4SMSTEMP(tempCode));
        smsRelRecode.setSlRelid(entityId);
        smsRelRecode.setSlSmscode(tempCode);
        smsRelRecode.setSlThirdid(null);
        this.whgYwiSmsRelMapper.insert(smsRelRecode);
    }



    /**
     * 存入验证码
     * @param phoneNumber
     * @param code
     * @param sessionId
     * @return
     */
    @CacheEvict(allEntries = true)
   public int insertWhgCode(String phoneNumber,String code,String sessionId){
        try {
            //插入wh_code
            WhgCode whgCode = new WhgCode();
            whgCode.setId(IDUtils.getID());
            whgCode.setMsgcontent(code);
            whgCode.setMsgphone(phoneNumber);
            whgCode.setSessid(sessionId);
            whgCode.setMsgtime(new Date());
            WhgCodeMapper.insert(whgCode);
            return 0;
        }catch (Exception e){
            log.error(e.toString());
            return 1;
        }
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
     * 根据模板编号，获得发送短信对应的实体类型
     * @param smsTempCode 短信模板
     * @return 关联类型。1-活动, 2-培训, 3-活动室, 4-用户, 9-其它
     */
    private int parseType4SMSTEMP(String smsTempCode){
        int type = 9;

        try {
            Map<String, Integer> tempCodeMap = new HashMap<String, Integer>();
            /** =================================培训短信================================================================= */
            tempCodeMap.put("TRA_CHECK_PASS", 2);//, "培训报名成功通知");
            tempCodeMap.put("TRA_CHECK_FAIL", 2);//, "培训报名未通过审核通知");
            tempCodeMap.put("TRA_VIEW_PASS", 2);//, "培训报名面试通过通知");
            tempCodeMap.put("TRA_VIEW_FAIL", 2);//, "培训报名面试不通过通知");
            tempCodeMap.put("TRA_CANCEL", 2);//, "培训取消报名通知");

            /** =================================场馆活动室短信================================================================= */
            tempCodeMap.put("VEN_ORDER_ADD", 3);//, "申请预定活动室(免费)通知");
            tempCodeMap.put("VEN_ORDER_UNADD", 3);//, "取消申请预定活动室通知");
            tempCodeMap.put("VEN_ORDER_SUCCESS", 3);//, "预定活动室审核通过通知");
            tempCodeMap.put("VEN_ORDER_FAIL", 3);//, "预定活动室审核不通过通知");
            tempCodeMap.put("ORDER_ADD_CHARGE", 3);//, "申请预定活动室(收费)通知");

            /** =================================登录短信================================================================= */
            tempCodeMap.put("LOGIN_VALIDCODE", 4);//, "发送注册验证码");
            tempCodeMap.put("LOGIN_PASSWROD", 4);//, "微信绑定手机并生成PC端账号和密码通知");

            /** =================================活动短信================================================================= */
            tempCodeMap.put("ACT_DUE", 1);//, "活动预定成功通知");
            tempCodeMap.put("ACT_UNORDER", 1);//, "活动取消预定成功通知");

            /** =================================实名认证短信================================================================= */
            tempCodeMap.put("REL_CHECK_PASS", 4);//, "实名认证审核通过通知");
            tempCodeMap.put("REL_CHECK_FAIL", 4);//, "实名认证审核失败通知");

            if (tempCodeMap.containsKey(smsTempCode)) {
                type = tempCodeMap.get(smsTempCode);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        return type;
    }



    /**
     * 吉信通发送短信
     * @param phoneNumber 手机号
     * @param content 内容
     * @return 是否发送成功
     */
    private boolean gxtSendMessage(String phoneNumber, String content){
        boolean isOK = false;
        String result= null;
        HttpURLConnection httpconn = null;
        BufferedReader rd = null;
        try {
            //动态获取配置参数
           // String gxtUrl = commPropertiesService.getSmsUrl();
            //String gxtId = commPropertiesService.getSmsUsername();
            // String gxtPwd = commPropertiesService.getSmsPassword();

            /* 属性名待与配置文件统一*/
            String gxtUrl = env.getProperty("smsurl");
            String gxtId = env.getProperty("smsUserName");
            String gxtPwd = env.getProperty("smsPassword");

            StringBuilder sb = new StringBuilder();
            sb.append(gxtUrl);		//正式环境下的地址
            sb.append("uid=").append(URLEncoder.encode(gxtId,"UTF-8"));	//吉信通用户名
            sb.append("&pwd=").append(gxtPwd);	//吉信通登录密码
            sb.append("&tos=").append(phoneNumber);	//接收的手机号码
            sb.append("&msg=").append(URLEncoder.encode(content,"UTF-8")); //短信模板
            sb.append("&otime=").append("");	//短信发送时间
            URL url = new URL(sb.toString());
            httpconn = (HttpURLConnection) url.openConnection();
            rd = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
            result = rd.readLine();
            String valueString = null;
            while ((valueString=rd.readLine())!=null){
                System.out.println(valueString);
                result += valueString;
            }

            /*if(result != null && result.length() > 2 && "000".equals(result.substring(0,3))){
                result = "100";
            }*/
            isOK = true;
        }catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally{
            if(rd != null){
                try{rd.close();}catch (Exception e){}
            }
            if(httpconn!=null){
                httpconn.disconnect();
            }
        }
        return isOK;
    }

    public static void main(String[] args)throws  Exception {
        String phoneNumber = "136773928331";
        if(phoneNumber == null || !phoneNumber.matches("\\d{11}")){
            System.out.println("error");
        }else{
            System.out.println("success");

        }
        /*Map<String, String> map = new HashMap<String, String>();
        map.put("user", "wangxinlin");
        System.out.println( SMSService.processSmsTemplate("你好，${user}", map) );*/
    }

}
