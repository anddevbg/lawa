package com.anddevbg.lawa.weathergraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
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
        initPaintObjects();
        drawBackgroundLines(canvas);
        drawGraphLines(canvas);
    }

    private void initPaintObjects() {
        mMinChartPaint = new Paint();
        mMinChartPaint.setStyle(Paint.Style.STROKE);
        mMinChartPaint.setColor(Color.rgb(29, 149, 255));
        mMinChartPaint.setAntiAlias(true);
        mMinChartPaint.setStrokeWidth(6);

        mMaxChartPaint = new Paint();
        mMaxChartPaint.setColor(Color.rgb(244, 255, 70));
        mMaxChartPaint.setStrokeWidth(6);
        mMaxChartPaint.setAntiAlias(true);
        mMaxChartPaint.setStyle(Paint.Style.STROKE);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.GRAY);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(4);

        mTemperaturePaint = new Paint();
        mTemperaturePaint.setColor(Color.rgb(255,255,255));
        mTemperaturePaint.setStyle(Paint.Style.STROKE);
        mTemperaturePaint.setTextSize(45);
        mTemperaturePaint.setAntiAlias(true);
        mTemperaturePaint.setStrokeWidth(4);
    }

    private void drawBackgroundLines(Canvas canvas) {
        int[] intArray = new int[]{30, 20, 10, 0, -10, -20};

        int fullHeight = getHeight();
        List<Float> floatList = new ArrayList<>();
        for (float y = fullHeight / 6; y < fullHeight; y += fullHeight / 6) {
            canvas.drawLine(0, y, getWidth(), y, mBackgroundPaint);
            floatList.add(y);
        }
        int width = getWidth();
        for (float x = width/4; x<width; x+= width/4) {
            mBackgroundPaint.setStrokeWidth(2);
            canvas.drawLine(x, 0, x, getHeight(), mBackgroundPaint);
        }
        for (int i = 0; i < 5; i++) {
            canvas.drawText(String.valueOf(intArray[i]), 0, floatList.get(i), mTemperaturePaint);
        }
    }

    public void setChartData(float[] maxCharData, float[] minCharData) {
        this.maxCharPoints = maxCharData.clone();
        this.minCharPoints = minCharData.clone();
        invalidate();
    }

    private void drawGraphLines(Canvas canvas) {
        Path path = new Path();
        if (maxCharPoints.length > 0) {
            path.moveTo(getXPos(0), getYPos(maxCharPoints[0]));
        }
        for (int i = 1; i < maxCharPoints.length; i++) {
            path.lineTo(getXPos(i), getYPos(maxCharPoints[i]));
            canvas.drawCircle(getXPos(i), getYPos(maxCharPoints[i]), 4, mMinChartPaint);
        }
        Path path2 = new Path();
        if (minCharPoints.length > 0) {
            path2.moveTo(getXPos(0), getYPos(minCharPoints[0]));
        }
        for (int y = 1; y < minCharPoints.length; y++) {
            path2.lineTo(getXPos(y), getYPos(minCharPoints[y]));
            canvas.drawCircle(getXPos(y), getYPos(minCharPoints[y]), 4, mMaxChartPaint);
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
