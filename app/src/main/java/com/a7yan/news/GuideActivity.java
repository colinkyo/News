package com.a7yan.news;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Integer.*;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.viewpage)
    ViewPager viewpage;
    @BindView(R.id.ll_point_group)
    LinearLayout ll_point_group;
    @BindView(R.id.btnStarMain)
    Button btnStarMain;

    private ArrayList<ImageView> imageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        int [] ids = new int[] {R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};
//        设置数据源
        imageViews = new ArrayList<>();
        for(int i=0;i<ids.length;i++){
            ImageView imageView = new ImageView(GuideActivity.this);
//          设置背景
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);
//            动态创建点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.point_normal);
            ll_point_group.addView(point);
        }
//        设置适配器
        viewpage.setAdapter(new MyViewPageAdapter());
    }
    class MyViewPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         *
         * @param view 当前视图view
         * @param object 是instantiateItem返回的值
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
//           如果返回position
//            return  view ==imageViews.get(Integer.parseInt((String) object));
        }

        /**
         * viewPage 默认会创建2个,最多三个
         * @param container viewPage
         * @param position 哪个页面要销毁
         * @param object 要销毁的页面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);  必须注释掉
            container.removeView((View) object);
        }

        /**
         * 相当于ListView的适配器中的getView方法
         * 创建每条item
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
    public void onClick() {
    }
}
