package ru.qudropthenia.androidbluetoothtest.ui.theme;

import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jaredrummler.android.colorpicker.ColorPanelView;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import ru.qudropthenia.androidbluetoothtest.R;

public class ChangeThemeActivity extends AppCompatActivity implements ColorPickerView.OnColorChangedListener, View.OnClickListener {
    private ColorPickerView colorPickerView;
    private ColorPanelView newColorPanelView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);

        setContentView(R.layout.activity_change_theme);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int initialColor = prefs.getInt("color_3", 0xFF000000);

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

    @Override public void onColorChanged(int newColor) {
        newColorPanelView.setColor(colorPickerView.getColor());
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_change__okButton:
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
                edit.putInt("color_3", colorPickerView.getColor());
                edit.apply();
//                finish();
                break;
            case R.id.activity_change__cancelButton:
//                finish();
                break;
        }
    }
}
// Пример использования https://habr.com/ru/post/496136/