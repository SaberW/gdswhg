package com.creatoo.hn.services.api.apiinside;

import com.creatoo.hn.dao.mapper.api.ApiInsPersonnelMapper;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.comm.ResetRefidService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgActivity", keyGenerator = "simpleKeyGenerator")
public class InsActivityService extends BaseService {

    @Autowired
    private ApiInsPersonnelMapper apiInsPersonnelMapper;

    @Autowired
    private ResetRefidService resetRefidService;

    /**
     * 人才列表
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectPersonList(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List list = this.apiInsPersonnelMapper.selectPersonList(recode);

        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 人才详情
     * @param id
     * @param protype
     * @return
     * @throws Exception
     */
    public Object findPerson(String id, String protype) throws Exception{
        Map info = this.apiInsPersonnelMapper.findPerson(id, protype);
        this.resetRefidService.resetRefid4type(info, "type");

        return info;
    }


    /**
     * 相关的推荐
     * @param id
     * @param protype
     * @return
     * @throws Exception
     */
    public List selectRecommonends(String id, Integer size, String protype, List cultid) throws Exception {
        if (size != null) {
            PageHelper.startPage(1, size);
        }
        return this.apiInsPersonnelMapper.selectRecommonends(id, protype, cultid);
    }

}
