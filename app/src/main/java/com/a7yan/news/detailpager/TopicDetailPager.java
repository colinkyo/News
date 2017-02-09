package com.a7yan.news.detailpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.a7yan.news.MainActivity;
import com.a7yan.news.R;
import com.a7yan.news.base.DetailBasePager;
import com.a7yan.news.domain.NewsCenterPagerBean;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 7Yan on 2017/1/25.
 */

public class TopicDetailPager extends DetailBasePager {

    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.indicator)
    TabPageIndicator indicator;
    @BindView(R.id.ib_next_tab)
    ImageButton ibNextTab;
    private TextView textView;
    private List<NewsCenterPagerBean.NewsCenterPagerData.ChildrenData> children;
    //新闻详情页面的页签页面
    private List<TopicTabDetailPager> tabDetailPagers;

    public TopicDetailPager(Context context, NewsCenterPagerBean.NewsCenterPagerData newsCenterPagerData) {
        super(context);
        children = newsCenterPagerData.getChildren();
    }

    @Override
    public View initView() {
        Log.d("NewsDetailPager", "专题中心详情页面视图被初始化了");
        /*textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        return textView;*/
        View view = View.inflate(mContext, R.layout.topic_detail_pager, null);
//        使用ButterKnife绑定XML文件
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.d("NewsDetailPager", "专题中心详情页面数据被初始化了");
        /*textView.setText("专题中心");*/
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            TopicTabDetailPager tabDetailPager = new TopicTabDetailPager(mContext, children.get(i));
            tabDetailPagers.add(tabDetailPager);
        }
        viewpage.setAdapter(new TopicDetailPager.NewsDetailPagerAdapter());
        //关联ViewPager
        indicator.setViewPager(viewpage);
        //以后监听页面-TabPageIndicator
        indicator.setOnPageChangeListener(new TopicDetailPager.MyOnPageChangeListener());
    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position==0){
                isEnableSlidingMenu(true);
            }else {
                isEnableSlidingMenu(false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    /**
     * 是否让SlidingMenu可以侧滑
     * @param isEnableSlidingMenu
     */
    private void isEnableSlidingMenu(boolean isEnableSlidingMenu) {
        MainActivity mainActivity = (MainActivity) mContext;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if(isEnableSlidingMenu){
            //可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            //不可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }
    @OnClick(R.id.ib_next_tab)
    public void onClick() {
        viewpage.setCurrentItem(viewpage.getCurrentItem()+1);
    }

    class NewsDetailPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TopicTabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
//            初始化数据，可以分离出来加载
            tabDetailPager.initData();
            return rootView;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
