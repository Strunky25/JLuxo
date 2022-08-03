package platform.opengl;

import luxo.Log;
import luxo.renderer.Shader;
import java.nio.FloatBuffer;
import org.joml.*;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL46C.*;

public class OpenGLShader implements Shader {
    
    private int rendererID;

    public OpenGLShader(String vertexSource, String fragmentSource) {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);
        if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            Log.coreError("Vertex Shader compilation failure!");
            Log.coreAssert(false, glGetShaderInfoLog(vertexShader));
                        
            glDeleteShader(vertexShader);
            return;
        }
        
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);
        if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            Log.coreError("Fragment Shader compilation failure!");
            Log.coreAssert(false, glGetShaderInfoLog(fragmentShader));
              
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
            return;
        }
        
        rendererID = glCreateProgram();
        glAttachShader(rendererID, vertexShader);
        glAttachShader(rendererID, fragmentShader);
        glLinkProgram(rendererID);
        if(glGetProgrami(rendererID, GL_LINK_STATUS) == GL_FALSE) {
            Log.coreError("Shader Linking failure!");
            Log.coreAssert(false, glGetProgramInfoLog(rendererID));
              
            glDeleteProgram(rendererID);
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
            return;
        }
        
        glDetachShader(rendererID, vertexShader);
        glDetachShader(rendererID, fragmentShader );
    }
    
    @Override
    public void dispose() { glDeleteProgram(rendererID); }
    
    @Override
    public void bind() { glUseProgram(rendererID); }
    
    @Override
    public void unbind() { glUseProgram(0); }
    
    public void uploadUniformMat4(final String name, final Matrix4f uniform) {
        int location = glGetUniformLocation(rendererID, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = uniform.get(stack.mallocFloat(16));
            glUniformMatrix4fv(location, false, fb);
        }
    }
    
    public void uploadUniformVec4(final String name, final Vector4f uniform) {
        int location = glGetUniformLocation(rendererID, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = uniform.get(stack.mallocFloat(4));
            glUniform4fv(location, fb);
        }
    }
    
    public void uploadUniformVec3(final String name, final Vector3f uniform) {
        int location = glGetUniformLocation(rendererID, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = uniform.get(stack.mallocFloat(3));
            glUniform3fv(location, fb);
        }
    }
    
    public void uploadUniformInt(final String name, final int uniform) {
        int location = glGetUniformLocation(rendererID, name);
        glUniform1i(location, uniform);
    }
}

