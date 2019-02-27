package com.creatoo.hn.services;

import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.admin.train.WhgTrainService;
import com.creatoo.hn.services.api.apioutside.activity.ApiActivityService;
import com.creatoo.hn.services.api.apioutside.venue.ApiVenueService;
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
public class JobScheds {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WhgGatherService whgGatherService;

    public final static long JOB_MINUTE = 1000*60*5;

    /**
     * 定时处理众筹时间结束后的成功状态
     */
    @Scheduled(initialDelay = JOB_MINUTE, fixedDelay = JOB_MINUTE)
    public void jobSchedGathers(){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.debug("=======>>>   jobSchedGathers  call start >>>>>>>>>>" + sdf.format(new Date()));

        try {
            this.whgGatherService.jobTimeGather();
        } catch (Exception e) {
            log.error("job error ===> jobSchedGathers error", e);
        }

        log.debug("=======<<<   jobSchedGathers  call end   <<<<<<<<<<" + sdf.format(new Date()));
    }

    /**
     * 定时处理众筹状态变更后的短信或消息通知
     */
    //@Scheduled(cron = "0 0 9,12,18 * * ?")
    @Scheduled(initialDelay = JOB_MINUTE, fixedDelay = JOB_MINUTE)
    public void jobSchedGathersMessage(){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.debug("=======>>>   jobSchedGathersMessage  call start >>>>>>>>>>" + sdf.format(new Date()));

        try {
            this.whgGatherService.jobTimeGatherMessage();
        } catch (Exception e) {
            log.error("job error ===> jobSchedGathersMessage error", e);
        }

        log.debug("=======<<<   jobSchedGathersMessage  call end   <<<<<<<<<<" + sdf.format(new Date()));
    }



    @Autowired
    private ApiVenueService apiVenueService;


    @Autowired
    private ApiActivityService apiActivityService;

    @Autowired
    private WhgTrainService whgTrainService;

    /**
     * 黑名单定时任务
     */
    @Scheduled(initialDelay = JOB_MINUTE, fixedDelay = JOB_MINUTE)
    public void jobSchedBlackList(){
        try {
            //活动室预订黑名单检查
            this.apiVenueService.jobBlackCall4Ven();
            //活动预订黑名单检查
            this.apiActivityService.jobBlackCall4Act("HD");
            //众筹预订黑名单检查
            this.apiActivityService.jobBlackCall4Act("ZC");
            //培训黑名单检查
            this.whgTrainService.t_addPxBlacklist("PX");
            //众筹培训黑名单检查
            this.whgTrainService.t_addPxBlacklist("ZC");

        } catch (Exception e) {
            log.error("job error ===> jobSchedBlackList error", e);
        }
    }
}
