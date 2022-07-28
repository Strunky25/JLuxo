package luxo;

import luxo.Window.WindowProperties;
import luxo.events.ApplicationEvent.WindowClosedEvent;
import luxo.events.Event;
import luxo.imgui.ImGuiLayer;
import luxo.renderer.Buffer;
import luxo.renderer.Buffer.BufferElement;
import luxo.renderer.Buffer.BufferLayout;
import luxo.renderer.Buffer.IndexBuffer;
import luxo.renderer.Buffer.ShaderDataType;
import luxo.renderer.Buffer.VertexBuffer;
import luxo.renderer.Shader;
import platform.windows.WindowsWindow;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL41C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class Application implements Runnable {
    
    private static Application app;
    
    protected final Window window;
    private final ImGuiLayer imGuiLayer;
    private final LayerStack layerStack;
    private boolean running;
    private final int vertexArray;
    private final Shader shader;
    private final VertexBuffer vertexBuffer;
    private final IndexBuffer indexBuffer;
    
    protected Application() {
        Log.coreAssert(app == null, "Application already exists!");
        app = this;
        
        running = true;
        
        window = new WindowsWindow(new WindowProperties());
        window.setEventCallback(this::onEvent);
        
        layerStack = new LayerStack();
        imGuiLayer = new ImGuiLayer(window.getPointer());
        pushOverlay(imGuiLayer);
        
        vertexArray = glGenVertexArrays();
        glBindVertexArray(vertexArray);

        float vertices[] = {
            -0.5f, -0.5f, 0.0f, 0.8f, 0.2f, 0.8f, 1.0f,
             0.5f, -0.5f, 0.0f, 0.2f, 0.3f, 0.8f, 1.0f,
             0.0f,  0.5f, 0.0f, 0.8f, 0.8f, 0.2f, 1.0f,
        };
        vertexBuffer = VertexBuffer.create(vertices);
        
        {
            BufferLayout layout = new BufferLayout(
                new BufferElement(ShaderDataType.FLOAT3, "position"),
                new BufferElement(ShaderDataType.FLOAT4, "color")
            );
            vertexBuffer.setLayout(layout);
        } 
        
        int index = 0;
        BufferLayout layout = vertexBuffer.getLayout();
        for(BufferElement element : layout) {
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(index, 
                element.getComponentCount(),
                element.type.toOpenGL(),
                element.normalized,
                layout.getStride(),
                element.offset);
            index++;
        }
                
        int indices[] = {0, 1, 2};
        indexBuffer = IndexBuffer.create(indices);
        
        String vertexSource =  
            """
            #version 330 core
            
            layout(location = 0) in vec3 position;
            layout(location = 1) in vec4 color;
            
            out vec3 v_pos;
            out vec4 v_col;
            
            void main() {
                gl_Position = vec4(position, 1.0);
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
    }
    
    @Override
    public void run() {
        GL.createCapabilities();
        glClearColor(0.1f, 0.1f, 0.1f, 1);
        while (running) {
            glClear(GL_COLOR_BUFFER_BIT);
            
            shader.bind();
            glBindVertexArray(vertexArray);
            glDrawElements(GL_TRIANGLES, indexBuffer.getCount(), GL_UNSIGNED_INT, NULL);
            
            layerStack.onUpdate();
            
            imGuiLayer.begin();
            layerStack.onImGuiRender();
            imGuiLayer.end();
            
            window.onUpdate();
        }
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
    
    public Window getWindow() { return this.window; }
    
    public static Application get() {
        return app;
    }
}
