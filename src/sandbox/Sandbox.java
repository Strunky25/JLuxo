package sandbox;

import luxo.Application;
import luxo.OrthoCameraController;
import luxo.core.Timestep;
import luxo.events.Event;
import luxo.Layer;
import luxo.renderer.*;
import luxo.renderer.Shader.ShaderLibrary;
import luxo.renderer.Texture.Texture2D;
import platform.opengl.OpenGLShader;

import imgui.ImGui;
import luxo.events.ApplicationEvent.WindowResizedEvent;
import org.joml.*;

public class Sandbox extends Application {
    
    public static class ExampleLayer extends Layer {
        
        private VertexArray squareVA;
        private Texture2D texture, texture2;
        private ShaderLibrary shaderLibrary;
        private OrthoCameraController camController;
        private float[] squareCol;

        @Override
        public void onAttach() {
            camController = new OrthoCameraController(1280.0f / 720.0f, true);
            
            squareCol = new float[]{0.2f, 0.3f, 0.8f};
            
            shaderLibrary = new ShaderLibrary();

            float[] squareVertices = {
                /*  position    */
                -0.5f, -0.5f, 0.0f, 0, 0,
                 0.5f, -0.5f, 0.0f, 1, 0,
                 0.5f,  0.5f, 0.0f, 1, 1,
                -0.5f,  0.5f, 0.0f, 0, 1,
            };

            squareVA = VertexArray.create();
            Buffer.VertexBuffer squareVB = Buffer.VertexBuffer.create(squareVertices);
            squareVB.setLayout(new Buffer.BufferLayout(
                new Buffer.BufferElement(Buffer.ShaderDataType.FLOAT3, "position"),
                new Buffer.BufferElement(Buffer.ShaderDataType.FLOAT2, "texCoord")
            ));
            squareVA.addVertexBuffer(squareVB);
            int squareIndices[] = {0, 1, 2, 2, 3, 0};
            Buffer.IndexBuffer squareIB = Buffer.IndexBuffer.create(squareIndices);
            squareVA.setIndexBuffer(squareIB);

            Shader flatColorShader = shaderLibrary.load("assets/shaders/FlatColor.glsl");
            Shader textureShader = shaderLibrary.load("assets/shaders/Texture.glsl");
             
            texture = Texture2D.create("assets/textures/Checkerboard.png");
            texture2 = Texture2D.create("assets/textures/ChernoLogo.png");
        
            textureShader.bind();
            ((OpenGLShader) textureShader).uploadUniformInt("tex", 0);
        }

        @Override
        public void onDetach() {}

        @Override
        public void onUpdate(Timestep ts) {                            
            RenderCommand.setClearColor(new Vector4f(0.1f, 0.1f, 0.1f, 1));
            RenderCommand.clear();
            
            camController.onUpdate(ts);
            
            Renderer.beginScene(camController.getCamera());
            
            Shader flatColorShader = shaderLibrary.get("FlatColor");
            flatColorShader.bind();
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    Vector3f pos = new Vector3f(i * 0.11f, j * 0.11f, 0);
                    ((OpenGLShader) flatColorShader).uploadUniformVec3("col", new Vector3f(squareCol));
                    Renderer.submit(flatColorShader, squareVA, new Matrix4f().translate(pos).scale(0.1f));
                }
            }
           
            texture.bind(0);
            
            Shader textureShader = shaderLibrary.get("Texture");
            Renderer.submit(textureShader, squareVA, new Matrix4f().scale(1.5f));     
            texture2.bind(0);
            Renderer.submit(textureShader, squareVA, new Matrix4f().scale(1.5f));      
            
            Renderer.endScene();      
            //Renderer.flush();
        }

        @Override
        public void onImGuiRender() {
            ImGui.begin("Settings");
            ImGui.colorEdit3("SquareColor", squareCol);
            ImGui.text("Application average %.3f ms/frame (%.1f FPS)".formatted(1000 / ImGui.getIO().getFramerate(), ImGui.getIO().getFramerate()));
            ImGui.end();
        }

        @Override
        public void onEvent(Event event) {
            camController.onEvent(event);
            if(event instanceof WindowResizedEvent evt) {
                camController.setAspectRatio((float) evt.getWidth() / (float) evt.getHeight());
            }
            //Event.EventDispatcher dispatcher = new Event.EventDispatcher(event);
            //dispatcher.dispatch(KeyPressedEvent.class, this::onKeyPressedEvent);
        }

        @Override
        public void dispose() {
            shaderLibrary.dispose();
            squareVA.dispose();
        }
    }
    
    public Sandbox() { pushLayer(new ExampleLayer()); }
}
