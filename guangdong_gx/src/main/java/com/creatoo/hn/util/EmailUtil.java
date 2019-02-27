package com.creatoo.hn.util;

import com.creatoo.hn.Constant;
import org.apache.commons.mail.SimpleEmail;

/**
 * 邮箱注册辅助方法
 * @author dzl
 *
 */
public class EmailUtil {

	/**邮箱通知
	 * @param userEmail
	 * @param moban
	 * @return
	 */
	public static boolean sendNoticeEmail(String userEmail,String content) {
		boolean sendOK=true;
		SimpleEmail email = new SimpleEmail();  
	    try {
			String email_smtpserver = Constant.EMAIL_SMTPSERVER;//SMTP服务器
			String email_smtpserver_port =Constant.EMAIL_SMTPSERVER_PORT;
			String email_username = Constant.EMAIL_USERNAME;
			String email_userpwd = Constant.EMAIL_USERPWD;
			String email_sender = Constant.EMAIL_SENDER;//邮箱发送人用户名
			String email_sendername = Constant.EMAIL_SENDERNAME;//邮箱发送人姓名
			String email_reg_subject = Constant.EMAIL_REG_SUBJECT;//邮件标题
	    	String email_content_tpl = content;
	    	// 邮箱服务器身份验证  
	    	email.setHostName(email_smtpserver);// 设置使用发电子邮件的邮件服务器
	    	email.setAuthentication(email_username, email_userpwd);  
	    	email.setSSLOnConnect(Boolean.TRUE);
	    	email.setSslSmtpPort(email_smtpserver_port); // 设定SSL端口
	        // 收件人邮箱  
	        email.addTo(userEmail,userEmail);  
	        // 发件人邮箱  
	        email.setFrom(email_sender, email_sendername);  
	        // 邮件主题  
	        email.setSubject(email_reg_subject);  
	        // 邮件内容  
	        String emailContent = email_content_tpl;
	        email.setMsg(emailContent);
	        // 发送邮件  
	        email.send();  
	    } catch (Exception ex) {  
	        ex.printStackTrace();  
	        sendOK = false;
	    } 
	    return sendOK;
	}
	
}
