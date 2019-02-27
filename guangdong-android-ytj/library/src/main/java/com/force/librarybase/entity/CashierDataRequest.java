package com.force.librarybase.entity;

import android.util.Log;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by joker on 2016/9/1.
 */
public class CashierDataRequest {

    private static final String TAG = "CashierDataRequest";

    private String posId;//pos机号
    private String posMenId;//pos员机号
    private String payType;//交易类型标志
    private String money;//金额
    private String payDate;//原交易日期
    private String refernumber;//原交易参考号
    private String systraceno;//原凭证号
    private String ordernumber;//订单号

    private Map<String, Integer> elementLenth = new HashMap<String, Integer>() {
        {
            put("posId", 8);
            put("posMenId", 8);
            put("payType", 2);
            put("money", 12);
            put("payDate", 8);
            put("refernumber", 12);
            put("systraceno", 12);
            put("ordernumber", 44);
        }
    };

    /**
     * 将原始数据用空格填充至所需长度（右补空格）
     *
     * @param value 原始数据
     * @param name  要填充的域
     * @return
     */
    private String generateValue(String value, String name) {
        int size = elementLenth.get(name);

        if (value == null) value = "";

        String data = value;

        if (name.equals("money")) {
            long amount;
            if (data == "")
                amount = 0;
            else {
                amount = (long) (Float.parseFloat(data) * 100);
            }

            data = String.format(Locale.US, "%012d", amount);
        } else {
            try {
                if (value.getBytes("GBK").length > size) {
                    Log.d(TAG, "字符串长度不正确");
                }
                for (int i = 0; i < (size - value.getBytes("GBK").length); i++) {
                    data = data + " ";
                }
            } catch (Exception e) {
                e.printStackTrace();
                data = "";
            }
        }
        return data;
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getPosMenId() {
        return posMenId;
    }

    public void setPosMenId(String posMenId) {
        this.posMenId = posMenId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getMoney() {
        return money;
    }

    //特殊处理
    public void setMoney(String money) {
        this.money = money;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getRefernumber() {
        return refernumber;
    }

    public void setRefernumber(String refernumber) {
        this.refernumber = refernumber;
    }

    public String getSystraceno() {
        return systraceno;
    }

    public void setSystraceno(String systraceno) {
        this.systraceno = systraceno;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    @Override
    public String toString() {
        return generateValue(posId, "posId") +
                generateValue(posMenId, "posMenId") +
                generateValue(payType, "payType") +
                generateValue(money, "money") +
                generateValue(payDate, "payDate") +
                generateValue(refernumber, "refernumber") +
                generateValue(systraceno, "systraceno") +
                generateValue(ordernumber, "ordernumber");
    }
}
