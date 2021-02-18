package com.example.fruitmachine.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BaseInterpolator;
/**
 * An interpolator where the rate of change starts and ends slowly but
 * accelerates through the middle.
 */
public class MyTigerInterpolator extends AccelerateDecelerateInterpolator {

    public float getInterpolation(float input) {
        return (float)(Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
    }

}