package com.creatoo.hn.services.admin.mylive;

import com.creatoo.hn.dao.mapper.WhgLiveCommMapper;
import com.creatoo.hn.dao.mapper.WhgTraCourseMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.activity.WhgActivityService;
import com.creatoo.hn.services.admin.train.WhgTrainCourseService;
import com.creatoo.hn.services.admin.train.WhgTrainService;
import com.creatoo.hn.util.CommUtil;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ImageUtil;
import com.creatoo.hn.util.enums.EnumState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 直播管理服务
 */
@Service
public class MyLiveService extends BaseService {
    /**
     * 活动服务
     */
    @Autowired
    private WhgActivityService whgActivityService;

    /**
     * 培训服务
     */
    @Autowired
    private WhgTrainService whgTrainService;

    /**
     * 培训课时服务
     */
    @Autowired
    private WhgTrainCourseService whgTrainCourseService;

    /**
     * whg_live_comm数据访问Mapper
     */
    @Autowired
    private WhgLiveCommMapper WhgLiveCommMapper;

    @Autowired
    private WhgTraCourseMapper whgTraCourseMapper;


    /**
     * 构造查询条件
     * @return Example
     * @throws Exception
     */
    public Example parseExample(WhgLiveComm whgLiveComm, String sort, String order)throws Exception{
        Example example = new Example(WhgLiveComm.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(whgLiveComm != null && whgLiveComm.getName() != null){
            c.andLike("name", "%"+whgLiveComm.getName()+"%");
            whgLiveComm.setName(null);
        }

        //部门
        if(whgLiveComm != null && StringUtils.isNotEmpty(whgLiveComm.getDeptid())){
            String deptids = whgLiveComm.getDeptid();
            String[] deptidArr = deptids.split(",");
            if(deptidArr.length > 1){
                c.andIn("deptid", Arrays.asList(deptidArr));
                whgLiveComm.setDeptid(null);
            }
        }

        //其它条件
        c.andEqualTo(whgLiveComm);

        //排序
        if(StringUtils.isEmpty(sort)){
            sort = "starttime";
            order = "desc";
        }
       this.setOrder(example, sort, order, "crtdate");

        return example;
    }

    /**
     * 分页查询直播列表
     * @param whgSysUser 会话用户
     * @param whgLiveComm 查询直播对象
     * @param page 第几页
     * @param rows 每页数
     * @param sort 排序字段
     * @param order 排序方式
     * @return PageInfo<WhgLiveComm>
     * @throws Exception
     */
    public PageInfo<WhgLiveComm> t_srchList4p(WhgSysUser whgSysUser, WhgLiveComm whgLiveComm, int page, int rows, String sort, String order)throws Exception{
        whgLiveComm.setCrtuser(whgSysUser.getId());
        //搜索条件
        Example example=null ;
        if("2".equals(whgLiveComm.getAddtype())||"3".equals(whgLiveComm.getAddtype())){
            example = new Example(WhgLiveComm.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("crtuser",whgSysUser.getId());
            criteria.andEqualTo("addtype",whgLiveComm.getAddtype());
        }else {
             example = this.parseExample(whgLiveComm, sort, order);
        }



        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgLiveComm> list = this.WhgLiveCommMapper.selectByExample(example);
        PageInfo<WhgLiveComm> pageInfo = new PageInfo<WhgLiveComm>(list);
        return pageInfo;
    }

    /**
     * List<Bean>转List<Map>
     * @param list
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> changeListToMap(List<WhgLiveComm> list)throws Exception{
        List<Map<String, Object>> myList = new ArrayList();
        for(WhgLiveComm live : list){
            Map<String, Object> map = CommUtil.transBean2Map(live);
            String reftitle = this.getRefTitle(live, null);
            map.put("reftitle", reftitle);
            myList.add(map);
        }
        return myList;
    }

    /**
     * 获取直播关系的活动或者培训的名称
     * @param live
     * @return
     * @throws Exception
     */
    public String getRefTitle(WhgLiveComm live, Map<String, String> params)throws Exception{
        String reftitle = "";
        if(live != null){
            java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
            if(live.getReftype() == 1){//活动
                WhgActivity act = this.whgActivityService.t_srchOne(live.getRefid());
                reftitle = act.getName();
                if(params != null){
                    params.put("limitStartTime", sdf2.format(act.getStarttime())+" 00:00:00");
                    params.put("limitEndTime", sdf2.format(act.getEndtime())+" 23:59:59");
                }
            }else{//培训
                WhgTra tra = this.whgTrainService.srchOne(live.getRefid());
                reftitle = tra.getTitle();
                if(params != null){
                    params.put("limitStartTime", sdf1.format(tra.getStarttime()));
                    params.put("limitEndTime", sdf1.format(tra.getEndtime()));
                }
            }
        }
        return reftitle;
    }

    /**
     * 通过直播信息获取课程标题
     * @param live
     * @return
     * @throws Exception
     */
    public String getCourseTitle(WhgLiveComm live)throws Exception{
        String courseTitle = "";
        if(live != null && StringUtils.isNotEmpty(live.getCourseid())){
            WhgTraCourse course = this.whgTrainCourseService.t_srchOne(live.getCourseid());
            if(course != null && StringUtils.isNotEmpty(course.getTitle())){
                courseTitle = course.getTitle();
            }else{
                WhgTra tra = this.whgTrainService.srchOne(live.getRefid());
                courseTitle = tra.getTitle();
            }
        }
        return courseTitle;
    }

    /**
     * 根据主键查询直播记录
     * @param id
     * @return
     * @throws Exception
     */
    public WhgLiveComm t_srchOne(String id)throws Exception{
        return this.WhgLiveCommMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据培训ID和课时ID查询直播记录
     * @param traid 培训标识
     * @param courseid 课时标识
     * @return
     * @throws Exception
     */
    public WhgLiveComm t_srchOneByTraidAndCourseid(String traid, String courseid)throws Exception{
        WhgLiveComm live = new WhgLiveComm();
        live.setReftype(3);
        live.setRefid(traid);
        live.setCourseid(courseid);
        return this.WhgLiveCommMapper.selectOne(live);
    }

    /**
     * 根据线下培训标识查询直播信息
     * @param traid 线下培训标识
     * @return WhgLiveComm直播信息
     * @throws Exception
     */
    public WhgLiveComm t_srchOneByTraid(String traid)throws Exception{
        WhgLiveComm live = new WhgLiveComm();
        live.setReftype(2);
        live.setRefid(traid);
        live.setState(EnumState.STATE_YES.getValue());
        return this.WhgLiveCommMapper.selectOne(live);
    }

    /**
     * 根据活动标识查询直播信息
     * @param actid 活动标识
     * @return WhgLiveComm直播信息
     * @throws Exception
     */
    public WhgLiveComm t_srchOneByActid(String actid)throws Exception{
        WhgLiveComm live = new WhgLiveComm();
        live.setReftype(1);
        live.setRefid(actid);
        live.setState(EnumState.STATE_YES.getValue());
        return this.WhgLiveCommMapper.selectOne(live);
    }

    /**
     * 修改回看地址
     * @param id 直播标识
     * @param enturl 回看地址
     * @throws Example
     */
    public void t_updateEntUrl(String id, String enturl)throws Exception{
        WhgLiveComm live = new WhgLiveComm();
        live.setId(id);
        live.setEnturl(enturl);
        this.WhgLiveCommMapper.updateByPrimaryKeySelective(live);
    }

    /**
     * 根据直播信息转换成前端可展示的信息
     * @param live 直播对象
     * @return Map{title:'直播标题', time:'直播时间', state:'1-正在进行, 2-未开始， 3-已结束， 4-回看', url:'直播地址'}
     * @throws Exception
     */
    public Map<String, String> findActOrTraLiveInfo(WhgLiveComm live)throws Exception{
        Map<String, String> liveMap = new HashMap<>();
        if(live != null){
            liveMap.put("hasLive", "1");
            Date st = live.getStarttime();
            Date et = live.getEndtime();
            Date now = new Date();
            java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
            java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("HH:mm");
            String time = sdf1.format(st)+"-"+sdf2.format(et);
            String title = live.getName();
            String state = "1";// 1-正在进行, 2-未开始， 3-已结束， 4-回看
            String backAddr = live.getEnturl();
            String playAddr = live.getPlayaddr();
            String advanceAddr=live.getAdvancevideoaddr();
            String addType=live.getAddtype();
            playAddr = playAddr.replaceAll("m3u8", "flv");
            if(now.after(et)){//已结束
                if(StringUtils.isEmpty(backAddr)){
                    backAddr = live.getLiveaddr();
                }
                if(StringUtils.isEmpty(backAddr)){
                    state = "3";//已结束
                }else{
                    state = "4";//回看
                    playAddr = backAddr;
                }
            }else if(st.after(now)){
                state = "2";//未开始
            }else{
                state = "1";//正在进行
            }

            liveMap.put("state", state);
            liveMap.put("time", time);
            liveMap.put("title", title);
            liveMap.put("url", playAddr);
            liveMap.put("advanceAddr", advanceAddr);
            liveMap.put("addType", addType);
        }else{
            liveMap.put("hasLive", "0");
        }
        return liveMap;
    }

    /**
     * 添加直播
     * @param sysUser
     * @param live
     * @throws Exception
     */
    public void add(WhgSysUser sysUser, WhgLiveComm live)throws Exception{
        live.setId(IDUtils.getID32());
        live.setState(EnumState.STATE_NO.getValue());
        live.setCrtdate(new Date());
        live.setCrtuser(sysUser.getId());
        this.WhgLiveCommMapper.insert(live);
        //验证能否启用
        validLive(live, null,live.getAddtype());
    }


    /**
     * 设置一体机首页视频
     * @param resurl
     * @throws Exception
     */
    public void setYTJLive(String resurl, String resname, String respicture)throws Exception{
        WhgLiveComm live = new WhgLiveComm();
        live.setReftype(999999);//这个99999表示是一体机首页视频
        live = this.WhgLiveCommMapper.selectOne(live);

        if(StringUtils.isNotEmpty(respicture)){
            respicture = ImageUtil.getImgName_750_500(respicture);
        }
        if(live != null && StringUtils.isNotEmpty(live.getId())){//存在
            live.setEnturl(resurl);
            live.setName(resname);
            live.setPlayaddr(respicture);//封面图，只在系统管理中配置时有用
            this.WhgLiveCommMapper.updateByPrimaryKeySelective(live);
        }else{//不存在
            live = new WhgLiveComm();
            live.setId(IDUtils.getID32());
            live.setReftype(999999);
            live.setEnturl(resurl);
            live.setName(resname);
            live.setPlayaddr(respicture);//封面图，只在系统管理中配置时有用
            this.WhgLiveCommMapper.insert(live);
        }
    }

    /**
     * 查询一体机首页视频配置
     * @return
     * @throws Exception
     */
    public WhgLiveComm getYTJLive()throws Exception{
        String url = "";
        WhgLiveComm live = new WhgLiveComm();
        live.setReftype(999999);//这个99999表示是一体机首页视频
        live = this.WhgLiveCommMapper.selectOne(live);
        if(live != null && StringUtils.isNotEmpty(live.getId())){//存在
            return live;
        }
        return null;
    }

    /**
     * 编辑直播
     * @param sysUser
     * @param live
     * @throws Exception
     */
    public void edit(WhgSysUser sysUser, WhgLiveComm live, String editVod)throws Exception{
        this.WhgLiveCommMapper.updateByPrimaryKeySelective(live);
        live = this.WhgLiveCommMapper.selectByPrimaryKey(live.getId());
        //验证能否启用
        validLive(live, editVod);
    }

    /**
     * 启用停用
     * @param sysUser
     * @param id
     * @param state
     * @throws Exception
     */
    public void updateState(WhgSysUser sysUser, String id, String state)throws Exception{
        WhgLiveComm live = new WhgLiveComm();
        live.setId(id);
        live.setState(Integer.parseInt(state));
        this.WhgLiveCommMapper.updateByPrimaryKeySelective(live);
        live=this.WhgLiveCommMapper.selectByPrimaryKey(id);

        Example example = new Example(WhgTraCourse.class);
        example.createCriteria()
                .andEqualTo("id", live.getCourseid());
        WhgTraCourse course = new WhgTraCourse();
        course.setState(Integer.parseInt(state));
        course.setStatemdfdate(new Date());
        course.setStatemdfuser(sysUser.getId());
        this.whgTraCourseMapper.updateByExampleSelective(course, example);
        if((EnumState.STATE_YES.getValue()+"").equals(state)){
            //验证能否启用
            validLive(this.t_srchOne(id), "1");
        }
    }

    /**
     * 验证直播数据是否有效
     * @param live
     * @throws Exception
     */
    public void validLive(WhgLiveComm live, String editVod)throws Exception{
        //0 开始时间必须大于当前时间10分钟以上
        Date stime = live.getStarttime();
        Date now = new Date();
        //if((stime.getTime()-now.getTime())<1000*60*60*2){
        if(!"1".equals(editVod)){
            if((stime.getTime()-now.getTime())<1){
                throw new Exception("直播开始时间必须大于当前时间");
            }
        }

        //1 直播名称不能重复
        Example example = new Example(WhgLiveComm.class);
        example.createCriteria().andEqualTo("name", live.getName()).andEqualTo("reftype", live.getReftype());
        int cnt = this.WhgLiveCommMapper.selectCountByExample(example);
        if(cnt > 1){
            throw new Exception("直播标题重复");
        }

        //2 直播关联活动|线下培训|在线培训课时不能得重复
        example.clear();
        if(live.getReftype() == 1 || live.getReftype() == 2){
            example.createCriteria().andEqualTo("refid", live.getRefid()).andEqualTo("reftype", live.getReftype());
            cnt = this.WhgLiveCommMapper.selectCountByExample(example);
            if(cnt > 1){
                String hit = live.getReftype() == 1 ? "一场活动" : "一场线下培训";
                throw new Exception(hit+"只能配置一场直播");
            }
        }else{
            example.createCriteria().andEqualTo("refid", live.getRefid())
                    .andEqualTo("reftype", live.getReftype())
                    .andEqualTo("courseid", live.getCourseid());
            cnt = this.WhgLiveCommMapper.selectCountByExample(example);
            if(cnt > 1){
                throw new Exception("在线课程一个课时只能配置一场直播");
            }
        }

        //3 直播流不能冲突,时间不能冲突
        if("2".equals(live.getAddtype())||"3".equals(live.getAddtype())){

            }else {
            example.clear();
            example.createCriteria().andEqualTo("streamname", live.getStreamname()).andGreaterThan("starttime", live.getStarttime()).andLessThan("endtime", live.getEndtime()).andEqualTo("state", EnumState.STATE_YES.getValue());
            example.or(example.createCriteria().andEqualTo("streamname", live.getStreamname()).andLessThan("starttime", live.getStarttime()).andGreaterThan("endtime", live.getStarttime()).andEqualTo("state", EnumState.STATE_YES.getValue()));
            example.or(example.createCriteria().andEqualTo("streamname", live.getStreamname()).andLessThan("starttime", live.getEndtime()).andGreaterThan("endtime", live.getEndtime()).andEqualTo("state", EnumState.STATE_YES.getValue()));
            List<WhgLiveComm> list = this.WhgLiveCommMapper.selectByExample(example);
            if(list != null && list.size() > 0){
                WhgLiveComm t_live = list.get(0);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                throw new Exception("“直播时间”或者“推流线路”冲突，请重新设置。发生冲突的直播：【名称："+t_live.getName()+"】【直播时间："+sdf.format(t_live.getStarttime())+" 至 "+sdf.format(t_live.getEndtime())+"】【推流线路："+t_live.getStreamname()+"】");
            }
        }

    }


    /**
     * 验证直播数据是否有效
     * @param live
     * @throws Exception
     */
    public void validLive(WhgLiveComm live, String editVod,String addType)throws Exception{
        //0 开始时间必须大于当前时间10分钟以上
            Date stime = live.getStarttime();
            Date now = new Date();
            //if((stime.getTime()-now.getTime())<1000*60*60*2){
            if(!"1".equals(editVod)){
                if((stime.getTime()-now.getTime())<1){
                    throw new Exception("直播开始时间必须大于当前时间");
                }
            }

        //1 直播名称不能重复
        Example example = new Example(WhgLiveComm.class);
        example.createCriteria().andEqualTo("name", live.getName()).andEqualTo("reftype", live.getReftype());
        int cnt = this.WhgLiveCommMapper.selectCountByExample(example);
        if(cnt > 1){
            throw new Exception("直播标题重复");
        }


        //2 直播关联活动|线下培训|在线培训课时不能得重复
            example.clear();
            if(live.getReftype() == 1 || live.getReftype() == 2){
                example.createCriteria().andEqualTo("refid", live.getRefid()).andEqualTo("reftype", live.getReftype());
                cnt = this.WhgLiveCommMapper.selectCountByExample(example);
                if(cnt > 1){
                    String hit = live.getReftype() == 1 ? "一场活动" : "一场线下培训";
                    throw new Exception(hit+"只能配置一场直播");
                }
            }else{
                example.createCriteria().andEqualTo("refid", live.getRefid())
                        .andEqualTo("reftype", live.getReftype())
                        .andEqualTo("courseid", live.getCourseid());
                cnt = this.WhgLiveCommMapper.selectCountByExample(example);
                if(cnt > 1){
                    throw new Exception("在线课程一个课时只能配置一场直播");
                }
            }


        //3 直播流不能冲突,时间不能冲突
        if("1".equals(live.getAddtype())){
            example.clear();
            example.createCriteria().andEqualTo("streamname", live.getStreamname()).andGreaterThan("starttime", live.getStarttime()).andLessThan("endtime", live.getEndtime()).andEqualTo("state", EnumState.STATE_YES.getValue());
            example.or(example.createCriteria().andEqualTo("streamname", live.getStreamname()).andLessThan("starttime", live.getStarttime()).andGreaterThan("endtime", live.getStarttime()).andEqualTo("state", EnumState.STATE_YES.getValue()));
            example.or(example.createCriteria().andEqualTo("streamname", live.getStreamname()).andLessThan("starttime", live.getEndtime()).andGreaterThan("endtime", live.getEndtime()).andEqualTo("state", EnumState.STATE_YES.getValue()));
            List<WhgLiveComm> list = this.WhgLiveCommMapper.selectByExample(example);
            if(list != null && list.size() > 0){
                WhgLiveComm t_live = list.get(0);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                throw new Exception("“直播时间”或者“推流线路”冲突，请重新设置。发生冲突的直播：【名称："+t_live.getName()+"】【直播时间："+sdf.format(t_live.getStarttime())+" 至 "+sdf.format(t_live.getEndtime())+"】【推流线路："+t_live.getStreamname()+"】");
            }
        }

    }
    /**
     * 删除
     * @param sysUser
     * @param id
     * @throws Exception
     */
    public void delete(WhgSysUser sysUser, String id)throws Exception{
        this.WhgLiveCommMapper.deleteByPrimaryKey(id);
    }
}
