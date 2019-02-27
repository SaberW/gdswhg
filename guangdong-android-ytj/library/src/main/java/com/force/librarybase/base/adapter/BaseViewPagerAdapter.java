package com.force.librarybase.base.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.force.librarybase.base.viewholder.BaseViewHolder;
import com.force.librarybase.base.viewholder.ViewHolderFactory;
import com.force.librarybase.utils.logger.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Kim.H
 * @version v1.0
 * @ Package com.l99.lotto.adapter.pageradapter
 * @ Description:
 * @ date 2015-09-17 10:59
 */
public class BaseViewPagerAdapter<T> extends PagerAdapter {

    protected Context mContext;
    protected List<T> mDataSet;
    protected LayoutInflater mLayoutInflater;

    protected Class clazz;
    protected int layoutId;

    private BaseViewPagerAdapter(Context context) {
        mContext = context;
        mDataSet = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public BaseViewPagerAdapter(Context context, Class viewHolderClazz, int itemLayoutId) {
        this(context);
        clazz = viewHolderClazz;
        layoutId = itemLayoutId;
    }

    public void refreshData(List<T> dataSet) {
        this.mDataSet.clear();
        this.mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        //        return super.instantiateItem(container, position);

        View view = mLayoutInflater.inflate(layoutId, null);
        RecyclerView.ViewHolder viewHolder = ViewHolderFactory.generateViewHolder(mContext, clazz, view);
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        try{
            ((BaseViewHolder) viewHolder).initView();
            ((BaseViewHolder) viewHolder).bindData(mDataSet.get(position), position);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        Logger.d("instantiateItem");
        return view;
    }

}
