package com.example.fruitmachine.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;

import com.example.fruitmachine.R;

import java.util.HashMap;

public class SoundPoolUtil {
    private static SoundPoolUtil soundPoolUtil;
    private SoundPool soundPool;
    private Context context;
    private HashMap<Integer,Integer> map = new HashMap<>();
    private boolean currentSpecial = false;

    //单例模式
    public static SoundPoolUtil getInstance(Context context) {
        if (soundPoolUtil == null)
            soundPoolUtil = new SoundPoolUtil(context);
        return soundPoolUtil;
    }

    @SuppressLint("NewApi")//这里初始化SoundPool的方法是安卓5.0以后提供的新方式
    private SoundPoolUtil(Context context) {
//        soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 0);
        this.context = context;
        soundPool = new SoundPool.Builder().build();
        //加载音频文件
        int idDing = soundPool.load(context, R.raw.raw_ding, 1);
        int idDang = soundPool.load(context, R.raw.raw_dang, 1);
        int idFail = soundPool.load(context, R.raw.raw_fail, 1);
        int idGongxi = soundPool.load(context, R.raw.raw_gongxi, 1);
        int idWin = soundPool.load(context, R.raw.raw_win, 1);
        int idTrain = soundPool.load(context, R.raw.raw_train, 1);
        map.put(R.raw.raw_ding,idDing);
        map.put(R.raw.raw_dang,idDang);
        map.put(R.raw.raw_fail,idFail);
        map.put(R.raw.raw_gongxi,idGongxi);
        map.put(R.raw.raw_win,idWin);
        map.put(R.raw.raw_train,idTrain);

    }

    public void play(int number,int resource) {
        //播放音频
        if (currentSpecial){
            return;
        }
        if (map.containsKey(resource)){
            soundPool.play(map.get(resource), 1, 1, 0, number-1, 1);
        } else {
            soundPool = new SoundPool.Builder().build();
            int id = soundPool.load(context, resource, 1);
            map.put(resource,id);
            soundPool.play(map.get(resource), 1, 1, 0, number-1, 1);
        }
    }
    public void play(int number,int resource,int priority) {
        //播放音频
        play(number,resource);
        currentSpecial = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentSpecial = false;
            }
        },3000);
    }

}
