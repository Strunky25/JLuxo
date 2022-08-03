package luxo;

import luxo.core.Timestep;
import luxo.events.Event;

public abstract class Layer {
    
    private final String debugName;
    
    public Layer(){ debugName = "layer"; }
    
    public Layer(String debugName) { this.debugName = debugName; }
    
    public abstract void onAttach();
    public abstract void onDetach();
    public abstract void onUpdate(Timestep ts);
    public abstract void onImGuiRender();
    public abstract void onEvent(Event event);
    public abstract void dispose();
    
    public final String getName() { return this.debugName; }
}
