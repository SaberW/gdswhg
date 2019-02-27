package hn.creatoo.com.web.mode;

/**
 * Created by wangxl on 2017/6/1.
 */
public class ResponseBean {
    public final static String SUCCESS = "1";
    public final static String FAIL = "0";
    public final static String SYS_ERR_CODE = "SYS-ERR-100";

    public String success = "1"; //1-成功 0-失败
    public String errCode;
    public String errMsg;
    public Object data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
