package de.passsy.friendbattle.games;

import java.util.Timer;
import java.util.TimerTask;

import de.passsy.friendbattle.controllers.FirstGets;
import de.passsy.friendbattle.controllers.PointProvider;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

public class ClickWhenWhite extends MiniGame{
    
    private final Handler mHandler = new Handler();
    private Timer timer = new Timer();
    
    private final Runnable mFireTimer = new Runnable() {
        public void run() {
            Action();
        }
    };

    public ClickWhenWhite(Context context) {
	super(context);
    }
    
    public ClickWhenWhite(Context context, AttributeSet attrs) {
	super(context, attrs);
    }
    
    private void Action() {
	setBackgroundColor(Color.WHITE);
	setCorrectness(true);
    }

    @Override
    protected void showIntroductions(int seconds) {
	Log.v("tag", "introduction");
    }

    @Override
    public void startGame() {
	int time =  (int) Math.round((Math.random()*4000))+2000;
	
        timer.schedule(new TimerTask() {
    	    
            @Override
            public void run() {
                mHandler.post(mFireTimer);
            }
        }, time);
	
    }

    @Override
    public CharSequence getDescription() {
	return "Click if the color is white";
    }


}