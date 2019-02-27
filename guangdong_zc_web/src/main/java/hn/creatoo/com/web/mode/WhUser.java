package hn.creatoo.com.web.mode;

import java.io.Serializable;

/**
 * 登录系统的会员信息
 * Created by wangxl on 2017/6/1.
 */
public class WhUser implements Serializable {
    private String userId;
    private String nickName;
    private String mobile;
    private String staticServerUrl;

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
}
