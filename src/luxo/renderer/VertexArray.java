package luxo.renderer;

import java.util.ArrayList;
import luxo.core.Log;
import luxo.core.Bindable;
import luxo.renderer.Buffer.IndexBuffer;
import luxo.renderer.Buffer.VertexBuffer;
import platform.opengl.OpenGLVertexArray;


public interface VertexArray extends Bindable {

    public void dispose();
        
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
