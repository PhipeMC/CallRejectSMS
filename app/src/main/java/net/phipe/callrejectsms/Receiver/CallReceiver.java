package net.phipe.callrejectsms.Receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import net.phipe.callrejectsms.Model.AppDatabase;
import net.phipe.callrejectsms.Model.Contact;
import net.phipe.callrejectsms.Model.ContactDAO;

public class CallReceiver extends BroadcastReceiver {

    private String status = "";
    private String incoming_number = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(this.getClass().toString(), "Broadcast recibido");

        String action = intent.getAction();

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state != null) {
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if (incomingNumber != null) {
                    incoming_number = incomingNumber;
                    Toast.makeText(context, incoming_number, Toast.LENGTH_SHORT).show();
                }
            }
        }

        //status = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if(!incoming_number.equals(-1) && incoming_number.length() > 0) {
            AppDatabase db = AppDatabase.getDatabaseInstance(context.getApplicationContext());
            ContactDAO dao = db.contactDAO();

            AppDatabase.databaseWriteExecutor.execute(() -> {
                Contact contact = dao.findByNumber(incoming_number);
                if (contact != null){
                    sendSMS(incoming_number, contact.message.toString(), context);
                }
            });

            incoming_number = "";
        }
    }

    private void sendSMS(String phoneNumber, String message, Context context) {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, "", message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al enviar el SMS",Toast.LENGTH_SHORT).show();
        }
    }
}
