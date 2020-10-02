package ru.qudropthenia.androidbluetoothtest.ui.recycler;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.qudropthenia.androidbluetoothtest.R;
import ru.qudropthenia.androidbluetoothtest.engine.Theme;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder> {
    private final List<Theme> themes;
    private final Listener onThemeClickListener;

    public ThemeAdapter(List<Theme> themes, Listener onThemeClickListener) {
        this.themes = themes;
        this.onThemeClickListener = onThemeClickListener;
    }

    // Создание
    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
//        view.setOnClickListener(v -> onThemeClickListener.onThemeClick((Theme) v.getTag(), v));
//        view.setOnClickListener(v -> onThemeClickListener.onThemeClick((Theme) v.getTag(), v));
        return new ThemeViewHolder(view);
    }

    // Запись новых данных
    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder holder, final int position) {
        Theme theme = themes.get(position);
        holder.bind(theme);
        holder.itemView.setTag(position);
        // Устанавливаем привязку к Theme
        holder.itemView.findViewById(R.id.recycler_item__btn_edit).setTag(position);
        holder.itemView.findViewById(R.id.recycler_item__btn_delete).setTag(position);
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    // Модель отрисовки
    static final class ThemeViewHolder extends RecyclerView.ViewHolder {
        public final View colorView;

        // View - что будет отрисовано в ячейке
        public ThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.recycler_item__color);
        }

        // Установка значений, которые будут отрисованы
        public void bind(Theme theme) {
//            Drawable backgroundColor = new ColorDrawable(0xFFFF6666);
            Drawable backgroundColor = new ColorDrawable(theme.getColor());
            colorView.setBackground(backgroundColor);
        }
    }

    interface Listener {
        void onThemeClick(Theme theme, View view);
    }
}
