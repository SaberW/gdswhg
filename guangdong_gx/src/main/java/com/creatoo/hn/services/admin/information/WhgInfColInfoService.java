package com.creatoo.hn.services.admin.information;

import com.creatoo.hn.dao.mapper.WhgInfColinfoMapper;
import com.creatoo.hn.dao.model.WhgInfColinfo;
import com.creatoo.hn.dao.model.WhgInfColumn;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.security.auth.Subject;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by LENUVN on 2017/7/25.
 */
@Service
@CacheConfig(cacheNames = "WhgInfColinfo", keyGenerator = "simpleKeyGenerator")
public class WhgInfColInfoService extends BaseService {

    @Autowired
    private WhgInfColinfoMapper whgInfColinfoMapper;

    @Autowired
    private WhgInfColumnService whgInfColumnService;


    @Autowired
    private WhgSystemUserService whgSystemUserService;

    public Object srchOne(String id) throws Exception{
        return this.whgInfColinfoMapper.selectByPrimaryKey(id);
    }
    @CacheEvict(allEntries = true)
    public void t_add(WhgInfColinfo info, WhgSysUser user) throws Exception{
        info.setTotop(0);//默认不置顶
        info.setUpindex(0); //默认不上首页
        info.setClnfopttime(new Date());
        //info.setClnfcrttime(new Date());
        info.setCrtuser(user.getId());
        info.setDelstate(0);
        info.setClnfstata(EnumBizState.STATE_CAN_EDIT.getValue());
        //循环添加
        String[] clnftypes=info.getClnftype().split(",");
        for(int i=0;i<clnftypes.length;i++){
            info.setClnfid(IDUtils.getID());
            info.setClnftype(clnftypes[i]);
            this.whgInfColinfoMapper.insert(info);
        }
    }
    @CacheEvict(allEntries = true)
    public void t_edit(WhgInfColinfo info, WhgSysUser user) throws Exception{
        if (info.getClnfstata() != null) {
            info.setClnfopttime(new Date());
        }
        this.whgInfColinfoMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 查询
     * @param param
     * @return
     */
    @Cacheable
    public Map<String, Object> selein(Map<String, Object> param, WhgSysUser sysUser) {
        //分页信息
        int page = Integer.parseInt((String)param.get("page"));
        int rows = Integer.parseInt((String)param.get("rows"));

        PageHelper.startPage(page, rows);
        Example example = new Example(WhgInfColinfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (sysUser != null) {
            if(param.get("cultid")== null) {
                try {
                    List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
                    if (cultids != null && cultids.size() > 0) {
                        criteria.andIn("clnvenueid", cultids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }else{
                criteria.andEqualTo("clnvenueid", (String)param.get("cultid"));
            }
            if( param.get("deptid")== null) {
                try {
                    List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUser.getId());
                    if (deptids != null && deptids.size() > 0) {
                        criteria.andIn("deptid", deptids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }else{
                criteria.andEqualTo("deptid", (String)param.get("deptid"));
            }
        }

        if (param.containsKey("userid") && param.get("userid") != null) {
            criteria.andEqualTo("crtuser", (String) param.get("userid"));
        }

        //标题
        if(param.containsKey("clnftltle") && param.get("clnftltle") != null){
            String clnftltle = (String)param.get("clnftltle");
            if(!"".equals(clnftltle.trim())){
                criteria.andLike("clnftltle", "%"+clnftltle.trim()+"%");
            }
        }
        //状态
        if(param.containsKey("clnfstata") && param.get("clnfstata") != null){
            criteria.andIn("clnfstata", (List) param.get("clnfstata"));
        }
//		example.setOrderByClause("clnfcrttime desc");
        //栏目
        if(param.containsKey("clnftype") && param.get("clnftype") != null){
            String clnftype = (String)param.get("clnftype");
            if(!"".equals(clnftype.trim())){
                criteria.andEqualTo("clnftype", clnftype);
            }
        }
        if (param.containsKey("delstate") && param.get("delstate") != null) {
            Integer delstate = (Integer) param.get("delstate");
            criteria.andEqualTo("delstate", delstate);
        } else {
            criteria.andEqualTo("delstate", 0);
        }
        //排序
        if (param.containsKey("sort") && param.get("sort") != null) {
            example.setOrderByClause("totop desc,clnfcrttime desc");
        } else {
            example.setOrderByClause("clnfcrttime desc");
        }

        List<WhgInfColinfo> list = this.whgInfColinfoMapper.selectByExample(example);

        PageInfo<WhgInfColinfo> pageInfo = new PageInfo<WhgInfColinfo>(list);
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("total", pageInfo.getTotal());
        rtnMap.put("rows", pageInfo.getList());
        return rtnMap;
    }

    /**
     * 前端查询列表
     * @param param
     * @return
     */
    public PageInfo selfrontlist(Map<String, Object> param) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //分页信息
        int page = Integer.parseInt((String)param.get("page"));
        int rows = Integer.parseInt((String)param.get("rows"));
        Example example = new Example(WhgInfColinfo.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria2 = example.createCriteria();
        Example.Criteria criteria3 = example.createCriteria();
        if(param.containsKey("cultid") && param.get("cultid")!= null) {
            criteria.andIn("clnvenueid",(List)param.get("cultid")) ;
            criteria2.andIn("clnvenueid",(List)param.get("cultid")) ;
            criteria3.andIn("clnvenueid",(List)param.get("cultid")) ;
        }

        if(param.containsKey("deptids") && param.get("deptids")!= null) {
            criteria.andIn("deptid",(List)param.get("deptids")) ;
            criteria2.andIn("deptid",(List)param.get("deptids")) ;
            criteria3.andIn("deptid",(List)param.get("deptids")) ;
        }
        //排序
        if(param.containsKey("sort") && param.get("sort")!= null){
            StringBuffer sbf = new StringBuffer((String)param.get("sort"));
            if(param.containsKey("order") && param.get("order") !=  null){
                sbf.append(" ").append(param.get("order"));
            }
            example.setOrderByClause(sbf.toString());
        }else {
            example.setOrderByClause("clnfcrttime desc");
        }
        //状态
        if(param.containsKey("clnfstata") && param.get("clnfstata") != null){
            int clnftltle = (Integer)param.get("clnfstata");
            criteria.andEqualTo("clnfstata",clnftltle);
            criteria2.andEqualTo("clnfstata",clnftltle);
            criteria3.andEqualTo("clnfstata",clnftltle);
        }
        //删除状态
        if (param.containsKey("delstate") && param.get("delstate") != null) {
            int delstate = (Integer) param.get("delstate");
            criteria.andEqualTo("delstate", delstate);
            criteria2.andEqualTo("delstate", delstate);
            criteria3.andEqualTo("delstate", delstate);
        }

        //相关推荐
       /* if(param.containsKey("relation") && param.get("relation") != null){
            criteria.andEqualTo("totop",1);//置顶
            criteria2.andEqualTo("totop",1);//置顶
            criteria3.andEqualTo("totop",1);//置顶
        }*/
        //省
        if(param.containsKey("province") && param.get("province") != null){
            criteria.andEqualTo("province",(String)param.get("province"));
            criteria2.andEqualTo("province",(String)param.get("province"));
            criteria3.andEqualTo("province",(String)param.get("province"));
        }
        //市
        if(param.containsKey("city") && param.get("city") != null){
            criteria.andEqualTo("city",(String)param.get("city"));
            criteria2.andEqualTo("city",(String)param.get("city"));
            criteria3.andEqualTo("city",(String)param.get("city"));
        }
        //区
        if(param.containsKey("area") && param.get("area") != null){
            criteria.andEqualTo("area",(String)param.get("area"));
            criteria2.andEqualTo("area",(String)param.get("area"));
            criteria3.andEqualTo("area",(String)param.get("area"));
        }

        if(param.containsKey("searcheKey") && param.get("searcheKey") != null){
            criteria.andLike("clnfkey","%"+(String)param.get("searcheKey")+"%");
            criteria2.andLike("clnftltle","%"+(String)param.get("searcheKey")+"%");
            criteria3.andLike("clnfdetail","%"+(String)param.get("searcheKey")+"%");
        }

        //标题
        if(param.containsKey("clnftltle") && param.get("clnftltle") != null){
            String clnftltle = (String)param.get("clnftltle");
            if(!"".equals(clnftltle.trim())){
                criteria.andLike("clnftltle", "%" + clnftltle.trim() + "%");
                criteria2.andLike("clnftltle", "%" + clnftltle.trim() + "%");
                criteria3.andLike("clnftltle", "%" + clnftltle.trim() + "%");
            }
        }
        String toproject=null;
        //所属项目  查询根据资讯栏目的所属项目来查询 故此 屏蔽资讯的所属项目
       /* if(param.containsKey("toproject") && param.get("toproject") != null){
            toproject = (String)param.get("toproject");
            if(!"".equals(toproject.trim())){
                criteria.andLike("toproject", "%"+toproject.trim()+"%");
                criteria2.andLike("toproject", "%"+toproject.trim()+"%");
                criteria3.andLike("toproject", "%"+toproject.trim()+"%");
            }
        }*/
//		example.setOrderByClause("clnfcrttime desc");
        //栏目
        String clnftype = null;
        if (param.containsKey("clnftype") && param.get("clnftype") != null) {
            clnftype = (String) param.get("clnftype");
        }
        List slist = new ArrayList();
        List<WhgInfColumn> list = this.whgInfColumnService.selectChildList(clnftype, toproject);//查找该节点下的子点
        for (WhgInfColumn obj : list) {
            slist.add(obj.getColid());
        }
        if (clnftype != null) {
            slist.add(clnftype);
        }
        criteria.andIn("clnftype", slist);
        criteria2.andIn("clnftype", slist);
        criteria3.andIn("clnftype", slist);

        if(param.containsKey("searcheKey") && param.get("searcheKey") != null){
            example.or(criteria2);
            example.or(criteria3);
        }
        example.setOrderByClause("clnfcrttime desc");//totop desc,
        PageHelper.startPage(page, rows);
        List<WhgInfColinfo> lists = this.whgInfColinfoMapper.selectByExample(example);

        PageInfo<WhgInfColinfo> pageInfo = new PageInfo<WhgInfColinfo>(lists);

        return pageInfo;
    }
    /**
     * 添加栏目信息
     * @param whc
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object addinfo(WhgInfColinfo whc) {
        return this.whgInfColinfoMapper.insert(whc);
    }
    /**
     * 编辑栏目信息
     * @param whc
     * @return
     */
    @CacheEvict(allEntries = true)
    public Object upmusinfo(WhgInfColinfo whc) {
        return this.whgInfColinfoMapper.updateByPrimaryKeySelective(whc);
    }
    /**
     * 根据主键删除栏目信息
     * @param clnfid
     * @return
     */
    @CacheEvict(allEntries = true)
    public int delete(String clnfid) {
        return this.whgInfColinfoMapper.deleteByPrimaryKey(clnfid);

    }

    /**
     * 根据主键获得栏目信息
     * @param clnfid
     * @return
     */
    @Cacheable
    public WhgInfColinfo getInfo(String clnfid) {
        return this.whgInfColinfoMapper.selectByPrimaryKey(clnfid);

    }

    /**
     * 增加一次资讯的浏览量
     * @param clnfid
     * @throws Exception
     */
    public void addViews(String clnfid)throws Exception{
        WhgInfColinfo info = this.whgInfColinfoMapper.selectByPrimaryKey(clnfid);
        Integer browse = info.getClnfbrowse();
        if(browse == null){
            browse = 1;
        }else{
            browse = browse.intValue()+1;
        }
        info.setClnfbrowse(browse);
        this.whgInfColinfoMapper.updateByPrimaryKey(info);
    }

    /**
     * 审核状态改变
     * @param whz
     */
    @CacheEvict(allEntries = true)
    public void checkin(WhgInfColinfo whz) {
        this.whgInfColinfoMapper.updateByPrimaryKeySelective(whz);

    }
    /**
     * 设置上首页 及排序
     * @param whz
     */
    @CacheEvict(allEntries = true)
    public void goHomePage(WhgInfColinfo whz) {
        this.whgInfColinfoMapper.updateByPrimaryKeySelective(whz);
    }
    /**
     * 批量审核
     * @param clnfid
     * @param fromstate
     * @param tostate
     */
    @CacheEvict(allEntries = true)
    public void checkexhi(String clnfid, int fromstate, int tostate, WhgSysUser user) {
        List<String> list = new ArrayList<String>();
        String[] cln = clnfid.split(",");
        for (int i = 0; i < cln.length; i++) {
            list.add(cln[i]);
        }
        WhgInfColinfo whz = new WhgInfColinfo();
        if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()) {//待发布  审核者信息
            whz.setCheckor(user.getId());
            whz.setCheckdate(new Date());
        } else if (tostate == EnumBizState.STATE_PUB.getValue()) {//已发布记录 发布者信息
            whz.setPublisher(user.getId());
            whz.setPublishdate(new Date());
        }
        whz.setClnfstata(tostate);
        whz.setClnfopttime(new Date());
        Example example = new Example(WhgInfColinfo.class);
        example.createCriteria().andIn("clnfid", list).andEqualTo("clnfstata", fromstate);
        this.whgInfColinfoMapper.updateByExampleSelective(whz, example);

    }

    /**
     * 置顶
     * @param whz
     */
    @CacheEvict(allEntries = true)
    public void toTop(WhgInfColinfo whz) {
//		if (whz.getTotop() != null && whz.getTotop() == 1 && !"".equals(whz.getClnftype())) {
//			WhgInfColinfo co = this.whgInfColinfoMapper.findIsTop(whz.getClnftype());
//			if (co != null) {
//				co.setTotop(0);
//				this.whgInfColinfoMapper.updateByPrimaryKeySelective(co);
//			}
//		}
        this.whgInfColinfoMapper.updateByPrimaryKeySelective(whz);
    }

    /**
     * 上首页
     * @param ids
     * @param formupindex
     * @param toupindex
     * @return
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_upindex(String ids, String formupindex, int toupindex) {
        ResponseBean res = new ResponseBean();
        if(ids == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("资讯主键丢失");
            return res;
        }
        Example example = new Example(WhgInfColinfo.class);
        Example.Criteria c = example.createCriteria();
        c.andIn("clnfid", Arrays.asList( ids.split("\\s*,\\s*") ));
        c.andIn("upindex", Arrays.asList( formupindex.split("\\s*,\\s*") ));
        WhgInfColinfo zx = new WhgInfColinfo();
        zx.setUpindex(toupindex);
        this.whgInfColinfoMapper.updateByExampleSelective(zx,example);
        return res;
    }
    
}
