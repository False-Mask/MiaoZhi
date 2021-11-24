package com.example.module_device.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import com.example.lib_base.base.SingleLiveEvent
import com.example.lib_common.common.GosControlModuleBaseActivity
import com.example.module_device.R
import com.example.module_device.databinding.DeviceActivityDeviceBinding
import com.gizwits.gizwifisdk.api.GizWifiDevice
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode
import java.util.concurrent.ConcurrentHashMap
import android.app.NotificationManager

import android.app.NotificationChannel


import android.app.PendingIntent

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat


class DeviceActivity : GosControlModuleBaseActivity() {

    private val temperature: MutableLiveData<Int> = MutableLiveData(Int.MIN_VALUE)
    private val isConnected: MutableLiveData<Boolean> = MutableLiveData(false)
    private val humidity: MutableLiveData<Int> = MutableLiveData(Int.MIN_VALUE)

    private val motorSpeed: MutableLiveData<Int> = MutableLiveData(Int.MIN_VALUE)
    private var motorMax: Int = 10


    private var temperatureErrorInfo: MutableLiveData<Boolean> = MutableLiveData(true)
    private val humidityBox: MutableLiveData<Boolean> = MutableLiveData(true)
    private val light: MutableLiveData<Boolean> = MutableLiveData(true)
    private val arrange: MutableLiveData<Boolean> = MutableLiveData(true)
    private val wifi: MutableLiveData<Boolean> = MutableLiveData(true)

    val alert:SingleLiveEvent<Boolean> = SingleLiveEvent()

    val alert1:SingleLiveEvent<Boolean> = SingleLiveEvent()
    val alert2:SingleLiveEvent<Boolean> = SingleLiveEvent()


    private val binding: DeviceActivityDeviceBinding by lazy {
        DeviceActivityDeviceBinding.inflate(layoutInflater)
    }

    private val NOTIFY_ID = 100

    private fun showNotification() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val hangIntent = Intent(this, NotificationActivity::class.java)
        val hangPendingIntent =
            PendingIntent.getActivity(this, 1001, hangIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val CHANNEL_ID = "ID" //应用频道Id唯一值， 长度若太长可能会被截断，
        val CHANNEL_NAME = "NAME" //最长40个字符，太长会被截断
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("温湿度异常")
            .setContentText("点击查看")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(hangPendingIntent)
           /* .setLargeIcon(BitmapFactory.decodeResource(resources, com.example.module_device.R.mipmap.head))*/
            .setAutoCancel(true)
            .build()

        //Android 8.0 以上需包添加渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
            manager.createNotificationChannel(notificationChannel)
        }
        manager.notify(NOTIFY_ID, notification)
    }

    private lateinit var mDevice: GizWifiDevice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.white)
        setContentView(binding.root)
        initDevice()
        initView()
        initEvent()
        initObserver()
    }

    private fun initView() {
        initToolbar()
    }

    private fun initToolbar() {
        binding.tbBar.title = mDevice.productName
    }

    private fun initObserver() {
        temperature.observe(this) {
            if (it == Int.MIN_VALUE) {
                binding.tvTemperature.text = "设备已断开"
            } else {
                binding.tvTemperature.text = "温度$it℃"
                alert.value = it >=40 || it<=20
            }
        }

        isConnected.observe(this) {
            if (!it) {
                binding.tvHumidometerDis.text = "设备已断开"
                binding.sThermometer.isChecked = false
            } else {
                binding.tvHumidometerDis.text = "设备已打开"
                binding.sThermometer.isChecked = true
            }
        }

        humidity.observe(this) {
            if (it == Int.MIN_VALUE) {
                binding.tvHumidity.text = "设备已断开"
            } else {
                binding.tvHumidity.text = "湿度$it%"
            }
        }

        temperatureErrorInfo.observe(this) {
            if (it) {
                binding.apply {
                    cvError.setCardBackgroundColor(Color.parseColor("#d8f6fd"))
                }
            } else {
                binding.apply {
                    cvError.setCardBackgroundColor(Color.parseColor("#f5fafe"))
                }
            }
        }

        humidityBox.observe(this) {
            if (it) {
                binding.apply {
                    cvHumidometer.setCardBackgroundColor(Color.parseColor("#d8f6fd"))
                    tvHumidometerDis.text = "设备已打开"
                }
            } else {
                binding.apply {
                    cvHumidometer.setCardBackgroundColor(Color.parseColor("#f5fafe"))
                    tvHumidometerDis.text = "设备已关闭"
                }
            }
        }

        motorSpeed.observe(this) {
            binding.sbMotorSpeed.max = motorMax
            binding.sbMotorSpeed.progress = motorSpeed.value?.plus(5) ?: 0
        }

        light.observe(this) {
            if (it) {
                binding.apply {
                    cvBluetooth.setCardBackgroundColor(Color.parseColor("#d8f6fd"))
                    tvLight2.text = "灯已打开"
                    sLight.isChecked = true
                }
            } else {
                binding.apply {
                    cvBluetooth.setCardBackgroundColor(Color.parseColor("#f5fafe"))
                    tvLight2.text = "灯已关闭"
                    sLight.isChecked = false
                }
            }
        }

        /*alert1.observe(this){
            if (it && temperatureErrorInfo.value == true){
                showNotification()
            }
        }


        alert2.observe(this){
            if (it && temperatureErrorInfo.value == true){
                showNotification()
            }
        }*/

        alert.observe(this){
            if (it){
                showNotification()
            }
        }

    }

    private fun initDevice() {
        mDevice = intent.getParcelableExtra<Parcelable>("GizWifiDevice") as GizWifiDevice
        mDevice.listener = gizWifiDeviceListener
        isConnected.value =
            !(mDevice.netStatus == GizWifiDeviceNetStatus.GizDeviceOffline || mDevice.netStatus == GizWifiDeviceNetStatus.GizDeviceUnavailable)
    }

    private fun initEvent() {
        val handler = EventHandler()
        binding.tbBar.setNavigationOnClickListener { finish() }

        binding.sError.setOnCheckedChangeListener(handler)
        binding.sLight.setOnCheckedChangeListener(handler)
        binding.sbMotorSpeed.setOnSeekBarChangeListener(handler)
        binding.sThermometer.setOnCheckedChangeListener(handler)

    }

    override fun didUpdateNetStatus(device: GizWifiDevice?, netStatus: GizWifiDeviceNetStatus) {
        if (netStatus == GizWifiDeviceNetStatus.GizDeviceOffline) {
            myToast("设备已断开")
            temperature.value = Int.MIN_VALUE
            temperature.value = Int.MIN_VALUE
            isConnected.value = false
        } else {
            isConnected.value = true
        }
        super.didUpdateNetStatus(device, netStatus)
    }

    override fun didReceiveData(
        result: GizWifiErrorCode?,
        device: GizWifiDevice?,
        dataMap: ConcurrentHashMap<String, Any>?,
        sn: Int,
    ) {
        parseData(dataMap,
            {
                alert1.value = it["Alert_1"] as? Boolean ?: false
                alert2.value = it["Alert_2"] as? Boolean ?: false
            }, {
                temperature.value = it["Temperature"] as? Int ?: Int.MIN_VALUE
                humidity.value = it["Humidity"] as? Int ?: Int.MIN_VALUE
                motorSpeed.value = it["Motor_Speed"] as? Int ?: Int.MIN_VALUE
                light.value = it["LED_OnOff"] as? Boolean ?: false

            }, {

            })
        super.didReceiveData(result, device, dataMap, sn)
    }


    private inline fun parseData(
        dataMap: ConcurrentHashMap<String, Any>?,
        crossinline alertTrans: ((ConcurrentHashMap<String, Object>) -> Unit),
        crossinline dataTrans: ((ConcurrentHashMap<String, Object>) -> Unit),
        crossinline faultsTrans: ((ConcurrentHashMap<String, Object>) -> Unit),
    ) {
        dataMap ?: return
        val alerts = dataMap["alerts"]
        val data = dataMap["data"]
        val faults = dataMap["faults"]

        if (alerts != null) {
            val d = alerts as ConcurrentHashMap<String, Object>
            alertTrans(d)
        }

        if (data != null) {
            val d = data as ConcurrentHashMap<String, Object>
            dataTrans(d)
        }

        if (faults != null) {
            val d = faults as ConcurrentHashMap<String, Object>
            faultsTrans(d)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mDevice.setSubscribe(false)
        mDevice.listener = null
    }


    inner class EventHandler : View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {
        override fun onClick(v: View?) {
            v ?: return
            when (v.id) {
            }
        }

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            seekBar ?: return
            writeToService()
        }

        private fun writeToService() {
            val dates = ConcurrentHashMap<String, Any>()
            dates.apply {
                put("LED_OnOff", binding.sLight.isChecked)
                put("Motor_Speed", binding.sbMotorSpeed.progress - 5)
            }
            mDevice.write(dates, 1)
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            buttonView ?: return
            when (buttonView.id) {
                R.id.s_error -> {
                    temperatureErrorInfo.value = isChecked
                }
                R.id.s_thermometer -> {
                    humidityBox.value = isChecked
                }
                R.id.s_light -> {
                    light.value = isChecked
                    writeToService()
                }
            }
        }

    }
}