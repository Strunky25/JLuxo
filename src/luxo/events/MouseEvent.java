package Luxo.Events;

public abstract class MouseEvent extends Event {

    @Override
    public int getCategoryFlags() {
        return Category.Mouse.getFlag() | Category.Input.getFlag();
    }
    
    public static class MouseMovedEvent extends MouseEvent {
        
        private final float mouseX, mouseY;
        
        public MouseMovedEvent(float x, float y) {
            super();
            this.mouseX = x;
            this.mouseY = y;
        }
        
        public float getX() { return mouseX; }
        public float getY() { return mouseY; }

        @Override
        public Type getType() {
            return Type.MouseMoved;
        }   
        
        @Override
        public String getName() {
            return "MouseMovedEvent: " + mouseX + ", " + mouseY;
        }
    }
    
    public static class MouseScrolledEvent extends MouseEvent {
        
        private final float xOffset, yOffset;
        
        public MouseScrolledEvent(float xOffset, float yOffset) {
            super();
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }
        
        public float getXOffset() { return xOffset; }
        public float getYOffset() { return yOffset; }

        @Override
        public Type getType() {
            return Type.MouseScrolled;
        }   
        
        @Override
        public String getName() {
            return "MouseScrolledEvent: " + xOffset + ", " + yOffset;
        }
        
    }
    
    public static abstract class MouseButtonEvent extends MouseEvent {

        protected int button;
        
        public MouseButtonEvent(int button) { this.button = button; }

        @Override
        public int getCategoryFlags() {
            return Category.MouseButton.getFlag() | Category.Mouse.getFlag() | Category.Input.getFlag();
        }

        public int getButton() { return this.button; }
    }

    public static class MouseButtonPressedEvent extends MouseButtonEvent {

        public MouseButtonPressedEvent(int button) { super(button); }

        @Override
        public Type getType() {
            return Type.MouseButtonPressed;
        }

        @Override
        public String getName() {
            return "MouseButtonPressedEvent: " + button;
        }
    }

    public static class MouseButtonReleasedEvent extends MouseButtonEvent {

        public MouseButtonReleasedEvent(int button) { super(button); }

        @Override
        public Type getType() {
            return Type.MouseButtonPressed;
        }

        @Override
        public String getName() {
            return "MouseButtonReleasedEvent: " + button;
        }
    }
}
