package ru.koshakmine.icstd.utils;

public class MathUtils {
    private static final double RAD_TO_DEGREES = 180d / Math.PI;

    public static double degreesToRad(double x) {
        return x / RAD_TO_DEGREES;
    }

    public static double radToDegrees(double x) {
        return x * RAD_TO_DEGREES;
    }
}
