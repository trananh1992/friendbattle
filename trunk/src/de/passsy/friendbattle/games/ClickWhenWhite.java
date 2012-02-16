package de.passsy.friendbattle.games;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;

public class ClickWhenWhite extends MiniGame {

    private final Handler mHandler = new Handler();
    private final Timer timer = new Timer();

    private final Runnable mFireTimer = new Runnable() {
	@Override
	public void run() {
	    Action();
	}
    };

    public ClickWhenWhite(final Context context) {
	super(context);
    }

    public ClickWhenWhite(final Context context, final AttributeSet attrs) {
	super(context, attrs);
    }

    private void Action() {
	setBackgroundColor(Color.WHITE);
	setCorrectness(true);
    }

    @Override
    protected void showIntroductions(final int seconds) {
	// Log.v("tag", "introduction");
    }

    @Override
    public void startGame() {
	final int time = (int) Math.round((Math.random() * 4000)) + 2000;

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

    @Override
    public void stopGame() {

    }

}
