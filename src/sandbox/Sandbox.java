package sandbox;

import luxo.Application;
import luxo.Input;
import luxo.Layer;
import luxo.events.Event;
import luxo.renderer.*;
import static luxo.KeyCode.*;
import luxo.Log;
import luxo.core.Timestep;

import org.joml.Vector3f;
import org.joml.Vector4f;


public class Sandbox extends Application {
    
    public static class ExampleLayer extends Layer {
        
        private Shader shader, blueShader;
        private VertexArray vertexArray, squareVA;
        private OrthoCamera camera;
        private Vector3f camPos;
        private float camMoveSpeed, camRotSpeed, camRot;

        @Override
        public void onAttach() {
            vertexArray = VertexArray.create();
            camMoveSpeed = 1f;
            camRotSpeed = 3f;
            camPos = new Vector3f(0);
            camera = new OrthoCamera(-1.6f, 1.6f, -0.9f, 0.9f);

            float vertices[] = {
                /*  position    */  /*  Color            */ 
                -0.5f, -0.5f, 0.0f, 0.8f, 0.2f, 0.8f, 1.0f,
                 0.5f, -0.5f, 0.0f, 0.2f, 0.3f, 0.8f, 1.0f,
                 0.0f,  0.5f, 0.0f, 0.8f, 0.8f, 0.2f, 1.0f,
            };
            Buffer.VertexBuffer vertexBuffer = Buffer.VertexBuffer.create(vertices);
            Buffer.BufferLayout layout = new Buffer.BufferLayout(
                new Buffer.BufferElement(Buffer.ShaderDataType.FLOAT3, "position"),
                new Buffer.BufferElement(Buffer.ShaderDataType.FLOAT4, "color")
            );

            vertexBuffer.setLayout(layout);
            vertexArray.addVertexBuffer(vertexBuffer);

            int indices[] = {0, 1, 2};
            Buffer.IndexBuffer indexBuffer = Buffer.IndexBuffer.create(indices);
            vertexArray.setIndexBuffer(indexBuffer);

            float[] squareVertices = {
                /*  position    */
                -0.75f, -0.75f, 0.0f,
                 0.75f, -0.75f, 0.0f,
                 0.75f,  0.75f, 0.0f,
                -0.75f,  0.75f, 0.0f,
            };

            squareVA = VertexArray.create();
            Buffer.VertexBuffer squareVB = Buffer.VertexBuffer.create(squareVertices);
            squareVB.setLayout(new Buffer.BufferLayout(
                new Buffer.BufferElement(Buffer.ShaderDataType.FLOAT3, "position")
            ));
            squareVA.addVertexBuffer(squareVB);
            int squareIndices[] = {0, 1, 2, 2, 3, 0};
            Buffer.IndexBuffer squareIB = Buffer.IndexBuffer.create(squareIndices);
            squareVA.setIndexBuffer(squareIB);

            String vertexSource =  
                """
                #version 330 core

                layout(location = 0) in vec3 position;
                layout(location = 1) in vec4 color;

                uniform mat4 viewProjection;

                out vec3 v_pos;
                out vec4 v_col;

                void main() {
                    gl_Position = viewProjection * vec4(position, 1.0);
                    v_pos = position;
                    v_col = color;
                }
                """;
            String fragmentSource = 
                """
                #version 330 core

                in vec3 v_pos;
                in vec4 v_col;
                out vec4 color;

                void main() {
                    color = v_col;
                }
                """;
            shader = new Shader(vertexSource, fragmentSource);
            String blueVertexSource =  
                """
                #version 330 core

                uniform mat4 viewProjection;

                layout(location = 0) in vec3 position;

                out vec3 v_pos;

                void main() {
                    gl_Position = viewProjection * vec4(position, 1.0);
                    v_pos = position;
                }
                """;
            String blueFragmentSource = 
                """
                #version 330 core

                in vec3 v_pos;
                out vec4 color;

                void main() {
                    color = vec4(0.2, 0.3, 0.8, 1.0);
                }
                """;
            blueShader = new Shader(blueVertexSource, blueFragmentSource);
        }

        @Override
        public void onDetach() {
        }

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
            if(Input.isKeyPressed(LX_KEY_A))
                camRot += camRotSpeed * ts.getSeconds();
            else if(Input.isKeyPressed(LX_KEY_D))
                camRot -= camRotSpeed * ts.getSeconds();
            
            RenderCommand.setClearColor(new Vector4f(0.1f, 0.1f, 0.1f, 1));
            RenderCommand.clear();
                      
            camera.setPosition(camPos);
            camera.setRotation(camRot);
            
            Renderer.beginScene(camera);
            
            Renderer.submit(blueShader, squareVA);
            Renderer.submit(shader, vertexArray);      
            
            Renderer.endScene();      
            //Renderer.flush();
        }

        @Override
        public void onImGuiRender() {
        }

        @Override
        public void onEvent(Event event) {
            //Event.EventDispatcher dispatcher = new Event.EventDispatcher(event);
            //dispatcher.dispatch(KeyPressedEvent.class, this::onKeyPressedEvent);
        }

        @Override
        public void dispose() {
            shader.dispose();
            blueShader.dispose();
            vertexArray.dispose();
            squareVA.dispose();
        }
    }
    
    public Sandbox() {
        pushLayer(new ExampleLayer());
    }
    
}
