package net.phipe.callrejectsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import net.phipe.callrejectsms.Model.AppDatabase;
import net.phipe.callrejectsms.Model.Contact;
import net.phipe.callrejectsms.Model.ContactDAO;
import net.phipe.callrejectsms.Service.CallBackgroundService;

public class MainActivity extends AppCompatActivity {

    EditText txtNumber, txtMessage;
    Button btnAdd;
    Switch btnSwitch;
    Boolean stateSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNumber = findViewById(R.id.txtPhone);
        txtMessage = findViewById(R.id.txtMessage);
        btnSwitch = findViewById(R.id.switchService);
        stateSwitch = isMyServiceRunning(CallBackgroundService.class);

        btnSwitch.setOnClickListener(view -> {
            stateSwitch = btnSwitch.isChecked();
            if(stateSwitch){
                Intent callService = new Intent(this, CallBackgroundService.class);
                try {
                    startService(callService);
                    Log.d(getPackageName(), "onClick: starting service");
                }catch (Exception ex){
                    Log.d(getPackageName(), ex.toString());
                }
            }else{
                stopService(new Intent(this, CallBackgroundService.class));
                Log.d(getPackageName(), "onClick: stoping service");
            }
        });

        findViewById(R.id.btnAdd).setOnClickListener(view -> {
            addContact();
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
    }

    private void addContact(){
        if(!TextUtils.isEmpty(txtMessage.getText()) && !TextUtils.isEmpty(txtMessage.getText())){
            AppDatabase db = AppDatabase.getDatabaseInstance(getApplication());
            ContactDAO dao = db.contactDAO();

            AppDatabase.databaseWriteExecutor.execute(() -> {
                Contact contact = new Contact();
                contact.id = dao.getLastID();
                contact.number = txtNumber.getText().toString();
                contact.message = txtMessage.getText().toString();

                Log.d("Insert", String.valueOf(dao.insert(contact)));

                clearText();
            });

            Toast.makeText(this, "Agregado correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearText(){
        txtNumber.setText("");
        txtMessage.setText("");
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}