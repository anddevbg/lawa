package com.anddevbg.lawa.weathergraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.eftimoff.androipathview.PathView;

/**
 * Created by adri.stanchev on 07/10/2015.
 */
public class WeatherGraphView extends FrameLayout {

    private GraphViewContent mGraphViewContent;
    private PathView mPathViewMin;
    private PathView mPathViewMax;

    private Path mPathMin;
    private Path mPathMax;


    public WeatherGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("asd", "in weatherGraphView constructor");
        mGraphViewContent = new GraphViewContent(context, attrs);
        mPathViewMin = new PathView(context);
        mPathViewMax = new PathView(context);
        addView(mGraphViewContent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mPathViewMin, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mPathViewMax, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public void setArrays(float[] min, float[] max) {
        mPathMax = new Path();
        mPathMin = new Path();
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
