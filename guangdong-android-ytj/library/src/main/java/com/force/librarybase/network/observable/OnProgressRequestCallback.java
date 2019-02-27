package com.force.librarybase.network.observable;


import com.force.librarybase.mvp.view.BaseView;
import com.force.librarybase.network.response.NormalResponse;

/**
 * @author hugh
 * @version v1.1
 * @ Package com.dayhr.temp.base.observable
 * @ Description:
 * @ date 2016/11/17 17:20
 */
public abstract class OnProgressRequestCallback<T extends NormalResponse>
        extends OnSimpleRequestCallback<T>{

    private BaseView baseView;

    public OnProgressRequestCallback(BaseView baseView){
        this.baseView = baseView;
    }

    /**
     * 注意这里代码顺序一定不能错
     */
    @Override
    public void onStart() {
//        baseView.showProgress();
        super.onStart();
    }

    /**
     * onFinish()一定会被调用
     */
    @Override
    public void onFinish() {
//        baseView.hideProgress();
    }
}