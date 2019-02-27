package com.creatoo.hn.services.api.ticket;

import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**取票订票服务
 * Created by caiyong on 2017/7/10.
 */
@SuppressWarnings("all")
@Service
@CacheConfig(cacheNames="ticket", keyGenerator = "simpleKeyGenerator")
public class ApiTicketService extends BaseService {

    @Autowired
    private WhgActivityOrderMapper whgActOrderMapper;

    @Autowired
    private WhgActivityTicketMapper whgActTicketMapper;

    @Autowired
    private WhgVenRoomOrderMapper whgVenRoomOrderMapper;

    @Autowired
    private WhgActivityMapper whgActActivityMapper;

    @Autowired
    private WhgActivityTimeMapper whgActTimeMapper;

    @Autowired
    private WhgVenRoomMapper whgVenRoomMapper;

    @Autowired
    private WhgVenMapper whgVenMapper;

    @Autowired
    private WhgActivitySeatMapper whgActSeatMapper;

    /**
     * 根据活动订单的取票码获取活动订单
     * @param ticketNo
     * @return
     */
    public WhgActivityOrder findActOrderByTicketNo(String ticketNo){
        try {
            Example example = new Example(WhgActivityOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("ordernumber",ticketNo);
            List<WhgActivityOrder> whgActOrderList = whgActOrderMapper.selectByExample(example);
            if(null == whgActOrderList || whgActOrderList.isEmpty()){
                return null;
            }
            return whgActOrderList.get(0);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 根据活动室订单的取票码获取活动室订单
     * @param ticketNo
     * @return
     */
    public WhgVenRoomOrder findVenRoomOrderByTicketNo(String ticketNo){
        try {
            Example example = new Example(WhgVenRoomOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orderid",ticketNo);
            List<WhgVenRoomOrder> whgVenRoomOrderList = whgVenRoomOrderMapper.selectByExample(example);
            if(null == whgVenRoomOrderList || whgVenRoomOrderList.isEmpty()){
                return null;
            }
            return whgVenRoomOrderList.get(0);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 获取活动信息
     * @param map
     * @return
     */
    public WhgActivity findActByParam(Map map){
        try {
            Example example = new Example(WhgActivity.class);
            Example.Criteria criteria = example.createCriteria();
            for(Object key : map.keySet()){
                criteria.andEqualTo((String)key,map.get(key));
            }
            List<WhgActivity> whgActActivityList = whgActActivityMapper.selectByExample(example);
            if(null == whgActActivityList || whgActActivityList.isEmpty()){
                return null;
            }
            return whgActActivityList.get(0);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 获取活动场次信息
     * @param map
     * @return
     */
    public WhgActivityTime findActTimeByParam(Map map){
        try {
            Example example = new Example(WhgActivityTime.class);
            Example.Criteria criteria = example.createCriteria();
            for(Object key : map.keySet()){
                criteria.andEqualTo((String)key,map.get(key));
            }
            List<WhgActivityTime> whgActTimeList = whgActTimeMapper.selectByExample(example);
            if(null == whgActTimeList || whgActTimeList.isEmpty()){
                return null;
            }
            return whgActTimeList.get(0);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 获取活动票务信息
     * @param map
     * @return
     */
    public List<WhgActivityTicket> findActTicketByParam(Map map){
        try {
            Example example = new Example(WhgActivityTicket.class);
            Example.Criteria criteria = example.createCriteria();
            for(Object key : map.keySet()){
                criteria.andEqualTo((String)key,map.get(key));
            }
            List<WhgActivityTicket> whgActTicketList = whgActTicketMapper.selectByExample(example);
            if(null == whgActTicketList || whgActTicketList.isEmpty()){
                return null;
            }
            return whgActTicketList;
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 获取座位信息
     * @param seatId
     * @return
     */
    public WhgActivitySeat findActSeatBySeatId(String seatId){
        try {
            WhgActivitySeat whgActSeat = new WhgActivitySeat();
            whgActSeat.setId(seatId);
            return whgActSeatMapper.selectOne(whgActSeat);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 获取活动室信息
     * @param map
     * @return
     */
    public WhgVenRoom findVenRoomByParam(Map map){
        try {
            Example example = new Example(WhgVenRoom.class);
            Example.Criteria criteria = example.createCriteria();
            for(Object key : map.keySet()){
                criteria.andEqualTo((String)key,map.get(key));
            }
            List<WhgVenRoom> whgVenRoomList = whgVenRoomMapper.selectByExample(example);
            if(null == whgVenRoomList || whgVenRoomList.isEmpty()){
                return null;
            }
            return whgVenRoomList.get(0);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 获取场馆信息
     * @param map
     * @return
     */
    public WhgVen findVenByParam(Map map){
        try {
            Example example = new Example(WhgVen.class);
            Example.Criteria criteria = example.createCriteria();
            for(Object key : map.keySet()){
                criteria.andEqualTo((String)key,map.get(key));
            }
            List<WhgVen> whgVenList = whgVenMapper.selectByExample(example);
            if(null == whgVenList || whgVenList.isEmpty()){
                return null;
            }
            return whgVenList.get(0);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 修改活动订单状态
     * @param orderId
     * @param ticketStatus
     * @return
     */
    public Integer updateActOrderState(String orderId,Integer ticketStatus){
        try {
            WhgActivityOrder whgActOrder = new WhgActivityOrder();
            whgActOrder.setId(orderId);
            whgActOrder = whgActOrderMapper.selectOne(whgActOrder);
            if(null == whgActOrder){
                return 1;
            }
            whgActOrder.setTicketstatus(ticketStatus);
            if(2 == ticketStatus){
                Integer printtickettimes = whgActOrder.getPrinttickettimes();
                printtickettimes = (null != printtickettimes?printtickettimes:0);
                whgActOrder.setPrinttickettimes(printtickettimes + 1);
            }
            whgActOrderMapper.updateByPrimaryKey(whgActOrder);
            return 0;
        }catch (Exception e){
            log.error(e.toString());
            return 1;
        }
    }

    /**
     * 修改活动室订单状态
     * @param orderId
     * @param state
     * @return
     */
    public Integer updateVenRoomOrderState(String orderId,Integer state){
        try {
            WhgVenRoomOrder whgVenRoomOrder = new WhgVenRoomOrder();
            whgVenRoomOrder.setId(orderId);
            whgVenRoomOrder = whgVenRoomOrderMapper.selectOne(whgVenRoomOrder);
            if(null == whgVenRoomOrder){
                return 1;
            }
            whgVenRoomOrder.setTicketstatus(state);
            if(2 == state){
                Integer printtickettimes = whgVenRoomOrder.getPrinttickettimes();
                printtickettimes = (null != printtickettimes?printtickettimes:0);
                whgVenRoomOrder.setPrinttickettimes(printtickettimes + 1);
            }
            whgVenRoomOrderMapper.updateByPrimaryKey(whgVenRoomOrder);
            return 0;
        }catch (Exception e){
            log.error(e.toString());
            return 1;
        }
    }
}
