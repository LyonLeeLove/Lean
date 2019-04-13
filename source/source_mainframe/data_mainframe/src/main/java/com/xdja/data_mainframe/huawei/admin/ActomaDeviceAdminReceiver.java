package com.xdja.data_mainframe.huawei.admin;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.xdja.dependence.uitls.LogUtil;

/**
 * Project：ActomaV2                  <br>
 * Description:                               <br>
 * Time：2019/1/14 11:48                  <br>
 *
 * @author liming@xdja.com                     <br>
 * @version V3.1.6                           <br>
 */
public class ActomaDeviceAdminReceiver extends DeviceAdminReceiver {
    private final static String TAG = ActomaDeviceAdminReceiver.class.getSimpleName();
    @Override
    public void onEnabled(Context context, Intent intent) {
        LogUtil.getUtils().d(TAG, "onEnabled");
        super.onEnabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        LogUtil.getUtils().d(TAG, "onDisableRequested");
        return super.onDisableRequested(context, intent);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        LogUtil.getUtils().d(TAG, "onDisabled");
        super.onDisabled(context, intent);
    }
}
