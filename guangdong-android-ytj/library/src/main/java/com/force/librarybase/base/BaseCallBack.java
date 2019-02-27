package com.force.librarybase.base;

import android.text.TextUtils;

import com.force.librarybase.BaseApplication;
import com.force.librarybase.network.response.NormalResponse;
import com.force.librarybase.utils.Null;
import com.force.librarybase.utils.ToastUtils;
import com.force.librarybase.utils.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Jacky.Cai
 * @version v1.0
 * @ Package com.dayhr.caterers.mobile.base
 * @ Description:
 * @ date 16/9/5 上午10:57
 */
public class BaseCallBack<T extends NormalResponse> implements Callback<T> {

    private static String TAG = "RESPONSE_SUCCESS | code:%s, msg:%s";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        //子类重写该方法时 在获取需鉴权的网络数据情况下，一定记得调用 super.onSuccess(response);
        if (!Null.isNull(response) && !Null.isNull(response.body()) && !response.body().isSuccess()) {
            Logger.d(TAG, response.body().statusCode, response.body().resultDesc);
            if (!TextUtils.isEmpty(response.body().resultDesc)) {
                ToastUtils.show(BaseApplication.getInstance(), response.body().resultDesc);
            }
        } else if (Null.isNull(response.body()) && response.code() != 200) {
            ToastUtils.show(BaseApplication.getInstance(), "服务器响应错误，请再试试");
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        ToastUtils.show(BaseApplication.getInstance(), t.getMessage());
    }
}
