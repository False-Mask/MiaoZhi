package com.example.lib_base.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 *@author ZhiQiang Tu
 *@time 2021/10/25  20:20
 *@signature 我们不明前路，却已在路上
 */
abstract class BaseApp : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var app: Context
    }

    override fun onCreate() {
        super.onCreate()
        app = applicationContext
    }
}