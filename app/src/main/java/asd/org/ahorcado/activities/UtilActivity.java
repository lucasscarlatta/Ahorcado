/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.activities;

import android.view.View;
import android.widget.RelativeLayout;

import asd.org.ahorcado.R;

public class UtilActivity {

    protected static void setBackground(View view, int life) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.content);
        switch (life) {
            case 5:
                relativeLayout.setBackgroundResource(R.drawable.hangman2);
                break;
            case 4:
                relativeLayout.setBackgroundResource(R.drawable.hangman3);
                break;
            case 3:
                relativeLayout.setBackgroundResource(R.drawable.hangman4);
                break;
            case 2:
                relativeLayout.setBackgroundResource(R.drawable.hangman5);
                break;
            case 1:
                relativeLayout.setBackgroundResource(R.drawable.hangman6);
                break;
            case 0:
                relativeLayout.setBackgroundResource(R.drawable.hangman7);
                break;
        }
    }

}