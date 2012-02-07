package de.passsy.friendbattle.games;

import android.content.Context;
import de.passsy.friendbattle.screenlayouts.Screen_TextViewsCenter;
import de.passsy.friendbattle.utility.GoodTimer;
import de.passsy.friendbattle.utility.GoodTimer.OnTimerListener;

public class MathGame extends MiniGame {

    int time = 10 * 1000;

    private GoodTimer timer;

    int problemNum = 2;

    private final Screen_TextViewsCenter mTextViews;

    private final OnTimerListener onTimerListener = new OnTimerListener() {

	@Override
	public void onTimer() {
	    boolean trueFalse = true;
	    if (getRandomNum() < 80) {
		trueFalse = false;
	    }
	    setCorrectness(trueFalse);
	    createRandomProblem(trueFalse);
	}
    };

    public MathGame(Context context) {
	super(context);
	mTextViews = new Screen_TextViewsCenter(context);
	this.addView(mTextViews);

	mTextViews.setTextSize(7);
	mTextViews.setText("?");

	timer = new GoodTimer(time, true);
	timer.setOnTimerListener(onTimerListener);
    }

    @Override
    public void startGame() {
	boolean trueFalse = true;
	if (getRandomNum() < 80) {
	    trueFalse = false;
	}
	setCorrectness(trueFalse);
	createRandomProblem(trueFalse);
	timer.start();

    }

    @Override
    protected void showIntroductions(int seconds) {
	// TODO Auto-generated method stub

    }

    @Override
    public CharSequence getDescription() {
	return "Press if you think the statment is correct.";
    }

    public void createRandomProblem(boolean trueFalse) {

	int problemType = (int) (Math.random() * problemNum);

	switch (problemType) {
	case 0:
	    basicCalculating(trueFalse);
	    break;
	case 1:
	    homage(trueFalse);
	    break;
	// case 2:
	// comparing(trueFalse);
	default:
	    // nothing. should not happen
	    break;
	}
    }

    private void homage(boolean trueFalse) {

	if (trueFalse) {
	    mTextViews.setText("This game rules!");
	} else {
	    mTextViews.setText("Chuck Norris can´t steel me a point!");
	}
    }

    /*
     * private void comparing() {
     * 
     * }
     */

    private void basicCalculating(boolean trueFalse) {

	String output;
	int operandCount = 2 + (int) (Math.random() * 3);
	int trueResult = getRandomNum();
	int nextOperand;

	output = String.valueOf(trueResult);

	for (int i = 1; i < operandCount; i++) {
	    nextOperand = getRandomNum();
	    if (getRandomNum() < 50) {
		output += " + " + String.valueOf(nextOperand);
		trueResult += nextOperand;
	    } else {
		output += " - " + String.valueOf(nextOperand);
		trueResult -= nextOperand;
	    }
	}

	output += " = ";
	if (!trueFalse) {
	    if (getRandomNum() < 50) {
		trueResult += getRandomNum();
	    } else {
		trueResult -= getRandomNum();
	    }
	}
	output += String.valueOf(trueResult);

	mTextViews.setText(output);

    }

    private int getRandomNum() {
	return ((int) (Math.random() * 100));
    }
}
