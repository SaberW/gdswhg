package com.force.librarybase.network.function;

import com.force.librarybase.network.exception.ServerException;
import com.force.librarybase.network.retrofit.HttpResponse;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 服务器结果处理函数
 *
 * @author ZhongDaFeng
 */
public class ServerResultFunction implements Function{

    @Override
    public Object apply(Object o) throws Exception {
        HttpResponse response = (HttpResponse) o;
        if (!response.isSuccess()) {
            throw new ServerException(response.getSuccess(), response.getErrormsg());
        }
        return o;
    }
}
