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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jaredrummler.android.colorpicker.ColorPanelView;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import ru.qudropthenia.androidbluetoothtest.R;
import ru.qudropthenia.androidbluetoothtest.ui.recycler.RecyclerActivity;
import ru.qudropthenia.androidbluetoothtest.engine.Theme;
import ru.qudropthenia.androidbluetoothtest.engine.ThemeList;

public class ChangeThemeActivity extends Activity implements ColorPickerView.OnColorChangedListener, View.OnClickListener {
    private ColorPickerView colorPickerView;
    private ColorPanelView newColorPanelView;
    private int themeIndex;
    private Theme theme;
    private ThemeList themeListApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        themeListApp = ((ThemeList) getApplication());

        // Получение переданной темы
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            themeIndex = (Integer) arguments.getSerializable(Integer.class.getSimpleName());
            theme = themeListApp.getThemeAtIndex(themeIndex);
        } else {
            theme = new Theme();
        }

        setContentView(R.layout.activity_change_theme);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        int initialColor = prefs.getInt("color_3", 0xFF000000);
        int initialColor = prefs.getInt("color_picker", theme.getColor());

        colorPickerView = (ColorPickerView) findViewById(R.id.activity_change__color_picker);
        ColorPanelView colorPanelView = (ColorPanelView) findViewById(R.id.activity_change__panel_old);
        newColorPanelView = (ColorPanelView) findViewById(R.id.activity_change__panel_new);

        Button btnOK = (Button) findViewById(R.id.activity_change__okButton);
        Button btnCancel = (Button) findViewById(R.id.activity_change__cancelButton);

        ((LinearLayout) colorPanelView.getParent()).setPadding(colorPickerView.getPaddingLeft(), 0,
                colorPickerView.getPaddingRight(), 0);

        colorPickerView.setOnColorChangedListener(this);
        colorPickerView.setColor(initialColor, true);
        colorPanelView.setColor(initialColor);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
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