package com.creatoo.hn.services.admin.resourse;

import com.creatoo.hn.dao.mapper.WhgResourceMapper;
import com.creatoo.hn.dao.mapper.api.ApiResourceMapper;
import com.creatoo.hn.dao.mapper.api.gather.CrtWhgGatherMapper;
import com.creatoo.hn.dao.model.WhgResource;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.activity.WhgActivityService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumResType;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 资源管理service
 *
 * @author luzhihuai
 * @version 1-201703
 *          Created by Administrator on 2017/3/29.
 */
@Service
@CacheConfig(cacheNames = "WhgResource", keyGenerator = "simpleKeyGenerator")
public class WhgResourceService extends BaseService {
    /**
     * 
     */
    @Autowired
    private WhgResourceMapper whgResourceMapper;

    @Autowired
    private ApiResourceMapper apiResourceMapper;

    @Autowired
    private CrtWhgGatherMapper crtWhgGatherMapper;

    @Autowired
    private WhgActivityService whgActivityService;

    /**
     * 分页查询分类列表信息
     *
     * @param request
     */
    @Cacheable
    public PageInfo<WhgResource> t_srchList4p(HttpServletRequest request, WhgResource WhgResource) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);

        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));
        //开始分页
        PageHelper.startPage(page, rows);

        //众筹品牌的资源
        Integer isbrand = Integer.parseInt((String) paramMap.get("isbrand"));
        Map recode = new HashMap();
        recode.put("refid", WhgResource.getRefid());
        recode.put("enttype", WhgResource.getEnttype());
        if(isbrand != null && isbrand == 1){
            List list = this.crtWhgGatherMapper.selectBrandResources(recode);
            return new PageInfo(list);
        } //众筹品牌的资源
        //文化品牌的资源
        if(isbrand != null && isbrand == 2){
            List<Map> list = apiResourceMapper.t_selppSource(WhgResource.getRefid(),null,EnumTypeClazz.TYPE_ACTIVITY.getValue(), WhgResource.getName());
            return new PageInfo(list);
        } //众筹品牌的资源
        //微专业的资源
        if(isbrand != null && isbrand == 3){
            List<Map> list = apiResourceMapper.t_selMajorSource(WhgResource.getRefid(),null, WhgResource.getName());
            return new PageInfo(list);
        } //微专业的资源


        String id = request.getParameter("id");
        //搜索条件
        Example example = new Example(WhgResource.class);
        Example.Criteria c = example.createCriteria();

        c.andEqualTo("refid",id);

        //名称条件
        if (WhgResource != null && WhgResource.getName() != null) {
            c.andLike("name", "%" + WhgResource.getName() + "%");
            WhgResource.setName(null);
        }
        //名称条件
        if (WhgResource != null && WhgResource.getReftype() != null) {
            if (WhgResource.getReftype() != null && !"".equals(WhgResource.getReftype())) {
//                if ("train".equals(WhgResource.getReftype()))//培训
//                    c.andEqualTo("reftype",1);
//                if ("act".equals(WhgResource.getReftype()))//活动
//                    c.andEqualTo("reftype",2);
//                if ("ven".equals(WhgResource.getReftype()))//场馆
//                    c.andEqualTo("reftype",3);
//                if ("venroom".equals(WhgResource.getReftype()))//场馆活动室
//                    c.andEqualTo("reftype",4);
//                if ("minglu".equals(WhgResource.getReftype()))//名录
//                    c.andEqualTo("reftype",5);
//                if ("succ".equals(WhgResource.getReftype()))//传承人
//                    c.andEqualTo("reftype",6);
//                if ("7".equals(WhgResource.getReftype()))//志愿活动
//                    c.andEqualTo("reftype",7);
//                if ("8".equals(WhgResource.getReftype()))//优秀组织
//                    c.andEqualTo("reftype",8);
//                if ("9".equals(WhgResource.getReftype()))//项目示范
//                    c.andEqualTo("reftype",9);
                c.andEqualTo("reftype", WhgResource.getReftype());//1、培训 2、活动 3、场馆 4、场馆活动室 5、名录、6传承人，7志愿活动，8优秀组织，9项目示范）
            }
            WhgResource.setReftype(null);
        }

        //其它条件
        c.andEqualTo(WhgResource);

        example.setOrderByClause("crtdate desc");
        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgResource> typeList = this.whgResourceMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 查询单条记录
     * @param id id
     * @return 对象
     * @throws Exception
     */
    @Cacheable
    public WhgResource t_srchOne(String id)throws Exception{
        WhgResource record = new WhgResource();
        record.setId(id);
        return this.whgResourceMapper.selectOne(record);
    }

    /**
     * 根据活动ID查询相关资源
     * @param actvid
     * @return
     */

    public List<WhgResource> selectactSource(String actvid,String enttype,String reftype){
        Example example = new Example(WhgResource.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("enttype", enttype);
        c.andEqualTo("reftype", reftype);
        c.andEqualTo("refid",actvid);
        return this.whgResourceMapper.selectByExample(example);
    }

    /**
     * 根据类型查询所有资源
     * @param enttype
     * @param reftype
     * @param refid
     * @return
     * @throws Exception
     */
    public List<WhgResource> findResource(String enttype, String reftype, String refid)throws Exception{
        Example example = new Example(WhgResource.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("enttype", enttype);
        c.andEqualTo("reftype", reftype);
        c.andEqualTo("refid",refid);
        example.setOrderByClause("crtdate");
        return this.whgResourceMapper.selectByExample(example);
    }

    /**
     * 根据条件查询资源
     * @param resource 条件对象
     * @param sort 排序字段名
     * @param order 排序方式
     * @return 资源列表
     * @throws Exception
     */
    public List<WhgResource> findResourceByCondition(WhgResource resource, String sort, String order)throws Exception{
        Example example = new Example(WhgResource.class);
        example.createCriteria().andEqualTo(resource);
        if(StringUtils.isNotEmpty(sort)){
            String orderby = sort;
            if("desc".equalsIgnoreCase(order) || "asc".equalsIgnoreCase(order)){
                orderby = orderby + " " + order;
            }
            example.setOrderByClause(orderby);
        }
        return this.whgResourceMapper.selectByExample(example);
    }

    /**
     * 根据条件统计资源数量
     * @param resource 条件对象
     * @return 满足条件的资源数量
     * @throws Exception
     */
    public int countResourceByCondition(WhgResource resource)throws Exception{
        return this.whgResourceMapper.selectCount(resource);
    }

    /**
     * 根据条件删除资源关系
     * @param resource 条件对象
     * @throws Exception
     */
    public void deleteResourceByCondition(WhgResource resource)throws Exception{
        this.whgResourceMapper.delete(resource);
    }

    /**
     * 添加
     *
     * @param wcr
     */
    @CacheEvict(allEntries = true)
    public void t_add(HttpServletRequest request, WhgResource wcr) throws Exception {

        WhgResource WhgResource = new WhgResource();
        String refid = request.getParameter("refid");
        if (refid != null && !"".equals(refid)) {
            WhgResource.setRefid(refid);
        }
        String reftype = request.getParameter("reftype");
        if (reftype != null && !"".equals(reftype)) {
            WhgResource.setReftype(reftype);//1、培训 2、活动 3、场馆 4、场馆活动室 5、名录、6传承人，7志愿活动，8优秀组织，9项目示范
        }

        String penturl = request.getParameter("penturl");
        if (penturl != null && !"".equals(penturl)) {
            WhgResource.setEnturl(penturl);
        }
        if(!"".equals(request.getParameter("doc_enturl")) && request.getParameter("doc_enturl") != null){
            WhgResource.setEnturl(request.getParameter("doc_enturl"));
        }

        if (wcr.getEnttype() != null && !"".equals(wcr.getEnttype())) {
            WhgResource.setEnttype(wcr.getEnttype());
        }
        if (wcr.getName() != null && !"".equals(wcr.getName())) {
            WhgResource.setName(wcr.getName());
        }
        if (wcr.getEnturl() != null && !"".equals(wcr.getEnturl())) {
            WhgResource.setEnturl(wcr.getEnturl());
        }
        if (wcr.getEnttimes() != null && !"".equals(wcr.getEnttimes())) {
            WhgResource.setEnttimes(wcr.getEnttimes());
        }
        if (wcr.getDeourl() != null && !"".equals(wcr.getDeourl())) {
            WhgResource.setDeourl(wcr.getDeourl());
        }
        if (wcr.getSummary() != null && !"".equals(wcr.getSummary())) {
            WhgResource.setSummary(wcr.getSummary());
        }
        if(wcr.getArttype() != null && !"".equals(wcr.getArttype())){
            WhgResource.setArttype(wcr.getArttype());
        }

        WhgResource.setId(IDUtils.getID());
        WhgResource.setCrtdate(new Date());

        int result = this.whgResourceMapper.insertSelective(WhgResource);
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
    public void t_edit(Map<String, Object> paramMap,WhgResource wcr) throws Exception {
        String penturl = (String) paramMap.get("penturl");
        if (penturl != null && !"".equals(penturl)) {
            wcr.setEnturl(penturl);
        }
        String docenturl = (String) paramMap.get("doc_enturl");
        if (docenturl != null && !"".equals(docenturl)) {
            wcr.setEnturl(docenturl);
        }
        wcr.setRedate(new Date());
        int result = this.whgResourceMapper.updateByPrimaryKeySelective(wcr);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 删除资源时删除关系
     * @param libid
     * @param resid
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del4Resource(String libid, String resid)throws Exception{
        if(StringUtils.isNotEmpty(libid) && StringUtils.isNotEmpty(resid)) {
            Example example = new Example(WhgResource.class);
            example.createCriteria().andEqualTo("libid", libid).andEqualTo("resid", resid);
            this.whgResourceMapper.deleteByExample(example);
        }
    }

    /**
     * 编辑资源时编辑关系
     * @param whgResource
     * @param libid
     * @param resid
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_edit4Resource(WhgResource whgResource, String libid, String resid)throws Exception{
        if(StringUtils.isNotEmpty(libid) && StringUtils.isNotEmpty(resid) && whgResource != null) {
            Example example = new Example(WhgResource.class);
            example.createCriteria().andEqualTo("libid", libid).andEqualTo("resid", resid);
            this.whgResourceMapper.updateByExampleSelective(whgResource, example);
        }
    }

    /**
     * 删除
     *
     * @param request
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(HttpServletRequest request) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        String entid = (String) paramMap.get("id");
        int result = this.whgResourceMapper.deleteByPrimaryKey(entid);
        if (result != 1) {
            throw new Exception("删除数据失败！");
        }
    }


    /**
     * 查询资源
     * @param reftype
     * @param refid
     * @param enttype
     * @return
     */
    public ApiResultBean t_getResource(String reftype, String refid, String enttype, String wechat, int page, int rows) throws Exception{//wechat 有值 代表从微信端请求数据 做分页处理
        ApiResultBean arb = new ApiResultBean();
        if(reftype != null && "35".equals(reftype)){//文化品牌 取其关联的活动下的  所有资源
            /*List<Map> list = selectppSource(refid,enttype, EnumTypeClazz.TYPE_ACTIVITY.getValue());
            if(list != null && list.size() > 0){
                if(wechat!=null&&wechat.equals("1")){
                    arb.setPageInfo(new PageInfo(list));
                }else{
                    arb.setData(list);
                }
                return arb;
            }*/

            PageInfo pageInfo = this.selectPPandActSource(0, 0, refid, enttype, null);
            if (wechat!=null&&wechat.equals("1")){
                arb.setPageInfo(pageInfo);
                arb.setRows(pageInfo.getList());
            }else{
                arb.setData(pageInfo.getList());
            }
            return arb;
        }else if(reftype != null && "29".equals(reftype)){
            Map recode = new HashMap();
            recode.put("refid", refid);
            List list = this.crtWhgGatherMapper.selectBrandResources(recode);
            if(list != null && list.size() > 0){
                if(enttype!=null&&!enttype.equals("")){
                    recode.put("enttype", enttype);
                }
                List _list = this.crtWhgGatherMapper.selectBrandResources(recode);
                if(wechat!=null&&wechat.equals("1")){
                    arb.setPageInfo(new PageInfo(_list));
                }else{
                    arb.setData(_list);
                }
                return arb;
            }
        } else if (reftype != null && "40".equals(reftype)) {//展品

            Example example = new Example(WhgResource.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("reftype", reftype).andEqualTo("refid", refid).andIn("enttype", Arrays.asList(1, 2, 3, 4));
            example.setOrderByClause("crtdate asc");
            PageHelper.startPage(page, rows);
            List<WhgResource> _list = this.whgResourceMapper.selectByExample(example);
            if (_list != null && _list.size() > 0) {
                if (enttype != null && !"".equals(enttype)) {
                    PageHelper.startPage(page, rows);
                    List<Map> list = this.apiResourceMapper.selectResource2(reftype, refid, enttype);
                    arb.setPageInfo(new PageInfo(list));
                    return arb;
                } else {
                    arb.setPageInfo(new PageInfo(_list));
                    return arb;
                }
            } else {
                arb.setCode(101);
                arb.setMsg("没有资源");
                return arb;
            }
        }else if(reftype != null && "34".equals(reftype)){

            List<Map> majorList = apiResourceMapper.t_selMajorSource(refid,null, null);
            if(majorList != null && majorList.size() > 0){
                List<Map> _majorList = apiResourceMapper.t_selMajorSource(refid,enttype, null);
                if(wechat!=null&&wechat.equals("1")){
                    arb.setPageInfo(new PageInfo(_majorList));
                }else{
                    arb.setData(_majorList);
                }
                return arb;
            }
        }else {
            Example example = new Example(WhgResource.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("reftype",reftype).andEqualTo("refid",refid).andIn("enttype", Arrays.asList(1,2,3,4));
            example.setOrderByClause("crtdate desc");
            List<WhgResource> _list = this.whgResourceMapper.selectByExample(example);
            if(_list != null && _list.size() > 0){
                if(enttype != null && !"".equals(enttype)){
                    List<Map> list  = this.apiResourceMapper.selectResource(reftype, refid, enttype);
                    if(wechat!=null&&wechat.equals("1")){
                        arb.setPageInfo(new PageInfo(list));
                    }else{
                        arb.setData(list);
                    }
                    return arb;
                }else {
                    if (wechat != null && wechat.equals("1")) {
                        arb.setPageInfo(new PageInfo(_list));
                    } else {
                        arb.setData(_list);
                    }
                    return arb;
                }
            }else {
                arb.setCode(101);
                arb.setMsg("没有资源");
                return arb;
            }
        }
        return arb;
    }

    /**
     * 查询品牌活动的资源
     * @param id
     * @param enttype
     * @param reftype
     * @return
     */
    public List<Map> selectppSource(String id, String enttype, String reftype) {
        List<Map> list = apiResourceMapper.t_selppSource(id,enttype,reftype,null);
        return list;
    }

    public PageInfo selectPPandActSource(int page, int pageSize, String ppid, String enttype, String name) throws Exception {
        if (page>0 && pageSize>0){
            PageHelper.startPage(page, pageSize);
        }
        List list = apiResourceMapper.t_selppAndtarSource(ppid, enttype, name);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }
}
