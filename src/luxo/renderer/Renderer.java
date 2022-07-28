package luxo.renderer;


public class Renderer {
    
    private static final RendererAPI RENDERER_API = RendererAPI.OPENGL;
    
    public static RendererAPI getAPI() { return RENDERER_API; }
    
    public enum RendererAPI {
        NONE,
        OPENGL
    }
}
