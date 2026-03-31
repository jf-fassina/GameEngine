package cardeal.scenes;

import cardeal.Camera;

public abstract class Scene {

    protected Camera camera;
    public Scene(){

    }

    public void init(){

    }


    //TODO: Game Obj, Render, PhysicsHandle
    public abstract void update(float deltaTime);

}
