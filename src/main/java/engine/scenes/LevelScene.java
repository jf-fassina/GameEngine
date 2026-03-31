package engine.scenes;

import engine.Window;

public class LevelScene extends Scene {

    public LevelScene() {
        System.out.println("Inside LevelScene");
        Window.getWindow().r = 1f;
        Window.getWindow().g = 1f;
        Window.getWindow().b = 1f;
    }

    @Override
    public void update(float deltaTime) {

    }

}
