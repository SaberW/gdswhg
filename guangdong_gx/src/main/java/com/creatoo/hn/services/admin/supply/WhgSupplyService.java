package com.creatoo.hn.services.admin.supply;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgSupplyMapper;
import com.creatoo.hn.dao.mapper.WhgSupplyTimeMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumConsoleSystem;
import com.creatoo.hn.util.enums.EnumSupplyGxType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rbg on 2017/8/22.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "supply", keyGenerator = "simpleKeyGenerator")
public class WhgSupplyService extends BaseService {

    @Autowired
    private WhgSupplyMapper whgSupplyMapper;

    @Autowired
    private WhgSupplyTimeMapper whgSupplyTimeMapper;

    @Autowired
    private WhgFkProjectService whgFkProjectService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;
    @Autowired
    private WhgXjReasonService whgXjReasonService;

    /**
     * 主键查询供需信息
     * @param id
     * @return
     * @throws Exception
     */
    @Cacheable
    public Object srchOne(String id) throws Exception{
        WhgSupply supply = this.whgSupplyMapper.selectByPrimaryKey(id);
        if (supply == null) {
            return null;
        }
        BeanMap bm = new BeanMap();
        bm.setBean(supply);

        Map rest = new HashMap();
        rest.putAll(bm);

        List list = this.selectTimes4Supply(id);
        rest.put("times", JSON.toJSONString(list));

        return rest;
    }
    /**
     * 主键查询供需信息
     * @param id
     * @return
     * @throws Exception
     */
    @Cacheable
    public WhgSupply t_srch(String id) throws Exception{
        WhgSupply supply = this.whgSupplyMapper.selectByPrimaryKey(id);
        if (supply == null) {
            return null;
        }
        return supply;
    }

    /**
     * 分页查询供需信息
     * @param page
     * @param rows
     * @param supply
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */

    public Object srchList4p(String type, int page, int rows, WhgSupply supply, String sort, String order, String sysUserId, Map<String, String> record) throws Exception {
        Example exp = new Example(WhgSupply.class);
        Example.Criteria ca = exp.createCriteria();

        //编辑列表
        if ("editlist".equalsIgnoreCase(type)) {
            ca.andEqualTo("crtuser", sysUserId);
        }
        //删除列表，查已删除 否则查未删除的
        if ("recycle".equalsIgnoreCase(type)) {
            ca.andEqualTo("delstate", 1);
        } else {
            ca.andEqualTo("delstate", 0);
        }
        //总分馆发布列表，查 6已发布 4已下架
        if ("syslistpublish".equalsIgnoreCase(type)) {
            ca.andIn("state", Arrays.asList(6, 4));
        }


            if (supply.getTitle()!=null && !supply.getTitle().isEmpty()){
                ca.andLike("title", "%"+supply.getTitle()+"%");
                supply.setTitle(null);
            }
            if (supply.getContacts()!=null && !supply.getContacts().isEmpty()){
                ca.andLike("contacts", "%"+supply.getContacts()+"%");
                supply.setContacts(null);
            }
            if (supply.getPhone()!=null && !supply.getPhone().isEmpty()){
                ca.andLike("phone", "%"+supply.getPhone()+"%");
                supply.setPhone(null);
            }
            if (supply.getState() != null) {
                ca.andEqualTo("state", supply.getState());
                supply.setState(null);
            } else {
                if (!"edit".equalsIgnoreCase(type)) {
                    ca.andIn("state", Arrays.asList(9, 2, 6, 4));
                    supply.setState(null);
                }
            }

        WhgSysUser sysUser = whgSystemUserService.t_srchOne(sysUserId);
        if (sysUser != null && sysUser.getAdmintype() != null && sysUser.getAdmintype().equals(EnumConsoleSystem.sysmgr.getValue())) {//区域管理员
            String iscult = record.get("iscult");
            if (iscult != null && "1".equals(iscult)) {
                String cultid = record.get("refid");
                if (cultid == null) {
                    throw new Exception("cultid is not null");
                }
                ca.andEqualTo("cultid", cultid);
            } else {
                String pcalevel = record.get("pcalevel");
                String pcatext = record.get("pcatext");
                if (pcalevel == null || pcalevel.isEmpty()) {
                    throw new Exception("pcalevel is not null");
                }
                if (pcatext == null || pcatext.isEmpty()) {
                    throw new Exception("pcatext is not null");
                }
                List<String> refcultids = this.whgSystemCultService.t_srchByArea(pcalevel, pcatext);
                if (refcultids == null || refcultids.size() == 0) {
                    throw new Exception("not cults info");
                }
                ca.andIn("cultid", refcultids);
            }
        } else {
            if (supply.getCultid() == null || supply.getCultid().isEmpty()) {
                supply.setCultid(null);
                try {
                    List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
                    if (cultids != null && cultids.size() > 0) {
                        ca.andIn("cultid", cultids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (supply.getDeptid() == null || supply.getDeptid().isEmpty()) {
                supply.setDeptid(null);
                try {
                    List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
                    if (deptids != null && deptids.size() > 0) {
                        ca.andIn("deptid", deptids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
            ca.andEqualTo(supply);

        if (sort!=null && !sort.isEmpty()){
            Example.OrderBy orderBy = exp.orderBy(sort);
            if (order!=null && "desc".equalsIgnoreCase(order)){
                orderBy.desc();
            }
        }else{
            exp.orderBy("crtdate").desc();
        }

        PageHelper.startPage(page, rows);
        List list = this.whgSupplyMapper.selectByExample(exp);

        return new PageInfo(list);
    }

    /**
     * 添加供需信息
     * @param supply
     * @param sysUser
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_add(WhgSysUser sysUser, WhgSupply supply, String times) throws Exception{
        ResponseBean resb = new ResponseBean();

        /*String cultid = sysUser.getCultid();
        if (cultid == null || cultid.isEmpty()) {
            cultid = Constant.ROOT_SYS_CULT_ID;
        }*/

        supply.setId(IDUtils.getID());
        //supply.setCultid(cultid);
        supply.setCrtdate(new Date());
        supply.setCrtuser(sysUser.getId());
        supply.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        supply.setDelstate(0);
        supply.setStatemdfuser(sysUser.getId());
        supply.setStatemdfdate(new Date());

        if (supply.getGxtype() == null || supply.getGxtype().isEmpty()) {
            supply.setGxtype(EnumSupplyGxType.TYPE_X.getValue());
        }

        this.whgSupplyMapper.insert(supply);

        this.resetSupplyTimes(supply.getId(), times);

        return resb;
    }

    /**
     * 编辑供需信息
     * @param sysUser
     * @param supply
     * @param times
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_edit(WhgSysUser sysUser, WhgSupply supply, String times) throws Exception{
        ResponseBean resb = new ResponseBean();

        if (supply == null || supply.getId() == null || supply.getId().isEmpty()) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("信息标识不能为空");
        }

        //处理设空值无法写入
        if (supply.getPscity() == null) {
            supply.setPscity("");
        }

        this.whgSupplyMapper.updateByPrimaryKeySelective(supply);

        this.resetSupplyTimes(supply.getId(), times);

        return resb;
    }

    /**
     * @param suuplyId
     * @param times 时间JSONArray 字符串
     * 重置供需信息时间表项
     * */
    @CacheEvict(allEntries = true)
    public void resetSupplyTimes(String suuplyId, String times) throws Exception{
        if (suuplyId == null){
            return;
        }

        this.clearSupplyTimes(suuplyId);

        List<WhgSupplyTime> timesList = null;
        if (times != null && !times.isEmpty()) {
            JSONArray timesArray = JSON.parseArray(times);
            if (timesArray.size()>0){
                timesList = new ArrayList();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for(Object ent : timesArray){
                    try {
                        JSONObject time = (JSONObject) ent;
                        WhgSupplyTime item = new WhgSupplyTime();
                        item.setId(IDUtils.getID());
                        item.setSupplyid(suuplyId);

                        String ts = (String) time.get("timestart");
                        String te = (String) time.get("timeend");
                        item.setTimestart(sdf.parse(ts));
                        item.setTimeend(sdf.parse(te));

                        this.whgSupplyTimeMapper.insert(item);
                    } catch (ParseException e) {
                        log.error(e.getMessage(), e);
                        continue;
                    }
                }
            }
        }
    }

    /**
     * 清除供需时间信息
     * @param suuplyId
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void clearSupplyTimes(String suuplyId) throws Exception{
        if (suuplyId == null){
            return;
        }

        WhgSupplyTime recode = new WhgSupplyTime();
        recode.setSupplyid(suuplyId);
        this.whgSupplyTimeMapper.delete(recode);
    }

    /**
     * 查供需的时间段列表
     * @param supplyId
     * @return
     * @throws Exception
     */
    public List selectTimes4Supply(String supplyId) throws Exception{
        /*Example example = new Example(WhgSupplyTime.class);
        example.createCriteria().andEqualTo("supplyid", supplyId);
        example.orderBy("timestart").asc();

        return this.whgSupplyTimeMapper.selectByExample(example);*/

        return (List) this.selectTimes4Supply(supplyId, null, null);
    }

    public Object selectTimes4Supply(String supplyId, Integer page, Integer pageSize) throws Exception{
        Example example = new Example(WhgSupplyTime.class);
        example.createCriteria().andEqualTo("supplyid", supplyId);
        example.orderBy("timestart").desc();

        if (page!=null && pageSize!=null){
            PageHelper.startPage(page, pageSize);
            List list = this.whgSupplyTimeMapper.selectByExample(example);
            return new PageInfo(list);
        }else{
            return this.whgSupplyTimeMapper.selectByExample(example);
        }

    }

    /**
     * 删除供需信息
     * @param id
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_del(String id) throws Exception{
        ResponseBean resb = new ResponseBean();
        if (id==null || id.isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("标识不能为空");
            return resb;
        }
        this.whgSupplyMapper.deleteByPrimaryKey(id);
        this.clearSupplyTimes(id);
        //删除fk表中对应的数据
        this.whgFkProjectService.delByFkid(id);
        return resb;
    }

    /**
     * 还原
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    public void t_undel(String id) {
        WhgSupply cultHeritage = whgSupplyMapper.selectByPrimaryKey(id);
        if (cultHeritage == null) {
            return;
        }
        cultHeritage = new WhgSupply();
        cultHeritage.setId(id);
        cultHeritage.setDelstate(0);
        cultHeritage.setState(EnumBizState.STATE_CAN_EDIT.getValue());//还原 撤销 回收 打回编辑列表 重走流程
        this.whgSupplyMapper.updateByPrimaryKeySelective(cultHeritage);
    }

    /**
     * 回收
     *
     * @param ids 活动ID，多个用逗号分隔
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updDelstate(String id) throws Exception {
        if (id != null) {
            WhgSupply cultHeritage = whgSupplyMapper.selectByPrimaryKey(id);
            if (cultHeritage == null) {
                return;
            }
            cultHeritage = new WhgSupply();
            cultHeritage.setId(id);
            cultHeritage.setDelstate(1);
            this.whgSupplyMapper.updateByPrimaryKeySelective(cultHeritage);
        }
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUser
     * @param optTime
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser sysUser, Date optTime, String reason, int issms) throws Exception {
        ResponseBean resb = new ResponseBean();
        if (ids == null){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("标识不能为空");
            return resb;
        }

        Example example = new Example(WhgSupply.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );

        WhgSupply info = new WhgSupply();
        info.setState(tostate);
        if (optTime==null) {
            optTime = new Date();
        }
        if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()) {//待发布  审核者信息
            info.setCheckor(sysUser.getId());
            info.setCheckdate(new Date());
        } else if (tostate == EnumBizState.STATE_PUB.getValue()) {//已发布记录 发布者信息
            info.setPublisher(sysUser.getId());
            info.setPublishdate(optTime);
        }

        try {
            if (reason != null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()) {
                List<WhgSupply> srclist = this.whgSupplyMapper.selectByExample(example);
                if (srclist != null) {
                    for (WhgSupply _src : srclist) {
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("供需场馆");
                        xjr.setFktitile(_src.getTitle());
                        xjr.setCrtuser(sysUser.getId());
                        xjr.setCrtdate(new Date());
                        xjr.setReason(reason);
                        xjr.setTouser(_src.getPublisher());
                        xjr.setIssms(issms);
                        this.whgXjReasonService.t_add(xjr);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        info.setStatemdfdate(optTime);
        info.setStatemdfuser(sysUser.getId());
        this.whgSupplyMapper.updateByExampleSelective(info, example);
        return resb;
    }
}
