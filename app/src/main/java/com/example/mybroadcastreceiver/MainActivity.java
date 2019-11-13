package com.example.mybroadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiSetting();
        bluetoothSetting();

    }

    public void wifiSetting(){
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(receiver, intentFilter);

        final WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Switch wifiSwitch = findViewById(R.id.wifi);
        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                wifiManager.setWifiEnabled(isChecked);
            }
        });
    }

    public void bluetoothSetting(){
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, intentFilter);


        final Switch bluetoothSwitch = findViewById(R.id.bluetooth);
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (bluetoothAdapter == null) {
                    Toast.makeText(MainActivity.this, "Your device has no bluetooth.", Toast.LENGTH_LONG).show();
                    bluetoothSwitch.setChecked(false);
                } else {
                    if (isChecked)
                        bluetoothAdapter.enable();
                    else
                        bluetoothAdapter.disable();
                }
            }
        });
    }



    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                Switch wifiSwitch = findViewById(R.id.wifi);
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                if (WifiManager.WIFI_STATE_ENABLED == wifiState)
                    wifiSwitch.setChecked(true);
                else if (WifiManager.WIFI_STATE_DISABLED == wifiState)
                    wifiSwitch.setChecked(false);
            }
            else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                Switch bluetoothSwitch = findViewById(R.id.bluetooth);
                int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                if (BluetoothAdapter.STATE_ON == bluetoothState)
                    bluetoothSwitch.setChecked(true);
                else if (BluetoothAdapter.STATE_OFF == bluetoothState)
                    bluetoothSwitch.setChecked(false);
            }
        }
    }
}
