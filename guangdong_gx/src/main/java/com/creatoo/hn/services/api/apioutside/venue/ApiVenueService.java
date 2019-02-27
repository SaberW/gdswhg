package com.creatoo.hn.services.api.apioutside.venue;

import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.mapper.api.ApiResourceMapper;
import com.creatoo.hn.dao.mapper.api.ApiVenueMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.user.WhgBlackListService;
import com.creatoo.hn.services.api.apiinside.InsMessageService;
import com.creatoo.hn.services.api.apioutside.user.ApiUserService;
import com.creatoo.hn.services.comm.ResetRefidService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

/**
 * Created by rbg on 2017/8/9.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames="venue", keyGenerator = "simpleKeyGenerator")
public class ApiVenueService extends BaseService {

    @Autowired
    private ApiVenueMapper apiVenueMapper;

    @Autowired
    private ApiResourceMapper apiResourceMapper;

    @Autowired
    private WhgVenMapper whgVenMapper;

    @Autowired
    private WhgVenRoomMapper whgVenRoomMapper;

    @Autowired
    private WhgVenRoomTimeMapper whgVenRoomTimeMapper;

    @Autowired
    private WhgVenRoomOrderMapper whgVenRoomOrderMapper;

    @Autowired
    private WhgUserMapper whgUserMapper;

//    @Autowired
//    private WhgUserBlacklistMapper whgUserBlacklistMapper;

    @Autowired
    private WhgUsrUnorderMapper whgUsrUnorderMapper;

    @Autowired
    private SMSService smsService;
    @Autowired
    private InsMessageService insMessageService;

    @Autowired
    private ResetRefidService resetRefidService;

    @Autowired
    private WhgBlackListService whgBlackListService;

    /**
     * 为首页提取场馆列表
     * @param cultid
     * @param pageSize
     * @return
     * @throws Exception
     */
    public Object selectVenList2IndexPage(List cultid, List deptids, int pageSize) throws Exception{

        PageHelper.startPage(1, pageSize);
        List list = this.apiVenueMapper.selectVenue4IndexPage(cultid, deptids, EnumProject.PROJECT_WBGX.getValue());
        PageInfo pageInfo = new PageInfo(list);

        return pageInfo;
    }

    /**
     * 分页查询场馆信息
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    @Cacheable
    public PageInfo selectVen4Page(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List reslist = this.apiVenueMapper.selectVenue4page(recode);
        for (Object obj : reslist) {
            this._changeRefIdValue((Map) obj);
        }

        PageInfo pageInfo = new PageInfo(reslist);
        return pageInfo;
    }

    /**
     * 场馆分页查询（表字段名）
     * @param page
     * @param pageSize
     * @param ven
     * @return
     * @throws Exception
     */
    @Cacheable
    public PageInfo selectVenList4Page(int page, int pageSize, Map recode) throws Exception {
        PageHelper.startPage(page, pageSize);
        List reslist = this.apiVenueMapper.selectVenList4page(recode);
        return new PageInfo(reslist);
    }

    /**
     * 大首页 热门场馆
     * @param page
     * @param pageSize
     * @param ven
     * @return
     * @throws Exception
     */
    @Cacheable
    public PageInfo selectHotVenList4Page(int page, int pageSize, Map recode) throws Exception {
        PageHelper.startPage(page, pageSize);
        List reslist = this.apiVenueMapper.selectHotVenList4page(recode);
        return new PageInfo(reslist);
    }

    /**
     * 转换相关值内容
     * @param ven or room
     */
    private void _changeRefIdValue(Map info){
        this.resetRefidService.resetRefid4type(info, "etype", "type", "area", "facility");
        this.resetRefidService.resetRefid4tag(info, "etag", "tag");
    }

    /**
     * 场馆详细信息
     * @param id
     * @return
     * @throws Exception
     */
    @Cacheable
    public Object selectVen4id(String id, String protype) throws Exception{
        //场馆
        Map ven = this.apiVenueMapper.findVenue4id(id);
        if (ven == null){
            return ven;
        }

        this._changeRefIdValue(ven);

        //场馆相关活动室列表
        Calendar c = Calendar.getInstance();
        /*c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);*/
        List roomList = this.apiVenueMapper.selectVenRoom4venid(id, c.getTime(), protype);
        ven.put("roomList", roomList);

        //资源-图片，视频，音频，文档
        List imageList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_VENUE.getValue(), id, EnumResType.TYPE_IMAGE.getValue());
        List videoList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_VENUE.getValue(), id, EnumResType.TYPE_VIDEO.getValue());
        List audioList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_VENUE.getValue(), id, EnumResType.TYPE_AUDIO.getValue());
        List fileList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_VENUE.getValue(), id, EnumResType.TYPE_FILE.getValue());
        ven.put("imageList", imageList);
        ven.put("videoList", videoList);
        ven.put("audioList", audioList);
        ven.put("fileList", fileList);

        //类型枚举值
        ven.put("enumTypeClazz", EnumTypeClazz.TYPE_VENUE.getValue());

        return FilterFontUtil.clearFont(ven, new String[]{"description", "facilitydesc"});
    }

    /**
     * 查场馆信息(表字段名)
     * @param id
     * @param protype
     * @return
     * @throws Exception
     */
    @Cacheable
    public Map findVenInfo(String id, String protype) throws Exception{
        Map rest = this.apiVenueMapper.findVenInfo4id(id, protype);
        this._changeRefIdValue(rest);
        return rest;
    }

    /**
     * 查场馆活动室列表(表字段名)
     * @param venid
     * @return
     * @throws Exception
     */
    @Cacheable
    public List selectRooms4ven(String venid, String protype) throws Exception{
        return this.apiVenueMapper.selectRooms4venSupply(venid, protype, null);
    }

    /**
     * 通过活动室id查找场馆信息
     *
     * @param venid
     * @return
     * @throws Exception
     */

    public Map selectVenByRoomid(String roomid) throws Exception {
        return this.apiVenueMapper.findVenByRoomid(roomid);
    }

    /**
     * 查询推荐场馆
     * @param exVenid 例外的场馆ID
     * @param size  条数
     * @return
     */
    @Cacheable
    public List selectRecommendVenList(String exVenid, List cultid,List deptids, Integer size, String protype) throws Exception{
        if (size!=null){
            PageHelper.startPage(1, size);
        }
        return this.apiVenueMapper.selectVenue4Recommend(protype, cultid,deptids, exVenid);
    }

    /**
     * 查询推荐内部供需场馆
     * @param exVenid 例外的场馆ID
     * @param size  条数
     * @return
     */
    @Cacheable
    public List selectRecommendVenList4supply(String exVenid, List cultid, List deptids, Integer size, String protype) throws Exception{
        if (size!=null){
            PageHelper.startPage(1, size);
        }
        return this.apiVenueMapper.selectVenue4Recommend4supply(protype, cultid, deptids, exVenid);
    }

    /**
     * 查指定活动室信息
     * @param id
     * @return
     * @throws Exception
     */
    @Cacheable
    public Object selectVenRoom4id(String id) throws Exception{
        Map room = this.apiVenueMapper.findVenRoom4id(id);
        if (room==null){
            return room;
        }
        //资源-图片，视频，音频，文档
        List imageList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_ROOM.getValue(), id, EnumResType.TYPE_IMAGE.getValue());
        List videoList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_ROOM.getValue(), id, EnumResType.TYPE_VIDEO.getValue());
        List audioList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_ROOM.getValue(), id, EnumResType.TYPE_AUDIO.getValue());
        List fileList = this.apiResourceMapper.selectResource(EnumTypeClazz.TYPE_ROOM.getValue(), id, EnumResType.TYPE_FILE.getValue());
        room.put("imageList", imageList);
        room.put("videoList", videoList);
        room.put("audioList", audioList);
        room.put("fileList", fileList);

        //类型枚举值
        room.put("enumTypeClazz", EnumTypeClazz.TYPE_ROOM.getValue());

        //标签
        this._changeRefIdValue(room);

        //相关场馆信息
        WhgVen ven = this.whgVenMapper.selectByPrimaryKey(room.get("venid"));
        if (ven!=null){
            room.put("venTitle", ven.getTitle());
            room.put("address", ven.getAddress());
            room.put("venContacts", ven.getContacts());
            room.put("venPhone", ven.getPhone());
            room.put("venTelephone", ven.getTelephone());
            room.put("latitude", ven.getLatitude());
            room.put("longitude", ven.getLongitude());
            room.put("venState", ven.getState());
            room.put("venDelstate", ven.getDelstate());
        }

        return FilterFontUtil.clearFont(room, new String[]{"description"});
    }

    /**
     * 查指定活动室的相关活动室
     * @param roomid
     * @param size
     * @return
     * @throws Exception
     */
    @Cacheable
    public List selectRefRoomList(String roomid, Integer size, String protype) throws Exception{
        List rest = new ArrayList();
        if (roomid == null || roomid.isEmpty()){
            return rest;
        }
        WhgVenRoom room = this.whgVenRoomMapper.selectByPrimaryKey(roomid);
        if (room==null){
            return rest;
        }

        if (size != null) {
            PageHelper.startPage(1, size);
        }
        rest = this.apiVenueMapper.selectRooms4ven(room.getVenid(), protype, room.getId());

        return rest;
    }

    /**
     * 查询活动室可用开放预订的最大和最小天数值
     * @param roomid
     * @return
     * @throws Exception
     */
    @Cacheable
    public Map<String, Object> getRoomTimeMaxMinDay(String roomid) throws Exception{
        Map<String, Object> rest = new HashMap();
        rest.put("minDay", null);
        rest.put("minDayString", null);
        rest.put("maxDay", null);
        rest.put("maxDayString", null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String timedayStr = sdf.format(new Date());

        Example example = new Example(WhgVenRoomTime.class);
        example.createCriteria()
                .andEqualTo("state", 1)
                .andEqualTo("roomid", roomid)
                .andGreaterThanOrEqualTo("timeday", sdf.parse(timedayStr));

        example.setOrderByClause("timeday asc");
        List<WhgVenRoomTime> times = this.whgVenRoomTimeMapper.selectByExampleAndRowBounds(example, new RowBounds(0,1));
        if (times!=null && times.size()>0){
            WhgVenRoomTime time = times.get(0);
            rest.put("minDay", time.getTimeday());
            rest.put("minDayString", sdf.format(time.getTimeday()) );
        }

        example.setOrderByClause("timeday desc");
        times = this.whgVenRoomTimeMapper.selectByExampleAndRowBounds(example, new RowBounds(0,1));
        if (times!=null && times.size()>0){
            WhgVenRoomTime time = times.get(0);
            rest.put("maxDay", time.getTimeday());
            rest.put("maxDayString", sdf.format(time.getTimeday()) );
        }
        return rest;
    }

    /**
     * 查询时段内活动室的开放预订
     * @param roomid
     * @param bday
     * @param eday
     * @return
     * @throws Exception
     */
    @Cacheable
    public List selectRoomTimeList(String roomid, Date bday, Date eday) throws Exception{
        Example example = new Example(WhgVenRoomTime.class);
        example.createCriteria()
                .andEqualTo("roomid", roomid)
                .andEqualTo("state", 1)
                .andGreaterThanOrEqualTo("timeday", bday)
                .andLessThanOrEqualTo("timeday", eday);
        example.setOrderByClause("timeday asc, timestart asc");
        return this.whgVenRoomTimeMapper.selectByExample(example);
    }

    /**
     * 查询时段内活动室成功预订信息
     * @param roomid
     * @param bday
     * @param eday
     * @return
     * @throws Exception
     */
    @Cacheable
    public List selectRoomOrderSuccess(String roomid, Date bday, Date eday) throws Exception{
        Example example = new Example(WhgVenRoomOrder.class);
        example.createCriteria()
                .andEqualTo("roomid", roomid)
                .andEqualTo("state", EnumStateOrderRoom.STATE_SUCCESS.getValue())
                .andGreaterThanOrEqualTo("timeday", bday)
                .andLessThanOrEqualTo("timeday", eday);
        example.setOrderByClause("timeday asc, timestart asc");
        return this.whgVenRoomOrderMapper.selectByExample(example);
    }

    /**
     * 查时段内的活动室 用户ID 的预订申请
     * @param roomid
     * @param bday
     * @param eday
     * @param userid
     * @return
     * @throws Exception
     */
    @Cacheable
    public List selectRoomOrderApply4userId(String roomid, String userid, Date bday, Date eday) throws Exception{
        Example example = new Example(WhgVenRoomOrder.class);
        example.createCriteria()
                .andEqualTo("roomid", roomid)
                .andEqualTo("userid", userid)
                .andEqualTo("state", EnumStateOrderRoom.STATE_APPLY.getValue())
                .andGreaterThanOrEqualTo("timeday", bday)
                .andLessThanOrEqualTo("timeday", eday);
        example.setOrderByClause("timeday asc, timestart asc");
        return this.whgVenRoomOrderMapper.selectByExample(example);
    }


    /**
     * 保存用户活动室预定
     * @param userId
     * @param roomTimeId
     * @param phone
     * @param ordercontact
     * @param purpose
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object saveUserRoomOrder(String userId, String roomTimeId, String phone, String ordercontact, String purpose) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (phone == null || !phone.trim().matches(ApiUserService.MOBILE_REX)){
            arb.setCode(131);
            arb.setMsg("手机号码格式不正确");
            return arb;
        }
        if (ordercontact==null || ordercontact.isEmpty() || ordercontact.trim().length()>20){
            arb.setCode(132);
            arb.setMsg("预订人为空或输入过长");
            return arb;
        }
        if (purpose != null && purpose.replaceAll("\r\n","").length() > 200){
            arb.setCode(133);
            arb.setMsg("预订用途输入过长");
            return arb;
        }

        //调用验证
        arb = (ApiResultBean) this.testUserRoomTime(userId, roomTimeId, phone, ordercontact, purpose);
        if (arb!=null && arb.getCode()!=null && arb.getCode().intValue()!=0){
            return arb;
        }

        WhgVenRoomTime time = this.whgVenRoomTimeMapper.selectByPrimaryKey(roomTimeId);

        WhgVenRoomOrder order = new WhgVenRoomOrder();
        order.setId(IDUtils.getID());
        order.setOrderid( IDUtils.getOrderID(EnumOrderType.ORDER_VEN.getValue()+"") );

        order.setRoomid(time.getRoomid());
        order.setTimeday(time.getTimeday());
        order.setTimestart(time.getTimestart());
        order.setTimeend(time.getTimeend());
        order.setTimelong(time.getTimelong());
        order.setHasfees(time.getHasfees());
        order.setState(EnumStateOrderRoom.STATE_APPLY.getValue());

        order.setCrtdate(new Date());
        order.setUserid(userId);
        order.setOrdercontactphone(phone);
        order.setOrdercontact(ordercontact);
        order.setPurpose(purpose);

        order.setTicketstatus(1);
        order.setPrinttickettimes(0);
        order.setTicketcheckstate(1);

        order.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());

        this.whgVenRoomOrderMapper.insert(order);

        try {
            WhgVenRoom room = this.whgVenRoomMapper.selectByPrimaryKey(order.getRoomid());
            WhgVen ven = this.whgVenMapper.selectByPrimaryKey(room.getVenid());
            Map<String, String> data = new HashMap<>();
            data.put("userName", order.getOrdercontact());
            data.put("title", room.getTitle());
            data.put("orderNum", order.getOrderid());
            data.put("phone", ven.getPhone());

            String msgTemp = "VEN_ORDER_ADD";
            if (room.getHasfees()!=null && room.getHasfees().intValue() == 1) {
                msgTemp = "VEN_ORDER_ADD_CHARGE";
            }

            if (room.getNoticetype() != null && !room.getNoticetype().isEmpty()) {
                String noticetype = room.getNoticetype();
                if (noticetype.contains("ZNX")){
                    try{
                        this.insMessageService.t_sendZNX(order.getUserid(), null, msgTemp, data, order.getRoomid(), EnumProject.PROJECT_WBGX.getValue());
                    }catch (Exception e){
                        log.error("roomOrderAdd t_sendZNX error", e);
                    }
                }
                if (noticetype.contains("SMS")){
                    try {
                        this.smsService.t_sendSMS(order.getOrdercontactphone(), msgTemp, data, room.getId());
                    } catch (Exception e) {
                        log.error("roomOrderAdd t_sendSMS error", e);
                    }
                }
            }

        } catch (Exception e) {
            log.error("roomOrderAdd sendSMS error", e);
        }

        arb.setData(order);
        return arb;
    }


    /**
     * 验证用户预订
     * @param userId
     * @param roomTimeId
     * @param phone
     * @param ordercontact
     * @param purpose
     * @return
     * @throws Exception
     */
    public Object testUserRoomTime(String userId, String roomTimeId, String phone, String ordercontact, String purpose) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (userId == null || userId.isEmpty()){
            arb.setCode(102);
            arb.setMsg("预订用户不能为空");
            return arb;
        }
        WhgUser user = this.whgUserMapper.selectByPrimaryKey(userId);
        if (user==null){
            arb.setCode(103);
            arb.setMsg("预订用户不存在");
            return arb;
        }

        if (phone == null){
            //用户手机号是否存在，拦截微信登录无手机号
            if (user.getPhone()==null || user.getPhone().isEmpty()){
                arb.setCode(104);
                arb.setMsg("您还未绑定手机，请先绑定手机！");
                return arb;
            }
        }
        //是否为黑名单
        /*WhgUserBlacklist ubl = new WhgUserBlacklist();
        ubl.setUserid(user.getId());
        ubl.setState(1);
        int ublcount = this.whgUserBlacklistMapper.selectCount(ubl);
        if (ublcount>0){
            arb.setCode(105);
            arb.setMsg("非常抱歉！您的操作行为已被列入黑名单，如需了解详细情况，请与管理员联系！");
            return arb;
        }*/
        boolean isBlack = this.whgBlackListService.isBlackListUser(user.getId());
        if (isBlack){
            arb.setCode(105);
            arb.setMsg("非常抱歉！您的操作行为已被列入黑名单，如需了解详细情况，请与管理员联系！");
            return arb;
        }


        WhgVenRoomTime time = this.whgVenRoomTimeMapper.selectByPrimaryKey(roomTimeId);
        if (time == null || time.getState()==null || time.getState().intValue()!=1){
            arb.setCode(106);
            arb.setMsg("申请的活动室所选开放时段已关闭预订");
            return arb;
        }
        WhgVenRoom room = this.whgVenRoomMapper.selectByPrimaryKey(time.getRoomid());
        if (room==null || room.getState()==null || room.getState().intValue()!=6 || room.getDelstate()==null || room.getDelstate().intValue()!=0){
            arb.setCode(107);
            arb.setMsg("申请的活动室不存在");
            return arb;
        }
        WhgVen ven = this.whgVenMapper.selectByPrimaryKey(room.getVenid());
        if (ven==null || ven.getState()==null || ven.getState().intValue()!=6 || ven.getDelstate()==null || ven.getDelstate().intValue()!=0){
            arb.setCode(108);
            arb.setMsg("申请的活动室所属场馆状态不可用");
            return arb;
        }

        //预订日期是否小于当前时间
        if (time.getTimeday()!=null ){
            Calendar c = Calendar.getInstance();
            c.setTime(time.getTimeday());
            if (time.getTimestart()!=null){
                Calendar cstime = Calendar.getInstance();
                cstime.setTime(time.getTimestart());

                c.set(Calendar.HOUR_OF_DAY, cstime.get(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, cstime.get(Calendar.MINUTE));
            }
            if (c.getTime().compareTo(new Date()) <0) {
                arb.setCode(109);
                arb.setMsg("申请的活动室开放时间已过时");
                return arb;
            }
        }

        //活动室时段是否已被预定成功
        WhgVenRoomOrder order = new WhgVenRoomOrder();
        order.setRoomid(time.getRoomid());
        order.setTimeday(time.getTimeday());
        order.setTimestart(time.getTimestart());
        order.setTimeend(time.getTimeend());
        order.setState(3);
        int contState3 = this.whgVenRoomOrderMapper.selectCount(order);
        if (contState3 > 0){
            arb.setCode(110);
            arb.setMsg("申请的活动室已被其他用户预定");
            return arb;
        }

        //活动室时段是否已被相同的用户或联系手机重复预订
        order.setState(null);
        Example example = new Example(WhgVenRoomOrder.class);
        example.or().andEqualTo(order).andEqualTo("userid", user.getId()).andIn("state", Arrays.asList(0,3));
        if (phone != null) {
            example.or().andEqualTo(order).andEqualTo("ordercontactphone", phone).andIn("state", Arrays.asList(0,3));
        }
        int contUser = this.whgVenRoomOrderMapper.selectCountByExample(example);
        if (contUser > 0){
            arb.setCode(111);
            arb.setMsg("重复申请了相同活动室开放时段，请查看已申请的预订");
            return arb;
        }

        //是否需要实名
        if (room!=null && room.getIsrealname()!=null && room.getIsrealname().intValue()==1){
            if (user==null || user.getIsrealname()==null || user.getIsrealname().intValue() != 1){
                arb.setCode(200);
                arb.setMsg("您还未完成实名认证，请先完成实名认证！");
                return arb;
            }
        }

        return arb;
    }

    /**
     * 取消活动室预定申请
     * @param orderId
     * @param userId
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object unOrder(String orderId, String userId) throws Exception{
        ApiResultBean arb = new ApiResultBean();
        if (orderId == null || orderId.isEmpty() || userId==null || userId.isEmpty()) {
            arb.setCode(102);
            arb.setMsg("参数为空");
            return arb;
        }

        //取得参数相关的预订申请状态订单
        WhgVenRoomOrder recode = new WhgVenRoomOrder();
        recode.setId(orderId);
        recode.setUserid(userId);
        recode.setState(EnumStateOrderRoom.STATE_APPLY.getValue());
        List<WhgVenRoomOrder> list = this.whgVenRoomOrderMapper.select(recode);
        if (list==null || list.size()==0){
            arb.setCode(103);
            arb.setMsg("查找订单失败");
            return arb;
        }
        recode = list.get(0);

        //验证是否开始时间在48小时以上
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 2);
        Date twoDayLater = c.getTime();

        Date timeDay = recode.getTimeday();
        Date timeStart = recode.getTimestart();
        c.setTime(timeDay);
        Calendar hm = Calendar.getInstance();
        hm.setTime(timeStart);
        c.set(Calendar.HOUR, hm.get(Calendar.HOUR));
        c.set(Calendar.MINUTE, hm.get(Calendar.MINUTE));
        c.set(Calendar.SECOND, hm.get(Calendar.SECOND));
        Date beginDay = c.getTime();

        if (beginDay.compareTo(twoDayLater)<0 ) {
            arb.setCode(104);
            arb.setMsg("预订的使用时间在48小时内，不能取消申请");
            return arb;
        }

        //变更状态为取消申请
        recode.setState(EnumStateOrderRoom.STATE_CANCEL.getValue());
        int upcount = this.whgVenRoomOrderMapper.updateByPrimaryKey(recode);
        //无修改阻止往下执行
        if (upcount==0){
            arb.setCode(101);
            arb.setMsg("取消预订失败");
            return arb;
        }

        //短信处理
        WhgVenRoomOrder order = this.whgVenRoomOrderMapper.selectByPrimaryKey(orderId);
        try {
            WhgVenRoom room = this.whgVenRoomMapper.selectByPrimaryKey(order.getRoomid());

            Map<String, String> data = new HashMap<>();
            data.put("userName", order.getOrdercontact());
            data.put("title", room.getTitle());
            data.put("orderNum", order.getOrderid());

            if (room.getNoticetype() != null && !room.getNoticetype().isEmpty()) {
                String noticetype = room.getNoticetype();
                if (noticetype.contains("ZNX")){
                    try{
                        this.insMessageService.t_sendZNX(order.getUserid(), null, "VEN_ORDER_UNADD", data, order.getRoomid(), EnumProject.PROJECT_WBGX.getValue());
                    }catch (Exception e){
                        log.error("roomOrderUnAdd t_sendZNX error", e);
                    }
                }
                if (noticetype.contains("SMS")){
                    try {
                        this.smsService.t_sendSMS(order.getOrdercontactphone(), "VEN_ORDER_UNADD", data, order.getRoomid());
                    } catch (Exception e) {
                        log.error("roomOrderUnAdd t_sendSMS error", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("roomOrderUnAdd sendSMS error", e);
        }

        //与黑名单相关的操作
//        if (userId != null){
//            try {
//                //当前用户场馆订单取消记数
//                WhgUsrUnorder uuo = new WhgUsrUnorder();
//                uuo.setUserid(userId);
//                uuo.setOrdertype(EnumOrderType.ORDER_VEN.getValue());
//
//                int currUnCount = this.whgUsrUnorderMapper.selectCount(uuo);
//                if (currUnCount>0){
//                    //超过一次了这就是第二次以上，记黑名单，清除记录
//                    /*WhgUserBlacklist ubl = new WhgUserBlacklist();
//                    ubl.setUserid(uuo.getUserid());
//                    ubl.setState(1);
//                    int ublcount = this.whgUserBlacklistMapper.selectCount(ubl);
//                    if (ublcount == 0){
//                        WhgUser user = this.whgUserMapper.selectByPrimaryKey(uuo.getUserid());
//                        ubl.setId(IDUtils.getID());
//                        ubl.setUserphone(user.getPhone());
//                        ubl.setType(1);
//                        ubl.setJointime(new Date());
//                        this.whgUserBlacklistMapper.insert(ubl);
//                    }*/
//
//                    WhgUser user = this.whgUserMapper.selectByPrimaryKey(uuo.getUserid());
//                    this.whgBlackListService.addBlackList(user.getId(), user.getPhone(), "两次以上取消场馆订单");
//
//                    this.whgUsrUnorderMapper.delete(uuo);
//                }else{
//                    //记入取消记录
//                    uuo.setOrderid(order.getId());
//                    uuo.setUntime(new Date());
//                    uuo.setId(IDUtils.getID());
//                    uuo.setOrdertype(EnumOrderType.ORDER_VEN.getValue());
//                    this.whgUsrUnorderMapper.insert(uuo);
//                }
//            } catch (Exception e) {
//                log.error("取消场馆订单处理黑名单失败", e);
//            }
//        }

        return arb;
    }


    /**
     * 删除取消的预订申请
     * @param orderId
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object delOrder(String orderId) throws Exception {
        ApiResultBean arb = new ApiResultBean();

        if (orderId == null || orderId.isEmpty()) {
            arb.setCode(102);
            arb.setMsg("订单标识不能为空");
            return arb;
        }

        WhgVenRoomOrder order = this.whgVenRoomOrderMapper.selectByPrimaryKey(orderId);
        if (order == null){
            arb.setCode(103);
            arb.setMsg("订单查找失败");
            return arb;
        }
        /*if (order.getState()==null || order.getState().intValue()!= EnumStateOrderRoom.STATE_CANCEL.getValue()){
            arb.setCode(104);
            arb.setMsg("订单不是取消状态，不能删除");
            return arb;
        }*/

        int delCount = 0;
        //申请中和取消状态物理删除，其它的标记删除
        if (order.getState()==null ||
                order.getState().intValue()!= EnumStateOrderRoom.STATE_CANCEL.getValue() ||
                order.getState().intValue()!= EnumStateOrderRoom.STATE_APPLY.getValue()){
            delCount = this.whgVenRoomOrderMapper.deleteByPrimaryKey(order.getId());
        }else{
            order.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            delCount = this.whgVenRoomOrderMapper.updateByPrimaryKeySelective(order);
        }

        if (delCount == 0){
            arb.setCode(101);
            arb.setMsg("操作失败");
            return arb;
        }
        return arb;
    }

    /**
     * 分页查询用户订单列表
     * @param page
     * @param pageSize
     * @param userid
     * @param nowtype
     * @return
     * @throws Exception
     */

    public PageInfo selectOrder4User(int page, int pageSize, String userid, String nowtype) throws Exception{
        if (userid==null || userid.isEmpty()){
            return new PageInfo(new ArrayList());
        }

        SimpleDateFormat nowdaysdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat nowtimesdf = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        String _nowday = nowdaysdf.format(now);
        String _nowtime = nowtimesdf.format(now);

        Map param = new HashMap();
        param.put("nowday", nowdaysdf.parse(_nowday));
        param.put("nowtime", nowtimesdf.parse(_nowtime));
        param.put("userid", userid);
        param.put("nowtype", nowtype);

        PageHelper.startPage(page, pageSize);
        List list = this.apiVenueMapper.selectOrder4User(param);

        PageInfo pageInfo = new PageInfo(list);

        return pageInfo;
    }


    /**
     * 黑名单定时调用处理--场馆活动室
     */
    public void jobBlackCall4Ven(){
        try {
            int checkNumber = 3;

            List<Map> list = this.apiVenueMapper.selectOrder4BlackListCheck(checkNumber, new Date());
            if (list == null || list.size() == 0) {
                return;
            }

            for (Map ent : list) {
                try {
                    String userid = (String) ent.get("userid");
                    String phone = (String) ent.get("phone");

                    this.whgBlackListService.addBlackList(userid, phone, checkNumber+"次以上没有进行预订活动室验票");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    continue;
                }
            }

        } catch (Exception e) {
            log.error("jobBlackCall4Ven error", e);
        }
    }
}
