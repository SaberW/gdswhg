package com.creatoo.guangdong_pos.api;

import com.creatoo.guangdong_pos.utils.CommonKey;
import com.force.librarybase.network.retrofit.HttpResponse;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * @author Jacky.Cai
 * @version v1.0
 * @Package com.dayhr.cscec.pmc.api
 * @Description:
 * @date 16/7/29 上午10:47
 */
public class RetrofitApi extends RetrofitSingleton {

    private volatile static RetrofitApi instance;

    /**
     * Returns singleton class instance
     */
    public static RetrofitApi getInstance() {
        if (instance == null) {
            synchronized (RetrofitApi.class) {
                if (instance == null) {
                    instance = new RetrofitApi();
                }
            }
        }
        return instance;
    }

    /**
     * 获取票据信息
     *
     * @param code 票据号
     * @return
     */
    public Observable<HttpResponse> mGetTicketInfo(String code){
        HashMap<String,Object> params = new HashMap<>();
        params.put(CommonKey.ApiParams.TICKET_NO ,code);
        return apiService.mGetTicketInfo(params);
    }


    /**
     * 取完票回调接口
     *
     * @param orderid 订单号
     * @param ordertype 订单类型：活动（act）、场馆（ven）
     * @return
     */
    public Observable<HttpResponse> mCompletePickTicket(String orderid,String ordertype){
        HashMap<String,Object> params = new HashMap<>();
        params.put(CommonKey.ApiParams.ORDER_ID ,orderid);
        params.put(CommonKey.ApiParams.ORDER_TYPE ,ordertype);
        return apiService.mCompletePickTicket(params);
    }


    /**
     * 取完票回调接口
     *
     * @param usernumber 身份证号
     * @param latitude 纬度
     * @param longitude 精度
     * @return
     */
    public Observable<HttpResponse> mUserSign(String usernumber,double latitude,double longitude){
        HashMap<String,Object> params = new HashMap<>();
        params.put(CommonKey.ApiParams.USER_NUMBER ,usernumber);
        params.put(CommonKey.ApiParams.LATITUDE ,latitude);
        params.put(CommonKey.ApiParams.LONGITUDE ,longitude);
        return apiService.mUserSign(params);
    }

}
