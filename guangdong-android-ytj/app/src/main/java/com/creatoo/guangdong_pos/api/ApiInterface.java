package com.creatoo.guangdong_pos.api;


import com.force.librarybase.network.retrofit.HttpResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import io.reactivex.Observable;


/**
 * @author hugh
 * @version v1.0
 * @ Package com.force.librarybase.common
 * @ Description:3
 * @ date 16/7/28 上午9:53
 */
public interface ApiInterface {

    String API_HOST_OFFLINE = "http://120.77.37.113:7001/";//测试环境
    String API_HOST_ONLINE = "http://120.77.37.113:8090/";//线上地址


    @FormUrlEncoded
    @POST("api/ticket/pickUp")
    Observable<HttpResponse> mGetTicketInfo(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("api/ticket/completePickUp")
    Observable<HttpResponse> mCompletePickTicket(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("api/px/mytra/usersign")
    Observable<HttpResponse> mUserSign(@FieldMap Map<String, Object> params);



}