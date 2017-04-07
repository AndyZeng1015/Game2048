package com.zyn.game2048.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.AppCompatImageView;

import com.zyn.game2048.R;
import com.zyn.game2048.base.BaseActivity;
import com.zyn.game2048.base.BaseApplication;

/**
 * Desc:
 * CreateDate: 2017/4/7 22:37
 * Author: Created by ZengYinan
 * Email: 498338021@qq.com
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        BaseApplication.getInstance().execRunnable(new Runnable() {
            @Override
            public void run() {
                //在这里加载设置
                SystemClock.sleep(2000);
                jump(SplashActivity.this, StartActivity.class, true);
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_splash);
    }
}
