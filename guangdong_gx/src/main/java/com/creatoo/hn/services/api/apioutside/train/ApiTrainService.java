package com.creatoo.hn.services.api.apioutside.train;


import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.mapper.api.ApiResourceMapper;
import com.creatoo.hn.dao.mapper.api.ApiTraMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.api.apiinside.InsMessageService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumBMState;
import com.creatoo.hn.util.enums.EnumProject;
import com.creatoo.hn.util.enums.EnumResType;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 培训报名服务类
 * Created by tangwei on 2017/4/6.
 */
@SuppressWarnings("ALL")
@Service
public class ApiTrainService extends BaseService{
    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private WhgTraMapper whgTraMapper;

    @Autowired
    private WhgTraCourseMapper whgTraCourseMapper;

    @Autowired
    private WhgTraEnrolMapper whgTraEnrolMapper;

    @Autowired
    private WhgUserMapper whgUserMapper;

    @Autowired
    private ApiTraMapper apiTraMapper;

    @Autowired
    private SMSService smsService;

    @Autowired
    private ApiResourceMapper apiResourceMapper;

    @Autowired
    private WhgGatherMapper whgGatherMapper;

    @Autowired
    private InsMessageService insMessageService;



    public PageInfo getTraList(int page, int pageSize, String cultid, String arttype, String type, String sort, String title) throws Exception {
//        Example example = new Example(WhgTra.class);
//        Example.Criteria c = example.createCriteria();
//        if(cultid != null && !"".equals(cultid)){
//            c.andEqualTo("cultid",cultid);
//        }
//        if(arttype != null && !"".equals(arttype)){
//            c.andEqualTo("arttype",arttype);
//        }
//        if(type != null && !"".equals(type)){
//            c.andEqualTo("etype",type);
//        }
//        if(sort != null && !"".equals(sort)){
//            if(sort.equals("1")){
//                c.andGreaterThanOrEqualTo("starttime",new Date());
//            }
//            if(sort.equals("2")){
//                c.andLessThan("starttime",new Date());
//                c.andGreaterThan("endtime",new Date());
//            }
//        }
//        if(title != null && !"".equals(title)){
//            c.andLike("title",title);
//        }
//        c.andEqualTo("state",6);
//        c.andEqualTo("delstate",0);
//        example.setOrderByClause("statemdfdate desc");
        List list=null;
        if(cultid!=null&&StringUtil.isNotEmpty(cultid)){
            list=Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));//多个cultid
        }
        PageHelper.startPage(page,pageSize);
        Date now = new Date();
        List traList = this.apiTraMapper.selTraList(list,arttype,type,sort,title,now);
        return new PageInfo(traList);
    }

    /**
     * 根据培训id查询培训信息
     * @param id
     * @return
     */

    public Object getOneTra(String id,String userid) throws Exception{
        Map whgTra = this.apiTraMapper.selTraById(id,userid);
        whgTra = FilterFontUtil.clearFont4Tra(whgTra);
        whgTra.put("currentApplyNum",this.countTraEnrol(id));
        //资源-图片，视频，音频，文档
        List imageList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_TRAIN.getValue(), id, EnumResType.TYPE_IMAGE.getValue());
        List videoList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_TRAIN.getValue(), id, EnumResType.TYPE_VIDEO.getValue());
        List audioList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_TRAIN.getValue(), id, EnumResType.TYPE_AUDIO.getValue());
        List fileList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_TRAIN.getValue(), id, EnumResType.TYPE_FILE.getValue());
        whgTra.put("whgTraCourseList",this.getCourseByTraId(id));
        whgTra.put("imageList",imageList);
        whgTra.put("videoList",videoList);
        whgTra.put("audioList",audioList);
        whgTra.put("fileList",fileList);
        return whgTra;
    }

    /**
     * 根据培训id查询课程
     * @param id
     * @return
     */
    public List<WhgTraCourse> getCourseByTraId(String id) throws Exception{
        Example example = new Example(WhgTraCourse.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid",id).andEqualTo("state",1).andEqualTo("delstate",0);
        List<WhgTraCourse> courseList = this.whgTraCourseMapper.selectByExample(example);
        return courseList;
    }

    /**
     * 根据培训id查询报名人数
     * @param id
     * @return
     */
    public Integer countTraEnrol(String id) throws Exception {
        Example example = new Example(WhgTraEnrol.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("traid",id);
        criteria.andIn("state", Arrays.asList(1,4,6));
        List<WhgTraEnrol> whgTraEnrolList = whgTraEnrolMapper.selectByExample(example);
        if(null == whgTraEnrolList){
            return null;
        }else {
            return whgTraEnrolList.size();
        }
    }

    /**
     * 检查是否可以报名
     * @param userid
     * @param traid
     * @return
     * @throws Exception
     */
    public ApiResultBean canSign(String userid, String traid) throws Exception {
        ApiResultBean arb = new ApiResultBean();
        WhgTra whgTra = this.whgTraMapper.selectByPrimaryKey(traid);
        Integer enrolCount = countTraEnrol(traid);
        if(null != enrolCount){
            if(enrolCount >= whgTra.getMaxnumber()){
                arb.setCode(103);
                arb.setMsg("超过报名人数");
                return arb;
            }
        }
        if(null != userid && !"".equals(userid)) {
            WhgUser user = whgUserMapper.selectByPrimaryKey(userid);
            if (1 == whgTra.getIsrealname()) {
                if (null == user.getIsrealname() || 1 != user.getIsrealname()) {
                    arb.setCode(104);
                    arb.setMsg("未实名认证");
                    return arb;
                }
            }
        }
        LocalDateTime enrolStart = date2LocalDateTime(whgTra.getEnrollstarttime());
        LocalDateTime enrolEnd = date2LocalDateTime(whgTra.getEnrollendtime());
        LocalDateTime now = date2LocalDateTime(new Date());
        if(now.isBefore(enrolStart)){
            arb.setCode(105);
            arb.setMsg("还未到报名时间");
            return arb;
        }
        if(now.isAfter(enrolEnd)){
            arb.setCode(106);
            arb.setMsg("报名时间已过");
            return arb;
        }
        return arb;
    }

    private LocalDateTime date2LocalDateTime(Date date){
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime;
    }

    /**
     * 获取推荐的培训
     * @param id
     * @param cultid
     * @param size
     * @return
     */
    public PageInfo getRecommendTra(String id,String cultid,Integer size) throws Exception{
        Example example = new Example(WhgTra.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("state",6);
        criteria.andEqualTo("recommend",1);
        criteria.andNotEqualTo("id",id);
        if(cultid != null && !"".equals(cultid)){
            criteria.andEqualTo("cultid",cultid);
        }
        example.setOrderByClause("statemdfdate desc");
        if(size != null){
            PageHelper.startPage(1,size);
        }else {
            PageHelper.startPage(1,3);
        }
        List<WhgTra> whgTraList = whgTraMapper.selectByExample(example);
        return new PageInfo(whgTraList);

    }

    /**
     * 检查培训报名
     * @param trainId
     * @param userid
     * @return
     */
    public ApiResultBean checkApplyTrain(String trainId, String userid,String gatherid) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if(gatherid != null && !"".equals(gatherid)){
            WhgGather gather = this.whgGatherMapper.selectByPrimaryKey(gatherid);
            int _count = this.countTraEnrol(trainId);
            if(_count >= gather.getNumsum()){
                arb.setCode(301);
                arb.setMsg("份数已满");
                return arb;
            }
        }
        WhgTra train = whgTraMapper.selectByPrimaryKey(trainId);
        //培训不存在,或者状态不是已发布
        if(train == null || train.getState().intValue() != 6){
            arb.setCode(102);
            arb.setMsg("培训不存在,或者状态不是已发布");
            return arb;
        }
        //培训报名已结束或未开始
        if(train.getEnrollendtime().before(Calendar.getInstance().getTime()) ){
            arb.setCode(103);
            arb.setMsg("已结束");
            return arb;
        }
        if(train.getEnrollstarttime().after(Calendar.getInstance().getTime())){
            arb.setCode(109);
            arb.setMsg("未开始");
            return arb;
        }
        //培训报名额已满
        if(!checkTrainMaxNumber(train)){
            arb.setCode(104);
            arb.setMsg("名额已满");
            return arb;
        }
        //培训报名重复
        if(userid != null && !"".equals(userid)){
            if(train.getBiz() != null && "ZC".equals(train.getBiz())){
                if(!checkExistEnrol(train.getId(),userid)){
                    arb.setCode(105);
                    arb.setMsg("您已经参与过了，不能重复申请参与");
                    return arb;
                }
            }else {
                if(!checkExistEnrol(train.getId(),userid)){
                    arb.setCode(105);
                    arb.setMsg("培训报名重复");
                    return arb;
                }
            }

        }
        //用户手机号不存在
        if(userid != null && !"".equals(userid)){
            WhgUser user = whgUserMapper.selectByPrimaryKey(userid);
            if(user.getPhone() == null || user.getPhone().isEmpty()){
                arb.setCode(106);
                arb.setMsg("用户手机号不存在");
                return arb;
            }
        }

        //培训可是是否重叠
        if(userid != null && !"".equals(userid)){
            //int count = 0;
            Example example = new Example(WhgTraCourse.class);
            example.createCriteria().andEqualTo("traid",trainId);
            List<WhgTraCourse> courseList = this.whgTraCourseMapper.selectByExample(example);
            if(courseList != null && courseList.size() > 0){
                for (WhgTraCourse course : courseList) {
                    Date starttime = course.getStarttime();
                    Date endtime = course.getEndtime();
                    int count = this.apiTraMapper.selCount(starttime,endtime,userid,new Date());
                    if(count > 0){
                        arb.setCode(107);
                        arb.setMsg("培训课程时间冲突");
                        return arb;
                    }
                }

            }
        }

        //实名制验证
        if(train.getIsrealname().intValue() ==1 && userid != null && !"".equals(userid)){
            WhgUser user = whgUserMapper.selectByPrimaryKey(userid);
            if(user.getIsrealname() == null || user.getIsrealname().intValue() != 1){
                arb.setCode(200);
                arb.setMsg("您还未完成实名认证，请先完成实名认证！");
                return arb;
            }
        }

        return arb;
    }

    /**
     * 检查是否超出报名名额
     * @param train
     */
    private Boolean checkTrainMaxNumber (WhgTra train){
        Example example  = new Example(WhgTraEnrol.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid", train.getId());
        c.andIn("state", Arrays.asList(1,4,6));
        int count = whgTraEnrolMapper.selectCountByExample(example);
        if(count >= train.getMaxnumber().intValue()){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 检查报名是否重复
     * @param trainId
     * @param userId
     * @return
     */
    private Boolean checkExistEnrol (String trainId,String userId){
        Example example  = new Example(WhgTraEnrol.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid",trainId);
        c.andEqualTo("userid",userId);
        c.andIn("state", Arrays.asList(1,4,5,6));
        int count =  whgTraEnrolMapper.selectCountByExample(example);
        return count == 0 ? Boolean.TRUE:Boolean.FALSE;
    }

    /**
     * 培训报名信息保存
     * @param enrol
     * @param userid
     * @return
     */
    public ApiResultBean syncAddTranEnrol(WhgTraEnrol enrol, String userid) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        WhgUser user = whgUserMapper.selectByPrimaryKey(userid);
        //验证信息是否完善
        if(enrol != null && enrol.getTraid() != null){
            WhgTra tra = this.whgTraMapper.selectByPrimaryKey(enrol.getTraid());
            if(enrol.getRealname() == null || "".equals(enrol.getRealname().trim())){
                arb.setCode(102);
                arb.setMsg("请完善真实姓名");//请完善真实姓名
            }
            if(tra.getAge() != null && !"".equals(tra.getAge())){
                if(enrol.getBirthday() == null){
                    arb.setCode(103);
                    arb.setMsg("请完善资料");//请完善资料
                }
            }
        }

        arb = checkApplyTrain(enrol.getTraid(),userid,null);
        if (arb.getCode().intValue() != 0){
            return arb;
        }
        if(enrol.getBirthday() != null && !"".equals(enrol.getBirthday())){
            arb = validAge(enrol, userid);
            if (arb.getCode().intValue() != 0){
                return arb;
            }
        }
        addTranEnrol(enrol,userid);
        return arb;
    }

    /**
     * 添加培训报名
     * @param enrol
     * @param userid
     * @return
     * @throws Exception
     */
    public int addTranEnrol(WhgTraEnrol enrol,String userid)throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        WhgTra train = getTrainById(enrol.getTraid());
        WhgUser user = whgUserMapper.selectByPrimaryKey(userid);
        String id = IDUtils.getID();
        //订单号和id保持一致
        enrol.setId(id);
        enrol.setOrderid(id);
        int state = EnumBMState.BM_SQ.getValue();
        Date now = Calendar.getInstance().getTime();
        //先报先得培训,直接报名通过
        if(train.getIsbasicclass() == 2){
            state = EnumBMState.BM_CG.getValue();
        }
        enrol.setBirthday(enrol.getBirthday());
        enrol.setState(state);
        enrol.setStatemdfdate(now);
        enrol.setStatemdfuser(userid);
        enrol.setUserid(userid);
        enrol.setCrttime(now);
        int result = whgTraEnrolMapper.insertSelective(enrol);
        //发送短信
        if(train.getBiz() != null && "PT".equals(train.getBiz())){
            if(result > 0 && state == EnumBMState.BM_CG.getValue()){
                Map<String,String> _map = new HashMap<>();
                _map.put("userName",enrol.getRealname());
                _map.put("title",train.getTitle());
                _map.put("starttime", DateFormatUtils.format(train.getStarttime(),"yyyy-MM-dd "));
                _map.put("endtime",DateFormatUtils.format(train.getEndtime(),"yyyy-MM-dd "));
                String tempCode = "TRA_VIEW_PASS";
                if(train.getNoticetype() != null && "ZNX".equals(train.getNoticetype())){
                    insMessageService.t_sendZNX(enrol.getUserid(),null,tempCode,_map,enrol.getTraid(), EnumProject.PROJECT_WLPX.getValue());
                }else if(train.getNoticetype() != null && "SMS".equals(train.getNoticetype())){
                    this.smsService.t_sendSMS(enrol.getContactphone(),tempCode,_map, enrol.getTraid());
                }else {
                    insMessageService.t_sendZNX(enrol.getUserid(),null,tempCode,_map,train.getId(), EnumProject.PROJECT_WLPX.getValue());
                    this.smsService.t_sendSMS(enrol.getContactphone(),tempCode,_map, enrol.getTraid());
                }

            }
        }else {
            Map<String,String> _map = new HashMap<>();
            _map.put("userName",enrol.getRealname());
            _map.put("title",train.getTitle());
            _map.put("starttime", DateFormatUtils.format(train.getStarttime(),"yyyy-MM-dd "));
            _map.put("endtime",DateFormatUtils.format(train.getEndtime(),"yyyy-MM-dd "));
            String tempCode = "ZC_CY_True";
            if(train.getNoticetype() != null && "ZNX".equals(train.getNoticetype())){
                insMessageService.t_sendZNX(enrol.getUserid(),null,tempCode,_map,enrol.getTraid(), EnumProject.PROJECT_ZC.getValue());
            }else if(train.getNoticetype() != null && "SMS".equals(train.getNoticetype())){
                this.smsService.t_sendSMS(enrol.getContactphone(),tempCode,_map, enrol.getTraid());
            }else {
                insMessageService.t_sendZNX(enrol.getUserid(),null,tempCode,_map,train.getId(), EnumProject.PROJECT_ZC.getValue());
                this.smsService.t_sendSMS(enrol.getContactphone(),tempCode,_map, enrol.getTraid());
            }
        }

        return result;
    }

    /**
     * 验证年龄段
     */
    public ApiResultBean validAge(WhgTraEnrol enrol,String userid)throws Exception{
        ApiResultBean arb = new ApiResultBean();
        //年龄段验证
        String[] age = new String[2];

        WhgTra train = getTrainById(enrol.getTraid());

        String _age = train.getAge();
        //适合年龄
        if(!"".equals(_age) && _age != null){
            age = _age.split(",");
        }
        if(age[0] != null && !"".equals(age[0]) && age[1] != null && !"".equals(age[1])){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cnow = Calendar.getInstance();
            int _now = cnow.get(Calendar.YEAR);
            cnow.setTime(enrol.getBirthday());
            int bir = cnow.get(Calendar.YEAR);
            int a = _now - bir;
            if(!(a >= Integer.parseInt(age[0]) && (Integer.parseInt(age[1])>= a))){
                arb.setCode(104); //年龄段不符合培训要求
                arb.setMsg("年龄段不符合培训要求");
            }
        }
        return arb;
    }

    /**
     * 根据ID获取培训信息
     * @param trainId
     * @return
     */
    public WhgTra getTrainById(String trainId) throws Exception{
        return whgTraMapper.selectByPrimaryKey(trainId);
    }

    /**
     * 分页查询我的培训列表
     * @param page
     * @param pageSize
     * @param userId
     * @param sdate
     * @return
     */
    public PageInfo getUserTraOrder(int page, int pageSize, String userId, Integer sdate) throws Exception {
        PageHelper.startPage(page,pageSize);
        Map map = new HashMap();
        map.put("sdate",sdate);
        map.put("userid",userId);
        List whgTraEnrolList = apiTraMapper.getTraEnrolListByUserId(map);
        return new PageInfo(whgTraEnrolList);

    }

    /**
     * 根据主键更新状态
     * @param enrol
     * @throws Exception
     */
    public void updateMyEnroll(WhgTraEnrol enrol)throws Exception {
        this.whgTraEnrolMapper.updateByPrimaryKeySelective(enrol);
    }

    /**
     * 删除订单
     * @param orderId
     * @throws Exception
     */
    public void delEnroll(String orderId)throws Exception {
        this.whgTraEnrolMapper.deleteByPrimaryKey(orderId);
    }

    /**
     * 查询老师相关培训
     * @param id
     * @param cultid
     * @param size
     * @return
     */
    public PageInfo getTraByTea(String id, String cultid, Integer size) {
        Example example = new Example(WhgTra.class);
        Example.Criteria c = example.createCriteria();
        c.andLike("teacherid","%"+id+"%");
        if(cultid != null && !"".equals(cultid)){
            c.andIn("cultid", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));//多个cultid
        }
        example.setOrderByClause("statemdfdate desc");
        if(size != null){
            PageHelper.startPage(1,size);
        }else {
            PageHelper.startPage(1,3);
        }
        List<WhgTra> list = this.whgTraMapper.selectByExample(example);
        return new PageInfo(list);
    }
}
