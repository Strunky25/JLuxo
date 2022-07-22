/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sandbox;

import luxo.Application;
import luxo.Layer;
import luxo.Log;
import luxo.events.Event;

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
            Log.info("ExampleLayer::Update");
        }

        @Override
        public void onEvent(Event event) {
            Log.trace(event);
        }
        
    }
    
    public Sandbox() {
        super();
        pushLayer(new ExampleLayer());
    }
    
    
}
