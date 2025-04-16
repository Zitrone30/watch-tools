package me.DNS.wmtools.utils;

public enum XDirection {
    WEST(-1),
    CENTER(0),
    EAST(1);

    private final int value;

    XDirection(int value) {
        this.value = value;
    }

    public int getRaw() {
        return value;
    }
}
