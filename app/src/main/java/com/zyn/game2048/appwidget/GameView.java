package com.zyn.game2048.appwidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zyn.game2048.activity.MainActivity;
import com.zyn.game2048.config.Config;
import com.zyn.game2048.util.DensityUtil;

import java.util.Random;

/**
 * Desc: 游戏界面
 * 算法说明（以向左滑动为例）：
 *  从左向右做循环，首先取到最左边的一个Card，与其后面的Card的数字一一做比较，
 *  当后面的数字小于0，跳过；
 *  当后面的数字大于0，判断当前位置的数字，
 *  如果当前的位置为0，则将当前的数字设置为后面的数字，并将后面的数字置为0，由于可能存在合并情况，所以在移动后将循环减1，之前位置再次进行查找；
 *  如果当前位置不为0，
 *  如果后面的数字和当前位置相等，将后面的数字加到当前位置，并将后面位置的值置为0，由于这里已经进行了合并，所以此处不需要进行减1操作。
 * CreateDate: 2017/4/7
 * Author: Created by ZengYinan
 * Email: 498338021@qq.com
 */

public class GameView extends GridLayout {

    private Context mContext;
    private CardView[][] mCardViews = new CardView[Config.LEVEL][Config.LEVEL];

    private Random mRandom;

    private boolean isInit = false;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initGameView();
    }

    //初始化游戏界面
    private void initGameView() {
        mRandom = new Random();
        this.setBackgroundColor(Color.parseColor("#57407C"));
        this.setColumnCount(Config.LEVEL);

        //设置手势事件
        this.setOnTouchListener(new OnTouchListener() {

            private float startX = 0;//按下的x轴
            private float startY = 0;//按下的y轴

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //抬起
                        float moveX = event.getX() - startX;
                        float moveY = event.getY() - startY;
                        if(Math.abs(moveX) > Math.abs(moveY)){
                            //X轴上的滑动
                            if(moveX > 5){
                                //向右滑动
//                                Toast.makeText(mContext, "向右滑动",Toast.LENGTH_SHORT).show();
                                slideRight();
                            }else if(moveX < -5){
                                //向左滑动
//                                Toast.makeText(mContext, "向左滑动",Toast.LENGTH_SHORT).show();
                                slideLeft();
                            }
                        }else{
                            //Y轴上的滑动
                            if(moveY > 5){
                                //向下滑动
//                                Toast.makeText(mContext, "向下滑动",Toast.LENGTH_SHORT).show();
                                slideDown();
                            }else if(moveY < -5){
                                //向上滑动
//                                Toast.makeText(mContext, "向上滑动",Toast.LENGTH_SHORT).show();
                                slideUp();
                            }
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    //向上滑动
    private void slideUp() {
        boolean isChanged = false;
        for (int x = 0; x < Config.LEVEL; x++) {
            for (int y = 0; y < Config.LEVEL; y++) {
                for (int y1 = y+1; y1 < Config.LEVEL; y1++) {
                    if(mCardViews[x][y1].getNum() > 0){
                        if(mCardViews[x][y].getNum() == 0){
                            mCardViews[x][y].setNum(mCardViews[x][y1].getNum());
                            mCardViews[x][y1].setNum(0);
                            y--;
                            isChanged = true;
                        }else{
                            if(mCardViews[x][y].getNum() == mCardViews[x][y1].getNum()){
                                mCardViews[x][y].setNum(mCardViews[x][y].getNum() * 2);
                                mCardViews[x][y1].setNum(0);
                                MainActivity.getInstance().addScore(mCardViews[x][y].getNum());
                                isChanged = true;
                            }
                        }
                        break;
                    }
                }
            }
        }
        if(isChanged){
            addRandomCardView();
        }
    }

    //向下滑动
    private void slideDown() {
        boolean isChanged = false;
        for (int x = Config.LEVEL-1; x >= 0; x--) {
            for (int y = Config.LEVEL-1; y >= 0; y--) {
                for(int y1 = y-1; y1 >= 0; y1--){
                    if(mCardViews[x][y1].getNum() > 0){
                        if(mCardViews[x][y].getNum() == 0){
                            mCardViews[x][y].setNum(mCardViews[x][y1].getNum());
                            mCardViews[x][y1].setNum(0);
                            y++;
                            isChanged = true;
                        }else{
                            if(mCardViews[x][y].getNum() == mCardViews[x][y1].getNum()){
                                mCardViews[x][y].setNum(mCardViews[x][y].getNum() * 2);
                                mCardViews[x][y1].setNum(0);
                                MainActivity.getInstance().addScore(mCardViews[x][y].getNum());
                                isChanged = true;
                            }
                        }
                        break;
                    }
                }
            }
        }
        if(isChanged){
            addRandomCardView();
        }
    }

    //向左滑动
    private void slideLeft() {
        boolean isChanged = false;
        for (int y = 0; y < Config.LEVEL; y++) {
            for (int x = 0; x < Config.LEVEL; x++) {
                for (int x1 = x+1; x1 < Config.LEVEL; x1++) {
                    if(mCardViews[x1][y].getNum() > 0){
                        if(mCardViews[x][y].getNum() == 0){
                            mCardViews[x][y].setNum(mCardViews[x1][y].getNum());
                            mCardViews[x1][y].setNum(0);
                            x--;
                            isChanged = true;
                        }else{
                            if(mCardViews[x][y].getNum() == mCardViews[x1][y].getNum()){
                                mCardViews[x][y].setNum(mCardViews[x][y].getNum() * 2);
                                mCardViews[x1][y].setNum(0);
                                MainActivity.getInstance().addScore(mCardViews[x][y].getNum());
                                isChanged = true;
                            }
                        }
                        break;
                    }
                }
            }
        }
        if(isChanged){
            addRandomCardView();
        }
    }

    //向右滑动
    private void slideRight() {
        boolean isChanged = false;
        for (int y = Config.LEVEL-1; y >= 0; y--) {
            for(int x = Config.LEVEL-1; x >= 0; x--){
                for(int x1 = x -1; x1>= 0; x1--){
                    if(mCardViews[x1][y].getNum() > 0){
                        if(mCardViews[x][y].getNum() == 0){
                            mCardViews[x][y].setNum(mCardViews[x1][y].getNum());
                            mCardViews[x1][y].setNum(0);
                            x++;
                            isChanged = true;
                        }else{
                            if(mCardViews[x][y].getNum() == mCardViews[x1][y].getNum()){
                                mCardViews[x][y].setNum(mCardViews[x][y].getNum() * 2);
                                mCardViews[x1][y].setNum(0);
                                MainActivity.getInstance().addScore(mCardViews[x][y].getNum());
                                isChanged = true;
                            }
                        }
                        break;
                    }
                }
            }
        }
        if(isChanged){
            addRandomCardView();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if(!isInit){
            int width = w-DensityUtil.dip2px(mContext, 5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.topMargin = DensityUtil.dip2px(mContext, 10);
            this.setLayoutParams(params);
            addEmptyCardViews((width - (Config.LEVEL+1) * (20/Config.LEVEL))/Config.LEVEL);
            isInit = true;
        }
    }

    //初始化界面的时候添加空的Card和两个随机位置的数字
    private void addEmptyCardViews(int width){
        for (int y = 0; y < Config.LEVEL; y++) {
            for (int x = 0; x < Config.LEVEL; x++) {
                CardView cardView = new CardView(mContext);
                cardView.setNum(0);
                this.addView(cardView, width, width);
                mCardViews[x][y] = cardView;
            }
        }
        for (int i = 0; i < 2; i++) {
            addRandomCardView();
        }
    }

    //在随机位置添加数字(使用while循环过滤已经有数字的Card)
    private void addRandomCardView(){
        int x = mRandom.nextInt(Config.LEVEL);
        int y = mRandom.nextInt(Config.LEVEL);
        while (mCardViews[x][y].getNum() != 0){
            x = mRandom.nextInt(Config.LEVEL);
            y = mRandom.nextInt(Config.LEVEL);
        }
        mCardViews[x][y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    //清空所有位置的数字
    private void clearCardView(){
        for (int y = 0; y < Config.LEVEL; y++) {
            for (int x = 0; x < Config.LEVEL; x++) {
                mCardViews[x][y].setNum(0);
            }
        }
    }
}
