package luxo.renderer;

import luxo.Log;
import static org.lwjgl.opengl.GL41C.*;


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
    
    public void bind() { glUseProgram(rendererID); }
    
    public void unbind() { glUseProgram(0); }
}
