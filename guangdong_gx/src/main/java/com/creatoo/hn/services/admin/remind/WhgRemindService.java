package com.creatoo.hn.services.admin.remind;

import com.creatoo.hn.dao.mapper.admin.WhgRemindMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.mass.WhgMassLibraryService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/1.
 */
@Service
public class WhgRemindService extends BaseService{

    @Autowired
    private WhgRemindMapper whgRemindMapper;

    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 群文资源服务类
     */
    @Autowired
    private WhgMassLibraryService whgMassLibraryService;

    /**
     * 查询内部供需各模块待审核待发布数量
     * @param whgSysUser
     * @return
     */
    public List t_nbgxsrchList(WhgSysUser whgSysUser) throws Exception{
        List<String> cultids=new ArrayList<>();
        List<String> deptids=new ArrayList<>();
        Map param = new HashMap();
        if(whgSysUser!=null){
            try {
                cultids = this.whgSystemUserService.getAllCultId4PMS(whgSysUser.getId());
                deptids = this.whgSystemUserService.getAllDeptId4PMS(whgSysUser.getId());
                param.put("cultids", cultids);
                param.put("deptids", deptids);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        List _list = whgRemindMapper.t_nbgxsrchList4p(param);

        return _list;
    }

    /**
     * 查询外部供需各模块待审核待发布数量
     * @param whgSysUser
     * @return
     */
    public List t_wbgxsrchList(WhgSysUser whgSysUser) {
        List<String> cultids=new ArrayList<>();
        List<String> deptids=new ArrayList<>();
        Map param = new HashMap();
        if(whgSysUser!=null){
            try {
                cultids = this.whgSystemUserService.getAllCultId4PMS(whgSysUser.getId());
                deptids = this.whgSystemUserService.getAllDeptId4PMS(whgSysUser.getId());
                param.put("cultids", cultids);
                param.put("deptids", deptids);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        List _list = whgRemindMapper.t_wbgxsrchList(param);
        List<Map> rtnList = new ArrayList<>();
        Map<Object, Object> rowmap = new HashMap<>();
        if(_list != null){
            for(Object map : _list){
                Object type = ((Map)map).get("type");
                Object cnt = ((Map)map).get("cnt");
                rowmap.put(type, cnt);
            }
            rtnList.add(rowmap);
        }
        return rtnList;
    }

    /**
     * 网络培训各模块待审核待发布数量
     * @param whgSysUser
     * @return
     */
    public List t_wlpxsrchList(WhgSysUser whgSysUser)throws Exception {
        List<String> cultids=new ArrayList<>();
        List<String> deptids=new ArrayList<>();
        Map param = new HashMap();
        if(whgSysUser!=null){
            try {
                cultids = this.whgSystemUserService.getAllCultId4PMS(whgSysUser.getId());
                deptids = this.whgSystemUserService.getAllDeptId4PMS(whgSysUser.getId());
                param.put("cultids", cultids);
                param.put("deptids", deptids);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        List _list = whgRemindMapper.t_wlpxsrchList(param);
        List<Map> rtnList = new ArrayList<>();
        Map<Object, Object> rowmap = new HashMap<>();
        if(_list != null){
            for(Object map : _list){
                Object type = ((Map)map).get("type");
                Object cnt = ((Map)map).get("cnt");
                rowmap.put(type, cnt);
            }
            rtnList.add(rowmap);
        }

        return rtnList;
    }


    /**
     * 群文待办统计
     * @param whgSysUser
     * @return
     * @throws Exception
     */
    public Map t_massSrchList(WhgSysUser whgSysUser) throws Exception{
        Map rest = new HashMap();

        if (whgSysUser == null || whgSysUser.getId() == null) {
            return rest;
        }

        List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(whgSysUser.getId());
        if (cultids == null || cultids.size() ==0){
            //cultids = null;
            return rest;
        }
        List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(whgSysUser.getId());
        if (deptids == null || deptids.size() ==0){
            //deptids = null;
            return rest;
        }

        Map param = new HashMap();
        param.put("cultids", cultids);
        param.put("deptids", deptids);

        List _list = whgRemindMapper.t_massSrchList(param);

        if(_list != null){
            for(Object map : _list){
                Object type = ((Map)map).get("type");
                Object cnt = ((Map)map).get("cnt");
                rest.put(type, cnt);
            }
        }

        //群文资源待审核/待发布
        long M_MAS_RES_C = this.whgMassLibraryService.findResourceByPaging(1,1, null, null, null, null, whgSysUser, "9", "0", null).getTotal();
        long M_MAS_RES_P = this.whgMassLibraryService.findResourceByPaging(1,1, null, null, null, null, whgSysUser, "2", "0", null).getTotal();
        rest.put("M_MAS_RES_C", M_MAS_RES_C);
        rest.put("M_MAS_RES_P", M_MAS_RES_P);
        return rest;
    }
}
