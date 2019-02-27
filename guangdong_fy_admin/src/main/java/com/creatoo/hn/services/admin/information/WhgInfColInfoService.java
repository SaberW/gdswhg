package com.creatoo.hn.services.admin.information;

import com.creatoo.hn.dao.mapper.WhgInfColinfoMapper;
import com.creatoo.hn.dao.model.WhgInfColinfo;
import com.creatoo.hn.dao.model.WhgInfColumn;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
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

    public Object srchOne(String id) throws Exception{
        return this.whgInfColinfoMapper.selectByPrimaryKey(id);
    }
    @CacheEvict(allEntries = true)
    public void t_add(WhgInfColinfo info, WhgSysUser user) throws Exception{
        info.setClnfid(IDUtils.getID());
        info.setTotop(0);//默认不置顶
        info.setUpindex(0); //默认不上首页
        info.setClnfopttime(new Date());
        this.whgInfColinfoMapper.insert(info);
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
    public Map<String, Object> selein(Map<String, Object> param) {
        //分页信息
        int page = Integer.parseInt((String)param.get("page"));
        int rows = Integer.parseInt((String)param.get("rows"));

        PageHelper.startPage(page, rows);
        Example example = new Example(WhgInfColinfo.class);
        Example.Criteria criteria = example.createCriteria();
        if(param.containsKey("cultid") && param.get("cultid")!= null) {
            criteria.andEqualTo("clnvenueid",param.get("cultid")) ;
        }
        //排序
        if(param.containsKey("sort") && param.get("sort")!= null){
            StringBuffer sbf = new StringBuffer((String)param.get("sort"));
            if(param.containsKey("order") && param.get("order") !=  null){
                sbf.append(" ").append(param.get("order"));
            }
            example.setOrderByClause(sbf.toString());
        }else {
            example.setOrderByClause("totop desc,clnfcrttime desc");
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
            String clnftltle = (String)param.get("clnfstata");
            if(!"".equals(clnftltle.trim())){
                criteria.andEqualTo("clnfstata", clnftltle.trim());
            }
        }
//		example.setOrderByClause("clnfcrttime desc");
        //栏目
        if(param.containsKey("clnftype") && param.get("clnftype") != null){
            String clnftype = (String)param.get("clnftype");
            if(!"".equals(clnftype.trim())){
                criteria.andEqualTo("clnftype", clnftype);
            }
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
        if(param.containsKey("cultid") && param.get("cultid")!= null) {
            criteria.andEqualTo("clnvenueid",param.get("cultid")) ;
        }
        //排序
        if(param.containsKey("sort") && param.get("sort")!= null){
            StringBuffer sbf = new StringBuffer((String)param.get("sort"));
            if(param.containsKey("order") && param.get("order") !=  null){
                sbf.append(" ").append(param.get("order"));
            }
            example.setOrderByClause(sbf.toString());
        }else {
            example.setOrderByClause("totop desc,clnfcrttime desc");
        }
        //状态
        if(param.containsKey("clnfstata") && param.get("clnfstata") != null){
            int clnftltle = (Integer)param.get("clnfstata");
            criteria.andEqualTo("clnfstata",clnftltle);
        }

        //相关推荐
        if(param.containsKey("relation") && param.get("relation") != null){
            criteria.andEqualTo("totop",1);//置顶
        }

        //标题
        if(param.containsKey("clnftltle") && param.get("clnftltle") != null){
            String clnftltle = (String)param.get("clnftltle");
            if(!"".equals(clnftltle.trim())){
                criteria.andLike("totop", "%"+clnftltle.trim()+"%");
            }
        }
        String toproject=null;
        //所属项目
        if(param.containsKey("toproject") && param.get("toproject") != null){
            toproject = (String)param.get("toproject");
            if(!"".equals(toproject.trim())){
                criteria.andLike("toproject", "%"+toproject.trim()+"%");
            }
        }
//		example.setOrderByClause("clnfcrttime desc");
        //栏目
        if(param.containsKey("clnftype") && param.get("clnftype") != null){
            String clnftype = (String)param.get("clnftype");
            if(!"".equals(clnftype.trim())){
              List slist=new ArrayList();
                List<WhgInfColumn> list=this.whgInfColumnService.selectChildList(clnftype,toproject);//查找该节点下的子点
              for(WhgInfColumn obj:list){
                  slist.add(obj.getColid());
              }
                slist.add(clnftype);
                criteria.andIn("clnftype", slist);
            }
        }
        PageHelper.startPage(page, rows);
        List<WhgInfColinfo> list = this.whgInfColinfoMapper.selectByExample(example);

        PageInfo<WhgInfColinfo> pageInfo = new PageInfo<WhgInfColinfo>(list);

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
    public void checkexhi(String clnfid, int fromstate, int tostate) {
        List<String> list = new ArrayList<String>();
        String[] cln = clnfid.split(",");
        for (int i = 0; i < cln.length; i++) {
            list.add(cln[i]);
        }
        WhgInfColinfo whz = new WhgInfColinfo();
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
