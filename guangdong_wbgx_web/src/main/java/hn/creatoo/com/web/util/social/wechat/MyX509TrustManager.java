package hn.creatoo.com.web.util.social.wechat;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
* 类名: MyX509TrustManager
* 描述:信任管理器
* 开发人员： souvc
* 创建时间：  2015-11-27
* 发布版本：V1.0
**/
public class MyX509TrustManager implements X509TrustManager {
	// 检查客户端证书
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    // 检查服务器端证书
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    // 返回受信任的X509证书数组
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
