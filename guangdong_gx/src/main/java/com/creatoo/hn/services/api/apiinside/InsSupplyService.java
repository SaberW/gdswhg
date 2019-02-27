package com.creatoo.hn.services.api.apiinside;

import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.mapper.api.ApiInsSupplyMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.comm.ResetRefidService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumSupplyType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "supply", keyGenerator = "simpleKeyGenerator")
public class InsSupplyService extends BaseService{

    @Autowired
    private WhgSupplyMapper whgSupplyMapper;
    @Autowired
    private WhgSupplyTimeMapper whgSupplyTimeMapper;

    @Autowired
    private WhgYwiTypeMapper whgYwiTypeMapper;

    /**
     * 文化馆服务类
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgDeliveryMapper whgDeliveryMapper;
    @Autowired
    private WhgDeliveryTimeMapper whgDeliveryTimeMapper;

    @Autowired
    private ApiInsSupplyMapper apiInsSupplyMapper;

    @Autowired
    private ResetRefidService resetRefidService;


    /**
     * 分页查供需需求列表
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo srchList4p(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List list = this.apiInsSupplyMapper.selectSupplyList(recode);

        return new PageInfo(list);
    }

    /**
     * 分页查供需供给列表
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo srchFkProjectList4p(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List list = this.apiInsSupplyMapper.selectFkProjectList(recode);

        if (list!=null && list.size()>0){
            for(Object obj : list){
                Map ent = (Map) obj;
                String supplyid = (String) ent.get("fkid");
                if (supplyid == null || supplyid.isEmpty()){
                    continue;
                }
                WhgSupplyTime exp = new WhgSupplyTime();
                exp.setSupplyid(supplyid);
                List<WhgSupplyTime> times = this.whgSupplyTimeMapper.select(exp);
                if (times != null && times.size()>0){
                    Set<String> psprovinceSet = new HashSet();
                    Set<String> pscitySet = new HashSet();
                    for (WhgSupplyTime supplyTime : times){
                        if (supplyTime.getPsprovince()!=null && !supplyTime.getPsprovince().isEmpty()) {
                            psprovinceSet.add(supplyTime.getPsprovince());
                        }
                        if (supplyTime.getPscity()!=null && !supplyTime.getPscity().isEmpty()) {
                            pscitySet.addAll(Arrays.asList(supplyTime.getPscity().split("\\s*,\\s*")));
                        }
                    }
                    ent.put("psprovince", psprovinceSet.stream().sorted().collect(Collectors.joining(",")));
                    ent.put("pscity", pscitySet.stream().sorted().collect(Collectors.joining(",")));
                }
            }
        }

        return new PageInfo(list);
    }

    /**
     * 查询供需需求信息记数
     * @param recode
     * @return
     * @throws Exception
     */
    public Object countSupplys(Map recode) throws Exception{
        return this.apiInsSupplyMapper.countSupplyList(recode);
    }

    /**
     * 查询供需供给信息记数
     * @param recode
     * @return
     * @throws Exception
     */
    public Object countFkProjects(Map recode) throws Exception{
        return this.apiInsSupplyMapper.countFkProjectList(recode);
    }


    public WhgSupply t_srchOne(String id) throws Exception {
         if(id!=null){
             return this.whgSupplyMapper.selectByPrimaryKey(id);
         }else {
             return null;
         }
    }

    /**
     * 主键查供需信息
     * @param id
     * @return
     * @throws Exception
     */
    public Object srchOne(String id) throws Exception{
        Map rest = new HashMap();

        WhgSupply supply = this.whgSupplyMapper.selectByPrimaryKey(id);
        if (supply != null) {
            BeanMap bm = new BeanMap();
            bm.setBean(supply);
            rest.putAll(bm);

            for(EnumSupplyType est : EnumSupplyType.values()){
                if (est.getValue().equals(supply.getEtype())){
                    rest.put("etypeText", est.getName());
                    break;
                }
            }

            /*WhgYwiType yt = this.whgYwiTypeMapper.selectByPrimaryKey(supply.getReftype());
            if (yt != null) {
                rest.put("reftypeText", yt.getName());
            }*/

            this.resetRefidService.resetRefid4type(rest, "reftype", "arttype");


            WhgSysCult cult = this.whgSystemCultService.t_srchOne(supply.getCultid());
            if (cult != null) {
                rest.put("cultidText", cult.getName());
            }

        }

        ApiResultBean arb = new ApiResultBean();
        arb.setData(FilterFontUtil.clearFont(rest, new String[]{"content"}));
        return arb;
    }

    /**
     * 供需匹配列表
     * @param id
     * @return
     * @throws Exception
     */
    public Object selectMatchList(String id, Integer size) throws Exception{
        ApiResultBean arb = new ApiResultBean();

        WhgSupply info = this.whgSupplyMapper.selectByPrimaryKey(id);
        if (info == null){
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            return arb;
        }

        Example exp = new Example(WhgSupply.class);
        Example.Criteria c = exp.createCriteria();
        //id不同
        c.andNotEqualTo("id", info.getId());
        //类型一至
        c.andEqualTo("etype", info.getEtype());
        //供需一至
        c.andEqualTo("gxtype", info.getGxtype());
        //状态有效
        c.andEqualTo("state", EnumBizState.STATE_PUB.getValue());

        exp.orderBy("statemdfdate").desc();

        if (size!=null){
            PageHelper.startPage(1, size);
        }
        List<WhgSupply> list = this.whgSupplyMapper.selectByExample(exp);
        List reslist = new ArrayList();
        if (list != null) {
            BeanMap bm = new BeanMap();
            for (WhgSupply sp : list) {
                bm.setBean(sp);
                Map rest = new HashMap();
                rest.putAll(bm);

                WhgSysCult cult = this.whgSystemCultService.t_srchOne(sp.getCultid());
                if (cult != null) {
                    rest.put("cultidName", cult.getName());
                }
                reslist.add(rest);
            }
        }

        arb.setRows(reslist);
        return arb;
    }

    /**
     * 提取供需指定年月的可申请配送时间
     * @param year
     * @param month
     * @return
     */
    public String  getOpenDays(String id) throws Exception{

        //找出结束时间大于参数年月的集合
        Example timeexp = new Example(WhgSupplyTime.class);
        timeexp.createCriteria()
                .andEqualTo("supplyid", id);
        timeexp.orderBy("timestart");
        List<WhgSupplyTime> list = this.whgSupplyTimeMapper.selectByExample(timeexp);
        List days = new ArrayList();
        //参数年月时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
       StringBuffer sb=new StringBuffer();
        //当前时间年月日
        Calendar cnd = Calendar.getInstance();
        for (WhgSupplyTime time : list) {
            Date ts = time.getTimestart();
            Date te = time.getTimeend();
            cnd.setTime(ts);
            cnd.set(Calendar.HOUR_OF_DAY, 0);
            cnd.set(Calendar.MINUTE, 0);
            cnd.set(Calendar.MINUTE, 0);

            while (cnd.getTime().compareTo(te) <= 0) {
                Date day = cnd.getTime();
                sb.append(cnd.get(Calendar.YEAR)+"-"+(cnd.get(Calendar.MONTH)<10?"0"+cnd.get(Calendar.MONTH):cnd.get(Calendar.MONTH))+"-"+(cnd.get(Calendar.DAY_OF_MONTH)<10?"0"+cnd.get(Calendar.DAY_OF_MONTH):cnd.get(Calendar.DAY_OF_MONTH)));
                sb.append(",");
                cnd.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        return sb.toString();
    }

    /**
     * 提取供需指定年月的可申请配送时间
     * @param year
     * @param month
     * @return
     */
    public String  getUseDays(String id) throws Exception{

        Example dtexp = new Example(WhgDeliveryTime.class);
        dtexp.createCriteria()
                .andEqualTo("state", 1)
                .andEqualTo("deliveryfkid", id);
        dtexp.orderBy("deliverytime");

        List<WhgDeliveryTime> usetimes  = this.whgDeliveryTimeMapper.selectByExample(dtexp);
        StringBuffer sb=new StringBuffer();
        for(WhgDeliveryTime whgDelivery:usetimes){
            sb.append(whgDelivery.getDeliverytime());
            sb.append(",");
        }
        return sb.toString();
    }

    /**
     * 提取供需指定年月的可申请配送时间
     * @param year
     * @param month
     * @return
     */
    public Map getGxtimeDays(String id, String year, String month, String timeid) throws Exception{
        if (id == null || id.isEmpty()
                || year == null || !year.matches("^\\d{4}$")
                || month == null || !month.matches("^\\d{1,2}$")) {
            return null;
        }

        if (month.matches("^\\d{1}$")) {
            month = "0"+month;
        }

        //参数年月时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String ymStr = year+"-"+month;
        Date ymDate = sdf.parse(ymStr);

        //当前时间年月日
        Calendar cnd = Calendar.getInstance();
        cnd.set(Calendar.HOUR_OF_DAY, 0);
        cnd.set(Calendar.MINUTE, 0);
        cnd.set(Calendar.SECOND, 0);
        cnd.set(Calendar.MILLISECOND, 0);
        Date ymdNow = cnd.getTime();

        //当前时间年月
        cnd.set(Calendar.DAY_OF_YEAR, 1);
        Date ymNow = cnd.getTime();
        //当前时间之前的参数时间，不需要返回解析
        if (ymDate.compareTo(ymNow) < 0) {
            return null;
        }

        //参考参数年月的结束时间
        cnd.setTime(ymDate);
        cnd.add(Calendar.MONTH,1);
        Date endYmDate = cnd.getTime();

        //找出结束时间大于参数年月的集合
        Example timeexp = new Example(WhgSupplyTime.class);
        Example.Criteria c = timeexp.createCriteria();
                c.andEqualTo("supplyid", id);
                c.andGreaterThanOrEqualTo("timeend", ymDate);
        if (timeid!=null && !timeid.isEmpty()){
            c.andEqualTo("id", timeid);
        }
        timeexp.orderBy("timestart");
        List<WhgSupplyTime> list = this.whgSupplyTimeMapper.selectByExample(timeexp);
        //无可用时段时，不需要返回解析
        if (list == null || list.size() == 0) {
            return null;
        }

        Map openTime = new HashMap();
        openTime.put("year", year);
        openTime.put("month", month);
        List days = new ArrayList();
        StringBuffer sb=new StringBuffer();
        for (WhgSupplyTime time : list) {
            Date ts = time.getTimestart();
            Date te = time.getTimeend();
            sb.append(" "+sdf2.format(ts)+" 至 "+sdf2.format(te));
            cnd.setTime(ts);
            cnd.set(Calendar.HOUR_OF_DAY, 0);
            cnd.set(Calendar.MINUTE, 0);
            cnd.set(Calendar.SECOND, 0);

            while (cnd.getTime().compareTo(te) <= 0) {
                Date day = cnd.getTime();
                //参数年月之外的终止加入
                if (day.compareTo(endYmDate) >= 0) {
                    break;
                }
                if (day.compareTo(ymdNow) >= 0 && day.compareTo(ymDate)>=0) {
                    days.add(cnd.get(Calendar.DAY_OF_MONTH));
                }
                cnd.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        openTime.put("days", days);
        openTime.put("dayslen", sb.toString());

        //对应的供需申请集
        WhgDelivery recode = new WhgDelivery();
        recode.setFkid(id);
        List<WhgDelivery> fklist = this.whgDeliveryMapper.select(recode);
        List<WhgDeliveryTime> usetimes = null;
        if (fklist != null && fklist.size() > 0) {
            //相关申请的ID集
            List ids = new ArrayList();
            for (WhgDelivery ent : fklist){
                ids.add(ent.getId());
            }
            Calendar cnds = Calendar.getInstance();
            cnds.setTime(ymDate);
            cnds.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
            cnds.set(Calendar.HOUR_OF_DAY,23);
            cnds.set(Calendar.MINUTE,59);
            cnds.set(Calendar.SECOND,59);
            ymDate=cnds.getTime();
            //供需已订生效的时段集
            Example dtexp = new Example(WhgDeliveryTime.class);
            dtexp.createCriteria()
                    .andEqualTo("state", 1)
                    .andIn("deliveryid", ids)
                    .andBetween("deliverytime",ymDate,endYmDate);
            dtexp.orderBy("deliverytime");

            usetimes = this.whgDeliveryTimeMapper.selectByExample(dtexp);
        }
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
        if (usetimes != null && usetimes.size() > 0) {
            List useDays = new ArrayList();
            for (WhgDeliveryTime ent : usetimes){
                cnd.setTime(sd.parse(ent.getDeliverytime()));
                int useday = cnd.get(Calendar.DAY_OF_MONTH);
                if (!useDays.contains(useday)) {
                    useDays.add(useday);
                }
            }

            openTime.put("useDays", useDays);
        }else{
            openTime.put("useDays", new ArrayList());
        }

        return openTime;
    }

    /**
     * 取参照当前时间的供需开放时间年月
     * @param id
     * @return
     * @throws Exception
     */
    public Object firstOpenTime(String id, String timeid) throws Exception{
        if (id == null || id.isEmpty()) {
            return null;
        }

        //当前时间年月日
        Calendar cnd = Calendar.getInstance();
        cnd.set(Calendar.HOUR_OF_DAY, 0);
        cnd.set(Calendar.MINUTE, 0);
        cnd.set(Calendar.SECOND, 0);
        Date ymdNow = cnd.getTime();

        Example timeexp = new Example(WhgSupplyTime.class);
        Example.Criteria c = timeexp.createCriteria();
                c.andEqualTo("supplyid", id);
                c.andGreaterThanOrEqualTo("timeend", ymdNow);
        if (timeid != null && !timeid.isEmpty()){
            c.andEqualTo("id", timeid);
        }
        timeexp.orderBy("timestart");
        List<WhgSupplyTime> list = this.whgSupplyTimeMapper.selectByExample(timeexp);
        //无可用时段时，不需要返回解析
        if (list == null || list.size() == 0) {
            return null;
        }
        WhgSupplyTime exp = list.get(0);
        Date timestart = exp.getTimestart();

        Date firstTime = timestart;
        //最早的开始时间小于当前年月，直接返回当前年月
        if (timestart.compareTo(ymdNow)<0){
            firstTime = ymdNow;
        }

        cnd.setTime(firstTime);
        cnd.set(Calendar.DAY_OF_MONTH, 1);
        firstTime = cnd.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(firstTime);
    }


    public Object getGXcity(String id) throws Exception{
        WhgSupply supply = this.whgSupplyMapper.selectByPrimaryKey(id);

        Map rest = new HashMap();
        if (supply == null) {
            return rest;
        }

        String province = supply.getPsprovince();
        String citys = supply.getPscity();
        rest.put("province", province);
        rest.put("city", citys);

        return rest;
    }


    /**
     * 检查时段可用
     * @param id
     * @return
     * @throws Exception
     */
    public boolean canApply4time(String id) throws Exception{
        if (id == null || id.isEmpty()) {
            return false;
        }

        //当前时间年月日
//        Calendar cnd = Calendar.getInstance();
//        cnd.set(Calendar.HOUR_OF_DAY, 0);
//        cnd.set(Calendar.MINUTE, 0);
//        cnd.set(Calendar.SECOND, 0);
//        Date ymdNow = cnd.getTime();
        Date ymdNow = new Date();
        Example timeexp = new Example(WhgSupplyTime.class);
        timeexp.createCriteria()
                .andEqualTo("supplyid", id)
                .andGreaterThanOrEqualTo("timeend", ymdNow);
        timeexp.orderBy("timestart");
        List<WhgSupplyTime> list = this.whgSupplyTimeMapper.selectByExample(timeexp);
        //有无可用时段
        if (list == null || list.size() == 0) {
            return false;
        }
        return true;
    }
}
