package ru.qudropthenia.androidbluetoothtest.ui.recycler;

public class Theme {
    private String color;
    private Integer contrast;
    private Integer brightness;

    public Theme(String color, Integer contrast, Integer brightness) {
        this.color = color;
        this.contrast = contrast;
        this.brightness = brightness;
    }

    public Theme() {
        this("red", 0, 0);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getContrast() {
        return contrast;
    }

    public void setContrast(Integer contrast) {
        this.contrast = contrast;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }
}
