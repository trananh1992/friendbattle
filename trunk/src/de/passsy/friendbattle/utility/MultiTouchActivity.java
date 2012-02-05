package de.passsy.friendbattle.utility;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MultiTouchActivity extends Activity implements OnTouchListener {

    private View parent;

    /******************************************************************************
     * Called when the activity starts - initialise the views
     ******************************************************************************/
    public void onCreate(Bundle instance) {
	super.onCreate(instance);
	// hide titlebar
	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// run in fullscreenmode
	this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	this.getWindow().clearFlags(
		WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	parent = findViewById(android.R.id.content).getRootView();
	parent.setOnTouchListener(this);
    }

    /******************************************************************************
     * OnTouch event handler
     ******************************************************************************/
    public boolean onTouch(View v, MotionEvent event) {

	for (int ptrIndex = 0; ptrIndex < event.getPointerCount(); ptrIndex++) {

	    // index of the pointer which starts this Event
	    int actionPointerIndex = event.getActionIndex();

	    // resolve the action as a basic type (up, down or move)
	    int actionResolved = event.getAction() & MotionEvent.ACTION_MASK;
	    if (actionResolved < 7 && actionResolved > 4) {
		actionResolved = actionResolved - 5;
	    }

	    if (actionResolved == MotionEvent.ACTION_MOVE) {

		dealEvent(ptrIndex, event, v, actionResolved);
		Log.v("tag", "move" + ptrIndex);

	    } else {
		dealEvent(actionPointerIndex, event, v, actionResolved);
	    }
	}
	return true;
    }

    private void dealEvent(int actionPointerIndex, MotionEvent event,
	    View eventView, int actionresolved) {
	int rawX, rawY;
	int location[] = { 0, 0 };
	eventView.getLocationOnScreen(location);
	// Log.v("tag", location + "");
	rawX = (int) event.getX(actionPointerIndex) + location[0];
	rawY = (int) event.getY(actionPointerIndex) + location[1];

	ArrayList<View> views = getTouchedViews(rawX, rawY, actionresolved);

	// dumpEvent(event);
	for (View view : views) {
	    int x, y;
	    view.getLocationOnScreen(location);
	    x = rawX - location[0];
	    y = rawY - location[1];
	    // Log.v("tag", "touched" + view.toString());
	    /*
	     * view.onTouchEvent(MotionEvent.obtain(event.getDownTime(),
	     * event.getEventTime(), event.getActionMasked(), x, y,
	     * event.getMetaState()));
	     */
	    // MotionEvent me = MotionEvent.obtain(event);
	    MotionEvent me = MotionEvent.obtain(event.getDownTime(),
		    event.getEventTime(), actionresolved, x, y,
		    event.getPressure(actionPointerIndex),
		    event.getPressure(actionPointerIndex),
		    event.getMetaState(), event.getXPrecision(),
		    event.getYPrecision(), event.getDeviceId(),
		    event.getEdgeFlags());
	    me.setLocation(x, y);

	    // Log.v("tag", "oldeventid: " + event.getAction() + " #" +
	    // actionPointerIndex+ " id" + ptrId+ " resolved "+ actionResolved);
	    // Log.v("tag", "neweventid: " + me.getAction() + " #" +
	    // actionPointerIndex+ " id" + ptrId+ " resolved "+ actionResolved);

	    if (!me.equals(event)) {
		/*
		 * Log.v("tag", "actionindex: " + actionPointerIndex +
		 * " resolved: " + actionPointerIndex + " to " + view.toString()
		 * + " y:" + view.getTop() + "-" + (view.getTop() +
		 * view.getHeight()));
		 */
		// me.setAction(actionresolved);
		view.onTouchEvent(me);
	    }

	    if (actionresolved == MotionEvent.ACTION_MOVE) {
		Log.v("tag",
			"#" + actionPointerIndex + "Rawx:" + rawX + " rawy:"
				+ rawY + "x:" + x + " y:" + y + " "
				+ view.toString());
	    }
	}

    }

    private ArrayList<View> getTouchedViews(int x, int y, int action) {

	int moveGap = 0;

	if (action == MotionEvent.ACTION_MOVE) {
	    moveGap = 0;
	}

	ArrayList<View> touchedViews = new ArrayList<View>();
	ArrayList<View> possibleViews = new ArrayList<View>();

	if (parent instanceof ViewGroup) {
	    possibleViews.add(parent);
	    for (int i = 0; i < possibleViews.size(); i++) {
		View view = possibleViews.get(i);

		int location[] = { 0, 0 };
		view.getLocationOnScreen(location);
		/*
		 * Log.v("tag", location + "");
		 * 
		 * Log.v("tag", "view = " + view.toString()); Log.v("tag", "bot"
		 * + (view.getHeight() + location[1]) + " right" +
		 * (view.getWidth() + location[0]) + " left" + view.getLeft() +
		 * " top" + view.getBottom());
		 */

		if (((view.getHeight() + location[1] + moveGap >= y)
			& (view.getWidth() + location[0] + moveGap >= x)
			& (view.getLeft() - moveGap <= x) & (view.getTop()
			- moveGap <= y))
			|| view instanceof FrameLayout) {

		    touchedViews.add(view);
		    possibleViews.addAll(getChildViews(view));
		}

	    }
	}

	return touchedViews;

    }

    private ArrayList<View> getChildViews(View view) {
	ArrayList<View> views = new ArrayList<View>();
	if (view instanceof ViewGroup) {
	    ViewGroup v = ((ViewGroup) view);
	    if (v.getChildCount() > 0) {
		for (int i = 0; i < v.getChildCount(); i++) {
		    views.add(v.getChildAt(i));
		}

	    }
	}
	return views;
    }
}
