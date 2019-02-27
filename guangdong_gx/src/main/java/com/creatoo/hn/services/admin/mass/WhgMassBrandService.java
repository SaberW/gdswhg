package com.creatoo.hn.services.admin.mass;

import com.creatoo.hn.dao.mapper.WhgMassBrandBatchMapper;
import com.creatoo.hn.dao.mapper.WhgMassBrandMapper;
import com.creatoo.hn.dao.model.WhgMassBrand;
import com.creatoo.hn.dao.model.WhgMassBrandBatch;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgXjReason;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by rbg on 2017/10/30.
 */
@SuppressWarnings("ALL")
@Service
public class WhgMassBrandService extends BaseService{

    @Autowired
    private WhgMassBrandMapper whgMassBrandMapper;

    @Autowired
    private WhgMassBrandBatchMapper whgMassBrandBatchMapper;

    @Autowired
    private CrtWhgMassService crtWhgMassService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgXjReasonService whgXjReasonService;


    //TODO brand methods
    /**
     * 主键查群文文化专题
     * @param id
     * @return
     * @throws Exception
     */
    public WhgMassBrand srchOne(String id) throws Exception{
        return this.whgMassBrandMapper.selectByPrimaryKey(id);
    }

    /**
     * 管理列表查文化专题
     * @param page
     * @param pageSize
     * @param recode
     * @param states
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public PageInfo srch4pBrand(int page, int pageSize, WhgMassBrand recode,
                                List states, String sort, String order, String sysUserId) throws Exception{
        if (recode == null) {
            throw new Exception("param recode is null");
        }
        Example exp = new Example(recode.getClass());
        Example.Criteria c = exp.createCriteria();
        if (recode.getTitle() != null && !recode.getTitle().isEmpty()) {
            c.andLike("title", "%"+recode.getTitle()+"%");
            recode.setTitle(null);
        }
        if (recode.getTitleshort() != null && !recode.getTitleshort().isEmpty()) {
            c.andLike("titleshort", "%"+recode.getTitleshort()+"%");
            recode.setTitleshort(null);
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
        List list = this.whgMassBrandMapper.selectByExample(exp);

        return new PageInfo(list);
    }

    public PageInfo sysSrch4pBrand(int page, int pageSize, WhgMassBrand recode, String sort, String order, Map<String, String> record) throws Exception{
        Example example = new Example(recode.getClass());

        Example.Criteria c = example.createCriteria();
        if (recode!=null){
            if (recode.getTitle() != null && !recode.getTitle().isEmpty()) {
                c.andLike("title", "%"+recode.getTitle()+"%");
                recode.setTitle(null);
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
        List<WhgMassBrand> list= this.whgMassBrandMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);

        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgMassBrand _row : list){
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
     * 管理表单添加文化专题
     * @param info
     * @throws Exception
     */
    public void t_add(WhgMassBrand info) throws Exception{
        this.whgMassBrandMapper.insert(info);
    }

    /**
     * 管理表单编辑文化专题
     * @param info
     * @throws Exception
     */
    public void t_edit(WhgMassBrand info) throws Exception{
        this.whgMassBrandMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 管理列表变更文化专题状态
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
        Example example = new Example(WhgMassBrand.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgMassBrand info = new WhgMassBrand();
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
                List<WhgMassBrand> srclist = this.whgMassBrandMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgMassBrand _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("群文文化专题");
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

        this.whgMassBrandMapper.updateByExampleSelective(info, example);
    }

    /**
     * 删除文化专题
     * @param id
     * @throws Exception
     */
    public void t_del(String id) throws Exception{
        WhgMassBrand info = this.whgMassBrandMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        //if (info.getDelstate()!=null && info.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgMassBrandMapper.deleteByPrimaryKey(id);
        /*}else {
            info = new WhgMassBrand();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            this.whgMassBrandMapper.updateByPrimaryKeySelective(info);
        }*/
    }

    /**
     * 还原文化专题
     * @param id
     * @throws Exception
     */
    /*public void t_undel(String id) throws Exception{
        WhgMassBrand info = this.whgMassBrandMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        info = new WhgMassBrand();
        info.setId(id);
        info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        this.whgMassBrandMapper.updateByPrimaryKeySelective(info);
    }*/

    /**
     * 检测文化专题是否有批次信息
     * @param id
     * @return
     * @throws Exception
     */
    public boolean isUseMassBrand(String id) throws Exception{
        if (id == null || id.isEmpty()) {
            return true;
        }
        WhgMassBrandBatch recode = new WhgMassBrandBatch();
        recode.setBrandid(id);
        int refcount = this.whgMassBrandBatchMapper.selectCount(recode);
        return refcount > 0;
    }


    /**
     * 取指定文化馆的未删除专题列表
     * @param cultid
     * @return
     * @throws Exception
     */
    public List getBrands4Cultid(String cultid) throws Exception{
        WhgMassBrand recode = new WhgMassBrand();
        recode.setCultid(cultid);
        recode.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        recode.setState(EnumBizState.STATE_PUB.getValue());
        return this.whgMassBrandMapper.select(recode);
    }


    //TODO batch methods
    /**
     * 主键查找届次信息
     * @param id
     * @return
     * @throws Exception
     */
    public WhgMassBrandBatch srchOneBatch(String id) throws Exception {
        return this.whgMassBrandBatchMapper.selectByPrimaryKey(id);
    }

    /**
     * 管理列表分页查届次
     * @param page
     * @param pageSize
     * @param recode
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public PageInfo srch4pBrandBatch(int page, int pageSize, WhgMassBrandBatch recode,
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
            exp.orderBy("batdate").desc();
        }

        PageHelper.startPage(page, pageSize);
        List<WhgMassBrandBatch> list = this.whgMassBrandBatchMapper.selectByExample(exp);

        List restList = new ArrayList();
        if (list != null) {
            BeanMap bm = new BeanMap();
            for(WhgMassBrandBatch ent: list){
                Map info = new HashMap();
                bm.setBean(ent);
                info.putAll(bm);

                try {
                    WhgMassBrand _brand = this.whgMassBrandMapper.selectByPrimaryKey(ent.getBrandid());
                    if (_brand!=null){
                        info.put("brandtitle", _brand.getTitle());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

                restList.add(info);
            }
        }


        return new PageInfo(restList);
    }

    public PageInfo sysSrch4pBrandBatch(int page, int pageSize, WhgMassBrandBatch recode, String sort, String order, Map<String, String> record) throws Exception {
        Example example = new Example(recode.getClass());

        Example.Criteria c = example.createCriteria();
        if (recode!=null){
            if (recode.getTitle() != null && !recode.getTitle().isEmpty()) {
                c.andLike("title", "%"+recode.getTitle()+"%");
                recode.setTitle(null);
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
        List<WhgMassBrandBatch> list= this.whgMassBrandBatchMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);

        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgMassBrandBatch _row : list){
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
     * 添加届次信息
     * @param info
     * @throws Exception
     */
    public void addMassBrandBatch(WhgMassBrandBatch info) throws Exception {
        this.whgMassBrandBatchMapper.insert(info);
    }

    /**
     * 编辑届次信息
     * @param info
     * @throws Exception
     */
    public void editMassBrandBatch(WhgMassBrandBatch info) throws Exception {
        this.whgMassBrandBatchMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 管理列表变更届次状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUserId
     * @param optTime
     * @throws Exception
     */
    public void t_updstateBrandBatch(String ids, String formstates, int tostate, String sysUserId, Date optTime, String reason, int issms) throws Exception{
        if (ids == null || formstates==null || formstates.isEmpty()){
            throw new Exception("params error");
        }
        Example example = new Example(WhgMassBrandBatch.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgMassBrandBatch info = new WhgMassBrandBatch();
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
                List<WhgMassBrandBatch> srclist = this.whgMassBrandBatchMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgMassBrandBatch _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("群文专题届次");
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

        this.whgMassBrandBatchMapper.updateByExampleSelective(info, example);
    }

    /**
     * 删除届次信息
     * @param id
     * @throws Exception
     */
    public void t_delBrandBatch(String id) throws Exception {
        WhgMassBrandBatch info = this.whgMassBrandBatchMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        //if (info.getDelstate()!=null && info.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgMassBrandBatchMapper.deleteByPrimaryKey(id);

            try {
                //清理资讯关联
                this.crtWhgMassService.clearMassRefZx4Mid(id, EnumTypeClazz.TYPE_MASS_BATCH.getValue());
                //清理人才关联
                this.crtWhgMassService.clearMassRefRc4Mid(id, EnumTypeClazz.TYPE_MASS_BATCH.getValue());
                //清理团队关联
                this.crtWhgMassService.clearMassRefTd4Mid(id, EnumTypeClazz.TYPE_MASS_BATCH.getValue());
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }

        /*}else {
            info = new WhgMassBrandBatch();
            info.setId(id);
            info.setDelstate(EnumStateDel.STATE_DEL_YES.getValue());
            this.whgMassBrandBatchMapper.updateByPrimaryKeySelective(info);
        }*/
    }

    /**
     * 还原届次
     * @param id
     * @throws Exception
     */
    /*public void t_undelBrandBatch(String id) throws Exception{
        WhgMassBrandBatch info = this.whgMassBrandBatchMapper.selectByPrimaryKey(id);
        if (info == null){
            return;
        }

        info = new WhgMassBrandBatch();
        info.setId(id);
        info.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        this.whgMassBrandBatchMapper.updateByPrimaryKeySelective(info);
    }*/





    //TODO API metheds


    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    /**
     * 查询群文专题列表
     * @param page
     * @param pageSize
     * @param recode
     * @return
     */
    public PageInfo selectMassBrands(int page, int pageSize, WhgMassBrand recode) throws Exception{
        Example exp = new Example(recode.getClass());
        Example.Criteria c = exp.createCriteria();

        if (recode != null) {
            if (recode.getCultid() != null && !recode.getCultid().isEmpty()) {
                c.andIn("cultid", Arrays.asList( recode.getCultid().split("\\s*,\\s*") ));
                recode.setCultid(null);
            }

            if (recode.getDeptid() != null && !recode.getDeptid().isEmpty()) {
                c.andIn("deptid", this.whgSystemDeptService.srchDeptStrList(recode.getDeptid()));
                recode.setDeptid(null);
            }
            c.andEqualTo(recode);
        }

        exp.setOrderByClause("recommend desc, statemdfdate desc");

        PageHelper.startPage(page, pageSize);
        List list = this.whgMassBrandMapper.selectByExample(exp);

        return new PageInfo(list);
    }

    /**
     * 查询指定专题的届次列表
     * @param page
     * @param pageSize
     * @param brandid  文化专题id
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectMassBrandBatchs(int page, int pageSize, String brandid, WhgMassBrandBatch recode) throws Exception{
        if (brandid == null || brandid.isEmpty()) {
            return new PageInfo(new ArrayList());
        }

        Example exp = new Example(recode.getClass());
        Example.Criteria c = exp.createCriteria();
        if (recode != null) {
            recode.setBrandid(brandid);

            if (recode.getCultid() != null && !recode.getCultid().isEmpty()) {
                c.andIn("cultid", Arrays.asList( recode.getCultid().split("\\s*,\\s*") ));
                recode.setCultid(null);
            }
            if (recode.getDeptid() != null && !recode.getDeptid().isEmpty()) {
                c.andIn("deptid", this.whgSystemDeptService.srchDeptStrList(recode.getDeptid()));
                recode.setDeptid(null);
            }
            c.andEqualTo(recode);
        }

        exp.setOrderByClause("batdate desc");

        PageHelper.startPage(page, pageSize);
        List list = this.whgMassBrandBatchMapper.selectByExample(exp);

        return new PageInfo(list);
    }

}
