package ru.qudropthenia.androidbluetoothtest.engine;

import java.io.Serializable;

public class Theme implements Serializable {
    private Integer color;
    private Integer contrast;
    private Integer brightness;

    public Theme(Integer color, Integer contrast, Integer brightness) {
        this.color = color;
        this.contrast = contrast;
        this.brightness = brightness;
    }

    public Theme() {
        this(-1029169481, 0, 0);
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
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
