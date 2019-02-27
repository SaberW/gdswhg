package com.force.librarybase.base.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.force.librarybase.base.adapter.BaseAdapter;
import com.force.librarybase.utils.Null;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * Created by kim on 2016/5/17.
 */
public class ViewHolderFactory {

    public static <V extends RecyclerView.ViewHolder> RecyclerView.ViewHolder generateViewHolder(Context context, Class<V> clazz, View itemView) {
        if (Null.isNull(clazz) || !BaseViewHolder.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("clazz can not be null and must be implement of OldViewHolder");
        }
        try {
            Constructor constructor = clazz.getDeclaredConstructor(Context.class, View.class, BaseAdapter.class);
            return (RecyclerView.ViewHolder) constructor.newInstance(context, itemView, null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <V extends RecyclerView.ViewHolder> RecyclerView.ViewHolder generateViewHolder(Context context, Class<V> clazz, View itemView, BaseAdapter<?> adapter) {
        if (Null.isNull(clazz) || !BaseViewHolder.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("clazz can not be null and must be implement of OldViewHolder");
        }
        try {
            Constructor constructor = clazz.getDeclaredConstructor(Context.class, View.class, BaseAdapter.class);
            return (RecyclerView.ViewHolder) constructor.newInstance(context, itemView, adapter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
