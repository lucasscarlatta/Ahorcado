/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import asd.org.ahorcado.R;
import asd.org.ahorcado.fragments.InputFragment;
import asd.org.ahorcado.models.AbstractMatch;
import asd.org.ahorcado.models.Match;

public class MainActivity extends FragmentActivity {

    private static final int INTERVAL = 2000; //2 seconds

    private AbstractMatch match = new Match();
    private long firstClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Button button = (Button) findViewById(R.id.play_game);
        button.setVisibility(View.INVISIBLE);
        //match.initialGame();
        //setInitialActivity();
        //UtilActivity.setBackground(view, match.getLife());
        getFragmentManager().beginTransaction().add(R.id.content, new InputFragment()).commit();
    }

    public void guessLetter(View view) {
        Button button = (Button) view;
        button.setEnabled(false);
        char letter = button.getText().toString().toCharArray()[0];
    }

    private void setInitialActivity() {
        InputFragment myFragment = (InputFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
        myFragment.setProgressBar(match.getLife(), match.getLife());
    }

    public int widthDisplay() {
        return sizeDisplay().x;
    }

    public int heightDisplay() {
        return sizeDisplay().y;
    }

    private Point sizeDisplay() {
        Display display = getWindowManager().getDefaultDisplay();
        Point tam = new Point();
        display.getSize(tam);
        return tam;
    }

}
