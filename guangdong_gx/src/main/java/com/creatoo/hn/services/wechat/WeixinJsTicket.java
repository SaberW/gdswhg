package com.creatoo.hn.services.wechat;

public class WeixinJsTicket {
	// 网页JS授权接口调用凭证
    private String ticket;
    // 凭证有效时长
    private int expiresIn;

    private String noncestr;

    private String timestamp;

    private String signature;

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public String getSignature() {
        return signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
