package com.creatoo.hn.services.admin.gather;

import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.mapper.api.gather.CrtWhgGatherMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.activity.WhgActivityPlayService;
import com.creatoo.hn.services.admin.activity.WhgActivitySeatService;
import com.creatoo.hn.services.admin.activity.WhgActivityService;
import com.creatoo.hn.services.admin.activity.WhgActivityTimeService;
import com.creatoo.hn.services.admin.delivery.WhgDeliveryService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.train.WhgTrainEnrolService;
import com.creatoo.hn.services.admin.train.WhgTrainService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.services.api.apiinside.InsMessageService;
import com.creatoo.hn.services.api.apioutside.activity.ApiActivityService;
import com.creatoo.hn.services.api.apitrain.ApiTraService;
import com.creatoo.hn.services.comm.ResetRefidService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rbg on 2017/9/25.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames="gather", keyGenerator = "simpleKeyGenerator")
public class WhgGatherService extends BaseService{

    @Autowired
    private WhgGatherBrandMapper whgGatherBrandMapper;

    @Autowired
    private WhgGatherMapper whgGatherMapper;

    @Autowired
    private WhgGatherOrderMapper whgGatherOrderMapper;

    @Autowired
    private CrtWhgGatherMapper crtWhgGatherMapper;


    @Autowired
    private WhgActivityMapper whgActivityMapper;

    @Autowired
    private WhgTraMapper whgTraMapper;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgActivityPlayService whgActivityPlayService;
    @Autowired
    private WhgActivitySeatService whgActivitySeatService;
    @Autowired
    private WhgActivityService activityService;

    @Autowired
    private WhgTrainService whgTrainService;

    @Autowired
    private WhgDeliveryService whgDeliveryService;

    @Autowired
    private WhgActivityTimeService whgActivityTimeService;

    @Autowired
    private ApiTraService apiTraService;

    @Autowired
    private WhgActivityOrderMapper whgActivityOrderMapper;
    @Autowired
    private WhgTraEnrolMapper whgTraEnrolMapper;
    @Autowired
    private ApiActivityService apiActivityService;

    @Autowired
    private WhgUserMapper whgUserMapper;

    @Autowired
    private WhgActivityTicketMapper whgActTicketMapper;

    @Autowired
    private SMSService smsService;
    @Autowired
    private InsMessageService insMessageService;

    @Autowired
    private WhgTrainEnrolService whgTrainEnrolService;

    @Autowired
    private ResetRefidService resetRefidService;

    @Autowired
    private WhgXjReasonService whgXjReasonService;



    /**
     * 添加
     * @param info
     * @throws Exception
     */
    public void t_addBrand(WhgGatherBrand info) throws Exception{
        this.whgGatherBrandMapper.insert(info);
    }

    /**
     * 查询 单个众筹对象
     * @param key
     * @throws Exception
     */
    public WhgGather t_srchOneGather(String  key) throws Exception{
         return  this.whgGatherMapper.selectByPrimaryKey(key);
    }
    /**
     * 修改众筹状态
     * @param key
     * @throws Exception
     */
    public void t_updateGatherState(String ids,String formstates, int tostate,WhgSysUser user,Date date, String reason, int issms) throws Exception{
        if(ids != null){
            String[] idArr = ids.split("\\s*,\\s*");
            Example example = new Example(WhgGather.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(formstates != null&&!formstates.equals("")){
                c.andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")));
            }

            List<WhgGather> infos = this.whgGatherMapper.selectByExample(example);

            WhgGather record = new WhgGather();
            record.setState(tostate);
            Date now = new Date();
            if(date!=null){
               record.setStatemdfdate(date);
            }else{
               record.setStatemdfdate(now);
            }
            record.setStatemdfuser(user.getId());

            if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()){
                record.setCheckor(user.getId());
                record.setCheckdate(new Date());
            }
            if (tostate == EnumBizState.STATE_PUB.getValue()){
                record.setPublisher(user.getId());
                record.setPublishdate(new Date());
            }

            try {
                if (reason!=null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()){
                    List<WhgGather> srclist = this.whgGatherMapper.selectByExample(example);
                    if (srclist!=null){
                        for (WhgGather _src : srclist){
                            WhgXjReason xjr = new WhgXjReason();
                            xjr.setFkid(_src.getId());
                            xjr.setFktype("众筹");
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

            this.whgGatherMapper.updateByExampleSelective(record, example);


            this.setRefState4Gather(infos, formstates, tostate, user);
        }
    }

    private void setRefState4Gather(List<WhgGather> infos, String formstates, int tostate, WhgSysUser user) throws Exception{
        if (infos == null || infos.size()==0) {
            return;
        }

        StringBuilder actids = new StringBuilder();
        StringBuilder traids = new StringBuilder();
        for(WhgGather info : infos){
            if (info == null || info.getRefid() == null || info.getRefid().isEmpty() || info.getEtype() == null) {
                continue;
            }
            String etype = info.getEtype();
            if (EnumTypeClazz.TYPE_ACTIVITY.getValue().equals(etype)) {
                if (actids.length()>0){
                    actids.append(",");
                }
                actids.append(info.getRefid());
            }
            if (EnumTypeClazz.TYPE_TRAIN.getValue().equals(etype)) {
                if (traids.length()>0){
                    traids.append(",");
                }
                traids.append(info.getRefid());
            }
        }

        if (actids.length()>0){
            this.activityService.t_updstate(null, actids.toString(), null, tostate + "", user, null);
        }

        if (traids.length()>0){
            this.whgTrainService.t_updstate(null, traids.toString(), null, tostate, user);
        }
    }

    /**
     * 添加 众筹信息
     * @throws Exception
     */
    public ResponseBean t_addGather(HttpServletRequest request, WhgSysUser sysUser, WhgGather gather, WhgActivity act, WhgTra tra) throws Exception{
        ResponseBean rb = new ResponseBean();

        gather.setId(IDUtils.getID());

        gather.setCrtdate(new Date());
        gather.setCrtuser(sysUser.getId());
        gather.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        gather.setStatemdfdate(new Date());
        gather.setStatemdfuser(sysUser.getId());
        gather.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        gather.setRecommend(0);
        gather.setIssuccess(0);

        /*String cultid = sysUser.getCultid();
        if (cultid == null || cultid.isEmpty()) {
            cultid = Constant.ROOT_SYS_CULT_ID;
        }
        gather.setCultid(cultid);*/

        /*WhgSysCult cult = this.whgSystemCultService.t_srchOne(cultid);
        gather.setProvince(cult.getProvince());
        gather.setCity(cult.getCity());
        gather.setArea(cult.getArea());*/


        //其它众筹，直接保存
        if (gather.getEtype() == null || "0".equals(gather.getEtype())) {
            gather.setEtype("0");
            this.whgGatherMapper.insert(gather);
            return rb;
        }

        String refid= IDUtils.getID();

        //活动众筹
        if (gather.getEtype().equals(EnumTypeClazz.TYPE_ACTIVITY.getValue())) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            //封装活动信息
            act.setName(gather.getTitle());
            act.setBiz("ZC");//标志众筹活动
            act.setImgurl(gather.getImgurl());
            act.setEnterstrtime(gather.getTimestart());
            act.setEnterendtime(gather.getTimeend());
            act.setEndtime(act.getStarttime());
            act.setNoticetype(gather.getNoticetype());

            Date time = act.getStarttime();
            Date endDate = act.getStarttime();

            //同步可同步的数据
            act.setState(gather.getState());
            act.setDelstate(gather.getDelstate());
            gather.setEnclosure(act.getFilepath());
            gather.setPhone(act.getTelphone());
            //gather.setCultid(act.getCultid());
            act.setCultid(gather.getCultid());
            act.setProvince(gather.getProvince());
            act.setCity(gather.getCity());
            act.setAreaid(gather.getArea());
            act.setDeptid(gather.getDeptid());

            //开始走活动业务逻辑
            String seatJson = request.getParameter("seatjson");
            String timeJson = request.getParameter("activityTimeList");

            //添加活动
            act = activityService.t_add(act,refid,sysUser);
            List<Map<String, String>> timePlayList = whgActivityPlayService.setTimeTemp(timeJson);
            //如果是在线选座，需要添加活动座位信息
            int ticketnum = 0;//
            if (seatJson != null && act.getSellticket().equals(3)) {
                List<Map<String, String>> seatList = whgActivitySeatService.setSeatList(seatJson);
                whgActivitySeatService.t_add(seatList,sysUser, act.getId());
                //计算座位数
                ticketnum = 0;
                for (Map<String, String> _set : seatList) {
                    if ("1".equals(_set.get("seatstatus"))) {
                        ticketnum++;
                    }
                }
            }
            if (ticketnum == 0) {
                if (act.getTicketnum() != null) {
                    ticketnum = act.getTicketnum();
                }
            }
            while (!activityService.isAfter(time, endDate)) {
                whgActivityTimeService.t_add(timePlayList, act.getId(), time, ticketnum);
                time = formatter.parse(activityService.getTomorrow(time));
            }

        }

        //培训
        if (gather.getEtype().equals(EnumTypeClazz.TYPE_TRAIN.getValue())) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String _age1 = request.getParameter("age1");
            String _age2 = request.getParameter("age2");
            String sin_starttime = request.getParameter("sin_starttime");
            String sin_endtime = request.getParameter("sin_endtime");
            String[] _starttime = request.getParameterValues("_starttime");
            String[] _endtime = request.getParameterValues("_endtime");
            String[] fixedweek = request.getParameterValues("fixedweek");
            String fixedstarttime = request.getParameter("fixedstarttime");
            String fixedendtime = request.getParameter("fixedendtime");
            String mid = request.getParameter("mid");
            String[] majorid = request.getParameterValues("majorid");

            tra.setId(refid);
            tra.setBiz("ZC");//标志众筹活动
            tra.setTitle(gather.getTitle());
            tra.setTrainimg(gather.getImgurl());
            tra.setMaxnumber(gather.getNumsum());
            tra.setBasicenrollnumber(gather.getNummin());
            tra.setEnrollstarttime(gather.getTimestart());
            tra.setEnrollendtime(gather.getTimeend());
            tra.setNoticetype(gather.getNoticetype());
            gather.setPhone(tra.getPhone());
            //gather.setCultid(tra.getCultid());
            tra.setCultid(gather.getCultid());
            tra.setProvince(gather.getProvince());
            tra.setCity(gather.getCity());
            tra.setArea(gather.getArea());
            tra.setDeptid(gather.getDeptid());
            rb = this.whgTrainService.t_add(tra,sysUser,_age1,_age2,sin_starttime,sin_endtime,_starttime,_endtime,fixedweek,fixedstarttime,fixedendtime,mid,majorid);
            if (ResponseBean.FAIL.equals(rb.getSuccess())) {
                return rb;
            }

        }

        //设置众筹关联保存
        gather.setRefid(refid);
        this.whgGatherMapper.insert(gather);

        return rb;
    }

    /**
     * 编辑
     * @param info
     * @throws Exception
     */
    public void t_editBrand(WhgGatherBrand info) throws Exception{
        if (info == null || info.getId() == null) {
            throw new Exception("primare key is null");
        }

        this.whgGatherBrandMapper.updateByPrimaryKeySelective(info);
    }
    /**
     * 编辑
     * @param info
     * @throws Exception
     */
    public void t_editGather(WhgGather info) throws Exception{
        if (info == null || info.getId() == null) {
            throw new Exception("primare key is null");
        }
        this.whgGatherMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 编辑 众筹信息
     * @throws Exception
     */
    public ResponseBean t_editGather(HttpServletRequest request, WhgSysUser sysUser, WhgGather gather, WhgActivity act, WhgTra tra) throws Exception{
        ResponseBean rb = new ResponseBean();

        if (gather == null || gather.getId() == null) {
            rb.setErrormsg("众筹标识获取失败");
            rb.setSuccess(ResponseBean.FAIL);
            return rb;
        }

        WhgGather info = this.whgGatherMapper.selectByPrimaryKey(gather.getId());
        String refid = info.getRefid();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (info.getEtype() == null || "0".equals(info.getEtype())) {
            info.setEtype("0");
            this.whgGatherMapper.updateByPrimaryKeySelective(gather);
            return rb;
        }

        //活动众筹
        if (info.getEtype().equals(EnumTypeClazz.TYPE_ACTIVITY.getValue())) {

            try {
                act.setId(refid);
                act.setName(gather.getTitle());
                act.setImgurl(gather.getImgurl());
                act.setEnterstrtime(gather.getTimestart());
                act.setEnterendtime(gather.getTimeend());
                act.setEndtime(act.getStarttime());
                act.setNoticetype(gather.getNoticetype());
                gather.setEnclosure(act.getFilepath());
                gather.setPhone(act.getTelphone());
                //gather.setCultid(act.getCultid());
                act.setProvince(gather.getProvince());
                act.setCity(gather.getCity());
                act.setAreaid(gather.getArea());
                act.setDeptid(gather.getDeptid());
                this.activityService.t_edit(act, sysUser);
                act = this.activityService.t_srchOne(act.getId());

                Date time = act.getStarttime();
                Date endDate = act.getStarttime();
                //开始走活动业务逻辑
                String seatJson = request.getParameter("seatjson");
                String timeJson = request.getParameter("activityTimeList");


                List<Map<String, String>> timePlayList = whgActivityPlayService.setTimeTemp(timeJson);
                //如果是在线选座，需要添加活动座位信息
                int ticketnum = 0;//
                if (seatJson != null && act.getSellticket().equals(3)) {
                    //删除座位信息
                    whgActivitySeatService.delActSeat4ActId(act.getId());
                    List<Map<String, String>> seatList = whgActivitySeatService.setSeatList(seatJson);
                    whgActivitySeatService.t_add(seatList, sysUser, act.getId());
                    //计算座位数
                    ticketnum = 0;
                    for (Map<String, String> _set : seatList) {
                        if ("1".equals(_set.get("seatstatus"))) {
                            ticketnum++;
                        }
                    }
                }
                if (timeJson != null && timeJson != "") {//编辑的时候 不能编辑场次信息 不然 与场次管理 会有冲突 故此屏蔽此功能
                    WhgActivityTime actTime = new WhgActivityTime();
                    actTime.setActid(act.getId());
                    whgActivityTimeService.t_del(actTime); //删除场次信息
                    //添加时间段模板
                    timePlayList = whgActivityPlayService.setTimeTemp(timeJson);
                }
                if (ticketnum == 0) {
                    if (act.getTicketnum() != null) {
                        ticketnum = act.getTicketnum();
                    }
                }
                if (timePlayList != null) {//场次不可修改 但是 座位数有可能更新
                    if (ticketnum != 0 && time != null && endDate != null) {
                        while (!activityService.isAfter(time, endDate)) {
                            whgActivityTimeService.t_add(timePlayList, act.getId(), time, ticketnum);
                            time = formatter.parse(activityService.getTomorrow(time));
                        }
                    }
                } else {
                    if (ticketnum != 0) {
                        WhgActivityTime actTime = new WhgActivityTime();
                        actTime.setSeats(ticketnum);
                        whgActivityTimeService.updateList(act.getId(), actTime);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                rb.setErrormsg(e.getMessage());
                rb.setSuccess(ResponseBean.FAIL);
                return rb;
            }

        }

        //培训
        if (info.getEtype().equals(EnumTypeClazz.TYPE_TRAIN.getValue())) {
            tra.setId(refid);
            tra.setTitle(gather.getTitle());
            tra.setTrainimg(gather.getImgurl());
            tra.setMaxnumber(gather.getNumsum());
            tra.setBasicenrollnumber(gather.getNummin());
            tra.setEnrollstarttime(gather.getTimestart());
            tra.setEnrollendtime(gather.getTimeend());
            tra.setNoticetype(gather.getNoticetype());
            gather.setPhone(tra.getPhone());
            //gather.setCultid(tra.getCultid());
            tra.setProvince(gather.getProvince());
            tra.setCity(gather.getCity());
            tra.setArea(gather.getArea());
            tra.setDeptid(gather.getDeptid());

            String _age1 = request.getParameter("age1");
            String _age2 = request.getParameter("age2");
            String sin_starttime = request.getParameter("sin_starttime");
            String sin_endtime = request.getParameter("sin_endtime");
            String[] _starttime = request.getParameterValues("_starttime");
            String[] _endtime = request.getParameterValues("_endtime");
            String[] fixedweek = request.getParameterValues("fixedweek");
            String fixedstarttime = request.getParameter("fixedstarttime");
            String fixedendtime = request.getParameter("fixedendtime");
            String starttime = request.getParameter("starttime");
            String endtime = request.getParameter("endtime");
            String[] majorid = request.getParameterValues("majorid");

            if("".equals(request.getParameter("age1")) && "".equals(request.getParameter("age2"))){
                tra.setAge("");
            }
            if(tra.getEbrand() == null ){
                tra.setEbrand("");
            }
            if(tra.getTeacherid() == null ){
                tra.setTeacherid("");
            }
            if(tra.getEkey() == null ){
                tra.setEkey("");
            }
            if(tra.getEtag() == null ){
                tra.setEtag("");
            }
            if(tra.getVenue() == null ){
                tra.setVenue("");
            }
            if(tra.getVenroom() == null ){
                tra.setVenroom("");
            }
            rb = this.whgTrainService.t_edit(tra, sysUser,_age1,_age2,sin_starttime,sin_endtime,_starttime,_endtime,fixedweek,fixedstarttime,fixedendtime,starttime,endtime,majorid);
        }

        if (ResponseBean.FAIL.equals(rb.getSuccess())) {
            return rb;
        }

        this.whgGatherMapper.updateByPrimaryKeySelective(gather);
        return rb;
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUserId
     * @param optTime
     * @throws Exception
     */
    public void t_updstateBrand(String ids, String formstates, int tostate, String sysUserId, Date optTime, String reason, int issms) throws Exception{
        if (ids == null || formstates==null || formstates.isEmpty()){
            throw new Exception("params error");
        }
        Example example = new Example(WhgGatherBrand.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgGatherBrand info = new WhgGatherBrand();
        info.setState(tostate);
        if (optTime==null) optTime = new Date();
        info.setStatemdfdate(optTime);
        info.setStatemdfuser(sysUserId);

        if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()){
            info.setCheckor(sysUserId);
            info.setCheckdate(new Date());
        }
        if (tostate == EnumBizState.STATE_PUB.getValue()){
            info.setPublisher(sysUserId);
            info.setPublishdate(new Date());
        }

        try {
            if (reason!=null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()){
                List<WhgGatherBrand> srclist = this.whgGatherBrandMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgGatherBrand _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("品牌众筹");
                        xjr.setFktitile(_src.getTitle());
                        xjr.setCrtuser(sysUserId);
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

        this.whgGatherBrandMapper.updateByExampleSelective(info, example);
    }

    /**
     * 删除
     * @param id
     * @throws Exception
     */
    public void t_delBrand(String id) throws Exception{
        WhgGatherBrand info = this.whgGatherBrandMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        //if (info.getDelstate()!=null && info.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgGatherBrandMapper.deleteByPrimaryKey(id);
        /*}else {
            info = new WhgGatherBrand();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            this.whgGatherBrandMapper.updateByPrimaryKeySelective(info);
        }*/
    }

    /**
     * 回收
     * @param id
     * @throws Exception
     */
    public void t_recoveryBrand(String id) throws Exception{
        WhgGatherBrand info = new WhgGatherBrand();
        info.setId(id);
        info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
        this.whgGatherBrandMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 删除
     * @param id
     * @throws Exception
     */
    @CacheEvict(cacheNames = {"WhgActivity","WhgTra"}, allEntries = true)
    public void t_delGather(String id) throws Exception{
        WhgGather info = this.whgGatherMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }
        //if (info.getDelstate()!=null && info.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgGatherMapper.deleteByPrimaryKey(id);
            //删除活动或者培训中关联数据
            if(EnumTypeClazz.TYPE_ACTIVITY.getValue().equals(info.getEtype())){//活动类型
                this.whgActivityMapper.deleteByPrimaryKey(info.getRefid());
            }else if(EnumTypeClazz.TYPE_TRAIN.getValue().equals(info.getEtype())){//培训类型
                this.whgTraMapper.deleteByPrimaryKey(info.getRefid());
            }
        /*}else{
            info = new WhgGather();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            this.whgGatherMapper.updateByPrimaryKeySelective(info);
            this.setRefDelState4Gather(info);
        }*/
    }

    @CacheEvict(cacheNames = {"WhgActivity","WhgTra"}, allEntries = true)
    public void t_recoveryGather(String id) throws Exception{
        WhgGather info = this.whgGatherMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }
        info = new WhgGather();
        info.setId(id);
        info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
        this.whgGatherMapper.updateByPrimaryKeySelective(info);
        this.setRefDelState4Gather(info);
    }

    /**
     * 还原
     * @param id
     * @throws Exception
     */
    public void t_undelBrand(String id) throws Exception{
        WhgGatherBrand info = this.whgGatherBrandMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        info = new WhgGatherBrand();
        info.setId(id);
        info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        info.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        this.whgGatherBrandMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 还原 众筹项目
     * @param id
     * @throws Exception
     */
    @CacheEvict(cacheNames = {"WhgActivity","WhgTra"}, allEntries = true)
    public void t_undelGather(String id) throws Exception{
        WhgGather info = this.whgGatherMapper.selectByPrimaryKey(id);
        if (info == null) {
            return;
        }
        info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        info.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        this.whgGatherMapper.updateByPrimaryKeySelective(info);
        this.setRefDelState4Gather(info);
    }

    /**
     * 众筹项目 关联体处理删除与还原状态
     * @param info
     * @throws Exception
     */
    private void setRefDelState4Gather(WhgGather info) throws Exception{
        if (info == null || info.getId()==null) {
            return;
        }

        info = this.whgGatherMapper.selectByPrimaryKey(info.getId());

        if (info.getEtype() == null || info.getRefid() == null || info.getRefid().isEmpty()) {
            return;
        }
        String etype = info.getEtype();
        String refid = info.getRefid();
        if (EnumTypeClazz.TYPE_ACTIVITY.getValue().equals(etype)) {
            WhgActivity act = new WhgActivity();
            act.setId(refid);
            act.setDelstate(info.getDelstate());
            if (info.getState()!=null){
                act.setState(info.getState());
            }
            this.whgActivityMapper.updateByPrimaryKeySelective(act);
        }
        if (EnumTypeClazz.TYPE_TRAIN.getValue().equals(etype)) {
            WhgTra tra = new WhgTra();
            tra.setId(refid);
            tra.setDelstate(info.getDelstate());
            if (info.getState()!=null){
                tra.setState(info.getState());
            }
            this.whgTraMapper.updateByPrimaryKeySelective(tra);
        }
    }


    /**
     * 检测是否被引用的品牌
     * @param key
     * @return
     * @throws Exception
     */
    public boolean isUseBrand(String key) throws Exception{
        WhgGather recode = new WhgGather();
        recode.setBrandid(key);
        int useNum = this.whgGatherMapper.selectCount(recode);
        return useNum > 0;
    }


    /**
     * 主键查众筹品牌
     * @param key
     * @return
     * @throws Exception
     */
    public Object srchOneBrand(String key) throws Exception{
        WhgGatherBrand info = this.whgGatherBrandMapper.selectByPrimaryKey(key);
        return info;
    }

    /**
     * 主键查众筹
     * @param key
     * @return
     * @throws Exception
     */
    public Map srchOneGather(String key) throws Exception{
         WhgGather info = this.whgGatherMapper.selectByPrimaryKey(key);
        if (info == null) {
            return null;
        }

        Map rest = new HashMap();

        BeanMap bm = new BeanMap();
        bm.setBean(info);
        for (Map.Entry ent : bm.entrySet()){
            String k = (String) ent.getKey();
            rest.put("gat_"+k, ent.getValue());
        }

        String etype = info.getEtype();
        String refid = info.getRefid();

        if (etype == null || "0".equals(etype) || etype.isEmpty() || refid == null || refid.isEmpty()) {
            return rest;
        }

        Object refinfo = null;
        Object timeinfo = null;
        if (etype.equals(EnumTypeClazz.TYPE_ACTIVITY.getValue())) {
            refinfo = this.whgActivityMapper.selectByPrimaryKey(refid);
            timeinfo = this.whgActivityTimeService.findWhgActivityTime4ActId(refid);
            rest.put("playstime",((WhgActivityTime)timeinfo).getPlaystime());
            rest.put("playetime",((WhgActivityTime)timeinfo).getPlayetime());
        } else if (etype.equals(EnumTypeClazz.TYPE_TRAIN.getValue())) {
            refinfo = this.whgTraMapper.selectByPrimaryKey(refid);
        }
        if (refinfo == null) {
            return rest;
        }
        bm.setBean(refinfo);
        rest.putAll(bm);

        return rest;
    }


    /**
     * 后台分页查询众筹品牌
     * @param page
     * @param pageSize
     * @param recode
     * @param states
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public PageInfo srch4pBrand(int page, int pageSize, WhgGatherBrand recode, List states,
                                String sort, String order, String sysUserId) throws Exception{
        if (recode == null) {
            throw new Exception("param recode is null");
        }
        Example exp = new Example(recode.getClass());
        Example.Criteria c = exp.createCriteria();
        if (recode.getTitle() != null && !recode.getTitle().isEmpty()) {
            c.andLike("title", "%"+recode.getTitle()+"%");
            recode.setTitle(null);
        }
        if (recode.getTitleshort() != null && !recode.getTitleshort().isEmpty()) {
            c.andLike("titleshort", "%"+recode.getTitleshort()+"%");
            recode.setTitleshort(null);
        }

        if (recode.getCultid() == null || recode.getCultid().isEmpty()) {
            recode.setCultid(null);
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
                if (cultids!=null && cultids.size()>0){
                    c.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (recode.getDeptid() == null || recode.getDeptid().isEmpty()) {
            recode.setDeptid(null);
            try {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
                if (deptids != null && deptids.size() > 0) {
                    c.andIn("deptid", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        c.andEqualTo(recode);
        if (states != null && states.size() > 0) {
            c.andIn("state", states);
        }

        if (sort!=null && !sort.isEmpty()){
            if (order != null && order.equalsIgnoreCase("desc")) {
                exp.orderBy(sort).desc();
            }else {
                exp.orderBy(sort).asc();
            }
        }else {
            exp.orderBy("crtdate").desc();
        }

        PageHelper.startPage(page, pageSize);
        List list = this.whgGatherBrandMapper.selectByExample(exp);

        return new PageInfo(list);
    }

    public PageInfo sysSrch4pBrand(int page, int pageSize, WhgGatherBrand recode, String sort, String order, Map<String, String> record) throws Exception{
        Example example = new Example(recode.getClass());

        Example.Criteria c = example.createCriteria();
        if (recode!=null){
            if (recode.getTitle() != null && !recode.getTitle().isEmpty()) {
                c.andLike("title", "%"+recode.getTitle()+"%");
                recode.setTitle(null);
            }

            c.andEqualTo(recode);
        }
        c.andIn("state", Arrays.asList(6,4));
        c.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

        String iscult = record.get("iscult");
        if (iscult!=null && "1".equals(iscult)){
            String cultid = record.get("refid");
            if (cultid == null){
                throw new Exception("cultid is not null");
            }

            c.andEqualTo("cultid", cultid);
        }else{
            String pcalevel = record.get("pcalevel");
            String pcatext = record.get("pcatext");
            if (pcalevel==null || pcalevel.isEmpty()){
                throw new Exception("pcalevel is not null");
            }
            if (pcatext==null || pcatext.isEmpty()){
                throw new Exception("pcatext is not null");
            }

            List<String> refcultids = this.whgSystemCultService.t_srchByArea(pcalevel, pcatext);
            if (refcultids== null || refcultids.size()==0){
                throw new Exception("not cults info");
            }

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

        PageHelper.startPage(page, pageSize);
        List<WhgGatherBrand> list= this.whgGatherBrandMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);

        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgGatherBrand _row : list){
                bm.setBean(_row);
                Map info = new HashMap();
                info.putAll(bm);
                if (_row.getCultid()!=null ){
                    WhgSysCult sysCult = this.whgSystemCultService.t_srchOne(_row.getCultid());
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

    /**
     * 品牌列表查询
     * @return
     * @throws Exception
     */
    public List srchListBrand(String cultid, String sysUserId) throws Exception{
        Example exp = new Example(WhgGatherBrand.class);
        Example.Criteria c = exp.createCriteria();
        c.andEqualTo("state", EnumBizState.STATE_PUB.getValue());
        c.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        if (cultid != null && !cultid.isEmpty()) {
            c.andEqualTo("cultid", cultid);
        }else {
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
                if (cultids!=null && cultids.size()>0){
                    c.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        try {
            List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
            if (deptids != null && deptids.size() > 0) {
                c.andIn("deptid", deptids);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        exp.orderBy("statemdfdate").desc();

        return this.whgGatherBrandMapper.selectByExample(exp);
    }


    /**
     * 后台分页查询项目众筹列表
     * @param page
     * @param pageSize
     * @param recode
     * @param states
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public PageInfo srch4pGather(int page, int pageSize, WhgGather recode, List states,
                                 String sort, String order, String sysUserId) throws Exception{
        if (recode == null) {
            throw new Exception("param recode is null");
        }
        Example exp = new Example(recode.getClass());
        Example.Criteria c = exp.createCriteria();
        if (recode.getTitle() != null && !recode.getTitle().isEmpty()) {
            c.andLike("title", "%"+recode.getTitle()+"%");
            recode.setTitle(null);
        }

        if (recode.getCultid() == null || recode.getCultid().isEmpty()) {
            recode.setCultid(null);
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
                if (cultids!=null && cultids.size()>0){
                    c.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (recode.getDeptid() == null || recode.getDeptid().isEmpty()) {
            recode.setDeptid(null);
            try {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
                if (deptids != null && deptids.size() > 0) {
                    c.andIn("deptid", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        c.andEqualTo(recode);
        if (states != null && states.size() > 0) {
            c.andIn("state", states);
        }

        if (sort!=null && !sort.isEmpty()){
            if (order != null && order.equalsIgnoreCase("desc")) {
                exp.orderBy(sort).desc();
            }else {
                exp.orderBy(sort).asc();
            }
        }else {
            exp.orderBy("crtdate").desc();
        }

        PageHelper.startPage(page, pageSize);
        List<WhgGather> list = this.whgGatherMapper.selectByExample(exp);

        PageInfo pageInfo = new PageInfo(list);

        List restlist = new ArrayList();
        BeanMap bm = new BeanMap();
        for(WhgGather gat : list){
            bm.setBean(gat);
            Map row = new HashMap();
            row.putAll(bm);

            if (gat.getEtype()!=null && "5".equals(gat.getEtype()) && gat.getRefid()!=null){
                WhgTra tra = this.whgTrainService.srchOne(gat.getRefid());
                row.put("refInfo", tra);
            }
            restlist.add(row);
        }
        pageInfo.setList(restlist);

        return pageInfo;
    }

    public PageInfo sysSrch4pGather(int page, int pageSize, WhgGather recode, String sort, String order, Map<String, String> record) throws Exception{
        Example example = new Example(recode.getClass());

        Example.Criteria c = example.createCriteria();
        if (recode!=null){
            if (recode.getTitle() != null && !recode.getTitle().isEmpty()) {
                c.andLike("title", "%"+recode.getTitle()+"%");
                recode.setTitle(null);
            }

            c.andEqualTo(recode);
        }
        c.andIn("state", Arrays.asList(6,4));
        c.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

        String iscult = record.get("iscult");
        if (iscult!=null && "1".equals(iscult)){
            String cultid = record.get("refid");
            if (cultid == null){
                throw new Exception("cultid is not null");
            }

            c.andEqualTo("cultid", cultid);
        }else{
            String pcalevel = record.get("pcalevel");
            String pcatext = record.get("pcatext");
            if (pcalevel==null || pcalevel.isEmpty()){
                throw new Exception("pcalevel is not null");
            }
            if (pcatext==null || pcatext.isEmpty()){
                throw new Exception("pcatext is not null");
            }

            List<String> refcultids = this.whgSystemCultService.t_srchByArea(pcalevel, pcatext);
            if (refcultids== null || refcultids.size()==0){
                throw new Exception("not cults info");
            }

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

        PageHelper.startPage(page, pageSize);
        List<WhgGather> list= this.whgGatherMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);

        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgGather _row : list){
                bm.setBean(_row);
                Map info = new HashMap();
                info.putAll(bm);
                if (_row.getCultid()!=null ){
                    WhgSysCult sysCult = this.whgSystemCultService.t_srchOne(_row.getCultid());
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


    /**
     * 后台查询其它众筹订单列表
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo srchList4Order(int page, int pageSize, WhgGatherOrder recode, String sort, String order) throws Exception{
        if (recode == null || recode.getGatherid() == null) {
            throw new Exception("param recode is null");
        }
        Example exp = new Example(recode.getClass());
        Example.Criteria c = exp.createCriteria();
        if (recode.getName() != null && !recode.getName().isEmpty()) {
            c.andLike("name", "%"+recode.getName()+"%");
            recode.setName(null);
        }
        if (recode.getPhone() != null && !recode.getPhone().isEmpty()) {
            c.andLike("phone", "%"+recode.getPhone()+"%");
            recode.setPhone(null);
        }

        c.andEqualTo(recode);
        if (sort!=null && !sort.isEmpty()){
            if (order != null && order.equalsIgnoreCase("desc")) {
                exp.orderBy(sort).desc();
            }else {
                exp.orderBy(sort).asc();
            }
        }else {
            exp.orderBy("crtdate").desc();
        }

        PageHelper.startPage(page, pageSize);
        List list = this.whgGatherOrderMapper.selectByExample(exp);

        return new PageInfo(list);
    }

    /**
     * 查询其它众筹订单详细信息
     * @param id
     * @return
     * @throws Exception
     */
    public Map findGatherOrder(String id) throws Exception{
        return this.crtWhgGatherMapper.findGatherOrder(id);
    }


    /**
     * 定时处理 标记众筹成功失败
     */
    public void jobTimeGather() throws Exception{
        Example exp = new Example(WhgGather.class);
        Example.Criteria c = exp.createCriteria();
        //众筹时间到了的
        c.andLessThanOrEqualTo("timeend", new Date());
        //是否成功状态不为1，2
        c.andNotIn("issuccess", Arrays.asList(1,2));
        //已发布的,未删除的
        c.andEqualTo("state", EnumBizState.STATE_PUB.getValue());
        c.andNotEqualTo("delstate", EnumStateDel.STATE_DEL_YES.getValue());

        //结束时间最近的先行处理
        exp.orderBy("timeend").desc();

        //查询过滤了修改的结果状态(issuccess)，不需自增；若不过滤结果状态，需自增
        int page = 1;
        //每次处理不超过500条
        int size = 500;
        List<WhgGather> list = null;
        //不超过10次以上外层处理，防止不能终止的可能
        int maxCount = 10;
        do{
            maxCount --;
            PageHelper.startPage(page, size);
            list = this.whgGatherMapper.selectByExample(exp);
            if (list == null || list.size() == 0) {
                return;
            }

            BeanMap bm = new BeanMap();
            for (WhgGather info : list) {
                if (info == null || info.getNummin() == null) {
                    continue;
                }

                try {
                    Map ent = new HashMap();
                    bm.setBean(info);
                    ent.putAll(bm);

                    int orderCount = this.getOrderCount(ent);
                    WhgGather recode = new WhgGather();
                    recode.setId(info.getId());
                    if (orderCount >= info.getNummin().intValue()){
                        recode.setIssuccess(1);
                        try {
                            //众筹成功 提醒
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
                            Map<String, String> data = new HashMap<>();
                            data.put("title", info.getTitle());
                            data.put("starttime", sdf.format(info.getTimestart()));
                            data.put("endtime",  sdf.format(info.getTimeend()));
                            WhgSysUser sysUser = whgSystemUserService.t_srchOne(info.getCrtuser()) ;
                            if(sysUser!=null&&sysUser.getContact()!=null&&!sysUser.getContact().equals("")){
                                this.smsService.t_sendSMS(sysUser.getContact(), "ZC_SUCCESS_NOTICE", data, info.getId());//众筹成功 短信提示 众筹创建人
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }else{
                        recode.setIssuccess(2);
                    }
                    this.whgGatherMapper.updateByPrimaryKeySelective(recode);
                } catch (Exception e) {
                    log.error("JOB_WHG_GATHER_ISSUCCESS_ERROR", e);
                    continue;
                }
            }
        }while (maxCount>0 && list!=null && list.size()==size);

    }


    /**
     * 定时处理 众筹成功失败时发送短信
     */
    public void jobTimeGatherMessage() throws Exception{
        Example exp = new Example(WhgGather.class);
        Example.Criteria c = exp.createCriteria();
        //已发布的,未删除的
        c.andEqualTo("state", EnumBizState.STATE_PUB.getValue());
        c.andNotEqualTo("delstate", EnumStateDel.STATE_DEL_YES.getValue());
        //众筹时间到了的
        c.andLessThanOrEqualTo("timeend", new Date());
        //是否成功状态为1，2
        c.andIn("issuccess", Arrays.asList(1,2));
        //没有标记发送通知的
        //c.andNotEqualTo("ismessage", 1);
        c.andIsNull("ismessage");

        //标记过后查询不需自增
        int page = 1;
        //每次处理不超过500条
        int size = 500;
        List<WhgGather> list = null;
        //不超过10次以上外层处理，防止不能终止的可能
        int maxCount = 10;
        do{
            maxCount --;
            PageHelper.startPage(page, size);
            list = this.whgGatherMapper.selectByExample(exp);
            if (list == null || list.size() == 0) {
                return;
            }

            for (WhgGather info : list) {
                try {
                    this.gatherMessages(info);
                } catch (Exception e) {
                    log.error("JOB_WHG_GATHER_MESSAGE_ERROR", e);
                    continue;
                }
            }
        }while (maxCount>0 && list!=null && list.size()==size);
    }

    /**
     * 处理分发信息调用
     * @param info
     * @throws Exception
     */
    private void gatherMessages(WhgGather info) throws Exception{
        String etype = info.getEtype();
        String refid = info.getRefid();
        Integer issuccess = info.getIssuccess();
        if (issuccess == null){
            return;
        }

        boolean success = false;
        if (issuccess.intValue() == 1){
            success = true;
        }

        //其它众筹发消息
        if (etype == null || etype.isEmpty() || "0".equals(etype)) {
            //相关的订单列表
            WhgGatherOrder wgo = new WhgGatherOrder();
            wgo.setState(1);
            wgo.setGatherid(info.getId());
            List<WhgGatherOrder> orders = this.whgGatherOrderMapper.select(wgo);
            if (orders != null) {
                for (WhgGatherOrder order : orders) {
                    try {
                        this.sendGatherMessage(info, order, success);
                    } catch (Exception e) {
                        log.error("JOB_WHG_GATHER_MESSAGE_OTHER_ERROR", e);
                    }
                }
            }
        }

        //活动众筹发消息
        if (EnumTypeClazz.TYPE_ACTIVITY.getValue().equals(etype) && refid!=null) {
            WhgActivityOrder wao = new WhgActivityOrder();
            wao.setActivityid(refid);
            wao.setOrderisvalid(1);
            List<WhgActivityOrder> orders = this.whgActivityOrderMapper.select(wao);
            if (orders != null) {
                for (WhgActivityOrder order : orders) {
                    try {
                        this.apiActivityService.sendSmsAct(order, success);
                    } catch (Exception e) {
                        log.error("JOB_WHG_GATHER_MESSAGE_ACT_ERROR", e);
                    }
                }
            }
        }

        //培训众筹发消息
        if (EnumTypeClazz.TYPE_TRAIN.getValue().equals(etype) && refid!=null) {
            Example exp = new Example(WhgTraEnrol.class);
            exp.createCriteria()
                    .andEqualTo("traid", refid)
                    .andIn("state", Arrays.asList(1,4,6));
            List<WhgTraEnrol> orders = this.whgTraEnrolMapper.selectByExample(exp);
            if (orders != null) {
                for (WhgTraEnrol order : orders) {
                    try {
                        this.whgTrainEnrolService.sendSmsTra(order, success);
                    } catch (Exception e) {
                        log.error("JOB_WHG_GATHER_MESSAGE_TRA_ERROR", e);
                    }
                }
            }
        }

        //修改状态发消息状态
        WhgGather recode = new WhgGather();
        recode.setId(info.getId());
        recode.setIsmessage(1);
        this.whgGatherMapper.updateByPrimaryKeySelective(recode);
    }

    /**
     * 其它众筹成功失败 通知
     * @param gather
     * @param order
     * @param success
     * @throws Exception
     */
    public void sendGatherMessage(WhgGather gather, WhgGatherOrder order, boolean success) throws Exception{
        String tempCode = "ZC_Failure";
        if (success){
            tempCode = "ZC_GAT_True";
        }

        Map<String, String> data = new HashMap<>();
        data.put("userName", order.getName());
        data.put("title", gather.getTitle());
        data.put("orderNum", order.getOrderid());
        data.put("phone", gather.getPhone());

        /*if (gather.getNoticetype() != null && "ZNX".equals(gather.getNoticetype())) {
            this.insMessageService.t_sendZNX(order.getUserid(), null, tempCode, data, order.getGatherid(), EnumProject.PROJECT_ZC.getValue());
        }else {
            this.smsService.t_sendSMS(order.getPhone(), tempCode, data, gather.getId());
        }*/
        if (gather.getNoticetype() != null && !gather.getNoticetype().isEmpty()) {
            String noticetype = gather.getNoticetype();
            if (noticetype.contains("ZNX")){
                try{
                    this.insMessageService.t_sendZNX(order.getUserid(), null, tempCode, data, order.getGatherid(), EnumProject.PROJECT_ZC.getValue());
                }catch (Exception e){
                    log.error("t_sendZNX error", e);
                }
            }
            if (noticetype.contains("SMS")){
                try {
                    this.smsService.t_sendSMS(order.getPhone(), tempCode, data, gather.getId());
                } catch (Exception e) {
                    log.error("t_sendSMS error", e);
                }
            }
        }
    }

    /**
     * 其它众筹申请成功通知
     * @param gather
     * @param order
     * @throws Exception
     */
    public void sendGatherApplyMessage(WhgGather gather, WhgGatherOrder order) throws Exception{
        Map<String, String> data = new HashMap<>();
        data.put("userName", order.getName());
        data.put("title", gather.getTitle());
        data.put("orderNum", order.getOrderid());
        data.put("phone", gather.getPhone());

        String tempCode = "ZC_CY_True";
        /*if (gather.getNoticetype() != null && "ZNX".equals(gather.getNoticetype())) {
            this.insMessageService.t_sendZNX(order.getUserid(), null, tempCode, data, order.getGatherid(), EnumProject.PROJECT_ZC.getValue());
        }else {
            this.smsService.t_sendSMS(order.getPhone(), tempCode, data, gather.getId());
        }*/
        if (gather.getNoticetype() != null && !gather.getNoticetype().isEmpty()) {
            String noticetype = gather.getNoticetype();
            if (noticetype.contains("ZNX")){
                try{
                    this.insMessageService.t_sendZNX(order.getUserid(), null, tempCode, data, order.getGatherid(), EnumProject.PROJECT_ZC.getValue());
                }catch (Exception e){
                    log.error("t_sendZNX error", e);
                }
            }
            if (noticetype.contains("SMS")){
                try {
                    this.smsService.t_sendSMS(order.getPhone(), tempCode, data, gather.getId());
                } catch (Exception e) {
                    log.error("t_sendSMS error", e);
                }
            }
        }
    }




    //TODO 接口分隔线================================

    /**
     * 查询品牌列表
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageInfo select4pBrands(int page, int pageSize, List cultid,String province,String city,String area, List deptids) throws Exception{
        PageHelper.startPage(page, pageSize);
        List list = this.crtWhgGatherMapper.selectBrands(cultid,province,city,area, deptids);

        return new PageInfo(list);
    }

    /**
     * 查询品牌信息
     * @param id
     * @return
     * @throws Exception
     */
    public Map findBrand(String id) throws Exception{
        return this.crtWhgGatherMapper.findBrand(id);
    }

    /**
     * 查指定品牌的项目
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectGathers4Brands(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgGatherMapper.selectBrandGathers(recode);
        if (list!=null){
            for (Map row : list) {
                row.put("orderCount", this.getOrderCount(row));
            }
        }
        return new PageInfo(list);
    }

    /**
     * 查指定品牌的资源信息
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectBrandResources(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List list = this.crtWhgGatherMapper.selectBrandResources(recode);
        return new PageInfo(list);
    }


    /**
     * 查询众筹项目列表
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectGatherList(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgGatherMapper.selectGathers(recode);
        if (list!=null){
            for (Map row : list) {
                row.put("orderCount", this.getOrderCount(row));
            }
        }
        return new PageInfo(list);
    }

    /**
     * 查询与活动 培训 有关联的 众筹
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public  WhgGather getGatherByRefId(String refId){
        Example exp = new Example(WhgGather.class);
        Example.Criteria c = exp.createCriteria();
        Example.Criteria c2 = exp.createCriteria();
        //已发布的,未删除的
        c.andEqualTo("state", EnumBizState.STATE_PUB.getValue());
        c2.andEqualTo("state", EnumBizState.STATE_PUB.getValue());
        c.andNotEqualTo("delstate", EnumStateDel.STATE_DEL_YES.getValue());
        c2.andNotEqualTo("delstate", EnumStateDel.STATE_DEL_YES.getValue());
        c.andLike("relateactid","%"+refId+"%");
        c2.andLike("relatetraid","%"+refId+"%");
        exp.or(c2);
        List<WhgGather> list=this.whgGatherMapper.selectByExample(exp);
        if(list.size()>0){
            return  list.get(0);
        }else{
            return null;
        }
    }


    /**
     * 查询众筹信息
     * @param id
     * @return
     * @throws Exception
     */
    public Map findGather(String id) throws Exception{
        Map rest = new HashMap();
        WhgGather info = this.whgGatherMapper.selectByPrimaryKey(id);
        if (info == null) {
            throw new Exception("not find gather info for key");
        }

        BeanMap bm = new BeanMap();
        bm.setBean(info);
        rest.putAll(bm);

        String etype = info.getEtype();
        String refid = info.getRefid();
        int orderCount = 0;
        //非refid 或 其它众筹可直接返回
        if (etype == null || "0".equals(etype) || refid == null || refid.isEmpty()) {
            orderCount = this.getOrderCount4gat(id);
            rest.put("orderCount", orderCount);
            return rest;
        }

        Object refinfo = null;
        if (EnumTypeClazz.TYPE_ACTIVITY.getValue().equals(etype)) {
            //活动众筹，加载活动信息
            refinfo = this.whgActivityMapper.selectByPrimaryKey(refid);
            //活动报名计数
            orderCount = this.getOrderCount4act(refid);
        } else if (EnumTypeClazz.TYPE_TRAIN.getValue().equals(etype)) {
            //培训众筹，加载培训信息
            refinfo = this.whgTrainService.srchOne(refid);
            //培训报名计数
            orderCount = this.getOrderCount4tra(refid);
            //培训课程表
           // List list = this.apiTraService.getCourseByTraId(refid);
           // rest.put("traCourseList", list);
        }

        rest.put("orderCount", orderCount);
        if (refinfo != null) {
            BeanMap refbm = new BeanMap(refinfo);
            Map refmap = new HashMap();
            refmap.putAll(refbm);
            resetRefidService.resetRefid4tag(refmap, "etag");
            rest.put("refinfo", refmap);
        }

        return rest;
    }

    /**
     * 查询众筹资源
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectGatherResources(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List list = this.crtWhgGatherMapper.selectGatherResources(recode);
        return new PageInfo(list);
    }




    /**
     * 活动报名计数
     * @param actid
     * @return
     */
    private int getOrderCount4act(String actid, String userid, String orderid){
        if (actid==null || actid.isEmpty()) return 0;
        /*WhgActivityOrder order = new WhgActivityOrder();
        order.setActivityid(actid);
        order.setOrderisvalid(1);
        return this.whgActivityOrderMapper.selectCount(order);*/

        return this.apiActivityService.getTicketCountAct(actid, userid, orderid);
    }
    private int getOrderCount4act(String actid){
        return this.getOrderCount4act(actid, null, null);
    }

    /**
     * 培训报名计数
     * @param traid
     * @return
     */
    private int getOrderCount4tra(String traid, String userid, String orderid){
        if (traid==null || traid.isEmpty()) return 0;
        Example exp = new Example(WhgTraEnrol.class);
        Example.Criteria c = exp.createCriteria();
        c.andEqualTo("traid", traid);
        c.andIn("state", Arrays.asList(1,4,6));
        if (userid != null && !userid.isEmpty()) {
            c.andEqualTo("userid", userid);
        }
        if (orderid != null && !orderid.isEmpty()){
            c.andEqualTo("id", orderid);
        }
        return this.whgTraEnrolMapper.selectCountByExample(exp);
    }
    private int getOrderCount4tra(String traid){
        return this.getOrderCount4tra(traid, null, null);
    }

    /**
     * 其它众筹报名计数
     * @param gatherid
     * @return
     */
    private int getOrderCount4gat(String gatherid, String userid, String orderid){
        if (gatherid==null || gatherid.isEmpty()) return 0;
        WhgGatherOrder order = new WhgGatherOrder();
        order.setGatherid(gatherid);
        order.setGathertype("0");
        order.setState(1);
        if (userid != null && !userid.isEmpty()) {
            order.setUserid(userid);
        }
        if (orderid !=null && !orderid.isEmpty()){
            order.setId(orderid);
        }
        return this.whgGatherOrderMapper.selectCount(order);
    }
    private int getOrderCount4gat(String gatherid){
        return this.getOrderCount4gat(gatherid, null, null);
    }

    /**
     * 列表项获取报名数
     * @param info
     * @return
     */
    private int getOrderCount(Map info, String userid, String orderid){
        if (info == null) return 0;
        String etype = (String) info.get("etype");
        String refid = (String) info.get("refid");
        String id = (String) info.get("id");

        if (etype == null || etype.isEmpty() || "0".equals(etype)) {
            return this.getOrderCount4gat(id, userid, orderid);
        }

        if (EnumTypeClazz.TYPE_ACTIVITY.getValue().equals(etype)) {
            //活动报名计数
            /*String oid = null;
            if (userid != null && info.containsKey("oid")){
                oid = (String) info.get("oid");
            }
            return this.getOrderCount4act(refid, userid, oid);*/
            return this.getOrderCount4act(refid, userid, orderid);
        }

        if (EnumTypeClazz.TYPE_TRAIN.getValue().equals(etype)) {
            return this.getOrderCount4tra(refid, userid, orderid);
        }
        return 0;
    }

    private int getOrderCount(Map info, String userid){
        return this.getOrderCount(info, userid, null);
    }

    private int getOrderCount(Map info){
        return this.getOrderCount(info, null, null);
    }



    /**
     * 申请检查众筹
     * @return
     * @throws Exception
     */
    public ApiResultBean applyCheckGather(String id, String userid, String etype) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (id == null || userid == null || id.isEmpty() || userid.isEmpty()) {
            arb.setCode(103);
            arb.setMsg("众筹或用户标识信息获取失败");
            return arb;
        }

        WhgGather info = this.whgGatherMapper.selectByPrimaryKey(id);
        WhgUser user = this.whgUserMapper.selectByPrimaryKey(userid);

        if (info == null || user == null) {
            arb.setCode(102);
            arb.setMsg("众筹或用户信息查找失败");
            return arb;
        }

        if (info.getEtype() == null || "0".equals(info.getEtype())) {
            WhgGatherOrder recode = new WhgGatherOrder();
            recode.setUserid(userid);
            recode.setGatherid(info.getId());
            recode.setState(1);
            int userOrderCount = this.whgGatherOrderMapper.selectCount(recode);
            if (userOrderCount > 0) {
                arb.setCode(103);
                arb.setMsg("您已经参与过了，不能重复申请参与");
                return arb;
            }
        }

        if (etype != null && info.getEtype() != null && !etype.equals(info.getEtype())) {
            arb.setCode(102);
            arb.setMsg("众筹类型检查失败");
            return arb;
        }

        if (info.getState() == null || info.getState().intValue()!=EnumBizState.STATE_PUB.getValue()){
            arb.setCode(102);
            arb.setMsg("众筹已下架");
            return arb;
        }

        if (info.getDelstate() != null && info.getDelstate().intValue() == EnumStateDel.STATE_DEL_YES.getValue()) {
            arb.setCode(102);
            arb.setMsg("众筹已被回收");
            return arb;
        }

        Date timestart = info.getTimestart();
        Date timeend = info.getTimeend();
        Date now = new Date();
        if (now.compareTo(timestart) < 0) {
            arb.setCode(102);
            arb.setMsg("众筹时间未开始");
            return arb;
        }
        if (timeend.compareTo(now) < 0) {
            arb.setCode(102);
            arb.setMsg("众筹时间已结束");
            return arb;
        }

        int orderCount = this.getOrderCount4gat(info.getId());
        if (EnumTypeClazz.TYPE_ACTIVITY.getValue().equals( info.getEtype() )) {
            orderCount = this.getOrderCount4act(info.getRefid());
        } else if (EnumTypeClazz.TYPE_TRAIN.getValue().equals( info.getEtype() )) {
            orderCount = this.getOrderCount4tra(info.getRefid());
        }
        if (info.getNumsum().intValue() <= orderCount) {
            arb.setCode(102);
            arb.setMsg("众筹数量已达上限");
            return arb;
        }

        return arb;
    }

    /**
     * 申请其它众筹
     * @return
     * @throws Exception
     */
    public ApiResultBean applyGather4other(WhgGatherOrder info, String gatherid, String userid) throws Exception {
        ApiResultBean arb = new ApiResultBean();
        if (info == null) {
            arb.setCode(103);
            arb.setMsg("订单信息获取失败");
            return arb;
        }
        String gathertype = "0";
        arb = this.applyCheckGather(gatherid, userid, gathertype);
        if (arb.getCode().intValue() != 0){
            return arb;
        }



        info.setGatherid(gatherid);
        info.setUserid(userid);
        info.setGathertype(gathertype);
        if (info.getCaedtype() == null) {
            info.setCaedtype(1);
        }

        if (info.getCaednumber() == null || info.getCaednumber().isEmpty() || info.getCaednumber().length()>50) {
            arb.setCode(103);
            arb.setMsg("证件号为空或长度过大");
            return arb;
        }
        if (info.getBirthday() == null || info.getBirthday().isEmpty()
                || !info.getBirthday().matches("\\d{4}\\D\\d{2}\\D\\d{2}")) {
            arb.setCode(103);
            arb.setMsg("生日为空或格式不正确");
            return arb;
        }
        if (info.getPhone() == null || !info.getPhone().matches("\\d{11,12}")) {
            arb.setCode(103);
            arb.setMsg("手机号为空或格式不正确");
            return arb;
        }

        if (info.getCaedtype().intValue() == 1) {
            if (!info.getCaednumber().matches("\\d{17}[Xx\\d]")) {
                arb.setCode(103);
                arb.setMsg("证件号格式不正确");
                return arb;
            }
        }

        info.setId(IDUtils.getID());
        info.setOrderid(IDUtils.getOrderID(EnumOrderType.ORDER_GAT.getValue()+""));
        info.setState(1);
        info.setCrtdate(new Date());
        this.whgGatherOrderMapper.insert(info);

        //处理通知
        try {
            WhgGather gather = this.whgGatherMapper.selectByPrimaryKey(gatherid);
            WhgGatherOrder order = this.whgGatherOrderMapper.selectByPrimaryKey(info.getId());
            this.sendGatherApplyMessage(gather, order);
        }catch (Exception e){
            log.error("WHG_GATHER_MESSAGE_APPLYGAT_ERROR", e);
        }

        return arb;
    }


    /**
     * 删除其它众筹订单
     * @param id
     * @param userid
     * @return
     */
    public ApiResultBean unApplyGather(String id, String userid){
        ApiResultBean arb = new ApiResultBean();

        WhgGatherOrder info = this.whgGatherOrderMapper.selectByPrimaryKey(id);
        if (info == null) {
            return arb;
        }
        if (userid != null && !userid.isEmpty() && !userid.equals(info.getUserid())) {
            arb.setCode(102);
            arb.setMsg("不能删除其它用户的订单");
            return arb;
        }
        if (info.getGatherid()!=null && !info.getGatherid().isEmpty()){
            WhgGather gatinfo = this.whgGatherMapper.selectByPrimaryKey(info.getGatherid());
            if (gatinfo != null && gatinfo.getIssuccess()!=null && gatinfo.getIssuccess().intValue() == 1) {
                arb.setCode(102);
                arb.setMsg("不能删除已成功的众筹订单");
                return arb;
            }
        }

        this.whgGatherOrderMapper.deleteByPrimaryKey(info.getId());

        return arb;
    }

    /**
     * 取消其它众筹订单
     * @param id
     * @param userid
     * @return
     */
    public ApiResultBean offApplyGather(String id, String userid){
        ApiResultBean arb = new ApiResultBean();

        WhgGatherOrder info = this.whgGatherOrderMapper.selectByPrimaryKey(id);
        if (info == null) {
            return arb;
        }
        if (userid != null && !userid.isEmpty() && !userid.equals(info.getUserid())) {
            arb.setCode(102);
            arb.setMsg("不能取消其它用户的订单");
            return arb;
        }
        if (info.getGatherid()!=null && !info.getGatherid().isEmpty()){
            WhgGather gatinfo = this.whgGatherMapper.selectByPrimaryKey(info.getGatherid());
            if (gatinfo != null && gatinfo.getIssuccess()!=null && gatinfo.getIssuccess().intValue() == 1) {
                arb.setCode(102);
                arb.setMsg("不能取消已成功的众筹订单");
                return arb;
            }
        }

        WhgGatherOrder record = new WhgGatherOrder();
        record.setId(info.getId());
        record.setState(0);
        this.whgGatherOrderMapper.updateByPrimaryKeySelective(record);

        return arb;
    }



    /**
     * 我的众筹查询
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectUserGatherList(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgGatherMapper.selectUserOrderGathers(recode);
        if (list!=null){
            for (Map row : list) {
                try {
                    row.put("orderCount", this.getOrderCount(row));

                    String ouserid = row.get("ouserid")!=null? row.get("ouserid").toString(): null;
                    String oid = row.get("oid")!=null? row.get("oid").toString(): null;
                    row.put("orderCount4User", this.getOrderCount(row, ouserid, oid));

                    String etype = (String) row.get("etype");
                    String refid = (String) row.get("refid");
                    if (refid == null || refid.isEmpty() || etype == null || "0".equals(etype)){
                        //转换订单有效无效(判定是否取消的订单)
                        row.put("unionOrderState", row.get("ostate"));
                        if(row.get("unionOrderState")!=null && "0".equals(row.get("unionOrderState").toString())){
                            row.put("orderCount4User",0);
                        }
                        continue;
                    }

                    //活动
                    if (etype.equals(EnumTypeClazz.TYPE_ACTIVITY.getValue())) {
                        //转换订单有效无效(判定是否取消的订单)
                        if (row.get("ostate")!=null && "3".equals(row.get("ostate").toString())){
                            row.put("unionOrderState", 0);
                            row.put("orderCount4User",0);
                        }else{
                            row.put("unionOrderState", 1);
                        }

                        Object refinfo = this.whgActivityMapper.selectByPrimaryKey(refid);
                        BeanMap bm = new BeanMap(refinfo);
                        row.put("refInfo", bm);

                        //场次开始时间
                        WhgActivityTime _time = whgActivityTimeService.findWhgActivityTime4ActId(refid);
                        if (_time != null) {
                            row.put("playStartTime", _time.getPlaystime());
                        }

                        //订单座位
                        Example example = new Example(WhgActivityTicket.class);
                        Example.Criteria criteria = example.createCriteria();
                        criteria.andEqualTo("orderid", row.get("oid"));
                        example.setOrderByClause("seatcode asc");
                        List<WhgActivityTicket> ticketList = whgActTicketMapper.selectByExample(example);
                        //String seatCode = "";
                        StringBuilder seatCode = new StringBuilder();
                        for(int j= 0 ; j<ticketList.size();j++){
                            WhgActivityTicket ticket = ticketList.get(j);
                            if(ticket != null){
                                if (seatCode.length()>0){
                                    seatCode.append(",");
                                }
                                if(0==ticketList.get(j).getTicketstatus()){
                                    //seatCode += ticketList.get(j).getSeatcode()+"(未验票),";
                                    seatCode.append(ticketList.get(j).getSeatcode()+"(未验票)");
                                }else{
                                    //seatCode += ticketList.get(j).getSeatcode()+"(已验票),";
                                    seatCode.append(ticketList.get(j).getSeatcode()+"(已验票)");
                                }
                            }
                        }
                        if (seatCode.length()>0)
                        row.put("seatCode", seatCode.toString());
                    }

                    //培训
                    if (etype.equals(EnumTypeClazz.TYPE_TRAIN.getValue())) {
                        //转换订单有效无效(判定是否取消的订单)
                        if (row.get("ostate")!=null && "2".equals(row.get("ostate").toString())){
                            row.put("unionOrderState", 0);
                            row.put("orderCount4User",0);
                        }else{
                            row.put("unionOrderState", 1);
                        }
                        Object refinfo = this.whgTraMapper.selectByPrimaryKey(refid);
                        row.put("refInfo", refinfo);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
     return new PageInfo(list);
    }

    /**
     * 查用户收藏表相应的众筹列表
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectGatherList4colle(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgGatherMapper.selectGathers4UserColle(recode);
        if (list!=null){
            for (Map row : list) {
                try {
                    row.put("orderCount", this.getOrderCount(row));

                    String etype = (String) row.get("etype");
                    String refid = (String) row.get("refid");
                    if (refid == null || refid.isEmpty() || etype == null || "0".equals(etype)){
                        continue;
                    }

                    //活动
                    if (etype.equals(EnumTypeClazz.TYPE_ACTIVITY.getValue())) {
                        Object refinfo = this.whgActivityMapper.selectByPrimaryKey(refid);
                        row.put("refInfo", refinfo);
                    }

                    //培训
                    if (etype.equals(EnumTypeClazz.TYPE_TRAIN.getValue())) {
                        Object refinfo = this.whgTraMapper.selectByPrimaryKey(refid);
                        row.put("refInfo", refinfo);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return new PageInfo(list);
    }
}
