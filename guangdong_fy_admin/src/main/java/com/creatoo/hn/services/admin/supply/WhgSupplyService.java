package com.creatoo.hn.services.admin.supply;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgSupplyMapper;
import com.creatoo.hn.dao.mapper.WhgSupplyTimeMapper;
import com.creatoo.hn.dao.model.WhgSupply;
import com.creatoo.hn.dao.model.WhgSupplyTime;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.project.WhgFkProjectService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rbg on 2017/8/22.
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "supply", keyGenerator = "simpleKeyGenerator")
public class WhgSupplyService extends BaseService {

    @Autowired
    private WhgSupplyMapper whgSupplyMapper;

    @Autowired
    private WhgSupplyTimeMapper whgSupplyTimeMapper;

    @Autowired
    private WhgFkProjectService whgFkProjectService;

    /**
     * 主键查询供需信息
     * @param id
     * @return
     * @throws Exception
     */
    @Cacheable
    public Object srchOne(String id) throws Exception{
        WhgSupply supply = this.whgSupplyMapper.selectByPrimaryKey(id);
        if (supply == null) {
            return null;
        }
        BeanMap bm = new BeanMap();
        bm.setBean(supply);

        Map rest = new HashMap();
        rest.putAll(bm);

        List list = this.selectTimes4Supply(id);
        rest.put("times", JSON.toJSONString(list));

        return rest;
    }

    /**
     * 分页查询供需信息
     * @param page
     * @param rows
     * @param supply
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    @Cacheable
    public Object srchList4p(int page, int rows, WhgSupply supply, String sort, String order) throws Exception{
        Example exp = new Example(WhgSupply.class);
        Example.Criteria ca = exp.createCriteria();

        if (supply!=null){
            if (supply.getTitle()!=null && !supply.getTitle().isEmpty()){
                ca.andLike("title", "%"+supply.getTitle()+"%");
                supply.setTitle(null);
            }
            if (supply.getContacts()!=null && !supply.getContacts().isEmpty()){
                ca.andLike("contacts", "%"+supply.getContacts()+"%");
                supply.setContacts(null);
            }
            if (supply.getPhone()!=null && !supply.getPhone().isEmpty()){
                ca.andLike("phone", "%"+supply.getPhone()+"%");
                supply.setPhone(null);
            }

            ca.andEqualTo(supply);
        }

        if (sort!=null && !sort.isEmpty()){
            Example.OrderBy orderBy = exp.orderBy(sort);
            if (order!=null && "desc".equalsIgnoreCase(order)){
                orderBy.desc();
            }
        }else{
            exp.orderBy("crtdate").desc();
        }

        PageHelper.startPage(page, rows);
        List list = this.whgSupplyMapper.selectByExample(exp);

        return new PageInfo(list);
    }

    /**
     * 添加供需信息
     * @param supply
     * @param sysUser
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_add(WhgSysUser sysUser, WhgSupply supply, String times) throws Exception{
        ResponseBean resb = new ResponseBean();

        /*String cultid = sysUser.getCultid();
        if (cultid == null || cultid.isEmpty()) {
            cultid = Constant.ROOT_SYS_CULT_ID;
        }*/

        supply.setId(IDUtils.getID());
        //supply.setCultid(cultid);
        supply.setCrtdate(new Date());
        supply.setCrtuser(sysUser.getId());
        supply.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        supply.setStatemdfuser(sysUser.getId());
        supply.setStatemdfdate(new Date());

        this.whgSupplyMapper.insert(supply);

        this.resetSupplyTimes(supply.getId(), times);

        return resb;
    }

    /**
     * 编辑供需信息
     * @param sysUser
     * @param supply
     * @param times
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_edit(WhgSysUser sysUser, WhgSupply supply, String times) throws Exception{
        ResponseBean resb = new ResponseBean();

        if (supply == null || supply.getId() == null || supply.getId().isEmpty()) {
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("信息标识不能为空");
        }

        //处理设空值无法写入
        if (supply.getPscity() == null) {
            supply.setPscity("");
        }

        this.whgSupplyMapper.updateByPrimaryKeySelective(supply);

        this.resetSupplyTimes(supply.getId(), times);

        return resb;
    }

    /**
     * @param suuplyId
     * @param times 时间JSONArray 字符串
     * 重置供需信息时间表项
     * */
    @CacheEvict(allEntries = true)
    public void resetSupplyTimes(String suuplyId, String times) throws Exception{
        if (suuplyId == null){
            return;
        }

        this.clearSupplyTimes(suuplyId);

        List<WhgSupplyTime> timesList = null;
        if (times != null || !times.isEmpty()) {
            JSONArray timesArray = JSON.parseArray(times);
            if (timesArray.size()>0){
                timesList = new ArrayList();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for(Object ent : timesArray){
                    try {
                        JSONObject time = (JSONObject) ent;
                        WhgSupplyTime item = new WhgSupplyTime();
                        item.setId(IDUtils.getID());
                        item.setSupplyid(suuplyId);

                        String ts = (String) time.get("timestart");
                        String te = (String) time.get("timeend");
                        item.setTimestart(sdf.parse(ts));
                        item.setTimeend(sdf.parse(te));

                        this.whgSupplyTimeMapper.insert(item);
                    } catch (ParseException e) {
                        log.error(e.getMessage(), e);
                        continue;
                    }
                }
            }
        }
    }

    /**
     * 清除供需时间信息
     * @param suuplyId
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void clearSupplyTimes(String suuplyId) throws Exception{
        if (suuplyId == null){
            return;
        }

        WhgSupplyTime recode = new WhgSupplyTime();
        recode.setSupplyid(suuplyId);
        this.whgSupplyTimeMapper.delete(recode);
    }

    /**
     * 查供需的时间段列表
     * @param supplyId
     * @return
     * @throws Exception
     */
    public List selectTimes4Supply(String supplyId) throws Exception{
        Example example = new Example(WhgSupplyTime.class);
        example.createCriteria().andEqualTo("supplyid", supplyId);
        example.orderBy("timestart").asc();

        return this.whgSupplyTimeMapper.selectByExample(example);
    }

    /**
     * 删除供需信息
     * @param id
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_del(String id) throws Exception{
        ResponseBean resb = new ResponseBean();
        if (id==null || id.isEmpty()){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("标识不能为空");
            return resb;
        }

        this.whgSupplyMapper.deleteByPrimaryKey(id);
        this.clearSupplyTimes(id);
        //删除fk表中对应的数据
        this.whgFkProjectService.delByFkid(id);
        return resb;
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUser
     * @param optTime
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser sysUser, Date optTime) throws Exception{
        ResponseBean resb = new ResponseBean();
        if (ids == null){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("标识不能为空");
            return resb;
        }

        Example example = new Example(WhgSupply.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );

        WhgSupply info = new WhgSupply();
        info.setState(tostate);
        if (optTime==null) {
            optTime = new Date();
        }
        info.setStatemdfdate(optTime);
        info.setStatemdfuser(sysUser.getId());
        this.whgSupplyMapper.updateByExampleSelective(info, example);
        return resb;
    }
}
