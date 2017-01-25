package com.a7yan.news.fragment;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.a7yan.news.MainActivity;
import com.a7yan.news.R;
import com.a7yan.news.base.BaseFragment;
import com.a7yan.news.base.BasePager;
import com.a7yan.news.pager.GovaffairPager;
import com.a7yan.news.pager.HomePager;
import com.a7yan.news.pager.NewsCenterPager;
import com.a7yan.news.pager.SettingPager;
import com.a7yan.news.pager.SmartServicePager;
import com.a7yan.news.utils.CacheUtils;
import com.a7yan.news.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 7Yan on 2017/1/24.
 */

public class ContentFragment extends BaseFragment {
    @BindView(R.id.viewpage)
    NoScrollViewPager viewpage;
//    private  NoScrollViewPager viewpage;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    private TextView tv;

//    加载5个页面
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        Log.d("ContentFragment", "我是主页面页面被初始化了");
       /* tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(DensityUtil.dip2px(mContext,20));
        return tv;*/
        View view = View.inflate(mContext, R.layout.content_frament, null);
        viewpage = (NoScrollViewPager) view.findViewById(R.id.viewpage);
//        使用ButterKnife绑定XML文件
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.d("ContentFragment", "我是主页面数据被初始化了");
//        tv.setText("我是主页面");
//        默认选中首页
        rgMain.check(R.id.rb_home);
//        设置RadioGroup状态的监听
        rgMain.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

//        设置数据源
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(mContext));
        basePagers.add(new NewsCenterPager(mContext));
        basePagers.add(new SmartServicePager(mContext));
        basePagers.add(new GovaffairPager(mContext));
        basePagers.add(new SettingPager(mContext));
//        创建适配器
//        绑定适配器
        viewpage.setAdapter(new ContentFragmentAdapter());
//        监听页面的改变
        viewpage.addOnPageChangeListener(new MyOnPageChangeListener());
//        默认加载第一个页面数据
        basePagers.get(0).initData();
//        默认左侧菜单不可以滑动
        isEnableSlidingMenu(false);

    }
//    得到新闻中心
    public NewsCenterPager getNewsCenterPager() {
            return (NewsCenterPager) basePagers.get(1);
    }

    class  MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position)
        {
//            哪个页面被选中就加载对应页面的数据
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_home:
//                    false禁止滑动效果
                    viewpage.setCurrentItem(0,false);
                    isEnableSlidingMenu(false);
                    break;
                case R.id.rb_newscenter:
                    viewpage.setCurrentItem(1,false);
                    isEnableSlidingMenu(true);
                    break;
                case R.id.rb_smartservice:
                    viewpage.setCurrentItem(2,false);
                    isEnableSlidingMenu(false);
                    break;
                case R.id.rb_govaffair:
                    viewpage.setCurrentItem(3,false);
                    isEnableSlidingMenu(false);
                    break;
                case R.id.rb_setting:
                    viewpage.setCurrentItem(4,false);
                    isEnableSlidingMenu(false);
                    break;
            }
        }
    }

    private void isEnableSlidingMenu(boolean isEnableSlidingMenu) {
        MainActivity mainActivity = (MainActivity) mContext;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if(isEnableSlidingMenu) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class ContentFragmentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            初始化视图
            BasePager basePager = basePagers.get(position);
            View view = basePager.rootView;
            container.addView(view);
//            初始化视图
//            初始化数据,可以屏蔽viewpage预加载数据 ***************重要**************
//            basePager.initData();
//            初始化数据
            return view;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

    }
}
