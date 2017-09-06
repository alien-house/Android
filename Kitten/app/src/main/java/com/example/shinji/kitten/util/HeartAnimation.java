package com.example.shinji.kitten.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.airbnb.lottie.LottieAnimationView;

/**
 * Created by shinji on 2017/09/04.
 */

public class HeartAnimation extends LottieAnimationView  {

    public HeartAnimation(Context context) {
        super(context);
    }
    public HeartAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public HeartAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setOff(){
        this.setProgress(0f);
    }
    public void setOn(){
        this.playAnimation();
    }
    public void setState(boolean boo){
        if(boo){
            this.setProgress(1f);
        }else{
            this.setProgress(0f);
        }
    }
}
