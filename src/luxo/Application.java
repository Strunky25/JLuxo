package luxo;

import glm_.vec3.Vec3;
import glm_.vec4.Vec4;
import luxo.Window.WindowProperties;
import luxo.events.ApplicationEvent.WindowClosedEvent;
import luxo.events.Event;
import luxo.imgui.ImGuiLayer;
import luxo.renderer.*;
import luxo.renderer.Buffer.*;
import platform.windows.WindowsWindow;


public abstract class Application implements Runnable {
    
    private static Application app;
    
    protected final Window window;
    private final ImGuiLayer imGuiLayer;
    private final LayerStack layerStack;
    private final Shader shader, blueShader;
    private final VertexArray vertexArray, squareVA;
    private boolean running;
    private OrthoCamera camera;
    
    protected Application() {
        Log.coreAssert(app == null, "Application already exists!");
        app = this;
        
        running = true;
        
        window = new WindowsWindow(new WindowProperties());
        window.setEventCallback(this::onEvent);
        
        layerStack = new LayerStack();
        imGuiLayer = new ImGuiLayer(window.getPointer());
        pushOverlay(imGuiLayer);
        
        vertexArray = VertexArray.create();
        
        camera = new OrthoCamera(-1.6f, 1.6f, -0.9f, 0.9f);

        float vertices[] = {
            /*  position    */  /*  Color            */ 
            -0.5f, -0.5f, 0.0f, 0.8f, 0.2f, 0.8f, 1.0f,
             0.5f, -0.5f, 0.0f, 0.2f, 0.3f, 0.8f, 1.0f,
             0.0f,  0.5f, 0.0f, 0.8f, 0.8f, 0.2f, 1.0f,
        };
        VertexBuffer vertexBuffer = VertexBuffer.create(vertices);
        BufferLayout layout = new BufferLayout(
            new BufferElement(ShaderDataType.FLOAT3, "position"),
            new BufferElement(ShaderDataType.FLOAT4, "color")
        );
        
        vertexBuffer.setLayout(layout);
        vertexArray.addVertexBuffer(vertexBuffer);
                
        int indices[] = {0, 1, 2};
        IndexBuffer indexBuffer = IndexBuffer.create(indices);
        vertexArray.setIndexBuffer(indexBuffer);
        
        float[] squareVertices = {
            /*  position    */
            -0.75f, -0.75f, 0.0f,
             0.75f, -0.75f, 0.0f,
             0.75f,  0.75f, 0.0f,
            -0.75f,  0.75f, 0.0f,
        };
        
        squareVA = VertexArray.create();
        VertexBuffer squareVB = VertexBuffer.create(squareVertices);
        squareVB.setLayout(new BufferLayout(
            new BufferElement(ShaderDataType.FLOAT3, "position")
        ));
        squareVA.addVertexBuffer(squareVB);
        int squareIndices[] = {0, 1, 2, 2, 3, 0};
        IndexBuffer squareIB = IndexBuffer.create(squareIndices);
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
    public void run() {
        camera.setPosition(new Vec3(0.5f, 0.5f, 0));
        camera.setRotation(45);
        while (running) {
            RenderCommand.setClearColor(new Vec4(0.1f, 0.1f, 0.1f, 1));
            RenderCommand.clear();
                      
            Renderer.beginScene(camera);
            
            Renderer.submit(blueShader, squareVA);
            Renderer.submit(shader, vertexArray);      
            
            Renderer.endScene();      
            Renderer.flush();
            
            layerStack.onUpdate();
            
            imGuiLayer.begin();
            //layerStack.onImGuiRender();
            imGuiLayer.end();
            
            window.onUpdate();
        }
        dispose();
    }  
    
    public void onEvent(Event event){
        if(event instanceof WindowClosedEvent) running = false;
        layerStack.onEvent(event);
    }
    
    public final void pushLayer(Layer layer){
        layerStack.pushLayer(layer);
        layer.onAttach();
    }
    
    public final void pushOverlay(Layer overlay){
        layerStack.pushLayer(overlay);
        overlay.onAttach();
    }

    public void dispose() {
        //imGuiLayer.dispose();
        //layerStack.dispose();
        shader.dispose();
        blueShader.dispose();
        vertexArray.dispose();
        squareVA.dispose();
        window.dispose();
    }
    
    public Window getWindow() { return this.window; }
    
    public static Application get() { return app; }
}
