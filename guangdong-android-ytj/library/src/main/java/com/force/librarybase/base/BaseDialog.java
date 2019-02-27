package com.force.librarybase.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.force.librarybase.R;

/**
 * @author Hugh
 * @version v1.0
 * @Package com.daydao.caterers.fastfood.base
 * @Description:
 * @date 17:22
 */
public abstract class BaseDialog extends Dialog implements View.OnClickListener{
    public BaseDialog(@NonNull Context context) {
        super(context, R.style.dialog_theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateView();
        initWindow();
        bindWidget();
        initViews();
    }

    @Override
    public void onClick(View v) {

    }

    public abstract void bindWidget();

    public abstract void initViews();

    public abstract void initWindow();

    public abstract void onCreateView();
}
