package com.creatoo.hn.util.bean;

import com.creatoo.hn.util.ENVUtils;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rbg on 2017/8/7.
 */
public class ApiResultBean implements Serializable{

    /**
     * 返回码， 默认成功
     */
    private Integer code = 0;

    /**
     * 失败时的错误信息
     */
    private String msg = "";

    /**
     * 返回数据实体
     */
    private Object data = "false";

    /**
     * 返回的数据list
     */
    private List rows = new ArrayList();

    /**
     * 返回的数据总条数
     */
    private long total = 0l;

    /**
     * 当前页
     */
    private int page = 1;

    /**
     * 每页最多记录数
     */
    private int pageSize = 10;

    /**
     * 当前页记录数
     */
    private int currentPageSize = 10;

    /**
     * 静态图片地址
     */
    private String imgPath = ENVUtils.env.getProperty("upload.local.server.addr");


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPageSize() {
        return currentPageSize;
    }

    public void setCurrentPageSize(int currentPageSize) {
        this.currentPageSize = currentPageSize;
    }

    public void setPageInfo(PageInfo pageInfo){
        if(pageInfo != null){
            this.total = pageInfo.getTotal();
            this.page = pageInfo.getPageNum();
            this.pageSize = pageInfo.getPageSize();
            this.rows = pageInfo.getList();
            this.currentPageSize = pageInfo.getList().size();
        }
    }

    public static void main(String[] args){
     /*   ApiResultBean retMobileEntity = new ApiResultBean();
        List list = new ArrayList();
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        RetMobileEntity.Pager pager = new RetMobileEntity.Pager();
        pager.setCount(8);
        pager.setIndex(1);
        pager.setSize(8);
        pager.setTotal(26);
        retMobileEntity.setCode(0);
        retMobileEntity.setErrormsg("error");
        retMobileEntity.setRows(list);
        retMobileEntity.setData(list);

        System.out.println(JSON.toJSONString(retMobileEntity));*/
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

}
