package com.creatoo.hn.services.api.apitrain;

import com.creatoo.hn.dao.mapper.WhgTraMajorMapper;
import com.creatoo.hn.dao.mapper.api.ApiResourceMapper;
import com.creatoo.hn.dao.mapper.api.ApiTraMajorMapper;
import com.creatoo.hn.dao.model.WhgTraMajor;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 培训微专业接口service
 * Created by Administrator on 2017/10/11.
 */
@Service
public class ApiTraMajorService {

    @Autowired
    private WhgTraMajorMapper whgTraMajorMapper;

    @Autowired
    private ApiTraMajorMapper apiMajorMapper;

    @Autowired
    private ApiResourceMapper apiResourceMapper;

    /**
     * 查询微专业列表
     * @param page
     * @param pageSize
     * @param recode
     * @return
     */
    public PageInfo getTraMajorList(int page, int pageSize, Map recode) throws Exception{
        PageHelper.startPage(page, pageSize);
        List<Map> reslist = this.apiMajorMapper.selectTraMajorList4page(recode);
        if(reslist != null && reslist.size() > 0){
           for(int i = 0;i<reslist.size();i++){
                String mid = reslist.get(i).get("id").toString();
                PageInfo info = this.getTra4mid(mid,1,2);
                reslist.get(i).put("tralist",info.getList());
           }
        }
        return new PageInfo(reslist);
    }

    /**
     * 查询微专业的培训
     * @param id
     * @return
     */
    public PageInfo getTra4mid(String id,int page, int pageSize) throws Exception {

        PageHelper.startPage(page, pageSize);

        List traList = this.apiMajorMapper.selTra4mid(id);
        return new PageInfo(traList);
    }

    /**
     * 根据id查询微专业
     * @param id
     * @return
     */
    public WhgTraMajor getMajorById(String id) {
        return this.whgTraMajorMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据微专业id查询关联老师
     * @param id
     * @param page
     * @return
     */
    public PageInfo getTea4mid(String id, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List teaList = this.apiMajorMapper.selTea4mid(id);
        return new PageInfo(teaList);
    }

    /**
     * 根据微专业id查询关联资源
     * @param id
     * @param page
     * @return
     */
    public PageInfo getDrsc4mid(String id, int page, int pageSize,String etype) {
        PageHelper.startPage(page, pageSize);
        List<Map> teaList = apiResourceMapper.t_selMajorSource(id,etype, null);
        return new PageInfo(teaList);
    }
}
