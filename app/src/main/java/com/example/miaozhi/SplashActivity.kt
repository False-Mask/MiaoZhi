package com.example.miaozhi

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.miaozhi.databinding.ActivitySplashBinding
import com.example.lib_common.common.MessageCenter
import com.example.module_login.LoginActivity

class SplashActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private val SPLASH_DISPLAY_LENGHT = 2000
    private lateinit var handler: Handler

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE")

    private lateinit var binding: ActivitySplashBinding


    //push-all-end
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#ebba95")
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!this.isTaskRoot) { // 判断此activity是不是任务控件的源Activity，“非”也就是说是被系统重新实例化出来的
            val mainIntent = intent
            val action = mainIntent.action
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && action == Intent.ACTION_MAIN
            ) {
                finish()
                return
            }
        }


        handler = Handler()
        window.decorView.post {
            //检测是否有写的权限
            val permission: Int = ActivityCompat.checkSelfPermission(this@SplashActivity,
                "android.permission.WRITE_EXTERNAL_STORAGE")
            if (permission != PackageManager.PERMISSION_GRANTED) {
                try {
                    // 没有写的权限，去申请写的权限，会弹出对话框
                    ActivityCompat.requestPermissions(this@SplashActivity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                initEvent()
            }
        }
    }

    private fun initEvent() {

        // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转到MainActivity
        handler.postDelayed({
            val intent = Intent(this@SplashActivity,
                LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DISPLAY_LENGHT.toLong())
        MessageCenter.getInstance(this@SplashActivity)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        initEvent()
    }


}