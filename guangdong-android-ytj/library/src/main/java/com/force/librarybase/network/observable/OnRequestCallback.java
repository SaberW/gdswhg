package com.force.librarybase.network.observable;

import com.force.librarybase.BaseApplication;
import com.force.librarybase.R;
import com.force.librarybase.network.response.NormalResponse;
import com.force.librarybase.utils.NetWorkUtil;
import com.force.librarybase.utils.Null;

import rx.Subscriber;

/**
 * @author hugh
 * @version v1.1
 * @ Package com.dayhr.temp.base.observable
 * @ Description:
 * @ date 2016/11/17 17:00
 */
public abstract class OnRequestCallback<T extends NormalResponse> extends Subscriber<T> {

    public abstract void onFailed(String code, String message, T response);

    public abstract void onException(Throwable e);

    public abstract void onResponse(T response);

    public abstract void onFinish();

    @Override
    public void onStart() {
        if (!NetWorkUtil.isConn(BaseApplication.getmAppContext())) {
            onFailed(-1 + "", BaseApplication.getmAppContext().getString(R.string.network_unavailable), null);
            onFinish();
            unsubscribe();
            return;
        }
    }

    @Override
    public final void onCompleted() {
        onFinish();
    }

    @Override
    public final void onError(Throwable e) {
        try {
            onException(e);
            onFinish();
        }catch (Exception ex){
            e.printStackTrace();
        }

    }

    @Override
    public final void onNext(T response) {
        if (!Null.isNull(response) && response.isSuccess()) {
            onResponse(response);
        } else {
            onFailed(response.statusCode, response.resultDesc, response);
        }
    }
}