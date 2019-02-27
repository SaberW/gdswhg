package com.creatoo.hn.controller.api.apioutside;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.services.api.apioutside.ApiGlobalSearchService;
import com.creatoo.hn.util.bean.ApiResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("ALL")
@Controller
@CrossOrigin
@RequestMapping("/api/global")
public class ApiGlobalSearchController extends BaseController {

    @Autowired
    private ApiGlobalSearchService apiGlobalSearchService;

    /**
     * 全局搜索
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object globalSearch(String srchkey,
                               @RequestParam(value = "page", required = false, defaultValue = "1")int page,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10")int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = (ApiResultBean) this.apiGlobalSearchService.globalSearch(page, pageSize, srchkey);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }
}
