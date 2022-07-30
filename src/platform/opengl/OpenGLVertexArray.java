package platform.opengl;

import java.util.ArrayList;
import luxo.Log;
import luxo.renderer.Buffer;
import luxo.renderer.Buffer.BufferLayout;
import luxo.renderer.Buffer.IndexBuffer;
import luxo.renderer.Buffer.VertexBuffer;
import luxo.renderer.VertexArray;
import static org.lwjgl.opengl.GL46C.*;

public class OpenGLVertexArray implements VertexArray{
    
    private final ArrayList<VertexBuffer> vertexBuffers;
    private IndexBuffer indexBuffer;
    private final int rendererID;
    
    public OpenGLVertexArray() {
        vertexBuffers = new ArrayList<>();
        rendererID = glCreateVertexArrays();
    }

    @Override
    public void dispose() { glDeleteVertexArrays(rendererID); }

    @Override
    public void bind() { glBindVertexArray(rendererID); }

    @Override
    public void unbind() { glBindVertexArray(0); }

    @Override
    public void addVertexBuffer(VertexBuffer vertexBuffer) {
        glBindVertexArray(rendererID);
        vertexBuffer.bind();
        
        int index = 0;
        BufferLayout layout = vertexBuffer.getLayout();
        Log.coreAssert(layout != null, "Vertex Buffer has no layout!");
        Log.coreAssert(!layout.getElements().isEmpty(), "Vertex Buffer Layout has no elements!");
        for(Buffer.BufferElement element : layout) {
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(index, 
                element.getComponentCount(),
                element.type.toOpenGL(),
                element.normalized,
                layout.getStride(),
                element.offset);
            index++;
        }
        
        vertexBuffers.add(vertexBuffer);
    }

    @Override
    public void setIndexBuffer(IndexBuffer indexBuffer) {
        glBindVertexArray(rendererID);
        indexBuffer.bind();
        
        this.indexBuffer = indexBuffer;
    }

    @Override
    public ArrayList<VertexBuffer> getVertexBuffers() { return vertexBuffers; }

    @Override
    public IndexBuffer getIndexBuffer() { return indexBuffer; }
}
