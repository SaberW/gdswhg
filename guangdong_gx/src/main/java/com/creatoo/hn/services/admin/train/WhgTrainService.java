package com.creatoo.hn.services.admin.train;


import com.creatoo.hn.dao.mapper.WhgMajorContactMapper;
import com.creatoo.hn.dao.mapper.WhgSysCultMapper;
import com.creatoo.hn.dao.mapper.WhgTraCourseMapper;
import com.creatoo.hn.dao.mapper.WhgTraMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.user.WhgBlackListService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.WeekDayUtil;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/3/21.
 */
@SuppressWarnings("ALL")
@Service
public class WhgTrainService extends BaseService{
    /**
     * 培训mapper
     */
    @Autowired
    private WhgTraMapper whgTraMapper;

    /**
     * 课程mapper
     */
    @Autowired
    private WhgTraCourseMapper whgTraCourseMapper;

    @Autowired
    private WhgMajorContactMapper whgMajorContactMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgBlackListService whgBlackListService;

    @Autowired
    private WhgSysCultMapper whgSysCultMapper;

    @Autowired
    private WhgXjReasonService whgXjReasonService;

    /**
     * 分页查询培训
     * @param page
     * @param rows
     * @param pageType  页面类型
     * @param whgTra  培训
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public PageInfo t_srchList4p(int page, int rows, String pageType,WhgTra whgTra, String sort, String order,String Sysuserid) throws Exception {
        Example example = new Example(WhgTra.class);
        Example.Criteria c = example.createCriteria();

        if ("edit".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(1,2,6,9,4));
            c.andEqualTo("crtuser",Sysuserid);
        }
        //审核列表，查 9待审核
        if ("check".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(2,6,9,4));
            whgTra.setCrtuser(null);
        }
        //发布列表，查 2待发布 6已发布 4已下架
        if ("publish".equalsIgnoreCase(pageType)){
            c.andIn("state",  Arrays.asList(2,6,9,4));
            whgTra.setCrtuser(null);
        }
        //删除列表，查已删除 否则查未删除的
        if ("del".equalsIgnoreCase(pageType)){
            c.andEqualTo("delstate", 1);
        }else{
            c.andEqualTo("delstate", 0);
        }
        if (whgTra.getTitle()!=null){
            c.andLike("title", "%"+whgTra.getTitle()+"%");
            whgTra.setTitle(null); //去除title等于条件
        }

        if (whgTra.getCultid() == null || whgTra.getCultid().isEmpty()) {
            whgTra.setCultid(null);
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(Sysuserid);
                if (cultids!=null && cultids.size()>0){
                    c.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (whgTra.getDeptid() == null || whgTra.getDeptid().isEmpty()) {
            whgTra.setDeptid(null);
            try {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(Sysuserid);
                if (deptids != null && deptids.size() > 0) {
                    c.andIn("deptid", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        whgTra.setIslive(0);
        whgTra.setBiz("PT");
        c.andEqualTo(whgTra);

        //排序
        if (sort!=null && !sort.isEmpty()){
            if (order!=null && "asc".equalsIgnoreCase(order)){
                example.orderBy(sort).asc();
            }else{
                example.orderBy(sort).desc();
            }
        }else{
            example.setOrderByClause(" crtdate desc");
        }

        PageHelper.startPage(page, rows);
        List list= this.whgTraMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }



    public List t_srchList(WhgTra whgTra, String bmEnd) throws Exception {
        Example example = new Example(WhgTra.class);
        Example.Criteria c = example.createCriteria();
        whgTra.setIslive(0);
        whgTra.setBiz("PT");
        c.andEqualTo(whgTra);
        if (bmEnd != null && !bmEnd.isEmpty()){
            c.andGreaterThanOrEqualTo("enrollendtime", new Date());
        }
        example.setOrderByClause(" crtdate desc");
        List list= this.whgTraMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据主键查询培训
     * @param id
     * @return
     */

    public WhgTra srchOne(String id) throws Exception{
        return whgTraMapper.selectByPrimaryKey(id);
    }

    /**
     *添加培训
     * @param tra
     * @param user
     */
    public ResponseBean t_add(WhgTra tra, WhgSysUser user, String _age1,String _age2,String sin_starttime,String sin_endtime,String[] _starttime,String[] _endtime,String[] fixedweek,String fixedstarttime,String fixedendtime,String mid, String[] majorid) throws Exception {
        ResponseBean res = new ResponseBean();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(_age1 != null && !"".equals(_age1) && _age2 != null && !"".equals(_age2)){
            tra.setAge(_age1+","+_age2);
        }
        Date now = new Date();
        if (tra.getId()==null || tra.getId().isEmpty()){
            tra.setId(IDUtils.getID());  //id
        }
        tra.setCrtdate(now);  //创建时间
        tra.setUpindex(0);   //上首页
        tra.setCrtuser(user.getId());   //创建人
        //tra.setCultid(user.getCultid());
        //tra.setDeptid(user);
        tra.setDelstate(0);   //删除状态
        tra.setState(1);  //编辑状态
        tra.setIslive(0);
        if(tra.getBiz() == null || "".equals(tra.getBiz())){
            tra.setBiz("PT");
        }

        tra.setStatemdfdate(now);  //状态时间
        tra.setStatemdfuser(user.getId());   //修改状态的人
        tra.setRecommend(0);  //是否推荐，默认不推荐
        if(sin_starttime != null && !"".equals(sin_starttime) && sin_endtime != null && !"".equals(sin_endtime)){
            tra.setStarttime(sdf.parse(sin_starttime));
            tra.setEndtime(sdf.parse(sin_endtime));
        }
        this.whgTraMapper.insertSelective(tra);
        //

        WhgTraCourse traCourse = new WhgTraCourse();
        int i;

        if(tra.getIsmultisite() == 1){
            if(_starttime != null && !"".equals(_starttime) && _endtime != null && !"".equals(_endtime)){
                for(int j = 0; j<_starttime.length; j++){
                    traCourse.setEndtime(sdf.parse(_endtime[j]));
                    traCourse.setStarttime(sdf.parse(_starttime[j]));
                    saveCourse(tra, user, traCourse, j);
                }
            }
        }else if(tra.getIsmultisite() == 2){
            if(fixedweek != null && !"".equals(fixedweek)){
                int courseIdx = 0;
                List<Integer> daysOfOneWeek = new ArrayList<Integer>();
                for( i=0; i<fixedweek.length; i++){
                    //List<Integer> daysOfOneWeek = new ArrayList<Integer>();
                    daysOfOneWeek.add(Integer.parseInt(fixedweek[i]));
                    /*List<String> daysNeedBookList = WeekDayUtil.getDates(sdf.format(tra.getStarttime()), sdf.format(tra.getEndtime()), daysOfOneWeek);
                    if(daysNeedBookList.size() > 0){
                        for (String s : daysNeedBookList) {
                            if(fixedstarttime != null && !"".equals(fixedstarttime) && fixedendtime != null && !"".equals(fixedendtime)){
                                String a = s+" "+fixedstarttime+":00";
                                String b = s+" "+fixedendtime+":00";
                                traCourse.setStarttime(sdf.parse(a));
                                traCourse.setEndtime(sdf.parse(b));
                                saveCourse(tra, user, traCourse, courseIdx);
                                courseIdx++;
                            }
                        }
                    }else{
                        res.setSuccess(ResponseBean.FAIL);
                        res.setErrormsg("您选择的周几在时段内没有,请重新选择.");
                        this.whgTraMapper.deleteByPrimaryKey(tra.getId());
                        return res;
                    }*/

                }
                List<String> daysNeedBookList = WeekDayUtil.getDates(sdf.format(tra.getStarttime()), sdf.format(tra.getEndtime()), daysOfOneWeek);
                if(daysNeedBookList.size() > 0){
                    for (String s : daysNeedBookList) {
                        if(fixedstarttime != null && !"".equals(fixedstarttime) && fixedendtime != null && !"".equals(fixedendtime)){
                            String a = s+" "+fixedstarttime+":00";
                            String b = s+" "+fixedendtime+":00";
                            traCourse.setStarttime(sdf.parse(a));
                            traCourse.setEndtime(sdf.parse(b));
                            saveCourse(tra, user, traCourse, courseIdx);
                            courseIdx++;
                        }
                    }
                }else{
                    res.setSuccess(ResponseBean.FAIL);
                    res.setErrormsg("您选择的周几在时段内没有,请重新选择.");
                    this.whgTraMapper.deleteByPrimaryKey(tra.getId());
                    return res;
                }
            }
            //saveCourse(tra, user, traCourse);
        }else{
            traCourse.setStarttime(sdf.parse(sin_starttime));
            traCourse.setEndtime(sdf.parse(sin_endtime));
            saveCourse(tra, user, traCourse, 0);
        }
        if(mid != null && !"".equals(mid)){
            WhgMajorContact contact = new WhgMajorContact();
            contact.setId(IDUtils.getID());
            contact.setEntid(tra.getId());
            contact.setMajorid(mid);
            contact.setType(1);
            this.whgMajorContactMapper.insertSelective(contact);
        }
        if(majorid != null && majorid.length > 0){
            WhgMajorContact contact = new WhgMajorContact();
            for (int m = 0;m < majorid.length;m++){
                contact.setId(IDUtils.getID());
                contact.setEntid(tra.getId());
                contact.setMajorid(majorid[m]);
                contact.setType(1);
                this.whgMajorContactMapper.insertSelective(contact);
            }
        }
        return res;
    }



    /**
     * 保存课程表
     * @param tra
     * @param user
     * @throws Exception
     */
    private void saveCourse(WhgTra tra, WhgSysUser user, WhgTraCourse traCourse, Integer index) throws Exception{
        Date now = new Date();
        traCourse.setId(IDUtils.getID());
        traCourse.setTraid(tra.getId());
        traCourse.setDelstate(0);
        traCourse.setCrtuser(user.getId());
        traCourse.setCrtdate(now);
        traCourse.setState(1);
        traCourse.setStatemdfdate(now);
        traCourse.setStatemdfuser(user.getId());

        String title = tra.getTitle()+"第"+(index+1)+"课";
        traCourse.setTitle(title);
        this.whgTraCourseMapper.insertSelective(traCourse);
    }

    public void t_editTra4id(WhgTra info) throws Exception{
        this.whgTraMapper.updateByPrimaryKeySelective(info);
    }

    /**
     *编辑培训
     * @param tra
     * @param sysUser
     */
    public ResponseBean t_edit(WhgTra tra, WhgSysUser sysUser, String _age1,String _age2,String sin_starttime,String sin_endtime,String[] _starttime,String[] _endtime,String[] fixedweek,String fixedstarttime,String fixedendtime,String starttime,String endtime,String[] majorid) throws Exception {
        ResponseBean res = new ResponseBean();
        if(_age1 != null && !"".equals(_age1) && _age2 != null && !"".equals(_age2)){
//            String _age1 = request.getParameter("age1");
//            String _age2 = request.getParameter("age2");
            tra.setAge(_age1+","+_age2);
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(tra.getIsmultisite() != null && tra.getIsmultisite() == 0 && sin_starttime != null && sin_endtime != null){
            tra.setStarttime(sdf.parse(sin_starttime));
            tra.setEndtime(sdf.parse(sin_endtime));
        }
        this.whgTraMapper.updateByPrimaryKeySelective(tra);
        WhgTra traValue = this.whgTraMapper.selectByPrimaryKey(tra.getId());
        if(traValue != null && traValue.getState().intValue() != 4){
            String traid = tra.getId();
            Example example = new Example(WhgTraCourse.class);
            example.createCriteria().andEqualTo("traid",traid);
            this.whgTraCourseMapper.deleteByExample(example);

            WhgTraCourse traCourse = new WhgTraCourse();
            if(tra.getIsmultisite() != null && tra.getIsmultisite() == 0 && sin_starttime != null && sin_endtime != null){
                traCourse.setStarttime(sdf.parse(sin_starttime));
                traCourse.setEndtime(sdf.parse(sin_endtime));
                saveCourse(tra, sysUser, traCourse, 0);
            }
            if(tra.getIsmultisite() != null && tra.getIsmultisite() == 1){
                if(_starttime != null && _endtime != null){
                    for(int j = 0; j<_starttime.length; j++){
    //                    String _starttime = (String) request.getParameterValues("_starttime")[j];
    //                    String _endtime = (String) request.getParameterValues("_endtime")[j];
                        if(_endtime[j] != null && _starttime[j] != null){
                            traCourse.setEndtime(sdf.parse(_endtime[j]));
                            traCourse.setStarttime(sdf.parse(_starttime[j]));
                            saveCourse(tra, sysUser, traCourse, j);
                        }
                    }
                }
            }
            if(tra.getIsmultisite() != null && tra.getIsmultisite() == 2 && starttime != null && endtime != null){
    //            String starttime = (String)request.getParameter("starttime");
    //            String endtime = (String)request.getParameter("endtime");
                if(fixedweek != null){
                    int courseIdx = 0;
                    List<Integer> daysOfOneWeek = new ArrayList<Integer>();
                    for(int i=0; i<fixedweek.length; i++){
                        //List<Integer> daysOfOneWeek = new ArrayList<Integer>();
                        daysOfOneWeek.add(Integer.parseInt(fixedweek[i]));
                        /*List<String> daysNeedBookList = WeekDayUtil.getDates(starttime, endtime, daysOfOneWeek);
                        if(daysNeedBookList.size() > 0) {
                            for (String s : daysNeedBookList) {
                                if (fixedstarttime != null && fixedendtime != null) {
                                    String a = s + " " + fixedstarttime + ":00";
                                    String b = s + " " + fixedendtime + ":00";
                                    traCourse.setStarttime(sdf.parse(a));
                                    traCourse.setEndtime(sdf.parse(b));
                                    saveCourse(tra, sysUser, traCourse, courseIdx);
                                    courseIdx++;
                                }
                            }
                        }else{
                            res.setSuccess(ResponseBean.FAIL);
                            res.setErrormsg("您选择的周几在时段内没有,请重新选择.");
                            return res;
                        }*/
                    }
                    List<String> daysNeedBookList = WeekDayUtil.getDates(starttime, endtime, daysOfOneWeek);
                    if(daysNeedBookList.size() > 0) {
                        for (String s : daysNeedBookList) {
                            if (fixedstarttime != null && fixedendtime != null) {
                                String a = s + " " + fixedstarttime + ":00";
                                String b = s + " " + fixedendtime + ":00";
                                traCourse.setStarttime(sdf.parse(a));
                                traCourse.setEndtime(sdf.parse(b));
                                saveCourse(tra, sysUser, traCourse, courseIdx);
                                courseIdx++;
                            }
                        }
                    }else{
                        res.setSuccess(ResponseBean.FAIL);
                        res.setErrormsg("您选择的周几在时段内没有,请重新选择.");
                        return res;
                    }
                }
            }
        }

        //更新微专业关联表
        WhgMajorContact contact = new WhgMajorContact();
        Example majorexample = new Example(WhgMajorContact.class);
        Example.Criteria majorc = majorexample.createCriteria();
        majorc.andEqualTo("entid",tra.getId());
        List<WhgMajorContact> mclist = whgMajorContactMapper.selectByExample(majorexample);
        for(int c = 0; c<mclist.size();c++){
            whgMajorContactMapper.deleteByPrimaryKey(mclist.get(c).getId());
        }
        if(majorid != null && majorid.length > 0){
            for (int m = 0;m < majorid.length;m++){
                if(mclist == null || mclist.size() == 0){
                    contact.setId(IDUtils.getID());
                    contact.setEntid(tra.getId());
                    contact.setMajorid(majorid[m]);
                    contact.setType(1);
                    this.whgMajorContactMapper.insertSelective(contact);
                }else {
                    contact.setId(IDUtils.getID());
                    contact.setEntid(tra.getId());
                    contact.setMajorid(majorid[m]);
                    contact.setType(1);
                    this.whgMajorContactMapper.insertSelective(contact);
                }
            }
        }
        return res;
    }

    /**
     * 删除培训
     * @param id
     * @param
     */
    public void t_del(String id) throws Exception {
        WhgTra tra = whgTraMapper.selectByPrimaryKey(id);
        if (tra == null){
            return;
        }
        if (tra.getDelstate()!=null && tra.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgTraMapper.deleteByPrimaryKey(id);
        }else if(tra.getState() == 1){
            this.whgTraMapper.deleteByPrimaryKey(id);
        }else {
            tra.setDelstate(1);
            this.whgTraMapper.updateByPrimaryKeySelective(tra);
        }
    }

    /**
     * 改变状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param user
     * @return
     */
    public ResponseBean t_updstate(String statemdfdate, String ids, String formstates, int tostate, WhgSysUser user, String reason, int issms) throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ResponseBean rb = new ResponseBean();
        Date now = new Date();
        if (ids == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("培训主键丢失");
            return rb;
        }
        Example example = new Example(WhgTra.class);
        Example.Criteria c = example.createCriteria();
        if (formstates != null && !formstates.isEmpty()) {
            c.andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ));
        }
        c.andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgTra tra = new WhgTra();
        tra.setState(tostate);
        if(statemdfdate != null && !"".equals(statemdfdate)){
            tra.setStatemdfdate(sdf.parse(statemdfdate));
        }else{
            tra.setStatemdfdate(now);
        }

        tra.setStatemdfuser(user.getId());
        if(tostate == 2){
            tra.setCheckor(user.getId());
            tra.setCheckdate(now);

        }else if(tostate == 6){
            tra.setPublisher(user.getId());
            tra.setPublishdate(now);
        }else if(tostate == 1){
            tra.setCheckor(user.getId());
            tra.setPublishdate(now);
        }

        try {
            if (reason!=null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()){
                List<WhgTra> srclist = this.whgTraMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgTra _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("线下培训");
                        xjr.setFktitile(_src.getTitle());
                        xjr.setCrtuser(user.getId());
                        xjr.setCrtdate(new Date());
                        xjr.setReason(reason);
                        xjr.setTouser(_src.getPublisher());
                        xjr.setIssms(issms);

                        this.whgXjReasonService.t_add(xjr);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }

        this.whgTraMapper.updateByExampleSelective(tra, example);
        return rb;
    }

    public ResponseBean t_updstate(String statemdfdate, String ids, String formstates, int tostate, WhgSysUser user) throws Exception {
        return this.t_updstate(statemdfdate, ids, formstates, tostate, user, null, 0);
    }

    /**
     * 是否推荐
     * @param ids
     * @param formrecoms
     * @param torecom
     * @return
     */
    public ResponseBean t_updrecommend(String ids, String formrecoms, int torecom) throws Exception {
        ResponseBean res = new ResponseBean();
        if(ids == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训主键丢失");
            return res;
        }
        Example example = new Example(WhgTra.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("state",6);
        c.andEqualTo("recommend",1);
        int result = this.whgTraMapper.selectCountByExample(example);
        Example example1 = new Example(WhgTra.class);
        Example.Criteria c1 = example1.createCriteria();
        c1.andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ));
        c1.andIn("recommend", Arrays.asList( formrecoms.split("\\s*,\\s*") ));
        WhgTra tra = new WhgTra();
        tra.setRecommend(torecom);
        this.whgTraMapper.updateByExampleSelective(tra,example1);
        return res;
    }

    /**
     * 还原
     * @param id
     * @param sysUser
     */
    public void t_undel(String id, WhgSysUser sysUser) {
        WhgTra tra = this.whgTraMapper.selectByPrimaryKey(id);
        if (tra == null){
            return;
        }
        if(tra.getState() == 4){
            tra.setId(IDUtils.getID());
            tra.setDelstate(0);
            tra.setState(1);
            this.whgTraMapper.insertSelective(tra);
        }else{
            tra.setDelstate(0);
            tra.setState(1);
            this.whgTraMapper.updateByPrimaryKeySelective(tra);
        }

    }

    /**
     * 上首页
     * @param ids
     * @param formupindex
     * @param toupindex
     * @return
     */
    public ResponseBean t_upindex(String ids, String formupindex, int toupindex) {
        ResponseBean res = new ResponseBean();
        if(ids == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("培训主键丢失");
            return res;
        }
        Example example = new Example(WhgTra.class);
        Example.Criteria c = example.createCriteria();
        c.andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ));
        c.andIn("upindex", Arrays.asList( formupindex.split("\\s*,\\s*") ));
        WhgTra tra = new WhgTra();
        tra.setUpindex(toupindex);
        this.whgTraMapper.updateByExampleSelective(tra,example);
        return res;
    }

    /**
     * 分页查询已发布的培训
     * @return
     */
    public PageInfo t_srchTraList(int page,int rows,String mid,String cultid) {

        PageHelper.startPage(page,rows);
        List list = whgTraMapper.selTraList(mid,cultid);
        return new PageInfo(list);
    }


    /**
     * 培训黑名单
     * @param actType
     */
    public void t_addPxBlacklist(String actType){
        try {

            //未签到三次
            int checkNumber = 3;
            List<Map> list = this.whgTraMapper.selectPxOrder4BlackListCheck(checkNumber, new Date(), actType);
            if (list != null && list.size() > 0) {
                for (Map ent : list) {
                    try {
                        String userid = (String) ent.get("userid");
                        String phone = (String) ent.get("phone");
                        if (actType != null && actType.equals("ZC")) {
                            this.whgBlackListService.addBlackList(userid, phone, checkNumber + "次以上没有进行众筹培训签到");
                        } else {
                            this.whgBlackListService.addBlackList(userid, phone, checkNumber + "次以上没有进行培训签到");
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        continue;
                    }
                }
            }


            //取消两次报名
            int unenrolcount = 2;
            List<Map> _list = this.whgTraMapper.selectPxSign4BlackListCheck(unenrolcount, new Date(), actType);
            if (_list != null && _list.size() > 0) {
                for (Map ent : _list) {
                    try {
                        String userid = (String) ent.get("userid");
                        String phone = (String) ent.get("phone");
                        if (actType != null && actType.equals("ZC")) {
                            this.whgBlackListService.addBlackList(userid, phone, unenrolcount + "次取消众筹培训报名");
                        } else {
                            this.whgBlackListService.addBlackList(userid, phone, unenrolcount + "次取消培训报名");
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        continue;
                    }
                }
            }

        } catch (Exception e) {
            log.error("jobBlackCall4Ven error", e);
        }
    }

    /**
     * 总分馆查询培训列表
     * @param page
     * @param rows
     * @param tra
     * @param sort
     * @param order
     * @param param
     * @return
     */
    public PageInfo t_srchSysList4p(int page, int rows, WhgTra tra, String sort, String order, Map<String, String> param)throws Exception {
        Example example = new Example(WhgTra.class);
        Example.Criteria c = example.createCriteria();
        if (tra!=null){
            //标题处理
            if (tra.getTitle()!=null){
                c.andLike("title", "%"+tra.getTitle()+"%");
                tra.setTitle(null); //去除title等于条件
            }

            c.andEqualTo(tra);
        }

        c.andIn("state", Arrays.asList(6,4));
        c.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

        String iscult = param.get("iscult");
        if (iscult!=null && "1".equals(iscult)){
            String cultid = param.get("syscultid");
            if (cultid == null){
                throw new Exception("文化馆ID丢失");
            }

            c.andEqualTo("cultid", cultid);
        }else{
            String level = param.get("level");
            String cultname = param.get("cultname");
            if (level==null || level.isEmpty()){
                throw new Exception("文化馆级别丢失");
            }
            if (cultname==null || cultname.isEmpty()){
                throw new Exception("cultname丢失");
            }

            Example syscultemp = new Example(WhgSysCult.class);
            Example.Criteria sc = syscultemp.createCriteria();
            sc.andEqualTo("state", EnumBizState.STATE_PUB.getValue());
            sc.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

            String areakey = "province";
            if ("2".equals(level)){
                areakey = "city";
            }else if ("3".equals(level)){
                areakey = "area";
            }
            sc.andEqualTo(areakey, cultname);

            List<String> refcultids = new ArrayList();
            List<WhgSysCult> refcults = this.whgSysCultMapper.selectByExample(syscultemp);
            if (refcults== null || refcults.size()==0){
                throw new Exception("没有找到文化馆");
            }
            for(WhgSysCult cult : refcults){
                refcultids.add(cult.getId());
            }
            //tra.setCultid(null);
            c.andIn("cultid", refcultids);
        }

        //排序
        if (sort!=null && !sort.isEmpty()){
            if (order!=null && "asc".equalsIgnoreCase(order)){
                example.orderBy(sort).asc();
            }else{
                example.orderBy(sort).desc();
            }
        }else{
            example.orderBy("crtdate").desc();
        }

        PageHelper.startPage(page, rows);
        List<WhgTra> list= this.whgTraMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgTra _tra : list){
                bm.setBean(_tra);
                Map info = new HashMap();
                info.putAll(bm);
                if (_tra.getCultid()!=null ){
                    WhgSysCult sysCult = this.whgSysCultMapper.selectByPrimaryKey(_tra.getCultid());
                    if (sysCult!=null){
                        info.put("cultname", sysCult.getName());
                    }
                }
                restList.add(info);
            }
        }

        pageInfo.setList(restList);
        return pageInfo;
    }
}
