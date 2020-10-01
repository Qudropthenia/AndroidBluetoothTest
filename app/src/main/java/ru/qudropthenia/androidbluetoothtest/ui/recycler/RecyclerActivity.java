package ru.qudropthenia.androidbluetoothtest.ui.recycler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ru.qudropthenia.androidbluetoothtest.R;
import ru.qudropthenia.androidbluetoothtest.engine.Theme;
import ru.qudropthenia.androidbluetoothtest.engine.ThemeList;
import ru.qudropthenia.androidbluetoothtest.ui.theme.ChangeThemeActivity;

public class RecyclerActivity extends AppCompatActivity implements View.OnClickListener {
    private ThemeAdapter adapter;
    private ThemeList themeListApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        themeListApp = ((ThemeList) getApplication());
        setupRecyclerView();
        setupFloatingButton();
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
        Integer themeIndex = (Integer) view.getTag();

        switch (view.getId()) {
            case R.id.recycler_item__btn_edit: {
                Intent intent = new Intent(this, ChangeThemeActivity.class);
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
            case R.id.recycler_item__color:
            default: {
                break;
            }
        }
    }
}
