package com.example.fruitmachine

import android.content.res.Resources
import android.util.TypedValue
import android.view.View

val Float.dip2px:Float
    get() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this, Resources.getSystem().displayMetrics)
    }

val Float.px2dip:Float
    get() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,this, Resources.getSystem().displayMetrics)
    }
