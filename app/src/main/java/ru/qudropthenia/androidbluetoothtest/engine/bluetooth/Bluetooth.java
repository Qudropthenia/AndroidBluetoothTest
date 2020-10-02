package ru.qudropthenia.androidbluetoothtest.engine.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Set;

public class Bluetooth extends Activity {
    private final static String HC_MAC_ADDRESS = "00:19:08:00:0D:8F";
    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;
    private final String LOG_TEG = "Bluetooth";

    public Bluetooth() {
        // Прежде чем соединяться с кем-нибудь и передавать данные
        // нужно убедиться, что ваш телефон имеет bluetooth модуль.
        // Первым делом при работе с bluetooth API нужно создать экземпляр класса BluetoothAdapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Телефон не поддерживает BL
        if (bluetoothAdapter == null) return;
        if (bluetoothAdapter.isEnabled()) {
            // Bluetooth включен. Работаем.
            Log.v(LOG_TEG, "Bluetooth ВКЛЮЧЕН");
        } else {
            // Bluetooth выключен. Предложим пользователю включить его.
            Log.v(LOG_TEG, "Bluetooth ВЫКЛЮЧЕН");
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        printNameAddress();
        getBluetoothDevice();
        asd();
    }

    public void printNameAddress() {
        String status;
        if (bluetoothAdapter.isEnabled()) {
            String myDeviceAddress = bluetoothAdapter.getAddress();
            String myDeviceName = bluetoothAdapter.getName();
            status = myDeviceName + " : " + myDeviceAddress;
        } else {
            status = "Bluetooth выключен";
        }
        Log.v(LOG_TEG, status);
//        Toast.makeText(this, status, Toast.LENGTH_LONG).show();
    }

    // Список спаренных устройств
    public void getBluetoothDevice() {
        Log.v(LOG_TEG, "Список спаренных устройств");
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        for (BluetoothDevice device : pairedDevices) {
            String info = device.getName() + " - " + device.getAddress();
            Log.v("BluetoothDevice", info);
        }
    }

    // Создаем BroadcastReceiver для ACTION_FOUND
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Когда найдено новое устройство
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Получаем объект BluetoothDevice из интента
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Добавляем имя и адрес в array adapter, чтобы показвать в ListView
                String info = device.getName() + " - " + device.getAddress();
                Log.v(LOG_TEG, info);
            }
        }
    };

    // Запрос на соединение со спаренным устройством
    public void asd() {
        // Регистрируем BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        try {
            // Не забудьте снять регистрацию в onDestroy
            registerReceiver(receiver, filter);
        } catch (Exception e) {
            Log.e(LOG_TEG, e.getMessage());
            Log.e(LOG_TEG, "receiver == null = " + (receiver == null));
            Log.e(LOG_TEG, "filter == null = " + (filter == null));
        }
    }
}
