package com.a7yan.news.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.a7yan.news.base.BasePager;

/**
 * Created by 7Yan on 2017/1/24.
 */

public class HomePager extends BasePager {

    private TextView textView;

    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        Log.d("HomePager", "主页数据被初始化了....");
        tv_title.setText("主页面");
        textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setText("主页面内容");
//        把内容添加到fl_content去
        fl_content.addView(textView);
    }
}
