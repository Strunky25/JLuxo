package sandbox;

import luxo.core.Layer;
import luxo.core.Timestep;
import luxo.events.Event;
import luxo.renderer.OrthoCameraController;
import luxo.renderer.RenderCommand;
import luxo.renderer.Renderer2D;
import imgui.ImGui;
import luxo.renderer.Texture.Texture2D;
import org.joml.*;


public class Sandbox2D extends Layer {
    
    private OrthoCameraController cameraController;
    private Texture2D texture;
    private float[] squareColor;
    
    public Sandbox2D() { super("Sandbox2D"); }

    @Override
    public void onAttach() {
        cameraController = new OrthoCameraController(16f / 9, true);
        texture = Texture2D.create("assets/textures/Checkerboard.png");
        squareColor = new float[]{0.8f, 0.2f, 0.3f, 1};
    }

    @Override
    public void onDetach() {
        Renderer2D.shutdown();
    }

    @Override
    public void onUpdate(Timestep ts) {
        cameraController.onUpdate(ts);
        
        RenderCommand.setClearColor(new Vector4f(0.1f, 0.1f, 0.1f, 1));
        RenderCommand.clear();       

        Renderer2D.beginScene(cameraController.getCamera());  
        Renderer2D.drawQuad(new Vector2f(-1, 0), new Vector2f(0.8f, 0.8f), new Vector4f(squareColor));
        Renderer2D.drawQuad(new Vector2f(0.5f, -0.5f), new Vector2f(0.5f, 0.75f), new Vector4f(0.2f, 0.3f, 0.8f, 1 ));
        Renderer2D.drawQuad(new Vector3f(0.2f, 0.5f, -0.1f), new Vector2f(10f, 10f), texture);
        Renderer2D.endScene(); 
    }

    @Override
    public void onEvent(Event event) { cameraController.onEvent(event); }

    @Override
    public void onImGuiRender() {
        ImGui.begin("Settings");
        ImGui.colorEdit4("SquareColor", squareColor);
        ImGui.text("Application average %.3f ms/frame (%.1f FPS)".formatted(1000 / ImGui.getIO().getFramerate(), ImGui.getIO().getFramerate()));
        ImGui.end();
    }
}
