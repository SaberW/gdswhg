package com.creatoo.guangdong_pos.mvp.ui.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.ui.viewmodel
 * @Description:
 * @date 2018/4/13
 */
public class NameViewModel extends ViewModel{
    private MutableLiveData<String> mCurrentName;

    private MutableLiveData<List<String>> mNameList;

    public MutableLiveData<String> getmCurrentName() {
        if (mCurrentName == null)
            mCurrentName = new MutableLiveData<>();
        return mCurrentName;
    }

    public void setmCurrentName(MutableLiveData<String> mCurrentName) {
        this.mCurrentName = mCurrentName;
    }

    public MutableLiveData<List<String>> getmNameList() {
        if (mNameList == null)
            mNameList = new MutableLiveData<>();
        return mNameList;
    }

    public void setmNameList(MutableLiveData<List<String>> mNameList) {
        this.mNameList = mNameList;
    }
}
