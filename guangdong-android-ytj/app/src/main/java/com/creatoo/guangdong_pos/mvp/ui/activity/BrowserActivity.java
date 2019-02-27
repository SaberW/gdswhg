/*
 * Tencent is pleased to support the open source community by making VasSonic available.
 *
 * Copyright (C) 2017 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 *
 */

package com.creatoo.guangdong_pos.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.creatoo.guangdong_pos.R;
import com.creatoo.guangdong_pos.api.WebViewConfig;
import com.creatoo.guangdong_pos.base.CAApplication;
import com.creatoo.guangdong_pos.base.CABaseActivity;
import com.creatoo.guangdong_pos.mvp.contract.BrowserContract;
import com.creatoo.guangdong_pos.mvp.presenter.BrowserPresenter;
import com.creatoo.guangdong_pos.utils.CommonKey;
import com.creatoo.guangdong_pos.utils.location.LocationHelper;
import com.force.librarybase.mvp.presenter.IBasePresenter;
import com.force.librarybase.utils.NetWorkUtil;
import com.force.librarybase.utils.NetWorkUtil2;
import com.force.librarybase.utils.StartAnimUtil;
import com.force.librarybase.utils.TimeUtils;
import com.force.librarybase.utils.ToastUtils;
import com.force.librarybase.utils.sonic.SonicSessionClientImpl;
import com.tencent.sonic.sdk.SonicCacheInterceptor;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionClient;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;
import com.tencent.sonic.sdk.SonicSessionConnectionInterceptor;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 *  A demo browser activity
 *  In this demo there are three modes,
 *  sonic mode: sonic mode means webview loads html by sonic,
 *  offline mode: offline mode means webview loads html from local offline packages,
 *  default mode: default mode means webview loads html in the normal way.
 *
 */
@EActivity(R.layout.activity_browser)
public class BrowserActivity extends CABaseActivity implements BrowserContract.View{


    public final static String PARAM_URL = "param_url";

    public final static String PARAM_MODE = "param_mode";

    private SonicSession sonicSession;
    @ViewById(R.id.wv_main)
    WebView mMainMv;

    @Extra
    int mWebMode;

    @Extra
    String  mUrl ;

    private SonicSessionClientImpl sonicSessionClient = null;

    private BrowserPresenter mPresenter;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Consumer<Long> mTimeConsumer = new Consumer<Long>() {
        @Override
        public void accept(Long aLong) throws Exception {
            if ( aLong < 5){
                if (NetWorkUtil.ping()){
                    startLoadWebView();
                    initWebChromeClient();
                    mCompositeDisposable.clear();
                }
            }else{
                ToastUtils.showToast(BrowserActivity.this,3,"网络异常，请检查网络",true);
            }
        }
    };
    private Disposable mDisposable;


    @AfterExtras
    void afterExtras(){

        mUrl = CAApplication.IS_DEVELOPE ? WebViewConfig.OFF_LINE_WEB_URL : WebViewConfig.ON_LINE_WEB_URL;
//        ToastUtils.showToast(this,3,mUrl,false);
//        mUrl = "http://192.168.0.166:8088/gdswhg/ytj/index";
        mWebMode = CommonKey.Constant.MODE_SONIC;
//        if (TextUtils.isEmpty(mUrl) || -1 == mWebMode) {
//            finish();
//            return;
//        }
        // init sonic engine if necessary, or maybe u can do this when application created
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new com.force.librarybase.utils.webview.SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }


        // if it's sonic mode , startup sonic session at first time
        if (CommonKey.Constant.MODE_DEFAULT != mWebMode) { // sonic mode
            SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
            sessionConfigBuilder.setSupportLocalServer(true);

            // if it's offline pkg mode, we need to intercept the session connection
            if (CommonKey.Constant.MODE_SONIC_WITH_OFFLINE_CACHE == mWebMode) {
                sessionConfigBuilder.setCacheInterceptor(new SonicCacheInterceptor(null) {
                    @Override
                    public String getCacheData(SonicSession session) {
                        return null; // offline pkg does not need cache
                    }
                });

                sessionConfigBuilder.setConnectionInterceptor(new SonicSessionConnectionInterceptor() {
                    @Override
                    public SonicSessionConnection getConnection(SonicSession session, Intent intent) {
                        return new OfflinePkgSessionConnection(BrowserActivity.this, session, intent);
                    }
                });
            }

            // create sonic session and run sonic flow
            sonicSession = SonicEngine.getInstance().createSession(mUrl, sessionConfigBuilder.build());
            if (null != sonicSession) {
                sonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
            } else {
                // this only happen when a same sonic session is already running,
                // u can comment following codes to feedback as a default mode.
                // throw new UnknownError("create session fail!");
//                Toast.makeText(this, "create sonic session fail!", Toast.LENGTH_LONG).show();
            }
        }
    }


    @AfterViews
    void afterViews(){
        new BrowserPresenter(this,this);
        getLifecycle().addObserver(mPresenter);
        initWebView();
        initDispose();
    }

    private void initDispose() {
        mDisposable = Observable.interval(0,3, TimeUnit.SECONDS)
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mTimeConsumer);
        mCompositeDisposable.add(mDisposable);
    }


    private void initWebChromeClient() {
        mMainMv.setWebChromeClient(new WebChromeClient() {

            private View myView = null;

            // 全屏
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);

                ViewGroup parent = (ViewGroup) mMainMv.getParent();
                parent.removeView(mMainMv);

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
                    parent.addView(mMainMv);
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
                        SignActivity_.intent(BrowserActivity.this).start();
                        StartAnimUtil.commonStartAnim(BrowserActivity.this);
                    }else if(uri.getAuthority().equals("ticket")){
                        TicketActivity_.intent(BrowserActivity.this).start();
                        StartAnimUtil.commonStartAnim(BrowserActivity.this);
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



    private void startLoadWebView() {

        // webview is ready now, just tell session client to bind
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(mMainMv);
            sonicSessionClient.clientReady();
        } else { // default mode
            mMainMv.loadUrl(mUrl);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        // init webview
        mMainMv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (sonicSession != null) {
                    sonicSession.getSessionClient().pageFinish(url);
                }
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (sonicSession != null) {
                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
                }
                return null;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                mMainMv.reload();
                super.onReceivedError(view, request, error);
            }
        });

        WebSettings webSetting = mMainMv.getSettings();
        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        webSetting.setJavaScriptEnabled(true);
//        mMainMv.removeJavascriptInterface("searchBoxJavaBridge_");
//        intent.putExtra(SonicJavaScriptInterface.PARAM_LOAD_URL_TIME, System.currentTimeMillis());
//        mMainMv.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, intent), "sonic");

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
//        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        String cacheDirPath = getFilesDir().getAbsolutePath()+"pos_cache";
//        webSetting.setDatabasePath(cacheDirPath);
//        //设置  Application Caches 缓存目录
//        webSetting.setAppCachePath(cacheDirPath);
//        //开启 Application Caches 功能
//        webSetting.setAppCacheEnabled(true);
        //WebView加载web资源
//		webSetting.setTextSize(WebSettings.TextSize.SMALLER);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && mMainMv.canGoBack()){
            mMainMv.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSessionClient.destroy();
            sonicSession = null;
        }else{
            mMainMv.clearCache(true);
            mMainMv.clearFormData();
            mMainMv.destroy();
            mMainMv = null;
        }
        super.onDestroy();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void setPresenter(IBasePresenter presenter) {
        this.mPresenter = (BrowserPresenter) presenter;
    }


    private static class OfflinePkgSessionConnection extends SonicSessionConnection {

        private final WeakReference<Context> context;

        public OfflinePkgSessionConnection(Context context, SonicSession session, Intent intent) {
            super(session, intent);
            this.context = new WeakReference<Context>(context);
        }

        @Override
        protected int internalConnect() {
            Context ctx = context.get();
            if (null != ctx) {
                try {
//                    InputStream offlineHtmlInputStream = ctx.getAssets().open("sonic-demo-index.html");
//                    responseStream = new BufferedInputStream(offlineHtmlInputStream);
                    return SonicConstants.ERROR_CODE_SUCCESS;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return SonicConstants.ERROR_CODE_UNKNOWN;
        }

        @Override
        protected BufferedInputStream internalGetResponseStream() {
            return responseStream;
        }

        @Override
        public void disconnect() {
            if (null != responseStream) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getResponseCode() {
            return 200;
        }

        @Override
        public Map<String, List<String>> getResponseHeaderFields() {
            return new HashMap<>(0);
        }

        @Override
        public String getResponseHeaderField(String key) {
            return "";
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainMv.onPause();
        mMainMv.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainMv.onResume();
        mMainMv.resumeTimers();
    }


}
