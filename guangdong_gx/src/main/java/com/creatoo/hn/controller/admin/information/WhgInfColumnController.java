package com.creatoo.hn.controller.admin.information;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgInfColumn;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.services.admin.information.WhgInfColumnService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.enums.EnumOptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LENUVN on 2017/7/25.
 */
@RequestMapping("/admin/information")
@RestController
public class WhgInfColumnController extends BaseController {

    @Autowired
    private WhgInfColumnService whgInfColumnService;

    @RequestMapping("/muse")
    @WhgOPT(optType = EnumOptType.COLUMN, optDesc = {"查看列表页"})
    public ModelAndView index() {
        return new ModelAndView("admin/information/museum");
    }


    /**
     * 查询树形
     *
     * @return
     */
    @RequestMapping("/selmus")
    public Object inquire() {
        try {
            return this.whgInfColumnService.inquire();
        } catch (Exception e) {
            log.debug(e.getMessage());
            return null;
        }
    }

    /**
     * 添加
     * @param whzx
     * @param req
     * @param resp
     * @param muspic_up
     * @return
     */
    @RequestMapping(value = "/addmus",method = RequestMethod.POST)
    @WhgOPT(optType = EnumOptType.COLUMN, optDesc = {"添加"})
    public Object add(WhgInfColumn whzx, HttpServletRequest req, HttpServletResponse resp, @RequestParam("muspic_up")MultipartFile muspic_up) {
        String success = "0";
        String errmasg = "";
        try {
            //当前日期
            Date now = new Date();
            //保存图片
            //String uploadPath = UploadUtil.getUploadPath(req);
//            if(muspic_up != null && !muspic_up.isEmpty()){
//                String imgPath_muspic = UploadUtil.getUploadFilePath(muspic_up.getOriginalFilename(), IDUtils.getID(), "shop", "picture", now);
//                muspic_up.transferTo( UploadUtil.createUploadFile(uploadPath, imgPath_muspic) );
//                whzx.setColpic(UploadUtil.getUploadFileUrl(uploadPath, imgPath_muspic));
//            }
            whzx.setColid(IDUtils.getID());
            this.whgInfColumnService.save(whzx);
        } catch (Exception e) {
            success = "1";
            errmasg = e.getMessage();
        }
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", success);
        res.put("msg", errmasg);
        return res;
    }
    /**
     * 修改
     */
    @RequestMapping("/upmus")
    @WhgOPT(optType = EnumOptType.COLUMN, optDesc = {"修改"})
    public Object revise(WhgInfColumn whzx,HttpServletRequest req, HttpServletResponse resp,@RequestParam("muspic_up")MultipartFile muspic_up) {
        String success = "0";
        String errmasg = "";
        try {
            //当前日期
            Date now = new Date();
//            String uploadPath = UploadUtil.getUploadPath(req);
//            if (muspic_up !=null && !muspic_up.isEmpty()) {
//                UploadUtil.delUploadFile(uploadPath, whzx.getColpic());
//                //保存图片
//                String imgPath_muspic = UploadUtil.getUploadFilePath(muspic_up.getOriginalFilename(), commService.getKey("art.picture"), "shop", "picture", now);
//                muspic_up.transferTo( UploadUtil.createUploadFile(uploadPath, imgPath_muspic) );
//                whzx.setColpic(UploadUtil.getUploadFileUrl(uploadPath, imgPath_muspic));
//            }
            this.whgInfColumnService.update(whzx);
        } catch (Exception e) {
            success = "1";
            errmasg = e.getMessage();
        }
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("success", success);
        res.put("msg", errmasg);
        return res;
    }
    /**
     * 删除deltyp
     */
    @RequestMapping("/delmus")
    @WhgOPT(optType = EnumOptType.COLUMN, optDesc = {"删除"})
    public Object delete(String colid,HttpServletRequest req,String colpic) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        String success = "0";
        String errmsg = "";
        //删除图片
        try {
//            String uploadPath = UploadUtil.getUploadPath(req);
//            if(colpic!= null && !colpic.isEmpty()){
//                UploadUtil.delUploadFile(uploadPath, colpic);
//            }
            this.whgInfColumnService.delete(colid);
        } catch (Exception e) {
            errmsg = e.getMessage();
        }

        // 返回
        rtnMap.put("success", success);
        rtnMap.put("errmsg", errmsg);
        return rtnMap;
    }

    /**
     * 带状态查询树形
     *
     * @return
     */
    @RequestMapping("/selecol")
    public Object inquires(HttpServletRequest req) {
        WhgSysUser sysUser = (WhgSysUser) req.getSession().getAttribute(Constant.SESSION_ADMIN_KEY);
        try {
            return this.whgInfColumnService.select(sysUser);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return null;
        }
    }

}
