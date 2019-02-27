package com.force.librarybase.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.force.librarybase.R;


/**
 * BaseAct中的头部布局（返回按钮、标题、可选操作的按钮）
 *
 * @author Jacky.Cai
 * @version V1.0.0 创建时间：2014-6-3 下午6:35:57
 */
public class HeaderBackTopView extends RelativeLayout {

    /**
     * 左边按钮
     */
    private TextView mLeftTv;
    /**
     * 标题
     */
    private TextView mTitleTv;
    /**
     * 操作按钮
     */
    private TextView mOptionTv;
    /**
     * 返回按钮的扩大作用域
     */
    private RelativeLayout mBackRl;

    private RelativeLayout mHeadView;

    /**
     * 操作按钮
     */
    private RelativeLayout mRightOptionRl;
    private RelativeLayout mLeftOptionRl;
    private LinearLayout mTitleLl;
    private ImageView mOptionIv;
    private TextView mBackView;
    private Context mContext;

    public HeaderBackTopView(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public HeaderBackTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    /**
     * 初始化UI控件
     */
    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_top_header, this);
        mLeftTv = (TextView) findViewById(R.id.view_top_left_title);
        mTitleTv = (TextView) findViewById(R.id.view_top_title);
        mOptionTv = (TextView) findViewById(R.id.view_top_option);
        mLeftOptionRl = (RelativeLayout) findViewById(R.id.rl_top_left_option);
        mRightOptionRl = (RelativeLayout) findViewById(R.id.rl_top_option);
        mBackRl = (RelativeLayout) findViewById(R.id.view_back_layout);
        mTitleLl = (LinearLayout) findViewById(R.id.view_top_ll);
        mOptionIv = (ImageView) findViewById(R.id.iv_option);
        mHeadView = (RelativeLayout) findViewById(R.id.view_head);
        mBackView = (TextView) findViewById(R.id.view_top_back);
    }

    public void setOptionTvColor(int colorRes,int backgroundRes){
        mOptionTv.setTextColor(ContextCompat.getColor(mContext,colorRes));
        mRightOptionRl.setBackgroundResource(backgroundRes);
    }

    public void setTitleTextColor(int colorRes) {
        mTitleTv.setTextColor(ContextCompat.getColor(mContext, colorRes));
    }

    public void setBackViewIconRes(int backRes) {
        mBackView.setBackgroundResource(backRes);
    }

    public void setHeadViewBackgroundRes(int bgRes) {
        mHeadView.setBackgroundResource(bgRes);
    }

    public void setHeadViewBackgroundColor(int colorRes) {
        mHeadView.setBackgroundColor(ContextCompat.getColor(mContext, colorRes));
    }

    public void setBackbtnBackgroundSelector(int selectorRes) {
        mBackRl.setBackgroundResource(selectorRes);
    }

    /**
     * 设置左边的操作按钮
     *
     * @param title
     * @param listener
     */
    public void setLeftOption(String title, OnClickListener listener) {
        mLeftOptionRl.setVisibility(View.VISIBLE);
        mLeftTv.setText(title);
        mLeftOptionRl.setOnClickListener(listener);
    }

    /**
     * 设置title显示的文字
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        mTitleTv.setText(title);
    }

    /**
     * 设置返回按钮可见/隐藏（可见的话，执行所在Activity的onBackPressed方法；或不可见）
     *
     * @param visible
     */
    public void setBackVisible(boolean visible) {
        if (visible) {
            mBackRl.setVisibility(View.VISIBLE);
            OnClickListener listener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) getContext()).onBackPressed();
                }
            };
            mBackRl.setOnClickListener(listener);
        } else {
            mBackRl.setVisibility(View.INVISIBLE);
        }
    }

    public void setBackVisible(boolean visible, OnClickListener listener) {
        if (visible) {
            mBackRl.setVisibility(View.VISIBLE);
            mBackRl.setOnClickListener(listener);
        } else {
            mBackRl.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置操作按钮
     *
     * @param option   操作按钮上显示的文字
     * @param listener 操作按钮的点击监听事件
     */
    public void setOption(String option, OnClickListener listener) {
        mRightOptionRl.setVisibility(View.VISIBLE);
        mOptionTv.setVisibility(View.VISIBLE);
        mOptionIv.setVisibility(View.GONE);
        mOptionTv.setText(option);
        mRightOptionRl.setOnClickListener(listener);
    }

    public void setOption(String option, int drawableid, OnClickListener listener) {
        mRightOptionRl.setVisibility(View.VISIBLE);
        mOptionTv.setVisibility(View.VISIBLE);
        mOptionIv.setVisibility(View.GONE);
        mOptionTv.setText(option);
        mRightOptionRl.setOnClickListener(listener);
        if (drawableid != 0) {
            mOptionTv.setBackgroundResource(drawableid);
        }
    }

    public void setOptionResourse(String option, int drawableid, OnClickListener listener) {
        mRightOptionRl.setVisibility(View.VISIBLE);
        mOptionTv.setVisibility(View.VISIBLE);
        mOptionIv.setVisibility(View.VISIBLE);
        mOptionTv.setText(option);
        mRightOptionRl.setOnClickListener(listener);
        if (drawableid != 0) {
            mOptionIv.setImageResource(drawableid);
        }
    }

    /**
     * 设置操作按钮是否可见
     *
     * @param show
     */
    public void setOptionVisible(boolean show) {
        if (show) {
            mRightOptionRl.setVisibility(View.VISIBLE);
        } else {
            mRightOptionRl.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获得title上显示的文字
     *
     * @return
     */
    public String getTitle() {
        return mTitleTv.getText().toString();
    }

    /**
     * 设置右侧操作按钮label
     *
     * @param title
     */
    public void setOptionTitle(CharSequence title) {
        mOptionTv.setText(title);
    }

    public void setOptionMore(OnClickListener listener) {
        mTitleLl.setOnClickListener(listener);
    }

    public void setOptionRes(int drawableId, OnClickListener listener) {
        mRightOptionRl.setVisibility(View.VISIBLE);
        mOptionTv.setVisibility(View.GONE);
        mOptionIv.setImageResource(drawableId);
        mOptionIv.setVisibility(View.VISIBLE);
        mRightOptionRl.setOnClickListener(listener);
    }

    /**
     * 获取顶部栏右侧操作按钮
     *
     * @return
     */
    public TextView getOptionView() {
        return mOptionTv;
    }
}