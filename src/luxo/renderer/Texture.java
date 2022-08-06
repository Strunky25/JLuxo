package luxo.renderer;

import luxo.core.Log;
import platform.opengl.OpenGLTexture2D;

public interface Texture {

    public abstract int getWidth();
    public abstract int getHeight();
    
    public abstract void bind();
    public abstract void bind(int slot);
    
    public abstract void setData(int[] data, int size);
    
    public void dispose();
    
    public static abstract class Texture2D implements Texture {
        
        public static Texture2D create(String path) {
            switch(Renderer.getAPI()) {
                case NONE -> Log.coreAssert(false, "RendererAPI.NONE is currently not supported!");
                case OPENGL -> { return new OpenGLTexture2D(path); }
            } 
            Log.coreAssert(false, "Unknown RendererAPI!");
            return null;
        }
        
        public static Texture2D create(int width, int height) {
            switch(Renderer.getAPI()) {
                case NONE -> Log.coreAssert(false, "RendererAPI.NONE is currently not supported!");
                case OPENGL -> { return new OpenGLTexture2D(width, height); }
            } 
            Log.coreAssert(false, "Unknown RendererAPI!");
            return null;
        }
    }
    
}
