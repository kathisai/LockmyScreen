package com.example.delllaptop.lockmyscreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{


    private Button lock;
    private Button disable;
    private Button enable;
    private Button status;
    private TextView writeStatus;

    static final int RESULT_ENABLE = 1;

    DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    ComponentName compName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        deviceManger = (DevicePolicyManager)getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager)getSystemService(
                Context.ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);

        setContentView(R.layout.activity_main);

        lock =(Button)findViewById(R.id.lock);
        lock.setOnClickListener(this);

        disable = (Button)findViewById(R.id.btnDisable);
        enable =(Button)findViewById(R.id.btnEnable);
        status =(Button)findViewById(R.id.ben_status);
        writeStatus = (TextView) findViewById(R.id.status);

        disable.setOnClickListener(this);
        enable.setOnClickListener(this);
        status.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == lock){
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.lockNow();
            }
        }

        if(v == enable){
            Intent intent = new Intent(DevicePolicyManager
                    .ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "Additional text explaining why this needs to be added.");
            startActivityForResult(intent, RESULT_ENABLE);
        }

        if(v == disable){
            deviceManger.removeActiveAdmin(compName);
            updateButtonStates();
        }
        if(v== status){
//            writeStatus.setText("Device lock ? :" + doesDeviceHaveSecuritySetup(MainActivity.this) + "\n"  +" is Device have pattern ? :" + isPatternSet(MainActivity.this)+ "\n" + "Is PIN/Password set ? :" + isPassOrPinSet(MainActivity.this) ) ;
            writeStatus.setText("Is PIN/Password set ? :" + isPassOrPinSet(MainActivity.this) ) ;
        }
    }


    private void updateButtonStates() {

        boolean active = deviceManger.isAdminActive(compName);
        if (active) {
            enable.setEnabled(false);
            disable.setEnabled(true);

        } else {
            enable.setEnabled(true);
            disable.setEnabled(false);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("DeviceAdminSample", "Admin enabled!");
                } else {
                    Log.i("DeviceAdminSample", "Admin enable FAILED!");
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


//    /**
//     * <p>Checks to see if the lock screen is set up with either a PIN / PASS / PATTERN</p>
//     *
//     * <p>For Api 16+</p>
//     *
//     * @return true if PIN, PASS or PATTERN set, false otherwise.
//     */
//    public static boolean doesDeviceHaveSecuritySetup(Context context)
//    {
//        return isPatternSet(context) || isPassOrPinSet(context);
//    }

    /**
     * @param context
     * @return true if pattern set, false if not (or if an issue when checking)
     */
//    private static boolean isPatternSet(Context context)
//    {
//        ContentResolver cr = context.getContentResolver();
//        try
//        {
//            int lockPatternEnable = Settings.Secure.getInt(cr, Settings.Secure.LOCK_PATTERN_ENABLED);
//            return lockPatternEnable == 1;
//        }
//        catch (Settings.SettingNotFoundException e)
//        {
//            Log.e("",e.getMessage());
//            return false;
//        }
//    }

    /**
     * @param context
     * @return true if pass or pin set
     */
    private static boolean isPassOrPinSet(Context context)
    {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE); //api 16+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return keyguardManager.isKeyguardSecure();
        }else {
            return keyguardManager.isKeyguardSecure();
        }
    }
}
