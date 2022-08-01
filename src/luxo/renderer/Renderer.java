package luxo.renderer;

import luxo.renderer.RendererAPI.API;

public class Renderer {
    
    public static void beginScene() {}
    public static void endScene() {}
    
    public static void submit(VertexArray vertexArray) {
        vertexArray.bind();
        RenderCommand.drawIndexed(vertexArray);
    }
    
    public static void flush() {}
    
    public static API getAPI() { return RendererAPI.getAPI(); }
}
