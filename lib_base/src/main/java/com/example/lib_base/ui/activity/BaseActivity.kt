package com.example.lib_base.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 *@author ZhiQiang Tu
 *@time 2021/10/31  22:15
 *@signature 我们不明前路，却已在路上
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        initView()
        initObserver()
    }

    open fun initObserver() {
    }

    open fun initView() {
    }

    open fun initListener() {

    }

}