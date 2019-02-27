package com.creatoo.hn.controller.api.apiqwzy;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.mass.WhgMassDownService;
import com.creatoo.hn.services.admin.mass.WhgMassLibraryService;
import com.creatoo.hn.util.CommUtil;
import com.creatoo.hn.util.FilterFontUtil;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ApiResultBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * 群文资源
 * Created by wangxl on 2017/12/7.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/mass/res")
public class ApiWhgMassController extends BaseController {
    /**
     * 群文资源服务类
     */
    @Autowired
    private WhgMassLibraryService whgMassLibraryService;

    @Autowired
    private WhgMassDownService whgMassDownService;

    /**
     * 获取资源一级分类
     * @return
     */
    @PostMapping("/types")
    public List<Map<String, Object>> getResType(@RequestParam(value = "cultid", required = false) String cultid){
        List<Map<String, Object>> statelist = new ArrayList<Map<String, Object>>();
        try {
            Class<?> class1 = Class.forName("com.creatoo.hn.util.enums.EnumUploadType");
            Object[] objs = class1.getEnumConstants();
            for (Object obj : objs) {
                Method valueMethod = obj.getClass().getMethod("getValue");
                Method nameMethod = obj.getClass().getMethod("getName");
                String _val = valueMethod.invoke(obj).toString();
                String _nam = nameMethod.invoke(obj).toString();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", _nam);
                map.put("value", _val);

               /* //二级分类
                WhgMassLibrary whgMassLibrary = new WhgMassLibrary();
                whgMassLibrary.setResourcetype(_val);
                if(StringUtils.isNotEmpty(cultid)) {
                    whgMassLibrary.setCultid(cultid);
                }
                List<WhgMassLibrary> list = whgMassLibraryService.find(null, null, whgMassLibrary, null);
                if(list == null){
                    list = new ArrayList<>();
                }
                map.put("child", list);*/
                statelist.add(map);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return statelist;
    }

    /**
     * 搜索群文资源
     * @param srchkey
     * @param restype
     * @param libid
     * @return
     */
    @PostMapping("/list")
    public ApiResultBean findResource(
                                    @RequestParam(value = "page", defaultValue = "1")int page,
                                    @RequestParam(value = "pageSize", defaultValue = "12") int pageSize,
                                    @RequestParam(value="srchkey", required=false) String srchkey,
                                    @RequestParam(value="restype", required=false) String restype,
                                    @RequestParam(value="libid", required=false) String libid,
                                    @RequestParam(value="cultid", required=false) String cultid,
                                    @RequestParam(value="arttype", required=false) String resarttype,
                                    @RequestParam(value="userid") String userid){
        ApiResultBean resultBean = new ApiResultBean();
        try {

            List<String> cultids = null;
            if (cultid != null && !cultid.isEmpty()) {
                cultids = Arrays.asList( cultid.split("\\s*,\\s*") );
            }

            resultBean = this.whgMassLibraryService.findApiResourceByPaging(page, pageSize, restype, libid, srchkey, null, cultids,resarttype,userid);

        }catch (Exception e){
            resultBean.setCode(101);
            resultBean.setMsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return resultBean;
    }

    /**
     * 资源详情
     * @param libid 资源库标识
     * @param resid 资源标识
     * @return
     */
    @PostMapping("/detail")
    public ApiResultBean findDetail(@RequestParam String libid, @RequestParam String resid){
        ApiResultBean resultBean = new ApiResultBean();
        try {
            Map resource = this.whgMassLibraryService.findResourceById(libid, resid);
            WhgMassLibrary library = this.whgMassLibraryService.findById(libid);
            resource.put("restype", library.getResourcetype());
            resultBean.setData(FilterFontUtil.clearFont4Map4All(resource));

            //扩展属性行列配置
            List<WhgMassLibraryFormField> fields_showFront = new ArrayList<>();
            WhgMassLibraryForm form = new WhgMassLibraryForm();
            form.setLibid(library.getId());
            List<WhgMassLibraryForm> forms = this.whgMassLibraryService.findLibraryForm(form);
            Map<String, String> formIdx = new HashMap<>();
            if(forms != null){
                String fieldname = "";
                String fieldval = "";
                for(WhgMassLibraryForm _form : forms){
                    int columntype = _form.getColumntype();
                    fieldname = _form.getLabelname();
                    fieldval = "";
                    if(columntype == 0 || columntype ==1){//一列一个字段 和 一列组合字段，不处理多列字段
                        WhgMassLibraryFormField field = new WhgMassLibraryFormField();
                        field.setFormid(_form.getId());//列标识
                        field.setIsshowfront(1);//显示在前端
                        List<WhgMassLibraryFormField> fields = this.whgMassLibraryService.findLibraryFormField(field);
                        if(fields != null && fields.size() > 0){
                            for(WhgMassLibraryFormField _field : fields){
                                fieldval += " "+String.valueOf(resource.get(_field.getFieldcode()));

                            }
                        }
                    }
                    if(StringUtils.isNotEmpty(fieldval)){
                        WhgMassLibraryFormField _field_front = new WhgMassLibraryFormField();
                        _field_front.setFieldname(fieldname);
                        _field_front.setFielddefaultval(fieldval);
                        fields_showFront.add(_field_front);
                    }
                }
                resultBean.setRows(fields_showFront);
            }
        }catch (Exception e){
            resultBean.setCode(101);
            resultBean.setMsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return resultBean;
    }


    /**
     * 文件下载
     *
     * @param response
     * @param request
     * @param libid
     * @param resid
     * @throws Exception
     */
    @ResponseBody
    @PostMapping(value = "/downloadFile")
    public void downloadFile(HttpServletResponse response, HttpServletRequest request, String libid, String resid) throws Exception {
        if (resid != null){
            InputStream fis = null;
            BufferedInputStream bis = null;
            Map resource = this.whgMassLibraryService.findResourceById(libid, resid);
            if (resource != null){
                String fileUrl = resource.get("resurl").toString();
                String fileName = resource.get("resname").toString();
                if(StringUtils.isNotBlank(fileUrl)){
                    String suffix = fileUrl.substring(fileUrl.lastIndexOf("."), fileUrl.length());
                    fileName = fileName + suffix;
                }
                response.reset();
                response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
                response.setHeader("Connection", "close");
                response.setHeader("Content-Type", "application/octet-stream");
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setContentType("application/OCTET-STREAM");
                try
                {
                    URL url = new URL(fileUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 设置超时间为3秒
                    conn.setConnectTimeout(3 * 1000);
                    // 防止屏蔽程序抓取而返回403错误
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    // 得到输入流
                    InputStream inputStream = conn.getInputStream();
                    try {
                        ServletOutputStream out = response.getOutputStream();
                        request.setCharacterEncoding("UTF-8");
                        int BUFFER = 1024*10;
                        byte data[] = new byte[BUFFER];
                        //获取文件输入流
                        fis = conn.getInputStream();
                        int read;
                        bis = new BufferedInputStream(fis,BUFFER);
                        while((read = bis.read(data)) != -1){
                            out.write(data, 0, read);
                        }

                        // 保存下载记录
                        WhgMassDown down = new WhgMassDown();
                        down.setId(IDUtils.getID32());
                        down.setIp(CommUtil.getIpAddr(request));
                        down.setDatetime(new Date());
                        down.setResid(resid);
                        this.whgMassDownService.add(down);
                    } catch (IOException e) {
                        log.error("文件下载异常", fileName, e.fillInStackTrace());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("文件下载抛出异常！！", e.fillInStackTrace());
                } finally {
                    if(fis != null){
                        fis.close();
                    }
                    if(bis != null){
                        bis.close();
                    }
                }
            }
        }
    }
}
