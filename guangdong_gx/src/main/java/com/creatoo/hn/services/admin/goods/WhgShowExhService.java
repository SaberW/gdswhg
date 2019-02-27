package com.creatoo.hn.services.admin.goods;

import com.creatoo.hn.dao.mapper.WhgShowExhMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.services.admin.supply.WhgSupplyTimesService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiYjpzservice;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumConsoleSystem;
import com.creatoo.hn.util.enums.EnumSupplyType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/1.
 */
@Service
public class WhgShowExhService extends BaseService {
    @Autowired
    private WhgYunweiYjpzservice whgYunweiYjpzservice;

    @Autowired
    private WhgShowExhMapper whgShowExhMapper;

    @Autowired
    private WhgSupplyService whgSupplyService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    private WhgFkProjectService whgFkProjectService;

    @Autowired
    private WhgXjReasonService whgXjReasonService;

    @Autowired
    private WhgSupplyTimesService whgSupplyTimesService;

    /**
     * 根据id查询展演类商品信息
     * @param id
     * @return
     */
    public WhgShowExh srchOne(String id) {
        return whgShowExhMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询展演类商品列表
     * @param page
     * @param rows
     * @param exh
     * @param sort
     * @param order
     * @return
     */
    public PageInfo t_srchlist4p(String type, int page, int rows, String userId, WhgShowExh exh, String sort, String order, Map<String, String> record) throws Exception {
        Example exp = new Example(WhgShowExh.class);
        Example.Criteria ca = exp.createCriteria();

        if (exh != null) {
            if ("edit".equalsIgnoreCase(type)) {
                ca.andIn("state", Arrays.asList(1, 2, 6, 9, 4));
                ca.andEqualTo("crtuser", userId);
            }
            //审核列表，查 9待审核
            if ("check".equalsIgnoreCase(type)) {
                ca.andIn("state", Arrays.asList(2, 6, 9, 4));
                exh.setCrtuser(null);
            }
            //发布列表，查 2待发布 6已发布 4已下架
            if ("publish".equalsIgnoreCase(type)) {
                ca.andIn("state", Arrays.asList(2, 6, 9, 4));
                exh.setCrtuser(null);
            }
            //总分馆发布列表，查 6已发布 4已下架
            if ("syslistpublish".equalsIgnoreCase(type)) {
                ca.andIn("state", Arrays.asList(6, 4));
                exh.setCrtuser(null);
            }
            //删除列表，查已删除 否则查未删除的
            if ("del".equalsIgnoreCase(type)) {
                ca.andEqualTo("delstate", 1);
            } else {
                ca.andEqualTo("delstate", 0);
            }

            if (exh.getTitle() != null && !exh.getTitle().isEmpty()) {
                ca.andLike("name", "%" + exh.getTitle() + "%");
                exh.setTitle(null);
            }
            WhgSysUser sysUser = whgSystemUserService.t_srchOne(userId);
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
                exh.setCultid(null);
            } else {
                if (exh.getCultid() == null || "".equals(exh.getCultid())) {
                    exh.setCultid(null);
                    List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(userId);
                    if (cultids != null && cultids.size() > 0) {
                        ca.andIn("cultid", cultids);

                    }
                }
                if (exh.getDeptid() == null || "".equals(exh.getDeptid())) {
                    exh.setDeptid(null);
                    List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(userId);
                    if (deptids != null && deptids.size() > 0) {
                        ca.andIn("deptid", deptids);
                    }
                }
                ca.andEqualTo(exh);
            }
        }
        ca.andEqualTo(exh);
        if (sort!=null && !sort.isEmpty()){
            Example.OrderBy orderBy = exp.orderBy(sort);
            if (order!=null && "desc".equalsIgnoreCase(order)){
                orderBy.desc();
            }
        }else{
            exp.orderBy("crtdate").desc();
        }
        PageHelper.startPage(page, rows);
        List list = this.whgShowExhMapper.selectByExample(exp);
        return new PageInfo(list);
    }

    /**
     * 添加展演类商品
     * @param exh
     * @param sysUser
     * @return
     */
    public ResponseBean t_add(WhgShowExh exh, WhgSysUser sysUser/*,String times*/) throws Exception {
        ResponseBean rsb = new ResponseBean();
        exh.setId(IDUtils.getID());
        exh.setCrtdate(new Date());
        exh.setDelstate(0);
        exh.setCrtuser(sysUser.getId());
        exh.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        exh.setStatemdfuser(sysUser.getId());
        exh.setStatemdfdate(new Date());
        this.whgShowExhMapper.insert(exh);
        //whgSupplyService.resetSupplyTimes(exh.getId(),times);
        WhgFkProject whgFkProject=new WhgFkProject();
        whgFkProject.setDelstate(0);
        whgFkProject.setCultid(exh.getCultid());//文化馆id
        whgFkProject.setFkid(exh.getId());//关联表id
        whgFkProject.setProvince(exh.getProvince());//省
        whgFkProject.setCity(exh.getCity());//市
        whgFkProject.setArea(exh.getArea());//区
        whgFkProject.setTitle(exh.getTitle());//标题
        whgFkProject.setImgpath(exh.getImage());//图片
        whgFkProject.setPscity(exh.getPscity());//配送区域
        whgFkProject.setType(EnumSupplyType.TYPE_ZL.getValue());//类型 展览
        whgFkProject.setPsprovince(exh.getPsprovince());//配送区域
        whgFkProject.setState(EnumBizState.STATE_CAN_EDIT.getValue());//状态
        whgFkProject.setMemo(exh.getExhexplain());//简介
        whgFkProjectService.addStr(whgFkProject);
        return rsb;
    }


    /**
     * 添加展演类商品
     * @param exh
     * @param sysUser
     * @return
     */
    @Transactional
    public ResponseBean t_add(WhgShowExh exh, WhgSysUser sysUser/*,String times*/,String[] yjpzName,String[] yjpzDetail) throws Exception {
        ResponseBean rsb = new ResponseBean();
        exh.setId(IDUtils.getID());
        exh.setCrtdate(new Date());
        exh.setDelstate(0);
        exh.setCrtuser(sysUser.getId());
        exh.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        exh.setStatemdfuser(sysUser.getId());
        exh.setStatemdfdate(new Date());
        this.whgShowExhMapper.insert(exh);
        if(yjpzName!=null&&yjpzDetail!=null){
            for (int i = 0; i < yjpzName.length; i++) {
                WhgYwiHardware whgYwiHardware = new WhgYwiHardware();
                whgYwiHardware.setEntid(exh.getId());
                whgYwiHardware.setName(yjpzName[i]);
                whgYwiHardware.setDetail(yjpzDetail[i]);
                whgYunweiYjpzservice.t_add(whgYwiHardware,sysUser);
            }
        }

        //whgSupplyService.resetSupplyTimes(exh.getId(),times);
        WhgFkProject whgFkProject=new WhgFkProject();
        whgFkProject.setDelstate(0);
        whgFkProject.setCultid(exh.getCultid());//文化馆id
        whgFkProject.setFkid(exh.getId());//关联表id
        whgFkProject.setProvince(exh.getProvince());//省
        whgFkProject.setCity(exh.getCity());//市
        whgFkProject.setArea(exh.getArea());//区
        whgFkProject.setTitle(exh.getTitle());//标题
        whgFkProject.setImgpath(exh.getImage());//图片
        whgFkProject.setPscity(exh.getPscity());//配送区域
        whgFkProject.setType(EnumSupplyType.TYPE_ZL.getValue());//类型 展览
        whgFkProject.setPsprovince(exh.getPsprovince());//配送区域
        whgFkProject.setState(EnumBizState.STATE_CAN_EDIT.getValue());//状态
        whgFkProject.setMemo(exh.getExhexplain());//简介
        whgFkProjectService.addStr(whgFkProject);
        return rsb;
    }

    /**
     * 编辑商品
     * @param exh
     * @return
     *//*
    public ResponseBean t_edit(WhgShowExh exh*//*,String times*//*) throws Exception{
        ResponseBean rsb = new ResponseBean();
        this.whgShowExhMapper.updateByPrimaryKeySelective(exh);
        //whgSupplyService.resetSupplyTimes(exh.getId(),times);
        WhgFkProject whgFkProject=whgFkProjectService.srchOneByFkId(exh.getId());
        if(whgFkProject!=null) {//编辑
            whgFkProject.setCultid(exh.getCultid());//文化馆id
            whgFkProject.setFkid(exh.getId());//关联表id
            whgFkProject.setProvince(exh.getProvince());//省
            whgFkProject.setCity(exh.getCity());//市
            whgFkProject.setArea(exh.getArea());//区
            whgFkProject.setTitle(exh.getTitle());//标题
            whgFkProject.setImgpath(exh.getImage());//图片
            whgFkProject.setPscity(exh.getPscity());//配送区域
            whgFkProject.setPsprovince(exh.getPsprovince());//配送区域
            whgFkProject.setMemo(exh.getExhexplain());//简介
            whgFkProjectService.t_edit(whgFkProject);
        }
        return rsb;
    }*/


    /**
     * 编辑展览展示硬件配置
     * @param exh
     * @return
     */
    @Transactional
    public ResponseBean t_edit(WhgShowExh exh/*,String times*/,WhgSysUser sysUser,String[] yjpzName,String[] yjpzDetail) throws Exception{
        String id = exh.getId();
        whgYunweiYjpzservice.delHardwareByExhId(id);
        if(yjpzName!=null&&yjpzDetail!=null){
            for (int i = 0; i < yjpzName.length; i++) {
                WhgYwiHardware whgYwiHardware = new WhgYwiHardware();
                whgYwiHardware.setEntid(exh.getId());
                whgYwiHardware.setName(yjpzName[i]);
                whgYwiHardware.setDetail(yjpzDetail[i]);
                whgYunweiYjpzservice.t_add(whgYwiHardware,sysUser);
            }
        }


        ResponseBean rsb = new ResponseBean();
        this.whgShowExhMapper.updateByPrimaryKeySelective(exh);
        //whgSupplyService.resetSupplyTimes(exh.getId(),times);
        WhgFkProject whgFkProject=whgFkProjectService.srchOneByFkId(exh.getId());
        if(whgFkProject!=null) {//编辑
            whgFkProject.setCultid(exh.getCultid());//文化馆id
            whgFkProject.setFkid(exh.getId());//关联表id
            whgFkProject.setProvince(exh.getProvince());//省
            whgFkProject.setCity(exh.getCity());//市
            whgFkProject.setArea(exh.getArea());//区
            whgFkProject.setTitle(exh.getTitle());//标题
            whgFkProject.setImgpath(exh.getImage());//图片
            whgFkProject.setPscity(exh.getPscity());//配送区域
            whgFkProject.setPsprovince(exh.getPsprovince());//配送区域
            whgFkProject.setMemo(exh.getExhexplain());//简介
            whgFkProjectService.t_edit(whgFkProject);
        }
        return rsb;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Transactional
    public ResponseBean t_del(String id) throws Exception{
        ResponseBean resb = new ResponseBean();
        WhgShowExh exh = whgShowExhMapper.selectByPrimaryKey(id);
        if (exh.getDelstate()!=null && exh.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgSupplyTimesService.clearSupplyTimes4Supplyid(id, EnumSupplyType.TYPE_ZL.getValue());
            this.whgShowExhMapper.deleteByPrimaryKey(id);
            whgFkProjectService.delByFkid(id);
            //YI@
            whgYunweiYjpzservice.delHardwareByExhId(id);
        }else if(exh.getState() == 1){
            this.whgSupplyTimesService.clearSupplyTimes4Supplyid(id, EnumSupplyType.TYPE_ZL.getValue());
            this.whgShowExhMapper.deleteByPrimaryKey(id);
            //YI@
            whgYunweiYjpzservice.delHardwareByExhId(id);
            whgFkProjectService.delByFkid(id);
        }else {
            exh.setDelstate(1);
            this.whgShowExhMapper.updateByPrimaryKeySelective(exh);
            WhgFkProject whgFkProject = whgFkProjectService.srchOneByFkId(exh.getId());
            if (whgFkProject != null) {//编辑
                whgFkProject.setCultid(exh.getCultid());//文化馆id
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
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser sysUser, Date optTime, String reason, int issms) throws Exception {
        ResponseBean resb = new ResponseBean();
        Date now = new Date();
        if (ids == null){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识丢失");
            return resb;
        }

        Example example = new Example(WhgShowExh.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );

        WhgShowExh exh = new WhgShowExh();
        exh.setState(tostate);
        if (optTime==null) {
            optTime = new Date();
        }
        exh.setStatemdfdate(optTime);
        exh.setStatemdfuser(sysUser.getId());
        if(tostate == 2){
            exh.setCheckor(sysUser.getId());
            exh.setCheckdate(now);

        }else if(tostate == 6){
            exh.setPublisher(sysUser.getId());
            exh.setPublishdate(now);
        }
        try {
            if (reason != null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()) {
                List<WhgShowExh> srclist = this.whgShowExhMapper.selectByExample(example);
                if (srclist != null) {
                    for (WhgShowExh _src : srclist) {
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
        this.whgShowExhMapper.updateByExampleSelective(exh, example);
        WhgFkProject whgFkProject=whgFkProjectService.srchOneByFkId(ids.split("\\s*,\\s*")[0]);
        if(whgFkProject!=null){//修改状态
            whgFkProject.setState(tostate);
            if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()) {//待发布  审核者信息
                whgFkProject.setCheckor(sysUser.getId());
                whgFkProject.setCheckdate(new Date());
            } else if (tostate == EnumBizState.STATE_PUB.getValue()) {//已发布记录 发布者信息
                whgFkProject.setPublisher(sysUser.getId());
                whgFkProject.setPublishdate(optTime);
            }
            whgFkProjectService.t_edit(whgFkProject);
        }
        return resb;
    }


    /**
     * 还原
     * @param id
     * @param sysUser
     */
    public void t_undel(String id, WhgSysUser sysUser) {
        WhgShowExh exh = this.whgShowExhMapper.selectByPrimaryKey(id);
        if (exh == null){
            return;
        }
        if(exh.getState() == 4){
            exh.setDelstate(0);
            exh.setState(1);
            this.whgShowExhMapper.updateByPrimaryKeySelective(exh);
        }else{
            exh.setDelstate(0);
            exh.setState(1);
            this.whgShowExhMapper.updateByPrimaryKeySelective(exh);
        }

        WhgFkProject whgFkProject = null;
        try {
            whgFkProject = this.whgFkProjectService.srchOneByFkId(exh.getId());
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
