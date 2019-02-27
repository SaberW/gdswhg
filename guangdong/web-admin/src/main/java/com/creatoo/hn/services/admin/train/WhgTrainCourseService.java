package com.creatoo.hn.services.admin.train;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.mapper.WhgTraCourseMapper;
import com.creatoo.hn.mapper.WhgTraEnrolCourseMapper;
import com.creatoo.hn.mapper.WhgTraEnrolMapper;
import com.creatoo.hn.mapper.WhgTraMapper;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.comm.CommService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private CommService commService;

    @Autowired
    private WhgTraEnrolCourseMapper whgTraEnrolCourseMapper;
    @Autowired
    private WhgTrainService whgTrainService;
    /**
     * 分页查询课程
     * @param request
     * @return
     */
    public PageInfo t_srchList4p(HttpServletRequest request) throws Exception {
        int page = Integer.parseInt((String)request.getParameter("page"));
        int rows = Integer.parseInt((String)request.getParameter("rows"));
        String id = request.getParameter("id");
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
     * 新增课程
     * @param course
     * @param sysUser
     */
    public void t_add(WhgTra tra,WhgTraCourse course, WhgSysUser sysUser) throws Exception {
        whgTrainService.saveCourse(tra, sysUser, course);
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
        example.setOrderByClause("starttime asc");
        if(tra.getIsmultisite() != 1){
            PageHelper.startPage(1,1);
        }
        return this.whgTraCourseMapper.selectByExample(example);
    }

    /**
     * 查看是否存在报名记录
     * @param traid 培训ID
     * @return
     */
    public int selCountEnroll(String traid) {
        Example example = new Example(WhgTraEnrol.class);
        example.createCriteria().andEqualTo("traid", traid);
        return this.whgTraEnrolMapper.selectCountByExample(example);
    }

    /**
     * 根据培训id查询课程表和报名信息
     * @param traid
     * @return
     */
    public ResponseBean t_srchOne(String traid,String userid) {
        ResponseBean rsb = new ResponseBean();
        List<Map> list = this.whgTraEnrolMapper.selCourseAndEnrol(traid,userid);
//        Example example = new Example(WhgTraCourse.class);
//        Example.Criteria c = example.createCriteria();
//        c.andEqualTo("traid",traid).andEqualTo("state",1);
//        List<WhgTraCourse> courseList = this.whgTraCourseMapper.selectByExample(example);
        rsb.setRows(list);
        return rsb;
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
            enrolcourse.setId(commService.getKey("whg_tra_enrol_course"));
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
}
