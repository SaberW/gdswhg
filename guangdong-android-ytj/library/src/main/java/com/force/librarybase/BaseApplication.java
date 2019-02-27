package com.force.librarybase;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.force.librarybase.command.Response;
import com.force.librarybase.command.Task;
import com.force.librarybase.command.TaskManager;
import com.force.librarybase.netstatus.NetworkStateReceiver;
import com.force.librarybase.utils.GlideLoader;
import com.force.librarybase.utils.PreferencesUtils;
import com.force.librarybase.utils.SDCardUtils;
import com.force.librarybase.utils.logger.LogLevel;
import com.force.librarybase.utils.logger.Logger;
import com.force.librarybase.utils.logger.Settings;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Jacky.Cai
 * @version v1.1
 * @Package com.force.librarybase
 * @Description:
 * @date 16/11/1 下午3:07
 */
public abstract class BaseApplication extends Application {

    protected static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY - 1; //ImageLoader 图片加载线程优先级别
    protected static final int DEFAULT_MEMORY_CACHE_SIZE = 1024 * 1024 * 16; // 默认内存缓存大小
    protected static final int DEFAULT_DISK_CACHE_SIZE = 100 * 1024 * 1024; //默认磁盘缓存大小

    /*******************
     * GreenDao相关
     *******************/
//    protected SQLiteDatabase readableDB;
//    protected SQLiteDatabase writableDB;

    /*******************
     * 图片加载相关
     *******************/
    protected GlideLoader mGlideLoader;

    protected TaskManager mTaskManager;

    protected EventBus mEventBus;

    protected static BaseApplication INSTANCE;

    protected boolean mDebugEnable = false;

    public static String mCacheDir;
    public static Context mAppContext = null;

    public static BaseApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        init();
        INSTANCE = this;
    }

    @SuppressLint("NewApi")
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Logger.i("onTrimMemory| level:%s", level);
        if (ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN >= level) {
            Glide.get(this).trimMemory(level);
        }
//		TRIM_MEMORY_COMPLETE：内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
//		TRIM_MEMORY_MODERATE：内存不足，并且该进程在后台进程列表的中部。
//		TRIM_MEMORY_BACKGROUND：内存不足，并且该进程是后台进程。
//		TRIM_MEMORY_UI_HIDDEN：内存不足，并且该进程的UI已经不可见了。
//		以上4个是4.0增加
//		TRIM_MEMORY_RUNNING_CRITICAL：内存不足(后台进程不足3个)，并且该进程优先级比较高，需要清理内存
//		TRIM_MEMORY_RUNNING_LOW：内存不足(后台进程不足5个)，并且该进程优先级比较高，需要清理内存
//		TRIM_MEMORY_RUNNING_MODERATE：内存不足(后台进程超过5个)，并且该进程优先级比较高，需要清理内存
//		以上3个是4.1增加
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.i("onLowMemory");
        Glide.get(this).clearMemory();
    }

    private void init() {
        try {
            initDB();
        } catch (Exception e) {
            Logger.e("init db error:%s", e.getMessage());
        }
        registerNetStatusChangeReceiver();
        initTaskManager();
        initEventBus();
        initLogger();
        initCache();
        initGlideLoader();
    }

    public void exit(){
        exitLogger();
    }

    /**
     * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
     */
    private void initCache() {
        if (getApplicationContext().getExternalCacheDir() != null && SDCardUtils.isExitsSdcard()) {
            mCacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            mCacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    /**
     * 初始化Glide
     */
    private void initGlideLoader() {
        mGlideLoader = GlideLoader.getInstance();
    }

    /**
     * 初始化任务管理器
     */
    private void initTaskManager() {
        mTaskManager = TaskManager.getInstance();
        mTaskManager.start();
    }

    /**
     * 初始化EventBus
     */
    private void initEventBus() {
        mEventBus = EventBus.getDefault();
    }

    /**
     * 初始化日志打印器
     * 在应用发布时，可在application中重新初始化，关闭日志打印开关
     */
    protected void initLogger() {
        Settings settings = Logger.init(getClass().getSimpleName());
        if (mDebugEnable) {
            settings.setLogLevel(LogLevel.FULL).needFileLog(this);
        } else {
            settings.setLogLevel(LogLevel.ERROR).needFileLog(this);
        }
    }


    private void exitLogger() {
        Settings settings = Logger.init(getClass().getSimpleName());
        settings.closeFileLog();
    }

    /**
     * 初始化数据库，在app中，需根据实际情况实现数据库升级逻辑
     */
    public abstract void initDB();

    // {
    // // 测试
    // DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db",
    // null);
    // // 正式
    // // OpenHelper helper = new DaoMaster.OpenHelper(this, "notes-db", null)
    // // {
    // //
    // // @Override
    // // public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
    // // // TODO Auto-generated method stub
    // // DaoMaster.dropAllTables(db, true);
    // // }
    // // };
    // readableDB = helper.getReadableDatabase();
    // writableDB = helper.getWritableDatabase();
    //
    // daoMasterReadable = new DaoMaster(readableDB);
    // daoSessionReadble = daoMasterReadable.newSession();
    //
    // mDaoMasterWritable = new DaoMaster(writableDB);
    // mDaoSessionWritable = mDaoMasterWritable.newSession();
    // }

//	public AbstractDaoSession getDaoSessionReadble() {
//		return daoSessionReadble;
//	}

    public GlideLoader getImageLoader() {
        return mGlideLoader;
    }

    public EventBus getEventBus() {
        return mEventBus;
    }

    public TaskManager getmTaskManager() {
        return mTaskManager;
    }

    public void sendTask(Task<Response> task) {
        mTaskManager.add(task);
    }

    /**
     * 结束任务队列
     */
    public void stopTaskQuenen() {
        if (mTaskManager != null) {
            mTaskManager.stop();
        }
    }

    /**
     * 取消任务
     *
     * @param task
     */
    public void cancelTask(Task<?> task) {
        cancelTask(task.getId());
    }

    /**
     * 取消任务
     *
     * @param taskId
     */
    public void cancelTask(String taskId) {
        if (mTaskManager != null && !mTaskManager.isStop()) {
            mTaskManager.cancelTask(taskId);
        }
    }

    /**
     * 取消任务，即使正在执行中（只是将任务移出任务队列并抛弃执行结果）
     *
     * @param task 任务实例
     */
    public void cancelTaskEvenInExecutting(Task<?> task) {
        if (mTaskManager != null && !mTaskManager.isStop()) {
            mTaskManager.cancelTaskEvenInExecution(task);
        }
    }

    /**
     * 取消任务，即使正在执行中（只是将任务移出任务队列并抛弃执行结果）
     *
     * @param taskId 任务ID
     */
    public void cancelTaskEvenInExecutting(String taskId) {
        if (mTaskManager != null && !mTaskManager.isStop()) {
            mTaskManager.cancelTaskEvenInExecution(taskId);
        }
    }

    /**
     * 获取默认SharedPreference管理器，文件名为"lifeix"
     *
     * @return
     */
    public PreferencesUtils getPreference() {
        return PreferencesUtils.getPreferenceConfig(this);
    }

    /**
     * 获取SharedPreference管理器
     *
     * @param preFileName 配置文件名
     * @return
     */
    public PreferencesUtils getPreference(String preFileName) {
        PreferencesUtils preferencesUtils = PreferencesUtils
                .getPreferenceConfig(this);
        preferencesUtils.loadConfig(preFileName);
        return preferencesUtils;
    }

    public void registerNetStatusChangeReceiver() {
        NetworkStateReceiver.registerNetworkStateReceiver(this);
    }

    protected void setLogDebugEnable(boolean debugEnable) {
        this.mDebugEnable = debugEnable;
    }

    public static Context getmAppContext() {
        return mAppContext;
    }

}

