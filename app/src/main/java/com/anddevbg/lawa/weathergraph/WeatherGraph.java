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

    private Paint mMaxChartPaint;
    private Paint mMinChartPaint;
    private Paint mTemperaturePaint;
    private Paint mBackgroundPaint;

    private float[] maxCharPoints = new float[]{};
    private float[] minCharPoints = new float[]{};

    public WeatherGraph(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaint();
        drawBackgroundLines(canvas);
        drawGraph(canvas);
//        drawTemperatureText(canvas);
    }

    private void initPaint() {
        mMinChartPaint = new Paint();
        mMinChartPaint.setStyle(Paint.Style.STROKE);
        mMinChartPaint.setColor(Color.BLUE);
        mMinChartPaint.setAntiAlias(true);
        mMinChartPaint.setStrokeWidth(6);

        mMaxChartPaint = new Paint();
        mMaxChartPaint.setColor(Color.YELLOW);
        mMaxChartPaint.setStrokeWidth(4);
        mMaxChartPaint.setStyle(Paint.Style.STROKE);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.GRAY);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(4);

        mTemperaturePaint = new Paint();
        mTemperaturePaint.setColor(Color.CYAN);
        mTemperaturePaint.setStyle(Paint.Style.STROKE);
        mTemperaturePaint.setTextSize(45);
        mTemperaturePaint.setAntiAlias(true);
        mTemperaturePaint.setStrokeWidth(4);
    }

    private void drawTemperatureText(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.YELLOW);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(4);
        textPaint.setTextSize(50);
        canvas.drawText("HELLO", getWidth() / 2, getHeight() / 2, textPaint);
    }

    private void drawBackgroundLines(Canvas canvas) {
        int[] intArray = new int[]{30, 20, 10, 0, -10, -20};

        int fullHeight = getHeight();
        List<Float> floatList = new ArrayList<>();
        for (float y = fullHeight / 6; y < fullHeight; y += fullHeight / 6) {
            canvas.drawLine(0, y, getWidth(), y, mBackgroundPaint);
            floatList.add(y);
        }
        for (int i = 0; i < 6; i++) {
            canvas.drawText(String.valueOf(intArray[i]), 0, floatList.get(i), mTemperaturePaint);
        }
    }

    public void setChartData(float[] maxCharData, float[] minCharData) {
        this.maxCharPoints = maxCharData.clone();
        this.minCharPoints = minCharData.clone();
        invalidate();
    }

    private void drawGraph(Canvas canvas) {
        Path path = new Path();
        if(maxCharPoints.length>0) {
            path.moveTo(getXPos(0), getYPos(maxCharPoints[0]));
        } else {
            Log.d("graph", "length is 0");
        }
        for (int i = 0; i < maxCharPoints.length; i++) {
            path.lineTo(getXPos(i), getYPos(maxCharPoints[i]));
            canvas.drawCircle(getXPos(i), getYPos(maxCharPoints[i]), 5, mMaxChartPaint);
            Log.d("graph", "moving line to x= " + getXPos(i) + "  y= " + getYPos(maxCharPoints[i]));
        }
        Path path2 = new Path();
        if(minCharPoints.length > 0) {
        path2.moveTo(getXPos(0), getYPos(minCharPoints[0]));
        } else {
            Log.d("graph", "length is 0");
        }
        for (int y = 0; y < minCharPoints.length; y++) {
            path2.lineTo(getXPos(y), getYPos(minCharPoints[y]));
            canvas.drawCircle(getXPos(y), getYPos(minCharPoints[y]), 5, mMinChartPaint);
            Log.d("graph", "moving line to x= " + getXPos(y) + "  y= " + getYPos(minCharPoints[y]));
        }
        canvas.drawPath(path, mMinChartPaint);
        canvas.drawPath(path2, mMaxChartPaint);
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
        float maxValue = maxCharPoints.length - 1;
        value = (value / maxValue) * width;
        return value;
    }
}
