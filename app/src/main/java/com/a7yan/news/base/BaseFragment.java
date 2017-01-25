package com.a7yan.news.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 7Yan on 2017/1/24.
 * LeftmenuFragment和ContentFragment都要继承它
 */

public abstract class BaseFragment extends Fragment
{
//    获取上下文
    public Context mContext;
    /**
     * 当该类被创建的时候被回调
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity(); //MainActivity
    }

    /**
     * 当创建视图的时候回调这个方法
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }
    /**
     * 让子类实现该方法，写自己特有的布局
     * @return
     */
    public abstract View initView();

    /**
     * 当Activity实例化好的时候回调这个方法
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    /**
     * 当孩子需要初始化数据，或者联网请求数据的时候，重新该方法。
     */
    public void initData()
    {

    }
}
