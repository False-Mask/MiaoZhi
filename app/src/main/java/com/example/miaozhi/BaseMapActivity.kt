package com.example.miaozhi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocationClient
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView

/**
 *@author ZhiQiang Tu
 *@time 2022/4/24  13:57
 *@signature 我将追寻并获取我想要的答案
 */
class BaseMapActivity : AppCompatActivity() {

    private lateinit var map:MapView

    open fun  inititalMap(map:MapView){
        this.map = map
    }


    override fun onSaveInstanceState(outState: Bundle) {

        AMapLocationClient.updatePrivacyAgree(application.applicationContext, true)
        AMapLocationClient.updatePrivacyShow(application.applicationContext, true, true)

        super.onSaveInstanceState(outState)
        map.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

}