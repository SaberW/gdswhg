package com.creatoo.guangdong_pos.mvp.contract;

import com.force.librarybase.mvp.presenter.IBasePresenter;
import com.force.librarybase.mvp.view.BaseView;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.contract
 * @Description:
 * @date 2018/4/18
 */
public interface SignContract {
    interface View extends BaseView<SignContract.Presenter> {
        void showToast(String msg);
        void showResultDialog(int mode,String content);
    }

    interface Presenter extends IBasePresenter {
        boolean readIDcard();
        void readState();
        void userSign();
    }
}
