//package com.example.module_device;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.res.ColorStateList;
//import android.graphics.PorterDuff;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.TextUtils;
//import android.text.style.ForegroundColorSpan;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentPagerAdapter;
//
//import com.example.lib_common.common.GosDeviceModuleBaseActivity;
//import com.example.lib_common.common.NoScrollViewPager;
//import com.example.lib_common.common.GosDeploy;
//import com.example.lib_common.utils.ToolUtils;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//
//
//public class GosMainActivity extends GosDeviceModuleBaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
//
//    Context context = null;
//    @SuppressWarnings("deprecation")
//
//    private int viewPagerSelected = 0;
//    private Intent intent;
//
//    public static Activity instance = null;
//    private BottomNavigationView navigation;
//    private NoScrollViewPager viewPager;
//
//    private static final int REQUEST_EXTERNAL_STORAGE = 22;
//    private static String[] PERMISSIONS_STORAGE = {"android.permission.CAMERA"};
//
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.navigation_device:
//                if (GosDeploy.appConfig_BindDevice_Qrcode()) {
//                    int color = GosDeploy.appConfig_Contrast();
//                    final Drawable upArrow = getResources().getDrawable(R.drawable.common_qrcode_button);
//                    upArrow.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
//                    mToolbar.setNavigationIcon(upArrow);
//                    SpannableString ssTitle = new SpannableString(this.getString(R.string.devicelist_title));
//                    ssTitle.setSpan(new ForegroundColorSpan(GosDeploy.appConfig_Contrast()), 0, ssTitle.length(),
//                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    tvTitle.setText(ssTitle);
//                } else {
//                    mToolbar.setNavigationIcon(null);
//                    SpannableString ssTitle = new SpannableString(this.getString(R.string.devicelist_title));
//                    ssTitle.setSpan(new ForegroundColorSpan(GosDeploy.appConfig_Contrast()), 0, ssTitle.length(),
//                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                    tvTitle.setText(ssTitle);
//                }
//                break;
//            case R.id.navigation_message:
//                mToolbar.setNavigationIcon(null);
//                SpannableString ssTitle1 = new SpannableString(this.getString(R.string.messagecenter));
//                ssTitle1.setSpan(new ForegroundColorSpan(GosDeploy.appConfig_Contrast()), 0, ssTitle1.length(),
//                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                tvTitle.setText(ssTitle1);
//                break;
//            case R.id.navigation_user:
//                mToolbar.setNavigationIcon(null);
//                SpannableString ssTitle2 = new SpannableString(this.getString(R.string.personal_center));
//                ssTitle2.setSpan(new ForegroundColorSpan(GosDeploy.appConfig_Contrast()), 0, ssTitle2.length(),
//                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                tvTitle.setText(ssTitle2);
//                break;
//        }
//        viewPager.setCurrentItem(TabFragment.form(item.getItemId()).ordinal());
//        return true;
//    }
//
//    private enum TabFragment {
//
//        device(R.id.navigation_device, GosDeviceListFragment.class),
//        message(R.id.navigation_message, MessageCenterFragment.class),
//        user(R.id.navigation_user, GosSettiingsFragment.class);
//
//        private int menuId;
//        private Class<? extends Fragment> mClass;
//        private Fragment fragment;
//
//        TabFragment(int menuId, Class<? extends Fragment> mClass) {
//            this.menuId = menuId;
//            this.mClass = mClass;
//        }
//
//        private Fragment fragment() {
//            if (fragment == null) {
//                try {
//                    fragment = mClass.newInstance();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    fragment = new Fragment();
//                }
//            }
//            return fragment;
//        }
//
//        public static TabFragment form(int menuId) {
//            for (TabFragment fragment : values()) {
//                if (fragment.menuId == menuId) {
//                    return fragment;
//                }
//            }
//            return user;
//        }
//
//        public static void onDestroy() {
//            for (TabFragment fragment : values()) {
//                fragment.fragment = null;
//            }
//        }
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gos_main);
//        context = GosMainActivity.this;
//        if (GosDeploy.appConfig_BindDevice_Qrcode()) {
//            setToolBar(true, R.string.devicelist_title);
//        } else {
//            setToolBar(false, R.string.devicelist_title);
//        }
//        initView();
//    }
//
//    private void initView() {
//        navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        setThemeColor(GosDeploy.appConfig_Background());
//        navigation.setOnNavigationItemSelectedListener(this);
//        viewPager = (NoScrollViewPager) findViewById(R.id.content);
//        viewPager.setNoScroll(true);
//        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return TabFragment.values()[position].fragment();
//            }
//
//            @Override
//            public int getCount() {
//                return TabFragment.values().length;
//            }
//        });
//
//        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                viewPagerSelected = position;
//                supportInvalidateOptionsMenu();
//                navigation.setSelectedItemId(TabFragment.values()[position].menuId);
//            }
//        });
//    }
//
//    //config-all-start
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        switch (viewPagerSelected) {
//            case 0:
//                if (GosDeploy.appConfig_Config_Airlink()) {
//                    if (GosDeploy.appConfig_Config_Softap()) {
//                        getMenuInflater().inflate(R.menu.gosdeviceconfig, menu);
//                    } else {
//                        getMenuInflater().inflate(R.menu.gosnull, menu);
//                        MenuItem menuItem = menu.findItem(R.id.add);
//                        menuItem.setIcon(ToolUtils.editIcon(getResources(), R.drawable.deviceonboarding_add));
//                    }
//                } else {
//                    if (GosDeploy.appConfig_Config_Softap()) {
//                        getMenuInflater().inflate(R.menu.gosnull, menu);
//                        MenuItem menuItem = menu.findItem(R.id.add);
//                        menuItem.setIcon(ToolUtils.editIcon(getResources(), R.drawable.deviceonboarding_add));
//                    }
//                }
//                break;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            //bindDevice_qrcode-false-start
//            case android.R.id.home:
//                if (GosDeploy.appConfig_BindDevice_Qrcode()) {
//                    int permission = ActivityCompat.checkSelfPermission(GosMainActivity.this,
//                            "android.permission.CAMERA");
//                    if (permission != PackageManager.PERMISSION_GRANTED) {
//                        try {
//                            // ???????????????????????????????????????????????????????????????
//                            ActivityCompat.requestPermissions(GosMainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        intent = new Intent(GosMainActivity.this, CaptureActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//                    }
//                }
//                break;
//            //bindDevice_qrcode-false-end
//            //config_airlink-false-start     config_softap-false-start
//            case R.id.airlink_config:
//                if (!checkNetwork(GosMainActivity.this)) {
//                    Toast.makeText(GosMainActivity.this, R.string.network_error, 2000).show();
//                } else {
//                    intent = new Intent(GosMainActivity.this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.softap_config:
//                if (!checkNetwork(GosMainActivity.this)) {
//                    Toast.makeText(GosMainActivity.this, R.string.network_error, 2000).show();
//                } else {
//                    intent = new Intent(GosMainActivity.this, GosChooseDeviceWorkWiFiActivity.class);
//                    startActivity(intent);
//                }
//                break;
//            //config_softap-false-end   config_airlink-false-end
//            case R.id.add:
//                if (GosDeploy.appConfig_Config_Airlink()) {
//                    if (!checkNetwork(GosMainActivity.this)) {
//                        Toast.makeText(GosMainActivity.this, R.string.network_error, 2000).show();
//                    } else {
//                        intent = new Intent(GosMainActivity.this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
//                        startActivity(intent);
//                    }
//                }
//                if (GosDeploy.appConfig_Config_Softap()) {
//                    if (!checkNetwork(GosMainActivity.this)) {
//                        Toast.makeText(GosMainActivity.this, R.string.network_error, 2000).show();
//                    } else {
//                        intent = new Intent(GosMainActivity.this, GosChooseDeviceWorkWiFiActivity.class);
//                        startActivity(intent);
//                    }
//                }
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    //config-all-end
//
//    //bindDevice_qrcode-false-start
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (GosDeploy.appConfig_BindDevice_Qrcode()) {
//            if (requestCode == 22) {
//                intent = new Intent(GosMainActivity.this, CaptureActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//            }
//        }
//    }
//    //bindDevice_qrcode-false-end
//
//
//    /**
//     * ????????????????????????
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exitBy2Click(); // ????????????????????????
//        }
//        return false;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == 666) {
//            finish();
//        } else if (resultCode == 98765) {
//            TipsDialog dialog = new TipsDialog(GosMainActivity.this,
//                    getResources().getString(R.string.devicedisconnected));
//            dialog.show();
//        }
//    }
//
//
//    public void exitBy2Click() {
//        Timer tExit = null;
//        if (isExit == false) {
//            isExit = true; // ???????????????
//            String doubleClick;
//            if (!TextUtils.isEmpty(spf.getString("UserName", ""))
//                    && !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
//                doubleClick = (String) getText(R.string.doubleclick_logout);
//            } else {
//                if (getIntent().getBooleanExtra("ThredLogin", false)) {
//                    doubleClick = (String) getText(R.string.doubleclick_logout);
//                } else {
//                    doubleClick = (String) getText(R.string.doubleclick_back);
//                }
//            }
//
//            Toast.makeText(this, doubleClick, 2000).show();
//            tExit = new Timer();
//            tExit.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    isExit = false; // ????????????
//                }
//            }, 2000); // ??????2?????????????????????????????????????????????????????????????????????????????????
//
//        } else {
//            logoutToClean();
//        }
//    }
//
//    /**
//     * ????????????
//     */
//    void logoutToClean() {
//        spf.edit().putString("UserName", "").commit();
//        spf.edit().putString("PassWord", "").commit();
//        spf.edit().putString("Uid", "").commit();
//        //push-all-start
//        GosPushManager.pushUnBindService(spf.getString("Token", ""));
//        //push-all-end
//        spf.edit().putString("Token", "").commit();
//
//        finish();
//        if (GosDeviceListFragment.loginStatus == 1) {
//            GosDeviceListFragment.loginStatus = 0;
//        } else {
//            GosDeviceListFragment.loginStatus = 4;
//        }
//    }
//
//    /**
//     * ??????????????????
//     */
//    private static Boolean isExit = false;
//
//    /**
//     * ????????????
//     *
//     * @param colorPrimary ?????????????????????
//     */
//    private void setThemeColor(int colorPrimary) {
//        final Drawable add = getResources().getDrawable(R.drawable.deviceonboarding_add);
//        int color = GosDeploy.appConfig_Contrast();
//        add.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
//        mToolbar.setOverflowIcon(add);
//        //  bindDevice_qrcode-false-start
//        if (GosDeploy.appConfig_BindDevice_Qrcode()) {
//            final Drawable upArrow = getResources().getDrawable(R.drawable.common_qrcode_button);
//            upArrow.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
//            mToolbar.setNavigationIcon(upArrow);
//        }
//        //  bindDevice_qrcode-false-end
//        //????????????????????????????????????
//        int[][] states = new int[][]{
//                new int[]{-android.R.attr.state_checked},
//                new int[]{android.R.attr.state_checked}
//        };
//
//        int[] colors = new int[]{ToolUtils.editTextAlpha(), color};
//        ColorStateList csl = new ColorStateList(states, colors);
//        //  ????????????
//        navigation.setBackgroundColor(colorPrimary);
//        //   ????????????
//        navigation.setItemIconTintList(csl);
//        // ????????????
//        navigation.setItemTextColor(csl);
//    }
//}
