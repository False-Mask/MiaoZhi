package com.example.module_main.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.LocationSource
import com.example.module_main.R
import com.example.module_main.databinding.MainActivityMapBinding

class MapActivity : AppCompatActivity(), AMapLocationListener, LocationSource {

    private val binding by lazy {
        MainActivityMapBinding.inflate(layoutInflater)
    }

    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var locationChangeListener: LocationSource.OnLocationChangedListener? = null

    private var amapLocationClient: AMapLocationClient? = null
    private var aMapLocationClientOption: AMapLocationClientOption =
        AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isOnceLocationLatest = true
            isNeedAddress = true
            httpTimeOut = 20000
            isLocationCacheEnable = false
        }

    companion object {
        private val REQUEST_LOCATION = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AMapLocationClient.updatePrivacyAgree(application.applicationContext, true)
        AMapLocationClient.updatePrivacyShow(application.applicationContext, true, true)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.map.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val filter = permissions.map {
                val result = checkSelfPermission(it)
                if (result == PackageManager.PERMISSION_DENIED) it else ""
            }.filter {
                it != ""
            }.toTypedArray()


            if (filter.isNotEmpty()) requestPermissions(filter, REQUEST_LOCATION)
        }

        initLocation()

        initMap()

    }

    private fun initMap() {
        binding.map.map.apply {
            setLocationSource(this@MapActivity)
            isMyLocationEnabled = true
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION -> {
                initLocation()
            }
        }
    }

    private fun initLocation() {

        amapLocationClient = AMapLocationClient(application.applicationContext)
            .apply {
                setLocationOption(aMapLocationClientOption)
                setLocationListener(this@MapActivity)
            }

        amapLocationClient?.startLocation()

    }


    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        amapLocationClient?.stopLocation()
        binding.map.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.map.onSaveInstanceState(outState)
    }

    override fun onLocationChanged(p0: AMapLocation?) {
        Log.e("TAG", p0.toString())
        locationChangeListener?.onLocationChanged(p0)
    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {
        locationChangeListener = p0
        amapLocationClient?.startLocation()
    }

    override fun deactivate() {
        amapLocationClient?.stopLocation()
        amapLocationClient?.onDestroy()
    }

}