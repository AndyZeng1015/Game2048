package com.zyn.game2048.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.zyn.game2048.R;
import com.zyn.game2048.appwidget.GameView;
import com.zyn.game2048.base.BaseActivity;
import com.zyn.game2048.util.SharedPreferencesUtils;

public class MainActivity extends BaseActivity {

    private GameView gv_view;
    private TextView tv_score;
    private TextView tv_best;
    private String gameData;
    private int gameScore;

    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        initView();
        initData();
    }

    private void initData() {
        tv_best.setText(loadBestScore()+"");
        if(getIntent().getBundleExtra("keys") != null){
            gameData = getIntent().getBundleExtra("keys").getString("game_data");
            gameScore = getIntent().getBundleExtra("keys").getInt("game_score");
            if(!gameData.isEmpty()){
                //表示继续游戏
                gv_view.loadHistoryGame(gameData);
                tv_score.setText(gameScore+"");
            }
        }
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        gv_view = (GameView) findViewById(R.id.gv_view);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_best = (TextView) findViewById(R.id.tv_best);
    }

    public static MainActivity getInstance(){
        return instance;
    }

    //清除分数
    public void clearScore(){
        tv_score.setText("0");
    }

    //设置分数
    public void setScore(int score){
        tv_score.setText(score+"");
    }

    //添加分数
    public void addScore(int addScore){
        tv_score.setText(Integer.parseInt(tv_score.getText().toString())+addScore+"");
        //最高分设置
        exceedBest();
    }

    //获取当前分数
    public int getCurrentScore(){
        return Integer.parseInt(tv_score.getText().toString());
    }

    //设置最高分
    public void setBestScore(int bestScore){
        tv_best.setText(bestScore+"");
    }

    //获取最高分
    public int getBestScore(){
        return Integer.parseInt(tv_best.getText().toString());
    }

    //获取保存的最高分
    public int loadBestScore(){
        return SharedPreferencesUtils.getInt(mContext, "game_best_score", 0);
    }

    //判断是当前游戏的最高分是否大于保存的最高分
    public void exceedBest(){
        if(Integer.parseInt(tv_score.getText().toString()) > Integer.parseInt(tv_best.getText().toString())){
            //当前分数大于最高分
            tv_best.setText(tv_score.getText().toString());
        }
    }
}
