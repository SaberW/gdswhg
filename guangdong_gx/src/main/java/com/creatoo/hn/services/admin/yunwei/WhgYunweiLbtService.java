package com.creatoo.hn.services.admin.yunwei;

import com.creatoo.hn.dao.mapper.WhgYwiLbtMapper;
import com.creatoo.hn.dao.mapper.admin.WhgYunweiLbtCustomMapper;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiLbt;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统管理的轮播图配置service
 *
 * @author luzhihuai
 * @version 1-201703
 *          Created by Administrator on 2017/3/20.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgYwiLbt", keyGenerator = "simpleKeyGenerator")
public class WhgYunweiLbtService extends BaseService{
    /**
     * 文化馆服务类
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    /**
     * 文化馆DAO
     */
    @Autowired
    private WhgYwiLbtMapper whgYwiLbtMapper;

    /**
     * 自定义轮播图查询
     */
    @Autowired
    private WhgYunweiLbtCustomMapper whgYunweiLbtCustomMapper;

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @Cacheable
    public PageInfo<WhgYwiLbt> t_srchList4p(int page,int rows,String name,String cultid) throws Exception {

        //开始分页
        PageHelper.startPage(page, rows);
        //搜索条件
        Example example = new Example(WhgYwiLbt.class);
        Example.Criteria c = example.createCriteria();
        if (name != null && !"".equals(name)) {
            c.andLike("name", "%" + name + "%");
        }
        if (cultid != null && !"".equals(cultid)) {
            c.andEqualTo("cultid",  cultid );
        }

        c.andEqualTo("state", 1);
        c.andEqualTo("delstate", 0);
        example.setOrderByClause("crtdate");
        List<WhgYwiLbt> typeList = this.whgYwiLbtMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 区域管理员分页查询轮播图配置
     * @param page 第几页
     * @param rows 每页数
     * @param level 1-全省站轮播图, 2-全市站轮播图
     * @param levelVal 省或者市的名称
     * @return 轮播图的配置
     * @throws Exception
     */
    public PageInfo t_srchList4p4area(int page, int rows,String province, String city, Integer state, Integer type)throws Exception{
        PageHelper.startPage(page, rows);
        List<Map> list = this.whgYunweiLbtCustomMapper.queryList(type, province, city, state);
        PageInfo<Map> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 查询可以推送到全省全市站的轮播图
     * @param page 第几页
     * @param rows 每页数
     * @param province 省名称
     * @param city 市名称
     * @param type 轮播图类型
     * @return 轮播图的配置
     * @throws Exception
     */
    public PageInfo t_srchList4p4cult(int page, int rows,String province, String city, Integer type, String name)throws Exception{
        PageHelper.startPage(page, rows);
        List<Map> list = null;

        if(StringUtils.isNotEmpty(name)){
            name = "%"+name+"%";
        }
        if(StringUtils.isNotEmpty(province)){
            list = this.whgYunweiLbtCustomMapper.queryList4Province(type, province, name);
        }else if(StringUtils.isNotEmpty(city)){
            list = this.whgYunweiLbtCustomMapper.queryList4City(type, city, name);
        }
        PageInfo<Map> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 列表查询
     *
     * @return
     * @throws Exception
     */
    public PageInfo<WhgYwiLbt> t_srchList(WhgYwiLbt whgYwiLbt,int page,int rows,List list) throws Exception {
        //搜索条件
        Example example = new Example(WhgYwiLbt.class);
        Example.Criteria c = example.createCriteria();
        if(whgYwiLbt.getProvince()!=null&&!whgYwiLbt.getProvince().trim().equals("")){
            c.andEqualTo("province",whgYwiLbt.getProvince() );
            whgYwiLbt.setProvince(null);
        }
        if(whgYwiLbt.getCity()!=null&&!whgYwiLbt.getCity().trim().equals("")){
            c.andEqualTo("city",whgYwiLbt.getCity() );
            whgYwiLbt.setCity(null);
        }
        if(whgYwiLbt.getCultid()==null&&list!=null){//文化馆list
            c.andIn("cultid",list) ;
            whgYwiLbt.setCultid(null);
        }
        c.andEqualTo(whgYwiLbt);
        if(whgYwiLbt != null && (StringUtils.isNotEmpty(whgYwiLbt.getProvince()) || StringUtils.isNotEmpty(whgYwiLbt.getCity()))){
            example.setOrderByClause("areaidx,crtdate desc");
        }else{
            example.setOrderByClause("crtdate desc");
        }
        //开始分页
        PageHelper.startPage(page, rows);
        return new PageInfo<>(this.whgYwiLbtMapper.selectByExample(example));
    }

    /**
     * 查询单条记录
     * @param id id
     * @return 对象
     * @throws Exception
     */
    @Cacheable
    public WhgYwiLbt t_srchOne(String id)throws Exception{
        if(id==null) return null;
        WhgYwiLbt record = new WhgYwiLbt();
        record.setId(id);
        return this.whgYwiLbtMapper.selectOne(record);
    }

    /**
     * 添加
     *
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgSysUser user, WhgYwiLbt wyl) throws Exception {
        WhgYwiLbt whgYwiLbt = new WhgYwiLbt();
        if (wyl.getName() != null && !"".equals(wyl.getName())) {
            whgYwiLbt.setName(wyl.getName());
        }
        if (wyl.getPicture() != null && !"".equals(wyl.getPicture())) {
            whgYwiLbt.setPicture(wyl.getPicture());
        }
        if (wyl.getUrl() != null && !"".equals(wyl.getUrl())) {
            whgYwiLbt.setUrl(wyl.getUrl());
        }
        if (wyl.getType() != null && !"".equals(wyl.getType())) {
            whgYwiLbt.setType(wyl.getType());
        }
        if (wyl.getToproject() != null && !"".equals(wyl.getToproject())) {
            whgYwiLbt.setToproject(wyl.getToproject());
        }
        if (wyl.getCultid() != null && !"".equals(wyl.getCultid())) {
            whgYwiLbt.setCultid(wyl.getCultid());
        }
        if(StringUtils.isNotEmpty(wyl.getProvince())){
            whgYwiLbt.setProvince(wyl.getProvince());
        }
        if(StringUtils.isNotEmpty(wyl.getCity())){
            whgYwiLbt.setCity(wyl.getCity());
        }
        if (StringUtils.isNotEmpty(wyl.getRemark())) {
            whgYwiLbt.setRemark(wyl.getRemark());
        }

        whgYwiLbt.setId(IDUtils.getID());
        whgYwiLbt.setState(EnumState.STATE_YES.getValue());
        whgYwiLbt.setCrtdate(new Date());
        whgYwiLbt.setCrtuser(user.getId());
        whgYwiLbt.setStatemdfuser(user.getId());
        int result = this.whgYwiLbtMapper.insertSelective(whgYwiLbt);
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
    public void t_edit(String id, WhgSysUser user, WhgYwiLbt wyl) throws Exception {

        WhgYwiLbt whgYwiLbt = this.whgYwiLbtMapper.selectByPrimaryKey(id);

//        Example example = new Example(WhgYwiLbt.class);
//        Example.Criteria c = example.createCriteria();
//        c.andNotEqualTo("id", wyl.getId());

//        whgYwiLbt=this.whgYwiLbtMapper.selectCountByExample(example);

        if (whgYwiLbt != null) {
            whgYwiLbt.setName(wyl.getName());
            whgYwiLbt.setPicture(wyl.getPicture());
            whgYwiLbt.setUrl(wyl.getUrl());
            whgYwiLbt.setCultid(wyl.getCultid());
            whgYwiLbt.setToproject(wyl.getToproject());
            whgYwiLbt.setStatemdfdate(new Date());
            whgYwiLbt.setStatemdfuser(user.getId());
            if(StringUtils.isNotEmpty(wyl.getProvince())){
                whgYwiLbt.setProvince(wyl.getProvince());
            }
            if(StringUtils.isNotEmpty(wyl.getCity())){
                whgYwiLbt.setCity(wyl.getCity());
            }
            if (StringUtils.isNotEmpty(wyl.getRemark())) {
                whgYwiLbt.setRemark(wyl.getRemark());
            }
        }
        int result = this.whgYwiLbtMapper.updateByPrimaryKeySelective(whgYwiLbt);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 更新状态
     *
     * @param paramMap
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updstate(String id, int state) throws Exception {
        WhgYwiLbt whgYwiLbt = this.whgYwiLbtMapper.selectByPrimaryKey(id);
        whgYwiLbt.setState(state);
        if(EnumState.STATE_NO.getValue() == state){
            if(StringUtils.isNotEmpty(whgYwiLbt.getCultid())) {
                whgYwiLbt.setProvince(null);
                whgYwiLbt.setCity(null);
            }
        }
        int result = this.whgYwiLbtMapper.updateByPrimaryKey(whgYwiLbt);
        if (result != 1) {
            throw new Exception("编辑数据失败！");
        }
    }

    /**
     * 删除
     *
     * @param request
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception {
        int result = this.whgYwiLbtMapper.deleteByPrimaryKey(id);
        if (result != 1) {
            throw new Exception("删除数据失败！");
        }
    }

    /**
     * 移动
     * @param id
     * @param type
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_move(String id, String type, String dataScope, String clazz) throws Exception{
        WhgYwiLbt thisObj = this.t_srchOne(id);

        List<WhgYwiLbt> lbtList = null;
        WhgYwiLbt whgYwiLbt = new WhgYwiLbt();
        if("province".equals(dataScope)){
            whgYwiLbt.setProvince(thisObj.getProvince());
            whgYwiLbt.setType(clazz);
            lbtList = this.t_srchList(whgYwiLbt, 1, 100, null).getList();
        }else if("city".equals(dataScope)){
            whgYwiLbt.setCity(thisObj.getCity());
            whgYwiLbt.setType(clazz);
            lbtList = this.t_srchList(whgYwiLbt, 1, 100, null).getList();
        }else{
            List<String> cultids = new ArrayList<>();
            cultids.add(thisObj.getCultid());
            whgYwiLbt.setType(clazz);
            lbtList = this.t_srchList(whgYwiLbt, 1, 100, null).getList();
        }

        //重置
        if("top".equals(type)){
            for(int i=0; i<lbtList.size(); i++){
                WhgYwiLbt updLbt = lbtList.get(i);
                int thisIdx = i+2;
                if(updLbt.getId().equals(id)){
                    thisIdx = 1;
                }
                updLbt.setAreaidx(thisIdx);
                this.whgYwiLbtMapper.updateByPrimaryKey(updLbt);
            }
        }else if("up".equals(type)){
            WhgYwiLbt preLbt = null;
            int preIdx = -1;
            boolean preEnd = false;
            for(int i=0; i<lbtList.size(); i++){
                WhgYwiLbt updLbt = lbtList.get(i);
                int thisIdx = i+1;
                if(updLbt.getId().equals(id)){
                    preIdx = thisIdx;
                    thisIdx = thisIdx -1;
                    preEnd = true;
                }
                if(!preEnd){
                    preLbt = updLbt;
                }
                updLbt.setAreaidx(thisIdx);
                this.whgYwiLbtMapper.updateByPrimaryKey(updLbt);
            }
            if(preIdx > -1) {
                preLbt.setAreaidx(preIdx);
                this.whgYwiLbtMapper.updateByPrimaryKey(preLbt);
            }
        }

    }

    /**
     * 推到省或者市
     * @param id
     * @param type
     * @param cultid
     */
    public ResponseBean t_ToRecommend(String id, int type) throws Exception {
        ResponseBean res = new ResponseBean();

        if(StringUtils.isNotEmpty(id)){
            String[] idArr = id.split(",");
            for(String lbtid : idArr){
                WhgYwiLbt lbt = whgYwiLbtMapper.selectByPrimaryKey(lbtid);
                WhgSysCult cult = this.whgSystemCultService.t_srchOne(lbt.getCultid());
                //推到省
                if(type == 1){
                    if(lbt.getProvince() != null && !"".equals(lbt.getProvince())){
                        lbt.setProvince(null);
                    }else {
                        String provice = cult.getProvince();
                        lbt.setProvince(provice);
                    }
                    whgYwiLbtMapper.updateByPrimaryKey(lbt);
                }else if(type == 2){
                    if(cult.getLevel() == 1 ){
                        res.setSuccess(ResponseBean.FAIL);
                        res.setErrormsg("省级馆无法推送全市站");
                    }else{
                        //推到市
                        if(lbt.getCity() != null && !"".equals(lbt.getCity())){
                            lbt.setCity(null);
                        }else {
                            String city = cult.getCity();
                            lbt.setCity(city);
                        }
                        whgYwiLbtMapper.updateByPrimaryKey(lbt);
                    }
                }
            }
        }
        return res;
    }
}
