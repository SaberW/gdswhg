package com.creatoo.hn.services.api.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.mapper.WhUserMapper;
import com.creatoo.hn.mapper.WhgRepLoginMapper;
import com.creatoo.hn.mapper.WhgUsrWeixinMapper;
import com.creatoo.hn.model.WhUser_old;
import com.creatoo.hn.model.WhgRepLogin;
import com.creatoo.hn.model.WhgUsrWeixin;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.utils.social.wechat.AdvancedUtil;
import com.creatoo.hn.utils.social.wechat.CommonUtil;
import com.creatoo.hn.utils.social.wechat.SNSUserInfo;
import com.creatoo.hn.utils.social.wechat.WeixinOauth2Token;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */
@Service
public class APIWechatLoginService {
    Logger log = Logger.getLogger(this.getClass());

    /**微信信息mapper*/
    @Autowired
    private WhgUsrWeixinMapper wxUserMapper;

    /**用户mapper*/
    @Autowired
    private WhUserMapper whUserMapper;

    /**Commservice*/
    @Autowired
    private CommService commService;

    @Autowired
    private WhgRepLoginMapper whgRepLoginMapper;

    /**
     * 根据code获取微信用户信息
     * @param req
     * @return
     */
    public ResponseBean getWxUserInfo(HttpServletRequest req) {
        ResponseBean res = new ResponseBean();
        WhgUsrWeixin wxUser = null;
        String code = req.getParameter("code");
        // 获取网页授权access_token
        WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken("wx8c2ea6639590f998", "7c2e3b112ca21c14b42333174be4f19a", code);
        // 网页授权接口访问凭证
        String accessToken = weixinOauth2Token.getAccessToken();
        // 用户标识
        String openId = weixinOauth2Token.getOpenId();
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 通过网页授权获取用户信息
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            try {
                wxUser = new WhgUsrWeixin();
                // 用户的标识
                wxUser.setOpenid(jsonObject.getString("openid"));
                // 昵称
                wxUser.setNickname(jsonObject.getString("nickname"));
                // 性别（1是男性，2是女性，0是未知）
                int sex = Integer.parseInt(jsonObject.getString("sex"));
                if(sex == 2){
                    sex = 0;
                }
                wxUser.setSex(sex);
                // 用户所在国家
                if(jsonObject.getString("country") != null && !"".equals(jsonObject.getString("country"))){
                    wxUser.setCountry(jsonObject.getString("country"));
                }
                // 用户所在省份
                if(jsonObject.getString("province") != null && !"".equals(jsonObject.getString("province"))){
                    wxUser.setProvince(jsonObject.getString("province"));
                }
                // 用户所在城市
                if(jsonObject.getString("city") != null && !"".equals(jsonObject.getString("city"))){
                    wxUser.setCity(jsonObject.getString("city"));
                }

                // 用户头像
                if(jsonObject.getString("headimgurl") != null && !"".equals(jsonObject.getString("headimgurl"))){
                    wxUser.setHeadimgurl(jsonObject.getString("headimgurl"));
                }

                //JSON.parseArray(jsonObject.getString("privilege"), String.class);
                // 用户特权信息
                //snsUserInfo.setPrivilegeList(JSON.parseArray(jsonObject.getString("privilege"), String.class));
                //用户unionid
                wxUser.setUnionid(jsonObject.getString("unionid"));
                res.setData(wxUser);
            } catch (Exception e) {
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("101");
                log.error(String.format("获取用户信息失败 errcode:%d errmsg:%s"));
            }
        }
        return res;
    }

    /**
     * 保存微信信息和创建用户信息
     * @param wxUser
     * @return
     */
    public ResponseBean saveWxUser(WhgUsrWeixin wxUser)throws Exception {
        ResponseBean res = new ResponseBean();
        WhUser_old user = new WhUser_old();
        String unionid = wxUser.getUnionid();
        String openid = wxUser.getOpenid();
        if(unionid == null || "".equals(unionid) || openid == null || "".equals(openid)){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("102");
            return res;
        }
        Example example = new Example(WhgUsrWeixin.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("unionid",unionid);
        List<WhgUsrWeixin> wxUserList = wxUserMapper.selectByExample(example);
        if(wxUserList != null && wxUserList.size() > 0){
            res.setData(wxUserList.get(0));
        }else {
            String userid = commService.getKey("whUser");
            wxUser.setId(commService.getKey("whg_usr_weixin"));
            wxUser.setUserid(userid);
            wxUser.setCrtdate(new Date());
            wxUserMapper.insertSelective(wxUser);
            //保存到用户表
            user.setId(userid);
            user.setNickname(wxUser.getNickname());
            user.setWxopenid(wxUser.getOpenid());
            user.setIsrealname(0);
            user.setIsperfect(0);
            user.setIsinner(0);
            user.setRegisttime(new Date());
            whUserMapper.insertSelective(user);
            res.setData(wxUser);
        }
        return res;
    }

    /**
     * 添加用户登录时间表记录
     * @param whgRepLogin
     */
    public void insertLoginTime(WhgRepLogin whgRepLogin) throws Exception{
        this.whgRepLoginMapper.insertSelective(whgRepLogin);
    }

}
