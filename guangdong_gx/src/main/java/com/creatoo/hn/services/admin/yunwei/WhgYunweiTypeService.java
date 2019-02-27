package com.creatoo.hn.services.admin.yunwei;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgYwiTypeMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiType;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统运营分类seivice
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/3/16.
 */
@Service
@CacheConfig(cacheNames = "WhgYwiType", keyGenerator = "simpleKeyGenerator")
public class WhgYunweiTypeService extends BaseService {

    /**
     * mapper
     */
    @Autowired
    private WhgYwiTypeMapper whgYwiTypeMapper;

    /**
     * 类型表存储微信的accessToken
     * @param tokenType 999999-accessToken, 888888-jsticket
     * @return WhgYwiType
     * @throws Exception
     */
    public String findYwiType4AccessToken(int tokenType)throws Exception{
        Date now = new Date();
        WhgYwiType type = new WhgYwiType();
        type.setType(tokenType);//
        List<WhgYwiType> list = this.whgYwiTypeMapper.select(type);
        if(list != null && list.size() > 0){
            WhgYwiType thisType =  list.get(0);
            String expiresIn = thisType.getTypeicon();
            Date expiresDate = new Date(Long.parseLong(expiresIn));
            if(expiresDate.after(now)){//缓存时间没到,取缓存数据
                return thisType.getCloseid();
            }else{ //删除过期缓存
                WhgYwiType type_del = new WhgYwiType();
                type_del.setType(tokenType);
                this.whgYwiTypeMapper.delete(type_del);
            }
        }
        return null;
    }

    /**
     * 类型表存储微信的accessToken
     * @param token
     * @param expiren
     * @throws Exception
     */
    public void save4AccessToken(String token, String expiren, int tokenType)throws Exception{
        //先删除
        WhgYwiType type_del = new WhgYwiType();
        type_del.setType(tokenType);
        this.whgYwiTypeMapper.delete(type_del);

        //再添加
        Date now = new Date();
        WhgYwiType type = new WhgYwiType();
        type.setType(tokenType);
        type.setCloseid(token);//accessToken
        type.setStatemdfdate(now);
        type.setState(EnumState.STATE_YES.getValue());
        type.setId(IDUtils.getID32());
        type.setCrtdate(now);
        type.setCrtuser("crtuser");
        type.setCultid("cultid");
        type.setIdx(1);
        type.setName("name");
        type.setPid("pid");
        type.setTypeicon(expiren);//过期时间毫秒数
        type.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
        this.whgYwiTypeMapper.insert(type);
    }

    @Cacheable
    public List<WhgYwiType> findAllYwiType(String type, String cultid)throws Exception{
        return this.findAllYwiType(type, cultid, false);
    }

    /**
     * 根据类型获取所有分类
     * @return
     * @throws Exception
     */
    public List<WhgYwiType> findAllYwiType(String type,String cultid,boolean closeFilter4CultidNull)throws Exception{
        Example example = new Example(WhgYwiType.class);
        //Example.Criteria c = example.createCriteria();
        Example.Criteria c = example.or();
        Example.Criteria c1 = example.or();

        c.andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        c1.andEqualTo("state", EnumState.STATE_YES.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
        if(type!=null&&!"".equals(type)){
            c.andEqualTo("type", type);
            c1.andEqualTo("type", type);
        }

        c.andIsNull("closeid");

        List cultids = new ArrayList();
        cultids.add(Constant.ROOT_SYS_CULT_ID);
        if(cultid!=null && !"".equals(cultid)){
            cultids.add(cultid);
            c1.andNotLike("closeid", "%"+cultid+"%");
        }else{
            if (closeFilter4CultidNull) {
                c1.andNotLike("closeid", "%" + Constant.ROOT_SYS_CULT_ID + "%");
            }
        }

        c.andIn("cultid", cultids);
        c1.andIn("cultid", cultids);

        //example.setOrderByClause("cultid, idx");
        example.setOrderByClause("idx");
        return this.whgYwiTypeMapper.selectByExample(example);
    }

    /**
     * 分页查询分类列表信息
     * @param
     */
    @Cacheable
    public PageInfo<WhgYwiType> t_srchList4p(int page, int rows, WhgYwiType whgYwiTypet) throws Exception {

        //开始分页
        PageHelper.startPage(page, rows);
        Example example = new Example(WhgYwiType.class);
        Example.Criteria c = example.createCriteria();
        List list=new ArrayList();
        if (whgYwiTypet.getType() != null && !"".equals(whgYwiTypet.getType())) {
            c.andEqualTo("type", whgYwiTypet.getType());
        }
        if (whgYwiTypet.getCultid() != null && !"".equals(whgYwiTypet.getCultid())) {
            list.add(whgYwiTypet.getCultid());
        }
        list.add("0000000000000000");
        c.andIn("cultid",list);//筛选 省馆 + 权限馆id
        if(whgYwiTypet.getName() != null && !"".equals(whgYwiTypet.getName())){
            c.andLike("name","%"+whgYwiTypet.getName()+"%");
        }
        c.andEqualTo("state",1);
        c.andEqualTo("delstate",0);
        example.setOrderByClause("idx asc");
        List<WhgYwiType> typeList = this.whgYwiTypeMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 添加分类
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_add(WhgSysUser sysUser, WhgYwiType whgYwiType)throws Exception {
        Example example = new Example(WhgYwiType.class);
        example.createCriteria().andEqualTo("name", whgYwiType.getName()).andEqualTo("type", whgYwiType.getType())
                .andEqualTo("cultid", whgYwiType.getCultid());
        int count = this.whgYwiTypeMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称重复");
        }
        if(whgYwiType.getId()==null||whgYwiType.getId().equals("")){
            whgYwiType.setId(IDUtils.getID());
        }
        whgYwiType.setState(1);
        whgYwiType.setCrtdate(new Date());
        whgYwiType.setCrtuser(sysUser.getId());
        whgYwiType.setStatemdfdate(new Date());
        whgYwiType.setStatemdfuser(sysUser.getId());
        int result = this.whgYwiTypeMapper.insertSelective(whgYwiType);
        if(result != 1){
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑分类
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgYwiType whgYwiType, WhgSysUser sysUser)throws Exception {
        Example example = new Example(WhgYwiType.class);
        example.createCriteria().andEqualTo("name", whgYwiType.getName()).andEqualTo("type", whgYwiType.getType())
                .andEqualTo("cultid", whgYwiType.getCultid())
                .andNotEqualTo("id", whgYwiType.getId());
        int count = this.whgYwiTypeMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("名称重复");
        }
        WhgYwiType _whgYwiType = this.whgYwiTypeMapper.selectByPrimaryKey(whgYwiType.getId());
        _whgYwiType.setName(whgYwiType.getName());
        _whgYwiType.setIdx(whgYwiType.getIdx());
        _whgYwiType.setPid(whgYwiType.getPid());
        _whgYwiType.setCultid(whgYwiType.getCultid());
        _whgYwiType.setStatemdfdate(new Date());
        _whgYwiType.setStatemdfuser(sysUser.getId());
        _whgYwiType.setTypeicon(whgYwiType.getTypeicon());
        int result = this.whgYwiTypeMapper.updateByPrimaryKeySelective(_whgYwiType);
        if(result != 1){
            throw new Exception("编辑数据失败！");
        }
    }

    /**
     * 修改状态 0 停止 1 启用
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_updateOpt(String id,String cultid,String type)throws Exception {
        WhgYwiType _whgYwiType = this.whgYwiTypeMapper.selectByPrimaryKey(id);
        String closeid = null;
        if (_whgYwiType.getCloseid() != null && !_whgYwiType.getCloseid().equals("null")) {
            closeid = _whgYwiType.getCloseid();
        }
        if(type.equals("0")){//停用
            if (closeid != null) {
                _whgYwiType.setCloseid(closeid + "," + cultid);
            } else {
                _whgYwiType.setCloseid("," + cultid);
            }
        }else{//启用 去除cultid
            if (closeid != null) {
                closeid = closeid.replace("," + cultid, "");
                _whgYwiType.setCloseid(closeid);
            }
        }
        _whgYwiType.setStatemdfdate(new Date());
        int result = this.whgYwiTypeMapper.updateByPrimaryKeySelective(_whgYwiType);
        if(result != 1){
            throw new Exception("修改状态失败！");
        }
    }

    /**
     * 删除分类信息
     * @param id
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String id) throws Exception {
        int result = this.whgYwiTypeMapper.deleteByPrimaryKey(id);
        if(result != 1){
            throw new Exception("删除数据失败！");
        }
    }

    public List<WhgYwiType> query(WhgYwiType whgYwiTypet){
        Example example = new Example(WhgYwiType.class);
        Example.Criteria c = example.createCriteria();
        List list=new ArrayList();
        if (whgYwiTypet.getType() != null && !"".equals(whgYwiTypet.getType())) {
            c.andEqualTo("type", whgYwiTypet.getType());
        }
        if (whgYwiTypet.getCultid() != null && !"".equals(whgYwiTypet.getCultid())) {
            list.add(whgYwiTypet.getCultid());
        }
        list.add("0000000000000000");
        c.andIn("cultid",list);//筛选 省馆 + 权限馆id
        if(whgYwiTypet.getName() != null && !"".equals(whgYwiTypet.getName())){
            c.andLike("name","%"+whgYwiTypet.getName()+"%");
        }
        c.andEqualTo("state",1);
        c.andEqualTo("delstate",0);
        example.setOrderByClause("idx asc");
        List<WhgYwiType> typeList = this.whgYwiTypeMapper.selectByExample(example);
        return typeList;
    }
}