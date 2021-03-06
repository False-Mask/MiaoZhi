package com.example.lib_common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.example.lib_common.R;
import com.example.lib_common.common.GosDeploy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/7/25.
 */

public class ToolUtils {
    public static float getHue(int color) {
        float[] HSV = new float[3];
        Color.colorToHSV(color, HSV);
        return (HSV[0] * 255 / 360);
    }

    public static int getInnerColor(float hue) {
        float[] HSV = new float[3];
        HSV[0] = hue * 360 / 255;
        HSV[1] = 255;
        HSV[2] = 255;
        return Color.HSVToColor(HSV);
    }

    private static long lastClickTime = 0;
    public static final int MIN_CLICK_DELAY_TIME = 800;

    public static boolean noDoubleClick() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return true;
        } else {
            return false;
        }
    }

    public static List<Map<String, Object>> parseJson(Context context, String str) throws JSONException {
        if (str == null || str.equals("")) {
            Toast.makeText(context, context.getString(R.string.download_fail), Toast.LENGTH_SHORT).show();
        }
        JSONObject uiJsonObject = new JSONObject(str);
        JSONObject jsonObject = uiJsonObject.has("object") ? uiJsonObject.getJSONObject("object") : null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (uiJsonObject != null) {
            JSONArray sessionJsonArrays = uiJsonObject.getJSONArray("sections");
            if (sessionJsonArrays != null) {
                for (int i = 0; i < sessionJsonArrays.length(); i++) {
                    JSONObject session = sessionJsonArrays.getJSONObject(i);
                    JSONArray elementjJsonArray = session.getJSONArray("elements");
                    if (elementjJsonArray != null) {
                        for (int j = 0; j < elementjJsonArray.length(); j++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            JSONObject elementObject = elementjJsonArray.getJSONObject(j);
                            String type = elementObject.getString("type");
                            String title = elementObject.has("title") ? elementObject.getString("title") : "";
                            map.put("title", title);
                            map.put("type", type);
                            String value = null;
                            if (type.equals("QBooleanElement")) {
                                value = elementObject.has("boolValue") ? elementObject.getString("boolValue") : "";
                            } else if (type.equals("QFloatElement")) {
                                value = elementObject.has("value") ? elementObject.getString("value") : "";
                            }
                            map.put("value", value);
                            list.add(map);
                        }
                    }
                }

            }
        }

        return list;
    }


    public static Drawable editIconAlpha(Resources res, int id) {
        int color = GosDeploy.appConfig_Contrast();
        Drawable drawable = SkxDrawableHelper.tintDrawable(res.getDrawable(id), color);
        drawable.setAlpha(60);
        return drawable;
    }

    public static Drawable editIcon(Resources res, int id) {
        int color = GosDeploy.appConfig_Contrast();
        return SkxDrawableHelper.tintDrawable(res.getDrawable(id), color);
    }

    public static int editTextAlpha() {
        int color = GosDeploy.appConfig_Contrast();
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int color1 = Color.argb(160, red, green, blue);
        return color1;
    }

    public static int editStatusBarColor(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int color1 = Color.argb(255, red, green, blue);
        return color1;
    }
//    // ?????????????????? ????????????
//    static long[] mHits = null;
//    //public static boolean mShow = false;
//
//    public static void onDisplaySettingButton() {
//        if (mHits == null) {
//            mHits = new long[5];
//        }
//        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);//?????????????????????????????????????????????????????????????????????????????????
//        mHits[mHits.length - 1] = SystemClock.uptimeMillis();//??????????????????
//        if (SystemClock.uptimeMillis() - mHits[0] <= 1000) {//????????????????????????
//            mHits = null;    //??????????????????????????????????????????????????????????????????????????????????????????????????????????????? ????????????????????????????????????????????????????????????
////            if (mShow) {
////                mShow = false;
////            } else {
////                mShow = true;
////            }
//            mShow = true;
//            Log.e("Tool", "onDisplaySettingButton: ??????????????????deploy??????");
//            //??????????????????mShow?????????sp??????
//        }
//    }


}
