package com.creatoo.guangdong_pos.mvp.presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.creatoo.guangdong_pos.R;
import com.creatoo.guangdong_pos.api.RetrofitApi;
import com.creatoo.guangdong_pos.api.response.TicketResponse;
import com.creatoo.guangdong_pos.base.CAApplication;
import com.creatoo.guangdong_pos.mvp.contract.TicketContract;
import com.creatoo.guangdong_pos.mvp.ui.activity.TicketActivity;
import com.creatoo.guangdong_pos.mvp.ui.dialog.NoticeDialog;
import com.force.librarybase.entity.BarcodeEvent;
import com.force.librarybase.mvp.presenter.BasePresenter;
import com.force.librarybase.network.exception.ApiException;
import com.force.librarybase.network.observable.HttpRxObserver;
import com.force.librarybase.network.retrofit.HttpResponse;
import com.force.librarybase.utils.CollectionUtils;
import com.force.librarybase.utils.NetWorkUtil;
import com.force.librarybase.utils.Null;
import com.force.librarybase.utils.TimeUtils;
import com.force.librarybase.utils.logger.Logger;
import com.force.librarybase.utils.scan.BarcodeScannerResolver;
import com.google.gson.Gson;
import com.tx.printlib.Const;
import com.tx.printlib.UsbPrinter;

import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

import static com.force.librarybase.network.observable.HttpRxObservable.getObservable;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.presenter
 * @Description:
 * @date 2018/4/19
 */
public class TicketPresenter extends BasePresenter<TicketContract.View,TicketActivity> implements TicketContract.Presenter{
    private final String TAG = TicketPresenter.class.getSimpleName();

    public static final int TYPE_INPUT_CODE = 1;
    public static final int TYPE_SCAN_QRCODE = 2;

    private UsbPrinter mUsbPrinter;

    private BarcodeScannerResolver mBarcodeScannerResolver;
    private UsbDevice dev;

    public TicketPresenter(TicketContract.View view, TicketActivity activity) {
        super(view, activity);
        mView.setPresenter(TicketPresenter.this);
        mUsbPrinter = new UsbPrinter(mActivity.getApplicationContext());
        dev = getCorrectDevice();
    }

    @Override
    public void printActivity(TicketResponse.DataInfo dataInfo) {
        if (dev != null && mUsbPrinter.open(dev)) {
            List<TicketResponse.DataInfo.TicketInfo> ticketInfos = dataInfo.getTicket();
            for (TicketResponse.DataInfo.TicketInfo ticketInfo : ticketInfos) {
                mUsbPrinter.init();
                mUsbPrinter.doFunction(Const.TX_FONT_SIZE, Const.TX_SIZE_1X, Const.TX_SIZE_2X);
                mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0);
                mUsbPrinter.doFunction(Const.TX_UNIT_TYPE, Const.TX_UNIT_MM, 0);
                mUsbPrinter.outputStringLn("活动名称:" + dataInfo.getAct().getName());
                mUsbPrinter.newline();

                String dateStr = TimeUtils.getTime(dataInfo.getTime().getPlaydate(),TimeUtils.DATE_FORMAT_DATE);

                mUsbPrinter.outputStringLn("活动时间:" + dateStr + " " + dataInfo.getTime().getPlaystime() + "-" + dataInfo.getTime().getPlayetime());
                mUsbPrinter.newline();

                mUsbPrinter.outputStringLn("活动地址:" + dataInfo.getAct().getAddress());
                mUsbPrinter.newline();

                mUsbPrinter.outputStringLn("票务信息:" + ticketInfo.getSeatcode());
                mUsbPrinter.newline();

                mUsbPrinter.outputStringLn("取票号码:" + dataInfo.getOrder().getOrdernumber());
                mUsbPrinter.newline();

                mUsbPrinter.resetFont();
//              mUsbPrinter.doFunction(Const.TX_ALIGN,Const.TX_ALIGN_CENTER,0);
//              mUsbPrinter.doFunction(Const.TX_QR_DOTSIZE,12,0);
                mUsbPrinter.write(new byte[]{0x1d,0x01,0x03,0x0c});
                mUsbPrinter.doFunction(Const.TX_QR_ERRLEVEL,Const.TX_QR_ERRLEVEL_M,0);
                mUsbPrinter.printQRcode(dataInfo.getOrder().getOrdernumber() +"****" +ticketInfo.getSeatid());

//              mUsbPrinter.resetFont();
                mUsbPrinter.doFunction(Const.TX_ALIGN,Const.TX_ALIGN_LEFT,0);
                mUsbPrinter.doFunction(Const.TX_FONT_SIZE, Const.TX_SIZE_1X, Const.TX_SIZE_2X);
                mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0);
                mUsbPrinter.doFunction(Const.TX_UNIT_TYPE, Const.TX_UNIT_MM, 0);
                mUsbPrinter.newline();
                mUsbPrinter.outputStringLn("取票时间:" + TimeUtils.getSystemTimeWithoutMillis());
                mUsbPrinter.newline();

                mUsbPrinter.doFunction(Const.TX_FEED, 30, 0);
                mUsbPrinter.doFunction(Const.TX_CUT, Const.TX_CUT_FULL, 0);
            }

            mUsbPrinter.close();
        }
    }

    @Override
    public void printRoom(TicketResponse.DataInfo dataInfo) {
        if (dev != null && mUsbPrinter.open(dev)) {
            mUsbPrinter.init();
            mUsbPrinter.doFunction(Const.TX_FONT_SIZE, Const.TX_SIZE_1X, Const.TX_SIZE_2X);
            mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0);
            mUsbPrinter.doFunction(Const.TX_UNIT_TYPE, Const.TX_UNIT_MM, 0);
            mUsbPrinter.outputStringLn("场馆名称:" + dataInfo.getVen().getTitle());
            mUsbPrinter.newline();

            mUsbPrinter.outputStringLn("场馆地址:" + dataInfo.getVen().getAddress() + dataInfo.getVenRoom().getLocation());
            mUsbPrinter.newline();

            mUsbPrinter.outputStringLn("活动室:" + dataInfo.getVenRoom().getTitle());
            mUsbPrinter.newline();

            String dateStr = TimeUtils.getTime(dataInfo.getOrder().getTimeday(),TimeUtils.DATE_FORMAT_DATE);
            String start = TimeUtils.getTime(dataInfo.getOrder().getTimestart(),TimeUtils.HOUR_MINUETE);
            String end = TimeUtils.getTime(dataInfo.getOrder().getTimeend(),TimeUtils.HOUR_MINUETE);
            mUsbPrinter.outputStringLn("使用时间:" + dateStr + " " + start + "-" + end);
            mUsbPrinter.newline();

            mUsbPrinter.outputStringLn("预定用户:" + dataInfo.getOrder().getOrdercontact());
            mUsbPrinter.newline();


            mUsbPrinter.outputStringLn("取票号码:" + dataInfo.getOrder().getOrderid());
            mUsbPrinter.newline();

            mUsbPrinter.resetFont();
//            mUsbPrinter.doFunction(Const.TX_ALIGN,Const.TX_ALIGN_CENTER,0);
            mUsbPrinter.write(new byte[]{0x1d,0x01,0x03,0x0c});
//            mUsbPrinter.doFunction(Const.TX_QR_DOTSIZE,12,0);
            mUsbPrinter.doFunction(Const.TX_QR_ERRLEVEL,Const.TX_QR_ERRLEVEL_M,0);
            mUsbPrinter.printQRcode(dataInfo.getOrder().getOrderid());

            mUsbPrinter.doFunction(Const.TX_ALIGN,Const.TX_ALIGN_LEFT,0);
            mUsbPrinter.doFunction(Const.TX_FONT_SIZE, Const.TX_SIZE_1X, Const.TX_SIZE_2X);
            mUsbPrinter.doFunction(Const.TX_CHINESE_MODE, Const.TX_ON, 0);
            mUsbPrinter.doFunction(Const.TX_UNIT_TYPE, Const.TX_UNIT_MM, 0);
            mUsbPrinter.newline();
            mUsbPrinter.outputStringLn("取票时间:" + TimeUtils.getSystemTimeWithoutMillis());
            mUsbPrinter.newline();

            mUsbPrinter.doFunction(Const.TX_FEED, 30, 0);
            mUsbPrinter.doFunction(Const.TX_CUT, Const.TX_CUT_FULL, 0);
            mUsbPrinter.close();
        }
    }

    @Override
    public boolean readState() {
        boolean result = true;
        if (dev != null && mUsbPrinter.open(dev)) {
            final long stat1 = mUsbPrinter.getStatus();
            if ((stat1 & 0x0020) != 0){
                result = false;
            }
//            final long stat2 = mUsbPrinter.getStatus2();
            mUsbPrinter.close();
        }else {
            result = false;
        }
        if (!result){
            mView.showResultDialog(NoticeDialog.MODE_PRINT_FAIL,"");
        }
        return result;


    }

    @Override
    public void startScanListener() {
        mBarcodeScannerResolver = new BarcodeScannerResolver();
        mBarcodeScannerResolver.setScanSuccessListener(new BarcodeScannerResolver.OnScanSuccessListener() {
            @Override
            public void onScanSuccess(String barcode) {
                CAApplication.getInstance().getEventBus().post(new BarcodeEvent(barcode));
            }
        });
    }

    @Override
    public void removeScanListener() {
        if (!Null.isNull(mBarcodeScannerResolver)) {
            mBarcodeScannerResolver.removeScanSuccessListener();
            mBarcodeScannerResolver = null;
        }
    }

    @Override
    public boolean dispatchKeyEvent(Context context, KeyEvent event) {
        if (!Null.isNull(mBarcodeScannerResolver)){
            mBarcodeScannerResolver.resolveKeyEvent(event);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void getTicketInfo(String code) {
        HttpRxObserver<HttpResponse> httpRxObserver = new HttpRxObserver<HttpResponse>(TAG + "getInfo") {


            @Override
            protected void onStart(Disposable d) {
                mView.showPrinting();
            }

            @Override
            protected void onError(ApiException e) {
                mView.showResultDialog(NoticeDialog.MODE_PRINT_FAIL,e.getMsg());
            }

            @Override
            protected void onSuccess(HttpResponse response) {
                if (!Null.isNull(response) && response.isSuccess()){
                    Logger.e(response.toString());
                    Gson gson = CAApplication.getInstance().getmGson();
                    TicketResponse.DataInfo ticketResponse = gson.fromJson(gson.toJson(response.getData()),TicketResponse.DataInfo.class);
                    boolean reslut = readState();
                    if (reslut){
                        if (!Null.isNull(ticketResponse.getAct())){
                            printActivity(ticketResponse);
                            mView.showResultDialog(NoticeDialog.MODE_PRINT_SUCCESS,"");
                            completePickTicket(ticketResponse);
                        }else if(!Null.isNull(ticketResponse.getVenRoom())) {
                            printRoom(ticketResponse);
                            mView.showResultDialog(NoticeDialog.MODE_PRINT_SUCCESS,"");
                            completePickTicket(ticketResponse);
                        }else{
                            mView.showResultDialog(NoticeDialog.MODE_CHECK_FAIL,response.getErrormsg());
                        }
                    }
                }else{
                    mView.showResultDialog(NoticeDialog.MODE_CHECK_FAIL,response.getErrormsg());
                }
            }
        };

        /**
         * 切入后台移除RxJava监听
         * ActivityEvent.PAUSE(FragmentEvent.PAUSE)
         * 手动管理移除RxJava监听,如果不设置此参数默认自动管理移除RxJava监听（onCrete创建,onDestroy移除）
         */

        if (NetWorkUtil.isConn(mActivity)){
            getObservable(RetrofitApi.getInstance().mGetTicketInfo(code),mActivity).subscribe(httpRxObserver);
        }else{
            mView.showToast(mActivity.getResources().getString(R.string.network_unavailable));
        }


    }

    @Override
    public void completePickTicket(TicketResponse.DataInfo dataInfo) {
        String ordertype = "";
        if (!Null.isNull(dataInfo.getAct())){
            ordertype = "act";
        }else if(!Null.isNull(dataInfo.getVenRoom())) {
            ordertype = "ven";
        }

        HttpRxObserver<HttpResponse> httpRxObserver = new HttpRxObserver<HttpResponse>(TAG + "completePickTicket") {


            @Override
            protected void onStart(Disposable d) {
                mView.showPrinting();
            }

            @Override
            protected void onError(ApiException e) {
                mView.showToast(e.getMsg());
            }

            @Override
            protected void onSuccess(HttpResponse response) {
                if (!Null.isNull(response) && response.isSuccess()){

                }else{

                }
            }
        };

        if (NetWorkUtil.isConn(mActivity)){
            getObservable(RetrofitApi.getInstance().mCompletePickTicket(dataInfo.getOrder().getId(),ordertype),mActivity).subscribe(httpRxObserver);
        }else{
            mView.showToast(mActivity.getResources().getString(R.string.network_unavailable));
        }
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        super.onCreate(owner);
        CAApplication.getInstance().getEventBus().register(mActivity);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        super.onDestroy(owner);
        CAApplication.getInstance().getEventBus().unregister(mActivity);
    }

    private UsbDevice getCorrectDevice() {
        final UsbManager usbMgr = (UsbManager) mActivity.getSystemService(Context.USB_SERVICE);
        final Map<String, UsbDevice> devMap = usbMgr.getDeviceList();
        for(String name : devMap.keySet()) {
            Logger.v("TicketPresenter", "check device: " + name);
            if (UsbPrinter.checkPrinter(devMap.get(name)))
                return devMap.get(name);
        }
        return null;
    }
}
