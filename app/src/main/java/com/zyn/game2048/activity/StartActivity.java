package com.zyn.game2048.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zyn.game2048.R;
import com.zyn.game2048.base.BaseActivity;

/**
 * Desc:
 * CreateDate: 2017/4/7 23:37
 * Author: Created by ZengYinan
 * Email: 498338021@qq.com
 */

public class StartActivity extends BaseActivity {

    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(StartActivity.this, MainActivity.class, false);
            }
        });
    }

    private void initData() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_start);
        btn_start = (Button) findViewById(R.id.btn_start);
    }
}
