package com.force.librarybase.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.force.librarybase.BaseFragment;


/**
 * @author Kim.H
 * @version v1.0
 * @ Package com.l99.lotto.adapter.pageradapter
 * @ Description:
 * @ date 2015-09-17 11:01
 */
public abstract class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    protected final SparseArray<BaseFragment> mFragments = new SparseArray<>();
    protected FragmentManager mFragmentManager;
    protected int mPageSize;

    public BaseFragmentPagerAdapter(FragmentManager fm, int pageSize) {
        super(fm);
        mFragmentManager = fm;
        mPageSize = pageSize;
    }

    @Override
    public int getCount() {
        return mPageSize;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public BaseFragment instantiateItem(ViewGroup container, int position) {

        String fragmentTag = getFragmentTagByPosition(position);
        BaseFragment cache = (BaseFragment) mFragmentManager.findFragmentByTag(fragmentTag);

        if (cache == null) {
            cache = generateFragmentByPosition(position);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(container.getId(), cache, fragmentTag).commit();
        }

        mFragments.put(position, cache);
        return cache;
        //return super.instantiateItem(container, position);
    }

    protected abstract String getFragmentTagByPosition(int position);

    protected abstract BaseFragment generateFragmentByPosition(int position);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        mFragmentManager.beginTransaction().detach((BaseFragment) object);
    }
}
