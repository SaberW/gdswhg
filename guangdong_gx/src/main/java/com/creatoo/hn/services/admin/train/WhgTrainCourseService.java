package com.creatoo.hn.services.admin.train;

import com.creatoo.hn.dao.mapper.WhgTraCourseMapper;
import com.creatoo.hn.dao.mapper.WhgTraEnrolCourseMapper;
import com.creatoo.hn.dao.mapper.WhgTraEnrolMapper;
import com.creatoo.hn.dao.mapper.WhgTraMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgTra;
import com.creatoo.hn.dao.model.WhgTraCourse;
import com.creatoo.hn.dao.model.WhgTraEnrolCourse;
import com.creatoo.hn.dao.vo.TrainCourseVO;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 培训课程管理action
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/3/21.
 */
@Service
public class WhgTrainCourseService {

    /**
     * 培训课程mapper
     */
    @Autowired
    private WhgTraCourseMapper whgTraCourseMapper;

    @Autowired
    private WhgTraMapper whgTraMapper;

    @Autowired
    private WhgTraEnrolMapper whgTraEnrolMapper;

    @Autowired
    private WhgTraEnrolCourseMapper whgTraEnrolCourseMapper;


    public TrainCourseVO change(WhgTraCourse course, boolean canSee, String title)throws Exception{
        TrainCourseVO vo = new TrainCourseVO();
        if(course != null){
            Date now = new Date();
            Date stime = course.getStarttime();
            Date etime = course.getEndtime();

            //列表状态 0-未开始, 1-正在直播, 2-已结束
            Integer listState = 1;
            if(stime.after(now)){
                listState = 0;
            }else if(etime.before(now)){
                listState = 2;
            }

            //时间描述
            java.text.SimpleDateFormat sdf_1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
            java.text.SimpleDateFormat sdf_2 = new java.text.SimpleDateFormat("HH:mm");
            String timedesc = sdf_1.format(stime)+" - "+ sdf_2.format(etime);

            //当前用户能否直播
            vo.setCanSee(canSee);

            vo.setListState(listState);
            vo.setTitle(title);
            vo.setTimedesc(timedesc);
            vo.setStarttime(course.getStarttime());
            vo.setEndtime(course.getEndtime());
            vo.setId(course.getId());
            vo.setTraid(course.getTraid());
        }
        return vo;
    }

    public List<TrainCourseVO> change(List<WhgTraCourse> courses, boolean canSee, String title)throws Exception{
        List<TrainCourseVO> vos = new ArrayList<>();
        if(courses != null){
            for(WhgTraCourse course : courses){
                vos.add(change(course, canSee, title));
            }
        }
        return vos;
    }



    /**
     * 分页查询课程
     * @param
     * @return
     */
    public PageInfo t_srchList4p(int page,int rows,String id) throws Exception {
        Example example = new Example(WhgTraCourse.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid",id);
        c.andEqualTo("delstate",0);
        example.setOrderByClause("starttime asc");
        PageHelper.startPage(page,rows);
        List<WhgTraCourse> list = this.whgTraCourseMapper.selectByExample(example);
        PageInfo info = new PageInfo(list);
        return info;
    }

    /**
     * 编辑课程
     * @param course
     * @param sysUser
     */
    public void t_edit(WhgTraCourse course, WhgSysUser sysUser) throws Exception {

        this.whgTraCourseMapper.updateByPrimaryKeySelective(course);
    }

    /**
     * 添加课程
     * @param course
     * @param sysUser
     * @throws Exception
     */
    public void t_add(WhgTraCourse course, WhgSysUser sysUser) throws Exception {
        course.setId(IDUtils.getID());
        course.setCrtdate(new Date());
        course.setState(1);
        course.setCrtuser(sysUser.getId());
        course.setDelstate(0);
        this.whgTraCourseMapper.insert(course);
    }


    /**
     * 删除
     * @param id
     * @param user
     */
    public void t_del(String id, WhgSysUser user) throws Exception {
        WhgTraCourse course = whgTraCourseMapper.selectByPrimaryKey(id);
        if(course == null){
            return;
        }
        this.whgTraCourseMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUser
     * @return
     */
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser sysUser) {
        ResponseBean rb = new ResponseBean();
        if (ids == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("课程标识丢失");
            return rb;
        }
        Example example = new Example(WhgTraCourse.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgTraCourse course = new WhgTraCourse();
        course.setState(tostate);
        course.setStatemdfdate(new Date());
        course.setStatemdfuser(sysUser.getId());
        this.whgTraCourseMapper.updateByExampleSelective(course, example);
        return rb;
    }

    /**
     * 根据培训ID查找课程
     * @param id
     * @return
     */
    public List<WhgTraCourse> srchList(String id) throws Exception {
        WhgTra tra = this.whgTraMapper.selectByPrimaryKey(id);

        Example example = new Example(WhgTraCourse.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid",id);
        c.andEqualTo("state",1);
        c.andEqualTo("delstate",0);
        example.setOrderByClause("starttime asc");
        if(tra.getIsmultisite() == 0){
            PageHelper.startPage(1,1);
        }
        return this.whgTraCourseMapper.selectByExample(example);
    }


    public List<WhgTraCourse> srchCount(String traid) throws Exception {
        Example example = new Example(WhgTraCourse.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid",traid);
        return this.whgTraCourseMapper.selectByExample(example);
    }



    /**
     * 根据培训id查询课程表和报名信息
     * @param
     * @return
     */
    public ResponseBean t_srchLeaveList(Map param)throws Exception {
        ResponseBean rsb = new ResponseBean();
        List<Map> list = this.whgTraEnrolMapper.selCourseAndEnrol(param);
        if(list != null && list.size() > 0){
            rsb.setRows(list);
        }else {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setRows(new ArrayList());
        }
        return rsb;
    }

    /**
     * 根据ID查看课程信息
     * @param courseId
     * @return
     * @throws Exception
     */
    public WhgTraCourse t_srchOne(String courseId)throws Exception{
        return this.whgTraCourseMapper.selectByPrimaryKey(courseId);
    }

    /**
     * 保存签到信息
     * @param traid
     * @param userid
     * @param courseid
     * @param starttime
     * @param endtime
     */
    public ResponseBean t_sign(String traid,String enrolid, String userid, String courseid, String starttime, String endtime)throws Exception {
        ResponseBean rsb = new ResponseBean();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Example example = new Example(WhgTraEnrolCourse.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid",traid).andEqualTo("courseid",courseid).andEqualTo("userid",userid);
        List<WhgTraEnrolCourse> couList = this.whgTraEnrolCourseMapper.selectByExample(example);
        if(couList != null && couList.size() > 0){
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("已签到");
        }else {
            Long stime = new Long(starttime);
            String _stime = sdf.format(stime);
            Long etime = new Long(endtime);
            String _etime = sdf.format(etime);
            WhgTraEnrolCourse enrolcourse = new WhgTraEnrolCourse();
            enrolcourse.setId(IDUtils.getID());
            enrolcourse.setCourseid(courseid);
            enrolcourse.setTraid(traid);
            enrolcourse.setSign(1);
            enrolcourse.setUserid(userid);
            enrolcourse.setCoursestime(sdf.parse(_stime));
            enrolcourse.setCourseetime(sdf.parse(_etime));
            enrolcourse.setEnrolid(enrolid);
            enrolcourse.setSigntime(new Date());
            this.whgTraEnrolCourseMapper.insertSelective(enrolcourse);
        }
        return rsb;

    }

    /**
     * 查询请假的培训名称
     * @return
     */
    public ResponseBean t_srchTra4Leave() {
        ResponseBean rsb = new ResponseBean();
        List<Map> list = this.whgTraEnrolMapper.t_srchTra4Leave();
        if(list != null && list.size() > 0){
            rsb.setRows(list);
        }else {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setRows(new ArrayList());
        }
        return rsb;
    }

    /**
     * 根据培训id查询课程表和报名信息
     * @param traid
     * @return
     */
    public ResponseBean t_srchOne4Courseid(String traid,String courseid)throws Exception {
        ResponseBean rsb = new ResponseBean();
        List<Map> list = this.whgTraEnrolMapper.t_srchOne4Courseid(traid,courseid);
        if(list != null && list.size() > 0){
            rsb.setRows(list);
        }else {
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setRows(new ArrayList());
        }
        return rsb;
    }
}
