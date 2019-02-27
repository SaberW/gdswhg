package com.creatoo.guangdong_pos.mvp.ui.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.creatoo.guangdong_pos.R;
import com.creatoo.guangdong_pos.base.CABaseActivity;
import com.force.librarybase.base.BaseDialog;
import com.force.librarybase.utils.Null;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.ui.dialog
 * @Description:
 * @date 2018/4/24
 */
public class NoticeDialog extends BaseDialog {
    public final static int MODE_PRINT_SUCCESS = 1;
    public final static int MODE_PRINT_FAIL = 2;
    public final static int MODE_SIGN_SUCCESS = 3;
    public final static int MODE_SIGN_FAIL = 4;
    public final static int MODE_CHECK_FAIL = 5;

    public final static int TYPE_INPUT_CODE = 1;
    public final static int TYPE_SCAN_QRCODE = 2;

    private int mMode = 1;
    private int mTicketOptionType = 1;


    private TextView mContentTv,mRemarkTv;
    private FrameLayout mParentFl;
    private Button mOptionBtn;
    private View mLineV;
    private OnOptionListener mListener;
    private CABaseActivity mActivity;

    private Disposable mOptionDisposable;
    private String mContentStr;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_option:
                if (!Null.isNull(mListener)) {
                    mListener.option();
                }
                break;
        }
    }

    public NoticeDialog(@NonNull Context context) {
        super(context);
        this.mActivity = (CABaseActivity) context;
    }

    @Override
    public void bindWidget() {
        mContentTv = findViewById(R.id.tv_dialog_content);
        mRemarkTv = findViewById(R.id.tv_dialog_remark);
        mParentFl = findViewById(R.id.fl_parent);
        mOptionBtn = findViewById(R.id.btn_option);
        mLineV = findViewById(R.id.v_line);
    }

    @Override
    public void initViews() {
        mOptionBtn.setOnClickListener(this);
    }

    @Override
    public void initWindow() {
        Window mWindow = getWindow();
        WindowManager m = mWindow.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用

        WindowManager.LayoutParams wl = mWindow.getAttributes();
        wl.gravity = Gravity.CENTER;
        //wl.height = ViewPager.LayoutParams.WRAP_CONTENT;
        //wl.width = (int) (d.getWidth());// * 0.95);
        mWindow.setAttributes(wl);
        setCancelable(false);
    }

    @Override
    public void onCreateView() {
        setContentView(R.layout.dialog_notice);
    }


    public interface  OnOptionListener{
        void option();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        initViews(mMode);
        super.onStart();

    }

    public void setTicket(OnOptionListener listener , int mMode,int type,String content){
        mContentStr = "";
        this.mMode = mMode;
        this.mTicketOptionType = type;
        this.mListener = listener;
        this.mContentStr = content;
    }

    public void setSign(OnOptionListener listener , int mMode,String content){
        mContentStr = "";
        this.mMode = mMode;
        this.mListener = listener;
        this.mContentStr = content;
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews(int mMode) {
        switch (mMode) {
            case MODE_PRINT_SUCCESS:
                mParentFl.setForeground(getContext().getDrawable(R.mipmap.print_success_icon));
                mContentTv.setTextColor(getContext().getColor(R.color.black_33));
                mContentTv.setText(getContext().getString(R.string.ticket_print_success));
                mLineV.setVisibility(View.GONE);
                mRemarkTv.setVisibility(View.GONE);
                startTime();
                break;
            case MODE_PRINT_FAIL:
                mParentFl.setForeground(getContext().getDrawable(R.mipmap.print_fail_icon));
                mContentTv.setTextColor(getContext().getColor(R.color.red_ac1));
                mContentTv.setText(getContext().getString(R.string.ticket_print_fail_msg));
                mLineV.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(mContentStr)){
                    mContentTv.setText(mContentStr);
                }
                mRemarkTv.setVisibility(View.VISIBLE);
                mRemarkTv.setText(getContext().getString(R.string.ticket_print_fail_remark));
                startTime();
                break;
            case MODE_SIGN_SUCCESS:
                mParentFl.setForeground(getContext().getDrawable(R.mipmap.sign_success_icon));
                mContentTv.setTextColor(getContext().getColor(R.color.black_33));
                mContentTv.setText(mContentStr);
                mLineV.setVisibility(View.GONE);
                mRemarkTv.setVisibility(View.GONE);
                startTime();
                break;
            case MODE_SIGN_FAIL:
                mParentFl.setForeground(getContext().getDrawable(R.mipmap.sign_fail_icon));
                mContentTv.setTextColor(getContext().getColor(R.color.red_ac1));
                mContentTv.setText(getContext().getString(R.string.sign_fail_msg));
                mLineV.setVisibility(View.VISIBLE);
                mRemarkTv.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(mContentStr)){
                    mContentTv.setText(mContentStr);
                }
                mRemarkTv.setText(getContext().getString(R.string.sign_fail_remark));
                startTime();
                break;
            case MODE_CHECK_FAIL:
                mParentFl.setForeground(getContext().getDrawable(R.mipmap.check_fail_icon));
                mContentTv.setTextColor(getContext().getColor(R.color.red_ac1));
                switch (mTicketOptionType) {
                    case TYPE_INPUT_CODE:
                        mContentTv.setText(getContext().getString(R.string.ticket_input_check_fail));
                        break;
                    case TYPE_SCAN_QRCODE:
                        mContentTv.setText(getContext().getString(R.string.ticket_scan_check_fail));
                        break;
                }
                mLineV.setVisibility(View.VISIBLE);
                mRemarkTv.setVisibility(View.VISIBLE);
                mRemarkTv.setText(getContext().getString(R.string.ticket_check_fail_remark));
                mOptionBtn.setText(getContext().getString(R.string.ticket_back));
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!Null.isNull(mOptionDisposable) && !mOptionDisposable.isDisposed()){
            mOptionDisposable.dispose();
        }

    }


    private void startTime(){
        mOptionDisposable = Observable.interval(0,1, TimeUnit.SECONDS)
                .take(20)
                .compose(mActivity.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mOptionBtn.setText((20-aLong) + "秒后返回主界面");
                        if (aLong == 19 && !Null.isNull(mListener)){
                            mListener.option();
                        }

                    }
                });
    }
}
