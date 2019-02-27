package com.creatoo.hn.services.admin.train;

import com.creatoo.hn.dao.mapper.WhgMajorContactMapper;
import com.creatoo.hn.dao.model.WhgMajorContact;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/25.
 */
@Service
public class WhgTraInMajorService extends BaseService{

    @Autowired
    private WhgMajorContactMapper whgMajorContactMapper;

    /**
     * 分页查询列表
     * @param page
     * @param rows
     * @return
     */
    public PageInfo t_srchlist4p(int page, int rows,String mid) {
        PageHelper.startPage(page,rows);
        List<Map> list = this.whgMajorContactMapper.selTraInMajor(mid);
        return new PageInfo(list);
    }

    /**
     * 删除
     * @param id
     */
    public void t_del(String id) {
        this.whgMajorContactMapper.deleteByPrimaryKey(id);
    }

    /**
     * 添加关联课程
     * @param entid
     */
    public void t_add(String entid,String mid) {
        WhgMajorContact contact = new WhgMajorContact();
        Example example = new Example(WhgMajorContact.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("entid",entid).andEqualTo("type",1).andEqualTo("majorid",mid);
        List<WhgMajorContact> list = this.whgMajorContactMapper.selectByExample(example);
        if(list == null || list.size() == 0){
            contact.setId(IDUtils.getID());
            contact.setEntid(entid);
            contact.setMajorid(mid);
            contact.setType(1);
            this.whgMajorContactMapper.insertSelective(contact);
        }
    }

    /**
     * 根据培训ID查询相关微专业信息
     * @param id
     * @return
     */
    public Object t_selMajorByTraid(String id,int type) {
        List<Map> list = whgMajorContactMapper.t_selMajorByTraid(id,type);
        String _split = "";
        String majorname = "";
        if(list != null && list.size() > 0){
            for (int i = 0;i < list.size(); i++){
                majorname += _split+list.get(i).get("id");
                _split = ",";
            }
        }
        return majorname;
    }
}
