package com.creatoo.hn.controller.api.basic;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgTra;
import com.creatoo.hn.util.bean.ResponseBean;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试培训API
 * Created by wangxl on 2017/10/11.
 */
@RestController
@RequestMapping("/v2/api/trains")
@Api(tags="Test-培训接口")
public class ApiResourceTrainController extends BaseController {
    /**
     * <pre>
     *      @ApiImplicitParam.paramType 可用值
     *           header-->请求参数的获取：@RequestHeader
     *           query-->请求参数的获取：@RequestParam
     *           path（用于restful接口）-->请求参数的获取：@PathVariable
     *           body（不常用）
     *           form（不常用）
     * </pre>
     * @param id
     * @return
     */
    @ApiOperation(value="获取培训详情", notes = "获取培训详情")
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    @ApiImplicitParam(name = "id", value = "标识", required = true, paramType = "path")
    public ResponseBean get(@PathVariable String id){
        ResponseBean rb = new ResponseBean();
        try {
            if (StringUtils.isNotEmpty(id) && "123".equals(id)) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", "123");
                map.put("name", "王新林");
                map.put("birthday", "2017-10-10");
                rb.setData(map);
            }else{
                throw new Exception("no exist for id:"+id);
            }
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg(e.getMessage());
        }
        return rb;
    }


    @ApiOperation("修改状态")
    @RequestMapping(value = "/{id}/state", method = RequestMethod.PUT)
    public ResponseBean state(@NotNull @PathVariable String id){
        return null;
    }

    @ApiOperation("编辑培训")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "培训标识", required = true, dataType = "String"),
            @ApiImplicitParam(name = "whgTra", value = "培训对象", required = true, dataType = "WhgTra")
    })
    public ResponseBean edit(@PathVariable String id, @RequestBody WhgTra whgTra){
        return null;
    }

    @ApiOperation("删除培训")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseBean delete(@PathVariable String id){
        return null;
    }
}
