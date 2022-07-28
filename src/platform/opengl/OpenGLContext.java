package platform.opengl;

import luxo.renderer.GraphicsContext;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGLContext extends GraphicsContext {
    
    private final long windowHandle;
    
    public OpenGLContext(long windowHandle) {
        assert windowHandle != NULL : "Window handle is Null!";
        this.windowHandle = windowHandle;
    }

    @Override
    public void init() {
        glfwMakeContextCurrent(windowHandle);
    }

    @Override
    public void swapBuffers() {
        glfwSwapBuffers(windowHandle);
    }

}
