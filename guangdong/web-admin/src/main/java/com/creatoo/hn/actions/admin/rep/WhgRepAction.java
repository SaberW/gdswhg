package com.creatoo.hn.actions.admin.rep;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbg on 2017/6/23.
 */

@Controller
@RequestMapping("/admin/rep")
public class WhgRepAction {
    Logger log = Logger.getLogger(this.getClass());

    @RequestMapping("/view/{type}")
    public String view(@PathVariable("type")String type, ModelMap mmp, WebRequest request){
        return "admin/rep/view_"+type;
    }
}
