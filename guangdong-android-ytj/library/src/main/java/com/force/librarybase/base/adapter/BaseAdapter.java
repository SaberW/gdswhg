package com.force.librarybase.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.force.librarybase.base.viewholder.BaseViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by kim on 2016/5/17.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter {

    protected Context mContext;
    protected List<T> mDataSet;
    protected LayoutInflater mLayoutInflater;
    protected Serializable extraData; //额外数据
    protected boolean ignoreEmptyDataSet = false; //不忽略空数据情况

    protected BaseViewHolder.OnItemClickListener mOnItemClickListener;
    protected BaseViewHolder.OnItemLongClickListener mOnItemLongClickListener;

    public BaseAdapter(Context context) {
        mContext = context;
        mDataSet = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 设置是否处理空数据集，默认为不处理
     *
     * @ param ignoreEmptyDataSet
     */
    public void ignoreEmptyDataSet(boolean ignoreEmptyDataSet) {
        this.ignoreEmptyDataSet = ignoreEmptyDataSet;
    }

    public void refreshData(List<T> dataSet) {
        /*if (CollectionUtils.isEmpty(dataSet)) {
            if (ignoreEmptyDataSet) {
                return;
            }
            dataSet = new ArrayList<>();
        }
        mDataSet.clear();
        mDataSet.addAll(dataSet);*/
        if(null != dataSet){
            mDataSet = dataSet;
            notifyDataSetChanged();
        }
    }

    /**
     * 绑定额外数据
     *
     * @ param extraData
     */
    public void bindExtraData(Serializable extraData) {
        this.extraData = extraData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public T getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return mDataSet.get(position);
    }

    public void setOnItemClickListener(BaseViewHolder.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(BaseViewHolder.OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }
}
