package hn.creatoo.com.web.actions.web;

import com.google.gson.JsonObject;
import hn.creatoo.com.web.util.BaseController;
import hn.creatoo.com.web.util.ConfigUtils;
import hn.creatoo.com.web.util.JsonUtils;
import hn.creatoo.com.web.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zengrong on 2017/7/3.
 */
@RestController
public class ApiAction extends BaseController{
    @Autowired
    private RestTemplate restTemplate;
    @RequestMapping(value = "/sys_api")
    public void doApi(HttpServletRequest request, HttpServletResponse response, RedirectAttributes model) {
        Map map = new HashMap();
        try {
            String rootUrl = ConfigUtils.getApiConfig().getRoot();
            Map<String, String> params = new HashMap();
            Map<String, String[]> reqMap = request.getParameterMap();
            String api  = null;
            for(Map.Entry<String, String[]> entry : reqMap.entrySet()){
                String[] values = entry.getValue();
                String key = entry.getKey();
                if (values != null && values.length > 0){
                    if(key != null && key.equals("api")){
                        api = values[0];
                        continue;
                    }
                    params.put(key, values[0]);
                }
            }
            //调用接口
            api = api.replace(rootUrl,"");
            String a = RestUtils.post(restTemplate, api, params);
            sendSucBackEx(response,a);
        }catch (Exception e) {
            sendFaiBack(response,1,"请求失败");
        }
    }
}