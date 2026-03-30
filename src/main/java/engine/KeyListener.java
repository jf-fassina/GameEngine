package engine;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {

    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[GLFW_KEY_LAST];


    private KeyListener() {
    }

    public static KeyListener getKeyListener() {
        if (instance == null) instance = new KeyListener();
        return KeyListener.instance;
    }


    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS)
            getKeyListener().keyPressed[key] = true;
        else if (action == GLFW_RELEASE)
            getKeyListener().keyPressed[key] = false;
    }


    public static boolean isKeyPressed(int key) {
        return getKeyListener().keyPressed[key];
        //makes so if a impossible key is pressed it crashes!
    }


}
