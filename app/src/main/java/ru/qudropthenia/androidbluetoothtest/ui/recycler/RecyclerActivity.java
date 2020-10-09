package ru.qudropthenia.androidbluetoothtest.ui.recycler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ru.qudropthenia.androidbluetoothtest.R;
import ru.qudropthenia.androidbluetoothtest.engine.Theme;
import ru.qudropthenia.androidbluetoothtest.engine.ThemeList;
import ru.qudropthenia.androidbluetoothtest.ui.theme.ChangeThemeActivity;

public class RecyclerActivity extends AppCompatActivity implements View.OnClickListener {
    // BL
    private static final int REQUEST_ENABLE_BT = 1;
    private static final String HC_MAC = "00:19:08:00:0D:8F";
    BluetoothAdapter bluetoothAdapter;
    private UUID myUUID;
    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;
    private StringBuilder sb = new StringBuilder();

    // App
    private ThemeAdapter adapter;
    private ThemeList themeListApp;
    private final String LOG_TEG = "Bluetooth_app";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        themeListApp = ((ThemeList) getApplication());
        setupRecyclerView();
        setupFloatingButton();
        blOnCreate();
    }

    // Инициализация Recycler
    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.activity_recycler__recycler);
        List<Theme> themes = themeListApp.getThemes();
        adapter = new ThemeAdapter(themes, this::onThemeClick);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    // Обработка нажатия по item
    private void onThemeClick(Theme theme, View view) {
        Integer color = theme.getColor();
        Toast.makeText(RecyclerActivity.this, color + "", Toast.LENGTH_SHORT).show();
    }

    // Инициализация кнопко добавления
    private void setupFloatingButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.activity_recycler__fb_add);
        floatingActionButton.setOnClickListener(v -> onAddClick());
    }

    // Добавление нового item`а
    private void onAddClick() {
        themeListApp.addTheme();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        final Integer themeIndex = (Integer) view.getTag();

        switch (view.getId()) {
            case R.id.recycler_item__btn_edit: {
                final Intent intent = new Intent(this, ChangeThemeActivity.class);
                intent.putExtra(Integer.class.getSimpleName(), themeIndex);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.recycler_item__btn_delete: {
                themeListApp.removeThemeAtIndex(themeIndex);
                adapter.notifyDataSetChanged();
                break;
            }
            case R.id.recycler_item__color: {
                final Theme theme = themeListApp.getThemeAtIndex(themeIndex);
                String strTheme = theme.getRGB().strTheme();
                Log.v(LOG_TEG, "Отправили: " + strTheme);
                byte[] bytesToSend = strTheme.getBytes();
                myThreadConnected.write(bytesToSend);
                break;
            }
            default: {
                break;
            }
        }
    }

    // Инициализация BL
    private void blOnCreate() {
        final String UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB";

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "BLUETOOTH NOT support", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        String stInfo = bluetoothAdapter.getName() + " " + bluetoothAdapter.getAddress();
        Log.v(LOG_TEG, "Это устройство: " + stInfo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Запрос на включение Bluetooth
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        setup();
    }

    private void setup() {
        // Создание списка сопряжённых Bluetooth-устройств
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // Если есть сопряжённые устройства
        if (pairedDevices.size() < 1) return;

        // Ищем среди списка ранее сопряженных устройств наш bl модуль
        boolean finedBlModel = false;
        for (BluetoothDevice device : pairedDevices) {
            if (device.getAddress().equals(HC_MAC)) {
                finedBlModel = true;
                break;
            }
        }
        if (!finedBlModel) {
            Toast.makeText(this, "Выполните сопряжение с BL модулем", Toast.LENGTH_LONG).show();
            return;
        }
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(HC_MAC);
        myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
        // Запускаем поток для подключения Bluetooth
        myThreadConnectBTdevice.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myThreadConnectBTdevice != null) myThreadConnectBTdevice.cancel();
    }

    // Поток для коннекта с Bluetooth
    private class ThreadConnectBTdevice extends Thread {
        private BluetoothSocket bluetoothSocket = null;

        private ThreadConnectBTdevice(BluetoothDevice device) {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() { // Коннект
            try {
                bluetoothSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RecyclerActivity.this, "Нет коннекта, проверьте Bluetooth-устройство с которым хотите соединица!", Toast.LENGTH_LONG).show();
                    }
                });
                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            // Если законнектились, тогда открываем панель с кнопками и запускаем поток приёма и отправки данных
            Log.v(LOG_TEG, "bluetoothSocket.isConnected() = " + bluetoothSocket.isConnected());
            if (bluetoothSocket.isConnected()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        ButPanel.setVisibility(View.VISIBLE); // открываем панель с кнопками
                    }
                });
                myThreadConnected = new ThreadConnected(bluetoothSocket);
                myThreadConnected.start(); // запуск потока приёма и отправки данных
            }
        }

        public void cancel() {
            Toast.makeText(getApplicationContext(), "Close - BluetoothSocket", Toast.LENGTH_LONG).show();
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // END ThreadConnectBTdevice:

    // Поток - приём и отправка данных
    private class ThreadConnected extends Thread {
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;
        private String sbprint;

        public ThreadConnected(BluetoothSocket socket) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() {
            // Приём данных
            while (true) {
                try {
                    byte[] buffer = new byte[100];
                    int bytes = connectedInputStream.read(buffer);
                    String strIncom = new String(buffer, 0, bytes);
                    sb.append(strIncom); // собираем символы в строку
                    int endOfLineIndex = sb.indexOf("\r\n"); // определяем конец строки

                    if (endOfLineIndex < 1) continue;
                    sbprint = sb.substring(0, endOfLineIndex);
//                    Log.v(LOG_TEG, "Get text: " + sbprint);
                    sb.delete(0, sb.length());
                    runOnUiThread(new Runnable() { // Вывод данных
                        @Override
                        public void run() {
                            Log.v(LOG_TEG, "Get text: " + sbprint);
//                            switch (sbprint) {
//                                case "test":
//                                    Log.v(LOG_TEG, "Get text: " + sbprint);
//                                    break;
//                                default:
//                                    break;
//                            }
                        }
                    });
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                Log.e(LOG_TEG, e.getMessage());
            }
        }
    }
}
