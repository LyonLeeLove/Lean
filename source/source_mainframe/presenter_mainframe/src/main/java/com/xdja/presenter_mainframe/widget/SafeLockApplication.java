package com.xdja.presenter_mainframe.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.xdja.comm.server.MiniConfigServer;
import com.xdja.comm.server.NotificationIntent;
import com.xdja.comm.server.SettingServer;
import com.xdja.comm.uitl.UniversalUtil;
import com.xdja.comm.uitl.handler.SafeLockUtil;
import com.xdja.comm.widget.FloatingWindowManager;
import com.xdja.contact.util.ContactUtils;
import com.xdja.data_mainframe.huawei.ActomaDeviceManager;
import com.xdja.dependence.uitls.LogUtil;
import com.xdja.frame.ActomaController;
import com.xdja.frame.AndroidApplication;
import com.xdja.frame.presenter.ActivityStack;
import com.xdja.presenter_mainframe.ActomaApplication;
import com.xdja.presenter_mainframe.presenter.activity.setting.OpenGesturePresenter;
import com.xdja.presenter_mainframe.receiver.Application2FrontReceiver;
import com.xdja.presenter_mainframe.receiver.ApplicationExitReceiver;
import com.xdja.presenter_mainframe.receiver.CkmsReceiver;
import com.xdja.presenter_mainframe.receiver.ReportReceiver;
import com.xdja.presenter_mainframe.receiver.SilentLoginReceiver;
import com.xdja.presenter_mainframe.receiver.UpdateReceiver;
import com.xdja.presenter_mainframe.receiver.ViewCallerReceiver;
import com.xdja.presenter_mainframe.util.LockPatternUtils;
import com.xdja.securevoip.manager.VOIPManager;
import com.xdja.securevoip.receiver.ClearReceiver;
import com.xdja.securevoip.utils.ScreenUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by licong on 2016/11/30.
 */
@SuppressLint("Registered")
public class SafeLockApplication extends AndroidApplication {

    public static String SCREEN_BROADCAST = "com.xdja.actoma_screenBroadCast";//灭屏广播到达
    private String isSetPattern = false + "";//是否设置开启安全锁 设置安全锁有三个状态 true 打开 false 关闭  -1 未设置 -2 忘记密码
    private boolean useBackGround = false;//是否设置后台锁定安通+
    private String isPauseActivity = "";//进入Pause状态的activity
    private ScreenBroadcastReceiver mScreenReceiver;//锁屏监听
    private ViewCallerReceiver viewCallerReceiver;
    private ApplicationExitReceiver exitReceiver;
    private UpdateReceiver updateReceiver;
    private ReportReceiver reportReceiver;
    private SilentLoginReceiver silentLoginReceiver;
    private Application2FrontReceiver application2FrontReceiver;
    private CkmsReceiver ckmsReceiver;
    private ClearReceiver clearReceiver;
    private boolean isInCallPresenterLaunched = false;
    private HandlerThread handlerThread;
    private Handler childHandler;

    private Handler mainHandler;

    private final int SHOW_FLOAT_WINDOW = 1;
    private final int HIDE_FLOAT_WINDOW = 2;
    @Override
    public void onCreate() {
        super.onCreate();

        mScreenReceiver = new ScreenBroadcastReceiver();
        viewCallerReceiver = new ViewCallerReceiver();
        exitReceiver = new ApplicationExitReceiver();
        updateReceiver = new UpdateReceiver();
        reportReceiver = new ReportReceiver();
        silentLoginReceiver = new SilentLoginReceiver();
        application2FrontReceiver = new Application2FrontReceiver();
        ckmsReceiver = new CkmsReceiver();
        clearReceiver = new ClearReceiver();


        registerScreenBroadcastReceiver();
        registerViewCallerReceiver();
        registerApplicationExitReceiver();
        registerUpdateReceiver();
        registerReportReceiver();
        registerSilentLoginReceiver();
        registerApplication2FrontReceiver();
        registerCkmsReceiver();
        registerClearReceiver();

        deviceManager();

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                String currentClass = activity.getLocalClassName();
                boolean inCallResume = currentClass.contains("com.xdja.securevoip.presenter.activity.InCallPresenter");
                boolean floatPreResume = currentClass.contains("com.xdja.contact.presenter.activity.FloatMatchInfoPresenter");
                if(MiniConfigServer.getInstance().floatContact() && !floatPreResume && !inCallResume){
                    processCheckRunAppMsg(true);
                }else{
                    LogUtil.getUtils().d("SafeLockApplication", "onActivityResumed currentClass "+currentClass);
                }
                if (inCallResume) {
                    FloatingWindowManager.getInstance().hideContactFloatingWindowView();
                    ActomaController.setCallShowing(true);
                }else{
                    ActomaController.setCallShowing(false);
                }
                removeCheckRunMsg();
                if (!activity.getLocalClassName().contains("com.xdja.presenter_mainframe.presenter.activity.setting.OpenGesturePresenter")) {
                    //修改在有系统锁的前提下，应用锁打开通话界面无法弹出的问题。
                    if(ScreenUtil.isScreenLock(SafeLockApplication.this)) {
                        //解决安卓5.0与安卓6.0的差异
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            return;
                        }
                        if(activity.getLocalClassName().contains("com.xdja.securevoip.presenter.activity.InCallPresenter")){
                            isInCallPresenterLaunched = true;
                        }else{
                            if(!isInCallPresenterLaunched){
                                return;
                            }else{
                                isInCallPresenterLaunched = false;
                            }
                        }
                    }
                    startLock(activity);
                    return;
                }
            }

            private void startLock(Activity activity) {
                //获取是否设置开启安全锁
                isSetPattern = SettingServer.getSafeLock();
                if (isSetPattern.equals("true")) {

                    //获取是否设置了手机锁屏后锁定安通+
//                    useScreenLock = SettingServer.getLockScreen();
                    //获取是否设置了后台运行时锁定安通+
                    useBackGround = SettingServer.getLockBackground();

                    //[S]fix bug 7717 by licong, for safeLock
                    if (((ActomaApplication)activity.getApplication()).getUserComponent() == null) {
                        return;
                    }
                    //[E]fix bug 7717 by licong, for safeLock

                    //[S] fix bug 最近任务栏切换 7972  by licong
                    if(activity.getLocalClassName().equals("com.xdja.imp.presenter.activity.ChooseIMSessionActivity")){
                        OpenGesturePresenter.isTransmit = true;
                    }
                    //[E] fix bug 最近任务栏切换 7972  by licong


                    if (activity.getLocalClassName().contains("com.xdja.presenter_mainframe.presenter.activity.login.LoginPresenter")
                            || activity.getLocalClassName().contains("com.xdja.presenter_mainframe.presenter.activity.login.ProductIntroductionAnimPresenter")
                            || activity.getLocalClassName().contains("com.xdja.presenter_mainframe.presenter.activity.login.LauncherPresenter")
                            || activity.getLocalClassName().contains("com.xdja.presenter_mainframe.presenter.activity.SplashPresenter")
                            || activity.getLocalClassName().contains("com.xdja.presenter_mainframe.presenter.activity.LauncherPresenter")) {
                        return;
                    }

                    //[S]xujinxi@xdja.com 2017-06-17 modify. fix bug 13593. review by self
                    if (activity.getLocalClassName().contains("com.xdja.securevoip.presenter.activity.InCallPresenter")
                            || activity.getLocalClassName().contains("com.xdja.securevoip.presenter.activity.InConferencePresenter")) {
                        return;
                    }
                    //[E]xujinxi@xdja.com 2017-06-17 modify. fix bug 13593. review by self

                   //[S] fix bug 7706 by licong for safeLock
                    LogUtil.getUtils().e("gbc", "startLock  isScreenLockerState  = " +ActomaApplication.isScreenLockerState());
                    if (ActomaApplication.isScreenLockerState()) {
                        if (SafeLockUtil.isUseCameraOrFile()) {
                            SafeLockUtil.setUseCameraOrFile(false);
                            synchronized (ActomaApplication.getObjLocker()) {
                                ActomaApplication.setScreenLockerState(false);
                            }
                        } else {
                            LockPatternUtils.startConfirmPattern(activity);
                            SafeLockUtil.setUseCameraOrFile(false);
                        }
                        return;
                    } else {
                        //maybe start lockactivity with broadcast
                        if (OpenGesturePresenter.isHaveLockActivity()) {
                            LockPatternUtils.startConfirmPattern(activity);
                        }
                    }
                    //[E] fix bug 7706 by licong for safeLock
                    if (activity.getLocalClassName().contains("com.xdja.presenter_mainframe.presenter.activity.MainFramePresenter")
                            && isPauseActivity.contains("com.xdja.presenter_mainframe.presenter.activity.SplashPresenter")) {
                        synchronized (ActomaApplication.getObjLocker()) {
                            ActomaApplication.setScreenLockerState(true);
                        }
                        LockPatternUtils.startConfirmPattern(activity);
                        return;
                    }
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                String currentClass = activity.getLocalClassName();
                boolean inCallPause = currentClass.contains("com.xdja.securevoip.presenter.activity.InCallPresenter");
                boolean floatPrePause = currentClass.contains("com.xdja.contact.presenter.activity.FloatMatchInfoPresenter");
                if(FloatingWindowManager.getInstance().isContactFloatShown() || inCallPause || floatPrePause) {
                    LogUtil.getUtils().d("SafeLockApplication", "onActivityPaused send message-----------");
                    sendCheckRunMsg();
                }
                isPauseActivity = activity.getLocalClassName();
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (SettingServer.getSafeLock().equals("true")) {
                    //后台锁定的时候，将变量设置成true
                    if (!isProcessRnning(activity) && SettingServer.getLockBackground()) {
                        synchronized (ActomaApplication.getObjLocker()) {
                            ActomaApplication.setScreenLockerState(true);
                            LogUtil.getUtils().d("gbc", "onActivityStopped" + SettingServer.getLockBackground());
                        }
                    }
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                //[S] fix bug 最近任务栏切换 7972 8719  by licong
                String currentClass = activity.getLocalClassName();
                if(currentClass.equals("com.xdja.imp.presenter.activity.ChooseIMSessionActivity")){
                    OpenGesturePresenter.isTransmit = false;
                    SafeLockUtil.setIsForwardMessage(false);
                }
                //[E] fix bug 最近任务栏切换 7972 8719
                else if(currentClass.contains("com.xdja.securevoip.presenter.activity.InCallPresenter")){
                    boolean floatOpen = UniversalUtil.getContactFloatwindow(SafeLockApplication.this,true);
                    boolean voipActive = VOIPManager.getInstance().hasActiveCall();
                    boolean floatPermission = UniversalUtil.hasFloatwindowPermission(SafeLockApplication.this);
                    if(floatOpen && !voipActive && floatPermission) {
                        FloatingWindowManager.getInstance().showContactFloatingWindowView();
                    }else{
                        LogUtil.getUtils().i("SafeLockApplication","onActivityDestroyed FloatingWindowManager floatOpen "
                                +floatOpen+" voipActive "+voipActive+" floatPermission "+floatPermission);
                    }
                }
            }
        });

        if(MiniConfigServer.getInstance().floatContact()) {
            handlerThread = new HandlerThread("CheckCurrentRunApp");
            handlerThread.start();
            childHandler = new Handler(handlerThread.getLooper(), new CheckCurrentRunAppCallback());
            mainHandler = new Handler(getMainLooper(), new FloatingWindowManagerCallback());
        }
    }

    public String getEMUIVersion() {
        String result = "";
        String key = "ro.build.version.emui";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method m = c.getMethod("get", String.class, String.class);
            result = (String) (m.invoke(c, key, ""));
            LogUtil.getUtils().d("glen", "EMUI_VERSION = " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getIntegerTypeEmui() {
        String emuiVersion = getEMUIVersion();
        if(!TextUtils.isEmpty(emuiVersion)){
            emuiVersion = emuiVersion.replace("EmotionUI_","");
            emuiVersion = emuiVersion.replace(".","");
            if(emuiVersion.length() == 2){
                emuiVersion = emuiVersion + "0";
            }
        }

        int emui = 0;
        try {
            emui = Integer.valueOf(emuiVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.getUtils().d("glen", "emui = " + emui);
        return emui;
    }

    private static final int EMUI_VERSION_9 = 900;
    private void deviceManager() {
        if (!Build.MANUFACTURER.equalsIgnoreCase("HUAWEI")) {
            LogUtil.getUtils().d("SafeLockApplication", "current device is " + Build.MANUFACTURER);
            return;
        }

        int emuiV = getIntegerTypeEmui();
        if (emuiV >= EMUI_VERSION_9) {
            ArrayList<String> packages = new ArrayList<>();
            packages.add("com.xdja.safekeyservice");
            packages.add("com.xdja.actoma");
            packages.add("com.xdja.HDSafeEMailClient");

            ActomaDeviceManager.getInstance().init(SafeLockApplication.this);
            ActomaDeviceManager.getInstance().setSilentActiveAdmin();
            ActomaDeviceManager.getInstance().addPersistentApp(packages);
            ActomaDeviceManager.getInstance().setSuperWhiteListForHwSystemManger(packages);
        } else {
            LogUtil.getUtils().d("SafeLockApplication", "current EMUI version is " + emuiV);
        }

    }


    private final int CHECK_RUN_APP_MSG = 1;
    private final long CHECK_RUN_APP_DELAY = 2000;
    public void sendCheckRunMsg(){
        if(UniversalUtil.getContactFloatwindow(this,false)) {
            childHandler.sendEmptyMessageDelayed(CHECK_RUN_APP_MSG, CHECK_RUN_APP_DELAY);
        }
    }

    public void sendCheckRunMsgNoDelay(){
        if(UniversalUtil.getContactFloatwindow(this,false)) {
            childHandler.sendEmptyMessage(CHECK_RUN_APP_MSG);
        }
    }

    public void removeCheckRunMsg(){
        childHandler.removeMessages(CHECK_RUN_APP_MSG);
    }

    /**
     * 判断安通+进程是否在前台运行
     *
     * @param context 上下文句柄
     * @return 是否在前台运行
     */
    public static boolean isProcessRnning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
            String processName = processInfo.processName;
            if (TextUtils.isEmpty(processName)) {
                return false;
            }
            //[S] fix bug by licong for 6.0 版本锁屏灭屏的判断
            if (processName.equals(context.getPackageName())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ((processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                            ||  (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_TOP_SLEEPING)) {
                        return true;
                    }
                } else {
                    if ((processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)) {
                        return true;
                    }
                }
            }
            //[E] fix bug by licong for 6.0 版本锁屏灭屏的判断
        }
        return false;
    }

    private void registerScreenBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.setPriority(1000);
        registerReceiver(mScreenReceiver, filter);
    }

    private void registerViewCallerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotificationIntent.notificationAction);
        registerReceiver(viewCallerReceiver, intentFilter);
    }

    private void registerApplicationExitReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xdja.application.exit");
        registerReceiver(exitReceiver, intentFilter);
    }

    private void registerUpdateReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xdja.actoma.update");
        registerReceiver(updateReceiver, intentFilter);
    }

    private void registerReportReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xdja.actoma.report");
        registerReceiver(reportReceiver, intentFilter);
    }

    private void registerSilentLoginReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xdja.action.silentlogin");
        registerReceiver(silentLoginReceiver, intentFilter);
    }

    private void registerApplication2FrontReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xdja.application2front");
        registerReceiver(application2FrontReceiver, intentFilter);
    }

    private void registerCkmsReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xdja.actoma.ckms.handler");
        registerReceiver(ckmsReceiver, intentFilter);
    }

    private void registerClearReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xdja.voip.cancel_missed_call_notification");
        registerReceiver(clearReceiver, intentFilter);
    }

    /**
     * 停止screen状态更新
     */
    public void unRegisterScreenStateUpdate() {
        unregisterReceiver(mScreenReceiver);
    }

    public void unRegisterViewCallerReceiver() {
        if (viewCallerReceiver != null) {
            unregisterReceiver(viewCallerReceiver);
        }
    }


    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                removeCheckRunMsg();
            }else if(Intent.ACTION_SCREEN_ON.equals(action)){
                sendCheckRunMsg();
            }
            if (Intent.ACTION_USER_PRESENT.equals(action) ||
                    Intent.ACTION_SCREEN_OFF.equals(action) /*||
                    Intent.ACTION_SCREEN_ON.equals(action)*/) {
                //当前界面不是通话界面，收到灭屏广播，执行业务
                //fix bug by licong for 12606
                if (SettingServer.getSafeLock().equals("true") && SettingServer.getLockScreen()
                        && !OpenGesturePresenter.isHaveLockActivity()) {

                    synchronized (ActomaApplication.getObjLocker()) {
                        synchronized (ActomaApplication.getObjLocker()) {
                            ActomaApplication.setScreenLockerState(true);
                        }
                        if (isProcessRnning(context)) {
                            //[S]xujinxi@xdja.com 2017-06-17 modify. fix bug 13593. review by self
                            if(!ActivityStack.getInstanse().getTopActivity().getLocalClassName().equals("com.xdja.securevoip.presenter.activity.InCallPresenter")
                                    && !ActivityStack.getInstanse().getTopActivity().getLocalClassName().equals("com.xdja.imp.presenter.activity.VideoRecorderPresenter")){
                                sendBroadcast(new Intent(SCREEN_BROADCAST));
                            }
                            //[E]xujinxi@xdja.com 2017-06-17 modify. fix bug 13593. review by self
                        }
                    }
                }
            }
        }
    }

    class CheckCurrentRunAppCallback implements Handler.Callback{
        @Override
        public boolean handleMessage(Message message) {
            if(message.what == CHECK_RUN_APP_MSG) {
                String runPkgName = processCheckRunAppMsg(false);
                if(!UniversalUtil.NONE_USAGE_PERMISSION.equals(runPkgName)) {
                    sendCheckRunMsg();
                }
            }
            return false;
        }
    }

    public String processCheckRunAppMsg(boolean forceSetIfEmpty){
        if(!MiniConfigServer.getInstance().floatContact()){
            return "";
        }
        ActomaController.setHomePackageName(ContactUtils.getHomePackageName(SafeLockApplication.this));
        String runPkgName = UniversalUtil.getRunningProcessName(SafeLockApplication.this,forceSetIfEmpty);
        if (!TextUtils.isEmpty(runPkgName) && !android.text.TextUtils.equals(runPkgName,"android")) {
            ActomaController.setCurrentAppPackageName(runPkgName);
        }
        boolean voipActive = VOIPManager.getInstance().hasActiveCall();
        boolean showFloatPresenter = ActomaController.isShowFloatPresenter();
        if (FloatingWindowManager.getInstance().canShowContactFloatIgnorVoip() && !voipActive && !showFloatPresenter) {
            mainHandler.removeMessages(HIDE_FLOAT_WINDOW);
            mainHandler.sendEmptyMessage(SHOW_FLOAT_WINDOW);
        } else {
            mainHandler.removeMessages(SHOW_FLOAT_WINDOW);
            mainHandler.sendEmptyMessage(HIDE_FLOAT_WINDOW);
        }
        return runPkgName;
    }


    class FloatingWindowManagerCallback implements Handler.Callback{
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case SHOW_FLOAT_WINDOW:
                    FloatingWindowManager.getInstance().showContactFloatingWindowView();
                    break;
                case HIDE_FLOAT_WINDOW:
                    FloatingWindowManager.getInstance().hideContactFloatingWindowView();
                    break;
            }
            return false;
        }
    }

    public View.OnClickListener contactFloatClickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = SafeLockApplication.this;
            Intent startIntent = ContactUtils.getContactFloatActionIntent(context);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(),startIntent,0);
            try{
                pendingIntent.send();
            }catch (PendingIntent.CanceledException e){
                LogUtil.getUtils().e("ActomaApplication","contactFloatClickLis exception "+e);
            }
        }
    };
}
