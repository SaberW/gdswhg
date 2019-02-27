package com.creatoo.hn.services.admin.supply;

import com.creatoo.hn.dao.mapper.WhgShowExhMapper;
import com.creatoo.hn.dao.mapper.WhgSupplyTraMapper;
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
import com.creatoo.hn.util.enums.EnumSupplyType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by Administrator on 2017/11/16.
 */
@SuppressWarnings("ALL")
@Service
public class WhgSupplyTraService extends BaseService{


    @Autowired
    private WhgSupplyTraMapper whgSupplyTraMapper;

    @Autowired
    private WhgSupplyService whgSupplyService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgFkProjectService whgFkProjectService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgXjReasonService whgXjReasonService;

    @Autowired
    private WhgSupplyTimesService whgSupplyTimesService;

    /**
     * 根据id查询展演类商品信息
     * @param id
     * @return
     */
    public WhgSupplyTra srchOne(String id) {
        return whgSupplyTraMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询展演类商品列表
     * @param page
     * @param rows
     * @param tra
     * @param sort
     * @param order
     * @return
     */
    public PageInfo t_srchlist4p(String type,int page, int rows, WhgSupplyTra tra, String sort, String order,String sysUserId) throws Exception{
        Example exp = new Example(WhgSupplyTra.class);
        Example.Criteria c = exp.createCriteria();

        if ("edit".equalsIgnoreCase(type)){
            c.andIn("state", Arrays.asList(1,2,6,9,4));
            c.andEqualTo("crtuser",sysUserId);
        }
        //审核列表，查 9待审核
        if ("check".equalsIgnoreCase(type)){
            c.andIn("state", Arrays.asList(2,6,9,4));
            tra.setCrtuser(null);
        }
        //发布列表，查 2待发布 6已发布 4已下架
        if ("publish".equalsIgnoreCase(type)){
            c.andIn("state",  Arrays.asList(2,6,9,4));
            tra.setCrtuser(null);
        }
        //删除列表，查已删除 否则查未删除的
        if ("del".equalsIgnoreCase(type)){
            c.andEqualTo("delstate", 1);
        }else{
            c.andEqualTo("delstate", 0);
        }

        if (tra.getTitle()!=null){
            c.andLike("title", "%"+tra.getTitle()+"%");
            tra.setTitle(null); //去除title等于条件
        }

        if (tra.getCultid() == null || "".equals(tra.getCultid())) {
            tra.setCultid(null);
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
                if (cultids!=null && cultids.size()>0){
                    c.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (tra.getDeptid() == null || "".equals(tra.getDeptid())) {
            tra.setDeptid(null);
            try {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
                if (deptids != null && deptids.size() > 0) {
                    c.andIn("deptid", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        c.andEqualTo(tra);

        if (sort!=null && !sort.isEmpty()){
            Example.OrderBy orderBy = exp.orderBy(sort);
            if (order!=null && "desc".equalsIgnoreCase(order)){
                orderBy.desc();
            }
        }else{
            exp.orderBy("crtdate").desc();
        }
        PageHelper.startPage(page, rows);
        List list = this.whgSupplyTraMapper.selectByExample(exp);
        return new PageInfo(list);
    }

    public PageInfo sysSrchList4p(int page, int rows, WhgSupplyTra whginfo, String sort, String order, Map<String, String> record) throws Exception{
        Example example = new Example(whginfo.getClass());

        Example.Criteria c = example.or();

        if (whginfo!=null){
            //标题处理
            if (whginfo.getTitle()!=null){
                c.andLike("title", "%"+whginfo.getTitle()+"%");
                whginfo.setTitle(null); //去除title等于条件
            }

            c.andEqualTo(whginfo);
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
        List<WhgSupplyTra> list= this.whgSupplyTraMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);

        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgSupplyTra _ent : list){
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
     * 添加展演类商品
     * @param tra
     * @param sysUser
     * @return
     */
    public ResponseBean t_add(WhgSupplyTra tra, WhgSysUser sysUser/*, String times*/) throws Exception {
        ResponseBean rsb = new ResponseBean();
        tra.setId(IDUtils.getID());
        tra.setCrtdate(new Date());
        tra.setDelstate(0);
        tra.setCrtuser(sysUser.getId());
        tra.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        tra.setStatemdfuser(sysUser.getId());
        tra.setStatemdfdate(new Date());
        this.whgSupplyTraMapper.insert(tra);
        //whgSupplyService.resetSupplyTimes(tra.getId(),times);
        WhgFkProject whgFkProject=new WhgFkProject();
        whgFkProject.setDelstate(0);
        whgFkProject.setCultid(tra.getCultid());//文化馆id
        whgFkProject.setFkid(tra.getId());//关联表id
        whgFkProject.setProvince(tra.getProvince());//省
        whgFkProject.setCity(tra.getCity());//市
        whgFkProject.setArea(tra.getArea());//区
        whgFkProject.setTitle(tra.getTitle());//标题
        whgFkProject.setImgpath(tra.getImage());//图片
        whgFkProject.setPscity(tra.getPscity());//配送区域
        whgFkProject.setType(EnumSupplyType.TYPE_PXKC.getValue());//类型 展览
        whgFkProject.setPsprovince(tra.getPsprovince());//配送区域
        whgFkProject.setState(EnumBizState.STATE_CAN_EDIT.getValue());//状态
        whgFkProject.setMemo(tra.getCoursedesc());//简介
        whgFkProjectService.addStr(whgFkProject);
        return rsb;
    }

    /**
     * 编辑商品
     * @param tra
     * @return
     */
    public ResponseBean t_edit(WhgSupplyTra tra/*,String times*/) throws Exception{
        ResponseBean rsb = new ResponseBean();
        this.whgSupplyTraMapper.updateByPrimaryKeySelective(tra);
        //whgSupplyService.resetSupplyTimes(tra.getId(),times);
        WhgFkProject whgFkProject=whgFkProjectService.srchOneByFkId(tra.getId());
        if(whgFkProject!=null) {//编辑
            whgFkProject.setCultid(tra.getCultid());//文化馆id
            whgFkProject.setFkid(tra.getId());//关联表id
            whgFkProject.setProvince(tra.getProvince());//省
            whgFkProject.setCity(tra.getCity());//市
            whgFkProject.setArea(tra.getArea());//区
            whgFkProject.setTitle(tra.getTitle());//标题
            whgFkProject.setImgpath(tra.getImage());//图片
            whgFkProject.setPscity(tra.getPscity());//配送区域
            whgFkProject.setPsprovince(tra.getPsprovince());//配送区域
            whgFkProject.setMemo(tra.getCoursedesc());//简介
            whgFkProjectService.t_edit(whgFkProject);
        }
        return rsb;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public ResponseBean t_del(String id) throws Exception{
        ResponseBean resb = new ResponseBean();
        WhgSupplyTra tra = whgSupplyTraMapper.selectByPrimaryKey(id);
        if (tra.getDelstate()!=null && tra.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgSupplyTimesService.clearSupplyTimes4Supplyid(id, EnumSupplyType.TYPE_PXKC.getValue());
            this.whgSupplyTraMapper.deleteByPrimaryKey(id);
            whgFkProjectService.delByFkid(id);
        }else if(tra.getState() == 1){
            this.whgSupplyTimesService.clearSupplyTimes4Supplyid(id, EnumSupplyType.TYPE_PXKC.getValue());
            this.whgSupplyTraMapper.deleteByPrimaryKey(id);
            whgFkProjectService.delByFkid(id);
        }else {
            tra.setDelstate(1);
            this.whgSupplyTraMapper.updateByPrimaryKeySelective(tra);

            WhgFkProject whgFkProject = whgFkProjectService.srchOneByFkId(tra.getId());
            if (whgFkProject != null) {//编辑
                whgFkProject.setCultid(tra.getCultid());//文化馆id
                whgFkProject.setFkid(id);
                whgFkProject.setDelstate(1);
                this.whgFkProjectService.t_edit(whgFkProject);
            }
        }
        return resb;
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUser
     * @return
     */
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser sysUser,Date optTime, String reason, int issms) throws Exception{
        ResponseBean resb = new ResponseBean();
        Date now = new Date();
        if (ids == null){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识丢失");
            return resb;
        }

        Example example = new Example(WhgSupplyTra.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );

        WhgSupplyTra tra = new WhgSupplyTra();
        tra.setState(tostate);
        if (optTime==null) {
            optTime = new Date();
        }
        tra.setStatemdfdate(optTime);
        tra.setStatemdfuser(sysUser.getId());
        if(tostate == 2){
            tra.setCheckor(sysUser.getId());
            tra.setCheckdate(now);

        }else if(tostate == 6){
            tra.setPublisher(sysUser.getId());
            tra.setPublishdate(now);
        }

        try {
            if (reason!=null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()){
                List<WhgSupplyTra> srclist = this.whgSupplyTraMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgSupplyTra _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("供需培训");
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
            log.error(e.getMessage(),e);
        }

        this.whgSupplyTraMapper.updateByExampleSelective(tra, example);

        WhgFkProject whgFkProject=whgFkProjectService.srchOneByFkId(ids.split("\\s*,\\s*")[0]);
        if(whgFkProject!=null){//修改状态
            whgFkProject.setState(tostate);
            if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()) {//待发布  审核者信息
                whgFkProject.setCheckor(sysUser.getId());
                whgFkProject.setCheckdate(new Date());
            } else if (tostate == EnumBizState.STATE_PUB.getValue()) {//已发布记录 发布者信息
                whgFkProject.setPublisher(sysUser.getId());
                whgFkProject.setPublishdate(new Date());
            }
            whgFkProjectService.t_edit(whgFkProject);
        }
        return resb;
    }

    /**
     * 还原供需培训
     * @param id
     * @param sysUser
     */
    public void t_undel(String id, WhgSysUser sysUser) {
        WhgSupplyTra tra = this.whgSupplyTraMapper.selectByPrimaryKey(id);
        if (tra == null){
            return;
        }
        tra.setDelstate(0);
        tra.setState(1);
        this.whgSupplyTraMapper.updateByPrimaryKeySelective(tra);
        WhgFkProject whgFkProject = null;
        try {
            whgFkProject = this.whgFkProjectService.srchOneByFkId(tra.getId());
            if (whgFkProject != null) {//编辑
                whgFkProject.setFkid(id);
                whgFkProject.setState(EnumBizState.STATE_CAN_EDIT.getValue());
                whgFkProject.setDelstate(0);
                this.whgFkProjectService.t_edit(whgFkProject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
