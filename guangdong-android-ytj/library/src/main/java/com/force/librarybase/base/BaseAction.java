package com.force.librarybase.base;

import android.content.Context;

/**
 * @author Hugh
 * @version v1.0
 * @Package com.daydao.caterers.fastfood.base
 * @Description:
 * @date 17:27
 */
public abstract class BaseAction {
    /**
     * 因后续要使用要OverLayer弹窗，context传 activity
     * --- 建议后续对OverLayer构造方法进行改造
     */
    protected Context mCtx;

    private String mActionName;

    protected BaseProcessor mProcessor;


    protected BaseProcessorCallback mCallback;


    private String mNextAction;

    public BaseAction(String actionName, BaseProcessor baseProcessor) {
        super();
        this.setActionName(actionName);
        this.mProcessor = baseProcessor;
    }

    public BaseAction(Context ctx, String actionName, BaseProcessor baseProcessor) {
        super();
        this.mCtx = ctx;
        this.setActionName(actionName);
        this.mProcessor = baseProcessor;
    }


    public abstract void doAction();


    public BaseProcessorCallback getmCallback() {
        return mCallback;
    }

    public void setmCallback(BaseProcessorCallback mCallback) {
        this.mCallback = mCallback;
    }

    public String getActionName() {
        return mActionName;
    }


    public void setActionName(String actionName) {
        this.mActionName = actionName;
    }


    public String getNextAction() {
        return mNextAction;
    }


    public void setNextAction(String nextAction) {
        this.mNextAction = nextAction;
    }
}
