/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luxo;

import luxo.Window.WindowProperties;
import luxo.events.ApplicationEvent.WindowClosedEvent;
import luxo.events.Event;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import platform.windows.WindowsWindow;

/**
 *
 * @author elsho
 */
public abstract class Application implements Runnable {
    
    private static Application app;
    
    private boolean running;
    protected final Window window;
    private final LayerStack layerStack;
    
    public Application() {
        assert app == null : "Application already exists!";
        app = this;
        running = true;
        window = new WindowsWindow(new WindowProperties());
        window.setEventCallback(this::onEvent);
        layerStack = new LayerStack();
    }
    
    @Override
    public void run() {
        GL.createCapabilities();
        glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
        while (running) {
            glClear(GL_COLOR_BUFFER_BIT);
            layerStack.onUpdate();
            window.onUpdate();
        }
    }  
    
    public void onEvent(Event event){
        if(event instanceof WindowClosedEvent) running = false;
        layerStack.onEvent(event);
    }
    
    public void pushLayer(Layer layer){
        layerStack.pushLayer(layer);
        layer.onAttach();
    }
    
    public void pushOverlay(Layer overlay){
        layerStack.pushLayer(overlay);
        overlay.onAttach();
    }
    
    public Window getWindow() { return this.window; }
    
    public static Application get() {
        return app;
    }
}
