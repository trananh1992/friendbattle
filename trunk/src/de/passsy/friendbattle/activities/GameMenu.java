package de.passsy.friendbattle.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import de.passsy.friendbattle.FriendBattle;
import de.passsy.friendbattle.R;
import de.passsy.friendbattle.controls.Buzzer;
import de.passsy.friendbattle.games.ClickWhenWhite;
import de.passsy.friendbattle.utility.MultiTouchActivity;

public class GameMenu extends MultiTouchActivity {

    public final static String GAME_PREF = "GamePreferences";
    private int mPlayers = 6;
    private final int mRounds = 1;

    private final String mGameVersion = "0.01";

    private final static int EASY = 0;
    private final static int NORMAL = 1;
    private final static int HARD = 2;

    private final ArrayList<String> mGames = new ArrayList<String>();

    private SeekBar mPlayerSeek;
    private Button mStartButton;
    private TextView mPlayer_txt;
    private final List<Buzzer> mBuzzer = new ArrayList<Buzzer>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	FriendBattle.setCurrentActivity(this);
	setContentView(R.layout.gamemenu);

	findViews();
	loadPreferences();
    }

    private void findViews() {
	mPlayer_txt = (TextView) findViewById(R.id.players_txt);
	mStartButton = (Button) findViewById(R.id.startgame);
	mStartButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(final View v) {
		startGame();
	    }
	});

	mPlayerSeek = (SeekBar) findViewById(R.id.playerSeek);
	mPlayerSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

	    @Override
	    public void onStopTrackingTouch(final SeekBar seekBar) {
		setPlayers(seekBar.getProgress() + 2);
	    }

	    @Override
	    public void onStartTrackingTouch(final SeekBar seekBar) {

	    }

	    @Override
	    public void onProgressChanged(final SeekBar seekBar,
		    final int progress, final boolean fromUser) {
		setPlayers(seekBar.getProgress() + 2);
	    }
	});

	for (int i = 0; i < 6; i++) {
	    final String buzzerID = "buzzer" + i;
	    final Buzzer buzzer = (Buzzer) findViewById(getResources()
		    .getIdentifier(buzzerID, "id", "de.passsy.friendbattle"));
	    mBuzzer.add(buzzer);
	    buzzer.setText("Player " + (i + 1));
	    buzzer.setOnTouchListener(this);
	    addMoveOutsideEnabledViews(buzzer);
	}

    }

    private void loadPreferences() {
	final SharedPreferences settings = getSharedPreferences(GAME_PREF, 0);
	// if the Game Version is different run a firstStart();
	if (settings.getString("version", null) == mGameVersion) {
	    Log.v("tag", settings.getString("version", "null") + " =? "
		    + mGameVersion);
	    Log.v("tag",
		    mGameVersion.equals(settings.getString("version", "null"))
			    + "");
	    getGames();
	    firstStart();
	}

	setPlayers(settings.getInt("players", 2));
	// TODO 10 to maxgames
	for (int i = 0; i < 10; i++) {
	    final String key = "game" + i;
	    if (settings.contains(key)) {
		final String get = settings.getString(key, "noDefValue");
		if (get != "noDefValue") {
		    mGames.add(get);
		}

	    }
	}

    }

    private void getGames() {
	addGames(ClickWhenWhite.class);

    }

    private void savePreferences() {
	final SharedPreferences settings = getSharedPreferences(GAME_PREF, 0);
	final SharedPreferences.Editor editor = settings.edit();
	editor.putInt("players", mPlayers);
	editor.putString("version", mGameVersion);
	editor.putInt("rounds", mRounds);
	for (int i = 0; i < mGames.size() - 1; i++) {
	    editor.putString("game" + i, mGames.get(i));
	}
	editor.commit();
    }

    private void startGame() {
	savePreferences();
	final Bundle arguments = new Bundle();
	arguments.putInt("players", mPlayers);
	arguments.putInt("difficulty", NORMAL);
	arguments.putInt("rounds", mRounds);
	arguments.putStringArrayList("games", mGames);
	final int[] colors = new int[6];
	for (int i = 0; i < colors.length; i++) {
	    colors[i] = mBuzzer.get(i).getColor();
	}
	arguments.putIntArray("buzzercolors", colors);
	FriendBattle.StartActivity(FriendBattleGame.class, arguments);
    }

    private void setPlayers(final int players) {
	mPlayers = players;
	mPlayer_txt.setText("Ein Spiel mit " + players + " Spielern starten");
	for (final Buzzer buzzer : mBuzzer) {
	    buzzer.setVisibility(View.GONE);
	}

	for (int i = 0; i < players; i++) {
	    mBuzzer.get(i).setVisibility(View.VISIBLE);
	}
	;
	mPlayerSeek.setProgress(players - 2);

    }

    private void addGames(final Class<?> game) {
	mGames.add(game.getName());
    }

    /**
     * set initial Options Parameters
     */
    private void firstStart() {
	final SharedPreferences settings = getSharedPreferences(GAME_PREF, 0);
	final SharedPreferences.Editor editor = settings.edit();
	editor.putInt("players", 4);
	editor.putInt("rounds", 3);
	int i = 1;
	for (final String gameClassName : mGames) {
	    editor.putString("game" + i, gameClassName);
	    i++;
	}
	// editor.putString("game" + 1, ClickWhenWhite.class.getName());

	// Log.v("tag", ClickWhenWhite.class.getName());
	editor.commit();

    }
}