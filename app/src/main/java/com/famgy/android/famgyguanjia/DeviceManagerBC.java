package com.famgy.android.famgyguanjia;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by famgy on 1/5/18.
 */

public class DeviceManagerBC extends DeviceAdminReceiver {
    private static final String TAG = "===DeviceManagerBC===";

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.i(TAG, "已经注册成为系统组件");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Log.i(TAG, "已经注销成为系统组件");

        final DevicePolicyManager mDevicepolicymanager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        // Setting reset system password
        mDevicepolicymanager.resetPassword("123456", 0);
        mDevicepolicymanager.lockNow();
    }

    @Override
    public  CharSequence onDisableRequested(Context context, Intent intent) {
        Log.i(TAG, "onDisableRequested, 正在注销成为系统组件");

        return "取消EMM设备激活，会导致EMM功能不能正常使用。";
    }
}
