package de.passsy.friendbattle.utility;

import android.widget.Toast;
import de.passsy.friendbattle.FriendBattle;

public class Tools {

    public static void toast(final CharSequence name) {

	final Toast toast = Toast.makeText(
		FriendBattle.getCurrentActivityContext(), name,
		android.widget.Toast.LENGTH_SHORT);
	toast.show();
    }

    /**
     * calculates a random integer between x1 and x2
     * 
     * @param x1
     *            min value (from)
     * @param x2
     *            max value (to)
     * @return
     */
    public static int getRandomNum(int x1, int x2) {
	if (x2 <= 0)
	    throw new IllegalArgumentException("x2 can't be lower or eqals 0");
	if (x1 > x2)
	    throw new IllegalArgumentException("x1 can't be higher than x2");
	return ((int) (Math.random() * x2 - x1)) + x1;
    }
}
