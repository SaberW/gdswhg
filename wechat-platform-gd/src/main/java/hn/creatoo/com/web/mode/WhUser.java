package hn.creatoo.com.web.mode;

import java.util.Date;

/**
 * 登录系统的会员信息
 * Created by wangxl on 2017/6/1.
 */
public class WhUser {
    private String userId;
    private String nickName;
    private String realName;
    private String mobile;
    private String staticServerUrl;
    private String birthday;
    private String idCard;
    private Integer idcardtype;
    private String headimgurl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStaticServerUrl() {
        return staticServerUrl;
    }

    public void setStaticServerUrl(String staticServerUrl) {
        this.staticServerUrl = staticServerUrl;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Integer getIdcardtype() {
        return idcardtype;
    }

    public void setIdcardtype(Integer idcardtype) {
        this.idcardtype = idcardtype;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }


}
