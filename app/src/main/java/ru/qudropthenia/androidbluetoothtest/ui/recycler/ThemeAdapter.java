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

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder> {
    private final List<Theme> themes;
    private final Listener onThemeClickListener;
    private final View.OnClickListener btnClick;

    public ThemeAdapter(List<Theme> themes, Listener onThemeClickListener, View.OnClickListener btnClick) {
        this.themes = themes;
        this.onThemeClickListener = onThemeClickListener;
        this.btnClick = btnClick;
    }

    // Создание
    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        view.setOnClickListener(v -> onThemeClickListener.onThemeClick((Theme) v.getTag(), v));
        return new ThemeViewHolder(view);
    }

    // Запись новых данных
    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder holder, final int position) {
        Theme color = themes.get(position);
        holder.bind(color);
        holder.itemView.setTag(color);
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
//            View viewById = itemView.findViewById(R.id.recycler_item__btn_edit);
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
