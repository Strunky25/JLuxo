package platform.opengl;

import glm_.vec4.Vec4;
import luxo.renderer.RendererAPI;
import luxo.renderer.VertexArray;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGLRendererAPI extends RendererAPI {

    @Override
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void setClearColor(Vec4 color) {
        glClearColor(color.r(), color.g(), color.b(), color.a());
    }

    @Override
    public void drawIndexed(VertexArray vertexArray) {
        glDrawElements(GL_TRIANGLES, vertexArray.getIndexBuffer().getCount(), GL_UNSIGNED_INT, NULL);
    }

}
