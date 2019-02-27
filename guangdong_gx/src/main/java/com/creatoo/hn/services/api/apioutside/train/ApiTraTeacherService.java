package com.creatoo.hn.services.api.apioutside.train;

import com.creatoo.hn.dao.mapper.WhgTraTeacherMapper;
import com.creatoo.hn.dao.model.WhgTraTeacher;
import com.creatoo.hn.services.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */
@Service
public class ApiTraTeacherService extends BaseService{

    @Autowired
    private WhgTraTeacherMapper whgTraTeacherMapper;

    /**
     * 查询培训师资列表
     * @param page
     * @param pageSize
     * @param cultid
     * @param arttype
     * @param title
     * @return
     */
    public PageInfo getTeaList(int page, int pageSize, String cultid, String arttype,String title) throws Exception{
        Example example = new Example(WhgTraTeacher.class);
        Example.Criteria c = example.createCriteria();
        if(cultid != null && !"".equals(cultid)){
            c.andIn("cultid",Arrays.asList(cultid.replaceAll(","," ").trim().split(" ")));//多个cultid
        }
        if(arttype != null && !"".equals(arttype)){
            c.andEqualTo("arttype",arttype);
        }
        if(title != null && !"".equals(title)){
            c.andLike("name",title);
        }
        c.andEqualTo("state",3);
        example.setOrderByClause("statemdfdate desc");
        PageHelper.startPage(page,pageSize);
        List<WhgTraTeacher> traList = this.whgTraTeacherMapper.selectByExample(example);
        return new PageInfo(traList);
    }

    /**
     * 根据ID查询培训老师详情
     * @param id
     * @return
     */
    public WhgTraTeacher selTeaById(String id) throws Exception{
        return this.whgTraTeacherMapper.selectByPrimaryKey(id);
    }


}
