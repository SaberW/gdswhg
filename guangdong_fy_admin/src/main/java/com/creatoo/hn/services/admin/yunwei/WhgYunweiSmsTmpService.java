package com.creatoo.hn.services.admin.yunwei;

import com.creatoo.hn.dao.mapper.WhgYwiSmstmpMapper;
import com.creatoo.hn.dao.model.WhgYwiSmstmp;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 短信模板管理service
 *
 * @author luzhihuai
 * @version 1-201703
 *          Created by Administrator on 2017/3/28.
 */
@Service
@CacheConfig(cacheNames = "WhgYwiSmstmp")
public class WhgYunweiSmsTmpService extends BaseService{
    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass().getName());


    /**
     * 短信模板DAO
     */
    @Autowired
    private WhgYwiSmstmpMapper whgYwiSmstmpMapper;

    /**
     * 分页查询分类列表信息
     *
     * @param
     */
    @Cacheable
    public PageInfo<WhgYwiSmstmp> t_srchList4p(int page, int rows, WhgYwiSmstmp whgYwiSmstmp) throws Exception {
        //开始分页
        PageHelper.startPage(page, rows);

        //搜索条件
        Example example = new Example(WhgYwiSmstmp.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (whgYwiSmstmp != null && whgYwiSmstmp.getCode() != null) {
            c.andLike("code", "%" + whgYwiSmstmp.getCode() + "%");
            whgYwiSmstmp.setCode(null);
        }

        //其它条件
//        c.andEqualTo(whgYwiSystemp);


        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgYwiSmstmp> typeList = this.whgYwiSmstmpMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 查询单条记录
     * @param id id
     * @return 对象
     * @throws Exception
     */
    @Cacheable
    public WhgYwiSmstmp t_srchOne(String id)throws Exception{
        WhgYwiSmstmp record = new WhgYwiSmstmp();
        record.setId(id);
        return this.whgYwiSmstmpMapper.selectOne(record);
    }

    /**
     * 添加
     *
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgYwiSmstmp wyl) throws Exception {
        WhgYwiSmstmp whgYwiSmstmp = new WhgYwiSmstmp();

        if (wyl.getCode() != null && !"".equals(wyl.getCode())) {
            whgYwiSmstmp.setCode(wyl.getCode());
        }
        if (wyl.getContent() != null && !"".equals(wyl.getContent())) {
            whgYwiSmstmp.setContent(wyl.getContent());
        }

        whgYwiSmstmp.setId(IDUtils.getID());
//        whgYwiSystemp.setCrtdate(new Date());

        int result = this.whgYwiSmstmpMapper.insertSelective(whgYwiSmstmp);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }

    }

    /**
     * 编辑
     *
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_edit(String id,WhgYwiSmstmp wyl) throws Exception {
        WhgYwiSmstmp whgYwiSmstmp = this.whgYwiSmstmpMapper.selectByPrimaryKey(id);

        if (whgYwiSmstmp != null) {
            if (wyl.getCode() != null && !"".equals(wyl.getCode())) {
                whgYwiSmstmp.setCode(wyl.getCode());
            }
            if (wyl.getContent() != null && !"".equals(wyl.getContent())) {
                whgYwiSmstmp.setContent(wyl.getContent());
            }
        }
        int result = this.whgYwiSmstmpMapper.updateByPrimaryKeySelective(whgYwiSmstmp);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }


    /**
     * 删除
     *
     * @param
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception {

        int result = this.whgYwiSmstmpMapper.deleteByPrimaryKey(id);
        if (result != 1) {
            throw new Exception("删除数据失败！");
        }
    }


}
