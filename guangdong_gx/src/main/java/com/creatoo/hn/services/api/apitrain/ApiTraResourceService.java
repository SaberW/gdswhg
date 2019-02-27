package com.creatoo.hn.services.api.apitrain;

import com.creatoo.hn.dao.mapper.WhgDrscMapper;
import com.creatoo.hn.dao.mapper.api.ApiDrscMapper;
import com.creatoo.hn.dao.model.WhgDrsc;
import com.creatoo.hn.services.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/10.
 */
@Service
public class ApiTraResourceService extends BaseService{

    @Autowired
    private ApiDrscMapper apiDrscMapper;

    @Autowired
    private WhgDrscMapper whgDrscMapper;

    /**
     * 查询培训资源列表
     * @param page
     * @param pageSize
     * @param recode
     * @return
     */
    public PageInfo findList(int page, int pageSize, Map recode) {
        PageHelper.startPage(page, pageSize);
        List reslist = this.apiDrscMapper.selectDrscList4page(recode);
        return new PageInfo(reslist);
    }

    /**
     * 根据ID查询培训资源详情
     * @param id
     * @return
     */
    public WhgDrsc selDescById(String id) throws Exception {
        return this.whgDrscMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取推荐培训资源
     * @param id
     * @param cultid
     * @param size
     * @return
     */
    public PageInfo getRecommendDesc(String id, String cultid, Integer size) {
        Example example = new Example(WhgDrsc.class);
        Example.Criteria c = example.createCriteria();
        c.andNotEqualTo("drscid",id);
        c.andEqualTo("drscstate",6);
        if(cultid != null && !"".equals(cultid)){
            c.andIn("drscvenueid", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));
        }
        example.setOrderByClause("isrecommend desc,drscopttime desc");
        if(size != null){
            PageHelper.startPage(1,size);
        }else {
            PageHelper.startPage(1,3);
        }
        List<WhgDrsc> list = this.whgDrscMapper.selectByExample(example);
        return new PageInfo(list);
    }
}
