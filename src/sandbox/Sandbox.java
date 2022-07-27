package Sandbox;

import Luxo.Application;
import Luxo.Layer;
import Luxo.Events.Event;
import Luxo.Events.KeyEvent.KeyPressedEvent;
import Luxo.Input;
import Luxo.Log;
import static Luxo.KeyCode.*;

public class Sandbox extends Application {

    public static class ExampleLayer extends Layer {

        public ExampleLayer() {
            super("Example");
        }

        @Override
        public void onAttach() {}

        @Override
        public void onDetach() {}

        @Override
        public void onUpdate() {
            if(Input.isKeyPressed(LX_KEY_TAB))
                Log.info("Tab key is pressed!");
        }

        @Override
        public void onEvent(Event event) {
            if(event.getType() == Event.Type.KeyPressed) {
                KeyPressedEvent evt = (KeyPressedEvent) event;
                Log.trace((char) evt.getKeyCode());
            }
        }

        @Override
        public void onImGuiRender() {
            
        }
        
    }
    
    public Sandbox() {
        super();
        init();
    }
    
    private void init() {
        pushLayer(new ExampleLayer());
    }
    
    
}
