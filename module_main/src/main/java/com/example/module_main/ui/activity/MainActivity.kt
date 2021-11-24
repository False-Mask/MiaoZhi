package com.example.module_main.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.module_main.R
import com.example.module_main.databinding.MainActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: MainActivityMainBinding by lazy {
        MainActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_main)
    }
}