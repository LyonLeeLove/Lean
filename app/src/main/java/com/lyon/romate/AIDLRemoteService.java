package com.lyon.romate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lyon.lean.CustomAIDL;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/27/2018 6:31 PM                    <br>
 */
public class AIDLRemoteService extends Service {
    private final CustomAIDL.Stub aidl = new CustomAIDL.Stub() {
        @Override
        public String getStr() throws RemoteException {
            Log.e("remote","我是远程服务返回的 HELLO");
            return " 我是远程服务返回的 HELLO ";
        }
    } ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return aidl;
    }
}
