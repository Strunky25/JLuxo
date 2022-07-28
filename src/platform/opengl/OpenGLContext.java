package platform.opengl;

import luxo.Log;
import luxo.renderer.GraphicsContext;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL41C.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGLContext extends GraphicsContext {
    
    private final long windowHandle;
    
    public OpenGLContext(long windowHandle) {
        Log.coreAssert(windowHandle != NULL , "Window handle is Null!");
        this.windowHandle = windowHandle;
    }

    @Override
    public void init() {
        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();
        Log.coreInfo("OpenGL Initialized:"); 
        Log.coreInfo("  Renderer: %s", glGetString(GL_RENDERER));
        Log.coreInfo("  Version: %s", glGetString(GL_VERSION));
    }

    @Override
    public void swapBuffers() {
        glfwSwapBuffers(windowHandle);
    }

}
