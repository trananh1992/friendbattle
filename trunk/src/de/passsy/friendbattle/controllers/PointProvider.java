package de.passsy.friendbattle.controllers;

import de.passsy.friendbattle.data.Player;
import de.passsy.friendbattle.games.MiniGame;
import de.passsy.friendbattle.games.MiniGame.Correctness;
import de.passsy.friendbattle.utility.GoodTimer;
import de.passsy.friendbattle.utility.GoodTimer.OnTimerListener;
import de.passsy.friendbattle.utility.GoodTimer.Repeat;

public abstract class PointProvider {

    protected boolean mCorrectness;
    protected MiniGame mMiniGame;

    public PointProvider(MiniGame miniGame) {
	mMiniGame = miniGame;
    }

    public Correctness evalCorrectness(Boolean gameCorrectness, Player player) {
	if (gameCorrectness) {
	    showResults();
	    mMiniGame.stopGame();
	    startNextGame(2000);
	}
	return Correctness.unclear;
    }

    private void startNextGame(int time) {
	final GoodTimer timer = new GoodTimer(time, Repeat.No);
	timer.setOnTimerListener(new OnTimerListener() {

	    @Override
	    public void onTimer() {
		mMiniGame.getOnNextGameListener().onNextGame();
	    }
	});
	timer.start();

    }

    public abstract void showResults();

    public void setCorrectness(boolean correctness) {
	mCorrectness = correctness;
    }

}
