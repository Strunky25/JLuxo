package luxo.renderer;

import luxo.Log;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL46C.*;


public class Shader {
    
    private int rendererID;

    public Shader(String vertexSource, String fragmentSource) {
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
    
    public void dispose() { glDeleteProgram(rendererID); }
    
    public void bind() { glUseProgram(rendererID); }
    
    public void unbind() { glUseProgram(0); }
    
    public void uploadUniformMat4(final String name, final Matrix4f uniform) {
        int location = glGetUniformLocation(rendererID, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = uniform.get(stack.mallocFloat(16));
            glUniformMatrix4fv(location, false, fb);
        }
    }
}
