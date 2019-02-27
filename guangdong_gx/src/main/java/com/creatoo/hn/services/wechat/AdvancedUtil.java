package com.creatoo.hn.services.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@SuppressWarnings("all")
public class AdvancedUtil {
    //accessToken缓存
    public static Map<String, Object> jsTicketMap = new HashMap<String, Object>();

    /**
     * 缓存两个小时取jsapiTicket
     * @param appid
     * @param secret
     * @return
     */
    public static String getAccessToken4Cache(String appid, String secret){
        String jsapiTicket4Cache = null; //jsapiTicket
        String key = "cache_accessToken";//缓存KEY
        Date now = new Date();
        if(jsTicketMap.containsKey(key)){
            Map<String, Object> obj = (Map<String, Object>)jsTicketMap.get(key);
            Date cacheDate = (Date) obj.get("cacheDate");
            if(cacheDate.after(now)){//缓存时间没到,取缓存数据
                jsapiTicket4Cache = (String) obj.get("jsapiTicket");
            }
        }

        //未取缓存或已过期
        if(StringUtils.isEmpty(jsapiTicket4Cache)){
            //清理缓存
            if(jsTicketMap.containsKey(key)){
                jsTicketMap.remove(key);
            }

            //获取accessToken
            WeixinAccessToken token = AdvancedUtil.getGlobalToken(appid, secret);
            String accessToken4Cache = token.getAccessToken();
            WeixinJsTicket weixinJsTicket = AdvancedUtil.getJsTicket(accessToken4Cache);
            jsapiTicket4Cache = weixinJsTicket.getTicket();

            //设置缓存到期时间
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(now);
            c.add(Calendar.HOUR_OF_DAY, 2);
            Date cacheDate = c.getTime();

            //保存缓存
            Map<String, Object> map = new HashMap<>();
            map.put("jsapiTicket", jsapiTicket4Cache);
            map.put("cacheDate", cacheDate);
            jsTicketMap.put(key, map);
        }
        return jsapiTicket4Cache;
    }


	/**
	 * 日志控制器
	 */ 
	static Logger log = Logger.getLogger(AdvancedUtil.class);
	
	public static String getOpenId(String appId, String redirectUrl, String state) {
		// 拼接请求地址
        String requestUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURL&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        requestUrl = requestUrl.replace("APPID", CommonUtil.urlEncodeUTF8(appId));
        requestUrl = requestUrl.replace("REDIRECTURL", CommonUtil.urlEncodeUTF8(redirectUrl));
        requestUrl = requestUrl.replace("STATE", CommonUtil.urlEncodeUTF8(state));
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                return jsonObject.toJSONString();
            } catch (Exception e) {
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("获取网页授权凭证失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return "";
	}
	
	/**
     * 获取网页授权凭证
     * 
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
        WeixinOauth2Token wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        requestUrl = requestUrl.replace("CODE", code);
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                wat = new WeixinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(Integer.parseInt(jsonObject.getString("expires_in")));
                System.out.println("expirese+in======================"+jsonObject.getString("expires_in").toString());
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wat = null;
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("获取网页授权凭证失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return wat;
    }
    
    /**
     * 通过网页授权获取用户信息
     * 
     * @param accessToken 网页授权接口调用凭证
     * @param openId 用户标识
     * @return SNSUserInfo
     */
	@SuppressWarnings( { "deprecation", "unchecked" })
    public static SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
        SNSUserInfo snsUserInfo = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 通过网页授权获取用户信息
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            try {
                snsUserInfo = new SNSUserInfo();
                // 用户的标识
                snsUserInfo.setOpenId(jsonObject.getString("openid"));
                // 昵称
                snsUserInfo.setNickname(jsonObject.getString("nickname"));
                // 性别（1是男性，2是女性，0是未知）
                snsUserInfo.setSex(Integer.parseInt(jsonObject.getString("sex")));
                // 用户所在国家
                snsUserInfo.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                snsUserInfo.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                snsUserInfo.setCity(jsonObject.getString("city"));
                // 用户头像
                snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                // 用户唯一ID
                snsUserInfo.setUnionid(jsonObject.getString("unionid"));
                // 用户特权信息
                snsUserInfo.setPrivilegeList(JSON.parseArray(jsonObject.getString("privilege"), String.class));
            } catch (Exception e) {
                snsUserInfo = null;
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("获取用户信息失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return snsUserInfo;
    }

    public static Response getResourceUrl(String resourecId, String accessToken){
	    try {
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
            requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
            requestUrl = requestUrl.replace("MEDIA_ID", resourecId);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(requestUrl).build();
            return okHttpClient.newCall(request).execute();
        }catch (Exception e){
	        log.error(e.toString());
	        return null;
        }
    }

    public static String  analysisFileName(String disposition){
        try {
            if(null == disposition){
                return null;
            }
            if(!disposition.contains("filename")){
                return null;
            }
            String fileName = disposition.substring(disposition.indexOf("filename"));
            System.out.println("fileName0:" + fileName);
            String oldFileName = fileName.substring(fileName.lastIndexOf("=") + 1);
            System.out.println("oldFileName0:" + oldFileName);
            oldFileName = oldFileName.replaceAll("\"","");
            System.out.println("oldFileName2:" + oldFileName);
            return oldFileName;
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

	/**
     * 刷新网页授权Token
     * 
     * @param appId 公众账号的唯一标识
     * @param token 公众账号的刷新密钥
     * @return WeixinAouth2Token
     */
    public static WeixinRefreshToken refreshOAuthToken(String appId, String token) {
    	WeixinRefreshToken wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESHTOKEN";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("REFRESHTOKEN", token);
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
            	if (!jsonObject.containsKey("errcode") || Integer.parseInt(jsonObject.getString("errcode")) == 0) {
            		wat = new WeixinRefreshToken();
            		wat.setAccessToken(jsonObject.getString("access_token"));
            		wat.setOpenId(jsonObject.getString("openid"));
            		wat.setRefreshToken(jsonObject.getString("refresh_token"));
            		wat.setScope(jsonObject.getString("scope"));
                    wat.setExpiresIn(Integer.parseInt(jsonObject.getString("expires_in")));
            	}
            } catch (Exception e) {
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("刷新access_token授权凭证失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return wat;
    }
	
    /**
     * 获取全局接口授权票据
     * 
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    public static WeixinAccessToken getGlobalToken(String appId, String secret) {
    	WeixinAccessToken wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("APPSECRET", secret);
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
            	if (!jsonObject.containsKey("errcode") || Integer.parseInt(jsonObject.getString("errcode")) == 0) {
                    wat = new WeixinAccessToken();
                    wat.setAccessToken(jsonObject.getString("access_token"));
                    wat.setExpiresIn(Integer.parseInt(jsonObject.getString("expires_in")));
            	}
            } catch (Exception e) {
                wat = null;
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("获取网页JS授权凭证失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return wat;
    }
    
	/**
     * 获取网页JS授权票据
     * 
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    public static WeixinJsTicket getJsTicket(String accessToken) {
    	WeixinJsTicket wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESSTOKEN&type=jsapi";
        requestUrl = requestUrl.replace("ACCESSTOKEN", accessToken);
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
            	if (!jsonObject.containsKey("errcode") || Integer.parseInt(jsonObject.getString("errcode")) == 0) {
                    wat = new WeixinJsTicket();
                    wat.setTicket(jsonObject.getString("ticket"));
                    wat.setExpiresIn(Integer.parseInt(jsonObject.getString("expires_in")));
            	}
            } catch (Exception e) {
                wat = null;
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("获取网页JS授权凭证失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return wat;
    }

    public static WeixinJsTicket getSignature(WeixinJsTicket weixinJsTicket, HttpServletRequest request){
        //其他数据
        weixinJsTicket.setNoncestr(AdvancedUtil.create_nonce_str());
        weixinJsTicket.setTimestamp(AdvancedUtil.create_timestamp());
        String signature = "";
        String string1 = "";
        String url = request.getHeader("Referer");
        String[] arrUrl = url.split("#");
        url = arrUrl[0];
        string1 = "jsapi_ticket=" + weixinJsTicket.getTicket() +
                "&noncestr=" + weixinJsTicket.getNoncestr() +
                "&timestamp=" + weixinJsTicket.getTimestamp() +
               "&url=" + url;
        signature = SHA1(string1);
        weixinJsTicket.setSignature(signature);
        return weixinJsTicket;
    }

    /**
     * 微信JS签名
     * @param ticket
     * @param noncestr
     * @param timestamp
     * @param url
     * @return
     */
    public static String wechatSignature(String ticket, String noncestr, Long timestamp, String url){
        String str = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        return SHA1(str);
    }

	public static String getQrAuthUrl(String appId, String redirectUrl, String state) {
		// 拼接请求地址
        String requestUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECTURL&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";
        requestUrl = requestUrl.replace("APPID", CommonUtil.urlEncodeUTF8(appId));
        requestUrl = requestUrl.replace("REDIRECTURL", CommonUtil.urlEncodeUTF8(redirectUrl));
        requestUrl = requestUrl.replace("STATE", CommonUtil.urlEncodeUTF8(state));
        return requestUrl;
	}

	public static String getWxAuthUrl(String appId, String redirectUrl, String state) {
        // 拼接请求地址
        String requestUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURL&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        requestUrl = requestUrl.replace("APPID", CommonUtil.urlEncodeUTF8(appId));
        requestUrl = requestUrl.replace("REDIRECTURL", CommonUtil.urlEncodeUTF8(redirectUrl));
        requestUrl = requestUrl.replace("STATE", CommonUtil.urlEncodeUTF8(state));
        return requestUrl;
    }

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * @date： 2015年12月17日 上午9:24:43
     * @description： SHA、SHA1加密
     * @parameter：   str：待加密字符串
     * @return：  加密串
     **/
    public static String SHA1(String str) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1"); //如果是SHA加密只需要将"SHA-1"改成"SHA"即可
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexStr = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
