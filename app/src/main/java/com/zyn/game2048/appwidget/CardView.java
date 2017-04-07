package com.zyn.game2048.appwidget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zyn.game2048.R;
import com.zyn.game2048.util.DensityUtil;

/**
 * Desc: 卡片界面
 * CreateDate: 2017/4/7 11:26
 * Author: Created by ZengYinan
 * Email: 498338021@qq.com
 */

public class CardView extends FrameLayout {

    private Context mContext;
    private int num;
    private TextView tv_num;

    public CardView(Context context) {
        super(context);
        mContext = context;
        tv_num = new TextView(mContext);
        tv_num.setTextSize(32);
        tv_num.setTextColor(Color.WHITE);
        tv_num.setGravity(Gravity.CENTER);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(DensityUtil.dip2px(mContext, 5),DensityUtil.dip2px(mContext, 5),DensityUtil.dip2px(mContext, 5),DensityUtil.dip2px(mContext, 5));
        addView(tv_num, layoutParams);
    }

    public void setNum(int num) {
        this.num = num;
        if(this.num == 0){
            tv_num.setText("");
            tv_num.setBackgroundResource(R.drawable.card_bg_0);
        }else{
            tv_num.setText(this.num +"");
            switch (this.num){
                case 2:
                    tv_num.setBackgroundResource(R.drawable.card_bg_2);
                    break;
                case 4:
                    tv_num.setBackgroundResource(R.drawable.card_bg_4);
                    break;
                case 8:
                    tv_num.setBackgroundResource(R.drawable.card_bg_8);
                    break;
                case 16:
                    tv_num.setBackgroundResource(R.drawable.card_bg_16);
                    break;
                case 32:
                    tv_num.setBackgroundResource(R.drawable.card_bg_32);
                    break;
                case 64:
                    tv_num.setBackgroundResource(R.drawable.card_bg_64);
                    break;
                case 128:
                    tv_num.setBackgroundResource(R.drawable.card_bg_128);
                    break;
                case 256:
                    tv_num.setBackgroundResource(R.drawable.card_bg_256);
                    break;
                case 512:
                    tv_num.setBackgroundResource(R.drawable.card_bg_512);
                    break;
                case 1024:
                    tv_num.setBackgroundResource(R.drawable.card_bg_1024);
                    break;
                case 2048:
                    tv_num.setBackgroundResource(R.drawable.card_bg_2048);
                    break;
            }
        }
    }

    public int getNum() {
        return num;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getNum() == ((CardView)obj).getNum();
    }
}
