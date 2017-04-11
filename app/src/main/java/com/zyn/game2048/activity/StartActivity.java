package com.zyn.game2048.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zyn.game2048.R;
import com.zyn.game2048.base.BaseActivity;
import com.zyn.game2048.base.BaseApplication;
import com.zyn.game2048.util.SharedPreferencesUtils;
import com.zyn.game2048.util.ToastUtil;

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
        initListener();
    }

    private void initListener() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在这里读取存储，判断是否有历史记录
                final String gameData = SharedPreferencesUtils.getString(mContext, "game_data", "");
                final int gameScore = SharedPreferencesUtils.getInt(mContext, "game_score", 0);
                if(gameData.equals("")){
                    jump(StartActivity.this, MainActivity.class, false);
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Warning");
                    builder.setMessage("There are currently archived, whether to continue the game?");
                    builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //开始新游戏，清空记录
                            SharedPreferencesUtils.saveInt(mContext, "game_score", 0);
                            SharedPreferencesUtils.saveString(mContext, "game_data", "");
                            jump(StartActivity.this, MainActivity.class, false);
                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle bundle = new Bundle();
                            bundle.putString("game_data", gameData);
                            bundle.putInt("game_score", gameScore);
                            jump(StartActivity.this, MainActivity.class, "keys", bundle, false);
                        }
                    });
                    builder.show();
                }
            }
        });
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

    private long lastPressTime = 0;
    @Override
    public void onBackPressed() {
        long nowPressTime = System.currentTimeMillis();
        if(nowPressTime - lastPressTime > 2000){
            ToastUtil.showToast(mContext, "Press again to exit the game");
            lastPressTime = nowPressTime;
        }else{
            BaseApplication.getInstance().exit();
            super.onBackPressed();
            StartActivity.this.finish();
        }
    }
}
