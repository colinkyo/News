package com.a7yan.news.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.a7yan.news.R;
import com.a7yan.news.base.DetailBasePager;
import com.a7yan.news.domain.NewsCenterPagerBean;
import com.a7yan.news.domain.TabDetailPagerBean;
import com.a7yan.news.utils.CacheUtils;
import com.a7yan.news.utils.Constants;
import com.a7yan.news.utils.DensityUtil;
import com.a7yan.news.view.HorizontalScrollViewPager;
import com.a7yan.news.view.RefreshListView;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by 7Yan on 2017/2/6.
 */

public class TabDetailPager extends DetailBasePager {

    //@BindView(R.id.listview)
    private RefreshListView listview;
    @BindView(R.id.viewpage)
    HorizontalScrollViewPager viewpage;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_point_group)
    LinearLayout ll_point_group;
    private NewsCenterPagerBean.NewsCenterPagerData.ChildrenData childrenData;
    private TextView textView;
    private String url;
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    /**
     * 上次选择的位置
     */
    private int preSelectPosition;
    private List<TabDetailPagerBean.DataBean.NewsBean> news;
    private TabDetailPagerListAdapter adapter;
    private String moreurl;
    private boolean isLoadMore = false;

    public TabDetailPager(Context context, NewsCenterPagerBean.NewsCenterPagerData.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;

    }

    @Override
    public View initView() {
        /*textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        return textView;*/
        View view = View.inflate(mContext, R.layout.tab_detail_pager, null);
        listview = (RefreshListView ) view.findViewById(R.id.listview);
        View topnewsView = View.inflate(mContext, R.layout.topnews, null);
//        使用ButterKnife绑定XML文件
        //ButterKnife.bind(this, view);
        ButterKnife.bind(this, topnewsView);
//        监听ViewPage页面的变化动态改变红点和标题
        viewpage.addOnPageChangeListener(new MyOnPageChangeListener());
//        把顶部新闻模块以头的方式加载到ListView中
//        listview.addHeaderView(topnewsView);
//        ListView自定义方法
        listview.addTopNews(topnewsView);
//        监听控件刷新
        listview.setOnRefreshListener(new MysetOnRefreshListener());
        return view;
    }
    class MysetOnRefreshListener implements RefreshListView.OnRefreshListener{

        @Override
        public void onPullDownlRefresh(){
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {
            if(TextUtils.isEmpty(moreurl))
            {
                Toast.makeText(mContext, "没有更多数据啦", Toast.LENGTH_SHORT).show();
                listview.onRefreshFinish(false);

            }else {
                getMoreDataFromNet();
            }
        }
    }

    private void getMoreDataFromNet() {
        OkHttpUtils
                .get()
                .url(moreurl)
                .id(100)
                .build()
                .execute(new MyMoreStringCallback());
    }
    class MyMoreStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            super.onBefore(request, id);
            Log.d("MyStringCallback", "开始联网。。。");
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            Log.d("MyStringCallback", "结束联网。。成功失败都会执行。");
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            super.inProgress(progress, total, id);
            Log.d("MyStringCallback", "联网中。。。");
        }

        @Override
        public void onError(Call call, Exception e, int i) {
            e.printStackTrace();
            listview.onRefreshFinish(false);
            Log.d("MyStringCallback", "联网失败:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d("MyStringCallback", "联网成功。。。" + response);
//            根据id,处理不同的联网请求
            switch (id) {
                case 100:
                    isLoadMore =true;
//                    保存数据
//                    CacheUtils.putString(mContext, url, response);
//                    解析数据,json格式
                    processData(response);
                    listview.onRefreshFinish(false);
                    break;
            }
        }
    }
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //放这里为了解决有时出现两个红点
            //把上次的点设置为灰色
            ll_point_group.getChildAt(preSelectPosition).setEnabled(false);
            //把当前的点设置为红色
            ll_point_group.getChildAt(position).setEnabled(true);
            preSelectPosition = position;
        }

        @Override
        public void onPageSelected(int position) {
           /* //把上次的点设置为灰色
            ll_point_group.getChildAt(preSelectPosition).setEnabled(false);
            //把当前的点设置为红色
            ll_point_group.getChildAt(position).setEnabled(true);
            preSelectPosition = position;*/

            //改变标题
            tv_title.setText(topnews.get(position).getTitle());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void initData() {
        super.initData();
        /*textView.setText(childrenData.getTitle());*/
        Log.d("TabDetailPager", Constants.BASE_URL + childrenData.getUrl());
        url = Constants.BASE_URL + childrenData.getUrl();
        String savejson = CacheUtils.getString(mContext, url);
        if (!TextUtils.isEmpty(savejson)) {
            processData(savejson);
        }
        getDataFromNet();
    }

    private void processData(String savejson) {
        TabDetailPagerBean bean = parsedJson(savejson);
        Log.d("TabDetailPager", bean.getData().getNews().get(2).getTitle());

        String more = bean.getData().getMore();
        if(TextUtils.isEmpty(more)){
            moreurl = "";
        }else{
            moreurl = Constants.BASE_URL+more;
        }
        if(!isLoadMore){
            //1.设置ViewPager的数据
            //得到顶部的数据
            topnews = bean.getData().getTopnews();
            viewpage.setAdapter(new TabDetailPagerAdapter());
            addPoint();
//        设置listview的适配器
            news = bean.getData().getNews();
            adapter = new TabDetailPagerListAdapter();
            listview.setAdapter(adapter);

        }else {
            news.addAll(bean.getData().getNews());
            adapter.notifyDataSetChanged();
            isLoadMore = false;
        }
    }

    class TabDetailPagerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_tab_detail_pager, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }
//            根据位置赋值
            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            //            获取图片地址
            String imageUrl = Constants.BASE_URL + newsBean.getListimage();
            Glide.with(mContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(viewHolder.iv_icon);
            viewHolder.tv_title.setText(newsBean.getTitle());
            viewHolder.tv_time.setText(newsBean.getPubdate());
            return convertView;
        }

    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView iv_icon;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_time)
        TextView tv_time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private void addPoint() {
        //2.根据有多少个页面设置多少个点
//        加载之前清空所有点
        ll_point_group.removeAllViews();

        for (int i = 0; i < topnews.size(); i++) {
            ImageView point = new ImageView(mContext);
            point.setImageResource(R.drawable.point_selector);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5));
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(mContext, 10);
            }
            if (i == 0) {
                point.setEnabled(true);
            } else {
                point.setEnabled(false);
            }
            point.setLayoutParams(params);
            //添加导航点
            ll_point_group.addView(point);
        }
        //默认标题
        tv_title.setText(topnews.get(preSelectPosition).getTitle());
    }

    class TabDetailPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.home_scroll_default);

//            获取图片地址
            String imageUrl = Constants.BASE_URL + topnews.get(position).getTopimage();
            Glide.with(mContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.home_scroll_default)
                    .error(R.drawable.home_scroll_default)
                    .into(imageView);
//            加入到ViewPage容器中去
            container.addView(imageView);

            Log.d("TabDetailPagerAdapter", imageUrl);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    private TabDetailPagerBean parsedJson(String savejson) {
        return JSON.parseObject(savejson, TabDetailPagerBean.class);
    }

    private void getDataFromNet() {
        OkHttpUtils
                .get()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            super.onBefore(request, id);
            Log.d("MyStringCallback", "开始联网。。。");
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            Log.d("MyStringCallback", "结束联网。。成功失败都会执行。");
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            super.inProgress(progress, total, id);
            Log.d("MyStringCallback", "联网中。。。");
        }

        @Override
        public void onError(Call call, Exception e, int i) {
            e.printStackTrace();
            listview.onRefreshFinish(false);
            Log.d("MyStringCallback", "联网失败:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            listview.onRefreshFinish(true);
            Log.d("MyStringCallback", "联网成功。。。" + response);
//            根据id,处理不同的联网请求
            switch (id) {
                case 100:
//                    保存数据
                    CacheUtils.putString(mContext, url, response);
//                    解析数据,json格式
                    processData(response);
                    break;
            }
        }
    }
}
