package engine;

import engine.util.Time;
import engine.listeners.KeyListener;
import engine.listeners.MouseListener;

import engine.scenes.LevelEditorScene;
import engine.scenes.LevelScene;
import engine.scenes.Scene;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static String title = "Título";
    private static int width = 900;
    private static int height = 700;


    private static Window window = null;
    private long glfwWindow;
    public float r, g, b, a;


    private static Scene currentScene = null;

    private Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.r = 1f;
        this.g = 1f;
        this.b = 1f;
        this.a = 1f;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "Unknown scene: " + newScene;
                break;
        }
    }

    //TODO: setWindow(){}
    public static Window getWindow() {
        if (window == null) {
            Window.window = new Window(title, width, height);
        }
        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        //-- Best Practices!!
        //Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW a free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    public void init() {
        // set up error callback
        //System.err.println("Error");
        GLFWErrorCallback.createPrint(System.err).set();


        //Initialize GLFW. Most functions don't work before this
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        //Listeners --
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);


        //Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Window.changeScene(0);
        //makes sure we are in a scene
    }

    public void loop() {
        //initializes time
        float beginTime = Time.getTime();
        float endTime;
        float deltaTime = -1f;


        while (!glfwWindowShouldClose(glfwWindow)) {
            //Poll Events -> mouse, keys
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (deltaTime >= 0) currentScene.update(deltaTime);

            glfwSwapBuffers(glfwWindow);

            //end time = now
            endTime = Time.getTime();
            //does delta
            deltaTime = endTime - beginTime;
            //new begin time
            beginTime = endTime;
        }

    }

    //GETTERS E SETTERS

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
