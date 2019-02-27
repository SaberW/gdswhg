package com.creatoo.hn.services.admin.venue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.dao.mapper.WhgVenRoomTimeMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgVenRoom;
import com.creatoo.hn.dao.model.WhgVenRoomTime;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rbg on 2017/3/30.
 */

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames="venue", keyGenerator = "simpleKeyGenerator")
public class WhgVenroomtimeService extends BaseService {

    @Autowired
    private WhgVenRoomTimeMapper whgVenRoomTimeMapper;

    @Autowired
    private WhgVenroomService whgVenroomService;

    @Cacheable
    public PageInfo srchList4p(int page, int rows, WhgVenRoomTime roomTime, String sort, String order, Date startDay, Date endDay) throws Exception{
        Example example = new Example(WhgVenRoomTime.class);
        Example.Criteria c = example.createCriteria();

        c.andEqualTo(roomTime);
        if (startDay != null){
            c.andGreaterThanOrEqualTo("timeday", startDay);
        }
        if (endDay != null){
            c.andLessThanOrEqualTo("timeday", endDay);
        }

        //排序
        if (sort!=null && !sort.isEmpty()){
            if (order!=null && "asc".equalsIgnoreCase(order)){
                example.orderBy(sort).asc();
            }else{
                example.orderBy(sort).desc();
            }
        }else{
            example.setOrderByClause("timeday desc, timestart desc");
        }

        PageHelper.startPage(page, rows);
        List list = this.whgVenRoomTimeMapper.selectByExample(example);

        return new PageInfo(list);
    }


    @CacheEvict(allEntries = true)
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser user) throws Exception{
        ResponseBean rb = new ResponseBean();
        if (ids == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("预定时段标记丢失");
            return rb;
        }
        Example example = new Example(WhgVenRoomTime.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgVenRoomTime roomTime = new WhgVenRoomTime();
        roomTime.setState(tostate);
        this.whgVenRoomTimeMapper.updateByExampleSelective(roomTime, example);

        return rb;
    }

    /**
     * 取得添加预定时段的起始时间
     * @return
     */
    //@Cacheable
    public Date selectAddStartDay(String roomid) {
        //当前时间
        Calendar c = Calendar.getInstance();

        try {
            WhgVenRoomTime roomTime = getMaxDayTime4RoomId(roomid);
            //当前日期小于等于最大日期时，启用最大日期
            if (roomTime!=null && roomTime.getTimeday()!=null && c.getTime().compareTo(roomTime.getTimeday())<=0){
                c.setTime(roomTime.getTimeday());
            }
        } catch (Exception e) {
            log.debug("获取预定最大时间对象出错", e);
        }

        //返回下一天的时间
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    @CacheEvict(allEntries = true)
    public ResponseBean t_add(String roomid, Date startDay, Date endDay, Integer hasfees) throws Exception{
        ResponseBean rb = new ResponseBean();
        if (startDay == null || endDay == null || roomid==null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("参数错误");
            return rb;
        }

        //获取相关活动室日期模板数据
        WhgVenRoom room = this.whgVenroomService.srchOne(roomid);
        if (room == null || room.getDelstate().compareTo(new Integer(1))==0){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("相关活动室状态异常，操作终止!");
            return rb;
        }
        String weektimejson = room.getWeektimejson();
        if (weektimejson==null || weektimejson.isEmpty()){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("相关活动室预订模板为空，操作终止!");
            return rb;
        }

        JSONObject weekJson = JSON.parseObject(weektimejson);

        //准备起始的天
        Calendar c = Calendar.getInstance();
        c.setTime(startDay);
        //当前的最大时间
        WhgVenRoomTime roomTime = getMaxDayTime4RoomId(roomid);
        if (roomTime!=null && roomTime.getTimeday().compareTo(startDay)>0){
            //修正起始天
            c.setTime(roomTime.getTimeday());
            c.add(Calendar.DAY_OF_YEAR, 1);
        }

        //开始依据规则 写入 预定记录
        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdfday = new SimpleDateFormat("yyyy-MM-dd");
        Pattern pat = Pattern.compile("\\d{2}:\\d{2}");
        while (c.getTime().compareTo(endDay)<=0){
            try {
                //天的星期
                int wdayInt = c.get(Calendar.DAY_OF_WEEK);
                //按天的星期获取对应的模板规则
                JSONArray wdayTimes = (JSONArray) weekJson.get( String.valueOf(wdayInt) );
                if (wdayTimes!=null){
                    for(Object ent : wdayTimes){
                        try {
                            JSONObject wdayTime = (JSONObject) ent;
                            String timestart = (String) wdayTime.get("timestart");
                            String timeend = (String) wdayTime.get("timeend");
                            if (timestart == null || timeend ==null){
                                continue;
                            }

                            Matcher ms = pat.matcher(timestart);
                            Matcher me = pat.matcher(timeend);
                            if (ms.find() && me.find()){
                                timestart = ms.group();
                                timeend = me.group();
                            }else{
                                continue;
                            }

                            Date tms = sdftime.parse(timestart);
                            Date tme = sdftime.parse(timeend);

                            WhgVenRoomTime vrt = new WhgVenRoomTime();
                            vrt.setTimeday(c.getTime());
                            vrt.setTimestart( tms );
                            vrt.setTimeend( tme );
                            vrt.setRoomid(roomid);
                            int count = this.whgVenRoomTimeMapper.selectCount(vrt);
                            if (count > 0){
                                continue;
                            }

                            vrt.setState(0);
                            vrt.setId(IDUtils.getID());

                            //计算时长分钟
                            float timeSum = (tme.getTime() - tms.getTime())/(1000*60);
                            vrt.setTimelong(timeSum);

                            if (hasfees==null) {
                                vrt.setHasfees(room.getHasfees());
                            }else{
                                vrt.setHasfees(hasfees);
                            }

                            this.whgVenRoomTimeMapper.insert(vrt);
                        } catch (Exception e) {
                            log.debug("处理添加预定时段项出错：Day="+sdfday.format(c.getTime())+" week="+wdayInt, e);
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("处理添加预定时段出错：Day="+sdfday.format(c.getTime()), e);
            }

            //加一天继续
            c.add(Calendar.DAY_OF_YEAR, 1);
        }

      return rb;
    }

    @CacheEvict(allEntries = true)
    public ResponseBean t_addOne(WhgVenRoomTime info) throws Exception{
        ResponseBean rb = new ResponseBean();

        WhgVenRoom room = this.whgVenroomService.srchOne(info.getRoomid());
        if (room == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("相关活动室查找失败!");
            return rb;
        }

        //验证此天时段，有无交叉
        Example example = new Example(WhgVenRoomTime.class);
        example.or().andEqualTo("roomid", info.getRoomid()).andEqualTo("timeday", info.getTimeday())
                .andBetween("timestart", info.getTimestart(), info.getTimeend());
        example.or().andEqualTo("roomid", info.getRoomid()).andEqualTo("timeday", info.getTimeday())
                .andBetween("timeend", info.getTimestart(), info.getTimeend());
        example.or().andEqualTo("roomid", info.getRoomid()).andEqualTo("timeday", info.getTimeday())
                .andLessThanOrEqualTo("timestart", info.getTimestart())
                .andGreaterThanOrEqualTo("timeend", info.getTimeend());

        int count = this.whgVenRoomTimeMapper.selectCountByExample(example);
        if (count > 0){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置的活动室时段同天内与其它时段有重复!");
            return rb;
        }

        //计算时长分钟
        Date tme = info.getTimeend();
        Date tms = info.getTimestart();
        if (tme!=null && tms!=null){
            float timeSum = (tme.getTime() - tms.getTime())/(1000*60);
            info.setTimelong(timeSum);
        }

        info.setId(IDUtils.getID());
        info.setState(0);
        if (info.getHasfees() == null){
            info.setHasfees(room.getHasfees());
        }
        this.whgVenRoomTimeMapper.insert(info);

        return rb;
    }


    @CacheEvict(allEntries = true)
    public ResponseBean t_edit(WhgVenRoomTime roomTime, WhgSysUser user) throws Exception{
        ResponseBean rb = new ResponseBean();

        WhgVenRoomTime target = this.whgVenRoomTimeMapper.selectByPrimaryKey(roomTime.getId());
        //验证时段在 此活动室 此天 除自己之外 有无与其它有交叉
        Example example = new Example(WhgVenRoomTime.class);
        example.or().andEqualTo("roomid", roomTime.getRoomid()).andEqualTo("timeday", target.getTimeday()).andNotEqualTo("id", roomTime.getId())
                .andBetween("timestart", roomTime.getTimestart(), roomTime.getTimeend());
        example.or().andEqualTo("roomid", roomTime.getRoomid()).andEqualTo("timeday", target.getTimeday()).andNotEqualTo("id", roomTime.getId())
                .andBetween("timeend", roomTime.getTimestart(), roomTime.getTimeend());
        example.or().andEqualTo("roomid", roomTime.getRoomid()).andEqualTo("timeday", target.getTimeday()).andNotEqualTo("id", roomTime.getId())
                .andLessThanOrEqualTo("timestart", roomTime.getTimestart())
                .andGreaterThanOrEqualTo("timeend", roomTime.getTimeend());

        int count = this.whgVenRoomTimeMapper.selectCountByExample(example);
        if (count > 0){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("设置的活动室时段同天内与其它时段有重复!");
            return rb;
        }

        //计算时长分钟
        Date tme = roomTime.getTimeend();
        Date tms = roomTime.getTimestart();
        if (tme!=null && tms!=null){
            float timeSum = (tme.getTime() - tms.getTime())/(1000*60);
            roomTime.setTimelong(timeSum);
        }

        this.whgVenRoomTimeMapper.updateByPrimaryKeySelective(roomTime);

        return rb;
    }

    private WhgVenRoomTime getMaxDayTime4RoomId(String roomid) throws Exception{
        //取对应活动室生成的预定数据的最大日期
        Example example = new Example(WhgVenRoomTime.class);
        example.createCriteria().andEqualTo("roomid", roomid);
        example.orderBy("timeday").desc();
        List list = this.whgVenRoomTimeMapper.selectByExampleAndRowBounds(example, new RowBounds(0, 1));
        if (list != null && list.size() > 0) {
            WhgVenRoomTime roomTime = (WhgVenRoomTime) list.get(0);
            return roomTime;
        }else{
            return null;
        }
    }
}
