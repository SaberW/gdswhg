package com.force.librarybase.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.force.librarybase.base.viewholder.BaseViewHolder;
import com.force.librarybase.base.viewholder.ViewHolderFactory;
import com.force.librarybase.utils.Null;


/**
 * Created by kim on 2016/5/17.
 */
public class SingleViewTypeAdapter<T, V extends RecyclerView.ViewHolder> extends BaseAdapter {

    private Class<V> mClazz;
    private int mItemLayoutId;
    protected BaseViewHolder.ListObserver<T> mListObserver;

    public SingleViewTypeAdapter(Context context, int itemLayoutId, Class<V> clazz) {
        super(context);
        this.mClazz = clazz;
        this.mItemLayoutId = itemLayoutId;
    }

    public SingleViewTypeAdapter(Context context, int itemLayoutId, Class<V> clazz, BaseViewHolder.ListObserver<T> listObserver) {
        super(context);
        this.mClazz = clazz;
        this.mItemLayoutId = itemLayoutId;
        this.mListObserver = listObserver;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        RecyclerView.ViewHolder viewHolder = ViewHolderFactory.generateViewHolder(mContext, mClazz, itemView, this);
        ((BaseViewHolder) viewHolder).initView();
        ((BaseViewHolder) viewHolder).setOnItemClickListener(mOnItemClickListener);
        ((BaseViewHolder) viewHolder).setOnItemLongClickListener(mOnItemLongClickListener);
        ((BaseViewHolder) viewHolder).setListObserver(mListObserver);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!Null.isNull(extraData)) {
            ((BaseViewHolder<T>) holder).bindExtraData(extraData);
        }
        ((BaseViewHolder<T>) holder).bindData((T) getItem(position), position);
    }
}
