package com.example.miaozhi

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.miaozhi.databinding.ActivityMainBinding
import com.example.module_device.ui.fragment.GosDeviceListFragment
import com.example.lib_common.common.GosDeviceModuleBaseActivity
import com.example.lib_common.common.GosDeploy
import com.example.module_device.ui.activity.GosAirlinkChooseDeviceWorkWiFiActivity
import com.example.module_device.ui.activity.GosChooseDeviceWorkWiFiActivity
import com.example.lib_common.utils.ToolUtils
import com.example.module_main.ui.fragment.MainFragment
import com.google.android.material.navigation.NavigationBarView
import zxing.CaptureActivity

class MainActivity :
    GosDeviceModuleBaseActivity() {

    private val REQUEST_EXTERNAL_STORAGE = 22
    private val PERMISSIONS_STORAGE = arrayOf("android.permission.CAMERA")

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = resources.getColor(R.color.color_primary)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initView()
        initListener()
    }

    private fun initListener() {
        val listener = EventHandler()
        binding.bnvBottomMenu.setOnItemSelectedListener(listener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (GosDeploy.appConfig_Config_Airlink()) {
            if (GosDeploy.appConfig_Config_Softap()) {
                menuInflater.inflate(R.menu.gosdeviceconfig, menu)
            } else {
                menuInflater.inflate(R.menu.gosnull, menu)
                val menuItem = menu.findItem(R.id.add)
                menuItem.icon = ToolUtils.editIcon(resources, R.drawable.deviceonboarding_add)
            }
        } else {
            if (GosDeploy.appConfig_Config_Softap()) {
                menuInflater.inflate(R.menu.gosnull, menu)
                val menuItem = menu.findItem(R.id.add)
                menuItem.icon = ToolUtils.editIcon(resources, R.drawable.deviceonboarding_add)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val isConsumed: Boolean
        item ?: return false
        when (item.itemId) {
            android.R.id.home -> if (GosDeploy.appConfig_BindDevice_Qrcode()) {
                val permission: Int =
                    ActivityCompat.checkSelfPermission(this, "android.permission.CAMERA")
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    try {
                        // 没有写的权限，去申请写的权限，会弹出对话框
                        ActivityCompat.requestPermissions(this,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    intent = Intent(this, CaptureActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left)
                }
            }
            R.id.airlink_config -> if (!checkNetwork(this)) {
                Toast.makeText(this, R.string.network_error, LENGTH_LONG).show()
            } else {
                intent = Intent(this,
                    GosAirlinkChooseDeviceWorkWiFiActivity::class.java)
                startActivity(intent)
            }
            R.id.softap_config -> if (!checkNetwork(this)) {
                Toast.makeText(this, R.string.network_error, LENGTH_LONG).show()
            } else {
                intent = Intent(this, GosChooseDeviceWorkWiFiActivity::class.java)
                startActivity(intent)
            }
            R.id.add -> {
                if (GosDeploy.appConfig_Config_Airlink()) {
                    if (!checkNetwork(this)) {
                        Toast.makeText(this, R.string.network_error, LENGTH_LONG).show()
                    } else {
                        intent = Intent(this,
                            GosAirlinkChooseDeviceWorkWiFiActivity::class.java)
                        startActivity(intent)
                    }
                }
                if (GosDeploy.appConfig_Config_Softap()) {
                    if (!checkNetwork(this)) {
                        Toast.makeText(this, R.string.network_error, LENGTH_LONG).show()
                    } else {
                        intent = Intent(this,
                            GosChooseDeviceWorkWiFiActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun produceToolbar(): Toolbar = binding.include.toolbar

    private fun initView() {
        initToolbar()
        initVp()
    }

    private fun initVp() {
        binding.vpContent.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> MainFragment.INSTANCE
                1 -> GosDeviceListFragment.INSTANCE
                else -> throw RuntimeException("vp2 index error!!!!!!!!!!")
            }

        }
        binding.vpContent.isUserInputEnabled = false
    }

    private fun initToolbar() {
        if (GosDeploy.appConfig_BindDevice_Qrcode()) {
            setToolBar(true, R.string.devicelist_title)
        } else {
            setToolBar(false, R.string.devicelist_title)
        }
        mToolbar.navigationIcon = null
    }

    private fun getPositionById(@LayoutRes id: Int): Int {
        return when (id) {
            R.id.menu_main -> 0
            R.id.menu_device -> 1
            R.id.menu_mine -> 2
            else -> throw RuntimeException("getPositionById index error")
        }
    }

    inner class EventHandler : View.OnClickListener, NavigationBarView.OnItemSelectedListener {
        override fun onClick(v: View?) {

        }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menu_main -> {
                    mToolbar.navigationIcon = null
                    val ssTitle1 = SpannableString(getString(R.string.messagecenter))
                    ssTitle1.setSpan(ForegroundColorSpan(GosDeploy.appConfig_Contrast()),
                        0,
                        ssTitle1.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    tvTitle.text = ssTitle1
                }
                R.id.menu_device -> {
                    if (GosDeploy.appConfig_BindDevice_Qrcode()) {
                        val color = GosDeploy.appConfig_Contrast()
                        val upArrow = resources.getDrawable(R.drawable.common_qrcode_button)
                        upArrow.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                        mToolbar.navigationIcon = upArrow
                        val ssTitle = SpannableString(getString(R.string.devicelist_title))
                        ssTitle.setSpan(ForegroundColorSpan(GosDeploy.appConfig_Contrast()),
                            0,
                            ssTitle.length,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        tvTitle.text = ssTitle
                    } else {
                        mToolbar.navigationIcon = null
                        val ssTitle = SpannableString(getString(R.string.devicelist_title))
                        ssTitle.setSpan(ForegroundColorSpan(GosDeploy.appConfig_Contrast()),
                            0,
                            ssTitle.length,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        tvTitle.text = ssTitle
                    }
                }
                R.id.menu_mine -> {
                    mToolbar.navigationIcon = null
                    val ssTitle2 = SpannableString(getString(R.string.personal_center))
                    ssTitle2.setSpan(ForegroundColorSpan(GosDeploy.appConfig_Contrast()),
                        0,
                        ssTitle2.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    tvTitle.text = ssTitle2
                }
            }
            binding.vpContent.currentItem = getPositionById(item.itemId)
            return true
        }

    }
}