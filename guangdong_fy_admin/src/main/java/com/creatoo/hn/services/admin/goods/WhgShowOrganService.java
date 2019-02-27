package com.creatoo.hn.services.admin.goods;

import com.creatoo.hn.dao.mapper.WhgShowOrganMapper;
import com.creatoo.hn.dao.model.WhgShowOrgan;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 */
@Service
public class WhgShowOrganService extends BaseService {

    @Autowired
    private WhgShowOrganMapper whgShowOrganMapper;

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
    public PageInfo t_srchlist4p(int page, int rows, WhgShowOrgan organ, String sort, String order) {
        Example exp = new Example(WhgShowOrgan.class);
        Example.Criteria ca = exp.createCriteria();

        if (organ!=null){
            if (organ.getTitle()!=null && !organ.getTitle().isEmpty()){
                ca.andLike("title", "%"+organ.getTitle()+"%");
                organ.setTitle(null);
            }
            ca.andEqualTo(organ);
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
    public ResponseBean t_add(WhgShowOrgan organ, WhgSysUser sysUser) throws Exception {
        ResponseBean rsb = new ResponseBean();
        Date now = new Date();
        organ.setId(IDUtils.getID());
        organ.setCrtdate(now);
        organ.setCrtuser(sysUser.getId());
        organ.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        organ.setStatemdfuser(sysUser.getId());
        organ.setStatemdfdate(now);
        this.whgShowOrganMapper.insert(organ);
        return rsb;
    }

    /**
     * 编辑商品
     * @param organ
     * @return
     */
    public ResponseBean t_edit(WhgShowOrgan organ) {
        ResponseBean rsb = new ResponseBean();
        this.whgShowOrganMapper.updateByPrimaryKeySelective(organ);
        return rsb;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public ResponseBean t_del(String id) {
        ResponseBean resb = new ResponseBean();
        this.whgShowOrganMapper.deleteByPrimaryKey(id);
        return resb;
    }

    /**
     * 修改状态
     * @param ids
     * @param formstates
     * @param tostate
     * @param sysUser
     * @return
     */
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser sysUser,Date optTime) {
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
        List<WhgShowOrgan> list = this.whgShowOrganMapper.selectByExample(example);
        return list;
    }
}
