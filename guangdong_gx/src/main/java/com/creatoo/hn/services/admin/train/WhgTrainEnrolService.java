package com.creatoo.hn.services.admin.train;


import com.creatoo.hn.dao.mapper.WhgTraEnrolMapper;
import com.creatoo.hn.dao.mapper.WhgTraMapper;
import com.creatoo.hn.dao.mapper.WhgTraleaveMapper;
import com.creatoo.hn.dao.mapper.WhgUserMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.api.apiinside.InsMessageService;
import com.creatoo.hn.services.api.apitrain.ApiTraService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBMState;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumProject;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.iterators.ArrayListIterator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 培训报名管理service
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/4/1.
 */
@SuppressWarnings("ALL")
@Service
public class WhgTrainEnrolService {
    /**
     * 培训报名mapper
     */
    @Autowired
    private WhgTraEnrolMapper whgTraEnrolMapper;

    @Autowired
    private WhgTraMapper whgTraMapper;

    @Autowired
    private WhgUserMapper whgUserMapper;

    @Autowired
    private WhgTraleaveMapper whgTraleaveMapper;

    @Autowired
    private WhgSystemCultService service;

    @Autowired
    private ApiTraService traService;

    /**
     * 发送短信service
     */
    @Autowired
    private SMSService smsService;

    @Autowired
    private InsMessageService insMessageService;

    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass());

    public PageInfo t_srchList4pOrders(int page, int rows, Map record) throws Exception{
        PageHelper.startPage(page,rows);
        List list = this.whgTraEnrolMapper.srchListOrders(record);
        return new PageInfo(list);
    }

    public int t_selCountOrder(int i, Map record){
        List stateList = null;
        if (i == 1){  stateList = Arrays.asList(1,2,3,4,5,6); }
        if (i == 2){  stateList = Arrays.asList(1,4,6); }
        if (i == 3){  stateList = Arrays.asList(1); }
        if (i == 4){  stateList = Arrays.asList(4,5,6); }
        if (i == 5){  stateList = Arrays.asList(6); }
        record.put("states", stateList);

        return this.whgTraEnrolMapper.selectOrdersCount(record);
    }

    public List selectTraList4Params(List<String> cultids, List<String> deptids, String biz, String isbasicclass, String state, String islive) throws Exception {
        Example exp = new Example(WhgTra.class);
        Example.Criteria c = exp.createCriteria();
        c.andEqualTo("biz", biz);
        c.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        if (cultids!=null && cultids.size()>0){
            c.andIn("cultid", cultids);
        }
        if (deptids!=null && deptids.size()>0){
            c.andIn("deptid", deptids);
        }
        if (isbasicclass != null) {
            c.andIn("isbasicclass", Arrays.asList(isbasicclass.split(",")));
        }
        if (state != null) {
            c.andEqualTo("state", Integer.parseInt(state));
        }
        if (islive != null && islive.matches("^\\d+$")){
            c.andEqualTo("islive", islive);
        }
        exp.orderBy("crtdate").desc();
        return this.whgTraMapper.selectByExample(exp);
    }


    /**
     *分页查询培训报名数据
     * @param
     * @return
     */
    public PageInfo t_srchUserTraList4p(int page, int rows, String traid, WhgUser whuser) throws Exception {

        Map emap = new HashMap();
        if (whuser != null && whuser.getName() != null) {
            emap.put("name", whuser.getName());
        }
        if (whuser != null && whuser.getNickname() != null) {
            emap.put("nickName", whuser.getNickname());
        }
        if (whuser != null && whuser.getPhone() != null) {
            emap.put("phone", whuser.getPhone());
        }
        if (whuser != null && whuser.getIsrealname() != null) {
            emap.put("isrealname", whuser.getIsrealname());
        }

        PageHelper.startPage(page, rows);
        List list = this.whgTraEnrolMapper.t_srchUserTraList4p(traid, emap);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo t_srchList4p(int page,int rows,String traid,int type,int tab,String state,String contactphone)throws Exception{

        Example example = new Example(WhgTraEnrol.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid",traid);
        if(type == 0 && tab == 0){
            c.andIn("state", Arrays.asList(1,2,3));
        }
        if(tab == 1){
            c.andIn("state", Arrays.asList(4,5,6));
        }
        if(state != null){
            c.andEqualTo("state", Integer.parseInt(state));
        }
        if(contactphone != null){
            c.andLike("contactphone", "%"+contactphone+"%");
        }
        example.setOrderByClause("crttime desc");
        PageHelper.startPage(page,rows);
        List<WhgTraEnrol> list = this.whgTraEnrolMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 查询培训的报名成功的学员列表
     * @param trainId 培训标识
     * @return List<WhgTraEnrol>
     * @throws Exception
     */
    public List<WhgTraEnrol> findEnrolUser(String trainId)throws Exception{
        WhgTraEnrol enrol = new WhgTraEnrol();
        enrol.setState(EnumBMState.BM_CG.getValue());
        enrol.setTraid(trainId);
        Example example = new Example(WhgTraEnrol.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo(enrol);
        example.setOrderByClause("crttime");
        List<WhgTraEnrol> list = this.whgTraEnrolMapper.selectByExample(example);
        return list;
    }

    /**
     * 是否已经报名
     * @param trainId 培训ID
     * @param userId 用户ID
     * @return true-已经报名 false-未报名
     * @throws Exception
     */
    public boolean enrolled(String trainId, String userId)throws Exception{
        WhgTraEnrol enrol = new WhgTraEnrol();
        enrol.setTraid(trainId);
        enrol.setUserid(userId);
        Example example = new Example(WhgTraEnrol.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo(enrol);

        c.andIn("state", Arrays.asList(new Integer[]{EnumBMState.BM_SQ.getValue(), EnumBMState.BM_CG.getValue(), EnumBMState.BM_DDMS.getValue()}));
        int rows  = this.whgTraEnrolMapper.selectCountByExample(example);
        return rows > 0;
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
     * 导出带条件查询
     * @param request
     * @return
     */
    public List<WhgTraEnrolExcel> serch(HttpServletRequest request) {
        Example example = new Example(WhgTraEnrol.class);
        Example.Criteria c = example.createCriteria();
        String traid = request.getParameter("traid");

        WhgTra whgTra = whgTraMapper.selectByPrimaryKey(traid);
        String traName = whgTra.getTitle();

        int type = Integer.parseInt(request.getParameter("type"));
        int tab = Integer.parseInt(request.getParameter("tab"));
        if (type == 0 && tab == 0) {
            c.andIn("state", Arrays.asList(1, 2, 3));
        }
        if (tab == 1) {
            c.andIn("state", Arrays.asList(4, 5, 6));
        }
        c.andEqualTo("traid", traid);
        if (request.getParameter("state") != null && !"".equals(request.getParameter("state"))) {
            int state = Integer.parseInt(request.getParameter("state"));
            c.andEqualTo("state", state);
        }
        if (request.getParameter("contactphone") != null) {
            String contactphone = request.getParameter("contactphone");
            c.andLike("contactphone", "%" + contactphone + "%");
        }
        example.setOrderByClause("crttime desc");
        List<WhgTraEnrol> list = this.whgTraEnrolMapper.selectByExample(example);
        String orderId;
        String realName;
        String cardNo;
        String contactphone;
        Date birthday;
        Date statemdfdate;
        Integer state;
        Integer sex;
        List<WhgTraEnrolExcel> taoist = new ArrayList<>();
        if (list.size() != 0) {
            for (WhgTraEnrol whgTraEnrol : list) {
                orderId = whgTraEnrol.getOrderid();
                realName = whgTraEnrol.getRealname();
                cardNo = whgTraEnrol.getCardno();
                contactphone = whgTraEnrol.getContactphone();
                birthday = whgTraEnrol.getBirthday();
                statemdfdate = whgTraEnrol.getStatemdfdate();
                state = whgTraEnrol.getState();
                sex = whgTraEnrol.getSex();
                WhgUser user = new WhgUser();
                user.setId(whgTraEnrol.getUserid());
                WhgUser uuser = whgUserMapper.selectOne(user);
                WhgTraEnrolExcel cc = new WhgTraEnrolExcel();
                cc.setTraName(traName);
                cc.setOrderid(orderId);
                if (uuser != null && uuser.getIsrealname() == 1) {
                    cc.setUserid(uuser.getName());//用真实姓名替代
                }
                cc.setCardno(cardNo);
                cc.setRealname(whgTraEnrol.getRealname());
                cc.setContactphone(contactphone);
                cc.setBirthday(birthday);
                cc.setSex(sex);
                cc.setStatemdfdate(statemdfdate);
                cc.setState(state);
                taoist.add(cc);
            }
        }
        return taoist;
    }

    /**
     * 修改状态
     * @param ids
     * @param fromstate
     * @param tostate
     * @param sysUser
     * @return
     */
    public ResponseBean t_updstate(String statedesc, String ids, String fromstate, int tostate, WhgSysUser sysUser, String viewtime, String viewaddress)throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ResponseBean rb = new ResponseBean();
        String[] enrollid = ids.split("\\s*,\\s*");

        if (ids == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("培训报名信息主键丢失");
            return rb;
        }
        //修改状态
        Example example = new Example(WhgTraEnrol.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( fromstate.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        List<WhgTraEnrol> list = this.whgTraEnrolMapper.selectByExample(example);
        String title = "";
        String noticetype = "";
        String biz = "";
        Integer isbasicclass = 0;
        int basicNumber = 0;
        Date starttime = new Date();
        Date endtime = new Date();
        if(list.size() != 0){
            String traid = list.get(0).getTraid();
            WhgTra tra = this.whgTraMapper.selectByPrimaryKey(traid);
            noticetype = tra.getNoticetype();
            isbasicclass = tra.getIsbasicclass();
            biz = tra.getBiz();
            title = tra.getTitle();
            starttime = tra.getStarttime();
            endtime = tra.getEndtime();
            basicNumber = tra.getBasicenrollnumber();
            Example enrol = new Example(WhgTraEnrol.class);
            enrol.createCriteria().andEqualTo("state",6).andEqualTo("traid",tra.getId());
            int count = whgTraEnrolMapper.selectCountByExample(enrol);

            if((tostate==6) &&(basicNumber - count) < enrollid.length){
                rb.setSuccess(ResponseBean.FAIL);
                rb.setErrormsg(title+"的基础报名人数是"+basicNumber+"，选择的人数已超过基础报名人数");
                return rb;
            }
        }else{
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("找不到相应的报名记录");
            return rb;
        }

        log.debug("SMS START===========================================================");
        for(int j=0; j<list.size(); j++){
            try {
                Date now = new Date();

                WhgTraEnrol whgTraEnrol = list.get(j);
                String phone = whgTraEnrol.getContactphone();
                String touserid = whgTraEnrol.getUserid();
                String name = whgTraEnrol.getRealname();
                String traid = whgTraEnrol.getTraid();
                Map<String,String> _map = new HashMap<>();
                _map.put("userName",name);
                _map.put("title",title);
                _map.put("starttime",sdf.format(starttime));
                _map.put("endtime",sdf.format(endtime));
                _map.put("address",viewaddress);
                log.debug("SMS 1============phone:"+phone+"===================_map:"+_map+"============================");
                String tempCode = "";
               if("PT".equals(biz)){
                   if(tostate == 4){
                       String _mstime = new SimpleDateFormat("yyyy-MM-dd(EEEE)HH:mm", Locale.CHINA).format(sdf1.parse(viewtime));
                       //       new SimpleDateFormat("yyyy-MM-dd(EEE)ahh:mm").format(sdf1.parse(viewtime));
                       _map.put("date",_mstime);
                       tempCode = "TRA_CHECK_PASS";
                   }
                   if(tostate == 3){
                       tempCode = "TRA_CHECK_FAIL";
                   }
                   if(tostate == 5){
                       tempCode = "TRA_VIEW_FAIL";
                   }
                   if(tostate == 6){
                       tempCode = "TRA_VIEW_PASS";
                   }
                   if(tostate == 2){
                       tempCode = "TRA_CANCEL";
                   }
               }else {
                   if(isbasicclass == 1){
                       if(tostate == 3){
                           tempCode = "ZC_TRA_CHECKFAIL";
                       }else if(tostate == 6){
                           tempCode = "ZC_TRA_CHECKTRUE";
                       }
                   }if(isbasicclass == 0){
                       if(tostate == 4){
                       String _mstime = new SimpleDateFormat("yyyy-MM-dd(EEEE)HH:mm", Locale.CHINA).format(sdf1.parse(viewtime));
                       _map.put("date",_mstime);
                       tempCode = "ZC_TRA_TRUE3";
                        }else if(tostate == 3){
                           tempCode = "ZC_TRA_CHECKFAIL";
                       }else if(tostate == 6){
                           tempCode = "ZC_TRA_VIEWTRUE";
                       }else if(tostate == 5){
                       tempCode = "ZC_TRA_VIEWFAIL";
                   }
                   }

//                   if(tostate == 4){
//                       String _mstime = new SimpleDateFormat("yyyy-MM-dd(EEEE)hh:mm", Locale.CHINA).format(sdf1.parse(viewtime));
//                       //       new SimpleDateFormat("yyyy-MM-dd(EEE)ahh:mm").format(sdf1.parse(viewtime));
//                       _map.put("mstime",_mstime);
//                       tempCode = "TRA_CHECK_PASS";
//                   }
//                   if(tostate == 3){
//                       tempCode = "TRA_CHECK_FAIL";
//                   }
//                   if(tostate == 5){
//                       tempCode = "TRA_VIEW_FAIL";
//                   }
//                   if(tostate == 6){
//                       tempCode = "TRA_VIEW_PASS";
//                   }
//                   if(tostate == 2){
//                       tempCode = "TRA_CANCEL";
//                   }
               }

                String projectBiz = EnumProject.PROJECT_WLPX.getValue();
                if (!"".equals(biz) && biz.equals("ZC")) {
                    projectBiz = EnumProject.PROJECT_ZC.getValue();
                }

                log.debug("SMS 2=============tempCode:"+tempCode+"========phone:"+phone+"===================_map:"+_map+"============================");
                if(noticetype != null && "ZNX".equals(noticetype)){
                    insMessageService.t_sendZNX(touserid, sysUser.getId(), tempCode, _map, traid, projectBiz);
                }else if(noticetype != null && "SMS".equals(noticetype)){
                    this.smsService.t_sendSMS(phone,tempCode,_map, traid);
                }else {
                    insMessageService.t_sendZNX(touserid, sysUser.getId(), tempCode, _map, traid, projectBiz);
                    this.smsService.t_sendSMS(phone,tempCode,_map, traid);
                }

                log.debug("SMS 3=============tempCode:"+tempCode+"========phone:"+phone+"===================_map:"+_map+"============================");
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
        log.debug("SMS END===========================================================");


        for(int i = 0; i<list.size(); i++){
            list.get(i).setState(tostate);
            list.get(i).setStatemdfdate(new Date());
            list.get(i).setStatedesc(statedesc);
            list.get(i).setStatemdfuser(sysUser.getId());
            this.whgTraEnrolMapper.updateByPrimaryKeySelective(list.get(i));
        }
        return rb;
    }


    /**
     * 查询报名人数
     * @param id
     * @param i(1、总报名数 2、有效的报名数 3、未处理的报名数 4、面试总人数 5、成功录取人数)
     * @return
     */
    public int t_selCount(String id, int i) {
        Example example = new Example(WhgTraEnrol.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid",id);
        if(i == 1){
            c.andIn("state",Arrays.asList(1,2,3,4,5,6));
        }
        if(i == 2){
            c.andIn("state",Arrays.asList(1,4,6));
        }
        if(i == 3){
            c.andEqualTo("state",1);
        }
        if(i == 4){
            c.andIn("state",Arrays.asList(4,5,6));
        }
        if(i == 5){
            c.andEqualTo("state",6);
        }
        return this.whgTraEnrolMapper.selectCountByExample(example);
    }


    /**
     * 随机报名
     * @param ids
     * @param fromstate
     * @param tostate
     * @return
     */
    public PageInfo ramEnroll2(String ids, int number, int page, int rows) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ResponseBean res = new ResponseBean();
        //拿到所选的未处理的报名记录
        Example example = new Example(WhgTraEnrol.class);
        example.createCriteria().andIn("traid", Arrays.asList(ids.split("\\s*,\\s*"))).andEqualTo("state", 1);

        List<WhgTraEnrol> list = whgTraEnrolMapper.selectByExample(example);
        PageHelper.startPage(page, rows);
        List<WhgTraEnrol> list2 = null;
        if (number < list.size()) {
            list2 = getRandomEnrolList(number, list);
        } else {
            list2 = getRandomEnrolList(list.size(), list);
        }

        return new PageInfo(list2);
    }

    public ResponseBean addorder(String ids, String traid, WhgSysUser sysUser) throws Exception {
        ResponseBean res = new ResponseBean();
        WhgTra train = whgTraMapper.selectByPrimaryKey(traid);
        //if (train == null || train.getState().intValue() != 6) {
        if (train == null) {
            res.setSuccess("102");
            res.setErrormsg("培训不存在");
            return res;
        }
        String[] str = ids.split(",");

        // 用户订单去重查询
        Example enrs = new Example(WhgTraEnrol.class);
        enrs.createCriteria().andIn("userid", Arrays.asList(str)).andEqualTo("traid", train.getId());
        int enrscount = whgTraEnrolMapper.selectCountByExample(enrs);
        if (enrscount > 0) {
            res.setSuccess("103");
            res.setErrormsg("曾报名过的用户不能重复新增");
            return res;
        }
        int len = 0;
        int basicNumber = train.getBasicenrollnumber();
        Example enr = new Example(WhgTraEnrol.class);
        enr.createCriteria().andEqualTo("state", 6).andEqualTo("traid", train.getId());
        int count = whgTraEnrolMapper.selectCountByExample(enr);
        for (int i = 0; i < str.length; i++) {
            WhgUser user = new WhgUser();
            user.setId(str[i]);
            WhgUser uuser = whgUserMapper.selectOne(user);
            if (uuser != null) {
                WhgTraEnrol enrol = new WhgTraEnrol();
                enrol.setBirthday(uuser.getBirthday());
                enrol.setTraid(traid);
                enrol.setRealname(uuser.getName());
                enrol.setCardno(uuser.getIdcard());
                enrol.setContactphone(uuser.getPhone());
                addTranEnrol2(enrol, uuser, train, sysUser);
                len++;
            }
        }
        int lencount = count + len;
        if (lencount > basicNumber) {//超出报名数 修改培训信息
            train.setBasicenrollnumber(lencount);
            this.whgTraMapper.updateByPrimaryKeySelective(train);
        }

        return res;
    }

    /**
     * 添加特殊报名
     *
     * @param enrol
     * @param userid
     * @return
     * @throws Exception
     */
    public int addTranEnrol2(WhgTraEnrol enrol, WhgUser user, WhgTra train, WhgSysUser sysUser) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String id = IDUtils.getID();
        //订单号和id保持一致
        enrol.setId(id);
        enrol.setOrderid(id);
        Date now = Calendar.getInstance().getTime();
        enrol.setBirthday(enrol.getBirthday());
        if (user.getSex() != null && !"".equals(user.getSex())) {
            enrol.setSex(Integer.parseInt(user.getSex()));
        }
        if (train.getIsbasicclass() == 1) {//人工录取
            enrol.setState(6);
        } else if (train.getIsbasicclass() == 0) {//面试
            enrol.setState(4);
        } else {
            enrol.setState(6);
        }
        enrol.setStatemdfdate(now);
        enrol.setStatemdfuser(user.getId());
        enrol.setUserid(user.getId());
        enrol.setCrttime(now);
        int result = whgTraEnrolMapper.insertSelective(enrol);
        if (result > 0 && train.getIsbasicclass() == 0) {//面试 短信
            Map<String, String> _map = new HashMap<>();
            _map.put("userName", user.getName());
            _map.put("title", train.getTitle());
            _map.put("starttime", sdf.format(train.getStarttime()));
            _map.put("endtime", sdf.format(train.getEndtime()));
            String tempCode = "TRA_CHECK_PASS";
            String projectBiz = EnumProject.PROJECT_WLPX.getValue();
            if (train.getBiz() != null && train.getBiz().equals("ZC")) {
                projectBiz = EnumProject.PROJECT_ZC.getValue();
            }
            if (train.getNoticetype() != null && "ZNX".equals(train.getNoticetype())) {
                insMessageService.t_sendZNX(enrol.getUserid(), sysUser.getId(), tempCode, _map, enrol.getTraid(), projectBiz);
            } else if (train.getNoticetype() != null && "SMS".equals(train.getNoticetype())) {
                this.smsService.t_sendSMS(enrol.getContactphone(), tempCode, _map, enrol.getTraid());
            } else {
                insMessageService.t_sendZNX(enrol.getUserid(), sysUser.getId(), tempCode, _map, enrol.getTraid(), projectBiz);
                this.smsService.t_sendSMS(enrol.getContactphone(), tempCode, _map, enrol.getTraid());
            }
        } else if (result > 0) {//录取短信
            Map<String, String> _map = new HashMap<>();
            _map.put("userName", user.getName());
            _map.put("title", train.getTitle());
            _map.put("starttime", sdf.format(train.getStarttime()));
            _map.put("endtime", sdf.format(train.getEndtime()));
            String tempCode = "TRA_VIEW_PASS";
            String projectBiz = EnumProject.PROJECT_WLPX.getValue();
            if (train.getBiz() != null && train.getBiz().equals("ZC")) {
                projectBiz = EnumProject.PROJECT_ZC.getValue();
            }
            if (train.getNoticetype() != null && "ZNX".equals(train.getNoticetype())) {
                insMessageService.t_sendZNX(enrol.getUserid(), sysUser.getId(), tempCode, _map, enrol.getTraid(), projectBiz);
            } else if (train.getNoticetype() != null && "SMS".equals(train.getNoticetype())) {
                this.smsService.t_sendSMS(enrol.getContactphone(), tempCode, _map, enrol.getTraid());
            } else {
                insMessageService.t_sendZNX(enrol.getUserid(), sysUser.getId(), tempCode, _map, enrol.getTraid(), projectBiz);
                this.smsService.t_sendSMS(enrol.getContactphone(), tempCode, _map, enrol.getTraid());
            }
        }
        return result;
    }

    public ResponseBean ramEnrolList(String ids, String fromstate, int tostate, WhgSysUser sysUser) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ResponseBean res = new ResponseBean();
        //拿到所选的未处理的报名记录
        Example example = new Example(WhgTraEnrol.class);
        example.createCriteria().andIn("id", Arrays.asList(ids.split("\\s*,\\s*"))).andEqualTo("state", 1);
        List<WhgTraEnrol> list = whgTraEnrolMapper.selectByExample(example);

        List<WhgTraEnrol> updateList = new ArrayList<WhgTraEnrol>();
        int basicenrollnumber = 0;
        int trabasicnumber = 0;
        String noticetype = "";
        WhgTra tra = new WhgTra();
        if (list.size() > 0) {
            //拿到这个培训已经通过报名的记录数量
            String traid = list.get(0).getTraid();
            tra = this.whgTraMapper.selectByPrimaryKey(traid);
            noticetype = tra.getNoticetype();
            trabasicnumber = tra.getBasicenrollnumber();
        }
        updateList = list;
        //更新报名表状态
        for (int i = 0; i < updateList.size(); i++) {
            WhgTraEnrol whgTraEnrol = updateList.get(i);
            String tempCode = "TRA_VIEW_PASS";
            if (tra.getIsbasicclass()!=null && tra.getIsbasicclass().intValue() == 1) {//人工录取
                whgTraEnrol.setState(6);
            } else if (tra.getIsbasicclass()!=null && tra.getIsbasicclass().intValue() == 0) {//面试
                tempCode = "TRA_CHECK_PASS";
                whgTraEnrol.setState(4);
            } else {
                whgTraEnrol.setState(6);
            }
            whgTraEnrol.setStatemdfdate(new Date());
            whgTraEnrol.setStatemdfuser(sysUser.getId());
            this.whgTraEnrolMapper.updateByPrimaryKeySelective(updateList.get(i));
            try {
                //发送短信
                Map<String, String> _map = new HashMap<>();
                _map.put("userName", updateList.get(i).getRealname());
                _map.put("title", tra.getTitle());
                _map.put("starttime", sdf.format(tra.getStarttime()));
                _map.put("endtime", sdf.format(tra.getEndtime()));

                String projectBiz = EnumProject.PROJECT_WLPX.getValue();
                if (tra.getBiz() != null && tra.getBiz().equals("ZC")) {
                    projectBiz = EnumProject.PROJECT_ZC.getValue();
                }
            /*if (noticetype != null && "ZNX".equals(noticetype)) {
                insMessageService.t_sendZNX(whgTraEnrol.getUserid(), sysUser.getId(), tempCode, _map, whgTraEnrol.getTraid(), projectBiz);
            } else if (noticetype != null && "SMS".equals(noticetype)) {
                this.smsService.t_sendSMS(whgTraEnrol.getContactphone(), tempCode, _map, whgTraEnrol.getTraid());
            } else {
                insMessageService.t_sendZNX(whgTraEnrol.getUserid(), sysUser.getId(), tempCode, _map, whgTraEnrol.getTraid(), projectBiz);
                this.smsService.t_sendSMS(whgTraEnrol.getContactphone(), tempCode, _map, whgTraEnrol.getTraid());
            }*/
                if (noticetype != null && noticetype.contains("ZNX")){
                    insMessageService.t_sendZNX(whgTraEnrol.getUserid(), sysUser.getId(), tempCode, _map, whgTraEnrol.getTraid(), projectBiz);
                }
                if (noticetype != null && noticetype.contains("SMS")){
                    this.smsService.t_sendSMS(whgTraEnrol.getContactphone(), tempCode, _map, whgTraEnrol.getTraid());
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            //this.smsService.t_sendSMS(updateList.get(i).getContactphone(),tempCode,_map, whgTraEnrol.getTraid());
        }
        //设置摇号标志
        tra.setIsyaohao(1);
        whgTraMapper.updateByPrimaryKeySelective(tra);
        if (tra != null && tra.getId() != null) {
            Example example2 = new Example(WhgTraEnrol.class);
            example2.createCriteria().andNotIn("id", Arrays.asList(ids.split("\\s*,\\s*"))).andEqualTo("state", 1).andEqualTo("traid", tra.getId());
            List<WhgTraEnrol> list2 = whgTraEnrolMapper.selectByExample(example2);
            if (list2.size() > 0) {
                //拿到这个培训已经通过报名的记录数量
                String traid = list2.get(0).getTraid();
                tra = this.whgTraMapper.selectByPrimaryKey(traid);
                noticetype = tra.getNoticetype();
                trabasicnumber = tra.getBasicenrollnumber();
            }
            //更新报名表状态
            for (int i = 0; i < list2.size(); i++) {
                WhgTraEnrol whgTraEnr = list2.get(i);
                /*if (tra.getIsbasicclass() == 1) {//人工录取
                    whgTraEnr.setState(3);//审核失败
                } else if (tra.getIsbasicclass() == 0) {//面试
                    whgTraEnr.setState(3);//审核失败
                } else {*/
                    whgTraEnr.setState(3);//审核失败
                //}
                whgTraEnr.setStatemdfdate(new Date());
                whgTraEnr.setStatemdfuser(sysUser.getId());
                whgTraEnr.setStatedesc("没有摇中号");
                this.whgTraEnrolMapper.updateByPrimaryKeySelective(whgTraEnr);
                try {
                    //发送短信
                    Map<String, String> _map = new HashMap();
                    _map.put("userName", list2.get(i).getRealname());
                    _map.put("title", tra.getTitle());
                    if (tra.getCultid() != null) {
                        WhgSysCult cult = service.t_srchOne(tra.getCultid());
                        if (cult != null) {
                            _map.put("whgCult", cult.getName());
                        }
                    }
                    _map.put("starttime", sdf.format(tra.getStarttime()));
                    _map.put("endtime", sdf.format(tra.getEndtime()));
                    //String tempCode = "TRA_VIEW_NOPASS";
                    String tempCode = "TRA_CHECK_FAIL";
                    String projectBiz = EnumProject.PROJECT_WLPX.getValue();
                    if (tra.getBiz() != null && tra.getBiz().equals("ZC")) {
                        projectBiz = EnumProject.PROJECT_ZC.getValue();
                    }
                /*if (noticetype != null && "ZNX".equals(noticetype)) {
                    insMessageService.t_sendZNX(whgTraEnr.getUserid(), sysUser.getId(), tempCode, _map, whgTraEnr.getTraid(), projectBiz);
                } else if (noticetype != null && "SMS".equals(noticetype)) {
                    this.smsService.t_sendSMS(whgTraEnr.getContactphone(), tempCode, _map, whgTraEnr.getTraid());
                } else {
                    insMessageService.t_sendZNX(whgTraEnr.getUserid(), sysUser.getId(), tempCode, _map, whgTraEnr.getTraid(), projectBiz);
                    this.smsService.t_sendSMS(whgTraEnr.getContactphone(), tempCode, _map, whgTraEnr.getTraid());
                }*/
                    if (noticetype != null && noticetype.contains("ZNX")){
                        insMessageService.t_sendZNX(whgTraEnr.getUserid(), sysUser.getId(), tempCode, _map, whgTraEnr.getTraid(), projectBiz);
                    }
                    if (noticetype != null && noticetype.contains("SMS")){
                        this.smsService.t_sendSMS(whgTraEnr.getContactphone(), tempCode, _map, whgTraEnr.getTraid());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

                //this.smsService.t_sendSMS(updateList.get(i).getContactphone(),tempCode,_map, whgTraEnrol.getTraid());
            }
        }
        return res;
    }


    public ResponseBean ramEnroll(String ids, String fromstate, int tostate, WhgSysUser sysUser) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ResponseBean res = new ResponseBean();
        //拿到所选的未处理的报名记录
        Example example = new Example(WhgTraEnrol.class);
        example.createCriteria().andIn("id", Arrays.asList(ids.split("\\s*,\\s*"))).andEqualTo("state", 1);
        List<WhgTraEnrol> list = whgTraEnrolMapper.selectByExample(example);

        List<WhgTraEnrol> updateList = new ArrayList<WhgTraEnrol>();
        int basicenrollnumber = 0;
        int trabasicnumber = 0;
        String noticetype = "";
        WhgTra tra = new WhgTra();
        if(list.size() > 0){
            //拿到这个培训已经通过报名的记录数量
            Example example1 = new Example(WhgTraEnrol.class);
            example1.createCriteria().andEqualTo("traid",list.get(0).getTraid()).andEqualTo("state",6);
            int count = whgTraEnrolMapper.selectCountByExample(example1);

            String traid = list.get(0).getTraid();
            tra = this.whgTraMapper.selectByPrimaryKey(traid);
            noticetype = tra.getNoticetype();
            trabasicnumber = tra.getBasicenrollnumber();
            //如果通过的数量不等于0 那么需要一键通过的名单数量为基础人数减去通过的人数
            if(count != 0){
                basicenrollnumber = trabasicnumber - count;
            }else{
                basicenrollnumber = trabasicnumber;
            }
        }
        if(basicenrollnumber <= list.size()){
            //取随机报名集合
            updateList = getRandomEnrolList(basicenrollnumber,list);
        }else{
            updateList = list;
        }
        //更新报名表状态
        for (int i = 0; i<updateList.size(); i++){
            WhgTraEnrol whgTraEnrol = updateList.get(i);
            whgTraEnrol.setState(tostate);
            whgTraEnrol.setStatemdfdate(new Date());
            whgTraEnrol.setStatemdfuser(sysUser.getId());
            this.whgTraEnrolMapper.updateByPrimaryKeySelective(updateList.get(i));
            //发送短信
            Map<String,String> _map = new HashMap<>();
            _map.put("userName",updateList.get(i).getRealname());
            _map.put("title",tra.getTitle());
            _map.put("starttime",sdf.format(tra.getStarttime()));
            _map.put("endtime",sdf.format(tra.getEndtime()));
            String tempCode = "TRA_VIEW_PASS";
            String projectBiz = EnumProject.PROJECT_WLPX.getValue();
            if (tra.getBiz() != null && tra.getBiz().equals("ZC")) {
                projectBiz = EnumProject.PROJECT_ZC.getValue();
            }
            if(noticetype != null && "ZNX".equals(noticetype)){
                insMessageService.t_sendZNX(whgTraEnrol.getUserid(), sysUser.getId(), tempCode, _map, whgTraEnrol.getTraid(), projectBiz);
            }else if(noticetype != null && "SMS".equals(noticetype)){
                this.smsService.t_sendSMS(updateList.get(i).getContactphone(),tempCode,_map, whgTraEnrol.getTraid());
            }else {
                insMessageService.t_sendZNX(whgTraEnrol.getUserid(), sysUser.getId(), tempCode, _map, whgTraEnrol.getTraid(), projectBiz);
                this.smsService.t_sendSMS(updateList.get(i).getContactphone(),tempCode,_map, whgTraEnrol.getTraid());
            }
            //this.smsService.t_sendSMS(updateList.get(i).getContactphone(),tempCode,_map, whgTraEnrol.getTraid());
        }
        return res;
    }

    /**
     * 随机报名集合
     * @param maxNumber
     * @param list
     * @return
     */
    private List<WhgTraEnrol> getRandomEnrolList(int maxNumber,List<WhgTraEnrol> list){
        if(maxNumber == 0 || list.size() == 0) {
            return Collections.emptyList();
        }
        List<WhgTraEnrol> randomList = new ArrayList<WhgTraEnrol>();
        int index = 0;
        while (randomList.size() < maxNumber){
            index = (int) Math.round((Math.random() * (list.size() - 1)));
            randomList.add(list.get(index));
            list.remove(index);
        }
        return randomList;
    }

    /**
     * 重发面试短信
     * @param id
     * @param viewtime
     * @param viewaddress
     * @return
     * @throws Exception
     */
    public void t_sendMsg(String id, String viewtime, String viewaddress) throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String _mstime = new SimpleDateFormat("yyyy-MM-dd(EEEE)HH:mm", Locale.CHINA).format(sdf.parse(viewtime));
        //new SimpleDateFormat("yyyy-MM-dd("+new SimpleDateFormat("EEEE").format(sdf.parse(viewtime))+")ahh:mm").format(sdf.parse(viewtime));
        WhgTraEnrol enrol = whgTraEnrolMapper.selectByPrimaryKey(id);
        String traid = enrol.getTraid();
        WhgTra tra = this.whgTraMapper.selectByPrimaryKey(traid);
        String phone = enrol.getContactphone();
        String name = enrol.getRealname();
        Map<String,String> _map = new HashMap<>();
        _map.put("userName",name);
        _map.put("title",tra.getTitle());
        _map.put("starttime",sdf.format(tra.getStarttime()));
        _map.put("endtime",sdf.format(tra.getEndtime()));
        _map.put("date",_mstime);
        _map.put("address",viewaddress);
        String tempCode = "TRA_CHECK_PASS";
        String projectBiz = EnumProject.PROJECT_WLPX.getValue();
        if (tra.getBiz() != null && tra.getBiz().equals("ZC")) {
            projectBiz = EnumProject.PROJECT_ZC.getValue();
        }
        if(tra.getNoticetype() != null && "ZNX".equals(tra.getNoticetype())){
            insMessageService.t_sendZNX(enrol.getUserid(), null, tempCode, _map, enrol.getTraid(), projectBiz);
        }else if(tra.getNoticetype() != null && "SMS".equals(tra.getNoticetype())){
            this.smsService.t_sendSMS(phone,tempCode,_map, enrol.getTraid());
        }else {
            insMessageService.t_sendZNX(enrol.getUserid(), null, tempCode, _map, enrol.getTraid(), projectBiz);
            this.smsService.t_sendSMS(phone,tempCode,_map, enrol.getTraid());
        }
        //this.smsService.t_sendSMS(phone,tempCode,_map, enrol.getTraid());
    }

    /**
     * 查询请假信息
     * @param id
     * @param userid
     * @return
     */
    public ResponseBean selLeave(String id,String userid) throws Exception{
        Example example = new Example(WhgTraleave.class);
        ResponseBean rsb = new ResponseBean();
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("courseid",id).andEqualTo("userid",userid);
        List<WhgTraleave> list = this.whgTraleaveMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            rsb.setData(list.get(0));
        }else{
            rsb.setSuccess(ResponseBean.FAIL);
        }
        return rsb;
    }

    /**
     *
     * @param leave
     */
    public ResponseBean t_editLeave(WhgTraleave leave) {
        ResponseBean rsb = new ResponseBean();
        if(leave.getId() == null || "".equals(leave.getId())){
            rsb.setSuccess(ResponseBean.FAIL);
            rsb.setErrormsg("主键丢失");
            return rsb;
        }
        this.whgTraleaveMapper.updateByPrimaryKeySelective(leave);
        return rsb;
    }

    /**
     * 众筹培训发送短信
     */
    public void sendSmsTra(WhgTraEnrol traenrol, boolean success){
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String traid=traenrol.getTraid();
            WhgTra tra = this.whgTraMapper.selectByPrimaryKey(traid);
            Map<String,String> _map = new HashMap<>();
            _map.put("userName",traenrol.getRealname());
            _map.put("title",tra.getTitle());
            _map.put("starttime",sdf.format(tra.getStarttime()));
            _map.put("endtime",sdf.format(tra.getEndtime()));
            String tempCode = "";
            if(success){
                if(tra.getIsbasicclass() == 2){
                    tempCode = "ZC_TRA_TRUE1";
                }else if(tra.getIsbasicclass() == 1){
                    tempCode = "ZC_TRA_TRUE2";
                }else if(tra.getIsbasicclass() == 0){
                    tempCode = "ZC_TRA_TRUE2";
                }
            }else {
                tempCode = "ZC_Failure";
            }


            if(tra.getBiz()!=null) {//众筹活动
                if(tra.getNoticetype()!=null && tra.getNoticetype().equals("ZNX")){//站内信
                    if(success){//众筹成功
                        insMessageService.t_sendZNX(traenrol.getUserid(),null, tempCode, _map, traid, EnumProject.PROJECT_ZC.getValue());
                    }else{
                        insMessageService.t_sendZNX(traenrol.getUserid(),null, tempCode, _map, traid, EnumProject.PROJECT_ZC.getValue());
                    }
                }else if(tra.getNoticetype()!=null && tra.getNoticetype().equals("SMS")){//短信
                    if(success){//众筹成功
                        smsService.t_sendSMS(traenrol.getContactphone(), tempCode, _map, traid);
                    }else{
                        smsService.t_sendSMS(traenrol.getContactphone(), tempCode, _map, traid);
                    }

                }else{//短信
                    if(success){//众筹成功
                        smsService.t_sendSMS(traenrol.getContactphone(), tempCode, _map, traid);
                        insMessageService.t_sendZNX(traenrol.getUserid(),null, tempCode, _map, traid, EnumProject.PROJECT_ZC.getValue());
                    }else{
                        insMessageService.t_sendZNX(traenrol.getUserid(),null, tempCode, _map, traid, EnumProject.PROJECT_ZC.getValue());
                        smsService.t_sendSMS(traenrol.getContactphone(), tempCode, _map, traid);
                    }

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
