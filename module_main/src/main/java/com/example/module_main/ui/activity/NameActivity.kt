package com.example.module_main.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.module_main.databinding.MainActivityNameBinding
import com.example.module_main.utils.put

class NameActivity : AppCompatActivity() {

    private val binding by lazy {
        MainActivityNameBinding.inflate(layoutInflater)
    }

    fun setName(value: String) {
        put("PET_NAME", value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#38b6ff")
        initView()
        initListener()
    }

    private fun initListener() {
        binding.btnConfirn.setOnClickListener {
            Intent(this, MapActivity::class.java).apply {
                putExtra("PET_NAME",binding.etText.text.toString())
                setName(binding.etText.text.toString())
            }.run {
                startActivity(this)
            }
        }
    }

    private fun initView() {
        initToobar()
    }

    private fun initToobar() {
        binding.tbBar.tvLeft.text = ""
        binding.tbBar.tvTitle.text = "智能硬件追踪"
    }
}