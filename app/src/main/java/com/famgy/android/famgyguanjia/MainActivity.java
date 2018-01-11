package com.famgy.android.famgyguanjia;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private static final String TAG = "===FamgyGuanjia===";
    private AccessibilityManager mAccessibilityManager;
    private ComponentName componentName;
    private DevicePolicyManager devicePolicyManager;
    private static final String ServerName = "com.famgy.android.famgyguanjia/.RobService";



    private Button bt_service_switch;
    private Button bt_reg_device;
    private Button bt_unreg_device;
    private Button bt_lock_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        componentName =  new ComponentName(this, DeviceManagerBC.class);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

        bt_service_switch = (Button)findViewById(R.id.bt_service_switch);
        bt_service_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        bt_reg_device = (Button)findViewById(R.id.bt_reg_device);
        bt_reg_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                startActivity(intent);
            }
        });

        bt_unreg_device = (Button)findViewById(R.id.bt_unreg_device);
        bt_unreg_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devicePolicyManager.removeActiveAdmin(componentName);
            }
        });

        bt_lock_screen = (Button)findViewById(R.id.bt_lock_screen);
        bt_lock_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (true == devicePolicyManager.isAdminActive(componentName)) {
                    devicePolicyManager.resetPassword("123456", 0);
                    devicePolicyManager.lockNow();
                } else {
                    Toast.makeText(MainActivity.this, "没有设备管理/系统组件权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        bt_service_switch = (Button)findViewById(R.id.bt_service_switch);
        mAccessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (false == checkEnabledAccessibilityService()) {
            bt_service_switch.setText("启动辅助服务");
        } else {
            bt_service_switch.setText("关闭辅助服务");
        }
    }

    private boolean checkEnabledAccessibilityService() {
        List<AccessibilityServiceInfo> accessibilityServiceInfoList =
                mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServiceInfoList) {
            Log.i(TAG, info.getId());
            if (info.getId().equals(ServerName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
