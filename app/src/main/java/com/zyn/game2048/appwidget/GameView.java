package com.zyn.game2048.appwidget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zyn.game2048.R;
import com.zyn.game2048.activity.MainActivity;
import com.zyn.game2048.config.Config;
import com.zyn.game2048.util.DensityUtil;
import com.zyn.game2048.util.SharedPreferencesUtils;

import java.util.Random;

import static android.view.Gravity.CENTER_HORIZONTAL;

/**
 * Desc: 游戏界面
 * 算法说明（以向左滑动为例）：
 * 从左向右做循环，首先取到最左边的一个Card，与其后面的Card的数字一一做比较，
 * 当后面的数字小于0，跳过；
 * 当后面的数字大于0，判断当前位置的数字，
 * 如果当前的位置为0，则将当前的数字设置为后面的数字，并将后面的数字置为0，由于可能存在合并情况，所以在移动后将循环减1，之前位置再次进行查找；
 * 如果当前位置不为0，
 * 如果后面的数字和当前位置相等，将后面的数字加到当前位置，并将后面位置的值置为0，由于这里已经进行了合并，所以此处不需要进行减1操作。
 * CreateDate: 2017/4/7
 * Author: Created by ZengYinan
 * Email: 498338021@qq.com
 */

public class GameView extends GridLayout {

    private Context mContext;
    private CardView[][] mCardViews = new CardView[Config.LEVEL][Config.LEVEL];

    private Random mRandom;

    private boolean isInit = false;

    private boolean isLoadData = false;

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
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //抬起
                        float moveX = event.getX() - startX;
                        float moveY = event.getY() - startY;
                        if (Math.abs(moveX) > Math.abs(moveY)) {
                            //X轴上的滑动
                            if (moveX > 5) {
                                //向右滑动
//                                Toast.makeText(mContext, "向右滑动",Toast.LENGTH_SHORT).show();
                                slideRight();
                            } else if (moveX < -5) {
                                //向左滑动
//                                Toast.makeText(mContext, "向左滑动",Toast.LENGTH_SHORT).show();
                                slideLeft();
                            }
                        } else {
                            //Y轴上的滑动
                            if (moveY > 5) {
                                //向下滑动
//                                Toast.makeText(mContext, "向下滑动",Toast.LENGTH_SHORT).show();
                                slideDown();
                            } else if (moveY < -5) {
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
                for (int y1 = y + 1; y1 < Config.LEVEL; y1++) {
                    if (mCardViews[x][y1].getNum() > 0) {
                        if (mCardViews[x][y].getNum() == 0) {
                            mCardViews[x][y].setNum(mCardViews[x][y1].getNum());
                            mCardViews[x][y1].setNum(0);
                            y--;
                            isChanged = true;
                        } else {
                            if (mCardViews[x][y].getNum() == mCardViews[x][y1].getNum()) {
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
        if (isChanged) {
            if(isHasBlank()){
                addRandomCardView();
            }
            if(isEnd()){
                //显示结束窗口
                showEnd();
            }
            //有修改，存储数据
            saveCurrentGame();
            saveCurrentGameScore();
        }
    }

    //向下滑动
    private void slideDown() {
        boolean isChanged = false;
        for (int x = Config.LEVEL - 1; x >= 0; x--) {
            for (int y = Config.LEVEL - 1; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (mCardViews[x][y1].getNum() > 0) {
                        if (mCardViews[x][y].getNum() == 0) {
                            mCardViews[x][y].setNum(mCardViews[x][y1].getNum());
                            mCardViews[x][y1].setNum(0);
                            y++;
                            isChanged = true;
                        } else {
                            if (mCardViews[x][y].getNum() == mCardViews[x][y1].getNum()) {
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
        if (isChanged) {
            if(isHasBlank()){
                addRandomCardView();
            }
            if(isEnd()){
                //显示结束窗口
                showEnd();
            }
            //有修改，存储数据
            saveCurrentGame();
            saveCurrentGameScore();
        }
    }

    //向左滑动
    private void slideLeft() {
        boolean isChanged = false;
        for (int y = 0; y < Config.LEVEL; y++) {
            for (int x = 0; x < Config.LEVEL; x++) {
                for (int x1 = x + 1; x1 < Config.LEVEL; x1++) {
                    if (mCardViews[x1][y].getNum() > 0) {
                        if (mCardViews[x][y].getNum() == 0) {
                            mCardViews[x][y].setNum(mCardViews[x1][y].getNum());
                            mCardViews[x1][y].setNum(0);
                            x--;
                            isChanged = true;
                        } else {
                            if (mCardViews[x][y].getNum() == mCardViews[x1][y].getNum()) {
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
        if (isChanged) {
            if(isHasBlank()){
                addRandomCardView();
            }
            if(isEnd()){
                //显示结束窗口
                showEnd();
            }
            //有修改，存储数据
            saveCurrentGame();
            saveCurrentGameScore();
        }
    }

    //向右滑动
    private void slideRight() {
        boolean isChanged = false;
        for (int y = Config.LEVEL - 1; y >= 0; y--) {
            for (int x = Config.LEVEL - 1; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (mCardViews[x1][y].getNum() > 0) {
                        if (mCardViews[x][y].getNum() == 0) {
                            mCardViews[x][y].setNum(mCardViews[x1][y].getNum());
                            mCardViews[x1][y].setNum(0);
                            x++;
                            isChanged = true;
                        } else {
                            if (mCardViews[x][y].getNum() == mCardViews[x1][y].getNum()) {
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
        if (isChanged) {
            if(isHasBlank()){
                addRandomCardView();
            }
            if(isEnd()){
                //显示结束窗口
                showEnd();
            }
            //有修改，存储数据
            saveCurrentGame();
            saveCurrentGameScore();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!isInit) {
            int width = w - DensityUtil.dip2px(mContext, 5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
            params.gravity = CENTER_HORIZONTAL;
            params.topMargin = DensityUtil.dip2px(mContext, 10);
            this.setLayoutParams(params);
            addEmptyCardViews((width - (Config.LEVEL + 1) * (20 / Config.LEVEL)) / Config.LEVEL);
            isInit = true;
        }
    }

    //初始化界面的时候添加空的Card和两个随机位置的数字
    private void addEmptyCardViews(int width) {
        if(!isLoadData){
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
        }else{
            for (int y = 0; y < Config.LEVEL; y++) {
                for (int x = 0; x < Config.LEVEL; x++) {
                    this.addView(mCardViews[x][y], width, width);
                }
            }
        }
    }

    //在随机位置添加数字(使用while循环过滤已经有数字的Card)
    private void addRandomCardView() {
        int x = mRandom.nextInt(Config.LEVEL);
        int y = mRandom.nextInt(Config.LEVEL);
        while (mCardViews[x][y].getNum() != 0) {
            x = mRandom.nextInt(Config.LEVEL);
            y = mRandom.nextInt(Config.LEVEL);
        }
        mCardViews[x][y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    //清空所有位置的数字
    private void clearCardView() {
        for (int y = 0; y < Config.LEVEL; y++) {
            for (int x = 0; x < Config.LEVEL; x++) {
                mCardViews[x][y].setNum(0);
            }
        }
    }

    //判断是否结束游戏
    public boolean isEnd() {
        //当没有空位置和每个相邻的Card都不能合并的时候表示游戏结束
        boolean isEnd = true;
        for (int y = 0; y < Config.LEVEL; y++) {
            for (int x = 0; x < Config.LEVEL; x++) {
                if (mCardViews[x][y].getNum() == 0) {
                    //如果有一个空格表示游戏未结束
                    isEnd = false;
                    break;
                }
            }
            if (isEnd == false) {
                break;
            }
        }

        if (isEnd == true) {
            //这里表示格子已经被填满
            for (int y = 0; y < Config.LEVEL; y++) {
                for (int x = 0; x < Config.LEVEL; x++) {
                    if ((x == 0 && y == 0)) {
                        //表示左上角
                        if (mCardViews[x][y].getNum() == mCardViews[x + 1][y].getNum() || mCardViews[x][y].getNum() == mCardViews[x][y + 1].getNum()) {
                            isEnd = false;
                            break;
                        }
                    } else if (x == Config.LEVEL - 1 && y == 0) {
                        //表示右上角
                        if (mCardViews[x][y].getNum() == mCardViews[x - 1][y].getNum() || mCardViews[x][y].getNum() == mCardViews[x][y + 1].getNum()) {
                            isEnd = false;
                            break;
                        }
                    } else if (x == 0 && y == Config.LEVEL - 1) {
                        //表示左下角
                        if (mCardViews[x][y].getNum() == mCardViews[x][y - 1].getNum() || mCardViews[x][y].getNum() == mCardViews[x + 1][y].getNum()) {
                            isEnd = false;
                            break;
                        }
                    } else if (x == Config.LEVEL - 1 && y == Config.LEVEL - 1) {
                        //表示右下角
                        if (mCardViews[x][y].getNum() == mCardViews[x - 1][y].getNum() || mCardViews[x][y].getNum() == mCardViews[x][y - 1].getNum()) {
                            isEnd = false;
                            break;
                        }
                    } else if (x == 0 && y != 0 && y != Config.LEVEL - 1) {
                        //表示左边
                        if (mCardViews[x][y].getNum() == mCardViews[x][y - 1].getNum() || mCardViews[x][y].getNum() == mCardViews[x][y + 1].getNum() || mCardViews[x][y].getNum() == mCardViews[x + 1][y].getNum()) {
                            isEnd = false;
                            break;
                        }
                    } else if (y == 0 && x != 0 && x != Config.LEVEL - 1) {
                        //表示上边
                        if (mCardViews[x][y].getNum() == mCardViews[x - 1][y].getNum() || mCardViews[x][y].getNum() == mCardViews[x + 1][y].getNum() || mCardViews[x][y].getNum() == mCardViews[x][y + 1].getNum()) {
                            isEnd = false;
                            break;
                        }
                    } else if (x == Config.LEVEL - 1 && y != 0 && y != Config.LEVEL - 1) {
                        //表示右边
                        if (mCardViews[x][y].getNum() == mCardViews[x][y - 1].getNum() || mCardViews[x][y].getNum() == mCardViews[x][y + 1].getNum() || mCardViews[x][y].getNum() == mCardViews[x - 1][y].getNum()) {
                            isEnd = false;
                            break;
                        }
                    } else if (y == Config.LEVEL - 1 && x != 0 && x != Config.LEVEL - 1) {
                        //表示下边
                        if (mCardViews[x][y].getNum() == mCardViews[x - 1][y].getNum() || mCardViews[x][y].getNum() == mCardViews[x + 1][y].getNum() || mCardViews[x][y].getNum() == mCardViews[x][y - 1].getNum()) {
                            isEnd = false;
                            break;
                        }
                    }else{
                        //表示中间
                        if(mCardViews[x][y].getNum() == mCardViews[x-1][y].getNum() || mCardViews[x][y].getNum() == mCardViews[x+1][y].getNum() || mCardViews[x][y].getNum() == mCardViews[x][y-1].getNum() || mCardViews[x][y].getNum() == mCardViews[x][y+1].getNum()){
                            isEnd = false;
                            break;
                        }
                    }

                }
                if(isEnd == false){
                    break;
                }
            }
        }
        return isEnd;
    }

    //判断是否还有空位置
    public boolean isHasBlank(){
        boolean isHasBlank = false;
        for (int y = 0; y < Config.LEVEL; y++) {
            for (int x = 0; x < Config.LEVEL; x++) {
                if (mCardViews[x][y].getNum() == 0) {
                    //如果有一个空格表示游戏未结束
                    isHasBlank = true;
                    break;
                }
            }
            if (isHasBlank == true) {
                break;
            }
        }
        return isHasBlank;
    }

    //显示结束
    public void showEnd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View mView = View.inflate(mContext, R.layout.dialog_gameover, null);

        builder.setView(mView);
        builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //重新开始
                //开始新游戏，清空记录
                SharedPreferencesUtils.saveInt(mContext, "game_score", 0);
                SharedPreferencesUtils.saveString(mContext, "game_data", "");
                ((MainActivity)mContext).jump(((MainActivity)mContext), MainActivity.class, true);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = DensityUtil.dip2px(mContext, 330);
        p.height = DensityUtil.dip2px(mContext, 380);
        dialog.getWindow().setAttributes(p);     //设置生效

    }

    //加载上次游戏
    public void loadHistoryGame(String gameData){
        int index = 0;
        String[] data = gameData.split(",");
        for (int y = 0; y < Config.LEVEL; y++) {
            for (int x = 0; x < Config.LEVEL; x++) {
                if(mCardViews[x][y] == null){
                    CardView cardView = new CardView(mContext);
                    cardView.setNum(Integer.parseInt(data[index]));
                    mCardViews[x][y] = cardView;
                }else{
                    mCardViews[x][y].setNum(Integer.parseInt(data[index]));
                }
                index++;
            }
        }
        isLoadData = true;
    }

    //保存当前游戏记录.
    public void saveCurrentGame(){
        String gameData = "";
        for (int y = 0; y < Config.LEVEL; y++) {
            for (int x = 0; x < Config.LEVEL; x++) {
                gameData += mCardViews[x][y].getNum() + ",";
            }
        }
        gameData = gameData.substring(0, gameData.length() - 1);
        SharedPreferencesUtils.saveString(mContext, "game_data", gameData);
    }

    public void saveCurrentGameScore(){
        SharedPreferencesUtils.saveInt(mContext, "game_score", MainActivity.getInstance().getCurrentScore());
        SharedPreferencesUtils.saveInt(mContext, "game_best_score", MainActivity.getInstance().getBestScore());
    }
}
