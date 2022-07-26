package luxo.core;

import luxo.events.Event;
import java.util.ArrayList;
import luxo.core.Element;
import luxo.core.Timestep;

public class LayerStack implements Element {
    
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
    
    @Override
    public void onUpdate(Timestep ts) { layers.forEach(((layer) -> layer.onUpdate(ts))); }
    
    @Override
    public void onImGuiRender() { layers.forEach(((layer) -> layer.onImGuiRender())); } 

    @Override
    public void onEvent(Event event) {
        for(Layer layer: layers) {
            layer.onEvent(event);
            if(event.handled) break;
        }
    }
    
    public void dispose() { layers.forEach(((layer) -> layer.onDetach())); }
}
