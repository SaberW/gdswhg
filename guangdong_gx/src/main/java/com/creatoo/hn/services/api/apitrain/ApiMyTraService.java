package com.creatoo.hn.services.api.apitrain;

import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.mapper.api.ApiTraMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.Distance;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by Administrator on 2017/10/13.
 */
@Service
public class ApiMyTraService extends BaseService{

    @Autowired
    private ApiTraMapper apiTraMapper;

    @Autowired
    private WhgTraleaveMapper whgTraleaveMapper;

    @Autowired
    private WhgTraEnrolMapper whgTraEnrolMapper;

    @Autowired
    private WhgTraEnrolCourseMapper whgTraEnrolCourseMapper;

    @Autowired
    private WhgTraCourseMapper whgTraCourseMapper;

    @Autowired
    private WhgTraMapper whgTraMapper;


    /**
     * 根据用户id查询培训订单
     * @param page
     * @param pageSize
     * @param userid
     * @return
     */
    public PageInfo t_getUserTraEnrol(int page, int pageSize, String userid,Integer type,String cultid) throws Exception {
        PageHelper.startPage(page, pageSize);
        List list=null;
        if(cultid!=null&&!"".equals(cultid)){
            list=Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));//多个cultid
        }
        List reslist = this.apiTraMapper.selTraEnrol4Userid(userid,type,list);
        return new PageInfo(reslist);
    }

    /**
     * 保存请假信息
     * @param leave
     * @return
     */
    public ApiResultBean t_saveTraLeave(WhgTraleave leave) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        Example example = new Example(WhgTraleave.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid",leave.getTraid());
        c.andEqualTo("courseid",leave.getCourseid());
        c.andEqualTo("userid",leave.getUserid());
        List<WhgTraleave> list = this.whgTraleaveMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            if(list.get(0).getState() == 2){
                leave.setId(list.get(0).getId());
                leave.setState(0);
                whgTraleaveMapper.updateByPrimaryKeySelective(leave);
                return arb;
            }else {
                arb.setCode(201);
                arb.setMsg("已经请假");
                return arb;
            }
        }else {
            leave.setId(IDUtils.getID());
            leave.setCrtdate(new Date());
            leave.setState(0);
            int result = whgTraleaveMapper.insertSelective(leave);
            if(result != 1){
                arb.setCode(202);
                arb.setMsg("添加失败");
                return arb;
            }
            return arb;
        }
    }

    /**
     * 课程表当前课程
     * @param page
     * @param pageSize
     * @param userid
     * @param cultid
     * @param now
     * @return
     */
    public PageInfo t_getkecheng(int page, int pageSize, String userid, String cultid, Date now) {
        PageHelper.startPage(page, pageSize);
        List<Map> reslist = this.apiTraMapper.selNowKecheng(userid,cultid,now);
        if(reslist != null && reslist.size() > 0){
            for(int i = 0;i < reslist.size(); i++){
                String cid = String.valueOf(reslist.get(i).get("id"));
                String title = String.valueOf(reslist.get(i).get("title"));
                String _title = "";
                String traid = String.valueOf(reslist.get(i).get("tid"));
                Example example = new Example(WhgTraCourse.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("traid",traid).andEqualTo("state",1);
                example.setOrderByClause("starttime asc");
                List<WhgTraCourse> _list = whgTraCourseMapper.selectByExample(example);
                if(_list != null && _list.size() > 0){
                    for (int j = 0;j < _list.size(); j++){
                        if(cid.equals(_list.get(j).getId())){
                            _title = title+"（第"+(j+1)+"课）";
                            reslist.get(i).put("title",_title);
                        }
                    }
                }
            }

        }
        return new PageInfo(reslist);
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
     * 查询课程
     * @param userid
     * @param cultid
     * @return
     */
    public List t_srchKecheng(String userid, String cultid) {
        List reslist = this.apiTraMapper.selAllKecheng(userid,cultid);
        return reslist;
    }

    /**
     * 根据培训id分页查询课程
     * @param page
     * @param pageSize
     * @param traid
     * @return
     */
    public PageInfo t_srchKecheng4Tid(int page, int pageSize, String traid,String userid) {
        PageHelper.startPage(page, pageSize);
        List reslist = this.apiTraMapper.selKecheng4Tid(traid,userid);
        return new PageInfo(reslist);
    }

    /**
     * 正在上课的课程
     * @param userid
     * @param cultid
     * @return
     */
    public List t_srchinClass(String userid, String cultid,Date now) {
        List reslist = this.apiTraMapper.selInClass(userid,cultid,now);
        return reslist;
    }

    /**
     *weixin课程表
     * @param page
     * @param pageSize
     * @param userid
     * @param cultid
     * @param now
     * @return
     */
    public PageInfo t_getWxKeCheng(int page, int pageSize, String userid, String cultid, Date now) {
        PageHelper.startPage(page, pageSize);
        List reslist = this.apiTraMapper.t_getWxKeCheng(userid,cultid,now);
        return new PageInfo(reslist);
    }

    /**
     * 签到
     * @param orderid
     * @param courseid
     * @param longitude
     * @param latitude
     * @return
     */
    public ApiResultBean t_signup(String orderid, String courseid, String longitude, String latitude) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        Calendar c = Calendar.getInstance() ;
        Example example = new Example(WhgTraEnrolCourse.class);
        Example.Criteria exc = example.createCriteria();
        exc.andEqualTo("enrolid",orderid).andEqualTo("courseid",courseid).andEqualTo("sign",1);
        List<WhgTraEnrolCourse> enrolCourseslist = whgTraEnrolCourseMapper.selectByExample(example);
        if(enrolCourseslist != null && enrolCourseslist.size() > 0){
            arb.setCode(106);
            arb.setMsg("已签到");
            return arb;
        }
        //报名信息
        WhgTraEnrol enrol = this.whgTraEnrolMapper.selectByPrimaryKey(orderid);
        if(enrol != null){
            String userid = enrol.getUserid();
            //培训信息
            String traid = enrol.getTraid();
            WhgTra tra = whgTraMapper.selectByPrimaryKey(traid);
            String _latitude = tra.getLatitude();
            String _longitude = tra.getLongitude();
            //课程信息
            WhgTraCourse course = whgTraCourseMapper.selectByPrimaryKey(courseid);
            if(course != null){
                Date starttime = course.getStarttime();
                Date endtime = course.getEndtime();
                Calendar endTime = Calendar.getInstance() ;
                endTime.setTime(endtime);
                //经纬度判断
                Distance ad = new Distance() ;
                ad.setLongitude(Double.parseDouble(longitude));
                ad.setDimensionality(Double.parseDouble(latitude));
                Distance ada = new Distance() ;
                ada.setLongitude(Double.parseDouble(_longitude));
                ada.setDimensionality(Double.parseDouble(_latitude));
                double dis = Distance.getDistance(ad,ada) ;
                //结束时间内 并且 开课地点与学院地点在500米范围之内才可签到
                if((endTime.compareTo(c) >= 0) && (dis <= 2)){
                    WhgTraEnrolCourse enrolCourse = new WhgTraEnrolCourse();
                    enrolCourse.setId(IDUtils.getID());
                    enrolCourse.setCourseid(courseid);
                    enrolCourse.setSign(1);
                    enrolCourse.setEnrolid(enrol.getId());
                    enrolCourse.setTraid(enrol.getTraid());
                    enrolCourse.setCoursestime(starttime);
                    enrolCourse.setCourseetime(endtime);
                    enrolCourse.setSigntime(new Date());
                    enrolCourse.setUserid(enrol.getUserid());
                    whgTraEnrolCourseMapper.insertSelective(enrolCourse);
                }else {
                    arb.setCode(102);
                    arb.setMsg("签到时间已过或您的位置不在培训地点范围之内");
                }
            }else {
                arb.setCode(104);
                arb.setMsg("没有查询到培训课程");
                return arb;
            }

        }else {
            arb.setCode(103);
            arb.setMsg("没有查询到培训订单");
            return arb;
        }
        return arb;
    }

    /**
     * 一体机签到
     *
     * @param longitude
     * @param latitude
     * @return
     */
    public ResponseBean t_usersignup(String usernumber, String longitude, String latitude) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        Calendar c = Calendar.getInstance();
        Date dd = new Date();
        c.setTime(dd);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        List<Map> courseslist = apiTraMapper.selTraCourseByUserid(usernumber, c.getTime(), calendar.getTime());
        if (courseslist.size() > 0) {
            Map map = courseslist.get(0);
            Example example = new Example(WhgTraEnrolCourse.class);
            Example.Criteria exc = example.createCriteria();
            exc.andEqualTo("enrolid", (String) map.get("enrolid")).andEqualTo("courseid", (String) map.get("courseid")).andEqualTo("sign", 1);
            List<WhgTraEnrolCourse> enrolCourseslist = whgTraEnrolCourseMapper.selectByExample(example);
            if (enrolCourseslist != null && enrolCourseslist.size() > 0) {
                responseBean.setSuccess("106");
                responseBean.setErrormsg("你已进行签到，请勿重复操作！");
                return responseBean;
            }
            WhgTraEnrol enrol = this.whgTraEnrolMapper.selectByPrimaryKey((String) map.get("enrolid"));
            if (enrol != null) {
                //培训信息
                String traid = enrol.getTraid();
                WhgTra tra = whgTraMapper.selectByPrimaryKey(traid);
                String _latitude = tra.getLatitude();
                String _longitude = tra.getLongitude();
                //课程信息
                WhgTraCourse course = whgTraCourseMapper.selectByPrimaryKey((String) map.get("courseid"));
                if (course != null) {
                    Date starttime = course.getStarttime();
                    Date endtime = course.getEndtime();
                    Calendar endTime = Calendar.getInstance();
                    endTime.setTime(endtime);
                    //经纬度判断
                    Distance ad = new Distance();
                    ad.setLongitude(Double.parseDouble(longitude));
                    ad.setDimensionality(Double.parseDouble(latitude));
                    Distance ada = new Distance();
                    ada.setLongitude(Double.parseDouble(_longitude));
                    ada.setDimensionality(Double.parseDouble(_latitude));
                    double dis = Distance.getDistance(ad, ada);
                    //结束时间内 并且 开课地点与学院地点在500米范围之内才可签到
                    if ((endTime.compareTo(c) >= 0) && (dis <= 2)) {
                        WhgTraEnrolCourse enrolCourse = new WhgTraEnrolCourse();
                        enrolCourse.setId(IDUtils.getID());
                        enrolCourse.setCourseid((String) map.get("courseid"));
                        enrolCourse.setSign(1);
                        enrolCourse.setEnrolid(enrol.getId());
                        enrolCourse.setTraid(enrol.getTraid());
                        enrolCourse.setCoursestime(starttime);
                        enrolCourse.setCourseetime(endtime);
                        enrolCourse.setSigntime(new Date());
                        enrolCourse.setUserid(enrol.getUserid());
                        whgTraEnrolCourseMapper.insertSelective(enrolCourse);
                    } else {
                        responseBean.setSuccess("102");
                        responseBean.setErrormsg("签到时间已过或您的位置不在培训地点范围之内！");
                    }
                } else {
                    responseBean.setSuccess("104");
                    responseBean.setErrormsg("没有查询到培训课程！");
                    return responseBean;
                }
            }
        } else {
            responseBean.setSuccess("106");
            responseBean.setErrormsg("身份证信息有误或未到签到时间，请于课程开始前30分钟进行签到！");
            return responseBean;
        }
        return responseBean;

    }

}
