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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.widget.Toast;

import com.creatoo.guangdong_pos.R;
import com.creatoo.guangdong_pos.api.WebViewConfig;
import com.creatoo.guangdong_pos.base.CAApplication;
import com.creatoo.guangdong_pos.base.CABaseActivity;
import com.creatoo.guangdong_pos.mvp.contract.BrowserContract;
import com.creatoo.guangdong_pos.mvp.contract.X5BrowserContract;
import com.creatoo.guangdong_pos.mvp.presenter.BrowserPresenter;
import com.creatoo.guangdong_pos.mvp.presenter.X5BrowserPresenter;
import com.creatoo.guangdong_pos.mvp.ui.wedget.webview.X5WebView;
import com.creatoo.guangdong_pos.utils.CommonKey;
import com.creatoo.guangdong_pos.utils.sonic.X5WebViewSonicSessionClientImpl;
import com.force.librarybase.mvp.presenter.IBasePresenter;
import com.force.librarybase.utils.StartAnimUtil;
import com.force.librarybase.utils.ToastUtils;
import com.force.librarybase.utils.webview.SonicRuntimeImpl;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.sonic.sdk.SonicCacheInterceptor;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;
import com.tencent.sonic.sdk.SonicSessionConnectionInterceptor;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  A demo browser activity
 *  In this demo there are three modes,
 *  sonic mode: sonic mode means webview loads html by sonic,
 *  offline mode: offline mode means webview loads html from local offline packages,
 *  default mode: default mode means webview loads html in the normal way.
 *
 */

@EActivity(R.layout.activity_webview)
public class X5BrowserActivity extends CABaseActivity implements X5BrowserContract.View{

    @ViewById(R.id.wv_main)
    X5WebView mMainMv;

    @Extra
    int mWebMode;

    @Extra
    String  mUrl ;

    private X5BrowserPresenter mPresenter;


    public final static String PARAM_URL = "param_url";

    public final static String PARAM_MODE = "param_mode";

    private SonicSession sonicSession;

    private X5WebViewSonicSessionClientImpl sonicSessionClient = null;

    @AfterExtras
    void afterExtras(){
        mUrl = CAApplication.IS_DEVELOPE ? WebViewConfig.OFF_LINE_WEB_URL : WebViewConfig.ON_LINE_WEB_URL;
        mWebMode = CommonKey.Constant.MODE_SONIC_WITH_OFFLINE_CACHE;
//        if (TextUtils.isEmpty(mUrl) || -1 == mWebMode) {
//            finish();
//            return;
//        }
        // init sonic engine if necessary, or maybe u can do this when application created
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
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
                        return new OfflinePkgSessionConnection(X5BrowserActivity.this, session, intent);
                    }
                });
            }

            // create sonic session and run sonic flow
            sonicSession = SonicEngine.getInstance().createSession(mUrl, sessionConfigBuilder.build());
            if (null != sonicSession) {
                sonicSession.bindClient(sonicSessionClient = new X5WebViewSonicSessionClientImpl());
            } else {
                // this only happen when a same sonic session is already running,
                // u can comment following codes to feedback as a default mode.
                // throw new UnknownError("create session fail!");
                Toast.makeText(this, "create sonic session fail!", Toast.LENGTH_LONG).show();
            }
        }
    }


    @AfterViews
    void afterViews(){
        new X5BrowserPresenter(this,this);
        getLifecycle().addObserver(mPresenter);
        initWebView();
        startLoadWebView();
        initWebChromeClient();
    }

    private void initWebChromeClient() {
        mMainMv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView webView, String url, String message, String defaultValue, JsPromptResult jsPromptResult) {
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
                        SignActivity_.intent(X5BrowserActivity.this).start();
                        StartAnimUtil.commonStartAnim(X5BrowserActivity.this);
                    }else if(uri.getAuthority().equals("ticket")){
                        TicketActivity_.intent(X5BrowserActivity.this).start();
                        StartAnimUtil.commonStartAnim(X5BrowserActivity.this);
                    }
                    jsPromptResult.confirm();
                    return true;
                }
                return super.onJsPrompt(webView, url, message, defaultValue, jsPromptResult);
            }
        });
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

    private void initWebView() {
        // init webview
        mMainMv.setWebViewClient( new WebViewClient(){
            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                if (sonicSession != null) {
                    sonicSession.getSessionClient().pageFinish(url);
                }
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView,WebResourceRequest webResourceRequest) {
                return shouldInterceptRequest(webView, webResourceRequest.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
                if (sonicSession != null) {
                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
                }
                return null;
            }
        });

        WebSettings webSettings = mMainMv.getSettings();

        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        webSettings.setJavaScriptEnabled(true);
//        mMainMv.removeJavascriptInterface("searchBoxJavaBridge_");
//        intent.putExtra(SonicJavaScriptInterface.PARAM_LOAD_URL_TIME, System.currentTimeMillis());
//        mMainMv.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, intent), "sonic");

        // init webview settings
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSessionClient.destroy();
            sonicSession = null;
        }
        super.onDestroy();
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(this,3,msg,true);
    }

    @Override
    public void setPresenter(IBasePresenter presenter) {
        this.mPresenter = (X5BrowserPresenter) presenter;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&mMainMv.canGoBack()){
            mMainMv.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
