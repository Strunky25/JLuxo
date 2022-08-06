package luxo.renderer;

import luxo.renderer.RendererAPI.API;
import org.joml.Matrix4f;

public class Renderer {
    
    private static final SceneData sceneData = new SceneData();
    
    public static void init() { 
        RenderCommand.init(); 
        Renderer2D.init();
    }
    
    public static void beginScene(OrthoCamera camera) {
        sceneData.viewProjectionMatrix = camera.getViewProjectionMatrix();
    }
    
    public static void endScene() {}
    
    public static void submit(Shader shader, VertexArray vertexArray, Matrix4f transform) {
        shader.bind();
        shader.setMat4("viewProjection", sceneData.viewProjectionMatrix);
        shader.setMat4("transform", transform);
        
        vertexArray.bind();
        RenderCommand.drawIndexed(vertexArray);
    }
    
    public static void onWindowResized(int width, int height) {
        RenderCommand.setViewport(0, 0, width, height);
    }
    
    public static void flush() {}
    
    public static API getAPI() { return RendererAPI.getAPI(); }
    
    public static class SceneData {
        Matrix4f viewProjectionMatrix;
    }
}
