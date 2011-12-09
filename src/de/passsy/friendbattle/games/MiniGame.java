package de.passsy.friendbattle.games;

import java.util.Timer;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import de.passsy.friendbattle.controllers.FirstGets;
import de.passsy.friendbattle.controllers.PointProvider;
import de.passsy.friendbattle.data.Player;

/**
 * The MiniGames for FriendBattle
 * @author Pascal.Welsch
 *
 */
/**
 * @author Pascal.Welsch
 * 
 */
public abstract class MiniGame extends RelativeLayout {

    public interface OnNextGameListener {
	/**
	 * fired if the current MiniGame is solved and a new game should launch
	 * 
	 * @param game
	 */
	public abstract void onNextGame(MiniGame game);
    }

    /**
     * Enum for the orrectness state
     * 
     * @author Pascal.Welsch
     * 
     */
    public enum Correctness {
	correct, incorrect, toolate;
    }

    /**
     * How long the Introductions are shown
     */
    private static final int HOWTO_TIME = 5;

    /**
     * Listener for the Event OnNextGame
     */
    private OnNextGameListener mNextGameListener;

    /**
     * true if the game has the state Correct false if the game isn't correct at
     * the moment
     */
    private boolean mCorrectness = false;

    /**
     * true if the game is solved else false
     */
    private boolean mSolved = false;

    /**
     * timer that runs if the Game is solved by a User to start the next Game
     */
    private final Timer mNewGameTimer = new Timer();

    /**
     * Handler to run the mFireTimer from the Timer thread
     */
    private final Handler mHandler = new Handler();

    /**
     * This Runnable is Called from the mNewGameTimer
     */
    final Runnable mNextGameTimer = new Runnable() {
	@Override
	public void run() {
	    if (mNextGameListener != null) {
		mNextGameListener.onNextGame(MiniGame.this);
	    }
	}
    };

    /**
     * Rounds per Game
     */
    private int mRounds;

    /**
     * holds the current PointProvider
     */
    public PointProvider mCurrentPointprovider = new FirstGets();

    /**
     * @return true if the Game is solved
     */
    private boolean isSolved() {
	return mSolved;
    }

    /**
     * true if the game is solved. Users are unable to get points
     * 
     * @param mSolved
     *            solvable?
     */
    private void setSolved(final boolean mSolved) {
	this.mSolved = mSolved;
    }

    /**
     * @return Rounds per Game
     */
    public int getRounds() {
	return mRounds;
    }

    /**
     * sets the rounds per game
     * 
     * @param mRounds
     *            rounds per game
     */
    public void setRounds(final int mRounds) {
	this.mRounds = mRounds;
    }

    /**
     * @return the correctness of the game
     */
    public boolean getCorrectness() {
	return mCorrectness;
    }

    /**
     * should only set true, if the players are able to solve the game
     * 
     * @param isCorrect
     *            game is correct and can solved?
     */
    public void setCorrectness(final boolean isCorrect) {
	mCorrectness = isCorrect;
    }

    /**
     * Constructor
     * 
     * @param context
     */
    public MiniGame(final Context context) {
	super(context);
    }

    /**
     * Constructor
     * 
     * @param context
     * @param attrs
     */
    public MiniGame(final Context context, final AttributeSet attrs) {
	super(context, attrs);
    }

    /**
     * standard GuessManagement. gives and reduce points of the Players if they
     * buzz starts the next Game if the answer was correct
     * 
     * @param player
     */
    public Correctness onGuess(final Player player) {

	return mCurrentPointprovider.evalCorrectness(getCorrectness(), player,
		this);

    };

    /**
     * gets fired after everything was loaded from the GameCycle run
     * super().start in method @Override
     * 
     */
    public void start() {
	showIntroductions(HOWTO_TIME);

	startGame();
    };

    /**
     * 
     * @return
     */
    public void setPointProvider(final PointProvider provider) {
	mCurrentPointprovider = provider;
    }

    /**
     * starts the Game
     */
    abstract public void startGame();

    /**
     * shows the introduction at the beginning of every Game
     * 
     * @param seconds
     */
    abstract protected void showIntroductions(int seconds);

    public void onNextGame(final View v) {
	if (mNextGameListener != null) {
	    mNextGameListener.onNextGame(this);
	}
    }

    public void setOnNextGameListener(final OnNextGameListener l) {
	mNextGameListener = l;
    }

    public OnNextGameListener getOnNextGameListener() {
	return mNextGameListener;
    }

    abstract public CharSequence getDescription();

}
