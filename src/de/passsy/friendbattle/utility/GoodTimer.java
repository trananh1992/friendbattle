package de.passsy.friendbattle.utility;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

public class GoodTimer {

    public interface OnTimerListener {
	public abstract void onTimer();
    }

    private OnTimerListener mTimerListener;

    private final Runnable timeoutRunnable = new Runnable() {

	@Override
	public void run() {
	    onTimer();

	}
    };

    private boolean mRepeat = false;

    private final int mDelay = 0;

    Timer mTimer = new Timer();

    private final Handler mHandler = new Handler();

    private final int mTimeout;

    private boolean mRunning;

    public GoodTimer(final int timeout, final boolean repeat) {

	mRepeat = repeat;
	mTimeout = timeout;
    }

    public void start() {

	if (mRunning) {
	    stop();
	}

	mTimer = new Timer();

	if (mTimeout <= 0) {
	    throw new IllegalArgumentException("timeout can't be 0 or below");
	}

	if (mRepeat) {

	    try {
		mTimer.schedule(new TimerTask() {

		    @Override
		    public void run() {
			mRunning = true;
			mHandler.post(timeoutRunnable);

		    }
		}, mTimeout, mTimeout);
	    } catch (final Exception e) {
		e.printStackTrace();
	    }
	} else {
	    try {
		mTimer.schedule(new TimerTask() {

		    @Override
		    public void run() {
			mRunning = true;
			mHandler.post(timeoutRunnable);
		    }
		}, mTimeout);
	    } catch (final Exception e) {
		e.printStackTrace();
	    }
	}

    }

    public void reset(final int timeout) {

	stop();
	start();
    }

    public void stop() {
	if (mRunning) {
	    mTimer.cancel();
	}
	mRunning = false;
    }

    /**
     * EXPERIMENTAL, NOT TESTED pause the timer.
     */
    public void pause() {
	if (mRunning) {
	    try {
		synchronized (mTimer) {
		    mTimer.wait();
		    mRunning = false;
		}
	    } catch (final InterruptedException e) {
		e.printStackTrace();
	    }
	}

    }

    /**
     * EXPERIMENTAL, NOT TESTED resumes the timer
     */
    public void resume() {
	if (!mRunning) {
	    mTimer.notify();
	    mRunning = true;
	}

    }

    public void onTimer() {
	if (mTimerListener != null) {
	    mTimerListener.onTimer();
	}
    }

    public void setOnTimerListener(final OnTimerListener l) {
	mTimerListener = l;
    }

    public boolean isRunning() {
	return mRunning;
    }

}
