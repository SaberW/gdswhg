package com.creatoo.hn.controller.admin.mass;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.mass.WhgMassLibraryService;
import com.creatoo.hn.services.admin.mass.WhgMassTypeService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.AliyunOssUtil;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 群文库资源控制器
 * Created by wangxl on 2017/11/19.
 */
@RestController
@RequestMapping("/admin/mass/libres")
public class WhgMassLibraryResourceController extends BaseController {
    /**
     * 资源库服务类
     */
    @Autowired
    private WhgMassLibraryService whgMassLibraryService;

    /**
     * 资源分类服务类
     */
    @Autowired
    private WhgMassTypeService whgMassTypeService;

    /**
     * 下架原因短信
     */
    @Autowired
    private WhgXjReasonService whgXjReasonService;

    /**
     * 文化馆服务
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @GetMapping("/view/{type}")
    @WhgOPT(optType = EnumOptType.MASS_RESOURCE,
            optDesc = {"访问群文库资源列表页", "访问添加群文库资源页面", "访问编辑群文库资源页面"},
            valid = {"type=list", "type=add", "type=edit"})
    public ModelAndView view(@PathVariable("type") String type, @RequestParam(value = "libid", required = false) String libid, @RequestParam(value = "id", required = false) String id) {
        ModelAndView view = new ModelAndView();
        try {
            if (StringUtils.isNotEmpty(libid)) {
                WhgMassLibrary library = this.whgMassLibraryService.findById(libid);
                view.addObject("library", library);
               /* WhgSysCult cult = this.whgSystemCultService.t_srchOne(library.getCultid());
                view.addObject("cult", cult);*/
            }
            if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(libid)) {
                Map res = this.whgMassLibraryService.findResourceById(libid, id);
                view.addObject("res", res);
                if (res != null && res.get("restype") != null) {
                    String restype = (String) res.get("restype");
                    WhgMassType wmType = this.whgMassTypeService.findById(restype);
                    if (wmType != null) {
                        view.addObject("restypeName", wmType.getName());
                    }
                }
            }
            view.setViewName("admin/mass/libres/view_" + type);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return view;
    }

    @GetMapping("/view/share")
    public ModelAndView shareView(@RequestParam(value = "libid", required = false) String libid) {
        ModelAndView view = new ModelAndView();
        try {
            view.addObject("libid", libid);
            view.setViewName("admin/mass/libres/view_share");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return view;
    }


    @PostMapping("/srchList4p")
    public ResponseBean findByPaging(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "rows", defaultValue = "10") Integer rows,
                                     @RequestParam(value = "sort", required = false) String sort,
                                     @RequestParam(value = "order", required = false) String order,
                                     @RequestParam(value = "libid", required = false) String libid,
                                     @RequestParam(value = "resname", required = false) String resname,
                                     @RequestParam(value = "state", required = false) String state,
                                     @RequestParam(value = "ptype", required = false) String ptype,
                                     @RequestParam(value = "delstate", defaultValue = "0") String delstate,
                                     @RequestParam(value = "recommend", required = false) String recommend,
                                     HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            String createUserId = null;
            if (StringUtils.isEmpty(state)) {
                if ("e".equals(ptype)) {
                    createUserId = sysUser.getId();
                } else if ("c".equals(ptype)) {
                    state = "9,2,4,6";
                } else if ("p".equals(ptype)) {
                    state = "9,2,4,6";
                } else if ("r".equals(ptype)) {
                    state = "";
                    delstate = "1";
                }
            }
            PageInfo<Map> pageInfo = this.whgMassLibraryService.findResourceByPaging(page, rows, sort, order, libid, resname, sysUser, state, delstate, recommend);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("/srchList5p")
    public ResponseBean findByPaging1(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "rows", defaultValue = "10") Integer rows,
                                      @RequestParam(value = "sort", required = false) String sort,
                                      @RequestParam(value = "order", required = false) String order,
                                      @RequestParam(value = "libid", required = false) String libid,
                                      @RequestParam(value = "resname", required = false) String resname,
                                      @RequestParam(value = "state", required = false) String state,
                                      @RequestParam(value = "ptype", required = false) String ptype,
                                      @RequestParam(value = "delstate", defaultValue = "0") String delstate,
                                      @RequestParam(value = "recommend", required = false) String recommend,
                                      HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            String createUserId = null;
            if (StringUtils.isEmpty(state)) {
                if ("e".equals(ptype)) {
                    createUserId = sysUser.getId();
                } else if ("c".equals(ptype)) {
                    state = "9,2,4,6";
                } else if ("p".equals(ptype)) {
                    state = "9,2,4,6";
                } else if ("r".equals(ptype)) {
                    state = "";
                    delstate = "1";
                }
            }
            PageInfo<Map> pageInfo = this.whgMassLibraryService.findResourceBySharePaging(page, rows, sort, order, libid, resname, sysUser, state, delstate, recommend);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }


//    @PostMapping("/add")
//    public ResponseBean add(@SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser,
//                            @RequestParam(value="libid")String libid,
//                            HttpServletRequest request){
//        ResponseBean res = new ResponseBean();
//        try {
//            //获取所有的请求参数
//            Map<String, String> paramMap = new HashMap<>();
//            Enumeration<String> names = request.getParameterNames();
//            while(names.hasMoreElements()){
//                String name = names.nextElement();
//                String[] vals = request.getParameterValues(name);
//                String value = StringUtils.join(vals, ",");
//                paramMap.put(name, value);
//            }
//            this.whgMassLibraryService.addResource(sysUser, libid, paramMap);
//        } catch (Exception e) {
//            res.setSuccess(ResponseBean.FAIL);
//            res.setErrormsg(e.getMessage());
//            res.setRows(new ArrayList(0));
//            res.setTotal(0);
//            log.error(e.getMessage());
//        }
//        return res;
//    }

    /**
     * 编辑资源库
     *
     * @return
     */
    @PostMapping("/edit")
    public ResponseBean edit(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
                             @RequestParam String libid,
                             @RequestParam String resid,
                             HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            //获取所有的请求参数
            Map<String, String> paramMap = new HashMap<>();
            Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                String[] vals = request.getParameterValues(name);
                String value = StringUtils.join(vals, ",");
                paramMap.put(name, value);
            }
            String webRootPath = request.getSession().getServletContext().getRealPath("/");
            this.whgMassLibraryService.editResource(sysUser, libid, resid, paramMap, webRootPath);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 分享资源库
     *
     * @return
     */
    @RequestMapping(value = "/shareLib", method = RequestMethod.POST)
    public ResponseBean shareLib(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, String libid, String cultid, HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassLibraryService.shareResource(sysUser, libid, cultid);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 查询已授权文化馆
     *
     * @return
     */
    @RequestMapping(value = "/getAuthorized", method = RequestMethod.POST)
    public ResponseBean getAuthorized(String libid, HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        List<Map<String, String>> rtnList = new ArrayList<>();
        try {
            if (StringUtils.isNotBlank(libid)) {
                WhgMassLibrary library = this.whgMassLibraryService.findById(libid);
                if (library != null && StringUtils.isNotBlank(library.getSharecultid())) {
                    //查询资源库对应的已授权文化馆
                    String[] str = library.getSharecultid().split(",");
                    List<WhgSysCult> cults = this.whgSystemCultService.t_srchByCults(Arrays.asList(str));
                    if (cults != null) {
                        for (WhgSysCult cult : cults) {
                            Map<String, String> map = new HashMap<>();
                            map.put("id", cult.getId());
                            map.put("text", cult.getName());
                            rtnList.add(map);
                        }
                    }
                }
            }
            res.setData(rtnList);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    @PostMapping("/del")
    public ResponseBean del(@RequestParam("libid") String libid, @RequestParam("id") String id, @RequestParam(value = "force", defaultValue = "0") String force) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassLibraryService.deleteResource("1".equals(force), libid, id);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 修改推荐状态
     *
     * @param libid     资源库标识
     * @param id        资源标识
     * @param recommend 推荐状态
     * @return
     */
    @PostMapping("/updateRecommend")
    public ResponseBean updateRecommend(@RequestParam String libid, @RequestParam String id, @RequestParam String recommend) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassLibraryService.updateResourceRecommend(libid, id, recommend);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 修改资源状态
     *
     * @param libid 资源库标识
     * @param id    资源标识
     * @param state 修改后的状态
     * @return
     */
    @PostMapping("/updateState")
    public ResponseBean updateState(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            @RequestParam String libid,
            @RequestParam String id,
            @RequestParam String state,
            String reason,
            String issms
    ) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassLibraryService.updateResourceState(sysUser, libid, id, state);
            if (reason != null && id != null) {// 下架原因
                Map resMap = this.whgMassLibraryService.findResourceById(libid, id);
                WhgXjReason whgReason = new WhgXjReason();
                whgReason.setFkid(id);
                if (resMap != null && resMap.containsKey("publisher")) {
                    whgReason.setTouser((String) resMap.get("publisher"));
                }
                if (resMap != null && resMap.containsKey("resname")) {
                    whgReason.setFktitile((String) resMap.get("resname"));
                }
                if (issms != null) {//是否发送短信
                    whgReason.setIssms(Integer.parseInt(issms));
                }
                whgReason.setCrtuser(sysUser.getId());
                whgReason.setReason(reason);
                whgReason.setFktype("群文资源");
                whgXjReasonService.t_add(whgReason);
            }
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 此处批量提交审核
     *
     * @param libid 资源库标识
     * @param id    资源标识
     * @param state 修改后的状态
     * @return
     */
    @PostMapping("/batchSubmit")
    public ResponseBean batchSubAudit(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser,
            @RequestParam String libid,
            @RequestParam String id,
            @RequestParam String state
    ) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassLibraryService.batchSubmit(sysUser, libid, id, state);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 修改资源状态
     *
     * @param libid    资源库标识
     * @param id       资源标识
     * @param delstate 修改后的删除状态
     * @return
     */
    @PostMapping("/updateDelstate")
    public ResponseBean updateDeltate(@RequestParam String libid, @RequestParam String id, @RequestParam String delstate) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassLibraryService.updateResourceDelstate(libid, id, delstate);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 上传文件后的回调
     *
     * @param libid    资源库标识
     * @param filename 资源名称
     * @param size     大小
     * @param mimeType 类型
     * @param height   高度
     * @param width    宽度
     * @return JSON STATUS
     */
    @CrossOrigin
    @RequestMapping("/notify")
    public Object notify(String uid, String libid, String filename, String size, String mimeType, String height, String width, HttpServletRequest request) {
        Map<String, String> ret = new HashMap<>();
        try {
            //System.out.println("====请求参数======libid:"+libid+"; filename:"+filename+"; size:"+size+"; mimeType:"+mimeType+";  height:"+height+"; width:"+width);
            String webRootPath = request.getSession().getServletContext().getRealPath("/");
            this.whgMassLibraryService.addResource4OSS(uid, libid, filename, size, mimeType, height, width, webRootPath);
            ret.put("Status", "OK");
        } catch (Exception e) {
            ret.put("Status", "OK");
            log.error(e.getMessage(), e);
        }
        return ret;
    }

    /**
     * 同一个资源库下是否存在相同的资源名称
     *
     * @param libid
     * @param resName
     * @return
     */
    @RequestMapping("/existResource")
    public ResponseBean existResource(String libid, String resName) {
        ResponseBean res = new ResponseBean();
        try {
            boolean exist = this.whgMassLibraryService.existResource(libid, resName);
            res.setData(exist ? "yes" : "no");
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 资源库是否可以上传资源
     *
     * @param libid
     * @param resName
     * @return
     */
    @RequestMapping("/libDisabled")
    public ResponseBean libDisabled(String libid, String resName) {
        ResponseBean res = new ResponseBean();
        try {
            boolean disabled = this.whgMassLibraryService.libDisable(libid);
            res.setData(disabled ? "yes" : "no");
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 获取资源库表单中的必须字段
     *
     * @param libid
     * @return
     */
    @RequestMapping("/queryLibFormRequiredField")
    public ResponseBean queryLibFormRequiredField(String libid, String resid) {
        ResponseBean res = new ResponseBean();
        try {
            List<WhgMassLibraryFormField> fields = this.whgMassLibraryService.queryLibFormRequiredField(libid, resid);
            res.setRows(fields);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }


    @RequestMapping("/download")
    public String getOssFile(HttpServletRequest request, HttpServletResponse response, String key) {

        // 创建OSSClient实例
        // endpoint以杭州为例，其它region请按实际情况填写，1改为自己的
        String endpoint = AliyunOssUtil.getEndpoint();
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
        String accessKeyId = AliyunOssUtil.getAccessKeyId();
        String accessKeySecret = AliyunOssUtil.getAccessKeySecret();
        String bucketName = AliyunOssUtil.getBucketName();

        //oss配置
       /* String endpoint =  "http://oss-cn-shanghai.aliyuncs.com";
        String accessKeyId ="LTAIuwerek5QXVEu";
        String accessKeySecret = "tUwAa6rw6A8DFT6aoW3VCnxYoucoHK";
        String bucketName = "szwhg-gds-admin";*/

        //要下载的文件名（Object Name）字符串，中间用‘,’间隔。文件名从bucket目录开始.5改为自己的
        //String key = fileurls.replaceAll("http://szwhg-gds-admin.oss-cn-shanghai.aliyuncs.com/","");
        try {
            // 初始化
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            //6改为自己的名称
            String fileName = "资源导出.zip";
            // 创建临时文件
            File zipFile = File.createTempFile("资源导出", ".zip");
            FileOutputStream f = new FileOutputStream(zipFile);
            /**
             * 作用是为任何OutputStream产生校验和
             * 第一个参数是制定产生校验和的输出流，第二个参数是指定Checksum的类型 （Adler32（较快）和CRC32两种）ossobject 获取大小
             */
            CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
            // 用于将数据压缩成Zip文件格式
            ZipOutputStream zos = new ZipOutputStream(csum);

            String[] keylist = key.split(",");
            for (String ossfile : keylist) {
                // 获取Object，返回结果为OSSObject对象
                OSSObject ossObject = ossClient.getObject(bucketName, ossfile);
                // 读去Object内容  返回
                InputStream inputStream = ossObject.getObjectContent();
                // 对于每一个要被存放到压缩包的文件，都必须调用ZipOutputStream对象的putNextEntry()方法，确保压缩包里面文件不同名

                zos.putNextEntry(new ZipEntry(ossfile));
                int bytesRead = 0;
                // 向压缩文件中输出数据
                while ((bytesRead = inputStream.read()) != -1) {
                    zos.write(bytesRead);
                }
                inputStream.close();
                zos.closeEntry(); // 当前文件写完，定位为写入下一条项目
            }
            zos.close();
            String header = request.getHeader("User-Agent").toUpperCase();
            if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
                fileName = URLEncoder.encode(fileName, "utf-8");
                fileName = fileName.replace("+", "%20");    //IE下载文件名空格变+号问题
            } else {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            }
            response.reset();
            response.setContentType("text/plain");
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Location", fileName);
            response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            FileInputStream fis = new FileInputStream(zipFile);
            BufferedInputStream buff = new BufferedInputStream(fis);
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            byte[] car = new byte[1024];
            int l = 0;
            while (l < zipFile.length()) {
                int j = buff.read(car, 0, 1024);
                l += j;
                out.write(car, 0, j);
            }
            // 关闭流
            fis.close();
            buff.close();
            out.close();

            ossClient.shutdown();
            // 删除临时文件
            zipFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping("/readerTxt")
    public void readerExcel(String txturl) throws IOException {
        OSSClient ossClient = AliyunOssUtil.getOssClient();
        String bucketName = AliyunOssUtil.getBucketName();

/*
        ObjectListing listing = ossClient.listObjects(bucketName, "sample/2/深圳文化馆");
// 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            System.out.println(objectSummary.getKey());
            System.out.println(objectSummary.getSize());//输出每个object文件的大小
        }
*/

/*
// 构造ListObjectsRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
// "/" 为文件夹的分隔符
        listObjectsRequest.setDelimiter("/");
// 列出fun目录下的所有文件和文件夹
        listObjectsRequest.setPrefix("sample/2/深圳文化馆/");
        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
// 遍历所有Object*/

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        listObjectsRequest.setPrefix("sample/2/深圳文化馆/");// 递归列出fun目录下的所有文件
        ObjectListing listing = null;// 遍历所有Object
        final int maxKeys = 30;
        String nextMarker = null;
        do {
            listing = ossClient.listObjects(listObjectsRequest.withMarker(nextMarker).withMaxKeys(maxKeys));
            List<OSSObjectSummary> sums = listing.getObjectSummaries();
            for (OSSObjectSummary s : sums) {
                String key = s.getKey();
                if (s.getSize() > 0) {
                    if (key.indexOf("发布级") >= 0 || key.indexOf("合成PDF") >= 0) {
                        key = "http://szwhg-gds-admin.oss-cn-shanghai.aliyuncs.com/" + key;
                        whgMassLibraryService.saveTxtUrl(key, s.getSize());
                    }
                }
            }
            nextMarker = listing.getNextMarker();
        } while (listing.isTruncated());


       /* URL url = new URL(txturl);
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String lineTxt = null;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            if (lineTxt.indexOf("发布级") >= 0 || lineTxt.indexOf("合成PDF")>=0) {
               String fileUrl = lineTxt.replace("/gdtest", "http://szwhg-gds-admin.oss-cn-shanghai.aliyuncs.com/sample");
                URL url1 = new URL(fileUrl);
                URLConnection uc = url1.openConnection();
                int fileSize = uc.getContentLength();
                System.out.println(fileSize);
                //whgMassLibraryService.saveTxtUrl(fileUrl,fileSize);
            }


        }
        is.close();*/
    }

    public static int getFileSize(String fileurl) throws IOException {
        URL url = new URL(fileurl);
        URLConnection uc = url.openConnection();
        int fileSize = uc.getContentLength();
        System.out.println(fileSize);
        return fileSize;
    }

    /**
     * 获取资源库表单中的必须字段
     *
     * @param libid
     * @return
     */
    @RequestMapping("/readerExcel")
    public ResponseBean readerExcel(@SessionAttribute(Constant.SESSION_ADMIN_KEY) WhgSysUser sysUser, String libid, String resid) {
        ResponseBean res = new ResponseBean();
        try {
            List<String[]> list = XLSXCovertCSVReader
                    .readerExcel(
                            "C:\\Users\\Administrator\\Desktop\\群文\\2018-12-20.xlsx",
                            "文档", 21);
            String level = "";
            int tm = 0,zrz = 0,ms = 0,dh = 0,sylb = 0,ml = 0,ly = 0;
            if (list != null && list.size() > 0) {
                String[] titlearray = list.get(0);
                for (int i = 0; i < titlearray.length; i++) {
                    if(titlearray[i]==null)  break;
                    if (titlearray[i].equals("题名")) tm = i;
                    else if (titlearray[i].equals("责任者")) zrz = i;
                    else if (titlearray[i].equals("描述")) ms = i;
                    else if (titlearray[i].equals("档号")) dh = i;
                    else if (titlearray[i].equals("使用类别")) sylb = i;
                    else if (titlearray[i].equals("门类")) ml = i;
                    else if (titlearray[i].equals("来源")) ly = i;
                }
                String danghao = "",resintroduce = "", resorigin = "",resauthor = "",resarttype = "",resname = "";

                for (String[] record : list) {
                    if (record[sylb].indexOf("发布级") >= 0) {
                        if (dh != 0) danghao = record[dh];
                        if (tm != 0) resname = record[tm];
                        if (ms != 0) resintroduce = record[ms];
                        if (ly != 0) resorigin = record[ly];
                        if (ml != 0) resarttype = record[ml];
                        if (zrz != 0) resauthor = record[zrz];

                        whgMassLibraryService.addExcelResource(sysUser, libid, danghao,
                                resname, resintroduce, resorigin, resarttype, resauthor, "音频");
                    }
                }
            }


            /*for (String[] record : list) {
                level = record[13];
                Map<String, String> paramMap = new HashMap<>();
                if(level.indexOf("发布级") >= 0){
                    whgMassLibraryService.addExcelResource(sysUser,libid,record,"音频");
                }
            }*/
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }
}
