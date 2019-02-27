package com.creatoo.hn.services.admin.mass;

import com.creatoo.hn.dao.mapper.WhgMassArtistMapper;
import com.creatoo.hn.dao.mapper.WhgYwiTypeMapper;
import com.creatoo.hn.dao.model.WhgMassArtist;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgXjReason;
import com.creatoo.hn.dao.model.WhgYwiType;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by rbg on 2017/11/2.
 */
@SuppressWarnings("ALL")
@Service
public class WhgMassArtistService extends BaseService{

    @Autowired
    private WhgMassArtistMapper whgMassArtistMapper;

    @Autowired
    private WhgYwiTypeMapper whgYwiTypeMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgXjReasonService whgXjReasonService;

    /**
     * 主键查找艺术人才
     * @param id
     * @return
     * @throws Exception
     */
    public WhgMassArtist srchOne(String id) throws Exception {
        return this.whgMassArtistMapper.selectByPrimaryKey(id);
    }

    /**
     * 后台管理列表分页查询艺术人才
     * @param page
     * @param pageSize
     * @param recode
     * @param states
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public PageInfo srch4p(int page, int pageSize, WhgMassArtist recode,
                           List states, String sort, String order, String sysUserId) throws Exception {
        if (recode == null) {
            throw new Exception("param recode is null");
        }
        Example exp = new Example(recode.getClass());
        Example.Criteria c = exp.createCriteria();
        if (recode.getName() != null && !recode.getName().isEmpty()) {
            c.andLike("name", "%"+recode.getName()+"%");
            recode.setName(null);
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
        List<WhgMassArtist> list = this.whgMassArtistMapper.selectByExample(exp);
        for(WhgMassArtist info : list){
            if (info == null || info.getFeattype() == null || info.getFeattype().isEmpty()) {
                continue;
            }
            String types = info.getFeattype();
            /*Example typeexp = new Example(WhgYwiType.class);
            typeexp.selectProperties("id","name");
            typeexp.or().andIn("id", Arrays.asList(types.split("\\s*,\\s*")));
            typeexp.orderBy("idx").asc();
            List<WhgYwiType> rows = this.whgYwiTypeMapper.selectByExample(typeexp);
            StringBuilder sb = new StringBuilder();
            for (WhgYwiType yt : rows) {
                if (yt != null && yt.getName() != null && !yt.getName().isEmpty()) {
                    if (sb.length()>0){
                        sb.append(",");
                    }
                    sb.append(yt.getName());
                }
            }
            info.setFeattype(sb.toString());*/
            info.setFeattype( this.getFeattypeText(types) );
        }

        return new PageInfo(list);
    }

    public PageInfo sysSrch4p(int page, int pageSize, WhgMassArtist recode, String sort, String order, Map<String, String> record) throws Exception {
        Example example = new Example(recode.getClass());

        Example.Criteria c = example.createCriteria();
        if (recode!=null){
            if (recode.getName() != null && !recode.getName().isEmpty()) {
                c.andLike("name", "%"+recode.getName()+"%");
                recode.setName(null);
            }

            c.andEqualTo(recode);
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

        PageHelper.startPage(page, pageSize);
        List<WhgMassArtist> list= this.whgMassArtistMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);

        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgMassArtist _row : list){
                if (_row != null && _row.getFeattype() != null && !_row.getFeattype().isEmpty()) {
                    String types = _row.getFeattype();
                    _row.setFeattype( this.getFeattypeText(types) );
                }

                bm.setBean(_row);
                Map info = new HashMap();
                info.putAll(bm);
                if (_row.getCultid()!=null ){
                    WhgSysCult sysCult = this.whgSystemCultService.t_srchOne(_row.getCultid());
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
    public void t_add(WhgMassArtist info) throws Exception{
        this.whgMassArtistMapper.insert(info);
    }

    /**
     * 编辑
     * @param info
     * @throws Exception
     */
    public void t_edit(WhgMassArtist info) throws Exception {
        this.whgMassArtistMapper.updateByPrimaryKeySelective(info);
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
    public void t_updstate(String ids, String formstates, int tostate, String sysUserId, Date optTime, String reason, int issms) throws Exception{
        if (ids == null || formstates==null || formstates.isEmpty()){
            throw new Exception("params error");
        }
        Example example = new Example(WhgMassArtist.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgMassArtist info = new WhgMassArtist();
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
                List<WhgMassArtist> srclist = this.whgMassArtistMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgMassArtist _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("群文艺术人才");
                        xjr.setFktitile(_src.getName());
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

        this.whgMassArtistMapper.updateByExampleSelective(info, example);
    }

    /**
     * 删除
     * @param id
     * @throws Exception
     */
    public void t_del(String id) throws Exception{
        WhgMassArtist info = this.whgMassArtistMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        //if (info.getDelstate()!=null && info.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgMassArtistMapper.deleteByPrimaryKey(id);
        /*}else {
            info = new WhgMassArtist();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            this.whgMassArtistMapper.updateByPrimaryKeySelective(info);
        }*/
    }

    /**
     * 还原
     * @param id
     * @throws Exception
     */
    /*public void t_undel(String id) throws Exception{
        WhgMassArtist info = this.whgMassArtistMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        info = new WhgMassArtist();
        info.setId(id);
        info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        this.whgMassArtistMapper.updateByPrimaryKeySelective(info);
    }*/


    public String getFeattypeText(String feattype){
        if (feattype == null || feattype.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        try {
            Example typeexp = new Example(WhgYwiType.class);
            typeexp.selectProperties("id","name");
            typeexp.or().andIn("id", Arrays.asList(feattype.split("\\s*,\\s*")));
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
}
