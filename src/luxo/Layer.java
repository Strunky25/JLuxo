/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luxo;

import luxo.events.Event;

/**
 *
 * @author elsho
 */
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
