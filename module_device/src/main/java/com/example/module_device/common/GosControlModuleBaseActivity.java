package com.example.module_device.common;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.module_device.utils.HexStrUtils;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;

import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;

public class GosControlModuleBaseActivity extends GosBaseActivity {

    /*
     * ===========================================================
     * 以下key值对应开发者在云端定义的数据点标识名
     * ===========================================================
     */
    // 数据点"开启/关闭红色灯"对应的标识名
    protected static final String KEY_LED_ONOFF = "LED_OnOff";
    // 数据点"设定电机转速"对应的标识名
    protected static final String KEY_MOTOR_SPEED = "Motor_Speed";
    // 数据点"红外探测"对应的标识名
    protected static final String KEY_INFRARED = "Infrared";
    // 数据点"环境温度"对应的标识名
    protected static final String KEY_TEMPERATURE = "Temperature";
    // 数据点"环境湿度"对应的标识名
    protected static final String KEY_HUMIDITY = "Humidity";
    // 数据点"报警1"对应的标识名
    protected static final String KEY_ALERT_1 = "Alert_1";
    // 数据点"报警2"对应的标识名
    protected static final String KEY_ALERT_2 = "Alert_2";
    // 数据点"LED故障"对应的标识名
    protected static final String KEY_FAULT_LED = "Fault_LED";
    // 数据点"电机故障"对应的标识名
    protected static final String KEY_FAULT_MOTOR = "Fault_Motor";
    // 数据点"温湿度传感器故障"对应的标识名
    protected static final String KEY_FAULT_TEMHUM = "Fault_TemHum";
    // 数据点"红外传感器故障"对应的标识名
    protected static final String KEY_FAULT_IR = "Fault_IR";

    /*
     * ===========================================================
     * 以下数值对应开发者在云端定义的可写数值型数据点增量值、数据点定义的分辨率、seekbar滚动条补偿值
     * _ADDITION:数据点增量值
     * _RATIO:数据点定义的分辨率
     * _OFFSET:seekbar滚动条补偿值
     * APP与设备定义的协议公式为：y（APP接收的值）=x（设备上报的值）* RATIO（分辨率）+ADDITION（增量值）
     * 由于安卓的原生seekbar无法设置最小值，因此代码中增加了一个补偿量OFFSET
     * 实际上公式中的：x（设备上报的值）=seekbar的值+补偿值
     * ===========================================================
     */
    // 数据点"设定电机转速"对应seekbar滚动条补偿值
    protected static final int MOTOR_SPEED_OFFSET = 0;
    // 数据点"设定电机转速"对应数据点增量值
    protected static final int MOTOR_SPEED_ADDITION = -5;
    // 数据点"设定电机转速"对应数据点定义的分辨率
    protected static final int MOTOR_SPEED_RATIO = 1;


    /*
     * ===========================================================
     * 以下变量对应设备上报类型为布尔、数值、扩展数据点的数据存储
     * ===========================================================
     */
    // 数据点"开启/关闭红色灯"对应的存储数据
    protected static boolean data_LED_OnOff;
    // 数据点"设定电机转速"对应的存储数据
    protected static int data_Motor_Speed;
    // 数据点"红外探测"对应的存储数据
    protected static boolean data_Infrared;
    // 数据点"环境温度"对应的存储数据
    protected static int data_Temperature;
    // 数据点"环境湿度"对应的存储数据
    protected static int data_Humidity;
    // 数据点"报警1"对应的存储数据
    protected static boolean data_Alert_1;
    // 数据点"报警2"对应的存储数据
    protected static boolean data_Alert_2;
    // 数据点"LED故障"对应的存储数据
    protected static boolean data_Fault_LED;
    // 数据点"电机故障"对应的存储数据
    protected static boolean data_Fault_Motor;
    // 数据点"温湿度传感器故障"对应的存储数据
    protected static boolean data_Fault_TemHum;
    // 数据点"红外传感器故障"对应的存储数据
    protected static boolean data_Fault_IR;

    /*
     * ===========================================================
     * 以下key值对应设备硬件信息各明细的名称，用与回调中提取硬件信息字段。
     * ===========================================================
     */
    protected static final String WIFI_HARDVER_KEY = "wifiHardVersion";
    protected static final String WIFI_SOFTVER_KEY = "wifiSoftVersion";
    protected static final String MCU_HARDVER_KEY = "mcuHardVersion";
    protected static final String MCU_SOFTVER_KEY = "mcuSoftVersion";
    protected static final String WIFI_FIRMWAREID_KEY = "wifiFirmwareId";
    protected static final String WIFI_FIRMWAREVER_KEY = "wifiFirmwareVer";
    protected static final String PRODUCT_KEY = "productKey";

    private Toast mToast;

    @SuppressWarnings("unchecked")
    protected void getDataFromReceiveDataMap(ConcurrentHashMap<String, Object> dataMap) {
        // 已定义的设备数据点，有布尔、数值和枚举型数据

        if (dataMap.get("data") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
            for (String dataKey : map.keySet()) {
                if (dataKey.equals(KEY_LED_ONOFF)) {
                    data_LED_OnOff = (Boolean) map.get(dataKey);
                }
                if (dataKey.equals(KEY_MOTOR_SPEED)) {

                    data_Motor_Speed = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_INFRARED)) {
                    data_Infrared = (Boolean) map.get(dataKey);
                }
                if (dataKey.equals(KEY_TEMPERATURE)) {

                    data_Temperature = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_HUMIDITY)) {

                    data_Humidity = (Integer) map.get(dataKey);
                }
                if (dataKey.equals(KEY_ALERT_1)) {
                    data_Alert_1 = (Boolean) map.get(dataKey);
                }
                if (dataKey.equals(KEY_ALERT_2)) {
                    data_Alert_2 = (Boolean) map.get(dataKey);
                }
                if (dataKey.equals(KEY_FAULT_LED)) {
                    data_Fault_LED = (Boolean) map.get(dataKey);
                }
                if (dataKey.equals(KEY_FAULT_MOTOR)) {
                    data_Fault_Motor = (Boolean) map.get(dataKey);
                }
                if (dataKey.equals(KEY_FAULT_TEMHUM)) {
                    data_Fault_TemHum = (Boolean) map.get(dataKey);
                }
                if (dataKey.equals(KEY_FAULT_IR)) {
                    data_Fault_IR = (Boolean) map.get(dataKey);
                }
            }
        }

        StringBuilder sBuilder = new StringBuilder();

        // 已定义的设备报警数据点，设备发生报警后该字段有内容，没有发生报警则没内容
        if (dataMap.get("alerts") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("alerts");
            for (String alertsKey : map.keySet()) {
                if ((Boolean) map.get(alertsKey)) {
                    sBuilder.append("报警:" + alertsKey + "=true" + "\n");
                }
            }
        }

        // 已定义的设备故障数据点，设备发生故障后该字段有内容，没有发生故障则没内容
        if (dataMap.get("faults") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("faults");
            for (String faultsKey : map.keySet()) {
                if ((Boolean) map.get(faultsKey)) {
                    sBuilder.append("故障:" + faultsKey + "=true" + "\n");
                }
            }
        }

        if (sBuilder.length() > 0) {
            sBuilder.insert(0, "[设备故障或报警]\n");
            myToast(sBuilder.toString().trim());
        }

        // 透传数据，无数据点定义，适合开发者自行定义协议自行解析
        if (dataMap.get("binary") != null) {
            byte[] binary = (byte[]) dataMap.get("binary");
            Log.i("", "Binary data:" + HexStrUtils.bytesToHexString(binary));
        }
    }

    public GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        /** 用于设备订阅 */
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            GosControlModuleBaseActivity.this.didSetSubscribe(result, device, isSubscribed);
        }

        ;

        /** 用于获取设备状态 */
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                   ConcurrentHashMap<String, Object> dataMap, int sn) {
            GosControlModuleBaseActivity.this.didReceiveData(result, device, dataMap, sn);
        }

        ;

        /** 用于设备硬件信息 */
        public void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                       ConcurrentHashMap<String, String> hardwareInfo) {
            GosControlModuleBaseActivity.this.didGetHardwareInfo(result, device, hardwareInfo);
        }

        ;

        /** 用于修改设备信息 */
        public void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
            GosControlModuleBaseActivity.this.didSetCustomInfo(result, device);
        }

        ;

        /** 用于设备状态变化 */
        public void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
            GosControlModuleBaseActivity.this.didUpdateNetStatus(device, netStatus);
        }

        ;

    };

    /**
     * 设备订阅回调
     *
     * @param result       错误码
     * @param device       被订阅设备
     * @param isSubscribed 订阅状态
     */
    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
    }

    /**
     * 设备状态回调
     *
     * @param result  错误码
     * @param device  当前设备
     * @param dataMap 当前设备状态
     * @param sn      命令序号
     */
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                  ConcurrentHashMap<String, Object> dataMap, int sn) {
    }

    /**
     * 设备硬件信息回调
     *
     * @param result       错误码
     * @param device       当前设备
     * @param hardwareInfo 当前设备硬件信息
     */
    protected void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                      ConcurrentHashMap<String, String> hardwareInfo) {
    }

    /**
     * 修改设备信息回调
     *
     * @param result 错误码
     * @param device 当前设备
     */
    protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
    }

    /**
     * 设备状态变化回调
     */
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void myToast(String string) {
        if (mToast != null) {
            mToast.setText(string);
        } else {
            mToast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    protected void hideKeyBoard() {
        // 隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    /**
     * Description:显示格式化数值，保留对应分辨率的小数个数，比如传入参数（20.3656，0.01），将返回20.37
     *
     * @param date  传入的数值
     * @param scale 保留多少位小数
     * @return
     */
    protected String formatValue(double date, Object scale) {
        if (scale instanceof Double) {
            DecimalFormat df = new DecimalFormat(scale.toString());
            return df.format(date);
        }
        return Math.round(date) + "";
    }

}