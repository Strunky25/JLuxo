package luxo.renderer;

import java.util.ArrayList;
import luxo.Log;
import luxo.renderer.Buffer.IndexBuffer;
import luxo.renderer.Buffer.VertexBuffer;
import platform.opengl.OpenGLVertexArray;


public interface VertexArray {

    public void dispose();
    
    public void bind();
    public void unbind();
    
    public void addVertexBuffer(final VertexBuffer vertexBuffer);
    public void setIndexBuffer(final IndexBuffer indexBuffer);
    
    public ArrayList<VertexBuffer> getVertexBuffers();
    public IndexBuffer getIndexBuffer();
    
    public static VertexArray create() {
        switch(Renderer.getAPI()) {
            case NONE -> Log.coreAssert(false, "RendererAPI.NONE is currently not supported!");
            case OPENGL -> { return new OpenGLVertexArray(); }
        } 
        Log.coreAssert(false, "Unknown RendererAPI!");
        return null;
    }
}
