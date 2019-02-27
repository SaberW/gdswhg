package com.creatoo.hn.services.admin.goods;

import com.creatoo.hn.dao.mapper.WhgShowGoodsMapper;
import com.creatoo.hn.dao.model.WhgFkProject;
import com.creatoo.hn.dao.model.WhgShowGoods;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgXjReason;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.services.admin.supply.WhgSupplyTimesService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumConsoleSystem;
import com.creatoo.hn.util.enums.EnumSupplyType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/25.
 */
@Service
public class WhgShowGoodsService extends BaseService{

    @Autowired
    private WhgShowGoodsMapper whgShowGoodsMapper;

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
    public WhgShowGoods srchOne(String id) throws Exception{
        return whgShowGoodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询展演类商品列表
     * @param page
     * @param rows
     * @param show
     * @param sort
     * @param order
     * @return
     */
    public PageInfo t_srchlist4p(String type, int page, int rows, WhgShowGoods show, String sort, String order, String sysUserId, Map<String, String> record) throws Exception {
        Example exp = new Example(WhgShowGoods.class);
        Example.Criteria ca = exp.createCriteria();

        if ("edit".equalsIgnoreCase(type)){
            ca.andIn("state", Arrays.asList(1,2,6,9,4));
            ca.andEqualTo("crtuser",sysUserId);
        }
        //审核列表，查 9待审核
        if ("check".equalsIgnoreCase(type)){
            ca.andIn("state", Arrays.asList(2,6,9,4));
            show.setCrtuser(null);
        }
        //发布列表，查 2待发布 6已发布 4已下架
        if ("publish".equalsIgnoreCase(type)){
            ca.andIn("state",  Arrays.asList(2,6,9,4));
            show.setCrtuser(null);
        }
        //总分馆发布列表，查 6已发布 4已下架
        if ("syslistpublish".equalsIgnoreCase(type)) {
            ca.andIn("state", Arrays.asList(6, 4));
            show.setCrtuser(null);
        }
        //删除列表，查已删除 否则查未删除的
        if ("del".equalsIgnoreCase(type)){
            ca.andEqualTo("delstate", 1);
        }else{
            ca.andEqualTo("delstate", 0);
        }

        if (show.getTitle()!=null && !show.getTitle().isEmpty()){
            ca.andLike("title", "%"+show.getTitle()+"%");
            show.setTitle(null);
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
            show.setCultid(null);
        } else {
            if (show.getCultid() == null || "".equals(show.getCultid())) {
                show.setCultid(null);
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
                if (cultids != null && cultids.size() > 0) {
                    ca.andIn("cultid", cultids);
                }
            }
            if (show.getDeptid() == null || "".equals(show.getDeptid())) {
                show.setDeptid(null);
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
                if (deptids != null && deptids.size() > 0) {
                    ca.andIn("deptid", deptids);
                }
            }
        }
        ca.andEqualTo(show);
        if (sort!=null && !sort.isEmpty()){
            Example.OrderBy orderBy = exp.orderBy(sort);
            if (order!=null && "desc".equalsIgnoreCase(order)){
                orderBy.desc();
            }
        }else{
            exp.orderBy("crtdate").desc();
        }
        PageHelper.startPage(page, rows);
        List list = this.whgShowGoodsMapper.selectByExample(exp);
        return new PageInfo(list);
    }

    /**
     * 添加展演类商品
     * @param show
     * @param sysUser
     * @return
     */
    public ResponseBean t_add(WhgShowGoods show, WhgSysUser sysUser/*,String times*/)throws Exception {
        ResponseBean rsb = new ResponseBean();
        show.setId(IDUtils.getID());
        show.setCrtdate(new Date());
        show.setCrtuser(sysUser.getId());
        show.setDelstate(0);
        show.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        show.setStatemdfuser(sysUser.getId());
        show.setStatemdfdate(new Date());
        this.whgShowGoodsMapper.insert(show);
        //whgSupplyService.resetSupplyTimes(show.getId(),times);
        WhgFkProject whgFkProject=new WhgFkProject();
        whgFkProject.setDelstate(0);
        whgFkProject.setCultid(show.getCultid());//文化馆id
        whgFkProject.setFkid(show.getId());//关联表id
        whgFkProject.setProvince(show.getProvince());//省
        whgFkProject.setCity(show.getCity());//市
        whgFkProject.setArea(show.getArea());//区
        whgFkProject.setTitle(show.getTitle());//标题
        whgFkProject.setImgpath(show.getImage());//图片
        whgFkProject.setPscity(show.getPscity());//配送区域
        whgFkProject.setType(EnumSupplyType.TYPE_YCJM.getValue());//类型 展览
        whgFkProject.setPsprovince(show.getPsprovince());//配送区域
        whgFkProject.setState(EnumBizState.STATE_CAN_EDIT.getValue());//状态
        whgFkProject.setMemo(show.getShowdesc());//简介
        whgFkProjectService.addStr(whgFkProject);
        return rsb;
    }

    /**
     * 编辑商品
     * @return
     */
    public ResponseBean t_edit(WhgShowGoods exh/*,String times*/) throws Exception{
        ResponseBean rsb = new ResponseBean();
        this.whgShowGoodsMapper.updateByPrimaryKeySelective(exh);
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
            whgFkProject.setMemo(exh.getShowexplain());//简介
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
        WhgShowGoods goods = whgShowGoodsMapper.selectByPrimaryKey(id);
        if (goods.getDelstate()!=null && goods.getDelstate().compareTo(new Integer(1))==0 ){
            this.whgSupplyTimesService.clearSupplyTimes4Supplyid(id, EnumSupplyType.TYPE_YCJM.getValue());
            this.whgShowGoodsMapper.deleteByPrimaryKey(id);
            whgFkProjectService.delByFkid(id);
        }else if(goods.getState() == 1){
            this.whgSupplyTimesService.clearSupplyTimes4Supplyid(id, EnumSupplyType.TYPE_YCJM.getValue());
            this.whgShowGoodsMapper.deleteByPrimaryKey(id);
            whgFkProjectService.delByFkid(id);
        }else {
            goods.setDelstate(1);
            this.whgShowGoodsMapper.updateByPrimaryKeySelective(goods);
            WhgFkProject whgFkProject = whgFkProjectService.srchOneByFkId(goods.getId());
            if (whgFkProject != null) {//编辑
                whgFkProject.setCultid(goods.getCultid());//文化馆id
                whgFkProject.setFkid(id);
                whgFkProject.setDelstate(1);
                this.whgFkProjectService.t_edit(whgFkProject);
            }
        }
//        this.whgShowGoodsMapper.deleteByPrimaryKey(id);
//        whgFkProjectService.delByFkid(id);
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

        Example example = new Example(WhgShowGoods.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );

        WhgShowGoods show = new WhgShowGoods();
        show.setState(tostate);
        if (optTime==null) {
            optTime = new Date();
        }
        show.setStatemdfdate(optTime);
        show.setStatemdfuser(sysUser.getId());
        if(tostate == 2){
            show.setCheckor(sysUser.getId());
            show.setCheckdate(now);

        }else if(tostate == 6){
            show.setPublisher(sysUser.getId());
            show.setPublishdate(now);
        }

        try {
            if (reason != null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()) {
                List<WhgShowGoods> srclist = this.whgShowGoodsMapper.selectByExample(example);
                if (srclist != null) {
                    for (WhgShowGoods _src : srclist) {
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

        this.whgShowGoodsMapper.updateByExampleSelective(show, example);
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
     * 还原文艺演出
     * @param id
     * @param sysUser
     */
    public void t_undel(String id, WhgSysUser sysUser) {
        WhgShowGoods show = this.whgShowGoodsMapper.selectByPrimaryKey(id);
        if (show == null){
            return;
        }
        if(show.getState() == 4){
            show.setDelstate(0);
            show.setState(1);
            this.whgShowGoodsMapper.updateByPrimaryKeySelective(show);
        }else{
            show.setDelstate(0);
            show.setState(1);
            this.whgShowGoodsMapper.updateByPrimaryKeySelective(show);
        }
        WhgFkProject whgFkProject = null;
        try {
            whgFkProject = this.whgFkProjectService.srchOneByFkId(show.getId());
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
