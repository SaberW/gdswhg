package com.creatoo.hn.services.api.apiinside;

import com.creatoo.hn.dao.mapper.WhgPersonnelMapper;
import com.creatoo.hn.dao.mapper.WhgShowGoodsMapper;
import com.creatoo.hn.dao.mapper.WhgShowOrganMapper;
import com.creatoo.hn.dao.model.WhgPersonnel;
import com.creatoo.hn.dao.model.WhgShowOrgan;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.comm.ResetRefidService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/5.
 */
@SuppressWarnings("ALL")
@Service
public class InsShowGoodsService extends BaseService{

    @Autowired
    private WhgShowGoodsMapper whgShowGoodsMapper;

    @Autowired
    private WhgShowOrganMapper whgShowOrganMapper;

    @Autowired
    private ResetRefidService resetRefidService;

    @Autowired
    private WhgPersonnelMapper whgPersonnelMapper;

    /**
     * 查询展演类商品列表
     * @param page
     * @param pageSize
     * @param cultid
     * @param type
     * @param title
     * @return
     */
    public PageInfo getShowGoodsList(int page, int pageSize, List cultid,List deptid, String type, String title,String protype,String province,String city,String area,String psprovince,String pscity) {
        PageHelper.startPage(page,pageSize);
        List showGoodsList = this.whgShowGoodsMapper.selInsShowGoodsList(cultid,deptid,type,title,protype,province,city,area,psprovince,pscity);
        return new PageInfo(showGoodsList);
    }

    /**
     * 展演类商品详情
     * @param id
     * @return
     */
    public Map getdetail(String id,String protype) throws Exception{
        Map _list = this.whgShowGoodsMapper.selectShowGoodsById(id,protype);
        if(_list != null){
            if (_list.containsKey("organ") && _list.get("organ")!=null){
                String organ = _list.get("organ").toString();
                List ids = Arrays.asList( organ.split("\\s*,\\s*") );

                if (ids !=null && ids.size()>0){
                    Example example = new Example(WhgShowOrgan.class);
                    Example.Criteria c = example.createCriteria();
                    c.andIn("id", ids );
                    List<WhgShowOrgan> organList = whgShowOrganMapper.selectByExample(example);
                    if(organList != null && organList.size() > 0){
                        String _split = " ";
                        String organtitle = " ";
                        for(int j = 0;j<organList.size();j++){
                            organtitle += _split+organList.get(j).getTitle();
                            _split = ",";
                        }
                        _list.put("organtitle",organtitle);
                    }
                }
            }

            if (_list.containsKey("showperson") && _list.get("showperson") != null) {
                String showperson = _list.get("showperson").toString();
                List ids = Arrays.asList( showperson.split("\\s*\\D+\\s*") );

                if (ids !=null && ids.size()>0){
                    Example example = new Example(WhgPersonnel.class);
                    example.createCriteria().andIn("id", ids);
                    List<WhgPersonnel> list = this.whgPersonnelMapper.selectByExample(example);
                    if (list!=null && list.size()>0){
                        for (WhgPersonnel ent : list){
                            showperson = showperson.replaceAll(ent.getId(), ent.getName());
                        }
                        _list.put("showperson", showperson);
                    }
                }
            }

            //服务类型，演出方式
            this.resetRefidService.resetRefid4type(_list, "fwtype", "showway");
        }
        //return this.whgShowGoodsMapper.selectShowGoodsById(id,protype);
        return _list;
    }

    /**
     * 查询相似表演
     * @param id
     * @param cultid
     * @param size
     * @param protype
     * @return
     */
    public List selectRecommendShowList(String id, List cultid, Integer size, String protype) {
        if (size!=null){
            PageHelper.startPage(1, size);
        }
        return this.whgShowGoodsMapper.selectShowRecommend(protype, cultid, id);
    }
}
