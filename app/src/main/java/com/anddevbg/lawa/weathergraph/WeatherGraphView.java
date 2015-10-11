package com.anddevbg.lawa.weathergraph;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eftimoff.androipathview.PathView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adri.stanchev on 07/10/2015.
 */
public class WeatherGraphView extends FrameLayout {

    private PathView mPathViewMin;
    private PathView mPathViewMax;

    private LinearLayout mTextViewsLinearLayout;
    private LinearLayout mTemperatureLinearLayout;

    private List<TextView> mTextViews;
    private TextView mDayOfWeekTextView;
    private TextView mDayOfWeekTextView2;
    private TextView mDayOfWeekTextView3;
    private TextView mDayOfWeekTextView4;
    private TextView mDayOfWeekTextView5;
    private WindowManager mWindowManager;

    private int mWidth;
    private int mHeight;



    public WeatherGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = mWindowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mWidth = size.x;
        mHeight = size.y;
        mTextViews = new ArrayList<>();
        GraphViewContent mGraphViewContent = new GraphViewContent(context, attrs);
        initPathViews(context);
        initLinearLayoutHorizontal(context);
        initLinearLayoutVertical(context);

        addView(mGraphViewContent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mPathViewMin, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mPathViewMax, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mTemperatureLinearLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mTextViewsLinearLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    private void initLinearLayoutVertical(Context context) {
        mTemperatureLinearLayout = new LinearLayout(context);
        mTemperatureLinearLayout.setOrientation(LinearLayout.VERTICAL);
        int[] tempArray = new int[] {30,20,10,0,-10,-20};
        TextView temp1 = new TextView(context);
        TextView temp2 = new TextView(context);
        TextView temp3 = new TextView(context);
        TextView temp4 = new TextView(context);
        TextView temp5 = new TextView(context);
        TextView temp6 = new TextView(context);
        List<TextView> tempList = new ArrayList<>();
        tempList.add(temp1);
        tempList.add(temp2);
        tempList.add(temp3);
        tempList.add(temp4);
        tempList.add(temp5);
        tempList.add(temp6);

        for(int i=0; i<tempList.size();i++) {
            tempList.get(i).setGravity(Gravity.BOTTOM);
            tempList.get(i).setHeight(mHeight/8);
            tempList.get(i).setText(String.valueOf(tempArray[i]));
            Log.d("graph", "tempArray[i] is: " + String.valueOf(tempArray[i]));
            mTemperatureLinearLayout.addView(tempList.get(i));
        }


    }

    private void initPathViews(Context context) {
        mPathViewMin = new PathView(context);
        mPathViewMax = new PathView(context);
    }

    private void initLinearLayoutHorizontal(Context context) {

        mDayOfWeekTextView = new TextView(context);
        mDayOfWeekTextView2 = new TextView(context);
        mDayOfWeekTextView2.setBackgroundColor(Color.BLACK);
        mDayOfWeekTextView3 = new TextView(context);
        mDayOfWeekTextView3.setGravity(Gravity.CENTER);
        mDayOfWeekTextView3.setBackgroundColor(Color.BLACK);
        mDayOfWeekTextView4 = new TextView(context);
        mDayOfWeekTextView4.setGravity(Gravity.END);
        mDayOfWeekTextView4.setBackgroundColor(Color.BLACK);
        mDayOfWeekTextView5 = new TextView(context);
        mDayOfWeekTextView5.setGravity(Gravity.END);

        mTextViews.add(mDayOfWeekTextView);
        mTextViews.add(mDayOfWeekTextView2);
        mTextViews.add(mDayOfWeekTextView3);
        mTextViews.add(mDayOfWeekTextView4);
        mTextViews.add(mDayOfWeekTextView5);
        for(int i=0; i<mTextViews.size(); i++) {
            mTextViews.get(i).setWidth(mWidth /5);
            mTextViews.get(i).setTextSize(15);
        }
        Log.d("graph", "mWidth/5 is " + mWidth /5 + 10);

        mTextViewsLinearLayout = new LinearLayout(context);
        mTextViewsLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTextViewsLinearLayout.addView(mDayOfWeekTextView);
        mTextViewsLinearLayout.addView(mDayOfWeekTextView2);
        mTextViewsLinearLayout.addView(mDayOfWeekTextView3);
        mTextViewsLinearLayout.addView(mDayOfWeekTextView4);
        mTextViewsLinearLayout.addView(mDayOfWeekTextView5);
//        addView(mTemperatureLinearLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public void setArrays(float[] min, float[] max, List<String> daysOfWeek) {
        Path mPathMax = new Path();
        Path mPathMin = new Path();
        if (max.length > 0) {
            mPathMax.moveTo(getXPos(0, max), getYPos(max[0]));
        }
        for (int i = 1; i < max.length; i++) {
            mPathMax.lineTo(getXPos(i, max), getYPos(max[i]));
        }
        if (min.length > 0) {
            mPathMin.moveTo(getXPos(0, max), getYPos(min[0]));
        }
        for (int y = 1; y < min.length; y++) {
            mPathMin.lineTo(getXPos(y, max), getYPos(min[y]));
        }

        for(int i = 0; i<daysOfWeek.size(); i++) {
            mTextViews.get(i).setText(daysOfWeek.get(i));
            Log.d("graph", "city name text view is + " + daysOfWeek.get(i));
        }
        mPathViewMax.setPath(mPathMax);
        mPathViewMax.setPathColor(Color.WHITE);
        mPathViewMax.setPathWidth(4);
        mPathViewMax.getPathAnimator()
                .delay(100)
                .duration(1000)
                .interpolator(new AccelerateDecelerateInterpolator())
                .start();
        mPathViewMin.setPath(mPathMin);
        mPathViewMin.setPathColor(Color.GREEN);
        mPathViewMin.setPathWidth(4);
        mPathViewMin.getPathAnimator()
                .delay(100)
                .duration(1000)
                .interpolator(new AccelerateDecelerateInterpolator())
                .start();
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

    private float getXPos(float value, float[] max) {
        float width = getWidth();
        float maxValue = max.length - 1;
        value = (value / maxValue) * width;
        return value;
    }

}
