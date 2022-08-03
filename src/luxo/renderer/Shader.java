package luxo.renderer;

import luxo.Log;
import platform.opengl.OpenGLShader;

public interface Shader {
        
    public void dispose();
    
    public void bind();
    public void unbind();  
    
    public static Shader create(String vertexSource, String fragmentSource) {
        switch(Renderer.getAPI()) {
            case NONE   -> Log.coreAssert(false, "RendererAPI.NONE is currently not supported!");
            case OPENGL -> { return new OpenGLShader(vertexSource, fragmentSource); }
        } 
        Log.coreAssert(false, "Unknown RendererAPI!");
        return null;
    }
}
