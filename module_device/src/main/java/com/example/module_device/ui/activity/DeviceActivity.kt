package com.example.module_device.ui.activity

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import com.example.lib_common.common.GosControlModuleBaseActivity
import com.example.module_device.R
import com.example.module_device.databinding.DeviceActivityDeviceBinding
import com.gizwits.gizwifisdk.api.GizWifiDevice
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode
import java.util.concurrent.ConcurrentHashMap

class DeviceActivity : GosControlModuleBaseActivity() {

    private val temperature: MutableLiveData<Int> = MutableLiveData(-1)
    private val humidity: MutableLiveData<Int> = MutableLiveData(-1)
    private val isConnected:MutableLiveData<Boolean> = MutableLiveData(false)
    private var temperatureErrorInfo:Boolean = true

    private val binding: DeviceActivityDeviceBinding by lazy {
        DeviceActivityDeviceBinding.inflate(layoutInflater)
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
            if (it == -1) {
                binding.tvTemperature.text = "设备已断开"
            } else {
                binding.tvTemperature.text = "温度$it℃"
            }
        }

        humidity.observe(this) {
            if (it == -1) {
                binding.tvHumidity.text = "设备已断开"
            } else {
                binding.tvHumidity.text = "湿度$it%"
            }
        }

        isConnected.observe(this){
            if (!it){
                binding.tvHumidometerDis.text = "设备已断开"
                binding.sThermometer.isChecked = false
            }else{
                binding.tvHumidometerDis.text = "设备已打开"
                binding.sThermometer.isChecked = true
            }
        }
    }

    private fun initDevice() {
        mDevice = intent.getParcelableExtra<Parcelable>("GizWifiDevice") as GizWifiDevice
        mDevice.listener = gizWifiDeviceListener
        isConnected.value =
            !(mDevice.netStatus == GizWifiDeviceNetStatus.GizDeviceOffline ||mDevice.netStatus == GizWifiDeviceNetStatus.GizDeviceUnavailable)
    }

    private fun initEvent() {
        val handler = EventHandler()
        binding.tbBar.setNavigationOnClickListener { finish() }

        binding.sBluetooth.setOnCheckedChangeListener(handler)
        binding.sError.setOnCheckedChangeListener(handler)
        binding.sSocket.setOnCheckedChangeListener(handler)
        binding.sWifi.setOnCheckedChangeListener(handler)

    }

    override fun didUpdateNetStatus(device: GizWifiDevice?, netStatus: GizWifiDeviceNetStatus) {
        if (netStatus == GizWifiDeviceNetStatus.GizDeviceOffline){
            myToast("设备已断开")
            temperature.value = -1
            temperature.value = -1
            isConnected.value = false
        }else{
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

            }, {
                temperature.value = it["temperature"] as? Int ?: -1
                humidity.value = it["humidity"] as? Int ?: -1
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
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            buttonView ?: return
            when(buttonView.id){
                R.id.s_error->{
                    temperatureErrorInfo = isChecked
                }
            }
        }

    }
}