package com.a7yan.news.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a7yan.news.R;
import com.a7yan.news.base.DetailBasePager;
import com.a7yan.news.domain.NewsCenterPagerBean;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 7Yan on 2017/1/25.
 */

public class NewsDetailPager extends DetailBasePager {
    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.indicator)
    TabPageIndicator indicator;
    private TextView textView;
    private List<NewsCenterPagerBean.NewsCenterPagerData.ChildrenData> children;
    //新闻详情页面的页签页面
    private List<TabDetailPager> tabDetailPagers;

    public NewsDetailPager(Context context, NewsCenterPagerBean.NewsCenterPagerData newsCenterPagerData) {
        super(context);
        children = newsCenterPagerData.getChildren();
    }

    @Override
    public View initView() {
        Log.d("NewsDetailPager", "新闻中心详情页面视图被初始化了");
        /*textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        return textView;*/
        View view = View.inflate(mContext, R.layout.news_detail_pager, null);
//        使用ButterKnife绑定XML文件
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.d("NewsDetailPager", "新闻中心详情页面数据被初始化了");
        /*textView.setText("新闻中心");*/
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            TabDetailPager tabDetailPager = new TabDetailPager(mContext, children.get(i));
            tabDetailPagers.add(tabDetailPager);
        }
        viewpage.setAdapter(new NewsDetailPagerAdapter());
        indicator.setViewPager(viewpage);
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
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
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
