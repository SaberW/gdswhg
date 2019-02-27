package com.force.librarybase.network.observable;

import com.force.librarybase.network.response.NormalResponse;
import com.force.librarybase.utils.logger.Logger;

/**
 * @author hugh
 * @version v1.1
 * @ Package com.dayhr.temp.base.observable
 * @ Description:
 * @ date 2016/11/17 17:18
 */
public abstract class OnSimpleRequestCallback<T extends NormalResponse> extends OnRequestCallback<T> {
    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onFinish() {

    }

    @Override
    public void onFailed(String code, String message,T response){
        Logger.d(message);
    }

    @Override
    public void onException(Throwable e){
        Logger.e(e.getMessage());
    }
}
