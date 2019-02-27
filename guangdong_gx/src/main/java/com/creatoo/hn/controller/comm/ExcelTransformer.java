package com.creatoo.hn.controller.comm;


import com.creatoo.hn.dao.model.WhgActOrderExcel;
import com.creatoo.hn.dao.model.WhgTraEnrolExcel;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelTransformer {
    //公用导出方法
    public void preProcessing(HttpServletResponse response, String fileName) throws Exception {
        response.setContentType("application/x-download");
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddHHmmss");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + t.format(new Date()) + ".xls");
        response.setBufferSize(2048);
    }

    public String state(WhgTraEnrolExcel y){
        if(y.getState()==1) {
            return "已申请";
        } else if(y.getState()==2){
            return "取消报名";
        } else if(y.getState()==3){
            return "审核失败";
        }else if(y.getState()==4){
            return "等待面试";
        }else if(y.getState()==5){
            return "面试不通过";
        }else {
            return "报名成功";
        }
    }

    public String sex(WhgTraEnrolExcel t){
        if(t.getSex() == 0) {
            return "女";
        } else{
            return "男";
        }
    }

    public String _state(WhgActOrderExcel act){
        if(act.getState()==1) {
            return "未取票";
        } else if(act.getState()==2){
            return "已取票";
        } else{
            return "已取消";
        }
    }

    public String ordersmsstate(WhgActOrderExcel act){
        if(act.getOrdersmsstate()==1) {
            return "未发送";
        } else if(act.getOrdersmsstate()==2){
            return "发送成功";
        } else{
            return "发送失败";
        }
    }

}
