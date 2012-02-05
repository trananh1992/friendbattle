package de.passsy.friendbattle.controllers;

import de.passsy.friendbattle.data.Player;
import de.passsy.friendbattle.games.MiniGame;
import de.passsy.friendbattle.games.MiniGame.Correctness;
import de.passsy.friendbattle.utility.GoodTimer;
import de.passsy.friendbattle.utility.GoodTimer.OnTimerListener;

public abstract class PointProvider {

    protected boolean mCorrectness;
    protected MiniGame mMiniGame;

    public PointProvider(MiniGame miniGame) {
	mMiniGame = miniGame;
    }

    public Correctness evalCorrectness(Boolean gameCorrectness, Player player) {
	if (gameCorrectness) {
	    showResults();
	}
	return Correctness.unclear;
    }

    public void showResults() {
	final GoodTimer timer = new GoodTimer(2000, false);
	timer.setOnTimerListener(new OnTimerListener() {

	    @Override
	    public void onTimer() {
		mMiniGame.getOnNextGameListener().onNextGame();
	    }
	});
	timer.start();
    }

    public void setCorrectness(boolean correctness) {
	mCorrectness = correctness;
    }

}
