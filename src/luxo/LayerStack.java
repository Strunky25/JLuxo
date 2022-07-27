/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luxo;

import java.util.ArrayList;
import luxo.events.Event;

/**
 *
 * @author elsho
 */
public class LayerStack {
    
    private final ArrayList<Layer> layers;
    private int layerInsert;
    
    public LayerStack() {
        this.layers = new ArrayList<>();
        this.layerInsert = 0;
    }
    
    public void pushLayer(Layer layer) {
        layers.add(layerInsert, layer);
        layerInsert++;
    }
    public void pushOverlay(Layer overlay) { layers.add(overlay); }
    public void popLayer(Layer layer) {
        layers.remove(layer);
        layerInsert--;
    }
    public void popOverlay(Layer overlay) { layers.remove(overlay); }
    
    public void onUpdate() { layers.forEach(((layer) -> layer.onUpdate())); }
    
    public void onImGuiRender() { layers.forEach(((layer) -> layer.onImGuiRender())); } 

    void onEvent(Event event) {
        for(Layer layer: layers) {
            layer.onEvent(event);
            if(event.handled) break;
        }
    }
}
