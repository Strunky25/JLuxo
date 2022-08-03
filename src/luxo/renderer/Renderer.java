package luxo.renderer;

import luxo.renderer.RendererAPI.API;
import org.joml.Matrix4f;
import platform.opengl.OpenGLShader;

public class Renderer {
    
    private static SceneData sceneData = new SceneData();
    
    public static void beginScene(OrthoCamera camera) {
        sceneData.viewProjectionMatrix = camera.getViewProjectionMatrix();
    }
    public static void endScene() {}
    
    public static void submit(Shader shader, VertexArray vertexArray, Matrix4f transform) {
        shader.bind();
        OpenGLShader shad = (OpenGLShader) shader;
        shad.uploadUniformMat4("viewProjection", sceneData.viewProjectionMatrix);
        shad.uploadUniformMat4("transform", transform);
        
        vertexArray.bind();
        RenderCommand.drawIndexed(vertexArray);
    }
    
    public static void flush() {}
    
    public static API getAPI() { return RendererAPI.getAPI(); }
    
    public static class SceneData {
        Matrix4f viewProjectionMatrix;
    }
}
