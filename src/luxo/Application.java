
package luxo;

import luxo.events.ApplicationEvent.*;


public abstract class Application implements Runnable {
    
    /* Static Variables */
    private static Application app;
   
    /* Member Variables */
    private final boolean running;

 
    public Application() {
        assert app == null : "Application already exists!";
        app = this;
        running = true;
    }
    
    @Override
    public void run() {
        WindowResizedEvent event = new WindowResizedEvent(1280, 720);
        Log.trace(event);
        while (running) {}
    }  
    
    
    public static Application get() {
        return app;
    }
}
