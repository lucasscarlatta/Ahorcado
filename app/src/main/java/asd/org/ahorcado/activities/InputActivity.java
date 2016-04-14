/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import asd.org.ahorcado.R;
import asd.org.ahorcado.exceptions.MatchLostException;
import asd.org.ahorcado.models.Match;

public class InputActivity extends AppCompatActivity {

    private Match match = new Match();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textViewWord = (TextView) findViewById(R.id.word);
        textViewWord.setText(match.getNewWord());
    }

    public void guessLetter(View view) {
        Button button = (Button) view;
        button.setEnabled(false);
        char letter = button.getText().toString().toCharArray()[0];
        TextView textViewWord = (TextView) findViewById(R.id.word);
        textViewWord.setText(match.execute(letter));
        try {
            if (match.afterExecute(letter)) {
                //Win
            }
        } catch (MatchLostException e) {
            //Lost
        }
    }

}
