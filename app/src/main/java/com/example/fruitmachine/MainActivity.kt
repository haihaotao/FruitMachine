package com.example.fruitmachine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.children
import com.example.fruitmachine.customView.TigerView
import com.example.fruitmachine.util.SoundPoolUtil
import com.example.kotlinnavigationdemo.base.MyApplication.Companion.context

class MainActivity : AppCompatActivity() {

    private lateinit var tigerView: TigerView
    private lateinit var btn_coin: Button
    private lateinit var btn_start: Button
    private lateinit var ll: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    fun initView() {
        tigerView = findViewById(R.id.tiger_view)
        btn_coin = findViewById(R.id.btn_coin)
        btn_start = findViewById(R.id.btn_start)
        ll = findViewById(R.id.ll)


        btn_start.setOnClickListener(View.OnClickListener {
            if (!tigerView.isGaming){
                tigerView.gameStart()
            }
            SoundPoolUtil.getInstance(context).play(1,R.raw.raw_dang)
        })


        btn_coin.setOnClickListener(View.OnClickListener {
            tigerView.addCoin(1)
            SoundPoolUtil.getInstance(context).play(1,R.raw.raw_dang)
        })

        for ((index,v) in ll.children.withIndex()){
            v.setOnClickListener(View.OnClickListener {
                tigerView.changeBuyScrore(index,1)
                SoundPoolUtil.getInstance(context).play(1,R.raw.raw_dang)
            })
        }
    }

}