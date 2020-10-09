package ru.qudropthenia.androidbluetoothtest.engine;

public class Theme {
    private Integer color;
    private Integer brightness;
    private static final Integer DEFAULT_BRIGHTNESS = 100;

    public Theme(Integer color, Integer brightness) {
        this.color = color;
        this.brightness = brightness;
    }

    public Theme(Integer color) {
        this(color, DEFAULT_BRIGHTNESS);
    }

    public Theme() {
        this(-13558637, DEFAULT_BRIGHTNESS);
    }

    public RGB getRGB() {
        return new RGB(color);
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    public class RGB {
        public Integer R;
        public Integer G;
        public Integer B;

        public RGB(Integer number) {
            int color = intToHex(number);
            R = (color >> 16) & 0xFF;
            G = (color >> 8) & 0xFF;
            B = (color >> 0) & 0xFF;
        }

        public String strRGB() {
            return R + "," + G + "," + B;
        }

        public String strTheme() {
            return strRGB() + "," + brightness;
        }

        public Integer intToHex(Integer number) {
            return (int) Long.parseLong(Integer.toHexString(number), 16);
        }

        public Integer getR() {
            return R;
        }

        public void setR(Integer r) {
            R = r;
        }

        public Integer getG() {
            return G;
        }

        public void setG(Integer g) {
            G = g;
        }

        public Integer getB() {
            return B;
        }

        public void setB(Integer b) {
            B = b;
        }
    }
}
