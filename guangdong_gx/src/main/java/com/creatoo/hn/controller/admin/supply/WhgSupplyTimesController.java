package com.creatoo.hn.controller.admin.supply;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgSupplyTime;
import com.creatoo.hn.services.admin.supply.WhgSupplyTimesService;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;

/**
 * 供需供给时段配置管理
 */
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/admin/supply/times")
public class WhgSupplyTimesController extends BaseController {

    @Autowired
    private WhgSupplyTimesService whgSupplyTimesService;

    /**
     * 供给时段列表界面
     * @param supplyId
     * @return
     */
    @RequestMapping("/view/list")
    public ModelAndView viewList(String supplyid,
                                 @RequestParam(value = "supplytype", required = false) String supplytype){
        ModelAndView mav = new ModelAndView();
        mav.addObject("supplyid", supplyid);
        mav.addObject("supplytype", supplytype);
        mav.setViewName("admin/supply/times/view_list");
        return mav;
    }

    @RequestMapping("/srchList4p")
    public Object srchList4p(int page, int rows, WhgSupplyTime record){
        ResponseBean rb = new ResponseBean();
        try {
            PageInfo pageInfo = this.whgSupplyTimesService.srchList4p(page, rows, record);
            rb.setRows( pageInfo.getList() );
            rb.setTotal( pageInfo.getTotal() );
        } catch (Exception e) {
            log.debug("查询数据失败", e);
            rb.setRows(new ArrayList());
            rb.setSuccess(ResponseBean.FAIL);
        }

        return rb;
    }

    @RequestMapping("/add")
    public Object add(WhgSupplyTime info,
                      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date _timestart,
                      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date _timeend){
        ResponseBean rb = new ResponseBean();
        if (info == null || info.getSupplyid() ==null || info.getSupplytype() == null){
            rb.setErrormsg("配送标识或类型参数丢失");
            rb.setSuccess(rb.FAIL);
            return rb;
        }
        if (info.getPsprovince()==null || info.getPscity()==null){
            rb.setErrormsg("配送范围参数不能为空");
            rb.setSuccess(rb.FAIL);
            return rb;
        }
        if (info.getHasfees() == null){
            rb.setErrormsg("是否收费参数不能为空");
            rb.setSuccess(rb.FAIL);
            return rb;
        }
        if (_timestart == null || _timeend == null){
            rb.setErrormsg("配送时间参数不能为空");
            rb.setSuccess(rb.FAIL);
            return rb;
        }

        try {
            info.setTimestart(_timestart);
            info.setTimeend(_timeend);
            this.whgSupplyTimesService.saveSupplyTime(info);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/edit")
    public Object edit(WhgSupplyTime info,
                      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date _timestart,
                      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date _timeend){
        ResponseBean rb = new ResponseBean();
        if (info == null || info.getId() ==null){
            rb.setErrormsg("标识参数丢失");
            rb.setSuccess(rb.FAIL);
            return rb;
        }

        try {
            if (_timestart != null){
                info.setTimestart(_timestart);
            }
            if (_timeend != null){
                info.setTimeend(_timeend);
            }
            this.whgSupplyTimesService.editSupplyTime(info);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息保存失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    @RequestMapping("/del")
    public Object del(String id){
        ResponseBean rb = new ResponseBean();
        try {
            this.whgSupplyTimesService.removeSupplyTime(id);
        }catch (Exception e){
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("信息删除失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

}
