package com.creatoo.hn.services.wechat;

public class WeixinAccessToken {
	// 接口调用凭证
    private String accessToken;
    // 凭证有效时长
    private int expiresIn;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
}
