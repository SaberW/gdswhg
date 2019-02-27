package com.creatoo.hn.services.admin.goods;

import com.creatoo.hn.dao.mapper.WhgShowOrganMapper;
import com.creatoo.hn.dao.model.WhgShowOrgan;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 */
@SuppressWarnings("ALL")
@Service
public class WhgShowOrganService extends BaseService {

    @Autowired
    private WhgShowOrganMapper whgShowOrganMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     *根据id查找组织机构
     * @param id
     * @return
     */
    public Object srchOne(String id) {
        return whgShowOrganMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询
     * @param page
     * @param rows
     * @param organ
     * @param sort
     * @param order
     * @return
     */
    public PageInfo t_srchlist4p(int page, int rows,String userId, WhgShowOrgan organ, List states, String sort, String order) throws Exception {
        Example exp = new Example(WhgShowOrgan.class);
        Example.Criteria ca = exp.createCriteria();

        if (organ!=null){
            if (organ.getTitle()!=null && !organ.getTitle().isEmpty()){
                ca.andLike("title", "%"+organ.getTitle()+"%");
                organ.setTitle(null);
            }
            if (organ.getCultid() == null||organ.getCultid()=="") {
                List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(userId);
                if (cultids!=null && cultids.size()>0){
                    ca.andIn("cultid",cultids);
                    organ.setCultid(null);
                }
            }
            if (organ.getDeptid() == null||organ.getCultid()=="") {
                List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(userId);
                if (deptids != null && deptids.size() > 0) {
                    ca.andIn("deptid",deptids);
                    organ.setDeptid(null);
                }
            }
            ca.andEqualTo(organ);
        }

        if (states != null && states.size() > 0) {
            ca.andIn("state", states);
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
        List list = this.whgShowOrganMapper.selectByExample(exp);
        return new PageInfo(list);
    }

    /**
     * 添加组织机构
     * @param organ
     * @param sysUser
     * @return
     */
    public void t_add(WhgShowOrgan organ, WhgSysUser sysUser) throws Exception {
        Date now = new Date();
        organ.setId(IDUtils.getID());
        organ.setCrtdate(now);
        organ.setCrtuser(sysUser.getId());
        organ.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        organ.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        organ.setStatemdfuser(sysUser.getId());
        organ.setStatemdfdate(now);
        this.whgShowOrganMapper.insert(organ);
    }

    /**
     * 编辑商品
     * @param organ
     * @return
     */
    public void t_edit(WhgShowOrgan organ) throws Exception{
        this.whgShowOrganMapper.updateByPrimaryKeySelective(organ);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public void t_del(String id) throws Exception{
        this.whgShowOrganMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUser
     * @return
     */
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser sysUser,Date optTime) throws Exception{
        ResponseBean resb = new ResponseBean();
        if (ids == null){
            resb.setSuccess(ResponseBean.FAIL);
            resb.setErrormsg("商品标识丢失");
            return resb;
        }

        Example example = new Example(WhgShowOrgan.class);
        example.createCriteria()
                .andIn("state", Arrays.asList( formstates.split("\\s*,\\s*") ))
                .andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ) );

        WhgShowOrgan organ = new WhgShowOrgan();
        organ.setState(tostate);
        if (optTime==null) {
            optTime = new Date();
        }
        organ.setStatemdfdate(optTime);
        organ.setStatemdfuser(sysUser.getId());

        if (tostate == EnumBizState.STATE_CAN_PUB.getValue() || tostate == EnumBizState.STATE_CAN_EDIT.getValue()){
            organ.setCheckor(sysUser.getId());
            organ.setCheckdate(new Date());
        }
        if (tostate == EnumBizState.STATE_PUB.getValue()){
            organ.setPublisher(sysUser.getId());
            organ.setPublishdate(new Date());
        }
        this.whgShowOrganMapper.updateByExampleSelective(organ, example);
        return resb;
    }

    /**
     *查询组织机构列表
     * @return
     */
    public List<WhgShowOrgan> t_srchList( String cultid) throws Exception{
        Example example = new Example(WhgShowOrgan.class);
        Example.Criteria c = example.createCriteria();
//        if(cultids != null && cultids.size() != 0){
//            c.andIn("cultid",cultids);
//        }
        c.andEqualTo("cultid",cultid);
        c.andEqualTo("state",6);
        c.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        List<WhgShowOrgan> list = this.whgShowOrganMapper.selectByExample(example);
        return list;
    }

    /**
     * 查询关联组织机构
     * @param page
     * @param pageSize
     * @param entid
     * @return
     */
    public PageInfo t_getZzjg(int page, int pageSize, String entid) {
        boolean isSplit = entid.contains(",");
        List<WhgShowOrgan> list = new ArrayList<>();
        Example example = new Example(WhgShowOrgan.class);
        Example.Criteria c = example.createCriteria();
        if(isSplit){
            String[] values = entid.split("\\s*,\\s*");
            c.andIn("id", Arrays.asList(values));
        }else{
            c.andEqualTo("id", entid);
        }
        example.setOrderByClause("statemdfdate desc");
        PageHelper.startPage(page, pageSize);
        list = this.whgShowOrganMapper.selectByExample(example);
        return new PageInfo(list);
    }
}
