package ru.qudropthenia.androidbluetoothtest.ui.recycler;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.qudropthenia.androidbluetoothtest.R;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorHolder> {
    private final List<ColorStyle> colorList;

    public ColorsAdapter(List<ColorStyle> colorList) {
        this.colorList = colorList;
    }

    // Создание
    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new ColorHolder(view);
    }

    // Запись новых данных
    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, final int position) {
        ColorStyle color = colorList.get(position);
        holder.bind(color);
        holder.itemView.setTag(color);
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    // Модель отрисовки
    static final class ColorHolder extends RecyclerView.ViewHolder {
        public final View colorView;
        public final TextView contrastValueView;
        public final TextView brightnessValueView;

        // View - что будет отрисовано в ячейке
        public ColorHolder(@NonNull View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.recycler_item__color);
            contrastValueView = itemView.findViewById(R.id.recycler_item__contrast_value);
            brightnessValueView = itemView.findViewById(R.id.recycler_item__brightness_value);
        }

        // Установка значений, которые будут отрисованы
        public void bind(ColorStyle style) {
            Drawable backgroundColor = new ColorDrawable(0xFFFF6666);
            String contrast = style.getContrast() + "";
            String bright = style.getBrightness() + "";
            colorView.setBackground(backgroundColor);
            contrastValueView.setText(contrast);
            brightnessValueView.setText(bright);
        }
    }
}
