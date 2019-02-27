package com.creatoo.hn.services.admin.venue;

import com.creatoo.hn.dao.mapper.WhgVenMapper;
import com.creatoo.hn.dao.mapper.WhgVenRoomMapper;
import com.creatoo.hn.dao.mapper.admin.CrtWhgVenueMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by rbg on 2017/3/23.
 */

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames="venue", keyGenerator = "simpleKeyGenerator")
public class WhgVenroomService extends BaseService{

    @Autowired
    private WhgVenRoomMapper whgVenRoomMapper;

    @Autowired
    private WhgVenMapper whgVenMapper;

    @Autowired
    private CrtWhgVenueMapper crtWhgVenueMapper;

    /*@Autowired
    private WhgSysCultMapper whgSysCultMapper;*/

    @Autowired
    private WhgFkProjectService whgFkProjectService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgXjReasonService whgXjReasonService;

    /**
     * 分页查询活动室
     * @param page
     * @param rows
     * @param request
     * @return
     * @throws Exception
     */
    @Cacheable
    public PageInfo srchList4p(int page, int rows, Map record) throws Exception{

        /*if (record != null && record.get("sysUserId")!=null) {
            if (record.get("cultid") == null || record.get("cultid").toString().isEmpty()) {
                try {
                    List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(record.get("sysUserId").toString());
                    if (cultids!=null && cultids.size()>0){
                        record.put("cultids", cultids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (record.get("deptid") == null || record.get("deptid").toString().isEmpty()) {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(record.get("sysUserId").toString());
                if (deptids != null && deptids.size() > 0) {
                    record.put("deptids", deptids);
                }
            }
        }*/

        PageHelper.startPage(page, rows);
        List list = this.crtWhgVenueMapper.srchListVenroom(record);
        return new PageInfo<Object>(list);
    }

    public PageInfo sysSrchList4p(int page, int rows, Map record) throws Exception{

        String iscult = (String) record.get("iscult");
        if (iscult!=null && "1".equals(iscult)){
            String cultid = (String) record.get("refid");
            if (cultid == null){
                throw new Exception("cultid is not null");
            }

            record.put("cultid", cultid);
        }else{
            String pcalevel = (String) record.get("pcalevel");
            String pcatext = (String) record.get("pcatext");
            if (pcalevel==null || pcalevel.isEmpty()){
                throw new Exception("pcalevel is not null");
            }
            if (pcatext==null || pcatext.isEmpty()){
                throw new Exception("pcatext is not null");
            }

            /*Example syscultemp = new Example(WhgSysCult.class);
            Example.Criteria sc = syscultemp.createCriteria();
            sc.andEqualTo("state", EnumBizState.STATE_PUB.getValue());
            sc.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

            String areakey = "province";
            if ("2".equals(pcalevel)){
                areakey = "city";
            }else if ("3".equals(pcalevel)){
                areakey = "area";
            }
            sc.andEqualTo(areakey, pcatext);

            List<String> refcultids = new ArrayList();
            List<WhgSysCult> refcults = this.whgSysCultMapper.selectByExample(syscultemp);
            if (refcults== null || refcults.size()==0){
                throw new Exception("not cults info");
            }
            for(WhgSysCult cult : refcults){
                refcultids.add(cult.getId());
            }*/
            List<String> refcultids = this.whgSystemCultService.t_srchByArea(pcalevel, pcatext);
            if (refcultids== null || refcultids.size()==0){
                throw new Exception("not cults info");
            }
            record.put("cultids", refcultids);
        }

        record.put("states", Arrays.asList(6,4));
        record.put("delstate", EnumStateDel.STATE_DEL_NO.getValue());

        PageHelper.startPage(page, rows);
        List<Map> list = this.crtWhgVenueMapper.srchListVenroom(record);
        PageInfo pageInfo = new PageInfo(list);

        List restList = new ArrayList();
        if (list!=null){
            for(Map info : list){
                Object cultid = info.get("vencultid");
                if (cultid!=null && !cultid.toString().isEmpty()){
                    WhgSysCult sysCult = this.whgSystemCultService.t_srchOne(cultid.toString()); //this.whgSysCultMapper.selectByPrimaryKey(cultid.toString());
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

    @Cacheable
    public List srchList(WhgVenRoom room, List states, String sort, String order, String sysUserId) throws Exception{
        Example example = new Example(WhgVenRoom.class);
        Example.Criteria c = example.createCriteria();

        //标题处理
        if (room.getTitle()!=null){
            c.andLike("title", "%"+room.getTitle()+"%");
            room.setTitle(null); //去除title等于条件
        }

        //指定状态集时
        if (states!=null && states.size()>0){
            c.andIn("state", states);
        }

        /*if (room.getDeptid() == null || room.getDeptid().isEmpty()) {
            room.setDeptid(null);
            try {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
                if (deptids != null && deptids.size() > 0) {
                    c.andIn("deptid", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }*/

        //常规等于处理
        c.andEqualTo(room);

        //排序
        if (sort!=null && !sort.isEmpty()){
            if (order!=null && "asc".equalsIgnoreCase(order)){
                example.orderBy(sort).asc();
            }else{
                example.orderBy(sort).desc();
            }
        }else{
            example.orderBy("statemdfdate").desc();
        }

        return this.whgVenRoomMapper.selectByExample(example);
    }

    /**
     * 查询指定ID的活动室
     * @param id
     * @return
     * @throws Exception
     */
    @Cacheable
    public WhgVenRoom srchOne(String id) throws Exception{
        return this.whgVenRoomMapper.selectByPrimaryKey(id);
    }


    /**
     * 删除活动室
     * @param id
     * @param user
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception{
        WhgVenRoom whgdata = this.whgVenRoomMapper.selectByPrimaryKey(id);
        if (whgdata == null){
            return;
        }

        //if (whgdata.getDelstate()!=null && whgdata.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgVenRoomMapper.deleteByPrimaryKey(id);
        /*}else {
            whgdata = new WhgVenRoom();
            whgdata.setId(id);
            whgdata.setDelstate(1);
            this.whgVenRoomMapper.updateByPrimaryKeySelective(whgdata);
        }*/
    }

    /**
     * 还原删除
     * @param id
     * @param user
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_undel(String id, WhgSysUser user) throws Exception{
        WhgVenRoom whgdata = this.whgVenRoomMapper.selectByPrimaryKey(id);
        if (whgdata == null){
            return;
        }

        whgdata = new WhgVenRoom();
        whgdata.setId(id);
        whgdata.setDelstate(0);
        this.whgVenRoomMapper.updateByPrimaryKeySelective(whgdata);
    }

    /**
     * 更新活动室状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param user
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser user, Date optTime, String reason, int issms) throws Exception{
        ResponseBean rb = new ResponseBean();
        if (ids == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("活动室标记丢失");
            return rb;
        }
        Example example = new Example(WhgVenRoom.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgVenRoom whgdata = new WhgVenRoom();
        whgdata.setState(tostate);
        if (optTime==null) optTime = new Date();
        whgdata.setStatemdfdate(optTime);
        whgdata.setStatemdfuser(user.getId());

        if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()){
            whgdata.setCheckor(user.getId());
            whgdata.setCheckdate(new Date());
        }
        if (tostate == EnumBizState.STATE_PUB.getValue()){
            whgdata.setPublisher(user.getId());
            whgdata.setPublishdate(new Date());
        }

        try {
            if (reason!=null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()){
                List<WhgVenRoom> srclist = this.whgVenRoomMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgVenRoom _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("活动室");
                        xjr.setFktitile(_src.getTitle());
                        xjr.setCrtuser(user.getId());
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

        this.whgVenRoomMapper.updateByExampleSelective(whgdata, example);

        return rb;
    }

    /**
     * 添加活动室
     * @param room
     * @param user
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_add(WhgVenRoom room, WhgSysUser user) throws Exception{
        ResponseBean rb = new ResponseBean();
        if (room == null || room.getVenid()==null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("活动室信息未指定");
            return rb;
        }
        WhgVen ven = new WhgVen();
        ven.setId(room.getVenid());
        ven.setDelstate(0);
        List expList = this.whgVenMapper.select(ven);
        if (expList==null || expList.size()==0){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("活动室指定的场馆不可用");
            return rb;
        }

        room.setId(IDUtils.getID());
        room.setState(1);
        room.setDelstate(0);
        room.setStatemdfdate(new Date());
        room.setStatemdfuser(user.getId());
        room.setCrtdate(new Date());
        room.setCrtuser(user.getId());
        room.setRecommend(0);
        this.whgVenRoomMapper.insert(room);

        return rb;
    }

    /**
     * 编辑活动室
     * @param room
     * @param user
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgVenRoom room) throws Exception{
        this.whgVenRoomMapper.updateByPrimaryKeySelective(room);
    }


    /**
     * 按场馆id 查有效活动室列表
     * @param venid
     * @return
     * @throws Exception
     */
    @Cacheable
    public List selectRoomList4venid(String venid) throws Exception{
        List<WhgVenRoom> rest = new ArrayList();
        if (venid == null || venid.isEmpty()){
            return rest;
        }

        WhgVenRoom recode = new WhgVenRoom();
        recode.setVenid(venid);
        recode.setState(EnumBizState.STATE_PUB.getValue());
        recode.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());

        rest = this.whgVenRoomMapper.select(recode);
        for(WhgVenRoom room : rest){
            room.setDescription(null);
        }

        return rest;
    }
}
