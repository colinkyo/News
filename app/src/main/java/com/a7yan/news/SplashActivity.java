package com.a7yan.news;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.rl_splash_root)
    RelativeLayout rl_splash_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

//        设置旋转，渐变，缩放的动画
        RotateAnimation ra = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
//        动画时长
//        ra.setDuration(500);
//        设置停留在播放后的状态
        ra.setFillAfter(true);

        AlphaAnimation aa = new AlphaAnimation(0,1);
//        aa.setDuration(500);
        aa.setFillAfter(true);

        ScaleAnimation sa = new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
//        sa.setDuration(500);
        sa.setFillAfter(true);

//        一起播放 false为不设置动画函数
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(ra);
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.setDuration(1500);

        rl_splash_root.setAnimation(set);
//        监听动画播放完成
        set.setAnimationListener(new MyAnimationListener());
    }
    class MyAnimationListener implements Animation.AnimationListener{
//      动画开始播放
        @Override
        public void onAnimationStart(Animation animation) {

        }
//      动画播放完成
        @Override
        public void onAnimationEnd(Animation animation) {
            Toast.makeText(SplashActivity.this, "动画播放完成", Toast.LENGTH_SHORT).show();
        }
//      动画重复播放
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
