package com.creatoo.hn.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created by LENUVN on 2017/9/1.
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String[]filterChars;
    private String[]replaceChars;

    public XssHttpServletRequestWrapper(HttpServletRequest request,String filterChar,String replaceChar,String splitChar) {
        super(request);
        if(filterChar!=null&&filterChar.length()>0){
            filterChars=filterChar.split(splitChar);
        }
        if(replaceChar!=null&&replaceChar.length()>0){
            replaceChars=replaceChar.split(splitChar);
        }
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }
//    public String getHeader(String name) {
//        String value = super.getHeader(name);
//        if (value == null)
//            return null;
//        return cleanXSS(value);
//    }

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values==null)  {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    private String cleanXSS(String value) {
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
        value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        for (int i = 0; i < filterChars.length; i++) {
            if(value.contains(filterChars[i].trim())){
                value=value.replace(filterChars[i].trim(), replaceChars[i]);
            }
        }
        return value;
    }

}
