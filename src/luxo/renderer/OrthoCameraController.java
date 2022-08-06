package luxo.renderer;

import luxo.core.Input;
import luxo.core.Element;
import luxo.core.Timestep;
import luxo.events.ApplicationEvent.WindowResizedEvent;
import luxo.events.Event;
import luxo.events.MouseEvent.MouseScrolledEvent;
import static luxo.core.KeyCode.*;
import org.joml.Vector3f;

public class OrthoCameraController implements Element {

    private final OrthoCamera camera;
    private float aspectRatio, zoomLevel;
    private final boolean rotation;
    
    private float camRotation;
    private float camTranslationSpeed, camRotationSpeed;
    private final Vector3f camPosition;
    
    public OrthoCameraController(float aspectRatio) { this(aspectRatio, false); }
    
    public OrthoCameraController(float aspectRatio, boolean rotation) {
        this.aspectRatio = aspectRatio;
        this.zoomLevel = 1;
        this.rotation = rotation;
        this.camera = new OrthoCamera(-aspectRatio * zoomLevel, +aspectRatio * zoomLevel, -zoomLevel, zoomLevel);
        
        this.camRotation = 0;
        this.camTranslationSpeed = this.camRotationSpeed = 1;
        this.camPosition = new Vector3f();
    }
    
    @Override
    public void onUpdate(Timestep ts) {
        if(Input.isKeyPressed(LX_KEY_A))
            camPosition.x -= camTranslationSpeed * ts.getSeconds();
        else if(Input.isKeyPressed(LX_KEY_D))
            camPosition.x += camTranslationSpeed * ts.getSeconds();
        
        if(Input.isKeyPressed(LX_KEY_S))
            camPosition.y -= camTranslationSpeed * ts.getSeconds();
        else if(Input.isKeyPressed(LX_KEY_W))
            camPosition.y += camTranslationSpeed * ts.getSeconds();
        
        if(rotation) {
            if(Input.isKeyPressed(LX_KEY_Q))
                camRotation += camRotationSpeed * ts.getSeconds();
            else if(Input.isKeyPressed(LX_KEY_E))
                camRotation -= camRotationSpeed * ts.getSeconds();
            camera.setRotation(camRotation);
        }
        camera.setPosition(camPosition);
        camTranslationSpeed = zoomLevel;
    }
    
    @Override
    public void onEvent(Event event) {
        if(event instanceof MouseScrolledEvent evt) 
            onMouseScrolled(evt);
        if(event instanceof WindowResizedEvent evt) 
            onWindowResized(evt);
    }
    
    public void setZoomLevel(float zoomLevel) {
        this.zoomLevel = zoomLevel;
        this.camera.setProjection(-aspectRatio * zoomLevel, +aspectRatio * zoomLevel, -zoomLevel, zoomLevel);
    }
    
    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        this.camera.setProjection(-aspectRatio * zoomLevel, +aspectRatio * zoomLevel, -zoomLevel, zoomLevel);
    }
    
    private boolean onMouseScrolled(MouseScrolledEvent evt) {
        this.zoomLevel -= evt.getYOffset() * 0.25;
        if(this.zoomLevel < 0.25) this.zoomLevel = 0.25f;
        this.camera.setProjection(-aspectRatio * zoomLevel, +aspectRatio * zoomLevel, -zoomLevel, zoomLevel);
        return false;
    }
    
    private boolean onWindowResized(WindowResizedEvent evt) {
        setAspectRatio((float) evt.getWidth() / (float) evt.getHeight());
        return false;
    }

    @Override
    public void onImGuiRender() {}
    
    public OrthoCamera getCamera() { return this.camera; }
}
