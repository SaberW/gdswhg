package com.creatoo.hn.services.admin.feiyi.fyimanage;

import com.creatoo.hn.dao.mapper.WhgFyiArchiveMapper;
import com.creatoo.hn.dao.model.WhgFyiArchive;
import com.creatoo.hn.dao.model.WhgFyiExpert;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */
@Service
public class WhgFyiArchiveService extends BaseService{

    @Autowired
    private WhgFyiArchiveMapper whgFyiArchiveMapper;


    /**
     * 根据主键查询公务档案
     * @param id
     * @return
     */
    public Object t_srchOne(String id) {
        return whgFyiArchiveMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询公文档案
     * @param page
     * @param rows
     * @param archive
     * @return
     */
    public PageInfo t_srchlist4p(int page, int rows, WhgFyiArchive archive) throws Exception{
        Example example = new Example(WhgFyiArchive.class);
        Example.Criteria c = example.createCriteria();
        //名称条件
        if(archive != null && archive.getTitle() != null){
            c.andLike("name", "%"+archive.getTitle()+"%");
            archive.setTitle(null);
        }
        if(archive!=null) {
            //其它条件
            c.andEqualTo(archive);
        }
        //排序
        example.setOrderByClause("statemdfdate desc");
        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgFyiArchive> list = this.whgFyiArchiveMapper.selectByExample(example);
        return new PageInfo<WhgFyiArchive>(list);
    }

    /**
     * 添加公文档案
     * @param archive
     * @param sysUser
     */
    public void t_add(WhgFyiArchive archive, WhgSysUser sysUser) throws Exception{
        archive.setId(IDUtils.getID());
        archive.setCrtdate(new Date());
        whgFyiArchiveMapper.insertSelective(archive);
    }

    /**
     * 编辑公文档案
     * @param archive
     * @param sysUser
     */
    public void t_edit(WhgFyiArchive archive, WhgSysUser sysUser) {
        archive.setStatemdfdate(new Date());
        whgFyiArchiveMapper.updateByPrimaryKeySelective(archive);
    }

    /**
     * 删除公文档案
     * @param id
     */
    public void t_del(String id) {
        whgFyiArchiveMapper.deleteByPrimaryKey(id);
    }
}
