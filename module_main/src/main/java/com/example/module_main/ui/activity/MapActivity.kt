package com.example.module_main.ui.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.example.module_main.R
import com.example.module_main.bean.ObjLocation
import com.example.module_main.databinding.MainActivityMapBinding
import com.example.module_main.utils.AMapServicesUtil
import com.example.module_main.utils.MapUtil
import com.example.module_main.utils.WalkRouteOverlay
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity : AppCompatActivity(), RouteSearch.OnRouteSearchListener {
    /**
     * 新增
     */
    lateinit var mStartPoint: LatLonPoint
    lateinit var mEndPoint: LatLonPoint
    lateinit var routeSearch: RouteSearch


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
    private var aMapLocationClientOption: AMapLocationClientOption =
        AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isOnceLocationLatest = true
            isNeedAddress = true
            httpTimeOut = 20000
            isLocationCacheEnable = false
        }

    private val locationHelper = LocationHelper()

    private var obj: Marker? = null

    private lateinit var petName: String

    companion object {
        private val REQUEST_LOCATION = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AMapLocationClient.updatePrivacyAgree(application.applicationContext, true)
        AMapLocationClient.updatePrivacyShow(application.applicationContext, true, true)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        windowInset()

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


        intent.apply {
            petName = extras?.get("PET_NAME") as String? ?: ""
        }
        initLocation()
        initMap()
        initWebSocket()
        initRoute()
        showNotification()


    }

    /**
     * 新增
     */
    private fun initRoute() {
        routeSearch = RouteSearch(this)
        routeSearch.setRouteSearchListener(this)
    }

    private fun startRouteSearch() {
        binding.map.let {
            it.map.addMarker(
                MarkerOptions().position(AMapServicesUtil.convertToLatLng(mStartPoint))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_start_start))
            )
            it.map.addMarker(
                MarkerOptions().position(AMapServicesUtil.convertToLatLng(mEndPoint))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_end_end))
            )
            val fromAndTo = RouteSearch.FromAndTo(mStartPoint, mEndPoint)
            val query = RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault)
            routeSearch.calculateWalkRouteAsyn(query)

        }
    }


    private fun windowInset() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, binding.root)?.apply {
            isAppearanceLightStatusBars = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) hide(WindowInsetsCompat.Type.statusBars())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.apply {
                attributes = attributes.apply {
                    layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            }
        }
    }

    val channelId = "com.example.miaozhi"
    val channelName = "pet"
    val notificationId = 1


    private fun showNotification() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MapActivity::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val builder = NotificationCompat.Builder(this, channelId)
            .setContentText("定位ing...")
            .setContentText("正在为您定位您的$petName")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher)
            .setAutoCancel(false)



        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }

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
        binding.floatBtn.setOnClickListener {
            startRouteSearch()
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
            mStartPoint =
                AMapServicesUtil.convertToLatLonPoint(p0?.let { LatLng(it.latitude, it.longitude) })
//            mEndPoint =
//                AMapServicesUtil.convertToLatLonPoint(p0?.let { LatLng(it.latitude+0.1, it.longitude+0.1) })
        }
    }

    inner class WebSocketConnector : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            val location = gson.fromJson(text, ObjLocation::class.java)
            mEndPoint =
                AMapServicesUtil.convertToLatLonPoint(LatLng(location.latBle, location.lonBle))
            obj?.remove()
            runOnUiThread {
                obj = binding.map.map.addMarker(
                    MarkerOptions()
                        .position(LatLng(location.latBle, location.lonBle))
                )
            }

        }
    }

    override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {
//        TODO("Not yet implemented")
    }

    override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {
//        TODO("Not yet implemented")
    }

    override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {
        binding.map.map.clear()
        if (p1 == com.amap.api.services.core.AMapException.CODE_AMAP_SUCCESS) {
            if (p0 != null && p0.paths != null) {
                if (p0.paths.size > 0) {
                    val walkPath = p0.paths.get(0) ?: return
                    val walkRouteOverlay =
                        WalkRouteOverlay(
                            this,
                            binding.map.map,
                            walkPath,
                            p0.startPos,
                            p0.targetPos
                        ).apply {
                            removeFromMap()
                            addToMap()
                            zoomToSpan()
                        }
                    val dis = walkPath.distance.toInt()
                    val dur = walkPath.duration.toInt()
                    val des =
                        MapUtil.getFriendlyTime(dur) + "(" + MapUtil.getFriendlyLength(dis) + ")"

                }
            }
        }
    }

    override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {
//        TODO("Not yet implemented")
    }

}