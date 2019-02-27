package com.creatoo.guangdong_pos.base;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creatoo.guangdong_pos.R;
import com.force.librarybase.BaseActivity;
import com.force.librarybase.utils.Null;
import com.force.librarybase.utils.SizeUtils;
import com.force.librarybase.utils.StartAnimUtil;
import com.force.librarybase.utils.SystemBarTintManager;
import com.force.librarybase.widget.HeaderBackTopView;

/**
 * @author Jacky.Cai
 * @version v1.0
 * @ Package com.dayhr.cscec.pmc.base
 * @ Description:
 * @ date 16/7/28 上午11:24
 */
public class CABaseActivity extends BaseActivity {
    protected HeaderBackTopView mHeadBar; //顶部栏

    @Override
    protected void setStatusBarTintColor() {
        if (!getEnableStatusBarTintColor()) {
            return;
        }
        mTintManager.setTintColor(ContextCompat.getColor(this, R.color.bg_header));
    }

    @Override
    protected boolean getEnableStatusBarTintColor() {
        return getStatusBarTransparentEnable();
    }

    protected boolean getStatusBarTransparentEnable() {
        return SystemBarTintManager.getStatusBarTransparentEnable();
    }

    /**
     * 动态设置标题栏和状态栏，继承自该类的子类，需手动在视图初始化之后调用
     * <p/>
     * 同时，需在页面根布局xml中添加
     * <p>android:clipToPadding="true"</p>
     * <p>android:fitsSystemWindows="true"</p>
     */
    protected void setTopbarHeightDynamic() {

        if (getStatusBarTransparentEnable()) { //状态栏可设置颜色
            return;
        }

        View topbar = null; //= findViewById(R.id.id_top);
        if (!Null.isNull(topbar)) {
            ViewGroup.LayoutParams params = topbar.getLayoutParams();
            int size = SizeUtils.getPixByDip(50, getResources().getDisplayMetrics());
            if (!Null.isNull(params)) {
                params.height = size;
                topbar.setLayoutParams(params);
            }
        }
    }

    public void finish() {
        finish(true);
    }

    public void finish(boolean needAnim) {
        if (needAnim) {
            super.finish();
            StartAnimUtil.commonFinishAnim(this);
        } else {
            super.finish();
        }
    }

    /********************************************
     * 顶部栏操作接口
     **********************************/
    private void initHeadbarLayze() {
        if (Null.isNull(mHeadBar)) {
//            mHeadBar = (HeaderBackTopView) findViewById(R.id.id_top);
        }
        if (Null.isNull(mHeadBar)) {
            throw new RuntimeException("If you need use HeadBar, Please add CBSHeaderBackTopView in your layout xml file, or you forget add id 'id_top' ? ");
        }
//        mHeadBar.setTitleTextColor(R.color.blue_f9);
//        mHeadBar.setBackViewIconRes(R.mipmap.icon_back);
//        mHeadBar.setHeadViewBackgroundRes(R.color.white);
//        mHeadBar.setBackbtnBackgroundSelector(R.drawable.header_back_btn);
//        mHeadBar.setOptionTvColor(R.color.blue_f0,R.drawable.header_back_btn);
    }

    protected void hideHeadBar() {
        initHeadbarLayze();
        mHeadBar.setVisibility(View.GONE);
    }

    protected void setHeadBarTitle(CharSequence title) {
        initHeadbarLayze();
        mHeadBar.setTitle(title);
        mHeadBar.requestFocus();
        mHeadBar.setFocusable(true);
        mHeadBar.setFocusableInTouchMode(true);
    }

    protected void setHeadBarTitle(int resId) {
        initHeadbarLayze();
        mHeadBar.setTitle(getString(resId));
    }

    protected void setBackVisible(boolean visible) {
        initHeadbarLayze();
        mHeadBar.setBackVisible(visible);
    }

    protected void setBackVisible(boolean visible, View.OnClickListener listener) {
        initHeadbarLayze();
        mHeadBar.setBackVisible(visible, listener);
    }

    public void setOptionTitle(CharSequence title) {
        mHeadBar.setOptionTitle(title);
    }

    protected void setOption(String option, View.OnClickListener listener) {
        initHeadbarLayze();
        mHeadBar.setOption(option, listener);
    }

    protected void setOption(String option, int drawableId, View.OnClickListener listener) {
        initHeadbarLayze();
        mHeadBar.setOption(option, drawableId, listener);
    }

    protected void setOptionRes(int drawableId, View.OnClickListener listener) {
        initHeadbarLayze();
        mHeadBar.setOptionRes(drawableId, listener);
    }

    protected void setOptionPadding(int left, int top, int right, int bottom) {
        initHeadbarLayze();
        TextView optionTv = (TextView) mHeadBar.findViewById(R.id.view_top_option);
        optionTv.setPadding(left, top, right, bottom);
    }

    protected void setOption(int optionResId, View.OnClickListener listener) {
        initHeadbarLayze();
        mHeadBar.setOption(getString(optionResId), listener);
    }

    protected void setOptionVisible(boolean show) {
        initHeadbarLayze();
        mHeadBar.setOptionVisible(show);
    }

    protected void hideHeaderBar() {
        initHeadbarLayze();
        mHeadBar.setVisibility(View.GONE);
    }

    protected void showHeaderBar() {
        initHeadbarLayze();
        mHeadBar.setVisibility(View.VISIBLE);
    }

    protected void setOptionMore(View.OnClickListener listener) {
        initHeadbarLayze();
        mHeadBar.setOptionMore(listener);
    }

    /**********************************************************************************************/

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = CAApplication.getInstance().getRefWatcher();
//        if (!Null.isNull(refWatcher)) {
//            refWatcher.watch(this);
//        }
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//    }
}

