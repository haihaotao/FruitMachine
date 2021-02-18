package com.example.kotlinnavigationdemo.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.kotlinnavigationdemo.base.MyApplication

class ImageUtils {
    companion object {
        fun getResourcesBitmap(width:Int,resourceId:Int): Bitmap {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeResource(MyApplication.context.resources,resourceId,options)
            options.inJustDecodeBounds = false
            options.inDensity = options.outWidth
            options.inTargetDensity = width
            return BitmapFactory.decodeResource(MyApplication.context.resources,resourceId,options)
        }
    }
}