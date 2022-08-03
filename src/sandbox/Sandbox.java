package sandbox;

import luxo.Application;
import luxo.Input;
import luxo.Layer;
import luxo.events.Event;
import luxo.renderer.*;
import luxo.core.Timestep;
import platform.opengl.OpenGLShader;
import static luxo.KeyCode.*;

import imgui.ImGui;
import luxo.renderer.Texture.Texture2D;
import org.joml.*;

public class Sandbox extends Application {
    
    public static class ExampleLayer extends Layer {
        
        private Shader flatColorShader, textureShader;  
        private VertexArray squareVA;
        private OrthoCamera camera;
        private Vector3f camPos, squarePos;
        private Texture2D texture;
        private float[] squareCol;
        private float camMoveSpeed, camRotSpeed, camRot;

        @Override
        public void onAttach() {
            camMoveSpeed = 1f;
            camRotSpeed = 3f;
            camPos = new Vector3f(0);
            squarePos = new Vector3f(0);
            camera = new OrthoCamera(-1.6f, 1.6f, -0.9f, 0.9f);
            squareCol = new float[]{0.2f, 0.3f, 0.8f};

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

            String flatColorVertexSource =  
                """
                #version 330 core

                uniform mat4 viewProjection;
                uniform mat4 transform;

                layout(location = 0) in vec3 position;
                layout(location = 1) in vec2 texCoord;

                out vec3 v_pos;

                void main() {
                    gl_Position = viewProjection * transform * vec4(position, 1.0);
                    v_pos = position;
                }
                """;
            String flatColorFragmentSource = 
                """
                #version 330 core

                uniform vec3 col;
                
                in vec3 v_pos;
                out vec4 color;

                void main() {
                    color = vec4(col, 1.0);
                }
                """;
            flatColorShader = Shader.create(flatColorVertexSource, flatColorFragmentSource);
            
            String textureVertexSource =  
                """
                #version 330 core
                
                layout(location = 0) in vec3 position;
                layout(location = 1) in vec2 texCoord;

                uniform mat4 viewProjection;
                uniform mat4 transform;

                out vec2 texCord;

                void main() {
                    gl_Position = viewProjection * transform * vec4(position, 1.0);
                    texCord = texCoord;
                }
                """;
            String textureFragmentSource = 
                """
                #version 330 core
                
                uniform sampler2D tex;
                
                in vec2 texCord;
                
                out vec4 color;

                void main() {
                    color = texture(tex, texCord);
                }
                """;
            texture = Texture2D.create("assets/textures/Checkerboard.png");
            texture.bind(0);
            textureShader = Shader.create(textureVertexSource, textureFragmentSource);
            
            textureShader.bind();
             ((OpenGLShader) flatColorShader).uploadUniformInt("tex", 0);
        }

        @Override
        public void onDetach() {}

        @Override
        public void onUpdate(Timestep ts) {          
            if(Input.isKeyPressed(LX_KEY_LEFT))
                camPos.x -= camMoveSpeed * ts.getSeconds();
            else if(Input.isKeyPressed(LX_KEY_RIGHT))
                camPos.x += camMoveSpeed * ts.getSeconds();
            if(Input.isKeyPressed(LX_KEY_DOWN))
                camPos.y -= camMoveSpeed * ts.getSeconds();
            else if(Input.isKeyPressed(LX_KEY_UP))
                camPos.y += camMoveSpeed * ts.getSeconds();
            
            if(Input.isKeyPressed(LX_KEY_J))
                squarePos.x -= camMoveSpeed * ts.getSeconds();
            else if(Input.isKeyPressed(LX_KEY_L))
                squarePos.x += camMoveSpeed * ts.getSeconds();
            if(Input.isKeyPressed(LX_KEY_K))
                squarePos.y -= camMoveSpeed * ts.getSeconds();
            else if(Input.isKeyPressed(LX_KEY_I))
                squarePos.y += camMoveSpeed * ts.getSeconds();
            
            if(Input.isKeyPressed(LX_KEY_A))
                camRot += camRotSpeed * ts.getSeconds();
            else if(Input.isKeyPressed(LX_KEY_D))
                camRot -= camRotSpeed * ts.getSeconds();
            
            RenderCommand.setClearColor(new Vector4f(0.1f, 0.1f, 0.1f, 1));
            RenderCommand.clear();
                      
            camera.setPosition(camPos);
            camera.setRotation(camRot);
            
            Renderer.beginScene(camera);
            
            flatColorShader.bind();
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    Vector3f pos = new Vector3f(i * 0.11f, j * 0.11f, 0);
                    ((OpenGLShader) flatColorShader).uploadUniformVec3("col", new Vector3f(squareCol));
                    Renderer.submit(flatColorShader, squareVA, new Matrix4f().translate(pos).scale(0.1f));
                }
            }
           
            Renderer.submit(textureShader, squareVA, new Matrix4f().scale(1.5f));      
            
            Renderer.endScene();      
            //Renderer.flush();
        }

        @Override
        public void onImGuiRender() {
            ImGui.begin("Settings");
            ImGui.colorEdit3("SquareColor", squareCol);
            ImGui.end();
        }

        @Override
        public void onEvent(Event event) {
            //Event.EventDispatcher dispatcher = new Event.EventDispatcher(event);
            //dispatcher.dispatch(KeyPressedEvent.class, this::onKeyPressedEvent);
        }

        @Override
        public void dispose() {
            flatColorShader.dispose();
            squareVA.dispose();
        }
    }
    
    public Sandbox() { pushLayer(new ExampleLayer()); }
}
