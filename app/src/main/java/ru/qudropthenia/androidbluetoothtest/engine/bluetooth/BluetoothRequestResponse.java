package ru.qudropthenia.androidbluetoothtest.engine.bluetooth;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Обмен данными по bl
 */
public class BluetoothRequestResponse extends Thread {
    private final InputStream connectedInputStream;
    private final OutputStream connectedOutputStream;
    private String sbprint;
    private final String LOG_TEG = "Bluetooth_BluetoothRR";

    public BluetoothRequestResponse() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = BluetoothApp.bluetoothSocket.getInputStream();
            out = BluetoothApp.bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectedInputStream = in;
        connectedOutputStream = out;
    }

    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        // Приём данных
        while (true) {
            try {
                byte[] buffer = new byte[100];
                int bytes = connectedInputStream.read(buffer);
                String strIncom = new String(buffer, 0, bytes);
                // собираем символы в строку
                sb.append(strIncom);
                // определяем конец строки
                int endOfLineIndex = sb.indexOf("\r\n");

                if (endOfLineIndex < 1) continue;
                sbprint = sb.substring(0, endOfLineIndex);
                sb.delete(0, sb.length());
                // Вывод полученных данных
                ((Activity) BluetoothApp.context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(LOG_TEG, "Get text: " + sbprint);
                    }
                });
            } catch (IOException e) {
                break;
            }
        }
    }

    public void write(byte[] buffer) throws IOException {
        try {
            connectedOutputStream.write(buffer);
        } catch (IOException e) {
            Log.e(LOG_TEG, e.getMessage() + " -> write");
        }
    }
}
