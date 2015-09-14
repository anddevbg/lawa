package com.anddevbg.lawa.weathergraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by adri.stanchev on 14/09/2015.
 */
public class WeatherGraph extends View {

    private int mTemperature;
    private String mDayOfWeek;

    public WeatherGraph(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //TODO calculate canvas to appear similar on all devices

        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStrokeWidth(5);
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        canvas.drawLine(45, 45, 25, height-25, p);
        canvas.drawLine(25, height- 25, width -25, height-25, p);
    }
}
