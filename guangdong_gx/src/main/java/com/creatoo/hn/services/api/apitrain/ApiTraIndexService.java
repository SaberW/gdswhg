package com.creatoo.hn.services.api.apitrain;

import com.creatoo.hn.dao.mapper.WhgTraMapper;
import com.creatoo.hn.dao.mapper.api.ApiTraMajorMapper;
import com.creatoo.hn.dao.mapper.api.ApiTraMapper;
import com.creatoo.hn.services.BaseService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/9.
 */
@Service
public class ApiTraIndexService extends BaseService{

    @Autowired
    private WhgTraMapper whgTraMapper;

    @Autowired
    private ApiTraMapper apiTraMapper;

    @Autowired
    private ApiTraMajorMapper apiTraMajorMapper;

    /**
     * 查询首页培训
     * @param cultid
     * @return
     */
    public List t_getindexTraList(String cultid,Integer size,String protype) throws Exception {
        if (size!=null){
            PageHelper.startPage(1, size);
        }
        List cultlist=null;
        if(cultid!=null&&!cultid.equals("")){
            cultlist= Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
        }
        List<Map> list = apiTraMapper.selindexTraList(cultlist,protype);
        return list;
    }

    /**
     * 查询首页的直播
     * @param cultid
     * @param size
     * @param protype
     * @return
     */
    public List t_getindexTraliveList(String cultid, Integer size, String protype) {
        if (size!=null){
            PageHelper.startPage(1, size);
        }
        List cultlist=null;
        if(cultid!=null&&!cultid.equals("")){
            cultlist=Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
        }
        List<Map> list = apiTraMapper.selindexTraliveList(cultlist,protype);
        return list;
    }

    /**
     * 查询首页的微专业
     * @param cultid
     * @param size
     * @return
     */
    public List t_getindexTraMajorList(String cultid, Integer size) {
        if (size!=null){
            PageHelper.startPage(1, size);
        }
        List cultlist=null;
        if(cultid!=null&&!cultid.equals("")){
            cultlist=Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
        }
        List<Map> list = apiTraMajorMapper.selindexTraMajorList(cultlist);
        return list;
    }
}
