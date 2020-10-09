package ru.qudropthenia.androidbluetoothtest.engine;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class ThemeList extends Application {
    private List<Theme> themes;

    public ThemeList() {
        themes = initThemes();
    }

    private List<Theme> initThemes() {
        List<Theme> themes = new ArrayList<>();

        themes.add(new Theme(-14784585));
        themes.add(new Theme(-5759305));
        themes.add(new Theme(-17871));
        themes.add(new Theme(-8678628));

        return themes;
    }

    private boolean checkIndex(int index) {
        return index >= 0 && index < this.themes.size();
    }

    public void addTheme() {
        themes.add(new Theme());
    }

    /**
     * Получение объекта Theme
     * @param index
     * @return
     */
    public Theme getThemeAtIndex(int index) {
        if (!checkIndex(index)) return null;
        return themes.get(index);
    }

    /**
     * Удаление Theme из колекции
     * @param index
     * @return
     */
    public boolean removeThemeAtIndex(int index) {
        if (!checkIndex(index)) return false;
        return (themes.remove(index) != null);
    }

    public boolean addTheme(Theme theme) {
        themes.add(theme);
        return true;
    }

    public boolean setTheme(int index, Theme theme) {
        if (!checkIndex(index)) return false;
        themes.set(index, theme);
        return true;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }
}
