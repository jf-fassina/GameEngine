package engine.scenes;

public abstract class Scene {

    public Scene(){

    }

    public void init(){

    }


    //TODO: Game Obj, Render, PhysicsHandle
    public abstract void update(float deltaTime);

}
