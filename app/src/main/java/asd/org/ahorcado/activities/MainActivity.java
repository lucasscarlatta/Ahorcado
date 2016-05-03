/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.activities;

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

import asd.org.ahorcado.R;
import asd.org.ahorcado.fragments.WordFragment;

public class MainActivity extends AppCompatActivity {

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void lunchGame(View view) {
        Button button = (Button) findViewById(R.id.play_game);
        button.setVisibility(View.INVISIBLE);
        TableLayout tl = (TableLayout) this.findViewById(R.id.tableLayout);
        tl.setVisibility(View.VISIBLE);
        WordFragment f = (WordFragment) getFragmentManager().findFragmentById(R.id.WordFragment);
        f.updateImage("_______", widthDisplay(), heightDisplay());
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
        Button button = (Button) view;
        button.setEnabled(false);
        char letter = button.getText().toString().toCharArray()[0];
        WordFragment f = (WordFragment) getFragmentManager().findFragmentById(R.id.WordFragment);
        f.updateImage(letter + "_______", widthDisplay(), heightDisplay());
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