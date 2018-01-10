package com.famgy.android.famgyguanjia;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by famgy on 1/9/18.
 */

public class LoadingActivity extends Activity {

    AlphaAnimation alphaAnimation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.loading_anim);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                startMainView(LoadingActivity.this);
            }
        });

        TextView rlSplashLayout = (TextView) findViewById(R.id.tv_register_state);
        rlSplashLayout.startAnimation(alphaAnimation);
    }

    private void startMainView(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
