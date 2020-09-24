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
    private List<ColorStyle> colorList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colorList = generateColors();
        setContentView(R.layout.activity_recycler);
        RecyclerView recyclerView = findViewById(R.id.activity_recycler__recycler);
        ColorsAdapter adapter = new ColorsAdapter(colorList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private List<ColorStyle> generateColors() {
        List<ColorStyle> colorList = new ArrayList<>();

        colorList.add(new ColorStyle("a", 1, 23));
        colorList.add(new ColorStyle("b", 6, 42));
        colorList.add(new ColorStyle("c", 1, 1));
        colorList.add(new ColorStyle("a", 56, 1));
        colorList.add(new ColorStyle("g", 4, 44));
        colorList.add(new ColorStyle("as", 45, 1));
        colorList.add(new ColorStyle("Asd", 34, 1));
        colorList.add(new ColorStyle("tg", 76, 1));
        colorList.add(new ColorStyle("cb", 23, 1));
        colorList.add(new ColorStyle("tgr", 0, 44));

        return colorList;
    }
}
