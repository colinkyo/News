package com.a7yan.news.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.a7yan.news.MainActivity;
import com.a7yan.news.R;
import com.a7yan.news.base.BaseFragment;
import com.a7yan.news.base.BasePager;
import com.a7yan.news.domain.NewsCenterPagerBean;
import com.a7yan.news.pager.NewsCenterPager;
import com.a7yan.news.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.BuildConfig;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.List;

/**
 * Created by 7Yan on 2017/1/24.
 */

public class LeftmenuFragment extends BaseFragment
{
    private TextView tv;
    private List<NewsCenterPagerBean.NewsCenterPagerData> data;
    private ListView listView;
    private int selectPosition;
    private MyLeftmenuFragmentAdapter adapter;

    @Override
    public View initView()
    {
        Log.d("LeftmenuFragment", "左侧菜单页面被初始化了");
       /* tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(DensityUtil.dip2px(mContext,20));
        return tv;*/
//        创建左侧菜单视图
        listView = new ListView(mContext);
//        屏蔽底版整个listView变灰
        listView.setCacheColorHint(Color.TRANSPARENT);
//        去掉分割线
        listView.setDividerHeight(0);
//        距离顶部40PX
        listView.setPadding(0,DensityUtil.dip2px(mContext,40),0,0);
//        屏蔽按下每条变色的效果
//        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setSelector(android.R.color.transparent);
//        设置点击事件监听
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return listView;
    }
    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            /*1.点击的要高亮显示
            记录被点击的位置，适配器刷新
            在getview这里判断谁被点击了，就设置谁为高亮*/
            selectPosition = position;
            adapter.notifyDataSetChanged();//getCount()-->getView();
//            2.把左侧菜单关闭
            MainActivity mainActivity = (MainActivity) mContext;
            SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
            slidingMenu.toggle();
//            3.切换到对应的详情页面，新闻，专题，图组，互动等详情页面
            setSwichePager(position);
        }
    }
    /**
     * 切换到不同的页面
     * @param position
     */
    private void setSwichePager(int position)
    {
        MainActivity mainActivity2 = (MainActivity) mContext;
        ContentFragment contentFragment = mainActivity2.getContentFragment();
//            得到新闻中心
        NewsCenterPager newsCenterPager=contentFragment.getNewsCenterPager();
//            切换到对应的详情页面
        newsCenterPager.swichPager(position);
    }

    @Override
    public void initData() {
        super.initData();
        Log.d("LeftmenuFragment", "左侧菜单数据被初始化了...");
        //tv.setText("我是左侧菜单");
    }

    public void setData(List<NewsCenterPagerBean.NewsCenterPagerData> data)
    {
        this.data =data;
        /*for (int i=0;i<data.size();i++){
            Log.d("LeftmenuFragment", "Title====" + data.get(i).getTitle());
        }*/
//        设置适配器
        adapter = new MyLeftmenuFragmentAdapter();
        listView.setAdapter(adapter);
//        设置默认新闻中心的详细页面
        setSwichePager(0);
    }

    class MyLeftmenuFragmentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            TextView textView = (TextView) View.inflate(mContext,R.layout.item_leftmenu,null);
            textView.setText(data.get(position).getTitle());
//           在listview 的item单击事件中，重新绘制视图
            if(selectPosition==position)
            {
                textView.setEnabled(true);

            }else{
                textView.setEnabled(false);
            }
            return textView;
        }
    }

}

