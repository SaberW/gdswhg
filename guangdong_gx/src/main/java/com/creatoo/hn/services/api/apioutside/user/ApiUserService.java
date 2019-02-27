package com.creatoo.hn.services.api.apioutside.user;

import com.creatoo.hn.dao.mapper.WhgCodeMapper;
import com.creatoo.hn.dao.mapper.WhgUserMapper;
import com.creatoo.hn.dao.mapper.WhgUserWeixinMapper;
import com.creatoo.hn.dao.model.WhgCode;
import com.creatoo.hn.dao.model.WhgUser;
import com.creatoo.hn.dao.model.WhgUserWeixin;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.UploadUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rbg on 2017/8/7.
 */

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgUser", keyGenerator = "simpleKeyGenerator")
public class ApiUserService extends BaseService {

    @Autowired
    private WhgUserMapper whgUserMapper;

    @Autowired
    private WhgCodeMapper whgCodeMapper;

    @Autowired
    private WhgUserWeixinMapper whgUserWeixinMapper;

    @Autowired
    private SMSService smsService;

    public static final String MOBILE_REX = "1[3-8]\\d{9}";

    /**
     * 用户注册
     * @param whgUser
     * @param code
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object userRegister (WhgUser whgUser, String code) throws Exception{
        /*ApiResultBean arb = (ApiResultBean) this.validWhgUser4Register(whgUser, code);
        if (arb.getCode()!=null && arb.getCode().intValue()!=0){
            return arb;
        }*/
        ApiResultBean arb = new ApiResultBean();
        if (whgUser ==null ){
            arb.setCode(103);
            arb.setMsg("注册信息获取失败");
            return arb;
        }
        if (whgUser.getPhone()==null || whgUser.getPhone().isEmpty() || !whgUser.getPhone().matches(MOBILE_REX)){
            arb.setCode(103);
            arb.setMsg("手机号格式不正确");
            return arb;
        }

        if (whgUser.getPassword()==null || whgUser.getPassword().isEmpty()){
            arb.setCode(103);
            arb.setMsg("登录密码不能为空");
            return arb;
        }

        boolean isUsePhone= this.isPhone(whgUser.getPhone());
        if (isUsePhone){
            arb.setCode(103);
            arb.setMsg("手机用户已存在");
            return arb;
        }

        if (code == null || code.isEmpty()){
            arb.setCode(102);
            arb.setMsg("短信验证码验证失败");
            return arb;
        }

        boolean isSmsCode = this.validSmsCode(whgUser.getPhone(), code);
        if (!isSmsCode){
            arb.setCode(102);
            arb.setMsg("短信验证码验证失败");
            return arb;
        }

        if (whgUser.getNickname()==null){
            String phone = whgUser.getPhone();
            String nickname = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            whgUser.setNickname(nickname);
        }
        whgUser.setIsrealname(0);
        whgUser.setIsperfect(0);
        whgUser.setRegisttime(new Date());
        whgUser.setId(IDUtils.getID());

        this.whgUserMapper.insert(whgUser);

        arb.setData(whgUser);
        return arb;
    }


    /**
     * 注册第二步，填入性别职业和眤称
     * @param whgUser
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object userRegister2(String id, WhgUser whgUser) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (id == null || id.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("用户标记不能为空");
            return arb;
        }

        WhgUser user = this.whgUserMapper.selectByPrimaryKey(id);
        if (user == null) {
            arb.setCode(102);
            arb.setMsg("用户信息查询失败");
            return arb;
        }

        if (whgUser == null) {
            arb.setCode(103);
            arb.setMsg("注册信息获取失败");
            return arb;
        }

        if (whgUser.getNickname() == null || whgUser.getNickname().isEmpty()) {
            arb.setCode(103);
            arb.setMsg("眤称不能为空");
            return arb;
        }

        /*if (whgUser.getSex() == null) {
            arb.setCode(103);
            arb.setMsg("性别不能为空");
            return arb;
        }

        if (whgUser.getJob()==null || whgUser.getJob().isEmpty()){
            arb.setCode(103);
            arb.setMsg("职业不能为空");
            return arb;
        }*/

        boolean isNick = this.isNickname(whgUser.getNickname(), whgUser.getId());
        if (isNick) {
            arb.setCode(103);
            arb.setMsg("眤称已被使用");
            return arb;
        }

        whgUser.setId(id);
        this.whgUserMapper.updateByPrimaryKeySelective(whgUser);

        return arb;
    }

    /**
     * 验证注册信息
     * @param whgUser
     * @param code
     * @return
     * @throws Exception
     */
    /*private Object validWhgUser4Register(WhgUser whgUser, String code) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (whgUser ==null ){
            arb.setCode(103);
            arb.setMsg("注册信息获取失败");
            return arb;
        }
        if (whgUser.getPhone()==null || whgUser.getPhone().isEmpty() || !whgUser.getPhone().matches(MOBILE_REX)){
            arb.setCode(103);
            arb.setMsg("手机号格式不正确");
            return arb;
        }

        if (whgUser.getPassword()==null || whgUser.getPassword().isEmpty()){
            arb.setCode(103);
            arb.setMsg("登录密码不能为空");
            return arb;
        }

        boolean isUsePhone= this.isPhone(whgUser.getPhone());
        if (isUsePhone){
            arb.setCode(103);
            arb.setMsg("手机用户已存在");
            return arb;
        }

        if (code == null || code.isEmpty()){
            arb.setCode(102);
            arb.setMsg("短信验证码验证失败");
            return arb;
        }

        boolean isSmsCode = this.validSmsCode(whgUser.getPhone(), code);
        if (!isSmsCode){
            arb.setCode(102);
            arb.setMsg("短信验证码验证失败");
            return arb;
        }

        return arb;
    }*/

    /**
     * 验证手机短信验证码
     * @param phone
     * @param code
     * @return
     * @throws Exception
     */
    public boolean validSmsCode(String phone, String code) throws Exception{
        Calendar ctime = Calendar.getInstance();
        ctime.add(Calendar.MINUTE, -5);

        Example example = new Example(WhgCode.class);
        example.createCriteria()
                .andEqualTo("msgphone", phone)
                .andEqualTo("msgcontent", code)
                .andGreaterThanOrEqualTo("msgtime", ctime.getTime());
        int usecode = this.whgCodeMapper.selectCountByExample(example);

        return usecode > 0;
    }


    /**
     * 是否是用户表中的手机号
     * @param phone
     * @return
     * @throws Exception
     */
    public boolean isPhone(String phone) throws Exception {
        WhgUser user = new WhgUser();
        user.setPhone(phone);
        int count = this.whgUserMapper.selectCount(user);
        return count>0;
    }

    /**
     * 验证眤称是否在用
     * @param nickname
     * @return
     * @throws Exception
     */
    public boolean isNickname(String nickname, String id) throws Exception{
        Example example = new Example(WhgUser.class);
        example.createCriteria()
                .andEqualTo("nickname", nickname)
                .andNotEqualTo("id", id);
        int count = this.whgUserMapper.selectCountByExample(example);
        return count>0;
    }


    /**
     * 发手机验证码
     * @param phone
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object sendSmsCode(String phone) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (phone == null || phone.isEmpty()){
            arb.setCode(101);
            arb.setMsg("手机号码没有指定");
            return arb;
        }

        //当天的手机验证码发送记录
        Calendar dayTime = Calendar.getInstance();
        dayTime.set(Calendar.HOUR_OF_DAY, 0);
        dayTime.set(Calendar.MINUTE, 0);
        dayTime.set(Calendar.SECOND, 0);
        Date dayStart = dayTime.getTime();
        dayTime.add(Calendar.DAY_OF_YEAR, 1);
        Date dayEnd = dayTime.getTime();

        Example example = new Example(WhgCode.class);
        example.createCriteria()
                .andEqualTo("msgphone", phone)
                .andGreaterThanOrEqualTo("msgtime", dayStart)
                .andLessThan("msgtime", dayEnd);
        example.orderBy("msgtime").desc();

        List<WhgCode> list = this.whgCodeMapper.selectByExample(example);
        if (list!=null && list.size()>=6){
            arb.setCode(102);
            arb.setMsg("验证码一天最多发6次");
            return arb;
        }
        if (list!=null && list.size()>0){
            WhgCode recode = list.get(0);
            if (recode.getMsgtime()!=null){
                Date now = new Date();
                long lastSend = (now.getTime()-recode.getMsgtime().getTime())/1000;
                if (lastSend < 120){
                    long notSecond = 120 - lastSend;
                    arb.setCode(103);
                    arb.setMsg(notSecond+"秒后重新发送验证码");
                    return arb;
                }
            }
        }

        //验证码
        Random random = new Random();
        int max = 1000000;
        int min = 100000;
        int xxx = random.nextInt(max-min)+min;
        String smsCode = xxx+"";

        Map smsData = new HashMap();
        smsData.put("validCode", smsCode);

        //发送
        String id = IDUtils.getID();
        this.smsService.t_sendSMS(phone, "LOGIN_VALIDCODE", smsData, id);

        //处理同手机号旧的有效验证码失效
        Calendar ctime = Calendar.getInstance();
        ctime.add(Calendar.MINUTE, -5);

        Example exp = new Example(WhgCode.class);
        exp.createCriteria()
                .andEqualTo("msgphone", phone)
                .andGreaterThanOrEqualTo("msgtime", ctime.getTime());

        ctime.add(Calendar.MINUTE, -1);
        WhgCode whgcode = new WhgCode();
        whgcode.setMsgtime(ctime.getTime());
        this.whgCodeMapper.updateByExampleSelective(whgcode, exp);

        //保存
        WhgCode recode = new WhgCode();
        recode.setId(id);
        recode.setMsgcontent(smsCode);
        recode.setMsgtime(new Date());
        recode.setMsgphone(phone);
        this.whgCodeMapper.insert(recode);

        //arb.setData(smsCode);
        return arb;
    }

    /**
     * 处理登录
     * @param phone
     * @param password
     * @return
     * @throws Exception
     */
    //@Cacheable
    public Object userLogin(String phone, String password, String visitType) throws Exception {
        ApiResultBean arb = new ApiResultBean();

        if (phone == null || phone.isEmpty() || password == null || password.isEmpty()){
            arb.setCode(101);
            arb.setMsg("用户名或密码错误");
            return arb;
        }

        WhgUser user = new WhgUser();
        user.setPhone(phone);
        if ("".equals(visitType)) {
            user.setPassword(password);
        }
        List<WhgUser> list = this.whgUserMapper.select(user);
        if (list == null || list.size()==0){
            arb.setCode(101);
            arb.setMsg("用户名或密码错误");
            return arb;
        }

        user = list.get(0);

        Map rest = new HashMap();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//        BeanMap bm = new BeanMap(user);
//        rest.putAll(bm);
        rest.put("userId", user.getId());
        rest.put("userName", user.getPhone());
        rest.put("mobile", user.getPhone());
        rest.put("relationMobile", user.getPhone());
        rest.put("sex", user.getSex());
        rest.put("idcardtype", user.getIdcardtype());
        if (user.getBirthday()!=null) {
            rest.put("birthday", sdf.format(user.getBirthday()));
        }
        rest.put("nickName", user.getNickname());
        rest.put("authState", user.getIsrealname());
        rest.put("idCard", user.getIdcard());
        if (user.getHeadurl() != null && !"".equals(user.getHeadurl())) {
            rest.put("headimgurl", env.getProperty("upload.local.server.addr") + user.getHeadurl());
        } else {
            //添加微信用户信息

            Example example = new Example(WhgUserWeixin.class);
            example.createCriteria()
                    .andEqualTo("userid", user.getId());
            example.orderBy("crtdate").desc();

            List<WhgUserWeixin> weixinlist = this.whgUserWeixinMapper.selectByExample(example);
            WhgUserWeixin wxUser = null;
            if (weixinlist.size() > 0) {
                wxUser = weixinlist.get(0);
            }
            if (wxUser != null) {
                rest.put("headimgurl", wxUser.getHeadimgurl());
            }
        }
        arb.setData(rest);
        return arb;
    }

    /**
     * 根据Id查询用户详情
     * @param userId
     * @return
     */

    public WhgUser getUserDetail(String userId){
        return whgUserMapper.selectByPrimaryKey(userId);
    }


    /**
     * 获取微信用户资料
     * @param openId
     * @return
     * @throws Exception
     */
    @Cacheable
    public Object wechatLogin(String openId) throws Exception{
        ApiResultBean arb = new ApiResultBean();

        if (openId == null || openId.isEmpty()){
            arb.setCode(101);
            arb.setMsg("用户标识为空");
            return arb;
        }

        WhgUserWeixin wx = new WhgUserWeixin();
        wx.setOpenid(openId);
        List<WhgUserWeixin> list = this.whgUserWeixinMapper.select(wx);
        if (list == null || list.size()==0){
            arb.setCode(101);
            arb.setMsg("登录失败");
        }
        wx = list.get(0);
        WhgUser user = this.whgUserMapper.selectByPrimaryKey(wx.getUserid());

        Map rest = new HashMap();
        rest.put("userId", user.getId());
        rest.put("userName", user.getPhone());
        rest.put("mobile", user.getPhone());
        rest.put("relationMobile", user.getPhone());
        rest.put("sex", user.getSex());
        rest.put("birthday", user.getBirthday());
        rest.put("nickName", user.getNickname());
        rest.put("authState", user.getIsrealname());

        arb.setData(rest);

        return arb;
    }

    /**
     * 关联手机
     *
     * @param userId
     * @param mobile
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object relationMobile(String userId, String mobile, String code) throws Exception {
        ApiResultBean arb = new ApiResultBean();

        if (userId == null || userId.isEmpty() || mobile == null || mobile.isEmpty() || code == null || code.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("参数不能为空");
            return arb;
        }

        if (!mobile.matches(MOBILE_REX)) {
            arb.setCode(103);
            arb.setMsg("手机格式错误");
            return arb;
        }

        boolean iscode = this.validSmsCode(mobile, code);
        if (!iscode) {
            arb.setCode(103);
            arb.setMsg("手机验证码错误");
            return arb;
        }

        WhgUser user = this.whgUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            arb.setCode(102);
            arb.setMsg("用户不存在");
            return arb;
        }

        /*Example exp = new Example(WhgUser.class);
        exp.createCriteria()
                .andNotEqualTo("id", userId)
                .andEqualTo("phone", mobile);
        int ct = this.whgUserMapper.selectCountByExample(exp);
        if (ct>0){
            arb.setCode(102);
            arb.setMsg("该手机号已被使用");
            return arb;
        }*/
        boolean ismobile = this.isPhone(mobile);
        if (ismobile) {
            arb.setCode(103);
            arb.setMsg("手机已被使用");
            return arb;
        }

        user.setPhone(mobile);
        this.whgUserMapper.updateByPrimaryKeySelective(user);

        return arb;
    }


    /**
     * 微信绑定手机
     * @param userId
     * @param mobile
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object relationMobile(String openid, String mobile, String code, String pwd) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if(StringUtils.isEmpty(openid)){
            arb.setCode(103);
            arb.setMsg("参数openid不能为空");
            return arb;
        }
        if(StringUtils.isEmpty(mobile)){
            arb.setCode(103);
            arb.setMsg("参数mobile不能为空");
            return arb;
        }

        if(StringUtils.isEmpty(code)){
            arb.setCode(103);
            arb.setMsg("参数code不能为空");
            return arb;
        }
        if (!mobile.matches(MOBILE_REX)){
            arb.setCode(103);
            arb.setMsg("手机号码格式错误");
            return arb;
        }
        boolean iscode = this.validSmsCode(mobile,code);
        if (!iscode){
            arb.setCode(103);
            arb.setMsg("手机验证码不正确");
            return arb;
        }

        WhgUserWeixin wxUser = new WhgUserWeixin();
        wxUser.setOpenid(openid);
        try {
            wxUser = this.whgUserWeixinMapper.selectOne(wxUser);
        }catch (Exception e){}
        if(wxUser == null || StringUtils.isEmpty(wxUser.getId())){
            arb.setCode(102);
            arb.setMsg("微信未授权，请重新授权");
            return arb;
        }

        //手机号是否已经存在系统中
        WhgUser user = new WhgUser();
        user.setPhone(mobile);
        user = this.whgUserMapper.selectOne(user);
        if(user != null && StringUtils.isNotEmpty(user.getId())){//存在系统中
            wxUser.setUserid(user.getId());
            user.setWxopenid(openid);
            user.setOpenid(openid);
            this.whgUserMapper.updateByPrimaryKeySelective(user);
            wxUser.setUserid(user.getId());
            this.whgUserWeixinMapper.updateByPrimaryKeySelective(wxUser);
            arb.setMsg("微信绑定手机号成功");
            arb.setData(user.getId());
            return arb;
        }else{//手机号不存在系统，需要新生成系统用户信息
            if(StringUtils.isEmpty(pwd)){
                arb.setCode(103);
                arb.setMsg("参数pwd不能为空");
                return arb;
            }
            Date now = new Date();
            user = new WhgUser();
            user.setId(IDUtils.getID());
            user.setName(wxUser.getNickname());
            user.setNickname(wxUser.getNickname());
            user.setLastdate(now);
            user.setSex(wxUser.getSex() == 1 ? "1" : "0");
            user.setPassword(pwd);
            user.setIsrealname(0);
            user.setOpenid(openid);
            user.setPhone(mobile);
            user.setWxopenid(openid);
            user.setRegisttime(now);
            this.whgUserMapper.insert(user);
            wxUser.setUserid(user.getId());
            this.whgUserWeixinMapper.updateByPrimaryKeySelective(wxUser);
            arb.setData(user.getId());
        }
        return arb;
    }

    /**
     * 手机号是否已经存在系统中
     * @param phone 手机号
     * @return true-此手机号已经注册成为会员, false-手机号未注册成为会员
     * @throws Exception
     */
    public String existPhoneInSystem(String phone)throws Exception{
        WhgUser user = new WhgUser();
        user.setPhone(phone);
        user = this.whgUserMapper.selectOne(user);
        if(user != null && StringUtils.isNotEmpty(user.getId())){
            return user.getId();
        }
        return null;
    }

    /**
     * 用户是否已经绑定微信
     * @param userid 会员标识
     * @return true-已经绑定 false-未绑定
     * @throws Exception
     */
    public boolean bindWeixin(String userid,String openid)throws Exception{
        WhgUserWeixin weixinUser = new WhgUserWeixin();
        weixinUser.setUserid(userid);
        weixinUser.setOpenid(openid);
        return this.whgUserWeixinMapper.selectCount(weixinUser) > 0;
    }

    /**
     * 找回密码
     * @param mobile
     * @param code
     * @param password
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object setPasswd(String mobile, String code, String password) throws Exception{
        ApiResultBean arb = new ApiResultBean();

        if (mobile==null || mobile.isEmpty() || code==null || code.isEmpty()){
            arb.setCode(103);
            arb.setMsg("手机号不能为空");
            return arb;
        }

        boolean isSmsCode = this.validSmsCode(mobile, code);
        if (!isSmsCode){
            arb.setCode(102);
            arb.setMsg("验证码校验错误");
            return arb;
        }

        if (password==null || password.isEmpty()){
            arb.setCode(103);
            arb.setMsg("密码不能为空");
            return arb;
        }

        if (!mobile.matches(MOBILE_REX)){
            arb.setCode(103);
            arb.setMsg("手机格式错误");
            return arb;
        }

        WhgUser user = new WhgUser();
        user.setPhone(mobile);
        List<WhgUser> list = this.whgUserMapper.select(user);
        if (list==null || list.size()==0){
            arb.setCode(103);
            arb.setMsg("手机号不存在");
            return arb;
        }

        for(WhgUser ent : list){
            WhgUser recode = new WhgUser();
            recode.setId(ent.getId());
            recode.setPassword(password);
            this.whgUserMapper.updateByPrimaryKeySelective(recode);
        }

        return arb;
    }

    /**
     * 修改密码
     * @param userId
     * @param passWord
     * @param newPwdMd5
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object setPassword(String userId, String passWord, String newPwdMd5) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (userId == null || userId.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("用户标识不能为空");
            return arb;
        }
        if (passWord == null || passWord.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("原始密码不能为空");
            return arb;
        }
        if (newPwdMd5 == null || newPwdMd5.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("新密码不能为空");
            return arb;
        }

        WhgUser user = new WhgUser();
        user.setId(userId);
        if(!"init".equals(passWord)){//第一次设置密码，不需要验证原始密码
            user.setPassword(passWord);
        }

        List<WhgUser> list = this.whgUserMapper.select(user);
        if (list == null || list.size() == 0) {
            arb.setCode(102);
            arb.setMsg("原始密码不正确");
            return arb;
        }

        user = list.get(0);
        user.setPassword(newPwdMd5);
        this.whgUserMapper.updateByPrimaryKeySelective(user);

        return arb;
    }


    /**
     * 实名认证步骤A
     */
    @CacheEvict(allEntries = true)
    public Object authVerifyA(String userId, String fullName, String idCard) throws Exception{
        ApiResultBean arb = new ApiResultBean();

        if (userId == null || userId.isEmpty()) {
            arb.setCode(101);
            arb.setMsg("用户标识不能为空");
            return arb;
        }
        if (fullName == null || fullName.isEmpty()) {
            arb.setCode(101);
            arb.setMsg("姓名不能为空");
            return arb;
        }
        if (idCard == null || idCard.isEmpty() || idCard.matches("\\d{15}|\\d{18}|\\d{17}[xX]")) {
            arb.setCode(101);
            arb.setMsg("身份证为空或格式不正确");
            return arb;
        }

        WhgUser user = this.whgUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            arb.setCode(102);
            arb.setMsg("用户信息不存在");
            return arb;
        }

        if (user.getIsrealname() != null && user.getIsrealname().intValue()==1) {
            arb.setCode(105);
            arb.setMsg("用户已完成实名");
            return arb;
        }

        String year = idCard.substring(6,10);
        String month = idCard.substring(10,12);
        String day = idCard.substring(12,14);
        String birth = year+"-"+month+"-"+day;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        WhgUser upUser = new WhgUser();
        upUser.setId(userId);
        upUser.setName(fullName);
        upUser.setIdcard(idCard);
        upUser.setBirthday(sdf.parse(birth));

        if (user.getIdcardface()!=null && !user.getIdcardface().isEmpty()
                && user.getIdcardback()!=null && !user.getIdcardback().isEmpty()){
            upUser.setIsrealname(3);
        }

        this.whgUserMapper.updateByPrimaryKeySelective(upUser);

        return arb;
    }
    /**
     * 实名认证
     */
    @CacheEvict(allEntries = true)
    public ApiResultBean authVerifyCode(String userId, String accesstoken,String frontId, String backId,String fullName, String idCard) throws Exception{
        ApiResultBean arb = new ApiResultBean();

        if (userId == null || userId.isEmpty()) {
            arb.setCode(101);
            arb.setMsg("用户标识不能为空");
            return arb;
        }
        if (fullName == null || fullName.isEmpty()) {
            arb.setCode(101);
            arb.setMsg("姓名不能为空");
            return arb;
        }
        if (idCard == null || idCard.isEmpty() || idCard.matches("\\d{15}|\\d{18}|\\d{17}[xX]")) {
            arb.setCode(101);
            arb.setMsg("身份证为空或格式不正确");
            return arb;
        }
        if (frontId == null || frontId.isEmpty()) {
            arb.setCode(101);
            arb.setMsg("身份证正面照资源ID不能为空");
            return arb;
        }
        if (backId == null || backId.isEmpty()) {
            arb.setCode(101);
            arb.setMsg("身份证背面照资源ID不能为空");
            return arb;
        }
        if (accesstoken == null || accesstoken.isEmpty()) {
            arb.setCode(101);
            arb.setMsg("accesstoken不能为空");
            return arb;
        }

        WhgUser user = this.whgUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            arb.setCode(102);
            arb.setMsg("用户信息不存在");
            return arb;
        }

        if (user.getIsrealname() != null && user.getIsrealname().intValue()==1) {
            arb.setCode(105);
            arb.setMsg("用户已完成实名");
            return arb;
        }

        String year = idCard.substring(6,10);
        String month = idCard.substring(10,12);
        String day = idCard.substring(12,14);
        String birth = year+"-"+month+"-"+day;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String idcardface = this._saveMediaImg2Local(accesstoken, frontId);
        String idcardback = this._saveMediaImg2Local(accesstoken, backId);
        WhgUser upUser = new WhgUser();
        upUser.setId(userId);
        upUser.setName(fullName);
        upUser.setIdcard(idCard);
        upUser.setBirthday(sdf.parse(birth));
        upUser.setIdcardface(idcardface);
        upUser.setIdcardback(idcardback);

        if (user.getIdcardface()!=null && !user.getIdcardface().isEmpty()
                && user.getIdcardback()!=null && !user.getIdcardback().isEmpty()){
            upUser.setIsrealname(3);
        }

        this.whgUserMapper.updateByPrimaryKeySelective(upUser);

        return arb;
    }

    /**
     * 修改用户资料
     * @param whUser 用户信息
     * @throws Exception
     */
    public void saveUserInfo(WhgUser whUser)throws  Exception{
        Example example = new Example(WhgUser.class);
        example.createCriteria().andEqualTo("id", whUser.getId());
        this.whgUserMapper.updateByExampleSelective(whUser, example);
    }


    /**
     * 实名认证步骤B
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = false)
    public Object authVerifyB(String userId, String accesstoken, String frontId, String backId) throws Exception{
        ApiResultBean arb = new ApiResultBean();

        if (userId == null || userId.isEmpty()) {
            arb.setCode(101);
            arb.setMsg("用户标识不能为空");
            return arb;
        }
        if (frontId == null || frontId.isEmpty()) {
            arb.setCode(101);
            arb.setMsg("身份证正面照资源ID不能为空");
            return arb;
        }
        if (backId == null || backId.isEmpty()) {
            arb.setCode(101);
            arb.setMsg("身份证背面照资源ID不能为空");
            return arb;
        }
        if (accesstoken == null || accesstoken.isEmpty()) {
            arb.setCode(101);
            arb.setMsg("accesstoken不能为空");
            return arb;
        }

        WhgUser user = this.whgUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            arb.setCode(102);
            arb.setMsg("用户信息不存在");
            return arb;
        }
        if (user.getIsrealname() != null && user.getIsrealname().intValue()==1) {
            arb.setCode(105);
            arb.setMsg("用户已完成实名");
            return arb;
        }

        String idcardface = this._saveMediaImg2Local(accesstoken, frontId);
        String idcardback = this._saveMediaImg2Local(accesstoken, backId);

        WhgUser upUser = new WhgUser();
        upUser.setId(userId);
        upUser.setIdcardface(idcardface);
        upUser.setIdcardback(idcardback);

        if (user.getIdcardface()!=null && !user.getIdcardface().isEmpty()
                && user.getIdcardback()!=null && !user.getIdcardback().isEmpty()){
            upUser.setIsrealname(3);
        }

        this.whgUserMapper.updateByPrimaryKeySelective(upUser);

        return arb;
    }

    private String _saveMediaImg2Local(String accesstoken, String media_id) throws Exception{
        Map<String, Object> mediaMap = this._getWapMediaStream(accesstoken, media_id);
        InputStream is = (InputStream) mediaMap.get("is");
        String filename = (String) mediaMap.get("filename");
        FileOutputStream fileOutputStream = null;

        String rootPath = env.getProperty("upload.local.addr");
        try {
            //获取文件后缀
            String fileExt = filename.substring(filename.lastIndexOf("."));
            Date now = new Date();
            String picPath = UploadUtil.getUploadFilePath(filename, media_id, "whguser", "picture", now);

            File dbfile = UploadUtil.createUploadFile(rootPath, picPath);
            if (dbfile.exists()){
                dbfile.delete();
            }
            dbfile.createNewFile();
            fileOutputStream = new FileOutputStream(dbfile);

            byte[] data = new byte[1024];
            int len = 0;
            while ((len = is.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }

            return UploadUtil.getUploadFileUrl(rootPath, picPath);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 按access_token 和 media_id 获取资源文件输入流
     * @param accesstoken
     * @param media_id
     * @return
     * @throws Exception
     */
    private Map<String, Object> _getWapMediaStream(String accesstoken, String media_id) throws Exception{
        InputStream is = null;
        try {
            String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="+accesstoken+ "&media_id=" + media_id;
            URL urlGet =  new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();

            // 获取文件转化为byte流
            is = http.getInputStream();

            //检查是否返回了错误信息
            String contentType = http.getContentType();
            if (contentType.startsWith("application/json")){
                int len = http.getContentLength();
                byte[] b = new byte[len]; is.read(b);
                String t = new String(b, "UTF-8");
                is.close();
                throw new Exception(t);
            }

            //文件名分析
            String disposition = http.getHeaderField("Content-Disposition");
            String filename = "XXX.jpg";
            if (disposition == null){
                contentType.startsWith("image");
                filename = media_id+".jpg";
            }else{
                String[] a = disposition.split("filename=");
                if (a.length>1){
                    filename = a[1];
                    filename = filename.replaceAll("\"", "");
                }
            }

            Map<String, Object> resmap = new HashMap<String, Object>();
            resmap.put("is", is);
            resmap.put("filename", filename);

            return resmap;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (is!=null){
                is.close();
            }
            throw e;
        }
    }

    public int updateUserHeadUrl(String userId, String headUrl) {
        try {
            Example example = new Example(WhgUser.class);
            example.createCriteria().andEqualTo("id", userId);
            List<WhgUser> whUserList = whgUserMapper.selectByExample(example);
            if (null == whUserList || whUserList.isEmpty()) {
                return 1;
            }
            WhgUser whUser = whUserList.get(0);
            whUser.setHeadurl(headUrl);
            whgUserMapper.updateByPrimaryKey(whUser);

            //微信用户图像也改掉
            WhgUserWeixin whgUserWeixin = new WhgUserWeixin();
            whgUserWeixin.setHeadimgurl(headUrl);
            Example myexample = new Example(WhgUserWeixin.class);
            myexample.createCriteria().andEqualTo("userid", userId);
            whgUserWeixinMapper.updateByExampleSelective(whgUserWeixin, myexample);

            return 0;
        } catch (Exception e) {
            log.error(e.toString());
            return 1;
        }
    }



    /**
     * 用户基本信息
     * @param userId
     * @return
     * @throws Exception
     */

    public Object findWhgUser(String userId) throws Exception{
        WhgUser user = this.whgUserMapper.selectByPrimaryKey(userId);

        Map rest = new HashMap();
        rest.put("userId", user.getId());
        rest.put("userName", user.getPhone());
        rest.put("mobile", user.getPhone());
        rest.put("relationMobile", user.getPhone());
        rest.put("pwd", StringUtils.isEmpty(user.getPassword())?"no":"yes");
        rest.put("sex", user.getSex());
        rest.put("birthday", user.getBirthday());
        rest.put("nickName", user.getNickname());
        rest.put("authState", user.getIsrealname());
        rest.put("realName", user.getName());
        rest.put("idCard", user.getIdcard());
        rest.put("idcardtype", user.getIdcardtype());
        rest.put("workPlace", user.getCompany());
        rest.put("address", user.getAddress());
        rest.put("intro", user.getResume());
        rest.put("idcardtype", user.getIdcardtype());
        rest.put("activities", user.getActbrief());
        rest.put("job", user.getJob());
        rest.put("idcardback", user.getIdcardback());
        rest.put("idcardface", user.getIdcardface());
        rest.put("checkmsg", user.getCheckmsg());
        if (user.getHeadurl()!=null && !user.getHeadurl().isEmpty()) {
            rest.put("headimgurl", env.getProperty("upload.local.server.addr") + user.getHeadurl());
        }
        WhgUserWeixin uwx = new WhgUserWeixin();
        uwx.setUserid(userId);
        List list =  this.whgUserWeixinMapper.select(uwx);
        if (list != null && list.size() > 0) {
            uwx = (WhgUserWeixin) list.get(0);
            rest.put("openId", uwx.getOpenid());
            rest.put("avatarUrl", uwx.getHeadimgurl());
            if (uwx.getHeadimgurl() != null && !uwx.getHeadimgurl().isEmpty()) {
                rest.put("headimgurl", uwx.getHeadimgurl());
            }
        }

        return rest;
    }

    /**
     * 用户单表查询
     * @param userId
     * @return
     * @throws Exception
     */
    public Object findUserInfo(String userId) throws Exception{
        return this.whgUserMapper.selectByPrimaryKey(userId);
    }

    /**
     * 根据会员标识和微信标识获得微信用户信息
     * @param userId 会员标识
     * @param openid 微信标识
     * @return 微信用户信息
     * @throws Exception
     */
    public WhgUserWeixin findWxUserInfo(String userId, String openid)throws Exception{
        WhgUserWeixin wxUser = new WhgUserWeixin();
        wxUser.setUserid(userId);
        if(StringUtils.isNotEmpty(openid)){
            wxUser.setOpenid(openid);
        }
        //return this.whgUserWeixinMapper.selectOne(wxUser);
        List<WhgUserWeixin> list = this.whgUserWeixinMapper.select(wxUser);
        if (list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 编译用户资料
     * @param user
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object editUser(WhgUser user, Boolean notValid) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (user == null){
            arb.setCode(103);
            arb.setMsg("用户信息获取失败");
            return arb;
        }

        if (user.getId() == null || user.getId().isEmpty()) {
            arb.setCode(103);
            arb.setMsg("用户标识不能为空");
            return arb;
        }

        if (notValid == null || !notValid) {

            if (user.getNickname()!=null && user.getNickname().length()>50){
                arb.setCode(103);
                arb.setMsg("昵称长度过大");
                return arb;
            }

            if (user.getSex() != null && !Arrays.asList("0", "1").contains(user.getSex())) {
                arb.setCode(103);
                arb.setMsg("性别值异常");
                return arb;
            }
            if (user.getJob() != null && user.getJob().length() > 150) {
                arb.setCode(103);
                arb.setMsg("职业长度过大");
                return arb;
            }
            if (user.getNation() != null && user.getNation().length() > 100) {
                arb.setCode(103);
                arb.setMsg("民族长度过大");
                return arb;
            }
            if (user.getOrigo() != null && user.getOrigo().length() > 100) {
                arb.setCode(103);
                arb.setMsg("籍贯长度过大");
                return arb;
            }
            if (user.getCompany() != null && user.getCompany().length() > 150) {
                arb.setCode(103);
                arb.setMsg("工作单位长度过大");
                return arb;
            }
            if (user.getAddress() != null && user.getAddress().length() > 200) {
                arb.setCode(103);
                arb.setMsg("地址长度过大");
                return arb;
            }
            if (user.getResume() != null && user.getResume().length() > 500) {
                arb.setCode(103);
                arb.setMsg("个人简历长度过大");
                return arb;
            }
            if (user.getActbrief() != null && user.getActbrief().length() > 500) {
                arb.setCode(103);
                arb.setMsg("简介长度过大");
                return arb;
            }
        }

        if (user.getPhone() != null) {
            user.setPhone(null); //不修改手机号
        }
        this.whgUserMapper.updateByPrimaryKeySelective(user);
        user = this.whgUserMapper.selectByPrimaryKey(user.getId());
        if (user.getNickname()!=null && user.getSex()!=null && user.getPhone()!=null) {
            WhgUser info = new WhgUser();
            info.setId(user.getId());
            info.setIsperfect(1);
            this.whgUserMapper.updateByPrimaryKeySelective(info);
        }

        return arb;
    }


    /**
     * 身份证上传图片
     * @param userId
     * @param filemake
     * @param file
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object uploadIdcard(String userId, String filemake, MultipartFile file, HttpServletRequest request) throws Exception {
        ApiResultBean arb = new ApiResultBean();
        if (userId == null || userId.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("用户标识不能为空");
            return arb;
        }
        if (filemake == null || filemake.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("图片标记不能为空");
            return arb;
        }
        if (file == null) {
            arb.setCode(103);
            arb.setMsg("图片文件不能为空");
            return arb;
        }
        String mimetype = file.getContentType().toLowerCase();
        if (mimetype == null || !mimetype.startsWith("image")){
            arb.setCode(103);
            arb.setMsg("文件类型不是图片");
            return arb;
        }

        //用户信息
        WhgUser user = this.whgUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            arb.setCode(102);
            arb.setMsg("用户信息查找失败");
            return arb;
        }
        //若已审核实名，阻止修改
        if (user.getIsrealname()!=null && user.getIsrealname().compareTo(new Integer(1))==0){
            arb.setCode(102);
            arb.setMsg("用户已实名审核完成，取消修改");
            return arb;
        }

        String rootPath = this.env.getProperty("upload.local.addr");//UploadUtil.getUploadPath(request);
        //删旧的相关图片
        String oldpath = user.getIdcardface();
        if (filemake!=null && filemake.equalsIgnoreCase("idcardback")){
            oldpath = user.getIdcardback();
        }
        UploadUtil.delUploadFile(rootPath, oldpath);

        //处理图片
        String picPath = UploadUtil.getUploadFilePath(
                file.getOriginalFilename(),
                IDUtils.getID(),
                "whguserreal",
                "picture",new Date());
        file.transferTo( UploadUtil.createUploadFile(rootPath, picPath) );
        String newPath = UploadUtil.getUploadFileUrl(rootPath, picPath);

        //写入新的图片关连
        if (filemake!=null && filemake.equalsIgnoreCase("idcardback")){
            user.setIdcardback(newPath);
        }else{
            user.setIdcardface(newPath);
        }

        _setterState(user);
        this.whgUserMapper.updateByPrimaryKeySelective(user);

        //返回文件路径
        arb.setData(newPath);
        return arb;
    }

    /**
     * 保存实名信息
     * @param userId
     * @param name
     * @param idcard
     * @param sex
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object saveRealInfo(String userId, String name, String idcard, String sex, Integer idcardtype, Date birthday) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (userId == null || userId.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("用户标识不能为空");
            return arb;
        }

        WhgUser user = this.whgUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            arb.setCode(102);
            arb.setMsg("用户信息查询失败");
            return arb;
        }

        //若已审核实名，阻止修改
        if (user.getIsrealname()!=null && user.getIsrealname().compareTo(new Integer(1))==0){
            arb.setCode(103);
            arb.setMsg("用户已实名审核完成，取消修改");
            return arb;
        }
        if (name == null || name.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("姓名不能为空");
            return arb;
        }
        if (idcard == null || idcard.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("身份证不能为空");
            return arb;
        }
        //若已存在审核或待审相同的身份证号，阻止修改
        if (this._getIdcardNum(idcard, user.getId()) > 0){
            arb.setCode(103);
            arb.setMsg("输入的身份证已存在，取消修改");
            return arb;
        }

        if (idcardtype!=null && idcardtype.intValue()==2){
            if (birthday!=null){
                user.setBirthday(birthday);
            }
        }
        else if (idcard.matches("^\\d{15,17}.*")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String year = idcard.substring(6,10);
            String month = idcard.substring(10,12);
            String day = idcard.substring(12,14);
            String birth = year+"-"+month+"-"+day;
            user.setBirthday(sdf.parse(birth));
        }

        user.setName(name);
        user.setIdcard(idcard);
        user.setIdcardtype(idcardtype);
        if (sex!=null){
            user.setSex(sex);
        }
        _setterState(user);
        this.whgUserMapper.updateByPrimaryKeySelective(user);

        arb.setData(user);
        return arb;
    }

    //设置用户实名状态
    private void _setterState(WhgUser user){
        //已实名的不做处理
        if (user.getIsrealname()!=null && user.getIsrealname().compareTo(new Integer(1))==0){
            return;
        }
        //信息完备后设为待审状态
        if (user.getName()!=null && !user.getName().isEmpty()
                && user.getIdcard()!=null && !user.getIdcard().isEmpty()
                && user.getIdcardface()!=null && !user.getIdcardface().isEmpty()
                && user.getIdcardback()!=null && !user.getIdcardback().isEmpty()
                && _getIdcardNum(user.getIdcard(), user.getId())==0){
            user.setIsrealname(3);
        }else{
            user.setIsrealname(0);
        }
    }
    /**
     * 检查用户身份证已审和待审状态的记录数，不包函自己ID的计数
     * @param idcard
     * @return
     */
    private int _getIdcardNum(String idcard, String uid){
        List<Integer> realState = new ArrayList<Integer>();
        realState.add(1);
        realState.add(3);
        Example example = new Example(WhgUser.class);
        example.createCriteria().andEqualTo("idcard", idcard)
                .andIn("isrealname", realState)
                .andNotEqualTo("id", uid);
        return this.whgUserMapper.selectCountByExample(example);
    }


    /**
     * 通过微信接口登录PC
     * @param unionid
     * @param openid
     * @param nickname
     * @param sex
     * @param province
     * @param city
     * @param country
     * @param headimgurl
     * @throws Exception
     */
    public void pcLoginFromWeixin(
            String unionid,
            String openid,
            String nickname,
            String sex,
            String province,
            String city,
            String country,
            String headimgurl
    )throws Exception{
        //是否已经有了微信的用户信息
        WhgUserWeixin userWeixin = new WhgUserWeixin();
        //userWeixin.setUnionid(unionid);
        userWeixin.setOpenid(openid);
        int cnt = this.whgUserWeixinMapper.selectCount(userWeixin);

        //没有微信的用户信息，保存微信的用户信息
        if(cnt < 1){
            userWeixin.setId(IDUtils.getID32());
            userWeixin.setOpenid(openid);
            userWeixin.setUnionid(unionid);
            userWeixin.setNickname(nickname);
            userWeixin.setCountry(country);
            userWeixin.setProvince(province);
            userWeixin.setCity(city);
            userWeixin.setHeadimgurl(headimgurl);
            userWeixin.setSex(Integer.parseInt(sex));
            userWeixin.setCrtdate(new Date());
            this.whgUserWeixinMapper.insert(userWeixin);
        }else{
            userWeixin = this.whgUserWeixinMapper.selectOne(userWeixin);
        }

        //是否绑定系统会员账号
        String userid = userWeixin.getUserid();
        //未绑定系统会员账号
        if(StringUtils.isEmpty(userid)){
            WhgUser whgUser = new WhgUser();
            Date now = new Date();
            whgUser.setId(IDUtils.getID());
            whgUser.setName(userWeixin.getNickname());
            whgUser.setNickname(userWeixin.getNickname());
            whgUser.setLastdate(now);
            whgUser.setSex(userWeixin.getSex() == 1 ? "1" : "0");
            //whgUser.setPassword(pwd);
            whgUser.setIsrealname(0);
            whgUser.setOpenid(openid);
            //whgUser.setPhone(mobile);
            whgUser.setWxopenid(openid);
            whgUser.setRegisttime(now);
            this.whgUserMapper.insert(whgUser);

            userWeixin.setUserid(whgUser.getId());
            this.whgUserWeixinMapper.updateByPrimaryKeySelective(userWeixin);
        }
    }
}


