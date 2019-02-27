package com.force.librarybase.network.response;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author Jacky.Cai
 * @version v1.0
 * @Package com.dayhr.caterers.mobile.api.response
 * @Description:
 * @date 16/9/5 上午10:19
 */
public class NormalResponse implements Serializable {

    public String result;
    public String resultDesc;
    public String statusCode;
    /**
     * ext : 2016-12-14 16:55:18.018
     * pageInfo : {"pageNum":1,"pageSize":12,"size":12,"orderBy":null,"startRow":0,"endRow":11,"total":12,"pages":1,"list":[{"mincharge":55,"storeid":35444,"status":"1","corpid":35444,"ismember":"1","createdate":"2016-12-14 13:47:18","updateuser":300016315,"id":2530452,"tipparam":55,"isdiscount":"0","createuser":300016315,"istip":"1","name":"测试5","updatedate":"2016-12-14 14:10:07","ispoint":"0","delflag":"0"}],"firstPage":1,"prePage":0,"nextPage":0,"lastPage":1,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1]}
     */

    public String ext;

    public NormalResponse(String result, String resultDesc, String statusCode) {
        this.result = result;
        this.resultDesc = resultDesc;
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "NormalResponse{" +
                "result='" + result + '\'' +
                ", resultDesc='" + resultDesc + '\'' +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return TextUtils.equals(result,"true");
    }
}
