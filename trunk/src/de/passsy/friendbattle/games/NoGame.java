package de.passsy.friendbattle.games;

import android.content.Context;
import de.passsy.friendbattle.data.Player;

public class NoGame extends MiniGame {

    public NoGame(final Context context) {
	super(context);
    }

    @Override
    protected void showIntroductions(final int seconds) {

    }

    @Override
    public Correctness onGuess(final Player player) {
	return Correctness.toolate;
    }

    @Override
    public void startGame() {
	// do nothing
    }

    @Override
    public CharSequence getDescription() {
	return "";
    }

    @Override
    public void stopGame() {
	// TODO Auto-generated method stub

    }

}
