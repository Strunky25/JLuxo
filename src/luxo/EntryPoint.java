/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package luxo;

import platform.windows.WindowsInput;
import sandbox.Sandbox;

/**
 *
 * @author elsho
 */
public class EntryPoint {


    public static void main(String[] args) {    
        /* Initialization */
        WindowsInput.loadInstance();
        Log.coreWarn("Initialized Log!");
        
        Application app = new Sandbox();
        app.run();
    }
    
}
