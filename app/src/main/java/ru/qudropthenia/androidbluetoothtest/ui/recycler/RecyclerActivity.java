package ru.qudropthenia.androidbluetoothtest.ui.recycler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.qudropthenia.androidbluetoothtest.R;
import ru.qudropthenia.androidbluetoothtest.ui.theme.ChangeThemeActivity;

public class RecyclerActivity extends Activity implements View.OnClickListener {
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
        ThemeAdapter adapter = new ThemeAdapter(themes, this::onThemeClick, this::onClick);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void onThemeClick(Theme theme, View view) {
        Integer color = theme.getColor();
        Toast.makeText(RecyclerActivity.this, color + "", Toast.LENGTH_SHORT).show();
    }

    private List<Theme> generateTheme() {
        List<Theme> themes = new ArrayList<>();

        themes.add(new Theme(27100, 1, 23));
        themes.add(new Theme(17871, 6, 42));
        themes.add(new Theme(-17871, 1, 1));
        themes.add(new Theme(37871, 56, 1));

        return themes;
    }

    @Override
    public void onClick(View view) {
        String msg = "";
        Theme theme = (Theme) view.getTag();
        Toast.makeText(this, "", Toast.LENGTH_LONG).show();

        switch (view.getId()) {
            case R.id.recycler_item__btn_edit: {
                msg = "recycler_item__btn_edit";
                Intent intent = new Intent(this, ChangeThemeActivity.class);
                intent.putExtra(Theme.class.getSimpleName(), theme);
                startActivity(intent);

                break;
            }
            case R.id.recycler_item__btn_delete: {
                msg = "recycler_item__btn_delete";
                break;
            }
            case R.id.recycler_item__color: {
                msg = "recycler_item__color";
                break;
            }
            default: {
                msg = "default";
                break;
            }
        }
//        Toast.makeText(RecyclerActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
