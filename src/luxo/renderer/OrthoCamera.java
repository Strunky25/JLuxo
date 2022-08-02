package luxo.renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class OrthoCamera {
    
    private final Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Matrix4f viewProjectionMatrix;
    private Vector3f position;
    private float rotation;
    
    public OrthoCamera(float left, float right, float bottom, float top) {
        projectionMatrix = new Matrix4f().ortho(left, right, bottom, top, -1, 1);
        viewMatrix = new Matrix4f();
        viewProjectionMatrix = projectionMatrix.mul(viewMatrix);
        position = new Vector3f();
    }
    
    public void setPosition(Vector3f position) { 
       this.position = position;
       recalculateViewMatrix();
    }
    public void setRotation(float rotation) { 
        this.rotation = rotation; 
        recalculateViewMatrix();
    }
    
    public Matrix4f getProjectionMatrix() { return projectionMatrix; }
    public Matrix4f getViewMatrix() { return viewMatrix; }
    public Matrix4f getViewProjectionMatrix() { return viewProjectionMatrix; }
    public Vector3f getPosition() { return position; }
    public float getRotation() { return rotation; }
    
    private void recalculateViewMatrix() {
        Matrix4f transform = new Matrix4f().translate(position).mul(
                new Matrix4f().rotate(rotation, new Vector3f(0, 0, 1)));
        viewMatrix = new Matrix4f(transform).invert();
        viewProjectionMatrix = new Matrix4f(projectionMatrix).mul(viewMatrix);
    }
}