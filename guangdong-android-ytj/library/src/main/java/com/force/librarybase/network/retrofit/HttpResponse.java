package com.force.librarybase.network.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * http响应参数实体类
 * 通过Gson解析属性名称需要与服务器返回字段对应,或者使用注解@SerializedName
 * 备注:这里与服务器约定返回格式
 *
 * @author ZhongDaFeng
 */
public class HttpResponse {

    /**
     * 描述信息
     */
    @SerializedName("errormsg")
    private String errormsg;

    /**
     * 状态码
     */
    @SerializedName("success")
    private String success;

    /**
     * 状态码
     */
    @SerializedName("data")
    private Object data;


    /**
     * 是否成功(这里约定200)
     *
     * @return
     */
    public boolean isSuccess() {
        return success.equals("1") ? true : false;
    }


    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
