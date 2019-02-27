package com.creatoo.hn.services.admin.rep;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.mapper.WhgTraMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2017/6/26.
 */
@Service
public class WhgTraRepService {

    @Autowired
    private WhgTraMapper whgTraMapper;

    public ResponseBean t_srchTraByEtype() throws Exception {
        ResponseBean res = new ResponseBean();
        List<Map> traList = this.whgTraMapper.selTraByEtype();
        res.setData(traList);
        return res;
    }

    public ResponseBean t_srchTraByArea() throws Exception {
        ResponseBean res = new ResponseBean();
        List<Map> traList = this.whgTraMapper.selTraByArea();
        res.setData(traList);
        return res;
    }

    /**
     * 查询培训top10
     * @return
     */
    public ResponseBean t_searchTraTop10() {
        ResponseBean res = new ResponseBean();
        List<Map> traList = whgTraMapper.t_searchTraTop10();
        res.setRows(traList);
        return res;
    }

    /**
     * 查询每月培训发布
     * @return
     */
    public Map t_srchTraByMonth() {
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

        List<Map> rest = this.whgTraMapper.srchTraByMonth(beginYear, endYear);
        if (rest != null){
            for(Map ent : rest){
                Integer repmonth = (Integer) ent.get("mymonth");
                Long repcount = (Long) ent.get("mycount");
                data[repmonth-1] = repcount.intValue();
            }
        }

        Map restMap = new HashMap();
        restMap.put("categories", categories);
        restMap.put("data", data);

        return restMap;
        //
//        ResponseBean res = new ResponseBean();
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.YEAR,0);
//        List<Map> traList = this.whgTraMapper.srchTraByMonth();
//        return res;
    }

    /**
     * 培训数据统计
     * @return
     */
    public ResponseBean t_reptra(HttpServletRequest req)throws Exception {
        ResponseBean res = new ResponseBean();
        Map<String,Object> params = new HashMap<String,Object>();
        int page = Integer.parseInt(req.getParameter("page"));
        int rows = Integer.parseInt(req.getParameter("rows"));
        String title = req.getParameter("title");
        String sort = req.getParameter("sort");
        String order = req.getParameter("order");
        params.put("title",title);
        params.put("sort",sort);
        params.put("order",order);
        PageHelper.startPage(page,rows);
        List<Map> traList = this.whgTraMapper.reptra(params);
        PageInfo info = new PageInfo(traList);
        res.setTotal(info.getTotal());
        res.setRows(info.getList());
        return res;
    }
}
