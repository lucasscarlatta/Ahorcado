/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import asd.org.ahorcado.R;
import asd.org.ahorcado.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InputFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static float START_DRAW = 0f;
    public static float DIVIDER = 2f;
    public static int LESS_LINE = 5;
    public static int LIMIT_OFFSET = 40;
    public static int WIDTH_LINE = 8;

    private int offset = 50;
    private int longLine = 100;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InputFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InputFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InputFragment newInstance(String param1, String param2) {
        InputFragment fragment = new InputFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = new ImageView(getActivity());
        MainActivity myActivity = (MainActivity) getActivity();
        imageView.setImageBitmap(createBitmap("AC_I_I_Y", myActivity.widthDisplay(), myActivity.heightDisplay()));
        ViewGroup viewGroup = (ViewGroup) view;
        viewGroup.addView(imageView);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setProgressBar(int max, int progress) {
        ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        progressBar.setMax(max);
        progressBar.setProgress(progress);
    }

    private Paint createPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(WIDTH_LINE);
        paint.setAntiAlias(true);
        return paint;
    }

    private Bitmap createBitmap(String word, int widthDisplay, int heightDisplay) {
        //Bitmap.createBitmap(Width, Height, Config)
        Bitmap bitmap = Bitmap.createBitmap(widthDisplay, heightDisplay, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);

        wordPaint(widthDisplay, heightDisplay, canvas, createPaint(), word.toCharArray());
        return bitmap;
    }

    private void wordPaint(int widthDisplay, int heightDisplay, Canvas canvas, Paint paint, char[] wordChar) {
        float start = startPaint(widthDisplay, wordChar.length);
        for (int i = 0; i < wordChar.length; i++) {
            float nextLine = start + longLine;
            if (wordChar[i] == '_') {
                //canvas.drawLine(startX, startY, stopX, stopY, paint)
                canvas.drawLine(start, heightDisplay / 2, nextLine, heightDisplay / 2, paint);
            } else {
                float startWord = start;
                if (wordChar[i] == 'I') {
                    startWord += longLine / 2;
                }
                paint.setTextScaleX(2f);
                paint.setTextSize(80);
                canvas.drawText(String.valueOf(wordChar[i]), startWord, heightDisplay / DIVIDER, paint);
            }
            start = nextLine + offset;
        }
    }


    private float startPaint(int widthDisplay, int wordSize) {
        boolean draw = true;
        float middleDisplay = widthDisplay / DIVIDER;
        float startDraw = START_DRAW;
        while (draw) {
            startDraw = (longLine + offset) * (wordSize / DIVIDER);
            if (startDraw < middleDisplay) {
                draw = false;
                startDraw = middleDisplay - startDraw;
            } else {
                offset -= LESS_LINE;
                if (offset == LIMIT_OFFSET) {
                    longLine -= LESS_LINE;
                }
            }
        }
        return startDraw;
    }


}