package platform.opengl;

import java.nio.*;
import luxo.core.Log;
import luxo.renderer.Texture.Texture2D;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.stb.STBImage.*;

public class OpenGLTexture2D extends Texture2D {
    
    private String path;
    private final int rendererID, width, height, internalFormat, dataFormat;
    
    public OpenGLTexture2D(int width, int height) {
        this.width = width;
        this.height = height;
        internalFormat = GL_RGBA8;
        dataFormat = GL_RGBA;
        
        rendererID = glCreateTextures(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, rendererID);
        glTextureStorage2D(rendererID, 1, internalFormat, width, height);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    }

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
        
        int channels = components.get();
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
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        
        glTextureSubImage2D(rendererID, 0, 0, 0, width, height, dataFormat, GL_UNSIGNED_BYTE, data);
        stbi_image_free(data);
    }
    
    @Override
    public void setData(int[] data, int size) {
        int bpp = dataFormat == GL_RGBA ? 4 : 3;
        Log.coreAssert(size == width * height * bpp, "Data must be entire texture!");
        glTextureSubImage2D(rendererID, 0, 0, 0, width, height, dataFormat, GL_UNSIGNED_BYTE, data);
    }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return width; }

    @Override
    public void bind() { bind(0); }
    
    @Override
    public void bind(int slot) { glBindTextureUnit(slot, rendererID); }

    @Override
    public void dispose() { glDeleteTextures(rendererID); }
}
