package com.creatoo.hn.services.admin.system;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.dao.mapper.WhgSysPmsDetailMapper;
import com.creatoo.hn.dao.mapper.WhgSysPmsMapper;
import com.creatoo.hn.dao.mapper.WhgSysPmsScopeAreaMapper;
import com.creatoo.hn.dao.mapper.WhgSysPmsScopeMapper;
import com.creatoo.hn.dao.mapper.admin.WhgSysPmsDetailCustomMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumConsoleSystem;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
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
 * 权限组服务类
 * Created by wangxl on 2018/1/15.
 */
@Service
@CacheConfig(cacheNames = "WhgSysPms", keyGenerator = "simpleKeyGenerator")
public class WhgSystemPmsService extends BaseService {
    /**
     * 权限组Mapper
     */
    @Autowired
    private WhgSysPmsMapper whgSysPmsMapper;

    /**
     * 权限组范围Mapper
     */
    @Autowired
    private WhgSysPmsScopeMapper whgSysPmsScopeMapper;

    /**
     * 权限组区域范围Mapper
     */
    @Autowired
    private WhgSysPmsScopeAreaMapper whgSysPmsScopeAreaMapper;

    /**
     * 权限组明细Mapper
     */
    @Autowired
    private WhgSysPmsDetailMapper whgSysPmsDetailMapper;

    /**
     * 扩展权限组明细Mapper
     */
    @Autowired
    private WhgSysPmsDetailCustomMapper whgSysPmsDetailCustomMapper;

    /**
     * 查询适应区域的所有权限组
     * @param areaLevel
     * @param areaValue
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysPmsScopeArea> t_srchAreaPms(Integer areaLevel, String areaValue)throws Exception{
        WhgSysPmsScopeArea whgSysPmsScopeArea = new WhgSysPmsScopeArea();
        whgSysPmsScopeArea.setArealevel(areaLevel);
        whgSysPmsScopeArea.setAreaval(areaValue);
        return this.whgSysPmsScopeAreaMapper.select(whgSysPmsScopeArea);
    }

    /**
     * 是否已经配置权限组适应到区域
     * @param pmsid 权限组标识
     * @param areaLevel 区域级别
     * @param areaVal 区域名称
     * @throws Exception
     */
    @Cacheable
    public boolean t_countScopeArea(String pmsid, Integer areaLevel, String areaVal)throws Exception{
        WhgSysPmsScopeArea whgSysPmsScopeArea = new WhgSysPmsScopeArea();
        whgSysPmsScopeArea.setPmsid(pmsid);
        whgSysPmsScopeArea.setArealevel(areaLevel);
        whgSysPmsScopeArea.setAreaval(areaVal);
        return this.whgSysPmsScopeAreaMapper.selectCount(whgSysPmsScopeArea) > 0;
    }

    /**
     * 配置权限组适应到区域
     * @param pmsid
     * @param areaLevel
     * @param areaVal
     * @throws Exception
     */
    @CacheEvict(allEntries=true)
    public void t_addScopeArea(String pmsid, Integer areaLevel, String areaVal)throws Exception{
        WhgSysPmsScopeArea whgSysPmsScopeArea = new WhgSysPmsScopeArea();
        whgSysPmsScopeArea.setId(IDUtils.getID32());
        whgSysPmsScopeArea.setPmsid(pmsid);
        whgSysPmsScopeArea.setArealevel(areaLevel);
        whgSysPmsScopeArea.setAreaval(areaVal);
        this.whgSysPmsScopeAreaMapper.insert(whgSysPmsScopeArea);
    }

    /**
     * 撤销配置权限组适应到区域
     * @param pmsid
     * @param areaLevel
     * @param areaVal
     * @throws Exception
     */
    @CacheEvict(allEntries=true)
    public void t_delScopeArea(String pmsid, Integer areaLevel, String areaVal)throws Exception{
        WhgSysPmsScopeArea whgSysPmsScopeArea = new WhgSysPmsScopeArea();
        whgSysPmsScopeArea.setPmsid(pmsid);
        whgSysPmsScopeArea.setArealevel(areaLevel);
        whgSysPmsScopeArea.setAreaval(areaVal);
        this.whgSysPmsScopeAreaMapper.delete(whgSysPmsScopeArea);
    }

    /**
     * 构造查询条件模板
     * @param whgSysPms
     * @param sort
     * @param order
     * @param cultid
     * @return Example
     * @throws Exception
     */
    @Cacheable
    private Example parseExample(WhgSysPms whgSysPms, String sort, String order, String cultid)throws Exception{
        //搜索条件
        Example example = new Example(WhgSysPms.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(StringUtils.isNotEmpty(whgSysPms.getName())){
            c.andLike("name", "%"+whgSysPms.getName()+"%");
            whgSysPms.setName(null);
        }

        //按文化馆查询
        List<WhgSysPmsScope> pmsScopes = null;
        String columnName = "pmsid";
        if(StringUtils.isNotEmpty(cultid)){
            WhgSysPmsScope pmsScope = new WhgSysPmsScope();
            pmsScope.setCultid(cultid);
            Example example1 = new Example(WhgSysPmsScope.class);
            example1.createCriteria().andEqualTo(pmsScope);
            example1.selectProperties(columnName);
            pmsScopes = this.whgSysPmsScopeMapper.selectByExample(example1);

            //按区域或者文化馆时根据相关条件查询
            List<String> pmsids = new ArrayList<>();
            if(pmsScopes != null){
                for(WhgSysPmsScope wsps : pmsScopes){
                    if(StringUtils.isNotEmpty(wsps.getPmsid())){
                        pmsids.add(wsps.getPmsid());
                    }
                }
            }
            if(pmsids.size() > 0){
                c.andIn("id", pmsids);
            }else{
                c.andEqualTo("state", 9);//没有文化馆的数据时，返回空
            }
        }


        //其它条件
        c.andEqualTo(whgSysPms);

        //排序
        this.setOrder(example, sort, order, "crtdate desc");

        return example;
    }

    /**
     * 分页查询权限组
     * @param page 第几页
     * @param rows 每页数
     * @param sort 排字字段
     * @param order 排序方式
     * @param whgSysPms 查询条件
     * @return 分页信息
     * @throws Exception
     */
    @Cacheable
    public PageInfo<WhgSysPms> t_srchList4p(WhgSysPms whgSysPms, int page, int rows, String sort, String order, String cultid)throws Exception{
        //条件
        Example example = this.parseExample(whgSysPms, sort, order, cultid);

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgSysPms> list = this.whgSysPmsMapper.selectByExample(example);
        return new PageInfo<WhgSysPms>(list);
    }

    /**
     * 列表查询权限组
     * @param sort
     * @param order
     * @param whgSysPms
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysPms> t_srchList(WhgSysPms whgSysPms, String sort, String order, String cultid)throws Exception{
        Example example = this.parseExample(whgSysPms, sort, order, cultid);
        return this.whgSysPmsMapper.selectByExample(example);
    }

    /**
     * 查询权限组信息
     * @param id 权限组标识
     * @return 权限组信息
     * @throws Exception
     */
    @Cacheable
    public WhgSysPms t_srchOne(String id)throws Exception{
        return this.whgSysPmsMapper.selectByPrimaryKey(id);
    }

    /**
     * 权限组是否已经启用，并且文化馆已经启用此权限组，返回true
     * @param id
     * @return
     * @throws Exception
     */
    public boolean isEnable(String id, WhgSysUserCult whgSysUserCult, String adminType)throws Exception{
        WhgSysPms sysPms = this.t_srchOne(id);
        boolean enable = sysPms.getState().intValue() == EnumState.STATE_YES.getValue() &&
                sysPms.getDelstate().intValue() == EnumStateDel.STATE_DEL_NO.getValue();
        if(enable && EnumConsoleSystem.bizmgr.getValue().equals(adminType) && whgSysUserCult != null){
            WhgSysPmsScope wsps = new WhgSysPmsScope();
            wsps.setCultid(whgSysUserCult.getCultid());
            wsps.setPmsid(id);
            int cnt = this.whgSysPmsScopeMapper.selectCount(wsps);
            enable = cnt > 0;
        }

        return enable;
    }

    /**
     * 查询权限组的明细
     * @param id 权限组标识
     * @return 权限组的明细
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysPmsDetail> t_srchPmsDetail(String id)throws Exception{
        WhgSysPmsDetail pmsDetail = new WhgSysPmsDetail();
        pmsDetail.setPmsid(id);
        return this.whgSysPmsDetailMapper.select(pmsDetail);
    }

    /**
     * 查询权限组适应的站点
     * @param id 权限组标识
     * @return List<WhgSysPmsScope>
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysPmsScope> t_srchPmsScope(String id)throws Exception{
        WhgSysPmsScope pmsScope = new WhgSysPmsScope();
        pmsScope.setPmsid(id);
        return this.whgSysPmsScopeMapper.select(pmsScope);
    }

    /**
     * 将权限组下的功能操作转化成JSON数据
     * @param whgSysPmsDetails
     * @return
     * @throws Exception
     */
    public Object parsePmsDetailJson(List<WhgSysPmsDetail> whgSysPmsDetails)throws Exception{
        List<String> pmsList = new ArrayList<String>();
        if(whgSysPmsDetails != null){
            for(WhgSysPmsDetail wsrp : whgSysPmsDetails){
                pmsList.add(wsrp.getPmsstr());
            }
        }
        return JSON.toJSON(pmsList);
    }

    /**
     * 添加
     * @param whgSysUser 管理员
     * @param whgSysPms 权限组
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public String t_add(WhgSysUser whgSysUser, WhgSysPms whgSysPms, String[] pms)throws Exception{
        //清除缓存
        this.clearLoginCache();

        //名称不能重复
        Example example = new Example(WhgSysPms.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", whgSysPms.getName());
        int count = this.whgSysPmsMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称重复");
        }

        //设置初始值
        Date now = new Date();
        String pmsid = IDUtils.getID32();
        whgSysPms.setId(pmsid);
        whgSysPms.setState(EnumState.STATE_YES.getValue());
        whgSysPms.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        whgSysPms.setCrtdate(now);
        whgSysPms.setCrtuser(whgSysUser.getId());
        whgSysPms.setStatemdfdate(now);
        whgSysPms.setStatemdfuser(whgSysUser.getId());
        int rows = this.whgSysPmsMapper.insertSelective(whgSysPms);
        if(rows != 1){
            throw new Exception("添加失败");
        }

        //权限组之权限
        if(pms != null){
            List<WhgSysPmsDetail> srpList = new ArrayList();
            for(String p : pms){
                WhgSysPmsDetail rpms = new WhgSysPmsDetail();
                rpms.setId(IDUtils.getID32());
                rpms.setPmsid(pmsid);
                rpms.setPmsstr(p);
                srpList.add(rpms);
            }
            this.whgSysPmsDetailCustomMapper.batchInsert(srpList);
        }
        return whgSysPms.getId();
    }

    /**
     * 编辑
     * @param whgSysUser
     * @param whgSysPms
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgSysUser whgSysUser, WhgSysPms whgSysPms, String[] pms)throws Exception{
        //清除缓存
        this.clearLoginCache();

        //名称不能重复
        Example example = new Example(WhgSysPms.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", whgSysPms.getName());
        c.andNotEqualTo("id", whgSysPms.getId());
        int count = this.whgSysPmsMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称重复");
        }

        //设置初始值
        Date now = new Date();
        whgSysPms.setStatemdfdate(now);
        whgSysPms.setStatemdfuser(whgSysUser.getId());
        this.whgSysPmsMapper.updateByPrimaryKeySelective(whgSysPms);

        //添加角色权限
        Example examplex = new Example(WhgSysPmsDetail.class);
        examplex.createCriteria().andEqualTo("pmsid", whgSysPms.getId());
        this.whgSysPmsDetailMapper.deleteByExample(examplex);
        if(pms != null){
            List<WhgSysPmsDetail> srpList = new ArrayList();
            for(String p : pms){
                WhgSysPmsDetail rpms = new WhgSysPmsDetail();
                rpms.setId(IDUtils.getID32());
                rpms.setPmsid(whgSysPms.getId());
                rpms.setPmsstr(p);
                srpList.add(rpms);
            }
            this.whgSysPmsDetailCustomMapper.batchInsert(srpList);
        }
    }

    /**
     * 文化馆保存默认权限组
     * @param scopeAreas
     * @param cultid
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_saveScope2cult(List<WhgSysPmsScopeArea> scopeAreas, String cultid)throws Exception{
        //清除登录缓存
        this.clearLoginCache();

        //删除文化馆的权限组
        Example example = new Example(WhgSysPmsScope.class);
        example.createCriteria().andEqualTo("cultid", cultid);
        this.whgSysPmsScopeMapper.deleteByExample(example);

        //添加默认权限组
        if(scopeAreas != null){
            List<WhgSysPmsScope> list = new ArrayList<>();
            for(WhgSysPmsScopeArea scopeArea : scopeAreas){
                WhgSysPmsScope pmsScope = new WhgSysPmsScope();
                pmsScope.setId(IDUtils.getID32());
                pmsScope.setCultid(cultid);
                pmsScope.setPmsid(scopeArea.getPmsid());
                list.add(pmsScope);
            }
            if(list != null && list.size() > 0){
                this.whgSysPmsDetailCustomMapper.batchInsertPmsScope(list);
            }
        }
    }

    /**
     * 保存权限组适应范围
     * @param pmsid
     * @param adds
     * @param dels
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_saveScope(String pmsid, String adds, String dels, String scopeArea)throws Exception{
        //清除登录缓存
        this.clearLoginCache();

        if(StringUtils.isNotEmpty(dels) && StringUtils.isNotEmpty(pmsid)) {
            Example example = new Example(WhgSysPmsScope.class);
            example.createCriteria().andEqualTo("pmsid", pmsid).andIn("cultid", Arrays.asList(dels.split(",")));
            this.whgSysPmsScopeMapper.deleteByExample(example);
        }
        if(StringUtils.isNotEmpty(adds) && StringUtils.isNotEmpty(pmsid)){
            List<WhgSysPmsScope> list = new ArrayList<>();
            String[] addArr = adds.split(",");
            for(String cultid : addArr){
                if(StringUtils.isNotEmpty(cultid)){
                    WhgSysPmsScope pmsScope = new WhgSysPmsScope();
                    pmsScope.setId(IDUtils.getID32());
                    pmsScope.setCultid(cultid);
                    pmsScope.setPmsid(pmsid);
                    list.add(pmsScope);
                }
            }
            if(list != null && list.size() > 0){
                this.whgSysPmsDetailCustomMapper.batchInsertPmsScope(list);
            }
        }

        //区域权限组
        if(StringUtils.isNotEmpty(scopeArea) && StringUtils.isNotEmpty(pmsid)){
            String[] scopeAreaArr = scopeArea.split(",");
            if(scopeAreaArr.length == 3){
                if("add".equals(scopeAreaArr[0])){
                    this.t_addScopeArea(pmsid, Integer.parseInt(scopeAreaArr[1]), scopeAreaArr[2]);
                }else if("del".equals(scopeAreaArr[0])){
                    this.t_delScopeArea(pmsid, Integer.parseInt(scopeAreaArr[1]), scopeAreaArr[2]);
                }
            }
        }
    }

    /**
     * 删除
     * @param ids 权限组唯一标识
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(WhgSysUser whgSysUser, String ids)throws Exception{
        //清除缓存
        this.clearLoginCache();

        if(ids != null){
            String[] idArr = ids.split(",");

            //删除权限组
            Example examplex = new Example(WhgSysPms.class);
            examplex.createCriteria().andIn("id", Arrays.asList(idArr));
            this.whgSysPmsMapper.deleteByExample(examplex);

            //删除权限组之权限
            Example example = new Example(WhgSysPmsDetail.class);
            example.createCriteria().andIn("pmsid", Arrays.asList(idArr));
            this.whgSysPmsDetailMapper.deleteByExample(example);
        }
    }

    /**
     * 更新角色状态
     * @param ids 文化馆ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updstate(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        //清除缓存
        this.clearLoginCache();

        if(StringUtils.isNotEmpty(ids) && StringUtils.isNotEmpty(toState)){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgSysPms.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("state", fromState);
            }
            WhgSysPms record = new WhgSysPms();
            record.setState(Integer.parseInt(toState));
            record.setStatemdfdate(new Date());
            record.setStatemdfuser(user.getId());
            this.whgSysPmsMapper.updateByExampleSelective(record, example);
        }
    }
}
