package platform.opengl;

import java.nio.*;
import luxo.Log;
import luxo.renderer.Texture.Texture2D;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.stb.STBImage.*;

public class OpenGLTexture2D extends Texture2D {
    
    private final String path;
    private final int rendererID, width, height;

    public OpenGLTexture2D(String path) {
        this.path = path;
        stbi_set_flip_vertically_on_load(true);
        IntBuffer tempWidth = BufferUtils.createIntBuffer(1);
        IntBuffer tempHeight = BufferUtils.createIntBuffer(1);
        IntBuffer components = BufferUtils.createIntBuffer(1);
        ByteBuffer data = stbi_load(path, tempWidth, tempHeight, components, 0);
        Log.coreAssert(data != null, "Failed to load image!");
        width = tempWidth.get();
        height = tempHeight.get();
        
        int internalFormat, dataFormat, channels = components.get();
        if(channels == 4){
            internalFormat = GL_RGBA8;
            dataFormat = GL_RGBA;
        } else {
            internalFormat = GL_RGB8;
            dataFormat = GL_RGB;
        }
        Log.coreAssert((internalFormat & dataFormat) != 0, "Format not supported!");
        
        rendererID = glCreateTextures(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, rendererID);
        glTextureStorage2D(rendererID, 1, internalFormat, width, height);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        
        glTextureSubImage2D(rendererID, 0, 0, 0, width, height, dataFormat, GL_UNSIGNED_BYTE, data);
        stbi_image_free(data);
    }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return width; }

    @Override
    public void bind(int slot) { glBindTextureUnit(slot, rendererID); }

    @Override
    public void dispose() { glDeleteTextures(rendererID); }
}
