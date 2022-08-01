package luxo.renderer;

import org.joml.Vector4f;

public abstract class RendererAPI {
    
    private static API api = API.OPENGL;
    
    public enum API {
        NONE,
        OPENGL
    }
    
    public abstract void setClearColor(Vector4f color);
    public abstract void clear();
    
    public abstract void drawIndexed(VertexArray vertexArray);

    public static API getAPI() { return api; }
}
