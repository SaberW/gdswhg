package com.creatoo.hn.services.admin.supply;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.dao.mapper.WhgSupplyRoomMapper;
import com.creatoo.hn.dao.mapper.WhgYwiTypeMapper;
import com.creatoo.hn.dao.mapper.api.ApiVenueMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.creatoo.hn.util.enums.EnumSupplyType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by rbg on 2017/11/16.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames="venue", keyGenerator = "simpleKeyGenerator")
public class WhgSupplyRoomService extends BaseService {
    @Autowired
    private WhgSupplyRoomMapper whgSupplyRoomMapper;

    @Autowired
    private WhgYwiTypeMapper whgYwiTypeMapper;

    @Autowired
    private ApiVenueMapper apiVenueMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgSupplyService whgSupplyService;

    @Autowired
    private WhgFkProjectService whgFkProjectService;

    @Autowired
    private WhgSupplyVenService supplyVenService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgXjReasonService whgXjReasonService;

    @Autowired
    private WhgSupplyTimesService whgSupplyTimesService;



    /**
     * 主键查找
     * @param id
     * @return
     * @throws Exception
     */
    public Map srchOne(String id) throws Exception {
        WhgSupplyRoom info = this.whgSupplyRoomMapper.selectByPrimaryKey(id);
        if (info == null) {
            return null;
        }
        BeanMap bm = new BeanMap();
        bm.setBean(info);

        Map rest = new HashMap();
        rest.putAll(bm);

        List list = this.whgSupplyService.selectTimes4Supply(id);
        rest.put("times", JSON.toJSONString(list));

        return rest;
    }

    public WhgSupplyRoom t_srch(String id) throws Exception {
        WhgSupplyRoom info = this.whgSupplyRoomMapper.selectByPrimaryKey(id);
        if (info == null) {
            return null;
        }
        return info;
    }

    /**
     * 后台管理列表分页查询
     * @param page
     * @param pageSize
     * @param recode
     * @param states
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public PageInfo srch4p(int page, int pageSize, WhgSupplyRoom recode,
                           List states, String sort, String order, String sysUserId) throws Exception {
        if (recode == null) {
            throw new Exception("param recode is null");
        }
        Example exp = new Example(recode.getClass());
        Example.Criteria c = exp.createCriteria();
        if (recode.getTitle() != null && !recode.getTitle().isEmpty()) {
            c.andLike("title", "%"+recode.getTitle()+"%");
            recode.setTitle(null);
        }

        if (recode.getCultid() == null || recode.getCultid().isEmpty()) {
            recode.setCultid(null);
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
                if (cultids!=null && cultids.size()>0){
                    c.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (recode.getDeptid() == null || recode.getDeptid().isEmpty()) {
            recode.setDeptid(null);
            try {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
                if (deptids != null && deptids.size() > 0) {
                    c.andIn("deptid", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        c.andEqualTo(recode);

        if (states != null && states.size() > 0) {
            c.andIn("state", states);
        }

        if (sort!=null && !sort.isEmpty()){
            if (order != null && order.equalsIgnoreCase("desc")) {
                exp.orderBy(sort).desc();
            }else {
                exp.orderBy(sort).asc();
            }
        }else {
            exp.orderBy("crtdate").desc();
        }

        PageHelper.startPage(page, pageSize);
        List<WhgSupplyRoom> list = this.whgSupplyRoomMapper.selectByExample(exp);
        for(WhgSupplyRoom info : list){
            if (info == null || info.getEtype() == null || info.getEtype().isEmpty()) {
                continue;
            }
            String types = info.getEtype();
            info.setEtype( this.getEtypeText(types) );
        }

        return new PageInfo(list);
    }

    public PageInfo sysSrchList4p(int page, int rows, WhgSupplyRoom room, String sort, String order, Map<String, String> record) throws Exception{
        Example example = new Example(room.getClass());

        Example.Criteria c = example.or();

        if (room!=null){
            //标题处理
            if (room.getTitle()!=null){
                c.andLike("title", "%"+room.getTitle()+"%");
                room.setTitle(null); //去除title等于条件
            }

            c.andEqualTo(room);
        }

        c.andIn("state", Arrays.asList(6,4));
        c.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

        String iscult = record.get("iscult");
        if (iscult!=null && "1".equals(iscult)){
            String cultid = record.get("refid");
            if (cultid == null){
                throw new Exception("cultid is not null");
            }

            c.andEqualTo("cultid", cultid);
        }else{
            String pcalevel = record.get("pcalevel");
            String pcatext = record.get("pcatext");
            if (pcalevel==null || pcalevel.isEmpty()){
                throw new Exception("pcalevel is not null");
            }
            if (pcatext==null || pcatext.isEmpty()){
                throw new Exception("pcatext is not null");
            }

            List<String> refcultids = this.whgSystemCultService.t_srchByArea(pcalevel, pcatext);
            if (refcultids== null || refcultids.size()==0){
                throw new Exception("not cults info");
            }

            c.andIn("cultid", refcultids);
        }

        //排序
        if (sort!=null && !sort.isEmpty()){
            if (order!=null && "asc".equalsIgnoreCase(order)){
                example.orderBy(sort).asc();
            }else{
                example.orderBy(sort).desc();
            }
        }else{
            example.orderBy("crtdate").desc();
        }

        PageHelper.startPage(page, rows);
        List<WhgSupplyRoom> list= this.whgSupplyRoomMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);

        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgSupplyRoom _ent : list){
                bm.setBean(_ent);
                Map info = new HashMap();
                info.putAll(bm);
                if (_ent.getCultid()!=null ){
                    WhgSysCult sysCult = this.whgSystemCultService.t_srchOne(_ent.getCultid());
                    if (sysCult!=null){
                        info.put("cultname", sysCult.getName());
                    }
                }
                restList.add(info);
            }
        }
        pageInfo.setList(restList);

        return pageInfo;
    }


    /**
     * 添加
     * @param info
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgSupplyRoom info/*, String times*/) throws Exception{
        WhgSupplyVen ven=null;
        if (info!=null && info.getVenid()!=null){
            ven=supplyVenService.srchOne(info.getVenid());
            if (ven!=null){
                info.setLatitude(ven.getLatitude());
                info.setLongitude(ven.getLongitude());
            }
        }

        this.whgSupplyRoomMapper.insert(info);

        //this.whgSupplyService.resetSupplyTimes(info.getId(), times);

        WhgFkProject whgFkProject=new WhgFkProject();
        whgFkProject.setCultid(info.getCultid());//文化馆id
        whgFkProject.setFkid(info.getId());//关联表id
        if(ven!=null) {
            whgFkProject.setProvince(ven.getProvince());//省
            whgFkProject.setCity(ven.getCity());//市
            whgFkProject.setArea(ven.getArea());//区
        }
        whgFkProject.setTitle(info.getTitle());//标题
        whgFkProject.setImgpath(info.getImgurl());//图片
        whgFkProject.setPscity(info.getPscity());//配送区域
        whgFkProject.setType(EnumSupplyType.TYPE_CG.getValue());//类型 展览
        whgFkProject.setPsprovince(info.getPsprovince());//配送区域
        whgFkProject.setState(EnumBizState.STATE_CAN_EDIT.getValue());//状态
        whgFkProject.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        whgFkProject.setCreatetime(info.getCrtdate());
        whgFkProject.setMemo(info.getSummary());//简介
        whgFkProjectService.addStr(whgFkProject);
    }

    /**
     * 编辑
     * @param info
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgSupplyRoom info/*, String times*/) throws Exception {
        WhgSupplyVen ven=null;
        if (info!=null && info.getVenid()!=null){
            ven=supplyVenService.srchOne(info.getVenid());
            if (ven!=null){
                info.setLatitude(ven.getLatitude());
                info.setLongitude(ven.getLongitude());
            }
        }
        this.whgSupplyRoomMapper.updateByPrimaryKeySelective(info);

        /*if (times!=null) {
            this.whgSupplyService.resetSupplyTimes(info.getId(), times);
        }*/
        WhgFkProject whgFkProject=whgFkProjectService.srchOneByFkId(info.getId());
        if(whgFkProject!=null) {//编辑
            whgFkProject.setCultid(info.getCultid());//文化馆id
            whgFkProject.setFkid(info.getId());//关联表id
            //WhgSupplyVen ven=supplyVenService.srchOne(info.getVenid());
            if(ven!=null) {
                whgFkProject.setProvince(ven.getProvince());//省
                whgFkProject.setCity(ven.getCity());//市
                whgFkProject.setArea(ven.getArea());//区
            }
            whgFkProject.setTitle(info.getTitle());//标题
            whgFkProject.setImgpath(info.getImgurl());//图片
            whgFkProject.setPscity(info.getPscity());//配送区域
            whgFkProject.setPsprovince(info.getPsprovince());//配送区域
            whgFkProject.setMemo(info.getSummary());//简介

            whgFkProject.setDelstate(info.getDelstate());
            whgFkProject.setState(info.getState());

            whgFkProjectService.t_edit(whgFkProject);
        }
    }

    /**
     * 修改state
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUserId
     * @param optTime
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updstate(String ids, String formstates, int tostate, String sysUserId, Date optTime, String reason, int issms) throws Exception{
        if (ids == null || formstates==null || formstates.isEmpty()){
            throw new Exception("params error");
        }
        Example example = new Example(WhgSupplyRoom.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgSupplyRoom info = new WhgSupplyRoom();
        info.setState(tostate);
        if (optTime==null) optTime = new Date();
        info.setStatemdfdate(optTime);
        info.setStatemdfuser(sysUserId);

        if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()){
            info.setCheckor(sysUserId);
            info.setCheckdate(new Date());
        }
        if (tostate == EnumBizState.STATE_PUB.getValue()){
            info.setPublisher(sysUserId);
            info.setPublishdate(new Date());
        }

        try {
            if (reason!=null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()){
                List<WhgSupplyRoom> srclist = this.whgSupplyRoomMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgSupplyRoom _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("供需活动室");
                        xjr.setFktitile(_src.getTitle());
                        xjr.setCrtuser(sysUserId);
                        xjr.setCrtdate(new Date());
                        xjr.setReason(reason);
                        xjr.setTouser(_src.getPublisher());
                        xjr.setIssms(issms);

                        this.whgXjReasonService.t_add(xjr);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }

        this.whgSupplyRoomMapper.updateByExampleSelective(info, example);
        WhgFkProject whgFkProject=whgFkProjectService.srchOneByFkId(ids.split("\\s*,\\s*")[0]);
        if(whgFkProject!=null){//修改状态
            whgFkProject.setState(tostate);
            if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()){
                whgFkProject.setCheckor(sysUserId);
                whgFkProject.setCheckdate(info.getCheckdate());
            }
            if (tostate == EnumBizState.STATE_PUB.getValue()){
                whgFkProject.setPublisher(sysUserId);
                whgFkProject.setPublishdate(info.getPublishdate());
            }
            whgFkProjectService.t_edit(whgFkProject);
        }
    }

    /**
     * 删除
     * @param id
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception{
        WhgSupplyRoom info = this.whgSupplyRoomMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        //if (info.getDelstate()!=null && info.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgSupplyTimesService.clearSupplyTimes4Supplyid(id, EnumSupplyType.TYPE_CG.getValue());
            this.whgSupplyRoomMapper.deleteByPrimaryKey(id);
            whgFkProjectService.delByFkid(id);
        /*}else {
            info = new WhgSupplyRoom();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            this.whgSupplyRoomMapper.updateByPrimaryKeySelective(info);
        }*/
    }

    /**
     * 还原
     * @param id
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_undel(String id) throws Exception{
        WhgSupplyRoom info = this.whgSupplyRoomMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        info = new WhgSupplyRoom();
        info.setId(id);
        info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        info.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        this.whgSupplyRoomMapper.updateByPrimaryKeySelective(info);

        WhgFkProject whgFkProject=whgFkProjectService.srchOneByFkId(id);
        if (whgFkProject!=null){
            whgFkProject.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            whgFkProject.setState(EnumBizState.STATE_CAN_EDIT.getValue());
            whgFkProjectService.t_edit(whgFkProject);
        }
    }


    public String getEtypeText(String etype){
        if (etype == null || etype.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        try {
            Example typeexp = new Example(WhgYwiType.class);
            typeexp.selectProperties("id","name");
            typeexp.or().andIn("id", Arrays.asList(etype.split("\\s*,\\s*")));
            typeexp.orderBy("idx").asc();
            List<WhgYwiType> rows = this.whgYwiTypeMapper.selectByExample(typeexp);
            for (WhgYwiType yt : rows) {
                if (yt != null && yt.getName() != null && !yt.getName().isEmpty()) {
                    if (sb.length()>0){
                        sb.append(",");
                    }
                    sb.append(yt.getName());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return sb.toString();
    }


    /**
     * 查指定活动室的相关活动室
     * @param roomid
     * @param size
     * @return
     * @throws Exception
     */
    public List selectRefRoomList(String roomid, Integer size, String protype) throws Exception{
        List rest = new ArrayList();
        if (roomid == null || roomid.isEmpty()){
            return rest;
        }
        WhgSupplyRoom room = this.whgSupplyRoomMapper.selectByPrimaryKey(roomid);
        if (room==null){
            return rest;
        }

        if (size != null) {
            PageHelper.startPage(1, size);
        }
        rest = this.apiVenueMapper.selectRooms4venSupply(room.getVenid(), protype, room.getId());

        return rest;
    }
}
