package com.anddevbg.lawa.weathergraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


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
        drawBackgroundLines(canvas);
//        drawTemperatureText(canvas);
    }

    private void drawTemperatureText(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.YELLOW);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(4);
        textPaint.setTextSize(50);
        canvas.drawText("HELLO", getWidth()/2, getHeight()/2, textPaint);
    }

    private void drawBackgroundLines(Canvas canvas) {
        int[] intArray = new int[]{30,20,10,0,-10,-20};
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(4);
        Paint temperaturePaint = new Paint();
        temperaturePaint.setColor(Color.CYAN);
        temperaturePaint.setStyle(Paint.Style.STROKE);
        temperaturePaint.setTextSize(45);
        temperaturePaint.setAntiAlias(true);
        temperaturePaint.setStrokeWidth(4);
        int fullHeight = getHeight();
        List<Float> floatList = new ArrayList<>();
        for(float y= fullHeight/6; y<fullHeight; y += fullHeight/6) {
            canvas.drawLine(0, y, getWidth(), y, backgroundPaint);
            floatList.add(y);
        }
        for(int i =0; i<6; i++) {
            canvas.drawText(String.valueOf(intArray[i]), 0, floatList.get(i), temperaturePaint);
        }
    }

    public void setChartData(float[] chartData) {
        this.points = chartData.clone();
        invalidate();
    }

    private void drawGraph(Canvas canvas) {
        Paint graphPaint = new Paint();
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
