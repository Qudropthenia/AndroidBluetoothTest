package ru.qudropthenia.androidbluetoothtest.ui.theme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jaredrummler.android.colorpicker.ColorPanelView;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import ru.qudropthenia.androidbluetoothtest.R;
import ru.qudropthenia.androidbluetoothtest.engine.ApplicationData;
import ru.qudropthenia.androidbluetoothtest.engine.Theme;
import ru.qudropthenia.androidbluetoothtest.engine.ThemeList;
import ru.qudropthenia.androidbluetoothtest.ui.recycler.RecyclerActivity;

public class ChangeThemeActivity extends Activity implements ColorPickerView.OnColorChangedListener, View.OnClickListener {
    private ColorPickerView colorPickerView;
    private ColorPanelView newColorPanelView;
    private SeekBar seekBar;
    private int themeIndex;
    private Theme theme;
    private ThemeList themeListApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        themeListApp = ((ApplicationData) getApplication()).themeList;

        // Получение переданной темы по её индексу из List
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            themeIndex = (Integer) arguments.getSerializable(Integer.class.getSimpleName());
            theme = themeListApp.getThemeAtIndex(themeIndex);
        } else {
            theme = new Theme();
        }
        setContentView(R.layout.activity_change_theme);
        initColorPanel();
        initSeekBar();
        Button btnOK = findViewById(R.id.activity_change__okButton);
        Button btnCancel = findViewById(R.id.activity_change__cancelButton);
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initColorPanel() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        int initialColor = prefs.getInt("color_3", 0xFF000000);
        int initialColor = prefs.getInt("color_picker", theme.getColor());
        colorPickerView = (ColorPickerView) findViewById(R.id.activity_change__color_picker);
        newColorPanelView = (ColorPanelView) findViewById(R.id.activity_change__panel_new);
        colorPickerView.setOnColorChangedListener(this);
        colorPickerView.setColor(initialColor, true);
    }

    private void initSeekBar() {
        TextView seekBarTextView = findViewById(R.id.activity_change__bright);
        seekBar = findViewById(R.id.activity_change__seekBar);
        Integer brightness = theme.getBrightness();
        seekBar.setProgress(brightness);
        seekBarTextView.setText(getSeekBarText(brightness));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String seekBarText = getSeekBarText(progress);
                seekBarTextView.setText(seekBarText);
                theme.setBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private String getSeekBarText(Integer value) {
        return (int) Math.ceil(value * 0.39) + " %";
    }

    @Override
    public void onColorChanged(int newColor) {
        newColorPanelView.setColor(colorPickerView.getColor());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_change__okButton: {
                int newColor = colorPickerView.getColor();
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
                edit.putInt("color_3", newColor);
                edit.apply();
                try {
                    theme.setColor(newColor);
                } catch (Exception e) {
                    Toast.makeText(this, "Err", Toast.LENGTH_LONG).show();
                }
                themeListApp.setTheme(themeIndex, theme);
                Log.d("New color", theme.getColor() + "");
                showRecycler();
                finish();
                break;
            }
            case R.id.activity_change__cancelButton: {
                showRecycler();
                break;
            }
        }
    }

    private void showRecycler() {
        Intent intent = new Intent(this, RecyclerActivity.class);
        startActivity(intent);
    }
}
// Пример использования https://habr.com/ru/post/496136/