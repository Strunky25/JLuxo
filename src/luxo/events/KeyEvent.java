package luxo.events;

public abstract class KeyEvent extends Event {
    
    protected int keyCode;
    
    protected KeyEvent(int keyCode) { this.keyCode = keyCode; }
    
    public int getKeyCode() { return this.keyCode; }
    
    @Override
    public int getCategoryFlags() {
        return Category.Keyoard.getFlag() | Category.Input.getFlag();
    }
    
    public static class KeyPressedEvent extends KeyEvent {
        
        private final int repeatCount;
        
        public KeyPressedEvent(int keyCode, int repeatCount) {
            super(keyCode);
            this.repeatCount = repeatCount;
        }
        
        public int getRepeatCount() { return this.repeatCount; }

        @Override
        public Type getType() { return Type.KeyPressed; }

        @Override
        public String getName() {
            return "KeyPressedEvent: " + keyCode + " (" + repeatCount + " repeats)";
        }        
    }
    
    public static class KeyReleasedEvent extends KeyEvent {
        
        public KeyReleasedEvent(int keyCode) { super(keyCode); }

        @Override
        public Type getType() { return Type.KeyReleased; }

        @Override
        public String getName() { return "KeyReleasedEvent: " + keyCode; }        
    }
}
