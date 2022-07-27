/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sandbox;

import luxo.Application;
import luxo.Input;
import static luxo.KeyCode.LX_KEY_TAB;
import luxo.Layer;
import luxo.Log;
import luxo.events.Event;
import luxo.events.KeyEvent.KeyPressedEvent;

/**
 *
 * @author elsho
 */
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
