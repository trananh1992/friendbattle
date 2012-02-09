package de.passsy.friendbattle.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.util.Log;
import de.passsy.friendbattle.data.Player;
import de.passsy.friendbattle.games.MiniGame;
import de.passsy.friendbattle.games.MiniGame.Correctness;
import de.passsy.friendbattle.utility.GoodTimer;
import de.passsy.friendbattle.utility.GoodTimer.OnTimerListener;

public class CloseGets extends PointProvider {

    public CloseGets(MiniGame miniGame) {
	super(miniGame);
	// TODO Auto-generated constructor stub
    }

    private GoodTimer mTimer;
    private HashMap<Long, Player> mGuesses = new HashMap<Long, Player>();
    private HashMap<Long, Player> mSortedGuesses = new HashMap<Long, Player>();
    private int mDelta = 500;

    private long mCorrectTime;

    @Override
    public Correctness evalCorrectness(Boolean gameCorrectness, Player player) {
	super.evalCorrectness(gameCorrectness, player);
	mGuesses.put(System.currentTimeMillis(), player);
	return Correctness.unclear;
    }

    @Override
    public void setCorrectness(boolean correctness) {
	super.setCorrectness(correctness);
	if (correctness) {
	    mCorrectTime = System.currentTimeMillis();
	    mTimer = new GoodTimer(mDelta, false);
	    mTimer.start();
	    mTimer.setOnTimerListener(new OnTimerListener() {

		@Override
		public void onTimer() {
		    dealPoints();
		    showResults();

		}

	    });
	}

    }

    private void dealPoints() {
	Log.v("tag", "dealPoints");
	if (mGuesses.size() > 0) {
	    ArrayList<Long> orderedKeys = new ArrayList<Long>(mGuesses.keySet());
	    Collections.sort(orderedKeys);
	    for (int i = 0; i < orderedKeys.size(); i++) {
		long key = (orderedKeys.get(i) - mCorrectTime);
		mSortedGuesses.put(key, mGuesses.get(orderedKeys.get(i)));
	    }
	    orderedKeys = new ArrayList<Long>(mSortedGuesses.keySet());
	    for (int i = 0; i < orderedKeys.size(); i++) {
		Player player = mSortedGuesses.get(orderedKeys.get(i));
		if (Math.abs(orderedKeys.get(i)) > mDelta) {
		    orderedKeys.remove(orderedKeys.get(i));
		    player.getBuzzer().showGuessState(Correctness.incorrect);
		    player.setPoints(player.getPoints() - 1);
		} else {
		    player.getBuzzer().showGuessState(Correctness.toolate);
		}
	    }
	    Collections.sort(orderedKeys);
	    // TODO IndexOutOfBoundException
	    Player player = mSortedGuesses.get(orderedKeys.get(0));
	    player.getBuzzer().showGuessState(Correctness.correct);
	    player.setPoints(player.getPoints() + 1);
	}
    }

    public void setDelta(int mDelta) {
	this.mDelta = mDelta;
    }

}
