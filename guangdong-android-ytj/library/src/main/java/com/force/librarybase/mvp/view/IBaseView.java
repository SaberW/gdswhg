package com.force.librarybase.mvp.view;

import com.force.librarybase.mvp.presenter.IBasePresenter;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.force.librarybase.mvp
 * @Description:
 * @date 2018/4/12
 */
public interface IBaseView <T extends IBasePresenter> {


    void setPresenter(T presenter);
}
