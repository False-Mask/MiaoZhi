package com.example.miaozhi

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.miaozhi.databinding.ActivityMainBinding
import com.example.module_device.GosDeviceModuleBaseActivity
import com.example.module_device.common.GosDeploy
import com.example.module_device.utils.ToolUtils

private const val MAIN_FRAGMENT = "MAIN"
private const val DEVICE_FRAGMENT = "DEVICE"
private const val MINE_FRAGMENT = "MINE"

class MainActivity :
    GosDeviceModuleBaseActivity()/*, NavigationView.OnNavigationItemSelectedListener */ {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var fragmentList: HashMap<String, Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = resources.getColor(R.color.color_primary)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_content) as NavHostFragment
        navController = navHostFragment.navController

        binding.bnvBottomMenu.setupWithNavController(navController)

        /* fragmentList = hashMapOf(
             MAIN_FRAGMENT to MainFragment(),
             DEVICE_FRAGMENT to GosDeviceListFragment()
         )*/
        initView()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (GosDeploy.appConfig_Config_Airlink()) {
            if (GosDeploy.appConfig_Config_Softap()) {
                menuInflater.inflate(R.menu.gosdeviceconfig, menu)
            } else {
                menuInflater.inflate(R.menu.gosnull, menu)
                val menuItem = menu.findItem(R.id.add)
                menuItem.setIcon(ToolUtils.editIcon(resources, R.drawable.deviceonboarding_add))
            }
        } else {
            if (GosDeploy.appConfig_Config_Softap()) {
                menuInflater.inflate(R.menu.gosnull, menu)
                val menuItem = menu.findItem(R.id.add)
                menuItem.setIcon(ToolUtils.editIcon(resources, R.drawable.deviceonboarding_add))
            }
        }
        return true
    }

    override fun initToolbar(): Toolbar = binding.include.toolbar

    private fun initView() {
        if (GosDeploy.appConfig_BindDevice_Qrcode()) {
            setToolBar(true, R.string.devicelist_title)
        } else {
            setToolBar(false, R.string.devicelist_title)
        }
    }

}