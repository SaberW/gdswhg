package com.creatoo.guangdong_pos.mvp.presenter;

import com.creatoo.guangdong_pos.mvp.contract.BrowserContract;
import com.creatoo.guangdong_pos.mvp.contract.X5BrowserContract;
import com.creatoo.guangdong_pos.mvp.ui.activity.X5BrowserActivity;
import com.force.librarybase.mvp.presenter.BasePresenter;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.presenter
 * @Description:
 * @date 2018/4/19
 */
public class X5BrowserPresenter extends BasePresenter<X5BrowserActivity,X5BrowserContract.View> implements X5BrowserContract.Presenter {
    public X5BrowserPresenter(X5BrowserActivity view, X5BrowserContract.View activity) {
        super(view, activity);
        mView.setPresenter(X5BrowserPresenter.this);
    }
}
