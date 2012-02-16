package de.passsy.friendbattle.games;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import de.passsy.friendbattle.controllers.FirstGets;
import de.passsy.friendbattle.screenlayouts.Screen_TextViewsCenter;
import de.passsy.friendbattle.utility.GoodTimer;
import de.passsy.friendbattle.utility.GoodTimer.OnTimerListener;
import de.passsy.friendbattle.utility.GoodTimer.Repeat;
import de.passsy.friendbattle.utility.Tools;

public class QuoteGame extends MiniGame {

    private Screen_TextViewsCenter mTextViews;
    private GoodTimer timer;
    private ArrayList<Quote> quotes = new ArrayList<Quote>();
    private OnTimerListener onTimerListener = new OnTimerListener() {

	@Override
	public void onTimer() {
	    showQuote(Tools.getRandomNum(0, 100) > 60 ? true : false);

	}
    };

    public QuoteGame(Context context) {
	super(context);
	mTextViews = new Screen_TextViewsCenter(context);
	this.addView(mTextViews);
	mTextViews.setTextSize(20);
	mTextViews.setText("?");
	mCurrentPointprovider = new FirstGets(this);
	addQuotes();
    }

    private void addQuotes() {

	quotes.add(new Quote("myQuote", "from test", new String[] {
		"it's meeee", "no me!!!" }));
	quotes.add(new Quote("otherQuote", "from me", new String[] { "orme?",
		"Orme:P" }));
    }

    protected void showQuote(boolean correctness) {
	Tools.toast(correctness + "");
	Quote quote = quotes.get(Tools.getRandomNum(0, quotes.size() - 1));
	mTextViews.setText(quote.getQuote() + "\n\n"
		+ quote.getAutor(correctness));
	setCorrectness(correctness);
    }

    @Override
    public void startGame() {
	final int time = 3000;
	timer = new GoodTimer(time, Repeat.Yes);
	timer.setOnTimerListener(onTimerListener);
	timer.start();
    }

    @Override
    protected void showIntroductions(int seconds) {
	// TODO Auto-generated method stub

    }

    @Override
    public CharSequence getDescription() {
	return "Guess if the quote fits to the author";
    }

    private class Quote {
	private String mQuote;
	private String mAuthor;
	private ArrayList<String> mPossibleAuthors = new ArrayList<String>();

	public Quote(String quote, String author, String[] possibleAuthor) {
	    mQuote = quote;
	    mAuthor = author;
	    mPossibleAuthors.addAll(Arrays.asList(possibleAuthor));
	}

	/**
	 * gives the quote
	 * 
	 * @return the quote
	 */
	public String getQuote() {
	    return mQuote;
	}

	/**
	 * returns the author of the quote
	 * 
	 * @param correctness
	 *            declares if the returned author should be correct or not
	 * @return a possible author of the quote
	 */
	public String getAutor(boolean correctness) {
	    if (correctness) {
		return mAuthor;
	    } else {
		return mPossibleAuthors.get(Tools.getRandomNum(0,
			mPossibleAuthors.size() - 1));
	    }

	}
    }

    @Override
    public void stopGame() {
	timer.stop();

    }

}
