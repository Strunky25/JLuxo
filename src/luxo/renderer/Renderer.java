package luxo.renderer;

import luxo.renderer.RendererAPI.API;
import org.joml.Matrix4f;

public class Renderer {
    
    private static SceneData sceneData = new SceneData();
    
    public static void beginScene(OrthoCamera camera) {
        sceneData.viewProjectionMatrix = camera.getViewProjectionMatrix();
    }
    public static void endScene() {}
    
    public static void submit(Shader shader, VertexArray vertexArray) {
        shader.bind();
        shader.uploadUniformMat4("viewProjection", sceneData.viewProjectionMatrix);
        
        vertexArray.bind();
        RenderCommand.drawIndexed(vertexArray);
    }
    
    public static void flush() {}
    
    public static API getAPI() { return RendererAPI.getAPI(); }
    
    public static class SceneData {
        Matrix4f viewProjectionMatrix;
    }
}
