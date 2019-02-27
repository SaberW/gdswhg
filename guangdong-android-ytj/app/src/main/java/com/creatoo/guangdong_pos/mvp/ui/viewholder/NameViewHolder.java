package com.creatoo.guangdong_pos.mvp.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.creatoo.guangdong_pos.R;
import com.force.librarybase.base.adapter.BaseAdapter;
import com.force.librarybase.base.viewholder.BaseViewHolder;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.mvp.ui.viewholder
 * @Description:
 * @date 2018/4/13
 */
public class NameViewHolder extends BaseViewHolder<String>{
    TextView mNameTv;

    public NameViewHolder(Context context, View itemView, BaseAdapter adapter) {
        super(context, itemView, adapter);
    }

    @Override
    public void initView() {
        mNameTv = itemView.findViewById(R.id.tv_name);
    }

    @Override
    public void bindData(String s, int position) {
        mNameTv.setText(s);
    }
}
