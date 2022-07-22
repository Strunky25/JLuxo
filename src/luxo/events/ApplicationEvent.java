/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luxo.events;

/**
 *
 * @author elsho
 */
public abstract class ApplicationEvent extends Event {
    
    @Override
    public int getCategoryFlags() {
        return Category.Application.getFlag();
    }
    
    public static class WindowResizedEvent extends ApplicationEvent {

        private final int width, height;
        
        public WindowResizedEvent(int width, int height) {
            super();
            this.width = width;
            this.height = height;
        }

        @Override
        public Type getType() {
            return Type.WindowResized;
        }
      
        @Override
        public String getName() {
            return "WindowResizedEvent: " + width + ", " + height;
        }    
    }
    
    public static class WindowClosedEvent extends ApplicationEvent {
        
        public WindowClosedEvent() {
            super();
        }

        @Override
        public ApplicationEvent.Type getType() {
            return ApplicationEvent.Type.WindowClosed;
        }
      
        @Override
        public String getName() {
            return "WindowClosedEvent";
        }    
    }
    
    public static class AppTickEvent extends ApplicationEvent {
        
        public AppTickEvent() {
            super();
        }

        @Override
        public ApplicationEvent.Type getType() {
            return ApplicationEvent.Type.AppTick;
        }
      
        @Override
        public String getName() {
            return "AppTickEvent";
        }    
    }
    
    public static class AppUpdateEvent extends ApplicationEvent {
        
        public AppUpdateEvent() {
            super();
        }

        @Override
        public ApplicationEvent.Type getType() {
            return ApplicationEvent.Type.AppUpdate;
        }
      
        @Override
        public String getName() {
            return "AppUpdateEvent";
        }    
    }
    
    public static class AppRenderEvent extends ApplicationEvent {
        
        public AppRenderEvent() {
            super();
        }

        @Override
        public ApplicationEvent.Type getType() {
            return ApplicationEvent.Type.AppRender;
        }
      
        @Override
        public String getName() {
            return "AppRenderEvent";
        }    
    }
}

