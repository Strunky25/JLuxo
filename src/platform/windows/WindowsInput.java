package platform.windows;

import luxo.core.Application;
import luxo.core.Input;
import static org.lwjgl.glfw.GLFW.*;

public class WindowsInput extends Input {
    
    public static void loadInstance() {
        Input.instance = new WindowsInput();
    }
    
    @Override
    protected boolean isKeyPressedImpl(int keycode) {
        long window = Application.get().getWindow().getPointer();
        int state = glfwGetKey(window, keycode);
        return state == GLFW_PRESS || state == GLFW_REPEAT;
    }

    @Override
    protected boolean isMouseButtonPressedImpl(int button) {
        long window = Application.get().getWindow().getPointer();
        int state = glfwGetMouseButton(window, button);
        return state == GLFW_PRESS;
    }

    @Override
    protected float[] getMousePosImpl() {
        long window = Application.get().getWindow().getPointer();
        double[] xpos = new double[1];
        double[] ypos = new double[1];
        glfwGetCursorPos(window, xpos, ypos);
        return new float[] {(float) xpos[0], (float) ypos[0]};
    }
}