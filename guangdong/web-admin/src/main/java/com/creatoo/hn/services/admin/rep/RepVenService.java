package com.creatoo.hn.services.admin.rep;

import com.creatoo.hn.mapper.statistics.StatisticsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by rbg on 2017/6/26.
 */

@SuppressWarnings("ALL")
@Service
public class RepVenService {
    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private StatisticsMapper statisticsMapper;

    public Map count4Type() throws Exception{

        List<Map> rest = this.statisticsMapper.selectRepCount4RoomType();

        List<String> categories = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        if (rest != null){
            for(Map ent : rest){
                try {
                    Long repcount = (Long) ent.get("repcount");
                    categories.add( (String)ent.get("name") );
                    data.add( repcount.intValue() );
                } catch (NumberFormatException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        Map restMap = new HashMap();
        restMap.put("categories", categories);
        restMap.put("data", data);

        return restMap;
    }

    public Map count4Area() throws Exception{

        List<Map> rest = this.statisticsMapper.selectRepCount4RoomArea();

        List<String> categories = new ArrayList<>();
        List<Integer> data = new ArrayList<>();
        if (rest != null){
            for(Map ent : rest){
                try {
                    Long repcount = (Long) ent.get("repcount");
                    categories.add( (String)ent.get("name") );
                    data.add( repcount.intValue() );
                } catch (NumberFormatException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        Map restMap = new HashMap();
        restMap.put("categories", categories);
        restMap.put("data", data);

        return restMap;
    }

    public Map count4Year() throws Exception{
        int[] data = new int[12];
        String[] categories = new String[12];
        for(int i=0; i<12; i++){
            data[i] = 0;
            categories[i] = (i+1)+"月";
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        Date beginYear = c.getTime();
        c.add(Calendar.YEAR, 1);
        Date endYear = c.getTime();

        List<Map> rest = this.statisticsMapper.selectRepCount4RoomYear(beginYear, endYear);
        if (rest != null){
            for(Map ent : rest){
                Integer repmonth = (Integer) ent.get("repmonth");
                Long repcount = (Long) ent.get("repcount");
                data[repmonth-1] = repcount.intValue();
            }
        }

        Map restMap = new HashMap();
        restMap.put("categories", categories);
        restMap.put("data", data);

        return restMap;
    }

    public List selectCount4Top10() throws Exception{
        return this.statisticsMapper.selectRepCount4RoomTop10();
    }

    public PageInfo selectTime4RoomOpen(int page, int rows, String title) throws Exception{
        PageHelper.startPage(page, rows);
        List list = this.statisticsMapper.selectRepTime4RoomOpen(title);
        return new PageInfo(list);
    }

    public PageInfo selectTime4RoomOrder(int page, int rows, String title) throws Exception{
        PageHelper.startPage(page, rows);
        List list = this.statisticsMapper.selectRepTime4RoomOrder(title);
        return new PageInfo(list);
    }
}
