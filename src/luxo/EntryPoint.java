package luxo;

import sandbox.Sandbox;
import platform.windows.WindowsInput;

public class EntryPoint {


    public static void main(String[] args) {    
        /* Initialization */
        WindowsInput.loadInstance();
        Log.coreWarn("Initialized Log!");
        
        Application app = new Sandbox();
        app.run();
        app.dispose();
    }
    
}