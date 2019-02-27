package com.creatoo.hn.services.api.apitrain;

import com.creatoo.hn.dao.mapper.WhgTraleaveMapper;
import com.creatoo.hn.dao.mapper.api.train.ApiTraLiveMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.dao.vo.TrainCourseVO;
import com.creatoo.hn.dao.vo.TrainLiveVO;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.mylive.MyLiveService;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.admin.train.WhgTrainCourseService;
import com.creatoo.hn.services.admin.train.WhgTrainEnrolService;
import com.creatoo.hn.services.admin.train.WhgTrainService;
import com.creatoo.hn.services.admin.user.WhgUserService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTagService;
import com.creatoo.hn.services.api.apioutside.collection.ApiCollectionService;
import com.creatoo.hn.util.WeekDayUtil;
import com.creatoo.hn.util.enums.EnumResType;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Api-在线课程服务
 * Created by wangxl on 2017/10/17.
 */
@Service
public class ApiTraLiveService extends BaseService {
    /**
     * 直播服务
     */
    @Autowired
    private MyLiveService myLiveService;

    /**
     * 文化馆服务
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgUserService whgUserService;

    @Autowired
    private ApiCollectionService apiCollectionService;

    @Autowired
    private WhgYunweiTagService whgYunweiTagService;

    @Autowired
    private WhgTrainEnrolService whgTrainEnrolService;

    @Autowired
    private WhgTrainCourseService whgTrainCourseService;

    @Autowired
    private WhgResourceService whgResourceService;

    @Autowired
    private WhgTrainService whgTrainService;

    @Autowired
    private ApiTraLiveMapper apiTraLiveMapper;

    @Autowired
    private WhgTraleaveMapper whgTraleaveMapper;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    /**
     * 获取推荐的在线培训
     * @param cultid
     * @return
     * @throws Exception
     */
    public List<Map> findRecommend(String cultid)throws Exception{
        Map<String, Object> paramMap = new HashMap<>();

        if(cultid!=null&&!cultid.equals("")){
            paramMap.put("cultid", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
        }

        paramMap.put("recommend", 1);
        PageHelper.startPage(1, 5);
        List<Map> lives = this.apiTraLiveMapper.queryAllLive(paramMap);
        PageInfo<Map> pageInfo = new PageInfo<>(lives);
        return pageInfo.getList();
    }

    /**
     * 查询培训(在线课程)列表
     * @param cultid 文化馆标识
     * @param deptid 部门标识
     * @param arttype 艺术分类
     * @param tratype 培训分类
     * @param livestate 培训状态:0-未开始(直播预告), 1-正在直播，2-已结束(精彩回顾)
     * @param sort 0-智能排序; 1-热门直播; 2-最新发布
     * @param keyword 搜索关键字
     * @param page 第几页
     * @param pageSize 每页数
     * @return PageInfo
     * @throws Exception
     */
    public PageInfo<Map> findAllLive(String cultid,String deptid, String arttype, String tratype,String province,String city,String area, Integer livestate, Integer sort, String keyword, Integer page, Integer pageSize)throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        //区分全省、全市站
        if (cultid == null || cultid.isEmpty()){
            paramMap.put("allprovince", true);
        }
        if (cultid != null && cultid.contains(",")){
            paramMap.put("allcity", true);
        }

        if(cultid!=null&&!cultid.equals("")){
            paramMap.put("cultid", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
        }
        if(deptid!=null&&!deptid.equals("")){
            paramMap.put("deptid", whgSystemDeptService.srchDeptStrList(deptid));
        }
        paramMap.put("arttype", arttype);
        paramMap.put("tratype", tratype);
        paramMap.put("province", province);
        paramMap.put("city", city);
        paramMap.put("area", area);
        paramMap.put("livestate", livestate);
        paramMap.put("sort", sort);
        paramMap.put("keyword", keyword);
        PageHelper.startPage(page, pageSize);
        List<Map> lives = this.apiTraLiveMapper.queryAllLive(paramMap);
        PageInfo<Map> pageInfo = new PageInfo<Map>(lives);
        return pageInfo;
    }

    /**
     * 查询在线培训课程详情
     * @param id 培训ID
     * @return Map
     * @throws Exception
     */
    public Map findLiveById(String id)throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("traid", id);
        List<Map> lives = this.apiTraLiveMapper.queryAllLive(paramMap);
        if(lives != null && lives.size() > 0){
            return lives.get(0);
        }
        return null;
    }

    /**
     * 查询课程直播详情
     * @param courseId
     * @param userId
     * @return TrainCourseVO
     * @throws Exception
     */
    public TrainCourseVO findLiveInfo(String courseId, String userId)throws Exception{
        WhgTraCourse course = this.whgTrainCourseService.t_srchOne(courseId);

        //培训信息
        WhgTra whgTra = this.whgTrainService.srchOne(course.getTraid());

        //培训人员
        List<WhgTraEnrol> users = new ArrayList<>();
        if(whgTra.getMustsignup() == null || whgTra.getMustsignup().intValue() == 1) {
            users = this.whgTrainEnrolService.findEnrolUser(course.getTraid());
        }

        //当前用户能否观看直播
        boolean canSee = false;
        if(whgTra.getMustsignup() == null || whgTra.getMustsignup().intValue() == 1) {
            if (StringUtils.isNotEmpty(userId)) {
                for (WhgTraEnrol enrolUser : users) {
                    if (userId.equals(enrolUser.getUserid())) {
                        canSee = true;
                        break;
                    }
                }
            }
        }else{
            canSee = true;
        }

        //当前课程表
        TrainCourseVO vo = this.whgTrainCourseService.change(course, canSee, whgTra.getTitle());
        vo.setCanSee(canSee);
        vo.setUsers(users);

        //培训图片
        vo.setTrainimg(whgTra.getTrainimg());

        //全部课程表
        List<WhgTraCourse> courses = this.whgTrainCourseService.srchList(course.getTraid());
        List<TrainCourseVO> courseVOS = this.whgTrainCourseService.change(courses, canSee, whgTra.getTitle());
        vo.setCourses(courseVOS);

        //查看视频直播地址
        if(canSee && vo.getListState().intValue() == 1) {
            //WhgTraLive live = this.whgTraLiveService.t_srchWhgTraLive(course.getTraid(), courseId);
            WhgLiveComm live = this.myLiveService.t_srchOneByTraidAndCourseid(course.getTraid(), courseId);
            String playAddr = live.getPlayaddr();
            String appName = live.getAppname();
            String streamName = live.getStreamname();
            playAddr = playAddr.replace("AppName", appName);
            playAddr = playAddr.replace("StreamName", streamName);
            playAddr = playAddr.replaceAll("m3u8", "flv");
            vo.setLiveUrl(playAddr);
        }

        //查看视频回看地址
        if(canSee && vo.getListState().intValue() == 2) {//已经结束,设置回看地址
            //WhgTraLive live = this.whgTraLiveService.t_srchWhgTraLive(course.getTraid(), courseId);
            WhgLiveComm live = this.myLiveService.t_srchOneByTraidAndCourseid(course.getTraid(), courseId);
            if(live != null) {
                String backUrl = live.getLiveaddr();
                String _backUrl = live.getEnturl();
                if (backUrl != null && !"".equals(backUrl)) {
                    vo.setBackUrl(backUrl);
                } else {
                    vo.setBackUrl(_backUrl);
                }
            }
        }

        //查询请假信息
        if(whgTra.getMustsignup() == null || whgTra.getMustsignup().intValue() == 1) {
            WhgTraleave leave = new WhgTraleave();
            leave.setTraid(whgTra.getId());
            leave.setUserid(userId);
            leave.setCourseid(courseId);
            leave = this.whgTraleaveMapper.selectOne(leave);
            if (leave != null && StringUtils.isNotEmpty(leave.getId())) {
                vo.setApplyLeave(true);
                vo.setApplyLeaveStime(leave.getStarttime());
                vo.setApplyLeaveEtime(leave.getEndtime());
                vo.setApplyLeaveState(leave.getState());
                vo.setApplyLeaveCause(leave.getCause());
                vo.setApplyLeaveSuggest(leave.getSuggest());
            } else {
                vo.setApplyLeave(false);
            }
        }else{
            vo.setApplyLeave(false);
        }
        return vo;
    }

    /**
     * 报名
     * @param enrol
     * @throws Exception
     */
    public void enrol(WhgTraEnrol enrol)throws Exception{

        //验证能否报名

        //报名

    }

    /**
     * 转换VO对象
     * @param traLive
     * @return
     * @throws Exception
     */
    public TrainLiveVO change(Map traLive, boolean detail, String userId)throws Exception{
        TrainLiveVO vo = new TrainLiveVO();
        if(traLive != null){
            Date now = new Date();
            String traid = (String) traLive.get("id");
            String title = (String) traLive.get("title");
            String picture = (String) traLive.get("trainimg");
            String notice = (String) traLive.get("notice");
            Date starttime = (Date) traLive.get("starttime");
            Date endtime = (Date) traLive.get("endtime");
            Date livestime = (Date) traLive.get("livestime");//下一场直播时间
            Long isover = ((Number) traLive.get("isover")).longValue();//是否结束
            Integer liveing = ((Number) traLive.get("liveing")).intValue();//直播中
            String host = (String) traLive.get("host");
            String cultid = (String) traLive.get("cultid");
            Integer state = (Integer) traLive.get("state");
            Integer delstate = (Integer) traLive.get("delstate");

            //主办单位
            if(StringUtils.isEmpty(host) && StringUtils.isNotEmpty(cultid)){
                WhgSysCult thiscult = this.whgSystemCultService.t_srchOne(cultid);
                host = thiscult.getName();
            }
            vo.setHost(host);

            //如果需要报名，报名时间和人数才有值
            Integer mustsignup = 1;//0-不需要报名, 1-需要报名
            try{
                mustsignup = ((Number) traLive.get("mustsignup")).intValue();//0-不需要报名, 1-需要报名
            }catch(Exception e){}
            Date enrolstarttime = (Date) traLive.get("enrollstarttime");
            Date enrolendtime = (Date) traLive.get("enrollendtime");
            Integer maxnumber = (Integer) traLive.get("maxnumber");
            Integer enrolnumber = ((Number) traLive.get("enrolnumber")).intValue();//当前报名人数
            Integer isrealname = ((Number)traLive.get("isrealname")).intValue();//是否需要实名
            Integer restPeoples = maxnumber != null ? maxnumber - enrolnumber : 0;//剩下报名人数

            //直播状态: 0-未开始(直播预告), 1-正在直播，2-已结束(精彩回顾)
            Integer listState = 0;
            if(isover > 0){
                listState = 2;
            }else if(liveing == 1){
                listState = 1;
            }else{
                listState = 0;
            }

            //限制年龄
            String age = (String) traLive.get("age");
            String limitAgeUnit = "不限";
            String limitAge = "";
            if(age != null && StringUtils.isNotEmpty(age)){
                String[] arr = age.split(",");
                if(arr.length == 1){
                    limitAge = arr[0];
                    limitAgeUnit = "岁或以上";
                }else if(arr.length == 2){
                    limitAge = arr[0]+"-"+arr[1];
                    limitAgeUnit = "岁";
                }
            }

            vo.setId(traid);
            vo.setTitle(title);
            vo.setTrainimg(picture);
            vo.setStarttime(starttime);
            vo.setNotice(notice);
            vo.setEndtime(endtime);
            vo.setListState(listState);
            vo.setEnrollstarttime(enrolstarttime);//报名开始时间
            vo.setEnrollendtime(enrolendtime);//报名结束时间
            vo.setMaxnumber(maxnumber);//总报名人数
            vo.setRestPeoples(restPeoples);//剩下报名人数
            vo.setCurtPeoples(enrolnumber);//当前报名人数
            vo.setLikedTimes( Integer.parseInt(String.valueOf(traLive.get("dianzan"))));//点赞次数
            vo.setFavoriteTimes(Integer.parseInt(String.valueOf(traLive.get("shoucang"))));//收藏次数
            if(listState == 0) {
                String[] restTime = WeekDayUtil.getRestInfo(livestime);//剩餘时间
                vo.setRestTimes(Integer.parseInt(restTime[0]));//剩下直播时间
                vo.setRestTimesUnit(restTime[1]);//剩下直播时间单位
            }
            vo.setAge(limitAge);//年龄
            vo.setAgeUnit(limitAgeUnit);//年龄单位
            vo.setMustsignup(mustsignup);//是否需要报名
            vo.setState(state);
            vo.setDelstate(delstate);

            //报名状态: 0-未开始，1-可报名, 2-已报满, 3-已结束, 4-不满足条件, 5-已报名
            if(1 == mustsignup.intValue()) {
                if (enrolstarttime.after(now)) {
                    vo.setEnrolState(0);
                } else if (enrolendtime.before(now)) {
                    vo.setEnrolState(3);
                } else if (enrolnumber >= maxnumber) {
                    vo.setEnrolState(2);
                } else {
                    vo.setEnrolState(1);
                }
            }

            //详情时查询更多属性
            if(detail){
                //标签
                String etag = (String) traLive.get("etag");
                if(StringUtils.isNotEmpty(etag)){
                    String[] arr = etag.split(",");
                    List<String> tags = new ArrayList<>();
                    for(String id : tags){
                        String tagName = this.whgYunweiTagService.findTagName(id);
                        tags.add(tagName);
                    }
                    vo.setTags(tags);
                }

                //培训老师
                String teachername = (String) traLive.get("teachername");
                vo.setTeachername(teachername);

                //联系电话
                String phone = (String) traLive.get("phone");
                vo.setPhone(phone);

                //地址
                String address = (String) traLive.get("address");
                vo.setAddress(address);

                //报名状态: 0-未开始，1-可报名, 2-已报满, 3-已结束, 4-不满足条件, 5-已报名
                if(1 == mustsignup) {
                    if (enrolstarttime.after(now)) {
                        vo.setEnrolState(0);
                    } else if (enrolendtime.before(now)) {
                        vo.setEnrolState(3);
                    } else if (enrolnumber >= maxnumber) {
                        vo.setEnrolState(2);
                    } else {
                        if (StringUtils.isNotEmpty(userId)) {//有userId时验证是否已报名或者是否满足条件
                            //是否已报名
                            boolean enrolled = this.whgTrainEnrolService.enrolled(vo.getId(), userId);
                            if (enrolled) {
                                vo.setEnrolState(5);
                            } else {
                                //是否满足条件
                                boolean isOK = false;
                                if (isrealname == 1) {//需要实名
                                    WhgUser whgUser = this.whgUserService.t_srchOne(userId);
                                    if (whgUser != null && whgUser.getIsrealname() != null && whgUser.getIsrealname().intValue() == 1) {
                                        isOK = true;
                                    }
                                } else {
                                    isOK = true;
                                }
                                if (!isOK) {
                                    vo.setEnrolState(4);//可报名，但不满足条件
                                } else {
                                    vo.setEnrolState(1);//可报名
                                }
                            }
                        } else {
                            vo.setEnrolState(1);//可报名
                        }
                    }
                }

                //培训人员
                List<WhgTraEnrol> users = this.whgTrainEnrolService.findEnrolUser(vo.getId());
                vo.setUsers(users);

                //课程简介
                vo.setCoursedesc((String)traLive.get("coursedesc"));

                //课程内容
                vo.setOutline((String)traLive.get("outline"));

                //老师介绍
                vo.setTeacherdesc((String)traLive.get("teacherdesc"));

                //当前用户能否观看直播
                boolean canSee = true;
                if(StringUtils.isNotEmpty(userId) && mustsignup == 1){
                    canSee = false;
                    for(WhgTraEnrol enrolUser : users){
                        if(userId.equals(enrolUser.getUserid())){
                            canSee = true;
                            break;
                        }
                    }
                }
                vo.setCanSee(canSee);

                //查看课程表
                List<WhgTraCourse> courses = this.whgTrainCourseService.srchList(vo.getId());
                vo.setCourses(this.whgTrainCourseService.change(courses, canSee, vo.getTitle()));

                //下载资料
                List<WhgResource> docs = this.whgResourceService.findResource(EnumResType.TYPE_FILE.getValue(), EnumTypeClazz.TYPE_LIVE.getValue(), vo.getId());
                vo.setDocs(docs);
            }
        }
        return vo;
    }

    /**
     * 转换成VO对象
     * @param traLives 在线课程列表
     * @return List<TrainLiveVO>
     * @throws Exception
     */
    public List<TrainLiveVO> change(List<Map> traLives, boolean detail, String userId)throws Exception{
        List<TrainLiveVO> vos = new ArrayList<>();
        if(traLives != null){
            for(Map map : traLives){
                vos.add(change(map, detail, userId));
            }
        }
        return vos;
    }

    /**
     * 保存直播的回调地址
     * @param appName 应用名称
     * @param streamName 流名称
     * @param uri 录制的地址
     * @throws Exception
     */
    public void saveBackUrl(String starttime, String appName, String streamName, String uri)throws Exception{
        //this.whgTraLiveService.t_edit()
        //#{starttime} and t2.appname=#{appName} and t2.streamname=#{streamName}
        Map<String, String> paramMap = new HashMap();
        paramMap.put("starttime", starttime);//2017-10-10 09 到小时的时间
        paramMap.put("appName", appName);
        paramMap.put("streamName", streamName);
        List<Map> list = this.apiTraLiveMapper.queryLiveByTime(paramMap);
        if(list != null && list.size() > 0){
            Map map = list.get(0);
            String liveId = (String) map.get("id");//whg_tra_live 主键标识
            String liveaddr = (String)map.get("liveaddr");//回看地址
            if(StringUtils.isEmpty(liveaddr)){
                //this.whgTraLiveService.t_editLiveBackUrl(liveId, uri);//修改回看地址
                this.myLiveService.t_updateEntUrl(liveId, uri);
            }
        }
    }
}

