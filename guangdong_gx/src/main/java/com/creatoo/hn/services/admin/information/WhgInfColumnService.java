package com.creatoo.hn.services.admin.information;

import com.creatoo.hn.dao.mapper.CrtWhgInfColumnMapper;
import com.creatoo.hn.dao.model.WhgInfColumn;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by LENUVN on 2017/7/25.
 */
@Service
public class WhgInfColumnService extends BaseService {

    @Autowired
    private CrtWhgInfColumnMapper whgInfColumnMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;
    /**
     * 查询顶级
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object inquire() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<Object> list = new ArrayList<Object>();
        // 分类表按分类条件查询
        Example example = new Example(WhgInfColumn.class);
        example.setOrderByClause("colidx,colpid");

        List<WhgInfColumn> listTp = this.whgInfColumnMapper.selectByExample(example);
        if (listTp != null) {
            for (WhgInfColumn whzx : listTp) {
                if (whzx.getColpid() != null && "0".equals(whzx.getColpid())) {
                    Map temp = BeanUtils.describe(whzx);
                    this.addList(temp, listTp);
                    temp.put("id", temp.get("colid"));
                    temp.put("text", temp.get("coltitle"));
                    list.add(temp);
                }
            }
        }
        return list;
    }
    /**
     * 查询子级目录
     * @param temp
     * @param listTp
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addList(Map temp, List<WhgInfColumn> listTp) {
        if (temp.get("children") == null) {
            temp.put("children", new ArrayList<Object>());
        }
        for (WhgInfColumn whzx : listTp) {
            if (temp.get("colid").equals(whzx.getColpid())) {
                List children = (List) temp.get("children");
                try {
                    Map temps = BeanUtils.describe(whzx);
                    this.addList(temps, listTp);
                    temps.put("id", temps.get("colid"));
                    temps.put("text", temps.get("coltitle"));
                    children.add(temps);
                } catch (Exception e) {
                }
            }
        }

    }
    /**
     * 添加
     * @param whzx
     */
    public void save(WhgInfColumn whzx) {
        this.whgInfColumnMapper.insert(whzx);
    }
    /**
     * 更新
     * @param whzx
     */
    public void update(WhgInfColumn whzx) {
        this.whgInfColumnMapper.updateByPrimaryKeySelective(whzx);

    }
    /**
     * 删除
     * @param colid
     */
    public Object delete(String colid) {

        int row=  this.whgInfColumnMapper.deleteByPrimaryKey(colid);

        return row;
    }
    /**
     * 带状态查询
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object select(WhgSysUser sysUser) throws Exception {
        List<Object> list = new ArrayList<Object>();
        List projectList = new ArrayList();
        Map map = new HashMap();
        if (sysUser != null) {
            WhgSysCult cult = whgSystemUserService.t_srchUserCult(sysUser.getId());
            if (cult != null && cult.getFrontmenu() != null) {
                projectList = Arrays.asList(cult.getFrontmenu().split(","));
            }
        }
        Example example = new Example(WhgInfColumn.class);
        Example.Criteria criteria = example.createCriteria();
        map.put("colstate", 1);
        if (projectList != null) {
            map.put("frontmenus", projectList);
        }
        List<WhgInfColumn> listTp = this.whgInfColumnMapper.srchColumnTree(map);
        if (listTp != null) {
            for (WhgInfColumn whzx : listTp) {
                if (whzx.getColpid() != null && "0".equals(whzx.getColpid())) {
                    Map temp = BeanUtils.describe(whzx);
                    this.addList(temp, listTp);
                    temp.put("id", temp.get("colid"));
                    temp.put("text", temp.get("coltitle"));
                    list.add(temp);
                }
            }
        }
        return list;
    }

    public Object selectFrontColumn(String colid,String toproject) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<Object> list = new ArrayList<Object>();
        // 分类表按分类条件查询
        Example example = new Example(WhgInfColumn.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("colstate",1);
        if(toproject!=null&&!toproject.equals("")){
            criteria.andLike("toproject","%"+toproject+"%");
        }
        if(colid!=null&&!colid.equals("")){
            criteria.andEqualTo("colid",colid);
        }
        example.setOrderByClause("colidx,colpid");
        List<WhgInfColumn> listTp = this.whgInfColumnMapper.selectByExample(example);
        if (listTp != null) {
            for (WhgInfColumn whzx : listTp) {
                if (whzx.getColpid() != null && "0".equals(whzx.getColpid())) {
                    Map temp = BeanUtils.describe(whzx);
                    this.addList(temp, listTp);
                    temp.put("id", temp.get("colid"));
                    temp.put("text", temp.get("coltitle"));
                    list.add(temp);
                }
            }
        }
        return list;
    }

    public  List<WhgInfColumn> selectChildList(String colid,String toproject) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<Object> list = new ArrayList<Object>();
        // 分类表按分类条件查询
        Example example = new Example(WhgInfColumn.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("colstate",1);
        if(toproject!=null&&!toproject.equals("")){
            criteria.andLike("toproject","%"+toproject+"%");
        }
        if(colid!=null&&!colid.equals("")){
            criteria.andEqualTo("colpid",colid);
        }
        example.setOrderByClause("colidx,colpid");
        List<WhgInfColumn> listTp = this.whgInfColumnMapper.selectByExample(example);
        return listTp;
    }
}
