package asd.org.ahorcado.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import asd.org.ahorcado.R;

public class HelpFragment extends Fragment {


    public HelpFragment() {

    }
    public void enableHelp(boolean isEnable){
        Button helpButton= (Button) getView().findViewById(R.id.buttonHelp);
        helpButton.setEnabled(isEnable);
    }

    public void setCoinsToView(int coins){
        TextView txtCoins=(TextView)getView().findViewById(R.id.txtHelp);
        txtCoins.setText(String.valueOf(coins));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.help_fragment, container, false);
    }

}
