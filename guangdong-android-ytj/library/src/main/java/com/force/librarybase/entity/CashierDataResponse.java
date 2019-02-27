package com.force.librarybase.entity;

import android.util.Log;

/**
 * Created by joker on 2016/9/2.
 */
public class CashierDataResponse {
    private static final String TAG = "CashierDataResponse";

    private String code = "";//返回码
    private String bankCode = "";//银行行号
    private String cardCode = "";//卡号
    private String systraceno = "";//凭证号
    private String money = "";//金额
    private String errorMsg = "";//错误说明
    private String merchantNo = "";//商户号
    private String terminalNo = "";//终端号
    private String batchno = "";//批次号
    private String payDate = "";//交易日期
    private String payTime = "";//交易时间
    private String refernumber = "";//交易参考号
    private String authNo = "";//授权号
    private String cleanDate = "";//清算日期
    private String ordernumber = "";//订单号

    public CashierDataResponse(String data) {
        analysisRequestData(data);
    }

    /**
     * 分析数据，将各域赋值给相应变量
     *
     * @param data 返回的交易数据
     */
    private void analysisRequestData(String data) {
        //数据难道还是变长的？
        if (data == null) {
            Log.e(TAG, "数据包错误");
            return;
        }
        byte[] response = data.getBytes();
        if (response.length < 145) {
            Log.e(TAG, "数据包错误");
            return;
        }
        code = new String(response, 0, 2).trim();
        bankCode = new String(response, 2, 4).trim();
        cardCode = new String(response, 6, 20).trim();
        systraceno = new String(response, 26, 6).trim();
        money = new String(response, 32, 12).trim();
        errorMsg = new String(response, 44, 40).trim();
        merchantNo = new String(response, 84, 15).trim();
        terminalNo = new String(response, 99, 8).trim();
        batchno = new String(response, 107, 6).trim();
        payDate = new String(response, 113, 4).trim();
        payTime = new String(response, 117, 6).trim();
        refernumber = new String(response, 123, 12).trim();
        authNo = new String(response, 135, 6).trim();
        cleanDate = new String(response, 141, 4).trim();
        ordernumber = new String(response, 145, 44).trim();
    }

    public String getCode() {
        return code;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getCardCode() {
        return cardCode;
    }

    public String getSystraceno() {
        return systraceno;
    }

    public String getMoney() {
        return money;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public String getBatchno() {
        return batchno;
    }

    public String getPayDate() {
        return payDate;
    }

    public String getPayTime() {
        return payTime;
    }

    public String getRefernumber() {
        return refernumber;
    }

    public String getAuthNo() {
        return authNo;
    }

    public String getCleanDate() {
        return cleanDate;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    @Override
    public String toString() {
        return "code:" + code +
                " bankCode:" + bankCode +
                " cardCode:" + cardCode +
                " systraceno:" + systraceno +
                " money:" + money +
                " errorMsg:" + errorMsg +
                " merchantNo:" + merchantNo +
                " terminalNo:" + terminalNo +
                " batchno:" + batchno +
                " payDate:" + payDate +
                " payTime:" + payTime +
                " refernumber:" + refernumber +
                " authNo:" + authNo +
                " cleanDate:" + cleanDate +
                " ordernumber:" + ordernumber;
    }

    public String[] toStrings() {
        return new String[]{
                " 返回码:" + code,
                " 银行行号:" + bankCode,
                " 卡号:" + cardCode,
                " 凭证号:" + systraceno,
                " 金额:" + money,
                " 错误说明:" + errorMsg,
                " 商户号:" + merchantNo,
                " 终端号:" + terminalNo,
                " 批次号:" + batchno,
                " 交易日期:" + payDate,
                " 交易时间:" + payTime,
                " 交易参考号:" + refernumber,
                " 授权号:" + authNo,
                " 清算日期:" + cleanDate,
                " 订单号:" + ordernumber
        };
    }
}
