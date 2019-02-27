package com.creatoo.hn.services.admin.activity;
import com.creatoo.hn.dao.mapper.WhgActivityOrderMapper;
import com.creatoo.hn.dao.mapper.WhgActivityTicketMapper;
import com.creatoo.hn.dao.model.WhgActivityOrder;
import com.creatoo.hn.dao.model.WhgActivityTicket;
import com.creatoo.hn.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动管理 服务层
 * @author heyi
 *
 */

@Service
@CacheConfig(cacheNames = "WhgActivityOrder", keyGenerator = "simpleKeyGenerator")
public class WhgActivityOrderService extends BaseService {
	
   
   @Autowired
    private WhgActivityOrderMapper WhgActivityOrderMapper;
    
    @Autowired
    private WhgActivityTicketMapper WhgActivityTicketMapper;

    /**
     * 根据订单Id获取订单对象
     * @param orderId
     * @return
     */
    @Cacheable
    public WhgActivityOrder findWhgActivityOrder4Id(String orderId){
    	WhgActivityOrder WhgActivityOrder = WhgActivityOrderMapper.selectByPrimaryKey(orderId);
    	return WhgActivityOrder;
    }
    
    /**
     * 根据订单Id查询座位信息
     * @param orderId
     * @return
     */
    @Cacheable
    public int findWhgActivityTicket4OrderId(String orderId){
    	WhgActivityTicket WhgActivityTicket = new WhgActivityTicket();
    	WhgActivityTicket.setOrderid(orderId);
    	return WhgActivityTicketMapper.selectCount(WhgActivityTicket);
    }
    @CacheEvict(allEntries = true)
    public int updateActOrder(String orderId){
    	WhgActivityOrder actOrder = WhgActivityOrderMapper.selectByPrimaryKey(orderId);
    	actOrder.setOrderisvalid(2);
		actOrder.setTicketstatus(3);
		return WhgActivityOrderMapper.updateByPrimaryKey(actOrder);
    }
    @Cacheable
    public List<WhgActivityOrder> findWhgActivityOrder4EventId(String eventId){
    	WhgActivityOrder WhgActivityOrder = new WhgActivityOrder();
    	WhgActivityOrder.setEventid(eventId);
    	return WhgActivityOrderMapper.select(WhgActivityOrder);
    }
    
}
