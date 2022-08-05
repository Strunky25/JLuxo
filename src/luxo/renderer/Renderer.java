package luxo.renderer;

import luxo.Log;
import luxo.renderer.RendererAPI.API;
import org.joml.Matrix4f;
import platform.opengl.OpenGLShader;

public class Renderer {
    
    private static final SceneData sceneData = new SceneData();
    
    public static void init() { RenderCommand.init(); }
    
    public static void beginScene(OrthoCamera camera) {
        sceneData.viewProjectionMatrix = camera.getViewProjectionMatrix();
    }
    
    public static void endScene() {}
    
    public static void submit(Shader shader, VertexArray vertexArray, Matrix4f transform) {
        shader.bind();
        ((OpenGLShader) shader).uploadUniformMat4("viewProjection", sceneData.viewProjectionMatrix);
        ((OpenGLShader) shader).uploadUniformMat4("transform", transform);
        
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
