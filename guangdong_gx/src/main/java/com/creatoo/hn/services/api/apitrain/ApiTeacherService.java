package com.creatoo.hn.services.api.apitrain;

import com.creatoo.hn.dao.mapper.WhgTraMapper;
import com.creatoo.hn.dao.mapper.WhgTraTeacherMapper;
import com.creatoo.hn.dao.mapper.WhgYwiTypeMapper;
import com.creatoo.hn.dao.mapper.api.ApiTeacherMapper;
import com.creatoo.hn.dao.model.WhgTra;
import com.creatoo.hn.dao.model.WhgTraTeacher;
import com.creatoo.hn.dao.model.WhgYwiType;
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
public class ApiTeacherService extends BaseService{

    @Autowired
    private WhgTraTeacherMapper whgTraTeacherMapper;

    @Autowired
    private ApiTeacherMapper apiTeacherMapper;

    @Autowired
    private WhgTraMapper whgTraMapper;

    @Autowired
    private WhgYwiTypeMapper whgYwiTypeMapper;
    /**
     * 查询培训师资列表页
     * @param page
     * @param pageSize
     * @param recode
     * @return
     */
    public PageInfo getTeaList(int page, int pageSize, Map recode) {
        PageHelper.startPage(page, pageSize);
        List<Map> reslist = this.apiTeacherMapper.selectTeaList4page(recode);
        if(reslist != null && reslist.size() > 0){
           for(int i = 0; i<reslist.size(); i++){
               String teatype = reslist.get(i).get("teachertype").toString();
               Example example = new Example(WhgYwiType.class);
               Example.Criteria c = example.createCriteria();
               c.andIn("id", Arrays.asList( teatype.split("\\s*,\\s*") ));
               List<WhgYwiType> typelist = this.whgYwiTypeMapper.selectByExample(example);
               if(typelist != null && typelist.size() > 0){
                   String _split = " ";
                   String typname = " ";
                   for(int j = 0;j<typelist.size();j++){
                       typname += _split+typelist.get(j).getName();
                       _split = ",";
                   }
                   reslist.get(i).put("typname",typname);
               }
           }
        }
        return new PageInfo(reslist);
    }

    /**
     * 根据ID查询培训老师详情
     * @param id
     * @return
     */
    public WhgTraTeacher selTeaById(String id) throws Exception{
        WhgTraTeacher teacher = this.whgTraTeacherMapper.selectByPrimaryKey(id);
        String teatype = teacher.getTeachertype();
        Example example = new Example(WhgYwiType.class);
        Example.Criteria c = example.createCriteria();
        c.andIn("id", Arrays.asList( teatype.split("\\s*,\\s*") ));
        List<WhgYwiType> typelist = this.whgYwiTypeMapper.selectByExample(example);
        if(typelist != null && typelist.size() > 0){
            String _split = " ";
            String typname = " ";
            for(int j = 0;j<typelist.size();j++){
                typname += _split+typelist.get(j).getName();
                _split = ",";
            }
            teacher.setTeachertype(typname);
        }
        return teacher;
    }

    /**
     * 查询老师相关培训
     * @param name
     * @param cultid
     * @param size
     * @return
     */
    public PageInfo getTraByTea(String name, String cultid, Integer size) {
        Example example = new Example(WhgTra.class);
        Example.Criteria c = example.createCriteria();
        c.andLike("teachername","%"+name+"%").andEqualTo("state",6).andEqualTo("biz","PT");
        if(cultid != null && !"".equals(cultid)){
            c.andEqualTo("cultid",cultid);
        }
        example.setOrderByClause("statemdfdate desc");
        if(size != null){
            PageHelper.startPage(1,size);
        }else {
            PageHelper.startPage(1,3);
        }
        List<WhgTra> list = this.whgTraMapper.selectByExample(example);
        return new PageInfo(list);
    }
}
