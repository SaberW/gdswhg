package com.creatoo.hn.services.api.apioutside.train;

import com.creatoo.hn.dao.mapper.WhgDrscMapper;
import com.creatoo.hn.dao.model.WhgDrsc;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */
@Service
public class ApiTraDrscService {

    @Autowired
    private WhgDrscMapper whgDrscMapper;

    /**
     * 在线点播列表查询
     * @param page
     * @param pageSize
     * @param cultid
     * @param drscarttyp
     * @param title
     * @param sort
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public PageInfo findList(int page,int pageSize,String cultid,String drscarttyp,String title,String sort)throws Exception {
        Example example = new Example(WhgDrsc.class);
        Example.Criteria c = example.createCriteria();
        if(cultid != null && !"".equals(cultid)){
            c.andIn("drscvenueid", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));//多个cultid
        }
        if(drscarttyp != null && !"".equals(drscarttyp)){
            c.andEqualTo("drscarttyp",drscarttyp);
        }
        if(title != null && !"".equals(title)){
            c.andLike("drsctitle",title);
        }
        if(sort != null && !"".equals(sort)){
            if(sort.equals("1")){
                example.setOrderByClause("drscopttime desc");
            }
        }else {
            example.setOrderByClause("drscopttime desc");
        }
        c.andEqualTo("drscstate",3);
        PageHelper.startPage(page, pageSize);
        List<WhgDrsc> drscList = this.whgDrscMapper.selectByExample(example);
        return new PageInfo(drscList);
    }

    /**
     * 根据ID查询在线点播详情
     * @param id
     * @return
     */
    public WhgDrsc selDescById(String id) throws Exception {
        return this.whgDrscMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取推荐在线点播
     * @param id
     * @param cultid
     * @param size
     * @return
     */
    public PageInfo getRecommendDesc(String id, String cultid, Integer size) {
        Example example = new Example(WhgDrsc.class);
        Example.Criteria c = example.createCriteria();
        c.andNotEqualTo("drscid",id);
        c.andEqualTo("drscstate",3);
        if(cultid != null && !"".equals(cultid)){
            c.andIn("drscvenueid", Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));//多个cultid
        }
        example.setOrderByClause("drscopttime desc");
        if(size != null){
            PageHelper.startPage(1,size);
        }else {
            PageHelper.startPage(1,3);
        }
        List<WhgDrsc> list = this.whgDrscMapper.selectByExample(example);
        return new PageInfo(list);
    }
}
