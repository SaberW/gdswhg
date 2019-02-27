package com.force.librarybase.base;

import android.content.Context;
import android.os.Handler;

import com.force.librarybase.utils.logger.Logger;

import java.util.HashMap;

/**
 * @author Hugh
 * @version v1.0
 * @Package com.daydao.caterers.fastfood.base
 * @Description:
 * @date 17:28
 */
public abstract class BaseProcessor {


    /**
     * 存储所有Action
     */
    private HashMap<String, BaseAction> mActionMap;

    public Context mCtx;

    /**
     * 过程所需参数
     */
    private BaseProcessorInfo mBaseProccessInfo;

    /**
     * 过程结果回调
     */
    private BaseProcessorCallback mResultCallback;

    /**
     * 当过程 失败/中止 时显示的错误提示，不设置将显示通用默认错误
     */
    private String mErrMsg;


    /**
     * 结果回调Handler
     */
    private Handler mResultHandler;

    public static final int MSG_ID_SUCCESS = 0;

    public static final int MSG_ID_FAIL = 1;

    public static final int MSG_ID_CANCEL = -1;

    private boolean mIsCheckouting = false;


    public BaseProcessor(Context context) {
        super();
        mCtx = context;
        initHandler();
        mActionMap = new HashMap<String, BaseAction>();
        initActions();
    }

    public abstract void initActions();

    /**
     * 从指定Action开始执行
     *
     * @param actionName
     */
    public void doAction(String actionName) {
        Logger.d("DoAction: " + actionName);

        final BaseAction action = mActionMap.get(actionName);

        action.setmCallback(new BaseProcessorCallback() {

            @Override
            public void successProcessor(BaseProcessorInfo processorInfo) {
                if (action.getNextAction() != null) {
                    //递归执行Action
                    Logger.d("DoAction Success: " + action.getActionName()
                            + ", NextAction: " + action.getNextAction());
                    doAction(action.getNextAction());
                } else {
                    //流程结束，回调正确结果
                    Logger.d("Checkout Success!");
                    processorSuccess(processorInfo);
                }
            }

            @Override
            public void failProcessor(BaseProcessorInfo processorInfo) {
                //Action执行中断，错误处理
                processorFail(processorInfo);
                Logger.w("DoAction Fail: " + action.getActionName());
            }

            @Override
            public void cancelProcessor(BaseProcessorInfo processorInfo) {
                Logger.d("DoAction cancel: " + action.getActionName());
                processorCancel(processorInfo);
            }

        });

        if (action != null) {
            action.doAction();
        }
    }

    public abstract void initHandler();

    /**
     * 发起流程
     *
     * @param processorInfo 流程参数 信息
     */
    public abstract void doProcessor(BaseProcessorInfo processorInfo);


    public abstract void processorSuccess(BaseProcessorInfo processorInfo);

    public abstract void processorFail(BaseProcessorInfo processorInfo);

    public abstract void processorCancel(BaseProcessorInfo processorInfo);

    public void setResultCallback(BaseProcessorCallback resultCallback) {
        this.mResultCallback = resultCallback;
    }

    public BaseProcessorCallback getmResultCallback() {
        return mResultCallback;
    }

    public BaseProcessorInfo getmBaseProccessInfo() {
        return mBaseProccessInfo;
    }

    public void setmBaseProccessInfo(BaseProcessorInfo mBaseProccessInfo) {
        this.mBaseProccessInfo = mBaseProccessInfo;
    }

    public Handler getmResultHandler() {
        return mResultHandler;
    }

    public void setmResultHandler(Handler handler) {
        this.mResultHandler = handler;
    }

    public HashMap<String, BaseAction> getmActionMap() {
        return mActionMap;
    }

    public void setmActionMap(HashMap<String, BaseAction> mActionMap) {
        this.mActionMap = mActionMap;
    }
}
