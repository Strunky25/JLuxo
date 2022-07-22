/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package platform.windows;

import luxo.Log;
import luxo.Window;
import luxo.events.ApplicationEvent.*;
import luxo.events.KeyEvent;
import luxo.events.KeyEvent.*;
import luxo.events.MouseEvent.*;
import org.lwjgl.glfw.*;
import org.lwjgl.system.jni.JNINativeInterface;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 *
 * @author elsho
 */
public class WindowsWindow extends Window {
    
    private static boolean GLFWInitialized = false;
    
    private long window;
    private final WindowData data;
    
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
            assert success : "Could not initialize GLFW!";
            GLFWInitialized = true;
        }
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        window = glfwCreateWindow(data.width, data.height, data.title, NULL, NULL);
        assert window != NULL : "Failed to create GLFW Window!";
        glfwMakeContextCurrent(window);
        long dataPointer = JNINativeInterface.NewGlobalRef(data);
        glfwSetWindowUserPointer(window, dataPointer);
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
        glfwSwapBuffers(window);
    }

    @Override
    public int getWidth() { return this.data.width; }

    @Override
    public int getHeight() { return this.data.height; }

    @Override
    public void setEventCallback(EventCallback callback) { data.eventCallback = callback; }

    @Override
    public void setVSync(boolean enabled) { this.data.VSync = enabled; }

    @Override
    public boolean isVSync() { return this.data.VSync; }

    @Override
    public Window create(WindowProperties properties) {
        return new WindowsWindow(properties);
    }
    
    public void dispose() {
        
    }
    
    private static void GLFWErrorCallback(int error, long description) {
        Log.coreError("GLFW Error (%d): %s", error, description);
    }
    
    @Override
    public long getPointer() {
        return this.window;
    }
}
