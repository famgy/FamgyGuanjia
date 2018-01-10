package com.famgy.android.famgyguanjia;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by famgy on 17-12-27.
 */

public class RobService extends AccessibilityService {

    private boolean isOpenedPacket = false;
    private static final String TAG = "===RobService===";
    private static final String TEXT_DETERMINE = "确定";
    private static final String TEXT_FORCE_STOP = "强行停止";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(null == event || null == event.getSource())
        {
            return;
        }

        int eventType = event.getEventType();
        String className = null;
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                //Log.e(TAG, "TYPE_NOTIFICATION_STATE_CHANGED");
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.e(TAG, "TYPE_WINDOW_STATE_CHANGED");

                handleWindowStateChangedEvent(event);

                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                //Log.e(TAG, "TYPE_WINDOW_CONTENT_CHANGED");
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void handleWindowStateChangedEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> infos = null;
        List<AccessibilityNodeInfo> subInfos = null;
        String className = null;


        className = event.getClassName().toString();
        Log.e(TAG, "CurClassName : " + className);
        if (className.equals("com.android.settings.applications.InstalledAppDetailsTop")) {
            simulationClick(event, TEXT_FORCE_STOP);
        } else if (className.equals("android.app.AlertDialog")) {
            simulationClick(event, TEXT_DETERMINE);
            //performGlobalAction(GLOBAL_ACTION_BACK);
        }

        if (nodeInfo != null) {

            /* 备份和重置 */
            infos = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/action_bar");
            if (null != infos && !infos.isEmpty()) {
                for (AccessibilityNodeInfo item : infos) {
                    //Log.e(TAG, "android:id/action_bar, childCount : " + item.getChildCount());

                    if (item.getChildCount() >= 2) {
                        if ((null != item.getChild(1).getText()) && (item.getChild(1).getText().toString().equals("备份和重置"))) {
                            Log.e(TAG, "备份和设置");


                            ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                            List<ActivityManager.RunningAppProcessInfo> appProcessInfos = mActivityManager.getRunningAppProcesses();
                            for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
                                Log.i("===RobService===", appProcessInfo.toString());
                                showPackageDetail("com.android.settings");
                            }

                            //item.getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        } else if ((null != item.getChild(1).getText()) && (item.getChild(1).getText().toString().equals("恢复出厂设置"))) {
                            //Log.e(TAG, "恢复出厂设置");
                            item.getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        } else if ((null != item.getChild(1).getText()) && (item.getChild(1).getText().toString().equals("要恢复出厂设置吗？"))) {
                            //Log.e(TAG, "要恢复出厂设置吗？");
                            item.getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
            }

            /* 辅助功能开关 */
            infos = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/action_bar");
            if (null != infos && !infos.isEmpty()) {
                for (AccessibilityNodeInfo item : infos) {
                    //Log.e(TAG, "android:id/action_bar, childCount : " + item.getChildCount());

                    if (item.getChildCount() >= 2) {
                        if ((null != item.getChild(1).getText()) && (item.getChild(1).getText().toString().equals("FamgyGuanjia"))) {
                            Log.e(TAG, "辅助功能开关");

                            performGlobalAction(GLOBAL_ACTION_BACK);
                        }
                    }
                }
            }

            nodeInfo.recycle();
        }
    }

    private void simulationClick(AccessibilityEvent event, String text){
        List<AccessibilityNodeInfo> nodeInfoList = event.getSource().findAccessibilityNodeInfosByText(text);
        for (AccessibilityNodeInfo node : nodeInfoList) {
            if (node.isClickable() && node.isEnabled()) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    private void showPackageDetail(String packageName){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.e(TAG, "===================onKeyEvent===================");
        int key = event.getKeyCode();
        switch(key){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                Intent downintent = new Intent("com.exmaple.broadcaster.KEYDOWN");
//                downintent.putExtra("dtime", System.currentTimeMillis());
//                sendBroadcast(downintent);
                Log.i(TAG, "KEYCODE_VOLUME_DOWN");
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
//                Intent upintent = new Intent("com.exmaple.broadcaster.KEYUP");
//                upintent.putExtra("utime", System.currentTimeMillis());
//                sendBroadcast(upintent);
                Log.i(TAG, "KEYCODE_VOLUME_UP");
                break;
            default:
                break;
        }
        //return super.onKeyEvent(event);
        return true;
    }

    @Override
    public void onInterrupt() {

    }
}
