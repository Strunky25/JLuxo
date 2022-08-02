package luxo.events;

public abstract class Event {
    
    public boolean handled;
    
    public Event() { handled = false; }
    
    public Type getType() { return Type.None; }
    
    public abstract String getName();
    
    public int getCategoryFlags() { return Category.None.getFlag(); }
    
    public boolean isInCategory(Category category){
        return (getCategoryFlags() & category.getFlag()) == category.getFlag();
    }

    @Override
    public String toString() {
        return getName();
    }    
    
    public enum Type {
        None,
        WindowClosed, WindowResized, WindowFocused, WindowLostFocus, WindowMoved,
        AppTick, AppUpdate, AppRender,
        KeyPressed, KeyReleased,
        MouseButtonPressed, MouseButtonReleased, MouseMoved, MouseScrolled
    }
    
    public enum Category {
        None(BIT(0)),
        Application(BIT(1)),
        Input(BIT(2)),
        Keyoard(BIT(3)),
        Mouse(BIT(4)),
        MouseButton(BIT(5));
        
        private final int bit;
        
        Category(int bit) { this.bit = bit;}
        
        public int getFlag() { return bit; }
    }
    
    private static int BIT(int pos) { return (1 << pos); }
    
    public interface EventCallback {
        public void callback(Event event);
    }
    
    public interface EventHandler {
        public <T extends Event> boolean callback(T event);
    }
    
    public static class EventDispatcher {
        
        private final Event event;
        
        public EventDispatcher(Event event) { this.event = event; }
        
        public <T extends Event> void dispatch(Class<T> t, EventHandler handler){
            if(t.isAssignableFrom(event.getClass())){
                event.handled = handler.callback(event);
            }
        }
    }
}
