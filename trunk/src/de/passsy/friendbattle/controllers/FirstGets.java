package de.passsy.friendbattle.controllers;

import de.passsy.friendbattle.data.Player;
import de.passsy.friendbattle.games.MiniGame;
import de.passsy.friendbattle.games.MiniGame.Correctness;

public class FirstGets extends PointProvider {

    public FirstGets(MiniGame miniGame) {
	super(miniGame);
	// TODO Auto-generated constructor stub
    }

    private Boolean mSolved = false;

    @Override
    public Correctness evalCorrectness(final Boolean gameCorrectness,
	    final Player player) {
	Correctness result = Correctness.incorrect;

	if (mMiniGame.getPrepare()) {
	    return Correctness.tooearly;
	}

	if (gameCorrectness) {
	    // Game is correct
	    if (!mSolved) {
		// First who guessed correct
		// Points++
		result = Correctness.correct;
		showResults();
	    } else {
		// Player was to late, someone else was faster
		// Points don't change
		result = Correctness.toolate;
	    }
	    // now no one can get points
	    mSolved = true;
	    // start next game in 1 second
	    // TODO move to results screen

	} else {
	    // Game isn't correct
	    if (!mSolved) {
		// Player gets Points--
		result = Correctness.incorrect;
	    }
	}

	return result;

    }

}
