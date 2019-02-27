package com.creatoo.hn.services.admin.information;


import com.creatoo.hn.dao.mapper.WhgInfColinfoMapper;
import com.creatoo.hn.dao.mapper.WhgPubInfoMapper;
import com.creatoo.hn.dao.model.WhgInfColinfo;
import com.creatoo.hn.dao.model.WhgPubInfo;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 培训、活动、非遗公用资讯Service
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/5/2.
 */
@Service
public class WhgInfoService extends BaseService{
    @Autowired
    private WhgPubInfoMapper whgPubInfoMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 栏目内容mapper
     */
    @Autowired
    private WhgInfColinfoMapper whZxColinfoMapper;
    /**
     * 分页查询资讯公告数据
     * @param paramMap
     * @return
     */
    public PageInfo t_srchList4p(Map<String, Object> paramMap) throws Exception {
        //分页信息
        int page = Integer.parseInt((String)paramMap.get("page"));
        int rows = Integer.parseInt((String)paramMap.get("rows"));

        //带条件的分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.whgPubInfoMapper.t_srchList4p(paramMap);

        // 取分页信息
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);
        return pageInfo;
    }

    /**
     *
     * @param info
     * @param user
     * @param request
     * @return
     */
    public void t_add(WhgInfColinfo info, WhgSysUser user, HttpServletRequest request, String entityid,Integer ispub) throws Exception {
        info.setClnfid(IDUtils.getID());
        //info.setClnfcrttime(new Date());
        Date now = new Date();
        info.setClnfopttime(now);
        if (info.getClnfcrttime() == null) {
            info.setClnfcrttime(now);
        }
        if(ispub != null && ispub.equals(1)){//发布
            info.setClnfstata(EnumBizState.STATE_PUB.getValue());
            info.setPublisher(user.getId());
            info.setPublishdate(now);
        }else {//编辑
            info.setClnfstata(EnumBizState.STATE_CAN_EDIT.getValue());
            info.setCrtuser(user.getId());
            //info.setClnfcrttime(now);
        }
       // info.setClnvenueid(user.getCultid());
        info.setCrtuser(user.getId());
        info.setDelstate(0);
        info.setTotop(0);
        this.whZxColinfoMapper.insert(info);

        WhgPubInfo pubInfo = new WhgPubInfo();
        pubInfo.setClnftype(info.getClnftype());
        pubInfo.setId(IDUtils.getID());
        pubInfo.setClnfid(info.getClnfid());
        pubInfo.setEntityid(entityid);
        this.whgPubInfoMapper.insert(pubInfo);
    }

    /**
     * 根据ID查找资讯公告信息
     * @param id
     * @return
     */
    public Map serchOne(String id) throws Exception {

        return this.whgPubInfoMapper.t_srchOne(id);
    }

    /**
     * 编辑资讯公告信息
     * @param info
     * @param user
     * @param request
     * @param entityid
     */
    public void t_edit(WhgInfColinfo info, WhgSysUser user, HttpServletRequest request, String entityid,Integer ispub)throws Exception {
        info.setTotop(0);
        Date now = new Date();
        if(ispub != null && ispub.equals(1)){
            info.setClnfstata(EnumBizState.STATE_PUB.getValue());
            info.setPublisher(user.getId());
            info.setPublishdate(now);
        }else {
            info.setClnfstata(EnumBizState.STATE_CAN_EDIT.getValue());
            info.setCrtuser(user.getId());
            info.setClnfcrttime(now);
        }
        this.whZxColinfoMapper.updateByPrimaryKeySelective(info);

        Example example = new Example(WhgPubInfo.class);
        example.createCriteria().andEqualTo("clnfid",info.getClnfid()).andEqualTo("clnftype",info.getClnftype());
        List<WhgPubInfo> pubInfo = whgPubInfoMapper.selectByExample(example);
        if(pubInfo.size()>0){
            for(int i = 0; i<pubInfo.size(); i++){
                pubInfo.get(i).setClnftype(info.getClnftype());
                this.whgPubInfoMapper.updateByPrimaryKey(pubInfo.get(i));
            }
        }
    }

    /**
     * 删除资讯公告
     * @param id
     */
    public void t_del(String id) throws Exception {
        whgPubInfoMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询可以进行选择的资讯公告
     * @param paramMap
     * @return
     */
    public PageInfo t_srchcolinfoList4p(Map<String, Object> paramMap,WhgSysUser whgSysUser)throws Exception {
        List<String> cultids=new ArrayList<>();
        List<String> deptids=new ArrayList<>();
        //分页信息
        int page = Integer.parseInt((String)paramMap.get("page"));
        int rows = Integer.parseInt((String)paramMap.get("rows"));
//        int entity = Integer.parseInt((String)paramMap.get("entity"));
//        Example example = new Example(WhZxColinfo.class);
//        Example.Criteria c = example.createCriteria();
//        if(entity == 1){
//            c.andIn("clnftype",Arrays.asList("2016111900000020","2016111900000021"));
//        }
//        if(entity == 2){
//            c.andIn("clnftype",Arrays.asList("2016111900000012","2016111900000018"));
//        }
//        if(entity == 3){
//            c.andIn("clnftype",Arrays.asList("2016112200000004","2016112200000005"));
//        }
//        c.andEqualTo("clnfstata",3);
//        example.setOrderByClause("clnfcrttime desc");
        if (paramMap.get("cultid") == null || paramMap.get("cultid") == "") {
            try {
                cultids = this.whgSystemUserService.getAllCultId4PMS(whgSysUser.getId());
                if (cultids!=null && cultids.size()>0){
                    paramMap.put("cultids", cultids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }else{
            cultids.add((String) paramMap.get("cultid"));
            paramMap.put("cultids", cultids);
        }
        if (paramMap.get("deptid") == null || paramMap.get("deptid") == "") {
            try {
                deptids= this.whgSystemUserService.getAllDeptId4PMS(whgSysUser.getId());
                if (deptids != null && deptids.size() > 0) {
                    paramMap.put("deptids", deptids);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }else{
            deptids.add((String) paramMap.get("deptid"));
            paramMap.put("deptids", deptids);
        }
        //带条件的分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.whgPubInfoMapper.t_srchcolinfoList4p(paramMap);

        // 取分页信息
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);
        return pageInfo;
    }

    /**
     * 选择添加资讯公告
     * @param request
     * @param ids
     */
    public void t_seladd(String ids,String entityid,  HttpServletRequest request) throws Exception{
        Example example = new Example(WhgInfColinfo.class);
        example.createCriteria().andIn("clnfid",Arrays.asList(ids.split("\\s*,\\s*")));
        List<WhgInfColinfo> list = whZxColinfoMapper.selectByExample(example);
        WhgPubInfo info = new WhgPubInfo();
        if(list.size() > 0){
            for(int i =0; i<list.size(); i++){
                info.setId(IDUtils.getID());
                info.setClnfid(list.get(i).getClnfid());
                info.setEntityid(entityid);
                info.setClnftype(list.get(i).getClnftype());
                whgPubInfoMapper.insert(info);
            }
        }

    }

    /**
     * 查询实体关联的资讯信息
     * @param entityid 实体ID
     * @param clnftype 栏目ID
     * @return
     */
    /*public List<WhgInfColinfo> findColinfo(String entityid, String clnftype)throws Exception{
        WhgInfColinfo colinfo = new WhgInfColinfo();
        List<WhgInfColinfo> list = new ArrayList<>();
        Example example = new Example(WhgPubInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("entityid",entityid);
        if (clnftype!=null && !clnftype.isEmpty()) {
            c.andEqualTo("clnftype", clnftype);
        }
        List<WhgPubInfo> Info = whgPubInfoMapper.selectByExample(example);
        if(Info.size() > 0){
            for(int i = 0; i < Info.size(); i++){
                String clnfid = Info.get(i).getClnfid();
                Example example1 = new Example(WhgInfColinfo.class);
                Example.Criteria c1 = example1.createCriteria();
                c1.andEqualTo("clnfid",clnfid);
                c1.andEqualTo("clnfstata", EnumBizState.STATE_PUB.getValue());
                c1.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());
                example1.setOrderByClause("clnfcrttime desc");
                List<WhgInfColinfo> colinfoList = whZxColinfoMapper.selectByExample(example1);
                if(colinfoList.size() > 0){
                    list.add(colinfoList.get(0));
                }
            }
        }
        return list;
    }*/

    public PageInfo findColinfo(Integer page, Integer pageSize, String entityid, String clnftype) throws Exception{
        Example example = new Example(WhgPubInfo.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("entityid",entityid);
        if (clnftype!=null && !clnftype.isEmpty()) {
            c.andEqualTo("clnftype", clnftype);
        }
        List<WhgPubInfo> pubs = whgPubInfoMapper.selectByExample(example);
        if (pubs == null || pubs.size() == 0){
            return new PageInfo(new ArrayList());
        }
        List clnfids = new ArrayList();
        for (WhgPubInfo pub : pubs){
            clnfids.add(pub.getClnfid());
        }

        Example example1 = new Example(WhgInfColinfo.class);
        example1.or().andEqualTo("clnfstata", EnumBizState.STATE_PUB.getValue())
                .andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue())
                .andIn("clnfid", clnfids);

        if (page != null && pageSize != null){
            PageHelper.startPage(page, pageSize);
        }
        List<WhgInfColinfo> colinfoList = whZxColinfoMapper.selectByExample(example1);
        return new PageInfo(colinfoList);
    }
}
