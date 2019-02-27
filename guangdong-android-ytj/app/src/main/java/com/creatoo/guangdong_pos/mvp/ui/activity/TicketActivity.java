package com.creatoo.guangdong_pos.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creatoo.guangdong_pos.R;
import com.creatoo.guangdong_pos.base.CABaseActivity;
import com.creatoo.guangdong_pos.mvp.contract.TicketContract;
import com.creatoo.guangdong_pos.mvp.presenter.TicketPresenter;
import com.creatoo.guangdong_pos.mvp.ui.dialog.NoticeDialog;
import com.creatoo.guangdong_pos.mvp.ui.wedget.keyboard.KeyboardUtil;
import com.creatoo.guangdong_pos.mvp.ui.wedget.keyboard.NumberKeyboardView;
import com.force.librarybase.entity.BarcodeEvent;
import com.force.librarybase.mvp.presenter.IBasePresenter;
import com.force.librarybase.utils.InputMethodUtils;
import com.force.librarybase.utils.ToastUtils;
import com.force.librarybase.utils.logger.Logger;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.ui.activity
 * @Description:
 * @date 2018/4/19
 */
@EActivity(R.layout.activity_ticket)
public class TicketActivity extends CABaseActivity implements TicketContract.View,NoticeDialog.OnOptionListener{

    @ViewById(R.id.tv_code)
    TextView mCodeTv;

    @ViewById(R.id.tv_qrcode)
    TextView mQrCodeTv;

    @ViewById(R.id.et_ticket_code)
    EditText mTicketCodeEt;

    @ViewById(R.id.btn_confirm)
    Button mConfirmBtn;

    @ViewById(R.id.kbv_number_input)
    NumberKeyboardView mNumberInputKbv;

    @ViewById(R.id.tv_remark)
    TextView mRemarkTv;

    @ViewById(R.id.rl_number_input)
    RelativeLayout mNumberInputRl;

    @ViewById(R.id.fl_time)
    FrameLayout mTimeFl;

    @ViewById(R.id.tv_time)
    TextView mTimeTv;

    @ViewById(R.id.btn_start_scan)
    Button mStartScanBtn;

    @ViewById(R.id.tv_result_remark)
    TextView mQrcodeResultRemarkTv;

    @ViewById(R.id.rl_scan)
    RelativeLayout mScanQrcodeRl;

    @ViewById(R.id.rl_square_loading)
    RelativeLayout mSquareLoadingRl;

    @ViewById(R.id.iv_square_loading)
    ImageView mSquareLoadingIv;

    @ViewById(R.id.iv_circle_loading)
    ImageView mCircleLoadingIv;



    private TicketPresenter mPresenter;
    private boolean mCanScan = false;
    private KeyboardUtil mKeyboardUtil;
    private Animation mAnimation;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Disposable mAnimDisposable;
    private Disposable mTimeDisposable;
    private NoticeDialog mNoticeDialog;
    private AnimationDrawable mSquareDrawable;
    private int mMode;
    private int mIndexTab = 1;


    private Consumer<Long> mAnimConsumer = new Consumer<Long>() {
        @Override
        public void accept(Long aLong) throws Exception {
            mCircleLoadingIv.startAnimation(mAnimation);
        }
    };


    private Consumer<Long> mTimeConsumer = new Consumer<Long>() {
        @Override
        public void accept(Long aLong) throws Exception {
            mTimeTv.setText((30 - aLong) + "``");
            if (aLong > 30){
                mCompositeDisposable.clear();
                mCanScan = false;
                changeScanState(mCanScan);
            }
        }
    };



    @Click({R.id.tv_code,R.id.tv_qrcode,R.id.btn_start_scan,R.id.btn_confirm,R.id.tv_home,R.id.tv_top,R.id.tv_back})
    void onClick(View view){
        switch (view.getId()) {
            case R.id.tv_code:
                mIndexTab = 1;
                mSquareLoadingRl.setVisibility(View.GONE);
                mCanScan = false;
                changeTab(mIndexTab);
                break;
            case R.id.tv_qrcode:
                mSquareLoadingRl.setVisibility(View.GONE);
                mCanScan = false;
                mIndexTab = 2;
                changeTab(mIndexTab);
                changeScanState(mCanScan);
                break;
            case R.id.btn_start_scan:
                mCanScan = true;
                changeScanState(mCanScan);
                mPresenter.startScanListener();
                break;
            case R.id.btn_confirm:
                String code = mTicketCodeEt.getText().toString();
                if (TextUtils.isEmpty(code)){
                    showToast("请输入取票码");
                }else{
                    mPresenter.getTicketInfo(code);
                }
                break;
            case R.id.tv_top:
                break;
            case R.id.tv_home:
            case R.id.tv_back:
                finish();
                break;
        }
    }



    @AfterInject
    void afterInject(){
        InputMethodUtils.hideSoftKeyboardSystem(this);
    }


    @AfterViews
    void afterViews(){
        new TicketPresenter(this,this);
        getLifecycle().addObserver(mPresenter);
        initKeyBoardView();
        initAnim();
        initDialog();
        changeTab(1);
    }

    private void initDialog() {
        mNoticeDialog = new NoticeDialog(this);
    }

    private void initAnim() {
        mAnimation = AnimationUtils.loadAnimation(TicketActivity.this,R.anim.circle_anim);
        mSquareDrawable = (AnimationDrawable) mSquareLoadingIv.getBackground();
    }

    private void initKeyBoardView() {
        mKeyboardUtil = new KeyboardUtil(this, findViewById(R.id.rl_number_input),
                R.id.kbv_number_input,R.xml.keyboard_number);
        mKeyboardUtil.attachTo(mTicketCodeEt);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(this,3,msg,true);
    }

    @Override
    public void showState(String state) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void showResultDialog(int mode,String content) {
        this.mMode = mode;
        mNoticeDialog.setTicket(this,mode,mIndexTab,content);
        mNoticeDialog.show();
    }

    @Override
    public void showPrinting() {
        mCompositeDisposable.clear();
        mNumberInputRl.setVisibility(View.GONE);
        mScanQrcodeRl.setVisibility(View.GONE);
        mSquareLoadingRl.setVisibility(View.VISIBLE);

        mSquareDrawable.start();
    }


    @Override
    public void setPresenter(IBasePresenter presenter) {
        this.mPresenter = (TicketPresenter) presenter;
    }

    /**
     * Activity截获按键事件.发给 BarcodeScannerResolver
     * dispatchKeyEvent() 和 onKeyDown() 方法均可
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean result = false;
        if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
//            InputMethodUtils.hideSoftwareKeyboard(this,mTicketCodeEt);
            result =  mPresenter.dispatchKeyEvent(this,event);
        }
        if(result){
            return true;
        }else {
            return super.dispatchKeyEvent(event);
        }
    }


    @Subscribe
    public void onEvent(BarcodeEvent event){

//        Logger.d( "TicketActivity-onEvent() canScan = " + mCanScan);
        if(mCanScan){
            String barcode = event.getBarCode();
            if(!TextUtils.isEmpty(barcode)){
                mCanScan = false;
                mPresenter.getTicketInfo(barcode);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mNoticeDialog != null && mNoticeDialog.isShowing()) {
            mNoticeDialog.dismiss();
            mNoticeDialog = null;
        }
        mPresenter.removeScanListener();
        super.onDestroy();
    }


    private void changeTab(int tabIndex){
        switch (tabIndex){
            case 1:
                mCodeTv.setSelected(true);
                mQrCodeTv.setSelected(false);
                break;
            case 2:
                mCodeTv.setSelected(false);
                mQrCodeTv.setSelected(true);
                break;
        }
        initTabView(tabIndex);
    }

    private void initTabView(int tabIndex) {
        switch (tabIndex){
            case 1:
                mNumberInputRl.setVisibility(View.VISIBLE);
                mScanQrcodeRl.setVisibility(View.GONE);
                mSquareLoadingRl.setVisibility(View.GONE);
                mTicketCodeEt.setText("");
                break;
            case 2:
                mCodeTv.setSelected(false);
                mQrCodeTv.setSelected(true);
                mNumberInputRl.setVisibility(View.GONE);
                mScanQrcodeRl.setVisibility(View.VISIBLE);
                mSquareLoadingRl.setVisibility(View.GONE);
                mTimeFl.setVisibility(View.GONE);
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void changeScanState(boolean mCanScan){
        if (mCanScan){
            mStartScanBtn.setVisibility(View.GONE);
            mQrcodeResultRemarkTv.setVisibility(View.GONE);
            mTimeFl.setVisibility(View.VISIBLE);
            mAnimDisposable = Observable.interval(0,3, TimeUnit.SECONDS)
                    .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mAnimConsumer);
            mTimeDisposable = Observable.interval(0,1,TimeUnit.SECONDS)
                    .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mTimeConsumer);
            mCompositeDisposable.add(mAnimDisposable);
            mCompositeDisposable.add(mTimeDisposable);

        }else{
            mStartScanBtn.setVisibility(View.VISIBLE);
            mQrcodeResultRemarkTv.setVisibility(View.VISIBLE);
            mTimeFl.setVisibility(View.GONE);
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void option() {
        switch (mMode) {
            case NoticeDialog.MODE_PRINT_SUCCESS:
                finish();
                break;
            case NoticeDialog.MODE_PRINT_FAIL:
                finish();
                break;
            case NoticeDialog.MODE_CHECK_FAIL:
                finish();
                break;
        }
    }


}
