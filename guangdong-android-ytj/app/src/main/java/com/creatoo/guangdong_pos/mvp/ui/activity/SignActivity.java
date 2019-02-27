package com.creatoo.guangdong_pos.mvp.ui.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creatoo.guangdong_pos.R;
import com.creatoo.guangdong_pos.base.CABaseActivity;
import com.creatoo.guangdong_pos.mvp.contract.SignContract;
import com.creatoo.guangdong_pos.mvp.presenter.SignPresenter;
import com.creatoo.guangdong_pos.mvp.ui.dialog.NoticeDialog;
import com.force.librarybase.mvp.presenter.IBasePresenter;
import com.force.librarybase.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AppSettingsDialog;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.ui.activity
 * @Description:
 * @date 2018/4/18
 */
@EActivity(R.layout.activity_sign)
public class SignActivity extends CABaseActivity implements SignContract.View ,NoticeDialog.OnOptionListener{



    @ViewById(R.id.tv_remark)
    TextView mRemarkTv;

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

    @ViewById(R.id.iv_circle_loading)
    ImageView mCircleLoadingIv;


    private SignPresenter mPresenter;
    private Animation mAnimation;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Disposable mAnimDisposable;
    private Disposable mTimeDisposable;
    private Disposable mScanDisposable;
    private NoticeDialog mNoticeDialog;
    private boolean mCanScan = false;
    private int mMode = -1;
    private String errorInfo = "";




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

    private Consumer<Long> mScanConsumer = new Consumer<Long>() {
        @Override
        public void accept(Long aLong) throws Exception {
            if (mPresenter.readIDcard()){
                mCompositeDisposable.clear();
                mCanScan = false;
                mPresenter.userSign();
            }
        }
    };



    @Click({R.id.tv_home,R.id.tv_top,R.id.tv_back,R.id.btn_start_scan})
    void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_start_scan:
                mCanScan = true;
                changeScanState(mCanScan);
                break;
            case R.id.tv_top:
                break;
            case R.id.tv_home:
            case R.id.tv_back:
                finish();
                break;
        }
    }




    @AfterViews
    void afterView(){
        new SignPresenter(this,this);
        getLifecycle().addObserver(mPresenter);
        changeScanState(false);
        initAnim();
        initDialog();
    }


    private void initDialog() {
        mNoticeDialog = new NoticeDialog(this);
    }

    private void initAnim() {
        mAnimation = AnimationUtils.loadAnimation(SignActivity.this,R.anim.circle_anim);
    }


    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(this,3,msg,true);
    }

    @Override
    public void showResultDialog(int mode, String content) {
        this.mMode = mode;
        mNoticeDialog.setSign(this,mMode,content);
        mNoticeDialog.show();
    }


    @Override
    public void setPresenter(IBasePresenter presenter) {
        this.mPresenter = (SignPresenter) presenter;
    }

    private void changeScanState(boolean mCanScan) {
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
            mScanDisposable = Observable.interval(0,3,TimeUnit.SECONDS)
                    .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mScanConsumer);
            mCompositeDisposable.add(mAnimDisposable);
            mCompositeDisposable.add(mTimeDisposable);
            mCompositeDisposable.add(mScanDisposable);

        }else{
            mStartScanBtn.setVisibility(View.VISIBLE);
            mQrcodeResultRemarkTv.setVisibility(View.VISIBLE);
            mTimeFl.setVisibility(View.GONE);
            mCompositeDisposable.clear();
        }

    }


    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == SignPresenter.RC_LOCATION_CONTACTS_PERM) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("动态权限获取")
                    .setRationale("当前应用缺少必要权限。请点击设置-权限-打开所需权限。最后点击两次后退按钮，即可返回。")
                    .setNegativeButton("取消")
                    .setPositiveButton("设置").build().show();
        }
    }

    @Override
    public void option() {
        switch (mMode) {
            case NoticeDialog.MODE_SIGN_SUCCESS:
                finish();
                break;
            case NoticeDialog.MODE_SIGN_FAIL:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mNoticeDialog != null && mNoticeDialog.isShowing()) {
            mNoticeDialog.dismiss();
            mNoticeDialog = null;
        }
        super.onDestroy();

    }
}
