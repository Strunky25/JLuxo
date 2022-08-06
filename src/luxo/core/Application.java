package luxo.core;

import luxo.core.Window.WindowProperties;
import luxo.events.ApplicationEvent.*;
import luxo.events.Event;
import luxo.imgui.ImGuiLayer;
import luxo.renderer.Renderer;
import platform.windows.WindowsWindow;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public abstract class Application {
    
    private static Application app;
    
    protected final Window window;
    private final ImGuiLayer imGuiLayer;
    private final LayerStack layerStack;
    private float lastFrameTime;
    private boolean running, minimized;
    
    protected Application() {
        Log.coreAssert(app == null, "Application already exists!");
        app = this;
        
        running = true;
        
        window = new WindowsWindow(new WindowProperties());
        window.setVSync(false);
        window.setEventCallback(this::onEvent);
        
        Renderer.init();
        
        layerStack = new LayerStack();
        imGuiLayer = new ImGuiLayer(window.getPointer());
        pushOverlay(imGuiLayer);
    }

    public void run() {
        while (running) {      
            float time = (float) glfwGetTime();
            Timestep timestep = new Timestep(time - lastFrameTime);
            lastFrameTime = time;
            
            if(!minimized) {
                layerStack.onUpdate(timestep);
            }
            imGuiLayer.begin();
            layerStack.onImGuiRender();
            imGuiLayer.end();

            
            window.onUpdate();
        }
        dispose();
    }  
    
    public void onEvent(Event event){
        if(event instanceof WindowClosedEvent evt) 
            event.handled = onWindowClosed(evt);
        if(event instanceof WindowResizedEvent evt)
            event.handled = onWindowResized(evt);
        layerStack.onEvent(event);
    }
    
    public void dispose() {
        layerStack.dispose();
        window.dispose();
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
    
    public static Application get() { return app; }
    
    private boolean onWindowClosed(WindowClosedEvent evt) {
        this.running = false;
        return true;
    }
    
    private boolean onWindowResized(WindowResizedEvent evt) {
        if(evt.getWidth() == 0 || evt.getHeight() == 0) {
            this.minimized = true;
            return false;
        }
        this.minimized = false;
        Renderer.onWindowResized(evt.getWidth(), evt.getHeight());
        return false;
    }
}
