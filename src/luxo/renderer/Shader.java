package luxo.renderer;

import java.util.HashMap;
import luxo.core.Log;
import luxo.core.Bindable;
import org.joml.*;
import platform.opengl.OpenGLShader;

public abstract class Shader implements Bindable {
        
    public abstract void dispose();  
    
    public abstract String getName();
    
    public static Shader create(String filepath) {
        switch(Renderer.getAPI()) {
            case NONE   -> Log.coreAssert(false, "RendererAPI.NONE is currently not supported!");
            case OPENGL -> { return new OpenGLShader(filepath); }
        } 
        Log.coreAssert(false, "Unknown RendererAPI!");
        return null;
    }
    
    public static Shader create(String name, String vertexSource, String fragmentSource) {
        switch(Renderer.getAPI()) {
            case NONE   -> Log.coreAssert(false, "RendererAPI.NONE is currently not supported!");
            case OPENGL -> { return new OpenGLShader("Texture", vertexSource, fragmentSource); }
        } 
        Log.coreAssert(false, "Unknown RendererAPI!");
        return null;
    }
    
    public abstract void setMat4(final String name, final Matrix4f uniform);
    
    public abstract void setVec4(final String name, final Vector4f uniform);
    
    public abstract void setVec3(final String name, final Vector3f uniform);
    
    public abstract void setInt(final String name, final int uniform);
    
    
    public static class ShaderLibrary {
        
        private HashMap<String, Shader> shaders;
        
        public ShaderLibrary() { shaders = new HashMap<>(); }
        
        public void add(Shader shader) {
            add(shader.getName(), shader);
        }

        public void add(String name, Shader shader) {
            Log.coreAssert(!shaders.containsKey(name), "Shader already exists!");
            shaders.put(name, shader);
        }

        public Shader load(String filepath) {
            Shader shader = Shader.create(filepath);
            add(shader);
            return shader;
        }

        public Shader load(String name, String filepath) {
            Shader shader = Shader.create(filepath);
            add(name, shader);
            return shader;
        }

        public Shader get(String name) { 
            Log.coreAssert(shaders.containsKey(name), "Shader doesn't exist!");
            return shaders.get(name); 
        }
        
        public void dispose() {
            shaders.values().forEach(shader -> { shader.dispose(); });
        }
        
    }
}
