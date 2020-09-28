package ru.qudropthenia.androidbluetoothtest.ui.recycler;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.qudropthenia.androidbluetoothtest.R;

public class RecyclerActivity extends AppCompatActivity {
    private List<Theme> themes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themes = generateTheme();
        setContentView(R.layout.activity_recycler);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.activity_recycler__recycler);
        ThemeAdapter adapter = new ThemeAdapter(themes);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private List<Theme> generateTheme() {
        List<Theme> themes = new ArrayList<>();

        themes.add(new Theme("a", 1, 23));
        themes.add(new Theme("b", 6, 42));
        themes.add(new Theme("c", 1, 1));
        themes.add(new Theme("a", 56, 1));
        themes.add(new Theme("g", 4, 44));
        themes.add(new Theme("as", 45, 1));
        themes.add(new Theme("Asd", 34, 1));
        themes.add(new Theme("tg", 76, 1));
        themes.add(new Theme("cb", 23, 1));
        themes.add(new Theme("tgr", 0, 44));

        return themes;
    }
}
