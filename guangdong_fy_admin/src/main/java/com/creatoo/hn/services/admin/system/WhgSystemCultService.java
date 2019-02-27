package com.creatoo.hn.services.admin.system;

import com.creatoo.hn.dao.mapper.WhgSysCultMapper;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 场馆服务类
 * Created by wangxl on 2017/3/18.
 */
@Service
@CacheConfig(cacheNames = "WhgSysCult", keyGenerator = "simpleKeyGenerator")
public class WhgSystemCultService extends BaseService {

    /**
     * 文化馆DAO
     */
    @Autowired
    private WhgSysCultMapper whgSysCultMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService ;

    /**
     * 查询所有文化馆
     * @return 所有可用的文化改名字列表
     * @throws Exception
     */
    public List<WhgSysCult> findCult()throws Exception{
        Example example = new Example(WhgSysCult.class);
        example.createCriteria().andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        example.setOrderByClause("crtdate desc");
        return whgSysCultMapper.selectByExample(example);
    }

    /**
     * 查询特有文化馆
     * @throws Exception
     */
    public WhgSysCult findCultByCondition(String province,String city,String area)throws Exception{
        Example example = new Example(WhgSysCult.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        c.andEqualTo("province",province);
        if(city!=null){
            c.andEqualTo("city",city);
        }else{
            c.andIsNull("city");
        }
        if(area!=null){
            c.andEqualTo("area",area);
        }else{
            c.andIsNull("area");
        }
        example.setOrderByClause("crtdate desc");
        List<WhgSysCult> list=whgSysCultMapper.selectByExample(example);
        if(list.size()>0){
            return whgSysCultMapper.selectByExample(example).get(0);
        }else{
            return null;
        }
    }


    /**
     * 分页查询文化馆信息
     * @param page 第几页
     * @param rows 每页数
     * @param sort 排序字段
     * @param order 排序方式
     * @param cult 查询条件
     * @return PageInfo<WhgSysCult>
     * @throws Exception
     */
    @Cacheable
    public PageInfo<WhgSysCult> t_srchList4p(int page, int rows, String sort, String order, WhgSysCult cult)throws Exception{
        //搜索条件
        Example example = new Example(WhgSysCult.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(cult != null && cult.getName() != null){
            c.andLike("name", "%"+cult.getName()+"%");
            cult.setName(null);
        }
        if(cult!=null) {
            //其它条件
            c.andEqualTo(cult);
        }
        //排序
        if(StringUtils.isNotEmpty(sort)){
            StringBuffer sb = new StringBuffer(sort);
            if(StringUtils.isNotEmpty(order)){
                sb.append(" ").append(order);
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("idx");
        }

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgSysCult> list = this.whgSysCultMapper.selectByExample(example);
        return new PageInfo<WhgSysCult>(list);
    }

    /**
     * 查询文化馆列表
     * @param sort 排序字段
     * @param order 排序方式
     * @param cult 查询条件
     * @return List<WhgSysCult>
     * @throws Exception
     */
    @Cacheable
    public List<WhgSysCult> t_srchList(String sort, String order, WhgSysCult cult)throws Exception{
        //搜索条件
        Example example = new Example(WhgSysCult.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(cult != null && cult.getName() != null){
            c.andLike("name", "%"+cult.getName()+"%");
            cult.setName(null);
        }

        //其它条件
        c.andEqualTo(cult);

        //排序
        if(StringUtils.isNotEmpty(sort)){
            StringBuffer sb = new StringBuffer(sort);
            if(StringUtils.isNotEmpty(order)){
                sb.append(" ").append(order);
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("idx");
        }

        //查询
        return this.whgSysCultMapper.selectByExample(example);
    }

    /**
     * 查询单个文化馆信息
     * @param id 文化馆主键
     * @return WhgSysCult文化馆对象
     * @throws Exception
     */
    @Cacheable
    public WhgSysCult t_srchOne(String id)throws Exception{
        return this.whgSysCultMapper.selectByPrimaryKey(id);
    }


    public WhgSysCult t_srchOneByUserId(String userId)throws Exception{
        if(userId!=null&&!userId.equals("")){
            WhgSysUser sysUser=whgSystemUserService.t_srchOne(userId);
            if(sysUser!=null&&sysUser.getCultid()!=null&&!sysUser.getCultid().equals("")){
                return this.whgSysCultMapper.selectByPrimaryKey(sysUser.getCultid());
            }else {
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * 添加文化馆
     * @param cult
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgSysCult t_add(WhgSysCult cult, WhgSysUser user)throws Exception{
        if(cult.getName()!=null) {
            //名称不能重复
            Example example = new Example(WhgSysCult.class);
            Example.Criteria c = example.createCriteria();
            c.andEqualTo("name", cult.getName());
            int count = this.whgSysCultMapper.selectCountByExample(example);
            if (count > 0) {
                throw new Exception("名称重复");
            }
        }

        //获得idx
        Example ex1 = new Example(WhgSysCult.class);
        ex1.createCriteria().andIsNotNull("id");
        int idx = this.whgSysCultMapper.selectCountByExample(ex1);
        idx++;
        //设置初始值
        Date now = new Date();
        cult.setId(IDUtils.getID());
        cult.setCrtdate(now);
        if(user!=null){
            cult.setState(EnumState.STATE_YES.getValue());
            cult.setCrtuser(user.getId());
            cult.setStatemdfuser(user.getId());
        }
        cult.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        cult.setStatemdfdate(now);
        cult.setUpindex(0);
        cult.setIdx(idx);
        int rows = this.whgSysCultMapper.insertSelective(cult);
        if(rows != 1){
            throw new Exception("添加资料失败");
        }

        return cult;
    }

    /**
     * 编辑文化馆
     * @param cult
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgSysCult t_edit(WhgSysCult cult, WhgSysUser user)throws Exception{
        //名称不能重复
        Example example = new Example(WhgSysCult.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", cult.getName());
        c.andNotEqualTo("id", cult.getId());
        int count = this.whgSysCultMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("文化馆名称重复");
        }

        //设置初始值
        Date now = new Date();
        cult.setStatemdfdate(now);
        cult.setStatemdfuser(user.getId());
        int rows = this.whgSysCultMapper.updateByPrimaryKeySelective(cult);
        if(rows != 1){
            throw new Exception("编辑文化馆资料失败");
        }

        return cult;
    }

    /**
     * 删除文化馆
     * @param ids 文化馆ID
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String ids, WhgSysUser user)throws Exception{
        if(ids != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgSysCult.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            this.whgSysCultMapper.deleteByExample(example);
        }
    }

    /**
     * 排序
     * @param id 主键
     * @param type up|top|idx
     * @param val 当type=idx时，表示直接设置排序值
     * @param user
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_sort(String id, String type, String val, WhgSysUser user)throws Exception{
        //初始所有记录的idx值
        WhgSysCult whgSysCult = this.whgSysCultMapper.selectByPrimaryKey(id);
        Integer idx = whgSysCult.getIdx();
        if(idx == null){
            Example example = new Example(WhgSysCult.class);
            example.setOrderByClause("crtdate desc");
            List<WhgSysCult> allList = this.whgSysCultMapper.selectByExample(example);
            if(allList != null && allList.size() > 0){
                for(int i=0; i<allList.size(); i++){
                    WhgSysCult _cult = allList.get(i);
                    _cult.setIdx(i+1);
                    this.whgSysCultMapper.updateByPrimaryKeySelective(_cult);
                }
            }
        }

        //up
        if("up".equals(type)){
            //当前记录
            WhgSysCult curCult = this.whgSysCultMapper.selectByPrimaryKey(id);
            int curIdx = curCult.getIdx();

            //前一条记录
            Example example2 = new Example(WhgSysCult.class);
            example2.createCriteria().andLessThan("idx", curCult.getIdx());
            example2.setOrderByClause("idx desc");
            PageHelper.startPage(1,1);
            List<WhgSysCult> lessList = this.whgSysCultMapper.selectByExample(example2);
            PageInfo<WhgSysCult> pi = new PageInfo<WhgSysCult>(lessList);
            if(lessList != null && lessList.size() == 1){
                WhgSysCult preCult = lessList.get(0);
                int preIdx = preCult.getIdx();

                curCult.setIdx(preIdx);
                this.whgSysCultMapper.updateByPrimaryKeySelective(curCult);

                preCult.setIdx(curIdx);
                this.whgSysCultMapper.updateByPrimaryKeySelective(preCult);
            }
        }

        //top
        else if("top".equals(type)){
            //当前记录
            WhgSysCult curCult = this.whgSysCultMapper.selectByPrimaryKey(id);
            int curIdx = curCult.getIdx();

            Example example = new Example(WhgSysCult.class);
            example.createCriteria().andLessThan("idx", curIdx);
            example.setOrderByClause("idx");
            List<WhgSysCult> allList = this.whgSysCultMapper.selectByExample(example);
            if(allList != null && allList.size() > 0){
                List<WhgSysCult> newList = new ArrayList<WhgSysCult>();
                for(WhgSysCult c : allList){
                    if(c != null && !curCult.getId().equals(c.getId())){
                        newList.add(c);
                    }
                }

                curCult.setIdx(1);
                this.whgSysCultMapper.updateByPrimaryKey(curCult);

                for(int i=0; i<newList.size(); i++){
                    WhgSysCult _cult = newList.get(i);
                    _cult.setIdx(i+2);
                    this.whgSysCultMapper.updateByPrimaryKeySelective(_cult);
                }
            }
        }

        //idx 直接设置idx值
        else if("idx".equals(type)){
            //当前记录
            WhgSysCult curCult = this.whgSysCultMapper.selectByPrimaryKey(id);
            curCult.setIdx(Integer.parseInt(val));
            this.whgSysCultMapper.updateByPrimaryKey(curCult);
        }
    }

    /**
     * 更新文化馆状态
     * @param ids 文化馆ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updstate(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgSysCult.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("state", fromState);
            }
            WhgSysCult record = new WhgSysCult();
            record.setState(Integer.parseInt(toState));
            Date now = new Date();
            record.setStatemdfdate(now);
            record.setStatemdfuser(user.getId());
            this.whgSysCultMapper.updateByExampleSelective(record, example);
        }
    }

    /**
     * 更新文化馆状态
     * @param ids 文化馆ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updsqstate(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgSysCult.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("sqstate", fromState);
            }
            WhgSysCult record = new WhgSysCult();
            record.setSqstate(Integer.parseInt(toState));
            Date now = new Date();
            record.setStatemdfdate(now);
            record.setStatemdfuser(user.getId());
            this.whgSysCultMapper.updateByExampleSelective(record, example);
        }
    }

    /**
     * 上首页
     * @param ids
     * @param formupindex
     * @param toupindex
     * @return
     */
    @CacheEvict(allEntries = true)
    public void t_upindex(String ids, String formupindex, int toupindex)throws Exception {
        Example example = new Example(WhgSysCult.class);
        Example.Criteria c = example.createCriteria();
        c.andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ));
        c.andIn("upindex", Arrays.asList( formupindex.split("\\s*,\\s*") ));
        WhgSysCult cult = new WhgSysCult();
        cult.setUpindex(toupindex);
        this.whgSysCultMapper.updateByExampleSelective(cult,example);
    }
}
