package luxo.renderer;

import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;
import static glm_.Java.glm;

public class OrthoCamera {
    
    private Mat4 projectionMatrix;
    private Mat4 viewMatrix;
    private Mat4 viewProjectionMatrix;
    private Vec3 position;
    private float rotation;
    
    public OrthoCamera(float left, float right, float bottom, float top) {
        projectionMatrix = glm.ortho(left, right, bottom, top, -1, 1);
        viewMatrix = new Mat4(1);
        viewProjectionMatrix = projectionMatrix.times(viewMatrix);
        position = new Vec3();
    }
    
    public void setPosition(Vec3 position) { 
       this.position = position;
       recalculateViewMatrix();
    }
    public void setRotation(float rotation) { 
        this.rotation = rotation; 
        recalculateViewMatrix();
    }
    
    public Mat4 getProjectionMatrix() { return projectionMatrix; }
    public Mat4 getViewMatrix() { return viewMatrix; }
    public Mat4 getViewProjectionMatrix() { return viewProjectionMatrix; }
    public Vec3 getPosition() { return position; }
    public float getRotation() { return rotation; }
    
    private void recalculateViewMatrix() {
        Mat4 transform = glm.translate(new Mat4(1), position).times(
                glm.rotate(new Mat4(1), rotation, new Vec3(0, 0, 1)));
        viewMatrix = glm.inverse(transform);
        viewProjectionMatrix = projectionMatrix.times(viewMatrix);
    }
}