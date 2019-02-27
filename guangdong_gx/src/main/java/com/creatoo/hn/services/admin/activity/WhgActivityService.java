package com.creatoo.hn.services.admin.activity;
import com.creatoo.hn.dao.mapper.CrtWhgActivityMapper;
import com.creatoo.hn.dao.mapper.WhgActivityMapper;
import com.creatoo.hn.dao.mapper.WhgActivityTimeMapper;
import com.creatoo.hn.dao.model.WhgActivity;
import com.creatoo.hn.dao.model.WhgActivityTime;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * 活动管理 服务层
 * @author heyi
 *
 */
@Service
@CacheConfig(cacheNames = "WhgActivity", keyGenerator = "simpleKeyGenerator")
public class WhgActivityService extends BaseService {

    /**
     * 活动DAO
     */
   @Autowired
    private WhgActivityMapper whgActivityMapper;

    @Autowired
    private CrtWhgActivityMapper crtWhgActivityMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;
    
    /**
     * 分页查询活动信息
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    public PageInfo srchList4p(int page, int rows, Map param,WhgSysUser whgSysUser) throws Exception{
        List<String> cultids=new ArrayList<>();
        List<String> deptids=new ArrayList<>();
        if(whgSysUser!=null){
            if (param.get("cultid") == null) {
                try {
                    cultids = this.whgSystemUserService.getAllCultId4PMS(whgSysUser.getId());
                    if (cultids!=null && cultids.size()>0){
                        param.put("cultids", cultids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }else{
                cultids.add((String) param.get("cultid"));
                param.put("cultids", cultids);
            }
            if (param.get("deptid") == null) {
                try {
                    deptids= this.whgSystemUserService.getAllDeptId4PMS(whgSysUser.getId());
                    if (deptids != null && deptids.size() > 0) {
                        param.put("deptids", deptids);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }else{
                deptids.add((String) param.get("deptid"));
                param.put("deptids", deptids);
            }
        }
        PageHelper.startPage(page, rows);
        List list = this.crtWhgActivityMapper.srchListActivity(param);
        return new PageInfo<Object>(list);
    }



    /**
     * 分页查询活动信息
     * @param request 请求对象
     * @param act 条件对象
     * @return 分页数据
     * @throws Exception
     */

    public PageInfo<WhgActivity> t_srchList4p(HttpServletRequest request, WhgActivity act)throws Exception{
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);

        //分页信息
        int page = Integer.parseInt((String)paramMap.get("page"));
        int rows = Integer.parseInt((String)paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgActivity.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(act != null && act.getName() != null){
            c.andLike("name", "%"+act.getName()+"%");
            act.setName(null);
        }

        //其它条件
        c.andEqualTo(act);

        //排序
        if(paramMap.containsKey("sort") && paramMap.get("sort")!= null){
            StringBuffer sb = new StringBuffer((String)paramMap.get("sort"));
            if(paramMap.containsKey("order") && paramMap.get("order") !=  null){
                sb.append(" ").append(paramMap.get("order"));
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("crtdate desc");
        }

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgActivity> list = this.whgActivityMapper.selectByExample(example);
        return new PageInfo<WhgActivity>(list);
    }

    /**
     * 分页查询活动信息
     * @return 分页数据
     * @throws Exception
     */
    @Cacheable
    public PageInfo<WhgActivity> findActivityBrand(Map paramMap)throws Exception{

        //分页信息
        int page = Integer.parseInt((String)paramMap.get("page"));
        int rows = Integer.parseInt((String)paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgActivity.class);
        Example.Criteria c = example.createCriteria();

        //品牌id
        if(paramMap.get("brandId") != null){
            c.andLike("ebrand", "%"+paramMap.get("brandId")+"%");
        }
        //文化馆id
        if(paramMap.get("cultid") != null){
            c.andEqualTo("cultid", paramMap.get("cultid"));
        }
        c.andEqualTo("state",6);
        //排序
        if(paramMap.containsKey("sort") && paramMap.get("sort")!= null){
            StringBuffer sb = new StringBuffer((String)paramMap.get("sort"));
            if(paramMap.containsKey("order") && paramMap.get("order") !=  null){
                sb.append(" ").append(paramMap.get("order"));
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("crtdate desc");
        }

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgActivity> list = this.whgActivityMapper.selectByExample(example);
        return new PageInfo<WhgActivity>(list);
    }


    /**
     * 查询活动列表
     * @param request 请求对象
     * @param act 条件对象
     * @return 活动列表
     * @throws Exception
     */

    public List<WhgActivity> t_srchActList(HttpServletRequest request, WhgActivity act)throws Exception{
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //搜索条件
        Example example = new Example(WhgActivity.class);
        Example.Criteria c = example.createCriteria();
        //名称条件
        if(act != null && act.getName() != null){
            c.andLike("name", "%"+act.getName()+"%");
            act.setName(null);
        }
        c.andIsNull("biz");
        //其它条件
        c.andEqualTo(act);

        if (paramMap.containsKey("bmEnd") && paramMap.get("bmEnd") != null && !paramMap.get("bmEnd").toString().isEmpty()){
            c.andGreaterThanOrEqualTo("enterendtime", new Date());
        }

        //排序
        if(paramMap.containsKey("sort") && paramMap.get("sort")!= null){
            StringBuffer sb = new StringBuffer((String)paramMap.get("sort"));
            if(paramMap.containsKey("order") && paramMap.get("order") !=  null){
                sb.append(" ").append(paramMap.get("order"));
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("crtdate desc");
        }

        //分页查询
        return this.whgActivityMapper.selectByExample(example);
    }

    /**
     * 查询活动列表
     * @param request 请求对象
     * @param act 条件对象
     * @return 活动列表
     * @throws Exception
     */

    public List<WhgActivity> t_srchList(HttpServletRequest request, WhgActivity act,WhgSysUser whgSysUser)throws Exception{
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);

        //搜索条件
        Example example = new Example(WhgActivity.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(act != null && act.getName() != null){
            c.andLike("name", "%"+act.getName()+"%");
            act.setName(null);
        }
        if (act.getCultid() == null || act.getCultid().isEmpty()) {
            act.setCultid(null);
            try {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(whgSysUser.getId());
                if (cultids!=null && cultids.size()>0){
                    c.andIn("cultid", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (act.getDeptid() == null || act.getDeptid().isEmpty()) {
            act.setDeptid(null);
            try {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(whgSysUser.getId());
                if (deptids != null && deptids.size() > 0) {
                    c.andIn("deptid", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        //其它条件
        c.andEqualTo(act);

        //排序
        if(paramMap.containsKey("sort") && paramMap.get("sort")!= null){
            StringBuffer sb = new StringBuffer((String)paramMap.get("sort"));
            if(paramMap.containsKey("order") && paramMap.get("order") !=  null){
                sb.append(" ").append(paramMap.get("order"));
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("crtdate desc");
        }

        //分页查询
        return this.whgActivityMapper.selectByExample(example);
    }

    /**
     * 查询单个活动信息
     * @param id 活动主键
     * @return 活动对象
     * @throws Exception
     */
    public WhgActivity t_srchOne(String id)throws Exception{
         return whgActivityMapper.selectByPrimaryKey(id);
    }


    /**
     * 根据活动的开始时间和结束时间算出它们的时间差
     * @param
     * @return
     * @throws ParseException
     */
    public int dayCount(Date strTime,Date endTime) throws ParseException
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strTime);
        int strNum = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTime(endTime);
        int endNum = calendar.get(Calendar.DAY_OF_YEAR);
        return endNum - strNum;
    }

    /**
     * 是否到结束日期
     * @param date
     * @param end
     * @return
     */
    public boolean isAfter(Date date,Date end){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate myDate = LocalDate.parse(sdf.format(date));
        LocalDate endDate = LocalDate.parse(sdf.format(end));
        return myDate.isAfter(endDate);
    }

    /**
     * 获取明天
     * @param date
     * @return
     */
    public String getTomorrow(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
        date=calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }
    /**
     * 添加活动
     * @param act
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgActivity t_add(WhgActivity act,String id, WhgSysUser user)throws Exception{
        //名称不能重复  需求有变，不需判重
      /*  Example example = new Example(WhgActivity.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", act.getName());
        int count = this.whgActivityMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("活动名称重复");
        }*/

        //设置初始值
        Date now = new Date();
        if(id!=null){
            act.setId(id);
        }else {
            act.setId(IDUtils.getID());
        }
        act.setState(EnumState.STATE_YES.getValue());
        act.setCrtdate(now);
        act.setCrtuser(user.getId());
        act.setStatemdfdate(now);
        act.setStatemdfuser(user.getId());
       // act.setCultid(user.getCultid());    //文化馆
       // act.setDeptid(user.get);    //部门
        act.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        act.setStatemdfdate(now);
        act.setStatemdfuser(user.getId());
        int rows = this.whgActivityMapper.insertSelective(act);
        if(rows != 1){
            throw new Exception("添加活动失败");
        }

        return act;
    }

    @CacheEvict(allEntries = true)
    public void t_edit4Pk(WhgActivity info) throws Exception{
        this.whgActivityMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 编辑活动
     * @param act
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
   public WhgActivity t_edit(WhgActivity act, WhgSysUser user)throws Exception{
        //名称不能重复
       /* Example example = new Example(WhgActivity.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", act.getName());
        c.andNotEqualTo("id", act.getId());
        int count = this.whgActivityMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("活动名称重复");
        }*/

        //设置初始值
        Date now = new Date();
        act.setStatemdfdate(now);//变更修改时间
        act.setStatemdfuser(user.getId());//修改user
        int rows = this.whgActivityMapper.updateByPrimaryKeySelective(act);
        if(rows != 1){
            throw new Exception("编辑活动信息失败");
        }

        return act;
    }

    /**
     * 删除活动
     * @param ids 活动ID
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String ids, WhgSysUser user)throws Exception{
        if(ids != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgActivity.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            this.whgActivityMapper.deleteByExample(example);
        }
    }

    /**
     * 更新活动状态
     * @param statemdfdate 状态修改时间
     * @param ids 活动ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updstate(String statemdfdate, String ids, String fromState, String toState, WhgSysUser user, String publishdate) throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(ids != null && toState != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgActivity.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("state", fromState);
            }
            WhgActivity record = new WhgActivity();
            record.setState(Integer.parseInt(toState));
            Date now = new Date();
            if (toState.equals(EnumBizState.STATE_CAN_PUB.getValue() + "") || toState.equals(EnumBizState.STATE_CAN_EDIT.getValue() + "")) {//待发布  审核者信息
                record.setCheckor(user.getId());
                record.setCheckdate(new Date());
            } else if (toState.equals(EnumBizState.STATE_PUB.getValue() + "")) {//已发布记录 发布者信息
                record.setPublisher(user.getId());
                if (publishdate != null) {
                    record.setPublishdate(sdf.parse(publishdate));
                } else {
                    record.setPublishdate(new Date());
                }
            }
            //设置状态修改时间
            if(!"".equals(statemdfdate) && statemdfdate != null){
                record.setStatemdfdate(sdf.parse(statemdfdate));
            }else{
                record.setStatemdfdate(new Date());
            }
            record.setStatemdfdate(now);
            record.setStatemdfuser(user.getId());
            this.whgActivityMapper.updateByExampleSelective(record, example);
        }
    }
    
    /**
     * 更新推荐状态
     * @param ids 活动ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updCommend(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            WhgActivity record = whgActivityMapper.selectByPrimaryKey(ids);
        	record.setIsrecommend(Integer.parseInt(toState));
        	/*if(Integer.parseInt(toState) == 1){
        		Date now = new Date();
                record.setStatemdfdate(now);
        	}*/
            record.setStatemdfuser(user.getId());
            this.whgActivityMapper.updateByPrimaryKey(record);
        }
    }

    /**
     * 更新推荐状态
     * @param ids 活动ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updBanner(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            WhgActivity record = whgActivityMapper.selectByPrimaryKey(ids);
            record.setIsbigbanner(Integer.parseInt(toState));
            /*if(Integer.parseInt(toState) == 1){
                Date now = new Date();
                record.setStatemdfdate(now);
            }*/
            record.setStatemdfuser(user.getId());
            this.whgActivityMapper.updateByPrimaryKey(record);
        }
    }
    
    /**
     * 回收
     * @param ids 活动ID，多个用逗号分隔
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_updDelstate(String ids,  String delState, WhgSysUser user)throws Exception{
        if (ids != null) {
            WhgActivity record = whgActivityMapper.selectByPrimaryKey(ids);
            	 record.setDelstate(Integer.parseInt(delState));
                 Date now = new Date();
                 record.setStatemdfdate(now);
                 record.setStatemdfuser(user.getId());
                 this.whgActivityMapper.updateByPrimaryKey(record);
        }
    }

    /**
     * 物理删除数据
     *
     * @param ids 活动ID，多个用逗号分隔
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_delstate(String ids, String delState, WhgSysUser user) throws Exception {
        if (ids != null && delState != null) {
            String[] idArr = ids.split(",");
            Example example = new Example(WhgActivity.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            this.whgActivityMapper.deleteByExample(example);
        }
    }

    @CacheEvict(allEntries = true)
   public void reBack(String ids,String delState, WhgSysUser user){
    	if(ids != null && delState != null){
    		 WhgActivity record = whgActivityMapper.selectByPrimaryKey(ids);
    		 record.setDelstate(Integer.parseInt(delState));
            record.setState(EnumBizState.STATE_CAN_EDIT.getValue());//还原 撤销 回收 打回编辑列表 重走流程
            Date now = new Date();
             record.setStatemdfdate(now);
             record.setStatemdfuser(user.getId());
             this.whgActivityMapper.updateByPrimaryKey(record);
    	}
    }

    /**
     * 查询所有活动订单，以时间降序排列
     * @param page
     * @param rows 由于组合查询，关联多表，去掉缓存
     * @return
     * @throws Exception
     */
    public PageInfo getActOrderForBackManager(Map map,int page, int rows) throws Exception{
        PageHelper.startPage(page, rows);
        List list = crtWhgActivityMapper.getActOrderForBackManager(map);
        return new PageInfo<Object>(list);
    }


}
