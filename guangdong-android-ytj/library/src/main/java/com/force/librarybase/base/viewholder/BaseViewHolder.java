package com.force.librarybase.base.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.force.librarybase.base.adapter.BaseAdapter;
import com.force.librarybase.utils.Null;
import com.force.librarybase.utils.logger.Logger;

import java.io.Serializable;


/**
 * Created by kim on 2016/5/17.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View v, int position);
    }

    public interface ListObserver<T> {
        void onDataChanged(T dataItem, int position);
    }

    protected Context mContext;
    protected BaseAdapter<?> mAdapter;
    protected ListObserver<T> mListObserver;
    protected Serializable extraData;

    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;

    public BaseViewHolder(Context context,View itemView,BaseAdapter adapter) {
        super(itemView);
        mContext = context;
        mAdapter = adapter;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setListObserver(ListObserver<T> listObserver) {
        mListObserver = listObserver;
    }

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 绑定数据
     *
     * @param t
     * @param position
     */
    public abstract void bindData(T t, int position);

    /**
     * 绑定额外数据
     *
     * @ param extraData
     */
    public void bindExtraData(Serializable extraData) {
        this.extraData = extraData;
    }


    /**
     * 设置viewHolder点击监听
     *
     * @ param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 设置ViewHolder长按监听
     *
     * @ param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public void onClick(View v) {
        if (!Null.isNull(mOnItemClickListener)) {
            int position = getAdapterPosition();
            mOnItemClickListener.onClick(itemView, position);
            Logger.d("position:%d clicked", position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (!Null.isNull(mOnItemLongClickListener)) {
            int position = getAdapterPosition();
            mOnItemLongClickListener.onLongClick(itemView, position);
            Logger.d("position:%d long clicked", position);
            return true;
        }
        return false;
    }
}
