package com.creatoo.hn.services.admin.train;

import com.creatoo.hn.dao.mapper.WhgMajorContactMapper;
import com.creatoo.hn.dao.mapper.WhgSysCultMapper;
import com.creatoo.hn.dao.mapper.WhgTraTeacherMapper;
import com.creatoo.hn.dao.model.*;
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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 培训师资管理Service
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/4/20.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames="WhgTraTeacher", keyGenerator = "simpleKeyGenerator")
public class WhgTrainTeacherService extends BaseService{
    /**
     * 培训师资mapper
     */
    @Autowired
    private WhgTraTeacherMapper teacherMapper;

    @Autowired
    private WhgMajorContactMapper whgMajorContactMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    @Autowired
    private WhgSysCultMapper whgSysCultMapper;

    @Autowired
    private WhgXjReasonService whgXjReasonService;

    /**
     * 查询培训师资
     * @param
     * @return
     */
    @Cacheable
    public PageInfo t_srchList4p(int page,int rows,WhgTraTeacher tea,String sort,String order,String sysUserId, String defaultState) throws Exception {
        Example example = new Example(WhgTraTeacher.class);
        Example.Criteria c = example.createCriteria();
        if (tea.getName()!=null){
            c.andLike("name", "%"+tea.getName()+"%");
            tea.setName(null); //去除title等于条件
        }

        //状态
        if(StringUtils.isNotEmpty(defaultState) && tea.getState() == null){
            tea.setState(null);
            c.andIn("state", Arrays.asList(defaultState.split(",")));
        }

        //文化馆和部门
        if (tea.getCultid() == null || tea.getCultid().isEmpty()) {
            tea.setCultid(null);
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
                if (cultids!=null && cultids.size()>0){
                    c.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (tea.getDeptid() == null || tea.getDeptid().isEmpty()) {
            tea.setDeptid(null);
            try {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
                if (deptids != null && deptids.size() > 0) {
                    c.andIn("deptid", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        c.andEqualTo(tea);

        //排序
        if (sort!=null && !sort.isEmpty()){
            if (order!=null && "asc".equalsIgnoreCase(order)){
                example.orderBy(sort).asc();
            }else{
                example.orderBy(sort).desc();
            }
        }else{
            example.setOrderByClause(" crtdate desc");
        }

        //带条件的分页查询
        PageHelper.startPage(page, rows);
        List<WhgTraTeacher> list = this.teacherMapper.selectByExample(example);
        // 取分页信息
        PageInfo<WhgTraTeacher> pageInfo = new PageInfo<WhgTraTeacher>(list);

        return pageInfo;
    }

    /**
     * 添加培训师资
     * @param tea
     * @param user
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgTraTeacher tea, WhgSysUser user,String mid,String[] majorid) throws Exception {
        Date now = new Date();
        tea.setStatemdfdate(now);
        tea.setId(IDUtils.getID());
        tea.setCrtuser(user.getId());
        tea.setCrtdate(now);
        tea.setState(1);
        tea.setStatemdfuser(user.getId());
        tea.setDelstate(0);
        this.teacherMapper.insertSelective(tea);
        if(mid != null && !"".equals(mid)){
            WhgMajorContact contact = new WhgMajorContact();
            contact.setId(IDUtils.getID());
            contact.setEntid(tea.getId());
            contact.setMajorid(mid);
            contact.setType(2);
            this.whgMajorContactMapper.insertSelective(contact);
        }
        if(majorid != null && majorid.length > 0){
            WhgMajorContact contact = new WhgMajorContact();
            for (int m = 0;m < majorid.length;m++){
                contact.setId(IDUtils.getID());
                contact.setEntid(tea.getId());
                contact.setMajorid(majorid[m]);
                contact.setType(2);
                this.whgMajorContactMapper.insertSelective(contact);
            }
        }
    }

    @CacheEvict(allEntries = true)
    public void t_edit4Pk(WhgTraTeacher info) throws Exception{
        this.teacherMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 编辑培训师资
     * @param tea
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgTraTeacher tea,String[] majorid) throws Exception {
        this.teacherMapper.updateByPrimaryKeySelective(tea);
        WhgMajorContact contact = new WhgMajorContact();
        Example majorexample = new Example(WhgMajorContact.class);
        Example.Criteria majorc = majorexample.createCriteria();
        majorc.andEqualTo("entid",tea.getId());
        List<WhgMajorContact> mclist = whgMajorContactMapper.selectByExample(majorexample);
        for(int c = 0; c<mclist.size();c++){
            whgMajorContactMapper.deleteByPrimaryKey(mclist.get(c).getId());
        }
        if(majorid != null && majorid.length > 0){
            for (int m = 0;m < majorid.length;m++){
                if(mclist == null || mclist.size() == 0){
                    contact.setId(IDUtils.getID());
                    contact.setEntid(tea.getId());
                    contact.setMajorid(majorid[m]);
                    contact.setType(2);
                    this.whgMajorContactMapper.insertSelective(contact);
                }else {
                    contact.setId(IDUtils.getID());
                    contact.setEntid(tea.getId());
                    contact.setMajorid(majorid[m]);
                    contact.setType(2);
                    this.whgMajorContactMapper.insertSelective(contact);
                }
            }
        }
    }

    /**
     * 删除培训师资
     * @param id
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception {
        WhgTraTeacher tea = teacherMapper.selectByPrimaryKey(id);
        if (tea == null){
            return;
        }
        this.teacherMapper.deleteByPrimaryKey(id);
    }

    /**
     * 回收
     * @param id
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_recycle(String id, int delstate)throws Exception{
        WhgTraTeacher tea = new WhgTraTeacher();
        tea.setId(id);
        tea.setState(1);
        tea.setDelstate(delstate);
        this.teacherMapper.updateByPrimaryKeySelective(tea);
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser user, String reason, int issms) {
        ResponseBean rb = new ResponseBean();
        if (ids == null){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("培训师资主键丢失");
            return rb;
        }
        Date now = new Date();
        Example example = new Example(WhgTraTeacher.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );
        WhgTraTeacher tea = new WhgTraTeacher();
        tea.setState(tostate);
        if(EnumBizState.STATE_CAN_PUB.getValue() == tostate) {//审核-通过
            tea.setCheckdate(now);
            tea.setCheckor(user.getId());
        }else if(EnumBizState.STATE_CAN_EDIT.getValue() == tostate && (EnumBizState.STATE_CAN_CHECK.getValue()+"").equals(formstates)){
            tea.setCheckdate(now);
            tea.setCheckor(user.getId());
        }else if(EnumBizState.STATE_PUB.getValue() == tostate) {//发布
            tea.setPublisher(user.getId());
            tea.setPublishdate(now);
        }
        tea.setStatemdfdate(new Date());
        tea.setStatemdfuser(user.getId());

        try {
            if (reason!=null && !reason.isEmpty() && tostate == EnumBizState.STATE_NO_PUB.getValue()){
                List<WhgTraTeacher> srclist = this.teacherMapper.selectByExample(example);
                if (srclist!=null){
                    for (WhgTraTeacher _src : srclist){
                        WhgXjReason xjr = new WhgXjReason();
                        xjr.setFkid(_src.getId());
                        xjr.setFktype("培训师资");
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

        this.teacherMapper.updateByExampleSelective(tea, example);
        return rb;
    }

    /**
     * 根据ID查找
     * @param teacherid
     * @return
     */
    @Cacheable
    public Object srchOne(String teacherid) {
        return teacherMapper.selectByPrimaryKey(teacherid);
    }

    /**
     * 查询所属文化馆老师列表
     * @param cultid
     * @return
     */
    @Cacheable
    public List<WhgTraTeacher> t_srchList(String cultid) {
        Example example = new Example(WhgTraTeacher.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("cultid",cultid);
        c.andEqualTo("state",6);
        List<WhgTraTeacher> list = this.teacherMapper.selectByExample(example);
        return list;
    }

    /**
     * 分页查询已发布的培训
     * @return
     */
    public PageInfo t_srchTeaList(int page,int rows,String mid,String cultid) {

        PageHelper.startPage(page,rows);
        List list = teacherMapper.t_srchTeaList(mid,cultid);
        return new PageInfo(list);
    }

    /**
     * 总分馆分页查询培训师资
     * @param page
     * @param rows
     * @param teacher
     * @param sort
     * @param order
     * @param param
     * @return
     */
    public PageInfo t_srchSysList4p(int page, int rows, WhgTraTeacher teacher, String sort, String order, Map<String, String> param)throws Exception {
        Example example = new Example(WhgTraTeacher.class);
        Example.Criteria c = example.createCriteria();
        if (teacher!=null){
            //标题处理
            if (teacher.getName()!=null){
                c.andLike("name", "%"+teacher.getName()+"%");
                teacher.setName(null); //去除title等于条件
            }

            c.andEqualTo(teacher);
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
        List<WhgTraTeacher> list= this.teacherMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        List restList = new ArrayList();
        if (list!=null){
            BeanMap bm = new BeanMap();
            for(WhgTraTeacher _teacher : list){
                bm.setBean(_teacher);
                Map info = new HashMap();
                info.putAll(bm);
                if (_teacher.getCultid()!=null ){
                    WhgSysCult sysCult = this.whgSysCultMapper.selectByPrimaryKey(_teacher.getCultid());
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
