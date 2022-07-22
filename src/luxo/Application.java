
package luxo;


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
        while (running) {}
    }  
    
    
    public static Application get() {
        return app;
    }
}
