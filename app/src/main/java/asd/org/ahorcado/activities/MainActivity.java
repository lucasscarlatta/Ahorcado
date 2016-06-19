/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import asd.org.ahorcado.R;
import asd.org.ahorcado.controller.GameController;
import asd.org.ahorcado.exceptions.MatchLostException;
import asd.org.ahorcado.fragments.HelpFragment;
import asd.org.ahorcado.fragments.InputFragment;
import asd.org.ahorcado.fragments.UserFragment;
import asd.org.ahorcado.fragments.WordFragment;
import asd.org.ahorcado.helpers.UserAdapter;
import asd.org.ahorcado.models.Word;
import asd.org.ahorcado.service.CustomVolleyRequestQueue;
import asd.org.ahorcado.utils.MySharedPreference;

public class MainActivity extends AppCompatActivity {

    private static int INTERVAL = 2000; //2 second

    private long firstClickTime;
    private GameController gameController;
    private RequestQueue mQueue;
    private String userName;
    private MySharedPreference mySharedPreference;
    private ProgressDialog progressDialog;
    private boolean serverAvailable = false;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkPlayServices();
        if (serverAvailable) {
            createProcessing();
            mySharedPreference = new MySharedPreference(MainActivity.this);
            boolean sentToken = mySharedPreference.hasUserSubscribeToNotification();
            if (sentToken) {
                Toast.makeText(MainActivity.this, getString(R.string.registered), Toast.LENGTH_SHORT).show();
                Button playVersus = (Button) findViewById(R.id.play_vs_game);
                playVersus.setVisibility(View.VISIBLE);
                Button availableVersus = (Button) findViewById(R.id.available_versus);
                availableVersus.setVisibility(View.INVISIBLE);
            } else {
                Button playVersus = (Button) findViewById(R.id.play_vs_game);
                playVersus.setVisibility(View.INVISIBLE);
            }
            TableLayout keyboard = (TableLayout) this.findViewById(R.id.keyboardTableLayout);
            keyboard.setVisibility(View.INVISIBLE);
            TableLayout tHelp = (TableLayout) this.findViewById(R.id.tableHelpLayout);
            tHelp.setVisibility(View.INVISIBLE);
            gameController = new GameController();
            progressDialog.dismiss();
        } else {
            finish();
        }
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
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    public void lunchGame(View view) {
        loadWord(view);
    }

    public void onAvailableClicked(View view) {
        boolean sentNewToken = mySharedPreference.hasUserSubscribeToNotification();
        if (sentNewToken) {
            Toast.makeText(MainActivity.this, getString(R.string.registered), Toast.LENGTH_SHORT).show();
            Button playVersus = (Button) findViewById(R.id.play_vs_game);
            playVersus.setVisibility(View.VISIBLE);
            Button availableVersus = (Button) findViewById(R.id.available_versus);
            availableVersus.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.un_register), Toast.LENGTH_SHORT).show();
        }
    }

    public void lunchVsGame(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.input_user_name);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userName = input.getText().toString();
                sendUserName();
            }
        });
        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
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

    private void startNewGame(View view) {
        Button button = (Button) findViewById(R.id.play_game);
        button.setVisibility(View.INVISIBLE);
        button = (Button) findViewById(R.id.play_vs_game);
        button.setVisibility(View.INVISIBLE);
        TableLayout tl = (TableLayout) this.findViewById(R.id.keyboardTableLayout);
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

    private void loadWord(final View view) {
        createProcessing();
        //TODO change URL
        String url = MySharedPreference.PREFIX_URL + "words/1";
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Word word = gameController.getWord(response);
                gameController.newMatch(word);
                progressDialog.dismiss();
                startNewGame(view);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        jsonRequest.setTag(MySharedPreference.REQUEST_GET_TAG);
        mQueue.add(jsonRequest);
    }

    private void sendUserName() {
        createProcessing();
        String url = MySharedPreference.PREFIX_URL + "installations/updateByToken";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                        loadUsers();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(UserAdapter.COLUMN_NAME, userName);
                params.put(MySharedPreference.TOKEN_TO_SERVER, FirebaseInstanceId.getInstance().getToken());
                return params;
            }
        };
        stringRequest.setTag(MySharedPreference.REQUEST_POST_TAG);
        mQueue.add(stringRequest);
    }

    private void loadUsers() {
        Button button = (Button) findViewById(R.id.play_game);
        button.setVisibility(View.INVISIBLE);
        button = (Button) findViewById(R.id.play_vs_game);
        button.setVisibility(View.INVISIBLE);
        UserFragment user = (UserFragment) getFragmentManager().findFragmentById(R.id.UserFragment);
        user.setContext(this);
        user.setRequestQueue(mQueue);
        findUsers(user, this);
    }

    private void findUsers(final UserFragment user, final Activity activity) {
        createProcessing();
        String url = MySharedPreference.PREFIX_URL + "installations";
        final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method
                .GET, url,
                new JSONArray(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                UserAdapter adapter = new UserAdapter(activity, gameController.getUserMap(response));
                user.setUserAdapter(adapter);
                user.updateList(adapter);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        jsonRequest.setTag(MySharedPreference.REQUEST_GET_TAG);
        mQueue.add(jsonRequest);
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

    private void createProcessing() {
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("MainActivity", "This device is not supported.");
                Toast.makeText(MainActivity.this, "This device is not supported.", Toast.LENGTH_SHORT).show();
            }
        } else {
            serverAvailable = true;
        }
    }

}