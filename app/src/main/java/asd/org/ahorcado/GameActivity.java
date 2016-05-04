/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import asd.org.controler.GameController;

public class GameActivity extends AppCompatActivity {

    GameController gameController = new GameController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView world = (TextView) findViewById(R.id.world);
        world.setText(gameController.setText());
    }

    public void guessLetter(View view) {
        Button button = (Button) view;
        button.setEnabled(false);
        CharSequence letter = button.getText();

        //TODO get original world.
        char[] originalWorld = "MUBER".toCharArray();

        TextView textViewWorld = (TextView) findViewById(R.id.world);
        final StringBuilder sb = new StringBuilder(textViewWorld.getText().length());
        sb.append(textViewWorld.getText());
        char[] myWorld = sb.toString().toCharArray();
        myWorld = gameController.findLetter(originalWorld, myWorld, letter);
        textViewWorld.setText(gameController.myNewWorld(myWorld));
    }

}
