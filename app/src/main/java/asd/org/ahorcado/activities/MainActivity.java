/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import asd.org.ahorcado.R;
import asd.org.ahorcado.controller.GameController;
import asd.org.ahorcado.exceptions.LostLifeException;
import asd.org.ahorcado.fragments.WordFragment;

public class MainActivity extends AppCompatActivity {

    private GameController gameController;

    private static int INTERVAL = 2000; //2 second

    private long firstClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TableLayout tl = (TableLayout) this.findViewById(R.id.tableLayout);
        tl.setVisibility(View.INVISIBLE);
        gameController = new GameController(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lunchGame(View view) {
        gameController.newMatch();
        Button button = (Button) findViewById(R.id.play_game);
        button.setVisibility(View.INVISIBLE);
        TableLayout tl = (TableLayout) this.findViewById(R.id.tableLayout);
        tl.setVisibility(View.VISIBLE);
        WordFragment f = (WordFragment) getFragmentManager().findFragmentById(R.id.WordFragment);
        f.updateImage(gameController.obtainPartialWord(), widthDisplay(), heightDisplay());
        try {
            UtilActivity.setBackground(view, gameController.getRemainingLives());
        } catch (LostLifeException e) {
        }
    }

    @Override
    public void onBackPressed() {
        if (firstClickTime + INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            System.exit(0);
        } else {
            Toast.makeText(this, R.string.back_button, Toast.LENGTH_SHORT).show();
        }
        firstClickTime = System.currentTimeMillis();
    }

    private void guessLogic(View view,char letter) {
        gameController.execute(letter);
        WordFragment f = (WordFragment) getFragmentManager().findFragmentById(R.id.WordFragment);
        f.updateImage(gameController.obtainPartialWord(), widthDisplay(), heightDisplay());
        if (gameController.isComplete()) {
            //WIN
        }
        int remainingLives = 0;
        try {
            remainingLives = gameController.getRemainingLives();
        } catch (LostLifeException e) {
        } finally {
            UtilActivity.setBackground(view, remainingLives);
        }
    }

    public void guessLetter(View view) {
        Button button = (Button) view;
        button.setEnabled(false);
        char letter = button.getText().toString().toCharArray()[0];
        guessLogic(view,letter);
    }

    public void showOneLetter(View view) {
        Button buttonHelp=(Button) view;
        buttonHelp.setEnabled(false);
        Map result=gameController.showOneLetter();
        guessLogic(view,(char)result.get("letter"));
    }

    private void disableLetterHelped(char letter) {
        int idButton = getResources().getIdentifier("button" + letter, "id", getPackageName());
        Button buttonLetter=(Button) findViewById(idButton);
        buttonLetter.setEnabled(false);
    }

    private int widthDisplay() {
        return sizeDisplay().x;
    }

    private int heightDisplay() {
        return sizeDisplay().y;
    }

    private Point sizeDisplay() {
        Display display = getWindowManager().getDefaultDisplay();
        Point tam = new Point();
        display.getSize(tam);
        return tam;
    }

}