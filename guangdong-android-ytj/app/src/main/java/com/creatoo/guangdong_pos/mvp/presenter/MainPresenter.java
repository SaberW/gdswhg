package com.creatoo.guangdong_pos.mvp.presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.creatoo.guangdong_pos.db.dao.UserDao;
import com.creatoo.guangdong_pos.mvp.contract.MainContract;
import com.creatoo.guangdong_pos.mvp.ui.activity.MainActivity;
import com.creatoo.guangdong_pos.mvp.ui.viewmodel.NameViewModel;
import com.force.librarybase.mvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.presenter
 * @Description:
 * @date 2018/4/12
 */
public class MainPresenter extends BasePresenter<MainContract.View,MainActivity> implements MainContract.Presenter{

    private NameViewModel mNameModel;

    private List<String> nameData = new ArrayList<>();

    private UserDao mUserDao;

    public MainPresenter(@NonNull MainContract.View view,@NonNull  MainActivity activity) {
        super(view, activity);
        mView.setPresenter(MainPresenter.this);
//        initLiveData();
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        super.onCreate(owner);
        mView.showToast("onCreate");
    }

//    @Override
//    public void initLiveData() {
//        mNameModel = ViewModelProviders.of(mActivity).get(NameViewModel.class);
//        final Observer<String> nameObserver = new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable final String newName) {
//                mActivity.updateName(newName);
//            }
//        };
//
//        final Observer<List<String>> nameListObserver = new Observer<List<String>>() {
//            @Override
//            public void onChanged(@Nullable List<String> strings) {
//                mActivity.updateNameList(strings);
//            }
//        };
//
//        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
//        mNameModel.getmCurrentName().observe(mActivity, nameObserver);
//        mNameModel.getmNameList().observe(mActivity, nameListObserver);
//    }

//    @Override
//    public void addName(String name) {
//        mNameModel.getmCurrentName().setValue(name);
//        nameData.add(name);
//        mNameModel.getmNameList().setValue(nameData);
//    }

    public NameViewModel getmNameModel() {
        return mNameModel;
    }

    public void setmNameModel(NameViewModel mNameModel) {
        this.mNameModel = mNameModel;
    }
}
