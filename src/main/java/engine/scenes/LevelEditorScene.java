package engine.scenes;

import engine.Window;
import engine.listeners.KeyListener;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2f;


    public LevelEditorScene() {
        System.out.println("Inside LevelEditorScene");
    }

    @Override
    public void update(float deltaTime) {
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) changingScene = true;
        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= deltaTime;
            Window.getWindow().r -= deltaTime * 5f;
            Window.getWindow().g -= deltaTime * 5f;
            Window.getWindow().b -= deltaTime * 5f;
        } else if (changingScene) Window.changeScene(1);


    }

}
