package com.a7yan.news;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.a7yan.news.fragment.ContentFragment;
import com.a7yan.news.fragment.LeftmenuFragment;
import com.a7yan.news.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {


    public static final String LEFTMENU_TAG = "leftmenu_tag";
    public static final String MAIN_TAG = "main_tag";


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initSlidingMenu();
        initFragment();

    }

    private void initFragment()
    {
//        1.得到FragmentManager
        FragmentManager fm = getSupportFragmentManager();
//        2.开启事务
        FragmentTransaction ft = fm.beginTransaction();
//        3.替换
        ft.replace(R.id.fl_leftmenu,new LeftmenuFragment(), LEFTMENU_TAG);
        ft.replace(R.id.fl_content,new ContentFragment(), MAIN_TAG);
//        4,提交
        ft.commit();
    }

    private void initSlidingMenu() {
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
        slidingMenu.setFadeDegree(0.1f);
//        设置主页面的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this,200));
    }
//    得到左侧菜单
    public LeftmenuFragment getLeftmenuFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        LeftmenuFragment leftmenuFragment = (LeftmenuFragment)fm.findFragmentByTag(LEFTMENU_TAG);
        return leftmenuFragment;
    }
//    得到内容容器
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment contentFragment = (ContentFragment)fm.findFragmentByTag(MAIN_TAG);
        return contentFragment;
    }
}
