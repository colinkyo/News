package com.a7yan.news;


import android.os.Bundle;

import com.a7yan.news.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        设置主页面
        setContentView(R.layout.activity_main);
//        设置左侧菜单
        setBehindContentView(R.layout.leftmenu);
//        设置右侧菜单
        SlidingMenu slidingMenu =getSlidingMenu();
        slidingMenu.setSecondaryMenu(R.layout.rightmenu);
//        设置滑动模式全屏，边缘，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        设置视图模式，左侧+主页，左侧+主页+右侧，主页+右侧
//        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        slidingMenu.setMode(SlidingMenu.LEFT);
//        设置主页面的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this,200));
    }
}
