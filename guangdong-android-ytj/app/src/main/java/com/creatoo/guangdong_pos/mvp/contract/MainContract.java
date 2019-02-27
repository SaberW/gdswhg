package com.creatoo.guangdong_pos.mvp.contract;

import com.force.librarybase.mvp.presenter.IBasePresenter;
import com.force.librarybase.mvp.view.BaseView;

public interface  MainContract {
     interface View extends BaseView<Presenter> {
        void showToast(String msg);
    }

     interface Presenter extends IBasePresenter {
    }
}
