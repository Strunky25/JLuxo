package luxo.core;

public abstract class Input {
    
    protected static Input instance;
    
    public static boolean isKeyPressed(int keycode) {
        return instance.isKeyPressedImpl(keycode);
    }
    
    public static boolean isMouseButtonPressed(int button) { 
        return instance.isMouseButtonPressedImpl(button);
    }
    
    public static float[] getMousePos() {
        return instance.getMousePosImpl();
    }
    
    protected abstract boolean isKeyPressedImpl(int keycode);
    protected abstract boolean isMouseButtonPressedImpl(int button);
    protected abstract float[] getMousePosImpl();
}
