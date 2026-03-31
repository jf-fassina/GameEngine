package cardeal.scenes;

import cardeal.Camera;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout (location=0) in vec3 posA;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(posA,1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexId, fragmentId, shaderProgram;
    private String uProjection = "uProjection";
    private String uView = "uView";
    private final String filepath = "src/main/shaders/default.glsl";

    private float[] vertexArray = {
            //positions              //colors
            // x     y     z         //r    g     b     a
            100.5f, 0.5f, 0.0f, /**/ 1.0f, 0.0f, 0.0f, 1.0f, //Bottom Right  0
            0.5f, 100.5f, 0.0f, /**/ 0.0f, 1.0f, 0.0f, 1.0f, //Top Left      1
            100.5f, 100.5f, 0.0f, /**/ 1.0f, 0.0f, 1.0f, 1.0f, //Top Right     2
            0.5f, 0.5f, 0.0f, /**/ 1.0f, 1.0f, 0.0f, 1.0f, //Bottom Left   3
    };

    //IMPORTANT: Must be in counte-clockwise order ( trigonometric circle at its peak)
    private int[] elementArray = {
            /*
            *   x       x
            *
            *   x       x
            */
            2, 1, 0,//Top Right Triangle
            0, 1, 3//Bottom Left Triangle
    };

    private int vaoId, vboId, eboId;

    private Shader defaultShader;

    public LevelEditorScene() {

    }

    //---------------------------------------------

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        defaultShader = new Shader(filepath);
        defaultShader.compileAndLink();
        //Generate VAO, VBO, EBO buffer objects, and send to GPU ---------------

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO and upload vertex buffer
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);//pos

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);//color

    }

    //-----------------------------------------

    @Override
    public void update(float deltaTime) {

        camera.position.x -= deltaTime * 50.0f;

        defaultShader.use();
        // I HATE STRINGS!
        defaultShader.uploadMat4f(uProjection, camera.getProjectionMatrix());
        defaultShader.uploadMat4f(uView, camera.getViewMatrix());

        //Bind VAO that we're using
        glBindVertexArray(vaoId);

        //Enable vertex Attribute Pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind all
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0); // 0 is Flag that Binds Nothing

        defaultShader.detach();

    }


}
