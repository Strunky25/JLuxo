package luxo.imgui;

import luxo.Application;
import luxo.events.Event;
import luxo.Layer;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import luxo.core.Timestep;
import org.lwjgl.glfw.GLFW;

public class ImGuiLayer extends Layer {
    
    private final ImGuiImplGlfw implGLFW;
    private final ImGuiImplGl3 implOpenGL;
    private final long window;
    
    public ImGuiLayer(long window) { 
        super("ImGuiLayer");
        this.window = window;
        implGLFW = new ImGuiImplGlfw();
        implOpenGL = new ImGuiImplGl3();
    }

    @Override
    public void onAttach() {
        ImGui.createContext();
        ImGui.styleColorsDark();
        
        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename(null);                                // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);  // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);      // Enable Docking
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);    // Enable Multi-Viewport / Platform Windows
        io.setConfigViewportsNoTaskBarIcon(true);
        
        implGLFW.init(window, false);
        implOpenGL.init("#version 410");
    }

    @Override
    public void onDetach() {
        implOpenGL.dispose();
        implGLFW.dispose();
        //ImGui.destroyContext();
    }

    @Override
    public void onUpdate(Timestep ts) {}
    
    public void begin() {
        /* Start Frame */
        implGLFW.newFrame();
        ImGui.newFrame();
    }
    
    public void end() {
        /* End Frame */
        ImGuiIO io = ImGui.getIO();
        Application app = Application.get();
        io.setDisplaySize(app.getWindow().getWidth(), app.getWindow().getHeight());
        
        ImGui.render();
        implOpenGL.renderDrawData(ImGui.getDrawData());
        
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }
    
    @Override
    public void onImGuiRender() {
        //ImGui.showDemoWindow();
    }

    @Override
    public void onEvent(Event event) {}

    @Override
    public void dispose() {
        onDetach();
    }
}