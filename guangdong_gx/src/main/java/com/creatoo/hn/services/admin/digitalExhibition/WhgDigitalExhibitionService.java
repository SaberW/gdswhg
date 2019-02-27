package com.creatoo.hn.services.admin.digitalExhibition;

import com.creatoo.hn.dao.mapper.WhgDigitalExhibitionMapper;
import com.creatoo.hn.dao.mapper.WhgDigitalExhibitionMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
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
 * Created by Administrator on 2017/9/1.
 */
@Service
public class WhgDigitalExhibitionService extends BaseService {


    @Autowired
    private WhgDigitalExhibitionMapper whgDigitalExhibitionMapper;


    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgXjReasonService whgXjReasonService;

    /**
     * 根据id查询展演类商品信息
     *
     * @param id
     * @return
     */
    public WhgDigitalExhibition srchOne(String id) {
        return whgDigitalExhibitionMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询展演类商品列表
     *
     * @param page
     * @param rows
     * @param exh
     * @param sort
     * @param order
     * @return
     */
    public PageInfo t_srchlist4p(String type, int page, int rows, String userId, WhgDigitalExhibition exh, String sort, String order, Map<String, String> record) throws Exception {
        Example exp = new Example(WhgDigitalExhibition.class);
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
                   /* List<String> refcultids = this.whgSystemCultService.t_srchByArea(pcalevel, pcatext);
                    if (refcultids == null || refcultids.size() == 0) {
                        throw new Exception("not cults info");
                    }
                    ca.andIn("cultid", refcultids);*/
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
        if (sort != null && !sort.isEmpty()) {
            Example.OrderBy orderBy = exp.orderBy(sort);
            if (order != null && "desc".equalsIgnoreCase(order)) {
                orderBy.desc();
            }
        } else {
            exp.orderBy("crtdate").desc();
        }
        PageHelper.startPage(page, rows);
        List list = this.whgDigitalExhibitionMapper.selectByExample(exp);
        return new PageInfo(list);
    }


    /**
     * 添加展演类商品
     *
     * @param exh
     * @param sysUser
     * @return
     */
    public ResponseBean t_add(WhgDigitalExhibition exh, WhgSysUser sysUser) throws Exception {
        ResponseBean rsb = new ResponseBean();
        exh.setId(IDUtils.getID());
        exh.setCrtdate(new Date());
        exh.setDelstate(0);
        exh.setCrtuser(sysUser.getId());
        exh.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        exh.setStatemdfuser(sysUser.getId());
        exh.setStatemdfdate(new Date());
        this.whgDigitalExhibitionMapper.insert(exh);
        return rsb;
    }

    /**
     * 编辑商品
     *
     * @param exh
     * @return
     */
    public ResponseBean t_edit(WhgDigitalExhibition exh, String times) throws Exception {
        ResponseBean rsb = new ResponseBean();
        this.whgDigitalExhibitionMapper.updateByPrimaryKeySelective(exh);
        return rsb;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public ResponseBean t_del(String id) throws Exception {
        ResponseBean resb = new ResponseBean();
        WhgDigitalExhibition exh = whgDigitalExhibitionMapper.selectByPrimaryKey(id);
        if (exh.getDelstate() != null && exh.getDelstate().compareTo(new Integer(1)) == 0) {
            this.whgDigitalExhibitionMapper.deleteByPrimaryKey(id);
        } else if (exh.getState() == 1) {
            this.whgDigitalExhibitionMapper.deleteByPrimaryKey(id);
        } else {
            exh.setDelstate(1);
            this.whgDigitalExhibitionMapper.updateByPrimaryKeySelective(exh);
        }
        return resb;
    }

    /**
     * 修改状态
     *
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUser
     * @return
     */
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser sysUser, Date optTime, String reason, int issms) throws Exception {
        ResponseBean resb = new ResponseBean();
        Date now = new Date();
        if (ids == null) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识丢失");
            return resb;
        }

        Example example = new Example(WhgDigitalExhibition.class);
        example.createCriteria()
                .andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")))
                .andIn("id", Arrays.asList(ids.split("\\s*,\\s*")));

        WhgDigitalExhibition exh = new WhgDigitalExhibition();
        exh.setState(tostate);
        if (optTime == null) {
            optTime = new Date();
        }
        exh.setStatemdfdate(optTime);
        exh.setStatemdfuser(sysUser.getId());
        if (tostate == 2) {
            exh.setCheckor(sysUser.getId());
            exh.setCheckdate(now);

        } else if (tostate == 6) {
            exh.setPublisher(sysUser.getId());
            exh.setPublishdate(now);
        }
        try {
            if (reason != null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()) {
                List<WhgDigitalExhibition> srclist = this.whgDigitalExhibitionMapper.selectByExample(example);
                if (srclist != null) {
                    for (WhgDigitalExhibition _src : srclist) {
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
        this.whgDigitalExhibitionMapper.updateByExampleSelective(exh, example);
        return resb;
    }


    /**
     * 还原
     *
     * @param id
     * @param sysUser
     */
    public void t_undel(String id, WhgSysUser sysUser) {
        WhgDigitalExhibition exh = this.whgDigitalExhibitionMapper.selectByPrimaryKey(id);
        if (exh == null) {
            return;
        }
        if (exh.getState() == 4) {
            exh.setDelstate(0);
            exh.setState(1);
            this.whgDigitalExhibitionMapper.updateByPrimaryKeySelective(exh);
        } else {
            exh.setDelstate(0);
            exh.setState(1);
            this.whgDigitalExhibitionMapper.updateByPrimaryKeySelective(exh);
        }
    }
}
