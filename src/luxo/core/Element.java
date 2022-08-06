/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package luxo.core;

import luxo.events.Event;

/**
 *
 * @author elsho
 */
public interface Element {
    
    public void onUpdate(Timestep ts);
    public void onEvent(Event event);
    public void onImGuiRender();
}
