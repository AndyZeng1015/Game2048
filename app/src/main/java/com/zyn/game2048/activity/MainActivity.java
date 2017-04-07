package com.zyn.game2048.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyn.game2048.R;
import com.zyn.game2048.appwidget.GameView;
import com.zyn.game2048.base.BaseActivity;

import org.w3c.dom.Text;

public class MainActivity extends BaseActivity {

    private GameView gv_view;
    private TextView tv_score;
    private TextView tv_best;

    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        initView();
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

}
