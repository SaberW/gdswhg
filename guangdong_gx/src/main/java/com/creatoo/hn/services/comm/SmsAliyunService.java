package com.creatoo.hn.services.comm;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.mapper.aliyunsms.AliyunSmsMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@SuppressWarnings("ALL")
@Service
public class SmsAliyunService extends BaseService {

    @Autowired
    private WhgYwiAliysmsGroupMapper whgYwiAliysmsGroupMapper;
    @Autowired
    private WhgYwiAliysmsCodeMapper whgYwiAliysmsCodeMapper;
    @Autowired
    private WhgYwiAliysmsRefgcMapper whgYwiAliysmsRefgcMapper;
    @Autowired
    private WhgYwiAliysmsRefusegropMapper whgYwiAliysmsRefusegropMapper;
    @Autowired
    private YwiConfigService ywiConfigService;

    @Autowired
    private AliyunSmsMapper aliyunSmsMapper;
    @Autowired
    private WhgYwiAliysmsSenderrMapper whgYwiAliysmsSenderrMapper;


    //TODO 短信组业务与切入点定义

    /**
     * 短信组业务类型
     * -- 尽量与EnumTypeClazz 枚举值对应
     * -- 用于管理端取值
     */
    private final static Map<String, String> smsGroupType = new HashMap(){{
        put("5", "培训类型");
        put("4", "活动类型");
        put("2", "场馆类型");
        put("26", "供需类型");
        put("28", "众筹类型");
        put("10000", "用户和管理员通知");
    }};
    /**
     * 培训组各业务类型的短信发送切入点
     * -- 一层key与业务组类型key对应
     * -- 二层切入点key与原短信发送的模板一至，调用短信发送时参考
     * -- 用于管理端业务组添加短信模板切入点取值
     */
    private final static Map<String, Map<String, String>> smsGroupPoint = new HashMap(){{
        //培训类型
        put("5", new HashMap(){{
            put("TRA_CHECK_PASS", "培训订单审核通过通知面试");
            put("TRA_CHECK_FAIL", "培训订单审核不通过");
            put("TRA_VIEW_PASS", "培训订单审核通过");
            put("TRA_VIEW_FAIL", "培训订单面试不通过");
            put("TRA_CANCEL", "培训订单管理员取消报名");
        }});
        //活动类型
        put("4", new HashMap(){{
            put("ACT_DUE", "活动订票成功");
        }});
        //场馆类型
        put("2", new HashMap(){{
            put("VEN_ORDER_ADD", "场馆预订成功");
            put("VEN_ORDER_ADD_CHARGE", "收费类场馆预订成功");
            put("VEN_ORDER_FAIL", "场馆预订审核不通过");
            put("VEN_ORDER_SUCCESS", "场馆预订审核通过");
            put("VEN_ORDER_UNADD", "场馆预订用户取消订单");
        }});
        //供需类型
        put("26", new HashMap(){{
            put("DELIVERY_SUC", "通知发布人审核配送申请");
            put("DELIVERY_SUC_ADMIN", "温馨提示配送申请还在审核");
        }});
        //众筹类型
        put("28", new HashMap(){{
            put("ZC_ACT", "众筹活动订票成功"); //未调用
            put("ZC_ACT_True", "通知众筹成功活动类众筹");
            put("ZC_CY_True", "通知成功参与众筹");
            put("ZC_Failure", "通知众筹失败");
            put("ZC_GAT_True", "通知众筹成功其它类众筹");
            put("ZC_TRA_CHECKFAIL", "众筹培训审核失败");
            put("ZC_TRA_CHECKTRUE", "众筹培训审核成功");
            put("ZC_TRA_TRUE1", "通知众筹成功培训类众筹");
            put("ZC_TRA_TRUE2", "通知众筹成功培训类众筹请等待审核");
            put("ZC_TRA_TRUE3", "通知培训类众筹审核通过参与面试");
            put("ZC_TRA_VIEWFAIL", "通知培训类众筹面试不通过");
            put("ZC_TRA_VIEWTRUE", "通知培训类众筹面试通过");
        }});
        //用户和管理员通知
        put("10000", new HashMap(){{
            put("BIND_REGISTER", "发送密码");
            put("LOGIN_PASSWROD", "微信绑定手机号密码通知");
            put("LOGIN_VALIDCODE", "发送验证码");
            put("REL_CHECK_FAIL", "通知实名认证审核失败");
            put("REL_CHECK_PASS", "通知实名认证审核成功");
            put("XJ_REASON", "通知管理员业务下架");
            put("ZC_SUCCESS_NOTICE", "通知管理员众筹成功");
        }});
    }};

    /**
     * 选项取值短信组业务类型
     * @return
     */
    public List getGpTypes(){
        List types = new ArrayList();
        for(Map.Entry<String, String> ent : smsGroupType.entrySet()){
            Map item = new HashMap();
            item.put("id", ent.getKey());
            item.put("text", ent.getValue());
            types.add(item);
        }
        return types;
    }
    /**
     * 选项取值短信组业务类型切入点
     * @return
     */
    public List getGpTypePoints(String gptypekey){
        List points = new ArrayList();
        if (gptypekey == null || gptypekey.isEmpty()) {
            return points;
        }
        Map<String, String> pointsMap = smsGroupPoint.get(gptypekey);
        if (pointsMap == null){
            return points;
        }
        for(Map.Entry ent : pointsMap.entrySet()){
            Map item = new HashMap();
            item.put("id", ent.getKey());
            item.put("text", ent.getValue());
            points.add(item);
        }
        return points;
    }


    private Map<String, String> actpointGptypeMap = null;

    /**
     * 通过切入点找到所属短信分组
     * @param actpoint
     * @return
     */
    public String getGpType4Actpoint(String actpoint){
        if (actpointGptypeMap!=null){
            return actpointGptypeMap.get(actpoint);
        }
        actpointGptypeMap = new HashMap();
        for(Map.Entry<String, Map<String,String>> group : smsGroupPoint.entrySet()){
            for(Map.Entry<String, String> point : group.getValue().entrySet()){
                actpointGptypeMap.put(point.getKey(), group.getKey());
            }
        }

        return actpointGptypeMap.get(actpoint);
    }


    //TODO 短信发送相关

    /**
     * 短信发送client构建
     * @throws Exception
     */
    private IAcsClient initAscClient() throws Exception{
        String accessKeyId = this.ywiConfigService.getConfigValue("aliyunsmsAccessKeyId", YwiConfigService.CFGTYPE_ALIYUNSMS);
        String accessKeySecret = this.ywiConfigService.getConfigValue("aliyunsmsAccessKeySecret", YwiConfigService.CFGTYPE_ALIYUNSMS);
        if (accessKeyId == null || accessKeySecret == null){
            throw new Exception("获取阿里云短信服务配置信息失败");
        }
        /*String accessKeyId = "LTAI8jvSvIq4I8Sk";
        String accessKeySecret =  "hT6ec2VLRkbruQfwbTuDbb1ELi68p9";*/
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        String product =  "Dysmsapi";
        String domain =  "dysmsapi.aliyuncs.com";

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        return new DefaultAcsClient(profile);
    }

    /**
     * 阿里云发送短信调用
     * @param phones
     * @param templateCode
     * @param param
     * @throws Exception
     */
    public List<WhgYwiSms> sendSms(String phones, String templateCode, Map param) throws Exception{
        IAcsClient acsClient = initAscClient();

        //String signName = "阿里云短信测试专用";
        String signName = this.ywiConfigService.getConfigValue("aliyunsmsSignName", YwiConfigService.CFGTYPE_ALIYUNSMS);
        if (signName == null){
            throw new Exception("获取阿里云短信服务配置信息失败");
        }

        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        request.setPhoneNumbers(phones);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        String jsonParam = "";
        if (param !=null){
            jsonParam = JSON.toJSONString(param);
            request.setTemplateParam(jsonParam);
        }
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            return this.querySmsSend(acsClient, phones, sendSmsResponse.getBizId());
        }else{
            String _code = sendSmsResponse.getCode();
            String _message = sendSmsResponse.getMessage();
            log.error("ALIYUNSMS 发送信息失败，code="+_code+" tempcode="+templateCode+" params="+jsonParam+" message="+_message);
            try {
                WhgYwiAliysmsSenderr senderr = new WhgYwiAliysmsSenderr();
                senderr.setCode(_code);
                senderr.setBizid(sendSmsResponse.getBizId());
                senderr.setMessage(_message);
                senderr.setParams(jsonParam);
                senderr.setPhone(phones);
                senderr.setTempcode(templateCode);
                this.saveSendError(senderr);
            } catch (Exception e) {
                log.error("ALIYUNSMS 保存发送失败信息出错："+e.getMessage(), e);
            }
            return null;
        }
    }

    /**
     * 阿里云查询短信发送信息
     * @param acsClient
     * @param phone
     * @param bizId
     * @return
     * @throws Exception
     */
    public List<WhgYwiSms> querySmsSend(IAcsClient acsClient, String phone, String bizId) throws Exception{
        return this.querySmsSend(acsClient, phone, bizId, new Date(), 1L, 10L);
    }
    public List<WhgYwiSms> querySmsSend(IAcsClient acsClient, String phone, String bizId, Date date, Long page, Long pageSize) throws Exception{
        if (acsClient == null){
            acsClient = initAscClient();
        }
        List<WhgYwiSms> reslist = new ArrayList();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String sendDate = sdf.format(date);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phone);
        //可选-调用发送短信接口时返回的BizId
        request.setBizId(bizId);
        //必填-短信发送的日期 支持30天内记录查询（可查其中一天的发送数据），格式yyyyMMdd
        request.setSendDate(sendDate);
        //必填-页大小
        request.setPageSize(pageSize);
        //必填-当前页码从1开始计数
        request.setCurrentPage(page);
        //hint 此处可能会抛出异常，注意catch

        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
        //获取返回结果
        if(querySendDetailsResponse.getCode() != null && querySendDetailsResponse.getCode().equals("OK")){
            //代表请求成功
            List<QuerySendDetailsResponse.SmsSendDetailDTO> list = querySendDetailsResponse.getSmsSendDetailDTOs();
            if (list!=null && list.size()>0){
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for(QuerySendDetailsResponse.SmsSendDetailDTO ent : list){
                    try {
                        Date senddate = sdf2.parse(ent.getSendDate());
                    } catch (ParseException e) {
                        log.error(e.getMessage(), e);
                    }

                    WhgYwiSms ywiSms = new WhgYwiSms();
                    ywiSms.setPhone(ent.getPhoneNum());
                    ywiSms.setSmscontent(ent.getContent());
                    ywiSms.setSenddate(sdf2.parse(ent.getSendDate()));
                    ywiSms.setSendstate(ent.getSendStatus().intValue());
                    reslist.add(ywiSms);
                }
            }
        }else{
            String _code = querySendDetailsResponse.getCode();
            String _message = querySendDetailsResponse.getMessage();
            log.error("ALIYUNSMS 查询发送信息失败，code="+_code+" message="+_message);
        }
        return reslist;
    }

   /* public static void main(String[] args) throws Exception{
        System.out.println("====>>> sendsms ...");
        SmsAliyunService test = new SmsAliyunService();
        Map param = new HashMap();
        param.put("code", "123456");
        List<WhgYwiSms> list = test.sendSms("15364189969", "SMS_140635014", param);
        System.out.println("====>>> sendsms end");
        if (list!=null) {
            System.out.println("====>>> querylist size "+ list.size());
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (WhgYwiSms ys : list) {
                System.out.println(ys.getPhone() + " " + ys.getSendstate() + " " + sdf2.format(ys.getSenddate()) + " " + ys.getSmscontent());
            }
        }
    }*/

    /**
     * 通过实体ID、短信组类型和切入点获得相关短信code
     * @param entid
     * @param gptype
     * @param actpoint
     * @return
     * @throws Exception
     */
    public String getAliySmsCode(String entid, String gptype, String point) throws Exception{
        if (gptype == null || point == null){
            return null;
        }

        WhgYwiAliysmsGroup group = null;
        //业务ID参数不为空时，先取值业务参数关联的组
        if (entid != null){
            //业务关联的组id获取
            WhgYwiAliysmsRefusegrop refuse = new WhgYwiAliysmsRefusegrop();
            refuse.setEntid(entid);
            refuse.setEnttype(gptype);
            List<WhgYwiAliysmsRefusegrop> reflist = this.whgYwiAliysmsRefusegropMapper.select(refuse);
            if (reflist != null && reflist.size()>0){
                refuse = reflist.get(0);
                group = this.whgYwiAliysmsGroupMapper.selectByPrimaryKey(refuse.getGroupid());
            }
        }
        //没有组信息时取默认组
        if (group == null){
            group = this.getAliySmsGroup4Defaut(gptype);
        }
        //默认组也没有
        if (group == null){
            return null;
        }
        //按组和切入点找到对应的模板
        WhgYwiAliysmsCode code = this.getAliysmsCode4GptypePoint(group.getId(), point);
        if (code!=null){
            return code.getTpcode();
        }

        return null;
    }

    /**
     * 通过指定短信组类型获取默认组
     * @param gptype
     * @return
     */
    public WhgYwiAliysmsGroup getAliySmsGroup4Defaut(String gptype) throws Exception{
        if (gptype == null){
            return null;
        }
        WhgYwiAliysmsGroup group = new WhgYwiAliysmsGroup();
        group.setGptype(gptype);
        group.setIsdefault(1);
        List<WhgYwiAliysmsGroup> grouplist = this.whgYwiAliysmsGroupMapper.select(group);
        if (grouplist != null && grouplist.size()>0){
            return grouplist.get(0);
        }
        return null;
    }

    /**
     * 通过短信组ID和切入点获取短信模板
     * @param groupid
     * @param point
     * @return
     * @throws Exception
     */
    public WhgYwiAliysmsCode getAliysmsCode4GptypePoint(String groupid, String point) throws Exception{
        if (groupid == null || point == null){
            return null;
        }
        WhgYwiAliysmsRefgc gc = new WhgYwiAliysmsRefgc();
        gc.setActpoint(point);
        gc.setGroupid(groupid);
        List<WhgYwiAliysmsRefgc> gclist = this.whgYwiAliysmsRefgcMapper.select(gc);
        if (gclist == null && gclist.size()==0){
            return null;
        }
        gc = gclist.get(0);
        return this.whgYwiAliysmsCodeMapper.selectByPrimaryKey(gc.getCodeid());
    }



    //TODO 后端管理业务处理支持

    /**
     * 分页查询短信组信息
     * @param page
     * @param rows
     * @param cored
     * @return
     * @throws Exception
     */
    public PageInfo selectAliySmsGroups(int page, int rows, WhgYwiAliysmsGroup cored) throws Exception{
        Example example = new Example(cored.getClass());
        Example.Criteria c = example.or();
        if (cored!=null && cored.getGpdesc()!=null){
            c.andLike("gpdesc", "%"+cored.getGpdesc()+"%");
            cored.setGptype(null);
        }
        c.andEqualTo(cored);

        PageHelper.startPage(page, rows);
        List list = this.whgYwiAliysmsGroupMapper.selectByExample(example);
        return new PageInfo(list);
    }

    /**
     * 查询指定ID短信组
     * @param id
     * @return
     * @throws Exception
     */
    public WhgYwiAliysmsGroup findAliySmsGroup(String id) throws Exception{
        return this.whgYwiAliysmsGroupMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加短信组信息
     * @param info
     * @throws Exception
     */
    public void saveAliySmsGroup(WhgYwiAliysmsGroup info) throws Exception{
        info.setId(IDUtils.getID());
        if (info.getIsdefault()!=null && info.getIsdefault().intValue() == 1){
            this.clearAliySmsGroupDefault(info.getGptype());
        }
        this.whgYwiAliysmsGroupMapper.insert(info);
    }

    /**
     * 编辑短信组信息
     * @param info
     * @throws Exception
     */
    public void editAliySmsGroup(WhgYwiAliysmsGroup info) throws Exception{
        if (info.getIsdefault()!=null && info.getIsdefault().intValue() == 1){
            this.clearAliySmsGroupDefault(info.getGptype());
        }
        this.whgYwiAliysmsGroupMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 清除组类型的默认指定
     * @param gptype
     * @throws Exception
     */
    public void clearAliySmsGroupDefault(String gptype) throws Exception{
        if (gptype==null || gptype.isEmpty()){
            return;
        }
        WhgYwiAliysmsGroup info = new WhgYwiAliysmsGroup();
        info.setIsdefault(0);
        Example example = new Example(WhgYwiAliysmsGroup.class);
        example.or().andEqualTo("gptype", gptype);

        this.whgYwiAliysmsGroupMapper.updateByExampleSelective(info, example);
    }

    /**
     * 删除指定ID的短信组，同时删除相关的短信组模板及相关的业务组引用关系
     * @param id
     * @throws Exception
     */
    public void removeAliySmsGroup(String id) throws Exception{
        if (id==null || id.isEmpty()){
            return;
        }
        //清除模板与组的关联
        WhgYwiAliysmsRefgc smsrefgc = new WhgYwiAliysmsRefgc();
        smsrefgc.setGroupid(id);
        this.whgYwiAliysmsRefgcMapper.delete(smsrefgc);
        //清除业务对组的引用
        WhgYwiAliysmsRefusegrop smsrefusergroup = new WhgYwiAliysmsRefusegrop();
        smsrefusergroup.setGroupid(id);
        this.whgYwiAliysmsRefusegropMapper.delete(smsrefusergroup);
        //删除组
        this.whgYwiAliysmsGroupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 分页查询短信模板列表
     * @param page
     * @param rows
     * @param record
     * @return
     * @throws Exception
     */
    public PageInfo selectAliysmsCodes(int page, int rows, WhgYwiAliysmsCode record) throws Exception{
        Example example = new Example(record.getClass());
        Example.Criteria c = example.or();
        if (record!=null && record.getTpname()!=null){
            c.andLike("tpname", "%"+record.getTpname()+"%");
            record.setTpname(null);
        }
        if (record!=null && record.getTpcode()!=null){
            c.andLike("tpcode", "%"+record.getTpcode()+"%");
            record.setTpcode(null);
        }
        c.andEqualTo(record);

        PageHelper.startPage(page, rows);
        List list = this.whgYwiAliysmsCodeMapper.selectByExample(example);
        return new PageInfo(list);
    }

    /**
     * 查询指定ID的短信模板
     * @param id
     * @return
     * @throws Exception
     */
    public WhgYwiAliysmsCode findAliysmsCode(String id) throws Exception{
        return this.whgYwiAliysmsCodeMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加短信模板
     * @param info
     * @throws Exception
     */
    public void saveAliySmsCode(WhgYwiAliysmsCode info) throws Exception{
        info.setId(IDUtils.getID());
        this.whgYwiAliysmsCodeMapper.insert(info);
    }

    /**
     * 保存短信模板
     * @param info
     * @throws Exception
     */
    public void editAliySmsCode(WhgYwiAliysmsCode info) throws Exception{
        this.whgYwiAliysmsCodeMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 删除短信模板
     * @param id
     * @throws Exception
     */
    public void removeAliySmsCode(String id) throws Exception{
        if (id==null || id.isEmpty()){
            return;
        }
        //清除短信组下模板的关联
        WhgYwiAliysmsRefgc refgc = new WhgYwiAliysmsRefgc();
        refgc.setCodeid(id);
        this.whgYwiAliysmsRefgcMapper.delete(refgc);
        //删除短信模板
        this.whgYwiAliysmsCodeMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询短信组的短信模板列表
     * @param groupid
     * @return
     * @throws Exception
     */
    public List srchAliySmsCode4Groupid(String groupid) throws Exception{
        return this.aliyunSmsMapper.selectAliySmsRefgc4Groupid(groupid);
    }

    /**
     * 保存短信组与模板关联信息
     * @param info
     * @throws Exception
     */
    public ResponseBean saveAliySmsRefgc(WhgYwiAliysmsRefgc info) throws Exception{
        ResponseBean rb = new ResponseBean();
        if (info == null){
            rb.setErrormsg("信息获取失败");
            rb.setSuccess(rb.FAIL);
            return rb;
        }
        if (info.getGroupid() == null || info.getGroupid().isEmpty()){
            rb.setErrormsg("短信组标识不能为空");
            rb.setSuccess(rb.FAIL);
            return rb;
        }
        if (info.getActpoint() == null || info.getActpoint().isEmpty()){
            rb.setErrormsg("切入点不能为空");
            rb.setSuccess(rb.FAIL);
            return rb;
        }
        if (info.getCodeid() == null || info.getCodeid().isEmpty()){
            rb.setErrormsg("短信模板不能为空");
            rb.setSuccess(rb.FAIL);
            return rb;
        }

        //查找对应的短信组和切入点数据
        WhgYwiAliysmsRefgc record = new WhgYwiAliysmsRefgc();
        record.setGroupid(info.getGroupid());
        record.setActpoint(info.getActpoint());
        List<WhgYwiAliysmsRefgc> reclist = this.whgYwiAliysmsRefgcMapper.select(record);
        //存在对应组和切入点就修改对应值，否则插入
        if (reclist!=null && reclist.size()>0){
            record = reclist.get(0);
            info.setId(record.getId());
            this.whgYwiAliysmsRefgcMapper.updateByPrimaryKeySelective(info);
        } else {
            info.setId(IDUtils.getID());
            this.whgYwiAliysmsRefgcMapper.insert(info);
        }

        return rb;
    }

    /**
     * 删除关联信息
     * @param refid
     * @throws Exception
     */
    public void removeAliySmsRefgc(String refid) throws Exception{
        this.whgYwiAliysmsRefgcMapper.deleteByPrimaryKey(refid);
    }

    /**
     * 查找业务引用短信组
     * @param entid
     * @param enttype
     * @return
     * @throws Exception
     */
    public Object findAliySmsRefuseGroup(String entid, String enttype) throws Exception{
        WhgYwiAliysmsRefusegrop refuse = new WhgYwiAliysmsRefusegrop();
        refuse.setEntid(entid);
        refuse.setEnttype(enttype);
        List list = this.whgYwiAliysmsRefusegropMapper.select(refuse);
        if (list!=null && list.size()>0){
            return list.get(0);
        }else {
            return null;
        }
    }

    /**
     * 查指定业务类型的短信组
     * @param gptype
     * @return
     * @throws Exception
     */
    public List srchAliySmsGroups4Type(String gptype) throws Exception{
        WhgYwiAliysmsGroup record = new WhgYwiAliysmsGroup();
        record.setGptype(gptype);
        return this.whgYwiAliysmsGroupMapper.select(record);
    }

    /**
     * 保存业务短信组引用
     * @param info
     * @return
     * @throws Exception
     */
    public ResponseBean saveAliySmsGroupRefuse(WhgYwiAliysmsRefusegrop info) throws Exception{
        ResponseBean rb = new ResponseBean();
        if (info == null){
            rb.setErrormsg("参数获取失败");
            rb.setSuccess(rb.FAIL);
            return rb;
        }
        if (info.getEntid() == null || info.getEntid().isEmpty()){
            rb.setErrormsg("业务标识获取失败");
            rb.setSuccess(rb.FAIL);
            return rb;
        }
        if (info.getEnttype() == null || info.getEnttype().isEmpty()){
            rb.setErrormsg("业务类型获取失败");
            rb.setSuccess(rb.FAIL);
            return rb;
        }

        WhgYwiAliysmsRefusegrop record = new WhgYwiAliysmsRefusegrop();
        record.setEntid(info.getEntid());
        record.setEnttype(info.getEnttype());
        //若无短信组标识，做清除引用处理
        if (info.getGroupid() == null || info.getGroupid().isEmpty()){
            this.whgYwiAliysmsRefusegropMapper.delete(record);
            return rb;
        }
        //若有短信组标识，保存或修改关联
        List<WhgYwiAliysmsRefusegrop> reslist = this.whgYwiAliysmsRefusegropMapper.select(record);
        if (reslist!=null && reslist.size()>0){
            WhgYwiAliysmsRefusegrop ref = reslist.get(0);
            info.setId(ref.getId());
            this.whgYwiAliysmsRefusegropMapper.updateByPrimaryKeySelective(info);
        }else {
            info.setId(IDUtils.getID());
            this.whgYwiAliysmsRefusegropMapper.insert(info);
        }

        return rb;
    }

    /**
     * 保存发送失败信息
     * @param info
     */
    public void saveSendError(WhgYwiAliysmsSenderr info) throws Exception{
        if (info == null){
            return;
        }
        info.setId(IDUtils.getID());
        info.setCrtdate(new Date());
        this.whgYwiAliysmsSenderrMapper.insertSelective(info);
    }

    /**
     * 分页查询发送失败记录
     * @param page
     * @param rows
     * @param record
     * @return
     * @throws Exception
     */
    public PageInfo selectAliysmsSendError4p(int page, int rows, WhgYwiAliysmsSenderr record) throws Exception{
        Example exp = new Example(record.getClass());
        Example.Criteria c = exp.or();
        if (record!=null){
            c.andEqualTo(record);
        }
        exp.orderBy("crtdate").desc();
        PageHelper.startPage(page, rows);
        List list = this.whgYwiAliysmsSenderrMapper.selectByExample(exp);
        return new PageInfo(list);
    }
}
