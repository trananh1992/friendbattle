package de.passsy.friendbattle.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import de.passsy.friendbattle.FriendBattle;
import de.passsy.friendbattle.R;
import de.passsy.friendbattle.controllers.GameCycle;
import de.passsy.friendbattle.controllers.GameCycle.OnEndListener;
import de.passsy.friendbattle.controllers.GameCycle.OnNewGameListener;
import de.passsy.friendbattle.controls.Buzzer;
import de.passsy.friendbattle.controls.Buzzer.OnBuzzListener;
import de.passsy.friendbattle.controls.TextViewFlipped;
import de.passsy.friendbattle.data.Player;
import de.passsy.friendbattle.games.MiniGame;
import de.passsy.friendbattle.games.MiniGame.Correctness;
import de.passsy.friendbattle.screenlayouts.Screen_Winner;
import de.passsy.friendbattle.screenlayouts.Screen_Winner.OnRestartListener;
import de.passsy.friendbattle.utility.MultiTouchActivity;

public class FriendBattleGame extends MultiTouchActivity {

    private static final int MAX_PLAYERS = 6;

    private int mDifficulty;
    private final List<Player> mPlayers = new ArrayList<Player>();
    private int mPlayerNumber;
    private final List<Buzzer> mBuzzer = new ArrayList<Buzzer>();
    private FrameLayout mGameModule;
    private MiniGame mCurrentGame;

    private final Screen_Winner mWinnerScreen = new Screen_Winner();

    private GameCycle mGameCycle;

    private int mRounds;

    private TextViewFlipped mBot_txt;
    private TextViewFlipped mTop_txt;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	FriendBattle.setCurrentActivity(this);
	setContentView(R.layout.friendbattle);
	findViews();
	readIntent();
	createListeners();
	linkPlayerAndButtons();
	loadGames();
	mGameCycle.start();
    }

    /**
     * restarts the Game with the same settings
     */
    public void restart() {
	loadGames();
	for (final Player player : mPlayers) {
	    player.setPoints(0);
	}
	mGameCycle.start();
    }

    /**
     * sets the listeners for the Buzzers
     */
    private void createListeners() {
	for (final Buzzer buzzer : mBuzzer) {
	    buzzer.setOnTouchListener(this);
	    buzzer.setOnBuzzListener(new OnBuzzListener() {

		@Override
		public Correctness onBuzz(final Buzzer btn) {
		    final MiniGame.Correctness correctness = mGameCycle
			    .getCurrentGame().onGuess(btn.getPlayer());
		    dealPoints(correctness, btn.getPlayer());
		    return correctness;
		}
	    });
	}

    }

    /**
     * deals the Points the the Player
     * 
     * @param correctness
     * @param player
     */
    // TODO here or in PointProvider?
    protected void dealPoints(final Correctness correctness, final Player player) {
	switch (correctness) {
	case correct:

	    player.setPoints(player.getPoints() + 1);
	    break;

	case incorrect:
	    player.setPoints(player.getPoints() - 1);
	    break;

	case toolate:

	    break;

	case unclear:
	    player.getBuzzer().freeze(true);
	    break;

	case tooearly:
	    player.setPoints(player.getPoints() - 1);
	    break;

	default:
	    break;
	}

    }

    private void findViews() {
	// find Buzzers
	for (int i = 0; i < MAX_PLAYERS; i++) {
	    final String buzzerID = "buzzer" + i;
	    final Buzzer buzzer = (Buzzer) findViewById(getResources()
		    .getIdentifier(buzzerID, "id", "de.passsy.friendbattle"));
	    mBuzzer.add(buzzer);
	}
	mGameModule = (FrameLayout) findViewById(R.id.gamemodule);

	mTop_txt = (TextViewFlipped) findViewById(R.id.top_txt);
	mBot_txt = (TextViewFlipped) findViewById(R.id.bot_txt);

	mWinnerScreen.setOnRestartListener(new OnRestartListener() {

	    @Override
	    public void onRestart() {
		restart();

	    }
	});
    }

    private void readIntent() {
	final Bundle extras = getIntent().getExtras();
	mDifficulty = extras.getInt("difficulty");
	mPlayerNumber = extras.getInt("players");
	mRounds = extras.getInt("rounds");
	final int colors[] = extras.getIntArray("buzzercolors");
	for (int i = 0; i < colors.length; i++) {
	    mBuzzer.get(i).setColor(colors[i]);
	    mBuzzer.get(i).setAllowUserChangeColor(false);
	}
	// Log.v("tag", mRounds + "");
    }

    private void linkPlayerAndButtons() {

	for (int i = 1; i <= mPlayerNumber; i++) {
	    final Player player = new Player(i);
	    player.setBuzzer(mBuzzer.get(i - 1));
	    mPlayers.add(player);
	    player.getBuzzer().setPlayer(player);
	    player.getBuzzer().setText("0");
	}

	while (mBuzzer.size() > mPlayerNumber) {
	    mBuzzer.get(mPlayerNumber).setVisibility(View.GONE);
	    mBuzzer.remove(mPlayerNumber);
	}

    }

    /**
     * Loads the Games
     */
    private void loadGames() {
	mGameCycle = new GameCycle(getApplicationContext(), mGameModule,
		mRounds);
	mGameCycle.setonEndListener(new OnEndListener() {

	    @Override
	    public void onEnd() {
		showGameResults();
	    }
	});
	mGameCycle.setOnNewGameListener(new OnNewGameListener() {

	    @Override
	    public void onNewGame(final CharSequence name,
		    final CharSequence description) {
		setDescription(description);
	    }
	});
    }

    /**
     * shows the Winner and the Points of all Players
     */
    protected void showGameResults() {
	// TODO update Winner screen
	mGameModule.addView(mWinnerScreen);
	// mWinnerScreen.setWinner(getWinner());
	setText("Player" + getWinner() + " won the Game");
    }

    // TODO merge setText and setDescription.
    private void setText(final CharSequence text) {
	mTop_txt.setText(text);
	mBot_txt.setText(text);
    }

    /**
     * calculates the Winner of the Game
     * 
     * @return Winner
     */
    private int getWinner() {

	// TODO return Player as a List of Players
	Player winner = new Player(-1);
	for (final Player player : mPlayers) {

	    if (player.getPoints() > winner.getPoints()) {
		winner = player;
	    }
	}
	return winner.getId();
    }

    /**
     * sets the description of the current game
     * 
     * @param text
     */
    public void setDescription(final CharSequence text) {
	mBot_txt.setText(text);
	mTop_txt.setText(text);
    }
}
