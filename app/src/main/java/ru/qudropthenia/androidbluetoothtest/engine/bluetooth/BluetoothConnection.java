package ru.qudropthenia.androidbluetoothtest.engine.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Подключение к удаленному bl
 */
public class BluetoothConnection implements Runnable {
    private Context context;
    private BluetoothSocket bluetoothSocket;
    private final String LOG_TEG = "Bluetooth_BluetoothConnection";

    public BluetoothConnection(BluetoothDevice device, Context context) {
        this.context = context;
        try {
            if (!BluetoothApp.isConnectedBl()) {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(BluetoothApp.myUUID);
                BluetoothApp.bluetoothSocket = bluetoothSocket;
            }
//            Log.v(LOG_TEG, "bluetoothSocket == " + BluetoothApp.isConnectedBl());
        } catch (IOException e) {
            Log.e(LOG_TEG, e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
//            if (!BluetoothApp.isConnectedBl()) {
                bluetoothSocket.connect();
//            }
//            Log.v(LOG_TEG, "Connected");
//                bluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Нет коннекта, проверьте Bluetooth-устройство с которым хотите соединица!", Toast.LENGTH_LONG).show();
                }
            });
            try {
                Log.v(LOG_TEG, "bluetoothSocket.close()");
                bluetoothSocket.close();
                BluetoothApp.bluetoothSocket = bluetoothSocket;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        Log.v(LOG_TEG, "BluetoothApp.isConnectedBl() = " + BluetoothApp.isConnectedBl());
        if (BluetoothApp.isConnectedBl()) {
            BluetoothApp.bluetoothSocket = bluetoothSocket;
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Успешно", Toast.LENGTH_LONG).show();
//                        ButPanel.setVisibility(View.VISIBLE); // открываем панель с кнопками
                }
            });
        }
    }
}
