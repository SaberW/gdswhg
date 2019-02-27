package com.creatoo.hn.services.admin.venue;

import com.creatoo.hn.dao.mapper.WhgVenRoomMapper;
import com.creatoo.hn.dao.mapper.WhgVenRoomOrderMapper;
import com.creatoo.hn.dao.mapper.admin.CrtWhgVenueMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgVenRoom;
import com.creatoo.hn.dao.model.WhgVenRoomOrder;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.api.apiinside.InsMessageService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by rbg on 2017/3/30.
 */

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames="venue", keyGenerator = "simpleKeyGenerator")
public class WhgVenroomorderService extends BaseService{

    @Autowired
    private WhgVenRoomOrderMapper whgVenRoomOrderMapper;

    @Autowired
    private WhgVenroomService whgVenroomService;

    @Autowired
    private SMSService smsService;

   /*@Autowired
    private WhgVenRoomMapper whgVenRoomMapper;*/

    @Autowired
    private CrtWhgVenueMapper crtWhgVenueMapper;

    @Cacheable
    public PageInfo srchList4p(int page, int rows, WhgVenRoomOrder roomOrder, String sort, String order, Date startDay, Date endDay) throws Exception{
        Example example = new Example(WhgVenRoomOrder.class);
        Example.Criteria c = example.createCriteria();

        if (roomOrder!=null && roomOrder.getOrdercontactphone()!=null){
            c.andLike("ordercontactphone", "%"+roomOrder.getOrdercontactphone()+"%");
            roomOrder.setOrdercontactphone(null);
        }
        c.andEqualTo(roomOrder);
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
            example.orderBy("timeday").desc();
        }

        PageHelper.startPage(page, rows);
        List list = this.whgVenRoomOrderMapper.selectByExample(example);

        return new PageInfo(list);
    }

    public PageInfo srchList4pOrders(int page, int rows, Map record /*WhgVenRoomOrder roomOrder, String sort, String order, String roomTitle, Date startDay, Date endDay*/) throws Exception{
        /*Example example = new Example(WhgVenRoomOrder.class);
        Example.Criteria c = example.createCriteria();

        if (roomOrder!=null){
            if (roomOrder.getOrdercontactphone()!=null){
                c.andLike("ordercontactphone", "%"+roomOrder.getOrdercontactphone()+"%");
                roomOrder.setOrdercontactphone(null);
            }

            c.andEqualTo(roomOrder);
        }

        if (roomTitle != null && !roomTitle.isEmpty()){
            Example roomexp = new Example(WhgVenRoom.class);
            roomexp.selectProperties("id");
            roomexp.or().andLike("title", "%"+roomTitle+"%");
            List<WhgVenRoom> roomlist = this.whgVenRoomMapper.selectByExample(roomexp);
            List roomids = new ArrayList();
            if (roomlist!=null && roomlist.size()>0){
                for(WhgVenRoom vr : roomlist){
                    roomids.add(vr.getId());
                }
            }else{
                roomids.add('0'); //用于无活动室结果时阻止单订结果
            }
            c.andIn("roomid", roomids);
        }

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
            example.orderBy("timeday").desc();
        }

        PageHelper.startPage(page, rows);
        List<WhgVenRoomOrder> list = this.whgVenRoomOrderMapper.selectByExample(example);

        PageInfo pageInfo = new PageInfo(list);
        List restList = new ArrayList();
        BeanMap bm = new BeanMap();
        for(WhgVenRoomOrder vro : list){
            Map item = new HashMap();
            bm.setBean(vro);
            item.putAll(bm);
            WhgVenRoom _room = this.whgVenRoomMapper.selectByPrimaryKey(vro.getRoomid());
            item.put("roomTitle", _room.getTitle());

            restList.add(item);
        }
        pageInfo.setList(restList);

        return pageInfo;*/

        PageHelper.startPage(page, rows);
        List list = this.crtWhgVenueMapper.srchListVenroomOrders(record);
        return new PageInfo(list);
    }

    //@Cacheable
    public WhgVenRoomOrder srchOne(String orderid) throws Exception{
        return this.whgVenRoomOrderMapper.selectByPrimaryKey(orderid);
    }


    @CacheEvict(allEntries = true)
    public ResponseBean t_updstate(WhgVenRoomOrder roomOrder, String formstates, int tostate, WhgSysUser user) throws Exception{
        ResponseBean rb = new ResponseBean();
        if (roomOrder == null || roomOrder.getId() == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("预定标记丢失");
            return rb;
        }

        WhgVenRoomOrder trgOrder = this.whgVenRoomOrderMapper.selectByPrimaryKey(roomOrder.getId());

        //验证是目标是否为前置状态
        List<String> forstates = Arrays.asList( formstates.split("\\s*,\\s*") );
        String state = String.valueOf( trgOrder.getState() );
        if ( !forstates.contains(state) ){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("核审的预订状态已变更，请刷新数据后再操作");
            return rb;
        }

        if (tostate == 3){
            //通过验证时检查是否有重复通过
            WhgVenRoomOrder recode = new WhgVenRoomOrder();
            recode.setState(3);
            recode.setRoomid(trgOrder.getRoomid());
            recode.setTimeday(trgOrder.getTimeday());
            recode.setTimestart(trgOrder.getTimestart());
            recode.setTimeend(trgOrder.getTimeend());

            int countState3 = this.whgVenRoomOrderMapper.selectCount(recode);
            if (countState3 > 0){
                rb.setSuccess(ResponseBean.FAIL);
                rb.setErrormsg("已存在相同时段其它通过的审核");
                return rb;
            }
        }
        if (tostate == 2){
            //拒绝时写入摘要
            trgOrder.setOrdersummary(roomOrder.getOrdersummary());
        }

        trgOrder.setState(tostate);
        this.whgVenRoomOrderMapper.updateByPrimaryKeySelective(trgOrder);
        sendOrderSMS(trgOrder);
        return rb;
    }


    @Autowired
    private InsMessageService insMessageService;

    private void sendOrderSMS(WhgVenRoomOrder trgOrder) {
        try {
            String phome = trgOrder.getOrdercontactphone();
            String sempTemp = "VEN_ORDER_SUCCESS";
            if (trgOrder.getState() != null && trgOrder.getState().compareTo(new Integer(2))==0){
                sempTemp = "VEN_ORDER_FAIL";
            }

            WhgVenRoom room = this.whgVenroomService.srchOne(trgOrder.getRoomid());
            Map<String, String> data = new HashMap<>();
            data.put("userName", trgOrder.getOrdercontact());
            data.put("title", room.getTitle());
            data.put("orderNum", trgOrder.getOrderid());

            if (room.getNoticetype() != null && !room.getNoticetype().isEmpty()) {
                String noticetype = room.getNoticetype();
                if (noticetype.contains("ZNX")){
                    try{
                        this.insMessageService.t_sendZNX(trgOrder.getUserid(), null, sempTemp, data, trgOrder.getRoomid(), EnumProject.PROJECT_WBGX.getValue());
                    }catch (Exception e){
                        log.error("roomOrder t_sendZNX error", e);
                    }
                }
                if (noticetype.contains("SMS")){
                    try {
                        this.smsService.t_sendSMS(phome, sempTemp, data, trgOrder.getRoomid());
                    } catch (Exception e) {
                        log.error("roomOrder t_sendSMS error", e);
                    }
                }
            }

        } catch (Exception e) {
            log.error("roomOrder sendSMS error", e);
        }
    }

}
