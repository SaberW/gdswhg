package com.creatoo.hn.controller.comm;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.model.WhgSysCult;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.comm.CommUploadService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.bean.UploadFileBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.UUID;

/**
 * 处理图片上传的控制器
 * Created by wangxl on 2017/3/24.
 */
@RestController
@RequestMapping("/comm")
public class CommUploadController {
    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass());

    /**
     * 图片文件上传服务
     */
    @Autowired
    private CommUploadService commUploadService;

    /**
     * 文化馆子站
     */
    @Autowired
    private WhgSystemCultService whgSystemCultService;

    @Autowired
    public Environment env;

    /**
     * 上传文件
     * @param request 请求对象
     * @param response 响应对象
     * @return
     */
    @RequestMapping("/upload")
    public ResponseBean uploadImg(HttpServletRequest request, HttpServletResponse response){
        ResponseBean res = new ResponseBean();
        try{
            //文件类型
            String uploadFileType = request.getParameter("uploadFileType");//图片/视频/音频/文件
            String needCut = request.getParameter("needCut");//是否裁剪图片，如果是上传的图片时有用
            String cutWidth = request.getParameter("cutWidth");//宽
            String cutHeight = request.getParameter("cutHeight");//高
            WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            //获取上传文件对象
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            MultipartFile mulFile = multipartHttpServletRequest.getFile("whgUploadFile");

            //生成新的文件名
            String oldFileName = mulFile.getOriginalFilename();
            String suffix = "";
            int idx = oldFileName.lastIndexOf(".");
            if(idx > -1){
                suffix = oldFileName.substring(idx);
            }
            String newFileName = UUID.randomUUID().toString()+suffix;
            String issingle = request.getParameter("isSingleSy");//是否调用自己站点水印
            String rootPath = env.getProperty("upload.local.addr");
            //上传处理
            String path = request.getSession().getServletContext().getRealPath("/");
            String waterFilePath="";
            if(issingle!=null&&issingle.equals("true")){
                WhgSysCult cult=null;
                if(sysUser!=null){
                    cult=whgSystemCultService.t_srchOne(sysUser.getCultid());
                }
            if(cult!=null&&cult.getSypicture()!=null&&!cult.getSypicture().equals("")){
                waterFilePath=rootPath+cult.getSypicture();
            }else{
                waterFilePath= path + File.separator + "static"+ File.separator  +"admin"+ File.separator  +"img"+ File.separator + "sgsy.png";//水印图片路径
            }
             }
            UploadFileBean uploadFileBean = commUploadService.uploadFile(path,mulFile, uploadFileType, "true".equals(needCut), cutWidth, cutHeight, waterFilePath);
            res.setData(uploadFileBean);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     * 图片裁剪
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/uploadCut")
    public ResponseBean uploadImgCut(HttpServletRequest request, HttpServletResponse response){
        ResponseBean res = new ResponseBean();
        try{
            WhgSysUser sysUser = (WhgSysUser) request.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
            //文件类型
            String imgurl = request.getParameter("imgurl");//图片地址
            String x = request.getParameter("x");
            String y = request.getParameter("y");
            String x2 = request.getParameter("x2");
            String y2 = request.getParameter("y2");
            String w = request.getParameter("w");
            String h = request.getParameter("h");
            String issingle = request.getParameter("isSingleSy");//是否调用自己站点水印
            String rootPath = env.getProperty("upload.local.addr");
            String path = request.getSession().getServletContext().getRealPath("/");
            String waterFilePath ="";
               if(issingle!=null&&issingle.equals("true")){
                   WhgSysCult cult=null;
                   if(sysUser!=null){
                       cult=whgSystemCultService.t_srchOne(sysUser.getCultid());
                   }
                   if(cult!=null&&cult.getSypicture()!=null&&!cult.getSypicture().equals("")){
                       waterFilePath=rootPath+cult.getSypicture();
                   }else{
                       waterFilePath= path + File.separator + "static"+ File.separator  +"admin"+ File.separator  +"img"+ File.separator + "sgsy.png";//水印图片路径
                   }
               }
            UploadFileBean uploadFileBean = this.commUploadService.uploadImgCut(path,imgurl, x, y, x2, y2, w, h, waterFilePath);
            res.setData(uploadFileBean);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     * 删除已上传的文件
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/delUpload")
    public ResponseBean delUpload(HttpServletRequest request, HttpServletResponse response){
        ResponseBean res = new ResponseBean();
        try{
            //已上传的文件URL
            String uploadURL = request.getParameter("uploadURL");
            commUploadService.delUploadFile(uploadURL);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     * 跳转到图片裁剪页面
     * @param request
     * @param response
     * @return 图片裁剪页面
     * @throws Exception
     */
    @RequestMapping("/cutImg")
    public ModelAndView cutimg(HttpServletRequest request, HttpServletResponse response)throws Exception{
        ModelAndView view = new ModelAndView("/comm/admin/cutimg");
        return view;
    }

}
