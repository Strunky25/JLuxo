package luxo.renderer;

import luxo.renderer.Buffer.*;
import luxo.renderer.Texture.Texture2D;
import org.joml.*;

public final class Renderer2D {
    
    private static final Renderer2DStorage data = new Renderer2DStorage();
    
    private Renderer2D() {}
    
    public static void init() {
        data.whiteTexture = Texture2D.create(1, 1);
        int[] buffer = { 0xffffffff };
        data.whiteTexture.setData(buffer, buffer.length * Integer.BYTES);
        
        data.textureShader = Shader.create("assets/shaders/Texture.glsl");
        data.textureShader.bind();
        data.textureShader.setInt("tex", 0);
        
        float[] squareVertices = {
            -0.5f, -0.5f, 0.0f, 0, 0,
             0.5f, -0.5f, 0.0f, 1, 0,
             0.5f,  0.5f, 0.0f, 1, 1,
            -0.5f,  0.5f, 0.0f, 0, 1
        };
        data.QuadVertexArray = VertexArray.create();
        VertexBuffer squareVB = VertexBuffer.create(squareVertices);
        squareVB.setLayout(new BufferLayout(
            new BufferElement(ShaderDataType.FLOAT3, "position"),
            new BufferElement(ShaderDataType.FLOAT2, "texCoord")
        ));
        data.QuadVertexArray.addVertexBuffer(squareVB);
        int squareIndices[] = {0, 1, 2, 2, 3, 0};
        IndexBuffer squareIB = IndexBuffer.create(squareIndices);
        data.QuadVertexArray.setIndexBuffer(squareIB);
    }
    
    public static void shutdown() { data.dispose(); }

    public static void beginScene(final OrthoCamera camera) {       
        data.textureShader.bind();
        data.textureShader.setMat4("viewProjection", camera.getViewProjectionMatrix());
    }
    
    public static void endScene() {}
    
    public static void drawQuad(final Vector2f position, final Vector2f size, final Vector4f color) {
        drawQuad(new Vector3f(position, 0), size, color);
    }
    
    public static void drawQuad(final Vector3f position, final Vector2f size, final Vector4f color) {
        data.whiteTexture.bind();
        data.textureShader.setVec4("col", color);
        data.textureShader.setMat4("transform", new Matrix4f().translate(position)
                .scale(new Vector3f(size, 1)));
        data.QuadVertexArray.bind();
        RenderCommand.drawIndexed(data.QuadVertexArray);
    }
    
    public static void drawQuad(final Vector2f position, final Vector2f size, final Texture2D texture) {
        drawQuad(new Vector3f(position, 0), size, texture);
    }
    
    public static void drawQuad(final Vector3f position, final Vector2f size, final Texture2D texture) {
        texture.bind();
        data.textureShader.setVec4("col", new Vector4f(1));
        data.textureShader.setMat4("transform", new Matrix4f().translate(position)
                .scale(new Vector3f(size, 1)));
        data.QuadVertexArray.bind();
        RenderCommand.drawIndexed(data.QuadVertexArray);  
    }
    
    
    private static final class Renderer2DStorage {
        private VertexArray QuadVertexArray;
        private Shader textureShader;
        private Texture2D whiteTexture;
        
        private void dispose() {
            QuadVertexArray.dispose();
            textureShader.dispose();
        }
    }
}
