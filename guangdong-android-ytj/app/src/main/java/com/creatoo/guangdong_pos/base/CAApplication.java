package com.creatoo.guangdong_pos.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.creatoo.guangdong_pos.BuildConfig;
import com.creatoo.guangdong_pos.R;
import com.creatoo.guangdong_pos.api.RetrofitSingleton;
import com.creatoo.guangdong_pos.mvp.ui.activity.CrashActivity;
import com.creatoo.guangdong_pos.utils.crash.CrashHelper;
import com.facebook.stetho.Stetho;
import com.force.librarybase.BaseApplication;
import com.force.librarybase.entity.AppInfo;
import com.force.librarybase.entity.DeviceInfo;
import com.force.librarybase.entity.ScreenInfo;
import com.force.librarybase.utils.MD5;
import com.force.librarybase.utils.MacUtil;
import com.force.librarybase.utils.StringUtils;
import com.google.gson.Gson;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.sdk.QbSdk;

import org.androidannotations.annotations.EApplication;

import java.io.File;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import jonathanfinerty.once.Once;

/**
 * @author Jacky.Cai
 * @version v1.0
 * @ Package com.dayhr.cscec.pmc.base
 * @ Description:
 * @ date 16/7/28 下午2:51
 */
@EApplication
public class CAApplication extends BaseApplication {
    public static final boolean IS_DEVELOPE = BuildConfig.isDev;// 上线时设置成false
    public static final boolean IS_LOG_DEBUG = BuildConfig.LOG_DEBUG;//是否打印日志，上线时设置为false
    public static final String IMAGE_URL = "http://files.dayhr.io:8080";

    private DeviceInfo mDeviceInfo;
    private ScreenInfo mScreenInfo;
    private AppInfo mAppInfo;

//    public boolean isMainPageInitialization;//首页是否已启动

    public String getmSaveDirPath() {
        return mSaveDirPath;
    }

    public void setmSaveDirPath(String mSaveDirPath) {
        this.mSaveDirPath = mSaveDirPath;
    }

    public String getmSaveVideoPath() {
        return mSaveVideoPath;
    }

    public void setmSaveVideoPath(String mSaveVideoPath) {
        this.mSaveVideoPath = mSaveVideoPath;
    }

    private String mSaveDirPath;
    private String mSaveImagePath;
    private String mSaveImagePathVisible;
    private String mSaveAudioPath;
    private String mSaveVideoPath;
    private String mSaveEmotionPath;
    private boolean runMainFalg; //是否进入主页面
    private boolean mIsLoginSuccessByAuthFailed; //是否因为cookie失效而又登录成功
    private static CAApplication INSTANCE;

    public static CAApplication getInstance() {
        return INSTANCE;
    }

    private int mPayType = 1;//默认自己的支付

    private RefWatcher mRefWatcher;

    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    private Gson mGson;

    @Override
    public void onCreate() {

        // 由配置文件配置是否打印日志
        setLogDebugEnable(IS_LOG_DEBUG);

        super.onCreate();
        INSTANCE = this;

        mGson = new Gson();

//        mRefWatcher = LeakCanary.install(this);

        initDeviceInfo();
        initVersionInfo();
        initScreenInfo();

        //初始化事件发生记录器
        Once.initialise(this);

        initRetrofit();
        createAppDir();

        //数据库查看工具
        initDbQuery();
        //全局异常捕获
//        initCrashCapture();
        initToast();
//        initLeakCanary();

//        initWebView();


    }

    private void initCrashCapture() {
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
        initialize(mAppContext, CrashActivity.class);
    }

    private void initWebView() {
        //初始化X5内核
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。

            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("@@","加载内核是否成功:"+b);
            }
        });
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);
    }


    private void initToast() {
        Toasty.Config.getInstance()
                .setErrorColor(getResources().getColor(R.color.error_color))
                .setInfoColor(getResources().getColor(R.color.info_color))
                .setSuccessColor(getResources().getColor(R.color.success_color))
                .setWarningColor(getResources().getColor(R.color.warning_color))
                .setTextColor(getResources().getColor(R.color.toast_text_color))
                .setTextSize(16)
                .setToastTypeface(Typeface.DEFAULT)
                .apply();


    }


    private void initDbQuery() {
        //查看数据库 chrome://inspect
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    private void initRetrofit() {
        // 初始化 retrofit
        RetrofitSingleton.init();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 初始化数据库
     */
    @Override
    public void initDB() {

    }

    /**
     * 获取设备号
     */
    private void initDeviceInfo() {
        mDeviceInfo = new DeviceInfo();
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String deviceId = "";
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                deviceId = tm.getDeviceId();
                if (StringUtils.isEmpty(deviceId)) {
                    deviceId = tm.getSubscriberId();
                }
                return;
            }
            String mac = MacUtil.getLocalMacAddress(this);
            String result = MD5.md5(deviceId + mac, "utf-8");
            mDeviceInfo.deviceId = result;
        } catch (Exception e) {
            String mac = MacUtil.getLocalMacAddress(this);
            String result = MD5.md5(UUID.randomUUID().toString().substring(0, 8) + mac, "utf-8");
            mDeviceInfo.deviceId = result;
        }
    }

    /**
     * 初始化版本信息
     */
    private void initVersionInfo() {
        try {
            mAppInfo = new AppInfo();
            PackageInfo pInfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            mAppInfo.versionCode = pInfo.versionCode;
            String versionName = pInfo.versionName;
            if (!StringUtils.isEmpty(versionName)) {
                mAppInfo.versionName = versionName.replace(BuildConfig.versionNameSuffix, "");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化屏幕信息
     */
    private void initScreenInfo() {
        mScreenInfo = new ScreenInfo();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mScreenInfo.density = displayMetrics.density;
        mScreenInfo.widthPixels = displayMetrics.widthPixels;
        mScreenInfo.heightPixels = displayMetrics.heightPixels;
    }

    /**
     * 创建文件目录
     */
    private void createAppDir() {
        String state = Environment.getExternalStorageState();
        String storage = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) { // We can read and write
            // the media
            storage = Environment.getExternalStorageDirectory().toString();
        } else {
            storage = getFilesDir().toString();
        }
        StringBuffer sb = new StringBuffer();
        mSaveDirPath = sb.append(storage).append(File.separator).append("caterers_pos").toString();
        createDir(mSaveDirPath);

        sb = new StringBuffer();
        mSaveImagePath = sb.append(mSaveDirPath).append(File.separator).append("image").append(File.separator).toString();
        createDir(mSaveImagePath);

        sb = new StringBuffer();
        mSaveAudioPath = sb.append(mSaveDirPath).append(File.separator).append("audio").append(File.separator).toString();
        createDir(mSaveAudioPath);

        sb = new StringBuffer();
        mSaveVideoPath = sb.append(mSaveDirPath).append(File.separator).append("video").append(File.separator).toString();
        createDir(mSaveVideoPath);

        sb = new StringBuffer();
        mSaveEmotionPath = sb.append(mSaveDirPath).append(File.separator).append("emotion").append(File.separator).toString();
        createDir(mSaveEmotionPath);

        // 用户点击保存后，存放图片到这里
        sb = new StringBuffer();
        mSaveImagePathVisible = sb.append(mSaveDirPath).append(File.separator).append("image").append(File.separator).toString();
        createDir(mSaveImagePathVisible);
    }

    private void createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void initialize(Context context, Class<? extends Activity> clazz) {
        CrashHelper.install(mAppContext, clazz);
    }

    public DeviceInfo getDeviceInfo() {
        return mDeviceInfo;
    }

    public ScreenInfo getScreenInfo() {
        return mScreenInfo;
    }

    public AppInfo getAppInfo() {
        return mAppInfo;
    }

    public String getSaveImagePath() {
        return mSaveImagePath;
    }

    public void setRunMainFlag(boolean runMainFalg) {
        this.runMainFalg = runMainFalg;
    }

    public boolean getRunMainFlag() {
        return runMainFalg;
    }

    public boolean ismIsLoginSuccessByAuthFailed() {
        return mIsLoginSuccessByAuthFailed;
    }

    public void setmIsLoginSuccessByAuthFailed(boolean mIsLoginSuccessByAuthFailed) {
        this.mIsLoginSuccessByAuthFailed = mIsLoginSuccessByAuthFailed;
    }

    public int getmPayType() {
        return mPayType;
    }

    public void setmPayType(int mPayType) {
        this.mPayType = mPayType;
    }

    public Gson getmGson() {
        return mGson;
    }
}

