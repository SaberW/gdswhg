package com.creatoo.hn.services.admin.system;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgSysJobMapper;
import com.creatoo.hn.dao.mapper.WhgSysJobPmsMapper;
import com.creatoo.hn.dao.model.WhgSysIds;
import com.creatoo.hn.dao.model.WhgSysJob;
import com.creatoo.hn.dao.model.WhgSysJobPms;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumConsoleSystem;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 系统岗位服务类
 * Created by wangxl on 2018/1/17.
 */
@Service
@CacheConfig(cacheNames = "WhgSysJob", keyGenerator = "simpleKeyGenerator")
public class WhgSystemJobService extends BaseService {
    /**
     * 岗位Mapper
     */
    @Autowired
    private WhgSysJobMapper whgSysJobMapper;

    /**
     * 岗位权限组Mapper
     */
    @Autowired
    private WhgSysJobPmsMapper whgSysJobPmsMapper;

    /**
     * 岗位是否已经启用
     * @param jobid
     * @return
     * @throws Exception
     */
    public boolean isEnable(String jobid)throws Exception{
        WhgSysJob job = this.t_srchOne(jobid);
        return (job.getState().intValue() == EnumState.STATE_YES.getValue() && job.getDelstate().intValue() == EnumStateDel.STATE_DEL_NO.getValue());
    }

    /**
     * 根据管理员获得岗位配置的系统类型: 区域管理和administrator只能配置总分馆系统的岗位，站点管理员只能配置本站点的岗位
     * @param whgSysUser
     * @return
     * @throws Exception
     */
    public void getSysflag(WhgSysUser whgSysUser, WhgSysJob whgSysJob)throws Exception{
        if(Constant.SUPER_USER_NAME.equals(whgSysUser.getAccount())
                || EnumConsoleSystem.sysmgr.getValue().equals(whgSysUser.getAdmintype())){//administrator和区域管理员只能查询区域角色
            whgSysJob.setSysflag(EnumConsoleSystem.sysmgr.getValue());
        }else{//站点管理员只能查询站点管理的角色
            whgSysJob.setSysflag(EnumConsoleSystem.bizmgr.getValue());
        }
        if(EnumConsoleSystem.bizmgr.getValue().equals(whgSysJob.getSysflag())){//站点管理员只能查询本站的岗位
            whgSysJob.setCultid(whgSysUser.getCultid());
        }
    }

    /**
     * 构造查询条件模板
     * @param whgSysUser 管理员
     * @param whgSysJob 岗位查询条件
     * @param sort 排序字段
     * @param order 排序方式
     * @return Example
     * @throws Exception
     */
    @Cacheable
    private Example parseExample(WhgSysUser whgSysUser, WhgSysJob whgSysJob, String sort, String order)throws Exception{
        //搜索条件
        Example example = new Example(WhgSysJob.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(StringUtils.isNotEmpty(whgSysJob.getName())){
            c.andLike("name", "%"+whgSysJob.getName()+"%");
            whgSysJob.setName(null);
        }

        //按文化馆查询
        if(StringUtils.isEmpty(whgSysJob.getSysflag())){
            this.getSysflag(whgSysUser, whgSysJob);
        }

        //其它条件
        c.andEqualTo(whgSysJob);

        //排序
        this.setOrder(example, sort, order, "crtdate desc");

        return example;
    }

    /**
     * 分页查询岗位信息
     * @param whgSysUser
     * @param WhgSysJob
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    @Cacheable
    public PageInfo<WhgSysJob> t_srchList4p(WhgSysUser whgSysUser, WhgSysJob WhgSysJob, int page, int rows, String sort, String order)throws Exception{
        //条件
        Example example = this.parseExample(whgSysUser, WhgSysJob, sort, order);

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgSysJob> list = this.whgSysJobMapper.selectByExample(example);
        return new PageInfo<WhgSysJob>(list);
    }

    /**
     * 列表查询岗位
     * @param sort
     * @param order
     * @param whgSysJob
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysJob> t_srchList(WhgSysUser whgSysUser, WhgSysJob whgSysJob, String sort, String order)throws Exception{
        Example example = this.parseExample(whgSysUser, whgSysJob, sort, order);
        return this.whgSysJobMapper.selectByExample(example);
    }

    /**
     * 查询岗位信息
     * @param id 权限组标识
     * @return 权限组信息
     * @throws Exception
     */
    @Cacheable
    public WhgSysJob t_srchOne(String id)throws Exception{
        return this.whgSysJobMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询岗位下的权限组
     * @param jobid
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysJobPms> t_srchJobPms(String jobid)throws Exception{
        WhgSysJobPms jobPms = new WhgSysJobPms();
        jobPms.setJobid(jobid);
        return this.whgSysJobPmsMapper.select(jobPms);
    }

    /**
     * 添加
     * @param whgSysUser
     * @param whgSysJob
     * @param pmsids
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public String t_add(WhgSysUser whgSysUser, WhgSysJob whgSysJob, String[] pmsids)throws Exception{
        //清除登录缓存
        this.clearLoginCache();

        //根据管理员类型，设置能够添加的系统
        this.getSysflag(whgSysUser, whgSysJob);

        //名称不能重复
        Example example = new Example(WhgSysJob.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", whgSysJob.getName());
        c.andEqualTo("sysflag", whgSysJob.getSysflag());
        if(EnumConsoleSystem.bizmgr.getValue().equals(whgSysJob.getSysflag())){
            c.andEqualTo("cultid", whgSysUser.getCultid());
        }
        int count = this.whgSysJobMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称重复");
        }

        //设置初始值
        Date now = new Date();
        String id = IDUtils.getID32();//commService.getKey("whg_sys_role");
        whgSysJob.setId(id);
        whgSysJob.setState(EnumState.STATE_YES.getValue());
        whgSysJob.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        whgSysJob.setCrtdate(now);
        whgSysJob.setCrtuser(whgSysUser.getId());
        whgSysJob.setStatemdfdate(now);
        whgSysJob.setStatemdfuser(whgSysUser.getId());
        int rows = this.whgSysJobMapper.insertSelective(whgSysJob);
        if(rows != 1){
            throw new Exception("添加失败");
        }

        //添加岗位权限
        if(pmsids != null){
            List<WhgSysJobPms> srpList = new ArrayList<>();
            for(String p : pmsids){
                WhgSysJobPms rpms = new WhgSysJobPms();
                rpms.setId(IDUtils.getID32());
                rpms.setJobid(id);
                rpms.setPmsid(p);
                srpList.add(rpms);
                this.whgSysJobPmsMapper.insert(rpms);
            }
        }
        return id;
    }

    /**
     * 编辑岗位
     * @param whgSysUser
     * @param whgSysJob
     * @param pmsids
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgSysUser whgSysUser, WhgSysJob whgSysJob, String[] pmsids)throws Exception{
        //清除登录缓存
        this.clearLoginCache();

        //根据管理员类型，设置能够添加的系统
        this.getSysflag(whgSysUser, whgSysJob);

        //名称不能重复
        Example example = new Example(WhgSysJob.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", whgSysJob.getName());
        c.andEqualTo("sysflag", whgSysJob.getSysflag());
        if(EnumConsoleSystem.bizmgr.getValue().equals(whgSysJob.getSysflag())){
            c.andEqualTo("cultid", whgSysUser.getCultid());
        }
        c.andNotEqualTo("id", whgSysJob.getId());
        int count = this.whgSysJobMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称重复");
        }

        //设置初始值
        Date now = new Date();
        whgSysJob.setStatemdfdate(now);
        whgSysJob.setStatemdfuser(whgSysUser.getId());
        int rows = this.whgSysJobMapper.updateByPrimaryKeySelective(whgSysJob);
        if(rows != 1){
            throw new Exception("编辑失败");
        }

        //添加岗位权限
        Example del_example = new Example(WhgSysJobPms.class);
        del_example.createCriteria().andEqualTo("jobid", whgSysJob.getId());
        this.whgSysJobPmsMapper.deleteByExample(del_example);
        if(pmsids != null){
            List<WhgSysJobPms> srpList = new ArrayList<>();
            for(String p : pmsids){
                WhgSysJobPms rpms = new WhgSysJobPms();
                rpms.setId(IDUtils.getID32());
                rpms.setJobid(whgSysJob.getId());
                rpms.setPmsid(p);
                srpList.add(rpms);
                this.whgSysJobPmsMapper.insert(rpms);
            }
        }
    }

    /**
     * 删除
     * @param whgSysUser
     * @param id
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(WhgSysUser whgSysUser, String id)throws Exception{
        //清除缓存
        this.clearLoginCache();

        //删除岗位权限
        Example del_example = new Example(WhgSysJobPms.class);
        del_example.createCriteria().andEqualTo("jobid", id);
        this.whgSysJobPmsMapper.deleteByExample(del_example);

        //删除岗位
        this.whgSysJobMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改状态
     * @param ids
     * @param fromState
     * @param toState
     * @param user
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updstate(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        //清除缓存
        this.clearLoginCache();

        if(StringUtils.isNotEmpty(ids) && StringUtils.isNotEmpty(toState)){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgSysJob.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("state", fromState);
            }
            WhgSysJob record = new WhgSysJob();
            record.setState(Integer.parseInt(toState));
            record.setStatemdfdate(new Date());
            record.setStatemdfuser(user.getId());
            this.whgSysJobMapper.updateByExampleSelective(record, example);
        }
    }
}
