package org.graphics.utils;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;


public class ShaderProgram {
    private final int programId;

    public ShaderProgram(String vertexPath, String fragmentPath) {
        int vertexShader = compileShader(loadShader(vertexPath), GL_VERTEX_SHADER);
        int fragmentShader = compileShader(loadShader(fragmentPath), GL_FRAGMENT_SHADER);

        programId = glCreateProgram();
        glAttachShader(programId, vertexShader);
        glAttachShader(programId, fragmentShader);
        glLinkProgram(programId);

        if(glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE){
            throw new IllegalStateException("Грешка при линкването на програмата: " + glGetProgramInfoLog(programId));
        }

        glDetachShader(programId, vertexShader);
        glDetachShader(programId, fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void use() {
        glUseProgram(programId);
    }

    public void setUniform(String name, Matrix4f matrix) {
        int location = glGetUniformLocation(programId, name);

        if(location == -1) {
            throw new IllegalStateException("Грешка при намирането на униформа: " + name);
        }

        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        matrix.get(matrixBuffer);
        glUniformMatrix4fv(location, false, matrixBuffer);
    }

    public void setUniform(String name, float value) {
        int location = glGetUniformLocation(programId, name);

        if (location != -1) {
            glUniform1f(location, value);
        }
    }


    private String loadShader(String path) {
        try {
            return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Грешка при зареждане на шейдъра: " + path, e);
        }
    }

    private int compileShader(String source, int type) {
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, source);
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE){
            throw new IllegalStateException("Грешка при компилирането на шейдъра: " + glGetShaderInfoLog(shaderID));
        }

        return shaderID;
    }
}
