package com.creatoo.hn.services.api.apiinside;

import com.creatoo.hn.dao.mapper.WhgExhGoodsMapper;
import com.creatoo.hn.dao.mapper.WhgShowExhMapper;
import com.creatoo.hn.dao.mapper.WhgYwiTypeMapper;
import com.creatoo.hn.dao.model.WhgExhGoods;
import com.creatoo.hn.dao.model.WhgYwiType;
import com.creatoo.hn.services.comm.ResetRefidService;
import com.creatoo.hn.util.FilterFontUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/5.
 */
@SuppressWarnings("ALL")
@Service
public class InsShowExhService {

    @Autowired
    private WhgShowExhMapper whgShowExhMapper;

    @Autowired
    private WhgExhGoodsMapper whgExhGoodsMapper;

    @Autowired
    private WhgYwiTypeMapper whgYwiTypeMapper;

    @Autowired
    private ResetRefidService resetRefidService;

    /**
     * 展览类商品列表
     * @param page
     * @param pageSize
     * @param cultid
     * @param type
     * @param title
     * @return
     */
    public PageInfo getShowExhList(int page, int pageSize, List cultid,List deptid, String type, String title,String protype,String province,String city,String area,String psprovince,String pscity) {
        PageHelper.startPage(page,pageSize);
        List showGoodsList = this.whgShowExhMapper.selInsShowExhList(cultid,deptid,type,title,protype,province,city,area,psprovince,pscity);
        return new PageInfo(showGoodsList);
    }

    /**
     * 查询展览类商品详情
     * @param id
     * @return
     */
    public Object getdetail(String id,String protype) {
        //return whgShowExhMapper.selectShowExhById(id,protype);
        Map info = (Map) whgShowExhMapper.selectShowExhById(id,protype);

        this.resetRefidService.resetRefid4type(info, "etype");
        return FilterFontUtil.clearFont(info, new String[]{"exhdesc"});
    }

    /**
     * 查询推荐展览
     * @param id
     * @param cultid
     * @param size
     * @param protype
     * @return
     */
    public List selectRecommendExhList(String id, List cultid, Integer size, String protype) {
        if (size!=null){
            PageHelper.startPage(1, size);
        }
        return this.whgShowExhMapper.selectExhRecommend(protype, cultid, id);
    }

    /**
     * 查询相关展品列表
     * @param page
     * @param pageSize
     * @param exhid
     * @return
     */
    public PageInfo selectExhGoodsList4exh(int page,int pageSize,String exhid,String cultid) {
        boolean isSplit = exhid.contains(",");
        List<WhgExhGoods> list = new ArrayList<>();
        Example example = new Example(WhgExhGoods.class);
        Example.Criteria c = example.createCriteria();
        if(isSplit){
            String[] values = exhid.split("\\s*,\\s*");
            c.andIn("id", Arrays.asList(values));
        }else{
            c.andEqualTo("id", exhid);
        }
        /*if(cultid != null && !"".equals(cultid)){
            c.andEqualTo("cultid",cultid);
        }*/
        example.setOrderByClause("statemdfdate desc");
        PageHelper.startPage(page, pageSize);
        list = this.whgExhGoodsMapper.selectByExample(example);
        String _type = "";
        if(list != null && !"".equals(list)){
            for(WhgExhGoods exh : list){
                _type = exh.getEtype();
                WhgYwiType ywiType = this.whgYwiTypeMapper.selectByPrimaryKey(_type);
                if(ywiType != null && ywiType.getName() != null){
                    exh.setEtype(ywiType.getName());
                }

            }

        }
        return new PageInfo(list);
    }

    /**
     * 查询展品详情
     * @param id
     * @return
     */
    public WhgExhGoods selectExhGoodsDetail(String id) {
        return this.whgExhGoodsMapper.selectByPrimaryKey(id);
    }
}
