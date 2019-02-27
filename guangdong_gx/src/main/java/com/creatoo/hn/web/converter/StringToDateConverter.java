package com.creatoo.hn.web.converter;


import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串转日期时的转换器
 * Created by wangxl on 2018/1/5.
 */
public class StringToDateConverter implements Converter<String,Date> {
    private static final String dateFormat      = "yyyy-MM-dd HH:mm:ss";
    private static final String shortDateFormat = "yyyy-MM-dd";
    private static final String shortDateFormat2 = "HH:mm";
    private static final String shortDateFormat3 = "HH:mm:ss";

    @Override
    public Date convert(String source) {
        try {
            if (StringUtils.isBlank(source)) {
                return null;
            }
            source = source.trim();
            SimpleDateFormat formatter;
            if (source.contains("-")) {
                if (source.contains(":")) {
                    formatter = new SimpleDateFormat(dateFormat);
                } else {
                    formatter = new SimpleDateFormat(shortDateFormat);
                }
                Date dtDate = formatter.parse(source);
                return dtDate;
            } else if (source.contains(":")) {
                if(source.split(":").length == 2){
                    formatter = new SimpleDateFormat(shortDateFormat2);
                }else {
                    formatter = new SimpleDateFormat(shortDateFormat3);
                }
                return formatter.parse(source);
            } else if (source.matches("^\\d+$")) {
                Long lDate = new Long(source);
                return new Date(lDate);
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).error(e.getMessage(), e);
        }
        throw null;
    }
}
