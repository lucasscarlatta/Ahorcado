/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import asd.org.ahorcado.R;
import asd.org.ahorcado.exceptions.MatchLostException;
import asd.org.ahorcado.models.Match;

public class InputActivity extends AppCompatActivity {

    private static final int INTERVAL = 2000; //2 seconds

    private Match match = new Match();
    private long firstClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        match.initialGame();
        setInitialActivity();
    }

    @Override
    public void onBackPressed(){
        if (firstClickTime + INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            startActivity(new Intent(this, MainActivity.class));
        }else {
            Toast.makeText(this, R.string.back_button, Toast.LENGTH_SHORT).show();
        }
        firstClickTime = System.currentTimeMillis();
    }

    public void guessLetter(View view) {
        Button button = (Button) view;
        button.setEnabled(false);
        char letter = button.getText().toString().toCharArray()[0];
        TextView textViewWord = (TextView) findViewById(R.id.word);
        textViewWord.setText(match.execute(letter));
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(android.R.drawable.ic_dialog_info);
        try {
            if (match.afterExecute(letter)) {
                createDialog(dialog, R.string.win_game, this);
            }
        } catch (MatchLostException e) {
            createDialog(dialog, R.string.lost_game, this);
        } finally {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setProgress(match.getLife());
            setBackground();
        }
    }

    private void setInitialActivity(){
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewWord = (TextView) findViewById(R.id.word);
        textViewWord.setText(match.getNewWord());
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(match.getLife());
        progressBar.setProgress(match.getLife());
    }

    private void createDialog(AlertDialog.Builder dialog, int title, final InputActivity thisActivity) {
        dialog.setTitle(title)
                .setPositiveButton(R.string.play_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(thisActivity, InputActivity.class));
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(thisActivity, MainActivity.class));
                    }
                }).show();
    }

    private void setBackground() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.input_layout);
        switch (match.getLife()) {
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