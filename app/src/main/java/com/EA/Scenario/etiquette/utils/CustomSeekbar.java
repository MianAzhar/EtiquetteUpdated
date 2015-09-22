package com.EA.Scenario.etiquette.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by Mian on 9/20/2015.
 */
public class CustomSeekbar extends SeekBar {

    private Rect rect;
    private Paint paint ;
    private int seekbar_height;

    public CustomSeekbar(Context context) {
        super(context);


    }

    public CustomSeekbar(Context context, AttributeSet attrs) {

        super(context, attrs);
        rect = new Rect();
        paint = new Paint();
        seekbar_height = 6;
    }

    public CustomSeekbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        rect.set(0 + getThumbOffset(),
                (getHeight() / 2) - (seekbar_height/2),
                getWidth()- getThumbOffset(),
                (getHeight() / 2) + (seekbar_height/2));

        paint.setColor(Color.GRAY);

        canvas.drawRect(rect, paint);



        if (this.getProgress() > 2) {


            rect.set(getWidth() / 2,
                    (getHeight() / 2) - (seekbar_height/2),
                    getWidth() / 2 + (getWidth() / 4) * (getProgress() - 2),
                    getHeight() / 2 + (seekbar_height/2));

            paint.setColor(Color.BLACK);
            canvas.drawRect(rect, paint);

        }

        if (this.getProgress() < 2) {

            rect.set(getWidth() / 2 - ((getWidth() / 4) * (2 - getProgress())),
                    (getHeight() / 2) - (seekbar_height/2),
                    getWidth() / 2,
                    getHeight() / 2 + (seekbar_height/2));

            paint.setColor(Color.BLACK);
            canvas.drawRect(rect, paint);

        }

        super.onDraw(canvas);
    }
}
