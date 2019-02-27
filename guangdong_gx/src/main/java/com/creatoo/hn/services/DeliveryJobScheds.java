package com.creatoo.hn.services;

import com.creatoo.hn.dao.model.WhgDelivery;
import com.creatoo.hn.services.admin.delivery.WhgDeliveryService;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by rbg on 2017/10/18.
 */
@Component
public class DeliveryJobScheds {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WhgDeliveryService whgDeliveryService;


    /**
     * 定时处理众筹状态变更后的短信或消息通知
     */
    /*@Scheduled(cron = "0 0 0,8 ? * ? ")  */
    @Scheduled(cron = "0 */10 * * * ?")
    public void jobSchedDeliveryMessage(){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.debug("=======>>>   jobSchedDeliveryMessage  call start >>>>>>>>>>" + sdf.format(new Date()));

        try {
            // this.whgDeliveryService.sendAdminSms();
        } catch (Exception e) {
            log.error("job error ===> jobSchedDeliveryMessage error", e);
        }

        log.debug("=======<<<   jobSchedDeliveryMessage  call end   <<<<<<<<<<" + sdf.format(new Date()));
    }
}
