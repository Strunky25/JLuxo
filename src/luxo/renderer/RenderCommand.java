package luxo.renderer;

import org.joml.Vector4f;
import platform.opengl.OpenGLRendererAPI;

public class RenderCommand {

    private static RendererAPI rendererAPI = new OpenGLRendererAPI();
    
    public static void init() {
        rendererAPI.init();
    }
    
    public static void drawIndexed(VertexArray vertexArray) { 
        rendererAPI.drawIndexed(vertexArray);
    }
    
    public static void setClearColor(Vector4f color) { 
        rendererAPI.setClearColor(color);
    };
    
    public static void clear() { rendererAPI.clear(); }

    public static void setViewport(int x, int y, int width, int height) { 
        rendererAPI.setViewport(x, y, width, height) ;
    }
}
