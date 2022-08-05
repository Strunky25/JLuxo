package luxo.events;

public abstract class ApplicationEvent extends Event {
    
    @Override
    public int getCategoryFlags() { return Category.Application.getFlag(); }
    
    public static class WindowResizedEvent extends ApplicationEvent {

        private final int width, height;
        
        public WindowResizedEvent(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public Type getType() { return Type.WindowResized; }
      
        @Override
        public String getName() {
            return "WindowResizedEvent: " + width + ", " + height;
        }
        
        public int getWidth() { return this.width; }
        public int getHeight() { return this.height; }
    }
    
    public static class WindowClosedEvent extends ApplicationEvent {
        
        @Override
        public ApplicationEvent.Type getType() {
            return ApplicationEvent.Type.WindowClosed;
        }
      
        @Override
        public String getName() { return "WindowClosedEvent"; }    
    }
    
    public static class AppTickEvent extends ApplicationEvent {
        
        @Override
        public ApplicationEvent.Type getType() {
            return ApplicationEvent.Type.AppTick;
        }
      
        @Override
        public String getName() { return "AppTickEvent"; }    
    }
    
    public static class AppUpdateEvent extends ApplicationEvent {
        
        @Override
        public ApplicationEvent.Type getType() {
            return ApplicationEvent.Type.AppUpdate;
        }
      
        @Override
        public String getName() { return "AppUpdateEvent"; }    
    }
    
    public static class AppRenderEvent extends ApplicationEvent {
        
        @Override
        public ApplicationEvent.Type getType() {
            return ApplicationEvent.Type.AppRender;
        }
      
        @Override
        public String getName() { return "AppRenderEvent"; }    
    }
}

