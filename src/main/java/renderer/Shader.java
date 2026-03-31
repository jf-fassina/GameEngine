package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderProgramId;
    private String vertexShaderSource;
    private String fragmentShaderSource;
    private String filepath = "";


    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("#type\\s+");

            //Find first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 5;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            //Find second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 5;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) vertexShaderSource = stripFirstLine(splitString[1]);
            else if (firstPattern.equals("fragment")) fragmentShaderSource = stripFirstLine(splitString[1]);
            else throw new IOException("Unexpected token '" + firstPattern + "' in: " + filepath);

            if (secondPattern.equals("vertex")) vertexShaderSource = stripFirstLine(splitString[2]);
            else if (secondPattern.equals("fragment")) fragmentShaderSource = stripFirstLine(splitString[2]);
            else throw new IOException("Unexpected token '" + secondPattern + "' in: " + filepath);

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: " + filepath;
        }
        System.out.println("Vertex shader source: " + vertexShaderSource);
        System.out.println("Fragment shader source: " + fragmentShaderSource);
    }

    public void compileAndLink() {
        int vertexId, fragmentId;

        //First load and compile vertex shaders
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        //Pass shader source to GPU
        glShaderSource(vertexId, vertexShaderSource);
        glCompileShader(vertexId);

        //Check for errors in compilation
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("Error compiling vertex shader: " + glGetShaderInfoLog(vertexId, length) + "\nAt: " + filepath);
            assert false : "";
        }

        //First load and compile fragment shaders
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass shader source to GPU
        glShaderSource(fragmentId, fragmentShaderSource);
        glCompileShader(fragmentId);

        //Check for errors in compilation
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("Error compiling fragment shader: " + glGetShaderInfoLog(fragmentId, length) + "\nAt: " + filepath);
            assert false : "";
        }

        //Link shaders and check for errors

        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexId);
        glAttachShader(shaderProgramId, fragmentId);
        glLinkProgram(shaderProgramId);

        //Check for linking errors
        success = glGetProgrami(shaderProgramId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
            System.out.println("Error linking shader: " + glGetProgramInfoLog(shaderProgramId, length) + "\nAt: " + filepath);
            assert false : "";
        }

    }

    public void use() {
        //Bind Shader Program
        glUseProgram(shaderProgramId);
    }

    public void detach() {
        glUseProgram(0);
    }

    private String stripFirstLine(String s) {
        int nl = s.indexOf('\n');
        return nl >= 0 ? s.substring(nl + 1) : s;
    }

}
