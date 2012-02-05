package de.passsy.friendbattle.controllers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import de.passsy.friendbattle.games.ClickWhenWhite;
import de.passsy.friendbattle.games.MiniGame;
import de.passsy.friendbattle.games.MiniGame.OnNextGameListener;
import de.passsy.friendbattle.games.NoGame;

public class GameCycle {

    public interface OnEndListener {
	public abstract void onEnd(GameCycle cycle);
    }

    public interface OnNewGameListener {
	public abstract void onNewGame(CharSequence name,
		CharSequence description);
    }

    private final List<Class<? extends MiniGame>> mMiniGames = new ArrayList<Class<? extends MiniGame>>();

    private int mGameNumber = 0;

    private MiniGame mCurrentGame;

    private int mRounds = 5;

    private int mCurrentRounds = 0;

    public MiniGame getCurrentGame() {
	if (mCurrentGame != null) {
	    return mCurrentGame;
	} else {
	    return new NoGame(mContext);
	}

    }

    private final FrameLayout mRootLayout;

    private OnEndListener mEndListener;

    private OnNewGameListener mNewGameListener;

    private final Context mContext;

    public GameCycle(final Context context, final FrameLayout rootLayout,
	    final int rounds) {
	mContext = context;
	mRootLayout = rootLayout;
	mRounds = rounds;
	loadGames();
    }

    public void start() {
	final Class<? extends MiniGame> nextGame = getNextGame();
	if (nextGame == null) {
	    end();
	    showWinner();
	    return;
	}
	try {
	    // Creates a new Instance of the next Game an passes the Context as
	    // parameter
	    mCurrentGame = nextGame.getConstructor(Context.class).newInstance(
		    mContext);
	} catch (final Exception e) {
	    Log.e("FriendBattle", "ClassNotFound");
	    e.printStackTrace();// Game isn't correct
	}
	mCurrentGame.setOnNextGameListener(new OnNextGameListener() {

	    @Override
	    public void onNextGame() {
		start();
	    }
	});
	// add MiniGame to Screen
	mRootLayout.removeAllViews();
	mRootLayout.addView(mCurrentGame);
	// starts the MiniGame
	final CharSequence name = mCurrentGame.getClass().getName();
	final CharSequence description = mCurrentGame.getDescription();
	mNewGameListener.onNewGame(name, description);
	mCurrentGame.start();

    }

    private void showWinner() {

    }

    private Class<? extends MiniGame> getNextGame() {
	if (mMiniGames.size() <= mGameNumber) {
	    end();
	    return null;
	} else {
	    final Class<? extends MiniGame> nextGame = mMiniGames
		    .get(mGameNumber);
	    mCurrentRounds++;
	    if (mCurrentRounds >= mRounds) {
		mGameNumber++;
		mCurrentRounds = 0;
	    }
	    return nextGame;
	}
    }

    public void onEnd(final View v) {
	if (mEndListener != null) {
	    mEndListener.onEnd(this);
	}
    }

    public void setonEndListener(final OnEndListener l) {
	mEndListener = l;
    }

    public void setOnNewGameListener(final OnNewGameListener l) {
	mNewGameListener = l;
    }

    private void end() {
	mRootLayout.removeAllViews();
	mEndListener.onEnd(this);

    }

    private void loadGames() {

	// mMiniGames.add(GuessWhen.class);
	mMiniGames.add(ClickWhenWhite.class);

    }

}
