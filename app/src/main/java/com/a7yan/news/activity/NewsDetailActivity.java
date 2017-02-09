package com.a7yan.news.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.a7yan.news.R;
import com.a7yan.news.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ib_menu)
    ImageButton ib_menu;
    @BindView(R.id.ib_back)
    ImageButton ib_back;
    @BindView(R.id.ib_textsize)
    ImageButton ib_textsize;
    @BindView(R.id.ib_share)
    ImageButton ib_share;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.pb_status)
    ProgressBar pb_status;
    private String url;
    private int temp = 2;
    private int textSize = temp;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
//        标题栏初始化
        ib_back.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.GONE);
        ib_textsize.setVisibility(View.VISIBLE);
        ib_share.setVisibility(View.VISIBLE);

        //获取启动Activity传递过来的参数
        url = getIntent().getStringExtra("url");
        url = Constants.BASE_URL + url;

//        url = "http://blog.csdn.net/maosidiaoxian/article/details/42671999";
//        webview加载网址

        webSettings = webview.getSettings();
        //设置支持javascript
        webSettings.setJavaScriptEnabled(true);
        //设置双击变大变小
        webSettings.setUseWideViewPort(true);

        //增加缩放按钮
        webSettings.setBuiltInZoomControls(true);

        //监听页面加载完成
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb_status.setVisibility(View.GONE);
            }
        });
        webview.loadUrl(url);
    }

    @OnClick({R.id.ib_back, R.id.ib_textsize, R.id.ib_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:
//                弹出对话框供选择
                showChangTextSizeDialog();
                break;
            case R.id.ib_share:
                break;
        }
    }

    private void showChangTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择字体大小");
        String[] items = new String[]{"超大字体", "大字体", "正常", "小字体", "超小字体"};
        builder.setSingleChoiceItems(items, textSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                temp = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textSize = temp;
                //改变字体大小
                changeTextSize();
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void changeTextSize() {
        switch (textSize) {
            case 0:
                webSettings.setTextZoom(200);
                break;
            case 1:
                webSettings.setTextZoom(150);
                break;
            case 2:
                webSettings.setTextZoom(100);
                break;
            case 3:
                webSettings.setTextZoom(75);
                break;
            case 4:
                webSettings.setTextZoom(50);
                break;
        }
    }
}
