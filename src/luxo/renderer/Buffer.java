package luxo.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import luxo.Log;
import luxo.core.Bindable;
import platform.opengl.OpenGLBuffer.*;
import static org.lwjgl.opengl.GL20C.*;

public interface Buffer extends Bindable {
    
    public void dispose();
      
    public static interface VertexBuffer extends Buffer {
        public static VertexBuffer create(float[] vertices) {
            switch(Renderer.getAPI()) {
                case NONE -> Log.coreAssert(false, "RendererAPI.NONE is currently not supported!");
                case OPENGL -> { return new OpenGLVertexBuffer(vertices); }
            } 
            Log.coreAssert(false, "Unknown RendererAPI!");
            return null;
        }
        
        public abstract void setLayout(BufferLayout layout);
        public abstract BufferLayout getLayout();
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
    
    public enum ShaderDataType {
        NONE,
        BOOL,
        INT, INT2, INT3, INT4,
        FLOAT, FLOAT2, FLOAT3, FLOAT4, 
        MAT3, MAT4;
        
        public int size() {
            switch(this) {
                case BOOL           -> { return 1; }
                case INT, FLOAT     -> { return 4; }
                case INT2, FLOAT2   -> { return 4 * 2; }
                case INT3, FLOAT3   -> { return 4 * 3; }
                case INT4, FLOAT4   -> { return 4 * 4; }
                case MAT3           -> { return 4 * 3 * 3; }
                case MAT4           -> { return 4 * 4 * 4; }
            }
            Log.coreAssert(false, "Unknown ShaderDataType!");
            return 0;
        }
        
        public int toOpenGL() {
            switch(this) {
                case BOOL                           -> { return GL_BOOL; }
                case INT, INT2, INT3, INT4          -> { return GL_INT; }
                case FLOAT, FLOAT2, FLOAT3, FLOAT4  -> { return GL_FLOAT; }
                case MAT3, MAT4                     -> { return GL_FLOAT; }
            }
            Log.coreAssert(false, "Unknown ShaderDataType!");
            return 0;
        }
    }
    
    public static class BufferElement {
        
        public final String name;
        public final ShaderDataType type;
        public final int size; // in bytes
        public int offset; // in bytes
        public boolean normalized;
        
        public BufferElement(ShaderDataType type, String name) {
            this.name = name;
            this.type = type;
            this.size = type.size();
            this.offset = 0;
            this.normalized = false;
        }
        
        public BufferElement(ShaderDataType type, String name, boolean normalized) {
            this.name = name;
            this.type = type;
            this.size = type.size();
            this.offset = 0;
            this.normalized = normalized;
        }
        
        public int getComponentCount() {
            switch(type) {
                case BOOL, INT, FLOAT   -> { return 1; }
                case INT2, FLOAT2       -> { return 2; }
                case INT3, FLOAT3       -> { return 3; }
                case INT4, FLOAT4       -> { return 4; }
                case MAT3               -> { return 3 * 3; }
                case MAT4               -> { return 4 * 4; }
            }
            Log.coreAssert(false, "Unknown ShaderDataType!");
            return 0;
        }
    }
    
    public static class BufferLayout implements Iterable<BufferElement> {
        
        private ArrayList<BufferElement> elements;
        private int stride;
        
        public BufferLayout(BufferElement... elements) {
            this.elements = new ArrayList<>(Arrays.asList(elements));
            int offset = 0;
            for(BufferElement element: this.elements) {
                element.offset = offset;
                offset += element.size;
                stride += element.size;
            }
        }
        
        public ArrayList<BufferElement> getElements() { return elements; }
        public int getStride() { return stride; }

        @Override
        public Iterator<BufferElement> iterator() { return elements.iterator(); }
    }
}
