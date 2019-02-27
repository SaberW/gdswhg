package com.creatoo.hn.services.api.wechat;

import com.creatoo.hn.dao.mapper.CrtWeChatMapper;
import com.creatoo.hn.dao.mapper.WhgRepLoginMapper;
import com.creatoo.hn.dao.mapper.WhgUsrWeixinMapper;
import com.creatoo.hn.dao.model.WhgRepLogin;
import com.creatoo.hn.dao.model.WhgUser;
import com.creatoo.hn.dao.model.WhgUsrWeixin;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.user.WhgUserService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTypeService;
import com.creatoo.hn.services.wechat.*;
import com.creatoo.hn.util.IDUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**微信专用服务
 * Created by caiyong on 2017/5/8.
 */
@Service
public class WechatService extends BaseService{
    /**
     * 系统会员服务
     */
    @Autowired
    private WhgUserService whgUserService;

    /**
     * 系统分类服务
     */
    @Autowired
    private WhgYunweiTypeService whgYunweiTypeService;

    @Autowired
    private CrtWeChatMapper crtWeChatMapper;

    @Autowired
    public Environment env;

    @Autowired
    private WhgUsrWeixinMapper whgUsrWeixinMapper;


    @Autowired
    private WhgRepLoginMapper whgRepLoginMapper;

    /**
     * 获取全局的JSTicket
     * @param appid
     * @param secret
     * @param accessToken
     * @return
     * @throws Exception
     */
    public String getGlobalJsTicket4Cache(String appid, String secret, String accessToken)throws Exception{
        String jsticket = this.whgYunweiTypeService.findYwiType4AccessToken(888888);
        if(jsticket == null){//没有缓存时再调用接口取
            WeixinJsTicket jsticketObj = AdvancedUtil.getJsTicket(accessToken);
            jsticket = jsticketObj.getTicket();

            //保存缓存到数据库
            int expiresIn = 7200;//秒
            Date now = new Date();
            String expiresInStr = (now.getTime()+expiresIn*1000-120000)+"";
            this.whgYunweiTypeService.save4AccessToken(jsticket, expiresInStr, 888888);
        }
        return jsticket;
    }

    /**
     * 获取全局微信accessToken
     * @param appid
     * @param secret
     * @return 全局accessToken
     * @throws Exception
     */
    public String getGlobalToken4Cache(String appid, String secret)throws Exception{
        //先取缓存-保存在数据库的类型表中whg_ywi_type:type为999999的记录
        String accessToken = this.whgYunweiTypeService.findYwiType4AccessToken(999999);
        if(accessToken == null){//没有缓存时再调用接口取
            WeixinAccessToken wxAccessToken = AdvancedUtil.getGlobalToken(appid, secret);
            accessToken = wxAccessToken.getAccessToken();

            //保存缓存到数据库
            int expiresIn = wxAccessToken.getExpiresIn();//秒
            Date now = new Date();
            String expiresInStr = (now.getTime()+expiresIn*1000-120000)+"";
            this.whgYunweiTypeService.save4AccessToken(accessToken, expiresInStr, 999999);
        }

        return accessToken;
    }

    /**
     * 获取BaseAuthUrl
     * @param request
     * @return
     */
    public String getBaseAuthUrl(HttpServletRequest request){
        String redirectUrl = String.format("%swechat/baseauth", CommonUtil.getWebBaseUrl(request));
        String state = "init";
        String to = request.getParameter("to");
        if(StringUtils.isNotEmpty(to)){
            try {
                to = java.net.URLDecoder.decode(to, "UTF-8");//前端需要两次编码
                to = java.net.URLDecoder.decode(to, "UTF-8");//前端需要两次编码
            }catch(Exception e){
                to = null;
            }
            if(StringUtils.isNotEmpty(to)){
                state = CommonUtil.urlEncodeUTF8(to);
            }
        }
        String scope = "snsapi_userinfo";//  基础授权-snsapi_base 深度授权-snsapi_userinfo
        redirectUrl = CommonUtil.urlEncodeUTF8(redirectUrl);
        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect", this.getWechartAppId(), redirectUrl, scope, state);
        return url;
    }

    /**
     * 添加用户登录时间表记录
     * @param whgRepLogin
     */
    public void insertLoginTime(WhgRepLogin whgRepLogin) throws Exception{
        this.whgRepLoginMapper.insertSelective(whgRepLogin);
    }

    /**
     * 获取UserAuthUrl
     * @param request
     * @return
     */
    public String getUserAuthUrl(HttpServletRequest request, String state){
        String appId = this.getWechartAppId();
        String redirectUrl = String.format("%swechat/userauth", "");
        String scope = "snsapi_userinfo";
        redirectUrl = CommonUtil.urlEncodeUTF8(redirectUrl);
        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect", appId, redirectUrl, scope, state);
        return url;
    }

    /**
     * 微信授权访问
     * @param snsUserInfo 微信用户信息
     * @return true-有系统账号并且已经绑定到微信公众号, false-没有
     * @throws Exception
     */
    public String wxAccess(SNSUserInfo snsUserInfo)throws Exception{
        String flag = null;
        try{
            //微信用户标识
            String openId = snsUserInfo.getOpenId();

            //微信用户昵称
            String nickName = snsUserInfo.getNickname();
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher = emoji.matcher(nickName);
            if (emojiMatcher.find()) {
                nickName = emojiMatcher.replaceAll("*");
            }

            //微信授权访问时间
            Date now = new Date();

            //通过openId判断是否有微信记录，没有写入微信记录
            WhgUsrWeixin wuwx = new WhgUsrWeixin();
            wuwx.setOpenid(openId);
            int cnt = this.whgUsrWeixinMapper.selectCount(wuwx);
            if(cnt == 1){//有微信访问记录
                WhgUsrWeixin wxUser = this.whgUsrWeixinMapper.selectOne(wuwx);

                //是否已经绑定PC账号
                if(StringUtils.isNotEmpty(wxUser.getUserid())){
                    WhgUser whgUser = this.whgUserService.t_srchOne(wxUser.getUserid());
                    if(whgUser != null && StringUtils.isNotEmpty(whgUser.getPhone())){
                        flag = whgUser.getId();
                    }
                }
                wxUser.setOpenid(snsUserInfo.getOpenId());
                wxUser.setCity(snsUserInfo.getCity());
                wxUser.setCountry(snsUserInfo.getCountry());
                wxUser.setCrtdate(now);
                wxUser.setNickname(nickName);
                wxUser.setHeadimgurl(snsUserInfo.getHeadImgUrl());
                wxUser.setProvince(snsUserInfo.getProvince());
                wxUser.setSex(snsUserInfo.getSex());
                //应对测试环境没有unionid的情况
                if(null == wxUser.getUnionid()){
                    wxUser.setUnionid(wxUser.getOpenid());
                }
                this.whgUsrWeixinMapper.updateByPrimaryKeySelective(wxUser);
            }else{//有微信访问记录
                WhgUsrWeixin whgUsrWeixin = new WhgUsrWeixin();
                whgUsrWeixin.setUnionid(snsUserInfo.getUnionid());
                whgUsrWeixin.setOpenid(openId);
                whgUsrWeixin.setId(IDUtils.getID());
                whgUsrWeixin.setCity(snsUserInfo.getCity());
                whgUsrWeixin.setCountry(snsUserInfo.getCountry());
                whgUsrWeixin.setCrtdate(now);
                whgUsrWeixin.setNickname(nickName);
                whgUsrWeixin.setHeadimgurl(snsUserInfo.getHeadImgUrl());
                whgUsrWeixin.setProvince(snsUserInfo.getProvince());
                whgUsrWeixin.setSex(snsUserInfo.getSex());
                whgUsrWeixin.setCrtdate(new Date());
                //应对测试环境没有unionid的情况
                if(null == whgUsrWeixin.getUnionid()){
                    whgUsrWeixin.setUnionid(whgUsrWeixin.getOpenid());
                }
                this.whgUsrWeixinMapper.insert(whgUsrWeixin);
            }
        }catch(Exception e){
            throw e;
        }
        return flag;
    }

    public  String  getWechartAppId(){
       return env.getProperty("wechat.appid");
    }

    public  String  getWechartAppsecret(){
        return env.getProperty("wechat.appsecret");
    }



    /**
     * 获取WhWechat
     * @param openId
     * @return
     */
    public WhgUsrWeixin getSessionByOpenId(String openId){
        try {
            WhgUsrWeixin whgUsrWeixin = new WhgUsrWeixin();
            whgUsrWeixin.setOpenid(openId);
            List<WhgUsrWeixin> list = crtWeChatMapper.queryWhWeChat(whgUsrWeixin);
            if(null == list || list.isEmpty()){
                return null;
            }
            return list.get(0);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 获取WhWechat
     * @param unionid
     * @return
     */
    public WhgUsrWeixin getSessionByUnionId(String unionid){
        try {
            WhgUsrWeixin whgUsrWeixin = new WhgUsrWeixin();
            whgUsrWeixin.setUnionid(unionid);
            List<WhgUsrWeixin> list = crtWeChatMapper.queryWhWeChat(whgUsrWeixin);
            if(null == list || list.isEmpty()){
                return null;
            }
            return list.get(0);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    public int updateSession(WhgUsrWeixin whgUsrWeixin){
        try {
            whgUsrWeixinMapper.updateByPrimaryKeySelective(whgUsrWeixin);
            return 0;
        }catch (Exception e){
            log.error(e.toString());
            return 1;
        }
    }

    public int addSession(WhgUsrWeixin whgUsrWeixin){
        try {
            whgUsrWeixinMapper.insert(whgUsrWeixin);
            return 0;
        }catch (Exception e){
            log.error(e.toString());
            return 1;
        }
    }

    /**
     * 根据openID查找用户
     * @param openId
     * @return
     */
    public List<WhgUsrWeixin> selUser(String openId)throws Exception {
        Example example = new Example(WhgUsrWeixin.class);
        example.createCriteria().andEqualTo("openid",openId);
        List<WhgUsrWeixin> wxUser = this.whgUsrWeixinMapper.selectByExample(example);
        return wxUser;
    }
}
