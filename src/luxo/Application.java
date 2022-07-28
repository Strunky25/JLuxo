package luxo;

import luxo.Window.WindowProperties;
import luxo.events.ApplicationEvent.WindowClosedEvent;
import luxo.events.Event;
import luxo.imgui.ImGuiLayer;
import platform.windows.WindowsWindow;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL41C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class Application implements Runnable {
    
    private static Application app;
    
    protected final Window window;
    private final ImGuiLayer imGuiLayer;
    private final LayerStack layerStack;
    private int vertexArray, vertexBuffer, indexBuffer;
    private boolean running;
    
    public Application() {
        assert app == null : "Application already exists!";
        app = this;
        
        running = true;
        
        window = new WindowsWindow(new WindowProperties());
        window.setEventCallback(this::onEvent);
        
        layerStack = new LayerStack();
        imGuiLayer = new ImGuiLayer(window.getPointer());
        pushOverlay(imGuiLayer);
        
        vertexArray = glGenVertexArrays();
        glBindVertexArray(vertexArray);
        
        vertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        
        float vertices[] = {
            -0.5f, -0.5f, 0.0f,
             0.5f, -0.5f, 0.0f,
             0.0f,  0.5f, 0.0f,
        };
        
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, NULL);
        
        indexBuffer = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
        
        int indices[] = {0, 1, 2};
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }
    
    @Override
    public void run() {
        GL.createCapabilities();
        glClearColor(0.1f, 0.1f, 0.1f, 1);
        while (running) {
            glClear(GL_COLOR_BUFFER_BIT);
            
            glBindVertexArray(vertexArray);
            glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_INT, NULL);
            
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
