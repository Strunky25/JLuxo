package platform.opengl;

import luxo.renderer.RendererAPI;
import luxo.renderer.VertexArray;
import org.joml.Vector4f;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGLRendererAPI extends RendererAPI {

    @Override
    public void init() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void setClearColor(Vector4f color) {
        glClearColor(color.x, color.y, color.z, color.w);
    }

    @Override
    public void drawIndexed(VertexArray vertexArray) {
        glDrawElements(GL_TRIANGLES, vertexArray.getIndexBuffer().getCount(), GL_UNSIGNED_INT, NULL);
    }

    @Override
    public void setViewport(int x, int y, int width, int height) {
        glViewport(x, y, width, height);
    }

}
