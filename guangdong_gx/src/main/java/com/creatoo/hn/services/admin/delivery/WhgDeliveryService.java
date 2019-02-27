package com.creatoo.hn.services.admin.delivery;

import com.creatoo.hn.dao.mapper.CrtWhgDeliveryMapper;
import com.creatoo.hn.dao.mapper.WhgDeliveryTimeMapper;
import com.creatoo.hn.dao.mapper.api.ExtendDeliveryMapper;
import com.creatoo.hn.dao.model.WhgDelivery;
import com.creatoo.hn.dao.model.WhgDeliveryTime;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 配送 service
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgDelivery", keyGenerator = "simpleKeyGenerator")
public class WhgDeliveryService extends BaseService {
    
    @Autowired
    private CrtWhgDeliveryMapper crtDeliveryMapper;

    @Autowired
    private WhgDeliveryTimeMapper whgDeliveryTimeMapper ;

    @Autowired
    private ExtendDeliveryMapper extendDeliveryMapper;

    @Autowired
    private SMSService smsService;

    @Autowired
    private WhgSystemCultService cultService;


    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @Cacheable
    public PageInfo<WhgDelivery> t_srchList4p(HttpServletRequest request, WhgDelivery cult) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgDelivery.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (cult != null && cult.getName() != null) {
            c.andLike("name", "%" + cult.getName() + "%");
            cult.setName(null);
        }

        String pageType = request.getParameter("type");
        //编辑列表
        if ("edit".equalsIgnoreCase(pageType)){
            c.andEqualTo("state", 0);
        }
        //审核列表，查 9待审核
        if ("check".equalsIgnoreCase(pageType)){
            c.andEqualTo("state", 1);
        }
        //发布列表，2 配送中 3配送成功
        if ("publish".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(2,3));
        }
        //删除列表，查已删除 否则查未删除的
        if ("recycle".equalsIgnoreCase(pageType)){
            c.andEqualTo("delState", 1);
        }else{
            c.andEqualTo("delState", 0);
        }
        if(request.getParameter("state") != null){
            int state = Integer.parseInt(request.getParameter("state"));
            c.andEqualTo("state", state);
        }

        example.setOrderByClause("crtdate desc");

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgDelivery> typeList = this.crtDeliveryMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @SuppressWarnings("all")
    public PageInfo<WhgDelivery> t_srchList4p(HttpServletRequest request, WhgDelivery cult,List<Map> relList, WhgSysUser user) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgDelivery.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (cult != null && cult.getName() != null) {
            paramMap.put("name",cult.getName());
        }

        String pageType = request.getParameter("type");

        //删除列表，查已删除 否则查未删除的
        if ("recycle".equalsIgnoreCase(pageType)){
            paramMap.put("delstate", 1);
        }else{
            paramMap.put("delstate", 0);
        }
        if(request.getParameter("state") != null){
            int state = Integer.parseInt(request.getParameter("state"));
            paramMap.put("state", state);
        }
        if(cult != null && cult.getType() != null) {
            paramMap.put("type", cult.getType());
        }
        if(cult != null&&cult.getCrtuser()!=null){
            paramMap.put("crtuser", cult.getCrtuser());
        }
        if(cult != null&&cult.getFktype()!=null){
            paramMap.put("fktype", cult.getFktype());
        }

        paramMap.put("touser", user.getId());
        //分页查询
        PageHelper.startPage(page, rows);
        List typeList = crtDeliveryMapper.srchListDelivery(paramMap);
        return new PageInfo<>(typeList);
    }

    /**
     * 查询单条记录
     *
     * @param id id
     * @return 对象
     * @throws Exception
     */
    public WhgDelivery t_srchOne(String id) throws Exception {
        return this.crtDeliveryMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询配送通过数目
     *
     * @param id id
     * @return 对象
     * @throws Exception
     */
    public int t_srchSucDelivery(String id) throws Exception {
        Example example = new Example(WhgDelivery.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("fkid", id);
        c.andEqualTo("delstate", 0);//未删除
        c.andIn("state", Arrays.asList(2,3));// 审核通过 配送成功
        return this.crtDeliveryMapper.selectCountByExample(example);
    }

    /**
     * 查询申请配送的文化馆
     *
     * @param id id
     * @return 对象
     * @throws Exception
     */
    public PageInfo srch_cult_delivery(int page, int pageSize, Map paramMap ) throws Exception {
        PageHelper.startPage(page, pageSize);
        List typeList = crtDeliveryMapper.srchListDelivery(paramMap);
        return new PageInfo(typeList);
    }

    /**
     * 查询单条记录
     *
     * @param id id
     * @return 对象
     * @throws Exception
     */
    public List<WhgDeliveryTime> getPstime(String id) throws Exception {
        Example example = new Example(WhgDeliveryTime.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("deliveryid", id);
        example.orderBy("deliverytime").asc();
        return this.whgDeliveryTimeMapper.selectByExample(example);
    }

    /**
     * 编辑
     *
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgDelivery cultHeritage,String pstime) throws Exception {
        cultHeritage.setStatemdfdate(new Date());
        int result = this.crtDeliveryMapper.updateByPrimaryKeySelective(cultHeritage);
        String strs[]=pstime.split("\\s*,\\s*");
            if(strs.length>0) {
                Example example = new Example(WhgDeliveryTime.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("deliveryid",cultHeritage.getId()) ;
                whgDeliveryTimeMapper.deleteByExample(example);
                for (String d : strs) {
                    WhgDeliveryTime deliveryTime = new WhgDeliveryTime();
                    deliveryTime.setId(IDUtils.getID());
                    deliveryTime.setDeliveryid(cultHeritage.getId());
                    deliveryTime.setDeliveryfkid(cultHeritage.getFkid());
                    deliveryTime.setDeliverytime(d);
                    if (cultHeritage.getState() == 2) {
                        deliveryTime.setState(1);//通过
                    } else {
                        deliveryTime.setState(0);//驳回
                    }
                    whgDeliveryTimeMapper.insert(deliveryTime);
                }
            }
        if (result != 1) {
            throw new Exception("编辑数据失败！");
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception {
        WhgDelivery cultHeritage = crtDeliveryMapper.selectByPrimaryKey(id);
        if (cultHeritage == null) {
            return;
        }
        if (cultHeritage.getDelstate() != null && cultHeritage.getDelstate().compareTo(1) == 0) {
            this.crtDeliveryMapper.deleteByPrimaryKey(id);
        } else {
            cultHeritage = new WhgDelivery();
            cultHeritage.setId(id);
            cultHeritage.setDelstate(1);
            cultHeritage.setState(4);
            this.crtDeliveryMapper.updateByPrimaryKeySelective(cultHeritage);
        }
    }
    /**
     * 修改状态
     *
     * @param ids        用逗号分隔的多个ID
     * @param formstates 修改之前的状态
     * @param tostate    修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser user) throws Exception {
        ResponseBean rb = new ResponseBean();
        if (ids == null) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("主键丢失");
            return rb;
        }
        Example example = new Example(WhgDelivery.class);
        example.createCriteria()
                .andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")))
                .andIn("id", Arrays.asList(ids.split("\\s*,\\s*")));
        WhgDelivery cultHeritage = new WhgDelivery();
        cultHeritage.setState(tostate);
        cultHeritage.setStatemdfdate(new Date());
        cultHeritage.setStatemdfuser(user.getId());
        this.crtDeliveryMapper.updateByExampleSelective(cultHeritage, example);
        return rb;
    }

    /**
     * 检测所选时间是否已审核通过或配送完成
     * @param date
     * @return int
     */
    public int checkDateTime(String date,String supplyId) {
        return extendDeliveryMapper.selDeliveryTimeCount(date,supplyId) ;
    }

    /**
     * 添加配送申请
     * @param delivery
     * @param dates
     * @param sysUser
     * @return
     * @throws ParseException
     */
    public int addDelivery(WhgDelivery delivery,String dates,WhgSysUser sysUser,WhgSysUser sysUser2,String phone) throws Exception {
        String id = IDUtils.getID() ;
        if(delivery!= null) {
            delivery.setId(id);
            delivery.setCrtdate(new Date());
            delivery.setState(1);
            delivery.setDelstate(0);//未删除
            delivery.setStatemdfdate(new Date());
            delivery.setStatemdfuser(sysUser.getId());
            delivery.setCultid(sysUser.getCultid());
            //此处配送开始时间与结束时间在数据库中不应该设置为不可为空
            delivery.setEndtime(new Date());

        }
        StringBuffer sb=new StringBuffer();
        if(dates!=null) {
            String[] date = dates.split(",") ;
            ArrayList<String> dateList=new ArrayList<String>(Arrays.asList(date)); //用ArrayList存数组

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
            Date datestr=null;
            for(String d:date) {
                //TODO: 此处查询判断用户是否正在申请同一日期的供给
                int count = extendDeliveryMapper.selHaveDeliveryTimeCount(d,delivery.getFkid(),delivery.getCrtuser()) ;
                //如果存在已申请的数据则不添加
                if(count>0){
                    dateList.remove(d);
                    continue;
                }

                WhgDeliveryTime deliveryTime = new WhgDeliveryTime() ;
                Date dd=sdf.parse(d);
                deliveryTime.setId(IDUtils.getID());
                deliveryTime.setDeliveryid(id) ;
                deliveryTime.setDeliveryfkid(delivery.getFkid());
                deliveryTime.setDeliverytime(d);
                sb.append(d+" ");
                if(datestr==null||dd.before(datestr)){
                    datestr=dd;
                }
                whgDeliveryTimeMapper.insert(deliveryTime) ;
            }

            if(dateList.size()<=0){
                  //重复申请
                  throw new Exception("Reapplication");
            }

            if(datestr!=null){
                delivery.setStarttime(datestr);
            }
        }
        sendDeliverySms(phone,"touser",delivery,sysUser.getCultid(),sysUser2.getCultid(),sb.toString());//短信发送
        return crtDeliveryMapper.insert(delivery) ;
    }

    /**
     * 发送配送短信
     * @param delivery
     * @param dates
     * @param sysUser
     * @return
     * @throws ParseException
     */
    public  void  sendDeliverySms(String phone,String type,WhgDelivery delivery,String fromcultid,String tocultid,String dates){

        try {
            if(phone!=null&&!phone.equals("")){
                Map<String, String> smsData = new HashMap<String, String>();
                WhgSysCult  cult = cultService.t_srchOne(fromcultid);
                WhgSysCult  cult2 = cultService.t_srchOne(tocultid);
                if(cult!=null){
                    smsData.put("cultName", cult.getName());
                }else{
                    smsData.put("cultName", "广东省文化馆");
                }
                if(cult2!=null){
                    smsData.put("cultName2", cult2.getName());
                }else{
                    smsData.put("cultName2", "广东省文化馆");
                }

                if(delivery.getType()!=null&&delivery.getType().equals(0)){
                    smsData.put("type", "供给");
                }else{
                    smsData.put("type", "需求");
                }
               /* if(delivery.getType()!=null&&delivery.getType().equals(EnumTypeClazz.TYPE_PER.getValue())){
                    smsData.put("type", "专家人才");
                }else if(delivery.getType()!=null&&delivery.getType().equals(EnumTypeClazz.TYPE_GOODS.getValue())){
                    smsData.put("type", "文艺辅材");
                }else if(delivery.getType()!=null&&delivery.getType().equals(EnumTypeClazz.TYPE_SHOWGOODS.getValue())){
                    smsData.put("type", "文艺演出");
                }else if(delivery.getType()!=null&&delivery.getType().equals(EnumTypeClazz.TYPE_SUP.getValue())){
                    smsData.put("type", "需求");
                }else if(delivery.getType()!=null&&delivery.getType().equals(EnumTypeClazz.TYPE_ROOM.getValue())){
                    smsData.put("type", "活动室");
                }else if(delivery.getType()!=null&&delivery.getType().equals(EnumTypeClazz.TYPE_TRAIN.getValue())){
                    smsData.put("type", "培训");
                }else if(delivery.getType()!=null&&delivery.getType().equals(EnumTypeClazz.TYPE_EXH.getValue())){
                    smsData.put("type", "展览展示");
                }*/
                smsData.put("title", delivery.getName());
                smsData.put("time", dates);
                smsData.put("region", delivery.getRegion());
                if(type.equals("touser")){
                    smsService.t_sendSMS(phone, "DELIVERY_SUC", smsData, delivery.getId());//短信发送
                }else if(type.equals("toadmin")){
                    smsService.t_sendSMS(phone, "DELIVERY_SUC_ADMIN", smsData, delivery.getId());//短信发送
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 通知管理员配送短信
     * @param delivery
     * @param dates
     * @param sysUser
     * @return
     * @throws ParseException
     */
    public  void  sendAdminSms(){
        try {
        Example example = new Example(WhgDelivery.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("state", 1);
        List<WhgDelivery> typeList = this.crtDeliveryMapper.selectByExample(example);
        Date date=new Date();
        Calendar cc=Calendar.getInstance();
        cc.setTime(date);
        int day=cc.get(Calendar.DATE);
        cc.set(Calendar.DATE,day+1);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String datestr=sdf.format(cc.getTime());
        for(WhgDelivery whgDelivery:typeList){
           if(whgDelivery.getStarttime()!=null){
               if(datestr.equals(sdf.format(whgDelivery.getStarttime()))){//比较当前时间是否 等于 申请的配送时间的前一天
                   Example exa = new Example(WhgDeliveryTime.class);
                   Example.Criteria criteria = exa.createCriteria();
                   criteria.andEqualTo("deliveryfkid",whgDelivery.getId());
                   exa.setOrderByClause("deliverytime");
                   List<WhgDeliveryTime> list= whgDeliveryTimeMapper.selectByExample(exa);
                   StringBuffer sb=new StringBuffer();
                   for(WhgDeliveryTime deliveryTime:list){
                       sb.append(deliveryTime.getDeliverytime()+" ");
                   }
                   WhgSysUser sysUser2 = whgSystemUserService.t_srchOne(whgDelivery.getTouser()) ;
                   sendDeliverySms(whgSystemUserService.t_srchOneByAcount("admin").getContactnum(),"toadmin",whgDelivery,whgDelivery.getCultid(),sysUser2.getCultid(),sb.toString());//短信发送
                 }
               }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

    /**
     * 分页查询分类列表信息（接口用）
     *
     * @param request。
     */
    public PageInfo<WhgDelivery> t_ApiSrchList4p(Integer index,Integer size,String crtuser,String touser,Integer type,Integer deliveryType,WhgSysUser sysUser) throws Exception {
        //分页信息
        //搜索条件
        Example example = new Example(WhgDelivery.class);
        Example.Criteria c = example.createCriteria();

//        //名称条件
//        if (cult != null && cult.getName() != null) {
//            c.andLike("name", "%" + cult.getName() + "%");
//            cult.setName(null);
//        }

        //前端查询条件
        //未完成的配送
        if (deliveryType == 1){
            c.andIn("state", Arrays.asList(0,1,2));
        }
        //已完成的配送
        if (deliveryType ==2){
            c.andEqualTo("state", 3);
        }

        //我的配送
        if(type!=null) {
            c.andEqualTo("type",type) ;
        }
        if(crtuser!=null&!crtuser.equals("")) {
            c.andEqualTo("crtuser",crtuser) ;
        }
        if(touser!=null&!touser.equals("")) {
            c.andEqualTo("touser",touser) ;
        }


        example.setOrderByClause("crtdate desc");

        //分页查询
        PageHelper.startPage(index, size);
        List<WhgDelivery> typeList = this.crtDeliveryMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    public WhgDelivery getWhgDelivery(String id){
        WhgDelivery whgDelivery = crtDeliveryMapper.selectByPrimaryKey(id);
        return  whgDelivery;
    }
}
