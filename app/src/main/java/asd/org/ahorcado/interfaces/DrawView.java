/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.interfaces;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class DrawView extends View {

    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.GREEN);
        Rect line = new Rect(2, 0, 2, 0);
        canvas.drawRect(line, paint);
    }
}