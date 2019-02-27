package com.force.librarybase;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;

import com.force.librarybase.netstatus.NetChangeObserver;
import com.force.librarybase.netstatus.NetworkStateReceiver;
import com.force.librarybase.utils.NetWorkUtil;
import com.force.librarybase.utils.SystemBarTintManager;
import com.force.librarybase.utils.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author Jacky.Cai
 * @version v1.1
 * @Package com.force.librarybase
 * @Description:
 * @date 16/11/1 下午3:06
 */
public class BaseActivity extends RxAppCompatActivity implements
        NetChangeObserver,EasyPermissions.PermissionCallbacks {


    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";



    BaseApplication mApplication;
    public SystemBarTintManager mTintManager;
    private Handler sHandler;

    /**
     * 若无需设置状态栏颜色，在子类中重写该方法并返回“false”即可
     *
     * @return
     */
    protected boolean getEnableStatusBarTintColor() {
        return true;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        mApplication = BaseApplication.getInstance();
        super.onCreate(arg0);
        if (getEnableStatusBarTintColor()) {
            mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            //mTintManager.setNavigationBarTintEnabled(true);
        }
        hideNavigator();
        setStatusBarTintColor();

    }

    private void hideNavigator() {
        final Runnable mHideRunnable = new Runnable() {
            @Override
            public void run() {
                int flags;
                int curApiVersion = android.os.Build.VERSION.SDK_INT;
                // This work only for android 4.4+
                if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
                    // This work only for android 4.4+
                    // hide navigation bar permanently in android activity
                    // touch the screen, the navigation bar will not show
                    flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN;

                } else {
                    // touch the screen, the navigation bar will show
                    flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                }

                // must be executed in main thread :)
                getWindow().getDecorView().setSystemUiVisibility(flags);
            }
        };

        sHandler = new Handler();
        sHandler.post(mHideRunnable); // hide the navigation bar

        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                sHandler.post(mHideRunnable); // hide the navigation bar
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needObserverNetStatusChange()) {
            registerNetChangeListener();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterNetChangeListener();
    }

    /**
     * 是否需要监听网络状态变化，默认返回false
     *
     * @return
     */
    protected boolean needObserverNetStatusChange() {
        return false;
    }

    /**
     * 监听网络状态变化
     */
    protected void registerNetChangeListener() {
        NetworkStateReceiver.registerObserver(this);
    }

    /**
     * 取消监听网络状态变化
     */
    protected void unRegisterNetChangeListener() {
        NetworkStateReceiver.removeRegisterObserver(this);
    }

    @Override
    public void onConnect(NetWorkUtil.NetType type) {
        Logger.i("net change call back onConnect(NetType type) not implements");
        Logger.i(String.format("NetType:", type.name()));
    }

    @Override
    public void onDisConnect() {
        Logger.d("net change call back onDisConnect() not implements");
    }

    protected void setStatusBarTintColor() {
    }

    /**
     * 判断软键盘是否弹出
     * @return
     */
    public boolean isSoftKeyboardShowing(){

        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom - getSoftButtonsBarHeight() != 0;
    }

    /**
     * 底部虚拟按键栏的高度
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }


//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs)
//    {
//        View view = null;
//        if (name.equals(LAYOUT_FRAMELAYOUT))
//        {
//            view = new AutoFrameLayout(context, attrs);
//        }
//
//        if (name.equals(LAYOUT_LINEARLAYOUT))
//        {
//            view = new AutoLinearLayout(context, attrs);
//        }
//
//        if (name.equals(LAYOUT_RELATIVELAYOUT))
//        {
//            view = new AutoRelativeLayout(context, attrs);
//        }
//
//        if (view != null) return view;
//
//        return super.onCreateView(name, context, attrs);
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
        }
    }
}
