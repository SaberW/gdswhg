package com.creatoo.hn.services.api.apiinside;

import com.creatoo.hn.dao.mapper.WhgTraMapper;
import com.creatoo.hn.dao.mapper.api.ApiTraMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */
@Service
public class InsTraService {

    @Autowired
    private ApiTraMapper apiTraMapper;

    @Autowired
    private WhgTraMapper whgTraMapper;

    /**
     * 查询列表
     * @param page
     * @param pageSize
     * @param cultid
     * @param arttype
     * @param type
     * @param title
     * @return
     * @throws Exception
     */
    public PageInfo getTraList(int page, int pageSize, List cultid,List deptid, String arttype, String type, String title,String protype,String province,String city,String area,String psprovince,String pscity) throws Exception {
        PageHelper.startPage(page,pageSize);
        List traList = this.apiTraMapper.selInsTraList(cultid,deptid,arttype,type,title,protype,province,city,area,psprovince,pscity);
        return new PageInfo(traList);
    }

    /**
     * 查询详情
     * @param id
     * @return
     */
    public Object getOneTra(String id,String protype) {
        return this.apiTraMapper.selectOneTra(id,protype);
    }

    /**
     * 查询推荐培训
     * @param traid
     * @param cultid
     * @param size
     * @param protype
     * @return
     */
    public List selectRecommendTraList(String traid, List cultid, Integer size, String protype) {
        if (size!=null){
            PageHelper.startPage(1, size);
        }
        return this.apiTraMapper.selectTrainRecommend(protype, cultid, traid);
    }
}
