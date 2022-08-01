package luxo.renderer;

import glm_.vec4.Vec4;

public abstract class RendererAPI {
    
    private static API api = API.OPENGL;
    
    public enum API {
        NONE,
        OPENGL
    }
    
    public abstract void setClearColor(Vec4 color);
    public abstract void clear();
    
    public abstract void drawIndexed(VertexArray vertexArray);

    public static API getAPI() { return api; }
}
