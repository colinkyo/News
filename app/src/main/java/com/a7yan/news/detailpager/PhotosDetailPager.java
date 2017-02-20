package com.a7yan.news.detailpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.a7yan.news.R;
import com.a7yan.news.base.DetailBasePager;
import com.a7yan.news.domain.PhotosDetailPagerBean;
import com.a7yan.news.pager.NewsCenterPager;
import com.a7yan.news.utils.BitmapUtils;
import com.a7yan.news.utils.CacheUtils;
import com.a7yan.news.utils.Constants;
import com.a7yan.news.utils.NetCacheUtils;
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
 * Created by 7Yan on 2017/1/25.
 */

public class PhotosDetailPager extends DetailBasePager {
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.gridview)
    GridView gridview;
    private List<PhotosDetailPagerBean.DataBean.NewsBean> news;
    private PhotosDetailPagerAdapter adapter;
    private BitmapUtils bitmapUtils ;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NetCacheUtils.SUCCESS://请求图片成功
                    Bitmap bitmap = (Bitmap) msg.obj;
                    int position = msg.arg1;
                    Log.e("TAG","图片请求成功了=="+position);
                    if(listview != null && listview.isShown()){
                        ImageView imageView = (ImageView) listview.findViewWithTag(position);
                        if(imageView != null && bitmap!= null){
                            imageView.setImageBitmap(bitmap);
                        }
                    }

                    if(gridview != null && gridview.isShown()){
                        ImageView imageView = (ImageView) gridview.findViewWithTag(position);
                        if(imageView != null && bitmap!= null){
                            imageView.setImageBitmap(bitmap);
                        }
                    }

                    break;
                case NetCacheUtils.FAIL://请求图片失败
                    position = msg.arg1;
                    Log.e("TAG","图片请求失败了=="+position);
                    break;
            }
        }
    };
    public PhotosDetailPager(Context context) {
        super(context);
        bitmapUtils = new BitmapUtils(handler);
    }

    @Override
    public View initView() {
        Log.d("PhotosDetailPager", "图组详情页面的视图被初始化了");
        View view = View.inflate(mContext, R.layout.photos_detail_pager, null);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.d("PhotosDetailPager", "图组详情页面数据被初始化了");
        String savejson = CacheUtils.getString(mContext,Constants.PHOTOS_URL);
        if(!TextUtils.isEmpty(savejson)){
            processData(savejson);
        }
        getDataFromNet();
    }
    private void getDataFromNet() {
        OkHttpUtils
                .get()
                .url(Constants.PHOTOS_URL)
                .id(100)
                .build()
                .execute(new PhotosDetailPager.MyStringCallback());
//        Toast.makeText(mContext, Constants.API_URL, Toast.LENGTH_SHORT).show();
    }
    /**
     * true:显示ListView,隐藏GridView
     * false显示GridView,隐藏ListView
     */
    private boolean isShowListView = true;
    public void swichListAndGrid(ImageButton ib_swich_list_grid)
    {
        if(isShowListView){
            //GridView显示和ListView隐藏
            gridview.setVisibility(View.VISIBLE);
            gridview.setAdapter(new PhotosDetailPagerAdapter());
            listview.setVisibility(View.GONE);

            //显示ListView的效果
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_list_type);
            isShowListView = false;

        }else{

            //Listview显示和GridView隐藏
            listview.setVisibility(View.VISIBLE);
            listview.setAdapter(new PhotosDetailPagerAdapter());
            gridview.setVisibility(View.GONE);

            //显示gridView的按钮效果
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
            isShowListView = true;
        }
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
            Log.d("MyStringCallback", "联网失败:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.d("MyStringCallback", "联网成功。。。" + response);
//            根据id,处理不同的联网请求
            switch (id) {
                case 100:
//                    保存数据
                    CacheUtils.putString(mContext,Constants.PHOTOS_URL,response);
//                    解析数据,json格式
                    processData(response);
                    break;
            }
        }
    }

    private void processData(String response) {
        PhotosDetailPagerBean bean = parsedJson(response);
//        Toast.makeText(mContext, bean.getData().getNews().get(0).getTitle(), Toast.LENGTH_SHORT).show();
//        准备数据源
        news = bean.getData().getNews();
        //设置适配器
        adapter = new PhotosDetailPagerAdapter();
//        listview绑定适配器
        listview.setAdapter(adapter);
    }
    class PhotosDetailPagerAdapter extends BaseAdapter{
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
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if(view==null){
                view = View.inflate(mContext,R.layout.item_photos_detail_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.iv_title = (TextView) view.findViewById(R.id.tv_title);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            PhotosDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            viewHolder.iv_title.setText(newsBean.getTitle());
            String imgUrl = newsBean.getLargeimage();
//            尤其没有对应的地址，所以统一用固定的地址
            imgUrl = Constants.BASE_URL+"/static/images/2014/02/11/59/1861510091Q6VY.jpg";
//            使用Glide加载网络图片
           /* Glide.with(mContext)
                    .load(imgUrl)
                    .placeholder(R.drawable.home_scroll_default)
                    .error(R.drawable.home_scroll_default)
                    .into(viewHolder.iv_icon);*/
            //2.使用自己定义三级缓存请求网络图片
            //设置tag,handler异步返回时，根据它找到控件
            viewHolder.iv_icon.setTag(position);
            Bitmap bitmap = bitmapUtils.getBitmap(imgUrl,position);//主线程
            if(bitmap != null){
                //图片来自，内存或者本地
                viewHolder.iv_icon.setImageBitmap(bitmap);
            }
            return view;
        }
    }
    static class ViewHolder{
        public ImageView iv_icon;
        public TextView iv_title;

    }
    private PhotosDetailPagerBean parsedJson(String savejson) {
        return JSON.parseObject(savejson, PhotosDetailPagerBean.class);
    }
}
