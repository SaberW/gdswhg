package hn.creatoo.com.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class BaseController {
	
	protected JSONObject getMyParam(HttpServletRequest request){
		String inputData = request.getParameter("inputData");
		if(null == inputData){
			return null;
		}
		return JSON.parseObject(inputData);		
	} 
	
	protected void sendSucBack(HttpServletResponse response,Object data){
		ResponseData responseData = ResponseData.getInstance(0);
		if(null != data){
			responseData.setData(data);
		}
		responseData.sendBack(response);
	}

	protected void sendSucBackEx(HttpServletResponse response,String msg){
		ResponseData responseData = ResponseData.getInstance(0);
		if(null != msg){
			responseData.setData(msg);
		}
		responseData.sendBackData(response);
	}

	protected void sendFaiBack(HttpServletResponse response,Integer code,String msg){
		ResponseData responseData = ResponseData.getInstance(1);
		if(null != code){
			responseData.setRsCode(code);
		}
		if(null != msg){
			responseData.setRsMsg(msg);
		}
		responseData.sendBack(response);
	}
}
