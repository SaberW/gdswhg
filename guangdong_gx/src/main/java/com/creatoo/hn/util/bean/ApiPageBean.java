package com.creatoo.hn.util.bean;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbg on 2017/8/7.
 */
public class ApiPageBean implements Serializable{

    private Integer index = 1;
    private Integer size = 10;
    private Integer count = 0;
    private Long total = 0l;

    /**
     * PageInfo转换成Pager对象
     * @param pageInfo
     * @return
     */
    public static  ApiPageBean parse(PageInfo pageInfo){
        ApiPageBean pager = new ApiPageBean();
        if(pageInfo != null){
            pager.setIndex(pageInfo.getPageNum());
            pager.setSize(pageInfo.getPageSize());
            pager.setCount(pageInfo.getSize());
            pager.setTotal(pageInfo.getTotal());
        }
        return pager;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

}
