package cardeal.listeners;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {

    private static MouseListener instance;
    private double scrollX, scrollY;
    private double posX, posY, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[GLFW_MOUSE_BUTTON_LAST];
    private boolean isDragging;


    protected MouseListener() {
        this.scrollX = 0d;
        this.scrollY = 0d;
        this.posX = 0d;
        this.posY = 0d;
        this.lastX = 0d;
        this.lastY = 0d;
    }

    public static MouseListener getMouseListener() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double posX, double posY) {
        getMouseListener().lastX = getMouseListener().posX;
        getMouseListener().lastY = getMouseListener().posY;
        getMouseListener().posX = posX;
        getMouseListener().posY = posY;
        getMouseListener().isDragging = getMouseListener().mouseButtonPressed[0] || getMouseListener().mouseButtonPressed[1] || getMouseListener().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < getMouseListener().mouseButtonPressed.length)
                getMouseListener().mouseButtonPressed[button] = true;

        } else if (action == GLFW_RELEASE) {
            getMouseListener().mouseButtonPressed[button] = false;
            getMouseListener().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        getMouseListener().scrollX = xOffset;
        getMouseListener().scrollY = yOffset;
    }


    public static void endFrame() {
        getMouseListener().lastX = 0d;
        getMouseListener().lastY = 0d;
        getMouseListener().lastX = getMouseListener().posX;
        getMouseListener().lastY = getMouseListener().posY;
    }


    public static float getX() {
        return (float) getMouseListener().posX;
    }

    public static float getY() {
        return (float) getMouseListener().posY;
    }

    public static float getDeltaX() {
        return (float) (getMouseListener().lastX - getMouseListener().posX);
    }

    public static float getDeltaY() {
        return (float) (getMouseListener().lastY - getMouseListener().posY);
    }

    public static float getScrollX() {
        return (float) getMouseListener().scrollX;
    }

    public static float getScrollY() {
        return (float) getMouseListener().scrollY;
    }

    public static boolean isDragging() {
        return getMouseListener().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < getMouseListener().mouseButtonPressed.length) return getMouseListener().mouseButtonPressed[button];
        else return false;
    }

}
