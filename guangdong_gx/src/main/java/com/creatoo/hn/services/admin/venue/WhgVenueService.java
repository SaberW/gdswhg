package com.creatoo.hn.services.admin.venue;

import com.creatoo.hn.dao.mapper.WhgVenMapper;
import com.creatoo.hn.dao.mapper.WhgVenRoomMapper;
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
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by rbg on 2017/7/26.
 */

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames="venue", keyGenerator = "simpleKeyGenerator")
public class WhgVenueService extends BaseService{

    @Autowired
    private WhgVenMapper whgVenMapper;

    @Autowired
    private WhgVenRoomMapper whgVenRoomMapper;

    /*@Autowired
    private WhgSysCultMapper whgSysCultMapper;*/

    @Autowired
    private WhgFkProjectService whgFkProjectService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 文化馆服务类
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgXjReasonService whgXjReasonService;


    /**
     * 查指定ID的单条场馆
     * @param id
     * @return
     * @throws Exception
     */
    @Cacheable
    public WhgVen srchOne(String id) throws Exception{
        return this.whgVenMapper.selectByPrimaryKey(id);
    }


    /**
     * 场馆列表页分页查询
     * @param page
     * @param rows
     * @param ven
     * @param request
     * @return
     * @throws Exception
     */
    @Cacheable
    public PageInfo srchList4p(int page, int rows, WhgVen ven, List states, String sort, String order, String sysUserId) throws Exception{

        Example example = getSrchListExample(ven, states, sort, order, sysUserId);

        //分页查
        PageHelper.startPage(page, rows);
        List list= this.whgVenMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo sysSrchList4p(int page, int rows, WhgVen ven, String sort, String order, Map<String, String> record) throws Exception{
        Example example = new Example(ven.getClass());

        Example.Criteria c = example.or();

        if (ven!=null){
            //标题处理
            if (ven.getTitle()!=null){
                c.andLike("title", "%"+ven.getTitle()+"%");
                ven.setTitle(null); //去除title等于条件
            }

            c.andEqualTo(ven);
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
        List<WhgVen> list= this.whgVenMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);

        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgVen _ven : list){
                bm.setBean(_ven);
                Map info = new HashMap();
                info.putAll(bm);
                if (_ven.getCultid()!=null ){
                    WhgSysCult sysCult = this.whgSystemCultService.t_srchOne(_ven.getCultid()); //this.whgSysCultMapper.selectByPrimaryKey(_ven.getCultid());
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
     * 场馆列表查询
     * @param ven
     * @param states
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */

    public List srchList(WhgVen ven, List states, String sort, String order,String userid) throws Exception{
        Example example = this.getSrchListExample(ven, states, sort, order, userid);
        return this.whgVenMapper.selectByExample(example);

        /*Example example = new Example(WhgVen.class);
        Example.Criteria c = example.createCriteria();
        //标题处理
        if (ven.getTitle()!=null){
            c.andLike("title", "%"+ven.getTitle()+"%");
            ven.setTitle(null); //去除title等于条件
        }
        //指定状态集时
        if (states!=null && states.size()>0){
            c.andIn("state", states);
        }
        if(userid!=null){
            if (ven.getCultid()!=null && !ven.getCultid().equals("")){
                c.andEqualTo("cultid", ven.getCultid());
            }
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
        return this.whgVenMapper.selectByExample(example);*/
    }

    public List srchListMore(WhgVen ven) throws Exception{
        if (ven == null || ven.getCultid() == null || ven.getCultid().isEmpty()) {
            return new ArrayList();
        }

        List list = new ArrayList();

        //本馆的数据
        List list1 = this.whgVenMapper.select(ven);
        list.addAll(list1);

        //相关的文化馆信息
        WhgSysCult cult = this.whgSystemCultService.t_srchOne(ven.getCultid());
        if (cult == null) {
            return list;
        }
        ven.setCultid(null);

        //本市的数据
        if (cult.getCity()!=null && !cult.getCity().isEmpty()){
            Example exp2 = new Example(WhgVen.class);
            Example.Criteria c2 = exp2.or();
            c2.andEqualTo(ven);
            c2.andEqualTo("city", cult.getCity());
            //排除文化馆与之前的重复
            c2.andNotEqualTo("cultid", cult.getId());

            List list2 = this.whgVenMapper.selectByExample(exp2);
            list.addAll(list2);
        }
        //其它市的
        Example exp3 = new Example(WhgVen.class);
        Example.Criteria c3 = exp3.or();
        c3.andEqualTo(ven);
        //排除文化馆与之前的重复
        c3.andNotEqualTo("cultid", cult.getId());
        if (cult.getCity()!=null && !cult.getCity().isEmpty()){
            c3.andNotEqualTo("city", cult.getCity());
        }
        List list3 = this.whgVenMapper.selectByExample(exp3);
        list.addAll(list3);

        return list;
    }


    /**
     * 组合列表查询条件
     * @param ven
     * @param states
     * @param sort
     * @param order
     * @return
     */
    private Example getSrchListExample(WhgVen ven, List states, String sort, String order, String sysUserId){
        Example example = new Example(WhgVen.class);
        Example.Criteria c = example.createCriteria();

        if (ven == null) {
            ven = new WhgVen();
        }

        //标题处理
        if (ven.getTitle()!=null){
            c.andLike("title", "%"+ven.getTitle()+"%");
            ven.setTitle(null); //去除title等于条件
        }

        //指定状态集时
        if (states!=null && states.size()>0){
            c.andIn("state", states);
        }

        if (ven.getCultid() == null || ven.getCultid().isEmpty()) {
            ven.setCultid(null);
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
                if (cultids!=null && cultids.size()>0){
                    c.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (ven.getDeptid() == null || ven.getDeptid().isEmpty()) {
            ven.setDeptid(null);
            try {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
                if (deptids != null && deptids.size() > 0) {
                    c.andIn("deptid", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        //常规等于处理
        c.andEqualTo(ven);

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

        return example;
    }

    /**
     * 添加场馆记录
     * @param ven
     * @param user
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgVen ven, WhgSysUser user) throws Exception{
        Date now = new Date();

        ven.setId( IDUtils.getID() );    //pk
        ven.setCrtdate(now);     //创建时间
        ven.setCrtuser(user.getId());   //创建人
        ven.setState(1);    //编辑状态
        ven.setStatemdfdate(now);   //状态时间
        ven.setStatemdfuser(user.getId());  //状态人
        ven.setDelstate(0); //标记未删除状态
        ven.setRecommend(0);

        this.whgVenMapper.insert(ven);
    }

    /**
     * 编辑场馆记录
     * @param ven
     * @param user
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgVen ven) throws Exception{
        this.whgVenMapper.updateByPrimaryKeySelective(ven);
    }

    /**
     * 删除场馆
     * @param id
     * @param user
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception{
        WhgVen ven = this.whgVenMapper.selectByPrimaryKey(id);
        if (ven == null){
            return;
        }

        //if (ven.getDelstate()!=null && ven.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgVenMapper.deleteByPrimaryKey(id);
        /*}else {
            ven = new WhgVen();
            ven.setId(id);
            ven.setDelstate(1);
            this.whgVenMapper.updateByPrimaryKeySelective(ven);
        }*/
    }

    /**
     * 检测场馆是否有活动室关联
     * @param venid
     * @return
     */
    public boolean isUsrVenue(String venid){
        if (venid==null || venid.isEmpty()){
            return false;
        }

        try {
            WhgVenRoom record = new WhgVenRoom();
            record.setVenid(venid);

            int count = this.whgVenRoomMapper.selectCount(record);
            return count>0;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 更改场馆 state
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
            rb.setErrormsg("场馆标记丢失");
            return rb;
        }
        Example example = new Example(WhgVen.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgVen ven = new WhgVen();
        ven.setState(tostate);
        if (optTime==null) optTime = new Date();
        ven.setStatemdfdate(optTime);
        ven.setStatemdfuser(user.getId());

        if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()){
            ven.setCheckor(user.getId());
            ven.setCheckdate(new Date());
        }
        if (tostate == EnumBizState.STATE_PUB.getValue()){
            ven.setPublisher(user.getId());
            ven.setPublishdate(new Date());
        }

        try {
            if (reason!=null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()){
                List<WhgVen> srclist = this.whgVenMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgVen _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("场馆");
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

        this.whgVenMapper.updateByExampleSelective(ven, example);

        return rb;
    }




    /**
     * 查询指定分馆ID集的有效场馆
     * @param cultids
     * @return
     */
    @Cacheable
    public Object selectVenList4Cultids(List cultids){
        List rest = new ArrayList();
        if (cultids == null || cultids.size()==0){
            return rest;
        }

        Example example = new Example(WhgVen.class);

        example.selectProperties("id", "title", "cultid", "deptid", "state", "delstate");
        example.createCriteria()
                .andEqualTo("state", EnumBizState.STATE_PUB.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue())
                .andIn("cultid", cultids);

        List<WhgVen> list = this.whgVenMapper.selectByExample(example);
        for(WhgVen ven : list){
            try {
                BeanMap bm = new BeanMap(ven);
                Map map = new HashMap();
                map.putAll(bm);
                WhgSysCult cult = this.whgSystemCultService.t_srchOne(ven.getCultid());

                if (cult!=null){
                    map.put("cultname", cult.getName());
                }else {
                    map.put("cultname", "--");
                }

                rest.add(map);
            } catch (Exception e) {
            }
        }

        return rest;
    }
}
