/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asd.org.ahorcado.R;

public class InputFragment extends Fragment {

    public InputFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.input_fragment, container, false);
    }

}