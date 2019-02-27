package com.creatoo.hn.services.api.apiinside;

import com.creatoo.hn.dao.mapper.WhgCollectionMapper;
import com.creatoo.hn.dao.mapper.WhgFkProjectMapper;
import com.creatoo.hn.dao.mapper.WhgSupplyTimeMapper;
import com.creatoo.hn.dao.mapper.api.ApiInsCollectionMapper;
import com.creatoo.hn.dao.model.WhgCollection;
import com.creatoo.hn.dao.model.WhgFkProject;
import com.creatoo.hn.dao.model.WhgSupplyTime;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Service
public class InsCommonService extends BaseService {

    @Autowired
    private ApiInsCollectionMapper apiInsCollectionMapper;

    @Autowired
    private WhgCollectionMapper whgCollectionMapper;


    @Autowired
    private WhgFkProjectMapper whgFkProjectMapper;

    @Autowired
    private WhgSupplyTimeMapper whgSupplyTimeMapper;

    /**
     * 内部供需收藏查询
     * @param page
     * @param pageSize
     * @param userId
     * @return
     * @throws Exception
     */
    public PageInfo getCollections(int page, int pageSize, String userId,String systype) throws Exception{
        PageHelper.startPage(page, pageSize);
        List list = this.apiInsCollectionMapper.selectCollections(userId,systype);
        return new PageInfo(list);
    }

    /**
     * 添加收藏
     * @param collection
     */
    public Object addMyColle(WhgCollection collection) throws Exception {
        return this.whgCollectionMapper.insert(collection);
    }

    /**
     *获取收藏数
     * @param cmreftyp
     * @param cmrefid
     * @return
     */
    public int shouCanShu(String reftyp, String refid) {
        Example example = new Example(WhgCollection.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("cmopttyp", "0"); //0-收藏,1-浏览,2-推荐,3-置顶
        if(reftyp != null && !"".equals(reftyp)){
            c.andEqualTo("cmreftyp",reftyp);
        }
        if(refid != null && !"".equals(refid)){
            c.andEqualTo("cmrefid", refid);
        }
        return this.whgCollectionMapper.selectCountByExample(example);
    }

    /**
     *判断是否收藏
     * @param uid
     * @param cmreftyp
     * @param cmrefid
     * @return
     */
    public boolean isColle(String uid, String reftyp, String refid) {
        boolean is = false;

        Example example = new Example(WhgCollection.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("cmopttyp", "0");//0-收藏,1-浏览,2-推荐,3-置顶
        c.andEqualTo("cmuid", uid);//用户id

        if(reftyp != null && !"".equals(reftyp)){
            c.andEqualTo("cmreftyp", reftyp);
        }

        if(refid != null &&  !"".equals(refid)){
            c.andEqualTo("cmrefid", refid);
        }
        is = this.whgCollectionMapper.selectCountByExample(example) > 0;
        return is;
    }

    /**
     * 删除我的收藏
     * @param cmuid
     * @return
     */
    public Object removeCollection(String cmuid,String cmrefid)throws Exception{
        Example example = new Example(WhgCollection.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("cmuid",cmuid).andEqualTo("cmrefid",cmrefid);
        return this.whgCollectionMapper.deleteByExample(example);
    }


    /**
     * 获取资源类型的 fk 配送范围
     * @param id
     * @return
     * @throws Exception
     */
    public Map getCitys4Resource(String id) throws Exception{
        /*WhgFkProject recode = new WhgFkProject();
        recode.setFkid(id);
        recode.setProtype(EnumProject.PROJECT_NBGX.getValue());
        List<WhgFkProject> list = this.whgFkProjectMapper.select(recode);
        Map rest = new HashMap();
        if (list == null || list.size()==0) {
            return rest;
        }
        recode = list.get(0);
        rest.put("province", recode.getPsprovince());
        rest.put("city", recode.getPscity());

        return rest;*/

        WhgSupplyTime info = this.whgSupplyTimeMapper.selectByPrimaryKey(id);
        Map rest = new HashMap();
        if (info!=null){
            rest.put("province", info.getPsprovince());
            rest.put("city", info.getPscity());
        }
        return rest;
    }
}
