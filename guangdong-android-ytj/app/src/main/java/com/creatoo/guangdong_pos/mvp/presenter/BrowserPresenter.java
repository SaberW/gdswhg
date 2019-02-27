package com.creatoo.guangdong_pos.mvp.presenter;

import android.Manifest;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.creatoo.guangdong_pos.R;
import com.creatoo.guangdong_pos.mvp.ui.activity.BrowserActivity;
import com.creatoo.guangdong_pos.mvp.ui.activity.X5BrowserActivity;
import com.creatoo.guangdong_pos.utils.location.LocationHelper;
import com.force.librarybase.mvp.presenter.BasePresenter;
import com.creatoo.guangdong_pos.mvp.contract.BrowserContract;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.presenter
 * @Description:
 * @date 2018/4/19
 */
public class BrowserPresenter extends BasePresenter<BrowserContract.View,BrowserActivity> implements BrowserContract.Presenter {

    public static final int RC_LOCATION_CONTACTS_PERM = 124;
    public static final String[] LOCATION_AND_CONTACTS =
            {Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE};


    public BrowserPresenter( BrowserContract.View view,BrowserActivity activity) {
        super(view, activity);
        mView.setPresenter(BrowserPresenter.this);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        super.onStart(owner);
        locationAndContactsTask();
    }

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void locationAndContactsTask() {
        if (hasLocationAndContactsPermissions()) {

        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    mActivity,
                    mActivity.getString(R.string.rationale_location_contacts),
                    RC_LOCATION_CONTACTS_PERM,
                    LOCATION_AND_CONTACTS);
        }
    }

    private boolean hasLocationAndContactsPermissions() {
        return EasyPermissions.hasPermissions(mActivity, LOCATION_AND_CONTACTS);
    }
}
