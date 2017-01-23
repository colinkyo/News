package com.a7yan.news.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.a7yan.news.MainActivity;
import com.a7yan.news.R;
import com.a7yan.news.utils.CacheUtils;
import com.a7yan.news.utils.DensityUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.ll_point_group)
    LinearLayout ll_point_group;
    @BindView(R.id.btnStarMain)
    Button btnStarMain;
    @BindView(R.id.iv_red_point)
    ImageView ivRedPoint;

    private ArrayList<ImageView> imageViews;
    private int marginLeft;
    public  static  final String START_MAIN = "start_main";
//    分辨率适配
    public  int widthDpi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initData();
//        求间距
//        视图渲染--测量--指定位置--绘制
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new MyGlobalLayoutListener());
//        设置页面滑动的监听
        viewpage.addOnPageChangeListener(new MyOnPageChangeListener());
    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面滚动了的时候回调
         * @param position 当前哪个页面滚动了
         * @param positionOffset 页面滑动的百分比
         * @param positionOffsetPixels 页面滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            间距间的滑动距离 = 间距 * 屏幕滑动的百分比
//            float leftMargin =position*marginLeft+marginLeft * positionOffset;
            float leftMargin =(position+positionOffset)*marginLeft;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
            params.leftMargin = (int) leftMargin;
            ivRedPoint.setLayoutParams(params);
//            最终的坐标 = 起始 + 间距间的滑动距离

        }

        /**
         * 当某个页面被选中的时候回调
         * @param position
         */
        @Override
        public void onPageSelected(int position)
        {
            if(position == imageViews.size()-1)
            {
                btnStarMain.setVisibility(View.VISIBLE);

            }else{
                btnStarMain.setVisibility(View.GONE);
            }
        }

        /**
         * 页面滑动的状态改变的时候回调
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    class MyGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener
    {
        @Override
        public void onGlobalLayout()
        {
//            取消注册
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            间距 = 第1个点距离左边的距离 - 第0个点距离左边的距离
            marginLeft = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();

        }
    }
    private void initData()
    {
        //    分辨率适配 像素转
        widthDpi = DensityUtil.dip2px(this,10);
        int[] ids = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
//        设置数据源
        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(GuideActivity.this);
//          设置背景
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);
//            动态创建点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.point_normal);
//            像素
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthDpi, widthDpi);
            if (i != 0) {
                params.leftMargin = widthDpi;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }
//        设置适配器
        viewpage.setAdapter(new MyViewPageAdapter());
    }

    class MyViewPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         * @param view   当前视图view
         * @param object 是instantiateItem返回的值
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
//           如果返回position
//            return  view ==imageViews.get(Integer.parseInt((String) object));
        }

        /**
         * viewPage 默认会创建2个,最多三个
         *
         * @param container viewPage
         * @param position  哪个页面要销毁
         * @param object    要销毁的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);  必须注释掉
            container.removeView((View) object);
        }

        /**
         * 相当于ListView的适配器中的getView方法
         * 创建每条item
         *
         * @param container viewpage
         * @param position  页面的下标位置
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
//          放入viewPage容器
            container.addView(imageView);
            return imageView;
//          如果返会position
//            return position;
        }
    }

    @OnClick(R.id.btnStarMain)
    public void onClick(){
//        进入主页面
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
//        保存进入主页面信息
        CacheUtils.putBoolean(this,START_MAIN,true);
//        关闭引导页面
        finish();
    }
}
