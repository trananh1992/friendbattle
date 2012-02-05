package de.passsy.friendbattle.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.passsy.friendbattle.R;
import de.passsy.friendbattle.data.Player;
import de.passsy.friendbattle.games.MiniGame.Correctness;
import de.passsy.friendbattle.utility.GoodTimer;
import de.passsy.friendbattle.utility.GoodTimer.OnTimerListener;

public class Buzzer extends RelativeLayout {

    public interface OnBuzzListener {
	public abstract Correctness onBuzz(Buzzer btn);
    }

    private OnBuzzListener mBuzzListener;
    private Player mPlayer;
    private TextView mPoints_txt;
    private ImageView mBackground;
    private Boolean mFlipped = false;
    private Boolean mCorrectBuzz = false;
    private Boolean mTooLateBuzz = false;
    private View mColorView;
    private int mColor;
    private Boolean mAllowUserChangeColor = true;

    private int mPreviousY;

    public Buzzer(final Context context) {
	super(context);
	init(context);
    }

    public Buzzer(final Context context, final AttributeSet attrs) {
	super(context, attrs);
	init(context);
	analyseAttributes(context, attrs);

    }

    private void init(final Context context) {
	LayoutInflater.from(context).inflate(R.layout.buzzer, this, true);
	findViews();
	setRandomColor();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
	final int action = event.getAction();
	switch (action) {
	case MotionEvent.ACTION_DOWN:
	    mPreviousY = (int) event.getY();

	    if (mBuzzListener != null && mPlayer != null) {
		showGuessState(mBuzzListener.onBuzz(Buzzer.this));

	    }
	    break;

	case MotionEvent.ACTION_MOVE:
	    onMove(event);
	    break;

	case MotionEvent.ACTION_UP:
	    mBackground.setImageResource(R.drawable.buzzerupdown);

	case MotionEvent.ACTION_CANCEL:
	    break;

	}
	return true;
    }

    private void setRandomColor() {
	final List<Integer> rgb = new ArrayList<Integer>();

	final int red = (int) (Math.round(Math.random()) * 255);
	final int green = (int) (Math.round(Math.random()) * 255);
	final int blue = (int) (Math.round(Math.random()) * 255);

	rgb.add(red);
	rgb.add(green);
	rgb.add(blue);

	rgb.get((int) (Math.round(Math.random())));

	// Log.v("tag", "random" + red + " " + green + " " + blue);
	if (red + green + blue == 3 * 255 || red + green + blue == 0) {
	    // Log.v("tag", "wrong colors");
	    setRandomColor();
	    return;
	} else {
	    setColor(Color.rgb(red, green, blue));
	}

    }

    private void findViews() {

	mBackground = (ImageView) findViewById(R.id.background);
	mPoints_txt = (TextView) findViewById(R.id.points_txt);
	mColorView = findViewById(R.id.color_view);
    }

    /**
     * sets the Color of the
     * 
     * @param color
     */
    public void setColor(final int color) {
	mColor = color;
	mColorView.setBackgroundColor(color);
    }

    public void setColor(final Color color) {
	setColor(color.hashCode());
    }

    public int getColor() {
	return mColor;
    }

    private int isHex(int i) {
	if (i > 255) {
	    i = 255;
	}
	if (i < 0) {
	    i = 0;
	}
	return i;
    }

    public void setOnBuzzListener(final OnBuzzListener l) {
	mBuzzListener = l;
    }

    /**
     * Analysis of the attributes set within the XML file
     * 
     * @param context
     * @param attrs
     */
    private void analyseAttributes(final Context context,
	    final AttributeSet attrs) {

	if (attrs != null) {

	    final TypedArray attributes = context.obtainStyledAttributes(attrs,
		    R.styleable.Buzzer);

	    final boolean flipped = attributes.getBoolean(
		    R.styleable.Buzzer_flipped, false);
	    if (flipped) {
		mFlipped = true;
	    }
	    attributes.recycle();
	}
    }

    public void setText(final CharSequence text) {
	mPoints_txt.setText(text);
    }

    public Player getPlayer() {
	return mPlayer;
    }

    public void setPlayer(final Player player) {
	mPlayer = player;
    }

    @Override
    protected void dispatchDraw(final Canvas canvas) {
	if (mFlipped) {

	    canvas.save();
	    final float py = getHeight() / 2.0f;
	    final float px = getWidth() / 2.0f;
	    canvas.rotate(180, px, py);
	    super.dispatchDraw(canvas);

	    canvas.restore();
	} else {
	    super.dispatchDraw(canvas);
	}
    }

    private void changeColor(final int delta) {
	if (!mAllowUserChangeColor) {
	    return;
	}

	int blue = mColor & 0xFF;
	int green = (mColor >> 8) & 0xFF;
	int red = (mColor >> 16) & 0xFF;
	// Log.v("tag", "before rgb:" + red + " " + green + " " + blue);
	if (delta > 0) {
	    if (red == 255 && green < 255 && blue == 0) {
		green += Math.abs(delta);
	    }
	    if (red > 0 && green == 255 && blue == 0) {
		red -= Math.abs(delta);
	    }
	    if (red == 0 && green == 255 && blue < 255) {
		blue += Math.abs(delta);
	    }
	    if (red == 0 && green > 0 && blue == 255) {
		green -= Math.abs(delta);
	    }
	    if (red < 255 && green == 0 && blue == 255) {
		red += Math.abs(delta);
	    }
	    if (red == 255 && green == 0 && blue > 0) {
		blue -= Math.abs(delta);
	    }
	} else {
	    if (red < 255 && green == 255 && blue == 0) {
		red += Math.abs(delta);
	    }
	    if (red == 255 && green > 0 && blue == 0) {
		green -= Math.abs(delta);
	    }
	    if (red == 255 && green == 0 && blue < 255) {
		blue += Math.abs(delta);
	    }
	    if (red > 0 && green == 0 && blue == 255) {
		red -= Math.abs(delta);
	    }
	    if (red == 0 && green < 255 && blue == 255) {
		green += Math.abs(delta);
	    }
	    if (red == 0 && green == 255 && blue > 0) {
		blue -= Math.abs(delta);
	    }
	}

	// Log.v("tag", "rgb:" + red + green + blue);

	setColor(Color.rgb(isHex(red), isHex(green), isHex(blue)));
	// mPoints_txt.setTextColor(IdealTextColor(isHex(red),isHex(green),isHex(blue)));
	invalidate();
    }

    private void onMove(final MotionEvent event) {
	final int y = (int) event.getY();
	final int delta = (mPreviousY - y) * 2;
	mPreviousY = y;
	if (mAllowUserChangeColor) {
	    changeColor(delta);
	}

    }

    public int IdealTextColor(int r, int g, int b) {
	int threshold = 110;
	int bgDelta = (int) ((r * 0.299) + (g * 0.587) + (b * 0.114));

	int foreColor = (255 - bgDelta < threshold) ? Color.BLACK : Color.WHITE;
	// Log.v("tag", "" + foreColor);
	return foreColor;
    }

    public Boolean getAllowUserChangeColor() {
	return mAllowUserChangeColor;
    }

    public void setAllowUserChangeColor(Boolean mAllowChangeColor) {
	this.mAllowUserChangeColor = mAllowChangeColor;
    }

    public void freeze(boolean b) {
	if (b) {
	    mBackground.setImageResource(R.drawable.buzzerdownup_gray);
	} else {
	    mBackground.setImageResource(R.drawable.buzzerdownup);
	}

    }

    public void showGuessState(Correctness correctness) {

	int colorImage;
	switch (correctness) {
	case correct:
	    colorImage = R.drawable.buzzerdownup_green;
	    break;
	case incorrect:
	    colorImage = R.drawable.buzzerdownup_red;
	    break;

	default:
	    colorImage = R.drawable.buzzerdownup_gray;
	    break;
	}

	mBackground.setImageResource(colorImage);
	GoodTimer timer = new GoodTimer(1000, false);
	timer.setOnTimerListener(new OnTimerListener() {

	    @Override
	    public void onTimer() {
		changeToNormalState();
	    }
	});
	timer.start();
    }

    public void changeToNormalState() {
	mBackground.setImageResource(R.drawable.buzzerupdown);
    }
}
