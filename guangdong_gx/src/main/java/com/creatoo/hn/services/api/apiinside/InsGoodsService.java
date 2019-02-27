package com.creatoo.hn.services.api.apiinside;

import com.creatoo.hn.dao.mapper.api.ApiInsGoodsMapper;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.comm.ResetRefidService;
import com.creatoo.hn.util.FilterFontUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgGoods", keyGenerator = "simpleKeyGenerator")
public class InsGoodsService extends BaseService {

    @Autowired
    private ApiInsGoodsMapper apiInsGoodsMapper;

    @Autowired
    private ResetRefidService resetRefidService;

    /**
     * 查询商品列表
     * @param page
     * @param pageSize
     * @param recode
     * @return
     */
    @Cacheable
    public PageInfo selectGoods(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List list = this.apiInsGoodsMapper.selectGoodsList(recode);

        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }


    /**
     * 查询商品
     * @param id
     * @param protype
     * @return
     * @throws Exception
     */
    @Cacheable
    public Map findGoods(String id, String protype) throws Exception{
        Map info = this.apiInsGoodsMapper.findGoods(id, protype);
        this.resetRefidService.resetRefid4type(info, "etype");
        this.resetRefidService.resetRefid4tag(info, "etag");
        return FilterFontUtil.clearFont(info, new String[]{"description"});
    }

    /**
     * 相关类型的商品
     * @param id
     * @param size
     * @param protype
     * @param cultid
     * @return
     * @throws Exception
     */
    public List selectGoodsReflist(String id, Integer size, String protype, List cultid, List deptid) throws Exception {
        if (size != null) {
            PageHelper.startPage(1,size);
        }
        return this.apiInsGoodsMapper.selectGoodsReflist(id, protype, cultid, deptid);
    }
}
