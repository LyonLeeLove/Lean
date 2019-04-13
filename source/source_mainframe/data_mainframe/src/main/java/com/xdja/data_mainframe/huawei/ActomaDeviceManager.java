package com.xdja.data_mainframe.huawei;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import com.huawei.android.app.admin.DeviceApplicationManager;
import com.huawei.android.app.admin.DeviceControlManager;
import com.huawei.android.app.admin.DeviceHwSystemManager;
import com.xdja.data_mainframe.huawei.admin.ActomaDeviceAdminReceiver;
import com.xdja.dependence.uitls.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Project：ActomaV2                  <br>
 * Description:                               <br>
 * Time：2019/1/14 11:47                  <br>
 *
 * @author liming@xdja.com                   <br>
 * @version V3.1.6                           <br>
 */
public class ActomaDeviceManager {
    private final static String TAG = ActomaDeviceManager.class.getSimpleName();
    private ComponentName componentName;
    private DevicePolicyManager devicePolicyManager;


    private ActomaDeviceManager() {

    }

    public static ActomaDeviceManager getInstance() {
        return ActomaDeviceManagerHolder.INSTANCE;
    }

    private static class ActomaDeviceManagerHolder {
        private static final ActomaDeviceManager INSTANCE = new ActomaDeviceManager();
    }

    public void init(Context context) {
        this.componentName = new ComponentName(context, ActomaDeviceAdminReceiver.class);
        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    /**
     * 设备是否激活
     */
    public boolean adminActive() {
        if (devicePolicyManager == null) {
            return false;
        }
        return devicePolicyManager.isAdminActive(componentName);
    }

    public ComponentName getComponentName() {
        return componentName;
    }

    /**
     *    设置静默激活设备管理器(EMUI5.1)
     * <p>
     * <p>  请求设备管理静默激活。激活设备后，才能设置配置规则。
     * <p>  注意：需要申请 com.huawei.permission.sec.MDM_DEVICE_MANAGER 权限才
     * 能调用此接口。
     */
    public void setSilentActiveAdmin() {
        if (!adminActive()) {
            LogUtil.getUtils().d(TAG, "setSilentActiveAdmin -> isActiveAdmin is false");
            try {
                DeviceControlManager controlManager = new DeviceControlManager();
                controlManager.setSilentActiveAdmin(componentName);
                LogUtil.getUtils().d(TAG, "setSilentActiveAdmin -> 设备管理器已激活");
            } catch (SecurityException e) {
                e.printStackTrace();
                LogUtil.getUtils().d(TAG, "setSilentActiveAdmin -> com.huawei.permission.sec.MDM_DEVICE_MANAGER is miss !!!");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                LogUtil.getUtils().d(TAG, "setSilentActiveAdmin -> 参数admin为null时");
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.getUtils().d(TAG, "setSilentActiveAdmin -> error !!!");
            }
        } else {
            LogUtil.getUtils().d(TAG, "Device already active !!!");
        }
    }


    /**
     *    添加保持某应用始终运行名单（EMUI4.1）
     * <p>本接口对应开发指导书“保持某应用始终运行”功能；</p>
     * <b>注意：</b>
     * <p>
     * 1）当前 apk 设置 packageNames 累计数量总和不能超过 3，否则抛出IllegalArgumentException 异常；<br>
     * 2）packageNames 不能包含系统应用包名
     * </p>
     *
     * @param packageNames 待添加到名单中的应用包名列表
     */
    public void addPersistentApp(List<String> packageNames) {
        if (!adminActive()) {
            LogUtil.getUtils().d(TAG, "addPersistentApp -> isActiveAdmin is false");
            return;
        }

        try {
            DeviceApplicationManager applicationManager = new DeviceApplicationManager();
            applicationManager.addPersistentApp(componentName, packageNames);
        } catch (SecurityException e) {
            e.printStackTrace();
            LogUtil.getUtils().d(TAG, "addPersistentApp -> 设备未激活或或此apk不属于当前用户或者缺少com.huawei.permission.sec.MDM_APP_MANAGEMENT 权限");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LogUtil.getUtils().d(TAG, "addPersistentApp -> 当admin为null\n" +
                    "或packageNames为null或empty\n" +
                    "或packageNames包含无效包名\n" +
                    "或APK设置的packageNames累计包名数量超过 3 时");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.getUtils().d(TAG, "addPersistentApp -> error !!!");
        }
    }

    /**
     *     设置可信任应用列表(EMUI5.1)
     * <p>
     * 设置可信任应用列表。<br>
     *  1、 默认允许自启，用户不可修改；<br>
     *  2、默认允许关联启动，用户不可修改；<br>
     *  3、默认允许显示该应用的通知，用户不可修改；<br>
     *  4、禁止用户修改 APK 的联网权限（包括数据业务和 WLAN）；
     *  5、 APK 需要默认受保护，禁止用户取消；
     *  6、设置应用为信任应用，允许开启悬浮窗，所申请权限不需要用户确认，禁止用户取消/修改 APK 的相关权限
     *  7、接口不具备保活功能
     * </p>
     *  需要申请 com.huawei.permission.sec.MDM_APP_MANAGEMENT 权限才能调用此接口。<br>
     *
     * @param list 需要设置为可信任应用的应用列表。
     * @return true：配置成功；false：配置失败。
     */
    public boolean setSuperWhiteListForHwSystemManger(ArrayList<String> list) {
        if (!adminActive()) {
            LogUtil.getUtils().d(TAG, "setSuperWhiteListForHwSystemManger -> isActiveAdmin is false");
            return false;
        }
        try {
            DeviceHwSystemManager hwSystemManager = new DeviceHwSystemManager();
            return hwSystemManager.setSuperWhiteListForHwSystemManger(componentName, list);
        } catch (SecurityException e) {
            e.printStackTrace();
            LogUtil.getUtils().d(TAG, "setSuperWhiteListForHwSystemManger -> 此apk未经设备管理激活或此apk没有com.huawei.permission.sec.MDM_APP_MANAGEMENT 权限");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LogUtil.getUtils().d(TAG, "setSuperWhiteListForHwSystemManger ->参数admin为null时");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.getUtils().d(TAG, "setSuperWhiteListForHwSystemManger -> Exception !!!");
        }
        return false;
    }
}
