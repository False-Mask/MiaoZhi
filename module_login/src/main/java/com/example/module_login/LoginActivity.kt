package com.example.module_login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.lib_common.common.GosDeploy
import com.example.lib_common.common.GosUserModuleBaseActivity
import com.example.module_login.databinding.LoginActivityLoginBinding
import com.gizwits.gizwifisdk.api.GizWifiSDK
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode

class LoginActivity : GosUserModuleBaseActivity() {
    private val binding:LoginActivityLoginBinding by lazy {
        LoginActivityLoginBinding.inflate(layoutInflater)
    }

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

    private val baseHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val key: handler_key =
                handler_key.values().get(msg.what)
            when (key) {
                handler_key.LOGIN -> {
                    progressDialog.show()
                    GizWifiSDK.sharedInstance()
                        .userLogin(binding.etUsername.text.toString(),
                            binding.etPsw.text.toString())
                }
                handler_key.AUTO_LOGIN -> {
                    progressDialog.show()
                    GizWifiSDK.sharedInstance().userLogin(
                        spf.getString("UserName", ""),
                        spf.getString("PassWord", ""))
                }
                handler_key.THRED_LOGIN -> {
                    progressDialog.show()
                    /* GosDeviceListFragment.loginStatus = 0
                     GizWifiSDK.sharedInstance().loginWithThirdAccount(
                         com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity.gizThirdAccountType,
                         com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity.thirdUid,
                         com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity.thirdToken)
                     spf.edit().putString("thirdUid",
                         com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity.thirdUid)
                         .commit()*/
                }
                handler_key.FOREIGN -> {
                    /*llForeign.setVisibility(View.VISIBLE)
                    llInland.setVisibility(View.GONE)*/
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.white)
        setContentView(binding.root)

        initListener()
    }

    private fun initListener() {
        val eventHandler = EventHandler()
        binding.btnGuest.setOnClickListener(eventHandler)
        binding.btnLoginRegist.setOnClickListener(eventHandler)

    }

    override fun onResume() {
        super.onResume()
        //push_jiguang-false-end
        //push-all-end
        autoLogin()
        cleanuserthing()
    }

    private fun cleanuserthing() {
        if (isclean) {
            binding.etUsername.setText("")
            binding.etPsw.setText("")
        }
    }

    private fun autoLogin() {
        if (TextUtils.isEmpty(spf.getString("UserName", ""))
            || TextUtils.isEmpty(spf.getString("PassWord", ""))
        ) {
            return
        }
        baseHandler.sendEmptyMessageDelayed(handler_key.AUTO_LOGIN.ordinal,
            1000)
    }

    /**
     * 用户登录回调
     */
    override fun didUserLogin(
        result: GizWifiErrorCode, uid: String?,
        token: String?,
    ) {
        progressDialog.cancel()
        /*if (GosDeviceListFragment.loginStatus === 4
            || GosDeviceListFragment.loginStatus === 3
        ) {
            return
        }*/
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) { // 登录失败
            Toast.makeText(this, toastError(result),
                toastTime).show()
        } else { // 登录成功
            //GosDeviceListFragment.loginStatus = 1
            Toast.makeText(this,
                R.string.toast_login_successful, toastTime).show()
            // TODO 绑定推送\
            //push-all-start
            //push_baidu-false-start   push_baidu-true-start
            if (GosDeploy.appConfig_Push_BaiDu()) {
                //push_baidu-true-end
                //GosPushManager.pushBindService(token)
                //push_baidu-true-start
            }
            //push_baidu-false-end   push_baidu-true-end
            //push_jiguang-false-start   push_jiguang-true-start
            if (GosDeploy.appConfig_Push_JiGuang()) {
                //push_jiguang-true-end
                //GosPushManager.pushBindService(token)
                //push_jiguang-true-start
            }
            //push_jiguang-false-end   push_jiguang-true-end
            //push-all-end
//            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "T45LbeyAo3muOzMHztipttr8");
            if (!TextUtils.isEmpty(binding.etUsername.text
                    .toString())
                && !TextUtils.isEmpty(binding.etPsw.text
                    .toString())
            ) {
                spf.edit().putString("UserName",
                    binding.etUsername.text
                        .toString())
                    .commit()
                spf.edit().putString("PassWord",
                    binding.etPsw.text
                        .toString())
                    .commit()
            }
            spf.edit().putString("Uid", uid).commit()
            spf.edit().putString("Token", token).commit()
            intent = null
            if (intent == null) {
                intent = Intent("com.example.miaozhi.main")
            }
            intent.putExtra("ThredLogin", true)
            startActivity(intent)
        }
    }


    inner class EventHandler : View.OnClickListener {
        override fun onClick(v: View?) {
            v ?: return
            when (v.id) {
                R.id.btn_login_regist -> {
                    if (TextUtils.isEmpty(binding.etUsername.text
                            .toString())
                    ) {
                        Toast.makeText(this@LoginActivity,
                            R.string.toast_name_wrong, toastTime).show()
                        return
                    }
                    if (TextUtils.isEmpty(binding.etPsw.text
                            .toString())
                    ) {
                        Toast.makeText(this@LoginActivity,
                            R.string.toast_psw_wrong, toastTime).show()
                        return
                    }
                    baseHandler.sendEmptyMessage(handler_key.LOGIN.ordinal)
                }
                R.id.btn_guest -> {
                    startActivity(Intent("com.example.miaozhi.main"))
                }
            }
        }

    }


}