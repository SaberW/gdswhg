package com.creatoo.hn.services.admin.goods;

import com.creatoo.hn.dao.mapper.WhgGoodsMapper;
import com.creatoo.hn.dao.model.*;
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
import com.creatoo.hn.util.enums.EnumStateDel;
import com.creatoo.hn.util.enums.EnumSupplyType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/8/3.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgGoods", keyGenerator = "simpleKeyGenerator")
public class WhgGoodsService extends BaseService {

    @Autowired
    private WhgGoodsMapper whgGoodsMapper;

    @Autowired
    private WhgFkProjectService whgFkProjectService;

    @Autowired
    private WhgSupplyService whgSupplyService;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

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
     */
    @Cacheable
    public WhgGoods srchOne(String id){
        return this.whgGoodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询
     * @param page
     * @param rows
     * @param goods
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    @Cacheable
    public PageInfo srchlist4p(int page, int rows, WhgGoods goods, List states, String sort, String order, String sysUserId, Map<String, String> record) throws Exception {
        Example exp = new Example(WhgGoods.class);
        Example.Criteria ca = exp.createCriteria();

            if (goods.getName()!=null && !goods.getName().isEmpty()){
                ca.andLike("name", "%"+goods.getName()+"%");
                goods.setName(null);
            }
            if (goods.getContacts()!=null && !goods.getContacts().isEmpty()){
                ca.andLike("contacts", "%"+goods.getContacts()+"%");
                goods.setContacts(null);
            }
            if (goods.getPhone()!=null && !goods.getPhone().isEmpty()){
                ca.andLike("phone", "%"+goods.getPhone()+"%");
                goods.setPhone(null);
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
            goods.setCultid(null);
        } else {
            if (goods.getCultid() == null || goods.getCultid().isEmpty()) {
                goods.setCultid(null);
                try {
                    List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
                    if (cultids != null && cultids.size() > 0) {
                        ca.andIn("cultid", cultids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (goods.getDeptid() == null || goods.getDeptid().isEmpty()) {
                goods.setDeptid(null);
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
            ca.andEqualTo(goods);

        if (states!=null && states.size()>0){
            ca.andIn("state", states);
        }

        if (sort!=null && !sort.isEmpty()){
            Example.OrderBy orderBy = exp.orderBy(sort);
            if (order!=null && "desc".equalsIgnoreCase(order)){
                orderBy.desc();
            }
        }else{
            exp.orderBy("crtdate").desc();
        }

        PageHelper.startPage(page, rows);
        List list = this.whgGoodsMapper.selectByExample(exp);

        return new PageInfo(list);
    }

    /**
     * 添加
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_add(WhgGoods goods, WhgSysUser sysUser/*,String times*/) throws Exception{
        ResponseBean resb = new ResponseBean();

        goods.setId(IDUtils.getID());
        goods.setCrtdate(new Date());
        goods.setCrtuser(sysUser.getId());
        goods.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        goods.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        goods.setStatemdfuser(sysUser.getId());
        goods.setStatemdfdate(new Date());

        this.whgGoodsMapper.insert(goods);

        //whgSupplyService.resetSupplyTimes(goods.getId(),times);

        WhgFkProject whgFkProject=new WhgFkProject();
        whgFkProject.setDelstate(0);
        whgFkProject.setCultid(goods.getCultid());//文化馆id
        whgFkProject.setFkid(goods.getId());//关联表id
        whgFkProject.setProvince(goods.getProvince());//省
        whgFkProject.setCity(goods.getCity());//市
        whgFkProject.setArea(goods.getArea());//区
        whgFkProject.setTitle(goods.getName());//标题
        whgFkProject.setImgpath(goods.getImgurl());//图片
        whgFkProject.setPscity(goods.getPscity());//配送区域
        whgFkProject.setType(EnumSupplyType.TYPE_WYPC.getValue());//类型 展览
        whgFkProject.setPsprovince(goods.getPsprovince());//配送区域
        whgFkProject.setState(EnumBizState.STATE_CAN_EDIT.getValue());//状态
        whgFkProject.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        whgFkProject.setCreatetime(goods.getCrtdate());
        whgFkProject.setMemo(goods.getDescription());//简介
        whgFkProjectService.addStr(whgFkProject);
        return resb;
    }

    /**
     * 编辑
     * @param goods
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_edit(WhgGoods goods/*,String times*/) throws Exception{
        ResponseBean resb = new ResponseBean();
        this.whgGoodsMapper.updateByPrimaryKeySelective(goods);
        /*if (times!=null){
            whgSupplyService.resetSupplyTimes(goods.getId(),times);
        }*/

        goods = this.whgGoodsMapper.selectByPrimaryKey(goods.getId());
        WhgFkProject whgFkProject=whgFkProjectService.srchOneByFkId(goods.getId());
        if(whgFkProject!=null) {//编辑
            whgFkProject.setCultid(goods.getCultid());//文化馆id
            whgFkProject.setFkid(goods.getId());//关联表id
            whgFkProject.setProvince(goods.getProvince());//省
            whgFkProject.setCity(goods.getCity());//市
            whgFkProject.setArea(goods.getArea());//区
            whgFkProject.setTitle(goods.getName());//标题
            whgFkProject.setImgpath(goods.getImgurl());//图片
            whgFkProject.setPscity(goods.getPscity());//配送区域
            whgFkProject.setPsprovince(goods.getPsprovince());//配送区域
            whgFkProject.setMemo(goods.getDescription());//简介
            whgFkProject.setState(goods.getState());
            whgFkProject.setDelstate(goods.getDelstate());
            whgFkProjectService.t_edit(whgFkProject);
        }
        return resb;
    }

    /**
     * 删除
     * @param id
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_del(String id) throws Exception{
        ResponseBean resb = new ResponseBean();
        //whgSupplyService.clearSupplyTimes(id);
        this.whgSupplyTimesService.clearSupplyTimes4Supplyid(id, EnumSupplyType.TYPE_WYPC.getValue());
        this.whgGoodsMapper.deleteByPrimaryKey(id);
        //删除fk表中对应的数据
        this.whgFkProjectService.delByFkid(id);
        return resb;
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
            resb.setErrormsg("商品标识丢失");
            return resb;
        }

        Example example = new Example(WhgGoods.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );

        WhgGoods goods = new WhgGoods();
        goods.setState(tostate);
        if (optTime==null) {
            optTime = new Date();
        }
        goods.setStatemdfdate(optTime);
        goods.setStatemdfuser(sysUser.getId());

        if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()){
            goods.setCheckor(sysUser.getId());
            goods.setCheckdate(new Date());
        }
        if (tostate == EnumBizState.STATE_PUB.getValue()){
            goods.setPublisher(sysUser.getId());
            goods.setPublishdate(optTime);
        }

        try {
            if (reason != null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()) {
                List<WhgGoods> srclist = this.whgGoodsMapper.selectByExample(example);
                if (srclist != null) {
                    for (WhgGoods _src : srclist) {
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("文艺辅材");
                        xjr.setFktitile(_src.getName());
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
        this.whgGoodsMapper.updateByExampleSelective(goods, example);
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
}
