package com.creatoo.hn.controller.api.apimass;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.mass.*;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.*;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbg on 2017/11/6.
 */
@SuppressWarnings("ALL")
@RestController
@CrossOrigin
@RequestMapping("/api/mass")
public class ApiMassController extends BaseController {

    @Autowired
    private WhgMassBrandService whgMassBrandService;

    @Autowired
    private CrtWhgMassService crtWhgMassService;

    @Autowired
    private WhgMassArtistService whgMassArtistService;

    @Autowired
    private WhgSystemDeptService whgSystemDeptService;

    @Autowired
    private WhgResManageService whgResManageService;

    /**
     * 管理员服务
     */
    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 资源库服务
     */
    @Autowired
    private WhgMassLibraryService whgMassLibraryService;

    @Autowired
    private WhgMassViewService massViewService;

    @Autowired
    private WhgMassDownService massDownService;


    /**
     * 资源库增加访问量
     *
     * @param massView
     * @param servletRequest
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/addVisit", method = {RequestMethod.GET, RequestMethod.POST})
    public Object increaseVisit(WhgMassView massView, HttpServletRequest servletRequest) {
        ApiResultBean arb = new ApiResultBean();
        try {
            String ip = new ReqParamsUtil().gerClientIP(servletRequest);
            massView.setId(IDUtils.getID32());
            massView.setIp(ip);
            massViewService.add(massView);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(101);
            arb.setMsg("添加浏览量失败");
        }
        return arb;
    }


    /**
     * 获取群文资源库详细信息
     *
     * @param id 文化专题id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/library/info", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassInfo(String id) {
        ApiResultBean arb = new ApiResultBean();

        try {
            WhgMassLibrary info = this.whgMassLibraryService.findById(id);
            arb.setData(info);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 获取资源库访问量
     *
     * @param massView
     * @param servletRequest
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getPageViewCount", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getPageViewCount(WhgMassView massView) {
        ApiResultBean arb = new ApiResultBean();
        try {
            Integer visitCount = massViewService.selectCountByResId(massView.getResid());
            arb.setData(visitCount == null ? 0 : visitCount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(101);
            arb.setMsg("获取资源库访问量失败");
        }
        return arb;
    }

    @CrossOrigin
    @RequestMapping(value = "/getInfoByResId", method = {RequestMethod.GET, RequestMethod.POST})
    public Object selectInfoByResId(String libid,String resid,String userid,Integer massType) {
        ApiResultBean arb = new ApiResultBean();
        try {
            Map info = whgMassLibraryService.selectInfoByResId(libid,resid,userid,massType);
            arb.setData(info);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(101);
            arb.setMsg("获取资源库访问量失败");
        }
        return arb;
    }



    /**
     * 获取资源库下载量
     *
     * @param massView
     * @param servletRequest
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getDownloadCount", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getDownloadCount(WhgMassView massView) {
        ApiResultBean arb = new ApiResultBean();
        try {
            Integer downloadCount = massDownService.seletByresId(massView.getResid());
            arb.setData(downloadCount == null ? 0 : downloadCount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            arb.setCode(101);
            arb.setMsg("获取资源库下载量失败");
        }
        return arb;
    }

    /**
     * 获取资源库列表
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/massLibraryPage", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassLibraryList(WhgMassLibrary recode, WebRequest request,
                                     @RequestParam(value = "userid") String userid,
                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
           /* WhgMassLibrary whgMassLibrary = new WhgMassLibrary();
            if(StringUtils.isNotEmpty(recode.getResourcetype())){
                whgMassLibrary.setResourcetype(recode.getResourcetype());
            }
            if(StringUtils.isNotEmpty(recode.getCultid())) {
                whgMassLibrary.setCultid(recode.getCultid());
            }*/
            recode.setState(EnumState.STATE_YES.getValue());
            PageInfo pageInfo = this.whgMassLibraryService.findByPaging4(page, pageSize, null, null, recode, userid);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 是否是用户表中的手机号
     *
     * @param phone
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/isPhone", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object isPhone(String phone) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (phone == null || phone.isEmpty()) {
                throw new Exception("params is null");
            }
            boolean isOk = this.whgSystemUserService.isPhone(phone);
            arb.setData(isOk ? 1 : 0);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
        }
        return arb;
    }

    /**
     * 找回密码
     *
     * @param mobile
     * @param code
     * @param password
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/setPasswd", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object setPasswd(String mobile, String code, String password) {
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = (ApiResultBean) this.whgSystemUserService.setPasswd(mobile, code, password);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("重置密码失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 群文前端通过接口登录验证
     *
     * @param userName
     * @param password
     * @param visitType 访问类型  1 外网访问登录
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/doLogin", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Object doLogin(String username, String password, HttpSession session) throws Exception {
        ApiResultBean arb = new ApiResultBean();
        try {
            WhgSysUser user = this.whgSystemUserService.doLogin(username, password);
            if (!user.getAdmintype().equals(EnumConsoleSystem.masmgr.getValue())) {
                arb.setCode(104);
                arb.setMsg("账号密码不正确!");
            }
            Map rest = new HashMap();
            rest.put("userId", user.getId());
            rest.put("userName", user.getAccount());
            rest.put("mobile", user.getContactnum());
            rest.put("nickName", user.getContact());
            rest.put("idCard", user.getIdcard());
            arb.setData(rest);
        } catch (Exception e) {
            arb.setCode(102);
            arb.setMsg("账号密码不正确!");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 获取群文专题列表
     *
     * @param page
     * @param pageSize
     * @param recode   文化专题对象
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/brand/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassBrands4p(WhgMassBrand recode, WebRequest request,
                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            if (recode != null) {
                BeanMap bm = new BeanMap(recode);
                for (Map.Entry ent : bm.entrySet()) {
                    if (ent.getValue() == null || ent.getValue().toString().isEmpty()) {
                        ent.setValue(null);
                    }
                }
                recode = (WhgMassBrand) bm.getBean();
            }

            recode.setState(EnumBizState.STATE_PUB.getValue());
            recode.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
            PageInfo pageInfo = this.whgMassBrandService.selectMassBrands(page, pageSize, recode);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 获取群文专题详细信息
     *
     * @param id 文化专题id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/brand/info", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassBrand(String id) {
        ApiResultBean arb = new ApiResultBean();

        try {
            WhgMassBrand info = this.whgMassBrandService.srchOne(id);
            arb.setData(info);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 获取专题的届次列表
     *
     * @param brandid  文化专题id
     * @param recode   届次对象
     * @param request
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/batch/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassBrandBatchs4p(String brandid, WhgMassBrandBatch recode, WebRequest request,
                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            BeanMap bm = new BeanMap(recode);
            for (Map.Entry ent : bm.entrySet()) {
                if (ent.getValue() == null || ent.getValue().toString().isEmpty()) {
                    ent.setValue(null);
                }
            }
            recode = (WhgMassBrandBatch) bm.getBean();

            recode.setState(EnumBizState.STATE_PUB.getValue());
            recode.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());

            PageInfo pageInfo = this.whgMassBrandService.selectMassBrandBatchs(page, pageSize, brandid, recode);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 获取届次详细信息
     *
     * @param id 届次id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/batch/info", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassBrandBatch(String id) {
        ApiResultBean arb = new ApiResultBean();

        try {
            WhgMassBrandBatch info = this.whgMassBrandService.srchOneBatch(id);
            arb.setData(info);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 获取届次的资讯列表
     *
     * @param id       届次id
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/batch/zxlist", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassBrandBatchZxlist(String id,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            String mtype = EnumTypeClazz.TYPE_MASS_BATCH.getValue();
            PageInfo pageInfo = this.crtWhgMassService.selectMassRefZxlist(page, pageSize, mtype, id, null);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 获取人才的资讯列表
     *
     * @param id       人才id
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/artist/zxlist", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassBrandArtistZxlist(String id,
                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                           @RequestParam(value = "cultid", required = false) String cultid) {
        ApiResultBean arb = new ApiResultBean();
        try {
            String mtype = EnumTypeClazz.TYPE_MASS_ARTIST.getValue();
            PageInfo pageInfo = this.crtWhgMassService.selectMassRefZxlist(page, pageSize, mtype, id, cultid);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 获取届次的艺术人才列表
     *
     * @param batchid  届次id
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/batch/rclist", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassBrandBatchRclist(String batchid,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo pageInfo = this.crtWhgMassService.selectMassBatchRclist(page, pageSize, batchid);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 获取届次的艺术团队列表
     *
     * @param batchid  届次id
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/batch/tdlist", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassBrandBatchTdlist(String batchid,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            PageInfo pageInfo = this.crtWhgMassService.selectMassBatchTdlist(page, pageSize, batchid);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 获取艺术人才列表
     *
     * @param request  content:模糊内容
     * @param recode
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/artist/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassArtistList(WebRequest request, WhgMassArtist recode,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {

            BeanMap bm = new BeanMap(recode);
            for (Map.Entry ent : bm.entrySet()) {
                if (ent.getValue() == null || ent.getValue().toString().isEmpty()) {
                    ent.setValue(null);
                }
            }

            Map params = new HashMap();
            params.putAll(bm);

            params.put("content", request.getParameter("content"));
            if (recode.getCultid() != null && !recode.getCultid().isEmpty()) {
                params.put("cultid", Arrays.asList(recode.getCultid().split("\\s*,\\s*")));
            }
            if (recode.getDeptid() != null && !recode.getDeptid().isEmpty()) {
                params.put("deptid", this.whgSystemDeptService.srchDeptStrList(recode.getDeptid()));
            }

            PageInfo pageInfo = this.crtWhgMassService.selectMassArtistList(page, pageSize, params);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 获取指定ID的艺术人才信息
     *
     * @param id
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/artist/info", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassArtistInfo(String id) {
        ApiResultBean arb = new ApiResultBean();
        try {
            WhgMassArtist info = this.whgMassArtistService.srchOne(id);

            info.setFeattype(this.whgMassArtistService.getFeattypeText(info.getFeattype()));

            arb.setData(info);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


    /**
     * 获取资源列表
     *
     * @param record   可指定实体类型,实体ID,查出对应的人才或届次的作品列表
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/opus/resources", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getResource4Mass(WhgResource record, @RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        try {
            BeanMap bm = new BeanMap();
            bm.setBean(record);
            for (Map.Entry ent : bm.entrySet()) {
                if (ent.getValue() == null || ent.getValue().toString().isEmpty()) {
                    ent.setValue(null);
                }
            }
            record = (WhgResource) bm.getBean();

            PageInfo pageInfo = this.whgResManageService.getWhtResources(page, pageSize, record);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }

        return arb;
    }

    /**
     * 查询群文个人中心收藏列表
     *
     * @param userid
     * @param page
     * @param pageSize
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/collections", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassCollections(String userid, String cmreftyp,
                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ApiResultBean arb = new ApiResultBean();
        if (userid == null || userid.isEmpty()) {
            return arb;
        }

        try {
            PageInfo pageInfo = this.crtWhgMassService.selectMassCollentions(page, pageSize, userid, cmreftyp);
            arb.setPageInfo(pageInfo);
            arb.setRows(pageInfo.getList());
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

    /**
     * 查询用户资源库权限
     *
     * @param userid 用户ID
     * @param libid  资源库ID
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getMassUserAuth", method = {RequestMethod.GET, RequestMethod.POST})
    public Object getMassUserAuth(String userid, String libid) {
        ApiResultBean arb = new ApiResultBean();
        if (StringUtils.isBlank(userid)) {
            return arb;
        }
        if (StringUtils.isBlank(libid)) {
            return arb;
        }
        try {
            WhgMassUserAuth auth = this.whgMassLibraryService.getMassUserAuth(userid, libid);
            arb.setData(auth != null ? auth : "");
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("查询数据失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }
    /**
     * 文件下载
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @ResponseBody
    @PostMapping(value = "/apiFileDownload")
    public HttpServletResponse apiFileDownload(HttpServletResponse response) {
        try {
            String path = this.getClass().getClassLoader().getResource("template" + File.separator + "API.docx").getPath();
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
            response.setHeader("Connection", "close");
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/OCTET-STREAM");

            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }



    /**
     * 申请权限
     * @param userid
     * @param applytype
     * @param masslibraryid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/user/apply", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object apply(String userid,String applytype,String masslibraryid) {
        ApiResultBean arb = new ApiResultBean();
        try {
            arb = (ApiResultBean) this.whgMassLibraryService.apply(userid, applytype, masslibraryid);
        } catch (Exception e) {
            arb.setCode(101);
            arb.setMsg("申请权限失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }

}
