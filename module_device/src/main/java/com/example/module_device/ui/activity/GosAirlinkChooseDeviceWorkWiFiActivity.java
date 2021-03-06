package com.example.module_device.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

//import com.example.module_device.GosMainActivity;
import com.example.module_device.R;
import com.example.lib_common.common.GosDeploy;
import com.example.lib_common.common.GosConfigModuleBaseActivity;
import com.example.lib_common.utils.NetUtils;
import com.example.lib_common.utils.ToolUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class GosAirlinkChooseDeviceWorkWiFiActivity extends GosConfigModuleBaseActivity implements OnClickListener {

    private AlertDialog create;
    private ArrayList<ScanResult> wifiList;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"};
    /**
     * wifi??????
     */
    public WifiInfo wifiInfo;

    /**
     * The et SSID
     */
    private EditText etSSID;

    /**
     * The et Psw
     */
    private EditText etPsw;

    /**
     * The btn Next
     */
    private Button btnNext;

    /** The ll ChooseMode */
    //private LinearLayout llChooseMode;

    /**
     * The cb Laws
     */
    private CheckBox cbLaws;

    /** The tv Mode */
    //private TextView tvMode;

    /**
     * The rl WiFiList
     */
    private RelativeLayout rlWiFiList;

    /**
     * ???????????????
     */
    private String workSSID, workSSIDPsw;

    /**
     * The data
     */
    List<String> modeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_airlink_choose_device_workwifi);
        // ??????ActionBar
        setToolBar(true, getString(R.string.choose_wifi));

        initData();
        initView();
        ininEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            // ??????workSSID && workSSIDPsw
            workSSID = NetUtils.getCurentWifiSSID(this);
            String mypass = spf.getString("mypass", "");

            if (!TextUtils.isEmpty(workSSID)) {
                etSSID.setText(workSSID);
                etSSID.setSelection(workSSID.length());
                if (!TextUtils.isEmpty(mypass)) {
                    JSONObject obj = new JSONObject(mypass);

                    if (obj.has(workSSID)) {
                        String pass = obj.getString(workSSID);
                        etPsw.setText(pass);
                    } else {
                        etPsw.setText("");
                    }
                }
            } else {
                etSSID.setText(NetUtils.getCurentWifiSSID(this));
            }

            // ???????????????????????????????????????????????????
//            if (TextUtils.isEmpty(etPsw.getText().toString())) {
//                cbLaws.setChecked(true);
//                etPsw.setInputType(0x90);
//            } else {
//                etPsw.setInputType(0x81);
//                cbLaws.setChecked(false);
//            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        //tvMode = (TextView) findViewById(R.id.tvMode);
        etSSID = (EditText) findViewById(R.id.etSSID);

        etPsw = (EditText) findViewById(R.id.etPsw);
        cbLaws = (CheckBox) findViewById(R.id.cbLaws);
        btnNext = (Button) findViewById(R.id.btnNext);
        //llChooseMode = (LinearLayout) findViewById(R.id.llChooseMode);
        rlWiFiList = (RelativeLayout) findViewById(R.id.rlWiFiList);

        // ??????????????????
        btnNext.setBackgroundDrawable(GosDeploy.appConfig_BackgroundColor());
        btnNext.setTextColor(GosDeploy.appConfig_Contrast());
        // llChooseMode.setVisibility(GosDeploy.setModuleSelectOn());

        //llChooseMode.setVisibility(View.GONE);
    }


    private void ininEvent() {
        btnNext.setOnClickListener(this);
        rlWiFiList.setOnClickListener(this);
        //llChooseMode.setOnClickListener(this);

        cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String psw = etPsw.getText().toString();

                if (isChecked) {
                    etPsw.setInputType(0x90);
                } else {
                    etPsw.setInputType(0x81);
                }
                etPsw.setSelection(psw.length());
            }
        });

        //????????????????????????????????????
        int permission = ActivityCompat.checkSelfPermission(GosAirlinkChooseDeviceWorkWiFiActivity.this,
                "android.permission.ACCESS_FINE_LOCATION");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            try {
                // ???????????????????????????????????????????????????????????????
                ActivityCompat.requestPermissions(GosAirlinkChooseDeviceWorkWiFiActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initData() {
        modeList = new ArrayList<String>();
        String[] modes = this.getResources().getStringArray(R.array.mode);
        for (String string : modes) {
            modeList.add(string);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnNext) {
            if (ToolUtils.noDoubleClick()) {
                workSSID = etSSID.getText().toString();
                workSSIDPsw = etPsw.getText().toString();

                if (TextUtils.isEmpty(workSSID)) {
                    Toast.makeText(GosAirlinkChooseDeviceWorkWiFiActivity.this, R.string.choose_wifi_list_title, toastTime)
                            .show();
                    return;
                }

                if (!workSSID.equals(NetUtils.getCurentWifiSSID(this))) {
                    final Dialog dialog1 = new AlertDialog.Builder(this, R.style.alert_dialog_style)
                            .setView(new EditText(this)).create();
                    dialog1.setCanceledOnTouchOutside(false);
                    dialog1.show();

                    Window window = dialog1.getWindow();
                    window.setContentView(R.layout.alert_gos_wifi);

                    LinearLayout llNo, llSure;
                    llNo = (LinearLayout) window.findViewById(R.id.llNo);
                    llSure = (LinearLayout) window.findViewById(R.id.llSure);

                    llNo.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog1.cancel();
                            return;
                        }
                    });

                    llSure.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (dialog1.isShowing()) {
                                dialog1.cancel();
                            }
                            isEmptyAlert();
                        }
                    });
                } else {
                    isEmptyAlert();
                }
            }
        } else if (id == R.id.rlWiFiList) {
            AlertDialog.Builder dia = new AlertDialog.Builder(GosAirlinkChooseDeviceWorkWiFiActivity.this);
            View view = View.inflate(GosAirlinkChooseDeviceWorkWiFiActivity.this, R.layout.alert_gos_wifi_list, null);
            ListView listview = (ListView) view.findViewById(R.id.wifi_list);
            List<ScanResult> rsList = NetUtils.getCurrentWifiScanResult(this);
            List<String> localList = new ArrayList<String>();
            localList.clear();
            wifiList = new ArrayList<ScanResult>();
            wifiList.clear();
            for (ScanResult sss : rsList) {
                if (sss.SSID.contains(SoftAP_Start)) {
                } else {
                    if (localList.toString().contains(sss.SSID)) {
                    } else {
                        localList.add(sss.SSID);
                        wifiList.add(sss);
                    }
                }
            }
            if (wifiList.size() == 0) {
                LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // ????????????????????????????????????????????????????????????????????????????????????????????????
                    Toast.makeText(this, getString(R.string.open), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            WifiListAdapter adapter = new WifiListAdapter(wifiList);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    ScanResult sResult = wifiList.get(arg2);
                    String sSID = sResult.SSID;
                    etSSID.setText(sSID);
                    etSSID.setSelection(sSID.length());
                    etPsw.setText("");
                    create.dismiss();
                }
            });
            dia.setView(view);
            create = dia.create();
            create.show();
        }
    }

    private static final String TAG = "GosAirlinkChooseDevice";

    private void toAirlinkReady() {
        // ??????????????????????????????wifi?????????

        try {
            String mypass = spf.getString("mypass", "");
            if (TextUtils.isEmpty(mypass)) {
                JSONObject mUserAndPass = new JSONObject();
                mUserAndPass.put(workSSID, workSSIDPsw);
                spf.edit().putString("mypass", mUserAndPass.toString()).commit();
            } else {
                JSONObject obj = new JSONObject(mypass);
                obj.put(workSSID, workSSIDPsw);
                spf.edit().putString("mypass", obj.toString()).commit();
            }
        } catch(JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        spf.edit().putString("workSSID", workSSID).commit();
        spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();
        //wifiModuleType-false-start  wifiModuleType-true-start
        if (GosDeploy.appConfig_WifiModuleType().size() == 0) {
            //wifiModuleType-false-end
            Intent intent = new Intent(this, GosModeListActivity2.class);
            startActivity(intent);
            //wifiModuleType-false-start
        } else {
            //wifiModuleType-true-end
            List<Integer> moduleTypes = GosDeploy.appConfig_WifiModuleType();
            JSONArray array = new JSONArray();
            for (int type : moduleTypes) {
                if (type == 4) {
                    type = 0;
                } else if (type < 4) {
                    type = type + 1;
                } else if (type == 12) {
                    type = 6;
                } else if (type >= 6 && type < 12) {
                    type = type + 1;
                }
                array.put(type);
            }
            spf.edit().putString("modulestyles", array.toString()).commit();
            Intent intent = new Intent(this, GosAirlinkReadyActivity.class);
            startActivity(intent);
            //wifiModuleType-true-start
        }
        //wifiModuleType-false-end  wifiModuleType-true-end

    }

    /*
     * // ?????????????????????WiFi?????????????????? protected boolean checkworkSSIDUsed(String workSSID)
     * { if (spf.contains("workSSID")) { if (spf.getString("workSSID",
     * "").equals(workSSID)) { return true; } } return false; }
     */


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent = new Intent(this, GosMainActivity.class);
//                startActivity(intent);
                //quitAlert(this, intent);
                finish();
                break;
        }

        return true;
    }


    private void isEmptyAlert() {
        if (TextUtils.isEmpty(workSSIDPsw)) {
            final Dialog dialog = new AlertDialog.Builder(this, R.style.alert_dialog_style)
                    .setView(new EditText(this)).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Window window = dialog.getWindow();
            window.setContentView(R.layout.alert_gos_empty);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
            LinearLayout llNo, llSure;
            llNo = (LinearLayout) window.findViewById(R.id.llNo);
            llSure = (LinearLayout) window.findViewById(R.id.llSure);

            llNo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();

                }
            });

            llSure.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (dialog.isShowing()) {
                        dialog.cancel();
                    }
                    toAirlinkReady();
                }
            });
        } else {
            toAirlinkReady();
        }
    }

    class WifiListAdapter extends BaseAdapter {

        ArrayList<ScanResult> xpgList;

        public WifiListAdapter(ArrayList<ScanResult> list) {
            this.xpgList = list;
        }

        @Override
        public int getCount() {
            return xpgList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder;
            if (view == null) {
                view = LayoutInflater.from(GosAirlinkChooseDeviceWorkWiFiActivity.this)
                        .inflate(R.layout.item_gos_wifi_list, null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            String ssid = xpgList.get(position).SSID;
            holder.getTextView().setText(ssid);

            return view;
        }

    }

    class Holder {
        View view;

        public Holder(View view) {
            this.view = view;
        }

        TextView textView;

        public TextView getTextView() {
            if (textView == null) {
                textView = (TextView) view.findViewById(R.id.SSID_text);
            }
            return textView;
        }
    }
}
