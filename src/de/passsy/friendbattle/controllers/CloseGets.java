package de.passsy.friendbattle.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import de.passsy.friendbattle.data.Player;
import de.passsy.friendbattle.games.MiniGame;
import de.passsy.friendbattle.games.MiniGame.Correctness;
import de.passsy.friendbattle.utility.GoodTimer;
import de.passsy.friendbattle.utility.GoodTimer.OnTimerListener;
import de.passsy.friendbattle.utility.GoodTimer.Repeat;

public class CloseGets extends PointProvider {

    public CloseGets(MiniGame miniGame) {
	super(miniGame);
    }

    private GoodTimer mTimer;
    private HashMap<Long, Player> mGuesses = new HashMap<Long, Player>();
    private HashMap<Long, Player> mGuessesDistance = new HashMap<Long, Player>();
    private ArrayList<Long> mOrder = new ArrayList<Long>();
    private ArrayList<Player> mPlayerClicked = new ArrayList<Player>();
    private boolean guessable = true;
    /**
     * time before and after the correctness is set and the answers are not
     * false but rather too late
     */
    private static int DELTA = 500;

    /**
     * currentMillis when the correctness was set
     */
    private long mCorrectTime;

    @Override
    public Correctness evalCorrectness(Boolean gameCorrectness, Player player) {
	super.evalCorrectness(gameCorrectness, player);

	if (!mPlayerClicked.contains(player) && guessable) {
	    mGuesses.put(System.currentTimeMillis(), player);
	    return Correctness.unclear;
	}
	return Correctness.tooearly;
    }

    @Override
    public void setCorrectness(boolean correctness) {
	super.setCorrectness(correctness);
	// if correctness will be set to true
	if (correctness) {
	    mCorrectTime = System.currentTimeMillis();
	    mTimer = new GoodTimer(DELTA, Repeat.No);
	    mTimer.start();
	    mTimer.setOnTimerListener(new OnTimerListener() {

		@Override
		public void onTimer() {
		    guessable = false;
		    dealPoints();
		    showResults();
		}

	    });
	}

    }

    private void dealPoints() {
	// if any person answers
	if (mGuesses.size() > 0) {

	    // mSortedGuesses is sorted.
	    // the key value will be the the millis from guess to mCorrectTime
	    for (long key : mGuesses.keySet()) {
		long distance = key - mCorrectTime;
		mGuessesDistance.put(Math.abs(distance), mGuesses.get(key));
	    }

	    mOrder = new ArrayList<Long>(mGuessesDistance.keySet());
	    Collections.sort(mOrder);

	    for (long time : mOrder) {
		Player player = mGuessesDistance.get(time);
		if (time > DELTA) {
		    player.getBuzzer().showGuessState(Correctness.incorrect);
		    player.setPoints(player.getPoints() - 1);
		} else {
		    player.getBuzzer().showGuessState(Correctness.toolate);
		}
	    }
	    Player winner = mGuessesDistance.get(mOrder.get(0));
	    if (mOrder.get(0) < DELTA) {
		winner.getBuzzer().showGuessState(Correctness.correct);
		winner.setPoints(winner.getPoints() + 1);
	    }
	} else {
	    // TODO if guesses are 0?
	}
    }

    public void setDelta(int mDelta) {
	this.DELTA = mDelta;
    }

    @Override
    public void showResults() {
	// TODO Auto-generated method stub

    }

}
