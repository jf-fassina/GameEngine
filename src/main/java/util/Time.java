package util;

public class Time {

    public static float timeStarted = System.nanoTime();

    //time elapsed since start of app
    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
    }

}
