/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package luxo.imgui;

import luxo.Layer;
import luxo.events.Event;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

/**
 *
 * @author elsho
 */
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
        
        implGLFW.init(window, true);
        GL.createCapabilities();
        implOpenGL.init("#version 410");
    }

    @Override
    public void onDetach() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onUpdate() {
        /* Start Frame */
        implGLFW.newFrame();
        ImGui.newFrame();
        
        /* Process */
        ImGui.showDemoWindow();
        
        /* End Frame */
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
    public void onEvent(Event event) {
        
    }
    
    
}
