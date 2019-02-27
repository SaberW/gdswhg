package com.creatoo.hn.services.admin.mass;

import com.creatoo.hn.dao.mapper.WhgResourceMapper;
import com.creatoo.hn.dao.mapper.api.ApiResourceMapper;
import com.creatoo.hn.dao.mapper.api.gather.CrtWhgGatherMapper;
import com.creatoo.hn.dao.model.WhgResource;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/12/4.
 * 群文资源管理 service
 */
@SuppressWarnings("ALL")
@Service
public class WhgResManageService extends BaseService {

    @Autowired
    private WhgResourceMapper whgResourceMapper;

    @Autowired
    private CrtWhgGatherMapper crtWhgGatherMapper;

    @Autowired
    private ApiResourceMapper apiResourceMapper;


    /**
     * 主键查询
     * @param id
     * @return
     * @throws Exception
     */
    public Object srchOne(String id) throws Exception{
        return this.whgResourceMapper.selectByPrimaryKey(id);
    }


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param record
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public PageInfo srchList4p(int page, int pageSize, WhgResource record, String sort, String order) throws Exception{
        Example exp = new Example(record.getClass());
        Example.Criteria c = exp.or();

        if (record != null) {
            if (record.getName() != null && !record.getName().isEmpty()) {
                c.andLike("name", "%"+record.getName()+"%");
                record.setName(null);
            }

            //非空值装入相等条件
            c.andEqualTo(record);
        }

        if (sort!=null && !sort.isEmpty()){
            if (order!=null && "desc".equalsIgnoreCase(order)){
                exp.orderBy(sort).desc();
            }else{
                exp.orderBy(sort).asc();
            }
        }else{
            exp.orderBy("crtdate").desc();
        }

        PageHelper.startPage(page, pageSize);
        List list = this.whgResourceMapper.selectByExample(exp);
        return new PageInfo(list);
    }

    /**
     * 众筹品牌资源查询
     * @param page
     * @param pageSize
     * @param record
     * @return
     * @throws Exception
     */
    public PageInfo srchList4pGatherBrandRes(int page, int pageSize, WhgResource record) throws Exception{
        Map recode = new HashMap();
        recode.put("refid", record.getRefid());
        recode.put("enttype", record.getEnttype());
        recode.put("name", record.getName());

        PageHelper.startPage(page, pageSize);
        List list = this.crtWhgGatherMapper.selectBrandResources(recode);
        return new PageInfo(list);
    }

    /**
     * 文化品牌资源查询
     * @param page
     * @param pageSize
     * @param record
     * @return
     * @throws Exception
     */
    public PageInfo srchList4pWhppBrandRes(int page, int pageSize, WhgResource record) throws Exception{
        PageHelper.startPage(page, pageSize);
        List<Map> list = apiResourceMapper.t_selppSource(record.getRefid(),record.getEnttype(), EnumTypeClazz.TYPE_ACTIVITY.getValue(), record.getName());
        return new PageInfo(list);
    }

    /**
     * 微专业资源查询
     * @param page
     * @param pageSize
     * @param record
     * @return
     * @throws Exception
     */
    public PageInfo srchList4pWzyBrandRes(int page, int pageSize, WhgResource record) throws Exception{
        PageHelper.startPage(page, pageSize);
        List<Map> list = apiResourceMapper.t_selMajorSource(record.getRefid(),record.getEnttype(), record.getName());
        return new PageInfo(list);
    }

    /**
     * 添加
     * @param info
     * @return
     * @throws Exception
     */
    public ResponseBean t_add(WhgResource info) throws Exception {
        ResponseBean rb = new ResponseBean();
        if (info == null) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("参数对象不能为空");
            return rb;
        }

        if (info.getName() == null || info.getName().isEmpty() || info.getName().length() > 100) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("名称为空或长度过长");
            return rb;
        }
        if (info.getEnttype() == null || info.getEnttype().isEmpty()) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("资源类型不能为空");
            return rb;
        }
        if (info.getReftype() == null || info.getReftype().isEmpty()) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("实体类型不能为空");
            return rb;
        }
        if (info.getRefid() == null || info.getRefid().isEmpty()) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("实体标识不能为空");
            return rb;
        }

        if (info.getId() == null || info.getId().isEmpty()) {
            info.setId(IDUtils.getID());
        }
        if (info.getExtisaward()==null) {
            info.setExtisaward(0);
        }
        this.whgResourceMapper.insert(info);
        return rb;
    }

    /**
     * 编辑
     * @param info
     * @throws Exception
     */
    public void t_edit(WhgResource info) throws Exception{
        this.whgResourceMapper.updateByPrimaryKeySelective(info);
    }

    /**
     * 删除处理
     * @param id
     * @throws Exception
     */
    public void t_del(String id) throws Exception{
        this.whgResourceMapper.deleteByPrimaryKey(id);
    }




    //TODO API Methods ...

    /**
     * 分页查询资源
     * @param page
     * @paraSize
     * @param record
     * @return
     * @throws Exception
     */
    public PageInfo getWhtResources(int page, int pageSize, WhgResource record) throws Exception{
        return this.srchList4p(page, pageSize, record, null, null);
    }

}
