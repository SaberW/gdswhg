package com.creatoo.hn.services.admin.train;

import com.creatoo.hn.dao.mapper.WhgSysCultMapper;
import com.creatoo.hn.dao.mapper.WhgTraMajorMapper;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgTraMajor;
import com.creatoo.hn.dao.model.WhgXjReason;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * Created by Administrator on 2017/9/22.
 */
@SuppressWarnings("ALL")
@Service
public class WhgTraMajorService extends BaseService{

    @Autowired
    private WhgTraMajorMapper whgTraMajorMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgSysCultMapper whgSysCultMapper;

    @Autowired
    private WhgXjReasonService whgXjReasonService;

    /**
     * 根据id查询微专业
     * @param id
     * @return
     */
    public Object srchOne(String id) {
        return whgTraMajorMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询微专业
     * @param page
     * @param rows
     * @param major
     * @param sort
     * @param order
     * @return
     */
    public PageInfo t_srchlist4p(int page, int rows, WhgTraMajor major, String sort, String order, String sysUserId, String defaultState) {
        Example exp = new Example(WhgTraMajor.class);
        Example.Criteria ca = exp.createCriteria();

        //名称
        if (major.getName()!=null && !major.getName().isEmpty()){
            ca.andLike("name", "%"+major.getName()+"%");
            major.setName(null);
        }

        //状态
        if(StringUtils.isNotEmpty(defaultState) && major.getState() == null){
            major.setState(null);
            ca.andIn("state", Arrays.asList(defaultState.split(",")));
        }

        //部门和文化馆
        if (major.getCultid() == null || major.getCultid().isEmpty()) {
            major.setCultid(null);
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
                if (cultids!=null && cultids.size()>0){
                    ca.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (major.getDeptid() == null || major.getDeptid().isEmpty()) {
            major.setDeptid(null);
            try {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
                if (deptids != null && deptids.size() > 0) {
                    ca.andIn("deptid", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        //其它条件
        ca.andEqualTo(major);

        //排序
        if (sort!=null && !sort.isEmpty()){
            Example.OrderBy orderBy = exp.orderBy(sort);
            if (order!=null && "desc".equalsIgnoreCase(order)){
                orderBy.desc();
            }
        }else{
            exp.orderBy("crtdate").desc();
        }

        //分页查询
        PageHelper.startPage(page, rows);
        List list = this.whgTraMajorMapper.selectByExample(exp);
        return new PageInfo(list);
    }

    /**
     * 添加微专业
     * @param major
     * @param user
     */
    public void t_add(WhgTraMajor major, WhgSysUser user) throws Exception {
        Date now = new Date();
        major.setStatemdfdate(now);
        major.setId(IDUtils.getID());
        major.setCrtdate(now);
        major.setCrtuser(user.getId());
        major.setState(1);
        major.setStatemdfuser(user.getId());
        major.setRecommend(0);
        major.setDelstate(0);
        this.whgTraMajorMapper.insertSelective(major);
    }

    /**
     * 编辑微专业
     * @param major
     */
    public void t_edit(WhgTraMajor major) throws Exception {
        this.whgTraMajorMapper.updateByPrimaryKeySelective(major);
    }

    /**
     * 删除微专业
     * @param id
     */
    public void t_del(String id) throws Exception {
        WhgTraMajor major = whgTraMajorMapper.selectByPrimaryKey(id);
        if (major == null){
            return;
        }
        this.whgTraMajorMapper.deleteByPrimaryKey(id);
    }

    /**
     * 回收
     * @param id
     * @throws Exception
     */
    public void t_recycle(String id, int delstate)throws Exception{
        WhgTraMajor major = new WhgTraMajor();
        major.setId(id);
        major.setState(1);
        major.setDelstate(delstate);
        this.whgTraMajorMapper.updateByPrimaryKeySelective(major);
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @return
     */
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser user, String reason, int issms) {
        ResponseBean rb = new ResponseBean();
        if (ids == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("培训师资主键丢失");
            return rb;
        }
        Date now = new Date();

        Example example = new Example(WhgTraMajor.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgTraMajor major = new WhgTraMajor();
        major.setState(tostate);

        if(EnumBizState.STATE_CAN_PUB.getValue() == tostate) {//审核者-通过
            major.setCheckdate(now);
            major.setCheckor(user.getId());
        }else if(EnumBizState.STATE_CAN_EDIT.getValue() == tostate && (EnumBizState.STATE_CAN_CHECK.getValue()+"").equals(formstates)){
            //审核者-不通过
            major.setCheckdate(now);
            major.setCheckor(user.getId());
        }else if(EnumBizState.STATE_PUB.getValue() == tostate) {//发布者
            major.setPublisher(user.getId());
            major.setPublishdate(now);
        }

        major.setStatemdfdate(now);
        major.setStatemdfuser(user.getId());

        try {
            if (reason!=null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()){
                List<WhgTraMajor> srclist = this.whgTraMajorMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgTraMajor _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("微专业");
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
            log.error(e.getMessage(),e);
        }

        this.whgTraMajorMapper.updateByExampleSelective(major, example);
        return rb;
    }

    /**
     * 是否推荐
     * @param ids
     * @param formrecoms
     * @param torecom
     * @return
     */
    public ResponseBean t_updrecommend(String ids, String formrecoms, int torecom) throws Exception {
        ResponseBean res = new ResponseBean();
        if(ids == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("微专业主键丢失");
            return res;
        }
//        Example example = new Example(WhgTraMajor.class);
//        Example.Criteria c = example.createCriteria();
//        c.andEqualTo("state",3);
//        c.andEqualTo("recommend",1);
//        int result = this.whgTraMajorMapper.selectCountByExample(example);
        Example example = new Example(WhgTraMajor.class);
        Example.Criteria c1 = example.createCriteria();
        c1.andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ));
        c1.andIn("recommend", Arrays.asList( formrecoms.split("\\s*,\\s*") ));
        WhgTraMajor major = new WhgTraMajor();
        major.setRecommend(torecom);
        this.whgTraMajorMapper.updateByExampleSelective(major,example);
        return res;
    }

    /**
     * 查询微专业
     * @param cultid
     * @return
     */
    public List<WhgTraMajor> t_srchList(String cultid) {
        Example example = new Example(WhgTraMajor.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("cultid",cultid);
        c.andEqualTo("state",6);
        List<WhgTraMajor> list = this.whgTraMajorMapper.selectByExample(example);
        return list;
    }

    /**
     * 总分馆微专业查询
     * @param page
     * @param rows
     * @param major
     * @param sort
     * @param order
     * @param param
     * @return
     */
    public PageInfo t_srchSysList4p(int page, int rows, WhgTraMajor major, String sort, String order, Map<String, String> param) throws Exception {
        Example example = new Example(WhgTraMajor.class);
        Example.Criteria c = example.createCriteria();
        if (major!=null){
            //标题处理
            if (major.getName()!=null){
                c.andLike("name", "%"+major.getName()+"%");
                major.setName(null); //去除title等于条件
            }

            c.andEqualTo(major);
        }

        c.andIn("state", Arrays.asList(6,4));
        c.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

        String iscult = param.get("iscult");
        if (iscult!=null && "1".equals(iscult)){
            String cultid = param.get("syscultid");
            if (cultid == null){
                throw new Exception("文化馆ID丢失");
            }

            c.andEqualTo("cultid", cultid);
        }else{
            String level = param.get("level");
            String cultname = param.get("cultname");
            if (level==null || level.isEmpty()){
                throw new Exception("文化馆级别丢失");
            }
            if (cultname==null || cultname.isEmpty()){
                throw new Exception("cultname丢失");
            }

            Example syscultemp = new Example(WhgSysCult.class);
            Example.Criteria sc = syscultemp.createCriteria();
            sc.andEqualTo("state", EnumBizState.STATE_PUB.getValue());
            sc.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

            String areakey = "province";
            if ("2".equals(level)){
                areakey = "city";
            }else if ("3".equals(level)){
                areakey = "area";
            }
            sc.andEqualTo(areakey, cultname);

            List<String> refcultids = new ArrayList();
            List<WhgSysCult> refcults = this.whgSysCultMapper.selectByExample(syscultemp);
            if (refcults== null || refcults.size()==0){
                throw new Exception("没有找到文化馆");
            }
            for(WhgSysCult cult : refcults){
                refcultids.add(cult.getId());
            }
            //tra.setCultid(null);
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
        List<WhgTraMajor> list= this.whgTraMajorMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgTraMajor _major : list){
                bm.setBean(_major);
                Map info = new HashMap();
                info.putAll(bm);
                if (_major.getCultid()!=null ){
                    WhgSysCult sysCult = this.whgSysCultMapper.selectByPrimaryKey(_major.getCultid());
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

}
