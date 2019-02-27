package com.creatoo.hn.services.admin.mass;


import com.creatoo.hn.dao.mapper.WhgMassLibraryMapper;
import com.creatoo.hn.dao.mapper.mass.CrtWhgMassLibraryMapper;
import com.creatoo.hn.dao.model.WhgMassLibrary;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.creatoo.hn.util.enums.EnumUploadType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Service
public class WhgMassCountService extends BaseService{

    @Autowired
    private WhgMassLibraryMapper whgMassLibraryMapper;

    @Autowired
    private CrtWhgMassLibraryMapper crtWhgMassLibraryMapper;

    @Autowired
    private WhgMassLibraryService whgMassLibraryService;


    /**
     * 按资源类型统计发布量
     * @pam cultid
     * @return
     * @throws Exception
     */
    public List countData4restype(String cultid) throws Exception{
        List list = new ArrayList();

        WhgMassLibrary wml = new WhgMassLibrary();
        wml.setCultid(cultid);
        wml.setState(1);

        Map exp = new HashMap();
        exp.put("state", EnumBizState.STATE_PUB.getValue());
        exp.put("delstate", EnumStateDel.STATE_DEL_NO.getValue());

        //表对应对资源类型
        EnumUploadType[] enumTypes = EnumUploadType.values();
        for (EnumUploadType type : enumTypes) {
            Map map = new HashMap();
            map.put("tick", type.getName());

            long counts = 0L;
            try {
                wml.setResourcetype(type.getValue());
                List<WhgMassLibrary> lbs = this.whgMassLibraryMapper.select(wml);

                for(WhgMassLibrary lb : lbs){
                    String table = "whg_mass_library_"+lb.getCultid()+"_"+lb.getTablename();
                    int count = this.crtWhgMassLibraryMapper.countByTableNameExample(table, exp);
                    counts += count;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            map.put("value", counts);

            list.add(map);
        }

        return list;
    }

    /**
     * 按年月统计
     * @param cultid
     * @return
     * @throws Exception
     */
    public List countData4yearMonth(String cultid) throws Exception{
        List list = new ArrayList();

        WhgMassLibrary wml = new WhgMassLibrary();
        wml.setCultid(cultid);
        wml.setState(1);

        Map exp = new HashMap();
        exp.put("state", EnumBizState.STATE_PUB.getValue());
        exp.put("delstate", EnumStateDel.STATE_DEL_NO.getValue());

        //当前年的限制起始时间
        LocalDate date = LocalDate.now();
        LocalDateTime beginTime = LocalDateTime.of(date.getYear(), 01, 01, 0, 0, 0);
        LocalDateTime endTime = beginTime.plusYears(1);
        exp.put("beginPublishtime", beginTime);
        exp.put("endPublishtime", endTime);

        Month[] months = Month.values();

        for (Month month : months){
            Map map = new HashMap();
            map.put("tick", month.getValue() +"月");

            long counts = 0L;
            try {
                List<WhgMassLibrary> lbs = this.whgMassLibraryMapper.select(wml);

                for(WhgMassLibrary lb : lbs){
                    String table = "whg_mass_library_"+lb.getCultid()+"_"+lb.getTablename();
                    exp.put("month", month.getValue());
                    int count = this.crtWhgMassLibraryMapper.countByTableNameExample(table, exp);
                    counts += count;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            map.put("value", counts);

            list.add(map);
        }

        return list;
    }


    /**
     * top10
     * @param cultid
     * @return
     * @throws Exception
     */
    public List countData4Top(String cultid) throws Exception{
        WhgMassLibrary wml = new WhgMassLibrary();
        wml.setCultid(cultid);
        wml.setState(1);

        List<WhgMassLibrary> lbs = this.whgMassLibraryMapper.select(wml);
        if (lbs == null || lbs.size() == 0) {
            return new ArrayList();
        }

        List<String> tabs = new ArrayList();
        for(WhgMassLibrary lb : lbs){
            String table = "whg_mass_library_"+lb.getCultid()+"_"+lb.getTablename();
            tabs.add(table);
        }
        if (tabs.size() == 0) {
            return new ArrayList();
        }

        List<Map> list = this.crtWhgMassLibraryMapper.countByTop(tabs);

        for(Map ent : list){
            Map resinfo = this.whgMassLibraryService.findResourceById(ent.get("eid").toString());
            if (resinfo!=null){
                ent.put("name", resinfo.get("resname"));
            }
        }

        return list;
    }
}
