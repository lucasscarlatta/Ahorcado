package asd.org.ahorcado.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import asd.org.ahorcado.R;

public class WordFragment extends Fragment {

    public static float START_DRAW = 0f;
    public static float DIVIDER = 2f;
    public static int LESS_LINE = 5;
    public static int LIMIT_OFFSET = 40;
    public static int WIDTH_LINE = 8;

    private int offset = 50;
    private int longLine = 100;

    ImageView imageView;

    public WordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveBundle) {
        View view = inflater.inflate(R.layout.word_fragment, container, false);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        return view;
    }

    public void updateImage(String word, int width, int height) {
        imageView.setImageBitmap(createBitmap(word, width, height));
    }

    private Bitmap createBitmap(String word, int widthDisplay, int heightDisplay) {
        //Bitmap.createBitmap(Width, Height, Config)
        Bitmap bitmap = Bitmap.createBitmap(widthDisplay, heightDisplay, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);

        wordPaint(widthDisplay, heightDisplay, canvas, createPaint(), word.toCharArray());
        return bitmap;
    }

    private Paint createPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(WIDTH_LINE);
        paint.setAntiAlias(true);
        return paint;
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