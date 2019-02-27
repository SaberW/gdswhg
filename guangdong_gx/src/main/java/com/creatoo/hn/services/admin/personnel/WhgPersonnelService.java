package com.creatoo.hn.services.admin.personnel;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgPersonnelMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.services.admin.supply.WhgSupplyService;
import com.creatoo.hn.services.admin.supply.WhgSupplyTimesService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumConsoleSystem;
import com.creatoo.hn.util.enums.EnumSupplyType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 专家人才 service
 * Created by LENUVN on 2017/7/27.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgPersonnel", keyGenerator = "simpleKeyGenerator")
public class WhgPersonnelService extends BaseService {


    @Autowired
    private WhgPersonnelMapper whgPersonnelMapper;

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
     * 分页查询分类列表信息
     *
     * @param request。
     */
    /*@Cacheable
    public PageInfo<WhgPersonnel> t_srchList4p(HttpServletRequest request, WhgPersonnel cult) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgPersonnel.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (cult != null && cult.getName() != null) {
            c.andLike("name", "%" + cult.getName() + "%");
            cult.setName(null);
        }
        if (cult.getCultid() == null || cult.getCultid().isEmpty()) {
            cult.setCultid(null);
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
                if (cultids!=null && cultids.size()>0){
                    c.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (cult.getDeptid() == null || cult.getDeptid().isEmpty()) {
            cult.setDeptid(null);
            try {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUser.getId());
                if (deptids != null && deptids.size() > 0) {
                    c.andIn("deptid", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        String pageType = request.getParameter("type");
        //编辑列表
        if ("edit".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(1,5));
        }
        //审核列表，查 9待审核
        if ("check".equalsIgnoreCase(pageType)){
            c.andEqualTo("state", 9);
        }
        //发布列表，查 2待发布 6已发布 4已下架
        if ("publish".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(2,6,4));
        }
        //删除列表，查已删除 否则查未删除的
        if ("recycle".equalsIgnoreCase(pageType)){
            c.andEqualTo("isdel", 1);
        }else{
            c.andEqualTo("isdel", 0);
        }
        if(request.getParameter("state") != null){
            int state = Integer.parseInt(request.getParameter("state"));
            c.andEqualTo("state", state);
        }

        example.setOrderByClause("crtdate desc");

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgPersonnel> typeList = this.whgPersonnelMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }*/

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @SuppressWarnings("all")
    @Cacheable
    public PageInfo<WhgPersonnel> t_srchList4p(HttpServletRequest request, WhgPersonnel cult, String pageType, WhgSysUser sysUser, Map<String, String> record) throws Exception {

        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgPersonnel.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (cult != null && cult.getName() != null) {
            c.andLike("name", "%" + cult.getName() + "%");
            cult.setName(null);
        }
        WhgSysUser user = whgSystemUserService.t_srchOne(sysUser.getId());
        if (user != null && user.getAdmintype() != null && user.getAdmintype().equals(EnumConsoleSystem.sysmgr.getValue())) {//区域管理员
            String iscult = record.get("iscult");
            if (iscult != null && "1".equals(iscult)) {
                String cultid = record.get("refid");
                if (cultid == null) {
                    throw new Exception("cultid is not null");
                }
                c.andEqualTo("cultid", cultid);
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
                c.andIn("cultid", refcultids);
            }
            cult.setCultid(null);
        } else {
            if (cult.getCultid() == null || cult.getCultid().isEmpty()) {
                cult.setCultid(null);
                try {
                    List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
                    if (cultids != null && cultids.size() > 0) {
                        c.andIn("cultid", cultids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (cult.getDeptid() == null || cult.getDeptid().isEmpty()) {
                cult.setDeptid(null);
                try {
                    List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUser.getId());
                    if (deptids != null && deptids.size() > 0) {
                        c.andIn("deptid", deptids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        //编辑列表
        if ("edit".equalsIgnoreCase(pageType)){
            c.andEqualTo("crtuser", sysUser.getId());
        }
        //总分馆发布列表，查 6已发布 4已下架
        if ("syslistpublish".equalsIgnoreCase(pageType)) {
            c.andIn("state", Arrays.asList(6, 4));
        }

        //删除列表，查已删除 否则查未删除的
        if ("recycle".equalsIgnoreCase(pageType)){
            c.andEqualTo("delstate", 1);
        }else{
            c.andEqualTo("delstate", 0);
        }
        if(request.getParameter("state") != null){
            int state = Integer.parseInt(request.getParameter("state"));
            c.andEqualTo("state", state);
        } else {
            if (!"edit".equalsIgnoreCase(pageType)) {
                c.andIn("state", Arrays.asList(9, 2, 6, 4));
            }
        }
        cult.setType(null);
        c.andEqualTo(cult);
        example.setOrderByClause("crtdate desc");

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgPersonnel> typeList = this.whgPersonnelMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 查询单条记录
     *
     * @param id id
     * @return 对象
     * @throws Exception
     */
    @Cacheable
    public WhgPersonnel t_srchOne(String id) throws Exception {
        return this.whgPersonnelMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加
     *
     * @param cultHeritage .
     */
    @CacheEvict(allEntries = true)
    public void t_add(HttpServletRequest request, WhgPersonnel cultHeritage/*,String times*/) throws Exception {
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        //cultHeritage.setId(commService.getKey("whg_cult_heritage"));在外面赋值
        cultHeritage.setCrtdate(new Date());
        cultHeritage.setIsrecommend(0);
        cultHeritage.setDelstate(0);
        cultHeritage.setStatemdfdate(new Date());
        cultHeritage.setStatemdfuser(user.getId());
        cultHeritage.setCrtuser(user.getId());
        cultHeritage.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        int result = this.whgPersonnelMapper.insertSelective(cultHeritage);

        //whgSupplyService.resetSupplyTimes(cultHeritage.getId(),times);
        WhgFkProject whgFkProject=new WhgFkProject();
        whgFkProject.setDelstate(0);
        whgFkProject.setCultid(cultHeritage.getCultid());//文化馆id
        whgFkProject.setFkid(cultHeritage.getId());//关联表id
        whgFkProject.setProvince(cultHeritage.getProvince());//省
        whgFkProject.setCity(cultHeritage.getCity());//市
        whgFkProject.setArea(cultHeritage.getArea());//区
        whgFkProject.setTitle(cultHeritage.getName());//标题
        whgFkProject.setImgpath(cultHeritage.getPicture());//图片
        whgFkProject.setPscity(cultHeritage.getPscity());//配送区域
        whgFkProject.setType(EnumSupplyType.TYPE_WYZJ.getValue());//类型 展览
        whgFkProject.setPsprovince(cultHeritage.getPsprovince());//配送区域
        whgFkProject.setState(EnumBizState.STATE_CAN_EDIT.getValue());//状态
        whgFkProject.setMemo(cultHeritage.getSummary());//简介
        whgFkProjectService.addStr(whgFkProject);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑
     *
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgPersonnel cultHeritage/*,String times*/) throws Exception {
        cultHeritage.setStatemdfdate(new Date());
        int result = this.whgPersonnelMapper.updateByPrimaryKeySelective(cultHeritage);
        if (result != 1) {
            throw new Exception("编辑数据失败！");
        }
        //whgSupplyService.resetSupplyTimes(cultHeritage.getId(),times);
        WhgFkProject whgFkProject=whgFkProjectService.srchOneByFkId(cultHeritage.getId());
        if(whgFkProject!=null) {//编辑
            whgFkProject.setCultid(cultHeritage.getCultid());//文化馆id
            whgFkProject.setFkid(cultHeritage.getId());//关联表id
            whgFkProject.setProvince(cultHeritage.getProvince());//省
            whgFkProject.setCity(cultHeritage.getCity());//市
            whgFkProject.setArea(cultHeritage.getArea());//区
            whgFkProject.setTitle(cultHeritage.getName());//标题
            whgFkProject.setImgpath(cultHeritage.getPicture());//图片
            whgFkProject.setPscity(cultHeritage.getPscity());//配送区域
            whgFkProject.setPsprovince(cultHeritage.getPsprovince());//配送区域
            whgFkProjectService.t_edit(whgFkProject);
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception {
        WhgPersonnel cultHeritage = whgPersonnelMapper.selectByPrimaryKey(id);
        if (cultHeritage == null) {
            return;
        }
        this.whgSupplyTimesService.clearSupplyTimes4Supplyid(id, EnumSupplyType.TYPE_WYZJ.getValue());
        this.whgPersonnelMapper.deleteByPrimaryKey(id);
            //删除fk表中对应的数据
        this.whgFkProjectService.delByFkid(id);
    }

    /**
     * 回收
     *
     * @param ids 活动ID，多个用逗号分隔
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updDelstate(String id) throws Exception {
        if (id != null) {
            WhgPersonnel cultHeritage = whgPersonnelMapper.selectByPrimaryKey(id);
            if (cultHeritage == null) {
                return;
            }
            cultHeritage = new WhgPersonnel();
            cultHeritage.setId(id);
            cultHeritage.setDelstate(1);
            this.whgPersonnelMapper.updateByPrimaryKeySelective(cultHeritage);
            WhgFkProject whgFkProject = whgFkProjectService.srchOneByFkId(cultHeritage.getId());
            if (whgFkProject != null) {//编辑
                whgFkProject.setCultid(cultHeritage.getCultid());//文化馆id
                whgFkProject.setFkid(id);
                whgFkProject.setDelstate(1);
                this.whgFkProjectService.t_edit(whgFkProject);
            }
        }
    }
    /**
     * 还原
     *
     * @param id
     */
    @CacheEvict(allEntries = true)
    public void t_undel(String id) {
        WhgPersonnel cultHeritage = whgPersonnelMapper.selectByPrimaryKey(id);
        if (cultHeritage == null) {
            return;
        }
        cultHeritage = new WhgPersonnel();
        cultHeritage.setId(id);
        cultHeritage.setDelstate(0);
        cultHeritage.setState(EnumBizState.STATE_CAN_EDIT.getValue());//还原 撤销 回收 打回编辑列表 重走流程
        this.whgPersonnelMapper.updateByPrimaryKeySelective(cultHeritage);
        WhgFkProject whgFkProject = null;
        try {
            whgFkProject = this.whgFkProjectService.srchOneByFkId(cultHeritage.getId());
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


    /**
     * 修改状态
     *
     * @param ids        用逗号分隔的多个ID
     * @param formstates 修改之前的状态
     * @param tostate    修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser user, String reason, int issms) throws Exception {
        ResponseBean rb = new ResponseBean();
        if (ids == null) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("主键丢失");
            return rb;
        }
        Example example = new Example(WhgPersonnel.class);
        example.createCriteria()
                .andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")))
                .andIn("id", Arrays.asList(ids.split("\\s*,\\s*")));
        WhgPersonnel cultHeritage = new WhgPersonnel();
        cultHeritage.setState(tostate);
        if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()) {//待发布  审核者信息
            cultHeritage.setCheckor(user.getId());
            cultHeritage.setCheckdate(new Date());
        } else if (tostate == EnumBizState.STATE_PUB.getValue()) {//已发布记录 发布者信息
            cultHeritage.setPublisher(user.getId());
            cultHeritage.setPublishdate(new Date());
        }
        cultHeritage.setStatemdfdate(new Date());
        cultHeritage.setStatemdfuser(user.getId());
        try {
            if (reason != null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()) {
                List<WhgPersonnel> srclist = this.whgPersonnelMapper.selectByExample(example);
                if (srclist != null) {
                    for (WhgPersonnel _src : srclist) {
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("供需场馆");
                        xjr.setFktitile(_src.getName());
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
            log.error(e.getMessage(), e);
        }


        this.whgPersonnelMapper.updateByExampleSelective(cultHeritage, example);
        WhgFkProject whgFkProject=whgFkProjectService.srchOneByFkId(ids.split("\\s*,\\s*")[0]);
        if(whgFkProject!=null){//修改状态
            whgFkProject.setState(tostate);
            if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()) {//待发布  审核者信息
                whgFkProject.setCheckor(user.getId());
                whgFkProject.setCheckdate(new Date());
            } else if (tostate == EnumBizState.STATE_PUB.getValue()) {//已发布记录 发布者信息
                whgFkProject.setPublisher(user.getId());
                whgFkProject.setPublishdate(new Date());
            }
            whgFkProjectService.t_edit(whgFkProject);
        }
        return rb;
    }

    /**
     * 更新推荐状态
     * @param ids 活动ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public int t_updCommend(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            WhgPersonnel record = whgPersonnelMapper.selectByPrimaryKey(ids);
            record.setIsrecommend(Integer.parseInt(toState));
            if(Integer.parseInt(toState) == 1){
                Date now = new Date();
                record.setStatemdfdate(now);
            }
            record.setStatemdfuser(user.getId());
            return this.whgPersonnelMapper.updateByPrimaryKey(record);
        }else {
            return 0;
        }
    }

    /**
     * 查询人才列表
     * @return
     */
    public List<WhgPersonnel> t_srchList( String cultid) {
        Example example = new Example(WhgPersonnel.class);
        Example.Criteria c = example.createCriteria();
//        if(cultids != null && cultids.size() != 0){
//            c.andIn("cultid",cultids);
//        }
        c.andEqualTo("cultid",cultid);
        c.andEqualTo("state",6);
        return this.whgPersonnelMapper.selectByExample(example);
    }
}
