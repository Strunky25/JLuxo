package luxo.renderer;

import glm_.vec4.Vec4;
import platform.opengl.OpenGLRendererAPI;

public class RenderCommand {

    private static RendererAPI rendererAPI = new OpenGLRendererAPI();
    
    public static void drawIndexed(VertexArray vertexArray) { 
        rendererAPI.drawIndexed(vertexArray);
    }
    
    public static void setClearColor(Vec4 color) { 
        rendererAPI.setClearColor(color);
    };
    public static void clear() { rendererAPI.clear(); }
}
