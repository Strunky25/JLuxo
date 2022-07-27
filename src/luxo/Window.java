package Luxo;

import Luxo.Events.Event;

public abstract class Window {
    
    public static class WindowProperties {
        public String title;
        public int width;
        public int height;
        
        public WindowProperties(){
            this.title = "Luxo Engine";
            this.width = 1280;
            this.height = 720;
        }
        
        public WindowProperties(String title, int width, int height){
            this.title = title;
            this.width = width;
            this.height = height;
        }
    }
    
    public abstract void onUpdate();
    
    public abstract int getWidth();
    public abstract int getHeight();
    
    public abstract void setEventCallback(EventCallback callback);
    public abstract void setVSync(boolean enabled);
    public abstract boolean isVSync();
    
    public abstract Window create(final WindowProperties properties);
    
    public interface EventCallback {
        public void callback(Event event);
    }
    
    public abstract long getPointer();
}

