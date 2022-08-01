package luxo.renderer;

import glm_.mat4x4.Mat4;
import luxo.renderer.RendererAPI.API;

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
        Mat4 viewProjectionMatrix;
    }
}
