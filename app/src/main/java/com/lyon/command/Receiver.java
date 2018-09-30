package com.lyon.command;

import android.util.Log;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/5/2018 8:59 PM                    <br>
 */
public class Receiver {
    public static final String TAG = Receiver.class.getSimpleName();
    /**
     * QQ登录方法
     */
    public void tencentLogin() {
        System.out.print("QQ登录");
    }

    /**
     * 微信登录
     */
    public void weChatLogin() {
        System.out.print("weChat登录");
    }

    /**
     * 新浪登录
     */
    public void sinaLogin() {
        System.out.print("sina登录");
    }
}
