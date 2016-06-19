package asd.org.ahorcado.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import asd.org.ahorcado.R;
import asd.org.ahorcado.helpers.UserAdapter;

public class VersusActivity extends AppCompatActivity {

    private String opponentName;
    private Long opponentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        opponentName = b.getString(UserAdapter.COLUMN_NAME);
        opponentUser = b.getLong(UserAdapter.COLUMN_ID);
        setContentView(R.layout.activity_versus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
