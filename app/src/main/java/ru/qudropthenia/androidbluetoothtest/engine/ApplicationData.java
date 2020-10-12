package ru.qudropthenia.androidbluetoothtest.engine;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

import ru.qudropthenia.androidbluetoothtest.engine.bluetooth.BluetoothApp;

public class ApplicationData extends Application {
    public BluetoothSocket bluetoothSocket;
    public BluetoothApp bluetooth;
    public ThemeList themeList;

    public ApplicationData() {
        bluetoothSocket = null;
        themeList = new ThemeList();
        bluetooth = new BluetoothApp();
    }
}
