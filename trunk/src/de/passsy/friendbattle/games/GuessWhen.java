package de.passsy.friendbattle.games;

import java.util.Timer;

import android.content.Context;
import android.os.Handler;
import de.passsy.friendbattle.controllers.CloseGets;
import de.passsy.friendbattle.screenlayouts.Screen_TextViewsCenter;
import de.passsy.friendbattle.utility.GoodTimer;
import de.passsy.friendbattle.utility.GoodTimer.OnTimerListener;

public class GuessWhen extends MiniGame {

    private final Screen_TextViewsCenter mTextViews;

    Timer mTimer = new Timer();

    private final Handler mHandler = new Handler();

    private GoodTimer timer;

    private int i = 0;
    private int time = (int) Math.round((Math.random() * 6)) + 5;
    private int max = time;

    private final OnTimerListener onTimerListener = new OnTimerListener() {

	@Override
	public void onTimer() {
	    calc();
	}
    };

    private void showStats() {

    }

    public GuessWhen(final Context context) {
	super(context);
	setPrepare(true);
	mTextViews = new Screen_TextViewsCenter(context);
	this.addView(mTextViews);

	mTextViews.setTextSize(20);
	mTextViews.setText("?");
	mCurrentPointprovider = new CloseGets(this);
    }

    @Override
    protected void showIntroductions(final int seconds) {

    }

    @Override
    public void startGame() {
	final int time = (int) Math.round((Math.random() * 1000)) + 500;
	((CloseGets) mCurrentPointprovider).setDelta(time);
	timer = new GoodTimer(time, true);
	timer.setOnTimerListener(onTimerListener);
	timer.start();
    }

    @Override
    public CharSequence getDescription() {
	return "Guess when it is";
    }

    private void calc() {
	time--;

	if (time > (max / 2) - 1) {
	    mTextViews.setText("" + time);
	} else {
	    mTextViews.setText("?");
	    if (getPrepare()) {
		setPrepare(false);
	    }
	}
	if (time == 0) {
	    setCorrectness(true);
	}
	if (time == -1) {
	    showStats();
	    timer.stop();
	}
    }

}
