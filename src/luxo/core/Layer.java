package luxo.core;

public abstract class Layer implements Element {
    
    private final String debugName;
    
    public Layer(){ debugName = "layer"; }
    
    public Layer(String debugName) { this.debugName = debugName; }
    
    public abstract void onAttach();
    public abstract void onDetach();
    
    public final String getName() { return this.debugName; }
}
