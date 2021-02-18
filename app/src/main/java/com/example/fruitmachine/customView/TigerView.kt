package com.example.fruitmachine.customView

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.example.fruitmachine.R
import com.example.fruitmachine.dip2px
import com.example.fruitmachine.util.SoundPoolUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TigerView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var currentIndex = 0
    private var leftScore = 0
        set(value) {
            field = value
            invalidate()
        }
    private var rightScore = 0
        set(value) {
            field = value
            invalidate()
        }

    var buyScore = intArrayOf(0,0,0,0,0,0,0,0)

    var lastBuyScore = intArrayOf(0,0,0,0,0,0,0,0)

    fun changeBuyScrore(index:Int,score:Int){
        if (buyScore[index] <= 99 && rightScore > 0){
            buyScore[index] += score
            rightScore -= score
            invalidate()
        }
    }


    private val leftRect = Rect()
    private val rightRect = Rect()
    private val itemRect = Rect()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ITEM_WIDTH = 50f.dip2px
    private val ITEM_WIDTH_TWO = 45f.dip2px
    private val images = intArrayOf(R.mipmap.icon_orange,R.mipmap.icon_ring,R.mipmap.icon_king25,
        R.mipmap.icon_king,R.mipmap.icon_apple,R.mipmap.icon_apple2,
        R.mipmap.icon_mongo,R.mipmap.icon_watermelon,R.mipmap.icon_watermelon2,
        R.mipmap.icon_luck_right,R.mipmap.icon_apple,R.mipmap.icon_orange2,
        R.mipmap.icon_orange,R.mipmap.icon_ring,R.mipmap.icon_seven2,
        R.mipmap.icon_seven,R.mipmap.icon_apple,R.mipmap.icon_mongo2,
        R.mipmap.icon_mongo,R.mipmap.icon_star,R.mipmap.icon_star2,
        R.mipmap.icon_luck_left,R.mipmap.icon_apple,R.mipmap.icon_ring2)
    val point = ArrayList<HashMap<String,Float>>()
    var index = 0
        set(value) {
            if (field != value%24){
                field = value % 24
                invalidate()
                if (lightType == LIGNT_TYPE_NORMAL){
                    SoundPoolUtil.getInstance(context).play(1,R.raw.raw_ding)
                }else if(lightType == LIGNT_TYPE_TRAIN){
                    SoundPoolUtil.getInstance(context).play(1,R.raw.raw_train2)
                }
            }
        }

    var shining = false
        set(value) {
            if (field != value){
                field = value
                invalidate()
                SoundPoolUtil.getInstance(context).play(1,R.raw.raw_dang)
            }
        }

    var lightType = LIGNT_TYPE_NORMAL
        set(value) {
            field = value
//            if (field == LIGNT_TYPE_NORMAL){
//                SoundPoolUtil.getInstance(context).setResource(R.raw.raw_ding)
//            } else if (field == LIGNT_TYPE_SHINING){
//                SoundPoolUtil.getInstance(context).setResource(R.raw.raw_dang)
//            }
        }
    var font = Typeface.createFromAsset(context.assets, "font/digital.ttf")

    var isGaming = false
        set(value) {
            field = value
            if (!field){
                buyScore = intArrayOf(0,0,0,0,0,0,0,0)
            }
        }
    init {
        for (i in 0..5){
            val hashMap = HashMap<String,Float>()
            hashMap["x"] = i * ITEM_WIDTH
            hashMap["y"] = 0f
            point.add(hashMap)
        }
        for (i in 6..11){
            val hashMap = HashMap<String,Float>()
            hashMap["x"] = 6 * ITEM_WIDTH
            hashMap["y"] = (i-6) * ITEM_WIDTH
            point.add(hashMap)
        }
        for (i in 12..17){
            val hashMap = HashMap<String,Float>()
            hashMap["x"] = (18-i) * ITEM_WIDTH
            hashMap["y"] = 6 * ITEM_WIDTH
            point.add(hashMap)
        }
        for (i in 18..23){
            val hashMap = HashMap<String,Float>()
            hashMap["x"] = 0f
            hashMap["y"] = (24 - i) * ITEM_WIDTH
            point.add(hashMap)
        }
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //绘制电子屏
        paint.color = Color.BLACK
        canvas.drawRect(10f.dip2px,10f.dip2px,150f.dip2px,50f.dip2px,paint)
        canvas.drawRect(width - 150f.dip2px,10f.dip2px,width - 10f.dip2px,50f.dip2px,paint)

        paint.color = Color.RED
        paint.textSize = 30f.dip2px
        paint.setTypeface(font)
        paint.getTextBounds(leftScore.toString(),0,leftScore.toString().length,leftRect)
        canvas.drawText(leftScore.toString(),150f.dip2px - leftRect.right -10f.dip2px,45f.dip2px,paint)

        paint.getTextBounds(rightScore.toString(),0,rightScore.toString().length,rightRect)
        canvas.drawText(rightScore.toString(),width - 10f.dip2px - rightRect.right -10f.dip2px,45f.dip2px,paint)

        //绘制游戏转盘
        canvas.translate(0f,60f.dip2px)

        canvas.drawBitmap(getResourcesBitmap(5 * ITEM_WIDTH.toInt(), R.mipmap.bg_gongxifacai),
            ITEM_WIDTH,ITEM_WIDTH,paint)

        for (i in 0..23){
            canvas.drawBitmap(getResourcesBitmap(ITEM_WIDTH.toInt(), images[i]),
                point[i]["x"]!!,point[i]["y"]!!,paint)
        }

        //绘制光标
        if (lightType == LIGNT_TYPE_NORMAL){
            paint.color = Color.parseColor("#80ffffff")
            canvas.drawRect(point[index]["x"]!!,point[index]["y"]!!,point[index]["x"]!! + ITEM_WIDTH,point[index]["y"]!! + ITEM_WIDTH,paint)
            paint.color = Color.WHITE
        } else if (lightType == LIGNT_TYPE_SHINING){
            paint.color = Color.parseColor("#80ffffff")
            if (shining){
                for ((index,map) in point.withIndex()){
                    if ((index + 1) % 2 == 0){
                        canvas.drawRect(point[index]["x"]!!,point[index]["y"]!!,point[index]["x"]!! + ITEM_WIDTH,point[index]["y"]!! + ITEM_WIDTH,paint)
                    }
                }
            } else{
                for ((index,map) in point.withIndex()){
                    if ((index + 1) % 2 != 0){
                        canvas.drawRect(point[index]["x"]!!,point[index]["y"]!!,point[index]["x"]!! + ITEM_WIDTH,point[index]["y"]!! + ITEM_WIDTH,paint)
                    }
                }
            }
            paint.color = Color.WHITE
        } else if (lightType == LIGNT_TYPE_TRAIN){
            paint.color = Color.parseColor("#80ffffff")
            canvas.drawRect(point[index]["x"]!!,point[index]["y"]!!,point[index]["x"]!! + ITEM_WIDTH,point[index]["y"]!! + ITEM_WIDTH,paint)
            for(i in 0..5){
                canvas.drawRect(point[(index+i)%24]["x"]!!,point[(index+i)%24]["y"]!!,point[(index+i)%24]["x"]!! + ITEM_WIDTH,point[(index+i)%24]["y"]!! + ITEM_WIDTH,paint)
            }
            paint.color = Color.WHITE
        }



        //绘制押分屏幕

        canvas.translate(0f,370f.dip2px)

        for (i in 0..7){
            paint.color = Color.BLACK
            canvas.drawRect(ITEM_WIDTH_TWO * i,0f,ITEM_WIDTH_TWO * i + 30f.dip2px,30f.dip2px,paint)
            paint.color = Color.RED
            paint.textSize = 20f.dip2px
            paint.setTypeface(font)
            paint.getTextBounds(buyScore[i].toString(),0,buyScore[i].toString().length - 1,itemRect)
            canvas.drawText(buyScore[i].toString().toString(),ITEM_WIDTH_TWO * i + 30f - itemRect.right ,25f.dip2px,paint)

        }
    }

    private fun getResourcesBitmap(width:Int,resourceId:Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources,resourceId,options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources,resourceId,options)
    }

    fun addCoin(num:Int){
        rightScore += num*10
    }

    fun gameStart(){
        if(leftScore!=0){
            rightScore+=leftScore
            leftScore=0
            return
        }

        var buyCount = 0
        for ((index,i) in buyScore.withIndex()){
            buyCount += i
        }
        if (buyCount == 0){ //没下注
            var lastBuyCount = 0
            for ((index,i) in lastBuyScore.withIndex()){
                lastBuyCount += i
            }
            if (lastBuyCount != 0 && lastBuyCount<= rightScore){ //上次有下注记录
                for ((index,i) in lastBuyScore.withIndex()){
                    changeBuyScrore(index,i)
                }
                invalidate()
            } else{
                return
            }
        } else{
            for ((index,i) in buyScore.withIndex()){
                lastBuyScore[index] = i
            }
        }
        isGaming = true

        var target = Math.random() * 100
        if (target<10.0){
            runTrainGame()
        } else if (target in 10.0..20.0){
            runKingGame()
        } else{
            runNormalGame()
        }


//        if (buyCount == 0){
//            return
//        } else if (buyCount > rightScore){
//            return
//        } else{
//            rightScore -= buyCount
//            isGaming = true
//            runNormalGame()
//        }


    }

    private fun runTrainGame(){
        SoundPoolUtil.getInstance(context).play(1,R.raw.raw_train,1)
        lightType = LIGNT_TYPE_TRAIN

        var reward = 0

        var target = Math.random() * 100
        if (target<5.0){
            target = 5.0
            reward= buyScore[7] * 2
        } else if (target in 5.0..10.0){
            target = 8.0
            reward= buyScore[1] * 2
        } else if (target in 10.0..15.0){
            target = 11.0
            reward= buyScore[6] * 2
        } else if (target in 15.0..20.0){
            target = 14.0
            reward= buyScore[2] * 2
        } else if (target in 20.0..25.0){
            target = 17.0
            reward= buyScore[5] * 2
        } else if (target in 25.0..30.0){
            target = 20.0
            reward= buyScore[3] * 2
        } else if (target in 30.0..35.0){
            target = 23.0
            reward= buyScore[4] * 2
        } else if (target in 35.0..40.0){
            target = 4.0
            reward = buyScore[7] * 5
        } else if (target in 40.0..45.0){
            target = 10.0
            reward = buyScore[7] * 5
        } else if (target in 45.0..50.0){
            target = 16.0
            reward = buyScore[7] * 5
        } else if (target in 50.0..55.0){
            target = 22.0
            reward = buyScore[7] * 5
        }
        else if (target in 55.0..80.0){
            target = 9.0
        } else{
            target = 21.0
        }


        target = target % 24 + 72
        val objectAnimator = ObjectAnimator.ofInt(this,"index",currentIndex,target.toInt())
        currentIndex = target.toInt() % 24
        objectAnimator.startDelay = 3000
        objectAnimator.duration = 10000
        objectAnimator.start()

        postDelayed(Runnable {
            leftScore+=reward
            isGaming = false
        },13000)

    }

    private fun runKingGame(){

        SoundPoolUtil.getInstance(context).play(1,R.raw.raw_gongxi,1)
        Shining(20)


        var overType  = OVER_TYPE_KING
        var reward = 0
        var target = Math.random() * 100
        if (target<40.0){
            target = 2.0
            reward= buyScore[0] * 25
        } else{
            target = 3.0
            reward= buyScore[0] * 50
        }

        target = target % 24 + 72
        val objectAnimator = ObjectAnimator.ofInt(this,"index",currentIndex,target.toInt())
        currentIndex = target.toInt() % 24
        objectAnimator.interpolator = FastOutSlowInInterpolator ()
        objectAnimator.startDelay = 4000
        objectAnimator.duration = 5000
        objectAnimator.start()

        postDelayed(Runnable {
            leftScore+=reward
            runLuckGame(overType)
        },10000)


    }


    private fun runNormalGame() {

        var overType  = OVER_TYPE_NORMAL
        var reward = 0

        var target = Math.random() * 100
        if (target<5.0){
            target = 5.0
            reward= buyScore[7] * 2
        } else if (target in 5.0..10.0){
            target = 8.0
            reward= buyScore[1] * 2
        } else if (target in 10.0..15.0){
            target = 11.0
            reward= buyScore[6] * 2
        } else if (target in 15.0..20.0){
            target = 14.0
            reward= buyScore[2] * 2
        } else if (target in 20.0..25.0){
            target = 17.0
            reward= buyScore[5] * 2
        } else if (target in 25.0..30.0){
            target = 20.0
            reward= buyScore[3] * 2
        } else if (target in 30.0..35.0){
            target = 23.0
            reward= buyScore[4] * 2
        } else if (target in 35.0..40.0){
            target = 4.0
            reward = buyScore[7] * 5
        } else if (target in 40.0..45.0){
            target = 10.0
            reward = buyScore[7] * 5
        } else if (target in 45.0..50.0){
            target = 16.0
            reward = buyScore[7] * 5
        } else if (target in 50.0..55.0){
            target = 22.0
            reward = buyScore[7] * 5
        }
        else if (target in 55.0..80.0){
            target = 9.0
            overType = OVER_TYPE_RIGHT
        } else{
            target = 21.0
            overType = OVER_TYPE_LEFT
        }


        target = target % 24 + 72
        val objectAnimator = ObjectAnimator.ofInt(this,"index",currentIndex,target.toInt())
        currentIndex = target.toInt() % 24
        objectAnimator.interpolator = FastOutSlowInInterpolator ()
        objectAnimator.duration = 5000
        objectAnimator.start()


        postDelayed(Runnable {
            leftScore+=reward
            if (overType != OVER_TYPE_NORMAL){
                runLuckGame(overType)
            } else{
                isGaming = false
            }
        },5000)

    }

    private fun runLuckGame(overtype:Int) {
        var target = Math.random() * 100
        var reward = 0
        
        if (overtype == OVER_TYPE_LEFT){
            if (target<15.0){
                target = 0.0
                reward = buyScore[6] * 10
            } else if (target in 15.0..30.0){
                target = 1.0
                reward = buyScore[4] * 10
            } else if (target in 30.0..45.0){
                target = 6.0
                reward = buyScore[5] * 10
            } else if (target in 45.0..60.0){
                target = 12.0
                reward = buyScore[6] * 10
            } else if (target in 60.0..75.0){
                target = 13.0
                reward = buyScore[4] * 10
            } else if (target in 75.0..80.0){
                target = 18.0
                reward = buyScore[5] * 10
            } else{
                SoundPoolUtil.getInstance(context).play(1,R.raw.raw_fail)
                postDelayed(Runnable {
                    isGaming = false
                },2000)
                return
            }
        } else {
            if (target<30.0){
                target = 7.0
                reward = buyScore[1] * 20
            } else if (target in 30.0..60.0){
                target = 15.0
                reward = buyScore[2] * 20
            } else if (target in 60.0..80.0){
                target = 19.0
                reward = buyScore[3] * 20
            } else{
                SoundPoolUtil.getInstance(context).play(1,R.raw.raw_fail)
                postDelayed(Runnable {
                    isGaming = false
                },2000)
                return
            }
        }
        Shining(10)
        target = target % 24 + 48
        val objectAnimator = ObjectAnimator.ofInt(this,"index",currentIndex,target.toInt())
        currentIndex = target.toInt() % 24
        objectAnimator.interpolator = AccelerateInterpolator()
        objectAnimator.duration = 3000
        objectAnimator.startDelay = 2000
//        objectAnimator.interpolator = DecelerateInterpolator()
        objectAnimator.start()

        postDelayed(Runnable {
            leftScore+=reward
            isGaming = false
        },5000)
    }

    private fun Shining(i:Int){
        val timer = Timer()
        var count = 0
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                shining = !shining
                count ++
                if ( count >= i){
                    timer.cancel();
                    lightType = LIGNT_TYPE_NORMAL
                }
            }
        }
        timer.schedule(task,0,200);
        lightType = LIGNT_TYPE_SHINING
    }

    companion object {
        const val START_TYPE_NORMAL = 1

        const val OVER_TYPE_NORMAL = 11
        const val OVER_TYPE_LEFT = 12
        const val OVER_TYPE_RIGHT = 13
        const val OVER_TYPE_KING = 14

        const val LIGNT_TYPE_NORMAL = 21
        const val LIGNT_TYPE_SHINING = 22
        const val LIGNT_TYPE_TRAIN = 23

    }
}