package com.example.module_login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.module_login.databinding.LoginActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:LoginActivityLoginBinding

    enum class handler_key {
        /**
         * 登录
         */
        LOGIN,

        /**
         * 自动登录
         */
        AUTO_LOGIN,

        /**
         * 第三方登录
         */
        THRED_LOGIN,

        /**
         * 国外域名登录
         */
        FOREIGN
    }

    /**
     * 与WXEntryActivity共用Handler
     */
    /*private val baseHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val key: handler_key =
                c.handler_key.values()
                    .get(msg.what)
            when (key) {
                handler_key.LOGIN -> {
                    progressDialog.show()
                    GosDeviceListFragment.loginStatus = 0
                    GizWifiSDK.sharedInstance()
                        .userLogin(com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity.etName.getText()
                            .toString(),
                            com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity.etPsw.getText()
                                .toString())
                }
                handler_key.AUTO_LOGIN -> {
                    progressDialog.show()
                    GosDeviceListFragment.loginStatus = 0
                    GizWifiSDK.sharedInstance().userLogin(
                        spf.getString("UserName", ""),
                        spf.getString("PassWord", ""))
                }
                handler_key.THRED_LOGIN -> {
                    progressDialog.show()
                    GosDeviceListFragment.loginStatus = 0
                    GizWifiSDK.sharedInstance().loginWithThirdAccount(
                        com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity.gizThirdAccountType,
                        com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity.thirdUid,
                        com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity.thirdToken)
                    spf.edit().putString("thirdUid",
                        com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity.thirdUid)
                        .commit()
                }
                handler_key.FOREIGN -> {
                    llForeign.setVisibility(View.VISIBLE)
                    llInland.setVisibility(View.GONE)
                }
            }
        }
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnGuest.setOnClickListener {
            //GizWifiSDK.sharedInstance().userLoginAnonymous()
            //利用隐式Intent打开app下的MainActivity
            startActivity(Intent("com.example.miaozhi.main"))
        }
    }

    override fun onResume() {
        super.onResume()
    }
}