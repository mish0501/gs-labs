package org.graphics.renderes;

import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.graphics.utils.ShaderProgram;
import org.lwjgl.BufferUtils;


public class Renderer {
    @SuppressWarnings("FieldCanBeLocal")
    private int vaoID, vboID;
    private ShaderProgram shaderProgram;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        float[] vertices = {
                // X, Y, Z      R, G, B
                // Left Triangle
                0.0f, 0.8f, 0.0f, 1.0f, 1.0f, 1.0f,
                0.0f, -0.4f, 0.0f, 0.4f, 0.4f, 0.4f,
                -0.6f, -0.4f, 0.0f, 0.4f, 0.4f, 0.4f,

                // Right Triangle
                0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f,
                0.0f, -0.4f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.6f, -0.4f, 0.0f, 1.0f, 0.0f, 0.0f,

                // Yellow Triangle
                0.0f, 0.75f, 0.0f, 1.0f, 1.0f, 0.0f,
                0.0f, 0.55f, 0.0f, 1.0f, 1.0f, 0.0f,
                0.25f, 0.65f, 0.0f, 1.0f, 1.0f, 0.0f,

                // Blue Triangles
                -0.8f, -0.4f, 0.0f, 0.0f, 0.7f, 1.0f,
                -0.4f, -0.8f, 0.0f, 0.0f, 0.7f, 1.0f,
                0.0f, -0.4f, 0.0f, 0.0f, 0.7f, 1.0f,
                0.4f, -0.8f, 0.0f, 0.0f, 0.7f, 1.0f,
                0.8f, -0.4f, 0.0f, 0.0f, 0.7f, 1.0f,
        };

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        // Атрибут 0 - Позиция (vec3)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Атрибут 1 - Цвят (vec3)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        shaderProgram.use();

        glLineWidth(2.0f);
        glBindVertexArray(vaoID);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glDrawArrays(GL_TRIANGLES, 3, 3);
        glDrawArrays(GL_TRIANGLES, 6, 3);
        glDrawArrays(GL_TRIANGLE_STRIP, 9, 5);
        glBindVertexArray(0);
    }
}
