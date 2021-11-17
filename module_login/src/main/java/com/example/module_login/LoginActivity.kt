package com.example.module_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.lib_base.ext.inflateViewBinding
import com.example.module_login.databinding.LoginActivityLoginBinding
import com.gizwits.gizwifisdk.api.GizWifiSDK

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:LoginActivityLoginBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnGuest.setOnClickListener {
            GizWifiSDK.sharedInstance().userLoginAnonymous()
            //利用隐式Intent打开app下的MainActivity
            startActivity(Intent("com.example.miaozhi.main"))
        }
    }
}