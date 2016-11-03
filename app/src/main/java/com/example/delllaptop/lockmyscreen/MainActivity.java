package com.example.delllaptop.lockmyscreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{


    private Button lock;
    private Button disable;
    private Button enable;
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
        disable.setOnClickListener(this);
        enable.setOnClickListener(this);
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
}
