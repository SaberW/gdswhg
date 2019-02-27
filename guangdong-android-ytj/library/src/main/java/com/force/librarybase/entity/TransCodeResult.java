package com.force.librarybase.entity;


import android.text.TextUtils;

public class TransCodeResult {

    private String processCode;

    private String amount;

    private String transNo;

    private String oriTransNo;

    private String batchNo;

    private String refNo;

    private String orderNo;

    private String transTime;

    private String transDate;

    private String terminalId;

    private String merchantId;

    private String merchantName;

	private String channel;

	private String qrLKLOrderNo;
	private String qrExpire;
	private String qrRetAmt;
	private String tradeOrderNo;

	public TransCodeResult(String processCode, String amount, String transNo, String oriTransNo,
                           String batchNo, String refNo, String orderNo, String transTime, String
								   transDate, String terminalId, String merchantId, String
								   merchantName, String channel, String qrLKLOrderNo, String qrExpire, String qrRetAmt, String tradeOederNo) {
		this.processCode = processCode;
		this.amount = amount;
		this.transNo = transNo;
		this.oriTransNo = oriTransNo;
		this.batchNo = batchNo;
		this.refNo = refNo;
		this.orderNo = orderNo;
		this.transTime = transTime;
		this.transDate = transDate;
		this.terminalId = terminalId;
		this.merchantId = merchantId;
		this.merchantName = merchantName;
		this.channel = channel;
		this.qrExpire = qrExpire;
		this.qrLKLOrderNo = qrLKLOrderNo;
		this.qrRetAmt = qrRetAmt;
		this.tradeOrderNo = tradeOederNo;
	}

    public TransCodeResult(String processCode, String amount, String transNo, String oriTransNo,
                           String batchNo, String refNo, String orderNo, String transTime, String
                                   transDate, String terminalId, String merchantId, String
                                   merchantName, String channel){
        this.processCode = processCode;
        this.amount = amount;
        this.transNo = transNo;
        this.oriTransNo = oriTransNo;
        this.batchNo = batchNo;
        this.refNo = refNo;
        this.orderNo = orderNo;
        this.transTime = transTime;
        this.transDate = transDate;
        this.terminalId = terminalId;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.channel = channel;
    }

	@Override
	public String toString() {
		return "TransCodeResult{" +
				"交易处理码='" + processCode + '\n' +
				", 金额='" + amount + '\n' +
				", 凭证号='" + transNo + '\n' +
				", 原交易凭证号='" + oriTransNo + '\n' +
				", 批次号='" + batchNo + '\n' +
				", 参考号='" + refNo + '\n' +
				", 订单号='" + orderNo + '\n' +
				", 交易时间='" + transTime + '\n' +
				", 交易日期='" + transDate + '\n' +
				", 终端号='" + terminalId + '\n' +
				", 商户号='" + merchantId + '\n' +
				", 商户名称='" + merchantName + '\n' +
				", 渠道='" + channel + '\n' +
				", 二维码退款金额='" + qrRetAmt + '\n' +
				", 二维码传入订单号='" + qrLKLOrderNo + '\n' +
				", 二维码有效期='" + qrExpire + '\n' +
				'}';
	}

	public String toStringNoNull() {
		StringBuilder builder = new StringBuilder();
		if (!TextUtils.isEmpty(processCode))
			builder.append("交易处理码=").append(processCode).append("\n");
		if (!TextUtils.isEmpty(amount))
			builder.append("金额=").append(amount).append("\n");
		if (!TextUtils.isEmpty(transNo))
			builder.append("凭证号=").append(transNo).append("\n");
		if (!TextUtils.isEmpty(oriTransNo))
			builder.append("原交易凭证号=").append(oriTransNo).append("\n");
		if (!TextUtils.isEmpty(batchNo))
			builder.append("批次号=").append(batchNo).append("\n");
		if (!TextUtils.isEmpty(refNo))
			builder.append("参考号=").append(refNo).append("\n");
		if (!TextUtils.isEmpty(orderNo))
			builder.append("订单号=").append(orderNo).append("\n");
		if (!TextUtils.isEmpty(transTime))
			builder.append("交易时间=").append(transTime).append("\n");
		if (!TextUtils.isEmpty(transDate))
			builder.append("交易日期=").append(transDate).append("\n");
		if (!TextUtils.isEmpty(terminalId))
			builder.append("终端号=").append(terminalId).append("\n");
		if (!TextUtils.isEmpty(merchantId))
			builder.append("商户号=").append(merchantId).append("\n");
		if (!TextUtils.isEmpty(merchantName))
			builder.append("商户名称=").append(merchantName).append("\n");
		if (!TextUtils.isEmpty(channel))
			builder.append("渠道=").append(channel).append("\n");
		if (!TextUtils.isEmpty(qrRetAmt))
			builder.append("二维码退款金额=").append(qrRetAmt).append("\n");
		if (!TextUtils.isEmpty(qrLKLOrderNo))
			builder.append("拉卡拉商户订单号=").append(qrLKLOrderNo).append("\n");
		if (!TextUtils.isEmpty(qrExpire))
			builder.append("二维码有效期=").append(qrExpire).append("\n");
		if (!TextUtils.isEmpty(tradeOrderNo))
			builder.append("平台交易单号=").append(tradeOrderNo).append("\n");
		return builder.toString();
	}
}
