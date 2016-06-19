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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import asd.org.ahorcado.R;
import asd.org.ahorcado.controller.GameController;
import asd.org.ahorcado.fragments.WordFragment;
import asd.org.ahorcado.helpers.UserAdapter;
import asd.org.ahorcado.models.AbstractMatch;
import asd.org.ahorcado.models.Word;
import asd.org.ahorcado.service.CustomVolleyRequestQueue;
import asd.org.ahorcado.utils.MySharedPreference;

public class VersusActivity extends AppCompatActivity {

    private static final int UPDATE_INTERVAL = 500;

    private String opponentName;
    private int opponentUser;
    private int opponentStatus;
    private Long matchId;
    private ProgressDialog progressDialog;
    private RequestQueue mQueue;
    private TextView tvOpponentName;
    private ProgressBar pbOpponent;
    private GameController gameController;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameController = new GameController();
        progressDialog = new ProgressDialog(this);
        Bundle b = getIntent().getExtras();
        opponentName = b.getString(UserAdapter.COLUMN_NAME);
        opponentUser = b.getInt(UserAdapter.COLUMN_ID);
        tvOpponentName = (TextView) findViewById(R.id.opponentName);
        tvOpponentName.setText(opponentName);
        matchId = b.getLong(AbstractMatch.MATCH_ID);
        if (matchId == null) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this).setCancelable(false);
            dialog.setTitle(R.string.challenge_title)
                    .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            isChallenger(true);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            isChallenger(false);
                            dialog.dismiss();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).show();
        } else if (matchId == -1) {
            createdNewMatch();
        } else {
            if(b.getBoolean(AbstractMatch.IS_ACTIVE)) {
                opponentGuessedLetters();
                int idWord = b.getInt("idWord");
                getNewWord(idWord);
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this).setCancelable(false);
                dialog.setTitle(R.string.challenge_title)
                        .setMessage(String.format(getString(R.string.challenger_refused), opponentName))
                        .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }).show();
            }
        }
        setContentView(R.layout.activity_versus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void getNewWord(final int idWord) {
        String url = MySharedPreference.PREFIX_URL + "words/" + idWord;
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Word word = gameController.getWord(response);
                pbOpponent = (ProgressBar) findViewById(R.id.opponentProgress);
                pbOpponent.setMax(word.getSize());
                pbOpponent.setProgress(0);
                gameController.newMatch(word);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                progressDialog.dismiss();
                Toast.makeText(VersusActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        jsonRequest.setTag(MySharedPreference.REQUEST_GET_TAG);
        mQueue.add(jsonRequest);
    }

    private void opponentGuessedLetters() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String url = MySharedPreference.PREFIX_URL + "match/opponentStatus/" + matchId + "/" + opponentUser;
                final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method
                        .GET, url,
                        new JSONObject(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            opponentStatus = gameController.countGuessedLetters(response.getString("word"));
                            pbOpponent.setProgress(opponentStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        progressDialog.dismiss();
                        Toast.makeText(VersusActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                jsonRequest.setTag(MySharedPreference.REQUEST_GET_TAG);
                mQueue.add(jsonRequest);
            }
        }, 0, UPDATE_INTERVAL);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
    }

    public void guessLetter(View view) {
        Button button = (Button) view;
        button.setEnabled(false);
        char letter = button.getText().toString().toCharArray()[0];
        guessLogic(view, letter);
    }

    private void guessLogic(View view, char letter) {
        synchronized (view) {
            String partialWord = gameController.obtainPartialWord();
            if (gameController.execute(letter)) {
                String url = MySharedPreference.PREFIX_URL + "match/updateStatus";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(AbstractMatch.MATCH_ID, String.valueOf(matchId));
                        params.put("partialWord", gameController.obtainPartialWord());
                        return params;
                    }
                };
                stringRequest.setTag(MySharedPreference.REQUEST_POST_TAG);
                mQueue.add(stringRequest);
            }
            WordFragment f = (WordFragment) getFragmentManager().findFragmentById(R.id.WordFragment);
            f.updateImage(partialWord, widthDisplay(), heightDisplay());
            resultGame();
        }
    }

    private void resultGame() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).setCancelable(false);
        if (gameController.isComplete()) {
            if (timer != null) {
                timer.cancel();
            }
            createDialog(dialog, this, R.string.win_game, gameController.originalWord());
        }
    }

    private void createDialog(AlertDialog.Builder dialog, final VersusActivity thisActivity, int title, String message) {
        dialog.setTitle(title).setMessage(message)
                .setPositiveButton(R.string.play_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        startActivity(new Intent(thisActivity, VersusActivity.class));
                    }
                }).show();
    }

    private void isChallenger(final Boolean accept) {
        UtilActivity.createProcessing(progressDialog);
        String url = MySharedPreference.PREFIX_URL + "notifications/challengeAccept";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        createdNewMatch();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (accept) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT);
                        }
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("challenger", String.valueOf(accept));
                params.put(MySharedPreference.TOKEN_TO_SERVER, FirebaseInstanceId.getInstance().getToken());
                params.put(MySharedPreference.ID_USER_TO, String.valueOf(opponentUser));
                return params;
            }
        };
        stringRequest.setTag(MySharedPreference.REQUEST_POST_TAG);
        mQueue.add(stringRequest);
    }

    private void createdNewMatch() {
        UtilActivity.createProcessing(progressDialog);
        String myToken = FirebaseInstanceId.getInstance().getToken();
        String url = MySharedPreference.PREFIX_URL + "matches/" + opponentUser + "/" + myToken;
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                finish();
                progressDialog.dismiss();
                gameController.getChallenger(response, opponentUser, opponentName, VersusActivity.this);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                progressDialog.dismiss();
                Toast.makeText(VersusActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        jsonRequest.setTag(MySharedPreference.REQUEST_GET_TAG);
        mQueue.add(jsonRequest);
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