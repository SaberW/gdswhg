package hn.creatoo.com.web.actions.web.jsonp;

import hn.creatoo.com.web.util.CookiesUtil;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JSONP方法
 */
@RestController
@RequestMapping("/jsonp")
public class JsonpAction {
    /**
     * 获得保存SESSION的Cookie值
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getCK", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getCK(HttpServletRequest request, HttpServletResponse response, String callback){
        String ckVal = "";
        //只能限制一期的url
        Cookie cookie = CookiesUtil.getCookieByName(request, "SESSION");
        if(cookie != null){
            ckVal = cookie.getValue();
        }

        //如果字符串不为空，需要支持jsonp调用 spring4.1 以上可用
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(ckVal);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;

        //return new JSONPObject("callback", ckVal);
    }
}
