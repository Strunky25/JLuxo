/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luxo;

import luxo.Window.WindowProperties;
import luxo.events.ApplicationEvent.WindowClosedEvent;
import luxo.events.Event;
import org.lwjgl.opengl.GL;
import platform.windows.WindowsWindow;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author elsho
 */
public abstract class Application implements Runnable {
    
    private static Application app;
    
    private boolean running;
    protected final Window window;
    
    public Application() {
        assert app == null : "Application already exists!";
        app = this;
        running = true;
        window = new WindowsWindow(new WindowProperties());
        window.setEventCallback(this::onEvent);
    }
    
    @Override
    public void run() {
        GL.createCapabilities();
        glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
        while (running) {
            glClear(GL_COLOR_BUFFER_BIT);
            window.onUpdate();
        }
    }  
    
    public void onEvent(Event event){
        Log.coreInfo(event);
    }
    
    
    public Window getWindow() { return this.window; }
    
    public static Application get() {
        return app;
    }
}
