package com.creatoo.hn.services.admin.resourse;

import com.creatoo.hn.dao.mapper.WhgResourceMapper;
import com.creatoo.hn.dao.model.WhgResource;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
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

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @Cacheable
    public PageInfo<WhgResource> t_srchList4p(HttpServletRequest request, WhgResource WhgResource) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));
        //开始分页
        PageHelper.startPage(page, rows);
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
                c.andEqualTo("reftype", WhgResource.getReftype());//1、培训 2、活动 3、场馆 4、场馆活动室 5、名录、6传承人，7志愿活动，8优秀组织，9项目示范）
            }
            WhgResource.setReftype(null);
        }

        //其它条件
        c.andEqualTo(WhgResource);

        example.setOrderByClause("crtdate asc");
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
    public ApiResultBean t_getResource(String reftype, String refid, String enttype) {
        ApiResultBean arb = new ApiResultBean();
        Example example = new Example(WhgResource.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("reftype",reftype).andEqualTo("refid",refid).andIn("enttype", Arrays.asList(1,2,3));
        List<WhgResource> _list = this.whgResourceMapper.selectByExample(example);

        if(_list != null && _list.size() > 0){
            if(enttype != null && !"".equals(enttype)){
                List<Map> list  = null;//this.apiResourceMapper.selectResource(reftype, refid, enttype);
                arb.setData(list);
                return arb;
            }else {
                arb.setData(_list);
                return arb;
            }
        }else {
            arb.setCode(101);
            arb.setMsg("没有资源");
            return arb;
        }
    }
}
