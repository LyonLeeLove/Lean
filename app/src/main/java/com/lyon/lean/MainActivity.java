package com.lyon.lean;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lyon.romate.AIDLRemoteService;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 4/20/2018 6:30 PM                    <br>
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_bind_service, btn_test_method ;
    private CustomAIDL customAIDL ;

    private boolean isServerStarted ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aidl_activity);
        initView() ;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            customAIDL = CustomAIDL.Stub.asInterface(service) ;

            Log.e("service:","onServiceConnected") ;
            isServerStarted = true ;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            customAIDL = null ;
            Log.e("service:","onServiceDisconnected") ;
            isServerStarted = false ;

        }
    } ;

    private void initView() {
        btn_bind_service = this.findViewById(R.id.btn_bind_service) ;
        btn_test_method = this.findViewById(R.id.btn_test_method) ;

        btn_bind_service.setOnClickListener(this);
        btn_test_method.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bind_service:
                bindService(new Intent(MainActivity.this,AIDLRemoteService.class),serviceConnection, Context.BIND_AUTO_CREATE) ;
                break ;
            case R.id.btn_test_method:
                if(!isServerStarted){
                    Toast.makeText(MainActivity.this,"请先绑定服务先",Toast.LENGTH_SHORT).show();
                    return ;
                }
                try {
                    String str = customAIDL.getStr();
                    Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();

                }
                break ;
            default:
                break ;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
