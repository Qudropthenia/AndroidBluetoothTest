package ru.qudropthenia.androidbluetoothtest.engine.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Инициализация bl
 */
public class BluetoothApp {
    // Поток для установления соединения с удаленным bl
    private Thread blConnection;
    // Поток отправки данных
    private BluetoothRequestResponse blRR;
    public static Context context;
    private Activity activity;
    private BluetoothAdapter bluetoothAdapter;
    private final String LOG_TEG = "Bluetooth_BluetoothApp";
    private static final String REMOTE_MAC = "00:19:08:00:0D:8F";
    private static final int REQUEST_ENABLE_BT = 1;
    public static BluetoothSocket bluetoothSocket;
    public static UUID myUUID;

    public BluetoothApp() {
        bluetoothSocket = null;
    }

    public void startBlWork(Context context) {
        if (isConnectedBl()) return;

        this.context = context;
        activity = (Activity) context;
        boolean isInitBl = initBl();
        if (!isInitBl) {
            return;
        }
        turnOnBluetooth();
        pairedDevices();
    }

    private void restartBlWork() {
        try {
            blConnection.interrupt();
            bluetoothSocket.close();
            bluetoothSocket = null;
            startBlWork(context);
        } catch (IOException e) {
            Log.e(LOG_TEG, e.getMessage());
        }
    }

    public static boolean isConnectedBl() {
        return bluetoothSocket != null && bluetoothSocket.isConnected();
    }

    // Инициализируем наш Bl
    private boolean initBl() {
        final String UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB";

        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(context, "BLUETOOTH NOT support", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
//        String stInfo = bluetoothAdapter.getName() + " " + bluetoothAdapter.getAddress();
//        Log.v(LOG_TEG, "Это устройство: " + stInfo);

        return true;
    }

    // Запрос на включение Bluetooth
    // Добавить проверку выбора пользователя
    private void turnOnBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    private boolean pairedDevices() {
        // Создание списка сопряжённых Bluetooth-устройств
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() < 1) return false;

        // Ищем среди списка ранее сопряженных устройств наш bl модуль
        boolean finedBlModel = false;
        for (BluetoothDevice device : pairedDevices) {
            if (device.getAddress().equals(REMOTE_MAC)) {
                finedBlModel = true;
                break;
            }
        }
        if (!finedBlModel) {
            Toast.makeText(context, "Выполните сопряжение с BL модулем", Toast.LENGTH_LONG).show();
            return false;
        }
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(REMOTE_MAC);
        blConnection = new Thread(new BluetoothConnection(device, context));
        blConnection.start();

        return true;
    }

    // Проверяем наличие соединения, при необходимости переподключаемся
    private void checkConnect() {
        while (!isConnectedBl() && blConnection.isAlive()) {
            try {
                wait(1000L);
                Log.v(LOG_TEG, "Выполняется подключение");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!isConnectedBl()) {
            blConnection.start();
        } else blRR = new BluetoothRequestResponse();
        // Запускаем поток на прослушку полученных данных
        blRR.start();

        if (blRR == null) {
            Log.v(LOG_TEG, "blRR == null");
            return;
        }
    }

    public void sendData(String msg) {
        checkConnect();
        Log.v(LOG_TEG, "MSG = " + msg);
        byte[] bytesToSend = msg.getBytes();
        try {
            blRR.write(bytesToSend);
        } catch (IOException e) {
            Log.e(LOG_TEG, e.getMessage());
            Toast.makeText(context, "Произошёл разрыв соединения, выполняется переподключение", Toast.LENGTH_LONG).show();
            restartBlWork();
        }
    }
}
