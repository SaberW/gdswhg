package com.creatoo.guangdong_pos.mvp.ui.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.creatoo.guangdong_pos.R;
import com.creatoo.guangdong_pos.base.CABaseActivity;
import com.creatoo.guangdong_pos.mvp.contract.MainContract;
import com.creatoo.guangdong_pos.mvp.presenter.MainPresenter;
import com.creatoo.guangdong_pos.mvp.ui.viewholder.NameViewHolder;
import com.creatoo.guangdong_pos.utils.CommonKey;
import com.force.librarybase.base.adapter.SingleViewTypeAdapter;
import com.force.librarybase.mvp.presenter.IBasePresenter;
import com.force.librarybase.utils.StartAnimUtil;
import com.force.librarybase.utils.ToastUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends CABaseActivity implements MainContract.View{

    private MainContract.Presenter mPresenter;

    private SingleViewTypeAdapter<String,NameViewHolder> mAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Click({R.id.btn_home,R.id.btn_sign,R.id.btn_ticket})
    void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_home:
                X5BrowserActivity_.intent(this).mWebMode(CommonKey.Constant.MODE_SONIC)
                        .mUrl("http://192.168.0.166:8088/gdswhg/ytj/index").start();
                StartAnimUtil.commonStartAnim(this);
                break;
            case R.id.btn_sign:
                SignActivity_.intent(this).start();
                StartAnimUtil.commonStartAnim(this);
                break;
            case R.id.btn_ticket:
                TicketActivity_.intent(this).start();
                StartAnimUtil.commonStartAnim(this);
                break;
        }
    }


   @AfterViews
    void afterView(){
        new MainPresenter(this,this);
        getLifecycle().addObserver(mPresenter);
   }


    @Override
    public void setPresenter(IBasePresenter presenter) {
        this.mPresenter = (MainContract.Presenter) presenter;
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(this,3,msg,true);
    }


//    @Override
//    public void initAdapter() {
//        mAdapter = new SingleViewTypeAdapter<String,NameViewHolder>(this,R.layout.item_name,NameViewHolder.class);
//        mNameRv.setLayoutManager(new LinearLayoutManager(this));
//        mNameRv.setAdapter(mAdapter);
//    }

//    @Override
//    public void updateName(String name) {
//        mCurrentNameTv.setText(name);
//    }
//
//    @Override
//    public void updateNameList(List<String> nameList) {
//        mAdapter.refreshData(nameList);
//    }



}
