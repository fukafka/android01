package com.norajet.firstgame

import android.app.Activity
import android.os.Bundle

class LinearLayoutActivity: Activity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)//初始化
        setContentView(R.layout.activity_linear)//画
    }
}