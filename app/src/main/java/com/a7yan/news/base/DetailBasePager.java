package com.a7yan.news.base;

import android.content.Context;
import android.view.View;

/**
 * Created by 7Yan on 2017/1/25.
 * 作用：详情页面的基类
 * 新闻，专题，图组，互动等详情页面继承它
 */
public abstract class DetailBasePager {

    public Context mContext;

    /**
     * 代表不同的详情页面
     */
    public View rootView;

    public DetailBasePager(Context context) {
        this.mContext = context;
        /**
         * 初始化视图
         */
        rootView = initView();
    }

    public abstract View initView();

    /**
     * 当孩子需要请求数据，或者需要动态显示数据的时候重新该方法
     */
    public void initData() {

    }
}
