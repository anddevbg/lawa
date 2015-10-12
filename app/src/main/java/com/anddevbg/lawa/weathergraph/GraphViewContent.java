package com.anddevbg.lawa.weathergraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by adri.stanchev on 14/09/2015.
 */
class GraphViewContent extends View {

    private Paint mBackgroundPaint;

    public GraphViewContent(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaintObjects();
        drawBackgroundLines(canvas);
    }

    private void initPaintObjects() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.GRAY);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(4);

        Paint mTemperaturePaint = new Paint();
        mTemperaturePaint.setColor(Color.rgb(255, 255, 255));
        mTemperaturePaint.setStyle(Paint.Style.STROKE);
        mTemperaturePaint.setTextSize(45);
        mTemperaturePaint.setAntiAlias(true);
        mTemperaturePaint.setStrokeWidth(4);
    }

    private void drawBackgroundLines(Canvas canvas) {
        int fullHeight = getHeight();
        List<Float> floatList = new ArrayList<>();
        for (float y = fullHeight / 6; y < fullHeight; y += fullHeight / 6) {
            canvas.drawLine(0, y, getWidth(), y, mBackgroundPaint);
            floatList.add(y);
        }
        int width = getWidth();
        for (float x = width / 4; x < width; x += width / 4) {
            mBackgroundPaint.setStrokeWidth(2);
            canvas.drawLine(x, 0, x, getHeight(), mBackgroundPaint);
        }
    }

}
