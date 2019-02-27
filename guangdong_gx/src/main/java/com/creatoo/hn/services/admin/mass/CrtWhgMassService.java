package com.creatoo.hn.services.admin.mass;

import com.creatoo.hn.dao.mapper.mass.CrtWhgMassMapper;
import com.creatoo.hn.dao.model.WhgMassArtist;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by rbg on 2017/11/4.
 */
@SuppressWarnings("ALL")
@Service
public class CrtWhgMassService extends BaseService {

    @Autowired
    private CrtWhgMassMapper crtWhgMassMapper;

    @Autowired
    private WhgMassArtistService whgMassArtistService;

    @Autowired
    private WhgMassLibraryService whgMassLibraryService;


    /**
     * 届次相关的群文资讯
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectMassRefZxList(int page, int pageSize, Map recode) throws Exception {
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgMassMapper.selectRefzxList(recode);

        return new PageInfo(list);
    }

    /**
     * 群文资讯
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectZxList4Mass(int page, int pageSize, Map recode) throws Exception {
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgMassMapper.selectZxList4Mass(recode);

        return new PageInfo(list);
    }

    /**
     * 添加届次与资讯关联
     * @param clnfids
     * @param mid
     * @param mtype
     * @return
     * @throws Exception
     */
    public ResponseBean addMassRefZxs(String clnfids, String mid, String mtype) throws Exception{
        ResponseBean rb = new ResponseBean();
        if (clnfids == null || clnfids.isEmpty() || mid == null || mid.isEmpty()) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("参数获取失败");
            return rb;
        }

        List<Map> infos = new ArrayList<Map>();

        List<String> ids = Arrays.asList( clnfids.split("\\s*,\\s*") );
        for (String id : ids) {
            if (id != null && !id.isEmpty()) {
                Map info = new HashMap();
                info.put("zxid", id);
                info.put("mid", mid);
                info.put("mtype", mtype);

                infos.add(info);
            }
        }

        this.crtWhgMassMapper.deleteMassRefZxList(ids, mid, mtype);
        this.crtWhgMassMapper.insertMassRefZxList(infos);

        return rb;
    }

    /**
     * 移除届次与资讯关联
     * @param clnfids
     * @param mid
     * @param mtype
     * @return
     * @throws Exception
     */
    public ResponseBean removeMassRefZxs(String clnfids, String mid, String mtype) throws Exception {
        ResponseBean rb = new ResponseBean();

        if (clnfids == null || clnfids.isEmpty() || mid == null || mid.isEmpty()) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("参数获取失败");
            return rb;
        }

        List<String> zxids = Arrays.asList( clnfids.split("\\s*,\\s*") );
        this.crtWhgMassMapper.deleteMassRefZxList(zxids, mid, mtype);
        return rb;
    }

    /**
     * 清理 届次的资讯关联
     * @param mid
     * @param mtype
     * @throws Exception
     */
    public void clearMassRefZx4Mid(String mid, String mtype) throws Exception{
        if (mid == null && mid.isEmpty()) {
            return;
        }

        this.crtWhgMassMapper.deleteMassRefZx4Mid(mid, mtype);
    }




    /**
     * 届次相关的人才查询
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectMassRefRcList(int page, int pageSize, Map recode) throws Exception {
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgMassMapper.selectRefrcList(recode);

        return new PageInfo(list);
    }

    /**
     * 可选人才查询
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectRcList4Mass(int page, int pageSize, Map recode) throws Exception {
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgMassMapper.selectRcList(recode);

        return new PageInfo(list);
    }

    /**
     * 添加人才关联
     * @param ids
     * @param mid
     * @param mtype
     * @return
     * @throws Exception
     */
    public ResponseBean addMassRefRcs(String ids, String mid, String mtype) throws Exception{
        ResponseBean rb = new ResponseBean();
        if (ids == null || ids.isEmpty() || mid == null || mid.isEmpty()) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("参数获取失败");
            return rb;
        }

        List<Map> infos = new ArrayList<Map>();

        List<String> _ids = Arrays.asList( ids.split("\\s*,\\s*") );
        for (String id : _ids) {
            if (id != null && !id.isEmpty()) {
                Map info = new HashMap();
                info.put("rcid", id);
                info.put("mid", mid);
                info.put("mtype", mtype);

                infos.add(info);
            }
        }

        this.crtWhgMassMapper.deleteMassRefRcList(_ids, mid, mtype);
        this.crtWhgMassMapper.insertMassRefRcList(infos);

        return rb;
    }

    /**
     * 删除人才关联
     * @param ids
     * @param mid
     * @param mtype
     * @return
     * @throws Exception
     */
    public ResponseBean removeMassRefRcs(String ids, String mid, String mtype) throws Exception {
        ResponseBean rb = new ResponseBean();

        if (ids == null || ids.isEmpty() || mid == null || mid.isEmpty()) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("参数获取失败");
            return rb;
        }

        List<String> rcids = Arrays.asList( ids.split("\\s*,\\s*") );
        this.crtWhgMassMapper.deleteMassRefRcList(rcids, mid, mtype);
        return rb;
    }

    /**
     * 清除人才关联
     * @param mid
     * @param mtype
     * @throws Exception
     */
    public void clearMassRefRc4Mid(String mid, String mtype) throws Exception{
        if (mid == null && mid.isEmpty()) {
            return;
        }

        this.crtWhgMassMapper.deleteMassRefRc4Mid(mid, mtype);
    }





    /**
     * 届次相关的团队查询
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectMassRefTdList(int page, int pageSize, Map recode) throws Exception {
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgMassMapper.selectReftdList(recode);

        return new PageInfo(list);
    }

    /**
     * 可选团队查询
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectTdList4Mass(int page, int pageSize, Map recode) throws Exception {
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgMassMapper.selectTdList(recode);

        return new PageInfo(list);
    }

    /**
     * 添加团队关联
     * @param ids
     * @param mid
     * @param mtype
     * @return
     * @throws Exception
     */
    public ResponseBean addMassRefTds(String ids, String mid, String mtype) throws Exception{
        ResponseBean rb = new ResponseBean();
        if (ids == null || ids.isEmpty() || mid == null || mid.isEmpty()) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("参数获取失败");
            return rb;
        }

        List<Map> infos = new ArrayList<Map>();

        List<String> _ids = Arrays.asList( ids.split("\\s*,\\s*") );
        for (String id : _ids) {
            if (id != null && !id.isEmpty()) {
                Map info = new HashMap();
                info.put("tdid", id);
                info.put("mid", mid);
                info.put("mtype", mtype);

                infos.add(info);
            }
        }

        this.crtWhgMassMapper.deleteMassRefTdList(_ids, mid, mtype);
        this.crtWhgMassMapper.insertMassRefTdList(infos);

        return rb;
    }

    /**
     * 删除团队关联
     * @param ids
     * @param mid
     * @param mtype
     * @return
     * @throws Exception
     */
    public ResponseBean removeMassRefTds(String ids, String mid, String mtype) throws Exception {
        ResponseBean rb = new ResponseBean();

        if (ids == null || ids.isEmpty() || mid == null || mid.isEmpty()) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("参数获取失败");
            return rb;
        }

        List<String> tdids = Arrays.asList( ids.split("\\s*,\\s*") );
        this.crtWhgMassMapper.deleteMassRefTdList(tdids, mid, mtype);
        return rb;
    }

    /**
     * 清除团队关联
     * @param mid
     * @param mtype
     * @throws Exception
     */
    public void clearMassRefTd4Mid(String mid, String mtype) throws Exception{
        if (mid == null && mid.isEmpty()) {
            return;
        }

        this.crtWhgMassMapper.deleteMassRefTd4Mid(mid, mtype);
    }





    //TODO API Methods

    /**
     * 查询关联资讯信息列表
     * @param page
     * @param pageSize
     * @param batchid
     * @return
     * @throws Exception
     */
    public PageInfo selectMassRefZxlist(int page, int pageSize, String mtype, String mid, String cultid) throws Exception {
        PageHelper.startPage(page, pageSize);
        List list = this.crtWhgMassMapper.apiSelectMassZxlist(mtype, mid, cultid);
        return new PageInfo(list);
    }

    /**
     * 查询届次的关联艺术人才列表
     * @param page
     * @param pageSize
     * @param batchid
     * @return
     * @throws Exception
     */
    public PageInfo selectMassBatchRclist(int page, int pageSize, String batchid) throws Exception {
        PageHelper.startPage(page, pageSize);
        List list = this.crtWhgMassMapper.apiSelectBatchRclist(batchid);
        return new PageInfo(list);
    }

    /**
     * 查询届次的关联艺术团队列表
     * @param page
     * @param pageSize
     * @param batchid
     * @return
     * @throws Exception
     */
    public PageInfo selectMassBatchTdlist(int page, int pageSize, String batchid) throws Exception {
        PageHelper.startPage(page, pageSize);
        List list = this.crtWhgMassMapper.apiSelectBatchTdlist(batchid);
        return new PageInfo(list);
    }

    /**
     * 查询艺术人才列表
     * @param page
     * @param pageSize
     * @param recode
     * @return
     * @throws Exception
     */
    public PageInfo selectMassArtistList(int page, int pageSize, Map params) throws Exception{
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgMassMapper.apiSelectMassArtistList(params);
        for(Map info : list){
            if (!info.containsKey("feattype")){
                info.put("feattype", "");
                continue;
            }
            String feattype = this.whgMassArtistService.getFeattypeText( info.get("feattype").toString() );
            info.put("feattype", feattype);
        }
        return new PageInfo(list);
    }

    /**
     * 群文个人收藏
     * @param page
     * @param pageSize
     * @param userid
     * @return
     * @throws Exception
     */
    public PageInfo selectMassCollentions(int page, int pageSize, String userid, String cmreftyp) throws Exception{
        PageHelper.startPage(page, pageSize);
        List<Map> list = this.crtWhgMassMapper.apiSelectMassCollections(userid, cmreftyp);
        if (list != null && list.size() > 0) {
            for(Map ent : list){
                if (ent.get("cmreftyp") == null || ent.get("cmrefid") == null) continue;

                if (ent.get("cmreftyp").toString().equals(EnumTypeClazz.TYPE_MASS_ARTIST.getValue()) ) {
                    WhgMassArtist info = this.whgMassArtistService.srchOne(ent.get("cmrefid").toString());
                    if (info != null) {
                        ent.put("imgurl", info.getImgurl());
                    }
                }

                if (ent.get("cmreftyp").toString().equals(EnumTypeClazz.TYPE_MASS_RESOURCE.getValue()) ) {
                    /*
                    WhgMassLibraryService.findResourceById 根据资源标识查询资源详情
                    map.get("resourcetype")  // img-图片  video-视频  audio-音频 file-文档
                    map.get("respicture")  //图片地址
                    map.get("libid")  //资源库标识
                    map.get("resid")  //资源标识
                     */
                    Map info = this.whgMassLibraryService.findResourceById(ent.get("cmrefid").toString());
                    if (info != null) {
                        ent.put("resourcetype", info.get("resourcetype"));
                        ent.put("respicture", info.get("respicture"));
                        ent.put("libid", info.get("libid"));
                        ent.put("resid", info.get("resid"));
                    }
                }

            }
        }

        return new PageInfo(list);
    }
}
