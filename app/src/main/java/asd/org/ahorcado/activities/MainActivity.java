/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;

import asd.org.ahorcado.R;
import asd.org.ahorcado.controller.GameController;
import asd.org.ahorcado.exceptions.MatchLostException;
import asd.org.ahorcado.fragments.HelpFragment;
import asd.org.ahorcado.fragments.InputFragment;
import asd.org.ahorcado.fragments.WordFragment;
import asd.org.ahorcado.service.CustomJSONArrayRequest;
import asd.org.ahorcado.service.CustomVolleyRequestQueue;

public class MainActivity extends AppCompatActivity implements Response.Listener,
        Response.ErrorListener {

    private static int INTERVAL = 2000; //2 second
    private static final String REQUEST_TAG = "MainVolleyActivity";

    private long firstClickTime;
    private GameController gameController;
    private RequestQueue mQueue;
    private boolean first;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TableLayout tl = (TableLayout) this.findViewById(R.id.tableLayout);
        tl.setVisibility(View.INVISIBLE);
        TableLayout tHelp = (TableLayout) this.findViewById(R.id.tableHelpLayout);
        tHelp.setVisibility(View.INVISIBLE);
        gameController = new GameController();
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

    @Override
    protected void onStart() {
        super.onStart();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = "http://localhost:3000/api/Words";
        final CustomJSONArrayRequest jsonRequest = new CustomJSONArrayRequest(Request.Method
                .GET, url,
                new JSONArray(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println(error.toString());
    }

    @Override
    public void onResponse(Object response) {
        JSONArray jsonArray = (JSONArray) response;
        gameController.newMatch(jsonArray);
        first = true;
        progressDialog.dismiss();
    }

    public void lunchGame(View view) {
        if (!first) {
            gameController.newMatch(null);
        } else {
            first = false;
        }
        Button button = (Button) findViewById(R.id.play_game);
        button.setVisibility(View.INVISIBLE);
        TableLayout tl = (TableLayout) this.findViewById(R.id.tableLayout);
        tl.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.InputFragment, new InputFragment()).commit();
        TableLayout tHelp = (TableLayout) this.findViewById(R.id.tableHelpLayout);
        tHelp.setVisibility(View.VISIBLE);
        HelpFragment hf = (HelpFragment) getSupportFragmentManager().findFragmentById(R.id.HelpFragment);
        int coins = gameController.getCoins();
        hf.setCoinsToView(coins);
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

    private void guessLogic(View view, char letter) {
        synchronized (view) {
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
                        dialog.dismiss();
                        lunchGame(view);
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        startActivity(new Intent(thisActivity, MainActivity.class));
                    }
                }).show();
    }

    public void guessLetter(View view) {
        Button button = (Button) view;
        button.setEnabled(false);
        char letter = button.getText().toString().toCharArray()[0];
        guessLogic(view, letter);
    }

    public void showOneLetter(View view) {
        HelpFragment hf = (HelpFragment) getSupportFragmentManager().findFragmentById(R.id.HelpFragment);
        char letter = gameController.showOneLetter();
        int coins = gameController.getCoins();
        hf.enableHelp(coins != 0);
        hf.setCoinsToView(coins);
        disableLetterHelped(letter);
        WordFragment f = (WordFragment) getFragmentManager().findFragmentById(R.id.WordFragment);
        f.updateImage(gameController.obtainPartialWord(), widthDisplay(), heightDisplay());
        resultGame(view);
    }

    private void disableLetterHelped(char letter) {
        int idButton = getResources().getIdentifier("button" + letter, "id", getPackageName());
        Button buttonLetter = (Button) findViewById(idButton);
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