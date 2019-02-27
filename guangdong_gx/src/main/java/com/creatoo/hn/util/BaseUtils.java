package com.creatoo.hn.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @auther ly
 * Date: 15-4-23
 * Time: 下午2:03
 */
public class BaseUtils {
	/**
	 * 获取request 中的参数，以map形式返回
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getParamMap(ServletRequest request) {
		//Assert.notNull(request,"参数不能为空");
		Map<String, Object> map = Maps.newHashMap();
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String name = en.nextElement();
			String[] values = request.getParameterValues(name);
			if (values == null || values.length == 0) {
				continue;
			}
			String value = values[0];
			if (value != null) {
				map.put(name, value);
			}
		}
		return map;
	}

	/**
	 * 把数组所有元素，按字母排序，然后按照“参数=参数值”的模式用“&”字符拼接成字符串
	 *
	 * @param params
	 *            需要签名的参数
	 * @return 签名的字符串
	 */
	public static String createLinkString(Map<String, Object> params) {
		List<String> keys = Lists.newArrayList(params.keySet().iterator());
		Collections.sort(keys);
		StringBuilder signStr = new StringBuilder();
		for (String key : keys) {
			if (StringUtils.isBlank(params.get(key).toString())) {
				continue;
			}
			signStr.append(key).append("=").append(params.get(key)).append("&");
		}
		return signStr.deleteCharAt(signStr.length() - 1).toString();
	}
}
