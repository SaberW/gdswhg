package com.creatoo.hn.services.admin.train;

import com.creatoo.hn.dao.mapper.WhgTraCourseMapper;
import com.creatoo.hn.dao.mapper.WhgTraEnrolCourseMapper;
import com.creatoo.hn.dao.mapper.api.train.KaoqinTraMapper;
import com.creatoo.hn.dao.model.WhgTraCourse;
import com.creatoo.hn.dao.model.WhgTraEnrolCourse;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@SuppressWarnings("ALL")
@Service
public class KaoqinTraService extends BaseService {

    @Autowired
    private KaoqinTraMapper kaoqinTraMapper;

    @Autowired
    private WhgTraEnrolCourseMapper whgTraEnrolCourseMapper;

    @Autowired
    private WhgTraCourseMapper whgTraCourseMapper;

    public ResponseBean updateKq2Qd(String userid, String traid, String enrolid, String courseid) throws Exception{
        ResponseBean rb = new ResponseBean();

        if (userid == null || userid.isEmpty() || traid == null || traid.isEmpty()
                || enrolid == null || enrolid.isEmpty() || courseid == null || courseid.isEmpty()){
            rb.setErrormsg("参数缺失");
            rb.setSuccess(rb.FAIL);
            return rb;
        }

        WhgTraEnrolCourse tec = new WhgTraEnrolCourse();
        tec.setUserid(userid);
        tec.setTraid(traid);
        tec.setEnrolid(enrolid);
        tec.setCourseid(courseid);
        List<WhgTraEnrolCourse> list = this.whgTraEnrolCourseMapper.select(tec);

        if (list!= null && list.size()>0){
            //如果已有记录，修改一个为已签到
            tec = list.get(0);
            if (tec.getSign()==null && tec.getSign().intValue() !=1){
                tec.setSign(1);
                this.whgTraEnrolCourseMapper.updateByPrimaryKey(tec);
                return rb;
            }
        }else{
            //添加一条记录
            tec.setSign(1);
            tec.setSigntime(new Date());
            tec.setId(IDUtils.getID());

            WhgTraCourse tc = this.whgTraCourseMapper.selectByPrimaryKey(courseid);
            tec.setCoursestime(tc.getStarttime());
            tec.setCourseetime(tc.getEndtime());

            this.whgTraEnrolCourseMapper.insert(tec);
        }

        return rb;
    }

    public List selectTraKqList(Map record) throws Exception{
        return this.kaoqinTraMapper.selectTraKaoqin(record);
    }

    public PageInfo getTraKqinfo4User(int page, int pageSize, Map record) throws Exception{
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.kaoqinTraMapper.selectTraKaoqin(record);
        PageInfo pageInfo = new PageInfo(list);

        Date NOW = new Date();
        for(Object ent : pageInfo.getList()){
            Map row = (Map) ent;
            String enrolName = (String) row.get("enrolName");
            if (enrolName==null || enrolName.isEmpty()){
                String _name = (String) row.get("name");
                if (_name == null || _name.isEmpty()){
                    _name = (String) row.get("nickname");
                }
                enrolName = _name;
            }
            String coruseTitle = (String) row.get("coruseTitle");
            if (coruseTitle==null || coruseTitle.isEmpty()){
                coruseTitle = (String) row.get("title");
            }
            Integer sign = (Integer) row.get("sign");
            Integer traleaveState = (Integer) row.get("traleaveState");
            Date endtime = (Date) row.get("endtime");
            String kqText = "";
            if (traleaveState!=null && traleaveState.intValue()==1){
                kqText = "已请假";
            }else if (sign !=null && sign.intValue() == 1){
                kqText = "已签到";
            }
            if (endtime!=null && NOW.compareTo(endtime)>0){
                if ("".equals(kqText)){
                    kqText = "缺勤";
                }
            }

            row.put("userKqText", kqText);
            row.put("userName", enrolName);
            row.put("kechengTitle", coruseTitle);
        }

        return pageInfo;
    }

    public Map getTraKqinfo(List<Map> kqlist) throws Exception{
        Map kqObject = new HashMap();
        List kqKechengList = new ArrayList(); //课程信息id title time
        List kqUserList = new ArrayList(); //人员对应课程考勤信息

        //取所有的考勤课程信息
        List<String> _coruseIds = new ArrayList();
        for(Map kqrow : kqlist){
            String coruseId = (String) kqrow.get("coruseId");
            if (coruseId==null || coruseId.isEmpty() || _coruseIds.contains(coruseId)){
                continue;
            }

            String coruseTitle = (String) kqrow.get("coruseTitle");
            if (coruseTitle==null || coruseTitle.isEmpty()){
                coruseTitle = (String) kqrow.get("title");
            }

            Map kechengInfo = new HashMap();
            kechengInfo.put("coruseId", coruseId);
            kechengInfo.put("title", coruseTitle);
            kechengInfo.put("starttime", kqrow.get("starttime"));
            kechengInfo.put("endtime", kqrow.get("endtime"));

            kqKechengList.add(kechengInfo);
            _coruseIds.add(coruseId);
        }

        //用户ids提取
        List<String> _userids = new ArrayList();
        for (Map row : kqlist){
            String userid = (String) row.get("userid");
            if (userid == null|| userid.isEmpty() || _userids.contains(userid)){
                continue;
            }
            _userids.add(userid);
        }

        //组装用户考勤信息
        Date NOW = new Date();
        for(String userid : _userids){
            Map kquserinfo = new HashMap();
            kquserinfo.put("userId", userid);   //用户id
            for(String coruseId : _coruseIds){
                //kquserinfo.put("coruseId", coruseId); //课程id
                for (Map row : kqlist){
                    String useridVal = (String) row.get("userid");
                    String coruseIdVal = (String) row.get("coruseId");
                    if (!userid.equals(useridVal) || !coruseId.equals(coruseIdVal)){
                        continue;
                    }

                    Map _kqinfo = new HashMap();
                    if (!kquserinfo.containsKey("userName")){ //无需多次取
                        //取用户名
                        String enrolName = (String) row.get("enrolName");
                        if (enrolName==null || enrolName.isEmpty()){
                            String _name = (String) row.get("name");
                            if (_name == null || _name.isEmpty()){
                                _name = (String) row.get("nickname");
                            }
                            enrolName = _name;
                        }
                        _kqinfo.put("userName", enrolName);
                        _kqinfo.put("enrolId", row.get("enrolId"));
                        _kqinfo.put("traId", row.get("id"));
                    }

                    _kqinfo.put("cid_"+coruseId, coruseId);
                    //_kqinfo.put("signtime_"+coruseId, row.get("signtime"));
                    _kqinfo.put("starttime_"+coruseId, row.get("starttime"));
                    _kqinfo.put("endtime_"+coruseId, row.get("endtime"));
                    _kqinfo.put("sign_"+coruseId, row.get("sign"));
                    _kqinfo.put("traleaveState_"+coruseId, row.get("traleaveState"));

                    Long sign = (Long) row.get("sign");
                    Long traleaveState = (Long) row.get("traleaveState");
                    Date endtime = (Date) row.get("endtime");
                    String kqText = "";
                    if (traleaveState!=null && traleaveState.intValue()==1){
                        kqText = "已请假";
                    }else if (sign !=null && sign.intValue() == 1){
                        kqText = "已签到";
                    }
                    if (endtime!=null && NOW.compareTo(endtime)>0){
                        if ("".equals(kqText)){
                            kqText = "缺勤";
                        }
                    }
                    _kqinfo.put(coruseId, kqText);

                    kquserinfo.putAll(_kqinfo);
                }
            }
            kqUserList.add(kquserinfo);
        }


        kqObject.put("kechengs", kqKechengList);
        kqObject.put("kqusers", kqUserList);
        return kqObject;
    }

}
