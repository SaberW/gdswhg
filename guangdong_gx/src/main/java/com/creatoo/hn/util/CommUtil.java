package com.creatoo.hn.util;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 公共的辅助方法
 * @author rbg
 *
 */
@SuppressWarnings("all")
public class CommUtil {

	/**
	 * 将WhTyp的List对象转化成前面EasyUI.tree组件需要的数据
	 * @param list 系统类型列表
	 * @return EasyUI.tree组件需要的数据
	 * @throws Exception
	 */
	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Object> parseTree(List<WhTyp> list)throws Exception{
		List<Object> res = new ArrayList<Object>();
		for (int i=0; i<list.size(); i++) {
			WhTyp whtyp = list.get(i);
			if (whtyp.getTyppid() == null || "".equals(whtyp.getTyppid().trim()) || "0".equals(whtyp.getTyppid().trim())){
				Map item = BeanUtils.describe(whtyp);
				CommUtil.compMenuTree(item, list);
				
				item.put("id", item.get("typid"));
				item.put("text", item.get("typname"));
				res.add(item);
				
				//list.remove(i);
				//--i;
			}
		}
		return res;
	}*/
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void compMenuTree(Map<String,Object> item, List<WhTyp> list){
		if (item.get("children")==null){
			item.put("children", new ArrayList<Object>());
		}
		for(int i=0; i<list.size(); i++){
			WhTyp m = list.get(i);
			if (item.get("typid").equals(m.getTyppid())){
				List children = (List) item.get("children");
				try {
					Map _item = BeanUtils.describe(m);
					CommUtil.compMenuTree(_item, list);
					_item.put("id", _item.get("typid"));
					_item.put("text", _item.get("typname"));
					children.add(_item);
					//list.remove(i);
					//--i;
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}*/

	/**
	 * 两个对象属性复制
	 * @param dest  目标对象
	 * @param orig 来源对象
	 * @throws Exception
	 */
	public static void copyProperties(Object dest, Object orig)throws IllegalAccessException, InvocationTargetException {
		BeanUtilsBean.getInstance().getConvertUtils().register(new org.apache.commons.beanutils.converters.DateConverter(null), java.util.Date.class);
		BeanUtils.copyProperties(dest, orig);
	}

	/**
	 * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> transBean2Map(Object obj) {

		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);

					map.put(key, value);
				}

			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}

		return map;

	}

	/**
	 * URL编码（utf-8）
	 *
	 * @param source
	 * @return
	 */
	public static String urlEncodeUTF8(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 判断是否为整形
	 * added by caiyong
	 * 2017/4/6
	 * */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 根据类反射对象获取请求参数，如果请求参数不存在该字段则以null为缺失
	 * @param request
	 * @param classObject
	 * @return
	 */
	public static Map<String,String> getRequestParamByClass(HttpServletRequest request, Class classObject){
		Map<String,String> map = new HashMap<String,String>();
		Field[] fields = classObject.getDeclaredFields();
		for(Field field : fields){
			String key = field.getName();
			String value = request.getParameter(key);
			if(null != value){
				map.put(key,value);
			}
		}
		return map;
	}

	/**
	 * 为订单生成6位长度的随机数
	 * @return
	 */
	public static String getRandom4Order(){
		String random = CommUtil.getRandom(6);
		while(random.length() != 6){
			random = CommUtil.getRandom(6);
		}
		return random;
	}

	/**
	 * 获取随机长度的字符串
	 * @param len
	 * @return
	 */
	public static String getRandom4Other(int len){
		String random = CommUtil.getRandom(len);
		while(random.length() != len){
			random = CommUtil.getRandom(len);
		}
		return random;
	}

	/**
	 * 根据长度生成随机数
	 * @param len 随机数长度
	 * @return
	 */
	public static String getRandom(int len){
		int result = 0;
		try{
			Integer[] array = {0,1,2,3,4,5,6,7,8,9};
			Random rand = new Random();
			for (int i = 10; i > 1; i--) {
				int index = rand.nextInt(i);
				int tmp = array[index];
				array[index] = array[i - 1];
				array[i - 1] = tmp;
			}
			for(int i = 0; i < len; i++){
				result = result * 10 + array[i];
			}
		}catch (Exception e){

		}
		return ""+result;
	}

	/**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
	 *
	 * @return ip
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		System.out.println("X-Forwarded-For ip: " + ip);
		if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			if( ip.indexOf(",")!=-1 ){
				ip = ip.split(",")[0];
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			System.out.println("Proxy-Client-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			System.out.println("WL-Proxy-Client-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
			System.out.println("HTTP_CLIENT_IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
			System.out.println("X-Real-IP ip: " + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			System.out.println("getRemoteAddr ip: " + ip);
		}
		System.out.println("获取客户端ip: " + ip);
		return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
}
