package net.phipe.callrejectsms.Service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import net.phipe.callrejectsms.Receiver.CallReceiver;

public class CallBackgroundService extends Service {

    CallReceiver callReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        callReceiver = new CallReceiver();
        registerReceiver(callReceiver, intentFilter);

        Toast.makeText(getApplicationContext(), "Servicio Iniciado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(callReceiver);
        Toast.makeText(getApplicationContext(),"Servicio detenido", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
