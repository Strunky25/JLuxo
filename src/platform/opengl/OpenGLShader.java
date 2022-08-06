package platform.opengl;

import java.io.IOException;
import luxo.core.Log;
import luxo.renderer.Shader;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joml.*;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL46C.*;

public class OpenGLShader extends Shader {
    
    private int rendererID;
    private String name;
    
    public OpenGLShader(String filepath) {
        List<String> source = readFile(filepath);
        Map<Integer, String> shaderSources = preProcess(source);
        compile(shaderSources);
        
        int lastSlash = filepath.lastIndexOf('/');
        lastSlash = lastSlash == -1 ? 0 : lastSlash + 1;
        int lastDot = filepath.lastIndexOf(".");
        lastDot = lastDot == -1 ? filepath.length() : lastDot;
        this.name = filepath.substring(lastSlash, lastDot);
    }

    public OpenGLShader(String name, String vertexSource, String fragmentSource) {
        this.name = name;
        HashMap<Integer, String> shaderSources = new HashMap<>();
        shaderSources.put(GL_VERTEX_SHADER, vertexSource);
        shaderSources.put(GL_FRAGMENT_SHADER, fragmentSource);
        compile(shaderSources);
    }
    
    @Override
    public void dispose() { glDeleteProgram(rendererID); }
    
    @Override
    public void bind() { glUseProgram(rendererID); }
    
    @Override
    public void unbind() { glUseProgram(0); }
    
    @Override
    public void setMat4(String name, Matrix4f uniform) {
        uploadUniformMat4(name, uniform);
    }

    @Override
    public void setVec4(String name, Vector4f uniform) {
        uploadUniformVec4(name, uniform);
    }

    @Override
    public void setVec3(String name, Vector3f uniform) {
        uploadUniformVec3(name, uniform);
    }

    @Override
    public void setInt(String name, int uniform) {
        uploadUniformInt(name, uniform);
    }

    @Override
    public String getName() { return name; }
       
    private void uploadUniformMat4(final String name, final Matrix4f uniform) {
        int location = glGetUniformLocation(rendererID, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = uniform.get(stack.mallocFloat(16));
            glUniformMatrix4fv(location, false, fb);
        }
    }
    
    private void uploadUniformVec4(final String name, final Vector4f uniform) {
        int location = glGetUniformLocation(rendererID, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = uniform.get(stack.mallocFloat(4));
            glUniform4fv(location, fb);
        }
    }
    
    private void uploadUniformVec3(final String name, final Vector3f uniform) {
        int location = glGetUniformLocation(rendererID, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = uniform.get(stack.mallocFloat(3));
            glUniform3fv(location, fb);
        }
    }
    
    private void uploadUniformInt(final String name, final int uniform) {
        int location = glGetUniformLocation(rendererID, name);
        glUniform1i(location, uniform);
    }
    
    private List<String> readFile(String filepath) {
        List<String> result = null;
        try {
             result = Files.readAllLines(Paths.get(filepath));
        } catch (IOException ex) {
            Log.coreError("Could not open file '%s'", filepath);
        }
        return result;
    }
    
    private Map<Integer, String> preProcess(final List<String> source) {
        HashMap<Integer, String> shaderSources = new HashMap<>();
        final String typeToken = "#type";
        String src = new String();
        int type = -1;
        boolean isFirst = true;
        for(String line: source) {
            if(line.contains(typeToken)) {
                if(!isFirst) shaderSources.put(type, src); 
                int pos = line.indexOf(typeToken);
                type = shaderTypeFromString(line.substring(pos + typeToken.length() + 1));
                isFirst = false;
                src = "";
            } else {
                src += line + "\n";
            }
        }
        shaderSources.put(type, src);
        return shaderSources;
    }
    
    private void compile(Map<Integer, String> shaderSources) {
        int program = glCreateProgram(), i = 0;
        int[] shaderID = new int[shaderSources.size()];
        for (Map.Entry<Integer, String> entry : shaderSources.entrySet()) {
            int shader = glCreateShader(entry.getKey());
            shaderID[i++] = shader;
            glShaderSource(shader, entry.getValue());
            glCompileShader(shader);
            if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
                String infoLog = glGetShaderInfoLog(shader);
                glDeleteShader(shader);
                Log.coreError("Shader compilation failure! \n  %s", infoLog);
                break;
            }
            glAttachShader(program, shader);
        }

        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            String infoLog = glGetProgramInfoLog(program);
            Log.coreError("Shader Linking failure! \n  %s", infoLog);
              
            glDeleteProgram(program);
            for(int shader: shaderID) glDeleteShader(shader);
            return;
        }
        for(int shader: shaderID) glDetachShader(program, shader);
        this.rendererID = program;
    }
    
    private static int shaderTypeFromString(final String type) {
        if("vertex".equals(type))
            return GL_VERTEX_SHADER;
        if("fragment".equals(type) || "pixel".equals(type))
            return GL_FRAGMENT_SHADER;
        Log.coreAssert(false, "Unknown shader type!");
        return 0;
    }
}

