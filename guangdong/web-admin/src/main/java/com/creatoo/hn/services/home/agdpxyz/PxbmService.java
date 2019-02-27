package com.creatoo.hn.services.home.agdpxyz;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumBMState;
import com.creatoo.hn.ext.emun.EnumDelState;
import com.creatoo.hn.ext.emun.EnumState;
import com.creatoo.hn.ext.emun.EnumTypeClazz;
import com.creatoo.hn.mapper.*;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.utils.IdUtils;
import com.creatoo.hn.utils.WhConstance;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.util.calendar.LocalGregorianCalendar;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 培训报名服务类
 * Created by tangwei on 2017/4/6.
 */
@Service
public class PxbmService {
    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private CommService commService;

    @Autowired
    public WhgTraMapper whTrainMapper;

    @Autowired
    public WhgTraEnrolMapper whgTraEnrolMapper;

    /**
     * 培训课程DAO
     */
    @Autowired
    public WhgTraCourseMapper whgTraCourseMapper;

    /**
     * 培训报名签到DAO
     */
    @Autowired
    public WhgTraEnrolCourseMapper whgTraEnrolCourseMapper;

    @Autowired
    private WhUserMapper userMapper;

    @Autowired
    private SMSService smsService;

    @Autowired
    private WhgSignDataMapper whgSignDataMapper;

    private final  String SUCCESS="0";//成功
    /**
     * 检查培训报名
     * @param trainId
     * @return
     */
    public String checkApplyTrain(String trainId,String userId)throws Exception{
        WhgTra train = whTrainMapper.selectByPrimaryKey(trainId);
        //培训不存在,或者状态不是已发布
        if(train == null || train.getState().intValue() != 6){
            return "100";
        }
        //培训报名已结束或未开始
        if(train.getEnrollendtime().before(Calendar.getInstance().getTime()) || train.getEnrollstarttime().after(Calendar.getInstance().getTime())){
            return "101";
        }
        //培训报名额已满
        if(!checkTrainMaxNumber(train)){
            return "102";
        }
        //培训报名重复
        if(userId != null && !"".equals(userId)){
            if(!checkExistEnrol(train.getId(),userId)){
                return "103";
            }
        }
        //用户手机号不存在
        if(userId != null && !"".equals(userId)){
            WhUser_old user = userMapper.selectByPrimaryKey(userId);
            if(user.getPhone() == null || user.getPhone().isEmpty()){
                return "205";
            }
        }

        //培训可是是否重叠
        if(userId != null && !"".equals(userId)){
            //int count = 0;
            Example example = new Example(WhgTraCourse.class);
            example.createCriteria().andEqualTo("traid",trainId);
            List<WhgTraCourse> courseList = this.whgTraCourseMapper.selectByExample(example);
            if(courseList != null && courseList.size() > 0){
                for (WhgTraCourse course : courseList) {
                    Date starttime = course.getStarttime();
                    Date endtime = course.getEndtime();
                    int count = this.whgTraEnrolMapper.selCount(starttime,endtime,userId,new Date());
                    if(count > 0){
                        return "206";
                    }
                }

            }
        }

        //实名制验证
        if(train.getIsrealname().intValue() ==1 && userId != null && !"".equals(userId)){
            WhUser_old user = userMapper.selectByPrimaryKey(userId);
            if(user.getIsrealname() == null || user.getIsrealname().intValue() != 1){
                return "104";
            }
        }
        //获取普及班分类ID

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        Date beginYear = c.getTime();
        c.add(Calendar.YEAR, 1);
        Date endYear = c.getTime();

        Date now = new Date();
        /*if(userId != null && !"".equals(userId)){
           *//* *//*
                int result = this.whgTraEnrolMapper.selEnrolCount(userId,now,beginYear,endYear);
                if (result >= 2) {
                    return "106";  //已经报了两场普及班
          *//*  }*//*

        }*/
        if(train.getIsmoney().intValue() == 0){
            if(userId != null && !"".equals(userId)){
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date myDate = dateFormat.parse("2017-12-01");
                int result = this.whgTraEnrolMapper.selQjEnrolCount(userId,myDate);
                if (result >= 2) {
                    return "106";  //已经报了两场普及班
                }
            }
        }



        /*List<WhgTraEnrol> list = new ArrayList();
        String etype = "";
        List<WhgYwiType> wyts = this.commService.findYwiType(EnumTypeClazz.TYPE_TRAIN.getValue());
        if(wyts != null){
            for(WhgYwiType wyt : wyts){
                if(WhConstance.getSysProperty("whg.sys.type.tra.pjb", "普及班").equals(wyt.getName())){
                    etype = wyt.getId();
                    break;
                }
            }
        }*/
        /*if(train.getEtype().equals(etype)){
            //普及班验证
            if(!"".equals(etype)) {
                //String uid = enrol.getUserid();
                Example example = new Example(WhgTraEnrol.class);
                example.createCriteria().andEqualTo("userid", userId).andIn("state", Arrays.asList(1, 6, 4));
                list = this.WhgTraEnrolMapper.selectByExample(example);
                int listSize = 0;
                Date now = new Date();
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        String traid = list.get(i).getTraid();
                        WhgTra tra = this.whTrainMapper.selectByPrimaryKey(traid);
                        if(tra.getEndtime().getTime() < now.getTime()){
                            Example example1 = new Example(WhgTra.class);
                            example1.createCriteria().andEqualTo("id", traid).andEqualTo("state", 6);
                            int count = this.whTrainMapper.selectCountByExample(example1);
                            listSize += count;
                            if (listSize >= 2) {
                                return "106";  //已经报了两场普及班
                            }
                        }
                    }
                }
          }
        }*/
        return SUCCESS;
    }

    /**
     * 根据ID获取培训信息
     * @param trainId
     * @return
     */
    public WhgTra getTrainById(String trainId) throws Exception{
        return whTrainMapper.selectByPrimaryKey(trainId);
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


    //同步保存方法
    public synchronized String syncAddTranEnrol(WhgTraEnrol enrol,String userId) throws Exception{
        WhUser_old user = userMapper.selectByPrimaryKey(userId);
        //验证信息是否完善
        if(enrol != null && enrol.getTraid() != null){
            WhgTra tra = this.whTrainMapper.selectByPrimaryKey(enrol.getTraid());
//            if(tra.getIsrealname().intValue() == 1){
//                if(enrol == null || enrol.getCardno() == null || enrol.getRealname() == null){
//                    return "202";//请实名认证
//                }
//            }
            if(enrol.getRealname() == null || "".equals(enrol.getRealname().trim())){
                return "203";//请完善真实姓名
            }
            if(tra.getAge() != null && !"".equals(tra.getAge())){
                if(enrol.getBirthday() == null){
                    return "201";//请完善资料
                }
            }
        }

        String checkResult = checkApplyTrain(enrol.getTraid(),userId);
        if (!SUCCESS.equals(checkResult)){
            return checkResult;
        }
        if(enrol.getBirthday() != null && !"".equals(enrol.getBirthday())){
            checkResult = validAge(enrol, userId);
            if (!SUCCESS.equals(checkResult)){
                return checkResult;
            }
        }

        addTranEnrol(enrol,userId);
        return SUCCESS;
    }


    /**
     * 添加培训报名
     * @param enrol
     * @param userId
     * @return
     * @throws Exception
     */
    public int addTranEnrol(WhgTraEnrol enrol,String userId)throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        WhgTra train = getTrainById(enrol.getTraid());
        WhUser_old user = userMapper.selectByPrimaryKey(userId);
        String id = commService.getKey("whg_tra_enrol");
        //订单号和id保持一致
        enrol.setId(id);
        enrol.setOrderid(id);
        int state = EnumBMState.BM_SQ.getValue();
        Date now = Calendar.getInstance().getTime();
        //先报先得培训,直接报名通过
        if(train.getIsbasicclass() == 2){
            state = EnumBMState.BM_CG.getValue();
        }
//        if(StringUtils.isNotEmpty(enrolBirthdayStr)){
//            String []parsePatterns ={"yyyy-MM-dd"};
//            enrol.setBirthday(DateUtils.parseDate(enrolBirthdayStr,parsePatterns));
//        }
        enrol.setBirthday(enrol.getBirthday());
        enrol.setState(state);
        enrol.setStatemdfdate(now);
        enrol.setStatemdfuser(userId);
        enrol.setUserid(userId);
        enrol.setCrttime(now);
        int result = whgTraEnrolMapper.insertSelective(enrol);
        //发送短信
        if(result > 0 && state == EnumBMState.BM_CG.getValue()){
            Map<String,String> _map = new HashMap<>();
            _map.put("name",enrol.getRealname());
            _map.put("title",train.getTitle());
            _map.put("starttime",DateFormatUtils.format(train.getStarttime(),"yyyy-MM-dd "));
            _map.put("endtime",DateFormatUtils.format(train.getEndtime(),"yyyy-MM-dd "));
            String tempCode = "TRA_VIEW_PASS";
            this.smsService.t_sendSMS(enrol.getContactphone(),tempCode,_map, enrol.getTraid());
        }
        return result;
    }

    /**
     * 验证年龄段
     */
    public String validAge(WhgTraEnrol enrol,String userid)throws Exception{
        //WhUser_old user = this.userMapper.selectByPrimaryKey(userid);
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
            //int bir = sdf.parse(enrolBirthdayStr).getYear();
            //Date nowDate = new Date();
            Calendar cnow = Calendar.getInstance();
            int _now = cnow.get(Calendar.YEAR);
            cnow.setTime(enrol.getBirthday());
            int bir = cnow.get(Calendar.YEAR);
            int a = _now - bir;
            if(!(a >= Integer.parseInt(age[0]) && (Integer.parseInt(age[1])>= a))){
                return "107"; //年龄段不符合培训要求
            }
        }



        return SUCCESS;
    }

    /**
     * 培训签到
     * @param cardno 身份证号码
     * @return 100-签到成功; 101-没有报名信息; 102-没有课程信息; 103-课程已取消  104-已签到
     * @throws Exception
     */
    public String signup(String cardno)throws Exception{
        String rtnCode = "100";//签到成功
        Example example = new Example(WhgTraEnrol.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("cardno",cardno);
        c.andEqualTo("state",6);
        List<WhgTraEnrol> enrolList = this.whgTraEnrolMapper.selectByExample(example);
        if(enrolList != null && enrolList.size() >0){
            for (WhgTraEnrol enrol: enrolList) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR_OF_DAY,-1);
                Date now = calendar.getTime();
                calendar.add(Calendar.HOUR_OF_DAY,2);
                Date _now = calendar.getTime();
                Example example1 = new Example(WhgTraCourse.class);
                Example.Criteria c1 = example1.createCriteria();
                c1.andEqualTo("traid",enrol.getTraid());
                c1.andLessThan("starttime",_now);
                c1.andGreaterThan("starttime",now);
                List<WhgTraCourse> course = this.whgTraCourseMapper.selectByExample(example1);

                if(course != null && course.size() > 0){
                    if(course.get(0).getState() == 0){
                        rtnCode = "103";
                        return rtnCode;
                    }else {
                        WhgTraEnrolCourse enrolCourse = new WhgTraEnrolCourse();
                        enrolCourse.setCourseid(course.get(0).getId());
                        enrolCourse.setSign(1);
                        List<WhgTraEnrolCourse> list = this.whgTraEnrolCourseMapper.select(enrolCourse);
                        if(list != null && list.size() > 0){
                            rtnCode = "104";
                            return rtnCode;
                        }else {
                            enrolCourse.setId(IdUtils.getId());
                            enrolCourse.setEnrolid(enrol.getId());
                            enrolCourse.setTraid(enrol.getTraid());
                            enrolCourse.setCourseid(course.get(0).getId());
                            enrolCourse.setCoursestime(course.get(0).getStarttime());
                            enrolCourse.setCourseetime(course.get(0).getEndtime());
                            enrolCourse.setSign(1);
                            enrolCourse.setSigntime(now);
                            enrolCourse.setUserid(enrol.getUserid());
                            this.whgTraEnrolCourseMapper.insert(enrolCourse);
                            rtnCode = "100";
                            return rtnCode;
                        }

                    }

                }else{
                    rtnCode = "102";
                }
            }
        }else {
            rtnCode = "101";
        }

       /* try {
            //查询签到信息
            WhgTraEnrolCourse record = new WhgTraEnrolCourse();
            record.setTraid(traId);
            record.setEnrolid(enrolId);
            record.setCourseid(courseId);
            record.setUserid(userId);
            record = this.whgTraEnrolCourseMapper.selectOne(record);

            if (record != null) {//有签到信息
                if(record.getSign().intValue() != 1){
                    record.setSign(1);//
                    record.setSigntime(new Date());
                }else{
                    rtnCode = "101";//已签到
                }
            } else {//没有签到信息
                //查询课程信息
                WhgTraCourse course = new WhgTraCourse();
                course.setId(courseId);
                course.setDelstate(EnumDelState.STATE_DEL_NO.getValue());
                course.setState(EnumState.STATE_YES.getValue());
                course = this.whgTraCourseMapper.selectOne(course);
                if(course != null && course.getId() != null){
                    record = new WhgTraEnrolCourse();
                    record.setId(commService.getKey("whg_tra_enrol_course"));
                    record.setSign(1);//
                    record.setSigntime(new Date());
                    record.setCoursestime(course.getStarttime());
                    record.setCoursestime(course.getEndtime());
                }else{
                    rtnCode = "102";//已签到课程已取消
                }
            }
        }catch (Exception e){
            rtnCode = "109";//签到失败
        }*/

        return rtnCode;
    }

    /**
     * 根据报名订单号查询所有课程
     * @param orderId 培训报名列表
     * @return
     */
    public List<Map<String, String>> queryEnrolCourseList(String orderId)throws Exception{
        List<Map<String, String>> list = new ArrayList<>();


        //查询报名记录得到培训ID
        WhgTraEnrol record = new WhgTraEnrol();
        record.setOrderid(orderId);
        record = this.whgTraEnrolMapper.selectOne(record);//.selectByExample(example);

        //根据报名ID取已签到信息
        Example example2 = new Example(WhgTraEnrolCourse.class);
        example2.createCriteria().andEqualTo("enrolid", record.getId());
        List<WhgTraEnrolCourse> clist = this.whgTraEnrolCourseMapper.selectByExample(example2);
        Map<String, WhgTraEnrolCourse> courseMap = new HashMap<String, WhgTraEnrolCourse>();
        if(clist != null){
            for(WhgTraEnrolCourse ec : clist){
                courseMap.put(ec.getCourseid(), ec);
            }
        }

        //根据培训ID查询所有课程
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String traid = record.getTraid();
        Example example = new Example(WhgTraCourse.class);
        example.createCriteria().andEqualTo("traid", traid)
                .andEqualTo("state", EnumState.STATE_YES.getValue())
        .andEqualTo("delstate", EnumDelState.STATE_DEL_NO.getValue());
        example.setOrderByClause("starttime");
        List<WhgTraCourse> listCourse = this.whgTraCourseMapper.selectByExample(example);
        if(listCourse != null){
            for(WhgTraCourse course : listCourse){
                Map<String, String> cmap = new HashMap<String, String>();
                cmap.put("traid", traid);
                cmap.put("enrolid", record.getId());
                cmap.put("courseid", course.getId());
                cmap.put("starttime", sdf.format(course.getStarttime()));
                cmap.put("endtime", sdf.format(course.getEndtime()));

                String sign = "0";//签到状态 0-未签到, 1-已签到
                String signtime = "";//签到时间
                String userid = ""; //签到会员ID
                if(courseMap != null && courseMap.containsKey(course.getId())){
                    WhgTraEnrolCourse wec = courseMap.get(course.getId());
                    sign = wec.getSign()+"";
                    signtime = sdf.format(wec.getSigntime());
                    userid = wec.getUserid();
                }
                cmap.put("sign", sign);
                cmap.put("signtime", signtime);
                cmap.put("userid", userid);


                list.add(cmap);
            }
        }

        return list;
    }

    private String checkEmpty(String str){
        if(StringUtils.isEmpty(str) || str.trim().length() == 0){
            return null;
        }
        return str;
    }

    public Object getUserById(String userid) {
        return userMapper.selectByPrimaryKey(userid);
    }

    /**
     * 保存签到身份证信息
     * @param name
     * @param sex
     * @param idcard
     * @throws Exception  109-操作失败;   104-已签到
     */
    public ResponseBean insertSignData(String name, Integer sex, String idcard)throws Exception {
        ResponseBean rsb = new ResponseBean();
        rsb.setErrormsg("100");
        Example example = new Example(WhgSignData.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("idcard",idcard);
        List<WhgSignData> signList = this.whgSignDataMapper.selectByExample(example);
        if(signList == null || signList.size() == 0){
            Date now = new Date();
            WhgSignData sign = new WhgSignData();
            sign.setId(commService.getKey("whg_sign_data"));
            sign.setName(name);
            sign.setCrtdate(now);
            sign.setIdcard(idcard);
            sign.setSex(sex);
            this.whgSignDataMapper.insertSelective(sign);
        }else {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("104");//已经签到
        }

        return rsb;
    }



}
