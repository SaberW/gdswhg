package com.creatoo.hn.services.admin.yunwei;

import com.creatoo.hn.dao.mapper.WhgYwiHardwareMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiHardware;
import com.creatoo.hn.dao.model.WhgYwiTag;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/14.
 */
@Service
@CacheConfig(cacheNames = "whg_ywi_hardware", keyGenerator = "simpleKeyGenerator")
public class WhgYunweiYjpzservice {

    /**
     * 关键字mapper
     */
    @Autowired
    private WhgYwiHardwareMapper whgYwiHardwareMapper;


    /**
     * 查询列表
     * @param type
     * @param cultid
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgYwiHardware> findAllYjpz(String type, String cultid)throws Exception{
        Example example = new Example(WhgYwiHardware.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        if (cultid != null && !"".equals(cultid)) {
            c.andEqualTo("cultid",cultid);
        }
        if (type != null && !"".equals(type)) {
            c.andEqualTo("type",type);
        }
        example.setOrderByClause("statemdfdate");
        return this.whgYwiHardwareMapper.selectByExample(example);
    }


    /**
     * 分页查询硬件配置列表信息
     * @param
     */
    @Cacheable
    public PageInfo<WhgYwiHardware> t_srchList4p(int page,int rows, WhgYwiHardware yjpz) throws Exception {
        //开始分页
        PageHelper.startPage(page, rows);
        Example example = new Example(WhgYwiHardware.class);
        Example.Criteria c = example.createCriteria();

        if (yjpz.getType() != null && !"".equals(yjpz.getType())) {
            c.andEqualTo("type", yjpz.getType());
        }
        if (yjpz.getEntid() != null && !"".equals(yjpz.getEntid())) {
            c.andEqualTo("entid", yjpz.getEntid());
        }
        if(yjpz.getName() != null && !"".equals(yjpz.getName())){
            c.andLike("name","%"+yjpz.getName()+"%");
        }
        c.andEqualTo("state",1);
        example.setOrderByClause("statemdfdate asc");
        List<WhgYwiHardware> typeList = this.whgYwiHardwareMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 添加硬件配置
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgYwiHardware yjpz, WhgSysUser sysUser)throws Exception {
        yjpz.setId(IDUtils.getID());
        yjpz.setState(1);
        yjpz.setStatemdfdate(new Date());
        yjpz.setStatemdfuser(sysUser.getId());
        int result = this.whgYwiHardwareMapper.insertSelective(yjpz);
        if(result != 1){
            throw new Exception("添加数据失败！");
        }

    }


    /**
     * 编辑硬件配置
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgYwiHardware yjpz, WhgSysUser sysUser)throws Exception {
        yjpz.setStatemdfdate(new Date());
        yjpz.setStatemdfuser(sysUser.getId());
        int result = this.whgYwiHardwareMapper.updateByPrimaryKeySelective(yjpz);
        if(result != 1){
            throw new Exception("编辑数据失败！");
        }
    }

    /**
     * 删除硬件配置信息
     * @param
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception {
        int result = this.whgYwiHardwareMapper.deleteByPrimaryKey(id);
        if(result != 1){
            throw new Exception("删除数据失败！");
        }
    }


    /**
     * 根据实体类型和实体ID查询硬件配置
     * @param type
     * @param entid
     * @return
     */
    public ApiResultBean t_getYjpz(int page,int pageSize,Integer type, String entid) throws Exception {
        ApiResultBean arb = new ApiResultBean();
        Example example = new Example(WhgYwiHardware.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("type",type).andEqualTo("entid",entid).andEqualTo("state",1);
        PageHelper.startPage(page, pageSize);
        List<WhgYwiHardware> list  = this.whgYwiHardwareMapper.selectByExample(example);
        PageInfo info = new PageInfo(list);
        if(list != null && list.size() > 0){
            arb.setRows(info.getList());
            arb.setPageInfo(info);
        }

        return arb;
    }


    /**
     * YI@ 通过展览展示id查询对应所有的硬件配置
     */
        public List<WhgYwiHardware> findHardwareByExhId(String id){
            Example example = new Example(WhgYwiHardware.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("entid",id);
            List<WhgYwiHardware> whgYwiHardwares = whgYwiHardwareMapper.selectByExample(example);
            return  whgYwiHardwares;
        }

    /**
     * YI@ 通过展览展示id删除对应所有的硬件配置
     */
        public  void delHardwareByExhId(String id){
            Example example = new Example(WhgYwiHardware.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("entid",id);
           whgYwiHardwareMapper.deleteByExample(example);
        }
}
