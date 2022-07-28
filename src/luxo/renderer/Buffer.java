package luxo.renderer;

import luxo.Log;
import platform.opengl.OpenGLBuffer.*;

public interface Buffer {
    
    public void dispose();
    public void bind();
    public void unbind();
      
    public static interface VertexBuffer extends Buffer {
        public static VertexBuffer create(float[] vertices) {
            switch(Renderer.getAPI()) {
                case NONE -> Log.coreAssert(false, "RendererAPI.NONE is currently not supported!");
                case OPENGL -> { return new OpenGLVertexBuffer(vertices); }
            } 
            Log.coreAssert(false, "Unknown RendererAPI!");
            return null;
        }
    }
    
    public static interface IndexBuffer extends Buffer {
        
        public int getCount();
        
        public static IndexBuffer create(int[] indices) {
            switch(Renderer.getAPI()) {
                case NONE -> Log.coreAssert(false, "RendererAPI.NONE is currently not supported!");
                case OPENGL -> { return new OpenGLIndexBuffer(indices); }
            } 
            Log.coreAssert(false, "Unknown RendererAPI!");
            return null;
        }
    }
}
