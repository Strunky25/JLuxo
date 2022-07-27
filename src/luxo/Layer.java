package luxo;

import luxo.events.Event;

public abstract class Layer {
    
    private final String debugName;
    
    public Layer(){ debugName = "layer"; }
    
    public Layer(String debugName) { this.debugName = debugName; }
    
    public abstract void onAttach();
    public abstract void onDetach();
    public abstract void onUpdate();
    public abstract void onImGuiRender();
    public abstract void onEvent(Event event);
    
    public final String getName() { return this.debugName; }
}
