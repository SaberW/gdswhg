package com.creatoo.hn.services.admin.rep;

import com.creatoo.hn.mapper.statistics.StatisticsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rbg on 2017/6/28.
 */

@SuppressWarnings("ALL")
@Service
public class RepSmsService {
    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private StatisticsMapper statisticsMapper;

    private Date getTargetMonthBegin(Date targetMonth){
        if (targetMonth == null) return null;

        Calendar c = Calendar.getInstance();
        c.setTime(targetMonth);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    private Date getTargetMonthEnd(Date targetMonth){
        if (targetMonth == null) return null;

        Date _begin = this.getTargetMonthBegin(targetMonth);
        Calendar c = Calendar.getInstance();
        c.setTime(_begin);
        c.add(Calendar.MONTH, 1);

        return c.getTime();
    }

    public PageInfo selectRepSmsCount4Room(int page, int rows, Date targetMonth) throws Exception{
        Date beginMonth = this.getTargetMonthBegin(targetMonth);
        Date endMonth = this.getTargetMonthEnd(targetMonth);

        PageHelper.startPage(page, rows);
        List list = this.statisticsMapper.selectRepSmsCount4Room(beginMonth,endMonth);
        return new PageInfo(list);
    }

    public PageInfo selectRepSmsCount4Tra(int page, int rows, Date targetMonth) throws Exception{
        Date beginMonth = this.getTargetMonthBegin(targetMonth);
        Date endMonth = this.getTargetMonthEnd(targetMonth);

        PageHelper.startPage(page, rows);
        List list = this.statisticsMapper.selectRepSmsCount4Tra(beginMonth,endMonth);
        return new PageInfo(list);
    }

    public PageInfo selectRepSmsCount4Act(int page, int rows, Date targetMonth) throws Exception{
        Date beginMonth = this.getTargetMonthBegin(targetMonth);
        Date endMonth = this.getTargetMonthEnd(targetMonth);

        PageHelper.startPage(page, rows);
        List list = this.statisticsMapper.selectRepSmsCount4Act(beginMonth,endMonth);
        return new PageInfo(list);
    }

    public PageInfo selectRepZxCount(int page, int rows, Date targetMonth) throws Exception{
        Date beginMonth = this.getTargetMonthBegin(targetMonth);
        Date endMonth = this.getTargetMonthEnd(targetMonth);

        PageHelper.startPage(page, rows);
        List list = this.statisticsMapper.selectRepZxCount(beginMonth,endMonth);
        return new PageInfo(list);
    }

}
