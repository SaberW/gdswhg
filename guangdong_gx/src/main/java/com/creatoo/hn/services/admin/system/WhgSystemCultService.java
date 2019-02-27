package com.creatoo.hn.services.admin.system;

import com.creatoo.hn.dao.mapper.WhgSysCultMapper;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysPmsScopeArea;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiArea;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiAreaService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumCultLevel;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * 场馆服务类
 * Created by wangxl on 2017/3/18.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgSysCult", keyGenerator = "simpleKeyGenerator")
public class WhgSystemCultService extends BaseService {
    /**
     * 管理员服务类
     */
    @Autowired
    private WhgSystemUserService whgSystemUserService ;

    /**
     * 权限组服务类
     */
    @Autowired
    private WhgSystemPmsService whgSystemPmsService;

    /**
     * 文化馆Mapper
     */
    @Autowired
    private WhgSysCultMapper whgSysCultMapper;

    @Autowired
    private WhgYunweiAreaService whgYunweiAreaService;

    /**
     * 根据分馆获取水印图片地址
     * @param request
     * @param cultid
     * @return
     * @throws Exception
     */
    public BufferedImage getCultSyPicture(String webRootPath, String cultid)throws Exception {
        BufferedImage waterbi =null;
        try {
            //水印地址
            WhgSysCult cult = this.t_srchOne(cultid);
            String waterFilePath = null;
            if (cult != null && StringUtils.isNotEmpty(cult.getSypicture())) {
                String rootPath = env.getProperty("upload.local.addr");
                waterFilePath = rootPath + cult.getSypicture();
            } else {
                waterFilePath= webRootPath + File.separator + "static"+ File.separator  +"admin"+ File.separator  +"img"+ File.separator + "sgsy.png";//水印图片路径
            }

            //水印图片对象
            File waterPic = new File(waterFilePath);
            waterbi = ImageIO.read(waterPic);
        }catch (Exception e){
            e.printStackTrace();
        }
        return waterbi;
    }



    /**
     * 文化馆是否已经发布
     * @param cultid 文化馆标识
     * @return true-已经发布并可用 false-未发布
     * @throws Exception
     */
    @Cacheable
    public boolean isPublished(String cultid)throws Exception{
        WhgSysCult validCult = new WhgSysCult();
        validCult.setId(cultid);
        validCult.setState(EnumBizState.STATE_PUB.getValue());
        validCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        return this.whgSysCultMapper.selectCount(validCult) == 1;
    }

    /**
     * 构造查询条件模板
     * @param whgSysCult
     * @param inState
     * @param inIds
     * @param inPids
     * @param level
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public Example parseExample(WhgSysCult whgSysCult, String inStates, String inIds, String inPids, String inLevel, String sort, String order)throws Exception{
        Example example = new Example(WhgSysCult.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(whgSysCult != null && whgSysCult.getName() != null){
            c.andLike("name", "%"+whgSysCult.getName()+"%");
            whgSysCult.setName(null);
        }

        //级别
        if(StringUtils.isNotEmpty(inLevel)){
            c.andIn("level", Arrays.asList(inLevel.split(",")));
            whgSysCult.setLevel(null);
        }

        //状态范围
        if(StringUtils.isNotEmpty(inStates)){
            c.andIn("state", Arrays.asList(inStates.split(",")));
            whgSysCult.setState(null);
        }

        //id范围
        if(StringUtils.isNotEmpty(inIds)){
            c.andIn("id", Arrays.asList(inIds.split(",")));
            whgSysCult.setId(null);
        }

        //pid范围
        if(StringUtils.isNotEmpty(inPids)){
            c.andIn("pid", Arrays.asList(inPids.split(",")));
            whgSysCult.setPid(null);
        }

        //其它条件
        c.andEqualTo(whgSysCult);

        //排序
        this.setOrder(example, sort, order, "crtdate");

        return example;
    }

    /**
     * 分页查询文化馆信息
     * @param page 第几页
     * @param rows 每页数
     * @param sort 排序字段
     * @param order 排序方式
     * @param cult 查询条件
     * @return PageInfo<WhgSysCult>
     * @throws Exception
     */
    @Cacheable
    public PageInfo<WhgSysCult> t_srchList4p(WhgSysUser whgSysUser, WhgSysCult cult, String inStates, String inLevel, int page, int rows, String sort, String order)throws Exception{
        //区域管理员，按区域查询

        //搜索条件
        Example example = this.parseExample(cult, inStates, null, null,inLevel, sort, order);

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgSysCult> list = this.whgSysCultMapper.selectByExample(example);
        return new PageInfo<WhgSysCult>(list);
    }

    /**
     * 查询文化馆列表
     * @param sort 排序字段
     * @param order 排序方式
     * @param cult 查询条件
     * @return List<WhgSysCult>
     * @throws Exception
     */
    public List<WhgSysCult> t_srchList(String sort, String order, WhgSysCult cult, String inLevel)throws Exception{
        Example example = this.parseExample(cult, null, null,null, inLevel, sort, order);
        return this.whgSysCultMapper.selectByExample(example);
    }

    /**
     * 根据查询条件过滤文化馆
     * @param cult 过滤条件
     * @return 文化馆列表
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysCult> t_srchList(WhgSysCult cult)throws Exception{
        Example example = this.parseExample(cult, null, null,null, null, null, null);
        return this.whgSysCultMapper.selectByExample(example);
    }

    /**
     * 按条件查询文化馆数量
     * @param cult
     * @return
     * @throws Exception
     */
    @Cacheable
    public int t_count(WhgSysCult cult)throws Exception{
        Example example = this.parseExample(cult, null, null,null, null, null, null);
        return this.whgSysCultMapper.selectCountByExample(example);
    }

    /**
     * 根据条件查询文化馆信息
     * @param whgSysCult 过滤条件
     * @param inStates 包含的状态
     * @param inIds 包含的id
     * @param inPids 包含的pid
     * @param inLevel 包含的level
     * @param sort 排序字段
     * @param order 排序方式
     * @return 文化馆列表
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysCult> t_srchList(WhgSysCult whgSysCult, String inStates, String inIds, String inPids, String inLevel, String sort, String order)throws Exception{
        Example example = this.parseExample(whgSysCult, inStates, inIds,inPids, inLevel, sort, order);
        return this.whgSysCultMapper.selectByExample(example);
    }

    /**
     * 查询所有已发布的文化馆
     * @return 所有可用的文化改名字列表
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysCult> t_srchList4Publish()throws Exception{
        WhgSysCult whgSysCult = new WhgSysCult();
        whgSysCult.setState(EnumBizState.STATE_PUB.getValue());
        whgSysCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        return this.t_srchList("crtdate", "desc", whgSysCult, null);
    }

    /**
     * 查询单个文化馆信息
     * @param id 文化馆主键
     * @return WhgSysCult文化馆对象
     * @throws Exception
     */
    @Cacheable
    public WhgSysCult t_srchOne(String id)throws Exception{
        return this.whgSysCultMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据文化馆标识 查 文化馆
     *
     * @throws Exception
     */
    public WhgSysCult t_srchOneBySite(String cultSite) throws Exception {
        WhgSysCult whgSysCult = new WhgSysCult();
        whgSysCult.setState(EnumBizState.STATE_PUB.getValue());
        whgSysCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        if (StringUtils.isNotEmpty(cultSite)) {
            whgSysCult.setCultsite(cultSite);
        }
        List<WhgSysCult> list = this.t_srchList(whgSysCult);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据文化馆关联的省市  查市站列表
     *
     * @throws Exception
     */
    public Object t_srchOneByCity(String code, String city) throws Exception {
        Map map = null;
        WhgYwiArea whgYwiArea = this.whgYunweiAreaService.findByCode(code);
        if (whgYwiArea != null && whgYwiArea.getCode() != null && !"".equals(whgYwiArea.getCode())) {
            map = new HashMap();
            WhgSysCult whgSysCult = new WhgSysCult();
            whgSysCult.setState(EnumBizState.STATE_PUB.getValue());
            whgSysCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            if (StringUtils.isNotEmpty(city)) {
                whgSysCult.setCity(city);
                List<WhgSysCult> list = this.t_srchList(whgSysCult);
                String str = "";
                if (list.size() > 0) {
                    for (WhgSysCult cult : list) {
                        str += "," + cult.getId();
                    }
                }
                map.put("id", str);
            } else {
                map.put("id", "");
            }
            map.put("cultsite", whgYwiArea.getCode());
            map.put("city", whgYwiArea.getName());
        }
        return map;
    }


    /**
     * 根据省市区查询文化馆
     * @throws Exception
     */
    @Cacheable
    public WhgSysCult t_srchOneByArea(String province, String city, String area)throws Exception{
        WhgSysCult whgSysCult = new WhgSysCult();
        whgSysCult.setState(EnumBizState.STATE_PUB.getValue());
        whgSysCult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        if(StringUtils.isNotEmpty(province)) {
            whgSysCult.setProvince(province);
        }
        if(StringUtils.isNotEmpty(city)){
            whgSysCult.setCity(city);
        }
        if(StringUtils.isNotEmpty(area)){
            whgSysCult.setArea(area);
        }
        List<WhgSysCult> list = this.t_srchList(whgSysCult);
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 指定省市区级别查文化馆 ID集
     * @param areaLevel 1省，2市，3区
     * @param areaVal 省市区名称
     * @return
     * @throws Exception
     */
    public List<String> t_srchByArea(String areaLevel, String areaVal) throws Exception{
        List<String> refcultids = new ArrayList();
        if (areaVal==null && areaVal.isEmpty()){
            return refcultids;
        }

        List<WhgSysCult> refcults = this.t_srchByAreaCults(areaLevel, areaVal);
        if (refcultids == null){
            return refcultids;
        }

        for(WhgSysCult cult : refcults){
            refcultids.add(cult.getId());
        }
        return refcultids;
    }
    public List<WhgSysCult> t_srchByAreaCults(String areaLevel, String areaVal) throws Exception{
        if (areaVal==null && areaVal.isEmpty()){
            return null;
        }

        Example syscultemp = new Example(WhgSysCult.class);
        Example.Criteria sc = syscultemp.createCriteria();
        sc.andEqualTo("state", EnumBizState.STATE_PUB.getValue());
        sc.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

        String areakey = "province";
        if ("2".equals(areaLevel)){
            areakey = "city";
        }else if ("3".equals(areaLevel)){
            areakey = "area";
        }
        sc.andEqualTo(areakey, areaVal);

        return this.whgSysCultMapper.selectByExample(syscultemp);
    }

    public List<WhgSysCult> t_srchByCults(List<String> cultids) throws Exception{
        if (cultids == null || cultids.size() <= 0){
            return null;
        }

        Example syscultemp = new Example(WhgSysCult.class);
        Example.Criteria sc = syscultemp.createCriteria();
        sc.andEqualTo("state", EnumBizState.STATE_PUB.getValue());
        sc.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        sc.andIn("id", cultids);
        return this.whgSysCultMapper.selectByExample(syscultemp);
    }

    /**
     * 添加文化馆
     * @param cult
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgSysCult t_add(WhgSysCult cult, WhgSysUser user)throws Exception{
        if(cult.getName()!=null) {
            //名称 站点标识不能重复
            Example example = new Example(WhgSysCult.class);
            Example.Criteria c = example.createCriteria();
            Example.Criteria c2 = example.createCriteria();
            c.andEqualTo("name", cult.getName());
            c2.andEqualTo("cultsite", cult.getCultsite());
            example.or(c2);
            int count = this.whgSysCultMapper.selectCountByExample(example);
            if (count > 0) {
                throw new Exception("名称或站点标识重复");
            }
        }

        //获得idx
        Example ex1 = new Example(WhgSysCult.class);
        Example.Criteria ex1cra = ex1.createCriteria();
        ex1cra.andIsNotNull("id");
        if (cult!=null && cult.getPid()!=null) {
            ex1cra.andEqualTo("pid", cult.getPid());
        }
        int idx = this.whgSysCultMapper.selectCountByExample(ex1);
        idx++;
        //设置初始值
        Date now = new Date();
        cult.setId(IDUtils.getID());
        cult.setCrtdate(now);
        if(user!=null){
            cult.setState(EnumBizState.STATE_CAN_EDIT.getValue()); //可编辑
            cult.setCrtuser(user.getId());
            cult.setStatemdfuser(user.getId());
        }
        cult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        cult.setStatemdfdate(now);
        cult.setUpindex(0);
        cult.setIdx(idx);
        int rows = this.whgSysCultMapper.insertSelective(cult);
        if(rows != 1){
            throw new Exception("添加资料失败");
        }

        return cult;
    }

    /**
     * 编辑文化馆
     * @param cult
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgSysCult t_edit(WhgSysCult cult, WhgSysUser user)throws Exception{
        //名称不能重复
        Example example = new Example(WhgSysCult.class);
        Example.Criteria c = example.createCriteria();
        Example.Criteria c2 = example.createCriteria();
        c.andEqualTo("name", cult.getName());
        c.andNotEqualTo("id", cult.getId());
        c2.andNotEqualTo("id", cult.getId());
        c2.andEqualTo("cultsite", cult.getCultsite());
        example.or(c2);
        int count = this.whgSysCultMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("文化馆名称h或站点标识重复");
        }

        //设置初始值
        Date now = new Date();
        cult.setStatemdfdate(now);
        cult.setStatemdfuser(user.getId());
        int rows = this.whgSysCultMapper.updateByPrimaryKeySelective(cult);
        if(rows != 1){
            throw new Exception("编辑文化馆资料失败");
        }

        return cult;
    }

    /**
     * 删除文化馆
     * @param ids 文化馆ID
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String ids, WhgSysUser user)throws Exception{
        if(ids != null && !ids.isEmpty()){
            String[] idArr = ids.split("\\s*,\\s*");
            Example example = new Example(WhgSysCult.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));

            List<WhgSysCult> list = this.whgSysCultMapper.selectByExample(example);

            for (WhgSysCult info : list) {
                if (info.getDelstate()!=null && info.getDelstate().intValue() == EnumStateDel.STATE_DEL_YES.getValue()){
                    //删除
                    this.whgSysCultMapper.deleteByPrimaryKey(info.getId());
                }else{
                    //回收
                    WhgSysCult record = new WhgSysCult();
                    record.setId(info.getId());
                    record.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
                    this.whgSysCultMapper.updateByPrimaryKeySelective(record);
                }
            }
        }
    }

    @CacheEvict(allEntries = true)
    public void t_undel(String ids){
        if (ids == null || ids.isEmpty()) return;

        String[] idArr = ids.split("\\s*,\\s*");
        Example example = new Example(WhgSysCult.class);
        Example.Criteria c = example.createCriteria();
        c.andIn("id", Arrays.asList(idArr));

        WhgSysCult record = new WhgSysCult();
        record.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        record.setState(EnumBizState.STATE_CAN_EDIT.getValue());

        this.whgSysCultMapper.updateByExampleSelective(record, example);
    }

    /**
     * 排序
     * @param id 主键
     * @param type up|top|idx
     * @param val 当type=idx时，表示直接设置排序值
     * @param user
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_sort(String id, String type, String val, WhgSysUser user)throws Exception{
        //初始所有记录的idx值
        WhgSysCult whgSysCult = this.whgSysCultMapper.selectByPrimaryKey(id);
        Integer idx = whgSysCult.getIdx();
        if(idx == null){
            Example example = new Example(WhgSysCult.class);
            example.setOrderByClause("crtdate desc");
            List<WhgSysCult> allList = this.whgSysCultMapper.selectByExample(example);
            if(allList != null && allList.size() > 0){
                for(int i=0; i<allList.size(); i++){
                    WhgSysCult _cult = allList.get(i);
                    _cult.setIdx(i+1);
                    this.whgSysCultMapper.updateByPrimaryKeySelective(_cult);
                }
            }
        }

        //up
        if("up".equals(type)){
            //当前记录
            WhgSysCult curCult = this.whgSysCultMapper.selectByPrimaryKey(id);
            int curIdx = curCult.getIdx();

            //前一条记录
            Example example2 = new Example(WhgSysCult.class);
            example2.createCriteria().andLessThan("idx", curCult.getIdx())
                    .andEqualTo("pid", curCult.getPid())
                    .andEqualTo("state", curCult.getState())
                    .andEqualTo("delstate", curCult.getDelstate());
            example2.setOrderByClause("idx desc");
            PageHelper.startPage(1,1);
            List<WhgSysCult> lessList = this.whgSysCultMapper.selectByExample(example2);
            if(lessList != null && lessList.size() == 1){
                WhgSysCult preCult = lessList.get(0);
                int preIdx = preCult.getIdx();

                curCult.setIdx(preIdx);
                this.whgSysCultMapper.updateByPrimaryKeySelective(curCult);

                preCult.setIdx(curIdx);
                this.whgSysCultMapper.updateByPrimaryKeySelective(preCult);
            }
        }

        //top
        else if("top".equals(type)){
            //当前记录
            WhgSysCult curCult = this.whgSysCultMapper.selectByPrimaryKey(id);
            int curIdx = curCult.getIdx();

            Example example = new Example(WhgSysCult.class);
            example.createCriteria().andLessThan("idx", curIdx)
                    .andEqualTo("pid", curCult.getPid())
                    .andEqualTo("state", curCult.getState());
            example.setOrderByClause("idx");
            List<WhgSysCult> allList = this.whgSysCultMapper.selectByExample(example);
            if(allList != null && allList.size() > 0){
                List<WhgSysCult> newList = new ArrayList<WhgSysCult>();
                for(WhgSysCult c : allList){
                    if(c != null && !curCult.getId().equals(c.getId())){
                        newList.add(c);
                    }
                }

                curCult.setIdx(1);
                this.whgSysCultMapper.updateByPrimaryKey(curCult);

                for(int i=0; i<newList.size(); i++){
                    WhgSysCult _cult = newList.get(i);
                    _cult.setIdx(i+2);
                    this.whgSysCultMapper.updateByPrimaryKeySelective(_cult);
                }
            }
        }

        //idx 直接设置idx值
        else if("idx".equals(type)){
            //当前记录
            WhgSysCult curCult = this.whgSysCultMapper.selectByPrimaryKey(id);
            curCult.setIdx(Integer.parseInt(val));
            this.whgSysCultMapper.updateByPrimaryKey(curCult);
        }
    }

    /**
     * 更新文化馆状态
     * @param ids 文化馆ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updstate(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            String[] idArr = ids.split(",");
            for(String id : idArr){
                Example example = new Example(WhgSysCult.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("id", id);
                if(fromState != null){
                    c.andIn("state", Arrays.asList(fromState.split("\\s*,\\s*")));
                }
                WhgSysCult record = new WhgSysCult();
                record.setState(Integer.parseInt(toState));
                Date now = new Date();
                record.setStatemdfdate(now);
                record.setStatemdfuser(user.getId());
                if((EnumBizState.STATE_CAN_PUB.getValue()+"").equals(toState) ||
                        ((EnumBizState.STATE_CAN_EDIT.getValue()+"").equals(toState)
                                && (EnumBizState.STATE_CAN_CHECK.getValue()+"").equals(toState)) ){//审核通过 审核打回
                    record.setCheckor(user.getId());
                    record.setCheckdate(now);
                }else if((EnumBizState.STATE_PUB.getValue()+"").equals(toState)){//发布者
                    record.setPublisher(user.getId());
                    record.setPublishdate(now);

                    //发布时自动配置默认权限组
                    WhgSysCult whgSysCult = this.t_srchOne(id);
                    List<WhgSysPmsScopeArea> scopeAreas = this.whgSystemPmsService.t_srchAreaPms(EnumCultLevel.Level_Province.getValue(), whgSysCult.getProvince());
                    if(whgSysCult.getLevel() == EnumCultLevel.Level_City.getValue()){
                        scopeAreas.addAll(this.whgSystemPmsService.t_srchAreaPms(EnumCultLevel.Level_City.getValue(), whgSysCult.getCity()));
                    }else if(whgSysCult.getLevel() == EnumCultLevel.Level_Area.getValue()){
                        scopeAreas.addAll(this.whgSystemPmsService.t_srchAreaPms(EnumCultLevel.Level_City.getValue(), whgSysCult.getCity()));
                        scopeAreas.addAll(this.whgSystemPmsService.t_srchAreaPms(EnumCultLevel.Level_Area.getValue(), whgSysCult.getArea()));
                    }
                    if(scopeAreas != null){
                        this.whgSystemPmsService.t_saveScope2cult(scopeAreas, id);
                    }
                }
                this.whgSysCultMapper.updateByExampleSelective(record, example);
            }
        }
    }

    /**
     * 更新文化馆状态
     * @param ids 文化馆ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updsqstate(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgSysCult.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("sqstate", fromState);
            }
            WhgSysCult record = new WhgSysCult();
            record.setSqstate(Integer.parseInt(toState));
            Date now = new Date();
            record.setStatemdfdate(now);
            record.setStatemdfuser(user.getId());
            this.whgSysCultMapper.updateByExampleSelective(record, example);
        }
    }

    /**
     * 上首页
     * @param ids
     * @param formupindex
     * @param toupindex
     * @return
     */
    @CacheEvict(allEntries = true)
    public void t_upindex(String ids, String formupindex, int toupindex)throws Exception {
        Example example = new Example(WhgSysCult.class);
        Example.Criteria c = example.createCriteria();
        c.andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ));
        c.andIn("upindex", Arrays.asList( formupindex.split("\\s*,\\s*") ));
        WhgSysCult cult = new WhgSysCult();
        cult.setUpindex(toupindex);
        this.whgSysCultMapper.updateByExampleSelective(cult,example);
    }
}
