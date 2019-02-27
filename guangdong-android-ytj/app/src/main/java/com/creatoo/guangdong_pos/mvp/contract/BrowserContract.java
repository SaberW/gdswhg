package com.creatoo.guangdong_pos.mvp.contract;

import com.force.librarybase.mvp.presenter.IBasePresenter;
import com.force.librarybase.mvp.view.BaseView;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.contract
 * @Description:
 * @date 2018/4/19
 */
public interface BrowserContract {
    interface View extends BaseView<BrowserContract.Presenter> {
        void showToast(String msg);
    }

    interface Presenter extends IBasePresenter {
    }
}
