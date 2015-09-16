package com.anddevbg.lawa.weathergraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by adri.stanchev on 14/09/2015.
 */
public class WeatherGraph extends View {

    private float[] points = new float[]{};

    public WeatherGraph(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGraph(canvas);
    }

    public void setChartData(float[] chartData) {
        this.points = chartData.clone();
        invalidate();
    }

    private void drawGraph(Canvas canvas) {
        Paint graphPaint = new Paint();
        Paint backgroundPaint = new Paint();
        Path path = new Path();
        path.moveTo(getXPos(0), getYPos(points[0]));
        for(int i=1; i<points.length; i++) {
            path.lineTo(getXPos(i), getYPos(points[i]));
        }
        graphPaint.setStyle(Paint.Style.STROKE);
        graphPaint.setColor(Color.BLUE);
        graphPaint.setAntiAlias(true);
        graphPaint.setStrokeWidth(6);
        canvas.drawPath(path, graphPaint);
        int fullHeight = getHeight();
        for(int h2= fullHeight/6; h2<fullHeight; h2 += fullHeight/6) {
            backgroundPaint.setColor(Color.GRAY);
            backgroundPaint.setAntiAlias(true);
            backgroundPaint.setStyle(Paint.Style.STROKE);
            backgroundPaint.setStrokeWidth(4);
            Log.d("qwe", "h2 is " + h2);
            canvas.drawLine(0, h2, getWidth(),h2, backgroundPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float getMaxTemp() {
        return 60;
    }

    private float getYPos(float value) {
        float height = getHeight();
        float maxValue = getMaxTemp();
        value = (value / maxValue) * height;
        value = height - value;
        return value;
    }

    private float getXPos(float value) {
        float width = getWidth();
        float maxValue = points.length-1;
        value = (value / maxValue) * width;
        return value;
    }
}
