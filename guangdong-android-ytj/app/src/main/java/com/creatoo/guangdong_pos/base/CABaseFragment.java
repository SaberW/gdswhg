package com.creatoo.guangdong_pos.base;

import android.view.View;
import android.widget.TextView;

import com.creatoo.guangdong_pos.R;
import com.force.librarybase.BaseFragment;
import com.force.librarybase.utils.Null;
import com.force.librarybase.widget.HeaderBackTopView;

/**
 * @author Jacky.Cai
 * @version v1.0
 * @ Package com.dayhr.caterers.mobile.base
 * @ Description:
 * @ date 16/8/23 下午3:50
 */
public class CABaseFragment extends BaseFragment {

    protected HeaderBackTopView mHeadBar; //顶部栏


    protected View findViewById(int id) {
        // empty implement
        return null;
    }

    /********************************************
     * 顶部栏操作接口
     **********************************/
    private void initHeadbarLayze() {
        if (Null.isNull(mHeadBar)) {
            //mHeadBar = (HeaderBackTopView) findViewById(R.id.id_top);
        }
        if (Null.isNull(mHeadBar)) {
            throw new RuntimeException("If you need use HeadBar, Please add CAHeaderBackTopView in your layout xml file, or you forget add id 'id_top' ? ");
        }
    }

    protected void setHeadBarTitle(CharSequence title) {
        initHeadbarLayze();
        mHeadBar.setTitle(title);
    }

    protected void setHeadBarTitle(int resId) {
        initHeadbarLayze();
        mHeadBar.setTitle(getString(resId));
    }

    protected void setBackVisible(boolean visible) {
        initHeadbarLayze();
        mHeadBar.setBackVisible(visible);
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

    protected void setLeftOption(int optionResId, View.OnClickListener listener) {
        initHeadbarLayze();
        mHeadBar.setLeftOption(getString(optionResId), listener);
    }

    protected void setOptionMore(View.OnClickListener listener) {
        initHeadbarLayze();
        mHeadBar.setOptionMore(listener);
    }

    /**********************************************************************************************/

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = CAApplication.getInstance().getRefWatcher();
//        if (!Null.isNull(refWatcher)) {
//            refWatcher.watch(this);
//        }
    }

    @Override
    protected void lazyLoad() {

    }
}
