package com.creatoo.hn.web.servlet;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.creatoo.hn.util.ENVUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OSS签名
 * Created by wangxl on 2017/10/22.
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/signature",
initParams={
        @WebInitParam(name="allow",value="192.168.16.110,127.0.0.1"),// IP白名单 (没有配置或者为空，则允许所有访问)
        @WebInitParam(name="deny",value="192.168.16.111"),// IP黑名单 (存在共同时，deny优先于allow)
        @WebInitParam(name="loginUsername",value="wangxl"),// 用户名
        @WebInitParam(name="loginPassword",value="wangxlpwd"),// 密码
        @WebInitParam(name="resetEnable",value="false")// 禁用HTML页面上的“Reset All”功能
})
public class PostObjectPolicy extends HttpServlet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5522372203700422672L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String endpoint = ENVUtils.env.getProperty("upload.oss.endpoint");//AliyunOssUtil.getEndpoint();//"*";
        String accessId = ENVUtils.env.getProperty("upload.oss.access.key.id");//AliyunOssUtil.getAccessKeyId();//"*";
        String accessKey = ENVUtils.env.getProperty("upload.oss.access.key.secret");//AliyunOssUtil.getAccessKeySecret();//"*";
        String bucket = ENVUtils.env.getProperty("upload.oss.bucket.name");//AliyunOssUtil.getBucketName();//"*";
        String dir = "";
        String _dir = request.getParameter("dir");
        if(_dir != null && !_dir.isEmpty() && _dir.endsWith("/")){
            dir = _dir;
        }
        String host = "http://" + bucket + "." + endpoint.substring(7);
        OSSClient client = new OSSClient(endpoint, accessId, accessKey);
        try {
            long expireTime = 60*60*24;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            //respMap.put("expire", formatISO8601Date(expiration));
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            JSONObject ja1 = JSONObject.fromObject(respMap);
            System.out.println(ja1.toString());
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
            response(request, response, ja1.toString());

        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).error(e.getMessage(), e);
        }
    }

    private void response(HttpServletRequest request, HttpServletResponse response, String results) throws IOException {
        String callbackFunName = request.getParameter("callback");
        if (callbackFunName==null || callbackFunName.equalsIgnoreCase(""))
            response.getWriter().println(results);
        else
            response.getWriter().println(callbackFunName + "( "+results+" )");
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
