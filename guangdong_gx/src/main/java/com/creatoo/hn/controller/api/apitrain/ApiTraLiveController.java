package com.creatoo.hn.controller.api.apitrain;

import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgInfUpload;
import com.creatoo.hn.dao.vo.TrainCourseVO;
import com.creatoo.hn.dao.vo.TrainLiveVO;
import com.creatoo.hn.services.admin.information.WhgInfUploadService;
import com.creatoo.hn.services.api.apitrain.ApiTraLiveService;
import com.creatoo.hn.services.api.apitrain.ApiTraService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 在线课程接口
 * Created by wangxl on 2017/10/16.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/zxkc")
@Api(tags="网络培训-在线课程")
public class ApiTraLiveController extends BaseController {
    /**
     * 在线课程培训查询服务
     */
    @Autowired
    private ApiTraLiveService apiTraLiveService;

    @Autowired
    private ApiTraService apiTraService;

    @Autowired
    private WhgInfUploadService whgInfUploadService;
    /**
     * 在线课程-培训列表
     * @return ApiResultBean
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value="查询在线培训列表", notes = "查询在线培训列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cultid", value = "文化馆标识", paramType = "query"),
            @ApiImplicitParam(name = "arttype", value = "艺术分类标识", paramType = "query"),
            @ApiImplicitParam(name = "etype", value = "培训分类标识", paramType = "query"),
            @ApiImplicitParam(name = "livestate", value = "培训状态:0-未开始(直播预告), 1-正在直播，2-已结束(精彩回顾)", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序：0-智能排序; 1-热门直播; 2-最新发布", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "搜索关键字", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "第几页", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数", paramType = "query")
    })
    public ApiResultBean list(
            @RequestParam(value = "cultid", required = false)String cultid,
            @RequestParam(value = "deptid", required = false)String deptid,
            @RequestParam(value = "arttype", required = false)String arttype,
            @RequestParam(value = "etype", required = false)String etype,
            @RequestParam(value = "province", required = false)String province,
            @RequestParam(value = "city", required = false)String city,
            @RequestParam(value = "area", required = false)String area,
            @RequestParam(value = "livestate", required = false)Integer livestate,
            @RequestParam(value = "sort", defaultValue="0" )Integer sort,
            @RequestParam(value = "title", required = false)String title,
            @RequestParam(value = "page", defaultValue = "1")int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize)throws Exception{
        ApiResultBean rb = new ApiResultBean();
        try {
            if(StringUtils.isEmpty(cultid)){
                //cultid = Constant.ROOT_SYS_CULT_ID;
            }
            PageInfo pageInfo = this.apiTraLiveService.findAllLive(cultid,deptid, arttype, etype,province,city,area, livestate, sort, title, page, pageSize);
            rb.setPageInfo(pageInfo);
            rb.setRows(this.apiTraLiveService.change(rb.getRows(), false, null));
        }catch (Exception e){
            throw e;
        }
        return rb;
    }

    /**
     * 培训详情
     * @return ApiResultBean
     */
    @RequestMapping(value = "/detail", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value="查询在线培训详情", notes = "查询在线培训详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "培训标识", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "userId", value = "用户标识", dataType = "string", paramType = "query")
    })
    public ApiResultBean detail(@RequestParam("id") String id, @RequestParam(value = "userId", required = false) String userId)throws Exception{
        ApiResultBean rb = new ApiResultBean();
        try {
            Map traInfo = this.apiTraLiveService.findLiveById(id);
            TrainLiveVO vo = this.apiTraLiveService.change(traInfo, true, userId);
            List<WhgInfUpload> list = whgInfUploadService.selecup(id);//资料列表
            rb.setRows(list);
            //培训组限制报名
            String groupid = (String) ((Map)traInfo).get("groupid");
            vo.setLimitEnrolForTrainGroup(apiTraService.limitEnrolForTrainGroup(groupid, userId));
            rb.setData(FilterFontUtil.clearFont(vo));
        }catch (Exception e){
            throw e;
        }
        return rb;
    }

    /**
     * 推荐的在线课程
     * @return ApiResultBean
     */
    @RequestMapping(value = "/recommends", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value="查询推荐的在线课程", notes = "查询推荐的在线课程")
    @ApiImplicitParam(name = "cultid", value = "文化馆标识", paramType = "query")
    public ApiResultBean recommendList(@RequestParam(value = "cultid", required = false)String cultid)throws Exception{
        ApiResultBean rb = new ApiResultBean();
        try {
            if(StringUtils.isEmpty(cultid)){
                //cultid = Constant.ROOT_SYS_CULT_ID;
            }
            List<Map> lives = this.apiTraLiveService.findRecommend(cultid);
            rb.setRows(lives);
        }catch (Exception e){
            throw e;
        }
        return rb;
    }

    /**
     * 直播详情
     * @return ApiResultBean
     */
    @RequestMapping(value = "/live", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value="查询在线课程直播详情", notes = "查询在线课程直播详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "培训课程标识", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户标识", required = true, dataType = "string", paramType = "query")
    })
    public ApiResultBean liveInfo(@RequestParam("id") String id, @RequestParam("userId")String userId)throws Exception{
        ApiResultBean rb = new ApiResultBean();
        try {
            TrainCourseVO vo = this.apiTraLiveService.findLiveInfo(id, userId);
            rb.setData(vo);
        }catch (Exception e){
            throw e;
        }
        return rb;
    }


    /**
     * 视频直播录制回调。测试地址：http://39.108.237.196:8001/api/zxkc/notify/record
     *
     * @return success-成功 fail-失败
     */
    @RequestMapping("/notify/record")
    public String notifyRecord(@RequestBody JSONObject json){
        String success = "success";
        try{
            String domain = ((Object) json.get("domain")).toString();//直播域名
            String appName = ((Object) json.get("app")).toString();//应用名称-appName
            String streamName = ((Object) json.get("stream")).toString();//流名称-streamName
            String uri = ((Object) json.get("uri")).toString();//录制地址-oss路径
            //String duration_s = ((Object) json.get("uri")).toString();
            String start_time_s = ((Object) json.get("start_time")).toString();
            String stop_time_s = ((Object) json.get("stop_time")).toString();
            Date start_time = new Date(Long.parseLong(start_time_s+"000"));
            Date stop_time = new Date(Long.parseLong(stop_time_s+"000"));
            //Double duration = Double.parseDouble(duration_s);

//            System.out.println("domain:"+domain);
//            System.out.println("appName:"+appName);
//            System.out.println("streamName:"+streamName);
//            System.out.println("uri:"+uri);
//            System.out.println("duration:"+duration);
//            System.out.println("start_time:"+start_time);
//            System.out.println("stop_time:"+stop_time);

            //回调处理
            if(StringUtils.isNotEmpty(appName) && StringUtils.isNotEmpty(streamName) && StringUtils.isNotEmpty(uri)){
                String url = "http://szwhg-gds-video.oss-cn-shanghai.aliyuncs.com/"+uri;//录制的地址
                //http://szwhg-dgoss-live.oss-cn-shanghai.aliyuncs.com/record/record/2017-10-19/dgswhg/live1/2017-10-19-14:08:42_2017-10-19-14:17:26.m3u8
                //this.videoService.notifyLive(domain, appName, streamName, uri, start_time, stop_time, duration);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH");
                String startTime = sdf.format(start_time);//转换成到小时的日期字符串，好根据时间，appName, streamName找到对应的直播信息，修改回看地址
                this.apiTraLiveService.saveBackUrl(startTime, appName, streamName, url);
            }
        }catch(Exception e){
            success = "false";
        }
        return success;
    }
}
