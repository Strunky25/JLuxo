package platform.windows;

import luxo.Log;
import luxo.Window;
import luxo.events.ApplicationEvent.*;
import luxo.events.Event.EventCallback;
import luxo.events.KeyEvent;
import luxo.events.KeyEvent.*;
import luxo.events.MouseEvent.*;
import luxo.renderer.GraphicsContext;
import platform.opengl.OpenGLContext;

import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowsWindow extends Window {
    
    private static boolean GLFWInitialized = false;
    
    private long window;
    private final WindowData data;
    private GraphicsContext context;
    
    public static class WindowData {
        String title;
        int width, height;
        boolean VSync;
        EventCallback eventCallback;
    }

    public WindowsWindow(WindowProperties properties) {
        data = new WindowData();
        init(properties);
    }   
    
    private void init(final WindowProperties properties) {
        data.title = properties.title;
        data.width = properties.width;
        data.height = properties.height;
        
        Log.coreInfo("Creating Window %s (%d, %d)", data.title, data.width, data.height);
        glfwSetErrorCallback(WindowsWindow::GLFWErrorCallback);
        if(!GLFWInitialized) {
            boolean success = GLFW.glfwInit();
            Log.coreAssert(success, "Could not initialize GLFW!");
            GLFWInitialized = true;
        }
        
        window = glfwCreateWindow(data.width, data.height, data.title, NULL, NULL);
        Log.coreAssert(window != NULL, "Failed to create GLFW Window!");
        
        context = new OpenGLContext(window);
        context.init();
        
        setVSync(true);
        
        glfwSetWindowSizeCallback(window, (long win, int width, int height) -> {
           WindowResizedEvent event = new WindowResizedEvent(width, height);
           data.width = width;
           data.height = height;
           data.eventCallback.callback(event);
        });
        
        glfwSetWindowCloseCallback(window, (long win) -> {
           WindowClosedEvent event = new WindowClosedEvent();
           data.eventCallback.callback(event);
        });
        
        glfwSetKeyCallback(window, (long win, int key, int scancode, int action, int modes) -> {
           KeyEvent event = null;
           switch(action){
               case GLFW_PRESS -> event = new KeyPressedEvent(key, 0);
               case GLFW_RELEASE -> event = new KeyReleasedEvent(key);
               case GLFW_REPEAT -> event = new KeyPressedEvent(key, 1);
           }
           data.eventCallback.callback(event);
        });
        
        glfwSetMouseButtonCallback(window, (long win, int button, int action, int modes) -> {
           MouseButtonEvent event = null;
           switch(action){
               case GLFW_PRESS -> event = new MouseButtonPressedEvent(button);
               case GLFW_RELEASE -> event = new MouseButtonReleasedEvent(button);
           }
           data.eventCallback.callback(event);
        });
        
        glfwSetScrollCallback(window, (long win, double xOffset, double yOffset) -> {
           MouseScrolledEvent event = new MouseScrolledEvent((float) xOffset, (float) yOffset);           
           data.eventCallback.callback(event);
        });
        
        glfwSetCursorPosCallback(window, (long win, double xPos, double yPos) -> {
           MouseMovedEvent event = new MouseMovedEvent((float) xPos, (float) yPos);           
           data.eventCallback.callback(event);
        });
    }
    
    @Override
    public void onUpdate() {
        glfwPollEvents();
        context.swapBuffers();
    }

    @Override
    public int getWidth() { return this.data.width; }

    @Override
    public int getHeight() { return this.data.height; }

    @Override
    public void setEventCallback(EventCallback callback) { data.eventCallback = callback; }

    @Override
    public void setVSync(boolean enabled) { 
        glfwSwapInterval(enabled ? 1 : 0);
        data.VSync = enabled;
    }

    @Override
    public boolean isVSync() { return this.data.VSync; }

    @Override
    public Window create(WindowProperties properties) {
        return new WindowsWindow(properties);
    }
    
    @Override
    public void dispose() {
        context.dispose();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        try { glfwSetErrorCallback(null).free();
        } catch (NullPointerException ex) {}
        glfwTerminate();
    }
    
    private static void GLFWErrorCallback(int error, long description) {
        Log.coreError("GLFW Error (%d): %s", error, GLFWErrorCallback.getDescription(description));
    }
    
    @Override
    public long getPointer() {
        return this.window;
    }
}
