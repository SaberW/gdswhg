package com.creatoo.hn.services.admin.mass;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.mapper.mass.CrtWhgMassLibraryMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiQuotaService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTypeService;
import com.creatoo.hn.util.AliyunOssUtil;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ImageUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.creatoo.hn.util.enums.EnumUploadType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 群文资源库服务
 * Created by wangxl on 2017/11/10.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgMassLibrary", keyGenerator = "simpleKeyGenerator")
public class WhgMassLibraryService extends BaseService {
    /**
     * 分馆服务
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 管理员服务
     */
    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 群文资源库Mapper
     */
    @Autowired
    private WhgMassLibraryMapper whgMassLibraryMapper;

    @Autowired
    private WhgMassUserAuthMapper whgMassUserAuthMapper;

    /**
     * 资源库表单Mapper
     */
    @Autowired
    private WhgMassLibraryFormMapper whgMassLibraryFormMapper;

    /**
     * 资源库表单字段Mapper
     */
    @Autowired
    private WhgMassLibraryFormFieldMapper whgMassLibraryFormFieldMapper;

    /**
     * 自定义资源库Mapper
     */
    @Autowired
    private CrtWhgMassLibraryMapper crtWhgMassLibraryMapper;

    /**
     * 资源关联关系Mapper
     */
    @Autowired
    private WhgResourceService whgResourceService;

    @Autowired
    private WhgSysCultMapper whgSysCultMapper;

    @Autowired
    private WhgMassViewMapper whgMassViewMapper;

    @Autowired
    private WhgMassDownMapper whgMassDownMapper;

    @Autowired
    private WhgMassApplyUserMapper applyUserMapper;

    @Autowired
    private WhgYunweiTypeService whgYunweiTypeService;

    @Autowired
    private WhgYunweiQuotaService quotaService;


    /**
     * 根据唯一标识查找资源库信息
     *
     * @param id 资源库唯一标识
     * @return 资源库信息对象
     * @throws Exception
     */
    @Cacheable
    public WhgMassLibrary findById(String id) throws Exception {
        return this.whgMassLibraryMapper.selectByPrimaryKey(id);
    }

    /**
     * 表单字段
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Cacheable
    public WhgMassLibraryFormField findLibraryFormFieldById(String id) throws Exception {
        return this.whgMassLibraryFormFieldMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询表单列
     *
     * @param id
     * @return,
     * @throws Exception
     */
    @Cacheable
    public WhgMassLibraryForm findLibraryFormById(String id) throws Exception {
        return this.whgMassLibraryFormMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询表单列表
     *
     * @param form
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgMassLibraryForm> findLibraryForm(WhgMassLibraryForm form) throws Exception {
        Example example = new Example(WhgMassLibraryForm.class);
        example.createCriteria().andEqualTo(form);
        example.setOrderByClause("rows,columns");
        return this.whgMassLibraryFormMapper.selectByExample(example);
    }

    /**
     * 查询表单字段
     *
     * @param field
     * @return
     * @throws Exception
     */
    @Cacheable
    public List<WhgMassLibraryFormField> findLibraryFormField(WhgMassLibraryFormField field) throws Exception {
        Example example = new Example(WhgMassLibraryFormField.class);
        example.createCriteria().andEqualTo(field);
        example.setOrderByClause("fieldidx");
        return this.whgMassLibraryFormFieldMapper.selectByExample(example);
    }

    /**
     * 根据条件查询资源库
     *
     * @param sort           排序字段
     * @param order          排序方式
     * @param whgMassLibrary 查询条件
     * @return 满足条件的资源库列表
     * @throws Exception
     */
    @Cacheable
    public List<WhgMassLibrary> find(String sort, String order, WhgMassLibrary whgMassLibrary, WhgSysUser sysUser) throws Exception {
        Example example = new Example(WhgMassLibrary.class);
        Example.Criteria c = example.createCriteria();

        //like条件
        if (whgMassLibrary != null && StringUtils.isNotEmpty(whgMassLibrary.getName())) {
            c.andLike("name", "%" + whgMassLibrary.getName() + "%");
            whgMassLibrary.setName(null);
        }

        //文化馆 部门
        if (sysUser != null && StringUtils.isNotEmpty(sysUser.getId())) {
            _parseCultAndDept(whgMassLibrary, sysUser.getId(), sysUser.getAccount(), c);

           /* // 查询共享的文化馆
            Example.Criteria c2 = example.createCriteria();
            c2.andLike("sharecultid", "%" + whgMassLibrary.getCultid() + "%");
            c2.andEqualTo("resourcetype", whgMassLibrary.getResourcetype());
            c2.andEqualTo("state", whgMassLibrary.getState());
            example.or(c2);*/
        }

        //其它条件
        c.andEqualTo(whgMassLibrary);

        //排序
        if (StringUtils.isNotEmpty(sort)) {
            String orderby = sort;
            if ("asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order)) {
                orderby += " " + order;
            }
            example.setOrderByClause(orderby);
        } else {
            example.setOrderByClause("idx desc");
        }

        //查询并返回
        return this.whgMassLibraryMapper.selectByExample(example);
    }


    /**
     * 根据条件查询资源库
     *
     * @param sort           排序字段
     * @param order          排序方式
     * @param whgMassLibrary 查询条件
     * @return 满足条件的资源库列表
     * @throws Exception
     */
    public List<WhgMassLibrary> findShareLibrary(String sort, String order, WhgMassLibrary whgMassLibrary, WhgSysUser sysUser) throws Exception {
        Example example = new Example(WhgMassLibrary.class);
        Example.Criteria c = example.createCriteria();

        //like条件
        if (whgMassLibrary != null && StringUtils.isNotEmpty(whgMassLibrary.getName())) {
            c.andLike("name", "%" + whgMassLibrary.getName() + "%");
            whgMassLibrary.setName(null);
        }

        /*//文化馆 部门
        if (sysUser != null && StringUtils.isNotEmpty(sysUser.getId())) {
            _parseCultAndDept(whgMassLibrary, sysUser.getId(), sysUser.getAccount(), c);

            // 查询共享的文化馆
            Example.Criteria c2 = example.createCriteria();
            example.or(c2);
        }*/

        c.andLike("sharecultid", "%" + whgMassLibrary.getCultid() + "%");
        whgMassLibrary.setCultid(null);
        c.andEqualTo("resourcetype", whgMassLibrary.getResourcetype());
        c.andEqualTo("state", whgMassLibrary.getState());

        //其它条件
        c.andEqualTo(whgMassLibrary);

        //排序
        if (StringUtils.isNotEmpty(sort)) {
            String orderby = sort;
            if ("asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order)) {
                orderby += " " + order;
            }
            example.setOrderByClause(orderby);
        } else {
            example.setOrderByClause("idx desc");
        }

        //查询并返回
        return this.whgMassLibraryMapper.selectByExample(example);
    }


    /**
     * 根据条件查询资源库是否共享
     *
     * @param whgMassLibrary
     * @param sysUser
     * @return
     * @throws Exception
     */
    @Cacheable
    public int find(WhgMassLibrary whgMassLibrary, WhgSysUser sysUser) throws Exception {
        Example example = new Example(WhgMassLibrary.class);
        Example.Criteria c = example.createCriteria();

        //文化馆 部门
        if (sysUser != null && StringUtils.isNotEmpty(sysUser.getId())) {
            _parseCultAndDept(whgMassLibrary, sysUser.getId(), sysUser.getAccount(), c);
        }

        //其它条件
        c.andEqualTo(whgMassLibrary);

        //查询并返回
        return this.whgMassLibraryMapper.selectCountByExample(example);
    }

    /**
     * 设置文化馆和部门条件
     *
     * @param whgMassLibrary 查询条件
     * @param userId         管理员标识
     * @param c              查询条件对象
     * @throws Exception
     */
    private void _parseCultAndDept(WhgMassLibrary whgMassLibrary, String userId, String userName, Example.Criteria c) throws Exception {
        //文化馆 部门
        if (whgMassLibrary != null) {
            List<String> cultids = new ArrayList<>();//权限文化馆，如果没有指定，则默认所有的权限文化馆
            List<String> deptids = new ArrayList<>();//根据权限馆查询所有权限部门

            if (StringUtils.isEmpty(whgMassLibrary.getCultid())) {//未指定权限馆
                cultids = this.whgSystemUserService.getAllCultId4PMS(userId);
                c.andIn("cultid", cultids);
                whgMassLibrary.setCultid(null);
            } else {//指定了权限馆查询
                cultids.add(whgMassLibrary.getCultid());
            }

            if (StringUtils.isEmpty(whgMassLibrary.getDeptid())) {//指定权限馆下面的所有部门
                deptids = this.whgSystemUserService.getAllDeptId4PMS(userId);
                if (deptids.size() > 0) {
                    c.andIn("deptid", deptids);

                    whgMassLibrary.setDeptid(null);
                }
            }
        }
    }


    /**
     * 分页查询资源列表
     *
     * @param page    第几页
     * @param rows    每页数
     * @param type    资源库类型
     * @param libid   资源库标识
     * @param srchkey 搜索关键字
     * @return
     * @throws Exception
     */
    @Cacheable
    public PageInfo findResourceByPaging(int page, int rows, String type, String libid, String srchkey, String resids,
                                         List<String> cultids, String resarttype) throws Exception {
        List<String> tablenames = new ArrayList<>();
        List<String> restypes = null;
        if (StringUtils.isNotEmpty(libid)) {//按库搜索
            WhgMassLibrary library = this.findById(libid);
            String tablename = this.__getTableName(library.getCultid(), library.getTablename());
            tablenames.add(tablename);
        } else {//全资源搜索
            WhgMassLibrary condition = new WhgMassLibrary();
            if (StringUtils.isNotEmpty(type)) {//按类型搜索
                String[] arr = type.split(",");
                if (arr.length > 1) {
                    restypes = Arrays.asList(arr);
                } else {
                    condition.setResourcetype(type);
                }
            }
            condition.setState(1);
            List<WhgMassLibrary> librarys = this.find(null, null, condition, null);
            if (librarys != null) {
                for (WhgMassLibrary library : librarys) {
                    String tablename = this.__getTableName(library.getCultid(), library.getTablename());
                    tablenames.add(tablename);
                }
            }
        }
        if (StringUtils.isNotEmpty(srchkey)) {
            srchkey = "%" + srchkey + "%";
        }
        if (StringUtils.isNotEmpty(resarttype)) {
            resarttype = "%" + resarttype + "%";
        }
        PageHelper.startPage(page, rows);
        List<Map> list = this.crtWhgMassLibraryMapper.globalFindResource(tablenames, srchkey, restypes, resids, cultids, resarttype);
        return new PageInfo<Map>(list);
    }


    /**
     * 分页查询资源列表
     *
     * @param page    第几页
     * @param rows    每页数
     * @param type    资源库类型
     * @param libid   资源库标识
     * @param srchkey 搜索关键字
     * @return
     * @throws Exception
     */
    //@Cacheable
    public ApiResultBean findApiResourceByPaging(int page, int rows, String type, String libid, String srchkey, String resids,
                                                 List<String> cultids, String resarttype, String userid) throws Exception {
        List<String> tablenames = new ArrayList<>();
        List<String> restypes = null;
        if (StringUtils.isNotEmpty(libid)) {//按库搜索
            WhgMassLibrary library = this.findById(libid);
            String tablename = this.__getTableName(library.getCultid(), library.getTablename());
            tablenames.add(tablename);
        } else {//全资源搜索

           /* WhgMassLibrary condition = new WhgMassLibrary();
            if (StringUtils.isNotEmpty(type)) {//按类型搜索
                String[] arr = type.split(",");
                if (arr.length > 1) {
                    restypes = Arrays.asList(arr);
                } else {
                    condition.setResourcetype(type);
                }
            }
            condition.setState(1);
            List<WhgMassLibrary> librarys = this.find(null, null, condition, null);*/

            List<WhgMassLibrary>  librarys = this.whgMassLibraryMapper.queryLibByApplyUserId(userid,type);
            if (StringUtils.isNotEmpty(type)) {//按类型搜索
                String[] arr = type.split(",");
                if (arr.length > 1) {
                    restypes = Arrays.asList(arr);
                }
            }
            if (librarys != null) {
                for (WhgMassLibrary library : librarys) {
                    String tablename = this.__getTableName(library.getCultid(), library.getTablename());
                    tablenames.add(tablename);
                }
            }
        }
        if(tablenames.size()==0){
            ApiResultBean resultBean = new ApiResultBean();
            resultBean.setCode(300);
            resultBean.setMsg("not masslibrary");
            return resultBean;
        }
        if (StringUtils.isNotEmpty(srchkey)) {
            srchkey = "%" + srchkey + "%";
        }
        if (StringUtils.isNotEmpty(resarttype)) {
            resarttype = "%" + resarttype + "%";
        }
        PageHelper.startPage(page, rows);
        List<Map> list = this.crtWhgMassLibraryMapper.apiGlobalFindResource(tablenames, srchkey, restypes, resids, cultids, resarttype, userid);

        ApiResultBean resultBean = new ApiResultBean();
        resultBean.setPageInfo(new PageInfo<Map>(list));

        PageHelper.clearPage();
        Map map = new HashMap();
        if (StringUtils.isNotBlank(type)) {
            map.put(type + "Count", resultBean.getTotal());
        } else {
            //前端获取图片资源数量
            List<String> imgType = new ArrayList<>();
            imgType.add("img");
            List<Map> imgList = this.crtWhgMassLibraryMapper.globalFindResource(tablenames, srchkey, imgType, resids, cultids, resarttype);
            map.put("imgCount", imgList.size());

            //前端获取视频资源数量
            List<String> videoType = new ArrayList<>();
            videoType.add("video");
            List<Map> videoList = this.crtWhgMassLibraryMapper.globalFindResource(tablenames, srchkey, videoType, resids, cultids, resarttype);
            map.put("videoCount", videoList.size());

            //前端获取音频资源数量
            List<String> audioType = new ArrayList<>();
            audioType.add("audio");
            List<Map> audioList = this.crtWhgMassLibraryMapper.globalFindResource(tablenames, srchkey, audioType, resids, cultids, resarttype);
            map.put("audioCount", audioList.size());

            //前端获取文档资源数量
            List<String> fileType = new ArrayList<>();
            fileType.add("file");
            List<Map> fileList = this.crtWhgMassLibraryMapper.globalFindResource(tablenames, srchkey, fileType, resids, cultids, resarttype);
            map.put("fileCount", fileList.size());
        }
        resultBean.setData(map);
        return resultBean;
    }

    /**
     * 分页查询资源库
     *
     * @param page           第几页
     * @param rows           每页数
     * @param sort           排序字段
     * @param order          排序方式
     * @param whgMassLibrary 查询条件
     * @return 资源库指定页数据
     * @throws Exception
     */
    //@Cacheable
    public PageInfo<WhgMassLibrary> findByPaging(int page, int rows, String sort, String order, WhgMassLibrary whgMassLibrary, WhgSysUser sysUser) throws Exception {
        Example example = new Example(WhgMassLibrary.class);
        Example.Criteria c = example.createCriteria();

        //like条件
        if (whgMassLibrary != null && StringUtils.isNotEmpty(whgMassLibrary.getName())) {
            c.andLike("name", "%" + whgMassLibrary.getName() + "%");
            whgMassLibrary.setName(null);
        }

        //文化馆 部门
        _parseCultAndDept(whgMassLibrary, sysUser.getId(), sysUser.getAccount(), c);

        //其它条件
        c.andEqualTo(whgMassLibrary);

        //排序
        if (StringUtils.isNotEmpty(sort)) {
            String orderby = sort;
            if ("asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order)) {
                orderby += " " + order;
            }
            example.setOrderByClause(orderby);
        } else {
            example.setOrderByClause("crtdate desc");
        }

        //查询
        PageHelper.startPage(page, rows);
        List<WhgMassLibrary> list = this.whgMassLibraryMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo<WhgMassLibrary>(list);

        List restList = new ArrayList();
        if (list != null) {
            BeanMap bm = new BeanMap();
            for (WhgMassLibrary _lib : list) {
                bm.setBean(_lib);
                Map info = new HashMap();
                info.putAll(bm);
                if (_lib.getCultid() != null) {
                    WhgSysCult sysCult = this.whgSysCultMapper.selectByPrimaryKey(_lib.getCultid());
                    if (sysCult != null) {
                        info.put("cultname", sysCult.getName());
                    }
                }

                //资源数
                Integer resourcecount = 0;
                Map exp = new HashMap();
                exp.put("state", EnumBizState.STATE_PUB.getValue());
                exp.put("delstate", EnumStateDel.STATE_DEL_NO.getValue());
                if (StringUtils.isNotEmpty(_lib.getId())) {//按库搜索
                    WhgMassLibrary library = this.findById(_lib.getId());
                    String tablename = this.__getTableName(library.getCultid(), library.getTablename());
                    Map map = this.crtWhgMassLibraryMapper.countByTableNameExample1(tablename, exp);
                    info.put("rescount", map.get("rescount"));
                    info.put("ressize", map.get("ressize"));
                }
                restList.add(info);
            }
        }
        pageInfo.setList(restList);
        return pageInfo;
    }

    /**
     * 分页查询资源库
     *
     * @param page           第几页
     * @param rows           每页数
     * @param sort           排序字段
     * @param order          排序方式
     * @param whgMassLibrary 查询条件
     * @return 资源库指定页数据
     * @throws Exception
     */
    @Cacheable
    public PageInfo<WhgMassLibrary> findByPaging2(int page, int rows, String sort, String order, WhgMassLibrary whgMassLibrary, WhgSysUser sysUser) throws Exception {
        Example example = new Example(WhgMassLibrary.class);
        Example.Criteria c = example.createCriteria();

        //like条件
        if (whgMassLibrary != null && StringUtils.isNotEmpty(whgMassLibrary.getName())) {
            c.andLike("name", "%" + whgMassLibrary.getName() + "%");
        }

        if (whgMassLibrary != null && StringUtils.isNotEmpty(whgMassLibrary.getArttype())) {
            c.andLike("arttype", "%" + whgMassLibrary.getArttype() + "%");
            whgMassLibrary.setArttype(null);
        }

        //文化馆 部门
        if (sysUser != null && StringUtils.isNotEmpty(sysUser.getId())) {
            _parseCultAndDept(whgMassLibrary, sysUser.getId(), sysUser.getAccount(), c);

            // 查询共享的文化馆
            Example.Criteria c2 = example.createCriteria();
            c2.andLike("sharecultid", "%" + whgMassLibrary.getCultid() + "%");
//            c2.andEqualTo("resourcetype", whgMassLibrary.getResourcetype());
            c2.andEqualTo("state", whgMassLibrary.getState());
            //like条件
            if (whgMassLibrary != null && StringUtils.isNotEmpty(whgMassLibrary.getName())) {
                c2.andLike("name", "%" + whgMassLibrary.getName() + "%");
            }
            example.or(c2);
        }
        whgMassLibrary.setName(null);

        //其它条件
        c.andEqualTo(whgMassLibrary);

        //排序
        if (StringUtils.isNotEmpty(sort)) {
            String orderby = sort;
            if ("asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order)) {
                orderby += " " + order;
            }
            example.setOrderByClause(orderby);
        } else {
            example.setOrderByClause("crtdate desc");
        }

        //查询
        PageHelper.startPage(page, rows);
        List<WhgMassLibrary> list = this.whgMassLibraryMapper.selectByExample(example);
        return new PageInfo<WhgMassLibrary>(list);
    }

    /**
     * 分页查询当前登录人-所能看到的资源库
     *
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @param whgMassLibrary
     * @param sysUser
     * @param userid
     * @return
     * @throws Exception
     */
    public PageInfo<Map> findByPaging3(int page, int rows, String sort, String order, WhgMassLibrary whgMassLibrary, WhgSysUser sysUser, String userid) throws Exception {
        Map map = new HashMap();
        //like条件
        if (whgMassLibrary != null && StringUtils.isNotEmpty(whgMassLibrary.getName())) {
            map.put("name", "%" + whgMassLibrary.getName() + "%");
        }
        //文化馆 部门
        if (sysUser != null && StringUtils.isNotEmpty(sysUser.getId())) {
            List<String> deptids = new ArrayList<>();//根据权限馆查询所有权限部门
            if (whgMassLibrary != null && StringUtils.isEmpty(whgMassLibrary.getDeptid())) {//指定权限馆下面的所有部门
                deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUser.getId());
                if (deptids.size() > 0) {
                    map.put("deptid", deptids);
                }
            }

            map.put("cultid", whgMassLibrary.getCultid());
            map.put("sharecultid", "%" + whgMassLibrary.getCultid() + "%");
            map.put("state", 1);
        }
        map.put("userid", userid);

        //查询
        PageHelper.startPage(page, rows);
        List list = this.whgMassLibraryMapper.t_whgsrchList(map);
        return new PageInfo<Map>(list);
    }


    /**
     * 分页查询资源库
     *
     * @param page           第几页
     * @param rows           每页数
     * @param sort           排序字段
     * @param order          排序方式
     * @param whgMassLibrary 查询条件
     * @return 资源库指定页数据
     * @throws Exception
     */
    //@Cacheable
    public PageInfo<WhgMassLibrary> findByPaging4(int page, int rows, String sort, String order, WhgMassLibrary whgMassLibrary, String userid) throws Exception {
        Example example = new Example(WhgMassLibrary.class);
        Example.Criteria c = example.createCriteria();

        //like条件
        if (whgMassLibrary != null && StringUtils.isNotEmpty(whgMassLibrary.getName())) {
            c.andLike("name", "%" + whgMassLibrary.getName() + "%");
        }

        if (whgMassLibrary != null && StringUtils.isNotEmpty(whgMassLibrary.getArttype())) {
            c.andLike("arttype", "%" + whgMassLibrary.getArttype() + "%");
            whgMassLibrary.setArttype(null);
        }

        whgMassLibrary.setName(null);

        //其它条件
        c.andEqualTo(whgMassLibrary);

        //排序
        if (StringUtils.isNotEmpty(sort)) {
            String orderby = sort;
            if ("asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order)) {
                orderby += " " + order;
            }
            example.setOrderByClause(orderby);
        } else {
            example.setOrderByClause("crtdate desc");
        }


        //查询
        PageHelper.startPage(page, rows);
        List<WhgMassLibrary> list = this.whgMassLibraryMapper.selectByExample(example);
        return new PageInfo<WhgMassLibrary>(list);
    }


    /**
     * 分页查询所有群文库资源
     *
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @param libid
     * @param resname
     * @param sysUser
     * @return PageInfo<Map>
     * @throws Exception
     */
    //@Cacheable
    public PageInfo<Map> findResourceByPaging(int page, int rows, String sort, String order, String libid, String resname, WhgSysUser sysUser, String state, String delstate, String recommend) throws Exception {
        //排序
        if (StringUtils.isEmpty(sort)) {
            sort = "crtdate";
            order = "desc";
        }


        //表名
        List<String> tablenames = new ArrayList<>();
        List<String> restypes = null;
        if (StringUtils.isNotEmpty(libid)) {//按库搜索
            WhgMassLibrary library = this.findById(libid);
            String tablename = this.__getTableName(library.getCultid(), library.getTablename());
            tablenames.add(tablename);
        } else {//全资源搜索
            List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
            List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUser.getId());
            Example example = new Example(WhgMassLibrary.class);
            example.createCriteria().andIn("cultid", cultids).andIn("deptid", deptids);
            List<WhgMassLibrary> librarys = this.whgMassLibraryMapper.selectByExample(example);
            if (librarys != null) {
                for (WhgMassLibrary library : librarys) {
                    String tablename = this.__getTableName(library.getCultid(), library.getTablename());
                    tablenames.add(tablename);
                }
            }
        }

        if (tablenames.size() == 0) {
            PageInfo emptyPi = new PageInfo(new ArrayList());
            emptyPi.setTotal(0);
            return emptyPi;
        }

        //查询条件
        List<Map<String, Object>> condition = new ArrayList<>();
        if (StringUtils.isNotEmpty(resname)) { //资源名称
            Map<String, Object> condi = new HashMap<>();
            condi.put("name", "resname");
            condi.put("opt", "like");
            condi.put("val", "%" + resname + "%");
            condition.add(condi);
        }
        if (StringUtils.isNotEmpty(state)) {//业务状态
            Map<String, Object> condi = new HashMap<>();
            condi.put("name", "state");
            condi.put("opt", "in");
            condi.put("val", "(" + state + ")");
            condition.add(condi);
        }
        if (StringUtils.isNotEmpty(recommend) && recommend.matches("\\d+")) {//推荐状态
            Map<String, Object> condi = new HashMap<>();
            condi.put("name", "recommend");
            condi.put("opt", "=");
            condi.put("val", Integer.parseInt(recommend));
            condition.add(condi);
        }
        if (StringUtils.isNotEmpty(delstate) && delstate.matches("\\d+")) {//回收状态
            Map<String, Object> condi = new HashMap<>();
            condi.put("name", "delstate");
            condi.put("opt", "=");
            condi.put("val", Integer.parseInt(delstate));
            condition.add(condi);
        }

        //查询
        PageHelper.startPage(page, rows);
        //List<Map> list = this.crtWhgMassLibraryMapper.selectResource(tablename, condition, sort, order);
        List<Map> list = this.crtWhgMassLibraryMapper.selectResource2(tablenames, condition, sort, order);
        return new PageInfo<Map>(list);
    }

    /**
     * 分页查询所有群文库资源
     *
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @param libid
     * @param resname
     * @param sysUser
     * @return PageInfo<Map>
     * @throws Exception
     */
    //@Cacheable
    public PageInfo<Map> findResourceBySharePaging(int page, int rows, String sort, String order, String libid, String resname, WhgSysUser sysUser, String state, String delstate, String recommend) throws Exception {
        //排序
        if (StringUtils.isEmpty(sort)) {
            sort = "crtdate";
            order = "desc";
        }


        //表名
        List<String> tablenames = new ArrayList<>();
        List<String> restypes = null;
        if (StringUtils.isNotEmpty(libid)) {//按库搜索
            WhgMassLibrary library = this.findById(libid);
            String tablename = this.__getTableName(library.getCultid(), library.getTablename());
            tablenames.add(tablename);
        } else {//全资源搜索
            /*List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUser.getId());
            List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUser.getId());
            Example example = new Example(WhgMassLibrary.class);
            example.createCriteria().andIn("cultid", cultids).andIn("deptid", deptids);*/
            WhgMassLibrary record = new WhgMassLibrary();
            //library.setResourcetype(id);
            record.setCultid(sysUser.getCultid());
            record.setState(EnumState.STATE_YES.getValue());
            List<WhgMassLibrary> librarys = this.findShareLibrary(null, null, record, sysUser);
            if (librarys != null) {
                for (WhgMassLibrary library : librarys) {
                    String tablename = this.__getTableName(library.getCultid(), library.getTablename());
                    tablenames.add(tablename);
                }
            }
        }

        if (tablenames.size() == 0) {
            PageInfo emptyPi = new PageInfo(new ArrayList());
            emptyPi.setTotal(0);
            return emptyPi;
        }

        //查询条件
        List<Map<String, Object>> condition = new ArrayList<>();
        if (StringUtils.isNotEmpty(resname)) { //资源名称
            Map<String, Object> condi = new HashMap<>();
            condi.put("name", "resname");
            condi.put("opt", "like");
            condi.put("val", "%" + resname + "%");
            condition.add(condi);
        }
        if (StringUtils.isNotEmpty(state)) {//业务状态
            Map<String, Object> condi = new HashMap<>();
            condi.put("name", "state");
            condi.put("opt", "in");
            condi.put("val", "(" + state + ")");
            condition.add(condi);
        }
        if (StringUtils.isNotEmpty(recommend) && recommend.matches("\\d+")) {//推荐状态
            Map<String, Object> condi = new HashMap<>();
            condi.put("name", "recommend");
            condi.put("opt", "=");
            condi.put("val", Integer.parseInt(recommend));
            condition.add(condi);
        }
        if (StringUtils.isNotEmpty(delstate) && delstate.matches("\\d+")) {//回收状态
            Map<String, Object> condi = new HashMap<>();
            condi.put("name", "delstate");
            condi.put("opt", "=");
            condi.put("val", Integer.parseInt(delstate));
            condition.add(condi);
        }

        //查询
        PageHelper.startPage(page, rows);
        //List<Map> list = this.crtWhgMassLibraryMapper.selectResource(tablename, condition, sort, order);
        List<Map> list = this.crtWhgMassLibraryMapper.selectResource2(tablenames, condition, sort, order);
        return new PageInfo<Map>(list);
    }

    /**
     * 根据资源ID分页查询信息
     *
     * @param resid 资源标识
     * @throws Exception
     */
    public Map findResourceById(String resid) throws Exception {
        if (StringUtils.isNotEmpty(resid)) {
            PageInfo<Map> pageInfo = this.findResourceByPaging(1, 1, null, null, null, resid, null, null);
            List<Map> list = pageInfo.getList();
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * 根据资源库和资源标识查询资源详情
     *
     * @param libid 资源库标识
     * @param resid 资源标识
     * @return Map
     * @throws Exception
     */
    public Map findResourceById(String libid, String resid) throws Exception {
        //排序
        String sort = "crtdate";
        String order = "desc";

        //表名
        WhgMassLibrary library = this.findById(libid);
        String tablename = this.__getTableName(library.getCultid(), library.getTablename());

        //查询条件
        List<Map<String, Object>> condition = new ArrayList<>();
        if (StringUtils.isNotEmpty(resid)) {
            Map<String, Object> condi = new HashMap<>();
            condi.put("name", "id");
            condi.put("opt", "=");
            condi.put("val", resid);
            condition.add(condi);
        }
        List<Map> list = this.crtWhgMassLibraryMapper.selectResource(tablename, condition, sort, order);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 群文库公字段
     *
     * @return
     * @throws Exception
     */
    private List<Map<String, String>> _getCommFields() throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        list.add(__getFieldMap("respicture", "varchar(1000)", "资源封面图片"));
        list.add(__getFieldMap("resname", "varchar(500)", "资源名称"));
        list.add(__getFieldMap("resauthor", "varchar(500)", "资源作者"));
        list.add(__getFieldMap("resarttype", "varchar(1000)", "艺术分类"));
        list.add(__getFieldMap("restype", "varchar(1000)", "资源分类"));
        list.add(__getFieldMap("restag", "varchar(1000)", "标签"));
        list.add(__getFieldMap("resurl", "varchar(1000)", "资源访问地址"));
        list.add(__getFieldMap("resorigin", "varchar(1000)", "资源来源说明"));
        list.add(__getFieldMap("resintroduce", "longtext", "资源简介"));
        list.add(__getFieldMap("resheight", "int", "图片资源高"));
        list.add(__getFieldMap("reswidth", "int", "图片资源宽"));
        list.add(__getFieldMap("ressize", "varchar(32)", "资源大小"));
        list.add(__getFieldMap("resmimetype", "varchar(32)", "资源媒体类型"));
        list.add(__getFieldMap("state", "int", "资源状态"));
        list.add(__getFieldMap("delstate", "int", "资源回收状态。1-已回收,0-未回收"));
        list.add(__getFieldMap("recommend", "int", "资源推荐状态。1-已推荐,0-未推荐"));
        list.add(__getFieldMap("publishtime", "datetime", "资源发布时间"));
        list.add(__getFieldMap("recommendtime", "datetime", "资源推荐时间"));
        list.add(__getFieldMap("libid", "varchar(32)", "资源库标识"));

        return list;
    }

    private Map<String, String> __getFieldMap(String name, String type, String comment) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("type", type);
        map.put("comment", comment);
        return map;
    }

    /**
     * 修改群文资源表单字段的临时状态为真实状态
     *
     * @param libid   资源库标识
     * @param adminid 管理员标识
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void updateFormFieldTempState(String libid, String adminid) throws Exception {
        WhgMassLibraryFormField field = new WhgMassLibraryFormField();
        field.setIstemp(0);
        Example example = new Example(WhgMassLibraryFormField.class);
        example.createCriteria().andEqualTo("istemp", 1).andEqualTo("crtuser", adminid).andEqualTo("libid", libid);
        this.whgMassLibraryFormFieldMapper.updateByExampleSelective(field, example);
    }

    /**
     * 添加群文库资源库辅助方法
     *
     * @param fields
     * @param values
     * @param fieldList
     * @param paramMap
     * @throws Exception
     */
    private void _parseParam(List<String> fields, List<Object> values, List<Map<String, String>> fieldList, Map<String, String> paramMap) throws Exception {
        for (Map<String, String> _map : fieldList) {
            String name = _map.get("name");
            if (!"id".equals(name) && !"crtdate".equals(name)) {
                if (paramMap.containsKey(name)) {
                    fields.add(name);
                    values.add(paramMap.get(name));
                }
            }
        }
    }

    private void _parseParam4edit(List<Map<String, Object>> fields, List<Map<String, String>> fieldList, Map<String, String> paramMap) throws Exception {
        for (Map<String, String> _map : fieldList) {
            String name = _map.get("name");
            if (!"id".equals(name) && !"crtdate".equals(name)) {
                if (paramMap.containsKey(name)) {
                    Map<String, Object> t_map = new HashMap<>();
                    t_map.put("name", name);
                    t_map.put("val", paramMap.get(name));
                    fields.add(t_map);
                }
            }
        }
    }

    private List<List<Object>> _parseParam4Child(List<String> fields, List<Map<String, String>> fieldList, Map<String, String> paramMap, String allIdx) throws Exception {
        for (Map<String, String> _map : fieldList) {
            String name = _map.get("name");
            if (!"id".equals(name) && !"crtdate".equals(name)) {
                if (paramMap.containsKey(name)) {
                    fields.add(name);
                }
            }
        }

        List<List<Object>> rtnList = new ArrayList<>();
        String[] idxArr = allIdx.split(",");
        for (String idx : idxArr) {
            List<Object> valList = new ArrayList();
            for (String name : fields) {
                String paramName = name + "_" + idx;
                String val = paramMap.get(paramName);
                valList.add(val);
            }
            rtnList.add(valList);
        }
        return rtnList;
    }

    /**
     * 通过OSS上传添加资源
     *
     * @param uid      管理员标识
     * @param libid    资源库标识
     * @param filename 资源KEY
     * @param size     资源大小
     * @param mimeType 资源媒体类型
     * @param height   图片资源高
     * @param width    图片资源宽
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void addResource4OSS(String uid, String libid, String filename, String size, String mimeType, String height, String width, String webRootPath) throws Exception {
        //根据资源标识，解析表和字段
        Map<String, List<Map<String, String>>> map = this._getDBTableAndField(libid, false);
        WhgMassLibrary library = this.findById(libid);

        //参数
        if (StringUtils.isNotEmpty(filename)) {
            filename = java.net.URLDecoder.decode(filename, "UTF-8");
        }
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("ressize", size);//资源大小
        if (StringUtils.isNotEmpty(mimeType) && mimeType.length() > 32) {
            mimeType = mimeType.substring(0, 32);
        }
        paramMap.put("resmimetype", mimeType);//资源类型
        int idx_1 = filename.lastIndexOf("/");
        int idx_2 = filename.lastIndexOf(".");
        String resname = filename.substring(idx_1 + 1, idx_2);//解析资源名称,直接取文件名
        paramMap.put("resname", resname); //资源默认名称为文件名
        paramMap.put("resurl", AliyunOssUtil.getUrl(filename));//资源访问地址
        if (EnumUploadType.TYPE_IMG.getValue().equals(library.getResourcetype())) {//如果是图片资源
            paramMap.put("respicture", AliyunOssUtil.getUrl(filename));//资源访问地址==图片封面地址
        }
        if (StringUtils.isNotEmpty(height)) {
            paramMap.put("resheight", height);
        }
        if (StringUtils.isNotEmpty(width)) {
            paramMap.put("reswidth", width);
        }

        //是否存在相同的资源名称
        String _tablename = this.__getTableName(library.getCultid(), library.getTablename());
        List<Map<String, Object>> condition = new ArrayList<>();
        Map<String, Object> condi_map = new HashMap<>();
        condi_map.put("name", "resname");
        condi_map.put("opt", "=");
        condi_map.put("val", resname);
        condition.add(condi_map);
        List<Map> list = this.crtWhgMassLibraryMapper.selectResource(_tablename, condition, "crtdate", "desc");
        String existid = null;
        if (list != null && list.size() > 0) {
            existid = (String) list.get(0).get("id");
        }

        //根据表的字段添加资源或者更新资源
        Date now = new Date();
        for (String key : map.keySet()) {
            String[] keyArr = key.split("@@@");
            String tablename = keyArr[0]; //表名
            String tablecomment = keyArr[1]; //表说明
            String[] tnArr = tablename.split("_");
            if (tnArr.length == 5) {//总表
                //解析字段和值
                List<String> fields = new ArrayList<>();//字段
                List<Object> values = new ArrayList<>();//值
                List<Map<String, String>> customfields = map.get(key);
                _parseParam(fields, values, customfields, paramMap);

                if (StringUtils.isEmpty(existid)) {//新增资源
                    String refid = IDUtils.getID32();
                    fields.add("id");
                    values.add(refid);//主键
                    fields.add("crtdate");
                    values.add(now);//创建时间
                    fields.add("crtuser");
                    values.add(uid);//创建人
                    fields.add("state");
                    values.add(1);//业务状态
                    fields.add("delstate");
                    values.add(0);//删除状态
                    fields.add("recommend");
                    values.add(0);//推荐状态
                    fields.add("libid");
                    values.add(library.getId());//资源库标识
                    fields.add("statemdfuser");
                    values.add(uid);//最后操作人
                    fields.add("statemdfdate");
                    values.add(now);//最后操作时间
                    this.crtWhgMassLibraryMapper.insertResource(tablename, fields, values);
                } else {//新增同名资源-只做修改操作
                    List<Map<String, Object>> _fields = new ArrayList<>();
                    for (Map<String, String> _map : customfields) {
                        String name = _map.get("name");
                        if (paramMap.containsKey(name)) {
                            Map<String, Object> setMap = new HashMap<>();
                            setMap.put("name", name);
                            setMap.put("val", paramMap.get(name));
                            _fields.add(setMap);
                        }
                    }
                    Map<String, Object> setMap = new HashMap<>();
                    setMap.put("name", "crtdate");
                    setMap.put("val", now);
                    _fields.add(setMap);
                    Map<String, Object> setMap2 = new HashMap<>();
                    setMap2.put("name", "crtuser");
                    setMap2.put("val", uid);
                    _fields.add(setMap2);
                    this.crtWhgMassLibraryMapper.updateResource(tablename, existid, _fields);
                }
            }
        }

        //如果是图片资源，需要生成缩放图片750_500|720_540|700_350三种格式的图片
        if (EnumUploadType.TYPE_IMG.getValue().equals(library.getResourcetype())) {
            //临时文件目录
            String tmpBasePath = System.getProperty("java.io.tmpdir");

            //获取水印图片
            BufferedImage image = whgSystemCultService.getCultSyPicture(webRootPath, library.getCultid());

            //获得原图片,保存到临时目录
            String srourceFile = AliyunOssUtil.oss_download(filename, tmpBasePath);

            //生成缩放图并存放到OSS中
            ImageUtil.saveZoomImg(libid, srourceFile, 750, 500, image, tmpBasePath);// 3比2
            ImageUtil.saveZoomImg(libid, srourceFile, 720, 540, image, tmpBasePath);// 4比3
            ImageUtil.saveZoomImg(libid, srourceFile, 700, 350, image, tmpBasePath);// 2比1
            ImageUtil.deleteTmpFile(srourceFile);//删除临时文件
        }
        //添加场馆配额关联
        quotaService.addQuotaRelation(library.getCultid(),size,1);
    }


    /**
     * 添加资源
     *
     * @param sysUser
     * @param libid
     * @param paramMap
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void addResource(WhgSysUser sysUser, String libid, Map<String, String> paramMap) throws Exception {
        //根据资源标识，解析表和字段
        Map<String, List<Map<String, String>>> map = this._getDBTableAndField(libid, false);

        //先添加总表
        String refid = "";
        for (String key : map.keySet()) {
            String[] keyArr = key.split("@@@");
            String tablename = keyArr[0]; //表名
            String[] tnArr = tablename.split("_");
            if (tnArr.length == 5) {//总表
                //解析字段和值
                List<String> fields = new ArrayList<>();
                List<Object> values = new ArrayList<>();
                List<Map<String, String>> customfields = map.get(key);
                _parseParam(fields, values, customfields, paramMap);
                refid = IDUtils.getID32();
                Date crtdate = new Date();
                fields.add("id");
                values.add(refid);//主键
                fields.add("crtdate");
                values.add(crtdate);//创建时间
                fields.add("crtuser");
                values.add(sysUser.getId());//创建人
                this.crtWhgMassLibraryMapper.insertResource(tablename, fields, values);
                //添加场馆配额关联
                quotaService.addQuotaRelation(sysUser.getCultid(),paramMap.get("ressize"),1);
            }
        }

        //再添加子表
        for (String key : map.keySet()) {
            String[] keyArr = key.split("@@@");
            String tablename = keyArr[0]; //表名
            String[] tnArr = tablename.split("_");
            if (tnArr.length == 3) {//子表
                String columnid = tablename.substring(tablename.lastIndexOf("_"));//子表对应的列
                String column_ids_arr = paramMap.get(columnid + "_idx");//子表所有参数的下标

                //解析字段和值
                List<String> fields = new ArrayList<>();
                List<Object> values = new ArrayList<>();
                List<Map<String, String>> customfields = map.get(key);
                List<List<Object>> valList = _parseParam4Child(fields, customfields, paramMap, column_ids_arr);

                for (List<Object> valLst : valList) {
                    String id = IDUtils.getID32();
                    Date crtdate = new Date();
                    fields.add("id");
                    values.add(id);//主键
                    fields.add("crtdate");
                    values.add(crtdate);//创建时间
                    fields.add("crtuser");
                    values.add(sysUser.getId());//创建人
                    fields.add("refid");
                    values.add(refid);
                    this.crtWhgMassLibraryMapper.insertResource(tablename, fields, valLst);
                }
            }
        }
    }

    /**
     * 编辑群文库资源
     *
     * @param sysUser
     * @param libid
     * @param paramMap
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void editResource(WhgSysUser sysUser, String libid, String resid, Map<String, String> paramMap, String webRootPath) throws Exception {
        //通过资源库获取所有表和字段
        Map<String, List<Map<String, String>>> map = this._getDBTableAndField(libid, false);

        String refid = resid;//资源唯一标识
        paramMap.put("id", refid);

        //文本编辑器对超链接进行过滤处理
        String resintroduce = paramMap.get("resintroduce");
        resintroduce = resintroduce.replaceAll("<a href[^>]*>", "");
        resintroduce = resintroduce.replaceAll("</a>", "");
        paramMap.put("resintroduce", resintroduce);

        for (String key : map.keySet()) {
            String[] keyArr = key.split("@@@");
            String tablename = keyArr[0]; //表名
            String[] tnArr = tablename.split("_");
            if (tnArr.length == 3) {//子表
                //先删除关联关系
                this.crtWhgMassLibraryMapper.deleteResource(tablename, null, refid);

                //再插入子表记录
                String columnid = tablename.substring(tablename.lastIndexOf("_"));//子表对应的列
                String column_ids_arr = paramMap.get(columnid + "_idx");//子表所有参数的下标
                if (StringUtils.isNotEmpty(column_ids_arr)) {
                    //解析字段和值
                    List<String> fields = new ArrayList<>();
                    List<Object> values = new ArrayList<>();
                    List<Map<String, String>> customfields = map.get(key);//所有字段
                    List<List<Object>> valList = _parseParam4Child(fields, customfields, paramMap, column_ids_arr);

                    for (List<Object> valLst : valList) {
                        String id = IDUtils.getID32();
                        Date crtdate = new Date();
                        fields.add("id");
                        values.add(id);//主键
                        fields.add("refid");
                        values.add(refid);//关联主表
                        fields.add("crtdate");
                        values.add(crtdate);//创建时间
                        fields.add("crtuser");
                        values.add(sysUser.getId());//创建人
                        this.crtWhgMassLibraryMapper.insertResource(tablename, fields, valLst);
                    }
                }
            } else {//总表
                //如果是视频资源，图片处理
                WhgMassLibrary library = this.findById(libid);
                if (EnumUploadType.TYPE_VIDEO.getValue().equals(library.getResourcetype())) {//视频资源
                    String respicture = paramMap.get("respicture");
                    if (StringUtils.isNotEmpty(respicture)) {//如果修改了视频图片
                        FileInputStream fis = null;
                        try {
                            //删除之前的封面图片
                            Map resourceInfo = this.findResourceById(libid, resid);
                            String old_respicture = (String) resourceInfo.get("respicture");
                            if (StringUtils.isNotEmpty(old_respicture) && !old_respicture.startsWith("/")) {
                                AliyunOssUtil.oss_deleteFile(old_respicture);//原图
                                AliyunOssUtil.oss_deleteFile(ImageUtil.getImgName_750_500(old_respicture));//缩放图
                                AliyunOssUtil.oss_deleteFile(ImageUtil.getImgName_720_540(old_respicture));
                                AliyunOssUtil.oss_deleteFile(ImageUtil.getImgName_700_350(old_respicture));
                            }

                            //更新新的封面图片
                            String imgBasePath = this.env.getProperty("upload.local.addr");
                            respicture = respicture.replaceAll("\\/", "\\" + File.separator);
                            File imgFile = new File(imgBasePath, respicture);
                            fis = new FileInputStream(imgFile);
                            String suffix = respicture.substring(respicture.lastIndexOf("."));
                            String key_res = "lib" + library.getId() + "/" + IDUtils.getID32() + suffix;//放在同一个资源库中
                            AliyunOssUtil.uploadFile_file(fis, key_res);
                            respicture = AliyunOssUtil.getUrl(key_res);
                            paramMap.put("respicture", respicture);

                            //生成缩放图片
                            if (StringUtils.isNotEmpty(respicture)) {
                                //临时文件目录
                                String tmpBasePath = System.getProperty("java.io.tmpdir");

                                //获取水印图片
                                BufferedImage image = whgSystemCultService.getCultSyPicture(webRootPath, library.getCultid());

                                //生成缩放图并存放到OSS中
                                ImageUtil.saveZoomImg(libid, imgFile.getAbsolutePath(), 750, 500, image, tmpBasePath, key_res);// 3比2
                                ImageUtil.saveZoomImg(libid, imgFile.getAbsolutePath(), 720, 540, image, tmpBasePath, key_res);// 4比3
                                ImageUtil.saveZoomImg(libid, imgFile.getAbsolutePath(), 700, 350, image, tmpBasePath, key_res);// 2比1
                            }

                            //删除本地文件
                            String bakImgaName = ImageUtil.getImgName_bak(imgFile.getAbsolutePath());
                            File bakFile = new File(bakImgaName);
                            if (bakFile.exists()) {
                                bakFile.delete();
                            }
                            if (imgFile.exists()) {
                                imgFile.delete();
                            }
                        } catch (Exception e) {

                        } finally {
                            if (fis != null) {
                                fis.close();
                            }
                        }
                    } else {
                        paramMap.remove("respicture");
                    }
                }

                //解析字段和值
                List<Map<String, String>> customfields = map.get(key);
                List<Map<String, Object>> fields = new ArrayList<>();
                _parseParam4edit(fields, customfields, paramMap);
                this.crtWhgMassLibraryMapper.updateResource(tablename, refid, fields);
            }
        }

        //如果资源是发布状态，同步修改关系中的值
        Map resourceMap = this.findResourceById(libid, resid);
        String resourceState = String.valueOf(resourceMap.get("state"));
        if ((EnumBizState.STATE_PUB.getValue() + "").equals(resourceState)) {
            String enturl = (String) resourceMap.get("resurl");
            String entname = (String) resourceMap.get("resname");
            String deourl = (String) resourceMap.get("respicture");
            WhgResource whgResource = new WhgResource();
            whgResource.setEnturl(enturl);
            whgResource.setName(entname);
            whgResource.setDeourl(deourl);
            this.whgResourceService.t_edit4Resource(whgResource, libid, resid);
        }
    }

    /**
     * 分享群文库资源
     *
     * @param sysUser
     * @param libid
     * @param cultids
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void shareResource(WhgSysUser sysUser, String libid, String cultid) throws Exception {
        if (StringUtils.isNotEmpty(libid)) {
            WhgMassLibrary library = this.findById(libid);
            if (StringUtils.isNotBlank(cultid)) {
                cultid = cultid.substring(0, cultid.length() - 1);
            }
            library.setSharecultid(cultid);
            Example example = new Example(WhgMassLibrary.class);
            example.createCriteria().andEqualTo("id", libid);
            this.whgMassLibraryMapper.updateByExampleSelective(library, example);
        }
    }

    /**
     * 修改资源业务状态
     *
     * @param libid 资源库唯一标识
     * @param id    资源标识
     * @param state 业务状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void updateResourceState(WhgSysUser sysUser, String libid, String id, String state) throws Exception {
        WhgMassLibrary library = this.findById(libid);
        String tablename = this.__getTableName(library.getCultid(), library.getTablename());
        List<Map<String, Object>> _fields = new ArrayList<>();
        Map<String, Object> setMap = new HashMap<>();
        setMap.put("name", "state");
        setMap.put("val", Integer.parseInt(state));
        _fields.add(setMap);
        if ((EnumBizState.STATE_PUB.getValue() + "").equals(state)) {//发布时间和发布人
            Map<String, Object> setMap1 = new HashMap<>();
            setMap1.put("name", "publishtime");
            setMap1.put("val", new Date());
            _fields.add(setMap1);
            Map<String, Object> setMap2 = new HashMap<>();
            setMap2.put("name", "publisher");
            setMap2.put("val", sysUser.getId());
            _fields.add(setMap2);
        } else if ((EnumBizState.STATE_CAN_PUB.getValue() + "").equals(state) || (EnumBizState.STATE_CAN_EDIT.getValue() + "").equals(state)) {//审核时间和审核人
            Map<String, Object> setMap1 = new HashMap<>();
            setMap1.put("name", "checkdate");
            setMap1.put("val", new Date());
            _fields.add(setMap1);
            Map<String, Object> setMap2 = new HashMap<>();
            setMap2.put("name", "checkor");
            setMap2.put("val", sysUser.getId());
            _fields.add(setMap2);
        } else if ((EnumBizState.STATE_NO_PUB.getValue() + "").equals(state)) {//下架，需要删除所有其它关联关系
            this.whgResourceService.t_del4Resource(libid, id);
        }
        this.crtWhgMassLibraryMapper.updateResource(tablename, id, _fields);
    }

    /**
     * 修改资源业务状态
     *
     * @param libid 资源库唯一标识
     * @param id    资源标识
     * @param state 业务状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void batchSubmit(WhgSysUser sysUser, String libidstr, String ids, String state) throws Exception {
        String[] libids = libidstr.split(",");
        for (int i = 0; i < libids.length; i++) {
            String libid = libids[i];
            WhgMassLibrary library = this.findById(libid);
            String tablename = this.__getTableName(library.getCultid(), library.getTablename());
            List<Map<String, Object>> _fields = new ArrayList<>();
            Map<String, Object> setMap = new HashMap<>();
            setMap.put("name", "state");
            setMap.put("val", Integer.parseInt(state));
            _fields.add(setMap);
            if ((EnumBizState.STATE_PUB.getValue() + "").equals(state)) {//发布时间和发布人
                Map<String, Object> setMap1 = new HashMap<>();
                setMap1.put("name", "publishtime");
                setMap1.put("val", new Date());
                _fields.add(setMap1);
                Map<String, Object> setMap2 = new HashMap<>();
                setMap2.put("name", "publisher");
                setMap2.put("val", sysUser.getId());
                _fields.add(setMap2);
            } else if ((EnumBizState.STATE_CAN_PUB.getValue() + "").equals(state) || (EnumBizState.STATE_CAN_EDIT.getValue() + "").equals(state)) {//审核时间和审核人
                Map<String, Object> setMap1 = new HashMap<>();
                setMap1.put("name", "checkdate");
                setMap1.put("val", new Date());
                _fields.add(setMap1);
                Map<String, Object> setMap2 = new HashMap<>();
                setMap2.put("name", "checkor");
                setMap2.put("val", sysUser.getId());
                _fields.add(setMap2);
            }
            this.crtWhgMassLibraryMapper.updateResource(tablename, ids.split(",")[i], _fields);
        }

    }

    /**
     * 修改删除状态
     *
     * @param libid    资源库标识
     * @param id       资源标识
     * @param delstate 删除状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void updateResourceDelstate(String libid, String id, String delstate) throws Exception {
        WhgMassLibrary library = this.findById(libid);
        String tablename = this.__getTableName(library.getCultid(), library.getTablename());
        List<Map<String, Object>> _fields = new ArrayList<>();
        Map<String, Object> setMap = new HashMap<>();
        setMap.put("name", "delstate");
        setMap.put("val", Integer.parseInt(delstate));
        if (Integer.parseInt(delstate) == 0) {
            Map<String, Object> setMap2 = new HashMap<>();
            setMap2.put("name", "state");
            setMap2.put("val", 1);
            _fields.add(setMap2);
        }
        _fields.add(setMap);
        this.crtWhgMassLibraryMapper.updateResource(tablename, id, _fields);
    }

    /**
     * 资源库是否存在名称相同的资源
     *
     * @param libid   资源库ID
     * @param resName 资源名称
     * @return true-存在 false-不存在
     * @throws Exception
     */
    public boolean existResource(String libid, String resName) throws Exception {
        boolean exist = true;
        try {
            WhgMassLibrary library = this.findById(libid);
            String tablename = this.__getTableName(library.getCultid(), library.getTablename());
            List<Map<String, Object>> condition = new ArrayList<>();
            Map<String, Object> condi_map = new HashMap<>();
            condi_map.put("name", "resname");
            condi_map.put("opt", "=");
            condi_map.put("val", resName);
            condition.add(condi_map);
            Map<String, Object> condi_map2 = new HashMap<>();
            condi_map2.put("name", "libid");
            condi_map2.put("opt", "=");
            condi_map2.put("val", libid);
            condition.add(condi_map2);
            List<Map> list = this.crtWhgMassLibraryMapper.selectResource(tablename, condition, "crtdate", "desc");
            if (list != null && list.size() > 0) {
                exist = true;
            } else {
                exist = false;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return exist;
    }

    /**
     * 资源库是否禁用失效
     *
     * @param libid 资源库标识
     * @return
     * @throws Exception
     */
    public boolean libDisable(String libid) throws Exception {
        boolean disabled = true;
        try {
            WhgMassLibrary library = new WhgMassLibrary();
            library.setId(libid);
            library.setState(EnumState.STATE_YES.getValue());
            int cnt = this.whgMassLibraryMapper.selectCount(library);
            if (cnt == 1) {
                disabled = false;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return disabled;
    }

    /**
     * 查询资源库必填的字段
     *
     * @param libid 资源库标识
     * @return
     * @throws Exception
     */
    public List<WhgMassLibraryFormField> queryLibFormRequiredField(String libid, String resid) throws Exception {
        List<WhgMassLibraryFormField> requirdFields = new ArrayList<WhgMassLibraryFormField>();

        List<WhgMassLibraryFormField> list = null;
        WhgMassLibraryFormField formField = new WhgMassLibraryFormField();
        formField.setLibid(libid);
        formField.setFieldrequired(1);

        //资源详情
        Map res = this.findResourceById(libid, resid);
        List<WhgMassLibraryFormField> fields = this.whgMassLibraryFormFieldMapper.select(formField);
        if (fields != null) {
            for (WhgMassLibraryFormField field : fields) {
                String fieldcode = field.getFieldcode();
                if (res != null) {
                    //是否配置了值
                    boolean hasVal = false;

                    //未设置值
                    if (res.containsKey(fieldcode)) {
                        Object fieldVal = res.get(fieldcode);
                        if (fieldVal != null) {
                            if (StringUtils.isNotEmpty(fieldVal.toString())) {
                                hasVal = true;
                            }
                        }
                    }

                    //如果未设置值，返回给前端
                    if (!hasVal) {
                        requirdFields.add(field);
                    }
                }
            }
        }


        return requirdFields;
    }

    /**
     * 更新资源推荐状态
     *
     * @param libid     资源库唯一标识
     * @param id        资源标识
     * @param recommend 推荐状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void updateResourceRecommend(String libid, String id, String recommend) throws Exception {
        WhgMassLibrary library = this.findById(libid);
        String tablename = this.__getTableName(library.getCultid(), library.getTablename());
        List<Map<String, Object>> _fields = new ArrayList<>();
        Map<String, Object> setMap = new HashMap<>();
        setMap.put("name", "recommend");
        setMap.put("val", Integer.parseInt(recommend));
        _fields.add(setMap);
        if ("1".equals(recommend)) {//设置推荐时间
            Map<String, Object> setMap1 = new HashMap<>();
            setMap1.put("name", "recommendtime");
            setMap1.put("val", new Date());
            _fields.add(setMap1);
        }
        this.crtWhgMassLibraryMapper.updateResource(tablename, id, _fields);
    }

    /**
     * 删除资源
     *
     * @param force true-强制删除 false-有引用不能删除
     * @param libid
     * @param id
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void deleteResource(boolean force, String libid, String id) throws Exception {
        WhgMassLibrary library = this.findById(libid);
        String tablename = this.__getTableName(library.getCultid(), library.getTablename());
        //非强制的时候提示不能删除
        if (!force) {
            WhgResource _resource = new WhgResource();
            _resource.setLibid(libid);
            _resource.setResid(id);
            int cnt = this.whgResourceService.countResourceByCondition(_resource);
            if (cnt > 0) {
                throw new Exception("被引用的资源不能删除");
            }
        }

        Map resource = this.findResourceById(libid, id);
        int delstate = ((Number) resource.get("delstate")).intValue();
        if (false && EnumStateDel.STATE_DEL_NO.getValue() == delstate) {//回收
            List<Map<String, Object>> _fields = new ArrayList<>();
            Map<String, Object> setMap = new HashMap<>();
            setMap.put("name", "delstate");
            setMap.put("val", 1);
            _fields.add(setMap);
            this.crtWhgMassLibraryMapper.updateResource(tablename, id, _fields);
        } else {//删除资源
            //删除关联关系
            WhgResource _resource = new WhgResource();
            _resource.setLibid(libid);
            _resource.setResid(id);
            this.whgResourceService.deleteResourceByCondition(_resource);

            //删除OSS中的资源
            List<Map<String, Object>> condition = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("name", "id");
            map.put("opt", "=");
            map.put("val", id);
            condition.add(map);
            List<Map> list = this.crtWhgMassLibraryMapper.selectResource(tablename, condition, "crtdate", "desc");
            if (list != null && list.size() > 0) {
                //删除资源
                String resurl = (String) ((Map) list.get(0)).get("resurl");
                if (resurl.startsWith("http://")) {
                    resurl = resurl.substring(7);
                }
                int idx = resurl.indexOf("/");
                if (idx > -1) {
                    resurl = resurl.substring(idx + 1);
                }
                String n_resurl = java.net.URLDecoder.decode(resurl, "UTF-8");
                AliyunOssUtil.oss_deleteFile(n_resurl);

                //删除图片资源缩放的图片
                if (library.getResourcetype().equals(EnumUploadType.TYPE_IMG.getValue())) {
                    AliyunOssUtil.oss_deleteFile(ImageUtil.getImgName_750_500(resurl));
                    AliyunOssUtil.oss_deleteFile(ImageUtil.getImgName_720_540(resurl));
                    AliyunOssUtil.oss_deleteFile(ImageUtil.getImgName_700_350(resurl));
                }

                //删除视频对应的图片资源
                String respicture = (String) ((Map) list.get(0)).get("respicture");
                if (StringUtils.isNotEmpty(respicture) && library.getResourcetype().equals(EnumUploadType.TYPE_VIDEO.getValue())) {
                    if (respicture.startsWith("http://")) {
                        respicture = respicture.substring(7);
                    }
                    int idx2 = respicture.indexOf("/");
                    if (idx2 > -1) {
                        respicture = respicture.substring(idx2 + 1);
                    }
                    String n_respicture = java.net.URLDecoder.decode(respicture, "UTF-8");
                    AliyunOssUtil.oss_deleteFile(n_respicture);
                    AliyunOssUtil.oss_deleteFile(ImageUtil.getImgName_750_500(respicture));
                    AliyunOssUtil.oss_deleteFile(ImageUtil.getImgName_720_540(respicture));
                    AliyunOssUtil.oss_deleteFile(ImageUtil.getImgName_700_350(respicture));
                }
            }

            //删除资源库表中的资源
            this.crtWhgMassLibraryMapper.deleteResource(tablename, id, null);
            //添加场馆配额关联
            quotaService.addQuotaRelation(library.getCultid(),resource.get("ressize").toString(),2);
        }
    }

    @CacheEvict(allEntries = true)
    private void _doAdds(String libid, String adds, List<String> fieldids, List<String> formids) {
        //添加项  row@column@fid2
        if (StringUtils.isNotEmpty(adds)) {
            String[] items = adds.split(",");
            for (String item : items) {
                String[] itemArr = item.split("@");
                if (itemArr.length == 4) {
                    String columnid = itemArr[0];//表单行标识
                    String rows = itemArr[1];//第几行
                    String columns = itemArr[2];//第几列
                    String fieldid = itemArr[3];//字段标识

                    WhgMassLibraryForm form = new WhgMassLibraryForm();
                    form.setId(columnid);
                    form.setLibid(libid);
                    form.setRows(Integer.parseInt(rows));
                    form.setColumns(Integer.parseInt(columns));
                    form.setIstemp(0);
                    this.whgMassLibraryFormMapper.updateByPrimaryKeySelective(form);
                    formids.add(form.getId());//有效的表单列标识

                    WhgMassLibraryFormField field = new WhgMassLibraryFormField();
                    field.setId(fieldid);
                    field.setFormid(columnid);
                    field.setLibid(libid);
                    field.setIstemp(0);
                    this.whgMassLibraryFormFieldMapper.updateByPrimaryKeySelective(field);
                    fieldids.add(field.getId());//有效的字段标识
                }
            }
        }
    }

    @CacheEvict(allEntries = true)
    private void _doEdits(String libid, String edits, List<String> fieldids, List<String> formids) {
        //eidts:formid@row@column@fid1@fid2
        if (StringUtils.isNotEmpty(edits)) {
            String[] items = edits.split(",");
            for (String item : items) {
                String[] itemArr = item.split("@");
                if (itemArr.length == 5) {
                    String columnid = itemArr[0];//表单行标识
                    String rows = itemArr[1];//第几行
                    String columns = itemArr[2];//第几列
                    String fid1 = itemArr[3];//字段标识1-已生效的
                    String fid2 = itemArr[4];//字段标识2-临时的

                    WhgMassLibraryForm form = new WhgMassLibraryForm();
                    form.setId(columnid);
                    form.setLibid(libid);
                    form.setRows(Integer.parseInt(rows));
                    form.setColumns(Integer.parseInt(columns));
                    form.setIstemp(0);
                    this.whgMassLibraryFormMapper.updateByPrimaryKeySelective(form);
                    formids.add(form.getId());

                    //修改编辑的数据为真实数据
                    this.whgMassLibraryFormFieldMapper.deleteByPrimaryKey(fid1);//删除以前生效的
                    WhgMassLibraryFormField field = new WhgMassLibraryFormField();
                    field.setId(fid2);
                    field.setFormid(columnid);
                    field.setLibid(libid);
                    field.setIstemp(0);
                    int row = this.whgMassLibraryFormFieldMapper.updateByPrimaryKeySelective(field);//把临时数据改成生效的
                    fieldids.add(fid2);
                }
            }
        }
    }

    @CacheEvict(allEntries = true)
    private void _doDels(String libid, String dels, List<String> fieldids, List<String> formids) {
        //删除项 formid@row@column@fid1
        if (StringUtils.isNotEmpty(dels)) {
            String[] items = dels.split(",");
            for (String item : items) {
                String[] itemArr = item.split("@");
                if (itemArr.length == 4) {
                    String columnid = itemArr[0];//表单行标识
                    String rows = itemArr[1];//第几行
                    String columns = itemArr[2];//第几列
                    String fid1 = itemArr[3];//字段标识
                    this.whgMassLibraryFormFieldMapper.deleteByPrimaryKey(fid1);
                }
            }
        }
    }

    @CacheEvict(allEntries = true)
    private void _doKeeps(String libid, String keeps, List<String> fieldids, List<String> formids) {
        //formid@row@column@fid1
        if (StringUtils.isNotEmpty(keeps)) {
            String[] items = keeps.split(",");
            for (String item : items) {
                String[] itemArr = item.split("@");
                if (itemArr.length == 4) {
                    String columnid = itemArr[0];
                    String rows = itemArr[1];//第几行
                    String columns = itemArr[2];//第几列
                    String fieldid = itemArr[3];//fid1

                    WhgMassLibraryForm form = new WhgMassLibraryForm();
                    form.setId(columnid);
                    form.setLibid(libid);
                    form.setRows(Integer.parseInt(rows));
                    form.setColumns(Integer.parseInt(columns));
                    form.setIstemp(0);
                    this.whgMassLibraryFormMapper.updateByPrimaryKeySelective(form);
                    formids.add(form.getId());

                    WhgMassLibraryFormField field = new WhgMassLibraryFormField();
                    field.setId(fieldid);//
                    field.setLibid(libid);
                    field.setFormid(columnid);
                    field.setIstemp(0);
                    this.whgMassLibraryFormFieldMapper.updateByPrimaryKeySelective(field);
                    fieldids.add(fieldid);
                }
            }
        }
    }

    @CacheEvict(allEntries = true)
    private void _cleans(WhgSysUser sysUser, String libid, List<String> fieldids, List<String> formids) {
        //自定义字段- 清除此用户的所有临时数据和无效的真实数据
        Example example = new Example(WhgMassLibraryFormField.class);
        WhgMassLibraryFormField field_c = new WhgMassLibraryFormField();
        field_c.setIstemp(1);
        field_c.setCrtuser(sysUser.getId());
        Example.Criteria c = example.createCriteria();
        c.andEqualTo(field_c);
        if (fieldids != null && fieldids.size() > 0) {
            Example.Criteria d = example.createCriteria();
            d.andEqualTo("istemp", 0).andEqualTo("libid", libid).andNotIn("id", fieldids);
            example.or(d);
        }
        this.whgMassLibraryFormFieldMapper.deleteByExample(example);

        //自定义表单-清除此用户的所有临时数据和无效的真实数据
        Example example2 = new Example(WhgMassLibraryForm.class);
        WhgMassLibraryForm field_d = new WhgMassLibraryForm();
        field_d.setIstemp(1);
        field_d.setCrtuser(sysUser.getId());
        Example.Criteria c_2 = example2.createCriteria();
        c_2.andEqualTo(field_d);
        if (formids != null && formids.size() > 0) {
            Example.Criteria d_2 = example.createCriteria();
            d_2.andEqualTo("istemp", 0).andEqualTo("libid", libid).andNotIn("id", formids);
            example2.or(d_2);
        }
        this.whgMassLibraryFormMapper.deleteByExample(example2);
    }

    public void _doCutstom(WhgSysUser sysUser, String libid, String adds, String edits, String dels, String keeps) {
        //最后生效的所有字段
        List<String> fieldids = new ArrayList();
        List<String> formids = new ArrayList();

        //添加项  formid@row@column@fid2
        _doAdds(libid, adds, fieldids, formids);

        //编辑项 formid@row@column@fid1@fid2
        _doEdits(libid, edits, fieldids, formids);

        //删除项 formid@row@column@fid1
        _doDels(libid, dels, fieldids, formids);

        //不变项
        _doKeeps(libid, keeps, fieldids, formids);

        //清理 当前用户的临时数据和此配置的真实数据
        _cleans(sysUser, libid, fieldids, formids);
    }

    /**
     * 获得所有表
     *
     * @param libid 资源库标识
     * @throws Exception
     */
    @Cacheable
    private Map<String, List<Map<String, String>>> _getDBTableAndField(String libid, boolean allowTemp) throws Exception {
        WhgMassLibrary library = this.whgMassLibraryMapper.selectByPrimaryKey(libid);

        //所有的表，所有字段
        Map<String, List<Map<String, String>>> tableFields = new HashMap<>();

        //数据库表名
        String tableName = this.__getTableName(library.getCultid(), library.getTablename());
        tableName = tableName + "@@@" + library.getName();
        List<Map<String, String>> tabelFields_1 = new ArrayList<>();
        tableFields.put(tableName, tabelFields_1);

        //获得要创建的表和字段
        Map<String, String> columnidMap = new HashMap<>();//子表名
        WhgMassLibraryForm form = new WhgMassLibraryForm();
        form.setLibid(libid);
        form.setIstemp(0);
        List<WhgMassLibraryForm> listForm = this.findLibraryForm(form);
        if (listForm != null) {
            for (WhgMassLibraryForm _form : listForm) {
                if (_form.getColumntype().intValue() == 2) {//子表
                    String ptn = tableName.substring(0, tableName.indexOf("@@@"));
                    String childTableName = "whg_resource_" + _form.getId();
                    childTableName = childTableName + "@@@" + _form.getLabelname();
                    columnidMap.put(_form.getId(), childTableName);

                    List<Map<String, String>> tabelFields_2 = new ArrayList<>();
                    tableFields.put(childTableName, tabelFields_2);
                }
            }
        }

        //获取表的所有字段
        WhgMassLibraryFormField field = new WhgMassLibraryFormField();
        field.setLibid(libid);
        if (!allowTemp) {
            field.setIstemp(0);
        } else {
            field.setIstemp(1);
        }
        List<WhgMassLibraryFormField> listField = this.findLibraryFormField(field);
        if (listField != null) {
            for (WhgMassLibraryFormField _field : listField) {
                String columnid = _field.getFormid();
                if (columnidMap.containsKey(columnid)) {//子表-字段
                    String childTableName = columnidMap.get(columnid);
                    tableFields.get(childTableName).add(_genTableField(_field.getFieldcode(), _field.getFieldtype(), _field.getFieldname()));
                } else {//总表字段
                    tableFields.get(tableName).add(_genTableField(_field.getFieldcode(), _field.getFieldtype(), _field.getFieldname()));
                }
            }
        }
        //总表添加公共字段
        tableFields.get(tableName).addAll(this._getCommFields());

        //子表添加关联字段
        for (String key : tableFields.keySet()) {
            String[] keyArr = key.split("@@@");
            String tablename = keyArr[0];
            String[] tnArr = tablename.split("_");
            if (tnArr.length == 3) {//子表添加一个字段
                tableFields.get(key).add(this.__getFieldMap("refid", "varchar(32)", "关联字段"));
            }
        }

        return tableFields;
    }

    /**
     * 比较资源库修改后各个表新增的字段
     *
     * @param oldFields 修改前的字段
     * @param newFields 修改后的字段
     * @return 新增的字段 ，只能新增
     * @throws Exception
     */
    private Map<String, List<Map<String, String>>> _compareTableField(Map<String, List<Map<String, String>>> oldFields, Map<String, List<Map<String, String>>> newFields) throws Exception {
        Map<String, List<Map<String, String>>> tableFields = new HashMap<>();

        Map<String, List<Map<String, String>>> new_oldFields = new HashMap<>();
        Map<String, List<Map<String, String>>> new_newFields = new HashMap<>();
        Map<String, String> tableComment = new HashMap<>();//表对应的表描述
        if (oldFields != null) {
            for (String tableName : oldFields.keySet()) {
                String[] arr = tableName.split("@@@");
                String tn = arr[0];
                String tc = arr[1];
                new_oldFields.put(tn, oldFields.get(tableName));
            }
        }
        if (newFields != null) {
            for (String tableName : newFields.keySet()) {
                String[] arr = tableName.split("@@@");
                String tn = arr[0];
                String tc = arr[1];
                new_newFields.put(tn, newFields.get(tableName));
                tableComment.put(tn, tc);
            }
        }
        for (String tableName : new_newFields.keySet()) {
            List<Map<String, String>> _newfields = new_newFields.get(tableName);//修改之前的字段
            List<Map<String, String>> _oldfields = new_oldFields.get(tableName);//修改之后的字段
            if (new_oldFields.containsKey(tableName)) {//只新增字段
                for (int i = 0; i < _newfields.size(); i++) {
                    String columnName = _newfields.get(i).get("name");//新的字段名
                    if (!inList(columnName, _oldfields)) {//不存在老的字段中，就是新增的字段
                        //修改字段
                        List<Map<String, String>> list_t = new ArrayList<>();
                        list_t.add(_newfields.get(i));
                        this.crtWhgMassLibraryMapper.alertTableAddField(tableName, list_t);
                    }
                }
            } else {//新增表
                Map<String, List<Map<String, String>>> newTableFields = new HashMap<>();
                tableName = tableName + "@@@" + tableComment.get(tableName);
                newTableFields.put(tableName, _newfields);
                this._creatTable(newTableFields);
            }
        }
        for (String tableName : new_oldFields.keySet()) {
            List<Map<String, String>> _newfields = new_newFields.get(tableName);//修改之前的字段
            List<Map<String, String>> _oldfields = new_oldFields.get(tableName);//修改之后的字段
            if (!new_newFields.containsKey(tableName)) {//删除子表
                this.crtWhgMassLibraryMapper.dropByTableName(tableName);
            } else {
                for (int i = 0; i < _oldfields.size(); i++) {
                    String columnName = _oldfields.get(i).get("name");//老的字段名
                    if (!inList(columnName, _newfields)) {//要删除的字段
                        this.crtWhgMassLibraryMapper.alertTableDropField(tableName, columnName);
                    }
                }
            }
        }
        return tableFields;
    }


    /**
     * 新建资源时创建表
     *
     * @param tableFields
     * @throws Exception
     */
    private void _creatTable(Map<String, List<Map<String, String>>> tableFields) throws Exception {
        if (tableFields != null) {
            for (String tableName : tableFields.keySet()) {
                List<Map<String, String>> list = tableFields.get(tableName);
                String[] arr = tableName.split("@@@");
                String tn = arr[0];
                String tc = arr[1];
                this.crtWhgMassLibraryMapper.createTable(tn, tc, list);
            }
        }
    }

    /**
     * 修改资源库时修改相应的表结构
     *
     * @param tableFields
     * @throws Exception
     */
    private void _alertTable(Map<String, List<Map<String, String>>> tableFields) throws Exception {
        if (tableFields != null) {
            for (String tableName : tableFields.keySet()) {
                List<Map<String, String>> list = tableFields.get(tableName);
                String[] arr = tableName.split("@@@");
                String tn = arr[0];
                String tc = arr[1];
                if (list != null) {//只能一条一条执行
                    for (Map<String, String> _map : list) {
                        List<Map<String, String>> list_t = new ArrayList<>();
                        list_t.add(_map);
                        this.crtWhgMassLibraryMapper.alertTableAddField(tn, list_t);
                    }
                }
            }
        }
    }

    /**
     * 字段名是否在列表中
     *
     * @param name 字段名
     * @param list 所有字段
     * @return true-存在 false-不存在
     * @throws Exception
     */
    private boolean inList(String name, List<Map<String, String>> list) throws Exception {
        boolean isIn = false;
        if (list != null) {
            for (Map<String, String> map : list) {
                String _name = map.get("name");
                if (_name.equals(name)) {
                    isIn = true;
                    break;
                }
            }
        }
        return isIn;
    }


    private Map<String, String> _genTableField(String name, String type, String comment) {
        String columntype = "";
        if ("easyui-textbox".equals(type)) {//普通文本
            columntype = "varchar(2000)";
        } else if ("easyui-combobox".equals(type)) {//下拉选择
            columntype = "varchar(500)";
        } else if ("easyui-datebox".equals(type)) {//日期
            columntype = "date";
        } else if ("easyui-datetimebox".equals(type)) {//日期时间
            columntype = "datetime";
        } else if ("easyui-numberspinner".equals(type)) {//数字
            columntype = "int";
        } else if ("radio".equals(type)) {//单选
            columntype = "varchar(500)";
        } else if ("checkbox".equals(type)) {//多选
            columntype = "varchar(500)";
        } else if ("textarea".equals(type)) {//多行文本
            columntype = "varchar(2000)";
        } else if ("imginput".equals(type)) {//图片
            columntype = "varchar(1000)";
        } else if ("fileinput".equals(type)) {//附件
            columntype = "varchar(1000)";
        } else if ("richtext".equals(type)) {//富文本
            columntype = "longtext";
        }

        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("type", columntype);
        map.put("comment", comment);
        return map;
    }


    /**
     * 添加资源库
     *
     * @param sysUser
     * @param library
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public String add(WhgSysUser sysUser, WhgMassLibrary library, String adds, String edits, String dels, String keeps) throws Exception {
        library.setId(IDUtils.getID32());
        library.setCrtuser(sysUser.getId());
        library.setCrtdate(new Date());
        library.setState(EnumState.STATE_YES.getValue());

        JSONArray optinfoarr = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", sysUser.getId());
        jsonObject.put("opt", "add");
        jsonObject.put("opttime", library.getCrtdate());
        optinfoarr.add(jsonObject);
        library.setOptinfo(optinfoarr.toJSONString());

        //名称不能重复
        WhgMassLibrary condi1 = new WhgMassLibrary();
        condi1.setName(library.getName());
        condi1.setCultid(library.getCultid());
        int cnt = this.whgMassLibraryMapper.selectCount(condi1);
        if (cnt > 0) {
            throw new Exception("资源库名称不能重复");
        }
        //代码不能重复
        WhgMassLibrary condi2 = new WhgMassLibrary();
        condi2.setTablename(library.getTablename());
        condi2.setCultid(library.getCultid());
        cnt = this.whgMassLibraryMapper.selectCount(condi2);
        if (cnt > 0) {
            throw new Exception("资源库代码不能重复");
        }

        this.whgMassLibraryMapper.insert(library);

        //自定义字段处理
        _doCutstom(sysUser, library.getId(), adds, edits, dels, keeps);

        //创建资源库表
        this._creatTable(this._getDBTableAndField(library.getId(), false));

        //一个资源库对应一个OSS目录, 添加时创建一个目录，删除时删除目录
        AliyunOssUtil.oss_createDir("lib" + library.getId());

        return library.getId();
    }

    /**
     * 编辑资源库
     *
     * @param sysUser
     * @param library
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void edit(WhgSysUser sysUser, WhgMassLibrary library, String adds, String edits, String dels, String keeps) throws Exception {
        //改之前的表信息
        Map<String, List<Map<String, String>>> oldTableInfo = this._getDBTableAndField(library.getId(), false);

        library.setLastoptuser(sysUser.getId());
        library.setLastoptdate(new Date());

        WhgMassLibrary oldLibrary = this.whgMassLibraryMapper.selectByPrimaryKey(library.getId());
        JSONArray optinfoarr = JSONArray.parseArray(oldLibrary.getOptinfo());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", sysUser.getId());
        jsonObject.put("opt", "edit");
        jsonObject.put("opttime", library.getLastoptdate());
        optinfoarr.add(jsonObject);
        library.setOptinfo(optinfoarr.toJSONString());

        //名称不能重复
        WhgMassLibrary condi1 = new WhgMassLibrary();
        condi1.setName(library.getName());
        condi1.setCultid(library.getCultid());
        Example example1 = new Example(WhgMassLibrary.class);
        example1.createCriteria().andEqualTo(condi1).andNotEqualTo("id", library.getId());
        int cnt = this.whgMassLibraryMapper.selectCountByExample(example1);
        if (cnt > 0) {
            throw new Exception("资源库名称不能重复");
        }
        //代码不能重复
        WhgMassLibrary condi2 = new WhgMassLibrary();
        condi2.setTablename(library.getTablename());
        condi2.setCultid(library.getCultid());
        Example example2 = new Example(WhgMassLibrary.class);
        example2.createCriteria().andEqualTo(condi2).andNotEqualTo("id", library.getId());
        cnt = this.whgMassLibraryMapper.selectCountByExample(example2);
        if (cnt > 0) {
            throw new Exception("资源库代码不能重复");
        }
        this.whgMassLibraryMapper.updateByPrimaryKeySelective(library);

        //处理自定义表单
        this._doCutstom(sysUser, library.getId(), adds, edits, dels, keeps);

        //改之后的表信息
        Map<String, List<Map<String, String>>> newTableInfo = this._getDBTableAndField(library.getId(), false);
        //修改表结构， 只添加字段
        this._compareTableField(oldTableInfo, newTableInfo);
    }

    /**
     * 修改状态
     *
     * @param sysUser   管理员
     * @param id        标识
     * @param fromState 修改之前的状态
     * @param toState   修改之后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void updateState(WhgSysUser sysUser, String id, Integer fromState, Integer toState) throws Exception {
        WhgMassLibrary oldLibrary = this.whgMassLibraryMapper.selectByPrimaryKey(id);
        String opt = null;
        if (oldLibrary == null || toState == null || !ArrayUtils.contains(new Integer[]{EnumState.STATE_YES.getValue(), EnumState.STATE_NO.getValue()}, toState)) {
            throw new Exception("请求参数不正确");
        } else if (EnumState.STATE_YES.getValue() == toState) {
            opt = "start-up";
        } else if (EnumState.STATE_NO.getValue() == toState) {
            opt = "stop-it";
            /*String tableName = __getTableName(oldLibrary.getCultid(), oldLibrary.getTablename());
            int cnt = this.crtWhgMassLibraryMapper.countByTableName(tableName);
            if(cnt > 0){
                throw new Exception("不能停用正在被使用的资源库");
            }*/
        }
        JSONArray optinfoarr = JSONArray.parseArray(oldLibrary.getOptinfo());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userid", sysUser.getId());
        jsonObject.put("opt", opt);
        jsonObject.put("opttime", new Date());
        optinfoarr.add(jsonObject);

        WhgMassLibrary condi1 = new WhgMassLibrary();
        condi1.setState(toState);
        condi1.setOptinfo(optinfoarr.toJSONString());
        Example example1 = new Example(WhgMassLibrary.class);
        example1.createCriteria().andEqualTo("id", id).andEqualTo("state", fromState);
        this.whgMassLibraryMapper.updateByExampleSelective(condi1, example1);
    }

    /**
     * 资源库表名
     *
     * @param cultid    文化馆标识
     * @param tableName 表报后缀
     * @return 表全名
     * @throws Exception
     */
    private String __getTableName(String cultid, String tableName) throws Exception {
        return "whg_mass_library_" + cultid + "_" + tableName;
    }

    /**
     * 删除资源库
     *
     * @param id 资源标识
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void delete(String id, boolean force) throws Exception {
        WhgMassLibrary oldLibrary = this.whgMassLibraryMapper.selectByPrimaryKey(id);
        if (oldLibrary == null) {
            throw new Exception("请求参数不正确");
        }
        String tableName = __getTableName(oldLibrary.getCultid(), oldLibrary.getTablename());
        if (!force) {
            int cnt = this.crtWhgMassLibraryMapper.countByTableName(tableName);
            if (cnt > 0) {
                throw new Exception("不能删除正在被使用的资源库");
            }
        }

        //获取所有表和字段
        Map<String, List<Map<String, String>>> allTable = this._getDBTableAndField(oldLibrary.getId(), false);
        if (allTable != null) {
            for (String key : allTable.keySet()) {
                String[] keyArr = key.split("@@@");
                String tablename = keyArr[0]; //表名
                String[] tnArr = tablename.split("_");
                if (tnArr.length == 3) {//子表
                    this.crtWhgMassLibraryMapper.dropByTableName(tablename);
                }
            }
        }

        //删除资源对应关系
        WhgResource whgResource = new WhgResource();
        whgResource.setLibid(id);
        this.whgResourceService.deleteResourceByCondition(whgResource);

        //删除资源库表单中的字段
        WhgMassLibraryFormField field = new WhgMassLibraryFormField();
        field.setLibid(id);
        this.whgMassLibraryFormFieldMapper.delete(field);

        //删除资源库表单信息
        WhgMassLibraryForm form = new WhgMassLibraryForm();
        form.setLibid(id);
        this.whgMassLibraryFormMapper.delete(form);

        //删除资源库
        this.whgMassLibraryMapper.deleteByPrimaryKey(id);

        //删除资源库表
        this.crtWhgMassLibraryMapper.dropByTableName(tableName);

        //一个资源库对应一个OSS目录, 添加时创建一个目录，删除时删除目录
        AliyunOssUtil.oss_deleteDir("lib" + id);
    }

    /**
     * 保存表单中的行信息
     *
     * @param sysUser 管理员
     * @param forms   表单行信息
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public List<String> saveRow4temp(WhgSysUser sysUser, List<WhgMassLibraryForm> forms) throws Exception {
        List<String> ids = new ArrayList<>();
        if (forms != null) {
            for (WhgMassLibraryForm form : forms) {
                form.setId(IDUtils.getID32());
                form.setCrtuser(sysUser.getId());
                form.setIstemp(1);
                int rows = this.whgMassLibraryFormMapper.insert(form);
                if (rows != 1) {
                    throw new Exception("保存失败");
                }
                ids.add(form.getId());
            }
        }
        return ids;
    }

    /**
     * 保存自定义的属性
     *
     * @param sysUser 管理员
     * @param field   自定义字段
     * @return 字段标识
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgMassLibraryFormField saveField4temp(WhgSysUser sysUser, WhgMassLibraryFormField field, String fid1) throws Exception {
        if (field != null) {
            field.setCrtuser(sysUser.getId());
            field.setIstemp(1);//临时修改

            //添加或者修改字段
            if (field.getId() != null && StringUtils.isNotEmpty(field.getId())) {//修改,保存过数据库一次
                //fieldcode不能重复
                Example example = new Example(WhgMassLibraryFormField.class);
                WhgMassLibraryFormField condition = new WhgMassLibraryFormField();
                condition.setFieldcode(field.getFieldcode());
                condition.setIstemp(0);
                if (StringUtils.isNotEmpty(field.getLibid())) {
                    condition.setLibid(field.getLibid());
                } else {
                    condition.setIstemp(field.getIstemp());
                    condition.setCrtuser(field.getCrtuser());
                }
                List<String> extids = new ArrayList<>();
                extids.add(field.getId());
                if (StringUtils.isNotEmpty(fid1)) {
                    extids.add(fid1);
                }
                example.createCriteria().andEqualTo(condition).andNotIn("id", extids);
                int cnt = this.whgMassLibraryFormFieldMapper.selectCountByExample(example);
                if (cnt > 0) {
                    throw new Exception("同一个库字段编码不能重复");
                }
                this.whgMassLibraryFormFieldMapper.updateByPrimaryKeySelective(field);
            } else {//添加
                //fieldcode不能重复
                Example example = new Example(WhgMassLibraryFormField.class);
                WhgMassLibraryFormField condition = new WhgMassLibraryFormField();
                condition.setFieldcode(field.getFieldcode());//一个资源库不能有相同的字段编码
                condition.setIstemp(0);
                if (StringUtils.isNotEmpty(field.getLibid())) {
                    condition.setLibid(field.getLibid());
                } else {
                    condition.setIstemp(field.getIstemp());
                    condition.setCrtuser(field.getCrtuser());
                }
                Example.Criteria c = example.createCriteria();
                c.andEqualTo(condition);
                if (StringUtils.isNotEmpty(fid1)) {
                    c.andNotEqualTo("id", fid1);//排除已经生效的字段
                }
                int cnt = this.whgMassLibraryFormFieldMapper.selectCountByExample(example);
                if (cnt > 0) {
                    throw new Exception("同一个库字段编码不能重复");
                }
                field.setId(IDUtils.getID32());
                this.whgMassLibraryFormFieldMapper.insert(field);
            }
        }
        return field;
    }


    /**
     * 给用户分配资源库权限
     *
     * @param userid
     * @param libids
     * @param power
     * @param flag
     * @throws Exception
     */
    public void updateUserAuthByLib(String userid, String libids, String power, String flag) throws Exception {
        String[] libid = libids.split(",");
        for (int i = 0; i < libid.length; i++) {
            if (StringUtils.isNotBlank(libid[i])) {
                Example example = new Example(WhgMassUserAuth.class);
                Example.Criteria c = example.createCriteria();
                c.andEqualTo("userid", userid);
                c.andEqualTo("masslibraryid", libid[i]);
                List<WhgMassUserAuth> userAuth = this.whgMassUserAuthMapper.selectByExample(example);
                if (userAuth.size() > 0) {// 修改
                    if (flag.equals("0")) {// 取消权限
                        WhgMassUserAuth auth = userAuth.get(0);
                        if (power.equals("view")) {// 取消查看，则下载权限也没有
                            auth.setAuthority(auth.getAuthority().replaceAll(power, "").replaceAll("download", ""));
                        } else {
                            auth.setAuthority(auth.getAuthority().replaceAll(power, ""));
                        }
                        int rows = this.whgMassUserAuthMapper.updateByExample(auth, example);
                        if (rows <= 0) {
                            throw new Exception("操作失败");
                        }
                    } else {// 添加权限
                        WhgMassUserAuth auth = userAuth.get(0);
                        String authority = auth.getAuthority();
                        if (StringUtils.isBlank(authority) || !authority.contains(power)) {//当权限为空或不包含当权限时(针对批量操作)
                            if (power.equals("download")) {// 有下载权限，则查看权限也有
                                String str = "";
                                if (StringUtils.isNotBlank(authority)) {
                                    if (authority.contains("view")) {
                                        str = authority + ",download";
                                    } else {
                                        str = authority + ",view,download";
                                    }
                                } else {
                                    str = "view,download";
                                }
                                auth.setAuthority(str);
                            } else {
                                String str = StringUtils.isNotBlank(auth.getAuthority()) ? auth.getAuthority() + "," + power : power;
                                auth.setAuthority(str);
                            }
                        }
                        int rows = this.whgMassUserAuthMapper.updateByExample(auth, example);
                        if (rows <= 0) {
                            throw new Exception("操作失败");
                        }
                    }
                } else {// 添加
                    WhgMassUserAuth auth = new WhgMassUserAuth();
                    if (power.equals("download")) {
                        auth.setAuthority("view,download");
                    } else {
                        auth.setAuthority(power);
                    }
                    auth.setMasslibraryid(libid[i]);
                    auth.setUserid(userid);
                    int rows = this.whgMassUserAuthMapper.insert(auth);
                    if (rows <= 0) {
                        throw new Exception("操作失败");
                    }
                }
            }
        }
    }


    /**
     * 查询数量
     *
     * @param resid
     * @param massType 1:资源库，2：资源
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Map selectInfoByResId(String libid, String resid, String userid, Integer massType) throws Exception {
        Map map = new HashMap();
        Example example = new Example(WhgMassView.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("resid", massType == 1 ? libid : resid);
        Integer viewCount = whgMassViewMapper.selectCountByExample(example);
        map.put("viewCount", viewCount);

        if (massType == 1) {
            WhgMassLibrary record = new WhgMassLibrary();
            record.setId(libid);
            WhgMassLibrary _lib = this.whgMassLibraryMapper.selectOne(record);
            if (_lib.getCultid() != null) {
                WhgSysCult sysCult = this.whgSysCultMapper.selectByPrimaryKey(_lib.getCultid());
                if (sysCult != null) {
                    map.put("cultname", sysCult.getName());
                }
            }
            //资源数
            Integer resourcecount = 0;
            Map exp = new HashMap();
            exp.put("state", EnumBizState.STATE_PUB.getValue());
            exp.put("delstate", EnumStateDel.STATE_DEL_NO.getValue());
            if (StringUtils.isNotEmpty(_lib.getId())) {//按库搜索
                WhgMassLibrary library = this.findById(_lib.getId());
                String tablename = this.__getTableName(library.getCultid(), library.getTablename());
                resourcecount = this.crtWhgMassLibraryMapper.countByTableNameExample(tablename, exp);
            }
            map.put("resourcecount", resourcecount);

            WhgMassUserAuth auth = getMassUserAuth(userid, libid);
            map.put("auth", auth != null ? auth.getAuthority().indexOf("view") : -1);   //查看权限
            map.put("api", auth != null ? auth.getAuthority().indexOf("api") : -1);   //api接口权限

            WhgMassApplyUser view = new WhgMassApplyUser();
            view.setMasslibraryid(libid);
            view.setUserid(userid);
            view.setApplytype("view");
            view = applyUserMapper.selectOne(view);
            if (view!=null)   map.put("view", view);

            WhgMassApplyUser download = new WhgMassApplyUser();
            download.setMasslibraryid(libid);
            download.setUserid(userid);
            download.setApplytype("download");
            download = applyUserMapper.selectOne(download);
            if (download!=null)   map.put("download", download);

        } else {
            //下载数
            Example example1 = new Example(WhgMassDown.class);
            Example.Criteria c1 = example1.createCriteria();
            c1.andEqualTo("resid", resid);
            map.put("downcount", this.whgMassDownMapper.selectCountByExample(example1));
        }

        return map;
    }


    /**
     * 查询用户资源库权限
     *
     * @param userid
     * @param libid
     * @return
     */
    public WhgMassUserAuth getMassUserAuth(String userid, String libid) {
        WhgMassUserAuth auth = new WhgMassUserAuth();
        auth.setUserid(userid);
        auth.setMasslibraryid(libid);
        return this.whgMassUserAuthMapper.selectOne(auth);
    }


    public PageInfo<Map> applyUserList(int page, int rows, WhgMassApplyUser applyUser, String account,
                                       String contactnum, WhgSysUser sysUser) {
        Map map = new HashMap();
        if (StringUtils.isNotEmpty(applyUser.getApplystate())) {
            map.put("applystate", applyUser.getApplystate());
        }
        /*if (StringUtils.isNotEmpty(applyUser.getApplytype())) {
            map.put("applytype","%" +  applyUser.getApplytype() + "%");
        }*/
        if (StringUtils.isNotEmpty(applyUser.getCultid())) {
            map.put("cultid", applyUser.getCultid());
        }
        if (StringUtils.isNotEmpty(sysUser.getCultid())) {
            map.put("libcultid", sysUser.getCultid());
        }
        if (StringUtils.isNotEmpty(account)) {
            map.put("account", "%" + account + "%");
        }
        if (StringUtils.isNotEmpty(contactnum)) {
            map.put("contactnum", "%" + contactnum + "%");
        }
        //查询
        PageHelper.startPage(page, rows);
        List list = this.whgMassLibraryMapper.apply_user_list(map);
        return new PageInfo<Map>(list);
    }

    /**
     * 更新文化馆状态
     *
     * @param ids       申请ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState   修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void applyuser_updstate(String ids, String toState, WhgSysUser admin) {
        if (ids != null && toState != null) {
            String[] idArr = ids.split(",");
            Example example = new Example(WhgSysUser.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            WhgMassApplyUser record = new WhgMassApplyUser();
            record.setStatemdfdate(new Date());
            record.setStatemdfuser(admin.getId());
            record.setApplystate(toState);
            int i = this.applyUserMapper.updateByExampleSelective(record, example);

            for (int j = 0; j < idArr.length; j++) {
                String id = idArr[j];
                WhgMassApplyUser o =  this.applyUserMapper.selectByPrimaryKey(id);

                WhgMassUserAuth auth = new WhgMassUserAuth();
                auth.setUserid(o.getUserid());
                auth.setMasslibraryid(o.getMasslibraryid());
                auth =  this.whgMassUserAuthMapper.selectOne(auth);
                //同意
                if(toState.equals("1")){
                    if(auth!=null){
                        if(o.getApplytype().equals("view")){
                            if(auth.getAuthority().contains("download")){
                                auth.setAuthority("view,download");
                            }else{
                                auth.setAuthority(o.getApplytype());
                            }
                        }else{
                            auth.setAuthority("view,download");
                        }
                        this.whgMassUserAuthMapper.updateByPrimaryKeySelective(auth);
                    }else{
                        auth = new WhgMassUserAuth();
                        auth.setUserid(o.getUserid());
                        auth.setMasslibraryid(o.getMasslibraryid());
                        if(o.getApplytype().equals("view")){
                            auth.setAuthority(o.getApplytype());
                        }else{
                            auth.setAuthority("view,download");
                        }
                        this.whgMassUserAuthMapper.insertSelective(auth);
                    }
                //拒绝
                }else{
                    if(auth!=null){
                        if(o.getApplytype().equals("view")){
                            this.whgMassUserAuthMapper.delete(auth);
                        }else{
                            auth.setAuthority("view");
                            this.whgMassUserAuthMapper.updateByPrimaryKeySelective(auth);
                        }
                    }
                }
            }
        }
    }

    public Object apply(String userid, String applytype, String masslibraryid) {
            ApiResultBean arb = new ApiResultBean();
            if (userid==null || userid.isEmpty()){
                arb.setCode(103);
                arb.setMsg("用户信息为空");
                return arb;
            }
            WhgMassApplyUser applyUser = new WhgMassApplyUser();
            applyUser.setId(IDUtils.getID());
            applyUser.setMasslibraryid(masslibraryid);
            applyUser.setUserid(userid);
            applyUser.setApplytime(new Date());
            applyUser.setApplytype(applytype);
            applyUser.setApplystate("0");
            this.applyUserMapper.insertSelective(applyUser);
            return arb;
    }

    public void saveTxtUrl(String lineTxt,long fileSize) {
        try {
                this.whgMassLibraryMapper.saveTxtUrl(fileSize, lineTxt);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param sysUser
     * @param libid
     * @param danghao 档号
     * @param resname 资源名
     * @param resintroduce 描述
     * @param resorigin 来源
     * @param resarttype 门类
     * @param resauthor 作者/责任人
     * @param restype 类型：视频，音频，图片，文档
     * @throws Exception
     */
    public void addExcelResource(WhgSysUser sysUser, String libid, String danghao,String resname,
            String resintroduce,String resorigin,String resarttype,String resauthor,String restype) throws Exception {
        //Get resource file url and file size based on file number
        Map<String,String> map = this.whgMassLibraryMapper.getResUrl("%"+danghao+"%");
        if(map!=null){
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("resname",resname);
            if(StringUtils.isNotEmpty(resintroduce))  paramMap.put("resintroduce",resintroduce);
            if(StringUtils.isNotEmpty(resorigin)) paramMap.put("resorigin",resorigin);
            if(StringUtils.isNotEmpty(resauthor)) paramMap.put("resauthor",resauthor);

            paramMap.put("libid",libid);
            paramMap.put("state","1");
            paramMap.put("delstate","0");
            String resurl = map.get("resurl");
            String ressize = map.get("ressize");

            paramMap.put("resurl", resurl);
            paramMap.put("ressize", ressize);
            paramMap.put("statemdfuser",sysUser.getId());

            if(restype.indexOf("视频")>=0){
                paramMap.put("resmimetype","video/mp4");
            }else if(restype.indexOf("图片")>=0){
                paramMap.put("resmimetype","image/jpeg");
            }else if(restype.indexOf("音频")>=0){
                paramMap.put("resmimetype","audio/mp3");
            }else if(restype.indexOf("文档")>=0){
                paramMap.put("resmimetype","application/"+resurl.substring(resurl.lastIndexOf(".")+1));
            }

            if(StringUtils.isNotEmpty(restype)){
                WhgYwiType yi = new WhgYwiType();
                yi.setType(1);
                yi.setName(resarttype);
                List<WhgYwiType> list=this.whgYunweiTypeService.query(yi);
                if(list.size()>0){
                    paramMap.put("resarttype",list.get(0).getId());
                }
            }

            //使用Date
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("当前时间：" + sdf.format(d));

            paramMap.put("statemdfdate", sdf.format(d));
            addResource(sysUser,libid,paramMap);
        }else{
            System.out.println("没有找到资源========"+resname);
        }

    }
}
