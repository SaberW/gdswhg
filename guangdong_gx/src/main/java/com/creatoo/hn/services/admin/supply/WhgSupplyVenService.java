package com.creatoo.hn.services.admin.supply;

import com.creatoo.hn.dao.mapper.WhgSupplyVenMapper;
import com.creatoo.hn.dao.mapper.WhgYwiTypeMapper;
import com.creatoo.hn.dao.model.WhgSupplyVen;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgXjReason;
import com.creatoo.hn.dao.model.WhgYwiType;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
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
public class WhgSupplyVenService extends BaseService {

    @Autowired
    private WhgSupplyVenMapper whgSupplyVenMapper;

    @Autowired
    private WhgYwiTypeMapper whgYwiTypeMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgXjReasonService whgXjReasonService;

    @Autowired
    private SMSService smsService;

    /**
     * 主键查找艺术团队
     * @param id
     * @return
     * @throws Exception
     */
    public WhgSupplyVen srchOne(String id) throws Exception {
        return this.whgSupplyVenMapper.selectByPrimaryKey(id);
    }

    /**
     * 后台管理列表分页查询艺术团队
     * @param page
     * @param pageSize
     * @param recode
     * @param states
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public PageInfo srch4p(int page, int pageSize, WhgSupplyVen recode,
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
        List<WhgSupplyVen> list = this.whgSupplyVenMapper.selectByExample(exp);
        for(WhgSupplyVen info : list){
            if (info == null || info.getEtype() == null || info.getEtype().isEmpty()) {
                continue;
            }
            String types = info.getEtype();
            info.setEtype( this.getEtypeText(types) );
        }

        return new PageInfo(list);
    }

    public PageInfo sysSrchList4p(int page, int rows, WhgSupplyVen ven, String sort, String order, Map<String, String> record) throws Exception{
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
        List<WhgSupplyVen> list= this.whgSupplyVenMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);

        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgSupplyVen _ven : list){
                bm.setBean(_ven);
                Map info = new HashMap();
                info.putAll(bm);
                if (_ven.getCultid()!=null ){
                    WhgSysCult sysCult = this.whgSystemCultService.t_srchOne(_ven.getCultid());
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
    public void t_add(WhgSupplyVen info) throws Exception{
        this.whgSupplyVenMapper.insert(info);
    }

    /**
     * 编辑
     * @param info
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgSupplyVen info) throws Exception {
        this.whgSupplyVenMapper.updateByPrimaryKeySelective(info);
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
        Example example = new Example(WhgSupplyVen.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgSupplyVen info = new WhgSupplyVen();
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
                List<WhgSupplyVen> srclist = this.whgSupplyVenMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgSupplyVen _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("供需场馆");
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

        this.whgSupplyVenMapper.updateByExampleSelective(info, example);

        //YI@ 审核通过后调用短信服务
        /*if(tostate==2){
            WhgSupplyVen whgSupplyVen = whgSupplyVenMapper.selectByPrimaryKey(ids);
            String phone = whgSupplyVen.getPhone();
            Map<String ,String> map =new  HashMap<String ,String>();
            map.put("name","乐乐");
            smsService.t_sendSMS(phone,"REL_CHECK_PASS",map,whgSupplyVen.getId());
        }*/

    }

    /**
     * 删除
     * @param id
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception{
        WhgSupplyVen info = this.whgSupplyVenMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        //if (info.getDelstate()!=null && info.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgSupplyVenMapper.deleteByPrimaryKey(id);
        /*}else {
            info = new WhgSupplyVen();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            this.whgSupplyVenMapper.updateByPrimaryKeySelective(info);
        }*/
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


    public List getVens4Cultid(String cultid){
        WhgSupplyVen recode = new WhgSupplyVen();
        recode.setCultid(cultid);
        recode.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        recode.setState(EnumBizState.STATE_PUB.getValue());
        return this.whgSupplyVenMapper.select(recode);
    }
}
