/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import asd.org.ahorcado.R;
import asd.org.ahorcado.controller.GameController;
import asd.org.ahorcado.exceptions.MatchLostException;
import asd.org.ahorcado.fragments.InputFragment;
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
        getSupportFragmentManager().beginTransaction().replace(R.id.InputFragment, new InputFragment()).commit();
        WordFragment f = (WordFragment) getFragmentManager().findFragmentById(R.id.WordFragment);
        f.updateImage(gameController.obtainPartialWord(), widthDisplay(), heightDisplay());
        try {
            UtilActivity.setBackground(view, gameController.getRemainingLives());
        } catch (MatchLostException e) {
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

    public void guessLetter(View view) {
        synchronized (view) {
            Button button = (Button) view;
            button.setEnabled(false);
            final char letter = button.getText().toString().toCharArray()[0];
            gameController.execute(letter);
            WordFragment f = (WordFragment) getFragmentManager().findFragmentById(R.id.WordFragment);
            f.updateImage(gameController.obtainPartialWord(), widthDisplay(), heightDisplay());
            resultGame(view);
        }
    }

    private void resultGame(View view) {
        int remainingLives = 0;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).setCancelable(false);
        try {
            remainingLives = gameController.getRemainingLives();
            if (gameController.isComplete()) {
                createDialog(dialog, this, view, R.string.win_game, gameController.originalWord());
            }
        } catch (MatchLostException e) {
            createDialog(dialog, this, view, R.string.lost_game, gameController.originalWord());
        } finally {
            UtilActivity.setBackground(view, remainingLives);
        }
    }

    private void createDialog(AlertDialog.Builder dialog, final MainActivity thisActivity, final View view, int title, String message) {
        dialog.setTitle(title).setMessage(message)
                .setPositiveButton(R.string.play_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        lunchGame(view);
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(thisActivity, MainActivity.class));
                    }
                }).show();
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