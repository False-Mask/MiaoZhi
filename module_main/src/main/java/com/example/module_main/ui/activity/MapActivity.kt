package com.example.module_main.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import com.example.module_main.bean.ObjLocation
import com.example.module_main.databinding.MainActivityMapBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {

    private val binding by lazy { MainActivityMapBinding.inflate(layoutInflater) }

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var gson: Gson


    private var amapLocationClient: AMapLocationClient? = null
    private var aMapLocationClientOption: AMapLocationClientOption = AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isOnceLocationLatest = true
            isNeedAddress = true
            httpTimeOut = 20000
            isLocationCacheEnable = false
        }

    private val locationHelper = LocationHelper()

    private var obj: Marker? = null


    companion object {
        private val REQUEST_LOCATION = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AMapLocationClient.updatePrivacyAgree(application.applicationContext, true)
        AMapLocationClient.updatePrivacyShow(application.applicationContext, true, true)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
        initWebSocket()

        binding.map.onCreate(savedInstanceState)


    }

    private fun initWebSocket() {

        okHttpClient.newWebSocket(
            Request.Builder()
                .url("ws://1.14.74.79:8089/huawei/iot/client/test")
                .build(), WebSocketConnector()
        )
            .request()

    }

    private fun initMap() {
        binding.map.map.apply {
            setLocationSource(locationHelper)
            isMyLocationEnabled = true
            myLocationStyle = MyLocationStyle().apply {
                myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
            }
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
                setLocationListener(locationHelper)
            }
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


    inner class LocationHelper : LocationSource, AMapLocationListener {

        private var myLocationChangeListener: LocationSource.OnLocationChangedListener? = null

        override fun activate(p0: LocationSource.OnLocationChangedListener?) {
            myLocationChangeListener = p0
            amapLocationClient?.startLocation()
        }

        override fun deactivate() {
            amapLocationClient?.stopLocation()
            amapLocationClient?.onDestroy()
        }

        override fun onLocationChanged(p0: AMapLocation?) {
            myLocationChangeListener?.onLocationChanged(p0)
        }
    }

    inner class WebSocketConnector : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            val location = gson.fromJson(text, ObjLocation::class.java)

            obj?.remove()
            runOnUiThread {
                obj = binding.map.map.addMarker(
                    MarkerOptions()
                        .position(LatLng(location.latBle, location.lonBle))
                )
            }

        }
    }

}