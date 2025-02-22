package org.graphics.renderes;

import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.graphics.utils.ShaderProgram;
import org.lwjgl.BufferUtils;


public class Renderer {
    private int vaoID, vboID;
    private ShaderProgram shaderProgram;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        float[] vertices = {
                0.0f, 0.0f, 0.0f
        };

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        shaderProgram.use();

        glPointSize(10.0f);
        glBindVertexArray(vaoID);
        glDrawArrays(GL_POINTS, 0, 1);
        glBindVertexArray(0);
    }
}
