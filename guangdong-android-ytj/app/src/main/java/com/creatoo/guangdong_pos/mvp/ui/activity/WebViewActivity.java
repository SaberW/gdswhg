package com.creatoo.guangdong_pos.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.creatoo.guangdong_pos.R;
import com.creatoo.guangdong_pos.api.WebViewConfig;
import com.creatoo.guangdong_pos.base.CAApplication;
import com.force.librarybase.BaseActivity;
import com.force.librarybase.utils.StartAnimUtil;
import com.force.librarybase.utils.ToastUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.ui.activity
 * @Description:
 * @date 2018/4/16
 */
@EActivity(R.layout.activity_browser)
public class WebViewActivity extends BaseActivity {
    @ViewById(R.id.wv_main)
    WebView mWebView;



    @AfterViews
    void afterViews() {

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });

        initWebViewSettings();

        String url = CAApplication.IS_DEVELOPE ? WebViewConfig.OFF_LINE_WEB_URL : WebViewConfig.ON_LINE_WEB_URL;

//        ToastUtils.showToast(this,3,url,false);

        mWebView.loadUrl(url);

        mWebView.setWebChromeClient(new WebChromeClient() {

            private View myView = null;

            // 全屏
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);

                ViewGroup parent = (ViewGroup) mWebView.getParent();
                parent.removeView(mWebView);

                // 设置背景色为黑色
                view.setBackgroundColor(getResources().getColor(R.color.black_33));
                parent.addView(view);
                myView = view;

                setFullScreen();

            }

            // 退出全屏
            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                if (myView != null) {

                    ViewGroup parent = (ViewGroup) myView.getParent();
                    parent.removeView(myView);
                    parent.addView(mWebView);
                    myView = null;

                    quitFullScreen();
                }
            }


            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                // 根据协议的参数，判断是否是所需要的url(原理同方式2)
                // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

                Uri uri = Uri.parse(message);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("js")) {

                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("sign")) {
                        // 执行JS所需要调用的逻辑
                        SignActivity_.intent(WebViewActivity.this).start();
                        StartAnimUtil.commonStartAnim(WebViewActivity.this);
                    }else if(uri.getAuthority().equals("ticket")){
                        TicketActivity_.intent(WebViewActivity.this).start();
                        StartAnimUtil.commonStartAnim(WebViewActivity.this);
                    }
                    result.confirm();
                    return true;
                }
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });

    }


    private void setFullScreen() {
        // 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 退出全屏
     */
    private void quitFullScreen() {
        // 声明当前屏幕状态的参数并获取
        final WindowManager.LayoutParams attrs =getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }



    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setLoadWithOverviewMode(true);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setMediaPlaybackRequiresUserGesture(false);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
//		webSetting.setTextSize(WebSettings.TextSize.SMALLER);



        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
        mWebView = null;
    }
}
