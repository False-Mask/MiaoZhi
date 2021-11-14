package com.example.module_login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.example.lib_base.ext.inflateViewBinding
import com.example.module_login.databinding.LoginActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding:LoginActivityLoginBinding by inflateViewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}