package com.creatoo.guangdong_pos.mvp.contract;

import android.content.Context;
import android.view.KeyEvent;

import com.creatoo.guangdong_pos.api.response.TicketResponse;
import com.force.librarybase.mvp.presenter.IBasePresenter;
import com.force.librarybase.mvp.view.BaseView;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.contract
 * @Description:
 * @date 2018/4/19
 */
public interface TicketContract {
    interface View extends BaseView<TicketContract.Presenter> {
        void showToast(String msg);
        void showState(String state);
        void showResultDialog(int mode,String content);
        void showPrinting();
    }

    interface Presenter extends IBasePresenter {
        void printActivity(TicketResponse.DataInfo dataInfo);
        void printRoom(TicketResponse.DataInfo dataInfo);
        boolean readState();

        /**
         * 开启扫码监听
         */
        void startScanListener();

        /**
         * 移除扫码监听
         */
        void removeScanListener();

        /**
         * 捕获键盘事件
         * @param context
         * @param event
         */
        boolean dispatchKeyEvent(Context context, KeyEvent event);

        //获取票据信息
        void getTicketInfo (String code);

        void completePickTicket(TicketResponse.DataInfo dataInfo);
    }
}
