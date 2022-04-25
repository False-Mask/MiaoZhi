package com.example.module_main.utils

import android.content.Context

/**
 *@author ZhiQiang Tu
 *@time 2022/4/25  15:52
 *@signature 我将追寻并获取我想要的答案
 */
private const val SP_NAME = "SP"
fun Context.put(key:String,value:String){
    val sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    sp.edit().apply {
        putString(key,value)
    }.commit()
}

fun Context.get(key: String):String{
    val sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    return sp.getString(key, "")!!
}