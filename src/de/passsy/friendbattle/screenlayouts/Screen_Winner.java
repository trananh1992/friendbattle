package de.passsy.friendbattle.screenlayouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import de.passsy.friendbattle.FriendBattle;
import de.passsy.friendbattle.R;

public class Screen_Winner extends RelativeLayout {

    public interface OnRestartListener {
	public abstract void onRestart();
    }

    private Button mBackTop_btn;
    private Button mBackBot_btn;
    private Button mRestartTop_btn;
    private Button mRestartBot_btn;

    private OnRestartListener mRestartListener;

    public void setOnRestartListener(final OnRestartListener restartListener) {
	mRestartListener = restartListener;
    }

    public Screen_Winner(final Context context) {
	super(context);
	init(context);
    }

    public Screen_Winner(final Context context, final AttributeSet attrs) {
	super(context, attrs);
	init(context);
    }

    public Screen_Winner(final Context context, final AttributeSet attrs,
	    final int defStyle) {
	super(context, attrs, defStyle);
	init(context);
    }

    private void init(final Context context) {
	LayoutInflater.from(context).inflate(R.layout.screen_winner2, this,
		true);
	findViews();
    }

    private void findViews() {
	mBackBot_btn = (Button) findViewById(R.id.back_bot);
	mBackBot_btn.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(final View v) {
		back();

	    }

	});
	mBackTop_btn = (Button) findViewById(R.id.back_top);
	mBackTop_btn.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(final View v) {
		back();

	    }
	});
	mRestartBot_btn = (Button) findViewById(R.id.restart_bot);
	mRestartBot_btn.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(final View v) {
		restart();

	    }
	});
	mRestartTop_btn = (Button) findViewById(R.id.restart_top);
	mRestartTop_btn.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(final View v) {
		restart();

	    }
	});
    }

    protected void restart() {
	mRestartListener.onRestart();
    }

    protected void back() {
	FriendBattle.getCurrentActivity().onBackPressed();
    }

}
