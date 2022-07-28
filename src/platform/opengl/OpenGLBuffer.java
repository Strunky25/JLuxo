package platform.opengl;

import luxo.renderer.Buffer;
import static org.lwjgl.opengl.GL45C.*;

public abstract class OpenGLBuffer implements Buffer {
    
    protected int rendererID;

    protected OpenGLBuffer() { rendererID = glCreateBuffers(); }
    
    @Override
    public void dispose() {
        glDeleteBuffers(rendererID);
    }

    @Override
    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, rendererID);
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    public static class OpenGLVertexBuffer extends OpenGLBuffer implements VertexBuffer {
        
        public OpenGLVertexBuffer(float[] vertices) {
            glBindBuffer(GL_ARRAY_BUFFER, rendererID);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        }      
    }
    
    public static class OpenGLIndexBuffer extends OpenGLBuffer implements IndexBuffer {
       
        private int count;
        
        public OpenGLIndexBuffer(int[] indices) {
            this.count = indices.length;
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rendererID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        }

        @Override
        public int getCount() { return count; }
    }
}
