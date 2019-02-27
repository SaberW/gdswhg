package com.force.librarybase;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.force.librarybase.command.Response;
import com.force.librarybase.command.Task;
import com.force.librarybase.utils.logger.Logger;

public abstract class BaseFragment extends Fragment {

    protected BaseApplication application;

    public String tag = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (BaseApplication) getActivity().getApplication();
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("Fragment tag:%s", tag);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 发送任务（处理耗时操作）
     *
     * @param task 任务实例 {@link Task}
     */
    protected final void doCommand(Task<Response> task) {
        application.sendTask(task);
    }

    protected boolean isVisible;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible(){
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible(){}
}
