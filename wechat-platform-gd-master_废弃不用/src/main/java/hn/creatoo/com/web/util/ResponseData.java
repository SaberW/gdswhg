package hn.creatoo.com.web.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**the data of HttpServletResponse
 * @author caiyong
 * @time 2017/3/10
 */
public class ResponseData implements Serializable{

	private static Logger log = Logger.getLogger(ResponseData.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ResponseData(){
		
	}
	
	public static ResponseData getInstance(int code){
		ResponseData responseData = new ResponseData();
		responseData.setRsCode(code);
		return responseData;
	}
	
	private Integer rsCode;
	
	private String rsMsg;
	
	private Object data;

	public Integer getRsCode() {
		return rsCode;
	}

	public void setRsCode(Integer rsCode) {
		this.rsCode = rsCode;
	}

	public String getRsMsg() {
		return rsMsg;
	}

	public void setRsMsg(String rsMsg) {
		this.rsMsg = rsMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		JSONObject object = new JSONObject();		
		object.put("RSCODE", this.rsCode);		
		object.put("RSMSG", this.rsMsg);	
		object.put("DATA", this.data);	
		return object.toJSONString();
	}
	
	public void sendBack(HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");	
		PrintWriter out = null;
		try {
			out = response.getWriter();			
			out.print(this.toString());			
			out.flush();                   
		}catch (IOException e) {
			log.error(e.toString());
		}finally {
			if(null != out){
				out.close();
			}
		} 		
	}

	public void sendBackData(HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(this.data.toString());
			out.flush();
		}catch (IOException e) {
			log.error(e.toString());
		}finally {
			if(null != out){
				out.close();
			}
		}
	}
}
