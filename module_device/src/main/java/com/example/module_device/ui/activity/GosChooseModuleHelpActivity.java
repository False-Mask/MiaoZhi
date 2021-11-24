package com.example.module_device.ui.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.module_device.R;
import com.example.lib_common.common.GosConfigModuleBaseActivity;
import com.example.lib_common.utils.AssetsUtils;


/**
 * Created by admin on 2017/6/15.
 */

public class GosChooseModuleHelpActivity extends GosConfigModuleBaseActivity {
    private WebView webHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_choose_module_help);
        initView();
        initEvent();
    }

    private void initEvent() {
        setToolBar(true, getString(R.string.module_help));
        WebSettings wSet = webHelp.getSettings();
        wSet.setJavaScriptEnabled(true);
        if (AssetsUtils.isZh(this)) {
            webHelp.loadUrl("file:///android_asset/moduleTypeInfo.html");
        } else {
            webHelp.loadUrl("file:///android_asset/moduleTypeInfoEnglish.html");
        }

    }

    private void initView() {
        webHelp = (WebView) findViewById(R.id.webHelp);
    }
}
