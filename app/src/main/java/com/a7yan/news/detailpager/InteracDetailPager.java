package com.a7yan.news.detailpager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.a7yan.news.base.DetailBasePager;

/**
 * Created by 7Yan on 2017/1/25.
 */

public class InteracDetailPager extends DetailBasePager {
    private TextView textView;

    public InteracDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView()
    {
        Log.d("NewsDetailPager", "互动中心详情页面视图被初始化了");
        textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        Log.d("NewsDetailPager", "互动中心详情页面数据被初始化了");
        textView.setText("互动中心");
    }
}
